package com.zhiren.dc.huaygl.huaysh.rulhysh;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/*
 * ���ߣ�����ƽ
 * ʱ�䣺2009-11-27
 * ��������¯�������sql��Ӹ���rulmzlb��diancxxb_id���ˡ� 
 * 
 * ���ߣ�������
 * ʱ�䣺2009-12-04
 * �������������diancxxb_id���˵�ʱ�����ܳ����治�ܿ��ֳ����ݵ�����
 * ��xitxxb��������ã�
 * insert into xitxxb (id,xuh,diancxxb_id,mingc,zhi,leib,zhuangt,beiz) 
 * 	values(getnewid(232),1,232,'��¯����Ƿ����diancxxb_id����','��','��¯���',1,'ʹ��');
 */
/*
 *���ߣ����
 *ʱ�䣺2013-07-06
 *���ݣ���˽�����ֵǰ������ֵ����
 *		���������ʾBUG
 */
/*
 *���ߣ����
 *ʱ�䣺2013-07-12
 *���ݣ�ʹ��MainGlobal.getXitxx_item("��¯", "��¯��������Ƿ���Ҫ��¯ú��", "0", "��")
 *		�ж���¯��������Ƿ���Ҫ��¯ú������ʾ��¼
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-11-26
 * ������������Դ��ʾ���ƽ�Qgad,daf���ΪQgar,daf
 */
