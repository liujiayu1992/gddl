package com.zhiren.dc.gdxw.jicxx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*
 * ����:���ܱ�
 * ����:2011-4-14 11:32:50
 * ����:ú��۶��ʽ���ҳ��
 * 
 * 
 */
public class Meikkdljs extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String CustomSetKey = "Meikkdljs";
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
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			
			if (mdrsl.getInt("id")==0) {
				
			} else {
				String jiesr="";
				String jiesrq="";
				long zhuangt=getExtGrid().getColumn("zhuangt").combo.getBeanId(mdrsl.getString("zhuangt"));
				if(zhuangt==0){
					jiesr=visit.getRenymc();
					jiesrq="sysdate";
				}else {
					jiesr="";
					jiesrq="null";
				}
				sbsql.append("update meikkdlsdb set jiessj="+jiesrq+",jiesr='"+jiesr+"',zhuangt=");
				sbsql.append(getExtGrid().getColumn("zhuangt").combo.getBeanId(mdrsl.getString("zhuangt"))+" where id="+mdrsl.getString("id")+";\n");
			
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	
	}
	
	




	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		String riq=this.getRiq();
		String riq2=this.getRiq1();
			sb.append(
				"select md.id,mk.mingc,md.suodrq,md.suodr,md.jiessj,\n" +
				"md.jiesr,decode(md.zhuangt,1,'������',0,'�ѽ���','') as zhuangt\n" + 
				"from meikkdlsdb md,meikxxb mk\n" + 
				"where  md.meikxxb_id=mk.id\n" +
				"and  md.suodrq>=to_date('"+riq+"','yyyy-mm-dd')\n" +
				"and  md.suodrq<=to_date('"+riq2+"','yyyy-mm-dd')\n" +
				"order by mk.mingc,md.suodrq");
			
		
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�����Ƿ���Ա༭
		egu.setTableName("meikkdlsdb");
		
	
		egu.getColumn("mingc").setHeader("ú��");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("suodrq").setHeader("��������");
		egu.getColumn("suodrq").setEditor(null);
		egu.getColumn("suodr").setHeader("������");
		egu.getColumn("suodr").setEditor(null);
		egu.getColumn("jiessj").setHeader("����ʱ��");
		egu.getColumn("jiessj").setEditor(null);
		egu.getColumn("jiesr").setHeader("������");
		egu.getColumn("jiesr").setEditor(null);
		egu.getColumn("zhuangt").setHeader("״̬");
	
		//����Grid����
		egu.addPaging(0);
		
		
		//״̬:0��ʹ��,1��ͣ��
		List sfss = new ArrayList();
		sfss.add(new IDropDownBean(1, "������"));
		sfss.add(new IDropDownBean(0, "�ѽ���"));
		egu.getColumn("zhuangt").setEditor(new ComboBox());
		egu.getColumn("zhuangt").setComboEditor(egu.gridId, new IDropDownModel(sfss));
		egu.getColumn("zhuangt").setReturnId(true);
		
		
		egu.addTbarText("��ú����:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("��:");
		DateField Endriq = new DateField();
		Endriq.Binding("RIQ1", "");
		Endriq.setValue(getRiq1());
		egu.addToolbarItem(Endriq.getScript());
		egu.addTbarText("-");
		
//		 ˢ�°�ť
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");

		
		/*egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("-");
		egu.addTbarText("���Ų���:");
		TextField tf=new TextField();
		tf.setWidth(80);
		tf.setValue(getChehao());
		
		tf.setListeners("change:function(own,n,o){document.getElementById('Chehao').value = n}");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");*/
	


		setExtGrid(egu);
		con.Close();
	}
//	ˢ���������ڰ�
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
	
//	ˢ���������ڰ�
	private String riq1;
	public void setRiq1(String riq1) {
		this.riq1 = riq1;
	}
	public String getRiq1() {
		return riq1;
	}
	
	private String chehao = "";
	public String getChehao(){
		return chehao;
	}
	public void setChehao(String ch){
		chehao = ch;
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
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
			setRiq(DateUtil.FormatDate(new Date()));
			setRiq1(DateUtil.FormatDate(new Date()));
			visit.setList1(null);
			getSelectData();
		}
	}

	// �ж������Ƿ��ڱ��ؿ����Ѿ�����(�����ڷ���0�����ڷ�������)
	private int Shujpd(JDBCcon con, String sql) {
		return JDBCcon.getRow(con.getResultSet(sql));
	}
}
