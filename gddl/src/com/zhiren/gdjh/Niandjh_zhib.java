package com.zhiren.gdjh;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ����
 * ʱ�䣺2012-10-18
 * �������������Ƽƻ���ť�����������û����������ʾ�ð�ť��������ʾ��
 * 		�����ܳ�����������ơ�
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-11-19
 * ���÷�Χ������������������糧
 * ������������ƻ�ָ���б�ú���ۺ���¯�ۺϱ�ú���۵ļ��㹫ʽ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-01-15
 * ���÷�Χ������������������糧
 * �������������δ��ʼ����BUG
 */
public class Niandjh_zhib extends BasePage implements PageValidateListener {
	public static final int col_odd = 1;
	public static final int col_even = 0;
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	public void setRiq() {
		setNianf(getNianfValue().getValue());
	}

	// ���������
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
			int _nianf = DateUtil.getYear(new Date())+1;
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			_NianfValue = Value;
		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2009; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
			getNianfModels();
			setNianfValue(null);
			setRiq();
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	private boolean _Cpyclick = false;

	public void CpyButton(IRequestCycle cycle) {
		_Cpyclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		if(_Cpyclick){
			_Cpyclick=false;
			DelData();
			CopyData();
		}
		getSelectData();
	}

	public void DelData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = this.getTreeid();
		String CurrZnDate = getNianf() + "��";
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String strSql = "delete from niandjh_zhib where riq = " + CurrODate
				+ " and diancxxb_id =" + diancxxb_id;
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
		}
		con.Close();
	}

	/**
	 * @param con
	 * @return true:���ϴ�״̬�� �����޸����� false:δ�ϴ�״̬�� �����޸�����
	 */
	private boolean getZhangt(JDBCcon con) {
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String sql = "select s.zhuangt zhuangt\n" + "  from niandjh_zhib s\n"
				+ " where  s.diancxxb_id = " + getTreeid() + "\n"
				+ "   and s.riq = " + CurrODate;
		ResultSetList rs = con.getResultSetList(sql);
		boolean zt = true;
		if (con.getHasIt(sql)) {
			while (rs.next()) {
				if (rs.getInt("zhuangt") == 0 || rs.getInt("zhuangt") == 2) {
					zt = false;
				}
			}
		} else {
			zt = false;
		}
		return zt;
	}

	public void getSelectData() {
//		initData();
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		ExtGridUtil egu = new ExtGridUtil("gridDiv");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		egu.setDefaultsortable(false);
		egu.addColumn(new GridColumn(GridColumn.ColType_Rownum));

		GridColumn ColName1 = new GridColumn(0, "COLNAME1", "�ֶ���1", 90);
		ColName1.setDataType(GridColumn.DataType_String);
		ColName1.setUpdate(true);
		ColName1.setHidden(true);
		egu.addColumn(ColName1);
		GridColumn Item1 = new GridColumn(0, "ITEM1", "��Ŀ1", 200);
		Item1.setDataType(GridColumn.DataType_String);
		Item1.setUpdate(true);
		Item1.setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		egu.addColumn(Item1);
		GridColumn Unit1 = new GridColumn(0, "UNIT1", "��λ1", 80);
		Unit1.setDataType(GridColumn.DataType_String);
		Unit1.setUpdate(true);
		Unit1.setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
		egu.addColumn(Unit1);
		GridColumn Value1 = new GridColumn(0, "VALUE1", "ֵ1", 80);
		NumberField nfkd = new NumberField();
		nfkd.setAllowBlank(false);
		nfkd.setDecimalPrecision(2);
		Value1.setEditor(nfkd);
		Value1.setUpdate(true);
		Value1.setDataType(GridColumn.DataType_Float);
		egu.addColumn(Value1);

		GridColumn ColName2 = new GridColumn(0, "COLNAME2", "�ֶ���2", 90);
		ColName2.setDataType(GridColumn.DataType_String);
		ColName2.setUpdate(true);
		ColName2.setHidden(true);
		egu.addColumn(ColName2);
		GridColumn Item2 = new GridColumn(0, "ITEM2", "��Ŀ2", 200);
		Item2.setDataType(GridColumn.DataType_String);
		Item2.setUpdate(true);
		Item2.setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		egu.addColumn(Item2);
		GridColumn Unit2 = new GridColumn(0, "UNIT2", "��λ2", 80);
		Unit2.setDataType(GridColumn.DataType_String);
		Unit2.setUpdate(true);
		Unit2.setRenderer("function(value,metadata){metadata.css='tdTextext2'; return value;}");
		egu.addColumn(Unit2);
		GridColumn Value2 = new GridColumn(0, "VALUE2", "ֵ2", 80);
		NumberField nfkd2 = new NumberField();
		nfkd2.setAllowBlank(false);
		nfkd2.setDecimalPrecision(2);
		Value2.setEditor(nfkd);
		Value2.setUpdate(true);
		Value2.setDataType(GridColumn.DataType_Float);
		egu.addColumn(Value2);
		// ȡ��grid����
		int rows = getGridDataRows(con, diancxxb_id);

		String BeforeEditScript = "gridDiv_grid.on('beforeedit',function(e){";
		String countScript = "function countEval(e){";
		String afterEditScript = "gridDiv_grid.on('afteredit',countEval);";
		String afterEditGSScript = "";
		String[][] data = null;
		if (rows >= 0) {
			data = new String[rows][8];
			String[] varCss = getGridData(con, diancxxb_id, rows, col_even,
					data, CurrODate);
			Value2.setRenderer(varCss[1]);
			countScript += varCss[0];
			BeforeEditScript += varCss[2];
			afterEditGSScript += varCss[3];
			varCss = getGridData(con, diancxxb_id, rows, col_odd, data, CurrODate);
			Value1.setRenderer(varCss[1]);
			BeforeEditScript += varCss[2];
			afterEditGSScript += varCss[3];
			countScript += varCss[0];
		} else {
			data = new String[][] { { "", "", "", "", "", "", "", "" } };
		}
		BeforeEditScript += "});\n";
		egu.setData(data);
		// /������ʾ������
		egu.setWidth(Locale.Grid_DefaultWidth);

		egu.addOtherScript(BeforeEditScript);
		countScript += getOtherVarScript(con, diancxxb_id);
		countScript += getCountColSetScript(con, diancxxb_id);
		countScript += afterEditGSScript;
		countScript += ";}\n";
		egu.addOtherScript(countScript);
		egu.addOtherScript(afterEditScript);

		egu.addTbarText("���");
		ComboBox comb1 = new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
		comb1.setLazyRender(true);// ��̬��
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// ���÷ָ���

		GridButton gbr = new GridButton("ˢ��",
				"function (){document.getElementById('RefreshButton').click()}");
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);

		// ɾ����ť
		GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");

		// �ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
		if (getZhangt(con)) {
			if(visit.getDiancxxb_id()==112){
				egu.addTbarBtn(gbd);
				egu.addTbarBtn(gbs);
			}else{
				setMsg("�����Ѿ��ϴ���������ϵ�ϼ���λ����֮����ܲ�����");
			}
		} else {
			if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�ƻ�ģ��", "�ֳ����ܳ���ʾ��ť", this.getTreeid(), "��").equals("��")){
				
			}else{
				egu.addTbarBtn(gbd);
				egu.addTbarBtn(gbs);
//				���������û����������ʾ���ư�ť
				String sql = "select s.id zhuangt from niandjh_zhib s\n"
						+ " where  s.diancxxb_id = " + getTreeid() + "\n"
						+ "   and s.riq = " + CurrODate;
				if(!con.getHasIt(sql)){
					String handler="function (){document.getElementById('CpyButton').click();"+
					"Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...'," +
					"progressText:'������...',width:300,wait:true,waitConfig: " +
					"{interval:200},icon:Ext.MessageBox.INFO});}";
					GridButton cpy = new GridButton("��������ȼƻ�", handler);
					cpy.setIcon(SysConstant.Btn_Icon_Copy);
					egu.addTbarBtn(cpy);
				}
			}
		}

		egu.addOtherScript(getAfterColorScript(con, diancxxb_id, col_even));
		egu.addOtherScript(getAfterColorScript(con, diancxxb_id, col_odd));

		setExtGrid(egu);
		con.Close();
	}

	private int UpdateDataBase(JDBCcon con, String diancxxb_id, String CurrODate, String upsql) {
		int flag = getUpdateSql(con, diancxxb_id, CurrODate, upsql);
		if (flag == -1) {
			setMsg("������̷����쳣��");
			return -1;
		}
		// ���µ��¼���ֵ
		flag = countNianjhzbgs(con, diancxxb_id, CurrODate);
		if (flag == -1) {
			setMsg("������̷����쳣��");
			return -1;
		}
		// ����ú�۱�ú���ۣ��������ã�����¯�ۺϱ�ú����
		flag = countBMDJ(con, diancxxb_id, CurrODate);
		if (flag == -1) {
			setMsg("������̷����쳣��");
			return -1;
		}
		return 0;
	}
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + this.getTreeid();
		return con.getHasIt(sql);
	}

	private void initData() {
		JDBCcon con = new JDBCcon();
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String sql = "select * from niandjh_zhib where riq = " + CurrODate
				+ " and diancxxb_id =" + diancxxb_id;
		if (!con.getHasIt(sql)) {
			con.getInsert(getInsertSql(diancxxb_id, CurrODate));
		}
		con.Close();
	}

	private double parseDouble(String value) {
		double dv = 0.0;
		try {
			dv = Double.parseDouble(value);
		} catch (Exception e) {
			return dv;
		}
		return dv;
	}

	private boolean isNotNAN(String value) {
		boolean isnotnan = true;
		if (value == null) {
			return false;
		}
		if ("".equals(value)) {
			return false;
		}
		return isnotnan;
	}

	private void Save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("����Ϊ�գ�");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String diancxxb_id = getTreeid();
		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-01-01");
		String upsql;
		initData();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		// ������±������ݵ�SQL
		upsql = "";
		while (rsl.next()) {
			if (isNotNAN(rsl.getString("COLNAME1"))) {
				upsql += "," + rsl.getString("COLNAME1") + "="
						+ parseDouble(rsl.getString("VALUE1"));
			}
			if (isNotNAN(rsl.getString("COLNAME2"))) {
				upsql += "," + rsl.getString("COLNAME2") + "="
						+ parseDouble(rsl.getString("VALUE2"));
			}
		}
		rsl.close();
		int flag = UpdateDataBase(con, diancxxb_id, CurrODate, upsql);
		con.commit();
		con.Close();
		if (flag == 0) {
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}
	
