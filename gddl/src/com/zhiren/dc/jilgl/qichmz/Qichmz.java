/*
 * 日期：2016年6月23日
 * 作者：Qiuzw
 * 修改内容：增加处理姚孟电厂“提煤单号”的功能
 * */

/*
 * 日期：2013-3-13
 * 作者：Qiuzw
 * 描述：记录仪表前后计量的差值在visit变量里记录。
* */

/*
 * 日期：2012年4月25日
 * 作者：Qiuzw
 * 修改内容：对于页面参数初始化方法，增加jdbc连接参数，提供代码执行效率
 * */

/*
 * 日期：2010年7月27日
 * 作者：Qiuzw
 * 修改内容：计算盈亏、运损的公式有问题。暂时没有电厂用到，消除隐患
 * */

/*
 * 时间：2009年3月16日
 * 作者：Qiuzw
 * 修改内容：初始化页面时，显示“当日未回皮”或者“手工退回”的回皮信息
 */
/*
 * 时间：2008-07-30
 * 作者：Qiuzw
 * 描述：可以通过基础参数“禁用汽车衡称重页面上的显示参数按钮”控制用户是否可使用页面上的“显示参数”按钮
 *       神头电厂提出
 */
/*
 * 时间：2008-06-09
 * 作者：Qiuzw
 * 描述：1、将原来在服务器端判断的毛重取舍问题移至客户端。同时能进行相关数据处理。
 *       2、页面每次加载时生成各个衡器号对应的磅单号，用于保存数据时的提示信息。
 * 影响电厂：
 *       神头电厂修改
 */
/*
 * 时间：2008-05-25
 * 作者：Qiuzw
 * 描述：由于保存数据时，有错误发生时，没有将commit状态修改为true，可能导致数据读取错误。
 */
