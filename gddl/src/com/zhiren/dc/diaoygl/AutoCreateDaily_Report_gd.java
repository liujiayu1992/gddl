package com.zhiren.dc.diaoygl;

import java.util.Date;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;

public class AutoCreateDaily_Report_gd {

/*
 *		���պĴ�¼������п��ļ��㷽��Ϊ��
 *			�ڳ���棺�ӵ����±���ȡ�ñ��µ��ڳ���棬����±�����������ȡ�ձ������µ׿��
 *			��棺�ڳ����+�����루��ֹ����ѡ���ڵĽ�ú������-�ܺ��ã���ֹ����ѡ���ڵĺ���������
*/
/*
 * ���ߣ����
 * ʱ�䣺2011-11-25
 * ʹ�÷�Χ�������������������λ
 * ��������������ȡ�÷��������պĴ�ϼ���ȡ�����¿��������
 * 		 ����ϵͳ�����ձ�����Ƿ���±���ȡ�á���Ĭ��ֵΪ��	
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-28
 * ʹ�÷�Χ�������������������λ
 * �������������ݼ�������ӶԵ��տ����ݵ��жϣ����Ҷ������ӵ���û����úʱ����ʾ�չ����޶�Ӧ���ݵĴ���
 */	
/*
 * ���ߣ����
 * ʱ�䣺2011-11-29
 * ʹ�÷�Χ�������������������λ
 * �����������պĴ��ձ�ʵʱ���¿�湦�ܣ�������ĳһ�տ����������֮��Ŀ����Ϣ��
 * 		 ������ʱ���µ��յ���ú��-������Ϣ�������������ں�Ŀ����
 */		
/*
 * ���ߣ����
 * ʱ�䣺2011-11-30
 * ʹ�÷�Χ�������������������λ
 * �������������պĴ��ձ�¼����������ɷ����жԺ�����Ϣ�Ƿ���µ��ж��߼��쳣
 */	
/*
 * ���ߣ����
 * ʱ�䣺2011-12-13
 * ʹ�÷�Χ�������������������λ
 * ������������ʾ����ÿ����ʾ3��������Ϣ
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-12-20
 * ���÷�Χ�������������������λ
 * ������ʹ�ò����������ձ�ʱ������Ϣ�����жϣ��Ƿ�ֻȡ�÷�������Ƥ����Ϣ��������Ϊ������ú��Ϣ��	
 * 		�����չ��۵�����ͳ�Ʒ�ʽ
 * 		ʹ���պĴ�ֿ����ɷ����С��պĴ��ձ���ú�Զ����ǡ��������պĴ��ձ�����ú������Ʊ�أ����𣬿��ֵ���Ϣ��������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-03-13
 * ���÷�Χ�������������������λ
 * �������޸ķֿ�������ú�ۺ��˼۵�ȡֵ��ʽ��ֻ�е���ֵȡ����Ϣʱ��д��ú�ۺ��˼���Ϣ��
 */	
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2012-04-18
 * ���÷�Χ�������������������λ
 * �������޸�2012-03-13��BUG��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-06-04
 * ���÷�Χ�������������������λ
 * �������������ڵ�������ˮ�ֲ����¼����ϢΪ�������µ����ɴ���
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-06-27
 * ���÷�Χ�������糧
 * ������Ϊ�����糧������Ӧ�ı��淽���ͻ�ȡ����
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-02
 * ���÷�Χ���������
 * ����������"�պĴ��ձ������Ƿ񵥶��ж�","��ú�Ƿ����ֳ���"���ж�����	
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-11
 * ���÷�Χ���������
 * ����������������λС�����µ�������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-08-10
 * ���÷�Χ������������������糧��������ׯ�Ӻͺ�����
 * ���������ӳ���ú����ú���У�����ú����ú����Ӱ���档
 */	
/*
 * ���ߣ����
 * ʱ�䣺2012-09-26
 * ���÷�Χ������������������糧��������ׯ�Ӻͺ�����
 * ����������RPChk��鷽����ú�ۺ��˼۵ļ�鷽ʽ
 * 		����ChkFKB��鷽����ú�ۺ��˼۵ļ�鷽ʽ
 */	
/*
 * ���ߣ����
 * ʱ�䣺2012-10-29
 * ���÷�Χ������������������糧��������ׯ�ӣ�
 * �����������ɵ�������δ�����BUG
 */	
/*
 * ����:���
 * ����:2013-03-25
 * �޸�����:���������ĵ�λ���Ϊ��ǧ��ʱ�����������ɷ�����
 */
/*
 * ����:���
 * ����:2013-07-18
 * �޸�����:��ϵͳ���Զ���ù�������Ϣ
 */		
/*
 * ����:���
 * ����:2013-09-26
 * �޸�����:ú�ۺ��˼۽����ӹ���ȡ��Ĭ��Ϊ0��
 * 			����ʱ����ú��˰���˼�˰�����˼ۣ����˼�˰�������˼ۣ������˼�˰
 */
/*
 * ����:���
 * ����:2013-10-16
 * �޸�����:���ɷֿ�����ʱ�������䷽ʽ
 */		
	public String CreateRBB(JDBCcon con, long dcid, Date riq) {
//		��������
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		int flag=0;//���³ɹ���ʶ
//		�պĴ��ձ��е��ֶα���
		double zuorkc = 0.0, fadl = 0.0,GONGRL=0.0, jingz = 0.0, biaoz = 0.0, yuns = 0.0, yingd = 0.0;
		double kuid = 0.0, fady = 0.0, gongry = 0.0, qity = 0.0, cuns = 0.0, tiaozl = 0.0;
		double shuifctz = 0.0, panyk = 0.0, kuc = 0.0, feiscy = 0.0,changwml=0.0,bukdml=0.0,kedkc=0.0;
		StringBuffer sb = new StringBuffer();

		sb.append("select sum(fadl)/10000 fadl,sum(GONGRL) GONGRL from riscsjb where riq = ").append(
				CurOraDate).append(" and diancxxb_id = ").append(dcid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fadl = rsl.getDouble("fadl");
			GONGRL=rsl.getDouble("GONGRL");
		}
		rsl.close();

		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";

		sb.delete(0, sb.length());
		String whereCondition="";
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�Ƿ��жϷ���Ƥ����Ϣ", "0", "��").equals("��")){
			whereCondition=" and piz>0 \n";
		}
		sb.append("select nvl("+roundFuncName+"jingz-koud"+roundSacle+",0) jingz, nvl("+roundFuncName+"biaoz"+roundSacle+",0) biaoz, nvl("+roundFuncName+"yuns"+roundSacle+",0) yuns,\n")
			.append("nvl("+roundFuncName+"yingd"+roundSacle+",0) yingd, nvl("+roundFuncName+"yingd-yingk"+roundSacle+",0) kuid \n")
			.append("from fahb where daohrq =")
			.append(CurOraDate)
			.append(" "+whereCondition);
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "��ú�Ƿ����ֳ���", "0", "��").equals("��")){
			sb.append(" and diancxxb_id=").append(dcid);
		}

		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			jingz = rsl.getDouble("jingz");
			biaoz = rsl.getDouble("biaoz");
			yuns = rsl.getDouble("yuns");
			yingd = rsl.getDouble("yingd");
			kuid = rsl.getDouble("kuid");
		}
		rsl.close();
		// ��ú��������������
		sb.delete(0, sb.length());
		sb
				.append(
						"select nvl(sum(fadhy),0) fady, nvl(sum(gongrhy),0) gongry, nvl(sum(qity),0) qity,nvl(sum(feiscy),0) feiscy from meihyb\n")
				.append("where rulrq =").append(CurOraDate).append(
						" and diancxxb_id =").append(dcid);
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fady = rsl.getDouble("fady");
			gongry = rsl.getDouble("gongry");
			qity = rsl.getDouble("qity");
			feiscy = rsl.getDouble("feiscy");
		}
		rsl.close();
		
