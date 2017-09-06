package com.zhiren.jt.jihgl.niansqjh;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.Format;
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
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.common.DateUtil;
//import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Niansqjh_qp extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		
		String tablename="niansqjhb";
		String shul="fadl";
		int flag =0;
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sql = new StringBuffer("begin \n");
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			String meikmc=mdrsl.getString(mdrsl.getColumnNames()[2]);//�õ�ú������
			if (meikmc.indexOf("��")<0){//������
				//String[][] data_change=new String[mdrsl.getRows()][mdrsl.getColumnCount()];
				for (int i = 4; i < mdrsl.getColumnCount(); i++) {
					String diancmc=mdrsl.getColumnNames()[i];//�糧����
					String fadl=mdrsl.getString(diancmc);//�糧����
					sql2.append("update ").append(tablename).append("\n");
					sql2.append(" set ").append(shul).append(" = ").append(fadl).append("\n");
					sql2.append(" where diancxxb_id in (select id from diancxxb where mingc like '"+diancmc+"')\n");
					sql2.append(" and gongysb_id in (select id from gongysb where mingc like '"+meikmc+"');\n");
					System.out.println("��"+i+"��:"+diancmc+"="+fadl);
				}
				sql.append(sql2);
			}
		}
		sql.append("end;");
		flag=con.getUpdate(sql.toString());
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
		con.commit();
		con.Close();
		
/*		
		System.out.println(getChange());
		visit.getExtGrid1().Save(getChange(), visit);*/
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
	}


	public void getSelectData(ResultSetList rsl) {
		Report rt = new Report();
		Visit visit = (Visit) getPage().getVisit();
		ChessboardTable cd = new ChessboardTable();
		JDBCcon con = new JDBCcon();
		
		// ����������ݺ��·�������

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
			str = "";
		} else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			str = "and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid()
//					+ ")";
			str = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			str = "and dc.id = " + getTreeid() + "";

		}
//		if (rsl_row == null ) {
			//�õ��б���-ú��
			StringBuffer strRow = new StringBuffer();
			strRow.append("select --grouping(gys.mingc) as rowjb, \n");
			strRow.append("decode(grouping(sf.shengfmc),0,sf.shengfmc,'С��') as ʡ��, \n");
			strRow.append("decode(grouping(gys.mingc),0,gys.mingc,'С��') as ��Ӧ�� \n");
			strRow.append("from niansqjhb n \n");//,diancxxb dc
			strRow.append(",(select xgys.id as id,xgys.shengfb_id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc \n");
			strRow.append("from gongysb xgys,gongysb dgys \n");
			strRow.append("where xgys.fuid=dgys.id(+))gys,diancxxb dc, \n");
			strRow.append("(select s.id,s.mingc,sd.mingc as shengfmc from shengfb s,shengfdqb sd where s.shengfdqb_id=sd.id) sf \n");
			strRow.append("where dc.id=n.diancxxb_id \n");//gs.fuid=dc.id and 
			strRow.append("and n.gongysb_id=gys.id and gys.shengfb_id=sf.id \n");
			strRow.append(str).append(" and n.nianf=").append(intyear).append("\n");
			strRow.append("group by rollup(sf.shengfmc,gys.mingc) \n");
			strRow.append("order by grouping(sf.shengfmc) desc,sf.shengfmc \n");
//			System.out.println("ú��SQL��"+strRow.toString());
			
			//�õ��б���-�糧
			StringBuffer strCol = new StringBuffer();
			strCol.append("select --grouping(dc.mingc) as coljb, \n");
			strCol.append("decode(grouping(dc.mingc),0,dc.mingc,'С��') as �糧 \n");
			strCol.append("from niansqjhb n,diancxxb dc \n");
			
//			strCol.append(",(select xgys.id as id,xgys.shengfb_id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc \n");
//			strCol.append("from gongysb xgys,gongysb dgys \n");
//			strCol.append("where xgys.fuid=dgys.id(+))gys,diancxxb gs, \n");
//			strCol.append("(select s.id,s.mingc,sd.mingc as shengfmc from shengfb s,shengfdqb sd where s.shengfdqb_id=sd.id) sf \n");
//			strCol.append("where gs.fuid=dc.id and gs.id=n.diancxxb_id \n");
//			strCol.append("and n.gongysb_id=gys.id and gys.shengfb_id=sf.id \n");
			
			strCol.append("where n.diancxxb_id=dc.id \n");
			
			strCol.append(str).append(" and n.nianf=").append(intyear).append("\n");
			strCol.append("group by rollup(dc.mingc)   order by grouping(dc.mingc) desc \n");
