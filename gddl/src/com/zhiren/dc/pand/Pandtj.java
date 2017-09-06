package com.zhiren.dc.pand;

/*
 * ���ߣ���ɭ��
 * ʱ�䣺2009-09-04
 * ����������xitxxb���ã���Լ�̵��ܶȱ���С��λ Ĭ��Ϊ4λ��������
 * 		mingc	= '�̵��ܶȱ���С��λ'
 * 		zhi		= С��λ
 * 		leib 	= '�̵�'
 * 		zhuangt = 1
 * 		diancxxb_id = �糧ID
 */
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
public class Pandtj extends BasePage implements PageValidateListener {
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
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
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
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + " and zhuangt=0 order by id desc";
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
	private boolean _AddChick = false;
	public void AddButton(IRequestCycle cycle) {
		_AddChick = true;
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
		} else if (_AddChick) {
			getSelectData();
		} else if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
	}
	public void delete() {
		String sSql = "";
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			//����ɾ������ʱ������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meictj,
					"Pandtjb",id);
		}
		sSql = "delete from Pandtjb  where pandb_id=" + getPandbID();
		con.getDelete(sSql);
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = null;
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
//		rsl = getExtGrid().getDeleteResultSet(getChange());
//		if (rsl == null) {
//			WriteLog.writeErrorLog(ErrorMessage.NullResult 
//					+ "Pandtj.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
//			setMsg(ErrorMessage.NullResult);
//			return;
//		}
//		while (rsl.next()) {
//			id = rsl.getInt("id");
//			sSql = "delete from Pandtjb where id=" + id;
//			flag = con.getDelete(sSql);
//			if (flag == -1) {
//				con.rollBack();
//				con.Close();				
//			}
//		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Pandtj.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getInt("id");
			if (id == 0) {
				sSql = "insert into Pandtjb(id, pandb_id, meicb_id, tij, mid, cunml, pandff, duig, dingc, dic, dingk, dik) values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ","
					+ getMeicbID(con, rsl.getString("meicb_id")) + ","
					+ rsl.getDouble("tij") + ","
					+ rsl.getDouble("mid") + ","
					+ rsl.getDouble("cunml") + ",'"
					+ rsl.getString("pandff") + "',"
					+ rsl.getDouble("duig") + ","
					+ rsl.getDouble("dingc") + ","
					+ rsl.getDouble("dic") + ","
					+ rsl.getDouble("dingk") + ","
					+ rsl.getDouble("dik")
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				//�����޸Ĳ���ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meictj,
						"pandtjb",id+"");
				sSql = "update pandtjb set "
					+ " pandb_id=" + getPandbID() + ","					
					+ " meicb_id=" + getMeicbID(con, rsl.getString("meicb_id")) + ","
					+ " tij=" + rsl.getDouble("tij") + ","
					+ " mid=" + rsl.getDouble("mid") + ","
					+ " cunml=" + rsl.getDouble("cunml") + ","
					+ " pandff='" + rsl.getString("pandff") + "',"
					+ " duig=" + rsl.getDouble("duig") + ","
					+ " dingc=" + rsl.getDouble("dingc") + ","
					+ " dic=" + rsl.getDouble("dic") + ","
					+ " dingk=" + rsl.getDouble("dingk") + ","
					+ " dik=" + rsl.getDouble("dik")
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
	}
	public String getMeicbID(JDBCcon con, String meicMC) {
		String yougbID = "";
		String sql = "select id from meicb where mingc='" + meicMC + "' and meicb.diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			yougbID = rs.getString("id");
		}
		return yougbID;
	}
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if (_AddChick) {
			int xiaosw=4;
			String sql="select zhi from xitxxb where mingc='�̵��ܶȱ���С��λ' and zhuangt=1";
			ResultSetList rl = con.getResultSetList(sql);
			if(rl.next()){
				xiaosw=rl.getInt("zhi");
			}
			String[][] qimrj = MainGlobal.getXitxx_items("�̵�", "'�̵������ݻ�'", String.valueOf( visit.getDiancxxb_id()));
			if (qimrj == null || qimrj[0][1].equals("")) {
				setMsg("ϵͳ������ȱ��[�̵������ݻ�]���ݣ�");
				return;
			}
			sSql = "select 0 as id,a.meicb_id,pandff,cunml,duig,dingc,dic,dingk,dik,b.mid,tij from\n"
				+ "	  (select meicb.xuh,meicb.id,meicb.mingc as meicb_id,nvl('�˹�','') as pandff,p.cunml,p.duig,p.dingc,p.dic,p.dingk,p.dik,p.mid,p.tij from meicb,\n"
				+ "	  (select * from pandtjb where pandb_id in (select id from pandb where bianm='" + getPandbm() + "'))p\n"
				+ "	  where p.meicb_id(+)=meicb.id  and diancxxb_id=" + visit.getDiancxxb_id() + "\n"
				+ "	  ) a,\n"
				+ "	  (select meicb_id,decode(celff,'ģ��',decode(sum(cedjj),0,0,round(sum(yangpzl/" + qimrj[0][1] + "*cedjj)/sum(cedjj),"+xiaosw+")),\n"
				+ "	  round(sum(chentmz-chentpz)/count(cedjj)/" + qimrj[0][1] + ","+xiaosw+")) as mid from pandmdb where pandb_id='" + getPandbID() + "'\n"
				+ "	  group by meicb_id,celff\n" 
				+ "	  ) b\n"
				+ "	where a.id=b.meicb_id(+)\n"
				+ "	order by a.xuh" ;
			ResultSetList rs = con.getResultSetList(sSql);
			
			
			/*if(rs.next()){
				if(rs.getDouble("mid")==0){
					setMsg("ȱ��[�̵��ܶ�]���ݣ�����¼��[ú���ܶ�]���ݣ�");
					return;
				}
			}*/
			//wangzongbing,�޸���ʾ��ʽ,ֻҪ��һ��mid����0,�Ͳ���ʾ
			int i=0;
			while(rs.next()){
				double mid=rs.getDouble("mid");
				if(mid!=0){
					i++;
				}
			}
			
			if(i==0){
				setMsg("ȱ��[�̵��ܶ�]���ݣ�����¼��[ú���ܶ�]���ݣ�");
				return;
			}
			
			
			
			
			rs.close();
		} else {
			sSql = "select p.id,meicb.mingc as meicb_id,pandff,cunml,duig,dingc,dic,dingk,dik,mid,p.tij\n"
				+ " from pandtjb p,pandb,meicb where  p.meicb_id=meicb.id\n" 
				+ " and p.pandb_id=pandb.id and pandb.bianm='" + getPandbm() + "'"
				+ " order by meicb.xuh";
		}
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
		egu.getColumn("meicb_id").setHeader("ú������");
		egu.getColumn("meicb_id").setEditor(null);
		egu.getColumn("cunml").setHeader("��ú�����֣�");
		egu.getColumn("pandff").setHeader("�̵㷽��");
		egu.getColumn("duig").setHeader("�Ѹߣ��ף�");
		egu.getColumn("dingc").setHeader("�������ף�");
		egu.getColumn("dic").setHeader("�׳����ף�");
		egu.getColumn("dingk").setHeader("�������ף�");
		egu.getColumn("dik").setHeader(" �׿����ף�");
		egu.getColumn("mid").setHeader("�ܶȣ���/�������ף�");
		egu.getColumn("mid").setWidth(140);
		egu.getColumn("mid").setEditor(null);
		egu.getColumn("tij").setHeader("����������ף�");
		List list = new ArrayList();
		list.add(new IDropDownBean(1,"�˹�"));
		list.add(new IDropDownBean(2,"����"));
		egu.getColumn("pandff").setEditor(new ComboBox());
		egu.getColumn("pandff").setDefaultValue("�˹�");
		egu.getColumn("pandff").setComboEditor(egu.gridId, new IDropDownModel(list));
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
		if (rsl.getRows() == 0) {
			gbt = new GridButton("����","function(){document.getElementById('AddButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Insert);
			egu.addTbarBtn(gbt);
		}
		GridButton dbt = new GridButton("ɾ��",GridButton.ButtonType_SaveAll,"gridDiv",egu.gridColumns,"DeleteButton");
//		gbt = new GridButton("ɾ��","function(){document.getElementById('DeleteButton').click();}");
		dbt.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(dbt);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		StringBuffer script = new StringBuffer(); 
/**
 * huochaoyuan
 * �޸Ĵ�ú�����㷽ʽ��ֱ��������������ɼ������ú����
 */
		script.append("\nvar tmpIndex = PandDropDown.getValue();\n");
		script.append("PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n");
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("if (e.field == 'DUIG' || e.field == 'DINGC' || e.field == 'DIC' || e.field == 'DINGK' || e.field == 'DIK'||e.field=='TIJ') {\n")
				.append("\tvar record = gridDiv_ds.getAt(e.row);\n")
				.append("\tif(record.get('TIJ')>0){\n")
			    .append("\tvar mid = eval(record.get('MID')||0);\n")
				.append("\tvar tij = eval(record.get('TIJ')||0);\n")
				.append("\tif (mid > 0) {\n")
				.append("\tvar cunml = Math.round( mid * tij * Math.pow(10,4)) / Math.pow(10,4);\n")
				.append("\trecord.set('CUNML',cunml);\n")
				.append("\t}\n")
				.append("\t}\n")
				.append( "\tif (record.get('DUIG')>0 && ").append("record.get('DINGC')>0 && ").append("record.get('DIC')>0 && ")
				.append("record.get('DINGK')>0 && ").append("record.get('DIK')>0) {\n")
				.append("\t\t var duig = eval(record.get('DUIG')||0);\n")
				.append("\t\t var dingc = eval(record.get('DINGC')||0);\n")
				.append("\t\t var dic = eval(record.get('DIC')||0);\n")
				.append("\t\t var dingk = eval(record.get('DINGK')||0);\n")
				.append("\t\t var dik = eval(record.get('DIK')||0);\n")
				.append("\t\t var mid = eval(record.get('MID')||0);\n")
				.append("\t\tvar tij = Math.round((duig / 6 * ((2 * dic + dingc) * dik + (2 * dingc + dic) * dingk)) * Math.pow(10,2)) / Math.pow(10,2);\n")
				.append("\t\trecord.set('TIJ',tij);\n")
				.append("\t\tif (mid > 0) {\n")
				.append("\t\t\tvar cunml = Math.round( mid * tij * Math.pow(10,4)) / Math.pow(10,4);\n")
				.append("\t\t\trecord.set('CUNML',cunml);\n")
				.append("\t\t}\n")
				.append("\t}\n")
				.append( "}\n")
				.append( "});");
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
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		getSelectData();
		_AddChick = false;
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