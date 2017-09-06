package com.zhiren.dc.chengbgl;

import java.util.ArrayList;
import java.util.List;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.dc.jilgl.Jilcz;
/*
 * ���ߣ�����
 * ʱ�䣺2009-10-29
 * ���������ӹ�����ͬʱ��Ʒ�ֵ��ж�
 */
/*
 * ����:tzf
 * ʱ��:2009-07-01
 * �޸�����:CountChengb������jdbc���ӣ�Ӧ����һ�������е�����     ����  ��  �� ͬһ���������в���ʱ��ǰһ������û��commit��
 * ��һ������Ҫ����ȷ�ϣ�����ʹϵͳͣ��.
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * ���������ɳɱ���IDʱ�����ͬ��IDΪ0 ���������ɳɱ���ID
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-12-03
 * �������޸ĳɱ�����ʱ������ͬƥ�估�볧�ɱ����ɵķ�ʽ ���ڵĳɱ����㰴��ÿ������Ӧһ���ɱ���¼
 */
public class Chengbjs {

	private static final int noIgnore = 0;//ƥ���ͬʱ�����չ�Ӧ�̡�ú�󡢷�վ��Ʒ����ȫƥ��
	private static final int Ignore_Faz = 1;//ƥ���ͬʱ�����չ�Ӧ�̡�ú��Ʒ�ֽ���ƥ�䣬�����Է�վ����
	private static final int Ignore_Pinz = 2;//ƥ���ͬʱ�����չ�Ӧ�̡�ú�󡢷�վ����ƥ�䣬������Ʒ������
	private static final int Ignore_Meik = 3;//ƥ���ͬʱ�����չ�Ӧ�̡���վ��Ʒ�ֽ���ƥ�䣬������ú������
	private static final int Ignore_Faz_Pinz = 4;//ƥ���ͬʱ�����չ�Ӧ�̡�ú�����ƥ�䣬���Է�վ��Ʒ������
	private static final int Ignore_Meik_Faz = 5;//ƥ���ͬʱ�����չ�Ӧ�̡�Ʒ�ֽ���ƥ�䣬���Է�վ��ú������
	private static final int Ignore_Meik_Pinz = 6;//ƥ���ͬʱ�����չ�Ӧ�̡���վ����ƥ�䣬����Ʒ�֡�ú������
	private static final int Ignore_All = 7;//ƥ���ͬʱ����ƥ�乩Ӧ�̣����������������
	
	public Chengbjs() {
		// TODO �Զ����ɹ��캯�����
	}
	public static void CountChengb(long diancxxb_id,List fhlist,boolean qiangzcs) {
//		String strzglb="�й�����";
//		strzglb=MainGlobal.getXitxx_item("�ɱ�", "�ݹ��㷨����", String.valueOf(diancxxb_id), strzglb).trim();
		String strzglb="���ƹ���";
		
		
		JDBCcon con = new JDBCcon();
//		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
//			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
//			if(ruccbb_id != null) {
//				Jilcz.addFahid(list, ruccbb_id);
//			}
		}
		con.Close();
		//if(strzglb.equals("���ƹ���")){
			
