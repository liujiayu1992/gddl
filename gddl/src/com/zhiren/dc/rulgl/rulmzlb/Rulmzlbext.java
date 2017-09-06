package com.zhiren.dc.rulgl.rulmzlb;

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

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.Checkbox;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2010-04-09
 * �������������ܣ�1.�÷�������ƹ���ƶ�
 * 		���÷�Χ����������
 */
/*����:���ܱ�
 *ʱ��:2010-4-8 20:26:20
 *����:1.��λ����������С��λ����������
 *     2.���ӷ������ڱ���¯���ڴ�1��Ĳ�������
 * select zhi from xitxxb where leib = '��¯' and mingc = '��¯������ֵС��λ��' and zhuangt=1
 * select zhi from xitxxb x where x.mingc='��¯������������Ƿ����¯���ڴ�1��' and leib='��¯' and x.zhuangt=1
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-16
 * �������޸�������ͷ����������ύΪȫ���ύ��
 */
/*
 * ����:tzf
 * ʱ��:2009-11-10
 * �޸�����:���Ӷ����滻����   ����ʱ   ����ԱĬ��Ϊ��¼��Ա����
 */
/*
 * ����:tzf
 * ʱ��:2009-10-29
 * �޸�����:���ӵ��밴ť��Ӧ���ύ����  ԭ������ֵ�������¼��㣬ֻ����������״̬
 */
/*
 * ����:tzf
 * ʱ��:2009-10-27
 * �޸�����:��������㵯Ͳֵ�͵�λ��ֵ���������� A B C D������鵯Ͳֵ��ֵ����ÿ�������λ��ֵ�����ֵΪ���յĵ�λ��ֵ��
 */
/*
 * ����:tzf
 * ʱ��:2009-10-26
 * �޸�����:���밴ť����Ϊ���ݿ��ַ�����Ĳ�һ�����ֵ���yx_mhysj������xhȥ���жϡ�
 */
/*
 * ����:tzf
 * ʱ��:2009-10-21
 * �޸�����:���ӵ��밴ť�����봦����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-28 18��07
 * ������������¯��Ͳ��ֵ����Ϊ80
 */
/*
 * ����
 * 2009-05-13
 * �糧����TreeIdδ��ʼ����BUG
 */
/**
 * @author ly
 * �޸�ʱ�䣺2009-04-27
 * �޸����ݣ����ӵ糧Tree
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-08
 * �������޸ĵ糧Treeˢ�¼�����û�п���һ�����Ƶ����⡣
 * �޸ı���ʱ����Ա��¼��Ա���滥��������
 */
/*
 * ���ߣ���ɭ��
 * ʱ�䣺2009-07-10
 * �������޸ı���ʱ����Ա��¼��Ա���滥��������
 */

/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-05-16
 * �������Ӳ���������¯����ֵ¼�벻�Զ��������¼���
 */
public class Rulmzlbext extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
//	ҳ���ʼ��(ÿ��ˢ�¶�ִ��)
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
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		if (MainGlobal.getXitxx_item("��¯", "��¯����ֵ¼���Ƿ����¼���","0", "��").equals("��")) {
			Save1(getChange(), visit);
		}else{
			TrueSave();
		}
		
