package com.zhiren.jt.gongys;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
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
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;
import com.zhiren.common.MainGlobal;

/*
 * 时间:2009-05-05 
 * 作者:ly 
 * 内容:修改了保存方法Save1(String strchange, Visit visit) 解决增、删、改的BUG
 * 
 */
/*
 * 作者：夏峥
 * 时间：2013-03-05
 * 使用范围：国电电力及其下属电厂
 * 描述：确认供应商添加或修改时只判断编码，名称和全称是否有重复。
 */
public class Gongysxg extends BasePage implements PageValidateListener {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
//	系统日志表中的状态字段
	private static  final String ZhangTConstant1="成功";
	private static  final String ZhangTConstant2="失败";
//	系统日志表中的类别字段
	private static  final String leiBConstant="供应商维护";
	
	private String SaveMsg;
	
	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);
	}

	protected void initialize() {
		super.initialize();
		setSaveMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick =false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick =true;
	}
	
	private boolean m_falg;

	public boolean getFalg() {
		return m_falg;
	}

	public void setFalg(boolean falg) {
		m_falg = falg;
	}
	
	private boolean _GuanlChick = false;
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
    }
    //停用
    private boolean _StopClick = false;
    public void StopButton(IRequestCycle cycle){
    	_StopClick=true;
    }
    //启用
    private boolean _BeginClick = false;
    public void BeginButton(IRequestCycle cycle){
    	_BeginClick=true;
    }
//  有可能从返回按钮返回本页面，也可能是从添加所选按钮返回本页面，标记作用
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
    
    private String DataSource;
	
	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	

	public String getGongysmc() {
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString3();
	}

	public void setGongysmc(String gongysmc) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString3(gongysmc);
	}

	public void submit(IRequestCycle cycle){
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_GuanlChick){
			_GuanlChick = false;
			cycle.activate("Gongys_Tjyy");
		}
		if (_RefreshChick) {
			_RefreshChick = false;
				ToAddMsg="";
		
		}
		if(_StopClick){
			_StopClick= false;
			Stop();
			getSelectData();
		}
		if(_BeginClick){
			_BeginClick= false;
			Begin();
			getSelectData();
		}
	}

	// private static final long serialVersionUID = 7020946077527159829L;

	public static final int Gridstyle_Read = 0;

	public static final int Gridstyle_Edit = 1;

	public static final int GridselModel_Row = 0;

	public static final int GridselModel_Check = 1;

	public String title;

	public int gridType;

	public String gridId;

	public int width;

	public int height;

	public int gridsm;

	public List gridColumns;

	public String[][] griddata;

	public String tbars;

	public int pagsize;

	public String otherScript;

	public int clicksToEdit = 1;

	public boolean frame = true;

	public String tableName;

	public String gridJsCode = "";

	public String gridJsDs = "";

	public void ExtGridUtil() {
	}

	/**
	 * @param gridId
	 *            设置grid的Id 及其绑定的DIV的id
	 * 
	 */
	public void ExtGridUtil(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @param gridId
	 *            设置grid的Id 及其绑定的DIV的id
	 * @param sql
	 *            取数的方法
	 */
	public void ExtGridUtil(String gridId, ResultSetList rsl) {
		this.gridId = gridId;
		addColumn(new GridColumn(GridColumn.ColType_Rownum));
		for (int i = 0; i < rsl.getColumnNames().length; i++) {
			GridColumn gc = new GridColumn(rsl.getColumnNames()[i], rsl.getColumnNames()[i]);
			if ("id".equals(rsl.getColumnNames()[i].toLowerCase())) {
				gc.setDefaultValue("0");
				gc.setHidden(true);
			} else {
				String extType = OratypeOfExt(rsl.getColumnTypes()[i]);
				gc.setDataType(extType);
				if ("float".equals(extType)) {
					gc.setEditor(new NumberField());
					((NumberField) gc.editor).setDecimalPrecision(rsl.getColScales()[i]);
				} else if ("string".equals(extType)) {
					gc.setEditor(new TextField());
					((TextField) gc.editor).setMaxLength(rsl.getColPrecisions()[i]);
				} else if ("date".equals(extType)) {
					gc.setEditor(new DateField());
					gc.setRenderer(GridColumn.Renderer_Date);
				}
				if (gc.editor != null) {
					gc.editor.allowBlank = rsl.isNullable(i);
				}
			}
			addColumn(gc);
		}
		initData(rsl.getRows(), rsl.getColumnCount());
		while (rsl.next()) {
			setRowData(rsl.getRow(), rsl.getRowString());
		}
	}

	public String OratypeOfExt(String oratype) {
		if ("NUMBER".equals(oratype)) {
			return "float";
		}
		if ("DATE".equals(oratype)) {
			return "date";
		}
		// if(oratype.equals(anObject)) {

		// }
		return "string";
	}

	/**
	 * @return 返回grid的类型
	 */
	public int getGridType() {
		return gridType;
	}

	/**
	 * @param gridType
	 *            设置grid的类型 Gridstyle_Read,Gridstyle_Edit
	 */
	public void setGridType(int gridType) {
		this.gridType = gridType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            grid的宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	/**
	 * @return 返回页面行数
	 */
	public int getPagSize() {
		return pagsize;
	}

	/**
	 * @param pagsize
	 *            页面行数 设置分页
	 */
	public void addPaging(int pagsize) {
		this.pagsize = pagsize;
	}

	/**
	 * @return 返回grid 的行选择模式
	 */
	public int getGridSelModel() {
		return gridsm;
	}

	/**
	 * @param selmodel
	 *            grid行选择模式
	 *            GridselModel_None,GridselModel_Row,GridselModel_Check
	 */
	public void setGridSelModel(int selmodel) {
		gridsm = selmodel;
	}

	/**
	 * @return 返回列的集合
	 */
	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	/**
	 * @param column
	 *            增加一列
	 */
	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	/**
	 * @param colindex
	 *            列索引
	 * @return 返回一列
	 */
	public GridColumn getColumn(int colindex) {
		return (GridColumn) getGridColumns().get(colindex);
	}

	public GridColumn getColumn(String coldataIndex) {
		for (int i = 0; i < getGridColumns().size(); i++) {
			if (coldataIndex.toUpperCase().equals(getColumn(i).dataIndex.toUpperCase())) {
				return getColumn(i);
			}
		}
		return null;
	}

	public int getDataColumnCount() {
		int count = 0;
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == GridColumn.ColType_default) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return 返回按钮的集合
	 */
	// public List getGridButtons() {
	// if(gridbuttons == null) {
	// gridbuttons = new ArrayList();
	// }
	// return gridbuttons;
	// }
	/**
	 * @param gridButtonType
	 *            按钮类型
	 * @param tapestryBtnId
	 *            对应的tapestry按钮ID
	 */
	public void addToolbarButton(int gridButtonType, String tapestryBtnId) {
		GridButton gb = new GridButton(gridButtonType, gridId,
				getGridColumns(), tapestryBtnId);
		addTbarBtn(gb);
	}

	public void addTbarBtn(GridButton gb) {
		addToolbarItem("{" + gb.getScript() + "}");
	}

	public void addTbarText(String text) {
		addToolbarItem("'" + text + "'");
	}

	public void addTbarTreeBtn(String treeid) {
		TextField tf = new TextField();
		tf.setId(treeid + "_text");
		tf.setWidth(100);
		addToolbarItem(tf.getScript());
		StringBuffer bf = new StringBuffer();
		bf.append("{").append("icon:'").append("ext/resources/images/list-items.gif").append("',");
		bf.append("cls: 'x-btn-icon',");
		bf.append("handler:function(){").append(treeid).append("_window.show();}}");
		addToolbarItem(bf.toString());
	}

	public void addToolbarItem(String item) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTbar()).append(item).append(",");
		setTbar(sb.toString());
	}

	/**
	 * 设置grid的全部数据
	 * 
	 * @param data
	 *            String[][]
	 */
	public void setData(String[][] data) {
		griddata = data;
	}

	/**
	 * 初始化Data数组
	 * 
	 * @param rows
	 *            行数
	 */
	public void initData(int rows, int cols) {
		griddata = new String[rows][cols];
	}

	/**
	 * 设置行数据
	 * 
	 * @param rowIndex
	 *            行下标 0 to ...
	 * @param data
	 *            String[] 数据
	 */
	public void setRowData(int rowIndex, String[] data) {
		if (rowIndex >= griddata.length) {
			// 异常
			return;
		}
		griddata[rowIndex] = data;
	}

	/**
	 * 设置每个单元格的值
	 * 
	 * @param rowIndex
	 *            行标 0 to ...
	 * @param colIndex
	 *            列标 0 to ...
	 * @param data
	 *            Stirng 值
	 */
	public void setDataValue(int rowIndex, int colIndex, String data) {
		if (rowIndex >= griddata.length) {
			// 异常
			return;
		}
		if (colIndex >= griddata[rowIndex].length) {
			// 异常
			return;
		}
		griddata[rowIndex][colIndex] = data;
	}

