package com.zhiren.dc.huaygl.huaysh.ruchysh;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.huaygl.Shenhcl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ���ΰ
 * ʱ�䣺2009-09-08
 * ���ݣ�����ҳ��ʱ��ʼ��setTreeid()����������һ�����Ʋ�ͬ����ͬʱ��¼������ݻ���
 */
/*
 *���ߣ���ΰ
 *ʱ�䣺2009-12-28
 *���ݣ���setJincpcModels()��������ӳ����жϣ�����һ�����ƽ���������������ʾ��������ı��
 */
/*
 *���ߣ����
 *ʱ�䣺2012-06-01
 *���ݣ����������ֶ���ǰ̨���㷽������
 */
/*
 *���ߣ����
 *ʱ�䣺2013-07-01
 *���ݣ�������˽�����ֵǰ������ֵ����
 */
/*
 *���ߣ����
 *ʱ�䣺2013-07-01
 *���ݣ�������˽�����ֵǰ������ֵ����
 */
public class Ruchyejsh extends BasePage implements PageValidateListener {
	private String msg = "";
	
	private String CustomSetKey = "Ruchyejsh";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg("");
		setZhilbids("");
	}
	
	private String zhilbids = "";
	public String getZhilbids(){
		return zhilbids;
	}
	public void setZhilbids(String ids){
		zhilbids= ids;
	}
	
