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
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.huaygl.Shenhcl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruchyejsh_szs extends BasePage implements PageValidateListener {
	private String CustomSetKey = "Ruchyejsh_szs";
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String zhilbids = "";
	public String getZhilbids(){
		return zhilbids;
	}
	public void setZhilbids(String ids){
		zhilbids= ids;
	}

	// ����ҳ���ϱ��ʵ��ܺ�
	private String BilTotal;

	public String getBilTotal() {
		return BilTotal;
	}

	public void setBilTotal(String BilTotal) {
		this.BilTotal = BilTotal;
	}

	// ������
//	boolean riqichange = false;
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
//			riqichange = true;
		}

	}

//	boolean riq2change = false;

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
//			riq2change = true;
		}

	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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

	private boolean _AvgClick = false;
	public void AvgButton(IRequestCycle cycle) {
		_AvgClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelect();
			getSelectData();
		}
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelect();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelect();
			getSelectData();
		}
		if (_AvgClick) {
			_AvgClick = false;
			getAvg();
		}
	}
	
	public void Save(){
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List fhlist = new ArrayList();
		String zhilbid = "";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		String strsql = "begin \n";
		while (mdrsl.next()) {
			strsql += "update zhillsb set shenhzt=7,shenhryej='"+visit.getRenymc()+"' where zhilb_id =" + mdrsl.getString("id") + ";\n";
			
			strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
			+"values("+mdrsl.getString("id")+",'"+mdrsl.getString("huaybh")+"',"+mdrsl.getString("caiyb_id")+","
			+ DateUtil.FormatOracleDate(mdrsl.getString("huaysj"))+","+mdrsl.getString("qnet_ar") + ","
			+ mdrsl.getString("aar") + "," + mdrsl.getString("ad") + "," + mdrsl.getString("vdaf") + ","
			 + mdrsl.getString("mt") + "," + mdrsl.getString("stad") + "," + mdrsl.getString("aad") + ","
			 + mdrsl.getString("mad") + "," + mdrsl.getString("qbad") + "," + mdrsl.getString("had") + ","
			 + mdrsl.getString("vad") + "," + mdrsl.getString("fcad") + "," + mdrsl.getString("std") + ","
			 + mdrsl.getString("qgrad") + "," + mdrsl.getString("hdaf") + "," + mdrsl.getString("qgrad_daf") + ","
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
//			����ʱ������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"�������",getExtGrid().mokmc,
					"zhilb",mdrsl.getString("ID"));
			
			String Sql = "select id from fahb where zhilb_id = " + zhilbid;
			ResultSetList rs = con.getResultSetList(Sql);
			while (rs.next()) {
				String id = rs.getString("id");
				Jilcz.addFahid(fhlist, id);
			}
			rs.close();
		}
		strsql += " end;";
		con.getUpdate(strsql);
		mdrsl.close();
		con.Close();
		Chengbjs.CountChengb(visit.getDiancxxb_id(),fhlist,false);
	}

	public void Huit(){
		Visit visit = (Visit) this.getPage().getVisit();
		String tableName = "zhillsb";
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			sql.append("update ").append(tableName).append(" set shenhzt=4,shenhryej='"+visit.getRenymc()+"' where zhilb_id =").append(mdrsl.getString("id"))
			.append(";\n");
//			����ʱ������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"������˻���",getExtGrid().mokmc,
					"zhilb",mdrsl.getString("ID"));
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}
	
	private boolean Display = false;
	private boolean xiansztl = false;
	private boolean xiansztq = false;
	private boolean yangplb = false;
	private boolean IsShow=false;
	
	public void getSelect(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
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
	//	�����������Ƿ�ֻ��ʾ�����ԭʼָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),Ĭ��Ϊȫ����ʾ
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
	//	�糧Treeˢ������
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
			+ "                l.bil\n"
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
			+ "          from fahb f, diancxxb dc\n"
			+ "				where f.diancxxb_id = dc.id \n"
			+ 				  str + "\n"
			+ "         group by pinzb_id, zhilb_id, meikxxb_id,faz_id) f,\n"
			+ "       meikxxb m,\n" + "       pinzb p, chezxxb cz\n"
			+ " where f.zhilb_id = z.id(+)\n"
			+ "   and c.zhilb_id = f.zhilb_id\n"
			+ "   and f.meikxxb_id = m.id\n"
			+ "   and f.zhilb_id = l.zhilb_id\n"
			+ "   and f.pinzb_id = p.id\n"
			+ "   and f.faz_id=cz.id\n"
			+ "   and zm.zhillsb_id = l.id\n"
			+ "   and (l.shenhzt = 5 or l.shenhzt = 6)\n"
			+ "   and l.zhilb_id in (select distinct c.zhilb_id from caiyb c,fahb f,zhillsb z\n" 
			+ "                       where f.zhilb_id = c.zhilb_id and c.zhilb_id = z.zhilb_id and (z.shenhzt = 5 or z.shenhzt = 6) \n"
			+ "                         and f.daohrq>=" + DateUtil.FormatOracleDate(getBeginRiq())
			+ "                         and f.daohrq<=" + DateUtil.FormatOracleDate(getEndRiq())
			+		")\n"
			+ " order by l.zhilb_id";
		rsl = con.getResultSetList(sql);
		String zhilb_ids = "";
		String sqlsb = new String();//"begin"
		String mark = MainGlobal.getXitxx_item("����", "�Ƿ�������ʼ��㻯��ֵ", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		if (mark.equals("��")) {
			while(rsl.next()){
				zhilb_ids += "," + rsl.getString("id");
				sqlsb = "\n update zhillsb set shifsy = 1, bil = " + rsl.getString("bil") + " where id = " + rsl.getString("id");
				con.getUpdate(sqlsb);
			}
		} else {
			while(rsl.next()){
				zhilb_ids += "," + rsl.getString("id");
				sqlsb = "\n update zhillsb set shifsy = 1" + " where id = " + rsl.getString("id");
				con.getUpdate(sqlsb);
			}
		}
		if(!"".equals(zhilb_ids)){
			zhilb_ids = zhilb_ids.substring(1);
		}else{
			zhilb_ids = "-1";
		}
		setZhilbids(zhilb_ids);
		//����������ʱ�����Ƿ�ʹ�õ�״̬
		sql = "update zhillsb set shifsy = 0 where zhilb_id in(" + getZhilbids() + ") ";
		con.getUpdate(sql);
		rsl.close();
		con.Close();
	}
	
	public void getSelectData(){
		JDBCcon con = new JDBCcon();
		String sql = "";
		String mark = MainGlobal.getXitxx_item("����", "�Ƿ�������ʼ��㻯��ֵ", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		String erjshxs = MainGlobal.getXitxx_item("����", "���������ʾ������", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��");
		String zzhy = MainGlobal.getXitxx_item("����", "���ջ���������", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "��������");
		if ("��".equals(mark)) {
			sql = Shenhcl.getErjshpj(con, getZhilbids(), getBilTotal());
		} else {
			if("��".equals(erjshxs) && "��������".equals(zzhy)){
				sql = "select a.id,a.meikdw,a.faz,a.caiyb_id,a.pinz,a.ches,a.shul,a.huaybh," +
						"zm.bianm huaybianh,a.huaysj,a.qnet_ar,a.aar,a.ad,a.vdaf,a.mt,a.stad," +
						"a.aad,a.mad,a.qbad,a.had,a.vad,a.fcad,a.std,a.qgrad,a.hdaf,a.qgrad_daf," +
						"a.sdaf,a.t1,a.t2,a.t3,a.t4,a.huayy,a.lury,a.beiz  " +
						"from (" + Shenhcl.Judgment(con, getZhilbids()) + ") a," +
								"zhillsb zls,zhuanmb zm,zhuanmlb zmlb " +
						"where a.id=zls.zhilb_id and zls.id=zm.zhillsb_id and zm.zhuanmlb_id=zmlb.id and zmlb.jib=3";
			}else{
				sql = Shenhcl.Judgment(con, getZhilbids());
			}
		}
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		// ���ñ��������ڱ���
		egu.setTableName("zhilb");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// ������ʾ������
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
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("shul").setHeader("����(��)");
		egu.getColumn("shul").setEditor(null);
		egu.getColumn("huaybh").setHeader("��������");
		egu.getColumn("huaybh").setEditor(null);
		
		if("��".equals(erjshxs) && "��������".equals(zzhy)){
			egu.getColumn("huaybianh").setHeader("������");
			egu.getColumn("huaybianh").setEditor(null);
			egu.getColumn("huaybianh").setWidth(90);
		}
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
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
		egu.getColumn("shul").setWidth(50);
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
		if(!yangplb){
			egu.getColumn("huaylb").setHidden(true);
		}		
		egu.addPaging(25);
		
		// ������
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
		df1.Binding("RIQ2", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
		egu.addTbarText("-");
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,"HuitButton");
		egu.addTbarText("-");
		GridButton Create = new GridButton("�鿴ƽ��ֵ", "ShowAvg", SysConstant.Btn_Icon_Insert);
		egu.addTbarBtn(Create);
		setExtGrid(egu);
		con.Close();
	}
	
	//�鿴ƽ��ֵ
	public void getAvg() {
		JDBCcon con = new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(this.getChange());
		String zhilb_ids = "";
		while (mdrsl.next()) {
			zhilb_ids += "," + mdrsl.getString("id");
		}
		mdrsl.close();
		zhilb_ids = zhilb_ids.substring(1);
		String sql = "select avg(z.qnet_ar) qnet_ar,\n"
				+ "       avg(z.aar) aar,\n"
				+ "       avg(z.ad) ad,\n"
				+ "       avg(z.vdaf) vdaf,\n"
				+ "       avg(z.mt) mt,\n"
				+ "       avg(z.stad) stad,\n"
				+ "       avg(z.aad) aad,\n"
				+ "       avg(z.mad) mad,\n"
				+ "       avg(z.qbad) qbad,\n"
				+ "       avg(z.had) had,\n"
				+ "       avg(z.vad) vad,\n"
				+ "       avg(z.fcad) fcad,\n"
				+ "       avg(z.std) std,\n"
				+ "      avg( z.qgrad) qgrad,\n"
				+ "       avg(z.hdaf) hdaf,\n"
				+ "       avg(z.qgrad_daf) qgrad_daf,\n"
				+ "       avg(z.sdaf) sdaf\n"
				+ "      from (select max(l.zhilb_id) as id,\n"
				+ "               max(l.huaysj) as huaysj,\n"
				+ "               avg(l.qnet_ar) qnet_ar,\n"
				+ "               avg(l.aar) aar,\n"
				+ "               avg(l.ad) ad,\n"
				+ "               avg(l.vdaf) vdaf,\n"
				+ "               avg(l.mt) mt,\n"
				+ "               avg(l.stad) stad,\n"
				+ "               avg(l.aad) aad,\n"
				+ "               avg(l.mad) mad,\n"
				+ "               avg(l.qbad) qbad,\n"
				+ "               avg(l.had) had,\n"
				+ "               avg(l.vad) vad,\n"
				+ "               avg(l.fcad) fcad,\n"
				+ "               avg(l.std) std,\n"
				+ "               avg(l.qgrad) qgrad,\n"
				+ "               avg(l.hdaf) hdaf,\n"
				+ "               avg(l.qgrad_daf) qgrad_daf,\n"
				+ "               avg(l.sdaf) sdaf \n"
				+ "          from zhillsb l\n"
				+ "         where id in ("
				+ zhilb_ids
				+ ")\n"
				+ "         group by l.zhilb_id, l.huaysj) z,\n"
				+ "       caiyb c,\n"
				+ "       (select sum(laimsl) as jingz, meikxxb_id, zhilb_id, pinzb_id\n"
				+ "          from fahb\n"
				+ "         group by pinzb_id, zhilb_id, meikxxb_id) f,\n"
				+ "       meikxxb m,\n" + "       pinzb p\n"
				+ " where f.zhilb_id = c.zhilb_id\n"
				+ "   and f.zhilb_id = z.id\n" + "   and c.zhilb_id = z.id\n"
				+ "   and f.meikxxb_id = m.id\n" + "   and f.pinzb_id = p.id";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			riqi = null;
//			riqichange = false;
			riq2 = null;
//			riq2change = false;
			this.setTreeid("" + visit.getDiancxxb_id());
			getSelect();
			getSelectData();
		}
//		if (riqichange || riq2change) {
//			riqichange = false;
//			riq2change = false;
//		}
		
	}

}