// 		�������տ��
//		���������㷽�����ڳ����+������-�ܺ���
//		ȡ������ĩ�ձ����
		sb.delete(0, sb.length());
		sb.append("SELECT KUC FROM SHOUHCRBB\n" +
				"WHERE DIANCXXB_ID = "+dcid+"\n" + 
				"AND RIQ = LAST_DAY(ADD_MONTHS("+CurOraDate+", -1))");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = rsl.getDouble("kuc");
		}
		rsl.close();

		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�ձ�����Ƿ���±���ȡ��", "0", "��").equals("��")){
			sb.delete(0, sb.length());
//			���ԴӺĴ�ϼ���ȡ�������±����
			sb.append("SELECT KUC\n" +
					"  FROM YUESHCHJB TJ\n" + 
					" WHERE TJ.RIQ =FIRST_DAY(ADD_MONTHS("+CurOraDate+", -1))\n" + 
					"   AND TJ.DIANCXXB_ID = "+dcid+"\n" + 
					"   AND FENX = '����'");
			rsl = con.getResultSetList(sb.toString());
			if (rsl.next()) {
				zuorkc = rsl.getDouble("kuc");
			}
			rsl.close();
		}

//		ȡ�����տ��+���տ���
		sb.delete(0, sb.length());
		sb.append("SELECT SUM(DANGRGM)+SUM(changwml) - SUM(FADY) - SUM(GONGRY) - SUM(QITY) - SUM(FEISCY) -\n" +
		"       SUM(CUNS) + SUM(TIAOZL) + SUM(SHUIFCTZ) + SUM(PANYK) KCC\n" + 
		"  FROM SHOUHCRBB WHERE DIANCXXB_ID = "+dcid+"\n" + 
		"   AND RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+" - 1");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = zuorkc+rsl.getDouble("KCC");
		}
		rsl.close();
//		ȡ�����ղ��ɵ�ú��
		sb.delete(0, sb.length());
		sb.append("SELECT BUKDML FROM SHOUHCRBB WHERE DIANCXXB_ID = "+dcid+" AND RIQ = "+CurOraDate+" - 1");
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			bukdml = rsl.getDouble("BUKDML");
		}
		rsl.close();

		// ������
		kuc = zuorkc + jingz - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk;
		// ����ɵ����
		kedkc=kuc-bukdml;
		// �鿴�����Ƿ��Ѿ��������Ĵ��ձ�
		sb.delete(0, sb.length());
		sb.append("select * from shouhcrbb where diancxxb_id="+dcid+" and riq = "+CurOraDate);
		rsl = con.getResultSetList(sb.toString());
		
