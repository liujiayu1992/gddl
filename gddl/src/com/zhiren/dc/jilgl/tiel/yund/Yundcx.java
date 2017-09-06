package com.zhiren.dc.jilgl.tiel.yund;

import java.sql.ResultSet;
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
import com.zhiren.common.Locale;
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
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * 
 * @author Rock
 *
 */
/*
 * 2009-02-16
 * ����
 * ������������š��޸��еĶ��뷽ʽ,���ӷֳ���Ĺ���
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-20 17��07
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
public class Yundcx extends BasePage {
//	�����û���ʾ
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
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString1(riq);
	}
	

	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
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
//	��ȡ������
	public String getRptTitle() {
//		Visit visit = (Visit) this.getPage().getVisit();
		String sb;
		sb=Locale.RptTitle_Jilcx_Huoy;
		return sb;
	}
//	¼��ʱ��
	public IDropDownBean getGuohsjValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getGuohsjModel().getOptionCount()>0) {
				setGuohsjValue((IDropDownBean)getGuohsjModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setGuohsjValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getGuohsjModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setGuohsjModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setGuohsjModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	public void setGuohsjModels() {
		String diancid = "" ;
		StringBuffer sb = new StringBuffer();
		if(getTreeid_dc()!=null && !"".equals(getTreeid_dc()) && !"0".equals(getTreeid_dc())) {
			if(hasDianc(getTreeid_dc())){
				diancid = " and d.fgsid = " + getTreeid_dc();
			} else {
				diancid = " and f.diancxxb_id ="+ getTreeid_dc();
			}
			
		}
		sb.append("select rownum,lursj from (select distinct to_char(lursj,'yyyy-mm-dd hh24:mi:ss') as lursj \n");
		sb.append("from chepb c,fahb f,vwdianc d where f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" and c.fahb_id=f.id and d.id=f.diancxxb_id\n");
		sb.append("and lursj >=").append(DateUtil.FormatOracleDate(getBeginRiq()))
		.append(" and lursj < ").append(DateUtil.FormatOracleDate(getEndRiq())).append(" +1 \n");
		sb.append(diancid);
		sb.append("order by lursj desc) where rownum < ").append("100");
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"��ѡ��"));
		setGuohsjModel(new IDropDownModel(list,sb));
	}
	
//	��ȡ��ص�SQL
	public StringBuffer getBaseSql() {
		Visit v = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb.append("select rownum,c.cheph,c.biaoz,g.mingc as gongysb_id,m.mingc as meikxxb_id,\n");
		sb.append("p.mingc as pinzb_id,j.mingc as jihkjb_id,to_char(f.fahrq,'yyyy-mm-dd') as fahrq,\n");
		sb.append("to_char(f.daohrq,'yyyy-mm-dd') as daohrq,f.chec,\n");
		sb.append("cb.mingc as chebb_id,\n");
		sb.append("fz.mingc as fazb_id,dz.mingc as daozb_id,ydz.mingc as yuandzb_id, \n");
		sb.append("vy.mingc as yuanshdwb_id,c.yuanmkdw,c.daozch,c.beiz \n");
		sb.append("from chepb c,fahb f,gongysb g,meikxxb m,chezxxb fz,chezxxb dz,chezxxb ydz, \n");
		sb.append("pinzb p,jihkjb j,chebb cb,diancxxb d,vwyuanshdw vy \n");
		sb.append("where c.fahb_id = f.id and f.gongysb_id = g.id and f.meikxxb_id = m.id \n");
		if(getGuohsjValue().getId()==-1){
			sb.append("and lursj=to_date('1970-01-01 00:00:00','yyyy-mm-dd hh24:mi:ss') ");
		}else{
			sb.append("and lursj = ").append(DateUtil.FormatOracleDateTime(getGuohsjValue().getValue())).append(" \n");
		}
		sb.append("and f.faz_id = fz.id and f.daoz_id = dz.id and f.yuandz_id = ydz.id \n");
		sb.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id and c.chebb_id = cb.id \n");
		sb.append("and f.yuanshdwb_id = vy.id and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" and f.diancxxb_id = d.id\n");
		sb.append(Jilcz.filterDcid(v, "f"));
		return sb;
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
//		if(visit.isFencb()) {
//			tb1.addText(new ToolbarText("����:"));
//			ComboBox changbcb = new ComboBox();
//			changbcb.setTransform("ChangbSelect");
//			changbcb.setWidth(130);
//			changbcb.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
//			tb1.addField(changbcb);
//			tb1.addText(new ToolbarText("-"));
//		}
//		�糧Tree
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
		tb1.addText(new ToolbarText("¼������:"));
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", null);// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("BeginRq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", null);// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"ˢ��","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(rbtn);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("ʱ��:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("LursjSelect");
		shij.setWidth(150);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('LursjSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton sbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('SearchButton').click();}");
		sbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(sbtn);
		tb1.addFill();
		tb1.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		setToolbar(tb1);
	}
	
	public String getPrintTable(){
		JDBCcon con = new JDBCcon();
		
		ResultSet rs = con.getResultSet(getBaseSql(),
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		if(rs == null) {
			setMsg("���ݻ�ȡʧ�ܣ�������������״����������� YDCX-001");
			return "";
		}
		Report rt = new Report();

		String[][] ArrHeader = new String[][] {{ "���", "����", "����", "��Ӧ��", "ú������", "Ʒ��",
				"�ƻ��ھ�", "��������", "��������", "����","����", "��վ", "��վ", "ԭ��վ", "ԭ�ջ���λ", "ԭú��λ", "��װ����", "��ע"} };

		int[] ArrWidth = new int[] { 35, 60, 45, 90, 90, 50, 65, 65, 65, 50, 50, 65, 65, 65, 90, 90, 80, 90 };
//		int[] ArrWidth = new int[] { 35, 60, 45, 90, 90, 50, 65, 65, 65, 50, 50, 65, 65, 65, 90, 90, 90, 90 };
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString4());//ȡ�ñ���ֽ������
		rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
		rt.setTitle(getRptTitle(), ArrWidth);
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.setDefaultTitle(1, 4, "�˵�ʱ�䣺" + getGuohsjValue().getValue(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(14, 4, "��λ���֡���", Table.ALIGN_RIGHT);
//		String[] arrFormat = new String[] { "", "", "", "", "", "", "", "",
//				"", "", "", "", "", "", "", "", "", "" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setColAlign(1, Table.ALIGN_RIGHT);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_RIGHT);
		for(int i=4;i<=18;i++)
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(arrFormat);
		rt.body.setPageRows(rt.PAPER_COLROWS);
        //	���ӳ��ȵ�����
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();

		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 4, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 5, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 5, "�Ʊ�", Table.ALIGN_LEFT);
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
//	������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
//		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
	
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
    	String sql="select id,mingc from vwgongysmk where diancxxb_id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        setGongysDropDownModel(new IDropDownModel(sql,"ȫ��")) ;
        return ;
    }
//	������ʹ�õķ���
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
		return getTree_dc().getScript();
	}
	
//	-------------------------�糧Tree END-------------------------------------------------------------
	
	
	
//	ҳ���ʼ��
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setChangbValue(null);
			setChangbModel(null);
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			setGuohsjValue(null);
			setGuohsjModel(null);
			setTbmsg(null);
			setTreeid_dc(null);
			
			visit.setString4(null);
			
			String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
			if (pagewith != null) {

				visit.setString4(pagewith);
			}
		//	visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
			
			getSelectData();
			
			
		}
	}
	
//	��ť�ļ����¼�
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
//	��ť�ļ����¼�
	private boolean _SearchChick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchChick = true;
	}

//	����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			setGuohsjValue(null);
			setGuohsjModel(null);
			setGuohsjModels();
			getSelectData();
		}
		if (_SearchChick) {
			_SearchChick = false;
			getSelectData();
		}
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
	
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
}
