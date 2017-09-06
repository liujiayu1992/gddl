package com.zhiren.dc.zhuangh.ruchysh;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*


/*����:���ܱ�
 *����:2010-3-24
 *����:����һ��������Ӹɻ���λ��ֵ(Qgr,d)
 * 
 * 
 * 
 */



 /*wzb
 * 2009-7-14
 * һ�����ҳ�����Ӳ���"����һ������Ƿ�ֻ��ʾ�������Ҫָ��"
 * 
 * 
 * 
 * 
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
 * ���ߣ���ʤ��
 * ʱ�䣺2012-10-31
 * ���÷�Χ��ֻ����ׯ�ӵ糧
 * ������	��zhillsb��RULMZLZMXB ��������SHENHSJ �У�Ϊ����ǩ������������

 */
public class Ruchyyjsh extends BasePage implements PageValidateListener {
	
	private String CustomSetKey = "Ruchyyjsh";
	
//	�ͻ��˵���Ϣ��
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
				+ "         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n"
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
				+ "         TO_CHAR (z.bianm) AS huaybh," + (ShowCal?"round_new(l.qnet_ar*1000/4.1816,0) qnet_ar_cal,":"") + " l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n"
				+ "         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n"
				+ "         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, l.t1, l.t2, l.t3, l.t4, huayy, l.lury,\n"
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
				+ "     AND (shenhzt = 3 OR shenhzt = 4)\n"//�˴�ҲӦ����0
				+ "--ORDER BY z.ID, z.bianm, l.huaylb\n";
		}
		String sq="("+sql+
			")\n" +
			"\n" + 
			"union (\n" + 
			"\n" + 
			"SELECT distinct  l.zhuanmbzllsb_id as id ,--z.id,\n" + 
			"         TO_CHAR (l.fenxrq, 'yyyy-mm-dd') AS huaysj,\n" + 
			"         TO_CHAR (z.bianm) AS huaybh, l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n" + 
			"         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n" + 
			"         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, 0 as t1,  0 as t2,  0 as t3,  0 as t4, huayy, l.lury,\n" + 
			"         l.beiz,'��¯' as  huaylb\n" + 
			"    FROM zhuanmb z,\n" + 
			"         rulmzlzmxb l\n" + 
			"      --   ,         caiyb c\n" + 
			"\n" + 
			"   WHERE z.zhillsb_id = l.zhuanmbzllsb_id\n" + 

			"AND z.zhuanmlb_id in (SELECT ID\n" +
			"                     FROM zhuanmlb\n" + 
			"                    WHERE jib = (SELECT NVL (MAX (jib), 0)\n" + 
			"                                   FROM zhuanmlb))"+
			"   and shenhzt=4\n" + 
			"\n" + 
			")"+
			//
			"\n" + 