//		�ձ���Ϣֻ���²������ݣ���������Ϣ�Ƿ����0�������0�򲻽��и��¡�
		sb.delete(0, sb.length());
		if (rsl.next()) {
			// ������պĴ���������������жԱ� �в��������
			boolean changed = false;
//			����������������򲻸��·�������Ϣ
			if(fadl!=rsl.getDouble("fadl")){
				changed = true;
				if (rsl.getDouble("fadl")>0) {
					fadl=rsl.getDouble("fadl");
				}
			}
			
			if(GONGRL!=rsl.getDouble("GONGRL")){
				changed = true;
				if (rsl.getDouble("GONGRL")>0) {
					GONGRL=rsl.getDouble("GONGRL");
				}
			}


//			�����չ�����ú�Զ�����������Ϣ�жϣ�Ĭ��ϵͳ�Զ�����������Ϣ��
			boolean Laimqz=MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�Զ�����", "0", "��").equals("��");
			
			if (jingz != rsl.getDouble("jingz")) {
				changed = true;
				if(!Laimqz){
					jingz=rsl.getDouble("jingz");
				}
			}
			if (biaoz != rsl.getDouble("biaoz")) {
				changed = true;
				if(!Laimqz){
					biaoz=rsl.getDouble("biaoz");
				}
			}
			if (yuns != rsl.getDouble("yuns")) {
				changed = true;
				if(!Laimqz){
					yuns=rsl.getDouble("yuns");
				}
			}
			if (yingd != rsl.getDouble("yingd")) {
				changed = true;
				if(!Laimqz){
					yingd=rsl.getDouble("yingd");
				}
			}
			if (kuid != rsl.getDouble("kuid")) {
				changed = true;
				if(!Laimqz){
					kuid=rsl.getDouble("kuid");
				}
			}
//			�����������������Ϣ���򲻸���
			if(fady!=rsl.getDouble("fady")){
				changed = true;
				if (rsl.getDouble("fady")>0) {
					fady=rsl.getDouble("fady");
				}
			}

//			������Ⱥ�������Ϣ���򲻸���
			if(gongry!=rsl.getDouble("gongry")){
				changed = true;
				if (rsl.getDouble("gongry")>0) {
					gongry=rsl.getDouble("gongry");
				}
			}

//			���������������Ϣ���򲻸���
			if(qity!=rsl.getDouble("qity")){
				changed = true;	
				if (rsl.getDouble("qity")>0) {
					qity = rsl.getDouble("qity");
				}
			}
			
//			���������������Ϣ���򲻸���
			if(feiscy!=rsl.getDouble("feiscy")){
				changed = true;
				if (rsl.getDouble("feiscy")>0) {
					feiscy = rsl.getDouble("feiscy");
				}
			}
			
//			������磬���ȣ�������������������һ�������ݣ���ô�������Ǹ�������
			if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ������Ƿ񵥶��ж�", "0", "��").equals("��")){
				if(rsl.getDouble("fady")+rsl.getDouble("gongry")+rsl.getDouble("qity")+rsl.getDouble("feiscy")>0){
					fady=rsl.getDouble("fady");
					gongry=rsl.getDouble("gongry");
					qity = rsl.getDouble("qity");
					feiscy = rsl.getDouble("feiscy");
				}
			}

//			�����������Ϣ���򲻸���
			if(cuns!=rsl.getDouble("cuns")){
				changed = true;
				if (rsl.getDouble("cuns")>0) {
					cuns = rsl.getDouble("cuns");
				}
			}
			
//			�������������Ϣ���򲻸���
			if(tiaozl!=rsl.getDouble("tiaozl")){
				changed = true;
				tiaozl = rsl.getDouble("tiaozl");
			}

//			���ˮ�ֲ��������Ϣ���򲻸���
			if(shuifctz!=rsl.getDouble("shuifctz")){
				changed = true;
				shuifctz = rsl.getDouble("shuifctz");
			}

//			�����ӯ������Ϣ���򲻸���
			if(panyk!=rsl.getDouble("panyk")){
				changed = true;
				panyk = rsl.getDouble("panyk");
			}
			
//			������ɵ�ú������Ϣ���򲻸���
			if(bukdml!=rsl.getDouble("bukdml")){
				changed = true;
				bukdml = rsl.getDouble("bukdml");
			}
//			�������ú������Ϣ���򲻸���
			if(changwml!=rsl.getDouble("changwml")){
				changed = true;
				changwml = rsl.getDouble("changwml");
			}

			if (rsl.getDouble("kedml")>0){
				changed = true;
				kedkc=rsl.getDouble("kedml");
			}
//			���¼�����
			kuc = zuorkc + jingz + changwml - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk;
//			���¼���ɵ����
			kedkc=kuc-bukdml;