//	����ҳ���ϱ��ʵ��ܺ�
	private String BilTotal;

	public String getBilTotal() {
		return BilTotal;
	}

	public void setBilTotal(String BilTotal) {
		this.BilTotal = BilTotal;
	}
	
	private boolean flag=false;

	// ������
	boolean riqichange = false;

	private String riqi;

	public String getBeginRiq() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setBeginRiq(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	boolean riq2change = false;

	private String riq2;

	public String getEndRiq() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setEndRiq(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	
	public IDropDownBean getJincpcValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean3()==null) {
			if(getJincpcModel().getOptionCount()>0) {
				setJincpcValue((IDropDownBean)getJincpcModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}
	public void setJincpcValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(value);
	}
	
	public IPropertySelectionModel getJincpcModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel3()==null) {
			setJincpcModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	public void setJincpcModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(value);
	}
	
	//ww 2009-12-28 ��ӳ����ж�
	public void setJincpcModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		String Dicid = "";
		Dicid = " and f.diancxxb_id=" + getTreeid()+" order by c.bianm";
		if (hasDCid(null, getTreeid())){
			Dicid = " and f.diancxxb_id in (select id from diancxxb where fuid=" + getTreeid() + ")  order by c.bianm";
		}
		
		String where = "";
		String lx = visit.getString2();
		if(lx.equals("zcy")){
			where = "	and z.huaylbb_id not in (select id from leibb where mingc like '%����%')\n";
		} else if(lx.equals("fcy")){
			where = "	and z.huaylbb_id in (select id from leibb where mingc like '%����%')\n";
		}
		
		StringBuffer sb = new StringBuffer();
		sb.append("select distinct c.zhilb_id,c.bianm from caiyb c,fahb f,zhillsb z\n")
		.append(" where f.zhilb_id = c.zhilb_id and c.zhilb_id = z.zhilb_id and (z.shenhzt = 5 or z.shenhzt = 6) \n")
		.append("and f.daohrq>=").append(DateUtil.FormatOracleDate(getBeginRiq()))
		.append(" and f.daohrq <=").append(DateUtil.FormatOracleDate(getEndRiq()))
		.append(where)
		.append(Dicid);
		List list = new ArrayList();
		list.add(new IDropDownBean(-1,"��ѡ��"));
		setJincpcModel(new IDropDownModel(list,sb));
	}
	
	private boolean hasDCid(JDBCcon con,String id){
		
		String sql=" select * from diancxxb where fuid="+id;
		
		boolean shifgb=false;
		if(con==null){ 
			con=new JDBCcon();
			shifgb=true;
		}
		
		ResultSetList rsl=con.getResultSetList(sql);
		
		boolean  flag=false;
		if(rsl.next()){
			flag=true;
		}
		
		rsl.close();
		
		if(shifgb){
			con.Close();
		}
		return flag;
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
		JDBCcon con = new JDBCcon();
		List fhlist = new ArrayList();
		String zhilbid = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		if (mdrsl.next()) {
			strsql += "update zhillsb set shenhzt=7,shenhryej='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("id") + ";\n";
			
			strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,qgrd,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
			+"values("+mdrsl.getString("id")+",'"+mdrsl.getString("huaybh")+"',"+mdrsl.getString("caiyb_id")+","
			+ DateUtil.FormatOracleDate(mdrsl.getString("huaysj"))+","+mdrsl.getString("qnet_ar") + ","
			+ mdrsl.getString("aar") + "," + mdrsl.getString("ad") + "," + mdrsl.getString("vdaf") + ","
			 + mdrsl.getString("mt") + "," + mdrsl.getString("stad") + "," + mdrsl.getString("aad") + ","
			 + mdrsl.getString("mad") + "," + mdrsl.getString("qbad") + "," + mdrsl.getString("had") + ","
			 + mdrsl.getString("vad") + "," + mdrsl.getString("fcad") + "," + mdrsl.getString("std") + ","
			 + mdrsl.getString("qgrad") + ","+mdrsl.getString("qgrd")+"," + mdrsl.getString("hdaf") + "," + mdrsl.getString("qgrad_daf") + ","
			 + mdrsl.getString("sdaf") + ",";
			if (mdrsl.getString("t1")==null ||mdrsl.getString("t1").equals("")){
				strsql=strsql+"0,";
			}else{
				strsql=strsql+mdrsl.getString("t1")+",";
			}
				
			if (mdrsl.getString("t2")==null ||mdrsl.getString("t2").equals("")){
				strsql=strsql+"0,";
			}else{
				strsql=strsql+mdrsl.getString("t2")+",";
			}
			if (mdrsl.getString("t3")==null ||mdrsl.getString("t3").equals("")){
				strsql=strsql+"0,";
			}else{
				strsql=strsql+mdrsl.getString("t3")+",";
			}
			if (mdrsl.getString("t4")==null ||mdrsl.getString("t4").equals("")){
				strsql=strsql+"0,";
			}else{
				strsql=strsql+mdrsl.getString("t4")+",";
			}
			
			strsql=strsql+"'" + mdrsl.getString("huayy") + "','"
			 	+ mdrsl.getString("lury") + "','" + mdrsl.getString("beiz") + "',1,1);\n";
			zhilbid = mdrsl.getString("ID");
			
		}
		strsql += " end;";
		int flag = con.getUpdate(strsql);
		
		// �����Ƿ�ʹ�ù�������
		boolean isLiuc = false;
		isLiuc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�ʹ�ù�������", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		if (isLiuc) {
			if (flag > 0) {
				Liuc.tij("zhillsb", Long.parseLong(visit.getString11()), visit.getRenyID(), "");
			}
		}
		
		String Sql = "select id from fahb where zhilb_id = " + zhilbid;
		mdrsl = con.getResultSetList(Sql);
		while (mdrsl.next()) {
			String id = mdrsl.getString("id");
			Jilcz.addFahid(fhlist, id);
		}
		mdrsl.close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(),fhlist,false);
		setJincpcValue(null);
		setJincpcModel(null);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set shenhzt=4,shenhryej='"+visit.getRenymc()+"' where zhilb_id =").append(mdrsl.getString("id"))
			.append(";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		setJincpcValue(null);
		setJincpcModel(null);
	}
	
	private void Select(){
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		JDBCcon con = new JDBCcon();
		StringBuffer sqlsb = new StringBuffer("begin");
		String mark = MainGlobal.getXitxx_item("����", "�Ƿ�������ʼ��㻯��ֵ", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		if (mark.equals("��")) {
			while(mdrsl.next()){
				zhilb_ids += "," + mdrsl.getString("id");
				sqlsb.append("\n update zhillsb set shifsy = 1, bil = ").append(mdrsl.getString("bil")).append(" where id = ").append(mdrsl.getString("id")).append(";");
			}
		} else {
			while(mdrsl.next()){
				zhilb_ids += "," + mdrsl.getString("id");
				sqlsb.append("\n update zhillsb set shifsy = 1").append(" where id = ").append(mdrsl.getString("id")).append(";");
			}
		}
		sqlsb.append("\nend;");
		con.getUpdate(sqlsb.toString());
		mdrsl.close();
		con.Close();
		zhilb_ids = zhilb_ids.substring(1);
		setZhilbids(zhilb_ids);
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SelectChick = false;

	public void SelectButton(IRequestCycle cycle) {
		_SelectChick = true;
	}
	
	private boolean _AvgClick = false;
	public void AvgButton(IRequestCycle cycle){
		_AvgClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SelectChick) {
			_SelectChick = false;
			Select();
		}
		if (_AvgClick){
			_AvgClick = false;
			getAvg();
		}

	}
	private boolean Display = false;
	
	private boolean xiansztl = false;

	private boolean xiansztq = false;
	
	private boolean yangplb = false;
	
	private boolean IsShow=false;
	
	private void getSelectData() {
		if("".equals(getZhilbids())){
			getSelect();
		}else{
			getQuer();
		}
	}
	
	
	private void getSelect(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		// �����Ƿ�ʹ�ù�������
		boolean isLiuc = false;
		isLiuc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�ʹ�ù�������", visit
				.getDiancxxb_id()
				+ "", "��"));
		String tj = getJincpcValue().getId() + "";
		String zhuangt = "   and (l.shenhzt = 5 or l.shenhzt = 6)\n";
		if (isLiuc) {
			tj = "(select zhilb_id from zhillsb where id = " + visit.getString11() + ")";
			zhuangt = "";
		}
		
		String sql = "";
		//��ϵͳ��Ϣ���в�������������Ʒ�����ʾ�����Ϊ'��'����ҳ����ʾ,�����Ϊ'��'ҳ������ʾ
		sql = "select zhi from xitxxb where mingc ='���������Ʒ�����ʾ' and zhuangt = 1 and diancxxb_id = "+
			 	visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			if(rsl.getString("zhi").equals("��")){
				yangplb = true;
			}else{
				yangplb = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ�볧������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}

		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ�볧������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '���������ʾ��'";
		rsl = con.getResultSetList(sql);
		while(rsl.next()){
			if(rsl.getString("zhi").equals("��ʾ")){
				Display = true;
			}
		}
		
