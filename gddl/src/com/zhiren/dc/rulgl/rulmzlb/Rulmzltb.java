package com.zhiren.dc.rulgl.rulmzlb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.Field;
import com.zhiren.dc.rulgl.meihyb.Meihybext;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rulmzltb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
		Meihybext.UpdateRulzlID(getRiqi(),visit.getDiancxxb_id());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
			
		}
		
		String chaxun = "select r.id,r.diancxxb_id,r.rulrq,r.fenxrq,rb.mingc as rulbzb_id,j.mingc as jizfzb_id,\n"
				+ "       r.qnet_ar,r.aar, r.ad,r.vdaf,r.mt,r.stad,r.aad,r.mad,r.qbad,r.had,r.vad,r.fcad,r.std,\n"
				+ "       r.qgrad,r.hdaf,r.sdaf,r.var,r.huayy,r.beiz,r.lury,r.lursj,r.shenhzt\n"
				+ "  from rulmzlb r, diancxxb d, rulbzb rb, jizfzb j\n"
				+ " where r.diancxxb_id = d.id(+)\n"
				+ "   and r.rulbzb_id = rb.id(+)\n"
				+ "   and r.jizfzb_id = j.id(+)\n"
				+ "   and r.rulrq = to_date('"+riqTiaoj+"','yyyy-mm-dd')\n" 
				+ "   and d.id="+visit.getDiancxxb_id()+"";

		;
	//System.out.println(chaxun);
	ResultSetList rsl = con.getResultSetList(chaxun);
    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
   	egu.setTableName("rulmzlb");
   	
   	egu.getColumn("diancxxb_id").setHeader("��λ");
	egu.getColumn("rulrq").setHeader("��¯����");
	egu.getColumn("rulrq").setEditor(null);
	egu.getColumn("fenxrq").setHeader("��������");
	egu.getColumn("diancxxb_id").setHidden(true);
	egu.getColumn("diancxxb_id").setEditor(null);
	egu.getColumn("rulbzb_id").setHeader("��¯����");
	egu.getColumn("jizfzb_id").setHeader("��¯����");
	egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
	egu.getColumn("aar").setHeader("Aar(%)");
	egu.getColumn("ad").setHeader("Ad(%)");
	egu.getColumn("vdaf").setHeader("Vdaf(%)");
	egu.getColumn("mt").setHeader("Mt(%)");
	egu.getColumn("stad").setHeader("St,ad(%)");
	egu.getColumn("aad").setHeader("Aad(%)");
	egu.getColumn("mad").setHeader("Mad(%)");
	egu.getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
	egu.getColumn("had").setHeader("Had(%)");
	egu.getColumn("vad").setHeader("Vad(%)");
	egu.getColumn("fcad").setHeader("Fcad(%)");
	egu.getColumn("std").setHeader("St,d(%)");
	egu.getColumn("qgrad").setHeader("Qgr,ad(Mj/kg)");
	egu.getColumn("hdaf").setHeader("Hdaf(%)");
	egu.getColumn("sdaf").setHeader("Sdaf(%)");
	egu.getColumn("var").setHeader("Var(%)");
	egu.getColumn("huayy").setHeader("����Ա");
	egu.getColumn("beiz").setHeader("��ע");
	egu.getColumn("lury").setHeader("¼��Ա");
	egu.getColumn("lury").setHidden(true);
	egu.getColumn("lury").setEditor(null);
	egu.getColumn("lursj").setHeader("¼��ʱ��");
	egu.getColumn("lursj").setHidden(true);
	egu.getColumn("lursj").setEditor(null);
	egu.getColumn("shenhzt").setHeader("״̬");
	egu.getColumn("shenhzt").setHidden(true);
	egu.getColumn("shenhzt").setEditor(null);
	
	egu.getColumn("rulrq").setWidth(85);
	egu.getColumn("fenxrq").setWidth(85);
	egu.getColumn("diancxxb_id").setWidth(85);
	egu.getColumn("rulbzb_id").setWidth(85);
	egu.getColumn("jizfzb_id").setWidth(85);
	
	egu.getColumn("qnet_ar").setWidth(60);
	egu.getColumn("aar").setWidth(60);
	egu.getColumn("ad").setWidth(60);
	egu.getColumn("vdaf").setWidth(60);
	egu.getColumn("mt").setWidth(60);
	egu.getColumn("stad").setWidth(60);
	egu.getColumn("aad").setWidth(60);
	egu.getColumn("mad").setWidth(60);
	egu.getColumn("qbad").setWidth(60);
	egu.getColumn("had").setWidth(60);
	egu.getColumn("vad").setWidth(60);
	egu.getColumn("fcad").setWidth(60);
	egu.getColumn("std").setWidth(60);
	egu.getColumn("qgrad").setWidth(60);
	egu.getColumn("hdaf").setWidth(60);
	egu.getColumn("sdaf").setWidth(60);
	egu.getColumn("var").setWidth(60);
	egu.getColumn("huayy").setWidth(60);
	
	
	
	
	
	egu.setGridType(ExtGridUtil.Gridstyle_Edit);//�趨grid���Ա༭
	egu.addPaging(25);//���÷�ҳ
	egu.setWidth(1000);//����ҳ��Ŀ��,������������ʱ��ʾ������
	//*****************************************����Ĭ��ֵ****************************
	egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
	egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
	egu.getColumn("fenxrq").setDefaultValue(DateUtil.FormatDate(new Date()));
	egu.getColumn("lury").setDefaultValue(visit.getRenymc());
	egu.getColumn("lursj").setDefaultValue(DateUtil.FormatDate(new Date()));
	egu.getColumn("shenhzt").setDefaultValue("0");
	
	
	//������������¯����
	ComboBox cb_banz=new ComboBox();
	egu.getColumn("rulbzb_id").setEditor(cb_banz);
	cb_banz.setEditable(true);
	String rulbzb_idSql="select r.id,r.mingc from rulbzb r where r.diancxxb_id="+visit.getDiancxxb_id()+" order by r.xuh";
	egu.getColumn("rulbzb_id").setComboEditor(egu.gridId, new IDropDownModel(rulbzb_idSql));
//	������������¯����
	ComboBox cb_jiz=new ComboBox();
	egu.getColumn("jizfzb_id").setEditor(cb_jiz);
	cb_jiz.setEditable(true);
	String cb_jizSql="select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and d.id="+visit.getDiancxxb_id()+"";
	egu.getColumn("jizfzb_id").setComboEditor(egu.gridId, new IDropDownModel(cb_jizSql));
	
	
//	 ������
	egu.addTbarText("����:");
	DateField df = new DateField();
	df.setValue(this.getRiqi());
	df.Binding("RIQI","forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
	egu.addToolbarItem(df.getScript());

	//************************************************************
	egu.addToolbarButton(GridButton.ButtonType_Insert, null);
	egu.addToolbarButton(GridButton.ButtonType_Delete, null);
	egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}
		
		getSelectData();
		
		
	}

//	���ڿؼ�
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(new Date());
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		
		
	}
}