//	/**
//	 * @return 返回表头的Script
//	 */
//	private String getColumnsScript() {
//		StringBuffer columnsScript = new StringBuffer();
//		columnsScript.append("var ").append(gridId).append(
//				"_cm = new Ext.grid.ColumnModel([");
//		for (int c = 0; c < getGridColumns().size(); c++) {
//			GridColumn Gc = (GridColumn) getGridColumns().get(c);
//			columnsScript.append(Gc.getScript());
//			columnsScript.append(",");
//		}
//		columnsScript.deleteCharAt(columnsScript.length() - 1);
//		columnsScript.append("]);\n ").append(gridId).append("_cm.defaultSortable=false;\n");
//		return columnsScript.toString();
//	}

//	/**
//	 * @return 返回新增记录的Script
//	 */
//	private String getPlantScript() {
//		StringBuffer plantScript = new StringBuffer();
//		plantScript.append("var ").append(gridId).append("_plant = Ext.data.Record.create([");
//		for (int c = 0; c < getGridColumns().size(); c++) {
//			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
//				GridColumn gc = ((GridColumn) getGridColumns().get(c));
//				plantScript.append("{name: '").append(gc.dataIndex).append("', type:'").append(gc.datatype).append("'},");
//			}
//		}
//		plantScript.deleteCharAt(plantScript.length() - 1);
//		plantScript.append("]);\n");
//		return plantScript.toString();
//	}

	/**
	 * @return 返回Toolbar 的Script
	 */
	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

	/**
	 * @return 返回数据的Script
	 */
