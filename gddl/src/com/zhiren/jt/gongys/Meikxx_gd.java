package com.zhiren.jt.gongys;

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

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-2-10
 * ����: ������ú��ά���в��ܱ��������¼
 * 		
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-20
 * ������ʹ��String2��¼��ǰ����
 */

/*
 * ���ߣ����
 * ʱ�䣺2013-03-2
 * ������ʹ��xl_meikxxb_idȡ��ú��͹�Ӧ�̶�Ӧ��ID������ʹ�ù�����������ID���������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-10-17
 * ���������Ӵ�ͬú��������Ᵽ������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-10-17
 * ���������Ӵ�ͬú��������Ᵽ������
 */
/*
 * ���ߣ���ҫ��
 * ʱ�䣺2014-03-03
 * �������޸��޷����ú�ͣ�õ�BUG
 */

public class Meikxx_gd extends BasePage implements PageValidateListener {
//  ϵͳ��־���е�״̬�ֶ�
//	private static final String ZhangTConstant1 = "�ɹ�";
//
//	private static final String ZhangTConstant2 = "ʧ��";

	// ϵͳ��־���е�����ֶ�
//	private static final String leiBConstant = "ú��ά��";
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	private String id;  //��¼ǰ̨���ݹ�����id
	public void setId(String id){
		this.id = id;
	}
	public String getId(){
		return id;
	}
	private String SaveMsg;

	public String getSaveMsg() {
		return SaveMsg;
	}

	public void setSaveMsg(String saveMsg) {
		SaveMsg = MainGlobal.getExtMessageBox(saveMsg, false);;
	}

	private boolean tiShi;// ������ʾ��Ϣ�Ƿ���ʾ��
	
	private boolean happenWrong; // �жϱ���ʱ�Ƿ��д������ݣ�trueΪ�ǣ�falseΪ��

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Parameters;// ��¼ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}
	
	private String Meikmc; // ����ҳ���ϵ�ú������
	
	public String getMeikmc() {
		return Meikmc;
	}

	public void setMeikmc(String meikmc) {
		Meikmc = meikmc;
	}
	
	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}
	
	private String wrongDataSource; // ����ʱ���д��������ƴ�ɿ�������Record���ַ����󣬱��浽�ñ����С�
	
	public String getWrongDataSource() {
		return wrongDataSource;
	}

	public void setWrongDataSource(String wrongDataSource) {
		this.wrongDataSource = wrongDataSource;
	}

	// �п��ܴӷ��ذ�ť���ر�ҳ�棬Ҳ�����Ǵ������ѡ��ť���ر�ҳ�棬�������
	private String ToAddMsg;

	public String getToAddMsg() {
		return ToAddMsg;
	}

	public void setToAddMsg(String toAddMsg) {
		ToAddMsg = toAddMsg;
	}
	
//	ʡ��IDropDownModel
	public IPropertySelectionModel getShengfModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setShengfModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setShengfModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setShengfModels() {
		String sql = "select sf.id, sf.quanc from shengfb sf";
		setShengfModel(new IDropDownModel(sql));
	}

