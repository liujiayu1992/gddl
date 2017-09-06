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
 * ʱ��:2009-05-05 
 * ����:ly 
 * ����:�޸��˱��淽��Save1(String strchange, Visit visit) �������ɾ���ĵ�BUG
 * 
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-03-05
 * ʹ�÷�Χ������������������糧
 * ������ȷ�Ϲ�Ӧ����ӻ��޸�ʱֻ�жϱ��룬���ƺ�ȫ���Ƿ����ظ���
 */
public class Gongysxg extends BasePage implements PageValidateListener {
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
//	ϵͳ��־���е�״̬�ֶ�
	private static  final String ZhangTConstant1="�ɹ�";
	private static  final String ZhangTConstant2="ʧ��";
//	ϵͳ��־���е�����ֶ�
	private static  final String leiBConstant="��Ӧ��ά��";
	
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
	
	// ҳ��仯��¼
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
    //ͣ��
    private boolean _StopClick = false;
    public void StopButton(IRequestCycle cycle){
    	_StopClick=true;
    }
    //����
    private boolean _BeginClick = false;
    public void BeginButton(IRequestCycle cycle){
    	_BeginClick=true;
    }
//  �п��ܴӷ��ذ�ť���ر�ҳ�棬Ҳ�����Ǵ������ѡ��ť���ر�ҳ�棬�������
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
	 *            ����grid��Id ����󶨵�DIV��id
	 * 
	 */
	public void ExtGridUtil(String gridId) {
		this.gridId = gridId;
	}

	/**
	 * @param gridId
	 *            ����grid��Id ����󶨵�DIV��id
	 * @param sql
	 *            ȡ���ķ���
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
	 * @return ����grid������
	 */
	public int getGridType() {
		return gridType;
	}

	/**
	 * @param gridType
	 *            ����grid������ Gridstyle_Read,Gridstyle_Edit
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
	 *            grid�Ŀ��
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
	 * @return ����ҳ������
	 */
	public int getPagSize() {
		return pagsize;
	}

	/**
	 * @param pagsize
	 *            ҳ������ ���÷�ҳ
	 */
	public void addPaging(int pagsize) {
		this.pagsize = pagsize;
	}

	/**
	 * @return ����grid ����ѡ��ģʽ
	 */
	public int getGridSelModel() {
		return gridsm;
	}

	/**
	 * @param selmodel
	 *            grid��ѡ��ģʽ
	 *            GridselModel_None,GridselModel_Row,GridselModel_Check
	 */
	public void setGridSelModel(int selmodel) {
		gridsm = selmodel;
	}

	/**
	 * @return �����еļ���
	 */
	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	/**
	 * @param column
	 *            ����һ��
	 */
	public void addColumn(GridColumn column) {
		getGridColumns().add(column);
	}

	/**
	 * @param colindex
	 *            ������
	 * @return ����һ��
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
	 * @return ���ذ�ť�ļ���
	 */
	// public List getGridButtons() {
	// if(gridbuttons == null) {
	// gridbuttons = new ArrayList();
	// }
	// return gridbuttons;
	// }
	/**
	 * @param gridButtonType
	 *            ��ť����
	 * @param tapestryBtnId
	 *            ��Ӧ��tapestry��ťID
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
	 * ����grid��ȫ������
	 * 
	 * @param data
	 *            String[][]
	 */
	public void setData(String[][] data) {
		griddata = data;
	}

	/**
	 * ��ʼ��Data����
	 * 
	 * @param rows
	 *            ����
	 */
	public void initData(int rows, int cols) {
		griddata = new String[rows][cols];
	}

	/**
	 * ����������
	 * 
	 * @param rowIndex
	 *            ���±� 0 to ...
	 * @param data
	 *            String[] ����
	 */
	public void setRowData(int rowIndex, String[] data) {
		if (rowIndex >= griddata.length) {
			// �쳣
			return;
		}
		griddata[rowIndex] = data;
	}