//	private String getDataScript() {
//		StringBuffer djs = new StringBuffer("var " + gridId + "_data = [\n");
//		
//		for (int r = 0; r < this.getExtGrid().getData().length; r++) {
//			djs.append("[");
//			for (int c = 0; c < getExtGrid().getData()[r].length; c++) {
//				djs.append("'");
//				djs.append(getExtGrid().getData()[r][c]);
//				djs.append("',");
//			}
//			djs.deleteCharAt(djs.length() - 1);
//			djs.append("],");
//		}
//		djs.deleteCharAt(djs.length() - 1);
//		djs.append("];\n");
//		return djs.toString();
//	}
//
//	/**
//	 * @return 返回数据的解析Script
//	 */
//	private String getDsScript() {
//		StringBuffer dsScript = new StringBuffer();
//		dsScript.append("var " + gridId + "_ds = new Ext.data.Store({\n");
//		dsScript.append("proxy : new Ext.zr.data.PagingMemoryProxy(" + gridId+ "_data),\n");
//		dsScript.append("reader: new Ext.data.ArrayReader({}, [\n");
//		for (int c = 0; c < getGridColumns().size(); c++) {
//			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
//				dsScript.append("{name:'");
//				dsScript.append(((GridColumn) getGridColumns().get(c)).dataIndex);
//				dsScript.append("'},");
//			}
//		}
//		dsScript.deleteCharAt(dsScript.length() - 1);
//		dsScript.append("])");
//		dsScript.append("});\n");
//		return dsScript.toString();
//	}

	/**
	 * @return 返回分页的Script
	 */
	public String getPagingScript() {
		StringBuffer pagScript = new StringBuffer();
		pagScript.append("var ").append(gridId).append("_pag").append(" = new Ext.PagingToolbar({ \n");
		pagScript.append("pageSize: ").append(getPagSize()).append(",\n");
		pagScript.append("store: ").append(gridId).append("_ds").append(",\n");
		pagScript.append("displayInfo: true,\n");
		pagScript.append("displayMsg: '显示第 {0} 条到 {1} 条记录，一共 {2} 条',\n");
		pagScript.append("emptyMsg: '没有记录' });\n");
		return pagScript.toString();
	}

	public String getGridPanelName() {
		return getGridType() == 0 ? "GridPanel" : "EditorGridPanel";
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
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

	/**
	 * @param sql
	 * 
	 */
	public void setGridDs(String sql) {
		gridJsDs = "var grid1_data = [";
		JDBCcon con = new JDBCcon();
		ResultSetList rs = con.getResultSetList(sql);
		int cols = rs.getColumnCount();
		while (rs.next()) {
			gridJsDs += "[";
			for (int i = 0; i < cols; i++) {
				gridJsDs += "'" + rs.getString(i) + "'";
				if (i + 1 < cols) {
					gridJsDs += ",";
				}
			}
			gridJsDs += "]";
			if (rs.getRow() + 1 < rs.getRows()) {
				gridJsDs += ",";
			}
		}
		gridJsDs += "];";
	}

	public String getDataset() {
		return gridJsDs;
	}
	//启用方法
	private void Begin(){
		Visit visit=(Visit) this.getPage().getVisit();
		Begin1(getChange(),visit);
	}
	//停用方法
	private  void Stop(){
		Visit visit = (Visit) this.getPage().getVisit();
		Stop1(getChange(), visit); 
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit); 
	}
	
//  判断端口地址时候配置
	private String zhi = "";
	public String Duankcx(){
		 zhi = "";
		 JDBCcon con = new JDBCcon();
		 String sql = "select zhi from xitxxb where mingc = '入口地址'";
		 ResultSetList rsl = con.getResultSetList(sql);
		 while(rsl.next()){
			 zhi = rsl.getString("zhi");
		 }
		 con.Close();
		 return zhi;
	}
	
//	判断数据是否在本地库中已经存在(不存在返回0，存在返回行数)
	private int Shujpd(JDBCcon con,String sql){
		return JDBCcon.getRow(con.getResultSet(sql));
	}
	
//	删除下级公司供应商的上级编码字段
	private void DeleteXiaj(JDBCcon con,String tableName,String bianm,String operation){
		
		InterCom_dt dt=new InterCom_dt();
		String sql=" select  d.id,d.mingc  from diancxxb d where d.fuid="+((Visit)(this.getPage().getVisit())).getDiancxxb_id();
		
		ResultSetList mdrsl=con.getResultSetList(sql);
		
		//有下级电厂单位，需要更新
		if(mdrsl.getRows()!=0){
			int count_success=0;
			int count_fail=0;
			String s="";
			while(mdrsl.next()){
				
				String diancxxb_id=mdrsl.getString("ID");
				String[] sqls=new String[]{"update gongysb set SHANGJGSBM='' where SHANGJGSBM='"+bianm+"'"};
				String[] answer=dt.sqlExe(diancxxb_id, sqls, true);
				
				if(answer[0].equals("true")){  //更新下级字段成功
					count_success++;
					s+="单位"+mdrsl.getString("MINGC")+"更新成功;";
				}else{
					count_fail++;
					s+="单位"+mdrsl.getString("MINGC")+"更新失败;";
				}
				
			}
			
			if(count_fail>0){
				zhuangT=ZhangTConstant2;
			}else{
				zhuangT=ZhangTConstant1;
			}
			logMsg+=operation+"表"+tableName+"中编码"+bianm.replaceAll("'","")+"更新下级"+mdrsl.getRows()+"个;"+s+",总共成功:"+count_success+"个，失败:"+count_fail+"个;";
			
		}
		
	}
	
	private void GengGSJBM(JDBCcon con,String tableName,String bianm,String operation,String oldBianM ){
		InterCom_dt dt=new InterCom_dt();
		
		String sql=" select  d.id,d.mingc  from diancxxb d where d.fuid="+((Visit)(this.getPage().getVisit())).getDiancxxb_id();
		
		ResultSetList mdrsl=con.getResultSetList(sql);
		
		//有下级电厂单位，需要更新
		if(mdrsl.getRows()!=0){
			
			int count_success=0;
			int count_fail=0;
			String s="";
			while(mdrsl.next()){
				
				String diancxxb_id=mdrsl.getString("ID");
				String[] sqls=new String[]{"update gongysb set SHANGJGSBM='"+bianm.replaceAll("'", "")+"' where SHANGJGSBM='"+oldBianM+"'"};
				String[] answer=dt.sqlExe(diancxxb_id, sqls, true);
				
				if(answer[0].equals("true")){  //更新下级字段成功
					count_success++;
					s+="单位"+mdrsl.getString("MINGC")+"更新成功;";
				}else{
					count_fail++;
					s+="单位"+mdrsl.getString("MINGC")+"更新失败;";
				}
				
			}
			
			if(count_fail>0){
				zhuangT=ZhangTConstant2;
			}else{
				zhuangT=ZhangTConstant1;
			}
			
			logMsg+=operation+"表"+tableName+"中编码"+bianm.replaceAll("'","")+"更新下级"+mdrsl.getRows()+"个;"+s+",总共成功:"+count_success+"个，失败:"+count_fail+"个;";
		}
	}
	
