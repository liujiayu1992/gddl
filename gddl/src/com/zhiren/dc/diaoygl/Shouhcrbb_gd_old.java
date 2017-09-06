package com.zhiren.dc.diaoygl;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ���ߣ����ܱ�
 * ʱ�䣺2012-11-5 1
 * ��������ͬ�糧����Ա��ʱ�õ��պĴ��ձ�����
 *  * ���У�
 * 
 */

public abstract class Shouhcrbb_gd_old extends BasePage {
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	������
	private String riq;
	public String getRiq() {
		return riq;
	}
	public void setRiq(String riq) {
		this.riq = riq;
	}
//	ҳ��仯��¼
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	����������
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer sql = new StringBuffer("begin \n");
		while (mdrsl.next()) {
			String mokmc="";
			if(!(mokmc==null)&&!mokmc.equals("")){
				String id = mdrsl.getString("id");
				//����ʱ������־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),SysConstant.RizOpType_UP,mokmc,"Shouhcrbb",id);
			}
			sql.append("update Shouhcrbb set ");
			for (int i = 1; i < mdrsl.getColumnCount(); i++) {
				sql.append(mdrsl.getColumnNames()[i]).append(" = ");
				sql.append(getExtGrid().getValueSql(getExtGrid().getColumn(mdrsl.getColumnNames()[i]),mdrsl.getString(i))).append(",");
			}
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
			
//			�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
			if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ�ʵʱ���¿��", "0", "��").equals("��")){
				String kuc_sql="select kuc from shouhcrbb where diancxxb_id="+getTreeid()+" and riq = "+DateUtil.FormatOracleDate(this.getRiq());
				ResultSetList kuc_rsl=con.getResultSetList(kuc_sql);
				if(kuc_rsl.next()){
					double kuccha = CustomMaths.sub(mdrsl.getDouble("kuc"),kuc_rsl.getDouble("KUC"));
	//				���µ�ǰ�����Ժ�����п��
						sql.append("update shouhcrbb set ")
						.append("kuc = kuc + ").append(kuccha)
						.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
						.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					���µ�ǰ�����Ժ�����пɵ����
						sql.append("update shouhcrbb set ")
						.append("kedkc = kuc -bukdml ")
						.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
						.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
					}
				kuc_rsl.close();
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if(flag==-1){
			setMsg( "�պĴ�����Ϣ����ʧ��");
		}
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
	
	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
	}

	private void CreateData() {
//		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = Long.parseLong(getTreeid());
		JDBCcon con = new JDBCcon();
//		����ʱ�Զ��������պĴ�ͷֿ�����
		AutoCreateDaily_Report_gd RP=new AutoCreateDaily_Report_gd();
		String rbb=RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String fcb=RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		String Smsg="";
		if(rbb.length()>0){
			Smsg+=rbb+"<br>";
		}
		if(fcb.length()>0){
			Smsg+=fcb+"<br>";
		}
		if(Smsg.length()>0){
			setMsg(Smsg);
		}
		con.Close();
	}
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	private void DelData() {
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sb.append("begin \n");
//		�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ�ʵʱ���¿��", "0", "��").equals("��")){
			String kuc_sql="select jingz+CHANGWML - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk kucca from shouhcrbb where diancxxb_id="+getTreeid()+" and riq = "+DateUtil.FormatOracleDate(this.getRiq());
			ResultSetList kuc_rsl=con.getResultSetList(kuc_sql);
				if(kuc_rsl.next()){
//					���µ�ǰ�����Ժ�����п��
					sb.append("update shouhcrbb set ")
					.append("kuc = kuc - ").append(kuc_rsl.getDouble("KUCCA"))
					.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
					.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					���µ�ǰ�����Ժ�����пɵ����
					sb.append("update shouhcrbb set ")
					.append("kedkc = kuc -bukdml ")
					.append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
					.append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
				}
			kuc_rsl.close();
		}
		sb.append("delete from shouhcrbb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
		sb.append("end;");

		con.getUpdate(sb.toString());
		con.Close();
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
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		} 
		getSelectData();
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getRiq());
		String diancxxb_id = getTreeid();
		StringBuffer sb = new StringBuffer();
		sb.append("select s.id,s.fadl, s.jingz,s.jingz as dangrgm, s.biaoz, s.yuns, s.yingd, s.kuid, s.fady, \n")
		.append("s.gongry, s.qity,s.fady+s.gongry+s.qity+s.cuns+s.feiscy as haoyqkdr,s.feiscy, " +
				"s.cuns, s.tiaozl, s.shuifctz, s.panyk,s.changwml, s.kuc,s.bukdml,s.kedkc \n")
		.append("from shouhcrbb s where diancxxb_id =")
		.append(diancxxb_id).append(" and riq=").append(CurDate);
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		egu.setWidth("bodyWidth");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("dangrgm").setHidden(true);
		egu.getColumn("haoyqkdr").setHidden(true);
		egu.getColumn("fadl").setHeader(Locale.fadl);
		egu.getColumn("fadl").setWidth(60);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);

		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(60);
	
		egu.getColumn("yingd").setHeader(Locale.yingd_fahb);
		egu.getColumn("yingd").setWidth(60);
	
		egu.getColumn("kuid").setHeader(Locale.kuid_fahb);
		egu.getColumn("kuid").setWidth(60);
		
		egu.getColumn("fady").setHeader(Locale.fady);
		egu.getColumn("fady").setWidth(60);
		egu.getColumn("gongry").setHeader(Locale.gongry);
		egu.getColumn("gongry").setWidth(60);
		egu.getColumn("qity").setHeader(Locale.qity);
		egu.getColumn("qity").setWidth(60);
		egu.getColumn("feiscy").setHeader(Locale.feiscy);
		egu.getColumn("feiscy").setWidth(60);
		egu.getColumn("cuns").setHeader(Locale.cuns);
		egu.getColumn("cuns").setWidth(60);
		egu.getColumn("tiaozl").setHeader(Locale.tiaozl);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("shuifctz").setHeader(Locale.shuifctz);
		egu.getColumn("shuifctz").setWidth(80);
		egu.getColumn("panyk").setHeader(Locale.panyk);
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("kuc").setHeader(Locale.kuc);
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("bukdml").setHeader("���ɵ�ú��");
		egu.getColumn("bukdml").setWidth(80);
		egu.getColumn("changwml").setHeader("����ú����ú��");
		egu.getColumn("changwml").setWidth(100);
		egu.getColumn("kedkc").setHeader("�ɵ����");
		egu.getColumn("kedkc").setWidth(60);
		egu.getColumn("kedkc").setHidden(true);
		//egu.getColumn("kedkc").setEditor(null);
