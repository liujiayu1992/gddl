package com.zhiren.dc.kaoh.hetsfkh;

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
 * ���ݣ����ݺ�ͬ¼��ˮ�ֱ�׼��Ϊ�۶������ṩ����
 */
public class Hetsfkh extends BasePage implements PageValidateListener {
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

	// xieb begin
	// ������
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}

	// ������
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	// xieb end
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
					+ "Hetsfkh.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			// ����ɾ������ʱ�����־
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(),
					visit.getRenymc(), SysConstant.RizOpType_DEL,
					"��ͬˮ�ֿ���", "HETSFKHB", id + "");
			sSql = "delete from HETSFKHB where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Hetsfkh.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			String kaohms="";
			String item_id="(select id from item where mingc='" + rsl.getString("mos") +"')";
			ResultSetList mos = con.getResultSetList(item_id);
			 if(mos.next()){
				kaohms= mos.getString("id");
			 }
			
			String d[]=rsl.getString("heth").split("_");			
			if (id == 0) {//���
				String sql="select * from hetsfkhb where hetb_id= (select id from hetb where hetbh='"+d[0]+"')";
				ResultSetList rsl_in = con.getResultSetList(sql);
				 if(rsl_in.next()){
					setMsg("�����ظ�,������ӣ�");
					return;
				 }
				 if(rsl.getString("mt").equals("")||rsl.getString("mt")==null){
						setMsg("ˮ��ֵ����Ϊ��");
						return;
					 }
				sSql = "insert into hetsfkhb values(getnewid("+visit.getDiancxxb_id()+")"
						+ ","
						+ "(select id from hetb where hetbh='"+d[0]+"')"
						+ "," + rsl.getString("mt")
						+ ",'"+rsl.getString("beiz") 
						+ "'," + kaohms
						+ ")\n";

				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {		
				String sql="select * from hetsfkhb where id<>"+rsl.getString("id")+" and hetb_id= (select id from hetb where hetbh='"+d[0]+"')";
				ResultSetList rsl_in = con.getResultSetList(sql);
				 if(rsl_in.next()){
					 	setMsg("����ظ�,�����޸ģ�");
						return; 
				 }				
				 else{
				sSql = "update hetsfkhb set " + " hetb_id="
				        + "(select id from hetb where hetbh='"+d[0]+"')" + ","
						+ " mt=" + rsl.getString("mt")
						+ ",mos=(select id from item where mingc='"+ rsl.getString("mos") 
						+ "'),beiz='" 
						+ rsl.getString("beiz") 
						+ "' where id=" + id;
				flag = con.getUpdate(sSql);
				}
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
		String sSql = "SELECT kh.ID,KH.HETB_ID,HT.HETBH||'_'||GYS.MINGC AS HETH,MT,\n" 
				+ "(select mingc from item where id=kh.MOS) mos,kh.BEIZ,nvl(js.id,0) jieszt FROM\n"
				+ "HETSFKHB KH,HETB HT,gongysb gys,jiesb js \n"
				+ "WHERE kh.hetb_id=ht.id\n"
				+ "AND gys.id=ht.gongysb_id\n"	
				+ "AND js.hetb_id(+)=ht.id\n"	
				+ "AND ((ht.qisrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and ht.qisrq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --��ʼ������ѡ��ķ�Χ��\n " 
				+ " OR  (ht.guoqrq>=to_date('"+getBRiq()+"','yyyy-mm-dd') and ht.guoqrq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --����������ѡ��ķ�Χ��\n "  
				+ " OR  (ht.qisrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and ht.guoqrq>=to_date('"+getERiq()+"','yyyy-mm-dd'))) --��ʼ�������ڰ���ѡ���ķ�Χ\n ";

		ResultSetList rsl = con.getResultSetList(sSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setEditor(null);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("hetb_id").setHeader("HETB_ID");
		egu.getColumn("hetb_id").setEditor(null);
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("heth").setHeader("��ͬ���");
		egu.getColumn("heth").setWidth(200);
		egu.getColumn("mt").setWidth(130);
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mos").setHeader("����ģʽ");
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(200);
		egu.getColumn("jieszt").setHeader("����״̬");
		egu.getColumn("jieszt").setEditor(null);
		egu.getColumn("jieszt").setHidden(true);
		egu.getColumn("heth").setEditor(new ComboBox());
		egu.getColumn("heth").setComboEditor(egu.gridId, 
				new IDropDownModel("select ht.id,HT.HETBH||'_'||GYS.MINGC AS HETH \n" +
								   "from hetb ht,gongysb gys \n" +
								   "where ht.gongysb_id=gys.id \n" 
									+ "AND ((ht.qisrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and ht.qisrq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --��ʼ������ѡ��ķ�Χ��\n " 
									+ " OR  (ht.guoqrq>=to_date('"+getBRiq()+"','yyyy-mm-dd') and ht.guoqrq<=to_date('"+getERiq()+"','yyyy-mm-dd')) --����������ѡ��ķ�Χ��\n "  
									+ " OR  (ht.qisrq>=to_date('"+getBRiq()+"','yyyy-mm-dd')  and ht.guoqrq>=to_date('"+getERiq()+"','yyyy-mm-dd'))) --��ʼ�������ڰ���ѡ���ķ�Χ\n "

								   ));
		egu.getColumn("mos").setEditor(new ComboBox());
		String sql = "SELECT item.id,item.mingc FROM item,itemsort "
				+ "WHERE item.itemsortid=itemsort.id "
				+ "AND itemsort.bianm='HETSFKHMS'";
		egu.getColumn("mos")
				.setComboEditor(egu.gridId, new IDropDownModel(sql));
		ResultSetList r = con.getResultSetList(sql);
		String mingc = "";
		if (r.next()) {
			mingc = r.getString("mingc");
		}
		egu.getColumn("mos").setDefaultValue(mingc);
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
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		
		StringBuffer sbDel=new StringBuffer();
		sbDel.append(" for( i=0;i<gridDiv_sm.getSelections().length;i++){\n");
		sbDel.append("	record = gridDiv_sm.getSelections()[i];\n");
		sbDel.append("	if (record.get('JIESZT')==1){\n");
		sbDel.append("		alert('�Ѿ����㣬����ɾ����');\n");
		sbDel.append("		return;\n");
		sbDel.append("	}\n");
		sbDel.append("}\n");		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null,sbDel.toString());
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String Oscript=	"gridDiv_grid.on('beforeedit',function(e){\n"+
        "if(e.record.get('ID') != 0 && e.record.get('JIESZT')==1){\n"+  
        "	 e.cancel = true;\n"+       
        "}});\n";					
		egu.addOtherScript(Oscript);
		
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
//			setJieszt("");
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