//	日志记录
	private String logMsg="";
	private String zhuangT="";
	private void WriteLog(JDBCcon con){
			
			Visit visit=(Visit)this.getPage().getVisit();
			
			if(!logMsg.equals("")){//不为空，需要写入日志记录
				Date date=new Date();
				SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String date_str=sdf.format(date);
				String sql=" insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values(" +
						" getnewid("+visit.getDiancxxb_id()+"),"
						+visit.getDiancxxb_id()+",'"+visit.getRenymc()+"','"+leiBConstant+"'," +
						"to_date('"+date_str+"','YYYY-MM-DD,HH24:mi:ss'),'"+this.zhuangT+"','"+logMsg+"')";
				con.getInsert(sql);
				logMsg="";
				
			}
	}

//	新增加一条记录时，通知上级待批复
	private void DaiPF(){
		
	}
	//启用方法
	public void Begin1(String strchange,Visit visit){
		JDBCcon con=new JDBCcon();
		ResultSetList uprsl=visit.getExtGrid1().getModifyResultSet(strchange);
		while(uprsl.next()){
			String sql="update gongysb set zhuangt=1 where id='"+uprsl.getString(0)+"'";
			int i=con.getUpdate(sql);
			if(i!=-1){
				setSaveMsg("启用成功!");
			}
			else{
				setSaveMsg("启用失败!");
			}
		}
		uprsl.close();
		con.Close();
	}
	//停用方法
	public void Stop1(String strchange,Visit visit){
		JDBCcon con=new JDBCcon();
		ResultSetList uprsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while(uprsl.next()){
			String sql="update gongysb set zhuangt=0 where id='"+uprsl.getString(0)+"'";
			int i=con.getUpdate(sql);
			if(i!=-1){
				setSaveMsg("停用成功！");
			}
			else{
				setSaveMsg("停用失败！");
			}
		}
		uprsl.close();
		con.Close();
	}
	public void Save1(String strchange, Visit visit) {
		String strSaveMsg="";
		logMsg="";
		String tableName = "gongysb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("");
		StringBuffer sql_dc = new StringBuffer("");
		ExtGridUtil egu=this.getExtGrid();

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
//		sql.append("begin\n");
		while (delrsl.next()) {
			sql = new StringBuffer("");
			sql.append("delete ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append("\n");
			int i=con.getDelete(sql.toString());
			if(i!=-1){  //操作成功，记录日志
				this.DeleteXiaj(con, tableName, delrsl.getString("BIANM"), "删除");
			}else{
				
			}
		}
		
		sql = new StringBuffer("");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql=new StringBuffer();
			sql_dc=new StringBuffer(); 
			String SaveMsgLocal="";
			String gys_id = MainGlobal.getNewID(visit.getDiancxxb_id());
			if ("0".equals(mdrsl.getString("ID"))) {
				ResultSetList weis = con.getResultSetList("select zhi from xitxxb where mingc='供应商编码位数' and zhuangt=1");
				if(weis.next()){
					if(mdrsl.getString("bianm").length()<weis.getInt("zhi")){
						SaveMsgLocal="供应商编码位数小于"+weis.getInt("zhi")+",请按规则重新编码。";
						setSaveMsg(SaveMsgLocal);
						return;
					}
				}
				String sql_check="select id from gongysb where (1=0 ";
				sql.append("insert into ").append(tableName).append("(id");
				String mingc="";
				String quanc="";
				String bianm="";
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(",").append(mdrsl.getColumnNames()[i]);
					if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {
						long SHENGFB_ID=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("SHENGFB_ID"));
						if(mdrsl.getString("SHENGFB_ID")==null || mdrsl.getString("SHENGFB_ID").equals("")){
							sql2.append(",").append("''");
						}else if(SHENGFB_ID==-1L){
							SaveMsgLocal+="--省份:"+mdrsl.getString("SHENGFB_ID");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(SHENGFB_ID);
						}
					}else if(mdrsl.getColumnNames()[i].equals("RONGQGX")){   
						long RONGQGX=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("RONGQGX"));
						if(mdrsl.getString("RONGQGX")==null || mdrsl.getString("RONGQGX").equals("")){
							sql2.append(",").append("''");
						}else if(RONGQGX==-1L){
							SaveMsgLocal+="--融洽关系:"+mdrsl.getString("RONGQGX");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("XINY")){
						long XINY=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("XINY"));
						if(mdrsl.getString("XINY")==null || mdrsl.getString("XINY").equals("")){
							sql2.append(",").append("''");
						}else if(XINY==-1L){
							SaveMsgLocal+="--信誉:"+mdrsl.getString("XINY");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("SHIFSS")){
						long SHIFSS=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("SHIFSS"));
						if(mdrsl.getString("XINY")==null || mdrsl.getString("SHIFSS").equals("")){
							sql2.append(",").append("''");
						}else if(SHIFSS==-1L){
							SaveMsgLocal+="--信誉:"+mdrsl.getString("SHIFSS");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("CHENGSB_ID")){
						long CHENGSB_ID=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("CHENGSB_ID"));
						if(mdrsl.getString("CHENGSB_ID")==null || mdrsl.getString("CHENGSB_ID").equals("")){
							sql2.append(",").append("''");
						}else if(CHENGSB_ID==-1L){
							SaveMsgLocal+="--城市:"+mdrsl.getString("CHENGSB_ID");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(CHENGSB_ID);
						}
					}else if(mdrsl.getColumnNames()[i].equals("SHANGJGSBM")&&(Duankcx()==null||Duankcx().equals(""))){ 
						sql2.append(",").append(mdrsl.getString("BIANM"));
					}else {//判断记录时候有重复
						if(mdrsl.getColumnNames()[i].equals("BIANM")){
							bianm=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
							sql_check+=" or bianm="+bianm+"";
						}
						if(mdrsl.getColumnNames()[i].equals("MINGC")){
							mingc=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
							sql_check+=" or mingc="+mingc+"";
						}if(mdrsl.getColumnNames()[i].equals("QUANC")){
							quanc=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
							sql_check+=" or quanc="+quanc+"";
						}
						sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
					}
				}
				
				//当SHANGJGSBM不为空时，说明是从上级导入的供应商，无需批复即可使用
				if(!mdrsl.getString("SHANGJGSBM").equals("")){
					if(this.Shujpd(con, sql_check + ") and leix = 1")!=0){  //在本地库中已经存在,要进行的操作
						SaveMsgLocal+="----------------添加记录-----------<br>--编码:"+bianm.replaceAll("'", "")+"---名称:"+mingc.replaceAll("'","")+"---全称:"+quanc.replaceAll("'","")+"--的记录有重复,不能保存!<br>";
					}else if(SaveMsgLocal.equals("")){
						sql.append(") values(").append(gys_id).append(sql2).append(")\n");
						int flag=con.getInsert(sql.toString());
						//插入时不需要记录日志
						if(flag==-1){  //插入不成功
		//					this.WriteLog();
						}else{        //插入成功 更新gongysdcglb表
							String id = MainGlobal.getNewID(visit.getDiancxxb_id());
							long diancxxb_id = visit.getDiancxxb_id();
							sql_dc.append("insert into gongysdcglb values(")
								.append(id).append(",").append(diancxxb_id).append(",").append(gys_id).append(")");
							con.getInsert(sql_dc.toString());
							
						}
					}
				}else{
					if(this.Shujpd(con, sql_check + ") and leix = 1")!=0){  //在本地库中已经存在,要进行的操作
						SaveMsgLocal+="----------------添加记录-----------<br>--编码:"+bianm.replaceAll("'","")+"---名称:"+mingc.replaceAll("'","")+"---全称:"+quanc.replaceAll("'","")+"--的记录有重复,不能保存!<br>";
					}else if(SaveMsgLocal.equals("")){
						sql.append(") values(").append(gys_id).append(sql2).append(")\n");
						int flag=con.getInsert(sql.toString());
						if(flag==-1){  //插入不成功
						}else{        //插入成功
							this.DaiPF();   //插入一条新的记录时，通知上级单位，为待批复状态
							String id = MainGlobal.getNewID(visit.getDiancxxb_id());
							long diancxxb_id = visit.getDiancxxb_id();
							sql_dc.append("insert into gongysdcglb values(")
								.append(id).append(",").append(diancxxb_id).append(",").append(gys_id).append(")");
							con.getInsert(sql_dc.toString());
						}
					}
				}
				
				strSaveMsg+=SaveMsgLocal;

			} else {
				//在update过程中不需要进行判断有的字段是否存在
				sql=new StringBuffer();
				String bianm="";
				String mingc="";
				String quanc="";
				StringBuffer sql_update=new StringBuffer();
				sql.append("update ").append(tableName).append(" set ");
				sql_update.append("select id from ").append(tableName).append(" where ( 1=0 ");
				for (int i = 1; i < mdrsl.getColumnCount(); i++) {
					sql.append(mdrsl.getColumnNames()[i]).append(" = ");
					
					if (mdrsl.getColumnNames()[i].equals("FUID")) {
						if (mdrsl.getString("fuid").equals("请选择")|| mdrsl.getString("fuid").equals("")) {
							sql.append(-1).append(",");
						} else {
							sql.append((egu.getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("fuid"))).append(",");
						}
					} else if (mdrsl.getColumnNames()[i].equals("SHENGFB_ID")) {
						sql.append((getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("shengfb_id")))
							.append(",");
					} else if (mdrsl.getColumnNames()[i].equals("RONGQGX")) {
						sql.append((getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("rongqgx")))
								.append(",");
					} else if(mdrsl.getColumnNames()[i].equals("BIANM")){
						bianm=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
						sql_update.append(" or bianm=").append(bianm);
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",");
						
					}else if(mdrsl.getColumnNames()[i].equals("MINGC")){
						mingc=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
						sql_update.append(" or mingc=").append(mingc);
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",");
						
					}else if(mdrsl.getColumnNames()[i].equals("QUANC")){
						quanc=getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i));
						sql_update.append(" or quanc=").append(quanc);
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",");
						
					}else if(mdrsl.getColumnNames()[i].equals("SHANGJGSBM")&&(Duankcx()==null||Duankcx().equals(""))){
						sql.append(mdrsl.getString("BIANM")).append(",");
						
					}else if(mdrsl.getColumnNames()[i].equals("ZHUANGT")){
						if(mdrsl.getString("ZHUANGT").equals("启用")){
							sql.append(1).append(",");
						}else if(mdrsl.getString("ZHUANGT").equals("停用")){
							sql.append(0).append(",");
						}
					}
					else {
						sql.append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i))).append(",");
					}
				}
				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ID")).append("\n");
				sql_update.append(") and leix = 1 and id<> ").append(mdrsl.getString("ID"));
				int flag=this.Shujpd(con, sql_update.toString());
				
				if(flag!=0){   //更新的数据在本地库中已经存在
//					this.WriteLog();
					strSaveMsg+="----------------更改记录-----------<br>--编码:"+bianm.replaceAll("'","")+"---名称:"+mingc.replaceAll("'","")+"---全称:"+quanc.replaceAll("'","")+"--的记录有重复,不能保存<br>!";
				}else if(visit.isDCUser()&&!visit.isFencb()){
					con.getUpdate(sql.toString());
				}else{
					
					String sql_change=" select bianm from "+tableName+" where id="+mdrsl.getString("ID");
					String oldValue=bianM_Change(con,sql_change);   //t为true说明需要更改，调用接口
					int k=con.getUpdate(sql.toString());
					if(k!=0){  //更新数据成功
						if(oldValue!=null && bianm!=null &&  !oldValue.equals(bianm.replaceAll("'", ""))){
		//					this.GengGXiaj(); 
							this.GengGSJBM(con, tableName, bianm, "更改", oldValue);
						}
						  //通过判断更改bianm字段前后的变化与否来判断是否更新下级单位的上级编码字段
//						this.WriteLog();
					}else{  //不成功
//						this.WriteLog();
					}
				}
			}
		}
		
		this.WriteLog(con);
		if(strSaveMsg.equals("")){
			strSaveMsg="更新成功";
			ToAddMsg="";  //更改后标识，上级导入的页面不再显示
			this.setSaveMsg(strSaveMsg);
		}else{
			setSaveMsg(strSaveMsg);
			ToAddMsg="";
		}
		
