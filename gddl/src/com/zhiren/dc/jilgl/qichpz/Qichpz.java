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

/**
 * 日期：2011年3月21日
 * 作者：Liufl
 * 修改内容：添加承运单位
 */
/*
 * 日期：2010年7月27日
 * 作者：Qiuzw
 * 修改内容：保存数据时，对部分数据进行修约处理
 * 
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
 * 描述：1、页面每次提交时，初始化页面脚本变量
 *       2、增加“扣皮重”、“扣杂”操作
 *       3、将原来在服务器端处理的扣吨数量的操作移至客户端处理
 *       4、页面初始化时，可以生成相应的过磅单编号
 * 影响电厂：
 *       神头电厂修改
 */
/*
 * 时间：2008-05-25
 * 作者：Qiuzw
 * 描述：由于保存数据时，有错误发生时，没有将commit状态修改为true，可能导致数据读取错误。
 */
/*
 * 时间：2008-04-29
 * 作者：Qiuzw
 * 描述：
 *      打印检斤单时显示的检皮人员名字。
 *      平圩电厂提出检毛人员和检皮人员是2人操作，应该是不同值。
 * 修改前：
 *     “检皮人员”=检毛人员
 * 修改后：
 *     “检皮人员”=系统登录人员
 * 影响电厂：
 *     平圩电厂
 */
/*
 * 时间：2015-04-13
 * 作者：夏峥
 * 描述：增加参数配置是否显示过磅单的表格
 * 	   select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单显示表格' and shifsy=1
 *	          并按照临河电厂提供的样式进行调整
 */
package com.zhiren.dc.jilgl.qichpz;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiren.common.*;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.main.Visit;

public class Qichpz extends BasePage {

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

	private String _msgdialog;

	public void setMsgDialog(String _value) {
		_msgdialog = _value;
	}

	public String getMsgDialog() {
		if (_msgdialog == null) {
			_msgdialog = "";
		}
		return _msgdialog;
	}

	protected void initialize() {
		super.initialize();
		_msg = "";
		_msgdialog = "";
	}
	
//	private double zhonglybc = 0.0;

	public String getZhonglybc() {
		return ((Visit)getPage().getVisit()).getString8();
	}

	public void setZhonglybc(String z) {
		((Visit)getPage().getVisit()).setString8(z);
	}

	// 毛重超差提示
	private String _showDialog = "n";

	public String getShowDialog() {
		return _showDialog;
	}

	public void setShowDialog(String v) {
		_showDialog = v;
	}

	// 保存毛重提示
	private String _showBaocts = "y";

	public String getShowBaocts() {
		return _showBaocts;
	}

	public void setShowBaocts(String v) {
		_showBaocts = v;
	}

