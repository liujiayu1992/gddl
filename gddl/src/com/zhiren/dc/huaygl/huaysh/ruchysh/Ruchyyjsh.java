package com.zhiren.dc.huaygl.huaysh.ruchysh;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.zhiren.common.*;
import com.zhiren.common.ext.*;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*����:���ܱ�
 * ����:2010-6-23 15:23:26
 * ����:���ӻ�����ˮָ��
 */


/*����:���ܱ�
 *����:2010-3-24
 *����:����һ��������Ӹɻ���λ��ֵ(Qgr,d)
 */

 /*wzb
 * 2009-7-14
 * һ�����ҳ�����Ӳ���"����һ������Ƿ�ֻ��ʾ�������Ҫָ��"
 */

/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺2009-09-18
 * 
 * �޸����ݣ�
 * 	   1.����һ����ֵ�Կ�����ʽ��ʾ
		INSERT INTO xitxxb VALUES(
				(SELECT MAX(ID)+1 AS ID FROM xitxxb),
				1,
				301,
				'һ�������ֵ�Կ���ʾ',
				'��',
				'',
				'����',
				1,
				'ʹ��'
				)
	   2.����һ�����������־��¼
		INSERT INTO xitxxb VALUES(
			(SELECT MAX(ID)+1 AS ID FROM xitxxb),
			1,
			301,
			'����һ�������־',
			'��',
			'',
			'����',
			1,
			'ʹ��'
			)
	   3.����һ������Ƿ���ʾ���䵥λ,Ĭ�ϲ����
	   4.����ҳ��ʱ��ʼ��setTreeid()����������һ�����Ʋ�ͬ����ͬʱ��¼������ݻ���
 */


//2009-10-19  ���ܱ�  ע�͵��˾仰,������ʾ���䵥λ�ĳ��ᱨ��
/*if (!ShowHuaylb) {
	egu.getColumn("yunsdw").setHidden(true);
}*/

/*
 * �޸��ˣ�ww
 * �޸�ʱ�䣺2009-11-20
 * �޸����ݣ�
 * 		   �޸ġ�һ�������ʾ�󡱲�������ʱ��ȫ����ʾ��������ǰ�ǿ��������е����ѡ����ʾ��
 * 		  һ���������ӿ�����grid�ؼ��֣���Ruchyyjsh��������Ҫ��ʾ����
 * 	      ����ʱ�������־��¼
 */

/*
 * ���ߣ����
 * ʱ�䣺2012-09-17
 * ������Ϊ���������糧�ĸ��Ի�����ʹ�ò����жϻ���һ��Ͷ�����Ƿ���Ҫȷ�ϲ���
 	5Ϊ����Ҫ����ȷ�ϲ�����9Ϊ��Ҫȷ�ϲ���
	String shenhzt=MainGlobal.getXitxx_item("����", "����һ��Ͷ�����Ƿ���Ҫȷ�ϲ���", getTreeid(), "5");
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-06-18
 * ������ȡ����ʾ��У�ˡ���ť
 */

public class Ruchyyjsh extends BasePage implements PageValidateListener {
	
	private String CustomSetKey = "Ruchyyjsh";
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getKuangm() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setKuangm(String kuangm) {
		((Visit) this.getPage().getVisit()).setString1(kuangm);
	}

	public String getBianh() {
		return ((Visit) this.getPage().getVisit()).getString2();
	}

	public void setBianh(String bianh) {
		((Visit) this.getPage().getVisit()).setString2(bianh);
	}

	public String getShul() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setShul(String shul) {
		((Visit) this.getPage().getVisit()).setString3(shul);
	}

	public String getHuaysj() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setHuaysj(String huaysj) {
		((Visit) this.getPage().getVisit()).setString4(huaysj);
	}

	private boolean xiansztl = false;