//		tiShi=true;
	}
	
//	查询编码值（根据id），判断前后是否改变，进而判断是否需要更改下级单位的上级编码字段
	public String bianM_Change(JDBCcon con,String sql){
		
		ResultSetList rsl=con.getResultSetList(sql);
		String oldValue=null;
		if(rsl.next()){
			oldValue=rsl.getString("bianm");
		}
		
		 //需要调用接口更改下级单位的上级编码字段
		
		return oldValue;
	}


	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			// return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}

	// //////////////////////////////////////////////////////////////////////
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		tbars = "";
		gridColumns = null;
		String condition="";
		if(getGongysmc()!=null && !getGongysmc().equals("")){
			condition=" and (g.mingc like '%"+getGongysmc()+"%' or g.quanc like '%"+getGongysmc()+"%')\n";
		}
		String sql = "";
		sql = "select g.id,\n" + "       g.xuh,\n" + "       g.bianm,\n"
				+ "       g.mingc,\n" + "       g.quanc,\n"
				+ "        g.piny, \n" + "       DECODE ((SELECT mingc\n"
				+ "                  FROM gongysb\n"
				+ "                 WHERE ID = g.fuid), NULL, '请选择',\n"
				+ "               (SELECT mingc\n"
				+ "                  FROM gongysb\n"
				+ "                 WHERE ID = g.fuid)) AS fuid,\n"
				+ "       s.quanc as shengfb_id,\n" + "       g.danwdz,\n"
				+ "       g.faddbr,\n" + "       g.weitdlr,\n"
				+ "       g.kaihyh,\n" + "       g.zhangh,\n"
				+ "       g.dianh,\n" + "       g.shuih,\n"
				+ "       g.youzbm,\n" + "       g.chuanz,\n"
				+ "       g.meitly,\n" + "       g.meiz,\n"
				+ "       g.chubnl,\n" + "       g.kaicnl,\n"
				+ "       g.kaicnx,\n" + "       g.shengcnl,\n"
				+ "       g.gongynl,\n" + "       g.liux,\n"
				+ "       g.yunsfs,\n" + "       g.shiccgl,\n"
				+ "       g.zhongdht,\n" + "       g.yunsnl,\n"
				+ "       g.heznx,\n" + "       g.rongqgx,\n"
				+ "       g.xiny,\n" + "       g.gongsxz,\n"
				+ "       g.kegywfmz,\n" + "       g.kegywfmzzb,\n"
				+ "       g.shifss,\n" + "       g.shangsdz,\n"
				+ "       g.zicbfb,\n" + "       g.shoumbfb,\n"
				+ "       g.qitbfb,\n" 
				+ "		  g.shangjgsbm,\n"+ " decode(g.zhuangt,1,'启用',0,'停用') as zhuangt,"
				+ "       beiz\n"
				+ "  from shengfb s, gongysb g\n"
				+ " where g.shengfb_id = s.id and g.leix = 1 \n" 
				+ condition
				+ "order by zhuangt, xuh";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		ExtGridUtil("gridDiv", rsl);
		this.gridId="gridDiv";
		egu.setTableName("gongysb");
		egu.setHeight(200);