//	����IDropDownModel
	public IPropertySelectionModel getChengsModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChengsModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChengsModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setChengsModels() {
		String sql = "select cs.id, cs.quanc from chengsb cs";
		setChengsModel(new IDropDownModel(sql));
	}

	private void gotochez(IRequestCycle cycle) {
		// ��Ҫ�����¸�ҳ��ȡֵ
		if (getChange() == null || "".equals(getChange())) {
			setMsg("��ѡ��һ����Ա���÷���!");
			return;
		}
		Visit visit = (Visit) getPage().getVisit();
		visit.setString1(getChange());
		visit.setString2(visit.getActivePageName().toString());
		cycle.activate("Chezgl");
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		SaveMsg = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next())	{
//			�жϱ����Ƿ���ϱ�׼��׼
			String SaveMsgLocal = "";
			ResultSetList weis = con.getResultSetList("select zhi from xitxxb where mingc='ú�����λ��' and zhuangt=1");
			if(weis.next()){
				if(mdrsl.getString("BIANM").length()<weis.getInt("zhi")){
					SaveMsgLocal="ú�����λ��С��"+weis.getInt("zhi")+",�밴Ҫ�����±��롣";
					setSaveMsg(SaveMsgLocal);
					return;
				}
			}
//			��ʼ������ֵ����ʱ�������
			String id="";
			ResultSetList rs = con.getResultSetList("select xl_meikxxb_id.nextval id from dual");
			if(rs.next()) {
				id = rs.getString(0);
			}
			
			String meikxxb_id=visit.getDiancxxb_id()+id;
			String gongysb_id = meikxxb_id;
			
//			String meikxxb_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
//			String gongysb_id = MainGlobal.getNewID(con, visit.getDiancxxb_id());
			
			String xuh=mdrsl.getString("XUH");
			long MEIKDQ_ID = (getExtGrid().getColumn("MEIKDQ_ID").combo).getBeanId(mdrsl.getString("MEIKDQ_ID"));
			String BIANM=mdrsl.getString("BIANM");
			String MINGC=mdrsl.getString("MINGC");
			String QUANC=mdrsl.getString("QUANC");
			String PINY=mdrsl.getString("PINY");
//			���ʡ��Ϊ�գ��򷵻�0
			long SHENGFB_ID =0;
			if(mdrsl.getString("SHENGFB_ID") == null|| mdrsl.getString("SHENGFB_ID").equals("")){
				SHENGFB_ID=0;
			}else{
				SHENGFB_ID=((IDropDownModel)getShengfModel()).getBeanId(mdrsl.getString("SHENGFB_ID"));
			}
//			�������Ϊ�գ��򷵻�0
			long CHENGSB_ID=0;
			if(mdrsl.getString("CHENGSB_ID") == null|| mdrsl.getString("CHENGSB_ID").equals("")){
				CHENGSB_ID=0;
			}else{
				CHENGSB_ID =((IDropDownModel)getChengsModel()).getBeanId(mdrsl.getString("CHENGSB_ID")); 
			}
			String LEIB = mdrsl.getString("LEIB");
			String LEIX = mdrsl.getString("LEIX");
			long JIHKJB_ID = (getExtGrid().getColumn("JIHKJB_ID").combo).getBeanId(mdrsl.getString("JIHKJB_ID"));
			String beiz=mdrsl.getString("BEIZ");
			String SHANGJGSBM=mdrsl.getString("SHANGJGSBM");
			
			boolean Dtmktssz=MainGlobal.getXitxx_item("������Ϣ", "��ͬú�������������", "0", "��").equals("��");
			
			String sql_check = "select id from meikxxb where (1=0 or bianm='"+BIANM+"' or mingc='"+MINGC+"' OR QUANC='"+QUANC+"')";
			
			if ("0".equals(mdrsl.getString("ID"))) {
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------��¼---------<br>--����:"
						+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
						+ "<br>�ļ�¼���ظ�,���ܱ���!";
					continue;
				}
				sql.append("begin \n");
//				���뵽��Ӧ�̱���
				sql.append("INSERT INTO GONGYSB\n" );
				sql.append("  (ID, FUID, XUH, MINGC, QUANC, PINY, BIANM, SHANGJGSBM,SHENGFB_ID, beiz,LEIX, ZHUANGT)\n");
				sql.append("VALUES\n");
				sql.append("  ("+gongysb_id+","+MEIKDQ_ID+",'"+xuh+"','"+MINGC+"','"+QUANC+"','"+PINY+"','"+BIANM+"'," +
						   "	'"+SHANGJGSBM+"',"+SHENGFB_ID+",'"+beiz+"',0,1);\n");
//				���뵽ú����Ϣ����
				sql.append("INSERT INTO MEIKXXB\n" );
				sql.append("  (ID,XUH,BIANM,MINGC,QUANC,PINY,SHENGFB_ID,JIHKJB_ID,CHENGSB_ID,LEIX,LEIB,\n" );
				sql.append("   SHANGJGSBM,MEIKDQ_ID,beiz,SHIYZT)\n" ); 
				sql.append("VALUES\n" );
				sql.append("   ("+meikxxb_id+","+xuh+",'"+BIANM+"','"+MINGC+"','"+QUANC+"','"+PINY+"'" +
						   ","+SHENGFB_ID+","+JIHKJB_ID+","+CHENGSB_ID+",'"+LEIX+"','"+LEIB+"','"+SHANGJGSBM+"',"+gongysb_id+",'"+beiz+"',1);\n");
				if(Dtmktssz){
					sql.append("UPDATE MEIKXXB SET MEIKDQ2_ID="+MEIKDQ_ID+" WHERE ID="+meikxxb_id+"; \n" );
				}
				sql.append("end;" );
			}else{
				sql_check+="and id<>"+mdrsl.getString("ID");
				if(con.getHasIt(sql_check)){
					SaveMsg += "----------��¼---------<br>--����:"
						+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
						+ "<br>�ļ�¼���ظ�,���ܱ���!";
					continue;
				}
				sql.append("begin \n");
//				���¹�Ӧ�̱�
				sql.append("UPDATE GONGYSB\n" );
				sql.append("   SET FUID       = "+MEIKDQ_ID+",\n" ); 
				sql.append("       XUH        = '"+xuh+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" );
				sql.append("       PINY       = '"+PINY+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       SHANGJGSBM = '"+SHANGJGSBM+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+",\n" );
				sql.append("       beiz = '"+beiz+"'\n" );
				sql.append(" WHERE ID = (SELECT MEIKDQ_ID FROM meikxxb WHERE ID="+mdrsl.getString("ID")+");\n");
//				����ú����Ϣ��
				sql.append("UPDATE MEIKXXB\n" );
				sql.append("   SET XUH        = '"+xuh+"',\n" );
				sql.append("       BIANM      = '"+BIANM+"',\n" );
				sql.append("       MINGC      = '"+MINGC+"',\n" );
				sql.append("       QUANC      = '"+QUANC+"',\n" ); 
				sql.append("       PINY       = '"+PINY+"',\n" ); 
				sql.append("       SHENGFB_ID = "+SHENGFB_ID+",\n" );
				sql.append("       JIHKJB_ID  = "+JIHKJB_ID+",\n" );
				sql.append("       LEIX       = '"+LEIX+"',\n" );
				sql.append("       LEIB       = '"+LEIB+"',\n" );
				sql.append("       beiz = '"+beiz+"',\n" );
				sql.append("       CHENGSB_ID = "+CHENGSB_ID+",\n" );
				sql.append("       SHANGJGSBM = '"+SHANGJGSBM+"'\n" );
				sql.append("    WHERE ID="+mdrsl.getString("ID")+";\n");
				if(Dtmktssz){
					sql.append("UPDATE MEIKXXB SET MEIKDQ2_ID="+MEIKDQ_ID+" WHERE ID="+mdrsl.getString("ID")+"; \n" );
				}
				sql.append("end;" );
			}
			int flag=con.getUpdate(sql.toString());
			sql.delete(0, sql.length());
			if(flag==-1){
				SaveMsg += "----------��¼---------<br>--����:"
					+ BIANM + "<br>--����:" + MINGC + "<br>--ȫ��:" + QUANC
					+ "<br>����ʧ��!";
			}
		}
		
		if(SaveMsg.length()>10){
			setSaveMsg(SaveMsg);
		}else{
			setSaveMsg("����ɹ�");
		}
	}