//		Meihybext.UpdateRulzlID(getRiqi(), Integer.parseInt(diancxxb_id));//visit.getDiancxxb_id()��Ϊһ�����ƶ��ģ�
	}
	
	//�����ı��水ť,���ݿ����ȱ���,������ύ
	public void TrueSave() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
	
		while (mdrsl.next()) {
			sql.append(
					"update rulmzlb\n" +
					"   set fenxrq = to_date('"+mdrsl.getString("FENXRQ")+"','yyyy-MM-dd'),\n" + 
					"       rulbzb_id = "+getExtGrid().getValueSql(getExtGrid().getColumn("RULBZB_ID"), mdrsl.getString("RULBZB_ID"))+",\n" + 
					"       mt = "+mdrsl.getDouble("MT")+",\n" + 
					"       had = "+mdrsl.getDouble("had")+",\n" + 
					"       mad = "+mdrsl.getDouble("MAD")+",\n" + 
					"       aad = "+mdrsl.getDouble("AAD")+",\n" + 
					"       vad = "+mdrsl.getDouble("VAD")+",\n" + 
					"       std = "+mdrsl.getDouble("Std")+",\n" + 
					"       stad = "+mdrsl.getDouble("Stad")+",\n" + 
					"       qbad = "+mdrsl.getDouble("QBAD")+",\n" + 
					"       huayy = '"+mdrsl.getString("HUAYY")+"',\n" + 
					"       beiz = '"+mdrsl.getString("BEIZ")+"',\n" + 
					"       lury = '"+visit.getRenymc()+"',\n" + 
					"       lursj = sysdate\n" + 
					" where id = "+mdrsl.getString("ID")+";\n"
			);
			
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		
		mdrsl.close();
		con.Close();
	}
	
	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		double meil = 0;
		StringBuffer sql = new StringBuffer("begin \n");
		StringBuffer sql2 = new StringBuffer("update rulmzlb set shenhzt=0 where id in (");
		String Sql_xiaosw = "select zhi from xitxxb where leib = '��¯' and mingc = '��¯������ֵС��λ��' and zhuangt=1";
		ResultSetList rsl = con.getResultSetList(Sql_xiaosw);
		int Qnetar_weis=3;
		if(rsl.next()){
			Qnetar_weis=rsl.getInt("zhi");
		}
		rsl.close();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		double[] zhi = new double[7];
		long[] rulmzlb_id = new long[mdrsl.getRows()]; 
		int i=0;
		while (mdrsl.next()) {
//			rulmzlbid = Long.parseLong(mdrsl.getString("ID"));
//			diancxxb_id = mdrsl.getString("diancxxb_id");
//			if (rulmzlbid == 0) {
//				rulmzlbid = Long.parseLong(MainGlobal.getNewID(visit
//						.getDiancxxb_id()));
//			} else {
//				StringBuffer sql1 = new StringBuffer("begin \n");
//				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
//						SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Rulhy,
//						tableName,rulmzlbid+"");
//				sql1.append("delete ").append(tableName).append(" where id = ")
//						.append(rulmzlbid).append(";");
//				sql1.append("end;");
//				con.getDelete(sql1.toString());
//			}
			zhi[0] = mdrsl.getDouble("mt");
			zhi[1] = mdrsl.getDouble("mad");
			zhi[2] = mdrsl.getDouble("aad");
			zhi[3] = mdrsl.getDouble("vad");
//			zhi[4] = mdrsl.getDouble(strl);
//			��
			zhi[4] = mdrsl.getDouble(visit.getString11())==0?visit.getDouble1():mdrsl.getDouble(visit.getString11());
			zhi[5] = mdrsl.getDouble("qbad");
//			zhi[6] = mdrsl.getDouble(strq);
//			��
			zhi[6] = mdrsl.getDouble(visit.getString12())==0?visit.getDouble2():mdrsl.getDouble(visit.getString12());
			
			String sqlmeil = "select fadhy+gongrhy+qity as meil from meihyb where rulmzlb_id="+mdrsl.getString("ID");
			ResultSetList rsmeil = con.getResultSetList(sqlmeil);
			if(rsmeil.next()){
				meil = rsmeil.getDouble("meil");
			}
			sql.append(
					"update rulmzlb\n" +
					"   set fenxrq = to_date('"+mdrsl.getString("FENXRQ")+"','yyyy-MM-dd'),\n" + 
					"       rulbzb_id = "+getExtGrid().getValueSql(getExtGrid().getColumn("RULBZB_ID"), mdrsl.getString("RULBZB_ID"))+",\n" + 
					"       mt = "+mdrsl.getString("MT")+",\n" + 
					"       mad = "+mdrsl.getString("MAD")+",\n" + 
					"       aad = "+mdrsl.getString("AAD")+",\n" + 
					"       vad = "+mdrsl.getString("VAD")+",\n" + 
					"       qbad = "+mdrsl.getString("QBAD")+",\n" + 
					"		meil = "+meil+",\n" +
					"       huayy = '"+mdrsl.getString("HUAYY")+"',\n" + 
					"       beiz = '"+mdrsl.getString("BEIZ")+"',\n" + 
					"       lury = '"+visit.getRenymc()+"',\n" + 
					"       lursj = sysdate\n" + 
					" where id = "+mdrsl.getString("ID")+";\n"
			);
			rulmzlb_id[i]=Long.parseLong(mdrsl.getString("ID"));
			i++;
//			sql
//					.append("insert into ")
//					.append(tableName)
//					.append(
//							" (id,diancxxb_id,fenxrq,lursj,rulrq,RULBZB_ID,JIZFZB_ID,huayy,LURY) values (");
//			sql.append(rulmzlbid
//					+ ","
//					+ diancxxb_id
//					+ ",to_date('"
//					+ mdrsl.getString("fenxrq")
//					+ "','yyyy-mm-dd'),to_date(to_char(sysdate,'yyyy-mm-dd')"
//					+ ",'yyyy-mm-dd'),to_date('"
//					+ mdrsl.getString("rulrq")
//					+ "','yyyy-mm-dd'),"
//					+ (getExtGrid().getColumn("rulbzb_id").combo)
//							.getBeanId(mdrsl.getString("rulbzb_id"))
//					+ ","
//					+ (getExtGrid().getColumn("jizfzb_id").combo)
//							.getBeanId(mdrsl.getString("jizfzb_id")) + ",'"
//					+ mdrsl.getString("huayy") + "','"+visit.getRenymc()+"');\n");
		}
		sql.append("end;");
		if(con.getUpdate(sql.toString())>=0){
			
			for(i=0;i<rulmzlb_id.length;i++){
				
				Compute.ComputeRULUValue(con, rulmzlb_id[i], visit.getDiancxxb_id(), zhi,Qnetar_weis);
				sql2.append(rulmzlb_id[i]).append(",");
			}
			
			sql2.deleteCharAt(sql2.length()-1);
			sql2.append(")");
			
			
			String strsql=" select * from xitxxb where mingc='��¯���鵼���ύ��ť��ʾ' and leib='��¯' and zhuangt=1 and zhi='��' and diancxxb_id="+visit.getDiancxxb_id();
			mdrsl=con.getResultSetList(strsql);
	 		if(mdrsl.next()){//��ͷר��
	 			
	 			if(con.getUpdate(sql2.toString())>=0){
					
					this.setMsg("����ɹ���");
				}else{
					
					this.setMsg("����ʧ�ܣ�");
				}
	 		}else{
//	 			������ͷ�糧ֱ����ʾ����ɹ�
	 			this.setMsg("�ύ�ɹ���");
	 		}
		}else{
			
			this.setMsg("�ύʧ�ܣ�");
		}
		mdrsl.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	
	private boolean _TrueSaveChick = false;

	public void TrueSaveButton(IRequestCycle cycle) {
		_TrueSaveChick = true;
	}
	
	private boolean _DaorChick;
	
	public void DaorButton(IRequestCycle cycle){
		_DaorChick=true;
	}

    private boolean _RefreshChick;

    public void RefreshButton(IRequestCycle cycle){
        _RefreshChick=true;
    }

	private boolean _DaorTjChick=false;
	public void DaorTjButton(IRequestCycle cycle){
		_DaorTjChick=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_DaorChick){
			_DaorChick=false;
			Daor();
			getSelectData();
		}
		if(_DaorTjChick){
			_DaorTjChick=false;
			DaorTj();
		}
		if(_TrueSaveChick){
			_TrueSaveChick=false;
			TrueSave();
			getSelectData();
		}
        if(_RefreshChick){
            _RefreshChick=false;
            getSelectData();
        }
	}
	
	private void DaorTj(){

		String tableName = "rulmzlb";
		Visit visit=(Visit)this.getPage().getVisit();

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");
		String strchange=this.getChange();
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		long rulmzlbid = 0;
		while (mdrsl.next()) {
			rulmzlbid = Long.parseLong(mdrsl.getString("ID"));
			sql.append(" update "+tableName+" set RULBZB_ID="+(getExtGrid().getColumn("rulbzb_id").combo)
					.getBeanId(mdrsl.getString("rulbzb_id")) +",huayy='"+mdrsl.getString("huayy")+"' where id="+rulmzlbid+";\n");
			sql.append(" update "+tableName+" set shenhzt = 1 where id="+rulmzlbid+";\n");
			
		}
		
		sql.append("end;");
		con.getUpdate(sql.toString());
		mdrsl.close();
		con.Close();
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		rsl.close();
		return s.substring(0,s.lastIndexOf(","));
	}
	
	private void Daor(){
		Visit visit=(Visit)this.getPage().getVisit();
		JDBCcon con=new JDBCcon();
		String dcidStr=getFenc(con);
		
		String insertSql="";
		
		String sql=" select * from xitxxb where mingc='��¯ú���鵼�����ݱ�' and leib='��¯'  and zhuangt=1";
		ResultSetList rsl=con.getResultSetList(sql);
		
		String tableName="yx_mhysj";
		if(rsl.next()){
			tableName=rsl.getString("zhi");
		}
		
		if(tableName!=null ){//��� ��ͷ
			
			sql=" select r.id, r.JIZFZB_ID,REGEXP_SUBSTR(j.mingc,'[0-9]+') jzmc from rulmzlb r,JIZFZB j where r.JIZFZB_ID=j.id and  r.rulrq="+DateUtil.FormatOracleDate(this.getRiqi())+" and  r.diancxxb_id in ("+dcidStr+") ";
			rsl=con.getResultSetList(sql);
			boolean flag=false;
			List list_mzbid=new ArrayList();
			List list_zhi=new ArrayList();
			List list_qbads=new ArrayList();
			while(rsl.next()){//˵����ʱ�仯��������ֵ����ú���ö�Ӧ�Ĺ�ϵ���䣬��id���䣬���Ļ���ֵ�ͻ���ֵ����Ϣ
				flag=true;
				
				String id=rsl.getString("id");
				String jzmc=rsl.getString("jzmc");
				
				sql=" select y.xh, y.CT_ZBBH,y.A"+jzmc+" aValue,y.B"+jzmc+" bValue,y.C"+jzmc+" cValue,y.D"+jzmc+" dValue,y.E"+jzmc+" jzvalue from "+tableName+"  y where y.DT_RQ="+DateUtil.FormatOracleDate(this.getRiqi());
				
				String qbad="";//��Ͳ��ֵ
				String mt="";//ȫˮ��
				String mad="";//�ոɻ�ˮ��
				String aad="";//�ոɻ��ҷ�
				String vad="";//�ոɻ��ӷ���
				String stad="";//�ոɻ���
				String had="";//��Ԫ��
				ResultSetList rsl2=con.getResultSetList(sql);
				while(rsl2.next()){//ȡ��ÿ������Ļ���ֵ��Ϣ
					
					if(rsl2.getString("xh").equals("-1")){
//						qbad=rsl2.getString("jzvalue");
						int cou=0;
						if(rsl2.getDouble("aValue")!=0) cou++;
						if(rsl2.getDouble("bValue")!=0) cou++;
						if(rsl2.getDouble("cValue")!=0) cou++;
						if(rsl2.getDouble("dValue")!=0) cou++;
						
						if(cou==0) qbad="0";
						else 
						qbad=(rsl2.getDouble("aValue")+rsl2.getDouble("bValue")+rsl2.getDouble("cValue")+rsl2.getDouble("dValue"))*1.0/1000/cou+"";
					
						List list_qbad=new ArrayList();
						list_qbad.add(rsl2.getDouble("aValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("bValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("cValue")*1.0/1000+"");
						list_qbad.add(rsl2.getDouble("dValue")*1.0/1000+"");
						list_qbads.add(list_qbad);
					}
					if(rsl2.getString("xh").equals("1")){
						mt=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("2")){
						mad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("3")){
						aad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("5")){
						vad=rsl2.getString("jzvalue");
					}
					if(rsl2.getString("xh").equals("6")){
						stad=rsl2.getString("jzvalue");
					}
				
				}
				
				sql=" select * from xitxxb where mingc='��¯ú���鵼����ֵ��' and leib='��¯'  and zhuangt=1";
				String qingTableName="yx_mhy_h";
				rsl2=con.getResultSetList(sql);
				if(rsl2.next()){
					qingTableName=rsl2.getString("zhi");
				}
				
				sql=" select * from "+qingTableName+" where ct_no='10004'";
				rsl2=con.getResultSetList(sql);
				if(rsl2.next()){
					had=rsl2.getString("CT_MEMO");
				}
				
				if(qbad==null || qbad.equals(""))   qbad="0";
				
				if(mt==null   || mt.equals("")) 	 mt="0";
				if(mad==null  || mad.equals(""))    mad="0";
				if(aad==null  || aad.equals(""))    aad="0";
				if(vad==null  || vad.equals(""))    vad="0";
				if(stad==null || stad.equals(""))   stad="0";
				if(had==null  || had.equals(""))    had="0";
				
				double[] zhi = new double[7];
				zhi[0] =Double.parseDouble(mt);
				zhi[1] = Double.parseDouble(mad);
				zhi[2] = Double.parseDouble(aad);
				zhi[3] = Double.parseDouble(vad);
				zhi[4] = Double.parseDouble(stad);
				zhi[5] = Double.parseDouble(qbad);
				zhi[6] = Double.parseDouble(had);
				list_zhi.add(zhi);
				
				insertSql+=" update rulmzlb set qbad="+qbad+",mt="+mt+",mad="+mad+",aad="+aad+",vad="+vad+",stad="+stad+",had="+had+",LURY='"+visit.getRenymc()+"' where id="+id+"; \n";
				list_mzbid.add(id);
				
			}
			if(flag){//�ж�Ӧ��ϵ
				insertSql=" begin \n"+insertSql+" end;";
//				System.out.println(insertSql);
				if(con.getUpdate(insertSql)>=0){//���³ɹ�,�������໯��ֵ
					for(int i=0;i<list_mzbid.size();i++){
						long rulmzlbid=Long.valueOf((String)list_mzbid.get(i)).longValue();
						double[] zhi=(double[])list_zhi.get(i);
						this.ComputeRULUValue(con, rulmzlbid, visit.getDiancxxb_id(), zhi,(List)list_qbads.get(i));
					}
				}
				
			}else{//���²���,��ú����û���κι�����ϵ
				
			}
		}
		con.Close();
	}
	
	//������㻯��ֵ�ķ���
	private  void ComputeRULUValue(JDBCcon con, long rulmzlbid,
			long diancxxb_id, double[] zhi,List list_qbad) {
		StringBuffer SQL = new StringBuffer("");
		double dblHad = 0;
		double dblVad = 0;
		double dblStad = 0;
		double dblAad = 0;
		double dblMt = 0;
		double dblMad = 0;
		double dblA = 0;
		double dblAd = 0;
		double dblAar = 0;
		double dblQgrad = 0;
		double dblQbad = 0;
		double dblQnetar = 0;
		double dblFcad = 0;
		double dblVdaf = 0;
		double dblStd = 0;
		double dblSdaf = 0;
		double dblHdaf = 0;
		double dblQgrdaf = 0;
		double dblHar = 0;
		double dblQgrd = 0;
		try {
			dblMt = zhi[0];
			dblMad = zhi[1];
			dblAad = zhi[2];
			dblVad = zhi[3];
			dblQbad = zhi[5];
			String SQLCtr = "";
			String SQLStr = "select * from xitxxb where leib = '��¯' and mingc = '��¯��ȡֵ'";
			ResultSetList rsl = con.getResultSetList(SQLStr);
			if (rsl.next()) {
				SQLCtr = "select zhi\n" + "  from rulysfxb\n"
						+ " where diancxxb_id = " + diancxxb_id + "\n"
						+ "   and zhuangt = 1\n"
						+ "   and rulysfxxm_id = (select id\n"
						+ "                         from xitxxb\n"
						+ "                        where leib = '��¯'\n"
						+ "                          and mingc like '��¯������'\n"
						+ "                          and zhuangt = 1\n"
						+ "                          and diancxxb_id = "
						+ diancxxb_id + " ) order by riq ";
				ResultSetList rsel = con.getResultSetList(SQLCtr);
				while (rsel.next()) {
					zhi[6] = rsel.getDouble("zhi");
				}
				rsel.close();
			}
			rsl.close();
			SQLStr = "select * from xitxxb where leib = '��¯' and mingc = '��¯��ȡֵ'";
			rsl = con.getResultSetList(SQLStr);
			if (rsl.next()) {
				SQLCtr = "select zhi\n" + "  from rulysfxb\n"
						+ " where diancxxb_id = " + diancxxb_id + "\n"
						+ "   and zhuangt = 1\n"
						+ "   and rulysfxxm_id = (select id\n"
						+ "                         from xitxxb\n"
						+ "                        where leib = '��¯'\n"
						+ "                          and mingc like '��¯������'\n"
						+ "                          and zhuangt = 1\n"
						+ "                          and diancxxb_id = "
						+ diancxxb_id + " ) order by riq ";
				ResultSetList rsel = con.getResultSetList(SQLCtr);
				while (rsel.next()) {
					zhi[4] = rsel.getDouble("zhi");
				}
				rsel.close();
			}
			rsl.close();
			String sql = "select zhi\n" + "  from xitxxb\n"
					+ " where mingc = '��¯������'\n" + "   and zhuangt = 1\n"
					+ "   and diancxxb_id = " + diancxxb_id;
			ResultSetList rs = con.getResultSetList(sql);
			String S = "";
			while (rs.next()) {
				S = rs.getString("zhi");
			}
			sql = "select zhi\n" + "  from xitxxb\n"
					+ " where mingc = '��¯������'\n" + "   and zhuangt = 1\n"
					+ "   and diancxxb_id = " + diancxxb_id;
			rs = con.getResultSetList(sql);
			String H = "";
			while (rs.next()) {
				H = rs.getString("zhi");
			}
			if (S.equals("Std")) {
				dblStd = zhi[4];
				dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
						2);
				dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
						2);
			} else if (S.equals("Stad")) {
				dblStad = zhi[4];
				if (dblMad == 100) {
					dblStd = 0;
				} else {
					dblStd = CustomMaths.Round_new(dblStad * 100
							/ (100 - dblMad), 2);
				}
				dblSdaf = CustomMaths.Round_new((100 / (100 - dblAd)) * dblStd,
						2);
			} else if (S.equals("Sdaf")) {
				dblSdaf = zhi[4];
				if (dblMad == 100) {
					dblStd = 0;
				} else {
					dblStd = CustomMaths.Round_new((100 - dblAd) / 100
							* dblSdaf, 2);
				}
				dblStad = CustomMaths.Round_new((100 - dblMad) * dblStd / 100,
						2);
			}
			if (H.equals("Had")) {
				dblHad = zhi[6];
				if ((100 - dblMad - dblAad) == 0) {
					dblHdaf = 0;
				} else {
					dblHdaf = CustomMaths.Round_new(dblHad * 100
							/ (100 - dblMad - dblAad), 2);
				}
			} else if (H.equals("Hdaf")) {
				dblHdaf = zhi[6];
				dblHad = CustomMaths.Round_new(((100 - dblMad - dblAad)
						* dblHdaf / 100), 2);
			}
			if (dblMad == 100) {
				dblAd = 0;
			} else {
				dblAd = CustomMaths.Round_new(dblAad * 100 / (100 - dblMad), 2);
			}
			if (dblQbad < 16.72) {
				dblA = 0.001;
			} else {
				if (dblQbad >= 16.72 && dblQbad <= 25.1) {
					dblA = 0.0012;
				} else {
					dblA = 0.0016;
				}
			}
			dblQgrad = CustomMaths.Round_new(
					(dblQbad - (0.0941 * dblStad + dblA * dblQbad)), 3);
			if (dblMad == 100) {
				dblQnetar = -23;
				dblAd = 0;
				dblAar = 0;
			} else {
				
				int cou=0;
				for(int i=0;i<list_qbad.size();i++){//��ƽ����λ��ֵ
					
					if(Double.parseDouble(list_qbad.get(i).toString())==0) continue;
					
					cou++;
					double qbad_i=Double.parseDouble(list_qbad.get(i).toString());
					double qgrad_i=CustomMaths.Round_new(
							(qbad_i - (0.0941 * dblStad + dblA * qbad_i)), 3);
					dblQnetar += CustomMaths.Round_new(((qgrad_i - 0.206 * dblHad)
							* (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), 3);
				}
				if(cou==0) dblQnetar=0;
				else dblQnetar=CustomMaths.Round_new(dblQnetar*1.0/cou, 3);
//				dblQnetar = CustomMaths.Round_new(((dblQgrad - 0.206 * dblHad)
//						* (100 - dblMt) / (100 - dblMad) - 0.023 * dblMt), 3);
				dblAd = CustomMaths.Round_new((dblAad * 100 / (100 - dblMad)),
						2);
				dblAar = CustomMaths.Round_new(
						(dblAad * (100 - dblMt) / (100 - dblMad)), 2);
				dblFcad = CustomMaths.Round_new(
						(100 - dblAad - dblVad - dblMad), 2);
			}
			if ((100 - dblMad - dblAad) == 0) {
				dblVdaf = 0;
			} else {
				dblVdaf = CustomMaths.Round_new(
						(dblVad * 100 / (100 - dblMad - dblAad)), 2);
			}
			dblQgrdaf = CustomMaths.Round_new(
					(100 / (100 - dblMad - dblAad) * dblQgrad), 3);
			dblQgrd = CustomMaths.Round_new(dblQgrad * 100 / (100 - dblMad), 3);
			dblHar = CustomMaths.Round_new(dblHad * (100 - dblMt)
					/ (100 - dblMad), 2);
			SQL = new StringBuffer("");
			SQL.append("update rulmzlb\n" + "              set qnet_ar = "
					+ dblQnetar + ",\n" + "                  aar = " + dblAar
					+ ",\n" + "                  ad = " + dblAd + ",\n"
					+ "                  vdaf = " + dblVdaf + ",\n"
					+ "                  mt = " + dblMt + ",\n"
					+ "                  stad = " + dblStad + ",\n"
					+ "                  aad = " + dblAad + ",\n"
					+ "                  mad = " + dblMad + ",\n"
					+ "                  qbad = " + dblQbad + ",\n"
					+ "                  had = " + dblHad + ",\n"
					+ "                  vad = " + dblVad + ",\n"
					+ "                  fcad = " + dblFcad + ",\n"
					+ "                  std = " + dblStd + ",\n"
					+ "                  har = " + dblHar + ",\n"
					+ "                  qgrd = " + dblQgrd + ",\n"
					+ "                  qgrad = " + dblQgrad + ",\n"
					+ "                  hdaf = " + dblHdaf + ",\n"
					+ "                  qgrad_daf = " + dblQgrdaf + ",\n"
					+ "                  sdaf = " + dblSdaf + ",\n"
					+ "                  shenhzt = 0 \n" + " where id = "
					+ rulmzlbid);
			con.getUpdate(SQL.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}

	
/*
 * ����:tzf
 * ʱ��:2009-4-15
 * �޸�����:��strl��strq�ṩĬ��ֵ������ϵͳ���úͶ��û�����ʱ���п�����ɵ�����
 */
	

	public void getSelectData() {
		
		 String strl = "Stad";//�ṩĬ��ֵ

		 String strq = "Had";//�ṩĬ��ֵ
		
		 boolean xiansztq = true;

		 boolean xiansztl = true;

		 String chr1 = "";

		 String chr2 = "";
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		�Ƿ�Ϊ���Ƿ���,��������ǵ糧����С�����ϡ��¡�����
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���",
				visit.getDiancxxb_id() + "", "��"));
		
		String riqTiaoj = this.getRiqi();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());

		}
		ResultSetList rsl = new ResultSetList();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ��¯������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ��¯������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztl = true;
			} else {
				xiansztl = false;
			}
		}
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '��¯������'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strl = rsl.getString("zhi");
		}
		sql = "select zhi\n" + "  from xitxxb\n" + " where mingc = '��¯������'\n"
				+ "   and zhuangt = 1\n" + "   and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			strq = rsl.getString("zhi");
		}
		if (xiansztq) {
			chr1 = "to_char("+strq + ",90.99) "+strq+",";
		}
		if (xiansztl) {
			chr2 = "to_char("+strl + ",90.99) "+strl+",";
		}
		
		rsl.close();
		
		double dblH = 0.0D;	//ϵͳ���ڵ�ǰһ���µ��볧������
		double dblS = 0.0D;	//ϵͳ���ڵ�ǰһ���µ��볧������
		
		visit.setString11(strl);
		visit.setString12(strq);
		
