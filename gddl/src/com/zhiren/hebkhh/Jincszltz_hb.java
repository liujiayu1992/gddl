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
 * @since 2009.09.24
 * @version 1.0
 * @discription ����ú��������ѯ
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
 * ʱ�䣺2009-09-25
 * ���������Ӿ��ص���ʾ���,��������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-31
 * �����������˱�������ѡ���Ϊ��ϸ�ͻ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-02
 * �������޸Ļ���ָ��Ϊ���±�ָ��һ�¡�
 */
public class Jincszltz_hb extends BasePage {

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
	
	private String getJincszltz_mx(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		String sql = 
		"select decode(grouping(f.daohrq),1,'<font color=orange><b><i>�ϼ�</b></i></font>'," +
		"to_char(f.daohrq,'yyyy-mm-dd')) dhrq, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" +
		"d.mingc) dcmc, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>С��</b>',y.mingc)) ysfs, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>С��</b>',g.mingc)) gys, \n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>С��</b>',m.mingc)) mkmc,\n" +
		"decode(grouping(i.bianm)+grouping(d.mingc),2,\n" +
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" +
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" +
		"decode(grouping(d.mingc)+grouping(y.mingc),1,'<b>С��</b>',p.mingc)) pz,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.jingz),0)||'</font>' jz, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.biaoz),0)||'</font>' bz, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.yuns),0)||'</font>' ys, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.yingk),0)||'</font>' yk, " +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(sum(f.zongkd),0)||'</font>' kd,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)),'0')||'</font>' qner_ar_mj,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/0.0041816,0)),'0')||'</font>' qner_ar_k,\n" +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz),1)),'0')||'</font>' mt,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz),2)),'0')||'</font>' vdaf,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz),2)),'0')||'</font>' std,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz),2)),'0')||'</font>' aar,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz),2)),'0')||'</font>' buhsdj,\n" + 
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz*z.qnet_ar),2)),'0')||'</font>' buhsdj_k,\n" +
		
		"decode(grouping(f.daohrq)+grouping(i.bianm)+grouping(d.mingc),3,'<font color=orange>',2,'<font color=green>'," +
		"1,decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color = black>')||\n" +
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj*z.qnet_ar)/sum(f.jingz*29.271),2)),'0')||'</font>' buhsbmdj\n" + 
		
		"from fahb f, item i, itemsort s, diancxxb d, yunsfsb y,\n" + 
		"gongysb g, meikxxb m, pinzb p, zhilb z, jiesb j\n" + 
		"where f.diancxxb_id = d.id and f.yunsfsb_id = y.id and f.gongysb_id = g.id\n" + 
		"and f.meikxxb_id = m.id and f.pinzb_id = p.id\n" + 
		"and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
		"and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
		"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
		"and f.diancxxb_id||'' = i.mingc\n" + 
		"and f.zhilb_id = z.id(+) and f.jiesb_id = j.id(+)\n" + 
		"group by rollup(daohrq,i.bianm,d.mingc,g.mingc,y.mingc,m.mingc,p.mingc)\n" + 
		"having grouping(p.mingc) = 0 or grouping(g.mingc) = 1 " +
		"order by f.daohrq,d.mingc,y.mingc,g.mingc,m.mingc,p.mingc" ;
		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{"����", "��λ", "���䷽ʽ", "��Ӧ��", "ú��", 
			"Ʒ��", "����<br>(��)", "Ʊ��<br>(��)", "����<br>(��)", "ӯ��<br>(��)", 
			"�۶�<br>(��)", "����", "����", "ˮ��<br>(mt)<br>(%)", "�ӷ���<br>(vdaf)<br>(%)", 
			"���<br>(std)<br>(%)", "�ҷ�<br>(aar)<br>(%)", "���㵥��", "���㵥��", "��ú����<br>Ԫ/��"},
    		{"����", "��λ", "���䷽ʽ", "��Ӧ��", "ú��", 
    			"Ʒ��", "����<br>(��)", "Ʊ��<br>(��)", "����<br>(��)", "ӯ��<br>(��)", 
    			"�۶�<br>(��)", "MJ/kg", "Kcal/kg", "ˮ��<br>(mt)<br>(%)", "�ӷ���<br>(vdaf)<br>(%)", 
    			"���<br>(std)<br>(%)", "�ҷ�<br>(aar)<br>(%)", "Ԫ/��.��", "Ԫ/��", "��ú����<br>Ԫ/��"}
			 };

    	ArrWidth = new int[] {70, 35, 35, 150, 100, 60, 70, 70, 70, 70, 70, 
    			70, 70, 70, 70, 70, 70, 70, 70, 70};

		rt.setTitle("��ú�ۺϲ�ѯ", ArrWidth);

	
		rt.title.fontSize=10;
		rt.title.setRowHeight(2, 30);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		
		rt.setBody(new Table(rs, 2, 0, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		
//		rt.body.setColFormat(strFormat);
		rt.body.setPageRows(40);
		for(int i = 3; i< rt.body.getRows() ; i++){
		rt.body.merge(i, 1, i, 6);
		}
		if(rt.body.getRows()>2)
			rt.body.mergeCell(rt.body.getRows(), 1, rt.body.getRows(), 6);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		
		for(int i = 1; i<= 6 ; i++){
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		for(int i = 7; i <=ArrWidth.length ; i++){
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
	
	private String getJincszltz_hz(){
		JDBCcon con = new JDBCcon();
		Visit v = (Visit) getPage().getVisit();
		Report rt = new Report();
		String sql = 
		"select decode(grouping(i.bianm)+grouping(d.mingc),2,\n" + 
		"'<font color=green><b>ʡ��˾�ϼ�</b></font>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>����','<font color=blue>��ͷ')||'С��</font>',\n" + 
		"d.mingc) dcmc,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.jingz)||'</font>' jingz,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.biaoz)||'</font>' biaoz,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.yuns)||'</font>' yuns,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.yingk)||'</font>' yingk,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"sum(f.zongkd)||'</font>' zongkd,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz),2)),'0')||'</font>' qner_ar_mj,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.qnet_ar)/sum(f.jingz)/0.0041816,0)),'0')||'</font>' qner_ar_k,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.mt)/sum(f.jingz),1)),'0')||'</font>' mt,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.vdaf)/sum(f.jingz),2)),'0')||'</font>' vdaf,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.std)/sum(f.jingz),2)),'0')||'</font>' std,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*z.aar)/sum(f.jingz),2)),'0')||'</font>' aar,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz),2)),'0')||'</font>' buhsdj,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj)/sum(f.jingz*z.qnet_ar),2)),'0')||'</font>' buhsdj_k,\n" + 
		"decode(grouping(i.bianm)+ grouping(d.mingc),2,'<font color=green>',1,\n" + 
		"decode(i.bianm,229,'<font color=red>','<font color=blue>'),'<font color=black>')||\n" + 
		"nvl(decode(sum(f.jingz),0,0,round_new(sum(f.jingz*j.buhsdj*z.qnet_ar)/sum(f.jingz*29.271),2)),'0')||'</font>' buhsbmdj\n" + 
		"from fahb f, item i, itemsort s, diancxxb d, zhilb z, jiesb j\n" + 
		"where f.diancxxb_id = d.id\n" + 
		"and f.daohrq >= "+DateUtil.FormatOracleDate(getBRiq()==null||"".equals(getBRiq())?DateUtil.FormatDate(new Date()):getBRiq())+"\n" + 
		"and f.daohrq <= "+DateUtil.FormatOracleDate(getERiq()==null||"".equals(getERiq())?DateUtil.FormatDate(new Date()):getERiq())+"\n" + 
		"and s.bianm = 'DCHZBM' and i.itemsortid = s.id\n" + 
		"and f.diancxxb_id||'' = i.mingc\n" + 
		"and f.zhilb_id = z.id(+) and f.jiesb_id = j.id(+)\n" + 
		"group by rollup(i.bianm,d.mingc)";

		ResultSetList rs = con.getResultSetList(sql);
		String[][] ArrHeader=null;
		int[] ArrWidth=null;
    	ArrHeader = new String[][] {
    		{ "��λ", "����<br>(��)", "Ʊ��<br>(��)", "����<br>(��)", "ӯ��<br>(��)", 
			"�۶�<br>(��)", "����", "����", "ˮ��<br>(mt)<br>(%)", "�ӷ���<br>(vdaf)<br>(%)", 
			"���<br>(std)<br>(%)", "�ҷ�<br>(aar)<br>(%)", "���㵥��", "���㵥��", "��ú����<br>Ԫ/��"},
    		{"��λ", "����<br>(��)", "Ʊ��<br>(��)", "����<br>(��)", "ӯ��<br>(��)", 
    			"�۶�<br>(��)", "MJ/kg", "Kcal/kg", "ˮ��<br>(mt)<br>(%)", "�ӷ���<br>(vdaf)<br>(%)", 
    			"���<br>(std)<br>(%)", "�ҷ�<br>(aar)<br>(%)", "Ԫ/��.��", "Ԫ/��", "��ú����<br>Ԫ/��"}
			 };

    	ArrWidth = new int[] {70, 70, 70, 70, 70, 70, 
    			70, 70, 70, 70, 70, 70, 70, 70, 70};

		rt.setTitle("��ú�ۺϲ�ѯ", ArrWidth);

	
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
		
		for(int i = 2; i <=ArrWidth.length ; i++){
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
			html = getJincszltz_mx();
		}else{
			html = getJincszltz_hz();
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
			setRptTypeModels();
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
