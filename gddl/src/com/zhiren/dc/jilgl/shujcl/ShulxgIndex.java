package com.zhiren.dc.jilgl.shujcl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-24
 * �������޸���Shulxxxg ҳ�����תʱ���������䷽ʽѡ�����͵����⡣
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-01 11��20
 * �������޸�ˢ��ʱδ��������Ӧ��ú�������Ҳ������ʾ
 */
/*
 * ����:tzf
 * ʱ��:2009-07-15
 * �޸�����:����ϵͳ���ã����Ӷ�ѡ��
 */
public class ShulxgIndex extends BasePage implements PageValidateListener {
	
	public static final String YUNSFS_QY = "QY";// ����

	public static final String YUNSFS_HY = "HY";// ����

	public static final String YUNSFS_All = "ALL";// ȫ��

	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString2();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString2(yunsfs);
	}
	
	private String msg = "";

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

//	 ������
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getRiq2() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setRiq2(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}

    //ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _XiugChick = false;
    public void XiugButton(IRequestCycle cycle) {
    	_XiugChick = true;
    }
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_XiugChick){
			_XiugChick = false;
			GotoShezfa(cycle);
		}
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		((Visit) getPage().getVisit()).setString1(this.getParameters());
//		System.out.print(((Visit) getPage().getVisit()).getString1());
		cycle.activate("Shulxxxg");
	}
	
	private String Parameters;//��¼��ĿID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}

	public void getSelectData() {
		Visit v = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String ysfs = "";
		if(YUNSFS_QY.equals(getYunsfs())){
			ysfs = " and yunsfsb_id = 2 ";
		}
		if(YUNSFS_HY.equals(getYunsfs())){
			ysfs = " and yunsfsb_id = 1 ";
		}
		String sql="select f.id as id,decode(f.liucztb_id,1,'�����',0,'δ���') as zhuangt,d.mingc diancxxb_id,g.mingc as g_mc,m.mingc as m_mc,p.mingc as p_mc,c.mingc as c_mc,"
                + " f.fahrq as fahrq,f.daohrq as daohrq,chec,j.mingc as j_mc,biaoz,jingz,ches,y.mingc as y_mc "
                + " from fahb f,gongysb g,meikxxb m,pinzb p,chezxxb c,jihkjb j,yunsfsb y, diancxxb d "
                + " where g.id(+) = f.gongysb_id and f.diancxxb_id = d.id "
                + Jilcz.filterDcid(v, "f")+"\n"
                + " and m.id(+) = f.meikxxb_id "
                + " and p.id = f.pinzb_id "
                + " and c.id = f.faz_id "
                + " and j.id = f.jihkjb_id "
                + " and y.id = yunsfsb_id "
                + " and f.daohrq>=to_date('" +getRiqi()+ "','yyyy-mm-dd') and f.daohrq<to_date('" +getRiq2()+ "','yyyy-mm-dd')+1 "
                + ysfs
                + " order by f.daohrq,f.id";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
		//����GRID�Ƿ���Ա༭
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		String duox=" select zhi from xitxxb where mingc='������Ϣ�޸Ķ�ѡ' and zhuangt=1 and leib='����' and diancxxb_id="+v.getDiancxxb_id();
		
		ResultSetList rsldx=con.getResultSetList(duox);
		
		if(rsldx.next() && rsldx.getString("zhi").equals("��")){
			egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		}else{
			egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		}
		
		egu.addPaging(25);
		
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zhuangt").setHeader("���״̬");
		egu.getColumn("zhuangt").setWidth(70);
		egu.getColumn("zhuangt").editor = null;
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("g_mc").setHeader("��Ӧ��");
		egu.getColumn("g_mc").setWidth(70);
		egu.getColumn("g_mc").editor = null;
		egu.getColumn("m_mc").setHeader("ú��");
		egu.getColumn("m_mc").setWidth(70);
		egu.getColumn("m_mc").editor = null;
		egu.getColumn("p_mc").setHeader("Ʒ��");
		egu.getColumn("p_mc").setWidth(70);
		egu.getColumn("p_mc").editor = null;
		egu.getColumn("c_mc").setHeader("��վ");
		egu.getColumn("c_mc").setWidth(70);
		egu.getColumn("c_mc").editor = null;
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("fahrq").editor = null;
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").editor = null;
		egu.getColumn("chec").setHeader("����");
		egu.getColumn("chec").setWidth(70);
		egu.getColumn("chec").editor = null;
		egu.getColumn("j_mc").setHeader("�ƻ��ھ�");
		egu.getColumn("j_mc").setWidth(70);
		egu.getColumn("j_mc").editor = null;
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("biaoz").setWidth(70);
		egu.getColumn("biaoz").editor = null;
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setWidth(70);
		egu.getColumn("jingz").editor = null;
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setWidth(70);
		egu.getColumn("ches").editor = null;
		egu.getColumn("y_mc").setHeader("���䷽ʽ");
		egu.getColumn("y_mc").setWidth(70);
		egu.getColumn("y_mc").editor = null;
		
//		�������ڲ�ѯ
		egu.addTbarText("��������:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		String str2=
			"   var recs = gridDiv_sm.getSelections(); \n"
	        +"  if(recs!=null && recs.length>0 ){  gridDiv_history='';\n"
	        +"  for(var i=0;i<recs.length;i++){ var rec=recs[i];\n"
	        +"      zt = rec.get('ZHUANGT');\n"
	        +"      if(zt == '�����'){\n"
	        +"          Ext.MessageBox.alert('��ʾ��Ϣ','��������ݲ����޸ģ�');\n"
	        +"          return;"
	        +"      } " 
	        +"      gridDiv_history += rec.get('ID'); if(i!=recs.length-1) gridDiv_history+=',';\n"
	        +"		}\n"  
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"      "
	        +"  }else{\n"
	        +"  	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ����Ŀ!'); \n"
	        +"  	return;"
	        +"  }"
	        +" document.getElementById('XiugButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("�޸�","function(){"+str2+"}").getScript()+"}");
		
		setExtGrid(egu);
		con.Close();

	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String reportType = cycle.getRequestContext().getParameter("lx");
		if (reportType != null) {
			setYunsfs(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			if(!visit.getActivePageName().toString().equals("Shulxxxg")){
				visit.setString1("");	//����ͬid	
				setRiqi(DateUtil.FormatDate(new Date()));
				setRiq2(DateUtil.FormatDate(new Date()));
				if(reportType==null){
					setYunsfs(YUNSFS_All);
				}
			}
			visit.setActivePageName(getPageName().toString());
			getSelectData();
		}
	}
}