	/**
	 * ����ÿ����Ԫ���ֵ
	 * 
	 * @param rowIndex
	 *            �б� 0 to ...
	 * @param colIndex
	 *            �б� 0 to ...
	 * @param data
	 *            Stirng ֵ
	 */
	public void setDataValue(int rowIndex, int colIndex, String data) {
		if (rowIndex >= griddata.length) {
			// �쳣
			return;
		}
		if (colIndex >= griddata[rowIndex].length) {
			// �쳣
			return;
		}
		griddata[rowIndex][colIndex] = data;
	}

//	/**
//	 * @return ���ر�ͷ��Script
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
//	 * @return ����������¼��Script
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
	 * @return ����Toolbar ��Script
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
	 * @return �������ݵ�Script
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
//	 * @return �������ݵĽ���Script
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
	 * @return ���ط�ҳ��Script
	 */
	public String getPagingScript() {
		StringBuffer pagScript = new StringBuffer();
		pagScript.append("var ").append(gridId).append("_pag").append(" = new Ext.PagingToolbar({ \n");
		pagScript.append("pageSize: ").append(getPagSize()).append(",\n");
		pagScript.append("store: ").append(gridId).append("_ds").append(",\n");
		pagScript.append("displayInfo: true,\n");
		pagScript.append("displayMsg: '��ʾ�� {0} ���� {1} ����¼��һ�� {2} ��',\n");
		pagScript.append("emptyMsg: 'û�м�¼' });\n");
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
	//���÷���
	private void Begin(){
		Visit visit=(Visit) this.getPage().getVisit();
		Begin1(getChange(),visit);
	}
	//ͣ�÷���
	private  void Stop(){
		Visit visit = (Visit) this.getPage().getVisit();
		Stop1(getChange(), visit); 
	}
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit); 
	}
	
//  �ж϶˿ڵ�ַʱ������
	private String zhi = "";
	public String Duankcx(){
		 zhi = "";
		 JDBCcon con = new JDBCcon();
		 String sql = "select zhi from xitxxb where mingc = '��ڵ�ַ'";
		 ResultSetList rsl = con.getResultSetList(sql);
		 while(rsl.next()){
			 zhi = rsl.getString("zhi");
		 }
		 con.Close();
		 return zhi;
	}
	
//	�ж������Ƿ��ڱ��ؿ����Ѿ�����(�����ڷ���0�����ڷ�������)
	private int Shujpd(JDBCcon con,String sql){
		return JDBCcon.getRow(con.getResultSet(sql));
	}
	
//	ɾ���¼���˾��Ӧ�̵��ϼ������ֶ�
	private void DeleteXiaj(JDBCcon con,String tableName,String bianm,String operation){
		
		InterCom_dt dt=new InterCom_dt();
		String sql=" select  d.id,d.mingc  from diancxxb d where d.fuid="+((Visit)(this.getPage().getVisit())).getDiancxxb_id();
		
		ResultSetList mdrsl=con.getResultSetList(sql);
		
		//���¼��糧��λ����Ҫ����
		if(mdrsl.getRows()!=0){
			int count_success=0;
			int count_fail=0;
			String s="";
			while(mdrsl.next()){
				
				String diancxxb_id=mdrsl.getString("ID");
				String[] sqls=new String[]{"update gongysb set SHANGJGSBM='' where SHANGJGSBM='"+bianm+"'"};
				String[] answer=dt.sqlExe(diancxxb_id, sqls, true);
				
				if(answer[0].equals("true")){  //�����¼��ֶγɹ�
					count_success++;
					s+="��λ"+mdrsl.getString("MINGC")+"���³ɹ�;";
				}else{
					count_fail++;
					s+="��λ"+mdrsl.getString("MINGC")+"����ʧ��;";
				}
				
			}
			
			if(count_fail>0){
				zhuangT=ZhangTConstant2;
			}else{
				zhuangT=ZhangTConstant1;
			}
			logMsg+=operation+"��"+tableName+"�б���"+bianm.replaceAll("'","")+"�����¼�"+mdrsl.getRows()+"��;"+s+",�ܹ��ɹ�:"+count_success+"����ʧ��:"+count_fail+"��;";
			
		}
		
	}
	
	private void GengGSJBM(JDBCcon con,String tableName,String bianm,String operation,String oldBianM ){
		InterCom_dt dt=new InterCom_dt();
		
		String sql=" select  d.id,d.mingc  from diancxxb d where d.fuid="+((Visit)(this.getPage().getVisit())).getDiancxxb_id();
		
		ResultSetList mdrsl=con.getResultSetList(sql);
		
		//���¼��糧��λ����Ҫ����
		if(mdrsl.getRows()!=0){
			
			int count_success=0;
			int count_fail=0;
			String s="";
			while(mdrsl.next()){
				
				String diancxxb_id=mdrsl.getString("ID");
				String[] sqls=new String[]{"update gongysb set SHANGJGSBM='"+bianm.replaceAll("'", "")+"' where SHANGJGSBM='"+oldBianM+"'"};
				String[] answer=dt.sqlExe(diancxxb_id, sqls, true);
				
				if(answer[0].equals("true")){  //�����¼��ֶγɹ�
					count_success++;
					s+="��λ"+mdrsl.getString("MINGC")+"���³ɹ�;";
				}else{
					count_fail++;
					s+="��λ"+mdrsl.getString("MINGC")+"����ʧ��;";
				}
				
			}
			
			if(count_fail>0){
				zhuangT=ZhangTConstant2;
			}else{
				zhuangT=ZhangTConstant1;
			}
			
			logMsg+=operation+"��"+tableName+"�б���"+bianm.replaceAll("'","")+"�����¼�"+mdrsl.getRows()+"��;"+s+",�ܹ��ɹ�:"+count_success+"����ʧ��:"+count_fail+"��;";
		}
	}
	
