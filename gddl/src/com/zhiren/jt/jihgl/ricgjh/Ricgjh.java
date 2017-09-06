package com.zhiren.jt.jihgl.ricgjh;

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
 * ����:2010-1-18 20:10:13
 * ����:���Ƿ����չ�ú�ƻ�
 * 
 * 
 */
public class Ricgjh extends BasePage implements PageValidateListener {
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
	
	//������ֵ���
	private boolean _CreatButton = false;

	public void CreatButton(IRequestCycle cycle) {
		_CreatButton = true;
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
		if (_CreatButton) {
			_CreatButton = false;
			CreatRezLiuf();
		}
	}

	//������ֵ���
	public void CreatRezLiuf() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û�������κθ���Ŷ��");
			return;
		}
		JDBCcon con = new JDBCcon();
		String sql1="select r.riq,r.gongysb_id,r.meikxxb_id from rijhb r where id="+getChange();
		ResultSetList rs1 = con.getResultSetList(sql1);
		if (rs1.getRows()<=0) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql1);
			setMsg("����,û���ҵ���ú��Ķ�Ӧ����!");
			con.Close();
			return;
		}
		String riq= "";
		long gongysb_id=0;
		long meikxxb_id=0;
		while (rs1.next()) {
			 riq=DateUtil.FormatDate(rs1.getDate("riq"));
			 gongysb_id=rs1.getLong("gongysb_id");
			 meikxxb_id=rs1.getLong("meikxxb_id");
			
		}
		//ȡ��������ǰ7��Ļ������ݼ�Ȩƽ��
		String riq2=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(this.getRiq()), -7, DateUtil.AddType_intDay));
		String sql_rez_liuf=
			"select f.meikxxb_id,\n" +
			"round_new(sum(ls.qnet_ar*f.laimsl)/sum(f.laimsl)*1000/4.1816,0) as rez,\n" + 
			"round_new(sum(ls.stad*f.laimsl)/sum(f.laimsl),2) as liuf\n" + 
			"from zhillsb ls,fahb f\n" + 
			"where\n" + 
			" f.zhilb_id=ls.zhilb_id\n" + 
			"and f.daohrq>=to_date('"+riq2+"','yyyy-mm-dd')\n" + 
			"and f.daohrq<=to_date('"+riq+"','yyyy-mm-dd')\n" + 
			"and f.gongysb_id="+gongysb_id+"\n" + 
			"and f.meikxxb_id="+meikxxb_id+"\n" + 
			"and ls.shenhzt=5\n" + 
			"group by  (f.meikxxb_id)";
		
		rs1=con.getResultSetList(sql_rez_liuf);
		if (rs1.getRows()<=0) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sql1);
			setMsg("��ú��ǰ7��û�ж�Ӧ�Ļ���ֵ,���ֹ�ά����ֵ!");
			con.Close();
			return;
		}
		long meik=0;
		long rez=0;
		double liuf=0.0;
		while (rs1.next()) {
			meik=rs1.getLong("meikxxb_id");
			rez=rs1.getLong("rez");
			liuf=rs1.getDouble("liuf");
			
		}
		
		String UpdateRijhb="update rijhb set rez="+rez+",liuf="+liuf+" where id ="+getChange();
		int zhuangt=con.getUpdate(UpdateRijhb);
		if(zhuangt==1){
			this.setMsg("������ֵ����ֳɹ�!");
		}else{
			this.setMsg("������ֵ�����ʧ��!");
		}
		con.Close();
		this.getSelectData();
	}

	
	
	// ������������
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	

		String riq1=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(this.getRiq()), -1, DateUtil.AddType_intDay));
		
		String chaxun ="select r.diancxxb_id,r.gongysb_id,r.meikxxb_id,r.did,r.jihl,\n" +
			"r.meicb_id,r.xiemflb_id,r.yunj1,r.yunj2,r.rez,r.liuf,r.lurry,r.beiz,r.yunsfsb_id\n" + 
			" from rijhb r where r.riq=to_date('"+riq1+"','yyyy-mm-dd')";

		
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
			long meikxxb_id=rslcopy.getLong("meikxxb_id");
			String did=rslcopy.getString("did");
			double jihl=rslcopy.getDouble("jihl");
			long meicb_id=rslcopy.getLong("meicb_id");
			long xiemflb_id=rslcopy.getLong("xiemflb_id");
			double yunj1=rslcopy.getDouble("yunj1");
			double yunj2=rslcopy.getDouble("yunj2");
			double rez=rslcopy.getDouble("rez");
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
							+ meikxxb_id+",'"
							+ did+ "',"
							+ jihl+ ","
							+ meicb_id+ ","
							+ xiemflb_id+ ","
							+ yunj1+ ","
							+ yunj2+ ","
							+ rez+ "," 
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

		
			
			String chaxun =
				"select r.id,r.riq, dc.mingc as diancxxb_id,g.mingc as gongysb_id,mk.mingc as meikxxb_id,ys.mingc as yunsfsb_id,\n" +
				"r.did,r.jihl,mc.mingc as meicb_id,fl.mingc as xiemflb_id,r.yunj1,r.yunj2,r.rez,r.liuf,r.lurry,r.beiz\n" + 
				"from rijhb r,diancxxb dc ,gongysb g ,meikxxb mk,meicb mc,xiemflb fl,yunsfsb ys\n" + 
				"where r.diancxxb_id=dc.id\n" + 
				"and r.gongysb_id=g.id\n" + 
				"and r.meikxxb_id=mk.id\n" + 
				"and r.riq=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n"+
				"and r.meicb_id=mc.id(+)\n" + 
				"and r.xiemflb_id=fl.id(+)\n" + 
				"and r.yunsfsb_id=ys.id order by g.mingc,mk.mingc";


			
			ResultSetList  	rsl = con.getResultSetList(chaxun);
		
		
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rijhb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
		egu.getColumn("did").setHeader("�ص�");
		egu.getColumn("jihl").setHeader("�ƻ���");
		egu.getColumn("meicb_id").setHeader("жú�ص�");
		egu.getColumn("xiemflb_id").setHeader("жú����");
		egu.getColumn("yunj1").setHeader("һ���˾�(Km)");
		egu.getColumn("yunj2").setHeader("�����˾�(Km)");
		
		egu.getColumn("rez").setHeader("��ֵ(��/��)");
		egu.getColumn("liuf").setHeader("���(%)");
		egu.getColumn("lurry").setHeader("¼��Ա");
		egu.getColumn("lurry").setHidden(true);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("beiz").setHeader("��ע");
		

		

		// �趨�г�ʼ���
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yunsfsb_id").setWidth(70);
		egu.getColumn("did").setWidth(70);
		egu.getColumn("jihl").setWidth(80);
		egu.getColumn("jihl").setDefaultValue("0");
		egu.getColumn("meicb_id").setWidth(80);
		
		egu.getColumn("xiemflb_id").setWidth(80);
		egu.getColumn("yunj1").setDefaultValue("0");
		egu.getColumn("yunj1").setWidth(80);
		egu.getColumn("yunj2").setDefaultValue("0");
		egu.getColumn("yunj2").setWidth(80);
	
		egu.getColumn("rez").setWidth(90);
		egu.getColumn("liuf").setWidth(90);
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
		egu.getColumn("lurry").setDefaultValue(visit.getRenymc());
		// *************************������*****************************************88
		// ���ù�Ӧ�̵�������
		//egu.getColumn("gongysb_id").setEditor(new ComboBox());
		ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql = "select id,mingc from gongysb  where leix=1 order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// ����ú��������
		ComboBox cb_meik = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String meikSql = "select id ,mingc from meikxxb c  order by c.mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(meikSql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		// �������䷽ʽ������
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		
		// ����ú��������
		ComboBox c5 = new ComboBox();
		egu.getColumn("meicb_id").setEditor(c5);
		c5.setEditable(true);

		String pinzSql = "select id,mingc from meicb order by mingc";
		egu.getColumn("meicb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		egu.getColumn("meicb_id").setReturnId(true);
		
		
//		 ����жú����������
		ComboBox c8 = new ComboBox();
		egu.getColumn("xiemflb_id").setEditor(c8);
		c5.setEditable(true);

		String xiemflSql = "select id,mingc from xiemflb order by mingc";
		egu.getColumn("xiemflb_id").setComboEditor(egu.gridId,
				new IDropDownModel(xiemflSql));
		egu.getColumn("xiemflb_id").setReturnId(true);
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
		egu.addTbarText("-");// ���÷ָ���
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");// ���÷ָ���
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);
		egu.addTbarText("-");// ���÷ָ���

	
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
			egu.addTbarText("-");// ���÷ָ���
			//������ֵ���
			
			
			
			String sPwHandler = "function(){"
				+"if(gridDiv_sm.getSelected() == null){"
				+"	 Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ��Ҫ���ɵ�������!');"
				+"	 return;"
				+"}"
				+"var grid_rcd = gridDiv_sm.getSelected();"
				
				+"if(grid_rcd.get('ID') == 0){"
				+"	 Ext.MessageBox.alert('��ʾ��Ϣ','���ȱ�������!');"
				+"	 return;"
				+"}"
				+"		    grid_history = grid_rcd.get('ID');"
				+"			var Cobj = document.getElementById('CHANGE');"
				+"			Cobj.value = grid_history;"
				+"			document.getElementById('CreatButton').click();"
			
				+"}";
			egu.addTbarBtn(new GridButton("������ֵ���",sPwHandler));

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
		String sql = "select id,mingc from gongysb  where leix=1 order by xuh,mingc";
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