	public String getcontext() {
		String context =  "var  targetPath='http://"
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
	 * 初始化毛重超差判定、皮重超差判定、超差提示、及免称皮重 四个参数
	 */
	public void InitParameter() {
		JDBCcon con = new JDBCcon();
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
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
			int zhonglc = 5;//杨晓宗加,关于汽车衡采样的问题!
			sql = "SELECT JIESBZ,MINGC FROM JICXXB WHERE LEIX='汽车衡检斤可采集重量差(皮)' AND ZHUANGT=1 AND DIANCXXB_ID ="
					+ diancid;
			wsrs = con.getResultSet(sql);
			if (wsrs.next()) {
				zhonglc = wsrs.getInt("MINGC");
			}
			wsrs.close();
			array.append(" drop = new Array();\n ");
			array.append(" drop1 = new Array();\n");
//			sbSql
//					.append("select 0,m.id ,m.meikdqmc,nvl(m.pinysy,0) pinysy,m.xuh from meikdqb m,changkglb c where c.zhuangt=1 and c.diancxxb_id= "
//							+ diancid + " and c.meikxxb_id = m.id order by xuh");
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
			}
			if (shujw == -1) {
				if (getDataBits().equals("-1")) {
					array.append("var shujw = window.prompt(\"请输入数据位!\",8);\n");
				} else {
					array.append("var shujw = " + getDataBits() + ";\n");
				}
			} else {
				array.append("var shujw = " + shujw + ";\n");
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
			}
			array.append("var isTest = " + isTest + ";\n");
			SysInfo si = new SysInfo();
			String jilhh = si.getBasicValue("汽车衡检斤的默认计量衡号", "A");

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
				//原来sql中日期条件影响执行效率
				
//				String strToday = cd.FormatDate(new Date());
				ResultSet r = con
						.getResultSet("Select ges,Trim(to_char(Max(xuh)+1,'000')) xuh From (select DECODE('"
								+ bianhType
								+ "','年月日',TO_CHAR(QC.JIANMSJ,'YYYYMMDD') ,TO_CHAR(QC.JIANMSJ,'MMDD')) As ges,\n"
								+ "DECODE(LENGTH(QC.XUH),1,'00'||TO_CHAR(Qc.XUH),2,'0'||TO_CHAR(Qc.XUH),TO_CHAR(Qc.XUH)) AS XUH  "
								+ "from qichjjbtmp QC where jilhh = '"
								+ qichh
								+ "' AND JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1 ) Group By ges");
				if (r.next()) {
					bianh = bianh + "arrBianh[" + k + "] = '" + qichh + "';\n";
					k = k + 1;
					bianh = bianh + "arrBianh[" + k + "] = '"
							+ (r.getString("ges") + r.getString("XUH"))
							+ "';\n";
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
			array.append(bianh);
			array.append("var zhonglc = " + zhonglc + ";\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			con = null;
		}
		return array.toString();
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
	 * 初始化皮重设置
	 */
	public void getInitPiz() {
		List _PizList = new ArrayList();
		JDBCcon con = new JDBCcon();
		CustomDate cd = new CustomDate();
		
//		String strToday = cd.FormatDate(new Date());
		StringBuffer sql = new StringBuffer();
		// q.piaobz = 1条件限制用户不能看到已经保存为“票不存档”的数据
		sql.append("select * from (select pz.* ,ceil(rownum/80) pagenum from ");
		sql
				.append("(select q.id, q.cheph, nvl(jb.piz,0) as biaozpz, d.mingc meikdqmc, k.mingc meikdwmc, r.pinz, j.mingc as kouj, q.fahl, \n");
		sql.append("q.maoz, q.piz, q.koud, q.chengydw, q.meigy, \n");
		sql
				.append("(select nvl(meicmc,'') as pandmc from meicfqb where id(+) = q.pandmc_id) as pandmc, \n");
		sql
				.append("(select meicmc from meicfqb where id = q.meicb_id) as meic, \n");
		sql.append("q.shouhr, c.mingc as changb, \n");
		sql
				.append("to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(q.jianmsj,'yyyy-mm-dd'),'yyyy-mm-dd')-nvl(q.beiz,0) beiz,Diaodbh \n");
		sql
				.append("from qichjjbtmp q, gongysb d, meikxxb k, ranlpzb r, jihkjb j, changbb c,(select * from chepjbxx where shiyzt = 1) jb \n");
		sql
				.append("where q.meikdqb_id = d.id(+) and q.meikxxb_id = k.id(+) and q.ranlpzb_id = r.id(+) \n");
		sql
				.append("and q.jihkjb_id = j.id(+) and q.changbb_id = c.id and q.cheph = jb.cheph(+) and q.piz=0 and q.piaobz = 1  and q.huipbz = 0  order by q.jianmsj) pz)\n");
		sql.append("where pagenum = " + getCurrentPageNumber());
		sql.append("\n union \n ");
		sql.append("select * from (select pz.* ,ceil(rownum/80) pagenum from ");
		sql
				.append("(select q.id, q.cheph, nvl(jb.piz,0) as biaozpz, d.mingc meikdqmc, k.mingc meikdwmc, r.pinz, j.mingc as kouj, q.fahl, \n");
		sql.append("q.maoz, q.piz, q.koud, q.chengydw, q.meigy, \n");
		sql
				.append("(select nvl(meicmc,'') as pandmc from meicfqb where id(+) = q.pandmc_id) as pandmc, \n");
		sql
				.append("(select meicmc from meicfqb where id = q.meicb_id) as meic, \n");
		sql.append("q.shouhr, c.mingc as changb, \n");
		sql
				.append("to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd')-to_date(to_char(q.jianmsj,'yyyy-mm-dd'),'yyyy-mm-dd')-nvl(q.beiz,0) beiz,Diaodbh \n");
		sql
				.append("from qichjjbtmp q, gongysb d, meikxxb k, ranlpzb r, jihkjb j, changbb c,(select * from chepjbxx where shiyzt = 1) jb \n");
		sql
				.append("where q.meikdqb_id = d.id(+) and q.meikxxb_id = k.id(+) and q.ranlpzb_id = r.id(+) \n");
		sql
				.append("and q.jihkjb_id = j.id(+) and q.changbb_id = c.id and q.cheph = jb.cheph(+) and q.piz=0 and q.piaobz = 1 and q.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1  order by q.jianmsj) pz)\n");
		sql.append("where pagenum = " + getCurrentPageNumber());
		try {
			
			//原来sql中的riq条件影响执行效率
			ResultSet rs = con
					.getResultSet(new StringBuffer(
							"select ceil(count(*)/80) as total from qichjjbtmp q where piz=0 and q.piaobz = 1 and (q.huipbz = 0 or q.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1)"));
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
				double koud = 0.0;// rs.getDouble("koud");在数据库中新增加“koupz”字段
				String meic = rs.getString("meic");
				String meigy = rs.getString("meigy");
				String shouhr = rs.getString("shouhr");
				String chengydw = rs.getString("chengydw");
				String changb = rs.getString("changb");
				String beiz = rs.getString("beiz");
				double biaozpz = rs.getDouble("biaozpz");
				String timdh = rs.getString("Diaodbh");
				double kouz = 0.0;

				Qichpzbean qcpz = new Qichpzbean(id, false, cheph, pandmc,
						meikdqmc, meikdwmc, pinz, kouj, fahl, maoz, piz, koud,
						meic, meigy, shouhr, chengydw, changb, beiz, biaozpz);
				qcpz.setBiaozpz(biaozpz);
				qcpz.setKouz(kouz);
				qcpz.setPiao("是");
				qcpz.setTimdh(timdh);
				_PizList.add(qcpz);
			}
			rs.close();
		} catch (Exception e) {
			setMsg("获取皮重相关信息失败！");
			e.printStackTrace();
		} finally {
			con.Close();
		}
		setEditValues_P(_PizList);

		sql = new StringBuffer();
		sql.append("select leix,jiesbz from jicxxb " + "where mingc='汽车衡修约方式' "
				+ "and zhuangt=1 and xuh=2");
		setScale(-1);
		setRuleForRounding("");
		try {
			ResultSet rs = con.getResultSet(sql);
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
			rs = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
			con = null;

		}
		SysInfo si = new SysInfo();
		setShowBaocts(si.getBasicValue("弹出汽车衡皮重检斤提示窗口", "n"));
		setShowDialog(si.getBasicValue("弹出汽车衡皮重超差提示窗口", "n"));

	}

	/*
	 * 保存皮重事件
	 */
	private void SavePiz() {
        JDBCcon con = new JDBCcon();
	    try{
            List _pizList = getEditValues_P();
            if (_pizList == null || _pizList.isEmpty()) {
                setMsg("保存失败!皮重数据为空!");
                return;
            }

            con.setAutoCommit(false);
            for (int listIndex = 0; listIndex < _pizList.size(); listIndex++) {
                Qichpzbean qcpz = ((Qichpzbean) _pizList.get(listIndex));
                boolean selected = qcpz.getSelected();
                if (selected) {
                    String chengydw=qcpz.getChengydw();
                    long id = qcpz.getId();
                    if (getPiz(id) != 0) {
                        setMsg("车号选择错误，该车已经完成检皮!");
                        con.setAutoCommit(true);
                        return;
                    }
                    long fahdwb_id = MainGlobal.getProperId(getIFahdwb_idModel(),
                            qcpz.getMeikdqmc());
                    long meikxxb_id = MainGlobal.getProperId(
                            getAllMeikxxbModels(fahdwb_id), ((Qichpzbean) _pizList
                                    .get(listIndex)).getMeikdwmc());
                    if (meikxxb_id==-1) {
                        setMsg("未能正确获得煤矿信息!");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }

                    String cheph = qcpz.getCheph();
                    double maoz = qcpz.getMaoz();
                    if (!getReg("汽车衡检斤", "毛重", maoz, con)) {
                        setMsg("毛重超出数值合理范围!");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }
                    double fahl = qcpz.getFahl();
                    if (!getReg("汽车衡检斤", "票重", fahl, con)) {
                        setMsg("票重超出数值合理范围!");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }
                    long meicb_id = MainGlobal.getProperId(getMEIC_Model(), qcpz
                            .getMeic());
                    if (meicb_id == -1) {
                        setMsg("保存失败!请正确填写煤场!");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }
                    double koud = qcpz.getKoud();
                    double piz = qcpz.getPiz();
                    piz = RoundPiz(piz);
                    if (getAutoPiz()) {
                        piz = getPiz(cheph);
                    } else if (getPizJudge()) {
                        double piz2 = getPiz(cheph, piz);
                        if (piz != piz2) {
                            if (getShowMsg()) {
                                setMsg("本车皮重 " + piz + "吨 低于皮重下限 " + piz2
                                        + "吨 ！\n本系统将自动计为" + piz2 + "吨!");
                            }
                            piz = piz2;
                        }
                    }
                    double kouz = 0.0;
                    // koud--包含皮重扣吨和煤场扣杂
                    // koupz--皮重扣吨，包含煤场扣杂
                    // kouz--独立的煤场扣杂
                    kouz = qcpz.getKouz();

                    if (!getReg("汽车衡检斤", "皮重", (piz - kouz), con)) {
                        // 判断皮重超差时，不考虑扣杂质部分
                        setMsg("皮重超出数值合理范围!");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }
                    if (maoz < piz) {
                        setMsg("皮重 " + piz + " 大于毛重 " + maoz + " ?");
                        con.rollBack();
                        con.setAutoCommit(true);
                        return;
                    }
                    double yingd = 0.0;
                    double kuid = 0.0;
                    double yuns = 0.0;
                    String jianjy = ((Visit) getPage().getVisit()).getRenymc();
                    if (fahl != 0 && maoz != 0 && piz != 0) {
                        double yingk = maoz - piz - fahl;
                        double yunsl = getYunsl(meikxxb_id);
                        if (yunsl==-1) {
                            setMsg("获取汽车煤运损率失败!");
                            con.rollBack();
                            con.setAutoCommit(true);
                            return;
                        }
                        yuns = CustomMaths.Round_new(yunsl * fahl,2);
                        if (yingk >= 0) {
                            yingd = yingk;
                            yuns = 0;
                        } else if (Math.abs(yingk) <= yuns) {
                            yuns = Math.abs(yingk);
                        } else {
                            kuid = Math.abs(yingk) - yuns;
                        }
                    }
                    if (fahl == 0 && piz != 0) {
                        fahl = maoz - piz;
                    }

                    int piaobz = 1;
                    if (qcpz.getPiao().equals("否")) {
                        // 判断“票”是否有存档
                        piaobz = 0;
                        piz = 0;// 现在数据提交
                    }
                    String timdh = qcpz.getTimdh();
                    StringBuffer sql = new StringBuffer();
                    sql.append("update qichjjbtmp set\n");
                    sql.append(" meicb_id =" + meicb_id + "\n");
                    sql.append(" ,fahl = " + fahl + "\n");
                    sql.append(" ,maoz = " + maoz + "\n");
                    sql.append(" ,piz = " + piz + "\n");
                    sql.append(" ,koud = koud + " + (koud + kouz) + "\n");
                    sql.append(" ,koupz = " + koud + "\n");
                    sql.append(" ,kouz = " + kouz + "\n");
                    sql.append(" ,yuns = " + yuns + "\n");
                    sql.append(" ,kuid = " + kuid + "\n");
                    sql.append(" ,jianpsj = sysdate\n");
                    sql.append(" ,yingd = " + yingd + " \n");
                    sql.append(" ,jianpy ='" + jianjy + "' \n");
                    sql.append(" ,piaobz =" + piaobz + " \n");
                    sql.append(" ,chengydw ='" + chengydw + "' \n");
                    sql.append(" ,Diaodbh ='" + timdh + "' \n");
                    sql.append(" where id=" + id);

                    int flag = con.getUpdate(sql.toString());
                    if (flag == -1) {
                        System.out.println("插入皮重失败! Qichpz.java SavePiz()。");
                        setMsg("汽车衡皮重保存失败！\n错误的sql语句：\n" + sql.toString());
                        con.rollBack();
                        con.Close();
                        con.setAutoCommit(true);
                        return;
                    }
                    sql = new StringBuffer();
                    sql.append("select * from chepjbxx where cheph='"
                            + cheph.trim() + "' and shiyzt = 1");
                    if (con.getHasIt(sql.toString())) {
                        sql = new StringBuffer(
                                "insert into chepjbxx (id,cheph,maoz,piz,maozcc,pizcc,chaoccs,riq)"
                                        + " values(xl_chepzlb_id.nextval,'"
                                        + cheph.trim()
                                        + "',"
                                        + maoz
                                        + ","
                                        + piz
                                        + ",0,0,0,to_date(to_char(sysdate,'yyyy-mm-dd'),'yyyy-mm-dd'))");
                        flag = con.getInsert(sql.toString());
                    }
                }
            }
            con.commit();
            con.setAutoCommit(true);
        }catch (Exception e){
	        e.printStackTrace();
	        con.rollBack();
        }finally {
            con.Close();
        }

	}

	private boolean _SavePizChick = false;

	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}