//			����б仯������պĴ��ձ���
			if (changed) {
				sb.append("update shouhcrbb set ")
				.append("fadl=").append(fadl)
				.append(",GONGRL=").append(GONGRL)
				.append(",jingz=").append(jingz)
				.append(",biaoz=").append(biaoz)
				.append(",yuns=").append(yuns)
				.append(",yingd=").append(yingd)
				.append(",kuid=").append(kuid)
				.append(",fady=").append(fady)
				.append(",gongry=").append(gongry)
				.append(",qity=").append(qity)
				.append(",cuns=").append(cuns)
				.append(",tiaozl=").append(tiaozl)
				.append(",shuifctz=").append(shuifctz)
				.append(",panyk=").append(panyk)
				.append(",dangrgm=").append(jingz)
				.append(",haoyqkdr=").append(fady + gongry + qity + cuns)
				.append(",feiscy=").append(feiscy)
				.append(",bukdml=").append(bukdml)
				.append(",changwml=").append(changwml)
				.append(",kedkc=").append(kedkc)
				.append(",kuc=").append(kuc)
				.append(" where id =").append(rsl.getString("id"));
				flag=con.getUpdate(sb.toString());
				if(flag==-1){
					return "�պĴ���Ϣ����ʧ��";
				}
				double kuccha = CustomMaths.sub(kuc, rsl.getDouble("kuc"));
//				�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
				if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ�ʵʱ���¿��", "0", "��").equals("��")){
//				���µ�ǰ�����Ժ�����п��Ϳɵ����
					sb.delete(0, sb.length());
					sb.append("begin \n");
					sb.append("update shouhcrbb set ");
					sb.append("kuc = kuc + ").append(kuccha+"\n");
					sb.append(" where riq > ").append(CurOraDate);
					sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
					sb.append("update shouhcrbb set ");
					sb.append("kedkc = kuc - bukdml").append("\n");
					sb.append(" where riq > ").append(CurOraDate);
					sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
					sb.append("end;");
					flag=con.getUpdate(sb.toString());
					if(flag==-1){
						return "�պĴ�����Ϣ����ʧ��";
					}
				}
			}
		} else {
			// ������պĴ���û�����������
			sb.append("insert into shouhcrbb(id, diancxxb_id, riq, fadl, jingz, biaoz, yuns,\n")
			.append("yingd, kuid, fady, gongry, qity, cuns, tiaozl, shuifctz, panyk, kuc, dangrgm," +
					" haoyqkdr,feiscy,bukdml,changwml,kedkc,GONGRL) values(")
			.append("getnewid(").append(dcid).append("),").append(dcid)
			.append(",").append(CurOraDate).append(",").append(fadl)
			.append(",").append(jingz).append(",").append(biaoz)
			.append(",").append(yuns).append(",").append(yingd).append(",")
			.append(kuid).append(",").append(fady).append(",")
			.append(gongry).append(",").append(qity)
			.append(",").append(cuns).append(",").append(tiaozl)
			.append(",").append(shuifctz).append(",").append(panyk)
			.append(",").append(kuc).append(",").append(jingz).append(",")
			.append(fady + gongry + qity + cuns)
			.append(",").append(feiscy)
			.append(",").append(bukdml)
			.append(",").append(changwml)
			.append(",").append(kedkc)
			.append(",").append(GONGRL).append(")");
			flag=con.getInsert(sb.toString());
			if(flag==-1){
				return "�պĴ���Ϣ����ʧ��";
			}
//			�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
			if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ�ʵʱ���¿��", "0", "��").equals("��")){
//			���µ�ǰ�����Ժ�����п��Ϳɵ����
				sb.delete(0, sb.length());
				sb.append("begin \n");
				sb.append("update shouhcrbb set ");
				sb.append("kuc = kuc + ").append(jingz - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk);
				sb.append(" where riq > ").append(CurOraDate);
				sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
				sb.append("update shouhcrbb set ");
				sb.append("kedkc = kuc - bukdml");
				sb.append(" where riq > ").append(CurOraDate);
				sb.append(" and diancxxb_id = ").append(dcid).append(";\n");
				sb.append("end;");
				
				flag=con.getUpdate(sb.toString());
				if(flag==-1){
					return "�պĴ�����Ϣ����ʧ��";
				}
			}
		}
		rsl.close();
		return "";
	}
