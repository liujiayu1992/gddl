package com.zhiren.shanxdted.rultz;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/**
 * ����:tzf
 * ʱ��:2009-09-23
 * ����:�����¯����̨��
 */
/**
 * �޸�����:2010-01-25
 * �޸���Ա��liht
 * ����:���ӡ��̶�̼��һ��
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-25
 * ����: ��λ������ǰ��
 *       ɾ�����̼�У�
 *       ��ֵ�����ҷ�ǰ
 *      ���հ��顢���ڲ�ѯʱ����б�ͷ����������ʾ�в�ƥ��
 */
public class Rultz extends BasePage implements PageValidateListener {
	
	private static String STYLE_RQ="�����ڲ�ѯ";
	private static String STYLE_BZ="�������ѯ";
	
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
	
//	��ʽ������
	public IDropDownBean getGesValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean4()==null) {
			if(getGesModel().getOptionCount()>0) {
				setGesValue((IDropDownBean)getGesModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean4();
	}
	
	public void setGesValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean4(value);
	}
	
	public IPropertySelectionModel getGesModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel4()==null) {
			setGesModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel4();
	}
	public void setGesModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void setGesModels() {	
		List list=new ArrayList();
		list.add(new IDropDownBean("1", STYLE_RQ));	
		list.add(new IDropDownBean("2", STYLE_BZ));
		setGesModel(new IDropDownModel(list));
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

	    return getRultz();
		
	}


	private String getRultz() {
		JDBCcon con = new JDBCcon();
		Visit v=(Visit)this.getPage().getVisit();
		// �����ͷ����
		String titlename = "";
		String titledate = "";// ��������
		titledate = getBRiq() + "��" + getERiq() + " ��¯ú����̨��";
		StringBuffer sb = new StringBuffer();
		//
		if (STYLE_RQ.equals(this.getGesValue().getValue())) {
			sb.append(" SELECT  rlrq,meil,mt, mad,qbad,qnet_ar,qnet_ar1,aad, ad,aar,vad,vdaf,stad,std,star FROM \n");
		}else{
			sb.append(" SELECT  rlrq,banz,jiz,fenxrq,meil,mt, mad,qbad,qnet_ar,qnet_ar1,aad, ad,aar,vad,vdaf,stad,std,star FROM \n");
		}
		
		sb.append("(select decode(z.rulrq,null,'�ϼ�',to_char(z.rulrq,'yyyy-mm-dd')) rlrq,b.mingc banz, j.mingc jiz,to_char(z.fenxrq,'yyyy-mm-dd') fenxrq,sum(h.fadhy+h.gongrhy+h.feiscy+h.qity) meil, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.mt,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mt)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),1)  ) mt, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.mad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) mad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.aad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.aad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) aad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.ad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.ad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) ad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.aar,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.aar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) aar, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.vad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.vad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) vad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.vdaf,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.vdaf)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) vdaf, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.qbad,0)),2)*1000,").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qbad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000 ) qbad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.qnet_ar,0)),2)*1000,").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qnet_ar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000 ) qnet_ar, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(round_new(avg(nvl(z.qnet_ar,0)),2)*1000/4.1816,0),").append("round(round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.qnet_ar)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)*1000/4.1816,0) ) qnet_ar1, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.stad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.stad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) stad, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.std,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.std)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) std, \n")
		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new( ( 100-round_new(avg(nvl(z.mt,0)),1) )*round_new( avg(nvl(z.stad,0)),2) /(100-round_new(avg( nvl(z.mad,0) ),2) ) ,2),").append("ROUND_NEW((100 - round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mt)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),1))\n")
		.append("*round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.stad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) / (100 - round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.mad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2)), 2) ) star\n")
