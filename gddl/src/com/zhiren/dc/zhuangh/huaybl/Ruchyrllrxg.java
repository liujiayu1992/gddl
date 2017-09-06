package com.zhiren.dc.zhuangh.huaybl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;

import com.zhiren.dc.huaygl.Compute;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * ���ߣ�����
 * ʱ�䣺2010-04-02
 * �������������ܣ�1.�÷�������ƹ���ƶ�
 * 				  2.ȥ�����ֶΣ�����ύʱ�Զ�������ֵ
 * 		���÷�Χ����������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-09-04 14��29
 * ����������������ExtGrid����������������Ĳ���Ԥ֪�Թ�ȥ���˻س����TAB���ķ���
 */
/*
 * ����:���	
 * ʱ��:2009-5-11
 * �޸�����:��֤�Ƿ����� �������
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-06-26
 * ����������xitxxb���ã���Լ��λ��������С��λ Ĭ��Ϊ3λ��������
 * 		mingc	= '��λ������С��λ'
 * 		zhi		= С��λ
 * 		leib 	= '����'
 * 		zhuangt = 1
 * 		diancxxb_id = �糧ID
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-27
 * ������������Ҫָ�갴�س���������һ��������Ĺ���
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-11 16��53
 * ����������ExtGrid�����ÿ���ʾ������
 */

/*
 * �޸�ʱ�䣺2009-09-18
 * �޸��ˣ�  ww
 * �޸����ݣ�
 * 			1 ���ӳ����ж�
 * 			�ڻ�ȡԪ�ط���ʱû�м��볧����жϣ��޷���ȡԪ�ط�����Ŀ��ֵ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '�ֳ�ͬ����ֵ��ͬ',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 
 *          2 ���Ӳ���ʱ��,Ĭ�ϲ���ʾ
 INSERT INTO xitxxb VALUES(
 getnewid(diancxbid),
 1,
 diancxbid,
 '����¼����ʾ��������',
 '��',
 '',
 '����',
 1,
 'ʹ��'
 )
 */

/*����:���ܱ�
 * ʱ��:2009-10-26 10:33:14
 * �޸�����:ˢ�¼����������ڵ��ж�,ֻ�������糧�����ַ���,
 *         ��ʱ�������糧������
 * 
 * 
 * 
 */

/* �޸�ʱ��:2010-01-25 
 * ��Ա��liht
 * �޸�����:����ʱ��¼��ʱ��ԭ�������ջ����Ͼ�ȷ��ʱ����
 */
/* 
 * ���ߣ����
 * �޸�ʱ��:2013-05-29
 * �޸�����:�޸ĳ��򱣴��߼�˳�����
 * 			��������ע����䡣
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-03-10
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		����������ڵ�BUG���������ע�͡�
 */
/*
 * ���ߣ����
 * ʱ�䣺2014-04-1
 * ����������ׯ���볧��¯�����жϣ�������ʱ��ڵ�����9����������9��Ϊһ��ͳ�����ڡ�
 * 		��2����¯ú��Ϊ1����9����2����9�����¯ú��
 */
public class Ruchyrllrxg extends BasePage implements PageValidateListener {

//	private String CustomSetKey = "Ruchylr";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

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

