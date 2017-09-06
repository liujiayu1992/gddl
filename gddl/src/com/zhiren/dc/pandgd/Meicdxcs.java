package com.zhiren.dc.pandgd;

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
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author lichenji
 * ʱ�䣺2010-8-6 
 * �������޸������ʹ֮�����Զ�����ǰ��ĳ���߼������ֵ��
 *
 */
/*
 * �޸��ˣ�ww
 * ʱ�䣺2010-09-17
 * �޸����ݣ� 1�����ʱ���ܶ�ȥ��λС��
 *          2���������=0,ֱ��¼�������������������ܶȼ�������
 */
/*
 * �޸��ˣ�songy
 * ʱ�䣺2011-5-31
 * �޸����ݣ� 1�� ˢ��ʱƽ���ܶȵĹ�ʽ��Ϊ�뾻�ؼ�Ȩ
 */
 /*
  * �޸��ˣ�songy
  * ʱ�䣺2011-6-10
  * �޸����ݣ� ��ú����С��λͨ��������xitxxb������
  */
public class Meicdxcs extends BasePage implements PageValidateListener{
//	�����û���ʾ
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	//�����̵������ݻ���ֵ
	private double zhi = -1;
	public double getZhi() {
		if(this.zhi == -1){
			setZhi();
		}
			return zhi;
	}
	public void setZhi() {
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '�̵������ݻ�' and diancxxb_id ="+v.getDiancxxb_id());
		if(rsl.next()){
			this.zhi=rsl.getDouble("zhi");
		}else{
			setMsg("���������̵������ݻ���ֵ");
		}
		
		rsl.close();
		con.Close();
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
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}else if(v.isFencb()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ "order by p.bianm desc";
			JDBCcon cn=new JDBCcon();
			ResultSetList rs=cn.getResultSetList(sql);
			if(rs.getRows()==0){
				v.setProSelectionModel10(new IDropDownModel(sql,"������̵����"));
			}else{
		    v.setProSelectionModel10(new IDropDownModel(sql));
			}
			rs.close();
			cn.Close();
		}
	    return v.getProSelectionModel10();
	}
	public void setPandValue(IDropDownBean value) {
		((Visit)getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		Visit v = (Visit) getPage().getVisit();
		String pandbm = "";
		//�ж�ҳ���Ƿ����̵��룬���û�еĻ��������ݿ��ж�ȡ
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pand_gd pd,diancxxb d where pd.diancxxb_id=d.id and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ") order by bianm desc";
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
	
	
	//���ò�������������
	private IPropertySelectionModel _celModel;
	public void setCelModel(IPropertySelectionModel  value){
		
		_celModel = value;
	}
	public IPropertySelectionModel getCelModel(){
		if(_celModel == null){
			List list = new ArrayList();
		
			list.add(new IDropDownBean(1,"ģ��"));
			list.add(new IDropDownBean(2,"��Ͱ"));
			_celModel = new IDropDownModel(list);
		}
		return _celModel;
	}
	
	public void setCelValue(IDropDownBean value) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setDropDownBean6(value);
		
	}
	public IDropDownBean getCelValue() {
		Visit visit = (Visit) getPage().getVisit();
		if(visit.getDropDownBean6()== null){
			setCelValue((IDropDownBean)getCelModel().getOption(0));
		}
		return visit.getDropDownBean6();
	}
	//ˢ�°�ť
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
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} 
		else if (_AddChick) {
			getSelectData();
		}
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		String celffid = getCelValue().getValue();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("û����Ҫ����ļ�¼��");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicdxcsb.Save �� getExtGrid().getDeleteResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//����ɾ�������������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Meicmd,
					"meicdxcsb",id+"");
			sSql = "delete from meicdxcsb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "meicdxcsb.Save �� getExtGrid().getModifyResultSet(getChange()) ����û����ȷ���ؼ�¼����");
			setMsg(ErrorMessage.NullResult);
			return;
		}
			while (rsl.next()) {
				id = rsl.getLong("id");
				if (id == 0) {
					sSql = "insert into meicdxcsb(id, pand_gd_id, meicb_id, shangk, xiak, gao, dingc, jic, tij,mid,shul) values(getNewId(" + visit.getDiancxxb_id() + "),"
						+ getPandbID() + ","
						+ getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) + ",'"
						+ rsl.getDouble("shangk") + "','"
						+ rsl.getDouble("xiak") + "',"
						+ rsl.getDouble("gao") + ","
						+ rsl.getDouble("dingc") + ","
						+ rsl.getDouble("jic") + ","
						+ rsl.getDouble("tij") + ","
						+ rsl.getDouble("mid") + ","
						+ rsl.getDouble("shul")
						+ " )";
					flag = con.getInsert(sSql);
					if (flag == -1) {
						setMsg("����ʧ��!");
						con.rollBack();
						con.Close();
						return;
					}
				} else {
				//�����޸Ĳ���ʱ�����־
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Meicmd,
						"meicdxcsb",id+"");
					sSql = "update meicdxcsb set "
						+ " pand_gd_id=" + getPandbID() + ","					
						+ " meicb_id=" + getExtGrid().getColumn("mingc").combo.getBeanId(rsl.getString("mingc")) + ","
						+ " shangk='" + rsl.getDouble("shangk") + "',"
						+ " xiak='" + rsl.getDouble("xiak") + "',"
						+ " gao=" + rsl.getDouble("gao") + ","
						+ " dingc=" + rsl.getDouble("dingc")+","
						+ " jic=" + rsl.getDouble("jic") + ","
						+ " tij=" + rsl.getDouble("tij") + ","
						+ " mid=" + rsl.getDouble("mid") + ","
						+ " shul=" + rsl.getDouble("shul")
						+ "  where id=" + id;
					flag = con.getUpdate(sSql);
					if (flag == -1) {
						setMsg("����ʧ��");
						con.rollBack();
						con.Close();
						return;
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
	
	// ����зֳ����ݱ���õ糧id����ֱ�ӵ�
	private long getDiancid(JDBCcon con) {
		Visit visit = (Visit) getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		ResultSetList rsl = null;
		String sql = "select diancxxb_id from PAND_GD where bianm = '" + getPandbm() + "'";
		rsl = con.getResultSetList(sql);
		if (visit.isFencb()) {
			if (rsl.next()) {
				diancxxb_id = rsl.getLong("diancxxb_id");
			}
		}
		
		rsl.close();
		return diancxxb_id;
	}
	
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		
		int panmmdxsw = 3;
		panmmdxsw = Integer.parseInt(MainGlobal.getXitxx_item("�̵�", "��ú�ܶ�С��λ", String.valueOf(visit.getDiancxxb_id()), "3"));
		
		int panmsl = 2;
		panmsl = Integer.parseInt(MainGlobal.getXitxx_item("�̵�", "��ú����С��λ", String.valueOf(visit.getDiancxxb_id()), "2"));
		
		JDBCcon con = new JDBCcon();
		if(_AddChick){
//			sSql=
//				"select b.id,mb.mingc,\n" +
//				"       0.00 shangk,\n" + 
//				"       0.00 xiak,\n" + 
//				"       0.00 gao,\n" + 
//				"       0.00 dingc,\n" + 
//				"       0.00 jic,\n" + 
//				"       0.00 tij,\n" + 
//				"       m.mid,\n" + 
//				"       0.00 shul,\n" + 
//				"'<a href = \""+MainGlobal.getHomeContext(this)+"/app?service=page/PandImageReport&&id='||b.id||'&&mk=pandReport\" target=\"_blank\">�鿴</a>' as panmt\n"+" "+
//				"  from MEICDXCSB b,\n" + 
//				"       (select round(sum(m.jingz * m.mid) / sum(m.jingz),3) as mid, m.meicb_id\n" + 
//				"          from MEICDJMDCDB m, (select meicb_id from MEICDXCSB) b,pand_gd pd\n" + 
//				"         where m.meicb_id = b.meicb_id(+)\n" + 
//				"			and pd.bianm='"+getPandbm()+"'\n" +
//				"           and pd.id=m.pand_gd_id\n" +
//				"         group by m.meicb_id\n" + 
//				"         order by m.meicb_id) m,\n" + 
//				"       meicb mb,pand_gd pg\n" + 
//				" where b.meicb_id(+) = m.meicb_id\n" + 
//				"   and mb.id = m.meicb_id and pg.bianm='"+getPandbm()+"' order by mb.mingc";
			con.getDelete(" DELETE FROM MEICDXCSB WHERE pand_gd_id IN (SELECT ID FROM pand_gd WHERE bianm='" + getPandbm() + "' and diancxxb_id="+visit.getDiancxxb_id()+")");
			sSql =
			"SELECT 0 id,\n" +
			"       mc.mingc,\n" + 
			"    0 shangk,\n" + 
			"    0 xiak,\n" + 
			"    0 gao,\n" + 
			"    0 dingc,\n" + 
			"    0 jic,\n" + 
			"    0 AS tij,\n" + 
			"   (SELECT decode(sum(jingz),0,0,round(SUM(jingz*mid)/SUM(jingz), "+panmmdxsw+")) FROM meicdjmdcdb\n" + 
			"   WHERE meicb_id=mc.id AND pand_gd_id=p.id\n" + 
			"   ) mid,\n" + 
			"    0 shul,\n" + 
			"'<a href = \""+MainGlobal.getHomeContext(this)+"/app?service=page/PandImageReport&&id=0&&mk=pandReport\" target=\"_blank\">�鿴</a>' as panmt\n"+" "+
			"    FROM pand_gd p,MEICDJMDCDB m,meicb mc,diancxxb d  WHERE\n" + 
			"p.id=m.pand_gd_id and p.diancxxb_id = d.id \n" + 
			"AND m.meicb_id=mc.id\n" + 
			"AND p.bianm='"+getPandbm()+"'\n" + 
			"AND ( p.DIANCXXB_ID = " + getDiancid(con) + " OR d.fuid = " + getDiancid(con) + ")"+
			"GROUP BY mc.mingc,mc.id,p.id\n" + 
			"ORDER BY  mc.mingc";


		}else{
		//String celffid = getCelValue().getValue();//�õ�ҳ��ѡ��Ĳ��������������ģ�ⷨ��ִ��IF�е���䣬����ǳ�Ͱ����ִ��ELSE�е����
	  
//		������ѯʱ���ܶ�
//		String where ="";
//		if(visit.isDCUser()){
//			where= "and b.diancxxb_id = "+visit.getDiancxxb_id()+" \n";
//		}
			sSql = 
				"select p.id, m.mingc, p.shangk, p.xiak, p.gao, p.dingc, p.jic, p.tij,ROUND(decode(nvl(SUM(MD.Jingz),0),0,0,SUM(MD.MID*MD.Jingz)/SUM(MD.Jingz)),"+panmmdxsw+") mid,p.shul,\n" +
				"'<a href = \""+MainGlobal.getHomeContext(this)+"/app?service=page/PandImageReport&&id='||p.id||'&&mk=pandReport\" target=\"_blank\">�鿴</a>' as panmt\n"+" "+
				"  from meicdxcsb p, meicb m, pand_gd b,meicdjmdcdb md,diancxxb d\n" +" "+ 
				" where p.pand_gd_id = b.id and b.diancxxb_id = d.id \n" +
				"   and m.id = p.meicb_id and b.bianm='"+getPandbm()+"'\n" +
				"	and md.pand_gd_id = b.id\n" +
				"   and md.meicb_id=p.meicb_id\n" +
				"   AND ( B.DIANCXXB_ID = " + getDiancid(con) + " OR d.fuid = " + getDiancid(con) + ")"+
				"   \n group by p.id,m.mingc,p.shangk,p.xiak,p.gao,p.dingc,p.jic,p.tij,p.shul\n"+
				" order by mingc";
		}
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		boolean cel = false;
		ExtGridUtil egu = getEgu(cel,rsl);
//		 ����ú��������
		ComboBox c1 = new ComboBox();
		egu.getColumn("mingc").setEditor(c1);
		c1.setEditable(true);
//		String Sql = "select id ,mingc from meicb where diancxxb_id=(select diancxxb_id from pand_gd where bianm='" + getPandbm() + "')";
		String Sql = "select id, mingc from meicb where diancxxb_id = " + getDiancid(con);
		egu.getColumn("mingc").setComboEditor(egu.gridId,new IDropDownModel(Sql));
//		���ù������̵����������
		
		egu.addTbarText("�̵���룺");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
//		���ò�������������
//		egu.addTbarText("��������");
//		ComboBox cobCel = new ComboBox();
//		cobCel.setWidth(100);
//		cobCel.setTransform("CelDropDown");
//		cobCel.setId("CelDropDown");
//		cobCel.setLazyRender(true);
//		egu.addToolbarItem(cobCel.getScript());
//		egu.addTbarText("-");
		GridButton gbt = new GridButton("ˢ��","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		if (rsl.getRows() == 0) {
			gbt = new GridButton("���","function(){document.getElementById('AddButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Insert);
			egu.addTbarBtn(gbt);
		}

		//egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton gbphoto = new GridButton("ͼƬ����","function (){var rec = gridDiv_sm.getSelected();if(!rec){"+MainGlobal.getExtMessageBox("��ѡ��ú���鿴��Ӧ���̵�ͼƬ", false)+"\n return;}"+MainGlobal.getOpenWinScript("PandImageUpLoad&mk=pandReport&id='+rec.get(\"ID\")+'")+ "}");
		gbphoto.setIcon(SysConstant.Btn_Icon_Show);
		egu.addTbarBtn(gbphoto);

		String moni ="gridDiv_grid.on('afteredit',function(e){\n" +
		"var record = gridDiv_ds.getAt(e.row);\n" + 
		"var tij = eval(record.get('TIJ')||0);\n" + 
		"var mid = eval(record.get('MID')||0);\n" + 
		"var gao = eval(record.get('GAO')||0);\n" + 
		"var jic = eval(record.get('JIC')||0);\n" + 
		"var dingc = eval(record.get('DINGC')||0);\n" + 
		"var xiak = eval(record.get('XIAK')||0);\n" + 
		"var shangk = eval(record.get('SHANGK')||0);\n" + 
		"if(e.field=='TIJ'||e.field=='MID'||e.field=='GAO'||e.field=='JIC'||e.field=='DINGC'||e.field=='CIAK'||e.field=='SHANGK'){\n" + 
		//"var cedjj = eval(record.get('CEDJJ')||0);\n" + 
		//"var zhi = record.get('ZHI');\n" + 
	/////	/////////////////////////////////////////////////////////
		"var shul = Round_new((gao/6*((2*jic+dingc)*xiak+(2*dingc+jic)*shangk))*mid,"+panmsl+");\n" + 
		//2010-09-17 ww �������=0,ֱ��¼�������������������ܶȼ�������
		"if (eval(shul||0)==0) { \n" +
		"	shul=Round_new(Math.round(tij*mid * Math.pow(10, 2))/Math.pow(10, 2),"+panmsl+")\n" +
		"}\n" +
		"//shul=shul.toFixed(5);\n" + 
		"record.set('SHUL',shul);\n" + 
		"}\n" + 
		"if(e.field=='GAO'||e.field=='JIC'||e.field=='DINGC'||e.field=='CIAK'||e.field=='SHANGK'){\n"+
		"var tij=gao/6*((2*jic+dingc)*xiak+(2*dingc+jic)*shangk);\n"+
		"record.set('TIJ',tij);\n"+
		"}\n"+
		"});";
	
	   egu.addOtherScript(moni);

		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
		setExtGrid(egu);
		con.Close();
	}
	public ExtGridUtil getEgu(boolean x,ResultSetList rsl){
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);

			egu.setGridType(ExtGridUtil.Gridstyle_Edit);
			egu.setWidth(Locale.Grid_DefaultWidth);
			egu.setHeight("bodyHeight");
			egu.getColumn("id").setHidden(true);
			egu.getColumn("mingc").setHeader("ú��");
			egu.getColumn("shangk").setHeader("�Ͽ�b1");
            egu.getColumn("shangk").setHidden(true);
			((NumberField) egu.getColumn("shangk").editor).setDecimalPrecision(2);
			
			egu.getColumn("xiak").setHeader("�¿�b");
            egu.getColumn("xiak").setHidden(true);
			((NumberField) egu.getColumn("xiak").editor).setDecimalPrecision(2);
			
			egu.getColumn("gao").setHeader("��h");
            egu.getColumn("gao").setHidden(true);
			((NumberField) egu.getColumn("gao").editor).setDecimalPrecision(2);
			
			egu.getColumn("dingc").setHeader("����a1");
            egu.getColumn("dingc").setHidden(true);
			((NumberField) egu.getColumn("dingc").editor).setDecimalPrecision(2);
			
			egu.getColumn("jic").setHeader("����a");
            egu.getColumn("jic").setHidden(true);
			((NumberField) egu.getColumn("jic").editor).setDecimalPrecision(2);
			
			egu.getColumn("tij").setHeader("���(m3)");
			((NumberField) egu.getColumn("tij").editor).setDecimalPrecision(2);
			
			egu.getColumn("mid").setHeader("�ܶ�(t/m3)");
			egu.getColumn("mid").setEditor(null);
			egu.getColumn("shul").setHeader("����(t)");
//			egu.getColumn("shul").setEditor(null);
			egu.getColumn("panmt").setHeader("��úͼ");
			egu.getColumn("panmt").setWidth(80);
			egu.getColumn("panmt").update = false;
			egu.getColumn("panmt").setEditor(null);
		return egu;
	}
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		//�ڽ���ҳ��֮ǰ���жϣ������ϵͳ��Ϣ����û�иó����̵������ݻ���������ʾ�û�����
//		JDBCcon con = new JDBCcon();
//		ResultSetList rsl = con.getResultSetList("select * from xitxxb where mingc ='�̵������ݻ�' and diancxxb_id ="+visit.getDiancxxb_id());
//		if(!rsl.next()){
//			setMsg("���������̵������ݻ�����");
//			rsl.close();
//			con.Close();
//			//return;
//		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
			setCelModel(null);
			setCelValue(null);
			setZhi();
		}
		init();
//		rsl.close();
//		con.Close();
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