public class Rulhysh2 extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

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
	
	private boolean xiansztq = true;

	private boolean xiansztl = true;

	public void ExtGridUtil() {
	}

	public void ExtGridUtil(String gridId, ResultSetList rsl) {
		this.gridId = gridId;
		addColumn(new GridColumn(GridColumn.ColType_Rownum));
		for (int i = 0; i < rsl.getColumnNames().length; i++) {
			GridColumn gc = new GridColumn(rsl.getColumnNames()[i], rsl
					.getColumnNames()[i]);
			if ("id".equals(rsl.getColumnNames()[i].toLowerCase())) {
				gc.setDefaultValue("0");
				gc.setHidden(true);
			} else {
				String extType = OratypeOfExt(rsl.getColumnTypes()[i]);
				gc.setDataType(extType);
				if ("float".equals(extType)) {
					gc.setEditor(new NumberField());
					((NumberField) gc.editor).setDecimalPrecision(rsl
							.getColScales()[i]);
				} else if ("string".equals(extType)) {
					gc.setEditor(new TextField());
					((TextField) gc.editor)
							.setMaxLength(rsl.getColPrecisions()[i]);
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
	 * @return ����������¼��Script
	 */
	private String getPlantScript() {
		StringBuffer plantScript = new StringBuffer();
		plantScript.append("var ").append(gridId).append(
				"_plant = Ext.data.Record.create([");
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
				GridColumn gc = ((GridColumn) getGridColumns().get(c));
				plantScript.append("{name: '").append(gc.dataIndex).append(
						"', type:'").append(gc.datatype).append("'},");
			}
		}
		plantScript.deleteCharAt(plantScript.length() - 1);
		plantScript.append("]);\n");
		return plantScript.toString();
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
			if (coldataIndex.toUpperCase().equals(
					getColumn(i).dataIndex.toUpperCase())) {
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
	 * @param gridButtonType
	 *            ��ť����
	 * @param tapestryBtnId
	 *            ��Ӧ��tapestry��ťID
	 */
	public void addToolbarButton(String text, int gridButtonType,
			String tapestryBtnId, String condition, String icon) {
		GridButton gb;
		if (condition != null) {
			gb = new GridButton(text, gridButtonType, gridId, getGridColumns(),
					tapestryBtnId, condition);
		} else {
			gb = new GridButton(text, gridButtonType, gridId, getGridColumns(),
					tapestryBtnId);
		}
		if (icon != null) {
			gb.setIcon(icon);
		}
		addTbarBtn(gb);
	}
	public void addToolbarButton(String text, int gridButtonType,
			String tapestryBtnId) {
		GridButton gb = new GridButton(text, gridButtonType, gridId,
				getGridColumns(), tapestryBtnId);
		addTbarBtn(gb);
	}

	public void addTbarBtn(GridButton gb) {
		addToolbarItem("{" + gb.getScript() + "}");
	}

	public void addTbarText(String text) {
		addToolbarItem("'" + text + "'");
	}

	public void addToolbarItem(String item) {
		StringBuffer sb = new StringBuffer();
		sb.append(getTbar()).append(item).append(",");
		setTbar(sb.toString());
	}

	/**
	 * @return �������ݵĽ���Script
	 */
	private String getDsScript() {
		StringBuffer dsScript = new StringBuffer();
		dsScript.append("var " + gridId + "_ds = new Ext.data.Store({\n");
		dsScript.append("proxy : new Ext.zr.data.PagingMemoryProxy(" + gridId
				+ "_data),\n");
		dsScript.append("reader: new Ext.data.ArrayReader({}, [\n");
		for (int c = 0; c < getGridColumns().size(); c++) {
			if (((GridColumn) getGridColumns().get(c)).coltype == 0) {
				dsScript.append("{name:'");
				dsScript
						.append(((GridColumn) getGridColumns().get(c)).dataIndex);
				dsScript.append("'},");
			}
		}
		dsScript.deleteCharAt(dsScript.length() - 1);
		dsScript.append("])");
		dsScript.append("});\n");
		return dsScript.toString();
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

	/**
	 * @return �����еļ���
	 */
	public List getGridColumns() {
		if (gridColumns == null) {
			gridColumns = new ArrayList();
		}
		return gridColumns;
	}

	private String getColumnsScript() {
		StringBuffer columnsScript = new StringBuffer();
		columnsScript.append("var ").append(gridId).append(
				"_cm = new Ext.grid.ColumnModel([");
		for (int c = 0; c < getGridColumns().size(); c++) {
			GridColumn Gc = (GridColumn) getGridColumns().get(c);

			columnsScript.append(Gc.getScript());
			columnsScript.append(",");
		}
		columnsScript.deleteCharAt(columnsScript.length() - 1);
		columnsScript.append("]);\n ").append(gridId).append(
				"_cm.defaultSortable=false;\n");
		return columnsScript.toString();
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
	public String getChange(String change) {
		change = change.replaceAll("'", "''");
		return change;
	}



	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}
	private void Huit(){
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
		
	}
	public void Save1(String strchange, Visit visit) {
		String tableName = "rulmzlb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

//		ResultSetList delrsl = visit.getExtGrid1()
//				.getDeleteResultSet(strchange);
//		while (delrsl.next()) {
//			sql.append("delete from ").append(tableName).append(" where id =")
//					.append(delrsl.getString(0)).append(";\n");
//		}

		ResultSetList mdrsl = getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("5").append(",");
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	public void Save2(String strchange, Visit visit) {
		String tableName = "rulmzlb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

//		ResultSetList delrsl = visit.getExtGrid1()
//				.getDeleteResultSet(strchange);
//		while (delrsl.next()) {
//			sql.append("delete from ").append(tableName).append(" where id =")
//					.append(delrsl.getString(0)).append(";\n");
//		}

		ResultSetList mdrsl =getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("1").append(",");
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
	}

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
	

	public void getSelectData() {
	    Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		tbars = "";
		gridColumns = null;
		ResultSetList rs = new ResultSetList();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ��¯������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			if (rs.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ��¯������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			if (rs.getString("zhi").equals("��")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}
		
		
//		��¯��������Ƿ�ֻ��ʾ�����ԭʼָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),Ĭ��Ϊȫ����ʾ
		 boolean IsShow=false;
		sql = "select zhi from xitxxb where mingc = '��¯��������Ƿ�ֻ��ʾ�������Ҫָ��'  and zhuangt = 1  ";
		rs = con.getResultSetList(sql);
		while (rs.next()) {
			if (rs.getString("zhi").equals("��")) {
				IsShow = true;
			} else {
				IsShow = false;
			}
		}
		//�Ƿ����diancxxb_id����
		String IsGulsql = "select zhi from xitxxb where mingc = '��¯����Ƿ����diancxxb_id����'  and zhuangt = 1";
		String IsGul = "";
		rs = con.getResultSetList(IsGulsql);
		while (rs.next()) {
			if (rs.getString("zhi").equals("��")) {
				IsGul = "      and r.diancxxb_id="+ visit.getDiancxxb_id()+" \n";
			}
		}
		
		String meil="";
		if(MainGlobal.getXitxx_item("��¯", "��¯��������Ƿ���Ҫ��¯ú��", "0", "��").equals("��")){
			meil="and r.meil!=0 \n";
		}
		
		String ssql = null;
		ssql = "select r.id,to_char(r.rulrq,'yyyy-mm-dd')as rulrq,to_char(r.fenxrq,'yyyy-mm-dd') as fenxrq,rb.mingc as rulbzb_id ," +
		"      j.mingc as jizfzb_id,r.meil,r.mt,r.mad,r.aad,r.ad,r.vad,r.vdaf,r.stad,r.std,r.had,r.fcad," +
                "r.qbad, r.qgrad,r.qnet_ar,round_new(r.qnet_ar/4.1816*1000,0) rezk,r.aar,\n" +
		"     r.hdaf,r.sdaf,r.var,r.har,\n" +
		"      r.qgrd,r.qgrad_daf,r.huayy,r.lury,to_char(r.lursj,'yyyy-mm-dd') as lursj,r.bianm,r.beiz\n" + 
		"      from rulmzlb r  ,rulbzb rb  ,jizfzb j \n" + 
		"      where r.rulbzb_id=rb.id and r.jizfzb_id=j.id \n" + 
			   IsGul+
		"	   and r.shenhzt=3 \n"+
		meil +
		"	   order by r.rulrq,j.mingc,rb.xuh";
		ResultSetList rsl = con
				.getResultSetList(ssql);
		ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		 setTableName("rulmzlb");
		//  ����ҳ�����Ա����ӹ�����
//		 setWidth(1000);
		// /������ʾ������
		 getColumn("id").setHidden(true);
		 getColumn("id").setEditor(null);
		 getColumn("rulrq").setEditor(null);
		 getColumn("rulrq").setHeader("��¯����");
		 getColumn("fenxrq").setEditor(null);
		 getColumn("fenxrq").setHeader("��������");
		 getColumn("rulbzb_id").setHeader("��¯����");
		 getColumn("rulbzb_id").setEditor(null);
		 getColumn("jizfzb_id").setHeader("����");
		 getColumn("jizfzb_id").setEditor(null);
        getColumn("meil").setHeader("ú��");
        getColumn("meil").setEditor(null);
        getColumn("mt").setHeader("Mt(%)");
        getColumn("mt").setEditor(null);
        getColumn("aad").setEditor(null);
        getColumn("mad").setHeader("Mad(%)");
		 getColumn("rezk").setHeader("Qnet,ar(Kcal/kg)");
		 getColumn("rezk").setUpdate(false);
		 getColumn("rezk").setEditor(null);
		 getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		 getColumn("qnet_ar").setEditor(null);
		 getColumn("aar").setHeader("Aar(%)");
		 getColumn("aar").setEditor(null);
		 getColumn("ad").setHeader("Ad(%)");
		 getColumn("ad").setEditor(null);
		 getColumn("vdaf").setHeader("Vdaf(%)");
		 getColumn("vdaf").setEditor(null);

		 getColumn("stad").setHeader("St,ad(%)");
		 getColumn("stad").setEditor(null);
		 getColumn("aad").setHeader("Aad(%)");

		 getColumn("mad").setEditor(null);
		 getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
		 getColumn("qbad").setEditor(null);
		 getColumn("had").setHeader("Had(%)");
		 getColumn("had").setEditor(null);
		 getColumn("vad").setHeader("Vad(%)");
		 getColumn("vad").setEditor(null);
		 getColumn("fcad").setHeader("FCad(%)");
		 getColumn("fcad").setEditor(null);
		 getColumn("std").setHeader("St,d(%)");
		 getColumn("std").setEditor(null);
		 getColumn("qgrad").setHeader("Qgr,ad(Mj/kg)");
		 getColumn("qgrad").setEditor(null);
		 getColumn("hdaf").setHeader("Hdaf(%)");
		 getColumn("hdaf").setEditor(null);
		 if (xiansztl==false) {
			 getColumn("stad").setHidden(true);
			 getColumn("std").setHidden(true);
			 getColumn("sdaf").setHidden(true);
		 }
		 if(xiansztq==false){
			 getColumn("hdaf").setHidden(true);
			 getColumn("har").setHidden(true);
			 getColumn("had").setHidden(true);
		 }
		 getColumn("qgrad_daf").setHeader("Qgar,daf(Mj/kg)");
		 getColumn("qgrad_daf").setEditor(null);
		 getColumn("sdaf").setHeader("Sdaf(%)");
		 getColumn("sdaf").setEditor(null);
		 getColumn("var").setHeader("Var(%)");
		 getColumn("var").setEditor(null);
		 getColumn("har").setHeader("Har(%)");
		 getColumn("har").setEditor(null);
		 getColumn("qgrd").setHeader("Qgrd(%)");
		 getColumn("qgrd").setEditor(null);
		 getColumn("huayy").setHeader("����Ա");
		 getColumn("huayy").setEditor(null);
		 getColumn("lury").setHeader("����¼��Ա");
		 getColumn("lury").setEditor(null);
		 getColumn("lursj").setHeader("¼��ʱ��");
		 getColumn("lursj").setEditor(null);
		 getColumn("bianm").setHeader("����");
		 getColumn("bianm").setEditor(null);
		 getColumn("beiz").setHeader("���鱸ע");
		 getColumn("beiz").setEditor(null);
		 
			if(IsShow){
				getColumn("aar").setHidden(true);
				getColumn("ad").setHidden(true);
				getColumn("vad").setHidden(true);
				getColumn("fcad").setHidden(true);
				getColumn("std").setHidden(true);
				getColumn("qgrad_daf").setHidden(true);
				getColumn("sdaf").setHidden(true);
				getColumn("var").setHidden(true);
				getColumn("har").setHidden(true);
				 getColumn("qgrd").setHidden(true);
				 getColumn("sdaf").setHidden(true);
				 getColumn("hdaf").setHidden(true);
			}
		 

		// //�����п��
		 getColumn("bianm").setWidth(80);
		 getColumn("mt").setWidth(80);
		 getColumn("aar").setWidth(80);
		 getColumn("ad").setWidth(80);
		 getColumn("vdaf").setWidth(80);
		 getColumn("stad").setWidth(80);
		 getColumn("qgrad_daf").setWidth(110);
		 getColumn("qnet_ar").setWidth(110);
		 getColumn("aad").setWidth(80);
		 getColumn("mad").setWidth(80);
		 getColumn("had").setWidth(80);
		 getColumn("vad").setWidth(80);
		 getColumn("fcad").setWidth(80);
		 getColumn("std").setWidth(80);
		 getColumn("hdaf").setWidth(80);
		 getColumn("qgrad_daf").setWidth(110);
		 getColumn("sdaf").setWidth(80);
		 getColumn("qgrad").setWidth(80);
		 getColumn("var").setWidth(80);
		 getColumn("huayy").setWidth(80);
		 getColumn("lury").setWidth(80);
		 getColumn("beiz").setWidth(80);
		
		// //���õ�ǰ���Ƿ�༭
		//		getColumn("piny").setEditor(null);
		// /���õ�ǰgrid�Ƿ�ɱ༭
		// //���÷�ҳ������ȱʡ25�пɲ��裩
		addPaging(25);
		// /��̬������
//		getColumn("caiyb_id").setEditor(new ComboBox());
//		getColumn("caiyb_id").setComboEditor(gridId,
//				new IDropDownModel("select id, bianm from caiyb"));
		// /��̬������
		/*List l = new ArrayList();
		l.add(new IDropDownBean(0, "�����"));
		l.add(new IDropDownBean(1, "δ���"));
		getColumn("shenhzt").setEditor(new ComboBox());
		getColumn("shenhzt").setComboEditor(gridId, new IDropDownModel(l));*/
		// /�Ƿ񷵻��������ֵ��ID
		//getColumn("shenhzt").setReturnId(true);
		// /����������Ĭ��ֵ
		//		getColumn("shenhzt").setDefaultValue(0);
		// /���ð�ť
		//		addToolbarButton(GridButton.ButtonType_Insert, null);
		//		addToolbarButton(GridButton.ButtonType_Delete, null);
		addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);
		addToolbarButton("����", GridButton.ButtonType_SubmitSel,
		"HuitButton");
		setGridSelModel(ExtGridUtil.GridselModel_Check);
		getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		//		System.out.println("����һ��SaveButton��ť");
		//		setExtGrid(ExtGridUtil("gridDiv", rsl));
		con.Close();
		//		System.out.println("����һ��GetSelect()����");
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	private String getDataScript() {
		StringBuffer djs = new StringBuffer("var " + gridId + "_data = [\n");
		for (int r = 0; r < griddata.length; r++) {
			djs.append("[");
			for (int c = 0; c < griddata[r].length; c++) {
				djs.append("'");
				djs.append(griddata[r][c]);
				djs.append("',");
			}
			djs.deleteCharAt(djs.length() - 1);
			djs.append("],");
		}
		djs.deleteCharAt(djs.length() - 1);
		djs.append("];\n");
		return djs.toString();
	}

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
	}

	public String getDataset() {
		return gridJsDs;
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
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

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

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript
					.append(gridId)
					.append("_ds.on('datachanged',function(){ ")
					.append(gridId)
					.append(
							"_history = ''; });diancTree_text.setValue(diancTree_treePanel.getSelectionModel().getSelectedNode()==null?'':diancTree_treePanel.getSelectionModel().getSelectedNode().text);");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
	}

	/**
	 * @return ���ط�ҳ��Script
	 */
	public String getPagingScript() {
		StringBuffer pagScript = new StringBuffer();
		pagScript.append("var ").append(gridId).append("_pag").append(
				" = new Ext.PagingToolbar({ \n");
		pagScript.append("pageSize: ").append(getPagSize()).append(",\n");
		pagScript.append("store: ").append(gridId).append("_ds").append(",\n");
		pagScript.append("displayInfo: true,\n");
		pagScript.append("displayMsg: '��ʾ�� {0} ���� {1} ����¼��һ�� {2} ��',\n");
		pagScript.append("emptyMsg: 'û�м�¼' });\n");
		return pagScript.toString();
	}

	/**
	 * @return ����ҳ������
	 */
	public int getPagSize() {
		return pagsize;
	}

	public String getGridScript() {
		StringBuffer gridScript = new StringBuffer();
		// gridScript.append(getSelModelScript()).append("\n");
		gridScript.append(getColumnsScript()).append("\n");
		gridScript.append(getDataScript()).append("\n");
		gridScript.append(getDsScript()).append("\n");
		gridScript.append(getPagSize() > 0 ? getPagingScript() : "").append(
				"\n");
		gridScript.append(getGridColumns().size() > 0 ? getPlantScript() : "");
		// gridScript.append(getTbar().length()>0?getToolbarScript()+",":"");

		// gridScript.append(getPagSize()>0?"bbar:"+gridId+"_pag,\n":"");
		// gridScript.append("stripeRows:true});\n");
		// gridScript.append(getOtherScript() == null ? "" : getOtherScript());
		return gridScript.toString();
	}

	public String getGridHtml() {
		StringBuffer sb = new StringBuffer();
		for (int c = 0; c < getGridColumns().size(); c++) {
			GridColumn gc = (GridColumn) getGridColumns().get(c);
			if (gc.combo != null) {
				sb.append(gc.getComboHtml()).append("\n");
			}
		}
		return sb.toString();
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
			gridColumns = null;
			getSelectData();

		}

	}
}