//	���ɷֿ���Ϣ
	public String CreateFCB(JDBCcon con, long dcid, Date riq)  {
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		ResultSetList list=getShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();
		boolean isTrue=false;
//		�����չ�����ú�Զ�����������Ϣ�жϣ�Ĭ��ϵͳ�Զ�����������Ϣ��
		boolean Laimqz=MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�Զ�����", "0", "��").equals("��");
		boolean Rezqz=MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ֵ�Զ�����", "0", "��").equals("��");
		
		insertSql.append("begin \n");
//		�ж��Ƿ����ԭʼ��¼
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double laimsl=list.getDouble("laimsl");
					double rez=list.getDouble("REZ");
//					�ж��Ƿ����ú��Ϣ���и���
					if(!Laimqz){
						laimsl=OLDlist.getDouble("laimsl");
					}
//					�ж��Ƿ����ֵ��Ϣ���и���
					if(!Rezqz){
						rez=OLDlist.getDouble("rez");
					}
//					���ú��Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					����˼�Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					�����ֵΪ0��ôú�ۺ��˼ۼ����Ӧ��˰���Ϊ0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) 
						&& list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
						&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) 
						&& list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
						&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						insertSql.append(
								"insert into shouhcfkb\n" +
								"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meijs,yunjs,huocyj,huocyjs,qicyj,qicyjs,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
								"values\n" + 
								"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
								laimsl+","+rez+","+meij+","+yunj+","+meijs+","+yunjs+","+huocyj+","+huocyjs+","
								+qicyj+","+qicyjs+","+list.getString("meikxxb_id")+","+
								list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
						isTrue=true;
						break;
					}
				}
				if(!isTrue){
					insertSql.append(
							"insert into shouhcfkb\n" +
							"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
							"values\n" + 
							"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
							list.getDouble("laimsl")+","+list.getDouble("rez")+","+
							list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
							list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
				}
				isTrue=false;
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}else{
			while (list.next()) {
				insertSql.append(
						"insert into shouhcfkb\n" +
						"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
						"values\n" + 
						"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
						list.getDouble("laimsl")+","+list.getDouble("rez")+","+
						list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
						list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
			}
			insertSql.append("end;");
		}

		int re=0;

//		ɾ������
		String delData="delete from shouhcfkb where diancxxb_id="+dcid+" and riq = "+CurOraDate;
		re=con.getDelete(delData);
		if(re==-1){
			OLDlist.close();
			list.close();
			return "�ֿ�����ɾ��ʧ�ܣ�";
		}
//		��������
		if(insertSql.length()>20){
			re=con.getInsert(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "�ֿ���������ʧ�ܣ�";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}
	
	
	//�õ�ԭʼ��Ϣ
	private ResultSetList getOLDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		StringBuffer sql=new StringBuffer();
		sql.append(
				"SELECT SHC.DIANCXXB_ID,\n" +
				"       SHC.REZ,\n" + 
				"       SHC.MEIJ,\n" + 
				"       SHC.YUNJ,\n" + 
				"       SHC.MEIJS,\n" + 
				"       SHC.YUNJS,\n" + 
				"       SHC.HUOCYJ,\n" + 
				"       SHC.HUOCYJS,\n" + 
				"       SHC.QICYJ,\n" + 
				"       SHC.QICYJS,\n" + 
				"       SHC.LAIMSL,\n" + 
				"       SHC.MEIKXXB_ID,\n" + 
				"       SHC.GONGYSB_ID,\n" + 
				"       SHC.JIHKJB_ID,\n" + 
				"       SHC.YUNSFSB_ID,\n" + 
				"       SHC.PINZB_ID\n" + 
				"  FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
				" WHERE SHC.RIQ ="+DateUtil.FormatOracleDate(riq)+"\n" + 
				"   AND SHC.DIANCXXB_ID = DC.ID"+
				"   and dc.id = "+diancxxb_id+" \n" );
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	private ResultSetList getShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";
		String whereCondition="";
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�Ƿ��жϷ���Ƥ����Ϣ", "0", "��").equals("��")){
			whereCondition+=" and f.piz>0 \n";
		}
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "��ú�Ƿ����ֳ���", "0", "��").equals("��")){
			whereCondition+=" and diancxxb_id="+diancxxb_id;
		}

//		��ʱ��ȡú�ۺ��˼۵�ֵ
		StringBuffer sql=new StringBuffer();
		sql.append(
				"select meikxxb_id,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,diancxxb_id,rez,\n" +
//				"		decode(rez,0,0,meij) meij,\n" +
//				"		decode(rez,0,0,yunj) yunj,\n" +
				"		0 meij,\n" +
				"		0 yunj,\n" +
				"		laimsl from(\n" +
				"		select f.meikxxb_id,\n" +
				"       f.gongysb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id,\n" + 
				"		f.diancxxb_id,\n"+
				"       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)),0,0,round_new(sum(z.qnet_ar * f.laimsl) / sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)), 2)) rez,\n" + 
				"       decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum((g.meij + g.meis) * f.laimsl) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)), 2)) meij,\n" + 
				//"       decode(sum(f.laimsl),0,0,round_new(sum((g.yunf + g.yunfs) * f.laimsl) / sum(f.laimsl),2)) yunj,\n" + 
				"		decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum(f.laimsl * decode(g.meij,0,0,(nvl(g.yunf,0) + nvl(g.yunfs,0)))) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)),2)) yunj,\n" + 
				"       nvl("+roundFuncName+"f.laimsl"+roundSacle+", 0) laimsl\n" + 
				"  from fahb f,\n" + 
				"       zhilb z,diancxxb d,\n" + 
				"       (select id, fahb_id, meij, meis, yunf, yunfs\n" + 
				"          from guslsb\n" + 
				"         where id in (select max(id) id\n" + 
				"                        from guslsb g,\n" + 
				"                             (select fahb_id, max(leix) leix\n" + 
				"                                from guslsb\n" + 
				"                               group by fahb_id) g2\n" + 
				"                       where g.fahb_id = g2.fahb_id\n" + 
				"                         and g.leix = g2.leix\n" + 
				"                       group by g.fahb_id)) g\n" + 
				" where f.zhilb_id = z.id(+)\n" + 
				"   and f.id = g.fahb_id(+)\n" + 
				"   and f.diancxxb_id = d.id \n"+
				whereCondition+
				"   and f.daohrq = "+DateUtil.FormatOracleDate(riq)+"\n" + 
				" group by f.diancxxb_id,f.gongysb_id, f.meikxxb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id)");
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	
//	���չ�Ӧ�̣�ú��Ʒ�֣��ƻ��ھ������䷽ʽȡֵ��ʽ���������⣩
//	���ɷֿ���Ϣ
	public String CreateFCB_HD(JDBCcon con, long dcid, Date riq)  {
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		ResultSetList list=getHDShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDHDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();
		boolean isTrue=false;
//		�����չ�����ú�Զ�����������Ϣ�жϣ�Ĭ��ϵͳ�Զ�����������Ϣ��
		boolean Laimqz=MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�Զ�����", "0", "��").equals("��");
		boolean Rezqz=MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ֵ�Զ�����", "0", "��").equals("��");
		
		insertSql.append("begin \n");
//		�ж��Ƿ����ԭʼ��¼
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double laimsl=list.getDouble("laimsl");
					double rez=list.getDouble("REZ");
//					�ж��Ƿ����ú��Ϣ���и���
					if(!Laimqz){
						laimsl=OLDlist.getDouble("laimsl");
					}
//					�ж��Ƿ����ֵ��Ϣ���и���
					if(!Rezqz){
						rez=OLDlist.getDouble("rez");
					}
//					���ú��Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					����˼�Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					�����ֵΪ0��ôú�ۺ��˼ۼ����Ӧ��˰���Ϊ0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) && list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
							&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) && list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
							&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						insertSql.append(
								"insert into shouhcfkb\n" +
								"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meijs,yunjs,huocyj,huocyjs,qicyj,qicyjs,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,yunsfsb_id)\n" + 
								"values\n" + 
								"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
								laimsl+","+rez+","+meij+","+yunj+","
								+meijs+","+yunjs+","+huocyj+","+huocyjs+","+qicyj+","+qicyjs+","
								+list.getString("meikxxb_id")+","+
								list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
						isTrue=true;
						break;
					}
				}
				if(!isTrue){
					insertSql.append(
							"insert into shouhcfkb\n" +
							"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,YUNSFSB_ID)\n" + 
							"values\n" + 
							"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
							list.getDouble("laimsl")+","+list.getDouble("rez")+","+
							list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
							list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
				}
				isTrue=false;
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}else{
			while (list.next()) {
				insertSql.append(
						"insert into shouhcfkb\n" +
						"  (id, diancxxb_id,riq,laimsl, rez, meij, yunj,meikxxb_id,pinzb_id,gongysb_id,JIHKJB_ID,YUNSFSB_ID)\n" + 
						"values\n" + 
						"  (getnewid("+dcid+"),"+dcid+","+CurOraDate+","+
						list.getDouble("laimsl")+","+list.getDouble("rez")+","+
						list.getDouble("meij")+","+list.getDouble("yunj")+","+list.getString("meikxxb_id")+","+
						list.getString("pinzb_id")+","+list.getString("gongysb_id")+","+list.getString("JIHKJB_ID")+","+list.getString("yunsfsb_id")+");\n");
			}
			insertSql.append("end;");
		}

		int re=0;