	// �����ֺ�����Χ����ȡ�� Mt, Mad��Aad,Vad,Std,Qbad�ķ�Χ
	public ResultSetList getmaxmin(JDBCcon con, long diancxxb_id) {
		ResultSetList shuzhirsl;
		String sql = "select shangx,xiax,mingc from shuzhlfwb "
				+ "where leib = '����' and beiz='����¼��'";
		shuzhirsl = con.getResultSetList(sql);
		return shuzhirsl;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "";
		// ���Ƿ�ɱ༭
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// ��Ԫ�ط���
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// ��Ԫ�ط���
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}
		// ��ֵ������Χ
		ResultSetList szhlfw = getmaxmin(con, visit.getDiancxxb_id());
		// ת������ж�Ӧ��������ID��
		String zhuanmlbid = "";
		sql = "select id from zhuanmlb where mingc = '�������'";
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			zhuanmlbid = rsl.getString("id");
		}
		rsl.close();
		if ("".equals(zhuanmlbid)) {
			WriteLog.writeErrorLog(ErrorMessage.ZhuanmlbNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.ZhuanmlbNotFound);
			return;
		}

		// �糧Treeˢ������
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
				ResultSet rsss = con
						.getResultSet("select id from diancxxb where fuid="
								+ getTreeid());
				if (rsss.next()) {
					str = "and dc.fuid=" + getTreeid();
				} else {
					str = "and dc.id = " + getTreeid();
				}
			} catch (SQLException e) {
				// TODO �Զ����� catch ��
				e.printStackTrace();
			}

		}

		// �Ƿ���ʾ����ʱ��
		String riqTiaoj = this.getRiq();
		if (riqTiaoj == null || "".equals(riqTiaoj)) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		boolean isShowRq = false;
		String strTemp = "";
		isShowRq = "��".equals(MainGlobal.getXitxx_item("����", "����¼����ʾ��������",
				visit.getDiancxxb_id() + "", "��"));
		if (isShowRq) {
			strTemp = " AND (SELECT caiyrq FROM caiyb WHERE zhilb_id=l.zhilb_id)=to_date('"
					+ riqTiaoj + "','yyyy-mm-dd')\n";
		}

		// �Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���", visit
				.getDiancxxb_id()
				+ "", "��"));

		String sql_h = "";
		if (!isYc) {
			sql_h = " l." + sign_H + ",\n";
		}
		// ��ѯ����SQL
		// ˵��:��������Ļ������б���Ҫ�ܹ������������ڵ���ˢ�³���,����������sql����д.�����糧������
		if (visit.getDiancmc().equals("��������")) {
			sql = "select l.id,\n" + "       to_char(z.bianm) as huaybh,\n"
					+ "       nvl(l.huaysj,sysdate) huaysj,\n"
					+ "       l.mt,\n" + "       l.mad,\n" + "       l.aad,\n"
					+ "       l.vad,\n" + "       l." + sign_S + ","
					+ "       l." + sign_H + ",\n" + "       l.qbad,\n"
					+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
					+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
					+ "       l.beiz,\n" + "       l.huaylb\n"
					+ "from zhuanmb z, zhillsb l,\n"
					+ "       (select distinct f.zhilb_id\n"
					+ "        from fahb f,diancxxb dc\n"
					+ "        where f.diancxxb_id = dc.id\n" + str
					+ ") fh ,zhiyryb zy\n"
					+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
					+ zhuanmlbid + "\n"
					+ "      and l.zhilb_id = fh.zhilb_id\n"
					+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n"
					+ "      and l.zhilb_id=zy.zhilb_id\n"
					+ "      and zy.zhiyrq=to_date('" + this.getZhiyrq()
					+ "','yyyy-mm-dd')\n" + strTemp + "order by l.id, l.huaylb";
		} else {
			sql = "select l.id,\n"
					+ "       to_char(z.bianm) as huaybh,\n"
					+ "       nvl(l.huaysj,sysdate) huaysj,\n"
					+ "       l.mt,\n"
					+ "       l.mad,\n"
					+ "       l.aad,\n"
					+ "       l.vad,\n"
					+ "       l."
					+ sign_S
					+ ","
					+ sql_h
					+ "       l.qbad,\n"
					// + " l." + sign_H + ",\n" + " l.qbad,\n"
					+ "       l.t1,\n" + "       l.t2,\n" + "       l.t3,\n"
					+ "       l.t4,\n" + "       huayy,\n" + "       l.lury,\n"
					+ "       l.beiz,\n" + "       l.huaylb,l.leix\n"
					+ "from zhuanmb z, zhillsb l,\n"
					+ "       (select distinct f.zhilb_id\n"
					+ "        from fahb f,diancxxb dc\n"
					+ "        where f.diancxxb_id = dc.id\n" + str + ") fh\n"
					+ "where z.zhillsb_id = l.id and zhuanmlb_id = "
					+ zhuanmlbid + "\n"
					+ "      and l.zhilb_id = fh.zhilb_id\n"
					+ "      and (l.shenhzt = 0 or l.shenhzt = 2)\n" + strTemp
					+ "order by l.id, l.huaylb";
		}
		String ssql = 

			" select  id,huaybh,huaysj,\n" +
			"  qnet_ar_rc,qnet_ar_rl, mt,mad,aad,vad,stad,had,qbad,t1,t2,t3,t4,huayy,lury,beiz,huaylb\n" + 
			"   from\n" + 
			"  (       select distinct c.zhilb_id as id,c.huaybh,to_char(min(c.huaysj),'YYYY-MM-DD hh24:mi:ss') huaysj,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb\n" + 
			"            from\n" + 
			"           ( select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n" + 
			"                   rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb\n" + 
			"            from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n" + 
			"            where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhillsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhuanmlb_id=zlb.id\n" + 
			"            and zlb.jib=3\n" + 
			"            and zlb.diancxxb_id=\n" 
			+ visit.getDiancxxb_id() + 
			"\n" + 
			"            and rb.shenhzt=6\n" + 
			"            ) c,\n" + 
			"            (\n" + 
			"                 select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n" + 
			"                   rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb\n" + 
			"            from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n" + 
			"            where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhillsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhuanmlb_id=zlb.id\n" + 
			"            and zlb.jib=2\n" + 
			"            and zlb.diancxxb_id=\n" + visit.getDiancxxb_id()+ 
			"\n" + 
			"          and rb.shenhzt=6\n" + 
			"            ) d\n" + 
			"            where c.zhilb_id=d.zhilb_id(+) group by (c.zhilb_id ,c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb)\n" + 
			"\n" + 
			") aa ,(\n" + 
			"select distinct\n" + 
			"rc.qnet_ar as qnet_ar_rc,\n" + 
			" rl.qnet_ar as qnet_ar_rl,\n" + 
			"\n" + 
			" rl.rulmzlzmxb_id\n" + 
			"  from\n" + 
			" (\n" + 
			" select yundh,l.mingc as chuanm,g.mingc as gongys,m.mingc as meikmc,p.mingc as pinz,\n" + 
			" c.mingc as faz,sum(biaoz) as yundl,sum(maoz-piz) as xiehl,sum(sanfsl) as shuicl,to_date(to_char(daohrq,'yyyy-mm-dd'),'yyyy-mm-dd') as daohrq,\n" + 
			" xiemkssj,xiemjssj,mc.meic,mc.duowmc,sum(mc.meil) as meicml,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl)/0.0041816,0)) as qnet_ar_k,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl),2)) as qnet_ar,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(stad,2))/sum(laimsl),2)) as stad\n" + 
			"  from fahb,gongysb g,meikxxb m,pinzb p,chezxxb c,luncxxb l,zhilb,\n" + 
			"  (select fahmcb.*,meicb.mingc as meic from fahmcb,meicb where fahmcb.meicb_id=meicb.id) mc\n" + 
			" where fahb.gongysb_id=g.id and fahb.meikxxb_id=m.id\n" + 
			" and fahb.pinzb_id=p.id and fahb.faz_id=c.id and fahb.luncxxb_id=l.id\n" + 
			" and mc.fahb_id=fahb.id and fahb.zhilb_id=zhilb.id(+)\n" + 
			" group by yundh,l.mingc,g.mingc,m.mingc,p.mingc,c.mingc,daohrq,xiemkssj,xiemjssj,mc.meic,mc.duowmc\n" + 
			" )rc,\n" + 
			" (select mx.id as rulmzlzmxb_id, rl.rulmzlb_id as rulmzlb_id, rl.shangmkssj,rl.shangmjssj,meizmc,meicmc,meidmc,jizlh,meich,mx.meil,round_new(round_new(mx.qnet_ar,2)/0.0041816 ,0) as qnet_ar_k,\n" + 
			" round_new(mx.qnet_ar,2) as qnet_ar,round_new(mx.mt,1) as mt,round_new(mx.mad,2) as mad,round_new(mx.aad,2) as aad,round_new(mx.vad,2) as vad,\n" + 
			" round_new(mx.stad,2) as stad\n" + 
			" from rulmzlzb rl,rulmzlzmxb mx\n" + 
			" where rl.id=mx.rulmzlzb_id\n" + 
			" )rl ,duowglb d\n" + 
			" where rc.pinz=rl.meizmc(+) and rc.meic=rl.meicmc(+)\n" + 
			"  and d.mingc_old=rc.duowmc and d.mingc_new=rl.meidmc  and rc.xiemkssj<=rl.shangmkssj(+)\n" + 
			" and nvl(rl.qnet_ar,0) > nvl(rc.qnet_ar,0)\n" + 
			" and nvl(rc.qnet_ar,0)<>0 and nvl(rl.qnet_ar,0)<>0\n" + 
			" order by rulmzlzmxb_id asc\n" + 
			" ) ba\n" + 
			" where aa.id=ba.rulmzlzmxb_id " +
			
			" union "+

			" select  id,huaybh,huaysj,\n" +
			"  qnet_ar_rc,qnet_ar_rl, mt,mad,aad,vad,stad,had,qbad,t1,t2,t3,t4,huayy,lury,beiz,huaylb\n" + 
			"   from\n" + 
			"  (       select distinct c.zhilb_id as id,c.huaybh,to_char(min(c.huaysj),'YYYY-MM-DD hh24:mi:ss') huaysj,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb\n" + 
			"            from\n" + 
			"           ( select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n" + 
			"                   rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb\n" + 
			"            from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n" + 
			"            where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhillsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhuanmlb_id=zlb.id\n" + 
			"            and zlb.jib=3\n" + 
			"            and zlb.diancxxb_id=\n" 
			+ visit.getDiancxxb_id() + 
			"\n" + 
			"            and rb.shenhzt=6\n" + 
			"            ) c,\n" + 
			"            (\n" + 
			"                 select distinct rb.zhuanmbzllsb_id zhilb_id,zb.bianm as huaybh, nvl(rb.FENXRQ,sysdate) huaysj,\n" + 
			"                   rb.mt,rb.mad,rb.aad,rb.vad,rb.stad,rb.had,rb.qbad, 0 as  t1,0 as t2,0 as t3,0 as t4   ,rb.huayy,rb.lury,rb.beiz,'��¯' as huaylb\n" + 
			"            from yangpdhb yb,rulmzlzmxb rb,zhuanmb zb,zhuanmlb zlb\n" + 
			"            where yb.zhilblsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhillsb_id=rb.zhuanmbzllsb_id\n" + 
			"            and zb.zhuanmlb_id=zlb.id\n" + 
			"            and zlb.jib=2\n" + 
			"            and zlb.diancxxb_id=\n" + visit.getDiancxxb_id()+ 
			"\n" + 
			"          and rb.shenhzt=6\n" + 
			"            ) d\n" + 
			"            where c.zhilb_id=d.zhilb_id(+) group by (c.zhilb_id ,c.huaybh,c.mt,c.mad,c.aad,c.vad,c.stad,c.had,c.qbad,c.t1,c.t2,c.t3,c.t4,c.huayy,c.lury,c.beiz,c.huaylb)\n" + 
			"\n" + 
			") aa ,(\n" + 
			"select distinct\n" + 
			"rc.qnet_ar as qnet_ar_rc,\n" + 
			" rl.qnet_ar as qnet_ar_rl,\n" + 
			"\n" + 
			" rl.rulmzlzmxb_id\n" + 
			"  from\n" + 
			" (\n" + 
			" select yundh,l.mingc as chuanm,g.mingc as gongys,m.mingc as meikmc,p.mingc as pinz,\n" + 
			" c.mingc as faz,sum(biaoz) as yundl,sum(maoz-piz) as xiehl,sum(sanfsl) as shuicl,to_date(to_char(daohrq,'yyyy-mm-dd'),'yyyy-mm-dd') as daohrq,\n" + 
			" xiemkssj,xiemjssj,mc.meic,mc.duowmc,sum(mc.meil) as meicml,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl)/0.0041816,0)) as qnet_ar_k,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(qnet_ar,2))/sum(laimsl),2)) as qnet_ar,\n" + 
			" decode(sum(laimsl),0,0,round_new(sum(laimsl*round_new(stad,2))/sum(laimsl),2)) as stad\n" + 
			"  from fahb,gongysb g,meikxxb m,pinzb p,chezxxb c,luncxxb l,zhilb,\n" + 
			"  (select fahmcb.*,meicb.mingc as meic from fahmcb,meicb where fahmcb.meicb_id=meicb.id) mc\n" + 
			" where fahb.gongysb_id=g.id and fahb.meikxxb_id=m.id\n" + 
			" and fahb.pinzb_id=p.id and fahb.faz_id=c.id and fahb.luncxxb_id=l.id\n" + 
			" and mc.fahb_id=fahb.id and fahb.zhilb_id=zhilb.id(+)\n" + 
			" group by yundh,l.mingc,g.mingc,m.mingc,p.mingc,c.mingc,daohrq,xiemkssj,xiemjssj,mc.meic,mc.duowmc\n" + 
			" )rc,\n" + 
			" (select mx.id as rulmzlzmxb_id, rl.rulmzlb_id as rulmzlb_id, rl.shangmkssj,rl.shangmjssj,meizmc,meicmc,meidmc,jizlh,meich,mx.meil,round_new(round_new(mx.qnet_ar,2)/0.0041816 ,0) as qnet_ar_k,\n" + 
			" round_new(mx.qnet_ar,2) as qnet_ar,round_new(mx.mt,1) as mt,round_new(mx.mad,2) as mad,round_new(mx.aad,2) as aad,round_new(mx.vad,2) as vad,\n" + 
			" round_new(mx.stad,2) as stad\n" + 
			" from rulmzlzb rl,rulmzlzmxb mx\n" + 
			" where rl.id=mx.rulmzlzb_id\n" + 
			" )rl,duowglb d\n" + 
			" where rc.pinz=rl.meizmc(+) and rc.meic=rl.meicmc(+)\n" + 
			" and rc.duowmc=rl.meidmc and rc.duowmc !=d.mingc_old and rc.xiemkssj<=rl.shangmkssj(+)\n" + 
			" and nvl(rl.qnet_ar,0) > nvl(rc.qnet_ar,0)\n" + 
			" and nvl(rc.qnet_ar,0)<>0 and nvl(rl.qnet_ar,0)<>0\n" + 
			" order by rulmzlzmxb_id asc\n" + 
			" ) ba\n" + 
			" where aa.id=ba.rulmzlzmxb_id";
		
		rsl = con.getResultSetList(ssql);
		// ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl, CustomSetKey);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		// //���ñ��������ڱ���
		egu.setTableName("zhillsb");
		// ����ҳ������Ա����ӹ�����
		egu.setWidth("bodyWidth");
		// /������ʾ������
		egu.getColumn("qnet_ar_rc").setHeader("�볧����ֵ");
		egu.getColumn("qnet_ar_rc").setEditor(null);
		egu.getColumn("qnet_ar_rl").setHeader("��¯����ֵ");
		egu.getColumn("qnet_ar_rl").setEditor(null);
		egu.getColumn("huaybh").setHeader("������");
		egu.getColumn("huaybh").setEditor(null);
		
		egu.getColumn("huaysj").setHeader("����ʱ��");
		egu.getColumn("mt").setHeader("ȫˮ��Mt(%)");
		egu.getColumn("mad").setHeader("ˮ��Mad(%)");
		egu.getColumn("aad").setHeader("�ҷ�Aad(%)");
		egu.getColumn("vad").setHeader("�ӷ���Vad(%)");
		egu.getColumn("qbad").setHeader("��Ͳ����Qbad(MJ/kg)");
		egu.getColumn("stad").setHeader("���" + "stad" + "(%)");
		// egu.getColumn(sign_S).setHeader("���" + sign_S + "(%)");
		if (!isYc) {
			egu.getColumn(sign_H).setHeader("��" + sign_H + "(%)");
		}
		egu.getColumn("t1").setHeader("T1(��)");
		egu.getColumn("t2").setHeader("T2(��)");
		egu.getColumn("t3").setHeader("T3(��)");
		egu.getColumn("t3").setHidden(true);
		egu.getColumn("t2").setHidden(true);
		egu.getColumn("t1").setHidden(true);
		egu.getColumn("t4").setHeader("T4(��)");
		egu.getColumn("t4").setHidden(true);
		egu.getColumn("huayy").setCenterHeader("����Ա");
		egu.getColumn("huayy").setWidth(150);
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("����¼��Ա");
		egu.getColumn("beiz").setHeader("���鱸ע");
		egu.getColumn("huaylb").setHeader("�������");
		egu.getColumn("huaylb").setEditor(null);

		egu.getColumn("huaysj").setCenterHeader("����ʱ��");
		egu.getColumn("huaysj").setWidth(120);
		egu.getColumn("huaysj").editor.setAllowBlank(false);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("mt").editor.setMaxValue("100");
		egu.getColumn("mt").editor.setMinValue("0");
		egu.getColumn("mt").setColtype(GridColumn.ColType_default);
		egu.getColumn("mt").editor.setAllowBlank(false);

		egu.getColumn("mad").setWidth(80);
		egu.getColumn("mad").editor.setMaxValue("100");
		egu.getColumn("mad").editor.setMinValue("0");
		egu.getColumn("mad").setColtype(GridColumn.ColType_default);
		egu.getColumn("mad").editor.setAllowBlank(false);

		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").editor.setMaxValue("100");
		egu.getColumn("aad").editor.setMinValue("0");
		egu.getColumn("aad").setColtype(GridColumn.ColType_default);
		egu.getColumn("aad").editor.setAllowBlank(false);

		egu.getColumn("vad").setWidth(80);
		egu.getColumn("vad").editor.setMaxValue("100");
		egu.getColumn("vad").editor.setMinValue("0");
		egu.getColumn("vad").setColtype(GridColumn.ColType_default);
		egu.getColumn("vad").editor.setAllowBlank(false);

		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qbad").editor.setMaxValue("100");
		egu.getColumn("qbad").editor.setMinValue("0");
		egu.getColumn("qbad").setColtype(GridColumn.ColType_default);
		egu.getColumn("qbad").editor.setAllowBlank(false);

		egu.getColumn("stad").setWidth(80);
		egu.getColumn("stad").editor.setMaxValue("100");
		egu.getColumn("stad").editor.setMinValue("0");
		egu.getColumn("stad").setColtype(GridColumn.ColType_default);

		if (editable_S) {
			egu.getColumn("stad").editor.setAllowBlank(false);
			if (isYc) {
				egu.getColumn("stad").editor.setListeners(getStr(9));
			}
		}

		if (!isYc) {
			egu.getColumn(sign_H).setWidth(80);
			egu.getColumn(sign_H).editor.setMaxValue("100");
			egu.getColumn(sign_H).editor.setMinValue("0");
			egu.getColumn(sign_H).setColtype(GridColumn.ColType_default);
			egu.getColumn(sign_H).setHidden(!editable_H);
			if (editable_H) {
				egu.getColumn(sign_H).editor.setAllowBlank(false);
			}
		}

		// ���û����õķ�Χ���ǳ���Ĭ��
		while (szhlfw.next()) {
			GridColumn gc = egu.getColumn(szhlfw.getString("mingc"));
			if (gc != null) {
				gc.editor.setMaxValue(szhlfw.getString("shangx"));
				gc.editor.setMinValue(szhlfw.getString("xiax"));
			}
		}
		egu.getColumn("t1").setWidth(80);
		egu.getColumn("t1").setDefaultValue("20");
		egu.getColumn("t2").setWidth(80);
		egu.getColumn("t2").setDefaultValue("20");
		egu.getColumn("t3").setWidth(80);
		egu.getColumn("t3").setDefaultValue("20");
		egu.getColumn("t4").setWidth(80);
		egu.getColumn("t4").setDefaultValue("20");
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setWidth(80);



		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(22);

		// �жϻ���ʱ��¼���Ƿ�ȷ��ʱ��
		String strXitszzt = MainGlobal.getXitxx_item("����",
				Locale.huaysjlrsfjqdsf_xitxx, String.valueOf(getTreeid()), "��");
		if (strXitszzt.equals("��")) {
			DatetimeField huaysj = new DatetimeField();
			huaysj.setFormat("Y-m-d H:i");
			huaysj.setMenu("new DatetimeMenu()");
			egu.getColumn("huaysj").setRenderer(GridColumn.Renderer_DateTime);
			egu.getColumn("huaysj").setEditor(huaysj);
		}

		DatetimeField datetime = new DatetimeField();
		egu.getColumn("huaysj").setEditor(datetime);
		egu.getColumn("huaysj").setDefaultValue(
				DateUtil.FormatDateTime(new Date()));

		// Toolbar tb1 = new Toolbar("tbdiv");
		// egu.setGridSelModel(3);
		egu.addTbarText("��λ����:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// �Ƿ���ʾ��������
		if (isShowRq) {
			egu.addTbarText("��������:");
			DateField df = new DateField();
			df.setValue(this.getRiq());
			df.Binding("Caiyrq", "");// ��htmlҳ�е�id��,���Զ�ˢ��
			egu.addToolbarItem(df.getScript());
			egu.addTbarText("-");
		}
		if (visit.getDiancmc().equals("��������")) {
			egu.addTbarText("��������:");
			DateField zhiyrq = new DateField();
			zhiyrq.Binding("Zhiyrq", "");
			zhiyrq.setValue(this.getZhiyrq());
			egu.addToolbarItem(zhiyrq.getScript());
			egu.addTbarText("-");
		}
		String treepanel =
			"var navtree = new Ext.tree.TreePanel({\n" +
			//"\t    title: '������Ա',\n" + 
			//"\t    region: 'east',\n " + 
			"\t    autoScroll:true,\n" + 
			"\t    rootVisible:false,\n" + 
			"\t    width: 200,\n" + 
			"\t    autoHeight:true," +
			"\t   \troot:navTree0,\n" + 
			"    \tlisteners : {\n" + 
			"    \t\t'dblclick':function(node,e){\n" + 
			"    \t\t\tif(node.getDepth() == 3){\n" + 
			"    \t\t\t\tvar Cobj = document.getElementById('CHANGE');\n" + 
			"        \t\t\tCobj.value = node.id;\n" + 
			"    \t\t\t}else{\n" + 
			"    \t\t\t\te.cancel = true;\n" + 
			"    \t\t\t}\n" + 
			"    \t\t},'checkchange': function(node,checked){\n" + 
			"\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\taddNode(node);\n" + 
			"\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\tsubNode(node);\n" + 
			"\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\tnode.expand();\n" + 
			"\t\t\t\t\tnode.attributes.checked = checked;\n" + 
			"\t\t\t\t\tnode.eachChild(function(child) {\n" + 
			"\t\t\t\t\t\tif(child.attributes.checked != checked){\n" + 
			"\t\t\t\t\t\t\t/*if(checked){\n" + 
			"\t\t\t\t\t\t\t\taddNode(child);\n" + 
			"\t\t\t\t\t\t\t}else{\n" + 
			"\t\t\t\t\t\t\t\tsubNode(child);\n" + 
			"\t\t\t\t\t\t\t}*/\n" + 
			"\t\t\t\t\t\t\tchild.ui.toggleCheck(checked);\n" + 
			"\t\t\t            \tchild.attributes.checked = checked;\n" + 
			"\t\t\t            \tchild.fireEvent('checkchange', child, checked);\n" + 
			"\t\t\t\t\t\t}\n" + 
			"\t\t\t\t\t});\n" + 
			"\t\t\t\t}\n" + 
			"    \t},\n" + 
			"\t   \ttbar:[{text:'ȷ��',handler:function(){\n" + 
			"        var cs = navtree.getChecked();\n" + 
			"        rec = gridDiv_grid.getSelectionModel().getSelected();\n"+
			"        var tmp=\"\";\n" + 
			"        var tmp2='';\n" + 
			"         if(cs==null){win.hide(this);return;}\n"+
			"        else{for(var i = 0; i< cs.length; i++) {\n" + 
			"        \tif(cs[i].isLeaf()){\n" + 
			"\t\t\t\ttmp = cs[i].text;\n" + 
			"        \t\ttmp2=(tmp2?tmp2+',':'')+tmp;\n" + 
			"\n" + 
			"           }\n" + 
			"        }\n" + 
			"        rec.set('HUAYY',tmp2);\n" + 
			"        win.hide(this);\n"+
			"\n" + 
			"        }}}]\n" + 
			"\t});";
		
		String Strtmpfunction = 

			" win = new Ext.Window({\n" + 
			" title: '������Ա',\n " + 
			"            closable:true,closeAction:'hide',\n" + 
			"            width:200,\n" + 
			"            autoHeight:true,\n" + 
			"            border:0,\n" + 
			"            plain:true,\n" + 
			"            items: [navtree]\n" + 
			" });\n";
		

		GridButton refurbish = new GridButton("ˢ��",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton("����", GridButton.ButtonType_SubmitSel,
				"SaveButton", null, null);
		// �����ж�
		
	egu.addOtherScript("gridDiv_grid.on('cellclick',function(own,irow,icol, e){ "
			+ "row = irow; \n"
			+ "if('HUAYY' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
			+"if(!win){"+treepanel+Strtmpfunction+"}"
			+"win.show(this);}});\n");
//		egu.addTbarBtn(zuofei);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		// ��֤�Ƿ����� �������
		String sqltmp = "select * from xitxxb where mingc='����ֵ¼����ʾ��Ʒ���' and zhi='����'";
		ResultSetList rsl2 = con.getResultSetList(sqltmp);
		if (rsl2.next()) {
			egu.getColumn("huaylb").setHidden(true);
		}
		egu.addTbarText("-");
		egu.addTbarText(" ˵������ҳ������¯ú��λ��ֵ�����볧��λ��ֵ������,���޸Ļ���ԭʼ���ݣ�");

		setExtGrid(egu);
		rsl2.close();
		con.Close();
	}

	public String getStr(int col) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Str = "";
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		Str = "specialkey:function(own,e){\n" + "			if(row>0){\n"
				+ "				if(e.getKey()==e.UP){\n"
				+ "					gridDiv_grid.startEditing(row-1, " + col + ");\n"
				+ "					row = row-1;\n" + "				}\n" + "			}\n"
				+ "			if(row+1 < gridDiv_grid.getStore().getCount()){\n"
				+ "				if(e.getKey()==e.DOWN){\n"
				+ "					gridDiv_grid.startEditing(row+1, " + col + ");\n"
				+ "					row = row+1;\n" + "				}\n" + "			}\n";
		if (editable_S) {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=10){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "			} else if (" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<10){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if (" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "			} else if (" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		} else {
			Str += "		if(e.getKey()==e.LEFT){\n" + "			if(" + col + ">5&&"
					+ col + "<=8){\n" + "				gridDiv_grid.startEditing(row, "
					+ col + "-1);\n" + "			} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 8);\n"
					+ " 			} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ " 			} else if(" + col + "==17){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n" + "			}\n"
					+ "		}\n" + "		if(e.getKey()==e.RIGHT){\n" + "			if(" + col
					+ ">=5&&" + col + "<8){\n"
					+ "				gridDiv_grid.startEditing(row, " + col + "+1);\n"
					+ "			} else if(" + col + "==8){\n"
					+ "				gridDiv_grid.startEditing(row, 10);\n"
					+ "	 		} else if(" + col + "==10){\n"
					+ "				gridDiv_grid.startEditing(row, 15);\n"
					+ "	 		} else if(" + col + "==15){\n"
					+ "				gridDiv_grid.startEditing(row, 17);\n" + "			}\n"
					+ "		}\n" + "	  }\n";
		}
		return Str;
	}

	public String getfunction(String binder, String s) {
		String handler = "function(){\n"
				+ "var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n"
				+ "for(i = 0; i< Mrcd.length; i++){\n"
				+ "gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HUAYBH update=\"true\">' + Mrcd[i].get('HUAYBH').replace('<','&lt;').replace('>','&gt;')+ '</HUAYBH>'+ '<HUAYSJ update=\"true\">' + ('object' != typeof(Mrcd[i].get('HUAYSJ'))?Mrcd[i].get('HUAYSJ'):Mrcd[i].get('HUAYSJ').dateFormat('Y-m-d'))+ '</HUAYSJ>'+ '<MT update=\"true\">' + Mrcd[i].get('MT')+ '</MT>'+ '<MAD update=\"true\">' + Mrcd[i].get('MAD')+ '</MAD>'+ '<AAD update=\"true\">' + Mrcd[i].get('AAD')+ '</AAD>'+ '<VAD update=\"true\">' + Mrcd[i].get('VAD')+ '</VAD>'+ '<"
				+ s.toUpperCase()
				+ " update=\"true\">' + Mrcd[i].get('"
				+ s.toUpperCase()
				+ "')+ '</"
				+ s.toUpperCase()
				+ ">'+ '<QBAD update=\"true\">' + Mrcd[i].get('QBAD')+ '</QBAD>'+ '<T1 update=\"true\">' + Mrcd[i].get('T1')+ '</T1>'+ '<T2 update=\"true\">' + Mrcd[i].get('T2')+ '</T2>'+ '<T3 update=\"true\">' + Mrcd[i].get('T3')+ '</T3>'+ '<T4 update=\"true\">' + Mrcd[i].get('T4')+ '</T4>'+  '<LURY update=\"true\">' + Mrcd[i].get('LURY').replace('<','&lt;').replace('>','&gt;')+ '</LURY>'+ '<BEIZ update=\"true\">' +'<HUAYY update=\"true)\">' + Mrcd[i].get('HUAYY').replace('<','&lt;').replace('>','&gt;')+ '</HUAYY>'+ Mrcd[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>'+ '<HUAYLB update=\"true\">' + Mrcd[i].get('HUAYLB').replace('<','&lt;').replace('>','&gt;')+ '</HUAYLB>' + '</result>' ; \n"
				+ "}\n"//
				+ "if(gridDiv_history==''){ \n"
				+ "Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ');\n"
				+ "}else{\n"
				+ "    var Cobj = document.getElementById('CHANGE');\n"
				+ "    Cobj.value = '<result>'+gridDiv_history+'</result>';\n"
				+ "    document.getElementById('"
				+ binder
				+ "').click();\n"
				+ "    Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n"
				+ "}\n" + "}";
		return handler;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(), visit);
	}

	private void Huit() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save2(getChange(), visit);
	}

	private void Zuof() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save3(getChange(), visit);
	}

	private int Judgment(String value) {
		int v = 0;
		if (value.equals(null) || value.equals("")) {
			v = 0;
		} else {
			v = Integer.parseInt(value);
		}
		return v;
	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString7();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString7(nav);
	}
	
	public void initNavigation(){
		Visit visit = (Visit) getPage().getVisit();
		setNavigetion("");
		
//		���������Ĳ�ѯSQL
		String sql=
			"select 0 id,'��' as mingc,1 jib,-1 fuid,0 checked from dual\n" +
			"union\n" + 
			"select r.id, r.quanc  as mingc,2 jib,0 fuid, 0 checked\n" + 
			"  from diancxxb d,renyxxb r\n" + 
			" where\n" + 
			"r.diancxxb_id=d.id\n" + 
			"and r.bum='����' and zhuangt=1 and d.id ="+visit.getDiancxxb_id()+"";
			
		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql,true);
		
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		// �Ƿ�Ϊ���Ƿ���
		boolean isYc = false;
		isYc = "��".equals(MainGlobal.getXitxx_item("����", "�Ƿ�Ϊ���Ƿ���", visit
				.getDiancxxb_id()
				+ "", "��"));
		// ���Ƿ�ɱ༭
		boolean editable_H = Compute.getYuansEditable(con, Compute.yuans_H,
				visit.getDiancxxb_id(), false);
		// ���Ƿ�ɱ༭
		boolean editable_S = Compute.getYuansEditable(con, Compute.yuans_S,
				visit.getDiancxxb_id(), false);
		// ��Ԫ�ط���
		String sign_H = Compute.getYuansSign(con, Compute.yuans_H, visit
				.getDiancxxb_id(), "Had");
		if ("had,hdaf".indexOf(sign_H.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_HNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_HNotFound);
			return;
		}
		// ��Ԫ�ط���
		String sign_S = Compute.getYuansSign(con, Compute.yuans_S, visit
				.getDiancxxb_id(), "Stad");
		if ("stad,std,sdaf".indexOf(sign_S.toLowerCase()) == -1) {
			WriteLog.writeErrorLog(ErrorMessage.Sign_SNotFound + "\n\t\t"
					+ this.getClass().getName());
			setMsg(ErrorMessage.Sign_SNotFound);
			return;
		}

		// �жϵ�½��Ա�Ƿ���ǻ���Ա,�����,���Զ���huayy�ֶδ����½��Ա����
		boolean xiansztq = false;
		String sql2 = "select zhi from xitxxb where mingc = '�볧����¼��Ĭ�ϵ�¼��Ա���ǻ���Ա' and zhuangt = 1 and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = con.getResultSetList(sql2);
		
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("��")) {
				xiansztq = true;
			} else {
				xiansztq = false;
			}
		}
		rsl.close();

		int sacle = Compute.getQnet_arScale(con, visit.getDiancxxb_id());
		StringBuffer sql = new StringBuffer("");
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(strchange);
		long zhillsbid = 0;
		String msg = "";
		while (mdrsl.next()) {
		
			zhillsbid = Long.parseLong(mdrsl.getString("ID"));
			double H = 0;
			if (!isYc) {
				H = mdrsl.getDouble(sign_H);
			}
			double S = mdrsl.getDouble(sign_S);
			double Mt = mdrsl.getDouble("mt");
			double Mad = mdrsl.getDouble("mad");
			double Aad = mdrsl.getDouble("aad");
			double Vad = mdrsl.getDouble("vad");
			double Qbad = mdrsl.getDouble("qbad");
			double Vdaf = 0;
//			boolean failed = false;

			/*
			 * ���ߣ�ww ʱ�䣺2009-09-18 �������ֳ���ͬ��Ĺ�Ӧ����ֵ��ͬ
			 */
			boolean isNotValue = "��".equals(MainGlobal.getXitxx_item("����",
					"�ֳ�ͬ����ֵ��ͬ", visit.getDiancxxb_id() + "", "��"));
			boolean blnFencb = false;
			blnFencb = ((Visit) this.getPage().getVisit()).isFencb();
			if (isNotValue && blnFencb) {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"), blnFencb, getTreeid());
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
//							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"),
							blnFencb, getTreeid());
					if (S == -1) {
						msg += "����:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
//						failed = true;
					}
				}

			} else {
				if (isYc) {
					if ((100 - Mad - Aad) == 0) {
						Vdaf = 0;
					} else {
						Vdaf = CustomMaths.Round_new(
								(Vad * 100 / (100 - Mad - Aad)), 2);
					}
					H = CustomMaths.Round_new((100 - Mad - Aad) / 100 * Vdaf
							/ (0.1462 * Vdaf + 1.1124), 2);
				} else {
					if (!editable_H) {
						H = Compute.getYuansValue(con, zhillsbid, visit
								.getDiancxxb_id(), sign_H, mdrsl
								.getDouble("vad"));
						if (H == -1) {
							msg += "����:" + mdrsl.getString("huaybh") + " "
									+ sign_H + " "
									+ ErrorMessage.getYuansfsFailed;
//							failed = true;
						}
					}
				}
				if (!editable_S) {
					S = Compute.getYuansValue(con, zhillsbid, visit
							.getDiancxxb_id(), sign_S, mdrsl.getDouble("vad"));
					if (S == -1) {
						msg += "����:" + mdrsl.getString("huaybh") + " " + sign_S
								+ " " + ErrorMessage.getYuansfsFailed;
//						failed = true;
					}
				}
			}
			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			
			if ("��¯".equals(mdrsl.getString("huaylb"))) {
				//��ѯ���кͱ���������ͬ��caib_id Ȼ���������rulmzmxb
				sql.append("update rulmzlzmxb set ");
				sql.append("fenxrq").append("=").append(
						"to_date('" + mdrsl.getString("HUAYSJ")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("shenhzt=7,");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));

			}

			else {// �볧��������

				sql.append("update zhillsb set ");
				sql.append("huaysj").append("=").append(
						"to_date('" + mdrsl.getString("Huaysj")
								+ "','YYYY-MM-DD hh24:mi:ss'),").append("shenhzt=7, ");
				sql.append(Compute.ComputeValue(Mt, Mad, Aad, Vad, Qbad,
						sign_H, H, sign_S, S, sacle));
				sql.append("t1").append(
						"=" + Judgment(mdrsl.getString("t1")) + ",");
				sql.append("t2").append(
						"=" + Judgment(mdrsl.getString("t2")) + ",");
				sql.append("t3").append(
						"=" + Judgment(mdrsl.getString("t3")) + ",");
				sql.append("t4").append(
						"=" + Judgment(mdrsl.getString("t4")) + ",");
			}
			// ���ܱ�,�жϻ���ֵ¼��ʱ,��½��Ա�Ƿ���ǻ���Ա
			if (xiansztq) {
				sql.append("huayy").append("='" + visit.getRenymc() + "',");
			} else {
				sql.append("huayy").append(
						"='" + mdrsl.getString("huayy") + "',");
			}
			String caiyb_id=mdrsl.getString("id");

			sql.append("beiz").append("='" + mdrsl.getString("beiz") + "' ");
			if("��¯".equals(mdrsl.getString("huaylb"))){
				
				sql.append("where zhuanmbzllsb_id =").append(caiyb_id).append("\n");//id
				con.getUpdate(sql.toString());
				StringBuffer sql_rl = new StringBuffer("begin \n");
				
				//	///////////////////////////////////////////////
//				String caiyid="";
				String zhillsb_id=mdrsl.getString("id");//ȡ��������ʱ��id
				String ru_sql=" select * from rulmzlzmxb where zhuanmbzllsb_id="+zhillsb_id;
				ResultSetList ru_rsl=con.getResultSetList(ru_sql);//��ѯ��ǰ��rulmzlzmxb ��id������ѡ���
				int count =0;
				while (ru_rsl.next()&&count<1){
					count++;
					String zhiyry=
						"select rb.id,rb.quanc from rulmzlzmxb rzb ,yangpdhb yb,zhiyryglb zb,renyxxb rb\n" +
						"where yb.zhilblsb_id=rzb.id and yb.id=zb.yangpdhb_id and zb.renyxxb_id=rb.id\n" + 
						"and rzb.id="+zhillsb_id;
					ResultSetList zy_rsl=con.getResultSetList(zhiyry);
					String zhiyy="";
					while(zy_rsl.next()){
						zhiyy=zy_rsl.getString("quanc");
					}
					
					
					String jiz="0";
//					String rulbzb_id="0";
					if("".equals(ru_rsl.getString("jizfzb_id"))||ru_rsl.getString("jizfzb_id")==null){
						ru_rsl.getString("jizfzb_id");
					}
//					if("".equals(ru_rsl.getString("rulbzb_id"))||ru_rsl.getString("rulbzb_id")==null){
//						
//					}else{
//						rulbzb_id=ru_rsl.getString("rulbzb_id");
//					}
					
//					long bin2=Integer.parseInt(ru_rsl.getDateTimeString("rulrq").substring(0, 10).replaceAll("-", ""));
//					Date d=new Date(bin2);
					int da=0;
//					String caiysj=String.valueOf(d.getTime());//getChangerq();
					String shijd=MainGlobal.getXitxx_item("����", "�볧��¯�ڵ�ʱ��", "0", "0");
					if(Integer.parseInt(ru_rsl.getDateTimeString("rulrq").substring(11, 13))>=Integer.parseInt(shijd)){
//						caiysj=String.valueOf(d.getTime()-1);
						da=1;
					}
					//��ѯ����caiyb_id ��ͬ��rulmzlzmxb��ȡ��ú�����ۼ�
					String caiy="select * from rulmzlzmxb where caiyb_id="+ru_rsl.getString("caiyb_id");
					ResultSetList rsl_caiy=con.getResultSetList(caiy);
					double totalmeil=0;
					boolean flag_meil=false;
					while(rsl_caiy.next()){
						//��ú�����ۼ�
						totalmeil+=	rsl_caiy.getDouble("meil");
						flag_meil=true;
					}
					if(!flag_meil){
						//����û��ú��
						totalmeil=	ru_rsl.getDouble("meil");
					}
					
					String ruf = ru_rsl.getString("id") + ",to_date('"+ru_rsl.getDateTimeString("rulrq").substring(0, 10)+"', 'YYYY-MM-DD')+"+da+",to_date('"+ru_rsl.getDateTimeString("fenxrq")+"', 'YYYY-MM-DD hh24:mi:ss'),"+visit.getDiancxxb_id()+","//1
					+ 0 + ","+jiz+","+ru_rsl.getString("qnet_ar")+","+ru_rsl.getString("aar")+","//2
					+ ru_rsl.getString("ad") + ","+ru_rsl.getString("vdaf")+","+ru_rsl.getString("mt")+","+ru_rsl.getString("stad")+","//3
					+ ru_rsl.getString("aad")+","+ru_rsl.getString("mad")+","+ru_rsl.getString("qbad")+","+ru_rsl.getString("had")+","//4
					+ru_rsl.getString("vad")+","+ru_rsl.getString("fcad")+","+ru_rsl.getString("std")+","+ru_rsl.getString("qgrad")+","//5
					+ru_rsl.getString("hdaf")+","+ru_rsl.getString("sdaf")+","+ru_rsl.getInt("var")+",'"+ru_rsl.getString("huayy")+"','"//6
					+ru_rsl.getString("beiz")+"','"+ru_rsl.getString("lury")+"',to_date('"+ru_rsl.getDateTimeString("shangmkssj")+"', 'YYYY-MM-DD hh24:mi:ss'),"+"7,'"//7
					+ru_rsl.getString("bianm")+"',"+ru_rsl.getString("har")+","+ru_rsl.getString("qgrd")+","+ru_rsl.getString("qgrad_daf")+","//8
					+totalmeil+",'"+zhiyy+"','"+visit.getRenymc()+"',"+"to_date(to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')"+",'"//9
					+visit.getRenymc()+"',"+"to_date(to_char(sysdate, 'yyyy-mm-dd hh24:mi:ss'),'yyyy-mm-dd hh24:mi:ss')"+",1";

					String sql_gz=" select * from rulmzlb where rulrq= to_date('"+
					ru_rsl.getDateTimeString("rulrq").substring(0, 10)+" ','yyyy-mm-dd')+"+da+" ";//���������
					ResultSetList rsl_gz=con.getResultSetList(sql_gz);
					String gouz="";
					String aa="";

					double meil=0;
					boolean flag =false;
					while(rsl_gz.next()){//������ڶ���Ӧ����һ������Ӧ��ͬ��caiyb_id
						gouz= 
							"select\n" +
							"    decode(sum(meil),0,0,round_new(sum(meil*qnet_ar)/sum(meil),2)) as qnet_ar,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*aar)/sum(meil),2)) as aar,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*ad)/sum(meil),2)) as ad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*vdaf)/sum(meil),2)) as vdaf,\n" + 
							"    decode(sum(meil),0,0,round_new(sum(meil*mt)/sum(meil),2)) as mt,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*stad)/sum(meil),2)) as stad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*aad)/sum(meil),2)) as aad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*mad)/sum(meil),2)) as mad,\n" + 
							"    decode(sum(meil),0,0,round_new(sum(meil*qbad)/sum(meil),2)) as qbad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*had)/sum(meil),2)) as had,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*vad)/sum(meil),2)) as vad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*fcad)/sum(meil),2)) as fcad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*std)/sum(meil),2)) as std,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*var)/sum(meil),2)) as var,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*har)/sum(meil),2)) as har,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*hdaf)/sum(meil),2)) as hdaf,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*sdaf)/sum(meil),2)) as sdaf,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*qgrad)/sum(meil),2)) as qgrad,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*qgrd)/sum(meil),2)) as qgrd,\n" + 
							"     decode(sum(meil),0,0,round_new(sum(meil*qgrad_daf)/sum(meil),2)) as qgrad_daf\n" + 
							" from\n" + 
							"(--ú����\n" + 
							"      select 1 as id, "+rsl_gz.getDouble("meil")+" as meil from dual\n" + //����ԭ�е�
							"        union\n" + 
							"      select 2 as id ,"+totalmeil+" as meil from dual\n" + //Ҫ���ӵ�
							"\n" +  //
							"       ) fdl,\n" + 
							"(---����ָ���\n" + //����ԭ�е�
							"select 1 as id, "+rsl_gz.getDouble("qnet_ar")+" as qnet_ar ,"+rsl_gz.getDouble("aar")+" as aar,"
							+rsl_gz.getDouble("ad")+" as ad,"+rsl_gz.getDouble("vdaf")+" as vdaf,"+rsl_gz.getDouble("mt")+
							" as mt,"+rsl_gz.getDouble("stad")+" as stad,"+rsl_gz.getDouble("aad")+" as aad,"+rsl_gz.getDouble("mad")+
							" as mad,"+rsl_gz.getDouble("qbad")+" as qbad" +
							","+rsl_gz.getDouble("had")+" as had,"+rsl_gz.getDouble("vad")+" as vad, "+rsl_gz.getDouble("fcad")+
							" as fcad,"+rsl_gz.getDouble("std")+" as std,"+rsl_gz.getDouble("qgrad")+" as qgrad,"
							+rsl_gz.getDouble("hdaf")+" as hdaf,"+rsl_gz.getDouble("sdaf")+" as sdaf ,"+rsl_gz.getDouble("var")+
							" as var,"+rsl_gz.getDouble("har")+" as har,"+rsl_gz.getDouble("qgrd")+" as qgrd,"+rsl_gz.getDouble("qgrad_daf")+
							" as qgrad_daf from dual\n" + 
							"        union\n" +  //Ҫ���ӵ�
							"   select  2 as id,  "+ru_rsl.getDouble("qnet_ar")+" as qnet_ar ,"+ru_rsl.getDouble("aar")+" as aar,"
							+ru_rsl.getDouble("ad")+" as ad,"+ru_rsl.getDouble("vdaf")+" as vdaf,"+ru_rsl.getDouble("mt")+
							" as mt,"+ru_rsl.getDouble("stad")+" as stad,"+ru_rsl.getDouble("aad")+" as aad,"+ru_rsl.getDouble("mad")+
							" as mad,"+ru_rsl.getDouble("qbad")+" as qbad" +
							","+ru_rsl.getDouble("had")+" as had,"+ru_rsl.getDouble("vad")+" as vad, "+ru_rsl.getDouble("fcad")+
							" as fcad,"+ru_rsl.getDouble("std")+" as std,"+ru_rsl.getDouble("qgrad")+" as qgrad,"
							+ru_rsl.getDouble("hdaf")+" as hdaf,"+ru_rsl.getDouble("sdaf")+" as sdaf ,"+ru_rsl.getDouble("var")+
							" as var,"+ru_rsl.getDouble("har")+" as har,"+ru_rsl.getDouble("qgrd")+" as qgrd,"+ru_rsl.getDouble("qgrad_daf")+
							" as qgrad_daf from dual\n" + 
							"\n" + 
							"   ) mh\n" + 
							"\n" + 
							"where\n" + 
							" fdl.id=mh.id";

						flag=true;
						aa=rsl_gz.getString("id");
						meil+=rsl_gz.getDouble("meil");//�Ѿ����ڵ�
					}
					
					if(flag){//�����ݣ�����
						ResultSetList rsl_g=con.getResultSetList(gouz);
						
						while(rsl_g.next()){
							sql_rl.append(" update rulmzlb set qnet_ar="+rsl_g.getDouble("qnet_ar")+",aar="+rsl_g.getDouble("aar")+"," +
									" ad="+rsl_g.getDouble("ad")+", vdaf="+rsl_g.getDouble("vdaf")+", mt="+
									rsl_g.getDouble("mt")+", stad="+rsl_g.getDouble("stad")+", aad="+rsl_g.getDouble("aad")+
									", mad="+rsl_g.getDouble("mad")+", qbad="+rsl_g.getDouble("qbad")+"," +
									"had="+rsl_g.getDouble("had")+", vad="+rsl_g.getDouble("vad")+", fcad="+rsl_g.getDouble("fcad")+
									", std="+rsl_g.getDouble("std")+", qgrad="+rsl_g.getDouble("qgrad")+", hdaf="
									+rsl_g.getDouble("hdaf")+", sdaf="+rsl_g.getDouble("sdaf")+", var="+rsl_g.getDouble("var")+
									",har="+rsl_g.getDouble("har")+",qgrd="+rsl_g.getDouble("qgrd")+",qgrad_daf="+
									rsl_g.getDouble("qgrad_daf")+",meil="+(totalmeil+meil)+//meil�Ѿ����ڵģ�totalmeil�������ӵ�
									" where id="+aa+";\n");
						}
						
					}else{//û�����ݣ���ô��Ҫinsert
						sql_rl.append("insert into rulmzlb(id, rulrq, fenxrq, diancxxb_id, rulbzb_id, jizfzb_id,"+//6
								"qnet_ar, aar, ad, vdaf, mt, stad, aad, mad, qbad,"+//9
								"had, vad, fcad, std, qgrad, hdaf, sdaf, var,huayy,beiz,lury,lursj,"+//12
								"shenhzt,bianm,har,qgrd,qgrad_daf,meil,zhiyr,shenhryyj,shenhsjyj,shenhryej ,shenhsjej,"+//11star,shenhryej ,shenhsjej
								"erjshzt"+//2
						") values (").append(ruf).append(");\n");
					}

				}
				sql_rl.append(" end;");
				
				int flag = 0;
				if(sql_rl.length()>13){
					flag = con.getInsert(sql_rl.toString());
				}
				if (flag == -1) {
					con.rollBack();
					con.Close();
					WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
							+ sql_rl);
					return;
				}
			/////////////////////////////////////////////////////
			}else{
				sql.append("where id =").append(mdrsl.getString("ID")).append("\n");//id
				con.getUpdate(sql.toString());
			}
			
			//���zhillsb������
			String sqlz="select * from zhillsb z where z.id=" +mdrsl.getString("ID");
			ResultSetList rsllz=con.getResultSetList(sqlz);
			
			while(rsllz.next()){				
				String sqll=
					"select f.gongysb_id from fahb f\n" +
					"where f.zhilb_id="+rsllz.getString("zhilb_id")+" and\n" + 
					"f.gongysb_id in\n" + 
					"( select zhi from xitxxb\n" + 
					" where  mingc ='�볧������˹�Ӧ��'\n" + 
					" and leib='�������'\n" + 
					" and zhuangt=1 and beiz='ʹ��'\n" + 
					"  )";
				ResultSetList rsll=con.getResultSetList(sqll);
				while(rsll.next()){
					String strsql="begin \n";
					strsql += "insert into zhilb (id,huaybh,caiyb_id,huaysj,qnet_ar,aar,ad,vdaf,mt,stad,aad,mad,qbad" +
					",had,vad,fcad,std,qgrad,qgrd,hdaf,qgrad_daf,sdaf,t1,t2,t3,t4,huayy,lury,beiz,shenhzt,liucztb_id)"
					+"values("+rsllz.getString("zhilb_id")+",'"+mdrsl.getString("huaybh")+"',"+"3,"
					+ DateUtil.FormatOracleDate(rsllz.getDateString("huaysj"))+","+rsllz.getString("qnet_ar") + ","//DateUtil.FormatOracleDate()
					+ rsllz.getString("aar") + "," + rsllz.getString("ad") + "," + rsllz.getString("vdaf") + ","
					+ rsllz.getString("mt") + "," + rsllz.getString("stad") + "," + rsllz.getString("aad") + ","
					+ rsllz.getString("mad") + "," + rsllz.getString("qbad") + "," + rsllz.getString("had") + ","
					+ rsllz.getString("vad") + "," + rsllz.getString("fcad") + "," + rsllz.getString("std") + ","
					+ rsllz.getString("qgrad") + ","+rsllz.getString("qgrd")+"," + rsllz.getString("hdaf") + "," + rsllz.getString("qgrad_daf") + ","
					+ rsllz.getString("sdaf") + ",";
					if (rsllz.getString("t1")==null ||rsllz.getString("t1").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t1")+",";
					}
					
					if (rsllz.getString("t2")==null ||rsllz.getString("t2").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t2")+",";
					}
					if (rsllz.getString("t3")==null ||rsllz.getString("t3").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t3")+",";
					}
					if (rsllz.getString("t4")==null ||rsllz.getString("t4").equals("")){
						strsql=strsql+"0,";
					}else{
						strsql=strsql+rsllz.getString("t4")+",";
					}
					
					strsql=strsql+"'" + rsllz.getString("huayy") + "','"
					+ rsllz.getString("LURY") + "','" + rsllz.getString("beiz") + "',1,1);\n";
					strsql += " end;";
					con.getInsert(strsql);
			}
			}