//	// �ж������Ƿ��ڱ��ؿ����Ѿ�����(�����ڷ���0�����ڷ�������)
//	private int Shujpd(JDBCcon con, String sql) {
//		return JDBCcon.getRow(con.getResultSet(sql));
//	}
//
//	// ��־��¼
//	private String logMsg = "";
//
//	private String zhuangT = "";

//	private void WriteLog(JDBCcon con) {
//
//		Visit visit = (Visit) this.getPage().getVisit();
//
//		if (!logMsg.equals("")) {// ��Ϊ�գ���Ҫд����־��¼
//
//			Date date = new Date();
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			String date_str = sdf.format(date);
//			String sql = " insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values("
//					+ " getnewid("
//					+ visit.getDiancxxb_id()
//					+ "),"
//					+ visit.getDiancxxb_id()
//					+ ",'"
//					+ visit.getRenymc()
//					+ "','"
//					+ leiBConstant
//					+ "',to_date('"
//					+ date_str
//					+ "','YYYY-MM-DD,HH24:mi:ss'),'"
//					+ this.zhuangT
//					+ "','"
//					+ logMsg + "')";
//
//			con.getInsert(sql);
//			logMsg = "";
//		}
//	}

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

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _BeginChick=false;
	
	public void BeginButtonQY(IRequestCycle cycle){
		_BeginChick = true;
	}
	
	private boolean _StopChick = false;
	
	public void StopButtonTY(IRequestCycle cycle){
		_StopChick = true;
	}

	private boolean _RbChick = false;

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}
	