//		ɾ������
		String delData="delete from shouhcfkb where diancxxb_id="+dcid+" and riq = "+CurOraDate;
		re=con.getDelete(delData);
		if(re==-1){
			OLDlist.close();
			list.close();
			return "�ֿ�����ɾ��ʧ�ܣ�";
		}
//		��������
		if(insertSql.length()>20){
			re=con.getInsert(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "�ֿ���������ʧ�ܣ�";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}
	
//	��ȡ��ֵ������
	public String GetRez(JDBCcon con, long dcid, Date riq)  {
		ResultSetList list=getHDShouhcfk(con, dcid,riq);
		ResultSetList OLDlist=getOLDHDShouhcfk(con, dcid,riq);
		StringBuffer insertSql=new StringBuffer();

		insertSql.append("begin \n");
//		�ж��Ƿ����ԭʼ��¼
		if(OLDlist.getRows()>0){
			while (list.next()) {
				while(OLDlist.next()){
					double rez=list.getDouble("REZ");

//					���ú��Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double meij=OLDlist.getDouble("meij");
//					if(rez>0 && meij==0){
//						meij=list.getDouble("meij");
//					}
//					����˼�Ϊ0����ʹ��ϵͳȡֵ���и��ǲ������������ݽ����ֲ���
					double yunj=OLDlist.getDouble("yunj");
//					if(rez>0 && yunj==0){
//						yunj=list.getDouble("yunj");
//					}
					
					double meijs=OLDlist.getDouble("meijs");
					double yunjs=OLDlist.getDouble("yunjs");
					double huocyj=OLDlist.getDouble("huocyj");
					double huocyjs=OLDlist.getDouble("huocyjs");
					double qicyj=OLDlist.getDouble("qicyj");
					double qicyjs=OLDlist.getDouble("qicyjs");
					
//					�����ֵΪ0��ôú�ۺ��˼ۼ����Ӧ��˰���Ϊ0
					if(rez==0){
						yunj=0;
						meij=0;
						meijs=0;
						yunjs=0;
						huocyj=0;
						huocyjs=0;
						qicyj=0;
						qicyjs=0;
					}
					
					if(list.getString("meikxxb_id").equals(OLDlist.getString("meikxxb_id")) && list.getString("pinzb_id").equals(OLDlist.getString("pinzb_id")) 
							&& list.getString("gongysb_id").equals(OLDlist.getString("gongysb_id")) && list.getString("JIHKJB_ID").equals(OLDlist.getString("JIHKJB_ID"))
							&& list.getString("YUNSFSB_ID").equals(OLDlist.getString("YUNSFSB_ID"))){
						
						insertSql.append("update shouhcfkb set rez="+rez+",\n" +
								" meij="+meij+","+
								" meijs="+meijs+","+
								" yunjs="+yunjs+","+
								" huocyj="+huocyj+","+
								" huocyjs="+huocyjs+","+
								" qicyj="+qicyj+","+
								" qicyjs="+qicyjs+","+
								" yunj="+yunj+" where id="+OLDlist.getString("ID")+";\n");
						break;
					}
				}			
				OLDlist.beforefirst();
			}
			insertSql.append("end;");
		}
		
//		��������
		if(insertSql.length()>20){
			int	re=con.getUpdate(insertSql.toString());
			if(re==-1){
				OLDlist.close();
				list.close();
				return "�ֿ���ֵ��ȡʧ�ܣ�";
			}
		}
		OLDlist.close();
		list.close();
		return "";
	}	
	
//	�õ�ԭʼ��Ϣ
	private ResultSetList getOLDHDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		StringBuffer sql=new StringBuffer();
		sql.append(
				"SELECT SHC.ID,\n" +
				"		SHC.DIANCXXB_ID,\n" +
				"       SHC.REZ,\n" + 
				"       SHC.MEIJ,\n" + 
				"       SHC.YUNJ,\n" + 
				"       SHC.MEIJS,\n" + 
				"       SHC.YUNJS,\n" + 
				"       SHC.HUOCYJ,\n" + 
				"       SHC.HUOCYJS,\n" + 
				"       SHC.QICYJ,\n" + 
				"       SHC.QICYJS,\n" + 
				"       SHC.LAIMSL,\n" + 
				"       SHC.MEIKXXB_ID,\n" + 
				"       SHC.GONGYSB_ID,\n" + 
				"       SHC.JIHKJB_ID,\n" + 
				"       SHC.yunsfsb_id,\n" + 
				"       SHC.PINZB_ID\n" + 
				"  FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
				" WHERE SHC.RIQ ="+DateUtil.FormatOracleDate(riq)+"\n" + 
				"   AND SHC.DIANCXXB_ID = DC.ID"+
				"   and dc.id = "+diancxxb_id+" \n" );
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
	private ResultSetList getHDShouhcfk(JDBCcon con,long diancxxb_id, Date riq){
		String roundFuncName = "sum(round(";
		String roundSacle = ",2))";
		String whereCondition="";
		if(MainGlobal.getXitxx_item("�պĴ��ձ�", "��ú�Ƿ����ֳ���", "0", "��").equals("��")){
			whereCondition+=" and diancxxb_id="+diancxxb_id;
		}
		StringBuffer sql=new StringBuffer();
		sql.append(
				"select meikxxb_id,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,diancxxb_id,rez,\n" +
//				"		decode(rez,0,0,meij) meij,\n" +
//				"		decode(rez,0,0,yunj) yunj,\n" +
				"		0 meij,\n" +
				"		0 yunj,\n" +
				"		laimsl from(\n" +
				"		select f.meikxxb_id,\n" +
				"       f.gongysb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id,\n" + 
				"		f.diancxxb_id,\n"+
				"       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)),0,0,round_new(sum(z.qnet_ar * f.laimsl) / sum(decode(nvl(z.qnet_ar,0),0,0,f.laimsl)), 2)) rez,\n" + 
				"       decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum((g.meij + g.meis) * f.laimsl) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)), 2)) meij,\n" + 
				//"       decode(sum(f.laimsl),0,0,round_new(sum((g.yunf + g.yunfs) * f.laimsl) / sum(f.laimsl),2)) yunj,\n" + 
				"		decode(sum(decode(nvl(g.meij,0),0,0,f.laimsl)),0,0,round_new(sum(f.laimsl * decode(g.meij,0,0,(nvl(g.yunf,0) + nvl(g.yunfs,0)))) / sum(decode(nvl(g.meij,0),0,0,f.laimsl)),2)) yunj,\n" + 
				"       nvl("+roundFuncName+"f.laimsl"+roundSacle+", 0) laimsl\n" + 
				"  from fahb f,\n" + 
				"       zhilb z,diancxxb d,\n" + 
				"       (select id, fahb_id, meij, meis, yunf, yunfs\n" + 
				"          from guslsb\n" + 
				"         where id in (select max(id) id\n" + 
				"                        from guslsb g,\n" + 
				"                             (select fahb_id, max(leix) leix\n" + 
				"                                from guslsb\n" + 
				"                               group by fahb_id) g2\n" + 
				"                       where g.fahb_id = g2.fahb_id\n" + 
				"                         and g.leix = g2.leix\n" + 
				"                       group by g.fahb_id)) g\n" + 
				" where f.zhilb_id = z.id(+)\n" + 
				"   and f.id = g.fahb_id(+)\n" + 
				"   and f.diancxxb_id = d.id \n"+
				whereCondition+"\n" + 
				"   and f.daohrq = "+DateUtil.FormatOracleDate(riq)+"\n" + 
				" group by f.diancxxb_id,f.gongysb_id, f.meikxxb_id,f.pinzb_id,f.JIHKJB_ID,f.yunsfsb_id)");
		ResultSetList list=con.getResultSetList(sql.toString());
		return list;
	}
//	ȡֵ����
	
//	����չ��۷ֿ��
	public String ChkFKB(JDBCcon con, String dcid, Date riq){
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		String sql="SELECT TO_CHAR(SHC.RIQ,'yyyy-mm-dd')RIQ\n" +
			"  FROM SHOUHCFKB SHC\n" + 
			" WHERE SHC.RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+"-1\n" + 
			"   AND SHC.DIANCXXB_ID = "+dcid+"\n" + 
			"   AND (SHC.REZ = 0 OR (SHC.MEIJ = 0 AND SHC.YUNJ=0))\n" + 
			" GROUP BY SHC.RIQ\n" + 
			" ORDER BY SHC.RIQ";
		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			InvalidDay = rsl.getString("RIQ");
		}
		rsl.close();
		return InvalidDay;
	}
	
	