//		egu.setWidth(1500);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(125);
		egu.getColumn("mingc").setHeader("简称");
		egu.getColumn("mingc").setWidth(180);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(80);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(180);
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("fuid").setHeader("隶属于");
		egu.getColumn("fuid").setWidth(180);
		egu.getColumn("shangjgsbm").setHeader("上级单位编码");
		egu.getColumn("shangjgsbm").setWidth(120);
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("faddbr").setHeader("法定代表人");
		egu.getColumn("weitdlr").setHeader("委托代理人");
		egu.getColumn("youzbm").setHeader("邮政编码");
		egu.getColumn("kaihyh").setHeader("开户银行");
		egu.getColumn("zhangh").setHeader("账号");
		egu.getColumn("dianh").setHeader("电话");
		egu.getColumn("shuih").setHeader("税号");
		egu.getColumn("chuanz").setHeader("传真");
		egu.getColumn("meitly").setHeader("煤炭来源");
		egu.getColumn("meiz").setHeader("煤种");
		egu.getColumn("chubnl").setHeader("储备能力(万吨)");
		egu.getColumn("kaicnl").setHeader("开采能力(万吨)");
		egu.getColumn("kaicnx").setHeader("开采年限(年)");
		egu.getColumn("shengcnl").setHeader("生产能力(万吨)");
		egu.getColumn("gongynl").setHeader("供应能力(万吨)");
		egu.getColumn("liux").setHeader("流向");
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("shiccgl").setHeader("市场采购量(万吨)");
		egu.getColumn("zhongdht").setHeader("重点合同(万吨)");
		egu.getColumn("yunsnl").setHeader("运输能力(万吨)");
		egu.getColumn("heznx").setHeader("合作年限(年)");
		egu.getColumn("rongqgx").setHeader("融洽关系");
		egu.getColumn("xiny").setHeader("信誉");
		egu.getColumn("gongsxz").setHeader("公司性质");
		egu.getColumn("kegywfmz").setHeader("可供煤种");
		egu.getColumn("kegywfmzzb").setHeader("可供煤种指标");
		//egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shifss").setHeader("是否上市");
		egu.getColumn("shangsdz").setHeader("上市地址");
		egu.getColumn("zicbfb").setHeader("自产比例");
		egu.getColumn("shoumbfb").setHeader("收煤比例");
		egu.getColumn("qitbfb").setHeader("其他比例");
		egu.getColumn("beiz").setHeader("备注");
		
		String gongyshangs="select zhi from xitxxb where mingc='供应商维护页面单页显示的行数' and zhuangt=1";
		rsl = con.getResultSetList(gongyshangs);
		if(rsl.next()){
			String zhi=rsl.getString("zhi");
			egu.addPaging(Integer.parseInt(zhi));
		}else{//默认每页显示18行
			egu.addPaging(18);
		}
		
		
		egu.getColumn("danwdz").setHidden(true);
		egu.getColumn("faddbr").setHidden(true);
		egu.getColumn("weitdlr").setHidden(true);
		egu.getColumn("youzbm").setHidden(true);
		egu.getColumn("kaihyh").setHidden(true);
		egu.getColumn("zhangh").setHidden(true);
		egu.getColumn("dianh").setHidden(true);
		egu.getColumn("shuih").setHidden(true);
		egu.getColumn("chuanz").setHidden(true);
		egu.getColumn("meitly").setHidden(true);
		egu.getColumn("meiz").setHidden(true);
		egu.getColumn("chubnl").setHidden(true);
		egu.getColumn("kaicnl").setHidden(true);
		egu.getColumn("kaicnx").setHidden(true);
		egu.getColumn("shengcnl").setHidden(true);
		egu.getColumn("gongynl").setHidden(true);
		egu.getColumn("liux").setHidden(true);
		egu.getColumn("yunsfs").setHidden(true);
		egu.getColumn("shiccgl").setHidden(true);
		egu.getColumn("zhongdht").setHidden(true);
		egu.getColumn("yunsnl").setHidden(true);
		egu.getColumn("heznx").setHidden(true);
		egu.getColumn("rongqgx").setHidden(true);
		egu.getColumn("xiny").setHidden(true);
		egu.getColumn("gongsxz").setHidden(true);
		egu.getColumn("kegywfmz").setHidden(true);
		egu.getColumn("kegywfmzzb").setHidden(true);
		egu.getColumn("shifss").setHidden(true);
		egu.getColumn("shangsdz").setHidden(true);
		egu.getColumn("zicbfb").setHidden(true);
		egu.getColumn("shoumbfb").setHidden(true);
		egu.getColumn("qitbfb").setHidden(true);
		egu.getColumn("beiz").setHidden(true);
		
		egu.getColumn("shengfb_id").setEditor(new ComboBox());
		egu.getColumn("shengfb_id").setComboEditor(gridId,
				new IDropDownModel("select id,quanc from shengfb"));
		egu.getColumn("fuid").setEditor(new ComboBox());
		egu.getColumn("fuid").setComboEditor(gridId,new IDropDownModel("select id,mingc from gongysb where leix=1 "));
		egu.getColumn("fuid").setDefaultValue("请选择");
		List rqgx = new ArrayList();
		rqgx.add(new IDropDownBean(1, "优"));
		rqgx.add(new IDropDownBean(2, "良"));
		rqgx.add(new IDropDownBean(3, "中"));
		rqgx.add(new IDropDownBean(4, "差"));
		egu.getColumn("rongqgx").setEditor(new ComboBox());
		egu.getColumn("rongqgx").setComboEditor(gridId,new IDropDownModel(rqgx));
		egu.getColumn("rongqgx").setReturnId(true);
