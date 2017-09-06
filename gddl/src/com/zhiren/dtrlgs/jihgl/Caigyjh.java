package com.zhiren.dtrlgs.jihgl;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caigyjh extends BasePage {
//	 界面用户提示
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			ResultSetList rs = new JDBCcon()
//					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') time from dual");
					.getResultSetList("select to_char(sysdate,'yyyy-mm-dd') time from dual");
			rs.next();
			riq = rs.getString("time");
		}
		return riq;
	}
	
	
	public List gridColumns;
	private String tableName="";
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	// 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// 按钮
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle){
		_SaveChick = true;
	}
	

	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}
	
	private void save(){
		Visit visit = (Visit) this.getPage().getVisit();
	 	
	    JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
				String id = delrsl.getString("id");
				sql.append("delete caigyjhb where id=" + id + "; ");
		}
		
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("ID"))) {
				String id="select getnewid("+visit.getDiancxxb_id()+") id from dual ";
				ResultSetList rslid = con.getResultSetList(id);
				rslid.next();
				sql.append("insert into caigyjhb(id,diancxxb_id,riq,pinzb_id,gongysb_id,meikxxb_id,meil,farl,meij,meijs,yunf,yunfs,biaomdj,beiz,lurry,lursj) ");
				
//				if(getTreeJib()==1){
//					sql.append("	values("+mdrsl.getString("diancxxb_id")+",");
//				}else{
					sql.append("	values("+rslid.getString("id")+","+(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl
							.getString("diancxxb_id"))+",");
//				}		
				sql.append("			trunc(to_date('" + mdrsl.getString("riq") + "','yyyy-mm-dd'),'mm'),");//日期
				sql.append("			"+(getExtGrid().getColumn("pinzb_id").combo).getBeanId(mdrsl
														.getString("pinzb_id"))+",");//品种
				sql.append("			"+(getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl
														.getString("gongysb_id"))+",");		//供应商		
				sql.append("			"+(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl
														.getString("meikxxb_id"))+",");//煤矿
				sql.append("			round("+mdrsl.getString("meil")+",2),");
				sql.append("			round("+mdrsl.getString("farl")+",3),");
				sql.append("			round("+mdrsl.getString("meij")+",2),");
				sql.append("			round("+mdrsl.getString("meijs")+",2),");
				sql.append("			round("+mdrsl.getString("yunf")+",2),");
				sql.append("			round("+mdrsl.getString("yunfs")+",2),");
				sql.append("			round("+mdrsl.getString("biaomdj")+",2),");
				sql.append("			'"+mdrsl.getString("beiz")+"',");
				sql.append("			'"+visit.getRenyID()+"',");
				sql.append("			sysdate);");
				
			} else {
				String id = mdrsl.getString("id");
				sql.append("update caigyjhb set ");
				sql.append("diancxxb_id="+(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(mdrsl
						.getString("diancxxb_id")));
				sql.append(",riq=trunc(to_date('" + mdrsl.getString("riq") + "','yyyy-mm-dd'),'mm')");
				sql.append(",pinzb_id="+(getExtGrid().getColumn("pinzb_id").combo).getBeanId(mdrsl
						.getString("pinzb_id")));
				sql.append(",gongysb_id="+(getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl
						.getString("gongysb_id")));
				sql.append(",meikxxb_id="+(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl
						.getString("meikxxb_id")));
				sql.append(",meil=round("+mdrsl.getString("meil")+",2)");
				sql.append(",farl=round("+mdrsl.getString("farl")+",3)");
				sql.append(",meij=round("+mdrsl.getString("meij")+",2)");
				sql.append(",meijs=round("+mdrsl.getString("meijs")+",2)");
				sql.append(",yunf=round("+mdrsl.getString("yunf")+",2)");
				sql.append(",yunfs=round("+mdrsl.getString("yunfs")+",2)");
				sql.append(",biaomdj=round("+mdrsl.getString("biaomdj")+",2)");
				sql.append(",beiz='"+mdrsl.getString("beiz")+"' ");				
				sql.append("where id=" + id+";");
				
			}
		}
