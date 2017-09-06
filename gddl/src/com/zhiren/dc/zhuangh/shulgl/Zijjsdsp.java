package com.zhiren.dc.zhuangh.shulgl;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zijjsdsp extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

//	 绑定日期
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			setRiqi(DateUtil.FormatDate(new Date()));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
	
	private String Changezid;

	public String getChangezid() {
		return Changezid;
	}

	public void setChangezid(String changezid) {
		Changezid = changezid;
	}
	private String riq2;
	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	public void setRiq2(String riq2) {
		this.riq2 = riq2;
	}

    //页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _Save1Chick = false;
	public void Save1Button(IRequestCycle cycle) {
		_Save1Chick = true;
	}
	
	private boolean _MeicChick = false;
	public void MeicButton(IRequestCycle cycle) {
		_MeicChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			
			Save(cycle);
		}
		
		if (_Save1Chick) {
			_Save1Chick = false;
			
			Save1();
		}
	}
	public void Meic(IRequestCycle cycle) {
		
Visit visit = (Visit) this.getPage().getVisit();
visit.setLong1(Integer.parseInt(getChangeid()));
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}
		
		while (rsl.next()) {
			
		}
		//宣威需要查看时绑定日期
//		visit.setString15(this.getRiqi());
//		cycle.activate("Shujdr");
	}
	//取得最大的采样XUH
	public String creatcaiybm(JDBCcon con,String date,long diancxxb_id) {
		String sql="select To_number(Substr(max(bianm), 9, 2))+1 as xuh from caiyb " +
				"where caiyrq=to_date('"+date+"','yyyy-mm-dd') and zhilb_id="+diancxxb_id+"";
		
		String xuh="01";
		ResultSetList rsl = con.getResultSetList(sql);
		
		while(rsl.next()){
			int xuh1=rsl.getInt("xuh");
			if (xuh1<10){
				xuh="0"+xuh1;
			}
		}
		rsl.close();
		
		return xuh;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//保存方法
	public void Save1() {
		

		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){

			//发货表
			String cc ="delete from zijsyspb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(cc.toString());
			
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}
		
		

		while (rsl.next()) {
			//fahb
			String shenqbm="";
			if("".equals(rsl.getString("shenqbm"))||rsl.getString("shenqbm")==null){
			
			}else{
				shenqbm=rsl.getString("shenqbm");
				
			}
			String shoukdw="";
			if("".equals(rsl.getString("shoukdw"))||rsl.getString("shoukdw")==null){
			}else{
				shoukdw=rsl.getString("shoukdw");
				
			}
			String fukfs="";
			if("".equals(rsl.getString("fukfs"))||rsl.getString("fukfs")==null){
			}else{
				fukfs=rsl.getString("fukfs");
				
			}
			String shenqzjdx="";
			if("".equals(rsl.getString("shenqzjdx"))||rsl.getString("shenqzjdx")==null){
			}else{
				shenqzjdx=rsl.getString("shenqzjdx");
				
			}
			
			String shenqzjxx="0";
			if("".equals(rsl.getString("shenqzjxx"))||rsl.getString("shenqzjxx")==null){
			}else{
				shenqzjxx=rsl.getString("shenqzjxx");
				
			}
						
			if ("0".equals(rsl.getString("id"))) {
				String id = MainGlobal.getNewID(visit.getDiancxxb_id());
				sb.append(" insert into zijsyspb (id,DIANCXXB_ID,shenqbm,shoukdw,shenqzjxx,fukfs,shenqzjdx,shenqrq,shiyzjjjnr)" +
						" values ("+id+","+visit.getDiancxxb_id()+",'"+shenqbm+"','"+shoukdw+"',"+shenqzjxx+",'"+fukfs+"','"
						+shenqzjdx+"',to_date('"+rsl.getString("shenqrq")+"','yyyy-mm-dd'),'"+rsl.getString("shiyzjjjnr")+"')")
				.append(";\n");
				
				
			}else{
                 sb.append(" update zijsyspb set shenqbm='"+shenqbm+"',shoukdw='"+shoukdw+"',shenqzjxx="+shenqzjxx+
                		 ",fukfs='"+fukfs+"',shenqzjdx='"+shenqzjdx+"',shenqrq=to_date('"+rsl.getString("shenqrq")+"','yyyy-mm-dd'),shiyzjjjnr='"+rsl.getString("shiyzjjjnr")+"'")
                		 .append(" where id="+rsl.getString("id")).append(";\n");
				
			}
			
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);

	
		
	
		
	
	}
	//配置煤场
	public void Save(IRequestCycle cycle) {
		
		Visit visit = (Visit) this.getPage().getVisit();
		

		
		String endriq=getRiq2();
		

		

		visit.SetStringBuffer2(new StringBuffer(getChangeid()));

		



		visit.SetStringBuffer20(new StringBuffer("fei"));
		cycle.activate("Zijjsdspcx_zh");
	}

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String beginriq=getRiqi();
		String endriq=getRiq2();
		
		StringBuffer sbsql = new StringBuffer("");
		

		sbsql.append(" select id,shenqbm,shoukdw,fukfs,shenqzjxx,shenqzjdx,shenqrq,shiyzjjjnr from zijsyspb " +
				" where shenqrq>=to_date('"+getRiqi()+"','yyyy-mm-dd') and shenqrq<=to_date('"+getRiq2()+"','yyyy-mm-dd')  order by id");

		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