//			System.out.println("�糧SQL��"+strCol.toString());
			
			//������
			StringBuffer sbsql = new StringBuffer();
			sbsql.append("select --grouping(gys.mingc) as rowjb,grouping(dc.mingc) as coljb, \n");
			sbsql.append("decode(grouping(sf.shengfmc),0,sf.shengfmc,'С��') as ʡ��, \n");
			sbsql.append("decode(grouping(gys.mingc),0,gys.mingc,'С��') as ��Ӧ��, \n");
			sbsql.append("decode(grouping(dc.mingc),0,dc.mingc,'С��') as �糧, \n");
			sbsql.append("sum (n.fadl) as ���� \n");
			sbsql.append("from niansqjhb n,diancxxb dc \n");
			sbsql.append(",(select xgys.id as id,xgys.shengfb_id,xgys.xuh,decode(xgys.fuid,0,xgys.mingc,dgys.mingc) as mingc \n");
			sbsql.append("from gongysb xgys,gongysb dgys \n");
			sbsql.append("where xgys.fuid=dgys.id(+))gys, \n");//,diancxxb gs
			sbsql.append("(select s.id,s.mingc,sd.mingc as shengfmc from shengfb s,shengfdqb sd where s.shengfdqb_id=sd.id) sf \n");
			sbsql.append("where dc.id=n.diancxxb_id \n");
			sbsql.append("and n.gongysb_id=gys.id and gys.shengfb_id=sf.id \n");
			sbsql.append(str).append(" and n.nianf=").append(intyear).append("\n");
			sbsql.append("group by cube(sf.shengfmc,gys.mingc ,dc.mingc) \n");