			Chengbcl.CountCb_PerFah(fhlist, true);
		//}		
	}
	
	
	public static void CountChengb(JDBCcon con,long diancxxb_id,List fhlist,boolean qiangzcs) {
//		String strzglb="�й�����";
//		strzglb=MainGlobal.getXitxx_item("�ɱ�", "�ݹ��㷨����", String.valueOf(diancxxb_id), strzglb).trim();
		String strzglb="���ƹ���";
		if(con==null){
			 con = new JDBCcon();
		}
//		JDBCcon con = new JDBCcon();
//		List list = new ArrayList();
		for(int i =0;i<fhlist.size();i++) {
			String fhid = (String)fhlist.get(i);
			long htid = UpdateFahHt(con,diancxxb_id,fhid);
//			String ruccbb_id = saveRuccb(con,diancxxb_id,fhid,htid);
//			if(ruccbb_id != null) {
//				Jilcz.addFahid(list, ruccbb_id);
//			}
		}
	//	con.Close();
//		if(strzglb.equals("�й�����")){
//			
//		}else if(strzglb.equals("���ƹ���")){
//			Chengbcl.CountCb_PerFah(fhlist, qiangzcs);
//		}	
		if(strzglb.equals("���ƹ���")){
			
			Chengbcl.CountCb_PerFah(fhlist, true);
		}
//		��Ϊ����Ķ���ɳɱ�ģ�����BUG 2008-11-04
//		Chengbcl.CountCb(list, diancxxb_id, 0);
	}
	public static void CountChengb(JDBCcon con,long diancxxb_id,List fhlist) {
		CountChengb(con,diancxxb_id,fhlist,false);
	}
	
	public static void CountChengb(long diancxxb_id,List fhlist) {
		CountChengb(diancxxb_id,fhlist,false);
	}
	
	
	public static long UpdateFahHt(JDBCcon con,long diancxxb_id,String fahbid) {
//		�������ã�ΪҪ����ɱ���fahb��¼�ҵ�����ʵĺ�ͬ��Ϣ
		long htid = 0;	//�������ʱ�Ѿ��ҵ���hetid
		long newhtid = 0;
		StringBuffer sb = new StringBuffer();
//		1��ȡ�����з����ĺ�ͬ��id
		sb.append("select hetb_id from fahb where id=").append(fahbid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()) {
			htid = rsl.getLong("hetb_id");
		}
		rsl.close();
//		2011��6��24�� Qiuzw ���ݷ�����ͬά������Ϣƥ���ͬ
		for (int i = noIgnore; i <= Ignore_All; i++) {
			if (newhtid == 0) {
				rsl = con.getResultSetList(getHtidSql(i, fahbid));
				if (rsl.next()) {
					newhtid = rsl.getLong("hetb_id");
					if (newhtid != 0)
						break;
				}
				rsl.close();
			} else
				break;
		}
		
//		����������е�hetb_id��������Ϊƥ���hetb_id������Ϊָ��ƥ��ĺ�ͬ��idΪ׼
		if(newhtid != htid){
			if (newhtid!=0){
				//���ԭ�еķ����к�ͬ����û��ƥ�䵽�º�ͬ�򲻸���
				con.getUpdate("update fahb set hetb_id = "+newhtid+" where id="+fahbid);
			}
		}
		return newhtid;
	}
	
	public static String saveRuccb(JDBCcon con,long diancxxb_id,String fhid,long htid) {
//		�������ã��õ�Ҫ�����볧�ɱ��ķ�����Ϣ��Ӧ���볧�ɱ���id
		StringBuffer sb = new StringBuffer();
		String ruccbb_id = null;
		String oraDate = null;
		String zhilb_id = null;
//		�������������볧�ɱ�
		sb.append("select hetb_id,daohrq,zhilb_id from fahb where id = " + fhid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl.next()){
			oraDate = DateUtil.FormatOracleDate(rsl.getDateString("daohrq"));
			zhilb_id = rsl.getString("zhilb_id");
		}
		rsl.close();
		if(oraDate != null && zhilb_id !=null){
			sb.delete(0, sb.length());
			sb.append("select ruccbb_id from fahb where daohrq =").append(oraDate)
			.append(" and zhilb_id=").append(zhilb_id).append(" and hetb_id=")
			.append(htid);
			rsl = con.getResultSetList(sb.toString());
			if(rsl.next()) {
				ruccbb_id = rsl.getString(0);
			}
		}
//		1���ҵ���˷���ͬһ�쵽������ʹ��ͬһ����ͬ�ķ�����Ϣ��������Ϊ��Щ����Ӧ��Ӧͬһ���볧�ɱ����ҵ�����볧�ɱ���id����
//		if(htid > 0){
//			sb.append("select ruccbb_id from fahb where to_char(daohrq,'yyyy-mm') =(select to_char(daohrq,'yyyy-mm') from fahb where id=")
//			.append(fhid).append(") and hetb_id=").append(htid);
//			ResultSetList 
//		}
		if(ruccbb_id == null || "".equals(ruccbb_id)) {
//			�������û�еõ��볧�ɱ���id����Ҫ�²����볧�ɱ���Ϣ
			sb.delete(0, sb.length());
			ruccbb_id = MainGlobal.getNewID(diancxxb_id);
			sb.append("insert into ruccb (id,fahb_id)")
			.append("values(").append(ruccbb_id)
			.append(",").append(fhid).append(")");
			int flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				WriteLog.writeErrorLog("�����볧�ɱ�ʱ��������");
			}
		}
		if(ruccbb_id != null && !"0".equals(ruccbb_id)) {
//			���·�����Ϣ���볧�ɱ���id
			sb.delete(0, sb.length());
			sb.append("update fahb set ruccbb_id=")
			.append(ruccbb_id)
			.append(" where id=").append(fhid);
			con.getUpdate(sb.toString());
			return ruccbb_id;
		}
		return null;
	}
	
	public static String getHtidSql(int IntModel,String fahid) {
		//����hetb�����ӣ�����ƥ�䵽һ����ɾ���ĺ�ͬ
		StringBuffer sb = new StringBuffer();
		sb.append("select h.hetb_id from fahb f, fahhtb h,hetb ht\n")
		.append("where f.id =").append(fahid).append("\n")
		.append("and f.gongysb_id = h.gongysb_id\n")
		.append("and f.diancxxb_id = h.diancxxb_id and h.hetb_id=ht.id \n");
		switch(IntModel) {
			case Ignore_Meik:
				//������ú������
				sb.append("and h.meikxxb_id = -1 and h.faz_id = f.faz_id and h.pinzb_id = f.pinzb_id\n");
				break;
			case Ignore_Pinz:
				//������Ʒ�ֵ����
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.pinzb_id = -1 and h.faz_id = f.faz_id\n");
				break;
			case Ignore_Faz:
				//�����Է�վ�����
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.pinzb_id = f.pinzb_id and h.faz_id = -1\n");
				break;
			case Ignore_Faz_Pinz:
				//ͬʱ���Է�վ��Ʒ��
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = -1 and h.pinzb_id = -1\n");
				break;
			case Ignore_Meik_Faz:
				//ͬʱ����ú�󡢷�վ
				sb.append("and h.meikxxb_id = -1 and h.pinzb_id = f.pinzb_id and h.faz_id = -1\n");
				break;
			case Ignore_Meik_Pinz:
				//ͬʱ����ú��Ʒ��
				sb.append("and h.meikxxb_id = -1 and h.pinzb_id = -1 and h.faz_id = f.faz_id\n");
				break;
			case noIgnore:
				//���ϸ�ƥ�䣬����ȫ������ƥ��
				sb.append("and h.meikxxb_id = f.meikxxb_id and h.faz_id = f.faz_id and h.pinzb_id = f.pinzb_id\n");
				break;
			case Ignore_All:
				break;
			default:
				//��ͬ��Ignore_All
				break;
		}
		sb.append(" and f.fahrq >= h.qisrq and f.fahrq <= h.jiesrq  Order By h.qisrq Desc, h.jiesrq ");
		return sb.toString();
	}
}