//		egu.getColumn("rongqgx").setDefaultValue("请选择");
		List xy = new ArrayList();
		xy.add(new IDropDownBean(1, "优"));
		xy.add(new IDropDownBean(2, "良"));
		xy.add(new IDropDownBean(3, "中"));
		xy.add(new IDropDownBean(4, "差"));
		
		egu.getColumn("xiny").setEditor(new ComboBox());
		egu.getColumn("xiny").setComboEditor(gridId,
				new IDropDownModel(xy));
		egu.getColumn("xiny").setReturnId(true);
		
		List sfss = new ArrayList();
		sfss.add(new IDropDownBean(1, "是"));
		sfss.add(new IDropDownBean(2, "否"));
		egu.getColumn("shifss").setEditor(new ComboBox());
		egu.getColumn("shifss").setComboEditor(gridId,new IDropDownModel(sfss));
		egu.getColumn("shifss").setReturnId(true);
//		addTbarText("-"); 
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		
		egu.addOtherScript("gridDiv_sm.addListener( \n"
		       + " 'rowselect', function(sm, row, rec) { \n"
		       + "     Ext.getCmp('company-form').getForm().loadRecord(rec); \n"
			   + "    	shengf_x.setValue(rec.get('SHENGFB_ID')); \n"
    		   + "		fuid_x.setValue(rec.get('FUID')); \n"
    		   + "		rongqgx_x.setValue(rec.get('RONGQGX')); \n"
    		   + " 		xiny.setValue(rec.get('XINY')); \n"
    		   + "		shifss.setValue(rec.get('SHIFSS')); \n"
		       + " }); \n gridDiv_sm.singleSelect=true;	\n"
		       );
		
		
		egu.addTbarText("供应商名称:");
		TextField tf = new TextField();
		tf.setId("Gongysmc");
		if(condition.length()>1){
			tf.setValue(getGongysmc());
		}
		egu.addToolbarItem(tf.getScript());
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('Gongysmc').value=Gongysmc.getValue(); document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		if(Duankcx()!=null&&!Duankcx().equals("")){
	         egu.addToolbarItem("{"+new GridButton("添加已有供应商","function(){document.getElementById('GuanlButton').click();}").getScript()+"}");
		}
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, "deletebutton");
//		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("停用",GridButton.ButtonType_SubmitSel, "StopButton");
		egu.addToolbarButton("启用",GridButton.ButtonType_SubmitSel, "BeginButton");
		//egu.addToolbarItem("{"+new GridButton("停用","function(){   document.getElementById('StopButton').click();}").getScript()+"}");
		//egu.addToolbarItem("{"+new GridButton("启用","function(){document.getElementById('BeginButton').click();}").getScript()+"}");
		
