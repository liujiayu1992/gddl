package com.zhiren.hebkhh;

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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author Rock
 * @since 2009.09.25
 * @version 1.0
 * @discription �ձ��ۺϲ�ѯ
 */
/*
 * ����SQL
insert into itemsort(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(209,209,1,'DCHZBM','�糧���ܱ���',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(300,209,1,229,'2291',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(310,209,1,229,'2292',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(320,209,1,232,'2321',1,'');
insert into item(id,itemsortid,xuh,bianm,mingc,zhuangt,beiz)
values(330,209,1,232,'2321',1,'');
 * 
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-31
 * �����������˱�������ѡ���Ϊ��ϸ�ͻ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-01
 * �������޸Ŀ���㷨��
 */
public class Ribzhcx_hb extends BasePage {
	
	private static final String RptType_mx = "��ϸ";
	private static final String RptType_hz = "����";
//	�����û���ʾ
	private String msg="";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO �Զ����ɷ������
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
//	��������������
	public IDropDownBean getRptTypeValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getRptTypeModel().getOptionCount()>0) {
				setRptTypeValue((IDropDownBean)getRptTypeModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setRptTypeValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getRptTypeModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setRptTypeModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setRptTypeModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	public void setRptTypeModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1,RptType_mx));
		list.add(new IDropDownBean(2,RptType_hz));
		setRptTypeModel(new IDropDownModel(list));
	}
	