//		System.out.println(sql);
		mdrsl.close();
		sql.append("end;");
		int flag= con.getUpdate(sql.toString());
		if(flag!=-1){
	 		setMsg(ErrorMessage.SaveSuccessMessage);
	 	}else{
			setMsg("保存失败！");
	 	}
		con.Close();
	}
	
	
	private void getSelectData(){
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();

		sql.append("select c.id,d.mingc diancxxb_id,c.riq,p.mingc pinzb_id,g.mingc gongysb_id,m.mingc meikxxb_id,c.meil, \n");
		sql.append(" c.farl,c.meij,c.meijs,c.yunf,c.yunfs,c.biaomdj,c.beiz  \n");
		sql.append(" from caigyjhb c,diancxxb d,pinzb p,gongysb g,meikxxb m ");
		sql.append(" where c.diancxxb_id=d.id and c.pinzb_id=p.id and c.gongysb_id=g.id and c.meikxxb_id=m.id");
		sql.append("	     and c.riq = to_date('" +getNianSelectValue()+"-"+ getYueSelectValue()+"-"+1+"','yyyy-mm-dd')");
		if(getTreeJib()==1){
			sql.append("	and d.jib=3 ");
		}else if(getTreeJib()==2){
			sql.append("	and d.fuid=" + getTreeid());
		}else{
			sql.append("	and d.id=" + getTreeid());
		}
		sql.append("  order by c.id");
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		setTableName("caigyjhb");
		
		egu.getColumn("diancxxb_id").setHeader("存煤地点");
		egu.getColumn("diancxxb_id").setWidth(90);
		String diancxxbSql = "";// where jieb=3 and cangkb_id<>1
		ComboBox c1 = new ComboBox();
		c1.setEditable(true);
		if(getTreeJib()==1){
			diancxxbSql = "select id,mingc from diancxxb where jib=3 order by mingc asc";
		}else if(getTreeJib()==2){
			diancxxbSql = "select id,mingc from diancxxb where fuid=" + getTreeid() + " order by mingc asc";
		}else{
			diancxxbSql = "select id,mingc from diancxxb where id=" + getTreeid() + " order by mingc asc";
//			c1.setId("0");
			egu.getColumn("diancxxb_id").setDefaultValue(getTreeValue());
			egu.getColumn("diancxxb_id").returnId=true;
		}
		egu.getColumn("diancxxb_id").setEditor(c1);
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(diancxxbSql));
		
		String date=DateUtil.FormatDate(DateUtil.getDate(getNianSelectValue().toString()+"-"+getYueSelectValue()+"-"+1));
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("riq").setDefaultValue(date);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("pinzb_id").setHeader("品种");
		String pinzbSql = " select id,mingc from pinzb where leib='煤'order by mingc asc";
		ComboBox c2 = new ComboBox();
		c2.setEditable(true);
		egu.getColumn("pinzb_id").setEditor(c2);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzbSql));
		egu.getColumn("pinzb_id").setWidth(70);
		
		egu.getColumn("gongysb_id").setHeader("供应商");
		String gongysbSql = "select id,mingc from gongysb order by mingc asc";
		ComboBox c3 = new ComboBox();
		c3.setEditable(true);
		egu.getColumn("gongysb_id").setEditor(c3);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(gongysbSql));
		egu.getColumn("gongysb_id").setWidth(80);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		String feiylbSql = "select id,mingc from meikxxb order by mingc asc";
		ComboBox c4 = new ComboBox();
		c4.setEditable(true);
		egu.getColumn("meikxxb_id").setEditor(c4);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(feiylbSql));
		egu.getColumn("meikxxb_id").setWidth(80);
		
		egu.getColumn("meil").setHeader("煤量(吨)");
		egu.getColumn("meil").setWidth(60);
		egu.getColumn("meil").setDefaultValue("0");
		((NumberField)egu.getColumn("meil").editor).setDecimalPrecision(2);
		
		egu.getColumn("farl").setHeader("发热量(MJ/kg)");
		egu.getColumn("farl").setWidth(80);
		egu.getColumn("farl").setDefaultValue("0");
		((NumberField)egu.getColumn("farl").editor).setDecimalPrecision(3);
		
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setDefaultValue("0");
		egu.getColumn("meij").setWidth(60);
		((NumberField)egu.getColumn("meij").editor).setDecimalPrecision(2);
		
		egu.getColumn("meijs").setHeader("煤税");
		egu.getColumn("meijs").setDefaultValue("0");
		egu.getColumn("meijs").setWidth(60);
		((NumberField)egu.getColumn("meijs").editor).setDecimalPrecision(2);
		
		egu.getColumn("yunf").setHeader("运费");
		egu.getColumn("yunf").setDefaultValue("0");
		egu.getColumn("yunf").setWidth(60);
		((NumberField)egu.getColumn("yunf").editor).setDecimalPrecision(2);
		
		egu.getColumn("yunfs").setHeader("运费税");
		egu.getColumn("yunfs").setDefaultValue("0");
		egu.getColumn("yunfs").setWidth(60);
		((NumberField)egu.getColumn("yunfs").editor).setDecimalPrecision(2);
		
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setDefaultValue("0");
		egu.getColumn("biaomdj").setWidth(60);
		((NumberField)egu.getColumn("biaomdj").editor).setDecimalPrecision(2);
		//egu.getColumn("biaomdj").setEditor(null);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(150);		
		
		