//	"������Ӧ��"��ť
	private boolean _GuanlgysClick = false;
	
	public void GuanlgysButton(IRequestCycle cycle) {
		_GuanlgysClick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public String getmeikmc() {
		Visit visit = (Visit) getPage().getVisit();
		return visit.getString3();
	}

	public void setmeikmc(String meikmc) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setString3(meikmc);
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			ToAddMsg = "";
		}
		if (_InsertChick) {
			_InsertChick = false;
		}
		if(_BeginChick){
			_BeginChick = false;
			Begin();
			getSelectData();
		}
		if(_StopChick){
			_StopChick = false;
			Stop();
			getSelectData();
		}
		if (_RbChick) {
			_RbChick = false;
			gotochez(cycle);
		}
		if (_GuanlgysClick) {
			_GuanlgysClick = false;
			Visit visit = (Visit) getPage().getVisit();
			visit.setString2(visit.getActivePageName().toString());//ʹ��String2��¼��ǰ����
			visit.setString9(getParameters()); // ��ú��id�����¸�ҳ��
			visit.setString10(getMeikmc()); // ��ú�����ƴ����¸�ҳ��
			cycle.activate("Meikglgys");
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			ToAddMsg = "";
		}
	}

	public void getSelectData() {

		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str1 ="";
		if(getShiyztValue().getId() == 1){
			str1="and m.shiyzt = 1 \n";
		}else{
			str1="and m.shiyzt = 0 \n";
		}
		
		String condition="";
		if(getmeikmc()!=null && !getmeikmc().equals("")){
			condition=" and (m.mingc like '%"+getmeikmc()+"%' or m.quanc like '%"+getmeikmc()+"%')\n";
		}
		
		String sql = "SELECT DISTINCT M.ID,\n" +
			"                M.XUH,\n" + 
			"                G.DQMC       MEIKDQ_ID,\n" + 
			"                M.BIANM,\n" + 
			"                M.MINGC,\n" + 
			"                M.QUANC,\n" + 
			"                M.PINY,\n" + 
			"                G.SMC        AS SHENGFB_ID,\n" + 
			"                C.QUANC      AS CHENGSB_ID,\n" + 
			"                M.LEIB,\n" + 
			"                M.LEIX,\n" + 
			"                J.MINGC      AS JIHKJB_ID,\n" + 
			"                M.BEIZ,\n" + 
			"                M.SHANGJGSBM\n" + 
			"  FROM MEIKXXB M, JIHKJB J, CHENGSB C, VWGONGYSDQ G\n" + 
			" WHERE M.ID = G.MK_ID(+)\n" + 
			"   AND M.CHENGSB_ID = C.ID(+)\n" + 
			"   AND M.JIHKJB_ID = J.ID(+)\n" + 
			str1 +
			condition+
			" ORDER BY M.XUH";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikxxb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);

		egu.getColumn("xuh").setHeader("���");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(95);
		egu.getColumn("meikdq_id").setHeader("ú�����");
		egu.getColumn("meikdq_id").setWidth(130);
		egu.getColumn("bianm").editor.allowBlank = false;
		egu.getColumn("quanc").setHeader("ȫ��");
		egu.getColumn("quanc").setWidth(220);
		egu.getColumn("mingc").setHeader("����");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("piny").setHeader("ƴ��");
		egu.getColumn("piny").setWidth(80);

		egu.getColumn("shengfb_id").setHeader("ʡ��");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("shengfb_id").setEditor(null);
		egu.getColumn("leib").setHeader("���");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("leix").setHeader("����");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("shangjgsbm").setHeader("�ϼ���˾����");
		egu.getColumn("shangjgsbm").setWidth(80);
		egu.getColumn("shangjgsbm").setHidden(true);
		egu.getColumn("chengsb_id").setHeader("����");
		egu.getColumn("chengsb_id").setWidth(80);
		egu.getColumn("chengsb_id").setEditor(null);
		
		ComboBox combMeikdq = new ComboBox();
		egu.getColumn("meikdq_id").setEditor(combMeikdq);