/*
 * 时间：2008-05-16
 * 作者：Qiuzw
 * 描述：页面初始化时，将系统设置中的衡器参数赋值给页面变量
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
	 * 初始化毛重超差判定、皮重超差判定、超差提示、及免称皮重 四个参数
	 */
	public void InitParameter(JDBCcon con) {
//		JDBCcon con = new JDBCcon();
		long longValue = -1;
		try {
			String sql = "select mingc from jicxxb "
					+ "where leix='汽车衡数据过滤设置' and zhuangt=1";
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
					+ "where leix='汽车衡数据自动复制' and mingc = '是' and zhuangt=1";
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
	 * 从数据库中提取参数 写入js
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
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡接口串口号' and diancxxb_id="
					+ diancid;
			ResultSet wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				commport = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡接口波特率' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				BaudRate = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡接口奇偶校验' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				jiojy = wsrs.getString("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡接口数据位' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				shujw = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡接口停止位' and diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				tingzw = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix= '汽车衡检斤测试参数' and zhuangt=1 and  diancxxb_id="
					+ diancid;
			wsrs = con.getResultSet(sql);
			while (wsrs.next()) {
				isTest = wsrs.getInt("mingc");
			}
			wsrs.close();
			sql = "select jiesbz,mingc from jicxxb where leix='汽车衡检斤可采集重量差' and zhuangt=1 and diancxxb_id ="
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
							.append("var commport = window.prompt(\"请输入端口号!\",1);\n");
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
							.append("var BaudRate = window.prompt(\"请输入波特率!\",4800);\n");
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
							.append("var jiojy = window.prompt(\"请输入奇偶校验!\",\"n\");\n");
				} else {
					array.append("var jiojy = " + getParitySetting() + ";\n");
				}
			} else {
				array.append("var jiojy = '" + jiojy + "';\n");
				setParitySetting(String.valueOf(jiojy));
			}
			if (shujw == -1) {
				if (getDataBits().equals("-1")) {
					array.append("var shujw = window.prompt(\"请输入数据位!\",8);\n");
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
							.append("var tingzw = window.prompt(\"请输入停止位!\",1);\n");
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
			String jilhh = si.getBasicValue("汽车衡检斤的默认计量衡号", "A",con);

			array.append("var jilhh = '" + jilhh + "';\n");

			sbSql.setLength(0);
			sbSql
					.append("SELECT * FROM XITXXB X WHERE X.DUIXM = '汽车衡衡号' And shifsy = 1");
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
								+ "','年月日',TO_CHAR(QC.JIANMSJ,'YYYYMMDD') ,TO_CHAR(QC.JIANMSJ,'MMDD')) As ges,\n"
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
					if (bianhType.equals("年月日")) {
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
	 * 初始化毛重设置
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
		// 如果皮重list不为空且有值 则从皮重list中提取毛重相关项的默认值
		// 否则从系统设置中提取
//		JDBCcon con = new JDBCcon();
		
		if (_PizList != null && !_PizList.isEmpty()) {
//			int size = _PizList.size();
			//皮重的数据是按照检毛时间降序排列的，所以采集首行记录即可
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
						.append("select x.zhi from xitxxb x where x.duixm = '煤场默认项'");
				ResultSet meicmcRs = con.getResultSet(sql);
				if (meicmcRs.next()) {
					mmeic = meicmcRs.getString("zhi");
				}
				meicmcRs.close();
				sql = new StringBuffer();
				sql.append("select id, zhi from xitxxb where duixm = '煤管员'");
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

		// 取毛重的修约原则
//		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select leix,jiesbz from jicxxb " + "where mingc='汽车衡修约方式' "
				+ "and zhuangt=1 and xuh=1");
		ResultSet rs = con.getResultSet(sql);
		setScale(-1);
		setRuleForRounding("");
		try {
			if (rs.next()) {
				String leix = rs.getString("leix");
				int scale = rs.getInt("jiesbz");
				setScale(scale);
				if (leix.equals("四舍五入")) {
					setRuleForRounding("UpOrDown");
				} else if (leix.equals("进位")) {
					setRuleForRounding("Up");
				} else if (leix.equals("舍去")) {
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

		setShowDialog(si.getBasicValue("弹出汽车衡毛重检斤提示窗口", "n",con));
	}

	/*
	 * 毛重bean 及 list
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
		String showTimdh = si.getBasicValue("汽车煤是否显示提煤单号", "0");
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
	 * 保存毛重事件
	 */
	private void SaveMaoz() {
        JDBCcon con = new JDBCcon();
	    try {
            System.out.println(getJilhh());
            List _maozList = getEditValues_M();

            if (_maozList == null || _maozList.isEmpty()) {
                setMsg("保存失败!毛重数据为空!");
                return;
            }

            con.setAutoCommit(false);
            int listIndex = 0;
            String cheph = ((Qichmzbean) _maozList.get(listIndex)).getCheph();
            String timdh = ((Qichmzbean) _maozList.get(listIndex)).getTimdh();//提煤单号
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
            // 如果为盘煤则判断盘点煤场是否正确填写 并将zhuangt 置为9
            if (((Qichmzbean) _maozList.get(listIndex)).getSelected()) {
                if (pandmc_id == -1) {
                    setMsg("保存失败!请正确填写盘点煤场!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                zhuangt = 9;
            } else {
                if (fahdwb_id == -1) {
                    setMsg("保存失败!请正确填写发货单位!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (meikxxb_id == -1) {
                    setMsg("保存失败!请正确填写煤矿地区!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (ranlpzb_id == -1) {
                    setMsg("保存失败!请正确填写品种!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
                if (jihkjb_id == -1) {
                    setMsg("保存失败!请正确填写计划口径!");
                    con.rollBack();
                    con.setAutoCommit(true);
                    return;
                }
            }
            double maoz = ((Qichmzbean) _maozList.get(listIndex)).getMaoz();
            if (!getReg("汽车衡检斤", "毛重", maoz, con)) {
                setMsg("毛重超出数值合理范围!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            if (getMaozJudge()) {
                maoz = RoundMaoz(maoz);
                double maoz2 = getMaoz(cheph, maoz, con);
                if (maoz != maoz2) {
                    if (getShowMsg()) {
                        setMsg("本车毛重 " + maoz + "吨 超过毛重上限 " + maoz2
                                + "吨 ！\n本系统将自动计为" + maoz2 + "吨!");
                    }
                    maoz = maoz2;
                }
            }
            double fahl = ((Qichmzbean) _maozList.get(listIndex)).getFahl();
            if (!getReg("汽车衡检斤", "票重", fahl, con)) {
                setMsg("票重超出数值合理范围!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            double koud = ((Qichmzbean) _maozList.get(listIndex)).getKoud();
            long meicb_id = MainGlobal.getProperId(getMEIC_Model(),
                    ((Qichmzbean) _maozList.get(listIndex)).getMeic());
            if (meicb_id == -1) {
                setMsg("保存失败!请正确填写煤场!");
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
                setMsg("保存失败!请正确填写厂别!");
                con.rollBack();
                con.setAutoCommit(true);
                return;
            }
            String shouhr = ((Qichmzbean) _maozList.get(listIndex)).getShouhr();
            String beiz = ((Qichmzbean) _maozList.get(listIndex)).getBeiz();
            double piz = 0.0;
            // 如果页面上的皮重免称复选框被选中 则从 chepjbxx 中取得皮重
            if (getAutoPiz()) {
                piz = getPiz(cheph, con);
            }
            String jianpsj = "null";
            if (piz != 0) {
                jianpsj = "sysdate";
            }
            // 这是一个标识字段，用于识别发货量是否为人工录入数据
            int autoFahl = 0;
            if (fahl != 0) {
                autoFahl = 1;
            }
            // maoz = maoz -koud;//=毛重-扣吨
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
            sql.append("'" + cheph.trim() + "',"); // 车皮号
            sql.append("'" + shouhr + "',"); // 收货人
            sql.append(fahdwb_id + ","); // 发货单位
            sql.append(meikxxb_id + ","); // 煤矿单位
            sql.append(ranlpzb_id + ","); // 燃料品种
            sql.append(fahl + ","); // 发货量
            sql.append(maoz + ","); // 毛重=毛重-扣吨
            sql.append(piz + ","); // 皮重
            sql.append(koud + ","); // 扣吨
            sql.append(yuns + ","); // 运损
            sql.append(kuid + ","); // 扣吨
            sql.append("sysdate,"); // 检毛时间
            sql.append(jianpsj + ","); // 检皮时间
            sql.append(yingd + ","); // 盈吨
            sql.append(zhuangt + ","); // 状态
            sql.append(jihkjb_id + ","); // 计划口径
            sql.append("'" + meigy + "',"); // 煤管员
            sql.append("'" + chengydw + "',");// 承运单位
            sql.append("'" + jianjy + "',");// 检斤员
            sql.append("'" + beiz + "',"); // 备注
            sql.append(meicb_id + ","); // 煤场
            sql.append(xuh + ","); // 序号
            sql.append(changbb_id + ","); // 厂别
            sql.append(pandmc_id); // 盘点煤场
            sql.append(",'").append(getJilhh());
            sql.append("'," + autoFahl + ",'" + timdh + "')");
            int flag = con.getInsert(sql.toString());
            if (flag == -1) {
                System.out.println("插入毛重失败! Qichmz.java SaveMaoz()。");
                setMsg("汽车衡毛重保存失败！\n错误的sql语句：\n" + sql.toString());
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
	 * 初始化皮重设置
	 */
	public void getInitPiz(JDBCcon con) {
		//Qiuzw 2009年3月13日 初始化“皮重”时，要求当日未回皮或者用户手工退回（q.huipbz = 0）
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
			setMsg("获取皮重相关信息失败！");
			e.printStackTrace();
		} finally {
//			con.Close();
		}
		setEditValues_P(_PizList);
	}

	public void getInitData(JDBCcon con) {
//		System.out.println("页面参数初始化...");
		InitParameter(con);
//		System.out.println("页面参数初始化1");
		getInitPiz(con);
//		System.out.println("页面参数初始化2");

//		System.out.println("页面参数初始化3");
		getArrayScript();
//		System.out.println("页面参数初始化.");
	}

	/*
	 * 皮重bean 及 list
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
	 * 页面提交事件
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
//			System.out.println("准备保存...");
			SaveMaoz();
//			System.out.println("保存结束.");
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
	 * 页面初始化
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if (!visit.getActivePageName().equals(this.getPageName())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法

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
		if(si.getBasicValue("禁用汽车衡称重页面上的显示参数按钮", 0,con)==1) {
			setDisabled(true);
		}
		con.Close();
	}

	/*
	 * 取得当天排序号
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
			System.out.println("获取序号失败!");
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
	 * 按基础信息设置修约毛重
	 */
	public double RoundMaoz(double maoz) {
		double _maoz = maoz;
		// JDBCcon con = new JDBCcon();
		// StringBuffer sql = new StringBuffer();
		// sql.append("select leix,jiesbz from jicxxb " + "where mingc='汽车衡修约方式'
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
				// 四舍五入
			} else if (leix.equals("Up")) {
				// 进位
				if (maoz > _maoz) {
					_maoz = _maoz + Arith.div(1, Math.pow(10, scale));
				}
			} else if (leix.equals("Down")) {
				// 舍去
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
	 * 取得并判断毛重是否超出最大限 如超出 返回毛重最大限
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
	 * 取得并判断皮重是否低于最小值 如过低 返回皮重最小值
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
	 * 取得已存皮重
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
	 * 取得汽车衡运损率
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
			System.out.println("获取汽车煤运损率失败!");
			e.printStackTrace();
		} finally {
			conyunsl.Close();
		}
		return yunsl;
	}

	/*
	 * 判断数值合理范围
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
			setMsg("数值合理范围判断时出现异常！");
		}
		return flag;
	}

	/*
	 * 下拉框组件设置
	 */
	// 燃料品种下拉框设置
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
		// where leix ='煤'
		setIRanlpzb_idModel(new IDropDownSelectionModel(sql));
	}

	// 计划口径表下拉框设置
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

	// 发货单位下拉框
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

	// 煤矿单位下拉框
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

	// 所有煤矿下拉框
	public IPropertySelectionModel getAllMeikxxbModels(long fahdwb_id) {
//		StringBuffer sql = new StringBuffer(
//				"select id ,meikdwmc from meikxxb where meikdqb_id ="
//						+ fahdwb_id + " order by xuh");
		String sql="select m.id ,m.mingc meikdwmc from meikxxb m \n" +
                "left join gongysmkglb g on m.id=g.meikxxb_id \n" +
                "/*where g.gongysb_id="+fahdwb_id+ "*/ order by m.mingc";
		return new IDropDownSelectionModel(sql);
	}

	// 煤管员下拉框
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
				"select id, zhi from xitxxb where duixm = '煤管员'");
		setMEIGY_Model(new IDropDownSelectionModel(sql));
	}

	// 承运单位下拉框
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
				"select id, jianc from FAHDWB where beiz = '运输公司'");
		setCHENGYDW_Model(new IDropDownSelectionModel(sql));
	}

	// 煤场下拉框
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

	// 厂别下拉框
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

	// 盘点煤场下拉框
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
	 * 取得并滚动当日基本信息
	 */
	public String getInfo() {
		String info = "";
		StringBuffer sql = new StringBuffer();
		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date());
		sql.append("select decode(q.zhuangt,9,'盘煤',null,'共计','进厂') zhuangt,\n");
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
				info = "  今日共过衡:" + zchec + "车;其中检毛:" + mchec + "车,检皮:" + pchec
						+ "车。" + "共计净重:" + jingz + "吨,票重:" + piaoz + "吨,运损:"
						+ yuns + "吨。";
			} else {
				info = "  今日暂无检斤。";
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
	 * 取得随车检斤单 不想往页面传太多的参数所以在后台将所有的数据写好后传出！ 无奈之举！
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
//					.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单标题' and shifsy=1");
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
//					.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单单位' and shifsy=1");
//			rs = con.getResultSet(sql);
//			try {
//				while (rs.next()) {
//					danw = rs.getString("zhi");
//				}
//				rs.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			// 下面的SQL排序用的是检皮时间而过滤用的是检毛时间,
//			// 如果存在隔天过车的情况就会出现打印随车检斤单按钮不好用的问题。
//			// 我也不是很理解 但是原来程序是这么写的 不知道有什么作用 所以就没有改动。
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
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 18pt; font-family:楷体_GB2312; padding-left:4px; padding-right:4px; text-align:center; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"标题\" value=\""
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
//						+ "		<p align=\"left\" style=\"FONT-SIZE: 11pt； FONT-FAMILY: 新宋体;\">时间：<input \n"
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"时间\" value=\""
//						+ Jianpsj
//						+ "\" id=\"Shij\" \n"
//						+ "			size=\"22\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:100px; height:30px\" \n"
//						+ "			align=\"middle\"></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:186px; height:30px\" \n"
//						+ "			align=\"middle\" colspan=\"2\" style=\"FONT-SIZE: 11pt； FONT-FAMILY: 新宋体;\">序号：<input \n"
//						+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"序号\" value=\""
//						+ chex
//						+ "\" id=\"Chex\" \n"
//						+ "			size=\"8\" />车</td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">矿别</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:300px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"3\"> \n"
//						+ "		<p align=\"center\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"矿别\" value=\""
//						+ kuangb
//						+ "\" id=\"Kuangb\" \n"
//						+ "			size=\"30\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">单位</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  vertical-align:middle; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"单位\" value=\""
//						+ danw
//						+ "\" id=\"Danw\" \n"
//						+ "			size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">承运单位</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:300px\" \n"
//						+ "			align=\"middle\" colspan=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"承运单位\" value=\""
//						+ changydw
//						+ "\" \n"
//						+ "			id=\"Chengydw\" size=\"30\" /></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; FONT-FAMILY: 新宋体; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">车号</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none #000000; BORDER-LEFT: medium none #000000; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"车号\" value=\""
//						+ cheph
//						+ "\" id=\"Cheh\" \n"
//						+ "			size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">毛重时间</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:200px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"毛重时间\" value=\""
//						+ Jianmsj
//						+ "\" \n"
//						+ "			id=\"Jianmsj\" size=\"22\" /></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">皮重时间</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1.5pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000; style: ; width:186px; height:25px\" \n"
//						+ "			align=\"middle\" colspan=\"2\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"皮重时间\" value=\""
//						+ Jianpsj
//						+ "\" \n"
//						+ "			id=\"Jianpsj\" size=\"22\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"HEIGHT: 25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\">毛重(吨)</font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: \" \n"
//						+ "			align=\"middle\">皮重(吨)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; FONT-FAMILY: 新宋体; BORDER-RIGHT: 1pt solid #000000; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; BORDER-BOTTOM: 1pt solid #000000; style: ; height:25px\" \n"
//						+ "			align=\"middle\">扣吨(吨)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">途损(吨)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">净重(吨)</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\">票重</td> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:25px\"> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\"> \n"
//						+ "		<p align=\"center\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ maoz
//						+ "\" displayName=\"毛重\" id=\"Maoz\" size=\"7\" /> \n"
//						+ "		</td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ piz
//						+ "\" displayName=\"皮重\" id=\"Piz\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1pt solid #000000; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid #000000; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ koud
//						+ "\" displayName=\"扣吨\" id=\"Koud\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ yuns
//						+ "\" displayName=\"运损\" id=\"Yuns\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><font size=\"3\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ jingz
//						+ "\" displayName=\"净重\" id=\"Jingz\" size=\"7\" /></font></td> \n"
//						+ "		<td \n"
//						+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
//						+ "			align=\"middle\"><input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 value=\""
//						+ fahl
//						+ "\" displayName=\"票重\" id=\"Biaoz\" size=\"7\" /></td> \n"
//						+ "	</tr> \n"
//						+ "	<tr> \n"
//						+ "		<TD \n"
//						+ "			style=\"BORDER-RIGHT: windowtext 1pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: windowtext 1.5pt solid; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none\" \n"
//						+ "			align=middle><FONT size=3>备注</FONT></TD> \n"
//						+ "		<TD align=middle colSpan=5 width=\"100\" \n"
//						+ "			style=\"BORDER-RIGHT: windowtext 1.5pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
//						+ "			align=middle><FONT size=3></FONT> <input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"备注\" value=\""
//						+ beiz
//						+ "\" id=\"Beiz\" \n"
//						+ "			size=\"26\" /></TD> \n"
//						+ "	</tr> \n"
//						+ "	<tr style=\"height:30px\"> \n"
//						+ "		<td align=\"middle\" colspan=\"3\" style=\"width: 300px; height: 30px\"> \n"
//						+ "		<p align=\"left\" valign=\"center\" \n"
//						+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;\">检 斤 员：<input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"检斤员\" value=\""
//						+ Jianjy
//						+ "\" id=\"Jianjy\" \n"
//						+ "			size=\"10\" /> \n"
//						+ "		</td> \n"
//						+ "		<td style=\"WIDTH: 100px; HEIGHT: 30px\" align=\"middle\"></td> \n"
//						+ "		<td style=\"WIDTH: 200px; HEIGHT: 30px\" align=\"middle\" colSpan=\"2\"> \n"
//						+ "		<p align=\"right\" valign=\"center\" \n"
//						+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;\">煤 管 员：<input \n"
//						+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
//						+ "			 displayName=\"煤管员\" value=\""
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