//	���ƹ���
	private void CopyData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(true);
		String strDate=getNianf()+"-01-01";
		
		String CopySql=
			"INSERT INTO NIANDJH_ZHIB\n" +
			"(ID,DIANCXXB_ID,RIQ,FADL,GONGDMH,FADCYDL,FADBML,GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,\n" + 
			"RUCRLRZC,MEIZBML,MEIZBMDJ,RANYL,YOUZBML,RANYDJ,YOUZBMDJ,QITFY,RLZHBMDJ,ZHUANGT)\n" + 
			"(SELECT GETNEWID("+this.getTreeid()+"),DIANCXXB_ID,ADD_MONTHS(RIQ, 12) RIQ,FADL,GONGDMH,FADCYDL,\n" + 
			"FADBML,GONGRL,GONGRMH,GONGRBML,BIAOMLHJ,RUCRLRZC,MEIZBML,0 MEIZBMDJ,RANYL,YOUZBML,\n" + 
			"RANYDJ,YOUZBMDJ,0 QITFY,0 RLZHBMDJ,0 ZHUANGT\n" + 
			"FROM NIANDJH_ZHIB\n" + 
			"WHERE RIQ = ADD_MONTHS(DATE '"+strDate+"', -12)\n" + 
			"AND DIANCXXB_ID = "+this.getTreeid()+")";

		con.getInsert(CopySql);
