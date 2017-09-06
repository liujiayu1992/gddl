package com.zhiren.dc.caiygl;

/*修改时间：2009-12-21
 *修改人：王刚
 *修改内容：增加参数All中显示列 
 * 
 */
/*
 *修改时间： 2009-11-14
 *修改人：     ww
 *修改内容：
 *        在xitxxb表中添加参数是否显示汽车、火车、全部三种可能		
 */
/*
 * 作者:tzf
 * 时间:2009-10-16
 * 修改内容:根据参数ALL_fahb_daohrq得到的报表进行调整，增加品种字段予以显示
 */
/*
 * 作者：王磊
 * 时间：2009-09-22
 * 描述：增加通过发货表到货日期查询的转码单
 */
/*
 * 作者：王磊
 * 时间：2009-07-28
 * 描述：增加大字体的采样编码转化验编码单
 */
/*
 * 修改时间 2009-03-13 
 * 作者：张森涛
 * 修改内容：添加新的转码单煤矿单位+品种+全部编码Zhuanmd&lx=Zhuanmb_pinz
 */
/*
 * 修改时间 2009-04-27 
 * 作者：ly
 * 修改问题：当在进厂批次号修改中合并两笔发货后，采制化编码查询时，被合并的编码行仍然存在，但编码与合并的编码重复
 * 修改内容：加入distinct查询，重新排序
 */

/*
 * 修改时间 2009-05-20
 * 作者：王总兵
 * 修改问题：采样编码转制样编码页面,加入采样编码下拉框选择,方便电厂选择编号或者输入编码查询
 * 修改内容：getCaiymzzym();
 */

/*
 * 修改时间 2009-05-25
 * 作者：ww
 * 修改问题：加入“煤矿单位（运输单位）+ 品种 + 进厂批次号 +转采样编码（Zhongcsj为到货日期，只适合汽运煤）”编码单
 * 修改内容：getMeikyjpbm();
 */

/*
 * 修改时间 2009-05-26
 * 作者：ww
 * 修改问题：加入“煤矿单位（运输单位）+ 品种 + 进厂批次号 +转采样编码（Zhongcsj为到货日期，只适合汽运煤）”编码单
 * 修改内容：getMeikyjpbm();
 */

/*
 * 修改时间 2009-05-26
 * 作者：李鹏
 * 修改问题：“采样编码转制样编码 ”、“制样编码转化验编码 ”与补录当天的编码混淆
 * 添加内容：getCaiyzzyzcsj();        getZhiyzhyzcsj();
 * 修改内容：getPrintTable();
 * 资源名：Zhuanmd&lx=Caiyzzyzcsj     Zhuanmd&lx=Zhiyzhayzcsj
 */

/*
 * 修改时间 2009-06-19
 * 作者：潘卫宁
 * 修改问题：一些转码单在一厂多制下无法正常查询编码的问题
 * 修改内容：getAll_Xiangxi()、getCaiybm()、getFCC_CZH()、
 * getZhuanmad_pinz()、getZhuanmad_daohrq()、getZhuanmad_Pinz_Daohrq()；
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-20 18：11
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */
/*
 * 修改时间 2010-05-20
 * 作者：车必达
 * 修改问题：增加采样分桶转码单汇总单。用于大同二电厂采样机自动分桶后编码查询
 * 修改内容：getZhuanmad_fent_huiz()
 */
/*
 * 修改时间 2010-07-07
 * 作者：玉沙沙
 * 修改问题：因鲁北电厂需求，增加“煤矿单位+品种+车次转采样编码”的编码查询
 * 修改内容：Zhuanmd_11 = "Zhuanmd_lb"  getMkPzCcBm()
 * 
 */
/*
 * 修改时间 2010-08-17
 * 作者：ww
 * 修改问题：增加带运输单位的转码单汇总单
 * 修改内容： Zhuanmd&lx=all_fah_daohrq_yunsdw
 */

/*
 * 修改时间 2012-08-31
 * 作者:赵胜男
 * 修改问题：转码单汇总查询界面中应只显示确认编码后的转码单信息
 * 修改内容：  入厂：AND zb.quersj IS NOT NULL，入炉： AND r.quersj IS NOT NULL，其他样：  AND zs.quersj IS NOT NULL
 */
/*
 * 时间：2013-05-27
 * 作者：夏峥
 * 描述：调整getZhuanmad_fah_daorhq()方法，
 * 		使用MainGlobal.getXitxx_item("采样", "质量需审核后才能看到采样转码单", visit.getDiancxxb_id()+"", "否")方法
 * 		控制是否需质量需审核后才能看到采样转码单
 */
/*
 * 作者：夏峥
 * 时间：2014-03-10
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		调整程序存在的BUG，清除无用注释。
 */