//		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.hdaf,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.hdaf)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) hdaf, \n")
//		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.had,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.had)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) had, \n")
//		.append(" decode(decode(grouping(rulmzlb_id),1,max(rulmzlb_id),null),null,round_new(avg(nvl(z.fcad,0)),2),").append("round(nvl(decode(sum(h.fadhy+h.gongrhy+h.feiscy+h.qity),0,0,sum((h.fadhy+h.gongrhy+h.feiscy+h.qity)*z.fcad)/sum(h.fadhy+h.gongrhy+h.feiscy+h.qity)),0),2) ) fcad \n")
		.append("from rulmzlb z,meihyb h,diancxxb d , rulbzb b, jizfzb j \n")
		.append("where h.rulmzlb_id(+)= z.id and z.diancxxb_id = d.id  \n")
		.append(" and z.rulbzb_id = b.id(+) and z.jizfzb_id = j.id(+) \n")
		.append("and z.rulrq >=").append(DateUtil.FormatOracleDate(getBRiq()))
		.append(" and z.rulrq <=").append(DateUtil.FormatOracleDate(getERiq()))	
		.append(" and z.diancxxb_id=").append(getTreeid())
		.append("  group by grouping sets((),(z.rulrq),(z.rulrq,b.mingc,b.xuh,j.mingc,j.xuh,z.fenxrq,h.rulmzlb_id) ) \n");
		
		if (STYLE_RQ.equals(this.getGesValue().getValue())) {
			sb.append("having not (grouping(b.mingc)=0) \n");
		}
		sb.append("   order by z.rulrq, b.xuh,j.xuh  )");

		
		Report rt = new Report();
		ResultSetList rstmp = con.getResultSetList(sb.toString());
		if (rstmp == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult);
			setMsg(ErrorMessage.NullResult);
			return "";
		}
		ResultSetList rs = null;
		String[][] ArrHeader=null;
		String[] strFormat=null;
		int[] ArrWidth=null;

    	rs = rstmp;
		if (STYLE_RQ.equals(this.getGesValue().getValue())) {
        	ArrHeader = new String[][]{{"��¯����", "ú��<br>(��)", "ȫˮ��<br>(%)<br>Mt","���������ˮ��<br>(%)<br>Mad","��Ͳ������<br>(J/g)<br>Qb,ad","�յ�����λ������<br>(J/g)<br>Qnet,ar","�յ�����λ������<br>(Kcal/kg)<br>Qnet,ar","����������ҷ�<br>(%)<br>Aad","������ҷ�<br>(%)<br>Ad","�յ����ҷ�<br>(%)<br>Aar","����������ӷ���<br>(%)<br>Vad","�����޻һ��ӷ���<br>(%)<br>Vdaf","ȫ���<br>(%)<br>St,ad","�������<br>(%)<br>St,d","�յ�����<br>(%)<br>St,ar"}};
    		ArrWidth = new int[] {85,65,65,65,65,65,65,65,65,65,65,65,65,65,65};
    		strFormat=new String[]{"","","","","","","","","","","","","","",""};
		} else {
        	ArrHeader = new String[][]{{"��¯����", "����", "����", "��������", "ú��<br>(��)", "ȫˮ��<br>(%)<br>Mt","���������ˮ��<br>(%)<br>Mad","��Ͳ������<br>(J/g)<br>Qb,ad","�յ�����λ������<br>(J/g)<br>Qnet,ar","�յ�����λ������<br>(Kcal/kg)<br>Qnet,ar","����������ҷ�<br>(%)<br>Aad","������ҷ�<br>(%)<br>Ad","�յ����ҷ�<br>(%)<br>Aar","����������ӷ���<br>(%)<br>Vad","�����޻һ��ӷ���<br>(%)<br>Vdaf","ȫ���<br>(%)<br>St,ad","�������<br>(%)<br>St,d","�յ�����<br>(%)<br>St,ar"}};
    		ArrWidth = new int[] {85, 70, 70, 70, 65,65,65,65,65,65,65,65,65,65,65,65,65,65};
    		
    		strFormat=new String[]{"","","","","","","","","","","","","","","","","",""};
		}
    	rt.setTitle(titledate + titlename, ArrWidth);
       
        
		rt.setBody(new Table(rs, 1, 0, 1));
		
		for(int i=0;i<ArrWidth.length;i++){
			
			rt.body.setColAlign(i+1, Table.ALIGN_CENTER);
		}
	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		String dcmc="";
		try{
			dcmc=MainGlobal.getTableCol("diancxxb", "quanc", "id="+this.getTreeid());
		}catch(Exception e){
			
		}
		rt.setDefaultTitle(1, 6, "�Ʊ�λ��" + dcmc,
				Table.ALIGN_LEFT);
		if (STYLE_RQ.equals(this.getGesValue().getValue())) {
			rt.setDefaultTitle(7, 5, "�������ڣ�" + DateUtil.FormatDate(new Date()),
					Table.ALIGN_CENTER);
		} else {
			rt.setDefaultTitle(8, 5, "�������ڣ�" + DateUtil.FormatDate(new Date()),
					Table.ALIGN_CENTER);
		}
		rt.setDefaultTitle(rt.body.getCols()-2, 2, "��λ���֡�%", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(100);
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.body.setColFormat(strFormat);
//		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		rt.createFooter(1,ArrWidth);
		rt.setDefautlFooter(1, 6, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 5, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(13, 4, "�Ʊ�", Table.ALIGN_LEFT);
		rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.footer.fontSize=10;
		// ����ҳ��
		 if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		
//		RPTInit.getInsertSql(v.getDiancxxb_id(),sb.toString(),rt,"��¯ú����̨��","ED_RULUTZ");
		
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
		
		tb1.addText(new ToolbarText("-"));
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
		
		

		tb1.addText(new ToolbarText("��ʽ:"));
		ComboBox ges = new ComboBox();
		ges.setTransform("GesSelect");
		ges.setWidth(100);
		tb1.addField(ges);
		
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
	
//	 ******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
			setDiancmcModels();
			setDiancjb();
			setGesModels();
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