//		�������е����ݿɱ༭
		combMeikdq.setEditable(true);
//		��дCombobox�Ĺ��˷��� ��ʹ֮����ģ��ƥ��������ַ�
		combMeikdq.setListeners("beforequery:function(e){" +
		                "var combo = e.combo;" +
		                "if(!e.forceAll){" +
		                "var value = e.query;" +
		                "combo.store.filterBy(function(record,id){" +
		                "var text = record.get(combo.displayField);" +
		                "" +
		                "return (text.indexOf(value)!=-1);" +
		                "});" +
		                "combo.expand();" +
		                "return false; " +
		                " } " +
		                "}");
		String Meikdqsql = "select id, mingc from meikdqb where zhuangt=1 order by xuh";
		egu.getColumn("meikdq_id").setComboEditor(egu.gridId,new IDropDownModel(Meikdqsql));
		egu.getColumn("meikdq_id").editor.allowBlank = true;
		
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id,mingc from jihkjb "));
		
		List l = new ArrayList();
		l.add(new IDropDownBean(1, "ͳ��"));
		l.add(new IDropDownBean(2, "�ط�"));
		egu.getColumn("leib").setEditor(new ComboBox());
		egu.getColumn("leib").setComboEditor(egu.gridId, new IDropDownModel(l));
		egu.getColumn("leib").setReturnId(false);
		egu.getColumn("leib").setDefaultValue("ͳ��");

		List k = new ArrayList();
		k.add(new IDropDownBean(1, "ú"));
		k.add(new IDropDownBean(2, "��"));
		k.add(new IDropDownBean(3, "��"));
		egu.getColumn("leix").setEditor(new ComboBox());
		egu.getColumn("leix").setComboEditor(egu.gridId, new IDropDownModel(k));
		egu.getColumn("leix").setReturnId(false);
		egu.getColumn("leix").setDefaultValue("ú");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		String meikhangs="select zhi from xitxxb where mingc='ú��ά��ҳ�浥ҳ��ʾ������' and zhuangt=1";
		rsl = con.getResultSetList(meikhangs);
		if(rsl.next()){
			String zhi=rsl.getString("zhi");
			egu.addPaging(Integer.parseInt(zhi));
		}else{//Ĭ��ÿҳ��ʾ25��
			egu.addPaging(25);
		}
		egu.addTbarText("ʹ��״̬:");
		ComboBox cb = new ComboBox();
		cb.setTransform("SHIYZT");
		cb.setWidth(80);
		cb.setListeners("select:function(){document.Form0.submit();}");
		egu.addToolbarItem(cb.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("ú������:");
		TextField tf = new TextField();
		tf.setId("meikmc");
		if(condition.length()>1){
			tf.setValue(getmeikmc());
		}
		egu.addToolbarItem(tf.getScript());
		
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('meikmc').value=meikmc.getValue(); document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		if(getShiyztValue().getId() == 0){
			egu.addTbarText("-");
			egu.addTbarBtn(new GridButton("����","function(){\n"+
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����¼');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.BeginButtonQY.click();" +
						"}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ���¼');\n"+
						"}}"));
		}else{
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("ͣ��","function(){" +
						"if(gridDiv_sm.getSelected()!=null){\n"+
						"var gridRow = gridDiv_sm.getSelected();\n"+
						"if(gridRow.get('ID')==0){Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ����¼');}\n"+
						"else{\n"+
						"document.getElementById('CHANGE').value = gridRow.get('ID');\n"+
						"document.all.StopButtonTY.click();}\n"+
						"}else{\n"+
						"Ext.MessageBox.alert('��ʾ��Ϣ','����ѡ���¼');\n"+
						"	}}"));
		}

		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		String sPowerHandler = "function(){"
				+ "if(gridDiv_sm.getSelected()== null){"
				+ "	Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú�������ó�վ');" + "	return;"
				+ "}" + "var grid_rcd = gridDiv_sm.getSelected();"
				+ "if(grid_rcd.get('ID') == '0'){"
				+ "	Ext.MessageBox.alert('��ʾ��Ϣ','�����ó�վ֮ǰ���ȱ���!');" + "	return;"
				+ "}" + "grid_history = grid_rcd.get('ID');"
				+ "var Cobj = document.getElementById('CHANGE');"
				+ "Cobj.value = grid_history;"
				+ "document.getElementById('RbButton').click();" + "}";
		egu.addTbarBtn(new GridButton("���ó�վ", sPowerHandler));
		
		egu.addTbarText("-");
		String guanlgys_click = 
			"function(){\n" +
			"    if(gridDiv_sm.getSelected()== null){\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��һ��ú���ٹ�����Ӧ��');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var rec = gridDiv_sm.getSelected();\n" + 
			"    if(rec.get('ID') == '0'){\n" + 
			"        Ext.MessageBox.alert('��ʾ��Ϣ','�ڹ�����Ӧ��֮ǰ���ȱ���!');\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    var cobjid = document.getElementById('PARAMETERS')\n" + 
			"    cobjid.value = rec.get('ID');\n" +
			"    var meikmc = document.getElementById('Meikmc')\n" + 
			"    meikmc.value = rec.get('MINGC');\n" + 
			"    document.getElementById('GuanlgysButton').click();\n" + 
			"}";
		egu.addTbarBtn(new GridButton("������Ӧ��", guanlgys_click));
		
		// �������ѡ��ť�����������ʾ��Ϣ��
		if (ToAddMsg.equals("toAdd")) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
		}
		
