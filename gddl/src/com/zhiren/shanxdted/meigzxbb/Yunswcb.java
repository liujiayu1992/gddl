package com.zhiren.shanxdted.meigzxbb;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Yunswcb extends BasePage implements PageValidateListener {
	
	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
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

	// ***************������Ϣ��******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	public String getPrintTable() {
		setMsg(null);

		return getZhiltz();
	}

	private String getZhiltz() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer.append(
				"select dq.mingc, m.mingc, y.mingc,\n" +
				"		decode(grouping(y.mingc),1,'',wm_concat(decode(f.diancxxb_id,301,'һ',302,'��',303,'��',304,'��'))) rucdd,\n" + 
				"       '' hetrz,\n" + 
				"       decode(sum(jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(jingz),2)) qnet_ar,\n" + 
				"       '' rucrzc,\n" + 
				"       '' jihml,\n" + 
				"       sum(f.biaoz) biaoz,sum(f.jingz) jingz,\n" + 
				"       decode(sum(biaoz),0,0,round((sum(biaoz)-sum(jingz))/sum(biaoz)*100,2)) bangc,\n" + 
				"       '' jihwc,\n" + 
				"       '' jihml2,\n" + 
				"       sum(f.ches) ches\n" + 
				"  from fahb f, meikxxb m, yunsdwb y, (select fahb_id,yunsdwb_id from chepb group by fahb_id,yunsdwb_id) c,zhilb z, meikdqb dq\n" + 
				" where f.meikxxb_id=m.id\n" + 
				"   and f.id=c.fahb_id\n" + 
				"   and c.yunsdwb_id=y.id\n" + 
				"   and f.zhilb_id=z.id\n" + 
				"   and m.meikdq_id=dq.id\n" + 
				"   and f.daohrq = to_date('"+getRiqi()+"','yyyy-mm-dd')\n" + 
				" group by rollup (dq.mingc,m.mingc,y.mingc)\n" + 
				" order by dq.mingc, m.mingc, y.mingc");
		
		ResultSet rs = con.getResultSet(buffer,
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		String[][] ArrHeader = new String[2][14];

		ArrHeader[0] = new String[] {
				"����","����","�����","�볧�ص�",
				"�ȡ�ֵ����/ǧ�ˣ�","�ȡ�ֵ����/ǧ�ˣ�","�ȡ�ֵ����/ǧ�ˣ�",
				"ú�������֣�","ú�������֣�","ú�������֣�","ú�������֣�","ú�������֣�","ú�������֣�","ú�������֣�",};
		ArrHeader[1] = new String[] {
				"����","����","�����","�볧�ص�",
				"��ͬ<br>��ֵ","�볧<br>��ֵ ","�볧��<br>��ͬ��",
				"��<br>�ƻ�<br>ú��","��<br>ʵ��<br>ú��","�볧<br>ʵ��<br>ú��","����<br>��%��","��ɼƻ�<br>����%��","��<br>�ƻ�<br>ú��","��<br>�볧<br>����",};
		int[] ArrWidth = new int[14];

		ArrWidth = new int[] {80,80,80,80,80,80,80,80,80,80,80,80,80,80};

		rt.setTitle("ֱ��ú��Ӧ��������������ɱ�", ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(rs, 2, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for (int i = 1; i <= 14; i++) {
			rt.body.mergeFixedCols();
		}
		
		
		for (int i = 1; i <= 14; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "���:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "�Ʊ�:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(21);

		return rt.getAllPagesHtml();

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString14("");
			
			this.setRiqi(null);
			
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
		}		
		if(cycle.getRequestContext().getParameter("ds") !=null){
			if(!cycle.getRequestContext().getParameter("ds").equals(this.getDataSource())){//��Ҫ��յı���
				
				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);
			}
		}
		getSelectData();
	}
	
	private String getDataSource(){
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString14();
	}

	// ������
	boolean riqichange = false;
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {
		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			Refurbish();
			_RefurbishChick = false;
		}
	}

	private void Refurbish() {
		// Ϊ "ˢ��" ��ť��Ӵ������
		getZhiltz();
	}
    
	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		tb1.addField(df);
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);

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