//		sql = "select nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.had) / sum(f.jingz), 4)),\n"
//				+ "           0) as had,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.hdaf) / sum(f.jingz), 4)),\n"
//				+ "           0) as hdaf,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.std) / sum(f.jingz), 4)),\n"
//				+ "           0) as std,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.stad) / sum(f.jingz), 4)),\n"
//				+ "           0) as stad,\n"
//				+ "       nvl(decode(sum(f.jingz),\n"
//				+ "                  0,\n"
//				+ "                  0,\n"
//				+ "                  round(sum(f.jingz * z.sdaf) / sum(f.jingz), 4)),\n"
//				+ "           0) as sdaf\n"
//				+ "  from fahb f, zhilb z, caiyb c\n"
//				+ " where f.zhilb_id = z.id\n"
//				+ "   and c.zhilb_id = f.zhilb_id\n"
//				+ "   and c.zhilb_id = z.id\n"
//				+ "   and f.diancxxb_id = "
//				+ visit.getDiancxxb_id()
//				+ "\n"
//				+ "   and to_char(z.huaysj, 'yyyy-mm') =\n"
//				+ "       to_char(add_months(to_date(to_char(sysdate,'yyyy-mm-dd'), 'yyyy-mm-dd'), -1),\n"
//				+ "               'yyyy-mm')";
//		�⡢��ȡ��¯Ԫ�ط���ֵ
		sql="select y.zhi as zhib, r.zhi\n" +
			"  from rulysfxb r,\n" + 
			"       (select id, zhi\n" + 
			"          from xitxxb\n" + 
			"         where leib = '��¯'\n" + 
			"           and (mingc = '��¯������' or mingc = '��¯������')\n" + 
			"           and zhuangt = 1) y\n" + 
			" where r.diancxxb_id = "+visit.getDiancxxb_id()+"\n" + 
			"   and to_char(r.riq, 'yyyy') = '"+riqTiaoj.substring(0,4)+"'\n" + 
			"   and r.rulysfxxm_id = y.id order by r.id";

		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			
			if(rs.getString("zhib").equals(strq)){
				
				dblH = rs.getDouble("zhi");
			}
			
			if(rs.getString("zhib").equals(strl)){
				
				dblS = rs.getDouble("zhi");
			}
		}
		rs.close();
		
		visit.setDouble1(dblS);		//��
		visit.setDouble2(dblH);		//��
		
		//�糧Treeˢ������
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			if(visit.isFencb()){
				diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
			}else{
				diancxxb_id = "and d.id = " + this.getTreeid() + "\n";
			}
		}
		/*
		 * 2009-5-29
		 * ���ܱ�
		 * �޸���¯�����л�����ֵ��ǰ������˳��
		 * 
		 */
		ResultSetList rs_shunx = con.getResultSetList("select zhi from xitxxb x where x.mingc='�볧��¯����ֵ¼����ʾ˳��' and x.zhuangt=1");
		String xianssx="";
		if(rs_shunx.next()){
			xianssx=rs_shunx.getString("ZHI");

		}else{
			xianssx=
					        " to_char(r.mt,90.99) mt," +
							"to_char(r.mad,90.99) mad,"+
							"to_char(r.aad,90.99) aad," +
							"to_char(r.vad,90.99) vad," +
									chr2+
									chr1+
							"to_char(r.qbad,90.99) qbad,\n";

		}
		
		
		String chaxun = "select r.id,r.diancxxb_id,r.rulrq,r.fenxrq ,rb.mingc as rulbzb_id,j.mingc as " +
				"jizfzb_id,m.fadhy+m.gongrhy+m.qity+m.feiscy meil ,\n"
				+ xianssx
				//yuss 2012-3-22  Ĭ����ʾ��¼�˵�����
				+"'"+visit.getRenymc()+"' as huayy,r.beiz\n" 
				//+ "       r.huayy,r.beiz\n"
				+ "  from rulmzlb r, diancxxb d, rulbzb rb, jizfzb j,meihyb m\n"
				+ " where r.diancxxb_id = d.id(+)\n"
				+ "   and r.rulbzb_id = rb.id(+)\n"
				+ "   and r.jizfzb_id = j.id(+) and r.id=m.rulmzlb_id\n"
				+ "   and r.rulrq = to_date('"
				+ riqTiaoj
				+ "','yyyy-mm-dd')\n"
				+ "   and (r.shenhzt = 0 or r.shenhzt = 2)\n"