//		�ж��Ƿ��д������ݣ��������ʾ��ҳ����
		if (happenWrong) {
			StringBuffer sb = new StringBuffer("\n");
			String[] recs = getWrongDataSource().split("&");
			for (int i = 0; i < recs.length; i ++) {
				egu.addOtherScript("var p=new gridDiv_plant("+ recs[i] +");\n gridDiv_ds.insert("+ i +",p);\n");
				sb.append(egu.gridId).append("_ds.getAt("+ i +").beginEdit();\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").dirty=true;\n")
				.append(egu.gridId).append("_ds.getAt("+ i +").endEdit();\n");
			}
			egu.addOtherScript(sb.toString());
			happenWrong = false;
		}
		
		String shengf_click = 
			"gridDiv_grid.on('cellclick',\n" +
			"function(own, irow, icol, e){\n" + 
			"    row = irow;\n" + 
			"    if('SHENGFB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){\n" + 
			"        shengfTree_window.show();\n" + 
			"    }\n" + 
			"});";
		egu.addOtherScript(shengf_click);
		egu.addOtherScript(" gridDiv_sm.singleSelect=true;\n");

		if (tiShi) {
			tiShi = false;
			SaveMsg = "Ext.Msg.alert('��ʾ��Ϣ',\"" + SaveMsg + "\");";
		} 
		
		DefaultTree dt = new DefaultTree(DefaultTree.tree_sf_cs, "shengfTree", ""+visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		String handler = 
			"function() {\n" +
			"    var cks = shengfTree_treePanel.getSelectionModel().getSelectedNode();\n" + 
			"    if(cks==null){\n" + 
			"        shengfTree_window.hide();\n" + 
			"        return;\n" + 
			"    }\n" + 
			"    rec = gridDiv_grid.getSelectionModel().getSelected();\n" + 
			"    if(cks.getDepth() == 2){\n" + 
			"        rec.set('SHENGFB_ID', cks.parentNode.text);\n" + 
			"        rec.set('CHENGSB_ID', cks.text);\n" + 
			"    }else if(cks.getDepth() == 1){\n" + 
			"        rec.set('SHENGFB_ID', cks.text);\n" +
			"        rec.set('CHENGSB_ID', '');\n" + 
			"    }\n" + 
			"    shengfTree_window.hide();\n" + 
			"    return;\n" + 
			"}";
		
		ToolbarButton btn = new ToolbarButton(null, "ȷ��", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);

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
	
	public String getTreeScript() {
//		System.out.print(((Visit)this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDefaultTree(null);
			setShengfModel(null);
			setShengfModels();
			setChengsModel(null);
			setChengsModels();
			setShiyztValue(null);
			setShiyztModel(null);

			ToAddMsg = cycle.getRequestContext().getRequest().getParameter("MsgAdd");

			if (ToAddMsg == null) {
				ToAddMsg = "";
			}

			DataSource = visit.getString13();
			getSelectData();
		} else {
			getSelectData();
		}
	}
		
		//ʹ��������
		public IDropDownBean getShiyztValue() {
			if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getShiyztModel()
								.getOption(0));
			}
			return ((Visit) getPage().getVisit()).getDropDownBean5();
		}

		public void setShiyztValue(IDropDownBean value) {

			((Visit) getPage().getVisit()).setDropDownBean5(value);
		}

		public void setShiyztModel(IPropertySelectionModel value) {

			((Visit) getPage().getVisit()).setProSelectionModel5(value);
		}

		public IPropertySelectionModel getShiyztModel() {
			if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
				getShiyztModels();
			}
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}

		public IPropertySelectionModel getShiyztModels() {
			List shiyzt = new ArrayList();
			shiyzt.add(new IDropDownBean(1, "ʹ����"));
			shiyzt.add(new IDropDownBean(0, "δʹ��"));
			((Visit) getPage().getVisit())
					.setProSelectionModel5(new IDropDownModel(shiyzt));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
		}
		
	//���õķ���	
		public void Begin(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "begin \n update meikxxb set shiyzt = 1 where id ="+getChange()+";\n";
			sql+="UPDATE (SELECT ZHUANGT\n" +
				"          FROM GONGYSB\n" + 
				"         WHERE ID =\n" + 
				"               (SELECT MEIKXXB.MEIKDQ_ID FROM MEIKXXB WHERE ID = "+getChange()+"))\n" + 
				"   SET ZHUANGT = 0; \n end;";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("���óɹ�!");
				}
				else{
					setSaveMsg("����ʧ��!");
				}
		}
	//ͣ�õķ���	
		public void Stop(){
			JDBCcon co = new JDBCcon();
//			Visit visit = (Visit)getPage().getVisit();
			String sql = "begin \n update meikxxb set shiyzt = 0 where id ="+getChange()+";\n";
			sql+="UPDATE (SELECT ZHUANGT\n" +
				"          FROM GONGYSB\n" + 
				"         WHERE ID =\n" + 
				"               (SELECT MEIKXXB.MEIKDQ_ID FROM MEIKXXB WHERE ID = "+getChange()+"))\n" + 
				"   SET ZHUANGT = 0; \n end;";
			int flag = co.getUpdate(sql);
				if(flag!=-1){
					setSaveMsg("ͣ�óɹ�!");
				}
				else{
					setSaveMsg("ͣ��ʧ��!");
				}
		}
		
	}