//			System.out.println("����SQL��"+sbsql.toString());
			
		rsl = con.getResultSetList(strCol.toString());//�õ��е糧

		cd.setRowNames("ʡ��,��Ӧ��");
		cd.setColNames("�糧");
		cd.setDataNames("����");
		cd.setDataOnRow(false);
		cd.setRowToCol(false);
		//�������̱�����
		cd.setData(strRow.toString(), strCol.toString(), sbsql.toString());
		rt.setBody(cd.DataTable);
		
		//System.out.println(rt.body.getHtml());
		
		for (int i=1;i<=cd.DataTable.getRows();i++){
			String shengf=cd.DataTable.getCellValue(i, 1);
			
			String shuj=cd.DataTable.getCellValue(i, 2);
			if(shengf.equals("С��") && shuj.equals("С��")){
				cd.DataTable.setCellValue(i, 2, "�ܼ�");
			}else if(shuj.equals("С��")){
				cd.DataTable.setCellValue(i, 2, shengf+"С��");
			}
		}
		if (cd.DataTable.getRows()>=3){
			cd.DataTable.setCellValue(1, 3, "�ϼ�");
		}
		
		
		ExtGridUtil egu =new ExtGridUtil("gridDiv");
		egu.setTableName("niansqjhb");
		GridColumn column=new GridColumn(GridColumn.ColType_default,"id","id",40);
		column.setDataType(GridColumn.DataType_String);
		column.setUpdate(true);
		column.setHidden(true);
		egu.addColumn(column);
		GridColumn column2=new GridColumn(GridColumn.ColType_default,"ʡ��","ʡ��",40);
		column2.setDataType(GridColumn.DataType_String);
		column2.setUpdate(true);
		column2.setHidden(true);
		egu.addColumn(column2);
		GridColumn column3=new GridColumn(GridColumn.ColType_default,"������λ","������λ",200);
		column3.setDataType(GridColumn.DataType_String);
		column3.setUpdate(true);
		
		egu.addColumn(column3);
		for (int i=3;i<=cd.DataTable.getCols();i++){
			String colsname=cd.DataTable.getCellValue(1, i);
			GridColumn column4=new GridColumn(GridColumn.ColType_default,colsname,colsname,80);
			column4.setEditor(new NumberField());
			column4.setDataType(GridColumn.DataType_Float);
			column4.setUpdate(true);
			egu.addColumn(column4);
		}
		if (rsl.getRows()!=0){
			egu.getColumn("С��").setEditor(null);
			egu.getColumn("С��").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
			egu.getColumn("������λ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		}
		int rows = cd.DataTable.getRows()-1;
		int cols = cd.DataTable.getCols()+1;
		egu.initData(rows, cols);//��ʼ������
		
		for (int i=2;i<=cd.DataTable.getRows();i++){
			egu.setDataValue(i-2, 0, "0");
			for (int j=1;j<cols;j++){
				String shuj=cd.DataTable.getCellValue(i, j);
				if (shuj.equals("")){
					egu.setDataValue(i-2, j, "0");
					//egu.getColumn(j).getEditorScript()
//					egu.getDataValue(i-2, j).''
					
				}else{
					egu.setDataValue(i-2, j, shuj);
				}
			}
		}
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ
		//egu.setWidth(1000);// ����ҳ��Ŀ��,������������ʱ��ʾ������

		// �������ڵ�Ĭ��ֵ,
//		egu.getColumn("nianf").setDefaultValue(""+intyear);

		// ********************������************************************************
		egu.addTbarText("���:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		// ������
		egu.addTbarText("��λ:");
//		System.out.println(getTreeid());
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// �趨�������������Զ�ˢ��
		
		egu.addTbarText("-");// ���÷ָ���
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		 ---------------ҳ��js�ļ��㿪ʼ------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit',function(e){");
		
		String sb_str=
			" var cols=gridDiv_cm.getColumnCount();\n" +
			" var shul=0;\n" +
			" for (var i=4;i<cols;i++){\n" +
			" 	var colsname=gridDiv_cm.getColumnHeader(i);\n" +
			" 	shul=shul+eval(e.record.get(colsname)||0);\n" +
			" }\n" +
			" e.record.set('�ϼ�',''+shul);\n" +
			"var sumzj=0;\n" +
			"var sumxj=0;\n" + 
			"var sumfldxj=0;\n" + 
			"var sumfldzj=0;\n" + 
			"var rows=gridDiv_ds.getTotalCount();\n" + 
			"var colhj=gridDiv_cm.getColumnHeader(3);\n" + 
			"var coldw=gridDiv_cm.getColumnHeader(2);\n" + 
			"var danw='';\n" + 
			"var rec;\n" + 
			"\n" + 
			"for (var i=rows-1 ;i>=0;i--){\n" + 
			"\trec=gridDiv_ds.getAt(i);\n" + 
			"\tdanw=rec.get(coldw);\n" + 
			"\tif (danw.indexOf(\"�ܼ�\")>=0){\n" + 
			"\t\trec.set('�ϼ�',''+sumzj);\n" + 
			"\t\trec.set(e.field,''+sumfldzj);\n" + 
			"\n" + 
			"\t}else if (danw.indexOf(\"С��\")>=0){\n" + 
			"\t\trec.set('�ϼ�',''+sumxj);\n" + 
			"\t\trec.set(e.field,''+sumfldxj);\n" + 
			"\n" + 
			"\t\tsumzj=sumzj+sumxj;\n" + 
			"\t\tsumfldzj=sumfldzj+sumfldxj;\n" + 
			"\t\tsumxj=0;\n" + 
			"\t\tsumfldxj=0;\n" + 
			"\t}else{\n" + 
			"\t\tsumxj=sumxj+ eval(rec.get(colhj)==''?0:rec.get(colhj));\n" + 
			"\t\tsumfldxj=sumfldxj+ eval(rec.get(e.field)==''?0:rec.get(e.field));\n" + 
			"\t}\n" + 
			"}";
		
		sb.append(sb_str);
		
		sb.append("});");
	
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("var shengfmc=e.record.get('ʡ��');");
		sb.append("if(e.record.get('������λ')=='�ܼ�'){e.cancel=true;}");//��"�ܼ�"ʱ,��һ�в�����༭
		sb.append("if(e.record.get('������λ')==shengfmc+'С��'){e.cancel=true;}");//��"С��"ʱ,��һ�в�����༭
		
		sb.append("});");
		
//		System.out.println(sb.toString());
		
		egu.addOtherScript(sb.toString());
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			//��Ӧ�̣�ú�󣬳�վ
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			//this.setYuefValue(null);
			this.setMsg(null);
			//this.getYuefModels();

		}
		getSelectData(null);

	}

	// ���
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// �·�
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"��ѡ��"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// �õ��糧��Ĭ�ϵ�վ
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}

	//�������������ݣ���Ӧ�̣�ú�󣬳�վ
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}
	
	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}