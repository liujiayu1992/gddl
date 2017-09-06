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
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


/*����:���ܱ�
 * ʱ��:2009-11-6 10:17:09
 * ����:�޸�ˢ������,���һ�������·ֳ����Ӧ��
 * 
 */
public class Pand extends BasePage implements PageValidateListener{
//	�����û���ʾ
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
//	 ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		return riq;
	}
	
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
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
		return getTree().getWindowTreeScript();
	}
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid==null||treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
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
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setOriRiq(getRiq());
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
	}
	public void save() {
		String sSql = "";
		String sMessage = "";
		String msg_insert="";
		String msg_update="";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		ResultSetList rs_bianm=new ResultSetList();
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandb.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			String zhuangt = rsl.getString("zhuangt");
			if (zhuangt.equals("��")) {
				sMessage = sMessage + "�� �룺" + rsl.getString("bianm") + "</br>";
				continue;
			} else {
				//����ɾ������ʱ�����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Pand,
						"pandb",id+"");
				sSql = "delete from pandb where id=" + id;
				flag = con.getDelete(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();				
				}
			}

		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandb.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				
				String sqlTem="select p.bianm from pandb p where p.diancxxb_id="+getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))
							+" and p.bianm='"+rsl.getString("bianm")+"'";
				
				rs_bianm=con.getResultSetList(sqlTem);
				
				if(rs_bianm!=null && rs_bianm.next()){//˵�������ظ�
					
					msg_insert+="�� �룺" + rsl.getString("bianm") + "</br>";
					continue;
				}
				sSql = "insert into pandb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ","
					+ "to_date('" + getOriRiq() + "','yyyy-mm-dd'),'"
					+ rsl.getString("bianm") + "','"
					+ rsl.getString("lury") + "',"
					+ "0,'"
					+ rsl.getString("beiz") + "','"
					+ rsl.getString("pandyj") + "'"
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				
				String sqlTem="select p.bianm from pandb p where p.diancxxb_id="+getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id"))
				+" and p.bianm='"+rsl.getString("bianm")+"' and id <> " +id;
			
				rs_bianm=con.getResultSetList(sqlTem);
				
				if(rs_bianm!=null && rs_bianm.next()){//˵�������ظ�
					
					

					String sql_bianm="select p.bianm from pandb p where p.id="+id;
					
					ResultSetList rm=con.getResultSetList(sql_bianm);
					
					if(rm.next()){
						msg_update+="�� �룺" + rm.getString("BIANM") + "</br>";
					}
					
					rm.close();
					continue;
				}
	
	
				//�����޸Ĳ���ʱ�����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Pand,
						"pandb",id+"");
				sSql = "update pandb set "
					+ " diancxxb_id=" + getExtGrid().getColumn("diancxxb_id").combo.getBeanId(rsl.getString("diancxxb_id")) + ","					
					+ " bianm='" + rsl.getString("bianm") + "',"
					+ " lury='" + rsl.getString("lury") + "',"
					+ " zhuangt=0,"
					+ " beiz='" + rsl.getString("beiz") + "',"
					+ " pandyj='" + rsl.getString("pandyj") + "'"
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
		con.Close();
		
		String msg_alert="";
		if (!sMessage.equals("")) {
			sMessage = sMessage + "����ɾ��<br>";
			
			msg_alert+=sMessage;
			//setMsg(sMessage);
		}
		if(!msg_insert.equals("")){
			msg_insert+="�������,���ظ���¼<br>";
			msg_alert+=msg_insert;
		}
		if(!msg_update.equals("")){
			msg_update+="���ܸ��ģ����ظ���¼<br>";
			msg_alert+=msg_update;
		}
		
		if(!msg_alert.equals("")){
			this.setMsg(msg_alert);
		}
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		visit.getRenyID();
		JDBCcon con = new JDBCcon();
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
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
			if(visit.isFencb()){//�Ƿ�ֳ���
			 str="and (dc.id = " + getTreeid() + " or dc.fuid = "
				+ getTreeid() + ")";
			}else{
				str = "and dc.id = " + getTreeid() + "";
			}
		
		}
		String sSql = "select p.id,dc.mingc as diancxxb_id,p.riq,p.bianm,p.lury,decode(p.zhuangt,0,'��','��')as zhuangt,p.beiz,p.pandyj\n"
				+ "from pandb p,diancxxb dc where p.diancxxb_id=dc.id and riq=to_date('" + getRiq() + "','yyyy-mm-dd')\n"
				+ str + " order by dc.id,p.id";
//		System.out.println(sSql);
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("�糧����");
		egu.getColumn("diancxxb_id").setWidth(130);
		egu.getColumn("riq").setHeader("����");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setDefaultValue(getRiq());
		egu.getColumn("bianm").setHeader("����");
		egu.getColumn("bianm").setWidth(130);
		egu.getColumn("lury").setHeader("¼��Ա");
		egu.getColumn("lury").setWidth(130);
		egu.getColumn("zhuangt").setHeader("�༭");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(250);
		egu.getColumn("pandyj").setHeader("���");
		egu.getColumn("pandyj").setWidth(250);
		
		IDropDownModel dc;
		if (treejib == 1) {
			dc =  new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc");
		} else if (treejib == 2) {
			dc =  new IDropDownModel("select id,mingc from diancxxb where fuid=" + getTreeid() + " order by mingc");
		} else {
			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			if(visit.isFencb()){
				dc = new IDropDownModel("select id,mingc from diancxxb where id = "+getTreeid()+" or fuid="+getTreeid()+" order by mingc");
			}else{
				dc = new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");
			}
		}
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,dc);
		egu.getColumn("diancxxb_id").setReturnId(true);
		egu.getColumn("diancxxb_id").setDefaultValue(dc.getBeanValue(getTreeid()));
		
		
		String lury = visit.getRenymc();
		if (!lury.equals("")) {
			egu.getColumn("lury").setEditor(null);
			egu.getColumn("lury").setDefaultValue(lury);		
		}
		egu.addTbarText("����:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String script = "";
		script = "\ngridDiv_grid.on('beforeedit', function(e) {\n" 
				+ "\tif (e.record.get('ZHUANGT') == '��') {e.cancel = true;}"
				+ "});";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			setTreeid(null);
		}
		init();
	}
	private void init() {
		setOriRiq(getRiq());
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