//		从添加所选按钮回来给予的提示信息
		if(ToAddMsg.equals("toAdd")){
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("\nvar p=new gridDiv_plant("+ recs[i] +");\ngridDiv_ds.insert("+ i +",p);");
				sb.append("\n").append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
		}
		
		setExtGrid(egu);
		con.Close();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	// //////////////////////////上级单位///////////////////////////////////
	public IDropDownBean getFuidValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit()).setDropDownBean6((IDropDownBean) getFuidModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setFuidValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setFuidModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getFuidModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getFuidModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getFuidModels() {
		String sql = "select id, mingc from gongysb where leix=1 order by xuh,mingc";

		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql, "请选择"));
		return;
	}

	// ////////////////////////////省份///////////////////////////////////////
	public IDropDownBean getShengfb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getShengfb_idModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setShengfb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setShengfb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getShengfb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getShengfb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getShengfb_idModels() {
		String sql = "select id,quanc from shengfb order by xuh,mingc";
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "请选择"));
		return;
	}
	
//	 ////////////////////////////融洽关系///////////////////////////////////////
	public IDropDownBean getRQGXValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit()).setDropDownBean7((IDropDownBean) getRQGXModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setRQGXValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setRQGXModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getRQGXModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getRQGXModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void getRQGXModels() {
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "优"));
		ls.add(new IDropDownBean(2, "良"));
		ls.add(new IDropDownBean(3, "中"));
		ls.add(new IDropDownBean(4, "差"));
		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(ls));
		return;
	}

//	 ////////////////////////////信誉///////////////////////////////////////
	public IDropDownBean getXYValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean) getXYModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setXYValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setXYModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getXYModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getXYModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getXYModels() {
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "优"));
		ls.add(new IDropDownBean(2, "良"));
		ls.add(new IDropDownBean(3, "中"));
		ls.add(new IDropDownBean(4, "差"));
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(ls));
		return;
	}
	
//	 ////////////////////////////是否上市///////////////////////////////////////
	public IDropDownBean getSFSSValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit()).setDropDownBean5((IDropDownBean) getSFSSModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setSFSSValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setSFSSModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getSFSSModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getSFSSModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getSFSSModels() {
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "是"));
		ls.add(new IDropDownBean(2, "否"));
		((Visit) getPage().getVisit()).setProSelectionModel5(new IDropDownModel(ls));
		return;
	}
	
	
	// //////////////////////////////////////////////////////////////////////////
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
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setProSelectionModel6(null);
			visit.setProSelectionModel7(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setDropDownBean4(null);
			visit.setDropDownBean5(null);
			visit.setDropDownBean6(null); 
			visit.setDropDownBean7(null);
//			gridColumns = null;
			setGongysmc(null);
			ToAddMsg=cycle.getRequestContext().getRequest().getParameter("MsgAdd");
			
			if(ToAddMsg==null){
				ToAddMsg="";
			}
			DataSource = visit.getString12();
		}
		getSelectData();
	}

	public ResultSetList getDeleteResultSet(String strchange) {
		ResultSetList rsl = new ResultSetList();
		List list = getElmentList(strchange);
		List cols = getUpdateColumns();
		String[] columnNames = new String[cols.size()];
		for (int i = 0; i < cols.size(); i++) {
			columnNames[i] = ((GridColumn) cols.get(i)).dataIndex;
		}
		rsl.setColumnNames(columnNames);
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if ("D".equals(e.getChild("sign").getText())) {
				String[] newrecord = new String[rsl.getColumnCount()];
				for (int j = 0; j < rsl.getColumnCount(); j++) {
					newrecord[j] = e.getChildText(rsl.getColumnNames()[j]);
				}
				rsl.getResultSetlist().add(newrecord);
			}
		}
		return rsl;
	}

	public ResultSetList getModifyResultSet(String strchange) {
		ResultSetList rsl = new ResultSetList();
		List list = getElmentList(strchange);
		List cols = getUpdateColumns();
		String[] columnNames = new String[cols.size()];
		for (int i = 0; i < cols.size(); i++) {
			columnNames[i] = ((GridColumn) cols.get(i)).dataIndex;
		}
		rsl.setColumnNames(columnNames);
		for (int i = 0; i < list.size(); i++) {
			Element e = (Element) list.get(i);
			if ("U".equals(e.getChildText("sign"))) {
				String[] newrecord = new String[rsl.getColumnCount()];
				for (int j = 0; j < rsl.getColumnCount(); j++) {
					newrecord[j] = e.getChildText(rsl.getColumnNames()[j]);
				}
				rsl.getResultSetlist().add(newrecord);
			}
		}
		return rsl;
	}

	public List getElmentList(String strchange) {
		strchange = getChange(strchange);
		StringReader sr = new StringReader(strchange);
		InputSource is = new InputSource(sr);
		Document doc = null;
		try {
			doc = (new SAXBuilder()).build(is);
		} catch (JDOMException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		Element root = doc.getRootElement();
		return root.getChildren();
	}

	public List getUpdateColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		List uplist = new ArrayList();
		for (int i = 0; i < gridColumns.size(); i++) {
			GridColumn gc = (GridColumn) gridColumns.get(i);
			if (gc.coltype == GridColumn.ColType_default) {
				if (gc.update) {
					uplist.add(gc);
				}
			}
		}
		return uplist;
	}

	public String getChange(String change) {
		change = change.replaceAll("'", "''");
		return change;
	}
}