//			zhilbid = mdrsl.getString("ID");
			sql.delete(0, sql.length());
//			strsql="";
		}
		setMsg(msg);
		con.Close();
	}

	public void Save2(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		String tableName = "zhillsb";
		String huaysj = "huaysj";
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("��¯")) {
				tableName = "rulmzlzmxb";
				huaysj = "rulrq";
			}

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append(huaysj).append("= null,");
			sql.append("qnet_ar").append("=0,");
			sql.append("aar").append("=0,");
			sql.append("ad").append("=0,");
			sql.append("vdaf").append("=0,");
			sql.append("mt").append("=0,");
			sql.append("stad").append("=0,");
			sql.append("aad").append("=0,");
			sql.append("mad").append("=0,");
			sql.append("qbad").append("=0,");
			sql.append("had").append("=0,");
			sql.append("vad").append("=0,");
			sql.append("fcad").append("=0,");
			sql.append("std").append("=0,");
			sql.append("qgrad").append("=0,");
			sql.append("hdaf").append("=0,");
			sql.append("qgrad_daf").append("=0,");
			sql.append("sdaf").append("=0,");
			if (mdrsl.getString("huaylb").equals("��¯")) {

				// û��t1-t4
			} else {// /�볧�Լ�������
				sql.append("t1").append("=0,");
				sql.append("t2").append("=0,");
				sql.append("t3").append("=0,");
				sql.append("t4").append("=0,");
			}
			sql.append("shenhzt").append("=0,");
			sql.append("huayy").append("=null,");
			sql.append("lury").append("=null,");
			sql.append("beiz").append("=null ");
			if(mdrsl.getString("huaylb").equals("��¯")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id")).append(";\n");
			}else{
				sql.append("where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append(" end;");
		
		con.getUpdate(sql.toString());
	}

	public void Save3(String strchange, Visit visit) {
		String tableName = "zhillsb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			if (mdrsl.getString("huaylb").equals("��¯")) {
				tableName = "rulmzlzmxb";
			}

			String id = mdrsl.getString("id");
			MainGlobal.LogOperation(con, visit.getDiancxxb_id(), visit
					.getRenymc(), SysConstant.RizOpType_UP,
					SysConstant.RizOpMokm_Huayzlu, "zhillsb", id);
			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append("=-1 ");
			if(mdrsl.getString("huaylb").equals("��¯")){
				sql.append("where zhuanmbzllsb_id =").append(mdrsl.getString("id")).append(";\n");
			}else{
				sql.append("where id =").append(mdrsl.getString("ID")).append(";\n");
			}
		}
		sql.append(" end;");
		con.getUpdate(sql.toString());
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

	private boolean _ZuofChick = false;

	public void ZuofButton(IRequestCycle cycle) {
		_ZuofChick = true;
	}

	private boolean _SavebChick = false;

	public void SavebButton(IRequestCycle cycle) {
		_SavebChick = true;
	}

	public void Saveb() {

		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	public void submit(IRequestCycle cycle) {

		if (_SavebChick) {
			_SavebChick = false;
			Saveb();

			getSelectData();
		}

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

		}
		if (_ZuofChick) {
			_ZuofChick = false;
			Zuof();

		}

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
			// �ڴ����ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setExtGrid(null);
			visit.setString7(null);
			visit.setString6(DateUtil.FormatDate(new Date()));
			riq1 = null;
			riq2 = null;
		}
		initNavigation();
		getSelectData();
	}

	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString5() == null
				|| ((Visit) this.getPage().getVisit()).getString5().equals("")) {

			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null
				&& !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}
	
	// ������
	private String riq1;

	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}

	public void setRiq1(String riq1) {
		if (this.riq1 != null && !this.riq1.equals(riq1)) {
			this.riq1 = riq1;
		}
	}

	private String riq2;
	public String getRiq2(){
		if(riq2 == null || riq2.equals("")){
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}
	public void setRiq2(String riq2){
		if(this.riq2 != null && !this.riq2.equals(riq2)){
			this.riq2 = riq2;
		}
	}

	// �������ڰ�,�����糧��
	public void setZhiyrq(String zhiyrq) {
		if (((Visit) this.getPage().getVisit()).getString6() != null
				&& !((Visit) this.getPage().getVisit()).getString6().equals(
						zhiyrq)) {

			((Visit) this.getPage().getVisit()).setString6(zhiyrq);
		}
	}

	public String getZhiyrq() {
		if (((Visit) this.getPage().getVisit()).getString6() == null
				|| ((Visit) this.getPage().getVisit()).getString6().equals("")) {

			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}
}