	public void getInitData() {
		getInitPiz();
		getArrayScript();
	}

	/*
	 * 皮重bean 及 list
	 */
	private Qichpzbean _EditValue_P;

	public Qichpzbean getEditValue_P() {
		return _EditValue_P;
	}

	public void setEditValue_P(Qichpzbean EditValue_P) {
		_EditValue_P = EditValue_P;
	}

	public List getEditValues_P() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues_P(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
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
		if (_ToFirstPageChick) {
			_ToFirstPageChick = false;
			setCurrentPageNumber(1);
			getInitData();
		}
		if (_ToPreviousPageChick) {
			_ToPreviousPageChick = false;
			setCurrentPageNumber(getCurrentPageNumber() - 1);
			getInitData();
		}
		if (_ToNextPageChick) {
			_ToNextPageChick = false;
			setCurrentPageNumber(getCurrentPageNumber() + 1);
			getInitData();
		}
		if (_ToLastPageChick) {
			_ToLastPageChick = false;
			setCurrentPageNumber(getTotalPageNumber());
			getInitData();
		}
		if (_GoPageChick) {
			_GoPageChick = false;
			setCurrentPageNumber(getGoPageNumber());
			getInitData();
		}
		if (_SavePizChick) {
			_SavePizChick = false;
			//System.out.println(new Date());
			SavePiz();
			//System.out.println(new Date());
			getInitData();
		}
	}

	/*
	 * 页面初始化
	 */
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		SysInfo si = new SysInfo();
		visit.setRenymc(null);
		if (!visit.getActivePageName().equals(this.getPageName())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法

			visit.setActivePageName(getPageName());
			visit.setList1(null);
			visit.setList2(null);
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
			getInitPiz();
			
			
			setCommPort("-1");
			setBaudRate("-1");
			setDataBits("-1");
			setStopBits("-1");
			setParitySetting("-1");
			visit.setLong1(1);
			visit.setLong2(1);
			visit.setLong3(1);
			InitParameter();
			getInitData();
		}
		
