/*
 * ���ڣ�2016��6��23��
 * ���ߣ�Qiuzw
 * �޸����ݣ����Ӵ���Ҧ�ϵ糧����ú���š��Ĺ���
 * */

/*
 * ���ڣ�2013-3-13
 * ���ߣ�Qiuzw
 * ��������¼�Ǳ�ǰ������Ĳ�ֵ��visit�������¼��
* */

/*
 * ���ڣ�2012��4��25��
 * ���ߣ�Qiuzw
 * �޸����ݣ�����ҳ�������ʼ������������jdbc���Ӳ������ṩ����ִ��Ч��
 * */

/*
 * ���ڣ�2010��7��27��
 * ���ߣ�Qiuzw
 * �޸����ݣ�����ӯ��������Ĺ�ʽ�����⡣��ʱû�е糧�õ�����������
 * */

/*
 * ʱ�䣺2009��3��16��
 * ���ߣ�Qiuzw
 * �޸����ݣ���ʼ��ҳ��ʱ����ʾ������δ��Ƥ�����ߡ��ֹ��˻ء��Ļ�Ƥ��Ϣ
 */
/*
 * ʱ�䣺2008-07-30
 * ���ߣ�Qiuzw
 * ����������ͨ�������������������������ҳ���ϵ���ʾ������ť�������û��Ƿ��ʹ��ҳ���ϵġ���ʾ��������ť
 *       ��ͷ�糧���
 */
/*
 * ʱ�䣺2008-06-09
 * ���ߣ�Qiuzw
 * ������1����ԭ���ڷ��������жϵ�ë��ȡ�����������ͻ��ˡ�ͬʱ�ܽ���������ݴ���
 *       2��ҳ��ÿ�μ���ʱ���ɸ��������Ŷ�Ӧ�İ����ţ����ڱ�������ʱ����ʾ��Ϣ��
 * Ӱ��糧��
 *       ��ͷ�糧�޸�
 */
/*
 * ʱ�䣺2008-05-25
 * ���ߣ�Qiuzw
 * ���������ڱ�������ʱ���д�����ʱ��û�н�commit״̬�޸�Ϊtrue�����ܵ������ݶ�ȡ����
 */
/*
 * ʱ�䣺2008-05-16
 * ���ߣ�Qiuzw
 * ������ҳ���ʼ��ʱ����ϵͳ�����еĺ���������ֵ��ҳ�����
 */
package com.zhiren.dc.jilgl.qichmz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.Arith;
import com.zhiren.common.CustomDate;
import com.zhiren.common.CustomMaths;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.IDropDownSelectionModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysInfo;
import com.zhiren.main.Visit;

public  class Qichmz extends BasePage{

	public String getCommPort() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setCommPort(String port) {
		((Visit) getPage().getVisit()).setString1(port);
	}

	public String getBaudRate() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setBaudRate(String port) {
		((Visit) getPage().getVisit()).setString2(port);
	}

