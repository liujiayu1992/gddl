package com.zhiren.dc.monthReport_mt;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2010-08-24
 * ��������ͷ�ֿ��պĴ��ձ�
 */
public class Fenkshcrb extends BasePage implements PageValidateListener {

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
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	ˢ���������ڰ�
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu=this.getExtGrid();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		String tableName="fenkshcrbb_mt";
		
		ResultSetList mdrsl = egu.getModifyResultSet(strchange);
		while (mdrsl.next()) {
			
			sql.append("update ").append(tableName).append(" set ")
				.append("yuejh=").append(mdrsl.getString("yuejh"))
				.append(",haom=").append(mdrsl.getString("dangrhm"))
				.append(",kuc=").append(mdrsl.getString("dangrkc"))
				.append(",beiz='").append(mdrsl.getString("beiz")).append("'")
			    .append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		}
		mdrsl.close();
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			setMsg("����ɹ���");
		}
		con.Close();
		sql=null;
	}
	
	public void DelData() {
		JDBCcon con = new JDBCcon();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String strRiq = rq[0]+"��"+rq[1]+"��"+rq[2]+"��";
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		
		String sql = "select * from fenkshcrbb_mt where riq>"+CurrODate+"\n";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			setMsg(strRiq+"�����ݲ������µ����ݣ�����ɾ����");
			return;
		}
		
		String strSql = "delete from fenkshcrbb_mt where riq="+ CurrODate
				+ "\n";
		int flag = con.getDelete(strSql);
		if (flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
					+ strSql);
			setMsg("ɾ�������з�������");
		} else {
			setMsg(strRiq + "�����ݱ��ɹ�ɾ����");
		}
		rsl.close();
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;

	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}

		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