		if (si.getBasicValue("禁用汽车衡称重页面上的显示参数按钮", 0) == 1) {
			setDisabled(true);
		}
	}

	/*
	 * 取得当天排序号
	 */
	public int getXuh() {
		int xuh = 0;
		JDBCcon con = new JDBCcon();
		CustomDate cd = new CustomDate();
//		String strToday = cd.FormatDate(new Date());
		StringBuffer sql = new StringBuffer(
				"select nvl(max(xuh),0) +1 as xuh from qichjjbtmp where " +
				"jianmsj BETWEEN trunc(sysdate) and trunc(sysdate)+1") ;
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
			con.Close();
		}
		return xuh;
	}

	/*
	 * 按基础信息设置修约皮重
	 */
	public double RoundPiz(double piz) {
		double _piz = piz;
		// JDBCcon con = new JDBCcon();
		// StringBuffer sql = new StringBuffer();
		// sql.append("select leix,jiesbz from jicxxb " + "where mingc='汽车衡修约方式'
		// "
		// + "and zhuangt=1 and xuh=2");
		// ResultSet rs = con.getResultSet(sql);
		// try {
		// if (rs.next()) {
		String leix = getRuleForRounding();// rs.getString("leix");
		int scale = getScale();// rs.getInt("jiesbz");
		// _piz = Arith.round(piz, scale);
		_piz = CustomMaths.Round_new(piz, scale);
		if (leix.equals("UpOrDown")) {

		} else if (leix.equals("Up")) {
			if (piz > _piz) {
				_piz = _piz + Arith.div(1, Math.pow(10, scale));
			}
		} else if (leix.equals("Down")) {
			if (piz < _piz) {
				_piz = _piz - Arith.div(1, Math.pow(10, scale));
			}
		}
		// }
		// rs.close();
		// } catch (Exception e) {
		// e.printStackTrace();
		// } finally {
		// con.Close();
		// }
		return _piz;
	}

	/*
	 * 取得并判断毛重是否超出最大限 如超出 返回毛重最大限
	 */
	public double getMaoz(String cheph, double maoz) {
		double _maoz = maoz;
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select maoz from chepjbxx where maoz+maozcc<" + maoz
				+ " and cheph='" + cheph.trim() + "' and shiyzt = 1");
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				_maoz = rs.getDouble("maoz");
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
	public double getPiz(String cheph) {
		double piz = 0.0;
		JDBCcon conpiz = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select piz from chepjbxx where cheph='" + cheph.trim()
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
			conpiz.Close();
		}
		return piz;
	}

	/*
	 * 根据qichjjbtmp id取得已存皮重
	 */
	public double getPiz(long id) {
		double piz = 0.0;
		JDBCcon conpiz = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select piz from qichjjbtmp where id=" + id);
		ResultSet rs = conpiz.getResultSet(sql);
		try {
			if (rs.next()) {
				piz = rs.getDouble("piz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conpiz.Close();
		}
		return piz;
	}

	/*
	 * 取得汽车衡运损率
	 */
	public double getYunsl(long meikxxb_id) {
		double yunsl = -1;
		JDBCcon conyunsl = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("select qicysl as zhi from meikxxb where id=" + meikxxb_id);
		ResultSet rs = conyunsl.getResultSet(sql);
		try {
			if (rs.next()) {
				yunsl = rs.getDouble("zhi");
			}else{
				yunsl = 0.01;
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
		StringBuffer sql = new StringBuffer("select m.id, m.meikdwmc\n"
				+ "from changkglb c, meikxxb m\n"
				+ " where m.id = c.meikxxb_id\n"
				+ " and c.zhuangt = 1 and m.meikdqb_id = " + id + "\n"
				+ " and c.diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " order by m.xuh");
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

//	 承运单位下拉框
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
		StringBuffer sql = new StringBuffer("select id, meicmc from meicfqb order by meicmc");
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
		sql.append("sum(q.maoz) maoz, sum(q.piz) piz, sum(q.maoz - q.piz) jingz, \n");
		sql.append("sum(q.fahl) fahl, sum(q.yuns) yuns, count(jianmsj) jianmsj, \n");
		sql.append("count(jianpsj) jianpsj from qichjjbtmp q where zhuangt !=9 \n");		
//				.append("and to_char(jianmsj,'yyyy-mm-dd') = to_char(sysdate,'yyyy-mm-dd') \n ");
		sql.append("and jianmsj BETWEEN trunc(sysdate) and trunc(sysdate)+1 \n ");
		sql.append(" group by rollup(q.zhuangt) having (grouping(q.zhuangt)=1)");
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
		SysInfo si = new SysInfo();
		if (si.getBasicValue("检斤单使用套打格式", 0) == 1) {
			return getPrint();//使用非套打格式
		}
		
		//套打
		String _Report = "";
		JDBCcon con = new JDBCcon();
		String biaot = "汽车衡随车检斤单";
		String danw = "吨";
		String fenl = "0";
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单标题' and shifsy=1");
			ResultSet rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					biaot = rs.getString("zhi");
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			sql = new StringBuffer();
			sql.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单单位' and shifsy=1");
			rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					danw = rs.getString("zhi");
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				fenl = si.getBasicValue("打印检斤单分联", "0");
				setFenl(fenl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 下面的SQL排序用的是检皮时间而过滤用的是检毛时间,
			// 如果存在隔天过车的情况就会出现打印随车检斤单按钮不好用的问题。
			// 我也不是很理解 但是原来程序是这么写的 不知道有什么作用 所以就没有改动。
			sql = new StringBuffer();
			CustomDate cd = new CustomDate();
			
//			String strToday = cd.FormatDate(new Date());
			sql.append("select qichjjbtmp.id,\n"
							+ " qichjjbtmp.xuh,\n"
							+ " qichjjbtmp.jihkjb_id,\n"
							+ " cheph,\n"
							+ " shouhr,\n"
							+ " qichjjbtmp.meikdqb_id,\n"
							+ " meikdqb.meikdqmc,\n"
							+ " qichjjbtmp.meikxxb_id,\n"
							+ " meikxxb.meikdwmc,\n"
							+ " ranlpzb.pinz,\n"
							+ " fahl,\n"
							+ " maoz,\n"
							+ " nvl(qichjjbtmp.piz,0)-nvl(qichjjbtmp.kouz,0)-nvl(qichjjbtmp.koupz,0) as piz,\n"
							+ " maoz - piz as jingz,\n"
							+ " koud,\n"
							+ " yuns,\n"
							+ " kuid,\n"
							+ " to_char(jianpsj, 'yyyy-mm-dd hh24:mi:ss') as jianpsj,\n"
							+ " to_char(jianmsj, 'yyyy-mm-dd hh24:mi:ss') as jianmsj,\n"
							+ " chengydw,\n"
							+ " meigy,\n"
							+ " qichjjbtmp.jianjy,\n"
							+ " qichjjbtmp.beiz\n"
							+ " from qichjjbtmp, meikdqb, meikxxb,ranlpzb\n"
							+ " where meikdqb.id = qichjjbtmp.meikdqb_id\n"
							+ " and qichjjbtmp.meikxxb_id = meikxxb.id\n"
							+ " and qichjjbtmp.ranlpzb_id = ranlpzb.id\n"
//							+ " and to_char(qichjjbtmp.jianmsj, 'yyyy-mm-dd') =\n"	+ " to_char(sysdate, 'yyyy-mm-dd')\n"
							+ " AND QICHJJBTMP.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1"
							+ " and maoz <> 0\n"
							+ " and piz <> 0 and qichjjbtmp.zhuangt <>9 order by jianpsj desc");
			rs = con.getResultSet(sql);
			if (rs.next()) {
			
				String cheph = rs.getString("CHEPH");
				if (cheph == null) {
					cheph = "&nbsp;";
				}
				String kuangb = rs.getString("MEIKDWMC");
				if (kuangb == null) {
					kuangb = "&nbsp;";
				}
				String fahdw = rs.getString("meikdqmc");
				if (fahdw == null) {
					fahdw = "&nbsp;";
				}
				String changydw = rs.getString("CHENGYDW");
				if (changydw == null) {
					changydw = "&nbsp;";
				}
			
				String Jianpsj = rs.getString("JIANPSJ");
				if (Jianpsj == null) {
					Jianpsj = "&nbsp;";
				}
				String jianmsj = rs.getString("JIANMSJ");
				if (jianmsj == null) {
					jianmsj = "&nbsp;";
				}
				String pinz = rs.getString("pinz");
				if (pinz == null) {
					pinz = "&nbsp;";
				}
				
				String X_ID = rs.getString("ID");
				if (X_ID == null) {
					X_ID = "";
				}
			
				double maoz = rs.getDouble("MAOZ");
				double piz = rs.getDouble("PIZ");
				double jingz = rs.getDouble("JINGZ");
				double fahl = rs.getDouble("FAHL");
				double koud = rs.getDouble("KOUD");
							
				_Report ="<div align=center style=\"display:none\" id=\"di\"> \n"
						+ "<div align=center id=\"printjjd\"> \n"
						+ "<div align=center> <br><br>\n"+
						"<table  width=704 cellPadding=0 cellSpacing=0 style=\"font-family:宋体;font-size:13pt;border-left:0px solid rgb(0,0,0);border-top:1px solid rgb(0,0,0);border-right:1px solid rgb(0,0,0);border-bottom:1px solid rgb(0,0,0);\">"+
						"<tr height=0>\n" + 
						"<td width=50 valign=middle  class=tdNoneLeft>&nbsp;</td>\n" + 
						"<td width=204 colspan=2 valign=middle  border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+jianmsj+"</td>\n" + 
						"<td width=75 valign=middle  class=tdNoneLeft>&nbsp;</td>\n" + 
						"<td width=204 colspan=2 valign=middle  border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+Jianpsj+"</td>\n" + 
						"<td width=88 valign=middle  class=tdNoneLeft>&nbsp;</td>\n" + 
						"<td width=88 valign=middle  class=tdNoneLeft>&nbsp;</td>\n" + 
						"</tr>\n" + 
						"</table>\n" + 
						"<table  width=704 cellPadding=0 cellSpacing=0 style=\"font-family:宋体;font-size:13pt;border-left:1px solid rgb(0,0,0);border-top:1px solid rgb(0,0,0);border-right:1px solid rgb(0,0,0);border-bottom:1px solid rgb(0,0,0);\">\n" + 
						"<tr height=50>\n" + 
						"<td width=400 colspan=5 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+fahdw+"</td>\n" + 
						"<td width=304 colspan=3 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+changydw+"</td>\n" + 
						"</tr>\n" + 
						"<tr height=50>\n" + 
						"<td width=704 colspan=8 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n" + 
						"</tr>\n" + 
						"<tr height=50>\n" + 
						"<td width=88 valign=center  align='left' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+pinz+"</td>\n" + 
						"<td width=88 valign=center  align='left' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+fahl+"</td>\n" + 
						"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+maoz+"</td>\n" + 
						"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+piz+"</td>\n" + 
						"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+koud+"</td>\n" + 
						"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+jingz+"</td>\n" + 
						"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+cheph+"</td>\n" + 
						"<td width=88 valign=center  align='right' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n" + 
						"</tr>\n" + 
						"<tr height=55>\n" + 
						"<td width=264 colspan=3 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n" + 
						"<td width=88 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+kuangb+"</td>\n" + 
						"<td width=352 colspan=4 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:0px solid rgb(0,0,0);\">&nbsp;</td>\n" + 
						"</tr>\n" + 
						"</table>";
				
				String biaod="否";
				sql = new StringBuffer();
				sql.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单显示表格' and shifsy=1");
				rs = con.getResultSet(sql);
				try {
					while (rs.next()) {
						biaod = rs.getString("zhi");
					}
					rs.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if(biaod.equals("是")){
					_Report = "<div align=center style=\"display:none\" id=\"di\"> \n"
							+"<div align=center id=\"printjjd\"> \n"
							+"<div align=center>\n"
							+"<font style=\"FONT-SIZE: 22pt; font-family:楷体_GB2312;\">"+biaot+"</font><br>\n"
							+"<br>\n"
							+"<table  width=704 cellPadding=0 cellSpacing=0 style=\"font-family:宋体;font-size:13pt;border-left:0px solid rgb(0,0,0);border-top:0px solid rgb(0,0,0);border-right:0px solid rgb(0,0,0);border-bottom:0px solid rgb(0,0,0);\">\n"
							+"<tr height=0>\n"
							+"<td width=88 valign=middle  class=tdNoneLeft>过磅时间:</td>\n"
							+"<td width=274 colspan=2 valign=middle  border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+jianmsj+"</td>\n"
							+"<td width=44 valign=middle  class=tdNoneLeft>　</td>\n"
							+"<td width=274 colspan=2 valign=middle  border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+Jianpsj+"</td>\n"
							+"<td width=132 valign=middle  align='right' class=tdNoneLeft>NO:"+X_ID+"</td>\n"
							+"<td width=0  valign=middle  class=tdNoneLeft></td>\n"
							+"</tr>\n"
							+"</table>\n"
							+"<table  width=704 cellPadding=0 cellSpacing=0 style=\"font-family:宋体;font-size:13pt;border-left:1px solid rgb(0,0,0);border-top:1px solid rgb(0,0,0);border-right:1px solid rgb(0,0,0);border-bottom:1px solid rgb(0,0,0);\">\n"
							+"<tr height=50>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">单位</td>\n"
							+"<td width=274 colspan=3 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+fahdw+"</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">运输单位</td>\n"
							+"<td width=274 colspan=3 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+changydw+"</td>\n"
							+"</tr>\n"
							+"<tr height=50>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">货名</td>\n"
							+"<td width=92 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">矿方量("+danw+")</td>\n"
							+"<td width=86 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">毛重("+danw+")</td>\n"
							+"<td width=86 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">皮重("+danw+")</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">扣吨("+danw+")</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">净重("+danw+")</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">车号</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">车型</td>\n"
							+"</tr>\n"
							+"<tr height=50>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+pinz+"</td>\n"
							+"<td width=92 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+fahl+"</td>\n"
							+"<td width=86 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+maoz+"</td>\n"
							+"<td width=86 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+piz+"</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+koud+"</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+jingz+"</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+cheph+"</td>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n"
							+"</tr>\n"
							+"<tr height=55>\n"
							+"<td width=88 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">过磅员<br>签字</td>\n"
							+"<td width=92 valign=center  align='left' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n"
							+"<td width=86 valign=center  align='middle' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">矿点</td>\n"
							+"<td width=86 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">"+kuangb+"</td>\n"
							+"<td width=352 colspan=4 valign=middle  align='center' style=\" border-left:1px solid rgb(0,0,0); border-top:1px solid rgb(0,0,0); border-right:1px solid rgb(0,0,0); border-bottom:1px solid rgb(0,0,0);\">&nbsp;</td>\n"
							+"</tr>\n"
							+"</table>";
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _Report;}
	public String getPrint () {
		//普通打印
		String _Report = "";
		JDBCcon con = new JDBCcon();
		String biaot = "";
		String danw = "";
		String fenl = "0";
		try {
			StringBuffer sql = new StringBuffer();
			sql
					.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单标题' and shifsy=1");
			ResultSet rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					biaot = rs.getString("zhi");
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			sql = new StringBuffer();
			sql
					.append("select x.zhi from xitxxb x where x.duixm = '汽车衡随车检斤单单位' and shifsy=1");
			rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					danw = rs.getString("zhi");
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				SysInfo si = new SysInfo();
				fenl = si.getBasicValue("打印检斤单分联", "0");
				setFenl(fenl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 下面的SQL排序用的是检皮时间而过滤用的是检毛时间,
			// 如果存在隔天过车的情况就会出现打印随车检斤单按钮不好用的问题。
			// 我也不是很理解 但是原来程序是这么写的 不知道有什么作用 所以就没有改动。
			sql = new StringBuffer();
			CustomDate cd = new CustomDate();
			
//			String strToday = cd.FormatDate(new Date());
			sql
					.append("select qichjjbtmp.id,\n"
							+ " qichjjbtmp.xuh,\n"
							+ " qichjjbtmp.jihkjb_id,\n"
							+ " cheph,\n"
							+ " shouhr,\n"
							+ " qichjjbtmp.meikdqb_id,\n"
							+ " meikdqb.meikdqmc,\n"
							+ " qichjjbtmp.meikxxb_id,\n"
							+ " meikxxb.meikdwmc,\n"
							+ " ranlpzb.pinz,\n"
							+ " fahl,\n"
							+ " maoz,\n"
							+ " nvl(qichjjbtmp.piz,0) as piz,\n"
							+ " maoz - piz as jingz,\n"
							+ " nvl(qichjjbtmp.kouz,0)+nvl(qichjjbtmp.koupz,0) as koud,\n"
							+ " yuns,\n"
							+ " kuid,\n"
							+ " to_char(jianpsj, 'yyyy-mm-dd hh24:mi:ss') as jianpsj,\n"
							+ " to_char(jianmsj, 'yyyy-mm-dd hh24:mi:ss') as jianmsj,\n"
							+ " chengydw,\n"
							+ " meigy,\n"
							+ " qichjjbtmp.jianjy,\n"
							+ " qichjjbtmp.beiz\n"
							+ " from qichjjbtmp, meikdqb, meikxxb,ranlpzb\n"
							+ " where meikdqb.id = qichjjbtmp.meikdqb_id\n"
							+ " and qichjjbtmp.meikxxb_id = meikxxb.id\n"
							+ " and qichjjbtmp.ranlpzb_id = ranlpzb.id\n"
//							+ " and to_char(qichjjbtmp.jianmsj, 'yyyy-mm-dd') =\n"	+ " to_char(sysdate, 'yyyy-mm-dd')\n"
							+ " AND QICHJJBTMP.JIANMSJ BETWEEN trunc(sysdate) and trunc(sysdate)+1"
							+ " and maoz <> 0\n"
							+ " and piz <> 0 and qichjjbtmp.zhuangt <>9 order by jianpsj desc");
			rs = con.getResultSet(sql);
			if (rs.next()) {
				// String jianpsj = rs.getString("JIANPSJ");
				String cheph = rs.getString("CHEPH");
				if (cheph == null) {
					cheph = "";
				}
				String kuangb = rs.getString("MEIKDWMC");
				if (kuangb == null) {
					kuangb = "";
				}
				String changydw = rs.getString("CHENGYDW");
				if (changydw == null) {
					changydw = "";
				}
				String Jianmsj = rs.getString("JIANMSJ");
				if (Jianmsj == null) {
					Jianmsj = "";
				}
				String Jianpsj = rs.getString("JIANPSJ");
				if (Jianpsj == null) {
					Jianpsj = "";
				}
				String pinz = rs.getString("pinz");
				if (pinz == null) {
					pinz = "";
				}
				String shouhr = rs.getString("shouhr");
				if (shouhr == null) {
					shouhr = "";
				}
				double maoz = rs.getDouble("MAOZ");
				double piz = rs.getDouble("PIZ");
				double jingz = rs.getDouble("JINGZ");
				double fahl = rs.getDouble("FAHL");
				double koud = rs.getDouble("KOUD");
				double yuns = rs.getDouble("YUNS");
				String beiz = rs.getString("BEIZ");
				if (beiz == null) {
					beiz = "";
				}
				String Jianjy = rs.getString("JIANJY");
				if (Jianjy == null) {
					Jianjy = "";
				}
				String Meigy = rs.getString("MEIGY");
				if (Meigy == null) {
					Meigy = "";
				}
				long Chex = rs.getLong("xuh");
				String chex = "";
				if (rs.getLong("xuh") < 10) {
					chex = "00" + rs.getLong("xuh");
				} else if (rs.getLong("xuh") >= 10 && rs.getLong("xuh") < 100) {
					chex = "0" + rs.getLong("xuh");
				} else {
					chex = String.valueOf(Chex);
				}
				if (fenl.equals("0")) {
					_Report = "<div align=center style=\"display:none\" id=\"di\"> \n"
							+ "<div align=center id=\"printjjd\"> \n"
							+ "<div align=center> <br><br>\n"
							+ "<table id=\"table1\" \n"
							+ "	style=\"WIDTH: 600px; BORDER-COLLAPSE: collapse; height:216px\" \n"
							+ "	cellpadding=\"0\" border=\"0\"> \n"
							+ "	<colgroup> \n"
							+ "		<col style=\"WIDTH: 54pt\" span=\"6\" width=\"72\"> \n"
							+ "	</colgroup> \n"
							+ "	<tr style=\"height:30px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-TOP-WIDTH: 1px; BORDER-LEFT-WIDTH: 1px; BORDER-RIGHT-WIDTH: 1px; width:600px; height:30px\" \n"
							+ "			bordercolor=\"#000000\" align=\"middle\" colspan=\"6\"> \n"
							+ "		<p align=\"center\"><b> <input \n"
							+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 18pt; font-family:楷体_GB2312; padding-left:4px; padding-right:4px; text-align:center; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"标题\" value=\""
							+ biaot
							+ "\" \n"
							+ "			id=\"jianjdTitle\" size=\"36\" /></b> \n"
							+ "		</td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"HEIGHT: 1pt\"> \n"
							+ "		<td style=\"WIDTH: 100px; HEIGHT: 1pt\" align=middle></td> \n"
							+ "		<td \n"
							+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
							+ "			borderColor=#000000 align=middle></td> \n"
							+ "		<td \n"
							+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
							+ "			borderColor=#000000 align=middle></td> \n"
							+ "		<td \n"
							+ "			style=\"PADDING-RIGHT: 4px; BORDER-TOP: 1px solid; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px\" \n"
							+ "			borderColor=#000000 align=middle></td> \n"
							+ "		<td \n"
							+ "			style=\"PADDING-RIGHT: 4px; PADDING-LEFT: 4px; PADDING-BOTTOM: 1px; WIDTH: 100px; PADDING-TOP: 1px; HEIGHT: 1px; border-top-style:solid; border-top-width:1px\" \n"
							+ "			borderColor=#000000 align=middle></td> \n"
							+ "		<td style=\"WIDTH: 100px; HEIGHT: 1pt\" align=middle></td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"height:30px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 0pt solid #000000; BORDER-BOTTOM: 1.5pt solid #000000; style: ; width:300px; height:30px; border-left-width:medium; border-top-width:medium\" \n"
							+ "			align=\"middle\" colspan=\"3\"> \n"
							+ "		<p align=\"left\" style=\"FONT-SIZE: 11pt； FONT-FAMILY: 新宋体;\">时间：<input \n"
							+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"时间\" value=\""
							+ Jianpsj
							+ "\" id=\"Shij\" \n"
							+ "			size=\"22\" /> \n"
							+ "		</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:100px; height:30px\" \n"
							+ "			align=\"middle\"></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:186px; height:30px\" \n"
							+ "			align=\"middle\" colspan=\"2\" style=\"FONT-SIZE: 11pt； FONT-FAMILY: 新宋体;\">序号：<input \n"
							+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 11pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"序号\" value=\""
							+ chex
							+ "\" id=\"Chex\" \n"
							+ "			size=\"8\" />车</td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"height:25px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\">矿别</font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:300px; height:25px\" \n"
							+ "			align=\"middle\" colspan=\"3\"> \n"
							+ "		<p align=\"center\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"矿别\" value=\""
							+ kuangb
							+ "\" id=\"Kuangb\" \n"
							+ "			size=\"30\" /> \n"
							+ "		</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">单位</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
							+ "			align=\"middle\" colspan=\"2\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  vertical-align:middle; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"单位\" value=\""
							+ danw
							+ "\" id=\"Danw\" \n"
							+ "			size=\"7\" /></td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"height:25px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\">承运单位</font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:300px\" \n"
							+ "			align=\"middle\" colspan=\"3\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"承运单位\" value=\""
							+ changydw
							+ "\" \n"
							+ "			id=\"Chengydw\" size=\"30\" /></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; FONT-FAMILY: 新宋体; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">车号</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none #000000; BORDER-LEFT: medium none #000000; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:100px; height:25px\" \n"
							+ "			align=\"middle\" colspan=\"2\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"车号\" value=\""
							+ cheph
							+ "\" id=\"Cheh\" \n"
							+ "			size=\"7\" /></td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"25px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\">毛重时间</font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: ; width:200px\" \n"
							+ "			align=\"middle\" colspan=\"2\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"毛重时间\" value=\""
							+ Jianmsj
							+ "\" \n"
							+ "			id=\"Jianmsj\" size=\"22\" /></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">皮重时间</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1.5pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000; style: ; width:186px; height:25px\" \n"
							+ "			align=\"middle\" colspan=\"2\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"皮重时间\" value=\""
							+ Jianpsj
							+ "\" \n"
							+ "			id=\"Jianpsj\" size=\"22\" /></td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"HEIGHT: 25px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\">毛重(吨)</font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: \" \n"
							+ "			align=\"middle\">皮重(吨)</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-TOP-WIDTH: medium; FONT-FAMILY: 新宋体; BORDER-RIGHT: 1pt solid #000000; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; BORDER-BOTTOM: 1pt solid #000000; style: ; height:25px\" \n"
							+ "			align=\"middle\">扣吨(吨)</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">途损(吨)</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">净重(吨)</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\">票重</td> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"height:25px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\"> \n"
							+ "		<p align=\"center\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ maoz
							+ "\" displayName=\"毛重\" id=\"Maoz\" size=\"7\" /> \n"
							+ "		</td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
							+ "			align=\"middle\"><font size=\"3\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ piz
							+ "\" displayName=\"皮重\" id=\"Piz\" size=\"7\" /></font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1pt solid #000000; PADDING-RIGHT: 1px; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1pt solid #000000; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid #000000; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ koud
							+ "\" displayName=\"扣吨\" id=\"Koud\" size=\"7\" /></font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ yuns
							+ "\" displayName=\"运损\" id=\"Yuns\" size=\"7\" /></font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; style: ; height:25px\" \n"
							+ "			align=\"middle\"><font size=\"3\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ jingz
							+ "\" displayName=\"净重\" id=\"Jingz\" size=\"7\" /></font></td> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; WIDTH: 100px; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\" \n"
							+ "			align=\"middle\"><input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 value=\""
							+ fahl
							+ "\" displayName=\"票重\" id=\"Biaoz\" size=\"7\" /></td> \n"
							+ "	</tr> \n"
							+ "	<tr> \n"
							+ "		<TD \n"
							+ "			style=\"BORDER-RIGHT: windowtext 1pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: windowtext 1.5pt solid; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none\" \n"
							+ "			align=middle><FONT size=3>备注</FONT></TD> \n"
							+ "		<TD align=middle colSpan=5 width=\"100\" \n"
							+ "			style=\"BORDER-RIGHT: windowtext 1.5pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none; WIDTH: 100px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \" \n"
							+ "			align=middle><FONT size=3></FONT> <input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"备注\" value=\""
							+ beiz
							+ "\" id=\"Beiz\" \n"
							+ "			size=\"26\" /></TD> \n"
							+ "	</tr> \n"
							+ "	<tr style=\"height:30px\"> \n"
							+ "		<td align=\"middle\" colspan=\"3\" style=\"width: 300px; height: 30px\"> \n"
							+ "		<p align=\"left\" valign=\"center\" \n"
							+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;\">检 斤 员：<input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"检斤员\" value=\""
							+ Jianjy
							+ "\" id=\"Jianjy\" \n"
							+ "			size=\"10\" /> \n"
							+ "		</td> \n"
							+ "		<td style=\"WIDTH: 100px; HEIGHT: 30px\" align=\"middle\"></td> \n"
							+ "		<td style=\"WIDTH: 200px; HEIGHT: 30px\" align=\"middle\" colSpan=\"2\"> \n"
							+ "		<p align=\"right\" valign=\"center\" \n"
							+ "			style=\"FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;\">煤 管 员：<input \n"
							+ "			style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"煤管员\" value=\""
							+ Meigy
							+ "\" id=\"Meigy\" \n"
							+ "			size=\"10\" /> \n"
							+ "		</td> \n"
							+ "	</tr> \n"
							+ "</table> \n"
							+ "</div> \n" + "</div> \n" + "</div>";
				} else {

					String shij = "";
					shij = DateUtil.Formatdate( "yyyy-MM-dd", new Date());
					_Report = "<div align=center style=\"display:none\" id=\"di\"> \n"
							+ "<div align=center id=\"printjjd\"> \n"
							+ "<div align=center> <br><br>\n"
							+ "<table id=\"table1\" \n"
							+ "	style=\"BORDER-COLLAPSE: collapse; height:216px\" \n"
							+ "	cellpadding=\"0\" border=\"0\"> \n"
							+ "	<colgroup> \n"
							+ "		<col style=\"WIDTH: 40pt\" span=\"6\" width=\"100\"> \n"
							+ "	</colgroup> \n"
							+ "	<tr style=\"height:20px\"> \n"
							+ "		<td \n"
							+ "			style=\"BORDER-TOP-WIDTH: 1px; BORDER-LEFT-WIDTH: 1px; BORDER-RIGHT-WIDTH: 1px; height:50px\" \n"
							+ "			bordercolor=\"#000000\" align=\"middle\" colspan=\"6\"> \n"
							+ "		<p align=\"center\"><b> <input \n"
							+ "			style=\"border-style:solid; border-width:0; FONT-SIZE: 18pt; font-family:楷体_GB2312; padding-left:4px; padding-right:4px; text-align:center; padding-top:1px; padding-bottom:1px\" \n"
							+ "			 displayName=\"标题\" value=\""
							+ biaot
							+ "\" \n"
							+ "			id=\"jianjdTitle\" size=\"36\" /></b> \n"
							+ "		</td> \n"
							+ "	</tr> \n"

							+ "<tr style=\"height:20px\"> \n"
							+ "     <td \n"
							+ "         style=\"BORDER-RIGHT: 0pt solid #000000; BORDER-BOTTOM: 1.5pt solid #000000; height:30px; border-left-width:medium; border-top-width:medium\" \n"
							+ "          align=\"middle\" colspan=\"3\"> \n"
							+ "        <p align=\"left\" style=\"font-size:10pt;\">打印时间：<input \n"
							+ "           style=\"border-style:solid; border-width:0; FONT-SIZE: 10pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "         displayName=\"时间\" value=\""
							+ shij
							+ "\" \n"
							+ "          id=\"Shij\" size=\"20\" /> \n"
							+ "        </td>\n"
							+ "      <td id=\"lian\" style=\"FONT-SIZE: 10pt; text-align:center;BORDER-RIGHT: 0pt solid #000000; BORDER-BOTTOM: 1.5pt solid #000000; height:30px; border-left-width:medium; border-top-width:medium\"> \n"
							+ "      </td> \n"
							+ "      <td style=\"BORDER-RIGHT: 0pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1.5pt solid windowtext; style: ; width:186px; height:30px\" \n"
							+ "        align=\"right\" colspan=\"2\" style=\"FONT-SIZE: 10pt;\">序号：<input \n"
							+ "           style=\"border-style:solid; border-width:0; FONT-SIZE: 10pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "           displayName=\"货物名称\" value=\""
							+ pinz
							+ "  \"         id=\"Pinz\" size=\"6\" /><input \n"
							+ "           style=\"border-style:solid; border-width:0; FONT-SIZE: 10pt; text-align:center;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "           displayName=\"序号\" value=\""
							+ chex
							+ " \"           id=\"Chex\" size=\"3\" />车</td>\n"
							+ "</tr> \n"
							+ "<tr style=\"height:60px\"> \n"
							+ "     <td \n"
							+ "	     style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext;width:80px;\n"
							+ "	      COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\" \n"
							+ "	      align=\"middle\"><font size=\"3\">车号</font></td> \n"
							+ "      <td \n"
							+ "	      style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; width:80px; height:25px\" \n"
							+ "	      align=\"middle\">\n"
							+ "        <p align=\"center\"><input \n"
							+ "	      style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "	       displayName=\"车号\" value=\""
							+ cheph
							+ " \"       id=\"Cheh\" size=\"10\" /></p></td> \n"
							+ "       <td \n"
							+ "         style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; WIDTH: 80px; BORDER-BOTTOM: 1pt solid windowtext;  height:25px\" \n"
							+ "          align=\"middle\">货物名称</td> \n"
							+ "       <td \n"
							+ "         style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; height:25px\" \n"
							+ "         align=\"middle\"><input \n"
							+ "         style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  vertical-align:middle; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\" \n"
							+ "         displayName=\"货物名称\" value=\""
							+ pinz
							+ " \"       id=\"Pinz\" size=\"10\" /></td> \n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px;\n"
							+ " BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt;\n"
							+ "  FONT-FAMILY: 新宋体; VERTICAL-ALIGN: middle; BORDER-LEFT: middle none; WIDTH: 80px; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\"\n"
							+ "align=\"middle\"><font size=\"3\">承运单位</font></td>\n"
							+ "<td style=\"BORDER-RIGHT: 1.5pt solid windowtext; BORDER-TOP: 1.5pt solid windowtext; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px;width:100px\"\n"
							+ "align=\"middle\" ><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"承运单位\" value=\""
							+ changydw
							+ "\" id=\"Chengydw\" size=\"16\" /></td>\n"
							+ "</tr>\n"
							+ "<tr style=\"height:25px\">\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: 1.5pt solid windowtext; COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; TEXT-DECORATION: none; height:25px\"\n"
							+ "align=\"middle\"><font size=\"3\">毛重(吨)</font></td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: 1pt solid windowtext; BORDER-BOTTOM: 1pt solid windowtext;  height:25px\"\n"
							+ "align=\"middle\">\n"
							+ "<p align=\"center\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ "value=\""
							+ maoz
							+ " \" displayName=\"毛重\" id=\"Maoz\" size=\"7\" />\n"
							+ "</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; FONT-FAMILY: 新宋体; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; height:25px\"\n"
							+ "align=\"middle\">发货单位</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; height:25px\"\n"
							+ "align=\"middle\" >\n"
							+ "<p align=\"center\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"矿别\" value=\""
							+ kuangb
							+ "\" id=\"Kuangb\"\n"
							+ "size=\"20\" /></p>\n"
							+ "</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\">检毛时间</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1.5pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000; height:25px\"\n"
							+ "align=\"middle\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"检毛时间\" value=\""
							+ Jianmsj
							+ "\"id=\"Jianmsj\" size=\"20\" /></td>\n"
							+ "</tr>\n"
							+ "<tr style=\"25px\">\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext; BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px;  \"\n"
							+ "align=\"middle\">皮重(吨)</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none;\n"
							+ "COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: 1pt solid windowtext; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none;  \"\n"
							+ "align=\"middle\"><font size=\"3\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ "value=\""
							+ piz
							+ " \"displayName=\"皮重\" id=\"Piz\" size=\"7\" /></font></td>\n"
							+ "\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; BORDER-BOTTOM: 1pt solid windowtext;  height:25px\"\n"
							+ "align=\"middle\">收货单位</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000;  height:25px\"\n"
							+ "align=\"middle\" ><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"收货单位\" value=\""
							+ shouhr
							+ "\"id=\"shouhr\" size=\"20\" /></td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: medium none; FONT-FAMILY: 新宋体; BORDER-BOTTOM: 1pt solid windowtext; height:25px\"\n"
							+ "align=\"middle\">检皮时间</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-TOP-WIDTH: medium; BORDER-RIGHT: 1.5pt solid windowtext; BORDER-LEFT-WIDTH: medium; BORDER-BOTTOM: 1pt solid #000000; height:25px\"\n"
							+ "align=\"middle\" ><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"检皮时间\" value=\""
							+ Jianpsj
							+ "\"id=\"Jianpsj\" size=\"20\" /></td>\n"
							+ "</tr>\n"
							+ "<tr style=\"HEIGHT: 25px\">\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\">净重(吨)</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; BORDER-TOP: medium none; BORDER-LEFT: 1pt solid windowtext; BORDER-BOTTOM: 1pt solid windowtext;  height:25px\"\n"
							+ "align=\"middle\">\n"
							+ "<p align=\"center\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt; FONT-FAMILY: 新宋体;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ "value=\""
							+ jingz
							+ " \" displayName=\"净重\" id=\"Jingz\" size=\"7\" />\n"
							+ "</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none;  BORDER-BOTTOM: 1pt solid windowtext; HEIGHT: 25px; style: \"\n"
							+ "align=\"middle\" >　</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-TOP-WIDTH: medium; FONT-FAMILY: 新宋体; BORDER-RIGHT: 1pt solid #000000; BORDER-LEFT: 1pt solid #000000;  BORDER-BOTTOM: 1pt solid #000000; style: ; height:25px\"\n"
							+ "align=\"middle\"></td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\"></td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1.5pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none;  BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\"></td>\n"
							+ "</tr>\n"
							+ "<tr style=\"height:25px\">\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: 1.5pt solid windowtext;  BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\">检毛人员</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none;  BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\" colspan =\"2\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"检斤员\" value=\""
							+ Jianjy
							+ "\" id=\"Jianjy\"\n"
							+ "size=\"10\" /></td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none;  BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\">检皮人员</td>\n"
							+ "<td\n"
							+ "style=\"BORDER-RIGHT: 1.5pt solid windowtext; FONT-FAMILY: 新宋体; BORDER-TOP: medium none; BORDER-LEFT: medium none; BORDER-BOTTOM: 1pt solid windowtext; style: ; height:25px\"\n"
							+ "align=\"middle\" colspan=\"2\"><input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 12pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ "displayName=\"检斤员\" value=\""
							+ ((Visit) getPage().getVisit()).getRenymc()// +Jianjy
							+ "\" id=\"Jianjy\"\n"
							+ "size=\"10\" /></td>\n"
							+ "</tr>\n"
							+ "<tr>\n"
							+ "<TD\n"
							+ "style=\"BORDER-RIGHT: windowtext 1pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: windowtext 1.5pt solid;  COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none\"\n"
							+ "align=middle><FONT size=3>备注</FONT></TD>\n"
							+ "<TD align=middle colspan=\"5\"\n"
							+ "style=\"BORDER-RIGHT: windowtext 1.5pt solid; PADDING-RIGHT: 1px; BORDER-TOP: medium none; PADDING-LEFT: 1px; FONT-WEIGHT: 400; FONT-SIZE: 11pt; VERTICAL-ALIGN: middle; BORDER-LEFT: medium none;  COLOR: windowtext; PADDING-TOP: 1px; BORDER-BOTTOM: windowtext 1.5pt solid; FONT-STYLE: normal; FONT-FAMILY: 新宋体; WHITE-SPACE: nowrap; HEIGHT: 25px; TEXT-DECORATION: none; style: \"\n"
							+ "align=middle><FONT size=3></FONT> <input\n"
							+ "style=\"border-style:solid; border-width:0; text-align:center; FONT-SIZE: 13pt;  padding-left:4px; padding-right:4px; padding-top:1px; padding-bottom:1px\"\n"
							+ " displayName=\"备注\" id=\"Beiz\" value = \""
							+ beiz
							+ "\" size=\"26\" /></TD>\n"
							+ "</tr>\n"
							+ "</table>\n"
							+ "</div>\n"
							+ "<br><br><br>\n"
							+ "</div>";

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _Report;
	}
	private String _fenl;

	public String getFenl() {
		return _fenl;
	}

	public void setFenl(String value) {
		_fenl = value;
	}

	private IDropDownBean _PiaoValue;

	public IDropDownBean getPIAO_Value() {
		return _PiaoValue;
	}

	public void setPIAO_Value(IDropDownBean value) {
		_PiaoValue = value;
	}

	private IPropertySelectionModel _PiaoModel;

	public IPropertySelectionModel getPIAO_Model() {
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(1, "是"));
		dropdownlist.add(new IDropDownBean(2, "否"));
		_PiaoModel = new IDropDownSelectionModel(dropdownlist);
		return _PiaoModel;
	}

	public void setPIAO_Model(IPropertySelectionModel value) {
		_PiaoModel = value;
	}

	private boolean _isDisabled = false;

	public boolean isDisabled() {
		return _isDisabled;
	}

	public void setDisabled(boolean b) {
		_isDisabled = b;
	}
}