	private boolean xiansztq = false;

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		sql = "select zhi from xitxxb where mingc = '�Ƿ���ʾ�볧������' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql);
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
		boolean Display = false;
		sql = "select zhi from xitxxb where mingc = 'һ�������ʾ��'";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��ʾ")) {
				Display = true;
			}
		}

		//����һ������Ƿ���ʾ�������,Ĭ��Ϊ��ʾ
		boolean ShowHuaylb=true;
		sql = "select zhi from xitxxb where mingc = '����һ������Ƿ���ʾ�������'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				ShowHuaylb = true;
			} else {
				ShowHuaylb = false;
			}
		}

//		����һ������Ƿ�ֻ��ʾ�����ԭʼָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar),Ĭ��Ϊȫ����ʾ
		boolean IsShow=false;
		sql = "select zhi from xitxxb where mingc = '����һ������Ƿ�ֻ��ʾ�������Ҫָ��'  and zhuangt = 1  ";
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				IsShow = true;
			} else {
				IsShow = false;
			}
		}

		//����һ������Ƿ���ʾ���䵥λ
		boolean ShowYunsdw = false;
		sql = "select zhi from xitxxb where mingc = 'һ�������ʾ���䵥λ' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��ʾ")) {
				ShowYunsdw = true;
			} else {
				ShowYunsdw = false;
			}
		}
		//����һ����ֵ�Կ�����ʽ��ʾ