//		egu.set
		egu.setDefaultsortable(false);
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("shenqrq").setHeader("申请日期");
		egu.getColumn("shenqrq").setWidth(80);
		
		egu.getColumn("shenqbm").setHeader("申请部门");
		egu.getColumn("shenqbm").setWidth(200);
		egu.getColumn("shoukdw").setHeader("收款单位");
		egu.getColumn("shoukdw").setWidth(200);
		egu.getColumn("fukfs").setHeader("付款方式");
		egu.getColumn("fukfs").setWidth(70);
		egu.getColumn("shenqzjxx").setHeader("申请资金（小写）");
		egu.getColumn("shenqzjxx").setWidth(120);
		egu.getColumn("shenqzjdx").setHeader("申请资金（大写）");
		egu.getColumn("shenqzjdx").setWidth(350);
		egu.getColumn("shenqzjdx").setEditor(null);
		
		egu.getColumn("shiyzjjjnr").setHeader("使用资金经济内容或经济事项");
		egu.getColumn("shiyzjjjnr").setWidth(500);
		
//		egu.getColumn("shiyzjjjnr").set;
		egu.getColumn("shiyzjjjnr").setEditor(null);
		
		ComboBox c1= new ComboBox();
		egu.getColumn("fukfs").setEditor(c1);
		c1.setEditable(true);
		String ydSql="select 0 as id,'现金' as mingc from dual union select 1 as id,'支票' as mingc from dual union " +
				" select 2 as id,'电汇' as mingc from dual union  select 3 as id,'汇票' as mingc from dual  ";
		egu.getColumn("fukfs").setComboEditor(egu.gridId, new IDropDownModel(ydSql));
		
		

 
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		

		

		egu.addTbarText("申请日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
//		
//		
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		String shez="";//设置记录结果
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "Save1Button");//,condition
		GridButton gb = new GridButton("打印", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"//zhillsb_id
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id; }"
				+
				"   var gridDivsave_history = '';\n" +
				"   var Mrcd = gridDiv_ds.getModifiedRecords();\n" + 
				"  for(i = 0; i< Mrcd.length; i++){\n" + 
				"    if(typeof(gridDiv_save)=='function'){\n" + 
				"    var revalue = gridDiv_save(Mrcd[i]);\n" + 
				"    if(revalue=='return'){\n" + 
				"    return;\n" + 
				"    }else if(revalue=='continue'){continue;}\n" + 
				"    }\n" + 

				"    gridDivsave_history += '<result>' + '<sign>U</sign>' +\n" + 
				"     '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" + 
				"     + '</result>' ;\n" + 
				"     }\n" 
				+ 
				"    if(gridDiv_history=='' && gridDivsave_history=='' && rec == null){\n" + 
				"    Ext.MessageBox.alert('提示信息','请选择要打印的数据');\n" + 
				"\t\t\t}else{\n" + 
				"\t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
				"\t\t\t\tCobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" + 
				"\t\t\t\tdocument.getElementById('SaveButton').click();\n" + 
				"\t\t\t\tExt.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" + 
				"\t}"

				+ " ");
		egu.addTbarBtn(gb);
		
		egu.addTbarText("-");
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
		sb
		.append("if((e.row>=0" +
				") && e.column>5)" +
				"{ " +//shenqbm,shoukdw,fukfs,shenqzjxx,shenqzjdx
		"var ches = convertCurrency(eval(gridDiv_ds.getAt(e.row).get('SHENQZJXX')))" +
		"" +
		"" +
		"" +
		";"+
		"gridDiv_ds.getAt(e.row).set('SHENQZJDX',ches);" +
		"}");
		

		
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
				+ "row = irow; \n"
				+ "if('SHIYZJJJNR' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+
				"Ext.MessageBox.show({\n" +
				"                title:'多行输入框',\n" + 
				"                msg:'可以输入多行:',\n" + 
				"                width:500, \n" + 
				"                buttons:Ext.MessageBox.OKCANCEL,\n" + 
				"                multiline:true,\n" + 
				"               fn:function(btn,text){\n" + 
				" var rec = gridDiv_grid.getSelectionModel().getSelected(); "+
				"                 if(btn=='ok'){  rec.set('SHIYZJJJNR',text); }else{ } \n" + 
				"                }\n" + 
				"            });"+
						"}" 
				
				+
						"});\n");

		sb.append("});");
		egu.addOtherScript(sb.toString());
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// GridselModel_Check
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.setDefaultsortable(false);
		setExtGrid(egu);
		con.Close();

	}

	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		导航栏树的查询SQL
		String sql=
			"select 0 id,'根' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select id,mingc as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from meicangb \n" + 
			" where\n" + 
			"diancxxb_id="+visit.getDiancxxb_id()+"\n" + ""
			;
			
		TreeOperation dt = new TreeOperation();
//		System.out.println(sql);
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setString1("");	//传合同id	
//			setRiqi(DateUtil.FormatDate(new Date()));
//			setRiq2(DateUtil.FormatDate(new Date()));
			initNavigation();
			getSelectData();
		}
	}
}
