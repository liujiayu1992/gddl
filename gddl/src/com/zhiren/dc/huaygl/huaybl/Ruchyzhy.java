package com.zhiren.dc.huaygl.huaybl;

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
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruchyzhy extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}
	

	private String str = "";

	private boolean xiansztl = false;

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ�볧������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}
		
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '�볧������'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			str = rsl.getString("zhi");
		}
		sql = "select l.id,\n"
				+ "       to_char(z.bianm) as huaybh,\n"
				+ "       l.huaysj,\n"
				+ "       l.mt,\n"
				+ "       l.mad,\n"
				+ "       l.aad,\n"
				+ "       l.vad,\n"
				+ "       l.str,\n"
				+ "       l.qbad,\n"
				+ "       l.t1,\n"
				+ "       l.t2,\n"
				+ "       l.t3,\n"
				+ "       l.t4,\n"
				+ "       huayy,\n"
				+ "       l.lury,\n"
				+ "       l.beiz,\n"
				+ "       l.huaylb\n"
				+ "  from zhuanmb z,\n"
				+ "       zhillsb l,\n"
				+ "       caiyb c,\n"
				+ "       (select *\n"
				+ "          from zhuanmb\n"
				+ "         where zhuanmb.zhuanmlb_id =\n"
				+ "               (select id from zhuanmlb where mingc = '�������')) zm\n"
				+ " where z.zhillsb_id = l.id\n"
				+ "   and c.zhilb_id = l.zhilb_id\n"
				+ "   and zm.zhillsb_id = l.id\n"
				+ "   and z.zhuanmlb_id =\n"
				+ "       (select id\n"
				+ "          from zhuanmlb\n"
				+ "         where jib = (select nvl(max(jib), 0) from zhuanmlb))\n"
				+ "   and (shenhzt = -1)\n" 
				+ "   and c.caiyrq<=to_Date('"+getEndRiq()+"','yyyy-mm-dd')\n"
				+ "   and c.caiyrq>=to_Date('"+getBeginRiq()+"','yyyy-mm-dd')\n"
				+ " order by l.id, l.huaylb";
//		System.out.println(sql);
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //
	     
		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(990);
		// /������ʾ������
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("mt").setHeader("ȫˮ��Mt(%)");
		egu.getColumn("mad").setHeader("ˮ��Mad(%)");
		egu.getColumn("aad").setHeader("�ҷ�Aad(%)");
		egu.getColumn("vad").setHeader("�ӷ���Vad(%)");
		egu.getColumn("qbad").setHeader("��Ͳ����Qbad(Mj/kg)");
		egu.getColumn(str).setHeader("���" + str + "(%)");
		if(xiansztl){
			egu.getColumn(str).setHidden(true);
		}
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("huaylb").setHeader("�������");

		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
	
		egu.getColumn("mad").setWidth(80);
	
		egu.getColumn("aad").setWidth(80);
	
		egu.getColumn("vad").setWidth(80);
		
		egu.getColumn("qbad").setWidth(80);
	
		egu.getColumn(str).setWidth(80);
	
		egu.getColumn("t1").setWidth(80);
		
		egu.getColumn("t2").setWidth(80);
	
		egu.getColumn("t3").setWidth(80);
	
		egu.getColumn("t4").setWidth(80);
		
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		List l=egu.getGridColumns();
		for(int i=0;i<l.size();i++){
			((GridColumn)l.get(i)).setEditor(null);
		}
		//egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		// Toolbar tb1 = new Toolbar("tbdiv");
		// egu.setGridSelModel(3);
		egu.addTbarText("����ʱ��:");
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", null);// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("��");
		DateField df2 = new DateField();
		df2.setValue(getEndRiq());
		df2.Binding("EndRq", null);// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df2.getScript());
		GridButton refurbish = new GridButton("ˢ��",
		"function (){document.getElementById('RefurbishButton').click();}");
                   refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
               egu.addTbarBtn(refurbish);
               egu.addTbarText("-");
		egu.addToolbarButton("��ԭ", GridButton.ButtonType_SubmitSel,"HuanyButton");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
	     egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		setExtGrid(egu);
		con.Close();
	}

/*	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}*/

	/*private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}*/
   private void Huany(){
	   Visit visit=(Visit)this.getPage().getVisit();
	   Save3(getChange(),visit);
   }
	private int Judgment(String value) {
		int v = 0;
		if (value.equals(null) || value.equals("")) {
			v = 0;
		} else {
			v = Integer.parseInt(value);
		}
		return v;
	}

	
	public void Save3(String strchange, Visit visit){
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append("=0");
			sql.append("where id =").append(mdrsl.getString("ID"))
			.append(";\n");
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
	}

	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	private boolean _HuanyChick=false;
	public void  HuanyButton(IRequestCycle cycle){
			_HuanyChick=true;
	}

	public void submit(IRequestCycle cycle) {
	
	
		if (_RefurbishChick) {
			_RefurbishChick = false;
           
		}
		if(_HuanyChick){
			_HuanyChick=false;
			Huany();
			
		}

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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
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
			setBeginRiq(DateUtil.FormatDate(new Date()));
			setEndRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}
		getSelectData();
		
	}

}