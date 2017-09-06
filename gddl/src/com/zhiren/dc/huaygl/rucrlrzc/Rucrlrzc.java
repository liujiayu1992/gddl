package com.zhiren.dc.huaygl.rucrlrzc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rucrlrzc extends BasePage implements PageValidateListener {
	
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

	// 页面变化记录
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
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
	}

	
	public void CreateData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String riq=this.getRiqi();
		//入厂化验必须终审
		String ruc = "select nvl(sum(round(f.laimsl,0)),0) as rucsl,\n"
				+ "nvl(round_new(decode(sum(f.laimsl),0,0,sum(f.laimsl*z.qnet_ar)/sum(f.laimsl)),2),0) as rucrl,\n"
				+ "nvl(round_new(decode(sum(f.laimsl),0,0,sum(f.laimsl*z.mt)/sum(f.laimsl)),1),0) as rucsf\n"
				+ "from fahb f ,zhilb z\n"
				+ "where f.daohrq=to_date('"+riq+"','yyyy-mm-dd')\n"
				+ "and f.zhilb_id=z.id(+)\n" 
				+ "and f.diancxxb_id="+this.getTreeid()+" \n"
				+ "and z.shenhzt=1";
		ResultSetList rsl = con.getResultSetList(ruc);
		double rucsl=0.0;
		double rucrl=0.0;
		double rucsf=0.0;
		if(rsl.next()){
			rucsl=rsl.getDouble("rucsl");
			rucrl=rsl.getDouble("rucrl");
			rucsf=rsl.getDouble("rucsf");
		}
		//入炉化验必须审核
		String rul = "select nvl(sum(hy.fadhy+hy.gongrhy),0) as rulsl,\n"
				+ " nvl(round_new(decode(sum(hy.fadhy+hy.gongrhy),0,0,sum((hy.fadhy+hy.gongrhy)*zl.qnet_ar)/sum(hy.fadhy+hy.gongrhy)),2),0) as rulrl,\n"
				+ " nvl(round_new(decode(sum(hy.fadhy+hy.gongrhy),0,0,sum((hy.fadhy+hy.gongrhy)*zl.mt)/sum(hy.fadhy+hy.gongrhy)),1),0) as rulsf\n"
				+ " from  meihyb hy,rulmzlb zl\n"
				+ " where hy.rulmzlb_id=zl.id(+)\n"
				+ " and hy.rulrq=to_date('"+riq+"','yyyy-mm-dd')\n"
				+ "and hy.diancxxb_id="+this.getTreeid()+"\n "
				+ " and zl.shenhzt=3";

		
		rsl = con.getResultSetList(rul);
		double rulsl=0.0;
		double rulrl=0.0;
		double rulsf=0.0;
		if(rsl.next()){
			rulsl=rsl.getDouble("rulsl");
			rulrl=rsl.getDouble("rulrl");
			rulsf=rsl.getDouble("rulsf");
		}
		
		double rezctzq=rucrl-rulrl;
		double rezctzh=rucrl-rulrl*(100-rucsf)/(100-rulsf);
		String insetRezcb="insert into rezcb (id,riq,diancxxb_id,rucsl,rucrl,rucsf,rulsl,rulrl,rulsf" +
				",rezctzq,rezctzh) values (" +
				""+MainGlobal.getNewID(visit.getDiancxxb_id())+"," +
				" to_date('"+riq+"','yyyy-mm-dd')," +
				" "+this.getTreeid()+"," +
				" "+rucsl+","+rucrl+","+rucsf+","+rulsl+","+rulrl+","+rulsf+","+rezctzq+",round_new("+rezctzh+",2))";
		String deleteRezc="delete rezcb r where r.riq=to_date('"+riq+"','yyyy-mm-dd') and diancxxb_id="+this.getTreeid()+"";
		int DeFlag=con.getDelete(deleteRezc);
		int flag=con.getInsert(insetRezcb);
		if(flag==-1){
			rsl.close();
			con.Close();
			WriteLog.writeErrorLog(this.getClass().getName() + 
					"\nSQL:" + insetRezcb +"更新rezcb表失败!");
			setMsg(this.getClass().getName() + ":更新rezcb表失败!");
			
		}else{
			rsl.close();
			con.Close();
			this.setMsg(riq+" 数据生成成功!");
		}
		
		
	}
	


	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID ="and dc.fuid="+ this.getTreeid();
			
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and dc.id="+this.getTreeid();
			 
		}
		
		String FirstDate=getRiqi();
		if(FirstDate==null||FirstDate.equals("")){
			FirstDate=DateUtil.FormatDate(new Date());
		}
		
		String chaxun =

			"select r.id,r.riq,dc.mingc as diancxxb_id,r.rucsl,r.rucrl,r.rucsf,r.rulsl,\n" +
			"  r.rulrl,r.rulsf,r.rezctzq,round_new(r.rezctzq*1000/4.1816,0) as rezctzqdk,\n" + 
			"  r.rezctzh,round_new(r.rezctzh*1000/4.1816,0) as rezctzhdk,\n" + 
			"  r.beiz,r.chaocfx,r.chaocfxry\n" + 
			" from rezcb  r,diancxxb dc\n" + 
			" where r.riq=to_date('"+FirstDate+"','yyyy-mm-dd')\n" + 
			" and r.diancxxb_id=dc.id\n" + 
			" "+strdiancTreeID+"";

 
		//System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("rucsl").setHeader("入厂数量<br>(吨)");
		egu.getColumn("rucrl").setHeader("入厂热量<br>(MJ/kg)");
		egu.getColumn("rucsf").setHeader("入厂水分<br>Mt(%)");
		egu.getColumn("rulsl").setHeader("入炉数量<br>(吨)");
		egu.getColumn("rulrl").setHeader("入炉热量<br>(MJ/kg)");
		egu.getColumn("rulsf").setHeader("入炉水分<br>Mt(%)");
		egu.getColumn("rezctzq").setHeader("水分调整前<br>热值差<br>(MJ/kg)");
		egu.getColumn("rezctzqdk").setHeader("水分调整前<br>前热值差<br>(kcal/kg)");
		egu.getColumn("rezctzh").setHeader("水分调整后<br>热值差<br>(MJ/kg)");
		egu.getColumn("rezctzhdk").setHeader("水分调整后<br>热值差<br>(kcal/kg)");
		egu.getColumn("beiz").setHeader("备注");
		
		egu.getColumn("rezctzqdk").setUpdate(false);
		egu.getColumn("rezctzhdk").setUpdate(false);
		
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setSortable(false);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rezctzq").setEditor(null);
		egu.getColumn("rezctzqdk").setEditor(null);
		egu.getColumn("rezctzh").setEditor(null);
		egu.getColumn("rezctzhdk").setEditor(null);
		
		egu.getColumn("chaocfx").setHidden(true);
		egu.getColumn("chaocfxry").setHidden(true);
		
		//设定列的初始宽度
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("rucsl").setWidth(60);
		egu.getColumn("rucrl").setWidth(65);
		egu.getColumn("rucsf").setWidth(65);
		egu.getColumn("rulsl").setWidth(65);
		egu.getColumn("rulrl").setWidth(65);
		egu.getColumn("rulsf").setWidth(65);
		egu.getColumn("rezctzq").setWidth(70);
		egu.getColumn("rezctzqdk").setWidth(70);
		egu.getColumn("rezctzh").setWidth(70);
		egu.getColumn("rezctzhdk").setWidth(70);
		egu.getColumn("beiz").setWidth(100);
		
		//设定列的小数位
		((NumberField)egu.getColumn("rucrl").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rucsf").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rulrl").editor).setDecimalPrecision(2);
		((NumberField)egu.getColumn("rulsf").editor).setDecimalPrecision(2);
		