//	����պĴ��ձ���
	public String ChkRBB(JDBCcon con, String dcid, Date riq){
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		String sql="SELECT TO_CHAR(BB.RIQ, 'yyyy-mm-dd') RIQ\n" +
			"  FROM SHOUHCRBB BB\n" + 
			" WHERE BB.RIQ BETWEEN FIRST_DAY("+CurOraDate+") AND "+CurOraDate+"-1\n" + 
			"   AND BB.DIANCXXB_ID = "+dcid+"\n" + 
			"   AND BB.FADY + BB.GONGRY + BB.QITY + BB.FEISCY = 0\n" + 
			" GROUP BY BB.RIQ\n" + 
			" ORDER BY BB.RIQ";

		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			InvalidDay = rsl.getString("RIQ");
		}
		rsl.close();
		return InvalidDay;
	}
	
//	����պĴ��ձ�����չ��۱���
	public String RPChk(JDBCcon con, String dcid, Date riq1, Date riq2){
		String BeginOraDate = DateUtil.FormatOracleDate(riq1); // ��ǰʱ��
		String EndOraDate = DateUtil.FormatOracleDate(riq2); // ��ǰʱ��
		String sql=
			"SELECT TH.DCMC, TH.RIQ, NVL(SJ.RBZHI, -1), NVL(SJ.GM,-1) GM, NVL(SJ.SHCZHI, -1),\n" +
			"DECODE(NVL(SJ.RBZHI, 0),0,0,DECODE(NVL(SJ.SHCZHI, 0),0,DECODE(NVL(SJ.GM,0),0,1,0),1))ck\n" + 
			"  FROM (SELECT DC.DCMC, RIQ.RIQ\n" + 
			"          FROM (SELECT TO_CHAR("+BeginOraDate+" + ROWNUM - 1, 'yyyy-mm-dd') RIQ\n" + 
			"                  FROM USER_OBJECTS\n" + 
			"                 WHERE ROWNUM <= "+EndOraDate+" - "+BeginOraDate+"+1) RIQ,\n" + 
			"               (SELECT MINGC DCMC FROM DIANCXXB WHERE ID IN ("+dcid+")AND ID NOT IN(100,112,300)) DC) TH,\n" + 
			"       (SELECT RB.DCMC, RB.RIQ, RB.ZHI RBZHI, RB.GM, SHC.ZHI SHCZHI\n" + 
			"          FROM (SELECT DCMC, RIQ, MIN(ZHI) ZHI\n" + 
			"                  FROM (SELECT DC.MINGC DCMC,\n" + 
			"                               TO_CHAR(SHC.RIQ, 'yyyy-mm-dd') RIQ,\n" + 
			"                               CASE\n" + 
			"                                 WHEN (SHC.REZ = 0 OR (SHC.MEIJ = 0 AND SHC.YUNJ=0)) THEN\n" + 
			"                                  0\n" + 
			"                                 ELSE\n" + 
			"                                  1\n" + 
			"                               END ZHI\n" + 
			"                          FROM SHOUHCFKB SHC, DIANCXXB DC\n" + 
			"                         WHERE SHC.RIQ BETWEEN "+BeginOraDate+" AND "+EndOraDate+"\n" + 
			"                           AND DC.ID = SHC.DIANCXXB_ID\n" + 
			"                           AND DC.ID IN ("+dcid+"))\n" + 
			"                 GROUP BY DCMC, RIQ) SHC,\n" + 
			"               (SELECT DC.MINGC DCMC,\n" + 
			"                       TO_CHAR(SHC.RIQ, 'yyyy-mm-dd') RIQ,\n" + 
			"                       DECODE(SHC.FADY + SHC.GONGRY + SHC.QITY + SHC.FEISCY, 0, 0, 1) ZHI,\n" + 
			"                       DECODE(SHC.DANGRGM, 0, 0, 1) GM\n" + 
			"                  FROM SHOUHCRBB SHC, DIANCXXB DC\n" + 
			"                 WHERE SHC.RIQ BETWEEN "+BeginOraDate+" AND "+EndOraDate+"\n" + 
			"                   AND DC.ID = SHC.DIANCXXB_ID\n" + 
			"                   AND DC.ID IN ("+dcid+")) RB\n" + 
			"         WHERE RB.DCMC = SHC.DCMC(+)\n" + 
			"           AND RB.RIQ = SHC.RIQ(+)) SJ\n" + 
			" WHERE TH.DCMC = SJ.DCMC(+)\n" + 
			"   AND TH.RIQ = SJ.RIQ(+)\n" + 
			"AND DECODE(NVL(SJ.RBZHI, 0),0,0,DECODE(NVL(SJ.SHCZHI, 0),0,DECODE(NVL(SJ.GM,0),0,1,0),1))=0\n"+
			" ORDER BY TH.DCMC, TH.RIQ";

		String InvalidDay="";
		ResultSetList rsl = con.getResultSetList(sql);
		int i=1;
		while(rsl.next()) {
			if(i%3==0 ){
				InvalidDay += rsl.getString("DCMC")+"��"+rsl.getString("RIQ")+"��,<br>";
			}else{
				InvalidDay += rsl.getString("DCMC")+"��"+rsl.getString("RIQ")+"��, ";
			}
			i++;
		}
		rsl.close();
		return InvalidDay;
	}
	
}
