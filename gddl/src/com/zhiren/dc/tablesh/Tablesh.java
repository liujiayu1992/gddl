package com.zhiren.dc.tablesh;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Liucdzcl;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.menu.Menu;
import com.zhiren.common.ext.menu.TextItem;
import com.zhiren.jt.het.shenhrz.Yijbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-01-17
 * 内容:表数据流程审核
 */
public class Tablesh extends BasePage implements PageValidateListener {
	
	private final static String TableIdChar="id#";//sql中 table id要替换的特殊字符
	private final static int TableIdCharLength=3;//sql中 table id 要替换的特殊字符长度
	
	private final static String DcIdChar="dc#";//sql中diancxxb id 要替换的特殊字符
	private final static int DcIdCharLength=3;//sql中diancxxb id要替换的特殊字符长度
	
	private String msg = "";

	public String getMsg() {
		return msg; 
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String MenuId;

	public String getMenuId() {
		return MenuId;
	}

	public void setMenuId(String menuId) {
		MenuId = menuId;
	}
	
	private String Histry_opinion;
	
	public String getHistry_opinion() {
		return Histry_opinion;
	}

	public void setHistry_opinion(String histry_opinion) {
		Histry_opinion = histry_opinion;
	}
	
	private String OpenWindow;

	public String getOpenWindow() {
		return OpenWindow;
	}

	public void setOpenWindow(String openWindow) {
		OpenWindow = openWindow;
	}
	
	//进入流程操作的表名称
	private void setListTableName(List list){
		((Visit)this.getPage().getVisit()).setList7(list);
	}
	
	private List getListTableName(){
		return ((Visit)this.getPage().getVisit()).getList7();
	}
	
	//进入流程操作的表id
	private void setListTableID(List list){
		((Visit)this.getPage().getVisit()).setList8(list);
	}
	
	private List getListTableID(){
		return ((Visit)this.getPage().getVisit()).getList8();
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		this.setListTableID(new ArrayList());
		this.setListTableName(new ArrayList());
		
		String liuxmc="";
		try {
			 liuxmc=MainGlobal.getTableCol("liucdzb", "mingc", "id="+this.getMenuId());//流程流向   提交 还是 回退
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
		String[] records=this.getChange().split("&");//页面选择的记录条数
		
////		 化验是否使用工作流程
//		boolean isLiuc = false;
//		isLiuc = "是".equals(MainGlobal.getXitxx_item("化验", "是否使用工作流程", visit
//				.getDiancxxb_id()
//				+ "", "否"));
//		
//		if (isLiuc) {
//			if (records.length > 1) {
//				this.setMsg("一次只能提交一条数据！");
//				return;
//			}
//		}
		
		for(int i=0;i<records.length;i++){
			String[] record=records[i].split(";");//每条记录 可能有多个相关的表 需要进入流程
			
			if(record.length>1){
				List listName=new ArrayList();
				List listID=new ArrayList();
				
				for(int j=0;j<record.length;j++){//得到每张具体表的名称 和 id 字段
					String[] rec=record[j].split(",");
					String tableName=rec[0];
					String tableId=(rec.length<2)?"-1":rec[1];//id 为空时  以 -1代替
					
					listName.add(tableName);
					listID.add(tableId);
			    }
				
				this.getListTableName().add(listName);//表名  和  表 id  依依对应 存入 list中
				this.getListTableID().add(listID);
			}else{//if
				String tableName=record[0].split(",")[0];
				String tableId=record[0].split(",")[1];
				
				this.getListTableName().add(tableName);
				this.getListTableID().add(tableId);
			}
			
		}//for
		
		String TableNameIdStr=Liucdzcl.TableNameIdStr(this.getListTableName(), this.getListTableID());
//		this.OpenWindow=Liucdzcl.getOpenWinScr(this.MenuId, this.getListTableName(), this.getListTableID());
		String sql = "select dongz from liucdzb where id = " + this.MenuId;
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		String dongz = "";
		
		if (rs.next()) {
			dongz = rs.getString("dongz");
		}
		
		if (dongz == null || dongz.equals("")) {
			this.OpenWindow=Liucdzcl.getOpenWinScr(this.MenuId, this.getListTableName(), this.getListTableID());
		} else {
			this.OpenWindow=Liucdzcl.Dongzcl(this.MenuId, this.getListTableName(), this.getListTableID());
		}
		
		visit.setLiucclsb(new StringBuffer(TableNameIdStr+"+"+this.Histry_opinion));
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		
		if(_RefurbishChick){
			_RefurbishChick=false;
			
		}
		getSelectData();
	}

	//替换datasql中 table id 所有的特殊字符
	private String replaceIdStr(String[] tableNameArr,long renyxxb_id,String leib,long qxlx,String dataSql,int startIndex,int tableIndex){
		String tableIdStr=dataSql.substring(startIndex);
		
		int endIndex=tableIdStr.indexOf(TableIdChar);//第一次  字符匹配的位置
	
		String tableName=tableNameArr[0];//默认为第一张表
		if(tableNameArr.length>=tableIndex+1){//第n个特殊字符时  第n张表存在  替换表名
			tableName=tableNameArr[tableIndex];
		}
		
		String replacement=Liuc.getTableLiuczt(tableName, renyxxb_id, leib, qxlx);
		String dataSqlNew=dataSql.replaceFirst(TableIdChar, replacement);
		
		if(endIndex<0 || tableIdStr.length()-endIndex<TableIdCharLength){
			return dataSqlNew;
		}
		
		return replaceIdStr(tableNameArr,renyxxb_id,leib,qxlx,dataSqlNew,startIndex+endIndex+replacement.length(),++tableIndex);
	}
	
//	替换datasql中 diancxxb id 所有的特殊字符
	private String replaceDCStr(String dataSql,int startIndex){
		String tableIdStr=dataSql.substring(startIndex);
		
		int endIndex=tableIdStr.indexOf(DcIdChar);//第一次  字符匹配的位置
		
		String replacement="  select id from diancxxb where id="+this.getTreeid()+" or fuid="+this.getTreeid();
		String dataSqlNew=dataSql.replaceFirst(DcIdChar, replacement);
		
		if(endIndex<0 || tableIdStr.length()-endIndex<DcIdCharLength){
			return dataSqlNew;
		}
		
		return replaceDCStr(dataSqlNew,startIndex+endIndex+replacement.length());
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=null;
		
		String sql=" select  *  from   LIUCSJSCB where upper(type)='"+this.getZyLeix().toUpperCase()+"' ";
		rsl=con.getResultSetList(sql);
		
		String dataSql="";
		String mingc=null;
		String liuclb="";
		String[] gridColNameArr=new String[]{};
		String[] gridColWidthArr=new String[]{};
		String[] tableNameArr=new String[]{};
		long isAblePz=1;//默认 可 配置
		if(rsl.next()){
			
			DataBassUtil dbu=new DataBassUtil();
			try {
				dataSql=dbu.getClob("LIUCSJSCB", "dataSql", rsl.getLong("id"));
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(rsl.getString("gridcolname")!=null && !rsl.getString("gridcolname").equals("")){
				gridColNameArr=rsl.getString("gridcolname").split(",");
			}
			
			if(rsl.getString("gridcolwidth")!=null && !rsl.getString("gridcolwidth").equals("")){
				gridColWidthArr=rsl.getString("gridcolwidth").split(",");
			}
			
			tableNameArr=rsl.getString("tablename").split(",");
			isAblePz=rsl.getLong("isablepz");
			liuclb=rsl.getString("liuclb");
		}
		
		if(isAblePz==1){
			mingc=rsl.getString("mingc");
		}
		
		dataSql=this.replaceDCStr(dataSql, 0);
		dataSql=this.replaceIdStr(tableNameArr, visit.getRenyID(), liuclb, this.getWeizSelectValue().getId(), dataSql, 0, 0);
		
		rsl=con.getResultSetList(dataSql);		
		int rsl_rows=rsl.getRows();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,mingc);
		
		if(gridColNameArr!=null){
			for(int i=0;i<gridColNameArr.length;i++){
				egu.getColumn(i+1).setHeader(gridColNameArr[i]);
			}
		}
		
		if(gridColWidthArr!=null){
			for(int i=0;i<gridColWidthArr.length;i++){
				egu.getColumn(i+1).setWidth(Integer.parseInt(gridColWidthArr[i]));
			}
		}
		
		int histryyj_index=0;//histryyj  在grid中位置
		int id_index=0;//id  在grid中的位置
		for(int i=0;i<egu.getGridColumns().size();i++){
			if( ((GridColumn)egu.getGridColumns().get(i)).dataIndex.equalsIgnoreCase("histryyj") ){
				histryyj_index=i;
			}
			
			if( ((GridColumn)egu.getGridColumns().get(i)).dataIndex.equalsIgnoreCase("id") ){
				id_index=i;
			}
		}
		
		for(int i=0;i<rsl_rows;i++){
			String strtmp="";
			List tmp=Liuc.getRiz(Long.parseLong(egu.getDataValue(i,id_index-1)));
			for(int j=0;j<tmp.size();j++){
				strtmp+=((Yijbean)tmp.get(j)).getXitts()+"\\n  "+(((Yijbean)tmp.get(j)).getYij()==null?"":((Yijbean)tmp.get(j)).getYij())+"\\n ";
			}
			egu.setDataValue(i, histryyj_index-1, "记录 "+(i+1)+":\\n "+strtmp);
		}
		
		egu.getColumn("tableid").setHidden(true);
		egu.getColumn("histryyj").setHidden(true);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(25);
		
//		 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("流程状态");
		ComboBox WeizSelect = new ComboBox();
		WeizSelect.setId("Weizx");
		WeizSelect.setWidth(80);
		WeizSelect.setLazyRender(true);
		WeizSelect.setTransform("WeizSelectx");
		egu.addToolbarItem(WeizSelect.getScript());
		egu.addOtherScript("Weizx.on('select',function(){ document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		
		if ((getWeizSelectValue().getId()) == 1) {
			Menu MuWdqx=new Menu();
			List List_Wdqx=this.getMenuItemList(con, liuclb, visit.getRenyID());
			MuWdqx.setItems(List_Wdqx);
			egu.addToolbarItem("{text:'我的权限',menu:"+MuWdqx.getScript()+"}");
		}
		
		String menuItemHandlerStr=this.getMenuItemOnClickHandler();
		egu.addOtherScript(menuItemHandlerStr);
		
		egu.addTbarText("-");// 设置分隔符
		GridButton ref=new  GridButton("刷新","function(){document.all.RefurbishButton.click();} ");
		ref.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(ref);
		
		setExtGrid(egu);
		con.Close();
	}
	
	private List getMenuItemList(JDBCcon con,String liuclb,long renyxxb_id ){
		
		String sql="select distinct\n" +
		"       liucdzb.id,\n" + 
		"       decode(liucdzb.mingc,'提交','从<'||leibztb.mingc||'>'||liucdzb.mingc||'<'||GetLiuc_Hjzt(liucdzb.id)||'>',\n" + 
		"             '回退','从<'||leibztb.mingc||'>'||liucdzb.mingc||'<'||GetLiuc_Hjzt(liucdzb.id)||'>') as mingc,\n" + 
		"             'onMenuItemClick' as dongz\n" + 
		"       from liucdzjsb,liucdzb,liucztb,liuclbb,leibztb\n" + 
		"       where liucdzjsb.liucdzb_id=liucdzb.id\n" + 
		"             and liucdzb.liucztqqid=liucztb.id\n" + 
		"             and liucztb.leibztb_id=leibztb.id\n" + 
		"             and leibztb.liuclbb_id=liuclbb.id\n" + 
		"             and liuclbb.mingc='"+liuclb+"'\n" + 
		"             and liucdzjsb.liucjsb_id in\n" + 
		"             (select liucjsb_id from renyjsb\n" + 
		"                     where renyxxb_id="+renyxxb_id+")";
		List List_Wdqx=new ArrayList();
		ResultSetList rsl=con.getResultSetList(sql);
		while(rsl.next()){
		
			List_Wdqx.add(new TextItem(rsl.getString("id"),rsl.getString("mingc"),rsl.getString("dongz")));
		}
		
		rsl.close();
		return List_Wdqx;
	}
	
	private String getMenuItemOnClickHandler(){
		
		StringBuffer bf=new StringBuffer();
		
		bf.append(" function onMenuItemClick(item){ \n");
		
		bf.append(" var rc = gridDiv_grid.getSelectionModel().getSelections(); \n");
		bf.append(" if(rc.length<=0){ Ext.Msg.alert('提示信息','请选择记录操作'); return; } \n");
		
		bf.append(" var tableidstr=''; document.all.Histry_opinion.value='';\n");
		bf.append(" for(var i=0;i<rc.length;i++){	\n");
		bf.append(" tableidstr+=rc[i].get('TABLEID'); \n");
		bf.append(" if(i!=rc.length-1){tableidstr+='&';} \n");
		
		bf.append(" var strtmp=rc[i].get('HISTRYYJ'); \n");
		bf.append(" document.all.Histry_opinion.value+=strtmp+'\\n';");
		bf.append(" }\n");

		bf.append(" document.all.CHANGE.value=tableidstr; \n");
		bf.append(" document.getElementById('MenuId').value=item.id; \n");
		bf.append(" document.all.SaveButton.click(); \n");
		
		bf.append(" } \n");
		return bf.toString();
	}
	
	private String treeid;
	
	private boolean treeid_changeBoo=false;
	
	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if(this.treeid==null || this.treeid.equals("") || !this.treeid.equals(treeid)){
			treeid_changeBoo=true;
		}
		this.treeid = treeid;
	}

	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

//	 位置
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getWeizSelectModel()
							.getOption(0));
		}
		
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){
			((Visit) getPage().getVisit()).setboolean1(true);
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "我的任务"));
		list.add(new IDropDownBean(2, "流程中"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
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

	//页面加载 传递的参数
	private void setZyLeix(String zyLeix){
		Visit visit = (Visit) getPage().getVisit();
		visit.setString5(zyLeix);
	}
	
	private String getZyLeix(){
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString5();
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
		
		String leix=cycle.getRequestContext().getRequest().getParameter("lx");
		if(leix!=null && !leix.equals("")){
			this.setZyLeix(leix);
			visit.setActivePageName("");
		}
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.setList1(null);
			this.setListTableName(new ArrayList());
			this.setListTableID(new ArrayList());
			setWeizSelectValue(null);
			setWeizSelectModel(null);
			this.setHistry_opinion("");
			this.setOpenWindow("");
			getSelectData();
		}
	}
}