/*
 * 作者：夏峥
 * 时间：2014-04-1
 * 描述：调整庄河入厂入炉日期判断，调整后时间节点以早9点至次日早9点为一个统计日期。
 * 		即2日入炉煤量为1日早9点至2日早9点的入炉煤量
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Zhuanmd extends BasePage {
	private boolean bigFontSize = false;

	public boolean getRaw() {
		return true;
	}

	private String userName = "";

	public void setUserName(String value) {
		userName = ((Visit) getPage().getVisit()).getRenymc();
	}

	public String getUserName() {
		return userName;
	}

	// private boolean reportShowZero(){
	// return ((Visit) getPage().getVisit()).isReportShowZero();
	// }

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

	// ***************设置消息框******************//
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

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	private String ALL = "Zhuanmb";

	private String ALL_fahb_daohrq = "all_fah_daohrq";

	private String ALL_PINZ = "Zhuanmb_pinz";

	private String CAIY = "Caiybmzzybm";

	private String CAIY_hd = "Caiybmzzybm_hd";

	private String HUAY = "Zhiybmzhybm";

	private String YUNSWDW = "Zhuanmb_yunsdw";// 运输单位+编码转换

	private String ALL_daohrq = "Zhuanmb_daohrq";

	private String Zhuanmd_1 = "Zhuanmd_01";// 煤矿单位转采样编码

	private String Zhuanmd_2 = "Zhuanmd_13";// 采样编码转化验编码

	private String zhuanmd_ch_big = "Zhuanmd_ch_big";// 采样编码转化验编码

	private String Zhuanmd_3 = "Zhuanmd_03";// 煤矿单位转化验编码

	private String Zhuanmd_4 = "Zhuanmd_00";// 煤矿单位+运输单位转采样编码

	private String Zhuanmd_5 = "Zhuanmd_93";// 煤矿单位+运输单位转化验编码

	private String Zhuanmd_6 = "Zhuanmd_94";// 煤矿单位+车次转采样编码

	private String Zhuanmd_7 = "Zhuanmd_12";// 采样编码转制样编码

	private String Zhuanmd_8 = "Zhuanmd_23";// 制样编码转化验编码

	private String Zhuanmd_9 = "Zhuanmd_91";// 煤矿单位（运输单位）+品种转采样编码（Zhongcsj为到货日期，只适合汽运煤）

	private String Zhuanmd_JINCBM = "Zhuanmd_JINCBM"; // 汽车 煤矿单位(运输单位)+品种 +

	// 进厂批次号 +转采样编码 (重车时间为准)

	// 火车 煤矿单位(车次)+品种 + 进厂批次号 +转采样编码 (重车时间为准，防止采样日期与录入日期不一致编码混乱)

	private String ALL_PINZ_DAOHRQ = "Zhuanmb_Pinz_Zhongcsj";// 煤矿单位+品种的解码单（Zhongcsj为到货日期，只适合汽运煤）

	private String CaiZhiHua = "Zhuanmd_CZH";// 采制化编码单(无任何其他信息)

	private String All_Xiangxi = "All_Xiangxi";// 详细解码单（煤矿单位+品种+发货日期+发站+车次+车数）

	private String AllBianm = "All";// 解码单（供应商+煤矿单位+品种+发货日期+发站+车次+车数+进厂批号+采制化编码）

	private String Zhuanmd_0 = "FCC_CZH";// 带有发货日期+车次+车数的三级编码单

	private String Zhuanmd_10 = "GX_HS";// 广西合山编码单 供应商+煤矿+发站+发货日期+车数+采样编码

	private String zhuanmd_cz_zcsj = "Caiyzzyzcsj";// 采样码转制样码(以重车时间为准)

	private String zhuanmd_zh_zcsj = "Zhiyzhayzcsj";// 制样码转化验码(以重车时间为准)

	private String zhuanmd_ty_qc = "TaiyQiczm";// 太原汽车转码单

	private String zhuanmd_ty_zh = "ZhiyZhuay";// 太原汽车制样转化验

	private String zhuanmd_tong = "TH2CAITBH"; // 大同二电 采样桶号转采样编码

	private String zhuanmd_fent_huiz = "FENT_HUIZ";// 大同二电厂采样分桶转码汇总单
	
	private String Zhuanmd_11 = "Zhuanmd_lb";//煤矿单位+品种+车次+采样编码
	
	
	private String Zhuanmd_12 = "Zhuanmd_AllXiangXi_lb";//供应商+煤矿+品种+车次+采样编码+制样编码+化验编码
	
	private String ALL_fahb_daohrq_yunsdw = "all_fah_daohrq_yunsdw"; //供应商+煤矿+运输单位+品种+车次+采样编码+制样编码+化验编码

	/**
	 * panweining 2009-04-21 添加新的转码类型 注意：以下两个转码单的到货时间以录入时间为准； 1、只有采制化编码的转码单
	 * Zhuanmd&lx=CaiZhiHua；
	 * 2、详细的解码单：煤矿单位+品种+发货日期+发站+车次+车数（Zhuanmd&lx=All_Xiangxi）;
	 * 3、带有发货日期+车次+车数的三级编码单Zhuanmd&lx=FCC_CZH;
	 */

	/**
	 * panweining 2009-04-07 添加新的转码类型 注意：以下两个转码单的到货时间以重车时间为准；只适用于汽车运煤
	 * 1、煤矿单位(运输单位)+品种转采样编号Zhuanmd&lx=Zhuanmd_91
	 * 2、煤矿单位+品种的解码单Zhuanmd&lx=Zhuanmb_Pinz_Zhongcsj
	 */

	/**
	 * huochaoyuan 2009-03-04 添加新的转码类型 煤矿单位+车次转采样编号Zhuanmd&lx=Zhuanmd_94
	 * 采样编号转制样编号Zhuanmd&lx=Zhuanmd_12 制样编号转化验编号Zhuanmd&lx=Zhuanmd_23
	 */

	/**
	 * zhangsentao 2009-03-13 添加新的转码单 煤矿单位+品种+全部编码Zhuanmd&lx=Zhuanmb_pinz
	 */
	/**
	 * 2008-10-18 huochaoyuan 添加按过衡时间查询的砖码单，该时间为准确的煤进场时间，显示煤矿以及全部编号；
	 * 报表函数getZhuanmad_daohrq() 资源名：Zhuanmd&lx=Zhuanmb_daohrq
	 */

	/**
	 * 2008-11-04 huochaoyuan 添加转码单函数getZhuanmad_daohrq_1(int a,int
	 * b)，火车以chepb中的lursj查询,其他运输方式以chepb中的轻重车时间查询
	 * 目前参数Zhuamd_01(矿名转1级编号)，Zhuamd_13(1级转3级编号),Zhuanmd_03(矿名转3级编号),Zhuanmd_04(矿名+运输单位转1级编号)
	 */

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		if (mstrReportName.equals(CAIY)) {
			return getCaiymzzym();
		} else if (mstrReportName.equals(CAIY_hd)) {
			return getCaiymzzym_hd();
		} else if (mstrReportName.equals(HUAY)) {
			return getZhiybmzhybm();
		} else if (mstrReportName.equals(ALL)) {
			return getZhuanmad();
		} else if (mstrReportName.equals(ALL_fahb_daohrq)) {
			return getZhuanmad_fah_daorhq();
		} else if (mstrReportName.equals(YUNSWDW)) {
			return getZhuanmad_yunsdw();
		} else if (mstrReportName.equals(ALL_daohrq)) {
			return getZhuanmad_daohrq();
		} else if (mstrReportName.equals(ALL_PINZ_DAOHRQ)) {
			return getZhuanmad_Pinz_Daohrq();
		} else if (mstrReportName.equals(CaiZhiHua)) {
			return getCaiZhiHua();
		} else if (mstrReportName.equals(All_Xiangxi)) {
			return getAll_Xiangxi();
		} else if (mstrReportName.equals(AllBianm)) {
			return getAllBianm();
		} else if (mstrReportName.equals(Zhuanmd_0)) {
			return getFCC_CZH();
		} else if (mstrReportName.equals(Zhuanmd_2)) {
			return getZhuanmad_daohrq_1(1, 3);
		} else if (mstrReportName.equals(zhuanmd_ch_big)) {
			bigFontSize = true;
			return getZhuanmad_daohrq_1(1, 3);
		} else if (mstrReportName.equals(Zhuanmd_1)) {
			return getZhuanmad_daohrq_1(0, 1);
		} else if (mstrReportName.equals(Zhuanmd_3)) {
			return getZhuanmad_daohrq_1(0, 3);
		} else if (mstrReportName.equals(Zhuanmd_4)) {
			return getZhuanmad_daohrq_1(-1, 1);
		} else if (mstrReportName.equals(Zhuanmd_5)) {
			return getZhuanmad_daohrq_1(-1, 3);
		} else if (mstrReportName.equals(Zhuanmd_6)) {
			return getZhuanmad_daohrq_1(-2, 1);
		} else if (mstrReportName.equals(Zhuanmd_7)) {
			return getZhuanmad_daohrq_1(1, 2);
		} else if (mstrReportName.equals(Zhuanmd_8)) {
			return getZhuanmad_daohrq_1(2, 3);
		} else if (mstrReportName.equals(Zhuanmd_9)) {
			return getMeikzcybm();
		} else if (mstrReportName.equals(ALL_PINZ)) {
			return getZhuanmad_pinz();
		} else if (mstrReportName.equals(Zhuanmd_10)) {
			return getCaiybm();
		} else if (mstrReportName.equals(Zhuanmd_JINCBM)) {
			return getMeikyjpbm();
		} else if (mstrReportName.equals(zhuanmd_cz_zcsj)) {
			return getCaiyzzyzcsj();
		} else if (mstrReportName.equals(zhuanmd_zh_zcsj)) {
			return getZhiyzhyzcsj();
		} else if (mstrReportName.equals(zhuanmd_ty_qc)) {
			return get_zhuanmd_taiyuan();
		} else if (mstrReportName.equals(zhuanmd_ty_zh)) {
			return getZhiyzhy();
		} else if (mstrReportName.equals(zhuanmd_tong)) {
			return getTongh2Caiybh();
		} else if (mstrReportName.equals(zhuanmd_fent_huiz)) {
			return getZhuanmad_fent_huiz();
		} else if (mstrReportName.equals(Zhuanmd_11)) {
			return getMkPzCcBm();
		}else if (mstrReportName.equals(Zhuanmd_12)) {
			return getAllXiangXi_lb();
		}else if (mstrReportName.equals(ALL_fahb_daohrq_yunsdw)) {
			return getZhuanmad_fah_daorhq_yunsdw();
		}else {
			return "无此报表";
		}
	}

	// 全部编码
	private String getZhuanmad() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		String sq=" select * from xitxxb where mingc='转码单' and zhuangt=1 and beiz='使用'";
		String shijd=MainGlobal.getXitxx_item("数量", "入厂入炉节点时间", "0", "0");
		ResultSet rsq = cn.getResultSet(sq);
		boolean flag=false;
		try {
			while (rsq.next()){
				flag=true;
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		String a=" ";
		if(flag){
			a=" ,'入厂' as meizmc,'入厂' as meicmc ,'入厂' as meidmc";//,'入厂采样' as caiyjmc 
		}else{
			
			sbsql
			.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
					+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
					+ "       c.caiybm,\n"
					+ "       z.zhiybm,\n"
					+ "       h.huaybm "+a+"\n"
					+ "  from (select bianm as caiybm, zhillsb_id\n"
					+ "          from zhuanmb\n"
					+ "         where zhillsb_id in\n"
					+ "               (select zm.zhillsb_id as id\n"
					+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
					+ "                 where zm.zhuanmlb_id = lb.id\n"
					+ "                   and lb.jib = 1\n"
					+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
					+ "                   and f.zhilb_id = z.zhilb_id\n"
					+ "                   and z.id = zm.zhillsb_id)\n"
					+ "           and zhuanmlb_id =\n"
					+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
					+ "       (select bianm as zhiybm, zhillsb_id\n"
					+ "          from zhuanmb\n"
					+ "         where zhillsb_id in\n"
					+ "               (select zm.zhillsb_id as id\n"
					+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
					+ "                 where zm.zhuanmlb_id = lb.id\n"
					+ "                   and lb.jib = 3\n"
					+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
					+ "                   and f.zhilb_id = z.zhilb_id\n"
					+ "                   and z.id = zm.zhillsb_id)\n"
					+ "           and zhuanmlb_id =\n"
					+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
					+ "       (select bianm as huaybm, zhillsb_id\n"
					+ "          from zhuanmb\n"
					+ "         where zhillsb_id in\n"
					+ "               (select zm.zhillsb_id as id\n"
					+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
					+ "                 where zm.zhuanmlb_id = lb.id\n"
					+ "                   and lb.jib = 3\n"
					+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
					+ "                   and f.zhilb_id = z.zhilb_id\n"
					+ "                   and z.id = zm.zhillsb_id)\n"
					+ "           and zhuanmlb_id =\n"
					+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
					+ "       (select distinct f.id,\n"
					+ "                        f.diancxxb_id,\n"
					+ "                        f.gongysb_id,\n"
					+ "                        f.meikxxb_id,\n"
					+ "                        z.id as zid\n"
					+ "          from zhillsb z, fahb f, chepb c,caiyb cb\n"
					+ "         where f.zhilb_id = z.zhilb_id  and z.zhilb_id=cb.zhilb_id\n"
					+ "           and c.fahb_id = f.id\n"
					+ "           and to_date(to_char(cb.caiyrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
					+ "               to_date('"
					+ getRiqi()
					+ "', 'yyyy-mm-dd')) s\n"
					+ " where c.zhillsb_id = z.zhillsb_id\n"
					+ "   and h.zhillsb_id = c.zhillsb_id\n"
					+ "   and z.zhillsb_id = h.zhillsb_id\n"
					+ "   and c.zhillsb_id = s.zid\n"
					+ "   and h.zhillsb_id = s.zid\n"
					+ "   and z.zhillsb_id = s.zid\n"
					+ Jilcz.filterDcid(visit, "s")
					+ "\n"
					+ " order by gongys,meik,caiybm,zhiybm,huaybm\n"
					+""
					
					
			)
			;
			
		}
		if(flag){
			sbsql.append(
					"select distinct  g.mingc,m.mingc,c.bianm,z.bianm ,h.bianm "+a+"\n" +
					"  from zhillsb  zb,\n" + 
					"       fahb     f,\n" + 
					"       zhuanmb  zm,\n" + 
					"       zhuanmlb l,\n" + 
					"       gongysb  g,\n" + 
					"       meikxxb  m,\n" + 
					"       pinzb    p,\n" + 
					"       --caiyb cy,\n" + 
					"               (select s.id, m.bianm from zhillsb s, zhuanmb m ,zhuanmlb l\n" + 
					"       where s.id=m.zhillsb_id and m.zhuanmlb_id=l.id and l.jib =1\n" + 
					"       )c,\n" + 
					"               (select s.id, m.bianm from zhillsb s, zhuanmb m ,zhuanmlb l\n" + 
					"       where s.id=m.zhillsb_id and m.zhuanmlb_id=l.id and l.jib =2\n" + 
					"       )z,\n" + 
					"       (select s.id, m.bianm from zhillsb s, zhuanmb m ,zhuanmlb l\n" + 
					"       where s.id=m.zhillsb_id and m.zhuanmlb_id=l.id and l.jib =3\n" + 
					"       )h\n" + 
					"\n" + 
					" where  zb.zhilb_id = f.zhilb_id\n" + 
					" and zm.zhillsb_id=zb.id and zm.zhuanmlb_id=l.id\n" + 
					" and l.jib=3 and f.gongysb_id=g.id and f.pinzb_id=p.id\n" + 
					" and f.meikxxb_id=m.id\n" + 
					" and zb.id=h.id\n" + 
					" and zb.id=c.id\n" + 
					" and zb.id=z.id\n" + 
					" AND zb.quersj IS NOT NULL\n" + 
					" and zb.caiysj>= to_date('"+getRiqi()+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')-1"+
					" and zb.caiysj< to_date('"+getRiqi()+" "+shijd+":00:00','yyyy-mm-dd hh24:mi:ss')"
);
			sbsql.append(	""+" "+	"union\n"
						+
						"select '入炉' as gongys,'入炉' as meik, a.bianm,b.bianm,c.bianm,r.meizmc,r.meicmc,r.meidmc from\n" +//,r.caiyjbh 
						"(select a.zhillsb_id, bianm from zhuanmb a,zhuanmlb zb where a.zhillsb_id in(\n" + 
						"select z.zhillsb_id from zhuanmb z where bianm in (\n" + 
						"\n" + 
						"select distinct c.bianm from rulmzlzmxb r,caiyb c where r.zhuanmbzllsb_id=c.zhilb_id and r.rulrq<=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')\n" + 
						"and r.rulrq>=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')-1"+
						")\n" + 
						") and a.zhuanmlb_id=zb.id and zb.jib=1) a,\n" + 
						"(select a.zhillsb_id, bianm from zhuanmb a,zhuanmlb zb where a.zhillsb_id in(\n" + 
						"select z.zhillsb_id from zhuanmb z where bianm in (\n" + 
						"\n" + 
						"select distinct c.bianm from rulmzlzmxb r,caiyb c where r.zhuanmbzllsb_id=c.zhilb_id and r.rulrq<=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')\n" + 
						"and r.rulrq>=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')-1"+
//						"select c.bianm from rulmzlzmxb r,caiyb c where r.caiyb_id=c.id and to_char(r.rulrq,'yyyy-mm-dd hh24:mm:ss')='"+getRiqi()+"'\n" + 
						")\n" + 
						") and a.zhuanmlb_id=zb.id and zb.jib=2) b\n" + 
						",\n" + 
						"(select  a.zhillsb_id, bianm from zhuanmb a,zhuanmlb zb where a.zhillsb_id in(\n" + 
						"select z.zhillsb_id from zhuanmb z where bianm in (\n" + 
						"\n" + 
						"select distinct c.bianm from rulmzlzmxb r,caiyb c where r.zhuanmbzllsb_id=c.zhilb_id and r.rulrq<=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')\n" + 
						"and r.rulrq>=to_date('"+getRiqi()+"  "+shijd+":00:00"+"','yyyy-mm-dd hh24:mi:ss')-1"+
//						"select c.bianm from rulmzlzmxb r,caiyb c where r.caiyb_id=c.id and to_char(r.rulrq,'yyyy-mm-dd hh24:mm:ss')='"+getRiqi()+"'\n" + 
						")\n" + 
						") and a.zhuanmlb_id=zb.id and zb.jib=3) c , rulmzlzmxb r\n" + 
						"where a.zhillsb_id=b.zhillsb_id and a.zhillsb_id=c.zhillsb_id and  a.zhillsb_id=r.zhuanmbzllsb_id    AND r.quersj IS NOT NULL \n");
			sbsql.append( " union "+
						"select distinct  '其他样' as mingc,'其他样' as mingc,c.bianm as bianm,z.bianm as bianm ,h.bianm as\n" +
						"bianm  ,'其他样' as meizmc,'其他样' as meicmc ,'其他样' as meidmc\n" + 
						"from\n" + 
						"(select q.id,z.bianm from qitymxb q ,zhillsb zs,zhuanmb z,zhuanmlb l\n" + 
						" where q.caiysj>= to_date('"+getRiqi()+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.caiysj< to_date('"+getRiqi()+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.id=zs.id\n" + 
						" and zs.id=z.zhillsb_id\n" + 
						" and z.zhuanmlb_id=l.id   AND zs.quersj IS NOT NULL\n" + 
						" and l.jib=1\n" + 
						" )c,\n" + 
						" (select q.id,z.bianm from qitymxb q ,zhillsb zs,zhuanmb z,zhuanmlb l\n" + 
						" where q.caiysj>= to_date('"+getRiqi()+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.caiysj< to_date('"+getRiqi()+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.id=zs.id\n" + 
						" and zs.id=z.zhillsb_id\n" + 
						" and z.zhuanmlb_id=l.id   AND zs.quersj IS NOT NULL\n" + 
						" and l.jib=2\n" + 
						" )z,\n" + 
						" (select q.id,z.bianm from qitymxb q ,zhillsb zs,zhuanmb z,zhuanmlb l\n" + 
						" where q.caiysj>= to_date('"+getRiqi()+" 00:00:00','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.caiysj< to_date('"+getRiqi()+" 23:59:59','yyyy-mm-dd hh24:mi:ss')\n" + 
						" and q.id=zs.id\n" + 
						" and zs.id=z.zhillsb_id\n" + 
						" and z.zhuanmlb_id=l.id  AND zs.quersj IS NOT NULL\n" + 
						" and l.jib=3\n" + 
						" )h\n" + 
						" where c.id=z.id\n" + 
						" and c.id=h.id"
						);
		}

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][5];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "采样编码", "制样编码", "化验编码" };
		int ArrWidth[] = new int[] { 150, 100, 100, 100, 100, };
		if(flag){
			ArrHeader = new String[1][9];
			ArrHeader[0]=new String[] { "供应商", "煤矿", "采样编码", "制样编码", "化验编码","煤种","煤场","煤堆" };//,"采样机"
			ArrWidth = new int[] { 150, 90, 80, 80, 80,70,70,70};
		}

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		if(flag){
			
		}else{
			
			rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
			rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);
		}

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 渭河(运输单位的编码查询)
	private String getZhuanmad_yunsdw() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "                (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "                s.mingc,\n"
						+ "                c.caiybm,\n"
						+ "                z.zhiybm,\n"
						+ "\n"
						+ "                h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid,\n"
						+ "                        y.mingc\n"
						+ "          from zhillsb z, fahb f, chepb c, yunsdwb y\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.yunsfsb_id != 1\n"
						+ "           and c.yunsdwb_id = y.id\n"
						+ "           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or\n"
						+ "               to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "              to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ "        union\n"
						+ "        select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid,\n"
						+ "                        y.mingc\n"
						+ "          from zhillsb z, fahb f, chepb c, yunsdwb y\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.yunsfsb_id = 1\n"
						+ "           and c.yunsdwb_id = y.id\n"
						+ "           and (to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or\n"
						+ "               to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ " order by gongys, meik, c.caiybm, s.mingc");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][5];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "运输单位", "采样编码", "制样编码",
				"化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 100, 100, 100, 100, };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单<p>(" + getRiqi() + ")", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	
	private String getZhuanmad_fah_daorhq_yunsdw() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct g.mingc gys,\n"
						+ "       m.mingc mk,\n" +
						"(SELECT mingc FROM yunsdwb WHERE ID=\n" +
						"(SELECT MAX(c.yunsdwb_id) FROM chepb c WHERE c.fahb_id=s.id)\n" + 
						") yunsdw,\n"
						+ "       c.mingc fz,\n"
						+ "        pz.mingc pinz,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        f.faz_id,\n"
						+ "                         f.pinzb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.daohrq =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s,gongysb g, meikxxb m,pinzb pz, chezxxb c\n"
						+ " where c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ "   and s.gongysb_id = g.id\n"
						+ "   and s.meikxxb_id = m.id\n"
						+ "    and s.pinzb_id=pz.id\n"
						+ "   and s.faz_id = c.id\n"
						+ Jilcz.filterDcid(visit, "s") + "\n"
						+ " order by gys,mk,fz,caiybm,zhiybm,huaybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][8];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "运输单位", "车站", "品种", "采样编码", "制样编码",
				"化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 100, 80, 80, 80, 80, 80 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getZhuanmad_fah_daorhq() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		String condition="";
		if(MainGlobal.getXitxx_item("采样", "质量需审核后才能看到采样转码单", visit.getDiancxxb_id()+"", "否").equals("是")){
			condition=" and z.shenhzt=7 \n";
		}
		
		sbsql.append("select distinct g.mingc gys,\n"
						+ "       m.mingc mk,\n"
						+ "       c.mingc fz,\n"
						+ "        pz.mingc pinz,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+condition
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 2\n"
						+condition
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+condition
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        f.faz_id,\n"
						+ "                         f.pinzb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.daohrq =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s,gongysb g, meikxxb m,pinzb pz, chezxxb c\n"
						+ " where c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ "   and s.gongysb_id = g.id\n"
						+ "   and s.meikxxb_id = m.id\n"
						+ "    and s.pinzb_id=pz.id\n"
						+ "   and s.faz_id = c.id\n"
						+ Jilcz.filterDcid(visit, "s") + "\n"
						+ " order by gys,mk,fz,caiybm,zhiybm,huaybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][7];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "车站", "品种", "采样编码", "制样编码",
				"化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 100, 100, 100, 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 只有采制化编码的转码单
	private String getCaiZhiHua() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ " order by caiybm,zhiybm,huaybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][3];
		ArrHeader[0] = new String[] { "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 100, 100, 100, };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 比较详细的解码单（煤矿单位+品种+发货日期+发站+车次+车数）
	private String getAll_Xiangxi() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "        (select mingc from pinzb where id=s.pinzb_id) as pinz,\n"
						+ "        (select to_char(fahrq,'yyyy-mm-dd') from fahb where id = s.id) as fahrq,\n"
						+ "        (select mingc from chezxxb where id = (select faz_id from fahb where id = s.id)) as faz,\n"
						+ "        (select chec from fahb where id = s.id) as chec,\n"
						+ "        (select ches from fahb where id = s.id) as ches,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 2\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by gongys,meik,pinz,fahrq,faz,chec,ches,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "品种", "发货日期", "发站", "车次",
				"车数", "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 50, 100, 50, 50, 50, 100, 100,
				100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 解码单（供应商+煤矿单位+品种+发货日期+发站+车次+车数+进厂批号+采制化编码）,到货日期为查询日期。
	private String getAllBianm() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("SELECT s.gongys,\n"
						+ "         s.meik,\n"
						+ "         s.pinz,\n"
						+ "         s.fahrq,\n"
						+ "         s.faz,\n"
						+ "         s.chec,\n"
						+ "         s.ches,\n"
						+ "         s.jincph,\n"
						+ "         c.caiybm,\n"
						+ "         z.zhiybm,\n"
						+ "         h.huaybm,\n"
						+ "         getBianmzt (h.zhillsb_id) AS huayzt\n"
						+ "    FROM (SELECT bianm AS caiybm, zhillsb_id\n"
						+ "            FROM zhuanmb\n"
						+ "           WHERE zhillsb_id IN\n"
						+ "                       (SELECT zm.zhillsb_id AS id\n"
						+ "                          FROM zhuanmb zm,\n"
						+ "                               zhuanmlb lb,\n"
						+ "                               yangpdhb y,\n"
						+ "                               zhillsb z,\n"
						+ "                               fahb f\n"
						+ "                         WHERE     zm.zhuanmlb_id = lb.id\n"
						+ "                               AND lb.jib = 1\n"
						+ "                               AND y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                               AND f.zhilb_id = z.zhilb_id\n"
						+ "                               AND z.id = zm.zhillsb_id)\n"
						+ "                 AND zhuanmlb_id = (SELECT id\n"
						+ "                                      FROM zhuanmlb\n"
						+ "                                     WHERE mingc = '采样编码')) c,\n"
						+ "         (SELECT bianm AS zhiybm, zhillsb_id\n"
						+ "            FROM zhuanmb\n"
						+ "           WHERE zhillsb_id IN\n"
						+ "                       (SELECT zm.zhillsb_id AS id\n"
						+ "                          FROM zhuanmb zm,\n"
						+ "                               zhuanmlb lb,\n"
						+ "                               yangpdhb y,\n"
						+ "                               zhillsb z,\n"
						+ "                               fahb f\n"
						+ "                         WHERE     zm.zhuanmlb_id = lb.id\n"
						+ "                               AND lb.jib = 2\n"
						+ "                               AND y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                               AND f.zhilb_id = z.zhilb_id\n"
						+ "                               AND z.id = zm.zhillsb_id)\n"
						+ "                 AND zhuanmlb_id = (SELECT id\n"
						+ "                                      FROM zhuanmlb\n"
						+ "                                     WHERE mingc = '制样编码')) z,\n"
						+ "         (SELECT bianm AS huaybm, zhillsb_id\n"
						+ "            FROM zhuanmb\n"
						+ "           WHERE zhillsb_id IN\n"
						+ "                       (SELECT zm.zhillsb_id AS id\n"
						+ "                          FROM zhuanmb zm,\n"
						+ "                               zhuanmlb lb,\n"
						+ "                               yangpdhb y,\n"
						+ "                               zhillsb z,\n"
						+ "                               fahb f\n"
						+ "                         WHERE     zm.zhuanmlb_id = lb.id\n"
						+ "                               AND lb.jib = 3\n"
						+ "                               AND y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                               AND f.zhilb_id = z.zhilb_id\n"
						+ "                               AND z.id = zm.zhillsb_id)\n"
						+ "                 AND zhuanmlb_id = (SELECT id\n"
						+ "                                      FROM zhuanmlb\n"
						+ "                                     WHERE mingc = '化验编码')) h,\n"
						+ "         (  SELECT f.diancxxb_id AS diancxxb_id,\n"
						+ "                   zl.id AS zlid,\n"
						+ "                   MAX (g.mingc) AS gongys,\n"
						+ "                   MAX (m.mingc) AS meik,\n"
						+ "                   p.mingc AS pinz,\n"
						+ "                   cz.mingc AS faz,\n"
						+ "                   TO_CHAR (f.fahrq, 'yyyy-mm-dd') AS fahrq,\n"
						+ "                   f.chec AS chec,\n"
						+ "                   f.ches AS ches,\n"
						+ "                   cy.bianm AS jincph\n"
						+ "              FROM gongysb g,\n"
						+ "                   meikxxb m,\n"
						+ "                   fahb f,\n"
						+ "                   pinzb p,\n"
						+ "                   chezxxb cz,\n"
						+ "                   zhillsb zl,\n"
						+ "                   chepb c,\n"
						+ "                   caiyb cy\n"
						+ "             WHERE     f.zhilb_id = zl.zhilb_id\n"
						+ "                   AND f.zhilb_id = cy.zhilb_id\n"
						+ "                   AND zl.zhilb_id = cy.zhilb_id\n"
						+ "                   AND f.pinzb_id = p.id\n"
						+ "                   AND c.fahb_id = f.id\n"
						+ "                   AND cz.id = f.faz_id\n"
						+ "                   AND f.gongysb_id = g.id\n"
						+ "                   AND f.meikxxb_id = m.id\n"
						+ "                   AND TO_DATE (TO_CHAR (f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "                         TO_DATE ('" + getRiqi()
						+ "', 'yyyy-mm-dd')\n"
						+ "          GROUP BY (g.mingc,\n"
						+ "                    m.mingc,\n"
						+ "                    p.mingc,\n"
						+ "                    f.fahrq,\n"
						+ "                    cz.mingc,\n"
						+ "                    f.chec,\n"
						+ "                    f.ches,\n"
						+ "                    cy.bianm,\n"
						+ "                    f.diancxxb_id,\n"
						+ "                    zl.id)) s\n"
						+ "   WHERE     c.zhillsb_id = z.zhillsb_id\n"
						+ "         AND h.zhillsb_id = c.zhillsb_id\n"
						+ "         AND z.zhillsb_id = h.zhillsb_id\n"
						+ "         AND c.zhillsb_id = s.zlid\n"
						+ "         AND h.zhillsb_id = s.zlid\n"
						+ "         AND z.zhillsb_id = s.zlid\n"
						+ Jilcz.filterDcid(visit, "s") + " \n"
						+ "GROUP BY (s.gongys,\n" + "          s.meik,\n"
						+ "          s.pinz,\n" + "          s.fahrq,\n"
						+ "          s.faz,\n" + "          s.ches,\n"
						+ "          s.chec,\n" + "          s.jincph,\n"
						+ "          c.caiybm,\n" + "          z.zhiybm,\n"
						+ "          h.huaybm,\n" + "          h.zhillsb_id)\n"
						+ "ORDER BY s.gongys,\n" + "         s.meik,\n"
						+ "         s.pinz,\n" + "         s.faz,\n"
						+ "         s.fahrq,\n" + "         s.chec,\n"
						+ "         s.ches,\n" + "         s.jincph,\n"
						+ "         caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "品种", "发货日期", "发站", "车次",
				"车数", "进厂批号", "采样编码", "制样编码", "化验编码", "化验状态" };

		int ArrWidth[] = new int[] { 150, 100, 50, 80, 50, 50, 50, 80, 100,
				100, 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("解 码 单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 采样编码单（供应商+煤矿单位+发站+发货日期+车数+采样编码）
	private String getCaiybm() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "        (select mingc from chezxxb where id = (select faz_id from fahb where id = s.id)) as faz,\n"
						+ "        (select to_char(fahrq,'yyyy-mm-dd') from fahb where id = s.id) as fahrq,\n"
						+ "        (select ches from fahb where id = s.id) as ches,\n"
						+ "       c.caiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 2\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by gongys,meik,faz,fahrq,ches,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][10];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "发站", "发货日期", "车数", "采样编码" };

		int ArrWidth[] = new int[] { 150, 150, 50, 100, 50, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.setColAlign(3, Table.ALIGN_LEFT);
		rt.body.setColAlign(4, Table.ALIGN_LEFT);
		rt.body.setColAlign(5, Table.ALIGN_CENTER);
		rt.body.setColAlign(6, Table.ALIGN_CENTER);
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(1, 2, "审核:", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(3, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 带有发货日期+车次+车数的三级编码单
	private String getFCC_CZH() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct (select to_char(fahrq,'yyyy-mm-dd') from fahb where id = s.id) as fahrq,\n"
						+ "        (select chec from fahb where id = s.id) as chec,\n"
						+ "        (select ches from fahb where id = s.id) as ches,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by fahrq,chec,ches,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][6];
		ArrHeader[0] = new String[] { "发货日期", "车次", "车数", "采样编码", "制样编码",
				"化验编码" };

		int ArrWidth[] = new int[] { 100, 50, 50, 100, 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 汽车 煤矿单位(运输单位)+品种 + 进厂批次号 +转采样编码 (重车时间为准)
	// 火车 煤矿单位(车次)+品种 + 进厂批次号 +转采样编码 (重车时间为准，防止采样日期与录入日期不一致编码混乱)
	private String getMeikyjpbm() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		boolean showQ = true;
		String sql = "select zhi from xitxxb where mingc = '采样单显示汽车' and leib = '采样' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		ResultSetList rsl = cn.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				showQ = true;
			} else {
				showQ = false;
			}
		}
		boolean showH = true;
		sql = "select zhi from xitxxb where mingc = '采样单显示火车' and leib = '采样' and diancxxb_id = "
				+ visit.getDiancxxb_id();
		rsl = cn.getResultSetList(sql);
		while (rsl.next()) {
			if (rsl.getString("zhi").equals("是")) {
				showH = true;
			} else {
				showH = false;
			}
		}
		sbsql
				.append("select a.bianm as bianm1, c.mingc as bianm2, b.jincbm, b.bianm as bianm3\n"
						+ " from (select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n"
						+ "    from (\n");
		if (showQ) {
			sbsql
					.append(" --汽车\n"
							+ "     select distinct zhilb_id,\n"
							+ "                    (m.mingc || '<p>(' ||\n"
							+ "                    decode(y.mingc, null, f.chec, y.mingc) || ')') as mingc\n"
							+ "          from chepb c, fahb f, meikxxb m, yunsdwb y\n"
							+ "          where c.fahb_id = f.id\n"
							+ "                and f.meikxxb_id = m.id\n"
							+ "                and c.yunsdwb_id = y.id(+)\n"
							+ "                and f.yunsfsb_id != 1\n"
							+ "                and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),'yyyy-mm-dd') =\n"
							+ "          to_date('"
							+ getRiqi()
							+ "', 'yyyy-mm-dd') or\n"
							+ "          to_date(to_char(c.qingcsj, 'yyyy-mm-dd'),'yyyy-mm-dd') =\n"
							+ "          to_date('" + getRiqi()
							+ "', 'yyyy-mm-dd'))\n" + "   --火车\n" + "  union\n");
		}
		if (showH) {
			sbsql
					.append("   select distinct zhilb_id,\n"
							+ "                   (m.mingc || '<p>(' || f.chec || ')') as mingc\n"
							+ "    from chepb c, fahb f, meikxxb m\n"
							+ "   where c.fahb_id = f.id\n"
							+ "        and f.meikxxb_id = m.id\n"
							+ "        and f.meikxxb_id = m.id\n"
							+ "        and f.yunsfsb_id = 1\n"
							+ "        and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
							+ "            to_date('" + getRiqi()
							+ "', 'yyyy-mm-dd')");
		}
		sbsql
				.append(") j,\n"
						+ "     caiyb c,\n"
						+ "     yangpdhb y\n"
						+ " where j.zhilb_id = c.zhilb_id\n"
						+ "   and y.caiyb_id = c.id) a,\n"
						+ "  (select z.zhillsb_id, z.bianm, zz.mingc,c.bianm AS jincbm\n"
						+ "   from (select distinct zhilb_id\n"
						+ "          from chepb c, fahb f\n"
						+ "         where c.fahb_id = f.id\n"
						+ "          and f.yunsfsb_id != 1\n"
						+ "           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),\n"
						+ "                      'yyyy-mm-dd') =\n"
						+ "             to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or\n"
						+ "            to_date(to_char(c.qingcsj, 'yyyy-mm-dd'),\n"
						+ "                      'yyyy-mm-dd') =\n"
						+ "             to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ "           union\n"
						+ "          select distinct zhilb_id\n"
						+ "            from chepb c, fahb f\n"
						+ "           where c.fahb_id = f.id\n"
						+ "           and f.yunsfsb_id = 1\n"
						+ "           and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),\n"
						+ "                       'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) j,\n" + "      caiyb c,\n"
						+ "     yangpdhb y,\n" + "     zhuanmb z,\n"
						+ "     zhuanmlb zz\n"
						+ "   where j.zhilb_id = c.zhilb_id\n"
						+ "     and y.caiyb_id = c.id\n"
						+ "     and z.zhillsb_id = y.zhilblsb_id\n"
						+ "     and z.zhuanmlb_id = zz.id\n"
						+ "     and zz.jib = 1) b,\n"
						+ "  (select distinct zhillsb.id, pinzb.mingc\n"
						+ "    from fahb, pinzb, zhillsb\n"
						+ "   where pinzb.id = fahb.pinzb_id\n"
						+ "     and zhillsb.zhilb_id = fahb.zhilb_id) c\n"
						+ "  where a.zhillsb_id = b.zhillsb_id\n"
						+ "    and a.zhillsb_id = c.id\n"
						+ "    and b.zhillsb_id = c.id\n"
						+ "  order by a.zhillsb_id");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][4];
		ArrHeader[0] = new String[] { "煤矿名称<p>（运输单位）", "品种", "进厂批次号", "采样编码" };

		int ArrWidth[] = new int[] { 150, 60, 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("采样编码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 带品种转码单
	private String getZhuanmad_pinz() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "        (select mingc from pinzb where id=s.pinzb_id) as pinz,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by gongys,meik,pinz,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][6];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "品种", "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 60, 100, 100, 100, };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 全部编码带矿名，查询条件为到货时间
	private String getZhuanmad_daohrq() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "             and f.yunsfsb_id !=1\n"
						+ "           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ "union\n"
						+ "       select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "             and f.yunsfsb_id =1\n"
						+ "           and (to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ ") s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by gongys,meik,c.caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][5];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 100, 100, 100, };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单<p>(" + getRiqi() + ")", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 编码解码单,查询条件：煤矿单位、品种、到货日期（重车时间）
	// 注：只适用于汽车衡
	private String getZhuanmad_Pinz_Daohrq() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql
				.append("select distinct (select mingc from gongysb where id = s.gongysb_id) as gongys,\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "        (select mingc from pinzb where id=s.pinzb_id) as pinz,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ "\n"
						+ " order by gongys,meik,pinz,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][6];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "品种", "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 120, 150, 60, 100, 100, 100, };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 煤矿、运输单位、桶号、采样编码、制样编码、化验编码
	// 适用于采样机自动分桶的转码汇总单
	private String getZhuanmad_fent_huiz() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql
				.append("select distinct "
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "       (select mingc from yunsdwb where id=s.yunsdwb_id )as yunsdw,\n"
						+ "       s.tongh ,\n"
						+ "       c.caiybm,\n"
						+ "       z.zhiybm,\n"
						+ "       h.huaybm\n"
						+ "  from "
						+ "       (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        cf.tongh,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        c.yunsdwb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,caiyftb cf\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and cf.meikxxb_id=f.meikxxb_id\n"
						+ "           and cf.yunsdwb_id=c.yunsdwb_id\n"
						+ "           and cf.caiyrq=to_date('"
						+ getRiqi()
						+ "','yyyy-mm-dd')"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s") + "\n"
						+ " order by meik,yunsdw,tongh,caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][6];
		ArrHeader[0] = new String[] { "煤矿", "运输单位", "桶号", "采样编码", "制样编码",
				"化验编码" };

		int ArrWidth[] = new int[] { 150, 120, 50, 100, 100, 100, };

		rt.setTitle("采样分桶转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 煤矿单位(运输单位)+品种转采样编码
	// 注：只适用于汽车衡
	private String getMeikzcybm() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();

		sbsql
				.append("select a.bianm as bianm1, c.mingc as bianm2, b.bianm as bianm3\n"
						+ " from (select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n"
						+ "    from (select distinct zhilb_id,\n"
						+ "                    (m.mingc || '<p>(' ||\n"
						+ "                    decode(y.mingc, null, f.chec, y.mingc) || ')') as mingc\n"
						+ "          from chepb c, fahb f, meikxxb m, yunsdwb y\n"
						+ "          where c.fahb_id = f.id\n"
						+ "                and f.meikxxb_id = m.id\n"
						+ "                and c.yunsdwb_id = y.id(+)\n"
						+ "                and f.yunsfsb_id != 1\n"
						+ "                and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),'yyyy-mm-dd') =\n"
						+ "          to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or\n"
						+ "          to_date(to_char(c.qingcsj, 'yyyy-mm-dd'),'yyyy-mm-dd') =\n"
						+ "          to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ "  union\n"
						+ "   select distinct zhilb_id,\n"
						+ "                   (m.mingc || '<p>(' ||\n"
						+ "                   decode(y.mingc, null, f.chec, y.mingc) || ')') as mingc\n"
						+ "    from chepb c, fahb f, meikxxb m, yunsdwb y\n"
						+ "   where c.fahb_id = f.id\n"
						+ "        and f.meikxxb_id = m.id\n"
						+ "        and c.yunsdwb_id = y.id(+)\n"
						+ "        and f.meikxxb_id = m.id\n"
						+ "        and f.yunsfsb_id = 1\n"
						+ "        and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "            to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) j,\n"
						+ "     caiyb c,\n"
						+ "     yangpdhb y\n"
						+ " where j.zhilb_id = c.zhilb_id\n"
						+ "   and y.caiyb_id = c.id) a,\n"
						+ "  (select z.zhillsb_id, z.bianm, zz.mingc\n"
						+ "   from (select distinct zhilb_id\n"
						+ "          from chepb c, fahb f\n"
						+ "         where c.fahb_id = f.id\n"
						+ "          and f.yunsfsb_id != 1\n"
						+ "           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),\n"
						+ "                      'yyyy-mm-dd') =\n"
						+ "             to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd') or\n"
						+ "            to_date(to_char(c.qingcsj, 'yyyy-mm-dd'),\n"
						+ "                      'yyyy-mm-dd') =\n"
						+ "             to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd'))\n"
						+ "           union\n"
						+ "          select distinct zhilb_id\n"
						+ "            from chepb c, fahb f\n"
						+ "           where c.fahb_id = f.id\n"
						+ "           and f.yunsfsb_id = 1\n"
						+ "           and to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'),\n"
						+ "                       'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) j,\n"
						+ "      caiyb c,\n"
						+ "     yangpdhb y,\n"
						+ "     zhuanmb z,\n"
						+ "     zhuanmlb zz\n"
						+ "   where j.zhilb_id = c.zhilb_id\n"
						+ "     and y.caiyb_id = c.id\n"
						+ "     and z.zhillsb_id = y.zhilblsb_id\n"
						+ "     and z.zhuanmlb_id = zz.id\n"
						+ "     and zz.jib = 1) b,\n"
						+ "  (select zhillsb.id, pinzb.mingc\n"
						+ "    from fahb, pinzb, zhillsb\n"
						+ "   where pinzb.id = fahb.pinzb_id\n"
						+ "     and zhillsb.zhilb_id = fahb.zhilb_id) c\n"
						+ "  where a.zhillsb_id = b.zhillsb_id\n"
						+ "    and a.zhillsb_id = c.id\n"
						+ "    and b.zhillsb_id = c.id\n"
						+ "  order by a.zhillsb_id");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][3];
		ArrHeader[0] = new String[] { "煤矿名称<p>（运输单位）", "品种", "采样编码" };

		int ArrWidth[] = new int[] { 150, 60, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.setTitle("采样编码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private String getZhuanmad_daohrq_1(int a, int b) {
		JDBCcon cn = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		int cybmwidth = 100;
		int hybmwidth = 100;
		int fontSize = 9;
		if (bigFontSize) {
			cybmwidth = 300;
			hybmwidth = 300;
			fontSize = 28;
		}
		sb.append("select a.bianm as bianm1,b.bianm as bianm2 \n");
		if (a == 0) {
			// 只有矿名，没有附加信息
			sb.append("from\n");

			sb
					.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
			sb.append("from\n");
			sb.append("(select distinct zhilb_id,m.mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb
					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			sb.append("union\n");
			sb.append("select distinct zhilb_id,m.mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb.append(")j,caiyb c,yangpdhb y\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id)a\n");

		} else if (a == -1) {
			// 矿名+火车车次/汽车运输单位
			sb.append("from\n");

			sb
					.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
			sb.append("from\n");
			sb
					.append("(select distinct zhilb_id,(m.mingc||'<p>('||decode(y.mingc,null,f.chec,y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb
					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			sb.append("union\n");
			sb
					.append("select distinct zhilb_id,(m.mingc||'<p>('||decode(y.mingc,null,f.chec,y.mingc)||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb.append(")j,caiyb c,yangpdhb y\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id)a\n");

		} else if (a == -2) {
			// 矿名+火车汽车车次
			sb.append("from\n");

			sb
					.append("(select y.zhilblsb_id as zhillsb_id,j.mingc as bianm,j.mingc as mingc\n");
			sb.append("from\n");
			sb
					.append("(select distinct zhilb_id,(m.mingc||'<p>('||f.chec||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb
					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			sb.append("union\n");
			sb
					.append("select distinct zhilb_id,(m.mingc||'<p>('||f.chec||')') as mingc\n");
			sb.append("from chepb c, fahb f,meikxxb m,yunsdwb y\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and c.yunsdwb_id=y.id(+)\n");
			sb.append("and f.meikxxb_id=m.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb.append(")j,caiyb c,yangpdhb y\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id)a\n");

		} else {
			sb.append("from(\n");
			sb.append("select  z.zhillsb_id,z.bianm,zz.mingc\n");
			sb.append("from\n");

			sb.append("(select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id !=1\n");
			sb
					.append("and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb
					.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd'))\n");
			sb.append("union\n");
			sb.append("select distinct zhilb_id\n");
			sb.append("from chepb c, fahb f\n");
			sb.append("where c.fahb_id = f.id\n");
			sb.append("and f.yunsfsb_id=1\n");
			sb
					.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
							+ getRiqi() + "', 'yyyy-mm-dd')\n");
			sb.append(") j,\n");
			sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
			sb.append("where  j.zhilb_id=c.zhilb_id\n");
			sb.append("and y.caiyb_id=c.id\n");
			sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
			sb.append("and z.zhuanmlb_id=zz.id\n");
			sb.append("and zz.jib=" + a + ") a\n");
		}
		sb.append(",\n");
		sb.append("(select  z.zhillsb_id,z.bianm,zz.mingc\n");
		sb.append("from\n");
		sb.append("(select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id !=1\n");
		sb
				.append("and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		sb
				.append("or to_date(to_char(c.qingcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd'))\n");
		sb.append("union\n");
		sb.append("select distinct zhilb_id\n");
		sb.append("from chepb c, fahb f\n");
		sb.append("where c.fahb_id = f.id\n");
		sb.append("and f.yunsfsb_id=1\n");
		sb
				.append("and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') = to_date('"
						+ getRiqi() + "', 'yyyy-mm-dd')\n");
		sb.append(") j,\n");
		sb.append("caiyb c,yangpdhb y,zhuanmb z,zhuanmlb zz\n");
		sb.append("where  j.zhilb_id=c.zhilb_id\n");
		sb.append("and y.caiyb_id=c.id\n");
		sb.append("and z.zhillsb_id=y.zhilblsb_id\n");
		sb.append("and z.zhuanmlb_id=zz.id\n");
		sb.append("and zz.jib=" + b + ") b\n");
		sb.append("where a.zhillsb_id=b.zhillsb_id\n");
		sb.append("order by a.zhillsb_id");

		ResultSet rs = cn.getResultSet(sb.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		String mingc1 = "";
		String mingc2 = "";
		ResultSet rss = cn.getResultSet("select mingc,jib from zhuanmlb\n"
				+ "union\n" + "select '煤矿名称' as mingc,0 as jib from dual\n"
				+ "union\n" + "select '煤矿名称' as mingc,-1 as jib from dual\n"
				+ "union\n" + "select '煤矿名称' as mingc,-2 as jib from dual");

		try {
			while (rss.next()) {
				if (rss.getInt("jib") == a) {
					mingc1 = rss.getString("mingc");
				}
				if (rss.getInt("jib") == b) {
					mingc2 = rss.getString("mingc");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		ArrHeader[0] = new String[] { mingc1, mingc2 };

		int ArrWidth[] = new int[] { cybmwidth, hybmwidth };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		// rt.body.mergeFixedRow();
		rt.body.setCells(1, 1, rt.body.getRows(), 2, Table.PER_ALIGN,
				Table.ALIGN_CENTER);

		rt.body.setFontSize(fontSize);

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		// rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		// rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 采样码转制样码
	private String getCaiymzzym() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = ((Visit) getPage().getVisit());
		// 根据车号查询
		String caiybm = this.getCaiybmValue().getValue();
		if (caiybm == "请输入" || caiybm.equals("请输入")) {
			caiybm = "";
		} else {
			caiybm = "  and c.caiybm='" + caiybm + "'";
		}

		sbsql
				.append("select distinct c.caiybm, z.zhiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n" + "  " + caiybm
						+ " \n" + Jilcz.filterDcid(visit, "s")
						+ " order by caiybm,zhiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "采样编码", "制样编码" };

		int ArrWidth[] = new int[] { 100, 100 };

		rt.setTitle("采样编码转制样编码", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 采样码转制样码
	private String getCaiymzzym_hd() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = ((Visit) getPage().getVisit());
		// 根据车号查询
		String caiybm = this.getCaiybmValue().getValue();
		if (caiybm == "请输入" || caiybm.equals("请输入")) {
			caiybm = "";
		} else {
			caiybm = "  and c.caiybm='" + caiybm + "'";
		}

		sbsql
				.append("select distinct c.caiybm, z.zhiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(c.lursj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n" + "  " + caiybm
						+ " \n" + Jilcz.filterDcid(visit, "s")
						+ " order by caiybm,zhiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "采样编码", "制样编码" };

		int ArrWidth[] = new int[] { 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.setTitle("采样编码转制样编码", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.body.setFontSize(18);
		rt.body.setColWidth(1, 200);
		rt.body.setColWidth(2, 200);
		rt.body.setRowHeight(20);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 制样编码转化验编码
	private String getZhiybmzhybm() {
		JDBCcon cn = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sbsql = new StringBuffer();

		sbsql
				.append("select distinct z.zhiybm, h.huaybm\n"
						+ "  from (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('"
						+ getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						// + " and to_date('" + getRiqi()+ "', 'yyyy-mm-dd') + 1
						// > c.zhongcsj \n"
						// + " and to_date('" + getRiqi()+ "', 'yyyy-mm-dd') <=
						// c.zhongcsj) s\n"

						+ " where z.zhillsb_id = h.zhillsb_id\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ " order by zhiybm,huaybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 100, 100 };
		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制

		rt.setTitle("制样编码转化验编码", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 采样码转制样码(以重车时间为准)
	private String getCaiyzzyzcsj() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = ((Visit) getPage().getVisit());
		// 根据车号查询
		String caiybm = this.getCaiybmValue().getValue();
		if (caiybm == "请输入" || caiybm.equals("请输入")) {
			caiybm = "";
		} else {
			caiybm = "  and c.caiybm='" + caiybm + "'";
		}

		sbsql
				.append("select distinct c.caiybm, z.zhiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd') + 1 > c.zhongcsj \n"
						+ "           and to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd') <= c.zhongcsj) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n" + "  " + caiybm
						+ " \n" + Jilcz.filterDcid(visit, "s")
						+ " order by caiybm,zhiybm");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "采样编码", "制样编码" };

		int ArrWidth[] = new int[] { 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("采样编码转制样编码", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));

		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 制样码转化验码(以重车时间为准)
	private String getZhiyzhyzcsj() {
		JDBCcon cn = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sbsql = new StringBuffer();

		sbsql
				.append("select distinct z.zhiybm, h.huaybm\n"
						+ "  from (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd') + 1 > c.zhongcsj \n"
						+ "           and to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd') <= c.zhongcsj) s\n"
						+ " where z.zhillsb_id = h.zhillsb_id\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s")
						+ " order by zhiybm,huaybm");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 100, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("制样编码转化验编码", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 太原汽车转码单 煤矿+到货日期+车数+净重+样品类别+采样编码
	private String get_zhuanmd_taiyuan() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getDiancxxb_id() == 264) {// 264是阳城发电的id,
											// 阳城发电需要增加序号列,太原二电不需要序号列.
			sbsql
					.append("select rownum as xuh,meik,yunsfs,daohrq,ches,jingz,leib,caiybm from( \n");
		}
		sbsql
				.append("select distinct\n"
						+ "       (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n"
						+ "        (select mingc from yunsfsb y where id=s.yunsfsb_id) as yunsfs,\n"
						+ "         decode(s.yunsfsb_id,1,(select to_char(max(zhongcsj),'yyyy-mm-dd  hh24:mi:ss') from chepb where fahb_id = s.id),\n"
						+ "          (select to_char(daohrq,'yyyy-mm-dd') from fahb where id = s.id)) as daohrq,                                                                 \n"
						+ "        (select ches from fahb where id = s.id) as ches,\n"
						+ "        (select round_new(sum(jingz),0) from fahb where id = s.id) as jingz,\n"
						+ "  		z.leib,\n"
						+ "       c.caiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id,(select huaylb from zhillsb where id=zhillsb_id) as leib\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 2\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid,\n"
						+ "                         f.yunsfsb_id\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.daohrq=to_date('" + this.getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s"));
		if (visit.getDiancxxb_id() == 264) {
			sbsql.append(" order by meik,daohrq,ches,leib)");
		} else {
			sbsql.append(" order by meik,daohrq,ches,leib");
		}

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();
		if (visit.getDiancxxb_id() == 264) {// 阳城电厂
			String ArrHeader[][] = new String[1][8];
			ArrHeader[0] = new String[] { "序号", "煤矿", "运输<br>方式", "到货日期", "车数",
					"净重", "类别", "采样编码" };
			int ArrWidth[] = new int[] { 50, 150, 60, 120, 50, 50, 70, 80 };
			int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
					.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
					.getString2());// 取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
			rt.setTitle("转码单", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 2));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(Report.PAPER_ROWS);
			// 增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			rt.createDefautlFooter(ArrWidth);
		} else {// 太原二电
			String ArrHeader[][] = new String[1][7];
			ArrHeader[0] = new String[] { "煤矿", "运输方式", "到货日期", "车数", "净重",
					"类别", "采样编码" };
			int ArrWidth[] = new int[] { 150, 80, 120, 60, 60, 80, 80 };
			int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
					.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
					.getString2());// 取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
			rt.setTitle("转码单", ArrWidth);
			rt.setBody(new Table(rs, 1, 0, 2));
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(Report.PAPER_ROWS);
			// 增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			rt.createDefautlFooter(ArrWidth);
		}

		rt.body.setFontSize(12);
		rt.setDefautlFooter(1, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(5, 1, "制表:", Table.ALIGN_LEFT);
		for (int i = 1; i <= rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	// 太原 采样转化验编码,按到货日期查询
	private String getZhiyzhy() {

		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append("select distinct\n"
						+ "       c.caiybm,\n"
						+ "       h.huaybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id,(select huaylb from zhillsb where id=zhillsb_id) as leib\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 2\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select bianm as huaybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '化验编码')) h,\n"
						+ "       (select distinct f.id,f.pinzb_id,\n"
						+ "                        f.diancxxb_id,\n"
						+ "                        f.gongysb_id,\n"
						+ "                        f.meikxxb_id,\n"
						+ "                        z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c,pinzb p\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "            and f.pinzb_id=p.id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and f.daohrq=to_date('" + this.getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and h.zhillsb_id = c.zhillsb_id\n"
						+ "   and z.zhillsb_id = h.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and h.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s") + " order by caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "采样编码", "化验编码" };

		int ArrWidth[] = new int[] { 150, 150 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);

		for (int i = 1; i <= rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rt.body.setFontSize(15);
		rt.body.setRowHeight(30);

		cn.Close();
		return rt.getAllPagesHtml();

	}

	private String getTongh2Caiybh() {
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();

		sbsql.append("select distinct c.tongh,c.caiybh from caiyftb c").append(
				"\n");
		sbsql
				.append(
						"where c.caiyrq=to_date('" + this.getRiqi()
								+ "','yyyy-mm-dd')").append(
						Jilcz.filterDcid(visit, "c")).append("\n");
		sbsql
				.append("and (select count(id) from jianjghb j where j.caiyftb_id=c.id "
						+ Jilcz.filterDcid(visit, "j") + ")>0");
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][2];
		ArrHeader[0] = new String[] { "采样桶号", "采样编码" };

		int ArrWidth[] = new int[] { 150, 150 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("转码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);

		for (int i = 1; i <= rt.body.getCols(); i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		rt.body.setFontSize(15);
		rt.body.setRowHeight(30);

		cn.Close();
		return rt.getAllPagesHtml();
	}
	/*修改时间：2010-08-04
	 *修改人：梁丽丽
	  修改内容：鲁北电厂增加序号列
	 * 
	 */
	private String getMkPzCcBm(){
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
	
		sbsql.
		    
		       append(
		    		   "select distinct rownum as xuh ," +
		    		   "				(select mingc from gongysb where id = s.gongysb_id) as gongys,\n" +
		    		   "                (select mingc from meikxxb where id = s.meikxxb_id) as meik,\n" + 
		    		   "                (select mingc from pinzb where id = s.pinzb_id) as pinz,\n" + 
		    		   "                (select chec from fahb where id = s.id) as chec,\n" + 
		    		   "                c.caiybm\n" + 
		    		   "  from (select bianm as caiybm, zhillsb_id\n" + 
		    		   "          from zhuanmb\n" + 
		    		   "         where zhillsb_id in\n" + 
		    		   "               (select zm.zhillsb_id as id\n" + 
		    		   "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
		    		   "                 where zm.zhuanmlb_id = lb.id\n" + 
		    		   "                   and lb.jib = 1\n" + 
		    		   "                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
		    		   "                   and f.zhilb_id = z.zhilb_id\n" + 
		    		   "                   and z.id = zm.zhillsb_id)\n" + 
		    		   "           and zhuanmlb_id =\n" + 
		    		   "               (select id from zhuanmlb where mingc = '采样编码')) c,\n" + 
		    		   "       (select bianm as zhiybm, zhillsb_id\n" + 
		    		   "          from zhuanmb\n" + 
		    		   "         where zhillsb_id in\n" + 
		    		   "               (select zm.zhillsb_id as id\n" + 
		    		   "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
		    		   "                 where zm.zhuanmlb_id = lb.id\n" + 
		    		   "                   and lb.jib = 2\n" + 
		    		   "                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
		    		   "                   and f.zhilb_id = z.zhilb_id\n" + 
		    		   "                   and z.id = zm.zhillsb_id)\n" + 
		    		   "           and zhuanmlb_id =\n" + 
		    		   "               (select id from zhuanmlb where mingc = '制样编码')) z,\n" + 
		    		   "       (select bianm as huaybm, zhillsb_id\n" + 
		    		   "          from zhuanmb\n" + 
		    		   "         where zhillsb_id in\n" + 
		    		   "               (select zm.zhillsb_id as id\n" + 
		    		   "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
		    		   "                 where zm.zhuanmlb_id = lb.id\n" + 
		    		   "                   and lb.jib = 3\n" + 
		    		   "                   and y.zhilblsb_id = zm.zhillsb_id\n" + 
		    		   "                   and f.zhilb_id = z.zhilb_id\n" + 
		    		   "                   and z.id = zm.zhillsb_id)\n" + 
		    		   "           and zhuanmlb_id =\n" + 
		    		   "               (select id from zhuanmlb where mingc = '化验编码')) h,\n" + 
		    		   "       (select distinct f.id,\n" + 
		    		   "                        f.pinzb_id,\n" + 
		    		   "                        f.diancxxb_id,\n" + 
		    		   "                        f.gongysb_id,\n" + 
		    		   "                        f.meikxxb_id,\n" + 
		    		   "                        z.id as zid\n" + 
		    		   "          from zhillsb z,\n" + 
		    		   "               fahb f,\n" + 
		    		   "               (select fahb_id, min(zhongcsj) zhongcsj\n" + 
		    		   "                  from chepb\n" + 
		    		   "                 group by fahb_id) c,\n" + 
		    		   "               pinzb p\n" + 
		    		   "         where f.zhilb_id = z.zhilb_id\n" + 
		    		   "           and f.pinzb_id = p.id\n" + 
		    		   "           and c.fahb_id = f.id\n" + 
		    		   "           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n" + 
		    		   "               to_date('"+getRiqi()+"', 'yyyy-mm-dd'))) s\n" + 
		    		   " where c.zhillsb_id = z.zhillsb_id\n" + 
		    		   "   and h.zhillsb_id = c.zhillsb_id\n" + 
		    		   "   and z.zhillsb_id = h.zhillsb_id\n" + 
		    		   "   and c.zhillsb_id = s.zid\n" + 
		    		   "   and h.zhillsb_id = s.zid\n" + 
		    		   "   and z.zhillsb_id = s.zid\n" + 
		    		   Jilcz.filterDcid(visit, "s")  +"\n"+
		    		   " order by xuh,gongys, meik, pinz, chec, caiybm");

		
		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][4];
		ArrHeader[0] = new String[] { "序号","供应商","煤矿名称", "品种", "车次", "采样编码" };

		int ArrWidth[] = new int[] { 50,150, 100,50, 50, 100 };

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("采样编码单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	
	}
	
	private String getAllXiangXi_lb(){
		JDBCcon cn = new JDBCcon();
		StringBuffer sbsql = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		sbsql
				.append(
						"SELECT s.gongys,\n" +
						"       s.meik,\n" + 
						"       s.pinz,\n" + 
						"       s.chec,\n" + 
						"       c.caiybm,\n" + 
						"       z.zhiybm,\n" + 
						"       h.huaybm\n" + 
						"  FROM (SELECT bianm AS caiybm, zhillsb_id\n" + 
						"          FROM zhuanmb\n" + 
						"         WHERE zhillsb_id IN\n" + 
						"               (SELECT zm.zhillsb_id AS id\n" + 
						"                  FROM zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
						"                 WHERE zm.zhuanmlb_id = lb.id\n" + 
						"                   AND lb.jib = 1\n" + 
						"                   AND y.zhilblsb_id = zm.zhillsb_id\n" + 
						"                   AND f.zhilb_id = z.zhilb_id\n" + 
						"                   AND z.id = zm.zhillsb_id)\n" + 
						"           AND zhuanmlb_id =\n" + 
						"               (SELECT id FROM zhuanmlb WHERE mingc = '采样编码')) c,\n" + 
						"       (SELECT bianm AS zhiybm, zhillsb_id\n" + 
						"          FROM zhuanmb\n" + 
						"         WHERE zhillsb_id IN\n" + 
						"               (SELECT zm.zhillsb_id AS id\n" + 
						"                  FROM zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
						"                 WHERE zm.zhuanmlb_id = lb.id\n" + 
						"                   AND lb.jib = 2\n" + 
						"                   AND y.zhilblsb_id = zm.zhillsb_id\n" + 
						"                   AND f.zhilb_id = z.zhilb_id\n" + 
						"                   AND z.id = zm.zhillsb_id)\n" + 
						"           AND zhuanmlb_id =\n" + 
						"               (SELECT id FROM zhuanmlb WHERE mingc = '制样编码')) z,\n" + 
						"       (SELECT bianm AS huaybm, zhillsb_id\n" + 
						"          FROM zhuanmb\n" + 
						"         WHERE zhillsb_id IN\n" + 
						"               (SELECT zm.zhillsb_id AS id\n" + 
						"                  FROM zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n" + 
						"                 WHERE zm.zhuanmlb_id = lb.id\n" + 
						"                   AND lb.jib = 3\n" + 
						"                   AND y.zhilblsb_id = zm.zhillsb_id\n" + 
						"                   AND f.zhilb_id = z.zhilb_id\n" + 
						"                   AND z.id = zm.zhillsb_id)\n" + 
						"           AND zhuanmlb_id =\n" + 
						"               (SELECT id FROM zhuanmlb WHERE mingc = '化验编码')) h,\n" + 
						"       (SELECT f.diancxxb_id AS diancxxb_id,\n" + 
						"               zl.id AS zlid,\n" + 
						"               MAX(g.mingc) AS gongys,\n" + 
						"               MAX(m.mingc) AS meik,\n" + 
						"               p.mingc AS pinz,\n" + 
						"               f.chec AS chec\n" + 
						"          FROM gongysb g,\n" + 
						"               meikxxb m,\n" + 
						"               fahb    f,\n" + 
						"               pinzb   p,\n" + 
						"               zhillsb zl,\n" + 
						"               chepb   c\n" + 
						"         WHERE f.zhilb_id = zl.zhilb_id\n" + 
						"           AND f.pinzb_id = p.id\n" + 
						"           AND c.fahb_id = f.id\n" + 
						"           AND f.gongysb_id = g.id\n" + 
						"           AND f.meikxxb_id = m.id\n" + 
						"           and (to_date(to_char(c.zhongcsj, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n" + 
		    		   "               to_date('"+getRiqi()+"', 'yyyy-mm-dd'))\n" + 
						"         GROUP BY (g.mingc, m.mingc, p.mingc, f.chec, f.diancxxb_id, zl.id)) s\n" + 
						" WHERE c.zhillsb_id = z.zhillsb_id\n" + 
						"   AND h.zhillsb_id = c.zhillsb_id\n" + 
						"   AND z.zhillsb_id = h.zhillsb_id\n" + 
						"   AND c.zhillsb_id = s.zlid\n" + 
						"   AND h.zhillsb_id = s.zlid\n" + 
						"   AND z.zhillsb_id = s.zlid\n" + 
		    		    Jilcz.filterDcid(visit, "s")  +"\n"+
						" GROUP BY (s.gongys, s.meik, s.pinz, s.chec, c.caiybm, z.zhiybm, h.huaybm,\n" + 
						"           h.zhillsb_id)\n" + 
						" ORDER BY s.gongys, s.meik, s.pinz, s.chec, caiybm");

		ResultSet rs = cn.getResultSet(sbsql.toString());
		Report rt = new Report();

		String ArrHeader[][] = new String[1][12];
		ArrHeader[0] = new String[] { "供应商", "煤矿", "品种",  "车次", "采样编码", "制样编码", "化验编码" };

		int ArrWidth[] = new int[] { 150, 100, 50, 50, 80, 80, 80};

		int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
				.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
				.getString2());// 取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
		rt.setTitle("解 码 单", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 2));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(Report.PAPER_ROWS);
		// 增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRow();

		rt.body.ShowZero = false;
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(7, 1, "审核:", Table.ALIGN_RIGHT);
		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_LEFT);

		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}
	


	private boolean _RefurbishClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {

			Refurbish();
			_RefurbishClick = false;
		}

	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		if (mstrReportName.equals(CAIY)) {
			getCaiymzzym();
		} else if (mstrReportName.equals(HUAY)) {
			getZhiybmzhybm();
		} else if ((mstrReportName).equals(ALL)) {
			getZhuanmad();
		}
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);

			// begin方法里进行初始化设置
			visit.setString2(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString2(pagewith);
			}
			// visit.setString2(null);保存传递的非默认纸张的样式

			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			if (!visit.getString1().equals(
					cycle.getRequestContext().getParameters("lx")[0])) {

				visit.setProSelectionModel10(null);
				visit.setDropDownBean10(null);
				visit.setProSelectionModel5(null);
				visit.setDropDownBean5(null);
				setRiqi(null);

			}
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}

		}

		blnIsBegin = true;
		getSelectData();

	}

	// 绑定日期
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}

	}

	public void getSelectData() {
		Toolbar tb1 = new Toolbar("tbdiv");
		getCaiybmModels();
		tb1.addText(new ToolbarText("到货日期或入炉日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQI");
		// df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		if (mstrReportName.equals(CAIY)) {
			// 车号
			tb1.addText(new ToolbarText("采样单号:"));
			ComboBox CB = new ComboBox();
			CB.setTransform("CAIYBM");
			CB.setWidth(120);
			CB.setListeners("select:function(){document.Form0.submit();}");
			CB.setEditable(true);
			tb1.addField(CB);
			tb1.addText(new ToolbarText("-"));
		}

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

	public String getcontext() {
		return "";
		// return "var context='http://"
		// + this.getRequestCycle().getRequestContext().getServerName()
		// + ":"
		// + this.getRequestCycle().getRequestContext().getServerPort()
		// + ((Visit) getPage().getVisit()).get() + "';";
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	private String _pageLink;

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page方法
	protected void initialize() {
		_pageLink = "";
	}

	// 采样编码
	public IDropDownBean getCaiybmValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getCaiybmModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setCaiybmValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setCaiybmModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getCaiybmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getCaiybmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getCaiybmModels() {

		StringBuffer sbsql = new StringBuffer();
		Visit visit = ((Visit) getPage().getVisit());
		sbsql
				.append("select distinct z.zhillsb_id as id, c.caiybm\n"
						+ "  from (select bianm as caiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 1\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '采样编码')) c,\n"
						+ "       (select bianm as zhiybm, zhillsb_id\n"
						+ "          from zhuanmb\n"
						+ "         where zhillsb_id in\n"
						+ "               (select zm.zhillsb_id as id\n"
						+ "                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n"
						+ "                 where zm.zhuanmlb_id = lb.id\n"
						+ "                   and lb.jib = 3\n"
						+ "                   and y.zhilblsb_id = zm.zhillsb_id\n"
						+ "                   and f.zhilb_id = z.zhilb_id\n"
						+ "                   and z.id = zm.zhillsb_id)\n"
						+ "           and zhuanmlb_id =\n"
						+ "               (select id from zhuanmlb where mingc = '制样编码')) z,\n"
						+ "       (select distinct f.id, f.diancxxb_id, z.id as zid\n"
						+ "          from zhillsb z, fahb f, chepb c\n"
						+ "         where f.zhilb_id = z.zhilb_id\n"
						+ "           and c.fahb_id = f.id\n"
						+ "           and to_date(to_char(f.daohrq, 'yyyy-mm-dd'), 'yyyy-mm-dd') =\n"
						+ "               to_date('" + getRiqi()
						+ "', 'yyyy-mm-dd')) s\n"
						+ " where c.zhillsb_id = z.zhillsb_id\n"
						+ "   and c.zhillsb_id = s.zid\n"
						+ "   and z.zhillsb_id = s.zid\n"
						+ Jilcz.filterDcid(visit, "s") + " order by caiybm");

		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sbsql.toString(),
						"请输入"));
		return;
	}
}