//		INSERT INTO xitxxb VALUES(
//				(SELECT MAX(ID)+1 AS ID FROM xitxxb),
//				1,
//				301,
//				'һ�������ֵ�Կ���ʾ',
//				'��',
//				'',
//				'����',
//				1,
//				'ʹ��'
//				)
		boolean ShowCal = false;
		sql = "select zhi from xitxxb where mingc = 'һ�������ֵ�Կ���ʾ' and diancxxb_id = "
				+ visit.getDiancxxb_id();

		rsl = con.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				ShowCal = true;
			} else {
				ShowCal = false;
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
		if (ShowYunsdw) {
			if (Display) {
				sql = "SELECT   l.ID, m.mingc meikdw, yd.mingc yunsdw,f.jingz AS shul, p.mingc AS pinz,\n";
			} else {
				sql = "SELECT   l.ID,\n";
			}
			sql+= "         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n"
				+ "         TO_CHAR (z.bianm) AS huaybh, " + (ShowCal?"round_new(l.qnet_ar*1000/4.1816,0) qnet_ar_cal,":"") + "l.qnet_ar, l.aar, l.ad, l.vdaf, l.mt,\n"
				+ "         l.stad, l.aad, l.mad, round_new(100*(l.mt-l.mad)/(100-l.mad),2) as mf,l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n"
				+ "         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, l.t1, l.t2, l.t3, l.t4, huayy, l.lury,\n"
				+ "         l.beiz, l.huaylb\n"
				+ "    FROM zhuanmb z,\n"
				+ "         zhillsb l,\n"
				+ "         caiyb c,\n"
				+ "         (SELECT DISTINCT fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,\n"
				+ "                          SUM (cp.maoz-cp.piz-cp.zongkd) jingz,cp.yunsdwb_id\n"
				+ "                     FROM fahb fh,diancxxb dc,chepb cp\n"
				+ "						WHERE fh.diancxxb_id = dc.id AND cp.fahb_id=fh.id\n"
				+ 							str + "\n"
				+ "                 GROUP BY fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,cp.yunsdwb_id) f,\n"
				+ "         meikxxb m,\n"
				+ "         pinzb p,\n"
				+ "         yunsdwb yd\n"
				+ "   WHERE z.zhillsb_id = l.ID\n"
				+ "     AND f.zhilb_id = c.zhilb_id\n"
				+ "     AND c.zhilb_id = l.zhilb_id\n"
				+ "     AND f.meikxxb_id = m.ID\n"
				+ "     AND f.pinzb_id = p.ID\n"
				+ "     AND f.yunsdwb_id = yd.id(+)\n"
				+ "     AND z.zhuanmlb_id = (SELECT ID\n"
				+ "                            FROM zhuanmlb\n"
				+ "                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n"
				+ "                                          FROM zhuanmlb))\n"
				+ "     AND (shenhzt = 3 OR shenhzt = 4)\n"
				+ "ORDER BY z.ID, z.bianm, l.huaylb";

		}
		else {
			if (Display) {
				sql = "SELECT   l.ID, m.mingc meikdw, f.jingz AS shul, p.mingc AS pinz,\n";
			} else {
				sql = "SELECT   l.ID,\n";
			}
		sql +=	"         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n"
				+ "         TO_CHAR (z.bianm) AS huaybh," + (ShowCal?"round_new(l.qnet_ar*1000/4.1816,0) qnet_ar_cal,":"") +
                "to_char(l.qnet_ar,'90.99') qnet_ar, " +
                "to_char(l.aar,'90.99') aar," +
                "to_char(l.ad,'90.99') ad, \n" +
				"to_char(l.vdaf,'90.99') vdaf," +
                "to_char(round_new(l.mt,2),'90.9') as mt,\n"+
				"to_char(l.stad,'90.99') stad, " +
                "to_char(l.aad,'90.99') aad, " +
                "to_char(l.mad,'90.99') mad," +
                "round_new(100*(l.mt-l.mad)/(100-l.mad),2) as mf, \n" +
				"to_char(round_new(l.qbad,2),'90.99') qbad, " +
                "to_char(l.had,'90.99') had, " +
                "to_char(l.vad,'90.99') vad, " +
                "to_char(l.fcad,'90.99') fcad," +
                "to_char(l.std,'90.99') std, " +
                "to_char(round_new(l.qgrad,2),'90.99') qgrad,\n"+
				"to_char(l.hdaf,'90.99') hdaf," +
                "to_char(round_new(l.qgrad_daf,2),'90.99') qgrad_daf," +
                "to_char(l.sdaf,'90.99') sdaf," +
                "to_char(round_new(l.qgrd,2),'90.99') qgrd, " +
                "l.t1, l.t2, l.t3, l.t4, huayy, l.lury,\n"
				+ "         l.beiz, l.huaylb\n"
				+ "    FROM zhuanmb z,\n"
				+ "         zhillsb l,\n"
				+ "         caiyb c,\n"
				+ "         (SELECT DISTINCT fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id,\n"
				+ "                          SUM (fh.jingz) jingz\n"
				+ "                     FROM fahb fh,diancxxb dc\n"
				+ "						WHERE fh.diancxxb_id = dc.id\n"
				+ 							str + "\n"
				+ "                 GROUP BY fh.zhilb_id, fh.meikxxb_id, fh.pinzb_id) f,\n"
				+ "         meikxxb m,\n"
				+ "         pinzb p\n"
				+ "   WHERE z.zhillsb_id = l.ID\n"
				+ "     AND f.zhilb_id = c.zhilb_id\n"
				+ "     AND c.zhilb_id = l.zhilb_id\n"
				+ "     AND f.meikxxb_id = m.ID\n"
				+ "     AND f.pinzb_id = p.ID\n"
				+ "     AND z.zhuanmlb_id = (SELECT ID\n"
				+ "                            FROM zhuanmlb\n"
				+ "                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n"
				+ "                                          FROM zhuanmlb))\n"
				+ "     AND (shenhzt = 3 OR shenhzt = 4)\n"
				+ "ORDER BY z.ID, z.bianm, l.huaylb";
		}
		rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new HuayshExtGridUtil("gridDiv", rsl,CustomSetKey);
		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		egu.setMokmc("����һ�����");
		// ����ҳ�����Ա����ӹ�����
		egu.setWidth(1000);
		//
		egu.setDefaultsortable(false);
		// /������ʾ������
//		if (!Display) {
//			egu.getColumn("meikdw").setHidden(true);
//			egu.getColumn("pinz").setHidden(true);
//			egu.getColumn("shul").setHidden(true);
//		}

		//���ܱ�  ע�͵��˾仰,������ʾ���䵥λ�ĳ��ᱨ��
		/*if (!ShowHuaylb) {
			egu.getColumn("yunsdw").setHidden(true);
		}*/
        // ����ҳ�����Ա����ӹ�����
//        egu.setWidth(Locale.Grid_DefaultWidth);
        egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
        egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
     egu.getColumn("ID").setHidden(true);
        egu.getColumn("ID").setEditor(null);
		if (Display) {
			egu.getColumn("meikdw").setHeader("ú��λ");
			egu.getColumn("meikdw").setEditor(null);
			egu.getColumn("meikdw").setWidth(60);
		}
		if (ShowYunsdw) {
			egu.getColumn("yunsdw").setHeader("���䵥λ");
			egu.getColumn("yunsdw").setEditor(null);
			egu.getColumn("yunsdw").setWidth(60);
		}
		if (Display) {
			egu.getColumn("pinz").setHeader("Ʒ��");
			egu.getColumn("pinz").setEditor(null);
			egu.getColumn("shul").setWidth(60);
			egu.getColumn("shul").setHeader("����(��)");
			egu.getColumn("shul").setRenderer("shifcc");
			egu.getColumn("shul").setEditor(null);
			egu.getColumn("pinz").setWidth(60);
		}
		egu.getColumn("huaybh").setHeader("������");
        egu.getColumn("huaybh").setWidth(130);
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);
		if(!ShowHuaylb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);

		if (ShowCal) {
			egu.getColumn("qnet_ar_cal").setHeader("�յ�����λ����<p>Qnet,ar(��)</p>");
			egu.getColumn("qnet_ar_cal").setEditor(null);
		}
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
		// ���ӻ�����ˮָ��
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
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("qgrd").setHeader("�������λ��ֵ<p>Qgr,d(Mj/kg)</p>");
		egu.getColumn("qgrd").setEditor(null);

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
		egu.getColumn("huaybh").setWidth(130);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(110);
		// egu.getColumn("huaysj").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mf").setWidth(80);
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

		//�Ƿ�ֻ��ʾ�������Ҫָ��(Mt,Mad,Vdaf,Aad,Stad,Had,Qb,ad,Qgr,ad,Qnet,ar)
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
		egu.addPaging(1000);
		// Toolbar tb1 = new Toolbar("tbdiv");