	public String getDataBits() {
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setDataBits(String port) {
		((Visit) getPage().getVisit()).setString3(port);
	}

	public String getStopBits() {
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setStopBits(String port) {
		((Visit) getPage().getVisit()).setString4(port);
	}

	public String getParitySetting() {
		return ((Visit) getPage().getVisit()).getString5();
	}

	public void setParitySetting(String port) {
		((Visit) getPage().getVisit()).setString5(port);
	}

	public String getJilhh() {
		return ((Visit) getPage().getVisit()).getString6();
	}

	public void setJilhh(String jilhh) {
		((Visit) getPage().getVisit()).setString6(jilhh);
	}

	public boolean getMaozJudge() {
		return ((Visit) getPage().getVisit()).getboolean1();
	}

	public void setMaozJudge(boolean judge) {
		((Visit) getPage().getVisit()).setboolean1(judge);
	}

	public boolean getPizJudge() {
		return ((Visit) getPage().getVisit()).getboolean2();
	}

	public void setPizJudge(boolean judge) {
		((Visit) getPage().getVisit()).setboolean2(judge);
	}

	public boolean getShowMsg() {
		return ((Visit) getPage().getVisit()).getboolean3();
	}

	public void setShowMsg(boolean show) {
		((Visit) getPage().getVisit()).setboolean3(show);
	}

	public boolean getAutoPiz() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setAutoPiz(boolean auto) {
		((Visit) getPage().getVisit()).setboolean4(auto);
	}

	public boolean getAutoCopy() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setAutoCopy(boolean auto) {
		((Visit) getPage().getVisit()).setboolean5(auto);
	}

	public long getCurrentPageNumber() {
		return ((Visit) getPage().getVisit()).getLong1();
	}

	public void setCurrentPageNumber(long currentnubmer) {
		((Visit) getPage().getVisit()).setLong1(currentnubmer);
	}

	public long getTotalPageNumber() {
		return ((Visit) getPage().getVisit()).getLong2();
	}

	public void setTotalPageNumber(long totalnubmer) {
		((Visit) getPage().getVisit()).setLong2(totalnubmer);
	}

	public long getGoPageNumber() {
		return ((Visit) getPage().getVisit()).getLong3();
	}

	public void setGoPageNumber(long gonumber) {
		((Visit) getPage().getVisit()).setLong3(gonumber);
	}

//	private double zhonglybc = 0.0;

	public String getZhonglybc() {
		return ((Visit)getPage().getVisit()).getString7();
	}

	public void setZhonglybc(String z) {
		((Visit)getPage().getVisit()).setString7(z);
	}

	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	protected void initialize() {
//		super.initialize();
		_msg = "";
	}

	/*
	 * ��ʼ��ë�س����ж���Ƥ�س����ж���������ʾ�������Ƥ�� �ĸ�����
	 */
	public void InitParameter(JDBCcon con) {
//		JDBCcon con = new JDBCcon();
		long longValue = -1;
		try {
			String sql = "select mingc from jicxxb "
					+ "where leix='���������ݹ�������' and zhuangt=1";
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				longValue = rs.getLong("mingc");
			}
			rs.close();
			if (longValue == -1) {
				setMaozJudge(false);
				setPizJudge(false);
				setShowMsg(false);
				setAutoPiz(false);
			} else {
				setMaozJudge(longValue / 1000 == 1);
				setPizJudge(longValue % 1000 / 100 == 1);
				setShowMsg(longValue % 100 / 10 == 1);
				setAutoPiz(longValue % 10 == 1);
			}
			sql = "select * from jicxxb "
					+ "where leix='�����������Զ�����' and mingc = '��' and zhuangt=1";
			rs = con.getResultSet(sql);
			if (rs.next()) {
				setAutoCopy(true);
			} else {
				setAutoCopy(false);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			con.Close();
		}
	}

	/*
	 * �����ݿ�����ȡ���� д��js
	 */
	public String getArrayScript() {
		JDBCcon con = new JDBCcon();
		StringBuffer array = new StringBuffer();
		StringBuffer sbSql = new StringBuffer();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql = "";
		try {
			int BaudRate = 4800;
			String jiojy = "";
			int shujw = 0;
			int tingzw = 0;
			int isTest = 0;
			int zhonglc = 2;
			int commport = 1;
			sql = "select jiesbz,mingc from jicxxb where leix= '������ӿڴ��ں�' and diancxxb_id="
					+ diancid;
			ResultSet wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				commport = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '������ӿڲ�����' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				BaudRate = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '������ӿ���żУ��' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				jiojy = wsrs.getString("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '������ӿ�����λ' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				shujw = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '������ӿ�ֹͣλ' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				tingzw = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '����������Բ���' and zhuangt=1 and  diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				isTest = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix='��������ɲɼ�������' and zhuangt=1 and diancxxb_id ="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				zhonglc = wsrs.getInt("mingc");
			}
			wsrs.close();
			array.append(" drop = new Array();\n ");
			array.append(" drop1 = new Array();\n");
//			sbSql
//					.append("select 0,m.id ,m.meikdqmc,nvl(m.pinysy,0) pinysy,m.xuh from meikdqb m,changkglb c where c.zhuangt=1 and c.diancxxb_id= "
//							+ diancid + " and c.meikxxb_id = m.id order by xuh");
//
			sbSql.append("select 0, id, mingc meikdqmc,0 pinysy, xuh\n" +
                    "  from gongysb \n" +
                    " where zhuangt = 1\n" +
                    " order by mingc");
			ResultSet sfrs = con.getResultSet(sbSql);
			int i = 0;
			while (sfrs.next()) {
				array.append("drop1[");
				array.append(i++);
				array.append("] = new Array(\"");
				array.append(sfrs.getString(1));
				array.append("\",\"");
				array.append(sfrs.getString(2));
				array.append("\",\"");
				array.append(sfrs.getString(3));
				array.append("\",\"");
				array.append(sfrs.getString(4));
				array.append("\");\n");
			}
			sfrs.close();
			array.append(" drop[0] = new Array('MEIKDQMC_M',drop1);\n");
			array.append(" drop2 = new Array();\n");
			sbSql = new StringBuffer();
//			sbSql
//					.append("select m.meikdqb_id,m.id,m.meikdwmc,nvl(m.pinysy,0) pinysy from meikxxb m,changkglb c where c.zhuangt=1 and c.diancxxb_id= "
//							+ diancid + " and c.meikxxb_id = m.id order by xuh");
			sbSql.append("select m.meikdqb_id, m.id, m.mingc meikdwmc, nvl(m.pinysy, 0) pinysy\n" +
                    "  from meikxxb m\n" +
                    " where zhuangt = 1\n" +
                    " order by mingc");
			ResultSet kwjrs = con.getResultSet(sbSql);
			i = 0;
			while (kwjrs.next()) {
				array.append("drop2[");
				array.append(i++);
				array.append("] = new Array(\"");
				array.append(kwjrs.getString(1));
				array.append("\",\"");
				array.append(kwjrs.getString(2));
				array.append("\",\"");
				array.append(kwjrs.getString(3));
				array.append("\",\"");
				array.append(kwjrs.getString(4));
				array.append("\");\n");
			}
			kwjrs.close();
			array.append(" drop[1] = new Array('MEIKDWMC_M',drop2);\n");
			array.append(" drop3 = new Array();\n");
			ResultSet mkkj = con.getResultSet("select m.meikdwmc, j.mingc "
					+ "from meikxxb m, jihkjb j, changkglb c "
					+ " where m.jihkjb_id = j.id(+) and m.id = c.meikxxb_id "
					+ " and c.zhuangt = 1 and c.diancxxb_id = " + diancid);
			i = 0;
			array.append("mkkj = new Array();\n");
			while (mkkj.next()) {
				array.append("mkkj[");
				array.append(i++);
				array.append("] = new Array(\"");
				array.append(mkkj.getString(1));
				array.append("\",\"");
				array.append(mkkj.getString(2));
				array.append("\");\n");
			}
			mkkj.close();
			if (commport == -1) {
				if (getCommPort().equals("-1")) {
					array
							.append("var commport = window.prompt(\"������˿ں�!\",1);\n");
				} else {
					array.append("var commport = " + getCommPort() + ";\n");
				}
			} else {
				array.append("var commport = " + commport + ";\n");
				setCommPort(String.valueOf(commport));
			}
			if (BaudRate == -1) {
				if (getBaudRate().equals("-1")) {
					array
							.append("var BaudRate = window.prompt(\"�����벨����!\",4800);\n");
				} else {
					array.append("var BaudRate = " + getBaudRate() + ";\n");
				}
			} else {
				array.append("var BaudRate = " + BaudRate + ";\n");
				setBaudRate(String.valueOf(BaudRate));
			}
			if (jiojy.equals("-1")) {
				if (getParitySetting().equals("-1")) {
					array
							.append("var jiojy = window.prompt(\"��������żУ��!\",\"n\");\n");
				} else {
					array.append("var jiojy = " + getParitySetting() + ";\n");
				}
			} else {
				array.append("var jiojy = '" + jiojy + "';\n");
				setParitySetting(String.valueOf(jiojy));
			}
			if (shujw == -1) {
				if (getDataBits().equals("-1")) {
					array.append("var shujw = window.prompt(\"����������λ!\",8);\n");
				} else {
					array.append("var shujw = " + getDataBits() + ";\n");
				}
			} else {
				array.append("var shujw = " + shujw + ";\n");
				setDataBits(String.valueOf(shujw));
			}
			if (tingzw == -1) {
				if (getStopBits().equals("-1")) {
					array
							.append("var tingzw = window.prompt(\"������ֹͣλ!\",1);\n");
				} else {
					array.append("var tingzw = " + getStopBits() + ";\n");
				}
			} else {
				array.append("var tingzw = " + tingzw + ";\n");
				setStopBits(String.valueOf(tingzw));
			}
			array.append("var isTest = " + isTest + ";\n");
			array.append("var zhonglc = " + zhonglc + ";\n");
			SysInfo si = new SysInfo();
			String jilhh = si.getBasicValue("���������Ĭ�ϼ������", "A",con);

			array.append("var jilhh = '" + jilhh + "';\n");

			sbSql.setLength(0);
			sbSql
					.append("SELECT * FROM XITXXB X WHERE X.DUIXM = '��������' And shifsy = 1");
			ResultSet rs = con.getResultSet(sbSql);
			String bianh = "";
			int k = 0;
			CustomDate cd = new CustomDate();
			while (rs.next()) {
				String qichh = rs.getString("zhi");
				String bianhType = rs.getString("danw");
//				String strToday=cd.FormatDate(new Date());
				ResultSet r = con
						.getResultSet("Select ges,Trim(to_char(Max(xuh)+1,'000')) xuh From (select DECODE('"
								+ bianhType
								+ "','������',TO_CHAR(QC.JIANMSJ,'YYYYMMDD') ,TO_CHAR(QC.JIANMSJ,'MMDD')) As ges,\n"
								+ "DECODE(LENGTH(QC.XUH),1,'00'||TO_CHAR(Qc.XUH),2,'0'||TO_CHAR(Qc.XUH),TO_CHAR(Qc.XUH)) AS XUH  "
								+ "from qichjjbtmp QC where jilhh = '"
								+ qichh
//								+ "' AND TO_CHAR(JIANMSJ,'YYYY-MM-DD') = TO_CHAR(SYSDATE,'YYYY-MM-DD')" 
								+ "' AND JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1"
								+") Group By ges");
				if (r.next()) {
					bianh = bianh + "arrBianh[" + k + "] = '" + qichh + "';\n";
					k = k + 1;
					bianh = bianh + "arrBianh[" + k + "] = '"
							+ (r.getString("ges") + r.getString("XUH")) + "';\n";
					k = k + 1;
				} else {
					if (bianhType.equals("������")) {
						bianh = bianh + "arrBianh[" + k + "] = '" + qichh
								+ "';\n";
						k = k + 1;

						bianh = bianh
								+ "arrBianh["
								+ k
								+ "] = '"
								+ (cd.FormatDate(new Date(), "yyyyMMdd") + "001")
								+ "';\n";
						k = k + 1;
					} else {
						bianh = bianh + "arrBianh[" + k + "] = '" + qichh
								+ "';\n";
						k = k + 1;

						bianh = bianh + "arrBianh[" + k + "] = '"
								+ (cd.FormatDate(new Date(), "MMdd") + "001")
								+ "';\n";
						k = k + 1;
					}
				}
				r.close();
				r = null;
			}
			cd = null;
			bianh = "var arrBianh = new Array(4);\n" + bianh;
			rs.close();
			rs = null;
			array.append(bianh);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			con = null;
		}
		return array.toString();
	}

	/*
	 * ��ʼ��ë������
	 */
	public void getInitMaoz(JDBCcon con) {
		String mshouhr = "";
		boolean selected = false;
		String mpandmc = "";
		String mmeic = "";
		String mmeigy = "";
		String mmeikdq = "";
		String mmeikdw = "";
		String mranlpz = "";
		if (getIRanlpzb_idModel().getOptionCount() > 1) {
			mranlpz = ((IDropDownBean) getIRanlpzb_idModel().getOption(1))
					.getValue();
		}
		String mjih = "";
		String mchangb = "";
		String myunsdw = "";
		double mfahl = 0.0;
		double mkoud = 0.0;
		List _MaozList = new ArrayList();
		List _PizList = getEditValues_P();
		// ���Ƥ��list��Ϊ������ֵ ���Ƥ��list����ȡë��������Ĭ��ֵ
		// �����ϵͳ��������ȡ
//		JDBCcon con = new JDBCcon();
		
		if (_PizList != null && !_PizList.isEmpty()) {
//			int size = _PizList.size();
			//Ƥ�ص������ǰ��ռ�ëʱ�併�����еģ����Բɼ����м�¼����
			Qichmzbean yd = ((Qichmzbean) _PizList.get(0));
			mpandmc = yd.getPandmc();
			if (mpandmc != null && !"".equals(mpandmc)) {
				selected = true;
			}
			mshouhr = yd.getShouhr();
			mmeic = yd.getMeic();
			mmeigy = yd.getMeigy();
			mmeikdq = yd.getMeikdqmc();
			mmeikdw = yd.getMeikdwmc();
			mranlpz = yd.getPinz();
			mjih = yd.getJihkj();
			mchangb = yd.getChangb();
			myunsdw = yd.getChengydw();
			if (getAutoCopy()) {
				mfahl = yd.getFahl();
				mkoud = yd.getKoud();
			}
		} else {
			StringBuffer sql = new StringBuffer();
			
			try {
				sql.append("select mingc from diancxxb");
				ResultSet shouhrRs = con.getResultSet(sql);
				if (shouhrRs.next()) {
					mshouhr = shouhrRs.getString(1);
				}
				shouhrRs.close();
				sql = new StringBuffer();
				sql
						.append("select x.zhi from xitxxb x where x.duixm = 'ú��Ĭ����'");
				ResultSet meicmcRs = con.getResultSet(sql);
				if (meicmcRs.next()) {
					mmeic = meicmcRs.getString("zhi");
				}
				meicmcRs.close();
				sql = new StringBuffer();
				sql.append("select id, zhi from xitxxb where duixm = 'ú��Ա'");
				ResultSet meigyRs = con.getResultSet(sql);
				if (meigyRs.next()) {
					mmeigy = meigyRs.getString("zhi");
				}
				meigyRs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (getCHANGB_Value() != null && getCHANGB_Value().getId() != -1) {
				mchangb = getCHANGB_Value().getValue();
			}
			if (getCHENGYDW_Value() != null
					&& getCHENGYDW_Value().getId() != -1) {
				myunsdw = getCHENGYDW_Value().getValue();
			}
		}
		Qichmzbean maoz = new Qichmzbean(selected, mshouhr, mmeikdq, mmeikdw,
				mranlpz, mjih, myunsdw, mmeic, mmeigy, mchangb, mpandmc, mfahl,
				mkoud);
		maoz.setTimdh("");
		_MaozList.add(maoz);
		setEditValues_M(_MaozList);

		// ȡë�ص���Լԭ��
//		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select leix,jiesbz from jicxxb " + "where mingc='��������Լ��ʽ' "
				+ "and zhuangt=1 and xuh=1");
		ResultSet rs = con.getResultSet(sql);
		setScale(-1);
		setRuleForRounding("");
		try {
			if (rs.next()) {
				String leix = rs.getString("leix");
				int scale = rs.getInt("jiesbz");
				setScale(scale);
				if (leix.equals("��������")) {
					setRuleForRounding("UpOrDown");
				} else if (leix.equals("��λ")) {
					setRuleForRounding("Up");
				} else if (leix.equals("��ȥ")) {
					setRuleForRounding("Down");
				}

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			con.Close();
//			con = null;
			rs = null;
		}
		SysInfo si = new SysInfo();

		setShowDialog(si.getBasicValue("����������ë�ؼ����ʾ����", "n",con));
	}

	/*
	 * ë��bean �� list
	 */
	private Qichmzbean _EditValue_M;

	public Qichmzbean getEditValue_M() {
		return _EditValue_M;
	}

	public void setEditValue_M(Qichmzbean EditValue_M) {
		_EditValue_M = EditValue_M;
	}

	public List getEditValues_M() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues_M(List value) {
		((Visit) getPage().getVisit()).setList1(value);
	}

	public String getcontext() {
		String context= "var  targetPath='http://"
				+ this.getRequestCycle().getRequestContext().getServerName()
				+ ":"
				+ this.getRequestCycle().getRequestContext().getServerPort()
				+ getpageLink("Baocts") + "';\n";
		SysInfo si = new SysInfo();
		String showTimdh = si.getBasicValue("����ú�Ƿ���ʾ��ú����", "0");
		context = context + "var showTimdh = "  + showTimdh + ";";
		return context;
	}

	protected String getpageLink(String Pagename) {
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		IEngine engine = cycle.getEngine();
		IEngineService pageService = engine.getService(Tapestry.PAGE_SERVICE);
		ILink link = pageService
				.getLink(cycle, this, new String[] { Pagename });
		return link.getURL();
	}

	/*
	 * ����ë���¼�
	 */
	private void SaveMaoz() {
        JDBCcon con = new JDBCcon();
	    try {
            System.out.println(getJilhh());
            List _maozList = getEditValues_M();

            if (_maozList == null || _maozList.isEmpty()) {
                setMsg("����ʧ��!ë������Ϊ��!");
                return;
            }

            con.setAutoCommit(false);
            int listIndex = 0;
            String cheph = ((Qichmzbean) _maozList.get(listIndex)).getCheph();
            String timdh = ((Qichmzbean) _maozList.get(listIndex)).getTimdh();//��ú����
            long fahdwb_id = MainGlobal.getProperId(getIFahdwb_idModel(),
                    ((Qichmzbean) _maozList.get(listIndex)).getMeikdqmc());
            long meikxxb_id = MainGlobal.getProperId(
                    getAllMeikxxbModels(fahdwb_id), ((Qichmzbean) _maozList
                            .get(listIndex)).getMeikdwmc());
            long ranlpzb_id = MainGlobal.getProperId(getIRanlpzb_idModel(),
                    ((Qichmzbean) _maozList.get(listIndex)).getPinz());
            long jihkjb_id = MainGlobal.getProperId(getIJihkjb_idModel(),
                    ((Qichmzbean) _maozList.get(listIndex)).getJihkj());
            int zhuangt = 0;
            long pandmc_id = MainGlobal.getProperId(getMEIC_Model(),
                    ((Qichmzbean) _maozList.get(listIndex)).getPandmc());
            // ���Ϊ��ú���ж��̵�ú���Ƿ���ȷ��д ����zhuangt ��Ϊ9
            if (((Qichmzbean) _maozList.get(listIndex)).getSelected()) {
                if (pandmc_id == -1) {
                    setMsg("����ʧ��!����ȷ��д�̵�ú��!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                zhuangt = 9;
            } else {
                if (fahdwb_id == -1) {
                    setMsg("����ʧ��!����ȷ��д������λ!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (meikxxb_id == -1) {
                    setMsg("����ʧ��!����ȷ��дú�����!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (ranlpzb_id == -1) {
                    setMsg("����ʧ��!����ȷ��дƷ��!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (jihkjb_id == -1) {
                    setMsg("����ʧ��!����ȷ��д�ƻ��ھ�!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
            }
            double maoz = ((Qichmzbean) _maozList.get(listIndex)).getMaoz();
            if (!getReg("��������", "ë��", maoz, con)) {
                setMsg("ë�س�����ֵ����Χ!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            if (getMaozJudge()) {
                maoz = RoundMaoz(maoz);
                double maoz2 = getMaoz(cheph, maoz, con);
                if (maoz != maoz2) {
                    if (getShowMsg()) {
                        setMsg("����ë�� " + maoz + "�� ����ë������ " + maoz2
                                + "�� ��\n��ϵͳ���Զ���Ϊ" + maoz2 + "��!");
                    }
                    maoz = maoz2;
                }
            }
            double fahl = ((Qichmzbean) _maozList.get(listIndex)).getFahl();
            if (!getReg("��������", "Ʊ��", fahl, con)) {
                setMsg("Ʊ�س�����ֵ����Χ!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            double koud = ((Qichmzbean) _maozList.get(listIndex)).getKoud();
            long meicb_id = MainGlobal.getProperId(getMEIC_Model(),
                    ((Qichmzbean) _maozList.get(listIndex)).getMeic());
            if (meicb_id == -1) {
                setMsg("����ʧ��!����ȷ��дú��!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            String meigy = ((Qichmzbean) _maozList.get(listIndex)).getMeigy();
            String jianjy = ((Visit) getPage().getVisit()).getRenymc();
            String chengydw = ((Qichmzbean) _maozList.get(listIndex)).getChengydw();
            long changbb_id = MainGlobal.getProperId(getCHANGB_Model(),
                    ((Qichmzbean) _maozList.get(listIndex)).getChangb());
            if (changbb_id == -1) {
                setMsg("����ʧ��!����ȷ��д����!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            String shouhr = ((Qichmzbean) _maozList.get(listIndex)).getShouhr();
            String beiz = ((Qichmzbean) _maozList.get(listIndex)).getBeiz();
            double piz = 0.0;
            // ���ҳ���ϵ�Ƥ����Ƹ�ѡ��ѡ�� ��� chepjbxx ��ȡ��Ƥ��
            if (getAutoPiz()) {
                piz = getPiz(cheph, con);
            }
            String jianpsj = "null";
            if (piz != 0) {
                jianpsj = "sysdate";
            }
            // ����һ����ʶ�ֶΣ�����ʶ�𷢻����Ƿ�Ϊ�˹�¼������
            int autoFahl = 0;
            if (fahl != 0) {
                autoFahl = 1;
            }
            // maoz = maoz -koud;//=ë��-�۶�
            double yingd = 0.0;
            double kuid = 0.0;
            double yuns = 0.0;
            int xuh = getXuh(getJilhh(), con);
            if (fahl != 0 && maoz != 0 && piz != 0) {
                double yingk = maoz - piz - fahl;
                yingk = CustomMaths.Round_new(yingk, 2);
                double yunsl = getYunsl(meikxxb_id);
                yuns = CustomMaths.Round_new(yunsl * fahl, 2);
                if (yingk >= 0) {
                    yingd = yingk;
                    yuns = 0.0;
                } else if (Math.abs(yingk) <= yuns) {
                    yuns = Math.abs(yingk);
                } else {
                    kuid = Math.abs(yingk) - yuns;
                }
            }

            StringBuffer sql = new StringBuffer();
            sql.append("insert into qichjjbtmp");
            sql
                    .append("(id, cheph, shouhr, meikdqb_id, meikxxb_id, ranlpzb_id, fahl, ");
            sql
                    .append("maoz, piz, koud, yuns, kuid, jianmsj, jianpsj, yingd, zhuangt, ");
            sql
                    .append("jihkjb_id, meigy, chengydw, jianjy, beiz, meicb_id, xuh, changbb_id, pandmc_id,jilhh,jingz,Diaodbh)");
            sql.append(" values(");
            sql.append("xl_qichjjbtmp_id.nextval,"); // id
            sql.append("'" + cheph.trim() + "',"); // ��Ƥ��
            sql.append("'" + shouhr + "',"); // �ջ���
            sql.append(fahdwb_id + ","); // ������λ
            sql.append(meikxxb_id + ","); // ú��λ
            sql.append(ranlpzb_id + ","); // ȼ��Ʒ��
            sql.append(fahl + ","); // ������
            sql.append(maoz + ","); // ë��=ë��-�۶�
            sql.append(piz + ","); // Ƥ��
            sql.append(koud + ","); // �۶�
            sql.append(yuns + ","); // ����
            sql.append(kuid + ","); // �۶�
            sql.append("sysdate,"); // ��ëʱ��
            sql.append(jianpsj + ","); // ��Ƥʱ��
            sql.append(yingd + ","); // ӯ��
            sql.append(zhuangt + ","); // ״̬
            sql.append(jihkjb_id + ","); // �ƻ��ھ�
            sql.append("'" + meigy + "',"); // ú��Ա
            sql.append("'" + chengydw + "',");// ���˵�λ
            sql.append("'" + jianjy + "',");// ���Ա
            sql.append("'" + beiz + "',"); // ��ע
            sql.append(meicb_id + ","); // ú��
            sql.append(xuh + ","); // ���
            sql.append(changbb_id + ","); // ����
            sql.append(pandmc_id); // �̵�ú��
            sql.append(",'").append(getJilhh());
            sql.append("'," + autoFahl + ",'" + timdh + "')");
            int flag = con.getInsert(sql.toString());
            if (flag == -1) {
                System.out.println("����ë��ʧ��! Qichmz.java SaveMaoz()��");
                setMsg("������ë�ر���ʧ�ܣ�\n�����sql��䣺\n" + sql.toString());
                con.rollBack();
                con.Close();
                con.setAutoCommit(true);
                return;
            }
            con.commit();
            con.setAutoCommit(true);
            getInitMaoz(con);
            getInitPiz(con);
        }catch (Exception e){
	        e.printStackTrace();
	        con.rollBack();
        }finally {
            con.Close();
        }
    }

	private boolean _SaveMaozChick = false;

	public void SaveMaozButton(IRequestCycle cycle) {
		_SaveMaozChick = true;
	}

	/*
	 * ��ʼ��Ƥ������
	 */
	public void getInitPiz(JDBCcon con) {
		//Qiuzw 2009��3��13�� ��ʼ����Ƥ�ء�ʱ��Ҫ����δ��Ƥ�����û��ֹ��˻أ�q.huipbz = 0��
		List _PizList = new ArrayList();
//		JDBCcon con = new JDBCcon();
		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date());
		StringBuffer sql = new StringBuffer();
		sql.append("select * from (select pz.* ,ceil(rownum/15) pagenum from ");
		sql
				.append("(select q.id, q.cheph, d.mingc meikdqmc, k.mingc meikdwmc, r.pinz, j.mingc as kouj, q.fahl, \n");
		sql.append("q.maoz, q.piz, q.koud, q.chengydw, q.meigy, \n");
		sql
				.append("(select nvl(meicmc,'') as pandmc from meicfqb where id(+) = q.pandmc_id) as pandmc, \n");
		sql
				.append("(select meicmc from meicfqb where id = q.meicb_id) as meic, \n");
		sql.append("q.shouhr, c.mingc as changb, q.beiz,q.Diaodbh \n");
		sql
				.append("from qichjjbtmp q, gongysb d, meikxxb k, ranlpzb r, jihkjb j, changbb c \n");
		sql
				.append("where q.meikdqb_id = d.id(+) and q.meikxxb_id = k.id(+) and q.ranlpzb_id = r.id(+) \n");
		sql
				.append("and q.jihkjb_id = j.id(+) and q.changbb_id = c.id and piz=0 and (q.huipbz = 0 or (q.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1)) order by q.jianmsj desc) pz)\n");
		sql.append("where pagenum = " + getCurrentPageNumber());
		try {
			ResultSet rs = con
					.getResultSet(new StringBuffer(
							"select ceil(count(*)/15) as total from qichjjbtmp q where piz=0 and (q.huipbz = 0 or (q.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1)) "));
			setCurrentPageNumber(1);
			if (rs.next()) {
				long total = rs.getLong("total");
				if (total > 0) {
					setTotalPageNumber(total);
				}
			}
			rs.close();
			rs = con.getResultSet(sql);
			while (rs.next()) {
				long id = rs.getLong("id");
				String cheph = rs.getString("cheph");
				String pandmc = rs.getString("pandmc");
				String meikdqmc = rs.getString("meikdqmc");
				String meikdwmc = rs.getString("meikdwmc");
				String pinz = rs.getString("pinz");
				String kouj = rs.getString("kouj");
				double fahl = rs.getDouble("fahl");
				double maoz = rs.getDouble("maoz");
				double piz = rs.getDouble("piz");
				double koud = rs.getDouble("koud");
				String meic = rs.getString("meic");
				String meigy = rs.getString("meigy");
				String shouhr = rs.getString("shouhr");
				String chengydw = rs.getString("chengydw");
				String changb = rs.getString("changb");
				String beiz = rs.getString("beiz");
				String timdh = rs.getString("Diaodbh");
				Qichmzbean PizValue = new Qichmzbean(id, false, cheph, pandmc, meikdqmc,
						meikdwmc, pinz, kouj, fahl, maoz, piz, koud, meic,
						meigy, shouhr, chengydw, changb, beiz);
				PizValue.setTimdh(timdh);
				_PizList.add(PizValue);
			}
			rs.close();
		} catch (Exception e) {
			setMsg("��ȡƤ�������Ϣʧ�ܣ�");
			e.printStackTrace();
		} finally {
//			con.Close();
		}
		setEditValues_P(_PizList);
	}

	public void getInitData(JDBCcon con) {
//		System.out.println("ҳ�������ʼ��...");
		InitParameter(con);
//		System.out.println("ҳ�������ʼ��1");
		getInitPiz(con);
//		System.out.println("ҳ�������ʼ��2");

//		System.out.println("ҳ�������ʼ��3");
		getArrayScript();
//		System.out.println("ҳ�������ʼ��.");
	}

	/*
	 * Ƥ��bean �� list
	 */
	private Qichmzbean _EditValue_P;

	public Qichmzbean getEditValue_P() {
		return _EditValue_P;
	}

	public void setEditValue_P(Qichmzbean EditValue_P) {
		_EditValue_P = EditValue_P;
	}

	public List getEditValues_P() {
		return ((Visit) getPage().getVisit()).getList2();
	}

	public void setEditValues_P(List editList) {
		((Visit) getPage().getVisit()).setList2(editList);
	}

	private boolean _ToFirstPageChick = false;

	private boolean _ToPreviousPageChick = false;

	private boolean _ToNextPageChick = false;

	private boolean _ToLastPageChick = false;

	private boolean _GoPageChick = false;

	public void ToFirstPageButton(IRequestCycle cycle) {
		_ToFirstPageChick = true;
	}

	public void ToPreviousPageButton(IRequestCycle cycle) {
		_ToPreviousPageChick = true;
	}

	public void ToNextPageButton(IRequestCycle cycle) {
		_ToNextPageChick = true;
	}

	public void ToLastPageButton(IRequestCycle cycle) {
		_ToLastPageChick = true;
	}

	public void GoPageButton(IRequestCycle cycle) {
		_GoPageChick = true;
	}

	/*
	 * ҳ���ύ�¼�
	 */
	public void submit(IRequestCycle cycle) {
		JDBCcon con = new JDBCcon();
		getInitData(con);
		con.Close();
		if (_ToFirstPageChick) {
			_ToFirstPageChick = false;
			setCurrentPageNumber(1);
		}
		if (_ToPreviousPageChick) {
			_ToPreviousPageChick = false;
			setCurrentPageNumber(getCurrentPageNumber() - 1);
		}
		if (_ToNextPageChick) {
			_ToNextPageChick = false;
			setCurrentPageNumber(getCurrentPageNumber() + 1);
		}
		if (_ToLastPageChick) {
			_ToLastPageChick = false;
			setCurrentPageNumber(getTotalPageNumber());
		}
		if (_GoPageChick) {
			_GoPageChick = false;
			setCurrentPageNumber(getGoPageNumber());
		}
		if (_SaveMaozChick) {
			_SaveMaozChick = false;
//			System.out.println("׼������...");
			SaveMaoz();
//			System.out.println("�������.");
		}
	}

	private String _showDialog = "n";

	public String getShowDialog() {
		return _showDialog;
	}

	public void setShowDialog(String v) {
		_showDialog = v;
	}

	/*
	 * ҳ���ʼ��
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if (!visit.getActivePageName().equals(this.getPageName())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�

			visit.setActivePageName(getPageName());
			visit.setList2(null);
			visit.setList1(null);
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setDropDownBean4(null);
			((Visit) getPage().getVisit()).setDropDownBean5(null);
			((Visit) getPage().getVisit()).setDropDownBean6(null);
			((Visit) getPage().getVisit()).setDropDownBean7(null);
			((Visit) getPage().getVisit()).setDropDownBean8(null);
			((Visit) getPage().getVisit()).setDropDownBean9(null);

			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setProSelectionModel4(null);
			((Visit) getPage().getVisit()).setProSelectionModel5(null);
			((Visit) getPage().getVisit()).setProSelectionModel6(null);
			((Visit) getPage().getVisit()).setProSelectionModel7(null);
			((Visit) getPage().getVisit()).setProSelectionModel8(null);
			((Visit) getPage().getVisit()).setProSelectionModel9(null);
//			getInitMaoz();
//			getInitPiz();
//			getReport();
			setCommPort("-1");
			setBaudRate("-1");
			setDataBits("-1");
			setStopBits("-1");
			setParitySetting("-1");
			visit.setLong1(1);
			visit.setLong2(1);
			visit.setLong3(1);
			getInitMaoz(con);
			getInitData(con);
		}
		SysInfo si = new SysInfo();
		if(si.getBasicValue("�������������ҳ���ϵ���ʾ������ť", 0,con)==1) {
			setDisabled(true);
		}
		con.Close();
	}

	/*
	 * ȡ�õ��������
	 */
	public int getXuh(String jilhh,JDBCcon con) {
		int xuh = 0;
//		JDBCcon con = new JDBCcon();
		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date());
		StringBuffer sql = new StringBuffer(
				"select nvl(max(xuh),0) +1 as xuh from qichjjbtmp where " +
				"jianmsj BETWEEN trunc(sysdate) and trunc(sysdate)+1");
		sql.append(" and jilhh = '").append(getJilhh()).append("'");
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				xuh = rs.getInt("xuh");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("��ȡ���ʧ��!");
		} finally {
//			con.Close();
		}
		return xuh;
	}

	private int _Scale = 0;

	public int getScale() {
		return _Scale;
	}

	public void setScale(int scale) {
		_Scale = scale;
	}

	private String _Rule = "";

	public String getRuleForRounding() {
		return _Rule;
	}

	public void setRuleForRounding(String rule) {
		_Rule = rule;
	}

	/*
	 * ��������Ϣ������Լë��
	 */
	public double RoundMaoz(double maoz) {
		double _maoz = maoz;
		// JDBCcon con = new JDBCcon();
		// StringBuffer sql = new StringBuffer();
		// sql.append("select leix,jiesbz from jicxxb " + "where mingc='��������Լ��ʽ'
		// "
		// + "and zhuangt=1 and xuh=1");
		// ResultSet rs = con.getResultSet(sql);
		// try {
		// if (rs.next()) {
		String leix = getRuleForRounding();// rs.getString("leix");
		int scale = getScale();// rs.getInt("jiesbz");
		// _maoz = Arith.round(maoz, scale);
		if (scale != -1) {
			_maoz = CustomMaths.Round_new(maoz, scale);
			if (leix.equals("UpOrDown")) {
				// ��������
			} else if (leix.equals("Up")) {
				// ��λ
				if (maoz > _maoz) {
					_maoz = _maoz + Arith.div(1, Math.pow(10, scale));
				}
			} else if (leix.equals("Down")) {
				// ��ȥ
				if (maoz < _maoz) {
					_maoz = _maoz - Arith.div(1, Math.pow(10, scale));
				}
			}
		}

		// }
		// rs.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// con.Close();
		// }
		return _maoz;
	}

	/*
	 * ȡ�ò��ж�ë���Ƿ񳬳������ �糬�� ����ë�������
	 */
	public double getMaoz(String cheph, double maoz,JDBCcon con) {
		double _maoz = maoz;
//		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select maoz from chepjbxx where maoz+maozcc<" + maoz
				+ " and cheph='" + cheph + "' and shiyzt = 1");
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				_maoz = rs.getDouble("maoz");
				con
						.getUpdate("update chepjbxx set chaoccs= chaoccs+1 where cheph='"
								+ cheph + "' and shiyzt = 1");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			con.Close();
		}
		return _maoz;
	}

	/*
	 * ȡ�ò��ж�Ƥ���Ƿ������Сֵ ����� ����Ƥ����Сֵ
	 */
	public double getPiz(String cheph, double piz) {
		double _piz = piz;
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select piz from chepjbxx where piz-pizcc>" + piz
				+ " and cheph='" + cheph.trim() + "' and shiyzt = 1");
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				_piz = rs.getDouble("piz");
				con
						.getUpdate("update chepjbxx set chaoccs= chaoccs+1 where cheph='"
								+ cheph.trim() + "' and shiyzt = 1");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _piz;
	}

	/*
	 * ȡ���Ѵ�Ƥ��
	 */
	public double getPiz(String cheph,JDBCcon conpiz) {
		double piz = 0.0;
//		JDBCcon conpiz = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select piz from " +
                " where cheph='" + cheph.trim()
				+ "' and shiyzt = 1");
		ResultSet rs = conpiz.getResultSet(sql);
		try {
			if (rs.next()) {
				piz = rs.getDouble("piz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
//			conpiz.Close();
		}
		return piz;
	}

	/*
	 * ȡ��������������
	 */
	public double getYunsl(long meikxxb_id) {
		double yunsl = 0.01;
		JDBCcon conyunsl = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select qicysl as zhi from meikxxb where id=" + meikxxb_id);
		ResultSet rs = conyunsl.getResultSet(sql);
		try {
			if (rs.next()) {
				yunsl = rs.getDouble("zhi");
			}
			rs.close();
		} catch (Exception e) {
			System.out.println("��ȡ����ú������ʧ��!");
			e.printStackTrace();
		} finally {
			conyunsl.Close();
		}
		return yunsl;
	}

	/*
	 * �ж���ֵ����Χ
	 */
	private boolean getReg(String leix, String zhibmc, double shuz, JDBCcon con) {
		boolean flag = false;
		// JDBCcon con = new JDBCcon();
		String sql = "select id from shuzhlfwb where duixmc='" + zhibmc
				+ "' and leix='" + leix + "' and diancxxb_id ="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				sql = "select * from shuzhlfwb where id=" + rs.getLong("id")
						+ " and helsx>=" + shuz + " and helxx<=" + shuz
						+ " and diancxxb_id ="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id();
				if (con.getHasIt(sql)) {
					flag = true;
				}
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
			setMsg("��ֵ����Χ�ж�ʱ�����쳣��");
		}
		return flag;
	}

	/*
	 * �������������
	 */
	// ȼ��Ʒ������������
	public IDropDownBean getRanlpzb_idValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setRanlpzb_idValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getIRanlpzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIRanlpzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setIRanlpzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public void getIRanlpzb_idModels() {
		StringBuffer sql = new StringBuffer(
				"select id ,pinz   from ranlpzb order by xuh");
		// where leix ='ú'
		setIRanlpzb_idModel(new IDropDownSelectionModel(sql));
	}

	// �ƻ��ھ�������������
	public IDropDownBean getJihkjb_idValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setJihkjb_idValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getIJihkjb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIJihkjb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setIJihkjb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getIJihkjb_idModels() {
		StringBuffer sql = new StringBuffer(
				"select id ,mingc as kouj,xuh   from jihkjb order by xuh");
		setIJihkjb_idModel(new IDropDownModel(sql));
	}

	// ������λ������
	public IDropDownBean getFahdwb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			if (getIFahdwb_idModel().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean5((IDropDownBean) getIFahdwb_idModel()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFahdwb_idValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean5(value);
	}

	public IPropertySelectionModel getIFahdwb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getIFahdwb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setIFahdwb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public void getIFahdwb_idModels() {
//		StringBuffer sql = new StringBuffer("select m.id, m.meikdqmc\n"
//				+ "  from meikdqb m, changkglb c\n"
//				+ " where m.id = c.meikxxb_id\n" + "   and c.zhuangt = 1\n"
//				+ "   and c.diancxxb_id ="
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//				+ " order by m.xuh");
		String sql="select id ,mingc meikdqmc  from gongysb where leix=1 and zhuangt=1 order by mingc";
		setIFahdwb_idModel(new IDropDownSelectionModel(sql));
	}

	// ú��λ������
	public IDropDownBean getMeikxxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIMeikxxb_idModel().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean2((IDropDownBean) getIMeikxxb_idModel()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setMeikxxb_idValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getIMeikxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIMeikxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setIMeikxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getIMeikxxb_idModels() {
		long id = -2;
		if (getFahdwb_idValue() != null) {
			id = getFahdwb_idValue().getId();
		}
//		StringBuffer sql = new StringBuffer("select m.id, m.meikdwmc\n"
//				+ "\tfrom changkglb c, meikxxb m\n"
//				+ " where m.id = c.meikxxb_id\n"
//				+ "\t and c.zhuangt = 1 and m.meikdqb_id = " + id + "\n"
//				+ "\t and c.diancxxb_id = "
//				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//				+ " order by m.xuh");
		String sql="select m.id,m.mingc meikdwmc from meikxxb m where m.zhuangt=1 order by m.mingc";
		setIMeikxxb_idModel(new IDropDownSelectionModel(sql));
	}

	// ����ú��������
	public IPropertySelectionModel getAllMeikxxbModels(long fahdwb_id) {
//		StringBuffer sql = new StringBuffer(
//				"select id ,meikdwmc from meikxxb where meikdqb_id ="
//						+ fahdwb_id + " order by xuh");
		String sql="select m.id ,m.mingc meikdwmc from meikxxb m \n" +
                "left join gongysmkglb g on m.id=g.meikxxb_id \n" +
                "/*where g.gongysb_id="+fahdwb_id+ "*/ order by m.mingc";
		return new IDropDownSelectionModel(sql);
	}

	// ú��Ա������
	public IDropDownBean getMEIGY_Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			if (getMEIGY_Model().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean6((IDropDownBean) getMEIGY_Model()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setMEIGY_Value(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean6(value);
	}

	public IPropertySelectionModel getMEIGY_Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getMEIGY_Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setMEIGY_Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public void getMEIGY_Models() {
		StringBuffer sql = new StringBuffer(
				"select id, zhi from xitxxb where duixm = 'ú��Ա'");
		setMEIGY_Model(new IDropDownSelectionModel(sql));
	}

	// ���˵�λ������
	public IDropDownBean getCHENGYDW_Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			if (getCHENGYDW_Model().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean7((IDropDownBean) getCHENGYDW_Model()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setCHENGYDW_Value(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean7(value);
	}

	public IPropertySelectionModel getCHENGYDW_Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getCHENGYDW_Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void setCHENGYDW_Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public void getCHENGYDW_Models() {
		StringBuffer sql = new StringBuffer(
				"select id, jianc from FAHDWB where beiz = '���乫˾'");
		setCHENGYDW_Model(new IDropDownSelectionModel(sql));
	}

	// ú��������
	public IDropDownBean getMEIC_Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			if (getMEIC_Model().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean8((IDropDownBean) getMEIC_Model()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setMEIC_Value(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean8(value);
	}

	public IPropertySelectionModel getMEIC_Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getMEIC_Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void setMEIC_Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public void getMEIC_Models() {
		StringBuffer sql = new StringBuffer(
				"select id, meicmc from meicfqb order by meicmc");
		setMEIC_Model(new IDropDownSelectionModel(sql));
	}

	// ����������
	public IDropDownBean getCHANGB_Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			if (getCHANGB_Model().getOptionCount() > 1) {
				((Visit) getPage().getVisit())
						.setDropDownBean4((IDropDownBean) getCHANGB_Model()
								.getOption(1));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setCHANGB_Value(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean4(value);
	}

	public IPropertySelectionModel getCHANGB_Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getCHANGB_Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setCHANGB_Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getCHANGB_Models() {
		StringBuffer sql = new StringBuffer(
				"select c.id, c.mingc from changbb c where c.diancxxb_id = "
						+ ((Visit) getPage().getVisit()).getDiancxxb_id());
		setCHANGB_Model(new IDropDownSelectionModel(sql));
	}

	// �̵�ú��������
	public IDropDownBean getPANDMC_Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getPANDMC_Model()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setPANDMC_Value(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean9(value);
	}

	public IPropertySelectionModel getPANDMC_Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getPANDMC_Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void setPANDMC_Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public void getPANDMC_Models() {
		StringBuffer sql = new StringBuffer(
				"select id, meicmc from meicfqb order by meicmc");
		setPANDMC_Model(new IDropDownSelectionModel(sql));
	}

	/*
	 * ȡ�ò��������ջ�����Ϣ
	 */
	public String getInfo() {
		String info = "";
		StringBuffer sql = new StringBuffer();
		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date());
		sql.append("select decode(q.zhuangt,9,'��ú',null,'����','����') zhuangt,\n");
		sql
				.append("sum(q.maoz) maoz, sum(q.piz) piz, sum(q.maoz - q.piz) jingz, \n");
		sql
				.append("sum(q.fahl) fahl, sum(q.yuns) yuns, count(jianmsj) jianmsj, \n");
		sql
				.append("count(jianpsj) jianpsj from qichjjbtmp q where zhuangt !=9 \n");
		sql
				.append("and jianmsj BETWEEN trunc(sysdate) and trunc(sysdate)+1 \n ");
		sql
				.append(" group by rollup(q.zhuangt) having (grouping(q.zhuangt)=1)");
		JDBCcon con = new JDBCcon();
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				int mchec = rs.getInt("jianmsj");
				int pchec = rs.getInt("jianpsj");
				int zchec = mchec + pchec;
				double jingz = rs.getDouble("jingz");
				double piaoz = rs.getDouble("fahl");
				double yuns = rs.getDouble("yuns");
				info = "  ���չ�����:" + zchec + "��;���м�ë:" + mchec + "��,��Ƥ:" + pchec
						+ "����" + "���ƾ���:" + jingz + "��,Ʊ��:" + piaoz + "��,����:"
						+ yuns + "�֡�";
			} else {
				info = "  �������޼�";
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return info;
	}

	/*
	 * ȡ���泵��ﵥ ������ҳ�洫̫��Ĳ��������ں�̨�����е�����д�ú󴫳��� ����֮�٣�
	 */
	public boolean getRaw() {
		return true;
	}

	public String getReport() {
		String _Report = "";
//		JDBCcon con = new JDBCcon();
//		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date()); 
//		String biaot = "";
//		String danw = "";
//		try {
//			StringBuffer sql = new StringBuffer();
//			sql
//					.append("select x.zhi from xitxxb x where x.duixm = '�������泵��ﵥ����' and shifsy=1");
//			ResultSet rs = con.getResultSet(sql);
//			try {
//				while (rs.next()) {
//					biaot = rs.getString("zhi");
//				}
//				rs.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			sql = new StringBuffer();
//			sql
//					.append("select x.zhi from xitxxb x where x.duixm = '�������泵��ﵥ��λ' and shifsy=1");
//			rs = con.getResultSet(sql);
//			try {
//				while (rs.next()) {
//					danw = rs.getString("zhi");
//				}
//				rs.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// �����SQL�����õ��Ǽ�Ƥʱ��������õ��Ǽ�ëʱ��,
//			// ������ڸ������������ͻ���ִ�ӡ�泵��ﵥ��ť�����õ����⡣
//			// ��Ҳ���Ǻ���� ����ԭ����������ôд�� ��֪����ʲô���� ���Ծ�û�иĶ���
//			sql = new StringBuffer();
//			sql
//					.append("select qichjjbtmp.id,\n"
//							+ "\t\t\t qichjjbtmp.xuh,\n"
//							+ "\t\t\t qichjjbtmp.jihkjb_id,\n"
//							+ "\t\t\t cheph,\n"
//							+ "\t\t\t shouhr,\n"
//							+ "\t\t\t qichjjbtmp.meikdqb_id,\n"
//							+ "\t\t\t meikdqb.meikdqmc,\n"
//							+ "\t\t\t qichjjbtmp.meikxxb_id,\n"
//							+ "\t\t\t meikxxb.meikdwmc,\n"
//							+ "\t\t\t qichjjbtmp.ranlpzb_id,\n"
//							+ "\t\t\t fahl,\n"
//							+ "\t\t\t maoz,\n"
//							+ "\t\t\t piz,\n"
//							+ "\t\t\t maoz - piz as jingz,\n"
//							+ "\t\t\t koud,\n"
//							+ "\t\t\t yuns,\n"
//							+ "\t\t\t kuid,\n"
//							+ "\t\t\t to_char(jianpsj, 'yyyy-mm-dd hh24:mi:ss') as jianpsj,\n"
//							+ "\t\t\t to_char(jianmsj, 'yyyy-mm-dd hh24:mi:ss') as jianmsj,\n"
//							+ "\t\t\t chengydw,\n"
//							+ "\t\t\t meigy,\n"
//							+ "\t\t\t qichjjbtmp.jianjy,\n"
//							+ "\t\t\t qichjjbtmp.beiz\n"
//							+ "\tfrom qichjjbtmp, meikdqb, meikxxb\n"
//							+ " where meikdqb.id = qichjjbtmp.meikdqb_id\n"
//							+ "\t and qichjjbtmp.meikxxb_id = meikxxb.id\n"
////							+ "\t and to_char(qichjjbtmp.jianmsj, 'yyyy-mm-dd') =\n"
////							+ "\t\t\t to_char(sysdate, 'yyyy-mm-dd')\n"
//							+" and jianmsj BETWEEN to_date('"+strToday+" 00:00:00','yyyy-mm-dd hh24:mi:ss') and to_date('"+strToday+" 23:59:59','yyyy-mm-dd hh24:mi:ss') \n"
//							+ "\t and maoz <> 0\n"
//							+ "\t and piz <> 0 and qichjjbtmp.zhuangt <>9 order by jianpsj desc");
//			rs = con.getResultSet(sql);
//			if (rs.next()) {
//				// String jianpsj = rs.getString("JIANPSJ");
//				String cheph = rs.getString("CHEPH");
//				if (cheph == null) {
//					cheph = "";
//				}
//				String kuangb = rs.getString("MEIKDWMC");
//				if (kuangb == null) {
//					kuangb = "";
//				}
//				String changydw = rs.getString("CHENGYDW");
//				if (changydw == null) {
//					changydw = "";
//				}
//				String Jianmsj = rs.getString("JIANMSJ");
//				if (Jianmsj == null) {
//					Jianmsj = "";
//				}
//				String Jianpsj = rs.getString("JIANPSJ");
//				if (Jianpsj == null) {
//					Jianpsj = "";
//				}
//				double maoz = rs.getDouble("MAOZ");
//				double piz = rs.getDouble("PIZ");
//				double jingz = rs.getDouble("JINGZ");
//				double fahl = rs.getDouble("FAHL");
//				double koud = rs.getDouble("KOUD");
//				double yuns = rs.getDouble("YUNS");
//				String beiz = rs.getString("BEIZ");
//				if (beiz == null) {
//					beiz = "";
//				}
//				String Jianjy = rs.getString("JIANJY");
//				if (Jianjy == null) {
//					Jianjy = "";
//				}
//				String Meigy = rs.getString("MEIGY");
//				if (Meigy == null) {
//					Meigy = "";
//				}
//				long Chex = rs.getLong("xuh");
//				String chex = "";
//				if (rs.getLong("xuh") < 10) {
//					chex = "00" + rs.getLong("xuh");
//				} else if (rs.getLong("xuh") >= 10 && rs.getLong("xuh") < 100) {
//					chex = "0" + rs.getLong("xuh");
//				} else {
//					chex = String.valueOf(Chex);
//				}
//				_Report = "<div align=center style=\"display:none\" id=\"di\"> \n"
//						+ "<div align=center id=\"printjjd\"> \n"
//						+ "<div align=center> <br><br>\n"
//						+ "<table id=\"table1\" \n"
//						+ "	style=\"WIDTH: 600px; BORDER-COLLAPSE: collapse; height:216px\" \n"
//						+ "	cellpadding=\"0\" border=\"0\"> \n"
//						+ "	<colgroup> \n"
//						+ "		<col style=\"WIDTH: 54pt\" span=\"6\" width=\"72\"> \n"
//						+ "	</colgroup> \n"
//						+ "	<tr style=\"height:30px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: 1px; BORDER-LEFT-WIDTH: 1px; BORDER-RIGHT-WIDTH: 1px; width:600px; height:30px\" \n"
//						+ "			bordercolor=\"#000000\" align=\"middle\" colspan=\"6\"> \n"
//						+ "		<p align=\"center\"><b> <input \n"
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 18pt; font-family:����_GB2312; padding-left:4px; padding-right:4px; text-align:center; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"����\" value=\""
//						+ biaot
//						+ "\" \n"
//						+ "			id=\"jianjdTitle\" size=\"36\" /></b> \n"
//						+ "		</td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"HEIGHT: 1pt\"> \n"
//						+ "		<td style=\"WIDTH: 100px; HEIGHT: 1pt\" align=middle></td> \n"
//						+ "		<td \n"
//						+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
//						+ "			borderColor=#000000 align=middle></td> \n"
//						+ "		<td \n"
//						+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
//						+ "			borderColor=#000000 align=middle></td> \n"
//						+ "		<td \n"
//						+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
//						+ "			borderColor=#000000 align=middle></td> \n"
//						+ "		<td \n"
//						+ "			style=\"PADDING-RIGHT: 4px; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px; border-top-style:solid; border-top-width:1px\" \n"
//						+ "			borderColor=#000000 align=middle></td> \n"
//						+ "		<td style=\"WIDTH: 100px; HEIGHT: 1pt\" align=middle></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:30px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 0pt solid #000000; BORDER-BOTTOM: 1.5pt solid #000000; style: ; width:300px; height:30px; border-left-width:medium; border-top-width:medium\" \n"
//						+ "			align=\"middle\" colspan=\"3\"> \n"
//						+ "		<p align=\"left\" style=\"FONT-SIZE: 11pt�� FONT-FAMILY: ������;\">ʱ�䣺<input \n"
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"ʱ��\" value=\""
//						+ Jianpsj
//						+ "\" id=\"Shij\" \n"
//						+ "			size=\"22\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:100px; height:30px\" \n"
//						+ "			align=\"middle\"></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:186px; height:30px\" \n"
//						+ "			align=\"middle\" colspan=\"2\" style=\"FONT-SIZE: 11pt�� FONT-FAMILY: ������;\">��ţ�<input \n"
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"���\" value=\""
//						+ chex
//						+ "\" id=\"Chex\" \n"
//						+ "			size=\"8\" />��</td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: ������; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ����; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">���</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:300px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"3\"> \n"
//						+ "		<p align=\"center\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: ����;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"���\" value=\""
//						+ kuangb
//						+ "\" id=\"Kuangb\" \n"
//						+ "			size=\"30\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: ������; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">��λ</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  vertical-align:middle; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"��λ\" value=\""
//						+ danw
//						+ "\" id=\"Danw\" \n"
//						+ "			size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: ������; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ����; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">���˵�λ</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:300px\" \n"
//						+ "			align=\"middle\" colspan=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"���˵�λ\" value=\""
//						+ changydw
//						+ "\" \n"
//						+ "			id=\"Chengydw\" size=\"30\" /></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; FONT-FAMILY: ������; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">����</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none #000000; BORDER-LEFT: medium none #000000; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"����\" value=\""
//						+ cheph
//						+ "\" id=\"Cheh\" \n"
//						+ "			size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: ������; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ����; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">ë��ʱ��</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:200px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"ë��ʱ��\" value=\""
//						+ Jianmsj
//						+ "\" \n"
//						+ "			id=\"Jianmsj\" size=\"22\" /></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: ������; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">Ƥ��ʱ��</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1.5pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000; style: ; width:186px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"Ƥ��ʱ��\" value=\""
//						+ Jianpsj
//						+ "\" \n"
//						+ "			id=\"Jianpsj\" size=\"22\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"HEIGHT: 25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ����; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">ë��(��)</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: ������; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: \" \n"
//						+ "			align=\"middle\">Ƥ��(��)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; FONT-FAMILY: ������; BORDER-RIGHT: 1pt solid #000000; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; BORDER-BOTTOM: 1pt solid #000000; style: ; height:25px\" \n"
//						+ "			align=\"middle\">�۶�(��)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: ������; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">;��(��)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: ������; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">����(��)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; FONT-FAMILY: ������; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">Ʊ��</td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\"> \n"
//						+ "		<p align=\"center\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: ������;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ maoz
//						+ "\" displayName=\"ë��\" id=\"Maoz\" size=\"7\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ������; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ piz
//						+ "\" displayName=\"Ƥ��\" id=\"Piz\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1pt solid #000000; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid #000000; FONT-STYLE: normal; FONT-FAMILY: ������; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ koud
//						+ "\" displayName=\"�۶�\" id=\"Koud\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ������; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ yuns
//						+ "\" displayName=\"����\" id=\"Yuns\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: ������; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ jingz
//						+ "\" displayName=\"����\" id=\"Jingz\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ fahl
//						+ "\" displayName=\"Ʊ��\" id=\"Biaoz\" size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr> \n"
//						+ "		<TD \n"
//						+ "			style=\"BORDER-RIGHT: windowtext 1pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: windowtext 1.5pt solid; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: ����; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none\" \n"
//						+ "			align=middle><FONT size=3>��ע</FONT></TD> \n"
//						+ "		<TD align=middle colSpan=5 width=\"100\" \n"
//						+ "			style=\"BORDER-RIGHT: windowtext 1.5pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: ������; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
//						+ "			align=middle><FONT size=3></FONT> <input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"��ע\" value=\""
//						+ beiz
//						+ "\" id=\"Beiz\" \n"
//						+ "			size=\"26\" /></TD> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:30px\"> \n"
//						+ "		<td align=\"middle\" colspan=\"3\" style=\"width: 300px; height: 30px\"> \n"
//						+ "		<p align=\"left\" valign=\"center\" \n"
//						+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: ������;\">�� �� Ա��<input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: ������;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"���Ա\" value=\""
//						+ Jianjy
//						+ "\" id=\"Jianjy\" \n"
//						+ "			size=\"10\" /> \n"
//						+ "		</td> \n"
//						+ "		<td style=\"WIDTH: 100px; HEIGHT: 30px\" align=\"middle\"></td> \n"
//						+ "		<td style=\"WIDTH: 200px; HEIGHT: 30px\" align=\"middle\" colSpan=\"2\"> \n"
//						+ "		<p align=\"right\" valign=\"center\" \n"
//						+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: ������;\">ú �� Ա��<input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: ������;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"ú��Ա\" value=\""
//						+ Meigy
//						+ "\" id=\"Meigy\" \n"
//						+ "			size=\"10\" /> \n"
//						+ "		</td> \n"
//						+ "	</tr> \n"
//						+ "</table> \n"
//						+ "</div> \n" + "</div> \n" + "</div>";
//			}
//			rs.close();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			con.Close();
//		}
		return _Report;
	}
	private boolean _isDisabled = false;
	
	public boolean isDisabled() {
		return _isDisabled; 
	}
	public void setDisabled(boolean b) {
		_isDisabled = b;
	}
}