// 		ÿ�α���ʱ���¼���ָ���е���ز���
 		countBMDJ(con,this.getTreeid(),"to_date('"+strDate+"','yyyy-mm-dd')");
 		con.Close();
		setMsg("���Ʋ�����ɣ�");
	}	

	private int countNianjhzbgs(JDBCcon con, String diancxxb_id,
			String CurrODate) {
		String sql = getNianjhzbdySql(diancxxb_id, "gongs", "leijgs");
		ResultSetList rsl = con.getResultSetList(sql);
		String upsql = "";
		while (rsl.next()) {
			upsql = "," + rsl.getString("ZIDM") + "=" + rsl.getString("GONGS");
			getUpdateSql(con, diancxxb_id, CurrODate, upsql);
		}
		rsl.close();
		return 1;
	}

	private int countBMDJ(JDBCcon con, String diancxxb_id, String CurrODate) {
		String upsql = 
			"SELECT MEIZBMDJ,\n" +
			"       ZAFJE,\n" + 
			"       ROUND(DECODE(BIAOMLHJ,0,0,(MEIZBML * MEIZBMDJ + RANYL * RANYDJ + ZAFJE) / BIAOMLHJ),2) RULZHBMDJ\n" + 
			"  FROM (SELECT NVL(CGJH.JIH_REZ, 0) JIH_REZ,\n" + 
			"               NVL(CGJH.JIH_DAOCJ, 0) JIH_DAOCJ,\n" + 
			"               NVL(ZAF.ZAFJE, 0) ZAFJE,\n" + 
			"               NVL(ZHIB.RUCRLRZC, 0) RUCRLRZC,\n" + 
			"               NVL(ZHIB.MEIZBML, 0) MEIZBML,\n" + 
			"               NVL(ZHIB.RANYL, 0) RANYL,\n" + 
			"               NVL(ZHIB.RANYDJ, 0) RANYDJ,\n" + 
			"               NVL(ZHIB.BIAOMLHJ, 0) BIAOMLHJ,\n" + 
			"               ROUND(DECODE((NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0)),0,0,NVL(JIH_DAOCJ, 0) * 29.2712 /(NVL(JIH_REZ, 0) - NVL(RUCRLRZC, 0))),2) MEIZBMDJ\n" + 
			"          FROM (SELECT SUM(JIH_SL) JIHSL,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * JIH_REZ) / SUM(JIH_SL)) JIH_REZ,\n" + 
			"                       DECODE(SUM(JIH_SL),0,0,SUM(JIH_SL * (JIH_MEIJBHS+JIH_YUNJBHS+JIH_ZAFBHS+JIH_QITBHS)) / SUM(JIH_SL)) JIH_DAOCJ\n" + 
			"                  FROM NIANDJH_CAIG\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") CGJH,\n" + 
			"               (SELECT SUM(YUCJE) ZAFJE\n" + 
			"                  FROM NIANDJH_ZAF\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND TRUNC(RIQ, 'yyyy') = "+CurrODate+") ZAF,\n" + 
			"               (SELECT RUCRLRZC, MEIZBML, RANYL, RANYDJ, BIAOMLHJ\n" + 
			"                  FROM NIANDJH_ZHIB\n" + 
			"                 WHERE DIANCXXB_ID = "+diancxxb_id+"\n" + 
			"                   AND RIQ = "+CurrODate+") ZHIB) SJ";
		
		ResultSetList rsl = con.getResultSetList(upsql);
		
		String updateSql="";
		if(rsl.next()) {
			double MEIZBMDJ=rsl.getDouble("MEIZBMDJ"); 
			double QITFY=rsl.getDouble("ZAFJE"); 
			double RULZHBMDJ=rsl.getDouble("RULZHBMDJ"); 
			updateSql = "update niandjh_zhib set MEIZBMDJ="+MEIZBMDJ+", QITFY="+QITFY+", RLZHBMDJ="+RULZHBMDJ+" where riq = " + CurrODate + " and diancxxb_id=" + diancxxb_id;
		}
		rsl.close();
		if(updateSql.length()>1){
			con.getUpdate(updateSql);
			return 1;
		}else{
			return -1;
		}
		
	}

	private String getNianjhzbdySql(String diancxxb_id, String gongsm) {
		return getNianjhzbdySql(diancxxb_id, gongsm, null);
	}

	private String getNianjhzbdySql(String diancxxb_id, String gongsm,
			String valigongs) {
		String sqltmp = valigongs == null || valigongs.equals("") ? ""
				: " and " + valigongs + " is null ";
		String sql = "select ZIDM,GONGS from JIHZBDYB\n"
				+ "where zhuangt = 1 and " + gongsm + " is not null " + sqltmp
				+ " \n" + " and diancxxb_id =" + diancxxb_id
				+ " and mokmc='��ƻ�' order by bianm";
		return sql;
	}

	private String getInsertSql(String diancxxb_id, String CurrODate) {
		String sql = "insert into niandjh_zhib (id,diancxxb_id,riq) values(getnewid("
				+ diancxxb_id + ")," + diancxxb_id + "," + CurrODate + ")";
		return sql;
	}

	private int getUpdateSql(JDBCcon con, String diancxxb_id, String CurrODate,
			String upsql) {
		upsql = upsql.substring(1);
		upsql = "update niandjh_zhib set " + upsql + " where riq = "
				+ CurrODate + " and diancxxb_id=" + diancxxb_id;
		return con.getUpdate(upsql);
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "��";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if (btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		} else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}

	private int getGridDataRows(JDBCcon con, String diancxxb_id) {
		int rows = 0;
		String sql = "select nvl(sum(decode(mod(xuh,2),0,1,0)),0) even\n"
				+ ",nvl(sum(mod(xuh,2)),0) odd from JIHZBDYB\n"
				+ "where zhuangt = 1 and mokmc='��ƻ�' and diancxxb_id = "
				+ diancxxb_id;
		ResultSetList rs = con.getResultSetList(sql);
		if (rs.next()) {
			rows = Math.max(rs.getInt("even"), rs.getInt("odd"));
		}
		rs.close();
		return rows;
	}

	private String getColumnValue(JDBCcon con, String date, String diancxxb_id,
			String zidm) {
		String value = "";
		String sql = "select " + zidm + " from niandjh_zhib where diancxxb_id="
				+ diancxxb_id + " and riq = " + date + "";
		ResultSetList rs = con.getResultSetList(sql);
		if (rs == null) {
			return null;
		}
		if (rs.next()) {
			value = rs.getString(zidm);
		}
		rs.close();
		return value;
	}

	private String[] getGridData(JDBCcon con, String diancxxb_id, int rows,
			int col, String[][] data, String CurrODate) {

		String[] GridVar = new String[4];
		String ValueName = "VALUE1";
		int colstart = 0;
		if (col == col_even) {
			colstart = 4;
			ValueName = "VALUE2";
		}
		String sql = "select *\n" + "from JIHZBDYB\n" + "where mod(xuh,2)= "
				+ col + " and  zhuangt = 1 and mokmc='��ƻ�'\n"
				+ "and diancxxb_id = " + diancxxb_id + "\n" + "order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		GridVar[0] = "";
		GridVar[1] = "function(value,metadata,record,rowIndex,colIndex,store){";
		GridVar[2] = "";
		GridVar[3] = "";
		while (rsl.next()) {
			GridVar[0] += "var " + rsl.getString("ZIDM")
					+ "= parseFloat(gridDiv_grid.getStore().getAt("
					+ rsl.getRow() + ").get('" + ValueName + "'));\n"
					+ rsl.getString("ZIDM") + "=eval(" + rsl.getString("ZIDM")
					+ "||0);\n";
			// �����ʽ��Ϊ�������������У������㱳��ɫ

			if (rsl.getString("GONGS") != null
					&& !"".equals(rsl.getString("GONGS"))) {
				GridVar[1] += "if(rowIndex==" + rsl.getRow()
						+ " && colIndex == " + (colstart + 4)
						+ "){metadata.css='tdTextext2';}\n";
				GridVar[2] += "if(e.row ==" + rsl.getRow() + " && e.column == "
						+ (colstart + 4) + "){e.cancel=true;}\n";
				GridVar[3] += "gridDiv_grid.getStore().getAt(" + rsl.getRow()
						+ ").set('" + ValueName + "',isNaN("
						+ rsl.getString("ZIDM") + ")?0:("
						+ rsl.getString("ZIDM") + "==\"Infinity\"?0:"
						+ rsl.getString("ZIDM") + "));\n";
			}
			String zhi = getColumnValue(con, CurrODate, diancxxb_id, rsl
					.getString("zidm"));
			if (zhi == null) {
				data[rsl.getRow()][colstart] = "";
				data[rsl.getRow()][colstart + 1] = "";
				data[rsl.getRow()][colstart + 2] = "";
				data[rsl.getRow()][colstart + 3] = "";
				continue;
			}
			data[rsl.getRow()][colstart] = rsl.getString("zidm");
			data[rsl.getRow()][colstart + 1] = rsl.getString("mingc");
			data[rsl.getRow()][colstart + 2] = rsl.getString("danw");
			data[rsl.getRow()][colstart + 3] = zhi;
		}
		GridVar[1] += "return value;}";
		if (rows > rsl.getRows())
			for (int i = rsl.getRows(); i < rows; i++) {
				data[i][colstart] = "";
				data[i][colstart + 1] = "";
				data[i][colstart + 2] = "";
				data[i][colstart + 3] = "";
			}
		rsl.close();
		return GridVar;
	}

	private String getAfterColorScript(JDBCcon con, String diancxxb_id, int col) {

		String sql = "select *\n" + "from JIHZBDYB\n" + "where mod(xuh,2)= "
				+ col + " and zhuangt = 1 and mokmc='��ƻ�'\n"
				+ "and diancxxb_id = " + diancxxb_id + "\n" + "order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);

		if (col == col_odd) {
			col = 0;
		} else {
			col = 1;
		}

		StringBuffer script = new StringBuffer();
		script.append("gridDiv_grid.on('afteredit',function(e){\n");
		while (rsl.next()) {
			if (rsl.getString("GONGS") != null
					&& !"".equals(rsl.getString("GONGS"))) {
				script.append("gridDiv_grid.getView().getCell(" + rsl.getRow()
						+ "," + (col * 4 + 4)
						+ ").style.backgroundColor='#E3E3E3';\n");
			}
		}
		script.append("});\n");
		return script.toString();
	}

	private String getOtherVarScript(JDBCcon con, String diancxxb_id) {
		String script = "";
		String sql = "select * from JIHZBDYB where zhuangt = 0 and mokmc='��ƻ�' and diancxxb_id ="
				+ diancxxb_id + " order by xuh";
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			script += "var " + rsl.getString("ZIDM") + "= 0;\n";
		}
		rsl.close();
		return script;
	}

	private String getCountColSetScript(JDBCcon con, String diancxxb_id) {
		String gongs = "";
		String sql = getNianjhzbdySql(diancxxb_id, "gongs");
		ResultSetList rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			gongs += rsl.getString("ZIDM") + "=eval(" + rsl.getString("GONGS")
					+ ");\n";
		}
		rsl.close();
		return gongs;
	}

}
