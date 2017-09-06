package com.zhiren.shanxdted.meicrzc;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
 
public abstract class Meicrzcwh extends BasePage {
//	界面用户提示
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
	}
//	绑定日期
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}

	private void CreateData() {
		String diancxxb_id = getTreeid();
		String riq=DateUtil.FormatOracleDate(this.getRiq());
		JDBCcon con = new JDBCcon();
		String sql=
			"INSERT INTO meicrzc\n" +
			"  (ID, riq, diancxxb_id, rucml, rucrz, rucmt, rucst, rulml, rulrz, rulmt, rulst)\n" + 
			"(SELECT getnewid("+diancxxb_id+") ID,"+riq+","+diancxxb_id+",nvl(jingz,0),nvl(QNET_AR,0),nvl(MT,0),nvl(STAD,0),0,0,0,0\n" + 
			"FROM(\n" + 
			"SELECT SUM(F.JINGZ) JINGZ,\n" + 
			"       DECODE(SUM(F.JINGZ), 0, 0, SUM(F.JINGZ * Z.QNET_AR) / SUM(F.JINGZ)) QNET_AR,\n" + 
			"       DECODE(SUM(F.JINGZ), 0, 0, SUM(F.JINGZ * Z.MT) / SUM(F.JINGZ)) MT,\n" + 
			"       DECODE(SUM(F.JINGZ), 0, 0, SUM(F.JINGZ * Z.STAD) / SUM(F.JINGZ)) STAD\n" + 
			"  FROM FAHB F, ZHILB Z\n" + 
			" WHERE F.ZHILB_ID = Z.ID\n" + 
			"   AND F.DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"   AND F.DAOHRQ = "+riq+")sr)";
		int flag=con.getInsert(sql);
		if(flag>-1){
			setMsg("数据已生成！");
		}else{
			setMsg("数据生成时出现异常！");
		}
		con.Close();
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sb.append("begin\n");
		sb.append("delete from meicrzc where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
		sb.append("end;");
		int flag=con.getUpdate(sb.toString());
		if(flag>-1){
			setMsg("数据已删除！");
		}else{
			setMsg("数据删除时出现异常！");
		}
		con.Close();
	}
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();

		String sql=
			"SELECT MC.ID,\n" +
			"       MC.RIQ,\n" + 
			"       DC.MINGC DIANCXXB_ID,\n" + 
			"       MC.RUCML,\n" + 
			"       MC.RUCRZ,\n" + 
			"       MC.RUCMT,\n" + 
			"       MC.RUCST,\n" + 
			"       MC.RULML,\n" + 
			"       MC.RULRZ,\n" + 
			"       MC.RULMT,\n" + 
			"       MC.RULST\n" + 
			"  FROM MEICRZC MC, DIANCXXB DC\n" + 
			" WHERE MC.DIANCXXB_ID = DC.ID\n" + 
			"   AND DC.ID = "+diancxxb_id+"\n" + 
			"   AND RIQ = "+CurDate;
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("MEICRZC");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("RIQ").setHidden(true);
		egu.getColumn("RIQ").setEditor(null);
		egu.getColumn("RIQ").setUpdate(false);
		
		egu.getColumn("DIANCXXB_ID").setHeader("单位名称");
		egu.getColumn("DIANCXXB_ID").setEditor(null);
		egu.getColumn("DIANCXXB_ID").setWidth(120);
		egu.getColumn("DIANCXXB_ID").setUpdate(false);
		
		egu.getColumn("RUCML").setHeader("入厂煤量<br>(吨)");
		egu.getColumn("RUCML").setWidth(100);
		((NumberField)egu.getColumn("RUCML").editor).setDecimalPrecision(2);
		
		egu.getColumn("RUCRZ").setHeader("入厂热值<br>(MJ/Kg)");
		egu.getColumn("RUCRZ").setWidth(100);
		egu.getColumn("RUCRZ").editor.setMaxValue("29.271");
		((NumberField)egu.getColumn("RUCRZ").editor).setDecimalPrecision(2);
		
		egu.getColumn("RUCMT").setHeader("入厂水份<br>(%)");
		egu.getColumn("RUCMT").setWidth(100);
		egu.getColumn("RUCMT").editor.setMaxValue("100");
		((NumberField)egu.getColumn("RUCMT").editor).setDecimalPrecision(2);
		
		egu.getColumn("RUCST").setHeader("入厂硫份<br>(%)");
		egu.getColumn("RUCST").setWidth(100);
		egu.getColumn("RUCST").editor.setMaxValue("100");
		((NumberField)egu.getColumn("RUCST").editor).setDecimalPrecision(2);
		
		egu.getColumn("RULML").setHeader("入炉煤量<br>(吨)");
		egu.getColumn("RULML").setWidth(100);
		((NumberField)egu.getColumn("RULML").editor).setDecimalPrecision(2);
		
		egu.getColumn("RULRZ").setHeader("入炉热值<br>(MJ/Kg)");
		egu.getColumn("RULRZ").setWidth(100);
		egu.getColumn("RULRZ").editor.setMaxValue("29.271");
		((NumberField)egu.getColumn("RULRZ").editor).setDecimalPrecision(2);
		
		egu.getColumn("RULMT").setHeader("入炉水份<br>(%)");
		egu.getColumn("RULMT").setWidth(100);
		egu.getColumn("RULMT").editor.setMaxValue("100");
		((NumberField)egu.getColumn("RULMT").editor).setDecimalPrecision(2);
		
		egu.getColumn("RULST").setHeader("入炉硫份<br>(%)");
		egu.getColumn("RULST").setWidth(100);
		egu.getColumn("RULST").editor.setMaxValue("100");
		((NumberField)egu.getColumn("RULST").editor).setDecimalPrecision(2);

		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('RIQ').value+'的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		生成按钮
		if(visit.isFencb() && isParentDc(con)){
			
		}else{
			if(!con.getHasIt(sql)){
				GridButton gbc = new GridButton("生成","function (){document.getElementById('CreateButton').click();}");
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
			}else{
//				删除按钮
				GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
				gbd.setIcon(SysConstant.Btn_Icon_Delete);
				egu.addTbarBtn(gbd);
//				
				GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
			}
		}

		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
//		if(btnName.endsWith("CreateButton")) {
//			btnsb.append("新生成数据将同时覆盖:<br>")
//			.append(cnDate).append("的已存数据，是否继续？");
//		}else {
			btnsb.append("是否删除全部数据？");
//		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			setTreeid(null);
			getSelectData();
		}
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}
