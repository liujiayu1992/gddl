package com.zhiren.dc.kaoh.baozmxx;


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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * ʱ�䣺2012-08-14
 * ���ݣ���װúά������
 */
public class Baozmxxb extends BasePage implements PageValidateListener {
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

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}


	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
		}
		getSelectData();
	}

	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Baozmxxb.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			// ����ɾ������ʱ�����־
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(),
					visit.getRenymc(), SysConstant.RizOpType_DEL,
					"��װú��Ϣ", "baozmxxb", id + "");
			sSql = "delete from baozmxxb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Baozmxxb.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into baozmxxb values(getnewid("+visit.getDiancxxb_id()+")"
						+ ","
						+ "(select id from gongysb where mingc='"+rsl.getString("mingc")+"')"
						+ ",to_date('" +rsl.getString("briq")+ "','yyyy-mm-dd')"
						+ ",to_date('" +rsl.getString("eriq")+ "','yyyy-mm-dd')"
						+ ","+rsl.getString("baozl")
						+ ","+rsl.getString("jiakje")
						+ ",0)\n";

				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				sSql = "update baozmxxb set " + " gongysb_id="
				        + "(select id from gongysb where mingc='"+rsl.getString("mingc")+"')" + ","
						+ " briq=to_date('" + rsl.getString("briq")+"','yyyy-mm-dd'),"
						+ " eriq=to_date('" + rsl.getString("eriq")+"','yyyy-mm-dd'),"
						+ " baozl="+ rsl.getString("baozl") 
						+ ",jiakje="+ rsl.getString("jiakje") 
						+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
		con.Close();
		setMsg("����ɹ�");
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		String sSql = "SELECT b.id,g.mingc,briq,eriq,baozl,jiakje,decode(jiesb_id,0,'δ����','�ѽ���') as jiesb_id from \n" 
				+ "baozmxxb b,gongysb g\n"
				+ "WHERE b.gongysb_id=g.id\n"
				+ "AND g.id=b.gongysb_id\n"			
		+ "AND ((briq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and briq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --��ʼ������ѡ��ķ�Χ��\n " 
		+ " OR  (eriq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and eriq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --����������ѡ��ķ�Χ��\n "  
		+ " OR  (briq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and eriq>=to_date('"+getERiq()+"','yyyy-mm-dd'))) --��ʼ�������ڰ���ѡ���ķ�Χ\n ";


		ResultSetList rsl = con.getResultSetList(sSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("������λ");
		egu.getColumn("mingc").setWidth(150);
		egu.getColumn("briq").setHeader("��ʼʱ��");
		egu.getColumn("eriq").setHeader("����ʱ��");
		egu.getColumn("baozl").setHeader("��װ��(��)");
		egu.getColumn("jiakje").setHeader("�ӿ۽��(Ԫ)");
		egu.getColumn("jiakje").setDefaultValue("20000");
		egu.getColumn("jiesb_id").setHeader("����״̬");
		egu.getColumn("jiesb_id").setDefaultValue("δ����");
		egu.getColumn("jiesb_id").setEditor(null);
		egu.getColumn("mingc").setEditor(new ComboBox());
		String sql = "SELECT id,mingc FROM gongysb ";
		egu.getColumn("mingc").setComboEditor(egu.gridId, new IDropDownModel(sql));
		((ComboBox)egu.getColumn("mingc").editor).setEditable(true);
		ResultSetList r = con.getResultSetList(sql);
		String mingc = "";
		if (r.next()) {
			mingc = r.getString("mingc");
		}
		egu.getColumn("mingc").setDefaultValue(mingc);			
		egu.addTbarText("����:");
		DateField dStart = new DateField();
		dStart.setValue(getBRiq());
		dStart.Binding("BRIQ", "forms[0]");
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText(" ��");
		DateField dEnd = new DateField();
		dEnd.setValue(getERiq());
		dEnd.Binding("ERIQ", "forms[0]");
		egu.addToolbarItem(dEnd.getScript());
		GridButton gbt = new GridButton("ˢ��",
				"function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
				
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		StringBuffer sbDel=new StringBuffer();
		sbDel.append(" for( i=0;i<gridDiv_sm.getSelections().length;i++){\n");
		sbDel.append("	record = gridDiv_sm.getSelections()[i];\n");
		sbDel.append("	if (record.get('JIESB_ID')=='�ѽ���'){\n");
		sbDel.append("		alert('�Ѿ����㣬����ɾ����');\n");
		sbDel.append("		return;\n");
		sbDel.append("	}\n");
		sbDel.append("}\n");		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null,sbDel.toString());
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String Oscript=	"gridDiv_grid.on('beforeedit',function(e){\n"+
        "if(e.record.get('JIESB_ID')=='�ѽ���'){\n"+
        "	 e.cancel=true;\n"+       
        "}});\n";
		egu.addOtherScript(Oscript);
		//xieb end			
		setExtGrid(egu);
		con.Close();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString()
				.equals(this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			visit.setExtGrid1(null);
			setBRiq(DateUtil.FormatDate(new Date()));
			setERiq(DateUtil.FormatDate(new Date()));
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