//工具栏	
//		 树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
											ExtTreeUtil.treeWindowType_Dianc,
											((Visit) this.getPage()	.getVisit()).getDiancxxb_id(), 
											getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText(" ");
		egu.addTbarText("-");// 设置分隔符
		
		//年
		egu.addTbarText("年:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NianDropDown");
		comb1.setId("Nian");
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(55);
		comb1.setListWidth(58);
		egu.addToolbarItem(comb1.getScript());
		egu.addOtherScript("Nian.on('select',function(){document.forms[0].submit();});");//动态刷新
		
		egu.addTbarText("月:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YueDropDown");
		comb2.setId("Yue");
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(45);
		comb2.setListWidth(48);
		egu.addToolbarItem(comb2.getScript());
		egu.addOtherScript("Yue.on('select',function(){document.forms[0].submit();});");//动态刷新
		egu.addTbarText("-");// 设置分隔符
		
		egu.addPaging(25);//设置一页多少行
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");	
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);//解决宽度问题
		
		StringBuffer script = new StringBuffer();
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("var record = gridDiv_ds.getAt(e.row);\n")
				.append("var meij = eval(record.get('MEIJ')||0);\n")
				.append("var farl = eval(record.get('FARL')||0);\n")
				.append("if(e.field=='MEIJ' || e.field=='FARL'){")
				.append("var biaomdj = meij * 29.271 / farl;\n")
				.append("record.set('BIAOMDJ',biaomdj);}\n")
				.append("});");
		egu.addOtherScript(script.toString());
		
		setExtGrid(egu);		
		con.Close();
		
	}
	
	//年
	public IDropDownBean getNianSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getNianSelectModel()
							.getOption(DateUtil.getYear(new Date())-2007));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	public void setNianSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}
	public void setNianSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	public IPropertySelectionModel getNianSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getNianSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	public void getNianSelectModels() {
				StringBuffer sql=new StringBuffer();
		int i=0;
		for(i=2007;i<DateUtil.getYear(new Date())+2;i++){
			sql.append("select " + i + " id," + i + " mingc from dual union all ");
		}
		sql.append("select " + i + " id," + i + " mingc from dual ");
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql));
	}
	
//	月
	public IDropDownBean getYueSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {

				int _yuef = DateUtil.getMonth(new Date());
				for (int i = 0; i < getYueSelectModel().getOptionCount(); i++) {
					Object obj = getYueSelectModel().getOption(i);
					if (_yuef == ((IDropDownBean) obj).getId()) {
						((Visit) getPage().getVisit())
						.setDropDownBean3((IDropDownBean) obj);
						break;
					}
				}
			
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setYueSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}
	public void setYueSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	public IPropertySelectionModel getYueSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getYueSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	public void getYueSelectModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(listYuef));
	}
	
	
	//树
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
	
	private int getTreeJib(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select jib from diancxxb where id="+getTreeid());
		rsl.next();
		con.Close();
		return rsl.getInt("jib");
	}
	
	private String getTreeValue(){
		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select mingc from diancxxb where id="+getTreeid());
		rsl.next();
		con.Close();
		return rsl.getString("mingc");
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setNianSelectValue(null);
			setNianSelectModel(null);
			setYueSelectValue(null);
			setYueSelectModel(null);
			getNianSelectModels();
			getYueSelectModels();
		}
		
		
		init();
	}

	private void init() {
		getSelectData();
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
}