//		�����������Ƿ�ֻ��ʾ�����ԭʼָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),Ĭ��Ϊȫ����ʾ
		sql = "select zhi from xitxxb where mingc = '�����������Ƿ�ֻ��ʾ�������Ҫָ��'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				IsShow = true;
			} else {
				IsShow = false;
			}
		}
		rsl.close();
		
//		�糧Treeˢ������
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
			try {
				ResultSet rsss=con.getResultSet("select id from diancxxb where fuid="+getTreeid());
				if(rsss.next()){
					str = "and dc.fuid="+ getTreeid() ;
				}else{
					str = "and dc.id = " + getTreeid() ;
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}
		}
		
			sql = "select distinct l.id,\n"
				+ "                l.zhilb_id as zhilid,\n"
				+ "                m.mingc meikdw,\n"
				+ "                cz.mingc faz,\n"
				+ "                p.mingc as pinz,\n"
				+ "                l.huaysj,\n" 
				+"				   l.huaylb,\n"
				+ "                zm.bianm as huaybh,\n"
				+ "                f.ches as ches,\n"
				+ "                f.jingz as shul,\n"
				+ "                l.bil,\n"
				+ "                round_new(l.qnet_ar/4.1816*1000,0)rezk,\n"
				+ "                l.qnet_ar,\n"
				+ "                l.aar,\n"
				+ "                l.ad,\n"
				+ "                l.vdaf,\n"
				+ "                l.mt,\n"
				+ "                l.stad,\n"
				+ "                l.aad,\n"
				+ "                l.mad,\n"
				+ "                round_new(100*(l.mt-l.mad)/(100-l.mad),2) as mf,\n"
				+ "                l.qbad,\n"
				+ "                l.had,\n"
				+ "                l.vad,\n"
				+ "                l.fcad,\n"
				+ "                l.std,\n"
				+ "                l.qgrad,\n"
				+"                 l.qgrd,\n "
				+ "                l.hdaf,\n"
				+ "                l.qgrad_daf,\n"
				+ "                l.sdaf,\n"
				+ "                l.t1,\n"
				+ "                l.t2,\n"
				+ "                l.t3,\n"
				+ "                l.t4,\n"
				+ "                l.huayy,\n"
				+ "                l.lury,\n"
				+ "                l.beiz\n"
				
				+ "  from zhilb z,\n"
				+ "       zhillsb l,\n"
				+ "       caiyb c,\n"
				+ "       (select *\n"
				+ "          from zhuanmb\n"
				+ "         where zhuanmlb_id =\n"
				+ "       (select id\n"
				+ "          from zhuanmlb\n"
				+ "         where jib = (select nvl(max(jib), 0) from zhuanmlb))) zm,\n"
				+ "       (select sum(laimsl) as jingz,sum(f.ches) as ches, meikxxb_id, zhilb_id, pinzb_id,faz_id\n"
				/**
				 * huochaoyuan 2009-02-17
				 * �޸��ϱ�fahb��ȡ�õ�������Ϣ����ǰ��sum(jingz-koud),�޸�Ϊ�����µ�ͳ����ȡֵ(laimsl)
				 */		
				+ "          from fahb f, diancxxb dc\n"
				+ "				where f.diancxxb_id = dc.id \n"
				+ str + "\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
				+ " where f.zhilb_id = z.id(+)\n"
				+ "   and c.zhilb_id = f.zhilb_id\n"
				+ "   and f.meikxxb_id = m.id\n"
				+ "   and f.zhilb_id = l.zhilb_id\n"
				+ "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id=cz.id\n"
				+ "   and zm.zhillsb_id = l.id\n"
				+ zhuangt

