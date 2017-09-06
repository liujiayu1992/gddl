package com.zhiren.dc.tableshsz;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DataBassUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者:tzf
 * 时间:2010-01-17
 * 内容:流程审核表设置
 */
public class Tableshsz extends BasePage implements PageValidateListener {
	
	private String msg;

	public String getMsg() {
		return MainGlobal.getExtMessageBox(msg, false);
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

	private String formPanelTitle="";
	
	public String getFormPanelTitle(){
		return formPanelTitle;
	}
	
	public void setFormPanelTitle(String formPanelTitle){
		this.formPanelTitle=formPanelTitle;
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String RecordIndex;
	
	public String getRecordIndex(){
		return RecordIndex;
	}
	
	public void setRecordIndex(String RecordIndex){
		this.RecordIndex=RecordIndex;
	}
	
	private String RecordId;
	
	public String getRecordId(){
		return RecordId;
	}
	
	public void setRecordId(String RecordId){
		this.RecordId=RecordId;
	}
	
	private String OldRecordId;
	
	public String getOldRecordId(){
		return OldRecordId;
	}
	
	public void setOldRecordId(String OldRecordId){
		this.OldRecordId=OldRecordId;
	}
	
	private String DataSqlValue;
	
	public String getDataSqlValue(){
		return DataSqlValue;
	}
	
	public void setDataSqlValue(String DataSqlValue){
		this.DataSqlValue=DataSqlValue;
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String tableName="liucsjscb";
		String mokmc=null;
		
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = this.getExtGrid().getModifyResultSet(this.getChange());
		String recId="";
		String newid="";
		if (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			newid=MainGlobal.getNewID(con, visit.getDiancxxb_id());
			sql2.append(newid).append(",'").append(this.getDataSqlValue().replaceAll("'", "''")).append("'");
			if ("0".equals(mdrsl.getString("ID"))) {
				recId=newid;
				sql.append("insert into ").append(tableName).append("(id,datasql");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					sql2.append(",").append(
							 this.getExtGrid().getValueSql( this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i)));
				}
				sql.append(") values(").append(sql2).append(");\n");
			} else {
				recId=mdrsl.getString("ID");
				if(!(mokmc==null)&&!mokmc.equals("")){
					String id = mdrsl.getString("id");
					//更改时增加日志
					MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
							SysConstant.RizOpType_UP,mokmc,
							tableName,id);
				}
				sql.append("update ").append(tableName).append(" set datasql='").append(this.getDataSqlValue().replaceAll("'", "''")).append("',");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					sql.append(
							 this.getExtGrid().getValueSql( this.getExtGrid().getColumn(mdrsl.getColumnNames()[i]),
									mdrsl.getString(i))).append(",");
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
						";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		
		int flag = con.getUpdate(sql.toString());
		if(flag>=0){
			this.setMsg("数据操作成功");
		}else{
			con.rollBack();
			con.Close();
			this.setMsg("数据操作失败");
			return;
		}
		
		con.commit();
		con.Close();
		
		this.setOldRecordId(recId);//新添加的记录  id要对应起来  否则页面上新添加的记录 无法保存
		this.setRecordId(recId);
		
		this.setMsg("数据操作成功");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
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
	
		if(_DeleteChick){
			_DeleteChick=false;
			Delete();
		}
		
		if(_RefurbishChick){
			_RefurbishChick=false;
		}
		
		this.setDataSqlValue("");
		
		if(treeid_changeBoo){
			treeid_changeBoo=false;
			this.setRecordId("");
			this.setRecordIndex("");
		}
		getSelectData();
	}
	
	private void Delete(){
		JDBCcon con=new JDBCcon();
		String recId=this.getChange();
		
		String sql=" delete from liucsjscb where id="+recId;
		
		int flag=con.getDelete(sql);
		
		con.Close();
		
		if(flag>=0){
			this.setMsg("数据操作成功");
		}else{
			this.setMsg("数据操作失败");
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql=" select l.id,(select mingc from diancxxb where id=l.diancxxb_id) diancxxb_id,l.type,l.mingc,\n" +
				"  l.tablename,l.gridcolname,l.gridcolwidth,l.liuclb, decode(l.isablepz,1,'是','否') isablepz,\n" +
				"  l.beiz from liucsjscb l where l.diancxxb_id in (" +
				"  select id from diancxxb where fuid=" +this.getTreeid()+
				" union select id from diancxxb where id="+this.getTreeid()+
				") order by l.riq asc ";
		
		ResultSetList rsl=con.getResultSetList(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setSortable(false);
		
		egu.getColumn("diancxxb_id").setHeader("单位");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setSortable(false);
		
		egu.getColumn("type").setHeader("资源关键字");
		egu.getColumn("type").setWidth(90);
		egu.getColumn("type").setSortable(false);
		
		egu.getColumn("mingc").setHeader("表格配置关键字");
		egu.getColumn("mingc").setWidth(100);
		egu.getColumn("mingc").setSortable(false);
		
		egu.getColumn("tablename").setHeader("表名");
		egu.getColumn("tablename").setWidth(120);
		egu.getColumn("tablename").setSortable(false);
		
		egu.getColumn("gridcolname").setHeader("字段名称");
		egu.getColumn("gridcolname").setWidth(120);
		egu.getColumn("gridcolname").setSortable(false);
		
		egu.getColumn("gridcolwidth").setHeader("字段宽度");
		egu.getColumn("gridcolwidth").setWidth(120);
		egu.getColumn("gridcolwidth").setSortable(false);
		
		egu.getColumn("isablepz").setHeader("配置");
		egu.getColumn("isablepz").setDefaultValue("是");
		egu.getColumn("isablepz").setWidth(50);
		egu.getColumn("isablepz").setSortable(false);
		
		egu.getColumn("liuclb").setHeader("流程类别");
		egu.getColumn("liuclb").setWidth(80);
		egu.getColumn("liuclb").setSortable(false);
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("beiz").setSortable(false);
		
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from diancxxb"));
		
		egu.getColumn("isablepz").setEditor(new ComboBox());
		egu.getColumn("isablepz").setComboEditor(egu.gridId, new IDropDownModel(" select 1 id,'是' mingc from dual union select 0 id,'否' mingc from dual order by id desc "));
		egu.getColumn("isablepz").setReturnId(true);
		
		egu.getColumn("liuclb").setEditor(new ComboBox());
		egu.getColumn("liuclb").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc  from liuclbb  "));
		egu.getColumn("liuclb").setReturnId(false);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
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
	
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		String delHandler=" function(){ Ext.Msg.confirm('提示信息','删除操作将不可撤销,是否确认删除?',function(btn){ if(btn=='yes'){\n" +
				" if(gridDiv_sm.getSelected()==null){Ext.Msg.alert('提示信息','请选择一条记录');return;}\n" +
				" document.all.CHANGE.value=gridDiv_sm.getSelected().get('ID');\n" +
				" document.all.RecordId.value='';\n" +
				" document.all.RecordIndex.value='';\n" +
				" document.all.DeleteButton.click(); } });" +
				"}";
		GridButton del=new GridButton("删除",delHandler);
		del.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(del);
		
		String condition="if(gridDiv_sm.getSelected()==null){Ext.Msg.alert('提示信息','请选择一条记录');return;}\n" +
				"if(extGS.getValue()==''){Ext.Msg.alert('提示信息','sql文本框不可为空');return;}\n" +
				"if(gridDiv_sm.getSelected().get('ID')!='0' && gridDiv_sm.getSelected().get('ID')!=document.all.OldRecordId.value){Ext.Msg.alert('提示信息','sql文本框和选择记录不配，请先刷新!');return;}\n" +
				"document.all.DataSqlValue.value=extGS.getValue();\n";
		egu.addToolbarButton(GridButton.ButtonType_SubmitSel_condition, "SaveButton",condition);
		
		egu.addTbarText("-");// 设置分隔符
		GridButton ref=new  GridButton("刷新","function(){" +
				"document.all.OldRecordId.value=document.all.RecordId.value;\n" +
				"document.all.RefurbishButton.click();} ");
		ref.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(ref);
		
		egu.addOtherScript("gridDiv_sm.addListener('rowselect',function(sm,rowIndex,record){document.all.RecordIndex.value=rowIndex;\n" +
				"document.all.RecordId.value=record.get('ID');\n" +
				"if(record.get('ID')=='0'){extGS.setValue('');} \n" +
//				"document.all.RefurbishButton.click();\n" +
				"});\n");
		egu.addOtherScript("gridDiv_sm.addListener('rowdeselect',function(sm,rowIndex,record){document.all.RecordId.value='';\n" +
				"document.all.RecordIndex.value='';\n" +
				"});\n");
		
		String title="'请选择一条记录'";
		if(this.getRecordId()!=null && !this.getRecordId().equals("") && !this.getRecordId().equals("0")){
			   DataBassUtil dbu=new DataBassUtil();
				try {
					String dataSql=dbu.getClob("liucsjscb", "datasql", Long.parseLong(this.getRecordId()));
					this.setDataSqlValue(dataSql);
				
				} catch (SQLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
		
				ResultSetList rs=con.getResultSetList(" select mingc from liucsjscb where  id="+this.getRecordId());
				
				if(rs.next()){
					title="'"+rs.getString("mingc")+"'";
				}
			
				rs.close();
		
			egu.addOtherScript("gridDiv_grid.getView().focusRow("+this.getRecordIndex()+");\n");
			egu.addOtherScript("gridDiv_sm.selectRow("+this.getRecordIndex()+");\n");
		}
		
		this.setFormPanelTitle(title);
		
		setExtGrid(egu);
		con.Close();
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
		if(this.treeid!=null && !this.treeid.equals("") && !this.treeid.equals(treeid)){
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.setList1(null);
			this.setDataSqlValue("");
			this.setRecordIndex("");
			this.setRecordId("");
			this.setOldRecordId("");
			getSelectData();
		}
	}
}
