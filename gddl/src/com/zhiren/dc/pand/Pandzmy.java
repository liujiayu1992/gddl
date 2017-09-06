package com.zhiren.dc.pand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-10 16��30
 * �������޸�һ�������¹����̵����ķ���
 */
public class Pandzmy extends BasePage implements PageValidateListener {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	//�̵���������
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IDropDownModel value) {
		_pandModel = value; 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(v.isFencb()){
			dcsql = "and d.fuid = " + v.getDiancxxb_id();
		}
		if (_pandModel == null) {
			String sql = "select p.id,p.bianm from pandb p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " and p.zhuangt=0"
					+ " order by p.id desc";
		    _pandModel = new IDropDownModel(sql);
		}
	    return _pandModel;
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		_pandValue = value;
	}
	public IDropDownBean getPandValue() {
		return _pandValue;
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() 
					+ " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} else if (_CreateChick) {
			_CreateChick = false;
			createData();
		} else if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
	}
	public String getPandRiq(JDBCcon con, String pandBm) throws SQLException  {
		Date riq = null;
		String sRiq = "";
		String sql = "select riq from pandb where bianm='" + pandBm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			riq = rs.getDate("riq");
			sRiq = DateUtil.FormatDate(riq);
		}
		rs.close();
		return sRiq;
	}
	public double getShangYKC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shangykc = 0.0;
		String sql = "select kuc from yueshcyb where riq=first_day(add_months(to_date('" + riq + "','yyyy-mm-dd'),-1))"
				+ " and fenx='����' and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			shangykc = rs.getDouble("kuc");
		} else {
			riq = String.valueOf((Integer.parseInt(riq.substring(5,7)) - 1));
			setMsg(riq + "�¿��Ϊ0��");
		}
		rs.close();
		return shangykc;
	}
	public double[] getShouHC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shouhc[] = null;
		String sql = "select sum(shourl)as jiny,sum(fady)as fady,sum(gongry)as gongry,\n"
				+ "sum(qity)as qity\n"
				+ "from shouhcrbyb where riq between first_day(to_date('" + riq + "','yyyy-mm-dd'))\n"
				+ "and to_date('" + riq + "','yyyy-mm-dd') and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		shouhc = new double[4];
		if (rs.next()) {
//			 0:���½���  1:������  2:������  3:������  
			shouhc[0] = rs.getDouble("jiny");
			shouhc[1] = rs.getDouble("fady");
			shouhc[2] = rs.getDouble("gongry");
			shouhc[3] = rs.getDouble("qity");
		} else {
			for (int i = 0; i < shouhc.length; i++) {
				shouhc[i] = 0.0;
			}
		}
		rs.close();
		return shouhc;
	}
	public double getShiJKC(JDBCcon con, String bianm) throws SQLException {
		double cunyl = 0.0;
		String sql = "select sum(cunyl)as cunyl from pandyb,pandb where pandb_id=pandb.id and  bianm='" + bianm + "' group by pandb_id";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			cunyl = rs.getDouble("cunyl");
		}
		rs.close();
		return cunyl;
	}
	public void createData() {	
		String sql = "";
		String riq = "";
		String pandbm = getPandbm();
		long pandid = getPandbID();
		double shangykc = 0.0;
		double zhangmkc = 0.0;
		double shijkc = 0.0;
		double panyk = 0.0;
		double[] shouhc = null;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		try {
//			��ú����
			riq = getPandRiq(con, pandbm);
			if (riq.equals("") || riq == null) {
				setMsg("�̵�����Ϊ�գ�");
				return;
			}
//			���¿��
			shangykc = getShangYKC(con, riq, visit);
//			�պô��������
			shouhc = getShouHC(con, riq, visit);
//			ʵ�ʿ��
			shijkc = getShiJKC(con, pandbm);
//			������
//			 0:���½���  1:������  2:������  3:������  
			zhangmkc = shangykc + shouhc[0] - shouhc[1] - shouhc[2]- shouhc[3];
//			��ӯ��
			panyk = shijkc - zhangmkc;
//			ɾ�� pandzmy ����
			sql = "delete from pandzmy where pandb_id=" + pandid;
			con.getDelete(sql);
//			���� pandzmy ���� 
			sql = "insert into pandzmy values ("
				+ "getnewid(" + visit.getDiancxxb_id() + "),"
				+ pandid + ","
				+ shouhc[0] + ","
				+ shouhc[1] + ","
				+ shouhc[2] + ","
				+ shouhc[3] + ","
				+ zhangmkc + ","
				+ shijkc + ","
				+ panyk 
				+ ")";
			con.getInsert(sql);
			con.commit();
		} catch(Exception e) {
			con.rollBack();
			setMsg("��������ʧ�ܣ�");
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void delete() {
		String sSql = "";
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Zhangmy,
					"pandzmy",id);
		}
		sSql = "delete from pandzmy  where pandb_id=" + getPandbID();
		flag = con.getDelete(sSql);
		if (flag == -1) {
			setMsg("ɾ������ʧ�ܣ�");
		}
		con.Close();
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		con.setAutoCommit(false);
		ResultSetList rsl = null;
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandzmy.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//�����޸Ĳ���ʱ�����־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Zhangmy,
					"pandzmy",id+"");
			sSql = "update pandzmy set "
				+ " pandb_id=" + getPandbID() + ","					
				+ " benyjy=" + rsl.getDouble("benyjy") + ","
				+ " fadh=" + rsl.getDouble("fadh") + ","
				+ " gongrh=" + rsl.getDouble("gongrh") + ","
				+ " qity=" + rsl.getDouble("qity") + ","
				+ " zhangmkc=" + rsl.getDouble("zhangmkc") + ","
				+ " shijkc=" + rsl.getDouble("shijkc") + ","
				+ " panyk=" + rsl.getDouble("panyk")
				+ " where id=" + id;
			flag = con.getUpdate(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		con.commit();
		con.Close();
	}
	public void getSelectData() {
		String sSql = "";
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		sSql = "select pandzmy.id,benyjy,fadh,gongrh,qity,zhangmkc,shijkc,panyk\n" 
			+ " from pandzmy,pandb\n"
			+ " where pandb_id = pandb.id  and pandb.bianm='" + getPandbm() + "'";
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("benyjy").setHeader("���½���");
		egu.getColumn("benyjy").setEditor(null);
		egu.getColumn("fadh").setHeader("�����");
		egu.getColumn("gongrh").setHeader("���Ⱥ�");
		egu.getColumn("qity").setHeader("������");
		egu.getColumn("zhangmkc").setHeader("������");
		egu.getColumn("zhangmkc").setEditor(null);
		egu.getColumn("shijkc").setHeader("ʵ�ʿ��");
		egu.getColumn("shijkc").setEditor(null);
		egu.getColumn("panyk").setHeader("��ӯ��");
		egu.getColumn("panyk").setEditor(null);
		egu.addTbarText("�̵���룺");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("����","function(){document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("ɾ��",GridButton.ButtonType_SaveAll,"gridDiv",egu.gridColumns,"DeleteButton");
		gbt.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		StringBuffer script = new StringBuffer();
		script.append("\nvar tmpIndex = PandDropDown.getValue();\n");
		script.append("PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n");
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("var record = gridDiv_ds.getAt(e.row);\n")
				.append("var changeValue = eval(e.originalValue||0) - (eval(e.value||0)<0 ? 0 : eval(e.value||0));\n")
				.append("if (e.field == 'SHUIFC') {\n")
				.append("\tchangeValue = -changeValue;\n")
				.append("}\n")
				.append("var zhangmkc = eval(record.get('ZHANGMKC')||0);\n")
				.append("var shijkc = eval(record.get('SHIJKC')||0);\n")
				.append("zhangmkc =Math.round((zhangmkc + changeValue) * Math.pow(10,2)) / Math.pow(10,2);\n")
				.append("var panyk = Math.round((shijkc - zhangmkc) * Math.pow(10,2)) / Math.pow(10,2);\n")
				.append("if (eval(e.value||0)<0) {\n")
				.append("\trecord.set(e.field,0);\n")
				.append("}\n")
				.append("record.set('ZHANGMKC',zhangmkc);\n")
				.append("record.set('PANYK',panyk);\n")
				.append("});");
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		con.Close();
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		getSelectData();
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
}