//		�жϵ�ǰϵͳ�����Ƿ�Ϊ����1����4�ա����Ϊ����1����4�գ���ô��ѡ�������Ƿ�Ϊ������ĩ�������������������Բ������
		String sql= "SELECT 1 FROM (SELECT FIRST_DAY(SYSDATE) FD,\n" + 
			"               TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)), 'yyyy-mm-dd') LD\n" + 
			"          FROM DUAL) SDAY,\n" + 
			"       (SELECT TO_CHAR(SYSDATE, 'yyyy-mm-dd') FD,\n" + 
			"               TO_CHAR("+CurDate+", 'yyyy-mm-dd') LD\n" + 
			"          FROM DUAL) UDAY\n" + 
			" WHERE SDAY.LD = UDAY.LD\n" +
			"	AND SYSDATE BETWEEN SDAY.FD AND SDAY.FD+3";
//		������ܲ�ѯ��������ô���𣬵�������ˮ�ֲ��������ӯ����Ϣ��������ʾ�Ҳ��ɱ༭
		if(!con.getHasIt(sql)&&MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���������Ϣ�ɱ༭", "0", "��").equals("��")){
			egu.getColumn("cuns").setEditor(null);
			egu.getColumn("cuns").setHidden(true);
			egu.getColumn("tiaozl").setEditor(null);
			egu.getColumn("tiaozl").setHidden(true);
			egu.getColumn("shuifctz").setEditor(null);
			egu.getColumn("shuifctz").setHidden(true);
			egu.getColumn("panyk").setEditor(null);
			egu.getColumn("panyk").setHidden(true);
		}
		
		
			egu.getColumn("biaoz").setEditor(null);
			egu.getColumn("jingz").setEditor(null);
			egu.getColumn("yuns").setEditor(null);
			egu.getColumn("yingd").setEditor(null);
			egu.getColumn("kuid").setEditor(null);
			egu.getColumn("kuc").setEditor(null);
		
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.Binding("RIQ","");
		df.setValue(getRiq());
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");// ���÷ָ���
		// ������
		egu.addTbarText("��λ:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
//		ˢ�°�ť
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'������,���Ժ�'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("ˢ��",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		���ɰ�ť
		if(visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�ֳ����ܳ���ʾ���ɰ�ť", diancxxb_id, "��").equals("��")){
			
		}else{
			GridButton gbc = new GridButton("����",getBtnHandlerScript("CreateButton"));
			gbc.setIcon(SysConstant.Btn_Icon_Create);
			egu.addTbarBtn(gbc);
//			ɾ����ť
			GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
			gbd.setIcon(SysConstant.Btn_Icon_Delete);
			egu.addTbarBtn(gbd);
//			��д���水ť
			String Script="function (){\n"+
				"Ext.MessageBox.confirm('��ʾ��Ϣ','���á������Ϣ�Ƿ���ȷ��',function(btn){if(btn == 'yes'){\n"+
				"var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n"+
				"for(i = 0; i< Mrcd.length; i++){\n"+
				"if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n"+
				"if(Mrcd[i].get('FADL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶη����� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('FADL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('FADL')!=0 && Mrcd[i].get('FADL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�DANGRGM ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� DANGRGM �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� DANGRGM ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶη����� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶι����� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ������� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶδ��� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ���� �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶε����� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ˮ�ֲ���� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ˮ�ֲ���� �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ˮ�ֲ���� ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ���ӯ�� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ��ӯ�� �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��ӯ�� ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('CHANGWML') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶγ����ú ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('CHANGWML') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� �����ú �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('CHANGWML')!=0 && Mrcd[i].get('CHANGWML') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �����ú ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('BUKDML') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶβ��ɵ�ú�� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('BUKDML') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ���ɵ�ú�� �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('BUKDML')!=0 && Mrcd[i].get('BUKDML') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���ɵ�ú�� ����Ϊ��');return;\n"+
				"}if(Mrcd[i].get('KEDKC') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶοɵ���� ������Сֵ -100000000');return;\n"+
				"}if( Mrcd[i].get('KEDKC') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� �ɵ���� �������ֵ 100000000000');return;\n"+
				"}if(Mrcd[i].get('KEDKC')!=0 && Mrcd[i].get('KEDKC') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� �ɵ���� ����Ϊ��');return;\n"+
				"}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n"+
				"+ '<FADL update=\"true\">' + Mrcd[i].get('FADL')+ '</FADL>'\n"+
				"+ '<JINGZ update=\"true\">' + Mrcd[i].get('JINGZ')+ '</JINGZ>'\n"+
				"+ '<DANGRGM update=\"true\">' + Mrcd[i].get('DANGRGM')+ '</DANGRGM>'\n"+
				"+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n"+
				"+ '<YUNS update=\"true\">' + Mrcd[i].get('YUNS')+ '</YUNS>'\n"+
				"+ '<YINGD update=\"true\">' + Mrcd[i].get('YINGD')+ '</YINGD>'\n"+
				"+ '<KUID update=\"true\">' + Mrcd[i].get('KUID')+ '</KUID>'\n"+
				"+ '<FADY update=\"true\">' + Mrcd[i].get('FADY')+ '</FADY>'\n"+
				"+ '<GONGRY update=\"true\">' + Mrcd[i].get('GONGRY')+ '</GONGRY>'\n"+
				"+ '<QITY update=\"true\">' + Mrcd[i].get('QITY')+ '</QITY>'\n"+
				"+ '<HAOYQKDR update=\"true\">' + Mrcd[i].get('HAOYQKDR')+ '</HAOYQKDR>'\n"+
				"+ '<FEISCY update=\"true\">' + Mrcd[i].get('FEISCY')+ '</FEISCY>'\n"+
				"+ '<CUNS update=\"true\">' + Mrcd[i].get('CUNS')+ '</CUNS>'\n"+
				"+ '<TIAOZL update=\"true\">' + Mrcd[i].get('TIAOZL')+ '</TIAOZL>'\n"+
				"+ '<SHUIFCTZ update=\"true\">' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'\n"+
				"+ '<PANYK update=\"true\">' + Mrcd[i].get('PANYK')+ '</PANYK>'\n"+
				"+ '<KUC update=\"true\">' + Mrcd[i].get('KUC')+ '</KUC>'\n"+
				"+ '<CHANGWML update=\"true\">' + Mrcd[i].get('CHANGWML')+ '</CHANGWML>'\n"+
				"+ '<BUKDML update=\"true\">' + Mrcd[i].get('BUKDML')+ '</BUKDML>'\n"+
				"+ '<KEDKC update=\"true\">' + Mrcd[i].get('KEDKC')+ '</KEDKC>'\n"+
				" + '</result>' ; }\n"+
				"if(gridDiv_history=='' && gridDivsave_history==''){\n"+ 
				"Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n"+
				"}else{\n"+
				"var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n"+
				"document.getElementById('SaveButton').click();\n"+
				"Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"+
				"}\n"+
				"};});\n"+
				"}";
//			
//			GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//			egu.addTbarBtn(gbs);
			GridButton gbs =new GridButton("����",Script);
			gbs.setIcon(SysConstant.Btn_Icon_Save);
			egu.addTbarBtn(gbs);
		}

//		grid ���㷽��
		egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);\n");
		
		AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
		String msg=DR.ChkRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
		if(msg.length()>0){
			egu.addOtherScript("Ext.MessageBox.alert('��ʾ��Ϣ','"+msg+"�����ݲ�������');\n");
		}
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		��ť��script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = "'+Ext.getDom('RIQ').value+'";
		btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("���������ݽ�ͬʱ����:���պĴ���չ���<br>")
			.append(cnDate).append("���Ѵ����ݣ��Ƿ������");
		}else {
			btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
			setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
			setChangbModels();
			getSelectData();
		}
	}
	
	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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
}
