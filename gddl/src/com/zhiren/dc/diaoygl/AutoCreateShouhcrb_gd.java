package com.zhiren.dc.diaoygl;

import java.util.Date;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;

/*
 * ���ߣ���衻�
 * ʱ�䣺2010-8-19 15:03:41
 * �����������ڹ���Shouhcrbb_gd.java ʵ����Ҫ���ܣ����ɵ�ú��ȡ������һ�����ֵ���ɵ����=���-���ɵ�ú��
 */

/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺2010-09-21
 * �޸����ݣ����ò�����ú����ͺ�ȡ��
 * 
 *     INSERT INTO xitxxb VALUES(
        getnewid(dcid),
        1,
        dcid,
        '�պĴ��ձ���ú���Ƿ�����ͺ�ȡ��',
        '��',
        '',
        '����',
        1,
        'ʹ��')
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-1
 * ���÷�Χ�������������������λ
 * �������Է�������ú��Ϣ������Լ�ұ���2λС��
 */

public class AutoCreateShouhcrb_gd {
	public static void Create(JDBCcon con, long dcid, Date riq) {
		Create(con,dcid,riq,false,false);
	}
	public static void Create(JDBCcon con, long dcid, Date riq,boolean autoCommit) {
		Create(con,dcid,riq,autoCommit,false);
	}
	public static void Create(JDBCcon con, long dcid, Date riq, boolean autoCommit, boolean Fenczc) {
/*		if(!autoCommit&&!con.getHasIt("select zhi,danw,beiz from xitxxb where mingc = '�Զ������պĴ�' and zhuangt = 1 and diancxxb_id=" + dcid)){
//			ϵͳ��û�������Զ������պĴ��ձ�
			return;
		}
		// ��������
		String CurOraDate = DateUtil.FormatOracleDate(riq); // ��ǰʱ��
		String LastOraDate = DateUtil.FormatOracleDate(DateUtil.AddDate(riq,
				-1, DateUtil.AddType_intDay)); // ��ǰʱ���ǰһ��
		// �պĴ��ձ��е��ֶα���
		double zuorkc = 0.0, fadl = 0.0, jingz = 0.0, biaoz = 0.0, yuns = 0.0, yingd = 0.0, kuid = 0.0, fady = 0.0, gongry = 0.0, qity = 0.0, cuns = 0.0, tiaozl = 0.0, shuifctz = 0.0, panyk = 0.0, kuc = 0.0, feiscy = 0.0,changwml=0.0,bukdml=0.0,kedkc=0.0;
		StringBuffer sb = new StringBuffer();
		// �������տ��
		sb.append("select kuc,bukdml from shouhcrbb where diancxxb_id=").append(dcid)
				.append(" and riq = ").append(LastOraDate);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			zuorkc = rsl.getDouble("kuc");
			bukdml=rsl.getDouble("bukdml");
		}
		rsl.close();
		// �������������м��㷢����
		sb.delete(0, sb.length());
		sb.append("select sum(fadl) fadl from riscsjb where riq = ").append(
				CurOraDate).append(" and diancxxb_id = ").append(dcid);
		rsl = con.getResultSetList(sb.toString());
		if (rsl.next()) {
			fadl = rsl.getDouble("fadl");
		}
		rsl.close();
		// �ӷ����м�����ú�������ú����
//		String sql = "select * from xitxxb where mingc='�պĴ��ձ���ú���Ƿ���Լ' " +
//				"and zhuangt=1 and diancxxb_id =" + dcid +" and zhi ='��'";
		String roundFuncName = "round_new(sum(";
		String roundSacle = "),2)";
//		if(con.getHasIt(sql)){
//			roundFuncName = "sum(round_new(" ;
//			roundSacle = ",0))";
//		}
//		
//		sql = "select * from xitxxb where mingc='�պĴ��ձ���ú���Ƿ�����ͺ�ȡ��' " +
//		"and zhuangt=1 and diancxxb_id =" + dcid +" and zhi ='��'";
//		if(con.getHasIt(sql)){
//			roundFuncName = "round_new(sum(" ;
//			roundSacle = "),0)";
//		}
		sb.delete(0, sb.length());
		if(Fenczc){
			sb
			.append(
					"select nvl("+roundFuncName+"laimsl"+roundSacle+",0) jingz, nvl("+roundFuncName+"biaoz"+roundSacle+",0) biaoz, nvl("+roundFuncName+"yuns"+roundSacle+",0) yuns,\n")
			.append(
					"nvl("+roundFuncName+"yingd"+roundSacle+",0) yingd, nvl("+roundFuncName+"yingd-yingk"+roundSacle+",0) kuid \n")
			.append("from fahb,diancxxb d where daohrq =").append(CurOraDate).append(
					" and diancxxb_id=d.id and d.fuid =").append(dcid);
		}else{
			sb
				.append(
						"select nvl("+roundFuncName+"laimsl"+roundSacle+",0) jingz, nvl("+roundFuncName+"biaoz"+roundSacle+",0) biaoz, nvl("+roundFuncName+"yuns"+roundSacle+",0) yuns,\n")
				.append(
						"nvl("+roundFuncName+"yingd"+roundSacle+",0) yingd, nvl("+roundFuncName+"yingd-yingk"+roundSacle+",0) kuid \n")
				.append("from fahb where daohrq =").append(CurOraDate).append(
						" and diancxxb_id=").append(dcid);
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
		// ������
		kuc = zuorkc + jingz - fady - gongry - qity - cuns - feiscy + tiaozl
				+ shuifctz + panyk;
		// ����ɵ����
		kedkc=kuc-bukdml;
		// �鿴�����Ƿ��Ѿ��������Ĵ��ձ�
		sb.delete(0, sb.length());
		sb.append("select * from shouhcrbb where diancxxb_id=").append(dcid)
				.append(" and riq = ").append(CurOraDate);
		rsl = con.getResultSetList(sb.toString());
		sb.delete(0, sb.length());
		if (rsl.next()) {
			// ������պĴ���������������жԱ� �в��������
			boolean changed = false;
//			if (zuorkc != rsl.getDouble("zuorkc")) {
//				changed = true;
//			}
			if (fadl != rsl.getDouble("fadl")) {
				changed = true;
			}
			if (jingz != rsl.getDouble("jingz")) {
				changed = true;
			}
			if (biaoz != rsl.getDouble("biaoz")) {
				changed = true;
			}
			if (yuns != rsl.getDouble("yuns")) {
				changed = true;
			}
			if (yingd != rsl.getDouble("yingd")) {
				changed = true;
			}
			if (kuid != rsl.getDouble("kuid")) {
				changed = true;
			}
			if (fady != rsl.getDouble("fady")) {
				changed = true;
			}
			if (gongry != rsl.getDouble("gongry")) {
				changed = true;
			}
			if (qity != rsl.getDouble("qity")) {
				changed = true;
			}
			if (cuns != rsl.getDouble("cuns")) {
				changed = true;
			}
			if (tiaozl != rsl.getDouble("tiaozl")) {
				changed = true;
			}
			if (shuifctz != rsl.getDouble("shuifctz")) {
				changed = true;
			}
			if (panyk != rsl.getDouble("panyk")) {
				changed = true;
			}
			if (feiscy != rsl.getDouble("feiscy")) {
				changed = true;
			}
			if (bukdml!=rsl.getDouble("bukdml")){
				changed = true;
			}
			if	(changwml!=rsl.getDouble("changwml")){
				changed = true;
			}
			if (kedkc!=rsl.getDouble("kedml")){
				changed = true;
			}
//			����б仯������պĴ��ձ���
			if (changed) {
				sb.append("update shouhcrbb set ")
				.append("fadl=").append(fadl)
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
				.append("changwml=").append(changwml)
				.append("kedkc=").append(kedkc)
				.append(" where id =").append(rsl.getString("id"));
				con.getUpdate(sb.toString());
				double kuccha = CustomMaths.sub(kuc, rsl.getDouble("kuc"));
				
//				�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
				sb.delete(0, sb.length());
				boolean synchronicUpdate = true;
				sb.append("select * from xitxxb where mingc='�պĴ��ձ�ʵʱ���¿��' and zhi ='��' and zhuangt=1 and diancxxb_id =" +dcid);
				synchronicUpdate = !con.getHasIt(sb.toString());
				if(synchronicUpdate){
	//				���µ�ǰ�����Ժ�����п��
					sb.delete(0, sb.length());
					sb.append("update shouhcrbb set ")
					.append("kuc = kuc + ").append(kuccha)
					.append(" where riq >= ").append(CurOraDate)
					.append(" and diancxxb_id = ").append(dcid);
					con.getUpdate(sb.toString());
				}
			}
		} else {
			// ������պĴ���û�����������
			sb
					.append(
							"insert into shouhcrbb(id, diancxxb_id, riq, fadl, jingz, biaoz, yuns,\n")
					.append(
							"yingd, kuid, fady, gongry, qity, cuns, tiaozl, shuifctz, panyk, kuc, dangrgm, haoyqkdr,feiscy,bukdml,changwml,kedkc) values(")
					.append("getnewid(").append(dcid).append("),").append(dcid)
					.append(",").append(CurOraDate).append(",").append(fadl)
					.append(",").append(jingz).append(",").append(biaoz)
					.append(",").append(yuns).append(",").append(yingd).append(
							",").append(kuid).append(",").append(fady).append(
							",").append(gongry).append(",").append(qity)
					.append(",").append(cuns).append(",").append(tiaozl)
					.append(",").append(shuifctz).append(",").append(panyk)
					.append(",").append(kuc).append(",").append(jingz).append(
							",").append(fady + gongry + qity + cuns)
					.append(",").append(feiscy)
					.append(",").append(bukdml)
					.append(",").append(changwml)
					.append(",").append(kedkc).append(")");
			con.getInsert(sb.toString());
		}
		rsl.close();
	*/
	}
}