//	��־��¼
	private String logMsg="";
	private String zhuangT="";
	private void WriteLog(JDBCcon con){
			
			Visit visit=(Visit)this.getPage().getVisit();
			
			if(!logMsg.equals("")){//��Ϊ�գ���Ҫд����־��¼
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

//	������һ����¼ʱ��֪ͨ�ϼ�������
	private void DaiPF(){
		
	}
	//���÷���
	public void Begin1(String strchange,Visit visit){
		JDBCcon con=new JDBCcon();
		ResultSetList uprsl=visit.getExtGrid1().getModifyResultSet(strchange);
		while(uprsl.next()){
			String sql="update gongysb set zhuangt=1 where id='"+uprsl.getString(0)+"'";
			int i=con.getUpdate(sql);
			if(i!=-1){
				setSaveMsg("���óɹ�!");
			}
			else{
				setSaveMsg("����ʧ��!");
			}
		}
		uprsl.close();
		con.Close();
	}
	//ͣ�÷���
	public void Stop1(String strchange,Visit visit){
		JDBCcon con=new JDBCcon();
		ResultSetList uprsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while(uprsl.next()){
			String sql="update gongysb set zhuangt=0 where id='"+uprsl.getString(0)+"'";
			int i=con.getUpdate(sql);
			if(i!=-1){
				setSaveMsg("ͣ�óɹ���");
			}
			else{
				setSaveMsg("ͣ��ʧ�ܣ�");
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
			if(i!=-1){  //�����ɹ�����¼��־
				this.DeleteXiaj(con, tableName, delrsl.getString("BIANM"), "ɾ��");
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
				ResultSetList weis = con.getResultSetList("select zhi from xitxxb where mingc='��Ӧ�̱���λ��' and zhuangt=1");
				if(weis.next()){
					if(mdrsl.getString("bianm").length()<weis.getInt("zhi")){
						SaveMsgLocal="��Ӧ�̱���λ��С��"+weis.getInt("zhi")+",�밴�������±��롣";
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
							SaveMsgLocal+="--ʡ��:"+mdrsl.getString("SHENGFB_ID");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(SHENGFB_ID);
						}
					}else if(mdrsl.getColumnNames()[i].equals("RONGQGX")){   
						long RONGQGX=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("RONGQGX"));
						if(mdrsl.getString("RONGQGX")==null || mdrsl.getString("RONGQGX").equals("")){
							sql2.append(",").append("''");
						}else if(RONGQGX==-1L){
							SaveMsgLocal+="--��Ǣ��ϵ:"+mdrsl.getString("RONGQGX");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("XINY")){
						long XINY=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("XINY"));
						if(mdrsl.getString("XINY")==null || mdrsl.getString("XINY").equals("")){
							sql2.append(",").append("''");
						}else if(XINY==-1L){
							SaveMsgLocal+="--����:"+mdrsl.getString("XINY");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("SHIFSS")){
						long SHIFSS=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("SHIFSS"));
						if(mdrsl.getString("XINY")==null || mdrsl.getString("SHIFSS").equals("")){
							sql2.append(",").append("''");
						}else if(SHIFSS==-1L){
							SaveMsgLocal+="--����:"+mdrsl.getString("SHIFSS");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(getValueSql(visit.getExtGrid1().getColumn(mdrsl.getColumnNames()[i]), mdrsl.getString(i)));
						}
					}else if(mdrsl.getColumnNames()[i].equals("CHENGSB_ID")){
						long CHENGSB_ID=(getExtGrid().getColumn(mdrsl.getColumnNames()[i]).combo).getBeanId(mdrsl.getString("CHENGSB_ID"));
						if(mdrsl.getString("CHENGSB_ID")==null || mdrsl.getString("CHENGSB_ID").equals("")){
							sql2.append(",").append("''");
						}else if(CHENGSB_ID==-1L){
							SaveMsgLocal+="--����:"+mdrsl.getString("CHENGSB_ID");
							sql2.append(",").append("''");
						}else{
							sql2.append(",").append(CHENGSB_ID);
						}
					}else if(mdrsl.getColumnNames()[i].equals("SHANGJGSBM")&&(Duankcx()==null||Duankcx().equals(""))){ 
						sql2.append(",").append(mdrsl.getString("BIANM"));
					}else {//�жϼ�¼ʱ�����ظ�
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
				
				//��SHANGJGSBM��Ϊ��ʱ��˵���Ǵ��ϼ�����Ĺ�Ӧ�̣�������������ʹ��
				if(!mdrsl.getString("SHANGJGSBM").equals("")){
					if(this.Shujpd(con, sql_check + ") and leix = 1")!=0){  //�ڱ��ؿ����Ѿ�����,Ҫ���еĲ���
						SaveMsgLocal+="----------------��Ӽ�¼-----------<br>--����:"+bianm.replaceAll("'", "")+"---����:"+mingc.replaceAll("'","")+"---ȫ��:"+quanc.replaceAll("'","")+"--�ļ�¼���ظ�,���ܱ���!<br>";
					}else if(SaveMsgLocal.equals("")){
						sql.append(") values(").append(gys_id).append(sql2).append(")\n");
						int flag=con.getInsert(sql.toString());
						//����ʱ����Ҫ��¼��־
						if(flag==-1){  //���벻�ɹ�
		//					this.WriteLog();
						}else{        //����ɹ� ����gongysdcglb��
							String id = MainGlobal.getNewID(visit.getDiancxxb_id());
							long diancxxb_id = visit.getDiancxxb_id();
							sql_dc.append("insert into gongysdcglb values(")
								.append(id).append(",").append(diancxxb_id).append(",").append(gys_id).append(")");
							con.getInsert(sql_dc.toString());
							
						}
					}
				}else{
					if(this.Shujpd(con, sql_check + ") and leix = 1")!=0){  //�ڱ��ؿ����Ѿ�����,Ҫ���еĲ���
						SaveMsgLocal+="----------------��Ӽ�¼-----------<br>--����:"+bianm.replaceAll("'","")+"---����:"+mingc.replaceAll("'","")+"---ȫ��:"+quanc.replaceAll("'","")+"--�ļ�¼���ظ�,���ܱ���!<br>";
					}else if(SaveMsgLocal.equals("")){
						sql.append(") values(").append(gys_id).append(sql2).append(")\n");
						int flag=con.getInsert(sql.toString());
						if(flag==-1){  //���벻�ɹ�
						}else{        //����ɹ�
							this.DaiPF();   //����һ���µļ�¼ʱ��֪ͨ�ϼ���λ��Ϊ������״̬
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
				//��update�����в���Ҫ�����ж��е��ֶ��Ƿ����
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
						if (mdrsl.getString("fuid").equals("��ѡ��")|| mdrsl.getString("fuid").equals("")) {
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
						if(mdrsl.getString("ZHUANGT").equals("����")){
							sql.append(1).append(",");
						}else if(mdrsl.getString("ZHUANGT").equals("ͣ��")){
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
				
				if(flag!=0){   //���µ������ڱ��ؿ����Ѿ�����
//					this.WriteLog();
					strSaveMsg+="----------------���ļ�¼-----------<br>--����:"+bianm.replaceAll("'","")+"---����:"+mingc.replaceAll("'","")+"---ȫ��:"+quanc.replaceAll("'","")+"--�ļ�¼���ظ�,���ܱ���<br>!";
				}else if(visit.isDCUser()&&!visit.isFencb()){
					con.getUpdate(sql.toString());
				}else{
					
					String sql_change=" select bianm from "+tableName+" where id="+mdrsl.getString("ID");
					String oldValue=bianM_Change(con,sql_change);   //tΪtrue˵����Ҫ���ģ����ýӿ�
					int k=con.getUpdate(sql.toString());
					if(k!=0){  //�������ݳɹ�
						if(oldValue!=null && bianm!=null &&  !oldValue.equals(bianm.replaceAll("'", ""))){
		//					this.GengGXiaj(); 
							this.GengGSJBM(con, tableName, bianm, "����", oldValue);
						}
						  //ͨ���жϸ���bianm�ֶ�ǰ��ı仯������ж��Ƿ�����¼���λ���ϼ������ֶ�
//						this.WriteLog();
					}else{  //���ɹ�
//						this.WriteLog();
					}
				}
			}
		}
		
		this.WriteLog(con);
		if(strSaveMsg.equals("")){
			strSaveMsg="���³ɹ�";
			ToAddMsg="";  //���ĺ��ʶ���ϼ������ҳ�治����ʾ
			this.setSaveMsg(strSaveMsg);
		}else{
			setSaveMsg(strSaveMsg);
			ToAddMsg="";
		}
		
//		tiShi=true;
	}
	
//	��ѯ����ֵ������id�����ж�ǰ���Ƿ�ı䣬�����ж��Ƿ���Ҫ�����¼���λ���ϼ������ֶ�
	public String bianM_Change(JDBCcon con,String sql){
		
		ResultSetList rsl=con.getResultSetList(sql);
		String oldValue=null;
		if(rsl.next()){
			oldValue=rsl.getString("bianm");
		}
		
		 //��Ҫ���ýӿڸ����¼���λ���ϼ������ֶ�
		
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
				+ "                 WHERE ID = g.fuid), NULL, '��ѡ��',\n"
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
				+ "		  g.shangjgsbm,\n"+ " decode(g.zhuangt,1,'����',0,'ͣ��') as zhuangt,"
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
		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(125);
		egu.getColumn("mingc").setHeader("���");
		egu.getColumn("mingc").setWidth(180);
		egu.getColumn("piny").setHeader("ƴ��");
		egu.getColumn("piny").setWidth(80);
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(180);
		egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("fuid").setHeader("������");
		egu.getColumn("fuid").setWidth(180);
		egu.getColumn("shangjgsbm").setHeader("�ϼ���λ����");
		egu.getColumn("shangjgsbm").setWidth(120);
		egu.getColumn("zhuangt").setHeader("״̬");
		egu.getColumn("zhuangt").setWidth(80);
		egu.getColumn("zhuangt").setDefaultValue("1");
		egu.getColumn("danwdz").setHeader("��λ��ַ");
		egu.getColumn("faddbr").setHeader("����������");
		egu.getColumn("weitdlr").setHeader("ί�д�����");
		egu.getColumn("youzbm").setHeader("��������");
		egu.getColumn("kaihyh").setHeader("��������");
		egu.getColumn("zhangh").setHeader("�˺�");
		egu.getColumn("dianh").setHeader("�绰");
		egu.getColumn("shuih").setHeader("˰��");
		egu.getColumn("chuanz").setHeader("����");
		egu.getColumn("meitly").setHeader("ú̿��Դ");
		egu.getColumn("meiz").setHeader("ú��");
		egu.getColumn("chubnl").setHeader("��������(���)");
		egu.getColumn("kaicnl").setHeader("��������(���)");
		egu.getColumn("kaicnx").setHeader("��������(��)");
		egu.getColumn("shengcnl").setHeader("��������(���)");
		egu.getColumn("gongynl").setHeader("��Ӧ����(���)");
		egu.getColumn("liux").setHeader("����");
		egu.getColumn("yunsfs").setHeader("���䷽ʽ");
		egu.getColumn("shiccgl").setHeader("�г��ɹ���(���)");
		egu.getColumn("zhongdht").setHeader("�ص��ͬ(���)");
		egu.getColumn("yunsnl").setHeader("��������(���)");
		egu.getColumn("heznx").setHeader("��������(��)");
		egu.getColumn("rongqgx").setHeader("��Ǣ��ϵ");
		egu.getColumn("xiny").setHeader("����");
		egu.getColumn("gongsxz").setHeader("��˾����");
		egu.getColumn("kegywfmz").setHeader("�ɹ�ú��");
		egu.getColumn("kegywfmzzb").setHeader("�ɹ�ú��ָ��");
		//egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shifss").setHeader("�Ƿ�����");
		egu.getColumn("shangsdz").setHeader("���е�ַ");
		egu.getColumn("zicbfb").setHeader("�Բ�����");
		egu.getColumn("shoumbfb").setHeader("��ú����");
		egu.getColumn("qitbfb").setHeader("��������");
		egu.getColumn("beiz").setHeader("��ע");
		
		String gongyshangs="select zhi from xitxxb where mingc='��Ӧ��ά��ҳ�浥ҳ��ʾ������' and zhuangt=1";
		rsl = con.getResultSetList(gongyshangs);
		if(rsl.next()){
			String zhi=rsl.getString("zhi");
			egu.addPaging(Integer.parseInt(zhi));
		}else{//Ĭ��ÿҳ��ʾ18��
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
		egu.getColumn("fuid").setDefaultValue("��ѡ��");
		List rqgx = new ArrayList();
		rqgx.add(new IDropDownBean(1, "��"));
		rqgx.add(new IDropDownBean(2, "��"));
		rqgx.add(new IDropDownBean(3, "��"));
		rqgx.add(new IDropDownBean(4, "��"));
		egu.getColumn("rongqgx").setEditor(new ComboBox());
		egu.getColumn("rongqgx").setComboEditor(gridId,new IDropDownModel(rqgx));
		egu.getColumn("rongqgx").setReturnId(true);
//		egu.getColumn("rongqgx").setDefaultValue("��ѡ��");
		List xy = new ArrayList();
		xy.add(new IDropDownBean(1, "��"));
		xy.add(new IDropDownBean(2, "��"));
		xy.add(new IDropDownBean(3, "��"));
		xy.add(new IDropDownBean(4, "��"));
		
		egu.getColumn("xiny").setEditor(new ComboBox());
		egu.getColumn("xiny").setComboEditor(gridId,
				new IDropDownModel(xy));
		egu.getColumn("xiny").setReturnId(true);
		
		List sfss = new ArrayList();
		sfss.add(new IDropDownBean(1, "��"));
		sfss.add(new IDropDownBean(2, "��"));
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
		
		
		egu.addTbarText("��Ӧ������:");
		TextField tf = new TextField();
		tf.setId("Gongysmc");
		if(condition.length()>1){
			tf.setValue(getGongysmc());
		}
		egu.addToolbarItem(tf.getScript());
		
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('Gongysmc').value=Gongysmc.getValue(); document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		if(Duankcx()!=null&&!Duankcx().equals("")){
	         egu.addToolbarItem("{"+new GridButton("������й�Ӧ��","function(){document.getElementById('GuanlButton').click();}").getScript()+"}");
		}
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, "deletebutton");
//		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarButton("ͣ��",GridButton.ButtonType_SubmitSel, "StopButton");
		egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel, "BeginButton");
		//egu.addToolbarItem("{"+new GridButton("ͣ��","function(){   document.getElementById('StopButton').click();}").getScript()+"}");
		//egu.addToolbarItem("{"+new GridButton("����","function(){document.getElementById('BeginButton').click();}").getScript()+"}");
		
//		�������ѡ��ť�����������ʾ��Ϣ
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

	// //////////////////////////�ϼ���λ///////////////////////////////////
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

		((Visit) getPage().getVisit()).setProSelectionModel6(new IDropDownModel(sql, "��ѡ��"));
		return;
	}

	// ////////////////////////////ʡ��///////////////////////////////////////
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
		((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql, "��ѡ��"));
		return;
	}
	
//	 ////////////////////////////��Ǣ��ϵ///////////////////////////////////////
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
		ls.add(new IDropDownBean(1, "��"));
		ls.add(new IDropDownBean(2, "��"));
		ls.add(new IDropDownBean(3, "��"));
		ls.add(new IDropDownBean(4, "��"));
		((Visit) getPage().getVisit()).setProSelectionModel7(new IDropDownModel(ls));
		return;
	}

//	 ////////////////////////////����///////////////////////////////////////
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
		ls.add(new IDropDownBean(1, "��"));
		ls.add(new IDropDownBean(2, "��"));
		ls.add(new IDropDownBean(3, "��"));
		ls.add(new IDropDownBean(4, "��"));
		((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(ls));
		return;
	}
	
//	 ////////////////////////////�Ƿ�����///////////////////////////////////////
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
		ls.add(new IDropDownBean(1, "��"));
		ls.add(new IDropDownBean(2, "��"));
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
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
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} catch (IOException e) {
			// TODO �Զ����� catch ��
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