//				+ "   and d.id="
				+ diancxxb_id + " order by r.fenxrq,r.rulrq,rb.xuh,j.xuh";
		rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("rulmzlb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("��λ");
		egu.getColumn("rulrq").setHeader("��¯����");
		egu.getColumn("rulrq").setEditor(null);
		egu.getColumn("fenxrq").setHeader("��������");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("rulbzb_id").setHeader("��¯����");
		egu.getColumn("jizfzb_id").setHeader("��¯����");
        egu.getColumn("meil").setHeader("ú��(��)");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("qbad").setHeader("Qb,ad(MJ/kg)");
		egu.getColumn("huayy").setHeader("����Ա");
		egu.getColumn("beiz").setHeader("��ע");
		if (xiansztq) {
			egu.getColumn(strq).setHeader(strq + "(%)");
			egu.getColumn(strq).setWidth(60);
			egu.getColumn(strq).setDefaultValue(String.valueOf(dblH));
			if(isYc){
				egu.getColumn(strq).editor.setListeners(getStr(0));
			}
		}
		if (xiansztl) {
			egu.getColumn(strl).setHeader(strl + "(%)");
			egu.getColumn(strl).setWidth(60);
			egu.getColumn(strl).setDefaultValue(String.valueOf(dblS));
			if(isYc){
				egu.getColumn(strl).editor.setListeners(getStr(0));
			}
		}
		egu.getColumn("rulrq").setWidth(85);
		egu.getColumn("fenxrq").setWidth(85);
		egu.getColumn("diancxxb_id").setWidth(85);
		egu.getColumn("rulbzb_id").setWidth(85);
		egu.getColumn("jizfzb_id").setWidth(85);

		egu.getColumn("mt").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("huayy").setWidth(60);
		
		if(isYc){
			egu.getColumn("mt").editor.setListeners(getStr(1));
			egu.getColumn("mad").editor.setListeners(getStr(0));
			egu.getColumn("aad").editor.setListeners(getStr(0));
			egu.getColumn("vad").editor.setListeners(getStr(0));
			egu.getColumn("qbad").editor.setListeners(getStr(0));
			egu.getColumn("huayy").editor.setListeners(getStr(0));
			egu.getColumn("beiz").editor.setListeners(getStr(2));
		}

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
		egu.addPaging(25);// ���÷�ҳ
		// *****************************************����Ĭ��ֵ****************************
		egu.getColumn("diancxxb_id").setDefaultValue(
				"" + visit.getDiancxxb_id());
		egu.getColumn("rulrq").setDefaultValue(riqTiaoj);
		egu.getColumn("fenxrq")
				.setDefaultValue(DateUtil.FormatDate(new Date()));

		// ������������¯����
		ComboBox cb_banz = new ComboBox();
		egu.getColumn("rulbzb_id").setEditor(cb_banz);
		cb_banz.setEditable(true);
		// һ������ѡ�ܳ�ʱӦ��ˢ�����зֳ�
		String rulbzb_idSql = "select r.id, r.mingc from rulbzb r, diancxxb dc where r.diancxxb_id = dc.id and (dc.id = "
				+ getTreeid() + " or dc.fuid = " + getTreeid() + ") order by r.xuh";
		egu.getColumn("rulbzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(rulbzb_idSql));
		// ������������¯����
		ComboBox cb_jiz = new ComboBox();
		egu.getColumn("jizfzb_id").setEditor(cb_jiz);
		cb_jiz.setEditable(true);
		String cb_jizSql = "select j.id,j.mingc from jizfzb j,diancxxb d where j.diancxxb_id=d.id(+) and (d.id="
				+ getTreeid() + " or d.fuid="+ getTreeid() + ")";
		egu.getColumn("jizfzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(cb_jizSql));

		// ������
		egu.addTbarText("����:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 �糧��
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// ���÷ָ���
		egu.addTbarText("��λ:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		
		//���������ı��水ť,���ݿ����ȱ�����ύ
		sql=" select * from xitxxb where mingc='��¯ú������ʾ���水ť' and leib='��¯' and zhuangt=1 and zhi='��' ";
		ResultSetList  rs1=con.getResultSetList(sql);
		if(rs1.next()){
		
			egu.addToolbarButton(GridButton.ButtonType_Save, "TrueSaveButton");
		}
		
		
    	sql=" select * from xitxxb where mingc='��¯���鵼���ύ��ť��ʾ' and leib='��¯' and zhuangt=1 and zhi='��' and diancxxb_id="+visit.getDiancxxb_id();
 	   rs1=con.getResultSetList(sql);
 		if(rs1.next()){//��ͷ�ȵ���
 			egu.addToolbarButton("�ύ",GridButton.ButtonType_SaveAll, "DaorTjButton",null, SysConstant.Btn_Icon_Show);
 			egu.addToolbarButton("����",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
 		}else{
 			egu.addToolbarButton("�ύ",GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);
 		}

        egu.addToolbarButton("ˢ��",GridButton.ButtonType_Refresh, "RefreshButton",null, SysConstant.Btn_Icon_Search);
//		��ӵ��밴ť
		sql=" select * from xitxxb where mingc='��¯ú������ӵ��밴ť' and leib='��¯' and zhuangt=1 and zhi='��' ";
		rs1=con.getResultSetList(sql);
		if(rs1.next()){
			GridButton daor=new GridButton("����","function(){ Ext.MessageBox.confirm('��ʾ��Ϣ','ȷ��Ҫ���ǵ���ʱ���������?',function(btn){if(btn=='yes'){document.all.DaorButton.click();} });}");
			egu.addTbarBtn(daor);
		}
		
//		�Ƿ���Ӷ����滻����
		
		sql=" select * from xitxxb where mingc='��¯��������滻��ʾ' and zhi='��' and leib='��¯' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		rs1=con.getResultSetList(sql);
		if(rs1.next()){
			Checkbox cbselectlike=new Checkbox();
			
			cbselectlike.setId("SelectLike");
			egu.addToolbarItem(cbselectlike.getScript());
			
			egu.addTbarText("�����滻");
			
			egu.addOtherScript(" gridDiv_grid.on('afteredit',function(e){\n" +
					
					" if(SelectLike.checked) { \n" +
					
					"for(var i=e.row;i<gridDiv_ds.getCount();i++){\n" +
					"var rec=gridDiv_ds.getAt(i);\n" +
					" rec.set(e.field+'',e.value);\n" +
					"}\n" +
					
					"}\n" +
					
					"" +
					"  }); ");
		}
		
		if(isYc){
			egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){row = irow;col=icol;});");//�õ��С���
		}

		ResultSetList rsl3 = con.getResultSetList("select * from xitxxb where mingc='��������¼���Ƿ���ʾ' and zhi='��' and leib='����' and zhuangt=1 ");
		if(rsl3.next()){
			StringBuffer sb = new StringBuffer();
			sb.append("gridDiv_grid.on('afteredit',function(e){\n");
			sb.append("if(e.field=='QBAD'){if(e.record.get('QBAD')>21||e.record.get('QBAD')<14.5){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϵ�Ͳ��ֵ�Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='MAD'){if(e.record.get('MAD')>7.5||e.record.get('MAD')<0.6){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ��ˮ���Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='MT'){if(e.record.get('MT')>18||e.record.get('MT')<3.5){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ��ȫˮ���Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='AAD'){if(e.record.get('AAD')>43||e.record.get('AAD')<15){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϻҷ��Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='VAD'){if(e.record.get('VAD')>25||e.record.get('VAD')<16){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ�ϻӷ����Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='STD'){if(e.record.get('STD')>3||e.record.get('STD')<0.5){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ������Ƿ���ȷ��');}}\n");
			sb.append("if(e.field=='HAD'){if(e.record.get('HAD')>3||e.record.get('HAD')<2){Ext.MessageBox.alert('��ʾ��Ϣ','��ȷ����ֵ�Ƿ���ȷ��');}}\n");
			sb.append("});");
			egu.addOtherScript(sb.toString());
		}
		setExtGrid(egu);
		rs1.close();
		con.Close();
	}
	
	//��������ƹ�����JS
	public String getStr(int col){
		String Str = "";
////		 ���Ƿ�ɱ༭
//		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
//				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" +
				"			if(row>0){\n" +
				"				if(e.getKey()==e.UP){\n" +
				"					gridDiv_grid.startEditing(row-1, col);\n" +
				"					row = row-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(row+1 < gridDiv_grid.getStore().getCount()){\n" +
				"				if(e.getKey()==e.DOWN){\n" +
				"					gridDiv_grid.startEditing(row+1, col);\n" +
				"					row = row+1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.LEFT){\n" +
				"				if("+col+"!=1){\n" +
				"					gridDiv_grid.startEditing(row, col-1);\n" +
				"					col = col-1;\n" +
				"				}\n" +
				"			}\n" +
				"			if(e.getKey()==e.RIGHT){\n" +
				"				if("+col+"!=2){\n" +
				"					gridDiv_grid.startEditing(row, col+1);\n" +
				"					col = col+1;\n" +
				"				}\n" +
				"			}\n" +
				"	 	 }\n";	
		return Str;
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
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			visit.setString11("");
			visit.setString12("");
			visit.setDouble1(0);
			visit.setDouble2(0);
			setRiqi(null);
		}

		getSelectData();

	}

	// ���ڿؼ�
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='��¯' and mingc ='��¯����¼��Ĭ������' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
			
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}
	
//	 �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}

	boolean treechange = false;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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