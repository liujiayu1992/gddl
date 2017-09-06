package com.zhiren.jt.het.hethqspd;

import java.sql.ResultSet;
import java.util.Date;

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
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Hethqspd extends BasePage {

	public boolean getRaw() {
		return true;
	}

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

	public String getPrintTable() {
		return getHethqspd();
	}

	// ȼ�ϲɹ���ָ���������ձ�
	private String getHethqspd() {
		Report rt = new Report();
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sqlliuc = new StringBuffer();
		sqlliuc.append(
				"select qianqztmc,caozy,miaos,to_char(shij, 'yyyy-mm-dd hh24:mi:ss') as shij\n" + 
				"from liucgzb\n" + 
				"where liucgzid=(select liucgzid from  hetb where hetbh='"+getBianmValue().getValue().trim()+"')\n" + 
				"and qianqztmc||to_char(shij,'yyyymmddhh24miss') in(\n" + 
				"select qianqztmc||to_char(max(shij),'yyyymmddhh24miss')\n" + 
				"from liucgzb\n" + 
				"where liucgzid=(select liucgzid from  hetb where hetbh='"+getBianmValue().getValue().trim()+"')\n" + 
				" and qianqztmc in(\n" + 
				" select  distinct qianqztmc\n" + 
				"from (\n" + 
				"select rownum i,t.*\n" + 
				"from (\n" + 
				"select *\n" + 
				"from liucgzb\n" + 
				"where liucgzid=(select liucgzid from  hetb where hetbh='"+getBianmValue().getValue().trim()+"') order by id desc\n" + 
				")t)where i>=(\n" + 
				"select max(i1)\n" + 
				"from(\n" + 
				"select  rownum i1,t1.*\n" + 
				"from(\n" + 
				"select liucgzb.*\n" + 
				"from liucgzb\n" + 
				"where liucgzid=(select liucgzid from  hetb where hetbh='"+getBianmValue().getValue().trim()+"') order by id desc\n" + 
				")t1 )where liucdzbmc='�ύ' and houjztmc =(\n" + 
				"select houjztmc\n" + 
				"from(\n" + 
				"select *\n" + 
				"from liucgzb\n" + 
				"where liucgzid=(select liucgzid from  hetb where hetbh='"+getBianmValue().getValue().trim()+"') order by id desc\n" + 
				")t1 where rownum=1\n" + 
				") )and  qianqztmc in(select l.qianqztmc from liucgzb l )\n" + 
				"and  qianqztmc <>'����'\n" + 
				"and  qianqztmc <>'����'\n" + 
				" )group by qianqztmc,caozy\n" + 
				" )\n" + 
				" order by shij");
		ResultSetList rs = con.getResultSetList(sqlliuc.toString());
		StringBuffer sqljcxx = new StringBuffer();
				sqljcxx.append(
						"select distinct h.id,h.hetbh,g.mingc as gongysmc,hs.hetl,h.gongfdh,h.xufdh\n" +
						",h.qianddd,h.xuffddbr,fqsj\n" + 
						"from liucgzb l, hetb h,hetslb hs,gongysb g,(select max(to_char(shij,'yyyy-mm-dd hh24:mi:ss'))as fqsj\n" + 
						"from liucgzb,hetb where qianqztmc ='����' and  liucgzb.liucgzid=hetb.liucgzid and hetb.hetbh='"+getBianmValue().getValue().trim()+"')\n" + 
						"where l.liucgzid=(select liucgzid from  hetb where hetb.hetbh='"+getBianmValue().getValue().trim()+"'\n" + 
						")and l.liucgzid=h.liucgzid and hs.hetb_id=h.id  and h.gongysb_id=g.id order by h.id desc");
;
	   ResultSet rsl = con.getResultSet(sqljcxx);

		
		String[][] ArrHeader = new String[7+rs.getResultSetlist().size()][6];
		try {
			if (rsl.next()) {
				   String gongfdh="";
				   String xufdh="";
				   if(rsl.getString("gongfdh")==null){
					   gongfdh="";
				   }
				   if(rsl.getString("xufdh")==null){
					   xufdh="";
				   }
				
				ArrHeader[0] = new String[] {"��ͬ���","��ͬ���","��ͬ���","����λ","����λ","����ʱ��"};;

				ArrHeader[1] = new String[] {""+ rsl.getString("hetbh"),rsl.getString("hetbh"),rsl.getString("hetbh")+"",
						"ȼ�Ϲ���","ȼ�Ϲ���",""+rsl.getString("fqsj")+""};
				ArrHeader[2]=new String[] {"����",""+rsl.getString("gongysmc")+"",
						""+rsl.getString("gongysmc")+"",
						""+rsl.getString("gongysmc")+"",
						""+rsl.getString("gongysmc")+"",
						""+rsl.getString("gongysmc")+""};

				ArrHeader[3] = new String[] { "ú��", "" + rsl.getString("hetl") + "",
						"��ϵ�绰", "��ϵ�绰", "" + gongfdh + "",
						""+gongfdh+"" };
				ArrHeader[4] = new String[] { "��", ""+visit.getDiancmc()+"",
						""+visit.getDiancmc()+"", 
						""+visit.getDiancmc()+"", 
						""+visit.getDiancmc()+"", 
						""+visit.getDiancmc()+""};
				ArrHeader[5] = new String[] { "������", ""+rsl.getString("xuffddbr")+"",
						"��ϵ�绰", "��ϵ�绰",""+  xufdh + "",
						""+  xufdh + ""};
				ArrHeader[6]=new String[] {"��λ����","������","����ʱ��","����ʱ��","�������","�������"};
				int i=7;
				while (rs.next()){
					
					ArrHeader[i++]=new String[] {""+rs.getString("qianqztmc"),
							                        rs.getString("caozy"),
							                        rs.getString("shij"),
							                        rs.getString("shij"),
							                        rs.getString("miaos"),
							                        rs.getString("miaos")};
					
				}
				

				} else
				return null;
			
			
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] {80,80,80,80,80,80};
		
		

		rt.setTitle("ú̿������ͬ��ǩ������", ArrWidth);
		rt.setBody(new Table(ArrHeader.length,6));
		rt.body.setHeaderData(ArrHeader);// ��ͷ����
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 20);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "��ӡ����:" + str, -1);
		//rt.setDefautlFooter(3, 1, "�����ˣ�", -1);
	
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
        
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
//		String[][] ArrHeader1 = new String[1][6];
//		ArrHeader1[0] = ArrHeader[0];
//		rt.body.setHeaderData(ArrHeader1);// ��ͷ����

		
		for (int i = 1; i <= ArrHeader.length; i++) {
			rt.body.setRowCells(i, Table.VALIGN_CENTER, 9);
		}
		rt.body.setRowHeight(45);

	 
		rt.body.mergeCell(1,1,1,3);
		rt.body.mergeCell(1,4,1,5);
		rt.body.mergeCell(2,1,2,3);
		rt.body.mergeCell(2,4,2,5);
		rt.body.mergeCell(3,2,3,6);
		rt.body.mergeCell(4,3,4,4);
		rt.body.mergeCell(4,5,4,6);
		rt.body.mergeCell(5,2,5,6);
		rt.body.mergeCell(6,3,6,4);
		rt.body.mergeCell(6,5,6,6);
		for (int i = 7; i <=ArrHeader.length; i++) {
			
			rt.body.mergeCell(i,3,i,4);
			rt.body.mergeCell(i, 5, i, 6);
		}
		
		
		rt.body.ShowZero = false;

		// ����ҳ��
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		//rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// ˢ�ºⵥ�б�
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		if (visit.isFencb()) {
			tb1.addText(new ToolbarText("����:"));
			ComboBox changbcb = new ComboBox();
			changbcb.setTransform("ChangbSelect");
			changbcb.setWidth(130);
			changbcb
					.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
			tb1.addField(changbcb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("ǩ������:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("��"));
		
		DateField df2 = new DateField();
		df2.setValue(getRiq2());
		df2.Binding("RIQ2", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("riq2");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("��ͬ���:"));
		ComboBox shij = new ComboBox();
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
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
		((DateField) getToolbar().getItem("riq")).setValue(getRiq());
		((DateField) getToolbar().getItem("riq2")).setValue(getRiq2());
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
			setChangbValue(null);
			setChangbModel(null);
			setBianmValue(null);
			setBianmModel(null);
			getSelectData();
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
	}

	// �����Ƿ�仯
	private boolean riqchange = false;

	// ������
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}
	
	private boolean riq2change = false;
	private String riq2;

	public String getRiq2() {
		return riq2;
	}

	public void setRiq2(String riq2) {
		if (this.riq2 != null) {
			if (!this.riq2.equals(riq2))
				riq2change = true;
		}
		this.riq2 = riq2;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		sb
				.append("select id,hetbh from hetb where qiandrq>="+DateUtil.FormatOracleDate(getRiq())+" and \n" +
						"qiandrq<="+DateUtil.FormatOracleDate(getRiq2())+"\n" +
						" order by id ");
		setBianmModel(new IDropDownModel(sb.toString(), "��ѡ��"));
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
			setChangbModel();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModel() {
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
}
