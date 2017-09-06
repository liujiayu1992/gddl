package com.zhiren.jt.zdt.jihgl.meitdhxqzbreport;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;

/*
 * 作者：陈泽天
 * 时间：2010-01-28 15：20
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
public class Meitdhxqzbreport extends BasePage implements PageValidateListener {

	// 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团
	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
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

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		isBegin = true;
		getSelectData();
	}
	
	// ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			getNianfModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setString3(null);
			visit.setString4(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
			isBegin = true;
			this.getSelectData();
		}
		if (nianfchanged) {
			nianfchanged = false;
			Refurbish();
		}

		if (_fengschange) {

			_fengschange = false;
			Refurbish();
		}
		getToolBars();
		Refurbish();
	}

	// 得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc() {
		String biaotmc = "";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc = "select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs = cn.getResultSet(sql_biaotmc);
		try {
			while (rs.next()) {
				biaotmc = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return biaotmc;

	}

	private String RT_HET = "yunsjhcx";

	private String mstrReportName = "yunsjhcx";

	public String getPrintTable() {
		if (!isBegin) {
			return "";
		}
		isBegin = false;
		if (mstrReportName.equals(RT_HET)) {
			if (getMingxlxValue() != null) {
				String jizzt = "";
				String zhuangt = "";// 机组状态
				if (getMingxlxValue().getValue().equals("全部")) {
					jizzt = "";
					zhuangt = "";
					return getMeitdhxqzb(jizzt, zhuangt);
				} else if (getMingxlxValue().getValue().equals("现役")) {
					jizzt = " and yx.jizzt = 0 ";
					zhuangt = "0";
					return getMeitdhxqzb(jizzt, zhuangt);
				} else if (getMingxlxValue().getValue().equals("新增")) {
					jizzt = " and yx.jizzt = 1 ";
					zhuangt = "1";
					return getMeitdhxqzb(jizzt, zhuangt);
				} else if (getMingxlxValue().getValue().equals("新增机组电煤需求")) {
					return getXinzjz();
				} else if (getMingxlxValue().getValue().equals("年需求煤量总表")) {
					return getSelectData();
				}
			}
			return "";
		} else {
			return "无此报表";
		}
	}

	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt = 0;

	public void setZhuangt(int _value) {
		intZhuangt = 1;
	}

	private boolean isBegin = false;

	private String getMeitdhxqzb(String jizzt, String zhuangt) {
		Visit visit = (Visit) getPage().getVisit();
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		String strGongsID = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strGongsID = " ";
		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid=" + this.getTreeid() + ")";

		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strGongsID = " and dc.id= " + this.getTreeid();

		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		visit.setString4(zhuangt);

		// 报表表头定义
		Report rt = new Report();

		String titlename = "需求煤量总表";
		// 报表内容
		visit.setString3("" + intyear);// 年份
		// 数据
		String shuj = "sum(yx.fadl) as fadl,\n"
				+ "sum(gongdbzmh) as gongdbzmh,\n"
				+ "round_new(decode(sum(decode(yx.zonghcydl,0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(yx.zonghcydl,0,0,yx.fadl))),2) as zonghcydl,\n"
				+ "round_new(decode(sum(decode(yx.fadbhm,0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(yx.fadbhm,0,0,yx.fadl))),2) as fadbhm,\n"
				+ "sum(yx.gongrl) as gongrl,\n"
				+ "round_new(decode(sum(decode(yx.gongrbzmh,0,0,yx.gongrl)),0,0,sum(yx.gongrbzmh*yx.gongrl)/sum(decode(yx.gongrbzmh,0,0,yx.gongrl))),2) as gongrbzmh,\n"
				+ "round_new(sum(yx.fadxybml)/10000,2) as fadxybml,round_new(sum(yx.gongrxybml)/10000,2) as gongrxybml,\n"
				+ "round_new(sum(yx.xuybml)/10000,2) as xuybml,sum(yx.dianhzryl) as dianhzryl,\n"
				+ "round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))),2) as youfrl,\n"
				+ "round_new(decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl)))*1000/4.1816,2) as youfrl_dk,\n"
				+ "round_new((sum(zongxql)-decode(sum(decode(yx.youfrl,0,0,yx.dianhzryl)),0,0,sum(yx.youfrl*yx.dianhzryl)/sum(decode(yx.youfrl,0,0,yx.dianhzryl))))/10000,2) as xuymzbml,\n"
				+ "round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml))),2) as rulrz,\n"
				+ "round_new(decode(sum(decode(yx.rulrz,0,0,yx.xuyyml)),0,0,sum(yx.rulrz*(yx.xuyyml))/sum(decode(yx.rulrz,0,0,yx.xuyyml)))*1000/4.1816,2) as rulrz_dk,\n"
				+ "round_new(sum(xuyyml)/10000,2) as xuyyml,\n"
				+ "round_new(sum(yx.qity)/10000,2) as qity,\n"
				+ "round_new(sum(yx.yuns)/10000,2) as yuns,\n"
				+ "round_new(sum(yx.qickc)/10000,2) as qickc,\n"
				+ "round_new(sum(yx.qimkc)/10000,2) as qimkc,\n"
				+ "round_new(sum(yx.zongxql)/10000,2) as zongxql";

		String group = "";
		if (jib == 1) {// 集团
		// if (jizzt.equals("")) {
		// group = "select
		// decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)as
		// danwmc,\n";
		// } else {
			group = "select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
					+ "decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,";
			// }
			strSQL = group
					+ shuj
					+ " from nianxqjhh yx,"
					+ "vwdianc dc\n"
					+ " where yx.diancxxb_id=dc.id\n"
					+ strGongsID
					+ " and shujzt="
					+ intyear
					+ " and yx.nianf= "
					+ intyear
					+ jizzt
					+ " group by rollup (dc.fgsmc,dc.mingc)\n"
					+ " order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
		} else if (jib == 2) {// 分公司或集团
			String ranlgs = "select id from diancxxb where shangjgsid= "
					+ this.getTreeid();
			try {
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if (rl.getRows() != 0) {// 燃料公司
				// if (jizzt.equals("")) {
				// group = "select
				// decode(grouping(dc.rlgsmc)+grouping(dc.mingc),2,'总计',1,dc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)
				// as danwmc,\n";
				// } else {
					group = "select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
							+ "decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',"
							+ "2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,\n";
					// }

					strSQL = group
							+ shuj
							+ " from nianxqjhh yx,"
							+ "vwdianc dc\n"
							+ "where yx.diancxxb_id=dc.id\n"
							+ strGongsID
							+ " and shujzt="
							+ intyear
							+ " and yx.nianf= "
							+ intyear
							+ jizzt
							+ "group by rollup (dc.rlgsmc,dc.fgsmc,dc.mingc)\n"
							+ "having not grouping(dc.rlgsmc)=1"
							+ "order by grouping(dc.rlgsmc) desc,dc.rlgsmc,grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
				} else {
					// if (jizzt.equals("")) {
					// group = "select
					// decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)
					// as danwmc,\n";
					// } else {
					group = " select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
							+ " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',"
							+ " 1,dc.fgsmc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n";
					// }
					strSQL = group
							+ shuj
							+ " from nianxqjhh yx,"
							+ "vwdianc dc\n"
							+ "where yx.diancxxb_id=dc.id\n"
							+ strGongsID
							+ " and shujzt="
							+ intyear
							+ " and yx.nianf= "
							+ intyear
							+ jizzt
							+ "group by rollup (dc.fgsmc,dc.mingc)\n"
							+ "having not grouping(dc.fgsmc)=1"
							+ "order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
				}
				rl.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cn.Close();
			}

		} else if (jib == 3) {
			// if (jizzt.equals("")) {
			// group = "select decode(grouping(dc.mingc),1,'总计',dc.mingc) as
			// danwmc,\n";
			// } else {
			group = " select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
					+ "decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danw,\n";
			// }

			strSQL = group + shuj + " from nianxqjhh yx," + " vwdianc dc\n"
					+ " where yx.diancxxb_id=dc.id\n" + strGongsID
					+ " and shujzt=" + intyear + " and yx.nianf= " + intyear
					+ jizzt + " group by rollup (dc.mingc)\n"
					+ " having not grouping(dc.mingc)=1"
					+ " order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
		}

		String ArrHeader[][] = new String[2][22];
		ArrHeader[0] = new String[] { "单位名称", "发电量<br>(万千瓦时)",
				"供电标准煤耗<br>(克/千瓦时)", "综合厂用电率<br>(克/千瓦时)", "发电标准煤耗<br>(克/千瓦时)",
				"供热量<br>(万吉焦)", "供热标准煤耗<br>(千克/吉焦)", "发电需用<br>标煤量(万吨)",
				"供热需用<br>标煤量(万吨)", "需用标煤总<br>量(万吨)", "点火助燃<br>油量(吨)", "油发热量",
				"油发热量", "需用煤折<br>标煤量(万吨)", "入炉热值", "入炉热值", "需用原煤<br>量(万吨)",
				"其他用<br>(万吨)", "运损<br>(万吨)", "期初库存<br>(万吨)", "期未库存<br>(万吨)",
				"总需求量<br>(万吨)" };
		ArrHeader[1] = new String[] { "单位名称", "发电量<br>(万千瓦时)",
				"供电标准煤耗<br>(克/千瓦时)", "综合厂用电率<br>(克/千瓦时)", "发电标准煤耗<br>(克/千瓦时)",
				"供热量<br>(万吉焦)", "供热标准煤耗<br>(千克/吉焦)", "发电需用<br>标煤量(万吨)",
				"供热需用<br>标煤量(万吨)", "需用标煤总<br>量(万吨)", "点火助燃<br>油量(吨)", "Mj/kg",
				"kcal/kg", "需用煤折<br>标煤量(万吨)", "Mj/kg", "kcal/kg",
				"需用原煤<br>量(万吨)", "其他用<br>(万吨)", "运损<br>(万吨)", "期初库存<br>(万吨)",
				"期未库存<br>(万吨)", "总需求量<br>(万吨)" };

		int ArrWidth[] = new int[] { 120, 65, 80, 80, 80, 50, 80, 80, 80, 80,
				80, 60, 60, 50, 50, 50, 55, 55, 55, 55, 55, 55 };

		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		// rt.setBody(new Table(rs,3, 0, iFixedRows));
		Table tb = new Table(rs, 2, 0, 1);
		rt.setBody(tb);
		rt.body.ShowZero = false;
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(getBiaotmc() + intyear + "年" + titlename, ArrWidth);
		rt
				.setDefaultTitle(1, 2, "制表单位:" + visit.getDiancqc(),
						Table.ALIGN_LEFT);
		// rt.setDefaultTitle(10, 2, intyear+ "年", Table.ALIGN_CENTER);
		// rt.setDefaultTitle(17, 6, "单位：万千瓦时、克/千瓦时、％、千卡/千克、万吨",
		// Table.ALIGN_RIGHT);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		// if(rt.body.getRows()>3){
		// rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
		// }
		// 页脚

		rt.createDefautlFooter(ArrWidth);

		// rt.setDefautlFooter(1, 3,
		// "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new
		// Date()))),Table.ALIGN_LEFT);
		// rt.setDefautlFooter(7,3,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2,
				Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strSQL = "";
		_CurrentPage = 1;
		_AllPages = 1;

		JDBCcon cn = new JDBCcon();

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		String strGongsID = "";
		int jib = this.getDiancTreeJib();
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strGongsID = " ";
		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid=" + this.getTreeid() + ")";

		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strGongsID = " and dc.id= " + this.getTreeid();

		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		// 数据状态
		String zhuangt = "";
		// if (visit.isShifsh() == true) {
		// if (visit.getRenyjb() == 3) {
		// zhuangt = "";
		// } else if (visit.getRenyjb() == 2) {
		// zhuangt = " and (yx.zhuangt=1 or yx.zhuangt=2)";
		// } else if (visit.getRenyjb() == 1) {
		// zhuangt = " and yx.zhuangt=2";
		// }
		// }
		// 机组类型
		String jizlx = "";

		visit.setString4(zhuangt);

		// 报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename = "需求煤量总表";
		// int iFixedRows = 0;// 固定行号
		// int iCol = 0;// 列数
		// 报表内容

		visit.setString3("" + intyear);// 年份

		String shuj = "sum(yx.fadl) as fadl,\n"
				+ "       round_new(decode(sum(decode(nvl(yx.fadbhm,0),0,0,yx.fadl)),0,0,sum(yx.fadbhm*yx.fadl)/sum(decode(nvl(yx.fadbhm,0),0,0,yx.fadl))),2) as fadbhm,\n"
				+ "\t\t\t round_new(decode(sum(decode(nvl(yx.zonghcydl,0),0,0,yx.fadl)),0,0,sum(yx.zonghcydl*yx.fadl)/sum(decode(nvl(yx.zonghcydl,0),0,0,yx.fadl))),2) as zonghcydl,\n"
				+ "\t\t\t round_new(decode(sum(decode(nvl(yx.rulrz,0),0,0,yx.zongxql)),0,0,sum(yx.rulrz*yx.zongxql)/sum(decode(nvl(yx.rulrz,0),0,0,yx.zongxql))),2) as rulrz,\n"
				+ "\t\t\t round_new(sum(yx.fadxyml)/10000,2) as fadxyml,round_new(sum(yx.zongxql)/10000,2) as zongxql\n";

		String group = "";
		if (jib == 1) {// 集团
			if (jizlx.equals("")) {
				group = "select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)as danwmc,\n";
			} else {
				group = "select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
						+ "decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,";
			}
			strSQL = group
					+ shuj
					+ "from nianxqjhh yx,"
					+
					// "(select n.diancxxb_id,sum(n.nianjhcgl) as zongxql\n" +
					// "from niancgjh n,jihkjb j\n" +
					// "where n.jihkjb_id=j.id and
					// to_char(n.riq,'yyyy')="+intyear+" and n.jizzt=0\n" +
					// "group by n.diancxxb_id) n," +
					"vwdianc dc\n"
					+ "where yx.diancxxb_id=dc.id\n"
					+ strGongsID
					+ " and shujzt="
					+ intyear
					+ " and yx.nianf= "
					+ intyear
					+ jizlx
					+ "group by rollup (dc.fgsmc,dc.mingc)\n"
					+ "order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
		} else if (jib == 2) {// 分公司或集团
			String ranlgs = "select id from diancxxb where shangjgsid= "
					+ this.getTreeid();

			try {
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if (rl.getRows() != 0) {// 燃料公司
					if (jizlx.equals("")) {
						group = "select decode(grouping(dc.rlgsmc)+grouping(dc.mingc),2,'总计',1,dc.rlgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,\n";
					} else {
						group = "select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								+ "decode(grouping(dc.rlgsmc)+grouping(dc.fgsmc)+grouping(dc.mingc),3,'总计',"
								+ "2,dc.rlgsmc,1,'&nbsp;&nbsp;'||dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc)) as danw,\n";
					}

					strSQL = group
							+ shuj
							+ " from nianxqjhh yx,"
							+
							// "(select n.diancxxb_id,sum(n.nianjhcgl) as
							// zongxql\n" +
							// "from niancgjh n,jihkjb j\n" +
							// "where n.jihkjb_id=j.id and
							// to_char(n.riq,'yyyy')="+intyear+" and
							// n.jizzt=0\n" +
							// "group by n.diancxxb_id) n," +
							"vwdianc dc\n"
							+ "where yx.diancxxb_id=dc.id\n"
							+ strGongsID
							+ " and shujzt="
							+ intyear
							+ " and yx.nianf= "
							+ intyear
							+ jizlx
							+ "group by rollup (dc.rlgsmc,dc.mingc)\n"
							+ "having not grouping(dc.rlgsmc)=1"
							+ "order by grouping(dc.rlgsmc) desc,dc.rlgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
				} else {
					if (jizlx.equals("")) {
						group = "select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',1,dc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danwmc,\n";
					} else {
						group = " select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
								+ " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'总计',"
								+ " 1,dc.fgsmc,'&nbsp;&nbsp;'||dc.mingc)) as danw,\n";
					}
					strSQL = group
							+ shuj
							+ " from nianxqjhh yx,"
							+
							// "(select n.diancxxb_id,sum(n.nianjhcgl) as
							// zongxql\n" +
							// "from niancgjh n,jihkjb j\n" +
							// "where n.jihkjb_id=j.id and
							// to_char(n.riq,'yyyy')="+intyear+" and
							// n.jizzt=0\n" +
							// "group by n.diancxxb_id) n," +
							"vwdianc dc\n"
							+ "where yx.diancxxb_id=dc.id\n"
							+ strGongsID
							+ " and shujzt="
							+ intyear
							+ " and yx.nianf= "
							+ intyear
							+ jizlx
							+ "group by rollup (dc.fgsmc,dc.mingc)\n"
							+ "having not grouping(dc.fgsmc)=1"
							+ "order by grouping(dc.fgsmc) desc,dc.fgsmc,grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
				}
				rl.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cn.Close();
			}

		} else if (jib == 3) {
			if (jizlx.equals("")) {
				group = "select decode(grouping(dc.mingc),1,'总计',dc.mingc) as danwmc,\n";
			} else {
				group = " select getLinkMingxTaiz('1',decode(grouping(dc.mingc),1,-1,max(dc.id)),"
						+ "decode(grouping(dc.mingc),1,'总计',dc.mingc)) as danw,\n";
			}

			strSQL = group
					+ shuj
					+ " from nianxqjhh yx,"
					+
					// "(select n.diancxxb_id,sum(n.nianjhcgl) as zongxql\n" +
					// "from niancgjh n,jihkjb j\n" +
					// "where n.jihkjb_id=j.id and
					// to_char(n.riq,'yyyy')="+intyear+" and n.jizzt=0\n" +
					// "group by n.diancxxb_id) n," +
					"vwdianc dc\n" + "where yx.diancxxb_id=dc.id\n"
					+ strGongsID + " and shujzt=" + intyear + " and yx.nianf= "
					+ intyear + jizlx + "group by rollup (dc.mingc)\n"
					+ "having not grouping(dc.mingc)=1"
					+ "order by grouping(dc.mingc) desc,max(dc.xuh),dc.mingc";
		}

		// 直属分厂汇总
		ArrHeader = new String[1][7];
		ArrHeader[0] = new String[] { "单位名称", intyear + "年计划发电量", "预计供电煤耗",
				"预计厂用电率", "预计入炉热值", "发电供热用原煤量", intyear + "年总需求量" };

		ArrWidth = new int[] { 150, 100, 100, 100, 100, 100, 100 };

		// iFixedRows = 1;
		// iCol = 10;

		ResultSet rs = cn.getResultSet(strSQL);

		// 数据
		// rt.setBody(new Table(rs,3, 0, iFixedRows));
		Table tb = new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		rt.body.ShowZero = false;
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(getBiaotmc() + intyear + "年份" + titlename, ArrWidth);
		rt
				.setDefaultTitle(1, 2, "制表单位:" + visit.getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "单位：万千瓦时、克/千瓦时、％、千卡/千克、万吨", Table.ALIGN_RIGHT);

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_COLROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		// if(rt.body.getRows()>3){
		// rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
		// }
		// 页脚

		rt.createDefautlFooter(ArrWidth);

		// rt.setDefautlFooter(1, 3,
		// "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new
		// Date()))),Table.ALIGN_LEFT);
		// rt.setDefautlFooter(7,3,"审核:",Table.ALIGN_LEFT);
		// rt.setDefautlFooter(11,2,"制表:",Table.ALIGN_LEFT);
		rt.setDefautlFooter(rt.footer.getCols() - 1, 2,
				Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);
		// 设置页数

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 新增机组电煤需求表
	private String getXinzjz() {
		_CurrentPage = 1;
		_AllPages = 1;
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		// 年，月
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}

		int jib = this.getDiancTreeJib();

		StringBuffer strSQL = new StringBuffer();

		if (jib == 1) {// 选集团时刷新出所有的电厂
			strSQL
					.append("select decode(grouping(vdc.fgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.fgsmc,vdc.mingc) as danwmc,\n");
			strSQL.append(" sf.quanc,\n");
			strSQL
					.append(" decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
			strSQL
					.append(" n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
			strSQL.append(" from nianxqjhh n,\n");
			strSQL
					.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
			strSQL
					.append(" Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
			strSQL
					.append(" from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
			strSQL.append(" and to_char(y.riq,'yyyy')=").append(intyear);
			strSQL
					.append(" group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
			strSQL
					.append(" where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
			strSQL
					.append(" and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
			strSQL.append(" and n.nianf=").append(intyear);
			strSQL
					.append(" group by rollup (vdc.fgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
			strSQL
					.append(" having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
			strSQL
					.append(" order by grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
			strSQL.append(" grouping(sf.quanc) desc,sf.quanc \n");

		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂

			String ranlgs = "select id from diancxxb where shangjgsid= "
					+ this.getTreeid();

			try {
				ResultSetList rl = cn.getResultSetList(ranlgs);
				if (rl.getRows() != 0) {// 燃料公司
					strSQL
							.append("select decode(grouping(vdc.rlgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.rlgsmc,vdc.mingc) as danwmc,\n");
					strSQL.append(" sf.quanc,\n");
					strSQL
							.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
					strSQL
							.append(" n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
					strSQL.append(" from nianxqjhh n,\n");
					strSQL
							.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
					strSQL
							.append(" Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
					strSQL
							.append(" from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
					strSQL.append(" and to_char(y.riq,'yyyy')=")
							.append(intyear);
					strSQL
							.append(" group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
					strSQL
							.append(" where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
					strSQL
							.append(" and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
					strSQL.append(" and n.nianf=").append(intyear);
					strSQL.append(
							" and (dc.fuid=  " + this.getTreeid()
									+ " or dc.shangjgsid= " + this.getTreeid()
									+ ") ").append("\n");
					strSQL
							.append("group by rollup (vdc.rlgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
					strSQL
							.append("having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
					strSQL
							.append("order by grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
					strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
				} else {
					strSQL
							.append("select decode(grouping(vdc.fgsmc)+grouping(vdc.mingc),2,'总计',1,vdc.fgsmc,vdc.mingc) as danwmc,\n");
					strSQL.append(" sf.quanc,\n");
					strSQL
							.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
					strSQL
							.append(" n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
					strSQL.append(" from nianxqjhh n,\n");
					strSQL
							.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
					strSQL
							.append(" Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
					strSQL
							.append(" from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
					strSQL.append(" and to_char(y.riq,'yyyy')=")
							.append(intyear);
					strSQL
							.append(" group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
					strSQL
							.append("where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
					strSQL
							.append("and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
					strSQL.append("and n.nianf=").append(intyear);
					strSQL.append(
							" and (dc.fuid=  " + this.getTreeid()
									+ " or dc.shangjgsid= " + this.getTreeid()
									+ ") ").append("\n");
					strSQL
							.append("group by rollup (vdc.fgsmc,vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
					strSQL
							.append("having not (grouping(vdc.mingc) || grouping(n.beiz)) =1 \n");
					strSQL
							.append("order by grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
					strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
				}
				rl.close();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				cn.Close();
			}

		} else {// 选择电厂
			strSQL
					.append("select decode(grouping(vdc.mingc),1,'总计',vdc.mingc) as danwmc,\n");
			strSQL.append("sf.quanc,\n");
			strSQL
					.append("decode(grouping(n.miaos),1,to_char(sum(n.zhuangjrl)),n.miaos) as zhuangjrl,\n");
			strSQL
					.append("n.toucrq,sum(n.fadxybml) as jihdl,n.shejmz,xq.gmsl,round_new(sum(n.xuyyml)/10000,2) as xuqml,xq.daozg,xq.yunsfs,n.beiz \n");
			strSQL.append(" from nianxqjhh n,\n");
			strSQL
					.append("(select y.riq,y.diancxxb_id,Getgmsl(y.diancxxb_id,y.riq) as gmsl,\n");
			strSQL
					.append("Getyunsfs(y.diancxxb_id,y.riq) as yunsfs,Getdaozg(y.diancxxb_id,y.riq) as daozg\n");
			strSQL
					.append(" from niancgjh y, gongysb g,yunsfsb ys  where y.gongysb_id = g.id(+) and y.yunsfsb_id = ys.id(+) and y.jizzt=1\n");
			strSQL.append(" and to_char(y.riq,'yyyy')=").append(intyear);
			strSQL
					.append(" group by y.diancxxb_id,y.riq) xq,diancxxb dc,shengfb sf,vwdianc vdc \n");
			strSQL
					.append(" where n.diancxxb_id=xq.diancxxb_id(+) and n.nianf=to_char(xq.riq(+),'yyyy')\n");
			strSQL
					.append(" and n.diancxxb_id=dc.id and dc.shengfb_id=sf.id and dc.id=vdc.id \n");
			strSQL.append(" and n.nianf=").append(intyear);
			strSQL.append("  and dc.id=").append(this.getTreeid()).append("\n");
			strSQL
					.append("group by rollup (vdc.mingc,sf.quanc,n.miaos,n.toucrq,n.shejmz,xq.gmsl,xq.daozg,xq.yunsfs,n.beiz) \n");
			strSQL
					.append("having not (grouping(sf.quanc) || grouping(n.beiz)) =1 \n");
			strSQL
					.append("order by grouping(vdc.mingc) desc,max(vdc.xuh),vdc.mingc,\n");
			strSQL.append("grouping(sf.quanc) desc,sf.quanc \n");
		}

		String ArrHeader[][] = new String[1][11];
		ArrHeader[0] = new String[] { "电厂名称", "所属省份", "装机容量<br>（万千瓦）", "投产日期",
				"计划电量<br>（万千瓦时）", "设计煤种", "主要需求供煤单位及数量<br>(万吨)",
				"需求煤量<br>（万吨）", "到站/中转港", "运输方式", "备注" };

		int ArrWidth[] = new int[] { 150, 70, 65, 120, 80, 58, 150, 58, 58, 58,
				60 };

		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据

		Table tb = new Table(rs, 1, 0, 2);
		rt.setBody(tb);

		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.setTitle(getBiaotmc() + intyear + "年新增机组电煤需求表", ArrWidth);
		rt.body.setRowHeight(1, 40);
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(rt.PAPER_ROWS);
		rt.body.setHeaderData(ArrHeader);// 表头数据
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(7, Table.ALIGN_LEFT);
		rt.body.setColAlign(9, Table.ALIGN_LEFT);
		rt.body.setColAlign(10, Table.ALIGN_LEFT);
		rt.body.mergeFixedRow();

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
		//		
	}

	// 得到登陆人员所属电厂或分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {

		String sql = "";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}

	// 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}

	// 年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			int _nianf = DateUtil.getYear(new Date());
			int _yuef = DateUtil.getMonth(new Date());
			if (_yuef == 1) {
				_nianf = _nianf - 1;
			}
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged = false;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	// ***************************报表初始设置***************************//
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}

	public Date getYesterday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	public Date getMonthFirstday(Date dat) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}

	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}

	// 页面判定方法
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

	// 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql, "中国大唐集团"));
	}

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		// nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("机组类型:"));
		ComboBox cb3 = new ComboBox();
		cb3.setTransform("MingxlxDropDown");
		cb3.setListeners("select:function(){document.Form0.submit();}");
		cb3.setId("Mingxlx");
		cb3.setWidth(120);
		tb1.addField(cb3);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);

	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private String treeid;

	/*
	 * public String getTreeid() { if (treeid == null || "".equals(treeid)) {
	 * return "-1"; } return treeid; }
	 * 
	 * public void setTreeid(String treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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

	// 报表类型
	public boolean _Mingxlxchange = false;

	private IDropDownBean _MingxlxValue;

	public IDropDownBean getMingxlxValue() {
		if (_MingxlxValue == null) {
			_MingxlxValue = (IDropDownBean) getIMingxlxModels().getOption(0);
		}
		return _MingxlxValue;
	}

	public void setMingxlxValue(IDropDownBean Value) {
		long id = -2;
		if (_MingxlxValue != null) {
			id = _MingxlxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Mingxlxchange = true;
			} else {
				_Mingxlxchange = false;
			}
		}
		_MingxlxValue = Value;
	}

	private IPropertySelectionModel _IMingxlxModel;

	public void setIMingxlxModel(IPropertySelectionModel value) {
		_IMingxlxModel = value;
	}

	public IPropertySelectionModel getIMingxlxModel() {
		if (_IMingxlxModel == null) {
			getIMingxlxModels();
		}
		return _IMingxlxModel;
	}

	public IPropertySelectionModel getIMingxlxModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(0, "全部"));
			fahdwList.add(new IDropDownBean(1, "现役"));
			fahdwList.add(new IDropDownBean(2, "新增"));
			fahdwList.add(new IDropDownBean(3, "新增机组电煤需求"));
			fahdwList.add(new IDropDownBean(4, "年需求煤量总表"));
			_IMingxlxModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMingxlxModel;
	}

}