//	ˢ�ºⵥ�б�
	public void getSelectData() {
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
//		��������ѡ��
		tb1.addText(new ToolbarText("����:"));
		ComboBox leix = new ComboBox();
		leix.setTransform("LeixSelect");
		leix.setWidth(80);
		leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");
		tb1.addField(leix);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton rbtn = new ToolbarButton(null,"��ѯ","function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
	
	private String getRibzhcx_mx(){
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String sql = 
			"select decode(grouping(riq),1,'<font color=orange><b><i>�ϼ�</b></i></font>',\n" +
			"to_char(riq,'yyyy-mm-dd')) riq,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,\n" + 
			"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" + 
			"d.mingc) dcmc,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(dangrgm),'0')||'</font>' drsm,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(haoyqkdr),'0')||'</font>' drhy,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(kuc),'0')||'</font>' drkc,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum(rc.rcrz*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rcrz,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum(rl.rlrz*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rlrz,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum((rc.rcrz - rl.rlrz)*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rzcj,\n" + 
			"decode(grouping(riq)+grouping(i.bianm)+ grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>',1,\n" +
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum((rc.rcrz - rl.rlrz)*dangrgm)/(0.0041816*sum(dangrgm)),0)),'0')||'</font>' rzck\n" + 
			"from shouhcrbb shc, diancxxb d, item i, itemsort s,\n" + 
			"(select f.daohrq,diancxxb_id, decode(sum(f.laimsl),0,0,\n" + 
			"round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) rcrz\n" + 
			"from fahb f,zhilb z where f.zhilb_id = z.id group by f.daohrq,diancxxb_id) rc,\n" + 
			"(select rulrq,diancxxb_id,round_new(avg(qnet_ar),2) rlrz\n" + 
			"from rulmzlb group by rulrq,diancxxb_id) rl\n" + 
			"where shc.riq = rc.daohrq(+)\n" + 
			"and shc.riq = rl.rulrq(+)\n" + 
			"and shc.diancxxb_id = d.id\n" + 
			"and shc.diancxxb_id = rc.diancxxb_id(+)\n" + 
			"and shc.diancxxb_id = rl.diancxxb_id(+)\n" + 
			"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
			"and shc.diancxxb_id||'' = i.mingc\n" + 
			"and shc.riq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
			"and shc.riq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
			"group by rollup(shc.riq,i.bianm,d.mingc)";

		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{"����", "��λ", "���Ĵ�(��)", "���Ĵ�(��)", "���Ĵ�(��)", 
			"�볧��ֵ<br>(MJ/kg)", "��¯��ֵ<br>(MJ/kg)", "��ֵ��", "��ֵ��"},
    		{"����", "��λ", "��", "��", "��", 
				"�볧��ֵ<br>(MJ/kg)", "��¯��ֵ<br>(MJ/kg)", "MJ/kg", "Kcal/kg"}
			 };

    	ArrWidth = new int[] {70, 80, 70, 70, 70, 70, 70, 60, 60};

		rt.setTitle("�ձ��ۺϲ�ѯ", ArrWidth);

	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 2, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		for(int i = 3; i< rt.body.getRows() ; i++){
		rt.body.merge(i, 1, i, 6);
		}
		if(rt.body.getRows()>2)
			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 2);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 1; i<= 2 ; i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		for(int i = 3; i <=ArrWidth.length ; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rs.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();// ph;
	}
	private String getRibzhcx_hz(){
		JDBCcon con = new JDBCcon();
		Report rt = new Report();
		String sql = 

			"select\n" +
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,\n" + 
			"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" + 
			"d.mingc) dcmc,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(dangrgm),'0')||'</font>' drsm,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(haoyqkdr),'0')||'</font>' drhy,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(sum(k.kuc),'0')||'</font>' drkc,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum(rc.rcrz*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rcrz,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum(rl.rlrz*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rlrz,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum((rc.rcrz - rl.rlrz)*dangrgm)/sum(dangrgm),2)),'0')||'</font>' rzcj,\n" + 
			"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
			"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')\n" + 
			"||nvl(decode(sum(dangrgm),0,0,round_new(sum((rc.rcrz - rl.rlrz)*dangrgm)/(0.0041816*sum(dangrgm)),0)),'0')||'</font>' rzck\n" + 
			"from shouhcrbb shc, diancxxb d, item i, itemsort s,\n" + 

			"(select h.diancxxb_id,h.kuc,hh.riq from shouhcrbb h,\n" +
			"(select diancxxb_id,max(riq)riq from shouhcrbb where\n" + 
			"riq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
			"and riq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
			"group by diancxxb_id ) hh\n" + 
			"where  h.diancxxb_id = hh.diancxxb_id\n" + 
			"and h.riq = hh.riq) k," +

			"(select f.daohrq,diancxxb_id, decode(sum(f.laimsl),0,0,\n" + 
			"round_new(sum(z.qnet_ar*f.laimsl)/sum(f.laimsl),2)) rcrz\n" + 
			"from fahb f,zhilb z where f.zhilb_id = z.id group by f.daohrq,diancxxb_id) rc,\n" + 
			"(select rulrq,diancxxb_id,round_new(avg(qnet_ar),2) rlrz\n" + 
			"from rulmzlb group by rulrq,diancxxb_id) rl\n" + 
			"where shc.riq = rc.daohrq(+)\n" +
			"and shc.riq = k.riq(+)\n" +
			"and shc.riq = rl.rulrq(+)\n" + 
			"and shc.diancxxb_id = k.diancxxb_id(+)\n" +
			"and shc.diancxxb_id = d.id\n" + 
			"and shc.diancxxb_id = rc.diancxxb_id(+)\n" + 
			"and shc.diancxxb_id = rl.diancxxb_id(+)\n" + 
			"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
			"and shc.diancxxb_id||'' = i.mingc\n" + 
			"and shc.riq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
			"and shc.riq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
			"group by rollup(i.bianm,d.mingc)";

		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{"��λ", "���Ĵ�(��)", "���Ĵ�(��)", "���Ĵ�(��)", 
			"�볧��ֵ<br>(MJ/kg)", "��¯��ֵ<br>(MJ/kg)", "��ֵ��", "��ֵ��"},
    		{"��λ", "��", "��", "��", 
				"�볧��ֵ<br>(MJ/kg)", "��¯��ֵ<br>(MJ/kg)", "MJ/kg", "Kcal/kg"}
			 };

    	ArrWidth = new int[] {80, 70, 70, 70, 70, 70, 60, 60};

		rt.setTitle("�ձ��ۺϲ�ѯ", ArrWidth);

	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 2, 0, 1));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 2; i <= ArrWidth.length; i++){
			rt.body.setColAlign(i, Table.ALIGN_RIGHT);
		}
		rs.close();
		con.Close();
		if(rt.body.getPages()>0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
     	return rt.getAllPagesHtml();// ph;
	}
	
	public String getPrintTable(){
		String html = "";
		if(RptType_mx.equalsIgnoreCase(getRptTypeValue().getValue())){
			html = getRibzhcx_mx();
		}else{
			html = getRibzhcx_hz();
		}
		return html;
		
	}
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		if(getToolbar() == null){
			return "";
		}
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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			getSelectData();
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
}