//			Countlj();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}


	public void CreateData() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		Visit visit = (Visit) getPage().getVisit();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String strRiq = rq[0]+"��"+rq[1]+"��"+rq[2]+"��";
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		long diancxxb_id = visit.getDiancxxb_id();
		int flag_delete = -1;
		int flag_insert = -1;
		String DeSql = "delete fenkshcrbb_mt where riq = " + CurrODate + "\n";
		flag_delete = con.getDelete(DeSql);
		String strSql = 
			"select 1 as jihkjb_id,\n" +
			"		bt.gongysb_id,\n" +
			"       bt.meikxxb_id,\n" + 
			"       bt.laim,\n" + 
			"       nvl(zr.kuc,0)+bt.laim as kuc\n" + 
			"from\n" + 
			"(select f.gongysb_id,\n" + 
			"        f.meikxxb_id,\n" + 
			"        sum(nvl(f.jingz,0)) as laim\n" + 
			"from fahb f\n" + 
			"where f.daohrq = "+CurrODate+" and f.yunsfsb_id=1 \n" + 
			"group by f.gongysb_id,f.meikxxb_id) bt,\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       nvl(f.kuc,0) as kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1) and f.jihkjb_id=1) zr\n" + 
			"where bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)\n" +
		    "	union\n" +
		    "select 2 as jihkjb_id,\n" +
		    "		bt.gongysb_id,\n" +
			"       bt.meikxxb_id,\n" + 
			"       bt.laim,\n" + 
			"       nvl(zr.kuc,0)+bt.laim as kuc\n" + 
			"from\n" + 
			"(select 1 as gongysb_id,\n" + 
			"        1 as meikxxb_id,\n" + 
			"        sum(nvl(f.jingz,0)) as laim\n" + 
			"from fahb f\n" + 
			"where f.daohrq = "+CurrODate+" and f.yunsfsb_id<>1) bt,\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       nvl(f.kuc,0) as kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1) and f.jihkjb_id=2) zr\n" + 
			"where bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)";
		ResultSetList rsl = con.getResultSetList(strSql);
		String sql = "begin\n";
		String strid = "";
		while(rsl.next()){
			strid = MainGlobal.getNewID(con, diancxxb_id);
			sql += 
				"insert into fenkshcrbb_mt\n" +
				"values\n" + 
				"  ("+strid+",\n" + 
				"	"+rsl.getString("jihkjb_id")+",\n" +
				"   "+diancxxb_id+",\n" + 
				"   "+rsl.getString("gongysb_id")+",\n" + 
				"   "+rsl.getString("meikxxb_id")+",\n" + 
				"   "+CurrODate+",\n" + 
				"   "+rsl.getString("laim")+",\n" + 
				"   0,\n" + 
				"   "+rsl.getString("kuc")+",\n" + 
				"   0,\n" + 
				"   '');\n";
		}
		sql+="end;\n";
		if(sql.length()>14){
			flag_insert = con.getInsert(sql);
		}
		if(flag_delete!=-1&&flag_insert!=-1){
			rsl.close();
			con.commit();
			con.Close();
			setMsg(strRiq+"�����ݳɹ����ɣ�");
		} else {
			rsl.close();
			con.rollBack();
			con.Close();
			setMsg(strRiq+"�����ݳɹ�ʧ�ܣ�");
		}
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String MonthFirstDate = "";
		if(rq.length==3){
			MonthFirstDate = DateUtil.FormatOracleDate(rq[0]+"-"+rq[1]+"-01");
		}
		String CurrODate = DateUtil.FormatOracleDate(getRiq());
		String strSql = "";
		strSql =
			"select bt.id,\n" +
			"		decode(bt.jihkjb_id,1,'�ص㶩��','�г��ɹ�') as jihkjb_id,\n" +
			"       g.mingc as gongysb_id,\n" + 
			"       m.mingc as meikxxb_id,\n" + 
			"       nvl(bt.yuejh,0) as yuejh,\n" + 
			"       nvl(zr.kuc,0) as shangrkc,\n" + 
			"       nvl(bt.laim,0) as dangrlm,\n" + 
			"       nvl(lj.laim,0) as leijlm,\n" + 
			"       nvl(bt.haom,0) as dangrhm,\n" + 
			"       nvl(lj.haom,0) as leijhm,\n" + 
			"       nvl(bt.kuc,0) as dangrkc,\n" + 
			"       bt.beiz\n" + 
			"from gongysb g,\n" + 
			"     meikxxb m,\n" + 
			"(select f.id,\n" + 
			"		f.jihkjb_id,\n" +
			"       f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       f.yuejh,\n" + 
			"       f.laim,\n" + 
			"       f.haom,\n" + 
			"       f.kuc,\n" + 
			"       f.beiz\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = "+CurrODate+") bt,\n" + 
			"\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       f.kuc\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq = ("+CurrODate+"-1)) zr,\n" + 
			"\n" + 
			"(select f.gongysb_id,\n" + 
			"       f.meikxxb_id,\n" + 
			"       sum(f.laim) as laim,\n" + 
			"       sum(f.haom) as haom\n" + 
			"from fenkshcrbb_mt f\n" + 
			"where f.riq >= "+MonthFirstDate+"\n" + 
			"  and f.riq <= "+CurrODate+"\n" + 
			"group by f.gongysb_id,f.meikxxb_id) lj\n" + 
			"where bt.gongysb_id = g.id(+)\n" + 
			"  and bt.meikxxb_id = m.id(+)\n" + 
			"  and bt.gongysb_id = zr.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = zr.meikxxb_id(+)\n" + 
			"  and bt.gongysb_id = lj.gongysb_id(+)\n" + 
			"  and bt.meikxxb_id = lj.meikxxb_id(+)\n" +
			"order by bt.jihkjb_id,g.mingc,m.mingc\n";

		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
					+ strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("fenkshcrbb_mt");
		// /������ʾ������
		egu.setWidth("bodyWidth");
		// egu.setHeight("bodyHeight");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setWidth(120);
		egu.getColumn("gongysb_id").setHeader("������λ");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setHeader("ú��λ");
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("yuejh").setHeader("�¼ƻ�");
		egu.getColumn("yuejh").setWidth(65);
		egu.getColumn("shangrkc").setHeader("���տ��");
		egu.getColumn("shangrkc").setEditor(null);
		egu.getColumn("shangrkc").setWidth(65);
		egu.getColumn("dangrlm").setHeader("������ú");
		egu.getColumn("dangrlm").setEditor(null);
		egu.getColumn("dangrlm").setWidth(65);
		egu.getColumn("leijlm").setHeader("�ۼ���ú");
		egu.getColumn("leijlm").setEditor(null);
		egu.getColumn("leijlm").setWidth(65);
		egu.getColumn("dangrhm").setHeader("���պ�ú");
		egu.getColumn("dangrhm").setWidth(65);
		egu.getColumn("leijhm").setHeader("�ۼƺ�ú");
		egu.getColumn("leijhm").setEditor(null);
		egu.getColumn("leijhm").setWidth(65);
		egu.getColumn("dangrkc").setHeader("���տ��");
		egu.getColumn("dangrkc").setEditor(null);
		egu.getColumn("dangrkc").setWidth(65);
		egu.getColumn("beiz").setHeader("��ע");
		egu.getColumn("beiz").setWidth(90);

		egu.setDefaultsortable(false);

		// �������и�Ϊ���Ա༭�ġ�
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);

		egu.addTbarText("����:");
		DateField dfRIQ = new DateField();
		dfRIQ.setReadOnly(true);
		dfRIQ.Binding("RIQ", "forms[0]");
		dfRIQ.setValue(getRiq());
		dfRIQ.setId("RIQ");
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		// ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb
				.append("function (){")
				.append(
						MainGlobal
								.getExtMessageBox(
										"'����ˢ������,���Ժ�'",
										true)).append(
						"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// ���ɰ�ť
		GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
		GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		// ���水ť
		GridButton gbs = new GridButton(GridButton.ButtonType_Save, "gridDiv", egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(gbs);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append(
				"	var haom = 0;											\n"+
				"	var leijhm = 0; var dangrkc = 0; var r = 0;				\n"+
				"	gridDiv_grid.on('beforeedit',function(e){				\n"+
				"		if(e.field=='DANGRHM'){								\n"+
				"			haom = e.value;									\n"+
				"		}													\n"+
				"	});														\n"+
				"	gridDiv_grid.on('afteredit',function(e){				\n"+
				"		r = e.row;											\n"+
				"		if(e.field=='DANGRHM'){								\n"+
				"			leijhm = eval(gridDiv_ds.getAt(r).get('DANGRHM'))-eval(haom)+eval(gridDiv_ds.getAt(r).get('LEIJHM'));\n" +
				"			dangrkc = eval(gridDiv_ds.getAt(r).get('SHANGRKC'))+eval(gridDiv_ds.getAt(r).get('DANGRLM'))-eval(gridDiv_ds.getAt(r).get('DANGRHM'));\n" +
				"			gridDiv_ds.getAt(r).set('LEIJHM',leijhm);		\n"+
				"			gridDiv_ds.getAt(r).set('DANGRKC',dangrkc);		\n"+
				"		}													\n"+
				"	});														\n"
		);
	
		egu.addOtherScript(sb.toString());
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// ��ť��script
		StringBuffer btnsb = new StringBuffer();
		String riq = getRiq();
		String[] rq = riq.split("-");
		String cnDate = rq[0] + "��" + rq[1] + "��" + rq[2] + "��";
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
//-----------------  ���������ͷż�¼��   ----------------------------------------------------
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiq(DateUtil.FormatDate( new Date()));
			getSelectData();
		}
		getSelectData();
	}

//	private boolean Cleaning_up_History(String Date,String Condition){
////		���û���������ɡ���ťʱ��Ҫ����ɾ������������ݣ��������¼���
////		���û���������桱��ťʱ��Ҫ���Ƚ��ۼ�����ɾ�����ٴ��¼����ۼ�ֵ
//		JDBCcon con = new JDBCcon();
//		String sql="";
//		boolean Falg=false;
//		String diancxxb_id=this.getTreeid();
//		
//		sql=
//				"delete from yuercbmdj where id in (\n" +
//				"    select js.id\n" + 
//				"       from yuercbmdj js,yuetjkjb_mt kj\n" + 
//				"       where js.yuetjkjb_id=kj.id\n" + 
//				"			  and kj.diancxxb_id=" +diancxxb_id+"\n"+
//				"             and kj.riq="+Date+"\n" +Condition+ 
//				")";
//		
//		if(con.getDelete(sql)>=0){
//			
//			Falg=true;
//		}
//		con.Close();
//		return Falg;
//	}
}