//		设定不可编辑列的颜色
		egu.getColumn("rezctzq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzqdk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzh").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rezctzhdk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		
		egu.setTableName("rezcb");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(100);
		egu.setWidth(1000);
		egu.setDefaultsortable(false);//设定页面不自动排序
		egu.getColumn("riq").setDataType(GridColumn.DataType_Date);    
		
		
	
		
		// *****************************************设置默认值****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		//System.out.println(this.getRiqi());
		
		//*************************下拉框*****************************************88
		//电厂下拉框
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		
		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// 设置分隔符
		

		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
			//计算热值擦调整前和调整后
			sb.append("e.record.set('REZCTZQ',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))),2)  );");
			sb.append("e.record.set('REZCTZQDK',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL')))*1000/4.1816,0)  );");
			sb.append("e.record.set('REZCTZH',Round((parseFloat(e.record.get('RUCRL')==''?0:e.record.get('RUCRL'))-parseFloat(e.record.get('RULRL')==''?0:e.record.get('RULRL'))*(100-parseFloat(e.record.get('RUCSF')==''?0:e.record.get('RUCSF')))/(100-parseFloat(e.record.get('RULSF')==''?0:e.record.get('RULSF')))),2));");
			sb.append("e.record.set('REZCTZHDK',Round((parseFloat(e.record.get('REZCTZH')==''?0:e.record.get('REZCTZH'))*1000/4.1816),0)  );");
			
			
			
		sb.append("});");