//			"union (\n" + 
//			"\n" + 
//			"SELECT   l.ID,--z.id,\n" + 
//			"         TO_CHAR (l.fenxrq, 'yyyy-mm-dd') AS huaysj,\n" + 
//			"         TO_CHAR (z.bianm) AS huaybh, l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n" + 
//			"         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n" + 
//			"         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, 0 as t1,  0 as t2,  0 as t3,  0 as t4, huayy, l.lury,\n" + 
//			"         l.beiz,'������' as  huaylb\n" + 
//			"    FROM zhuanmb z,\n" + 
//			"         QITYMXB l\n" + 
//			"      --   ,         caiyb c\n" + 
//			"\n" + 
//			"   WHERE z.zhillsb_id = l.ID\n" + 
//
//			"AND z.zhuanmlb_id in (SELECT ID\n" +
//			"                     FROM zhuanmlb\n" + 
//			"                    WHERE jib = (SELECT NVL (MAX (jib), 0)\n" + 
//			"                                   FROM zhuanmlb))"+
//			"   and shenhzt=4\n" + 
//			"\n" + 
//			")"+
			
			"union (\n" +
			"\n" + 
			"SELECT   l.ID,\n" + 
			"         TO_CHAR (l.huaysj, 'yyyy-mm-dd') AS huaysj,\n" + 
			"         TO_CHAR (z.bianm) AS huaybh, l.qnet_ar, l.aar, l.ad, l.vdaf, round_new(l.mt,2) as mt,\n" + 
			"         l.stad, l.aad, l.mad, l.qbad, l.had, l.vad, l.fcad, l.std, l.qgrad,\n" + 
			"         l.hdaf, l.qgrad_daf, l.sdaf,l.qgrd, 0 as t1,  0 as t2,  0 as t3,  0 as t4, huayy, l.lury,\n" + 
			"         l.beiz,huaylb\n" + 
			"    FROM zhuanmb z,\n" + 
			"         zhillsb l\n" + 
			"\n" + 
			"   WHERE z.zhillsb_id = l.ID\n" + 
			"    AND z.zhuanmlb_id = (SELECT ID\n" + 
			"                            FROM zhuanmlb\n" + 
			"                           WHERE jib = (SELECT NVL (MAX (jib), 0)\n" + 
			"                                          FROM zhuanmlb))\n" + 
			"   and shenhzt=4\n" + 
			"\n" + 
			")"
			//
			
			;


		rsl = con.getResultSetList(sq);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		egu.setWidth("bodyWidth");
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
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);
		egu.getColumn("huaylb").setHidden(true);
		if(!ShowHuaylb){
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.getColumn("qnet_ar").setHeader("�յ�����λ����<p>Qnet,ar(MJ/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setHidden(true);
		
		if (ShowCal) {
			egu.getColumn("qnet_ar_cal").setHeader("�յ�����λ����<p>Qnet,ar(��)</p>");
			egu.getColumn("qnet_ar_cal").setEditor(null);
			egu.getColumn("qnet_ar_cal").setHidden(true);
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
		egu.getColumn("qbad").setHeader("�����������Ͳ��ֵ<p>Qb,ad(MJ/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("�����������<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("����������ӷ���<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("�̶�̼<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("�����ȫ��<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("�����������λ��ֵ<p>Qgr,ad(MJ/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("�����޻һ���<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("qgrad_daf").setHeader("�����޻һ���λ��ֵ<p>Qgr,daf(MJ/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("sdaf").setHeader("�����޻һ�ȫ��<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("qgrd").setHeader("�������λ��ֵ<p>Qgr,d(MJ/kg)</p>");
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
		egu.getColumn("huaybh").setWidth(80);
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
		egu.addPaging(25);
		// Toolbar tb1 = new Toolbar("tbdiv");
		egu.setGridSelModel(3);
		
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		
		//��Ϊû���ᵽ��˹���������ע����
//		egu.addToolbarButton("У��", GridButton.ButtonType_Sel, "HedButton");
		egu.addToolbarButton("���", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);

		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
				"HuitButton");

		setExtGrid(egu);
		con.Close();
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
//		getSelectData();
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
		boolean flag=false;
		while (mdrsl.next()) {
			String huaylb=mdrsl.getString("HUAYLB");
			String zhillsb_id=mdrsl.getString("ID");
			StringBuffer sql2 = new StringBuffer();
			if(huaylb.equals("��¯")){//��¯һ�����
				String ru_sql=" select * from rulmzlzmxb where id="+zhillsb_id;
				ResultSetList ru_rsl=con.getResultSetList(ru_sql);
				tableName="rulmzlzmxb";
			}
			if(huaylb.equals("������")){//��¯һ�����
			
//				tableName="QITYMXB";
				String getId_sql="select id from  huirdjlb where bianm='"
					+mdrsl.getString("HUAYBH")+"'";
				ResultSetList ru_rsl=con.getResultSetList(getId_sql);
				if(ru_rsl.getRows()>0){
					
					//������˵������¼��Ļ��۵�
				}else{
					//�����ܱ����
					setMsg("��ȷ�����۵���Ϣ�Ѿ�¼�룡");
					flag=true ;
				}
			}
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("5").append(",");
			
			//���� shenhry�ļ�¼
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			//���� shenhsj�ļ�¼
			sql.append(" shenhsj=sysdate,");
			
			sql.deleteCharAt(sql.length() - 1);
			if(huaylb.equals("��¯")){
				sql.append(" where zhuanmbzllsb_id =").append(mdrsl.getString("ID")).append(
				";\n");

				
			}else{
				
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
				";\n");
			}
		}
		sql.append("end;");
//		if(flag){
//			setMsg("��ȷ�����۵���Ϣ�Ѿ�¼�룡");
//			con.Close();
//		}else{
//			
			int fla = con.getUpdate(sql.toString());
			
			if(fla==-1){
				setMsg("����ʧ��");
			}else{
				setMsg("����ɹ�");
			}
			con.commit();
			con.Close();
//		}
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
			String huaylb=mdrsl.getString("HUAYLB");
			String zhillsb_id=mdrsl.getString("ID");
			if(huaylb.equals("��¯")){//��¯һ�����
				String ru_sql=" select * from rulmzlzmxb where id="+zhillsb_id;
				ResultSetList ru_rsl=con.getResultSetList(ru_sql);
				tableName="rulmzlzmxb";
			}
			
//			if(huaylb.equals("������")){//��¯һ�����
//
//				tableName="QITYMXB";
//			}
			StringBuffer sql2 = new StringBuffer();
			sql2.append("getnewid(").append(visit.getDiancxxb_id()).append(")");

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = ");
			sql.append("0").append(",");
			
			sql.append(" shenhry='").append(visit.getRenymc()).append("',");
			
			sql.deleteCharAt(sql.length() - 1);
			if(huaylb.equals("��¯")){
				sql.append(" where zhuanmbzllsb_id =").append(mdrsl.getString("ID")).append(
				";\n");

				
			}else{
				
				sql.append(" where id =").append(mdrsl.getString("ID")).append(
				";\n");
			}
//			sql.append(" where id =").append(mdrsl.getString("ID")).append(
//					";\n");
			
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

//			getSelectData();
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