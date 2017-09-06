package com.zhiren.dc.diaoygl;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.contrib.palette.SortMode;

public class Shouhcrbb_fk_Report extends BasePage implements PageValidateListener{
//			�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
		_CurrentPage = _value;
	}

	private int _AllPages = -1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
//			������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//			ҳ��仯��¼
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
//			
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
	
	public IPropertySelectionModel Diancjb;
	public void setDiancjb() {
		String sql = "select id,jib from diancxxb";
		Diancjb =new IDropDownModel(sql);
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
		return getShouhcrbb_fk_Report();
	}

	private String getShouhcrbb_fk_Report(){
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String beforeDate = DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiq()),-1,DateUtil.AddType_intDay));
		
		String diancxxb_id = "";
		if(getTreeid()!=null) {
			diancxxb_id = getTreeid();
		}
		JDBCcon con = new JDBCcon();
		// �����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "";
		int iFixedRows = 0;// �̶��к�
		String titledate = "";// ��������
		titledate = "�ֿ��պĴ��ձ�";
		StringBuffer sb = new StringBuffer();
		sb.append("select meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id,quanyjh,shangrkc,dangrlm,laimlj,fady,gongry,qity,cuns,panyk,tiaozl,haomlj,benrjc,beiz from ( ");
		sb.append("select (select max(id) + 1 from fenkshcrbb) as id,"+diancxxb_id+" as diancxxb_id,'�ϼ�' as meikxxb_id, '' as jihkjb_id,'' as pinzb_id,'' as yunsfsb_id,0 as quanyjh, " +
				"(select kuc from shouhcrbb where riq = to_date('"+beforeDate+"', 'yyyy-mm-dd') and diancxxb_id = "+diancxxb_id+") as shangrkc, " +
				"dangrgm as dangrlm, " +
				"(select decode(sum(laimlj),null,0,sum(laimlj)) from fenkshcrbb where diancxxb_id="+diancxxb_id+" and riq= to_date('"+beforeDate+"', 'yyyy-mm-dd'))+dangrgm as laimlj, " +
				"fady,gongry,qity,cuns,panyk,shuifctz as tiaozl, " +
				"(select decode(sum(haomlj),null,0,sum(haomlj)) from fenkshcrbb where diancxxb_id="+diancxxb_id+" and riq= to_date('"+beforeDate+"', 'yyyy-mm-dd'))+fady+gongry+qity as haomlj, " +
				"kuc as benrjc,'' as beiz " +
				"from shouhcrbb where riq = "+CurDate+" and diancxxb_id = "+diancxxb_id+" \n");
		sb.append("union \n");
		sb.append("select f.id,diancxxb_id,m.mingc as meikxxb_id,j.mingc as jihkjb_id,p.mingc as pinzb_id,y.mingc as yunsfsb_id,quanyjh,shangrkc,dangrlm,laimlj,fady,gongry,qity,cuns,panyk,tiaozl,haomlj,benrjc,f.beiz " +
				"from fenkshcrbb f,meikxxb m,jihkjb j,pinzb p,yunsfsb y " +
				"where f.meikxxb_id=m.id and f.jihkjb_id=j.id and f.pinzb_id=p.id and f.yunsfsb_id=y.id " +
				"and f.riq="+CurDate+" and f.diancxxb_id="+diancxxb_id);
		sb.append(" order by id desc");
		sb.append(") fenkshc");
		ResultSet rs = con.getResultSet(sb.toString());
		ArrHeader = new String[2][17];
		ArrHeader[0]=new String[]{"��λ����","�ƻ��ھ�","Ʒ��","���䷽ʽ","ȫ�¼ƻ�","���տ��", "��ú","��ú","��ú","��ú","��ú","��ú","��ú","��ú","��ú","���ս��","��ע"};
		ArrHeader[1]=new String[]{"��λ����","�ƻ��ھ�","Ʒ��","���䷽ʽ","ȫ�¼ƻ�","���տ��", "����","�ۼ�","������","������","������","����","��ӯ��","������","�ۼ�","���ս��","��ע"};
		ArrWidth = new int[] {120,80,60,60,80,80,60,60,60,60,60,60,60,60,60,80,80};
		iFixedRows = 1;
		// ����
		rt.setBody(new Table(rs, 2, 0, iFixedRows));
//				���ñ�ͷ
		rt.setTitle(titledate + titlename, ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 5, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(6, 4, "�������ڣ�" + DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(11, 3, "��λ���֡�%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
//				rt.body.setColFormat(ArrFormat);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 5, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 4, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		// ����ҳ��
		 if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		con.Close();
		// System.out.println(rt.getAllPagesHtml());
		return rt.getAllPagesHtml();
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public void getToolBars() {
		Visit visit = (Visit)this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("����:"));
		DateField dfb = new DateField();
		dfb.setValue(getRiq());
		dfb.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
	
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
		
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		
		setToolbar(tb1);
	}
	
	
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
	
//			 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setDiancmcModels();
			setDiancjb();		
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