/**369
 * huochaoyuan 2009-03-04,�����ϱ�SQL���������ظ������ݲ�����ȫ�������ע����
 */				
				+ "and l.zhilb_id = " + tj
				+ " order by l.zhilb_id";
		rsl = con.getResultSetList(sql);
		//����������ʱ�����Ƿ�ʹ�õ�״̬
		sql = "update zhillsb set shifsy = 0 where zhilb_id = " + getJincpcValue().getId();
		
		if (isLiuc) {
			sql = "update zhillsb set shifsy = 0 where id = " + visit.getString11();
		}
		
		con.getUpdate(sql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		// //���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		// /������ʾ������
		if(!Display){
			egu.getColumn("meikdw").setHidden(true);
			egu.getColumn("faz").setHidden(true);
			egu.getColumn("pinz").setHidden(true);
			egu.getColumn("shul").setHidden(true);
		}
		egu.getColumn("zhilid").setHidden(true);
		egu.getColumn("meikdw").setHeader("ú��λ");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("shul").setHeader("����(��)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("shul").setWidth(70);
		egu.getColumn("bil").setHeader("����");
		egu.getColumn("bil").setHidden(true);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setWidth(80);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("rezk").setHeader("�յ�����λ����<p>Qnet,ar(Kcal/Kg)</p>");
		egu.getColumn("rezk").setEditor(null);
		egu.getColumn("rezk").setUpdate(false);
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/Kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("�յ����ҷ�<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("������ҷ�<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("ȫˮ��<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("���������ȫ��<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("����������ҷ�<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("���������ˮ��<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("mf").setHeader("��ˮ<p>Mf(%)</p>");
		egu.getColumn("mf").setEditor(null);
		egu.getColumn("mf").setHidden(true);
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("qgrd").setHeader("�������λ��ֵ<p>Qgr,d(Mj/kg)</p>");
		egu.getColumn("qgrd").setEditor(null);
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("bil").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("qgrd").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		
		
//		�Ƿ�ֻ��ʾ�������Ҫָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),Ĭ��Ϊȫ����ʾ
		if(IsShow){
			egu.getColumn("aar").setHidden(true);
			egu.getColumn("ad").setHidden(true);
			egu.getColumn("vad").setHidden(true);
			egu.getColumn("fcad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("qgrad_daf").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
			egu.getColumn("t1").setHidden(true);
			egu.getColumn("t2").setHidden(true);
			egu.getColumn("t3").setHidden(true);;
			egu.getColumn("t4").setHidden(true);
		}
		
		
		
		if(xiansztq){
			egu.getColumn("had").setHidden(true);
			egu.getColumn("hdaf").setHidden(true);
		}
		if(xiansztl){
			egu.getColumn("stad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
		}
		//�ж��Ƿ���ʾ�������
		if(!yangplb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.addPaging(25);
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		// Toolbar tb1 = new Toolbar("tbdiv");
		 
		if (!isLiuc) {
			egu.addTbarText("��λ����:");
			ExtTreeUtil etu = new ExtTreeUtil("diancTree",
					ExtTreeUtil.treeWindowType_Dianc,
					((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
			setTree(etu);
			egu.addTbarTreeBtn("diancTree");
			
			egu.addTbarText("-");
			
			egu.addTbarText("��ʼ����:");
			DateField df = new DateField();
			df.setReadOnly(true);
			df.setValue(this.getBeginRiq());
			df.Binding("RIQI", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			df.setId("riqi");
			egu.addToolbarItem(df.getScript());
			
			egu.addTbarText("��ֹ����:");
			DateField df1 = new DateField();
			df1.setReadOnly(true);
			df1.setValue(this.getEndRiq());
			df1.Binding("RIQ2", "Form0");// ��htmlҳ�е�id��,���Զ�ˢ��
			df1.setId("riq2");
			egu.addToolbarItem(df1.getScript());

			egu.addTbarText("-");
			
			egu.addTbarText("�������κ�:");
			ComboBox shij = new ComboBox();
			shij.setTransform("JincpcSelect");
			shij.setWidth(150);
			shij.setListeners("select:function(own,rec,index){Ext.getDom('JincpcSelect').selectedIndex=index;document.forms[0].submit();}");
			egu.addToolbarItem(shij.getScript());
			egu.addTbarText("-");
		}
		
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		String mark = MainGlobal.getXitxx_item("����", "�Ƿ�������ʼ��㻯��ֵ", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		
		if (mark.equals("��")) {
			flag=true;//ȫ�ֲ�������
			egu.getColumn("bil").setHidden(false);
			egu.addOtherScript(
					"\ngridDiv_grid.on('rowdblclick',function(own, irow, e){\n" +
					"    win.show();\n" + 
					"});\n");
			String condition = 
				"var rsl = gridDiv_grid.getSelectionModel().getSelections();\n" +
				"var total = 0;\n" + 
				"for (i = 0; i < rsl.length; i ++) {\n" + 
				"    if (rsl[i].get('BIL') == '' || rsl[i].get('BIL') == null) {\n" +
				"        rsl[i].set('BIL',1);\n" + 
				"    }\n" +
				"   total=addValue(total,rsl[i].get('BIL'));\n" + 
				"}\n" + 
				"document.all.BilTotal.value = eval(total);";
			
			egu.addToolbarButton("ȷ��", GridButton.ButtonType_SubmitSel_condition, "SelectButton", condition);
		} else {
			egu.addToolbarButton("ȷ��", GridButton.ButtonType_SubmitSel, "SelectButton");
		}
		
		egu.addTbarText("-");
		GridButton Create = new GridButton("�鿴ƽ��ֵ", "ShowAvg", SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		
		setExtGrid(egu);
		con.Close();
	}

	/*
	 * author:yuss
	 * time:2012-3-31
	 * describe:��һ������������������ձ��ʼ�Ȩ�������������������ܺͲ�����1ʱ����ʾ�û�;
	 *          �������ñ��ʣ��԰���ƽ��ֵ����;һ������һ������ʱ��Ĭ�ϱ���=1
	 *          xitxxb�����ò��� mingc=���Ƿ�������ʼ��㻯��ֵ��  leib='����'  zhi='��'
	 */
	private void getQuer() {
		System.out.println("BilTotal=="+getBilTotal());
		if((flag&&(getBilTotal().equals("1")||getBilTotal().equals("01")))||!flag){//�����ڣ�һ��������������ֵ�������ܺ�=1��һ������һ������ֵ��δʹ�ñ���
		JDBCcon con = new JDBCcon();
		String sql = "";
		String mark = MainGlobal.getXitxx_item("����", "�Ƿ�������ʼ��㻯��ֵ", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		if (mark.equals("��")) {
			sql = Shenhcl.getErjshpj(con, getZhilbids(), getBilTotal());
		} else {
			sql = Shenhcl.Judgment(con, getZhilbids());
		}
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
//		 ���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("meikdw").setHeader("ú��λ");
		egu.getColumn("meikdw").setEditor(null);
		egu.getColumn("meikdw").setWidth(60);
		egu.getColumn("faz").setHeader("��վ");
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("pinz").setHeader("Ʒ��");
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("ches").setHeader("����");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("shul").setHeader("����(��)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		
		egu.getColumn("rezk").setHeader("�յ�����λ����<p>Qnet,ar(Kcal/Kg)</p>");
		egu.getColumn("rezk").setEditor(null);
		egu.getColumn("rezk").setUpdate(false);
		
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("�յ����ҷ�<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("������ҷ�<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("ȫˮ��<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("���������ȫ��<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("����������ҷ�<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("���������ˮ��<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("qgrd").setHeader("�������λ��ֵ<p>Qgr,d(Mj/kg)</p>");
		egu.getColumn("qgrd").setEditor(null);
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t1").setEditor(null);
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t2").setEditor(null);
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setEditor(null);
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setEditor(null);
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		
		egu.getColumn("beiz").setWidth(80);
		
		if(IsShow){
			egu.getColumn("aar").setHidden(true);
			egu.getColumn("ad").setHidden(true);
			egu.getColumn("vad").setHidden(true);
			egu.getColumn("fcad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("qgrad_daf").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
			egu.getColumn("t1").setHidden(true);
			egu.getColumn("t2").setHidden(true);
			egu.getColumn("t3").setHidden(true);;
			egu.getColumn("t4").setHidden(true);
		}
		if(!Display){
			egu.getColumn("meikdw").setHidden(true);
			egu.getColumn("faz").setHidden(true);
			egu.getColumn("pinz").setHidden(true);
			egu.getColumn("shul").setHidden(true);
		}
		if(xiansztq){
			egu.getColumn("had").setHidden(true);
			egu.getColumn("hdaf").setHidden(true);
		}
		if(xiansztl){
			egu.getColumn("stad").setHidden(true);
			egu.getColumn("std").setHidden(true);
			egu.getColumn("sdaf").setHidden(true);
		}
		
		
		egu.addPaging(25);
		// Toolbar tb1 = new Toolbar("tbdiv");

		egu.addTbarText("-");

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);

		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
				"HuitButton");

		setExtGrid(egu);
		con.Close();
		}else{
			setMsg("�����ܺͲ�����1");
		}
	}
	
	public void getAvg(){
		
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		while(mdrsl.next()){
			zhilb_ids += "," + mdrsl.getString("id");
		}
		mdrsl.close();
		zhilb_ids = zhilb_ids.substring(1);
		String sql = "select avg(z.qnet_ar) qnet_ar,\n" +
			"       avg(z.aar) aar,\n" + 
			"       avg(z.ad) ad,\n" + 
			"       avg(z.vdaf) vdaf,\n" + 
			"       avg(z.mt) mt,\n" + 
			"       avg(z.stad) stad,\n" + 
			"       avg(z.aad) aad,\n" + 
			"       avg(z.mad) mad,\n" + 
			"       avg(z.qbad) qbad,\n" + 
			"       avg(z.had) had,\n" + 
			"       avg(z.vad) vad,\n" + 
			"       avg(z.fcad) fcad,\n" + 
			"       avg(z.std) std,\n" + 
			"      avg( z.qgrad) qgrad,\n" + 
			"       avg(z.hdaf) hdaf,\n" + 
			"       avg(z.qgrad_daf) qgrad_daf,\n" + 
			"       avg(z.sdaf) sdaf\n" + 
			"      from (select max(l.zhilb_id) as id,\n" + 
			"               max(l.huaysj) as huaysj,\n" + 
			"               avg(l.qnet_ar) qnet_ar,\n" + 
			"               avg(l.aar) aar,\n" + 
			"               avg(l.ad) ad,\n" + 
			"               avg(l.vdaf) vdaf,\n" + 
			"               avg(l.mt) mt,\n" + 
			"               avg(l.stad) stad,\n" + 
			"               avg(l.aad) aad,\n" + 
			"               avg(l.mad) mad,\n" + 
			"               avg(l.qbad) qbad,\n" + 
			"               avg(l.had) had,\n" + 
			"               avg(l.vad) vad,\n" + 
			"               avg(l.fcad) fcad,\n" + 
			"               avg(l.std) std,\n" + 
			"               avg(l.qgrad) qgrad,\n" + 
			"               avg(l.hdaf) hdaf,\n" + 
			"               avg(l.qgrad_daf) qgrad_daf,\n" + 
			"               avg(l.sdaf) sdaf \n" + 
			"          from zhillsb l\n" + 
			"         where id in ("+ zhilb_ids +")\n" + 
			"         group by l.zhilb_id, l.huaysj) z,\n" + 
			"       caiyb c,\n" + 
			"       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id\n" + 
			"          from fahb\n" + 
			"         group by pinzb_id, zhilb_id, meikxxb_id) f,\n" + 
			"       meikxxb m,\n" + 
			"       pinzb p\n" + 
			" where f.zhilb_id = c.zhilb_id\n" + 
			"   and f.zhilb_id = z.id\n" + 
			"   and c.zhilb_id = z.id\n" + 
			"   and f.meikxxb_id = m.id\n" + 
			"   and f.pinzb_id = p.id";
		ResultSetList  rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setTableName("zhilb");
		egu.setWidth(1000);
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("aar").setHeader("�յ����ҷ�<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("������ҷ�<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("�����޻һ��ӷ���<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("ȫˮ��<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("stad").setHeader("���������ȫ��<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("����������ҷ�<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("���������ˮ��<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.addPaging(18);
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("����",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(refurbish);

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
		if (treeid.equals("")) {

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
		
		// �����Ƿ�ʹ�ù�������
		boolean isLiuc = false;
		isLiuc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�ʹ�ù�������", visit
				.getDiancxxb_id()
				+ "", "��"));
		
		if (isLiuc) {
			String Tb_name_id = cycle.getRequestContext().getRequest().getParameter("Tb_name_id");
			if(Tb_name_id != null && Tb_name_id.length()>0) {
				String[] T = Tb_name_id.split(",");
				visit.setString11(T[1]);
				if (T[1].endsWith(";")) {
					this.setMsg("һ��ֻ���ύһ�����ݣ�");
					visit.setString11("0");
					this.getSelectData();
					return;
				}
			}
		}
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())||cycle.getRequestContext().getParameter("lx")!=null) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			riqi = null;
			riqichange = false;
			riq2 = null;
			riq2change = false;
			setJincpcValue(null);
			setJincpcModel(null);
			/*
			 * �޸��ˣ���ΰ
			 * �޸�ʱ�䣺2009-09-08
			 * �޸����ݣ���ʼ��setTreeid()����������һ�����Ʋ�ͬ����ͬʱ��¼������ݻ���
			 */
			this.setTreeid("" + visit.getDiancxxb_id());
			visit.setString2("");//ҳ������
		}
		
		String pagewith = cycle.getRequestContext().getParameter("lx");// �ж��Ƿ�����������
		if (pagewith != null) {
			visit.setString2(pagewith);
		}
		
		if(riqichange || riq2change){
			riqichange = false;
			riq2change = false;
			setJincpcValue(null);
			setJincpcModel(null);
			getSelectData();
		}
		getSelectData();
	}
}