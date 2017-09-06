package com.zhiren.dc.jilgl.baob;

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.tree.DefaultTree;
//import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*����:���ܱ�
 * ����:2010-1-22 13:55:02
 * ����:���Ƿ����ú�ձ�
 * 
 * 
 */
public class Dianmrb_sx extends BasePage implements PageValidateListener {
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
//	ˢ���������ڰ�
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
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
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if (flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}

	// ��������
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	

		String riq1=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(this.getRiq()), -1, DateUtil.AddType_intDay));
		
		String chaxun ="diancxxb_id,gongysb_id,gongm,rucrz,likj,yunf,yingkc,shikc,lury\n" +
			" from dianmrb_sx where riq=to_date('"+riq1+"','yyyy-mm-dd')";

		
		// System.out.println("����ͬ�ڵ�����:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(chaxun);
		if (rslcopy.getRows()<=0) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + chaxun);
			setMsg("����������,��˶�!");
			return;
		}
		
		while (rslcopy.next()) {
			long diancxxb_id = rslcopy.getLong("diancxxb_id");
			long gongysb_id = rslcopy.getLong("gongysb_id");
			double   gongm=rslcopy.getDouble("gongm");
			
			double rucrz=rslcopy.getDouble("rucrz");
		
			double likj=rslcopy.getDouble("likj");
			double yunf=rslcopy.getDouble("yunf");
			double yingkc=rslcopy.getDouble("rez");
			double liuf=rslcopy.getDouble("liuf");
			String lurry=rslcopy.getString("lurry");
			String beiz = rslcopy.getString("beiz");
			long yunsfsb_id = rslcopy.getLong("yunsfsb_id");
			

			String _id = MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id());
			con.getInsert("insert into rijhb(ID,RIQ,DIANCXXB_ID,GONGYSB_ID,MEIKXXB_ID,DID,JIHL,MEICB_ID,XIEMFLB_ID,YUNJ1,YUNJ2,REZ,LIUF,LURRY,LURSJ,BEIZ,YUNSFSB_ID) values("
							+
							_id
							+ ","
							+ "to_date('"
							+ this.getRiq()
							+ "','yyyy-mm-dd'),"
							+ diancxxb_id+","
							+ gongysb_id+","
							
							+liuf+",'"
							+lurry+"',sysdate,'"
							+beiz+"',"
							+ yunsfsb_id + ")");

		}
		getSelectData();
		con.Close();
		this.setMsg("�����������ݳɹ�!");
	}



	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// ����������ݺ��·�������

		
			
			String chaxun ="select sx.id,sx.riq, dc.mingc as diancxxb_id,\n" +
				"g.mingc as gongysb_id,\n" + 
				"sx.gongm,sx.rucrz,sx.likj,sx.yunf,sx.yingkc,sx.shikc,sx.lury,sx.beiz\n" + 
				" from dianmrb_sx  sx ,gongysb g,diancxxb dc\n" + 
				"where riq=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
				"and sx.gongysb_id=g.id\n" + 
				"and sx.diancxxb_id=dc.id\n" + 
				"order by g.mingc";



			
			ResultSetList  	rsl = con.getResultSetList(chaxun);
		
		
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("dianmrb_sx");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("riq").setCenterHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setCenterHeader("�糧����");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setCenterHeader("������λ");
		egu.getColumn("gongm").setCenterHeader("ʵ��<br>(��)");
		egu.getColumn("rucrz").setCenterHeader("�볧��ֵ<br>(MJ/Kg)");
		egu.getColumn("likj").setCenterHeader("����<br>(Ԫ/��)");
		egu.getColumn("yunf").setCenterHeader("�˷�<br>(Ԫ/��)");
		egu.getColumn("yingkc").setCenterHeader("Ӧ���<br>(��)");
		egu.getColumn("shikc").setCenterHeader("ʵ���<br>(��)");
		
		

		
		egu.getColumn("lury").setCenterHeader("¼��Ա");
		egu.getColumn("lury").setHidden(true);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setCenterHeader("��ע");
		

		

		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("diancxxb_id").setWidth(120);
		
		egu.getColumn("gongm").setWidth(70);
		egu.getColumn("gongm").setDefaultValue("0");
		egu.getColumn("rucrz").setWidth(90);
		egu.getColumn("rucrz").setDefaultValue("0");
		egu.getColumn("likj").setWidth(80);
		egu.getColumn("likj").setDefaultValue("0");
		egu.getColumn("yunf").setWidth(80);
		egu.getColumn("yunf").setDefaultValue("0");
		egu.getColumn("yingkc").setWidth(80);
		egu.getColumn("yingkc").setDefaultValue("0");
		egu.getColumn("shikc").setWidth(90);
		egu.getColumn("shikc").setDefaultValue("0");
	
		egu.getColumn("beiz").setWidth(150);
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(100);// ���÷�ҳ

		// *****************************************����Ĭ��ֵ****************************
		
//		�糧������
		

		
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancmc());
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
			
		
		// �������ڵ�Ĭ��ֵ,
		egu.getColumn("riq").setDefaultValue(this.getRiq());
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());
		
		// *************************������*****************************************88
		// ���ù�Ӧ�̵�������
		//egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql = "select id,mingc from gongysb where leix=0 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		
		
	
		// ********************������************************************************
		egu.addTbarText("����:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		//System.out.println(getTreeid());
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// �趨�������������Զ�ˢ��
		egu.addTbarText("-");// ���÷ָ���
		String sRefreshHandler = 
			"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
			"if (grid_Mrcd.length>0) {" +
			"Ext.MessageBox.confirm('��Ϣ��ʾ','ˢ�½�����������ĸ��Ľ���������,�Ƿ����?',function(btn){" +
			"if (btn == 'yes') {" +
			"document.getElementById('RefreshButton').click();" +
			"}" +
			"})" +
			"}else {document.getElementById('RefreshButton').click();}" +
			"}";
		GridButton gRefresh = new GridButton("ˢ��",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addTbarText("-");
		
		GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert, egu.gridId,
				egu.getGridColumns(), null);
				gb_insert.setId("INSERT");
				egu.addTbarBtn(gb_insert);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);


	
//			egu.addToolbarItem("{"
//				+ new GridButton("����ͬ�ڼƻ�",
//				"function(){document.getElementById('CopyButton').click();}")
//				.getScript() + "}");
			
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("������������", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		

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
			
			this.setTreeid(null);
			//��Ӧ�̣�ú�󣬳�վ
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			setRiq(DateUtil.FormatDate( new Date()));
			this.setMsg(null);
			

		}
		getSelectData();

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
