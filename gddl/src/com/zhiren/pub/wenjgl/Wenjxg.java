package com.zhiren.pub.wenjgl;


import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Wenjxg extends BasePage  {
	private String riq1;
	private String riq2;
	public String getRiq1() {
		if(riq1==null||riq1.equals("")){
			riq1=DateUtil.FormatDate(new Date());
		}
		return riq1;
	}
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	public String getRiq2() {
		if(riq2==null||riq2.equals("")){
			riq2=DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}
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
	private boolean RefurbishButton;
	public void RefurbishButton(IRequestCycle cycle) {
		RefurbishButton = true;
	}
	public void submit(IRequestCycle cycle) {
		if(RefurbishButton){
			Refurbish();
		}
	}
	private void Refurbish(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con= new JDBCcon();
		String sql=
		"select id,biaot,leix,shij,reny,'�鿴'chak,'�޸�'xiug\n" +
		"from wenjb\n" + 
		"where wenjb.diancxxb_id="+visit.getDiancxxb_id()+" and to_char(shij,'YYYY-MM-DD')>='"+getRiq1()+"'and to_char(shij,'YYYY-MM-DD')<='"+getRiq2()+"'";
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row);
//		����ÿҳ��ʾ����
		egu.addPaging(25);
		//����ҳ������body��Ӧ
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("biaot").setHeader("����");
		egu.getColumn("biaot").setWidth(160);
		egu.getColumn("biaot").setEditor(null);
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setWidth(60);
		egu.getColumn("leix").setEditor(null);
//		egu.getColumn("neir").setHeader("����");
//		egu.getColumn("neir").setWidth(160);
//		egu.getColumn("neir").setEditor(null);
		egu.getColumn("shij").setHeader("����༭ʱ��");
		egu.getColumn("shij").setWidth(100);
		egu.getColumn("shij").setEditor(null);
		egu.getColumn("reny").setHeader("����༭��");
		egu.getColumn("reny").setWidth(100);
		egu.getColumn("reny").setEditor(null);
		egu.getColumn("chak").setHeader("�鿴");
		egu.getColumn("chak").setWidth(100);
		egu.getColumn("chak").setEditor(null);
		egu.getColumn("xiug").setHeader("�޸�");
		egu.getColumn("xiug").setWidth(100);
		egu.getColumn("xiug").setEditor(null);
		String str=
       		" var url1 = 'http://'+document.location.host+document.location.pathname;"+
            "var end1 = url1.indexOf(';');"+
			"url1 = url1.substring(0,end1);"+
       	    "url1 = url1 + '?service=page/' + 'Wenjlr&wenjb_id='+record.data['ID'];";
			egu.getColumn("xiug").setRenderer(
					"function(value,p,record){" +str+
					"return \"<a href=# onclick=window.open('\"+url1+\"&flag=2','_self')>�޸�</a>\"}"
			);
			egu.getColumn("chak").setRenderer(
					"function(value,p,record){" +str+
					"return \"<a href=# onclick=window.open('\"+url1+\"&flag=1','_self')>�鿴</a>\"}"
			);
	
		//
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarText("�༭ʱ��") ;
		DateField riq1 = new DateField();
		riq1.setValue(this.getRiq1());
		riq1.Binding("RIQ1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		riq1.setId("riq1");
		riq1.emptyText="��ʼ����";
		egu.addToolbarItem(riq1.getScript());
		
		DateField riq2 = new DateField();
		riq2.setValue(this.getRiq2());
		riq2.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		riq2.setId("riq2");
		riq2.emptyText="��������";
		egu.addToolbarItem(riq2.getScript());
		egu.addTbarBtn(refurbish);
		setExtGrid(egu);
		con.Close();
	}
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			Refurbish();
		}
		
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
}
