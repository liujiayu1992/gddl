package com.zhiren.dc.gdxw.qiangzzy;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ����:tzf
 * ʱ��:2009-12-29
 * �޸�����:ǿ������ʱ��ͬ��ά��������Ϣzhiyryb
 */
public class Qiangzzy extends BasePage implements PageValidateListener {
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
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
	
	
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	
	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	
	
	private void Show() {
		
		Visit visit=(Visit)this.getPage().getVisit();
		if(getChange()==null || "".equals(getChange())) {
			setMsg("��ѡ�����ݽ���ǿ��������");
			return;
		}
		int flag=0;
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		StringBuffer sbsql = new StringBuffer();
		
		sbsql.append("begin\n");
		while(rsl.next()) {
			sbsql.append("update gdxw_cy set zhuangt=1,beiz='ǿ������'  where id ="+rsl.getString("id")+";\n");
		}	
		
		sbsql.append("end;");
		
		
		flag=con.getUpdate(sbsql.toString());
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail + "SQL:" + sbsql.toString());
			setMsg("������̳��ִ��󣡱���δ�ɹ���");
			con.rollBack();
			con.Close();
			return;
		}
		
		con.Close();
		this.setMsg("ǿ�������ɹ�!");
		
	}
	
	
	
	
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show();
			getSelectData();
		}
	}
	


	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riq=this.getRiq();
		StringBuffer sb = new StringBuffer();
		
				sb.append("select cy.id,rownum  as xuh,y.mingc,cy.jingz\n");
				sb.append("from gdxw_cy cy,caiyb c ,cunywzb y\n");
				sb.append(" where  cy.zhuangt=0\n"); 
				sb.append(" and cy.zhilb_id=c.zhilb_id\n");
				sb.append(" and c.cunywzb_id=y.id\n"); 
				sb.append(" and to_char(cy.shengcrq,'yyyy-mm-dd')='"+riq+"'\n"); 
				sb.append(" order by y.xuh");



		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		���ö�ѡ��
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//����ÿҳ��ʾ����
		egu.addPaging(0);
		
		
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
	
		
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		
		egu.getColumn("mingc").setHeader("Ͱ��");
		egu.getColumn("mingc").setWidth(120);
	
		egu.getColumn("jingz").setHeader("ú��");
		egu.getColumn("jingz").setWidth(80);
		
	
		
	
		
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("RIQ");
		egu.addTbarText("��ú���ڣ�");
		egu.addToolbarItem(df.getScript());
		/*	
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		dfe.setId("EndRq");
		egu.addTbarText("��");
		egu.addToolbarItem(dfe.getScript());*/
		
		
		GridButton refurbish = new GridButton("ˢ��","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("ǿ������",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_SelSubmit);
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
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
	
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			
			
			visit.setActivePageName(getPageName().toString());
			setRiq( DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			getSelectData();
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
}