//		设定第几行不可编辑
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//设定电厂列不可编辑
		sb.append("});");
		
		
		
		
		egu.addOtherScript(sb.toString());
		
		
		//---------------页面js计算结束--------------------------
		
		egu.addTbarText("-");
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");// 设置分隔符
//		 生成按钮
		GridButton gbc = new GridButton("生成",
				getBtnHandlerScript("CreateButton"));
		
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		egu.addTbarText("-");// 设置分隔符
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addToolbarItem("{"+new GridButton("超差分析","function(){ " +
				"if(gridDiv_sm.isSelected(7)){ " +
				"Ext.MessageBox.alert('提示','请选择具体日期行数据进行超差分析!'); " +
				"return;} " +
				"if(gridDiv_sm.hasSelection()){ " +
				" if(false){ " +
				" document.getElementById('DivMy_opinion').className = 'x-hidden';}" +
				"	window_panel.show(); " +
				"  rec = gridDiv_grid.getSelectionModel().getSelections(); " +
				" document.getElementById('My_opinion').value='';" +
				" document.getElementById('Histry_opinion').value='';" +
				" var strmyp=''; " +
				" for(var i=0;i<rec.length;i++){ " +
				" if(strmyp.substring(rec[i].get('CHAOCFX'))>-1){ " +
				" if(strmyp==''){ strmyp=rec[i].get('CHAOCFX');}else{ strmyp+=','+rec[i].get('CHAOCFX');}}" +
				" var strtmp=rec[i].get('CHAOCFXRY');" +
				" document.getElementById('Histry_opinion').value+=strtmp+'\\n';} document.getElementById('My_opinion').value=strmyp;"+
				" }else{ "+
				" 	Ext.MessageBox.alert('提示','请先选择具体日期行数据!');} "+
				"}").getScript()+"}");
		
		egu.addTbarText("-");// 设置分隔符
		
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
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setTreeid(null);
			this.setRiqi(null);
			this.setRiqi(DateUtil.FormatDate(new Date()));
			this.setMsg("");
			//getSelectData();
		}
		
			getSelectData();
		
	}


	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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

	//日期控件
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
	//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
	
//	超差分析
	public void setMy_opinion(String value){
		
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getMy_opinion(){
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	//超差分析人员
	public void setHistry_opinion(String value){
		
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public String getHistry_opinion(){
		return ((Visit) getPage().getVisit()).getString2();
	}
	

	public String getQuedButtonScript() {
		//为超超分析的确定按钮添加js处理
		return "for(i=0;i<=res.getCount()-1;i++){"
				+ " resa=res.getAt(i);"
				+ " resa.set('CHAOCFX',document.getElementById('My_opinion').value);"
				+ " resa.set('CHAOCFXRY',document.getElementById('Histry_opinion').value);"
				+ " }";
	}
	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = this.getRiqi();
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} 
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	  //frontpage里面计算汇总行加权热量的js备份 
	/*function countRucrl(){ 
		  var slsum = 0;
		  var  slrlsum = 0;
		  for(i=0;i<7;i++){
			  reci = gridDiv_ds.getAt(i);
			  sl =eval(reci.get('RUCSL')||0); 
			  rl = eval(reci.get('RUCRL')||0);
			  if(sl==0||rl==0){ 
				  continue; 
			  }
			  slrlsum += sl*rl;
			  slsum += sl; 
		  }
		  rectotal =gridDiv_ds.getAt(7); 
		  if(slsum != 0){ 
			  rectotal.set('RUCRL',Round(slrlsum /slsum,2) );
		  } 
	
	  
		  if(e.field=='RUCRL'){
			  countRucrl();
		  }
	 }*/
	 
	
}
