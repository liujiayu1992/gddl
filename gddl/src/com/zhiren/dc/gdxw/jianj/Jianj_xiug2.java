package com.zhiren.dc.gdxw.jianj;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.gdxw.jianj.gdxw_qcjj_hp;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;




public class Jianj_xiug2 extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // ��������
	
	public String getRiq() {
		return riq;
	}


	public void setRiq(String riq) {
		
		this.riq = riq;
		
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	

	
	
	private String Markmk = "true"; // ���ú��λ�������Ƿ�ѡ��
	
	public String getMarkmk() {
		return Markmk;
	}

	public void setMarkmk(String markmk) {
		Markmk = markmk;
	}
	
	
//	ú��λ������_��ʼ
	public IDropDownBean getMeikdwValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getMeikdwModel().getOptionCount() > 0) {
				setMeikdwValue((IDropDownBean) getMeikdwModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setMeikdwValue(IDropDownBean LeibValue) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(LeibValue);
	}

	public IPropertySelectionModel getMeikdwModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikdwModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikdwModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getMeikdwModels() {
		String sql = "select mk.id, mk.mingc from meikxxb mk where id in " +
				"(select distinct meikxxb_id  from fahb f where f.daohrq=date'"+this.getRiq()+"') order by mk.mingc";
		setMeikdwModel(new IDropDownModel(sql, "��ѡ��"));
	}
//	ú��λ������_����
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
			
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			
		}
		
		
		this.getSelectData();
	}
	
	
	
	
	public void save() {

	
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		StringBuffer sbsql = new StringBuffer("begin\n");
		String riq =this.getRiq();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			sbsql.append("update fahb set koud="+mdrsl.getString("koud")+"  where id="+mdrsl.getString("id")+";\n");
			
		}
				
		sbsql.append("end;");
		long zhuangt=0;
		if (sbsql.length()>15){
			zhuangt=con.getUpdate(sbsql.toString());
		}
		
		if(zhuangt>=0){
			this.setMsg("�޸ĳɹ�!");
		}else{
			this.setMsg("�޸�ʧ��!");
		}
		mdrsl.close();
		con.Close();
		
	
	}
	
	public void getSelectData() {
		

		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		String meikmc=this.getMeikdwValue().getValue();
		//System.out.println(meikmc);
		if(meikmc.equals("��ѡ��")){
			tiaoj="";
		}else{
			tiaoj= "and mk.mingc='"+meikmc+"'";
		}
			
		JDBCcon con = new JDBCcon();
		String sql ="select f.id,mk.mingc as meikxxb_id," +
				"f.daohrq,f.ches,f.maoz,f.piz,f.koud,f.jingz,decode(f.chex,0,'��','С��') as chex\n" +
			"from fahb f ,meikxxb mk\n" + 
			"where f.daohrq=date'"+this.getRiq()+"'\n" + 
			"and f.meikxxb_id=mk.id\n" + 
			"  "+tiaoj+""+
			" order by mk.mingc";


		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
//		 ����ҳ����
		egu.setWidth(Locale.Grid_DefaultWidth);
//		���ø�grid�����з�ҳ
		egu.addPaging(0);
		
		
		egu.getColumn("id").setEditor(null);
		
		
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("meikxxb_id").setEditor(null);
		
		
		
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("daohrq").setWidth(80);
		
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(80);
		
		egu.getColumn("maoz").setHeader("ë��");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("maoz").setWidth(60);
		
		egu.getColumn("piz").setHeader("Ƥ��");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("piz").setWidth(60);
		
		egu.getColumn("koud").setHeader("�۶�");
		egu.getColumn("koud").setWidth(60);
		
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("chex").setHeader("����");
		egu.getColumn("chex").setEditor(null);
		egu.getColumn("chex").setWidth(120);
		
		
	

		ComboBox cmc= new ComboBox();
		egu.getColumn("chex").setEditor(cmc); 
		cmc.setEditable(true);
		String mcSql="select 0 as id,'��' as chex from dual\n" +
			"union\n" + 
			"select 1 as id,'С��' as chex from dual";

		egu.getColumn("chex").setComboEditor(egu.gridId, new IDropDownModel(mcSql));
		
		
		egu.addTbarText("�������ڣ�");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.getElementById('Mark_mk').value = 'true';document.forms[0].submit();});");
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("ú��λ��");
		ComboBox comb = new ComboBox();
		comb.setWidth(120);
		comb.setListWidth(150);
		comb.setTransform("Meikdw");
		comb.setId("Meikdw");
		comb.setLazyRender(true);
		comb.setEditable(true);
		egu.addToolbarItem(comb.getScript());
		egu.addOtherScript("Meikdw.on('select',function(){document.getElementById('Mark_mk').value = 'false';document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("ˢ��", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		
		

	
	
		setExtGrid(egu);
		rsl.close();
		con.Close();
	
	}
	
	/**
	 * �����ҳ����ȡ����ֵΪNull���ǿմ�����ô�����ݿⱣ���ֶε�Ĭ��ֵ
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
			visit.setProSelectionModel3(null); // ú��λ������
			visit.setDropDownBean3(null);
			
			
		}
		if (getMarkmk().equals("true")) { // �ж����getMarkmk()����"true"����ô���³�ʼ��ú��λ������ͱ��������
			getMeikdwModels();
			
		}
		getSelectData();
	}
	
	
	
}