//		egu.setGridSelModel(3);

		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);

//		egu.addToolbarButton("У��", GridButton.ButtonType_Sel, "HedButton");

		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Show);

		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,"HuitButton");

		setExtGrid(egu);
		con.Close();
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Hed() {
		// Visit visit = (Visit) this.getPage().getVisit();
		// JDBCcon con = new JDBCcon();
		// String sql = "";
		// sql = "";
	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		
//		5Ϊ����Ҫ����ȷ�ϲ�����9Ϊ��Ҫȷ�ϲ���
		String shenhzt=MainGlobal.getXitxx_item("����", "����һ��Ͷ�����Ƿ���Ҫȷ�ϲ���", getTreeid(), "5");
		
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");
			
			/*����:���ܱ�,ʱ��:2010-8-8 17:03:41 �糧:�����糧
			 * ������ֵ��Դ�ڻ���ӿ�ԭʼ���浥ʱ,���һ������������,����Ա����ֻ�ύ��һ����,����2����Ա
			 * ��Ϊ��ȷ����������������¾͵�������һ�����������.��ô������Ա��ԭʼ���浥�������ύ
			 * ����һ�����Ļ�����ʱ,�����������2�����˲���ȥ.ԭ�����zhilb�Ѿ�������ֵ��.
			 * �����ڻ���һ������ύʱ,���ж����������Ӧ��zhilb���Ƿ���ֵ,�����ֵ,��zhilb�е���һ������
			 * ɾ����.Ȼ���������zhilb��������Ӧ��zhillsb��״̬���Ƿ�ʹ���ֶ�.
			 */
			
			String zhillsb_id=mdrsl.getString("ID");
			String IsZhilbHaveZhi=
				"select zl.id from zhilb zl where zl.id in (\n" +
				"select ls.zhilb_id from zhillsb ls where ls.id="+zhillsb_id+"\n" + 
				")";
			ResultSetList rs =con.getResultSetList(IsZhilbHaveZhi);
			if(rs.next()){
				sql.append("delete zhilb where id=").append(rs.getLong("id")).append(";\n");
				sql.append("update zhillsb ls set ls.shenhzt="+shenhzt+",ls.shifsy=0 where ls.id in (");
				sql.append("select ls.id from zhillsb ls where ls.zhilb_id="+rs.getLong("id")+" and ls.shifsy=1)").append(";\n");
			}
			
			
			
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt ="+shenhzt).append(",");
			
			//���� shenhry�ļ�¼
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	public void Save2(String strchange, Visit visit) {
		String tableName = "zhillsb";
		
		JDBCcon con = new JDBCcon();
		
//		����һ�����������־��¼
//		INSERT INTO xitxxb VALUES(
//		(SELECT MAX(ID)+1 AS ID FROM xitxxb),
//		1,
//		301,
//		'����һ�������־',
//		'��',
//		'',
//		'����',
//		1,
//		'ʹ��'
//		)
		boolean saveLog = true;
		ResultSetList rs = con.getResultSetList("select zhi from xitxxb where mingc = '����һ�������־' and diancxxb_id = "
				+ visit.getDiancxxb_id());
		while (rs.next()) {
			if (rs.getString("zhi").equals("��")) {
				saveLog = true;
			} else {
				saveLog = false;
			}
		}
		
		StringBuffer sql = new StringBuffer("begin \n");	
		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("2").append(",");
			
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
			
			//����ʱ������־
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					"һ�����",getExtGrid().mokmc,
					tableName,mdrsl.getString("id"));
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		
		if (saveLog) {
			SaveLog(con, strchange, visit );
		}		
	}
	
	private String SaveLog(JDBCcon con,String strchange,Visit visit){
		String[] ipAddress = new String[2];
		getIpAddr(ipAddress);
		String tableName = "zhillsb_log";
		StringBuffer sb = new StringBuffer("begin \n");
				
		ResultSetList delrsl = visit.getExtGrid1()
		.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sb.append( "insert into ").append(tableName);
			sb.append(
					"(ID,ZHILB_ID,HUAYSJ,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QGRAD,HDAF,QGRAD_DAF,SDAF,\n" +
					" T1,T2,T3,T4,HUAYY,LURY,BEIZ,SHENHZT,BANZ,HUAYLBB_ID,HUAYLB,LIUCZTB,BUMB_ID,HAR,QGRD,VAR,QBRAD,SHIFSY,\n" + 
					" SHENHRY,SHENHRYEJ,zhillsb_id,IPADDRESS,HOSTNAME,DONGZMC,DONGZR,DONGZSJ\n" + 
					" )\n");
				sb.append(" values(getNewId(" + visit.getDiancxxb_id() + "),"); 
				sb.append(delrsl.getLong("zhilb_id")).append(",").append("to_date('" + delrsl.getString("Huaysj")+ "','yyyy-mm-dd'),");
				sb.append(delrsl.getDouble("qnet_ar")).append(",").append(delrsl.getDouble("aar")).append(",");
				sb.append(delrsl.getDouble("ad")).append(",").append(delrsl.getDouble("vdaf")).append(",");
				sb.append(delrsl.getDouble("mt")).append(",").append(delrsl.getDouble("stad")).append(",");
				sb.append(delrsl.getDouble("aad")).append(",").append(delrsl.getDouble("mad")).append(",");
				sb.append(delrsl.getDouble("qbad")).append(",").append(delrsl.getDouble("had")).append(",");
				sb.append(delrsl.getDouble("vad")).append(",").append(delrsl.getDouble("fcad")).append(",");
				sb.append(delrsl.getDouble("std")).append(",").append(delrsl.getDouble("qgrad")).append(",");
				sb.append(delrsl.getDouble("hdaf")).append(",").append(delrsl.getDouble("qgrad_daf")).append(",");
				sb.append(delrsl.getDouble("sdaf")).append(",").append(delrsl.getDouble("t1")).append(",");
				sb.append(delrsl.getDouble("t2")).append(",").append(delrsl.getDouble("t3")).append(",");
				sb.append(delrsl.getDouble("t4")).append(",'").append(delrsl.getString("huayy")).append("','");
				sb.append(delrsl.getString("lury")).append("','").append(delrsl.getString("beiz")).append("',");
				sb.append(delrsl.getInt("shenhzt")).append(",").append(delrsl.getString("banz")).append(",");
				sb.append(delrsl.getLong("huaylbb_id")).append(",'").append(delrsl.getString("huaylb")).append("',");
				sb.append(delrsl.getLong("liucztb")).append(",").append(delrsl.getLong("bumb_id")).append(",");
				sb.append(delrsl.getDouble("har")).append(",").append(delrsl.getDouble("qgar")).append(",");
				sb.append(delrsl.getDouble("var")).append(",").append(delrsl.getDouble("qbrad")).append(",");
				sb.append(delrsl.getInt("shifsy")).append(",").append(delrsl.getString("shenhry")).append(",");
				sb.append(delrsl.getString("shenhryej")).append(",").append(delrsl.getLong("id")).append(",'");
			sb.append(ipAddress[0]).append("','").append(ipAddress[1]).append("',").append("'һ��ɾ��'").append(",'");
			sb.append(visit.getRenymc()).append("',").append("sysdate); \n");
		}
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while(mdrsl.next()){
			sb.append( "insert into ").append(tableName);
			sb.append(
				"(ID,ZHILB_ID,HUAYSJ,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QGRAD,HDAF,QGRAD_DAF,SDAF,\n" +
				" T1,T2,T3,T4,HUAYY,LURY,BEIZ,SHENHZT,BANZ,HUAYLBB_ID,HUAYLB,LIUCZTB,BUMB_ID,HAR,QGRD,VAR,QBRAD,SHIFSY,\n" + 
				" SHENHRY,SHENHRYEJ,zhillsb_id,IPADDRESS,HOSTNAME,DONGZMC,DONGZR,DONGZSJ\n" + 
				" )\n");
			sb.append(" values(getNewId(" + visit.getDiancxxb_id() + "),"); 
			sb.append(mdrsl.getLong("zhilb_id")).append(",").append("to_date('" + mdrsl.getString("Huaysj")+ "','yyyy-mm-dd'),");
			sb.append(mdrsl.getDouble("qnet_ar")).append(",").append(mdrsl.getDouble("aar")).append(",");
			sb.append(mdrsl.getDouble("ad")).append(",").append(mdrsl.getDouble("vdaf")).append(",");
			sb.append(mdrsl.getDouble("mt")).append(",").append(mdrsl.getDouble("stad")).append(",");
			sb.append(mdrsl.getDouble("aad")).append(",").append(mdrsl.getDouble("mad")).append(",");
			sb.append(mdrsl.getDouble("qbad")).append(",").append(mdrsl.getDouble("had")).append(",");
			sb.append(mdrsl.getDouble("vad")).append(",").append(mdrsl.getDouble("fcad")).append(",");
			sb.append(mdrsl.getDouble("std")).append(",").append(mdrsl.getDouble("qgrad")).append(",");
			sb.append(mdrsl.getDouble("hdaf")).append(",").append(mdrsl.getDouble("qgrad_daf")).append(",");
			sb.append(mdrsl.getDouble("sdaf")).append(",").append(mdrsl.getDouble("t1")).append(",");
			sb.append(mdrsl.getDouble("t2")).append(",").append(mdrsl.getDouble("t3")).append(",");
			sb.append(mdrsl.getDouble("t4")).append(",'").append(mdrsl.getString("huayy")).append("','");
			sb.append(mdrsl.getString("lury")).append("','").append(mdrsl.getString("beiz")).append("',");
			sb.append(mdrsl.getInt("shenhzt")).append(",").append(mdrsl.getString("banz")).append(",");
			sb.append(mdrsl.getLong("huaylbb_id")).append(",'").append(mdrsl.getString("huaylb")).append("',");
			sb.append(mdrsl.getLong("liucztb")).append(",").append(mdrsl.getLong("bumb_id")).append(",");
			sb.append(mdrsl.getDouble("har")).append(",").append(mdrsl.getDouble("qgar")).append(",");
			sb.append(mdrsl.getDouble("var")).append(",").append(mdrsl.getDouble("qbrad")).append(",");
			sb.append(mdrsl.getInt("shifsy")).append(",").append(mdrsl.getString("shenhry")).append(",");
			sb.append(mdrsl.getString("shenhryej")).append(",").append(mdrsl.getLong("id")).append(",'");
			sb.append(ipAddress[0]).append("','").append(ipAddress[1]).append("',").append("'һ�����'").append(",'");
			sb.append(visit.getRenymc()).append("',").append("sysdate); \n");			
		}		
		sb.append("end;");	
		con.getInsert(sb.toString());
		return sb.toString();	
	}
	
	private  void getIpAddr(String[] ipAddress) {
		//IRequestCycle cycle
		
		ipAddress[0] = this.getPage().getRequestCycle().getRequestContext().getRequest().getRemoteAddr();
		ipAddress[1] = this.getPage().getRequestCycle().getRequestContext().getRequest().getRemoteHost();	
	};
	
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

	private boolean blnHed = false;

	public void HedButton(IRequestCycle cycle) {
		blnHed = true;

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

		}
		if (_RefurbishChick) {
			_RefurbishChick = false;

		}
		if (blnHed) {
			blnHed = false;
			Hed();
		}

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

	public String gridId;

	public int pagsize;

	public int getPagSize() {
		return pagsize;
	}

	public String getGridScriptLoad() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(gridId).append("_grid.render();");
		if (getPagSize() > 0) {
			gridScript.append(gridId).append(
					"_ds.load({params:{start:0, limit:").append(getPagSize())
					.append("}});\n");
			gridScript.append(gridId).append(
					"_ds.on('datachanged',function(){ ").append(gridId).append(
					"_history = ''; });");
		} else {
			gridScript.append(gridId).append("_ds.load();");
		}
		return gridScript.toString();
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

	public String getTbarScript() {
		StringBuffer gridScript = new StringBuffer();
		gridScript.append(getTbar().length() > 0 ? getToolbarScript() + ","
				: "");
		return gridScript.toString();
	}

	public String tbars;

	public String getTbar() {
		if (tbars == null) {
			tbars = "";
		}
		return tbars;
	}

	public void setTbar(String tbars) {
		this.tbars = tbars;
	}

	private String getToolbarScript() {
		StringBuffer tbarScript = new StringBuffer();
		tbarScript.append("tbar: [");
		tbarScript.append(getTbar());
		tbarScript.deleteCharAt(tbarScript.length() - 1);
		tbarScript.append("]");
		return tbarScript.toString();
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
			
			/*
			 * �޸��ˣ���ΰ
			 * �޸�ʱ�䣺2009-09-08
			 * �޸����ݣ���ʼ��setTreeid()����������һ�����Ʋ�ͬ����ͬʱ��¼������ݻ���
			 */
			this.setTreeid("" + visit.getDiancxxb_id());
			getSelectData();

		}
		getSelectData();
	}

}