package com.zhiren.gangkjy.zhilgl.huayxx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:童忠付
 * 时间:2009-3-25
 * 内容:质量管理 装船化验统计查询
 */
public class Zhuangchytjcx extends BasePage implements PageValidateListener {

	private static final String BAOBPZB_GUANJZ = "ZL_HY_ZC_HY_TJ";// baobpzb中对应的关键字   质量 化验 装船 化验 统计查询
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

	private String TAIZ = "Zhiltz";

	private String YUEB = "Zhilyb";

	

	private boolean blnIsBegin = false;

	// private String leix = "";

	private String mstrReportName = "";

	public String getPrintTable() {
		setMsg(null);
		if (!blnIsBegin) {
			return "";
		}

		blnIsBegin = false;

		
			return getZhiltz();
		}
	

	// 全部编码
	/*
	 * 将检质数量由jingz改为发货表laimsl字段 并对弹筒热值、发热量（MJ）、发热量（Kcal）进行修约
	 * 
	 */
	
	private StringBuffer getBaseSql(){

		StringBuffer buffer = new StringBuffer();
		Visit v = (Visit) getPage().getVisit();

		String str="";
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = " and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = " and dc.id = " + getTreeid() + "";
		}
		buffer.append(
				"select hangc,\n" +
				"         luncxxb_id,\n" + 
				"       xiaosgysb_id,\n" + 
				"       ligsj,\n" + 
				"       zhuangcl,\n" + 
				"       mt,\n" + 
				"       mad,\n" + 
				"       aad,\n" + 
				"       ad,\n" + 
				"       aar,\n" + 
				"       vad,\n" + 
				"       vdaf,\n" + 
				"       qbad*1000,\n" + 
				"       farl*1000,\n" + 
				"       qbar,\n" + 
				
				"       sdaf,stad,\n" + 
				"       std,star,\n" + 
				"       hdaf,had,\n" + 
				
				"       fcad,\n" + 
				"       qgrd\n" + 
				"  from (select decode(grouping(zc.hangc), 1, '总计',zc.hangc) as hangc,\n" + 
				"               decode(grouping(zc.hangc) + grouping(l.mingc),\n" + 
				"                      1,\n" + 
				"                      '合计',\n" + 
				"                       l.mingc) luncxxb_id,\n" + 
				"               decode(grouping(zc.hangc) + grouping(l.mingc) +\n" + 
				"                      grouping( g.mingc),\n" + 
				"                      1,\n" + 
				"                       '船次小计',\n" + 
				"                       g.mingc) xiaosgysb_id,\n" + 
				"                decode(grouping(zc.hangc) + grouping(l.mingc) +"+
				"                grouping( g.mingc)+grouping(zc.ligsj), 1, '公司小计',to_char(zc.ligsj, 'yyyy-mm-dd')) ligsj,"+
				"               sum(round_new(zc.zhuangcl,"+v.getShuldec()+")) zhuangcl,\n" + 
			
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.mad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as mad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as aad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.ad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as ad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aar * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as aar,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as vad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vdaf * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as vdaf,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(zc.zhuangcl,"+v.getShuldec()+")) /\n" + 
				"                                          sum(round_new(zc.zhuangcl,"+v.getShuldec()+"))\n" + 
				"                                           * 1000 / 4.1816,\n" + 
				"                                0)) as qbar,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.sdaf * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as sdaf,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.stad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as stad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.std * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as std,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as star,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.hdaf * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as hdaf,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.had * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as had,\n" + 
				
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.fcad * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as fcad,\n" + 
				"               decode(sum(round_new(zc.zhuangcl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.qgrd * round_new(zc.zhuangcl,"+v.getShuldec()+")) / sum(round_new(zc.zhuangcl,"+v.getShuldec()+")), 2)) as qgrd \n" + 
	//			"               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n" + 
	//			"               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n" + 
				"         from  zhillsb z,caiyb c,zhuangcb zc,caiylbb cb, luncxxb l,vwxuqdw g, diancxxb dc\n" + 
				"          where z.caiyb_id=c.id and c.caiylbb_id=cb.id \n" + 
				"          and zc.diancxxb_id=dc.id and cb.bianm='ZC' and z.zhilb_id=zc.zhilb_id \n" + 
				"           and l.id=zc.luncxxb_id   and zc.xiaosgysb_id=g.id and zc.diancxxb_id="+v.getDiancxxb_id()+"\n" + 
		
				"           and zc.zhuangcjssj >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
				"           and zc.zhuangcjssj <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" + 
				str+
			//	"           and f.yunsfsb_id = "+getYunsfsValue().getId()+"\n" + 
				"           group by rollup(zc.hangc, l.mingc, g.mingc, zc.ligsj)\n" + 
			//	"        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n" + 
				"         order by grouping(zc.hangc) desc,grouping(l.mingc) desc,grouping(g.mingc) desc,l.mingc,g.mingc,grouping(zc.ligsj) desc) ");
				
			return buffer;
	}
	private String getZhiltz() {
		Visit v = (Visit) getPage().getVisit();
		
		JDBCcon con = new JDBCcon();

		StringBuffer buffer = new StringBuffer();
		buffer=this.getBaseSql();

		ResultSetList rstmp = con.getResultSetList(getBaseSql().toString());
//		System.out.println(buffer);
		Report rt = new Report();
		ResultSetList rs = null;
		String[][] ArrHeader = null;
		String[] strFormat = null;
		int[] ArrWidth = null;
		String[] Zidm = null;
		StringBuffer sb = new StringBuffer();
		sb
				.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
						+ BAOBPZB_GUANJZ + "' order by xuh");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl.getRows() > 0) {
			ArrWidth = new int[rsl.getRows()];
			strFormat = new String[rsl.getRows()];
			String biaot = rsl.getString(0, 1);
			String[] Arrbt = biaot.split("!@");
			ArrHeader = new String[Arrbt.length][rsl.getRows()];
			Zidm = new String[rsl.getRows()];
			rs = new ResultSetList();
			while (rsl.next()) {
				Zidm[rsl.getRow()] = rsl.getString("zidm");
				ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
				strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
						: rsl.getString("format");
				String[] title = rsl.getString("biaot").split("!@");
				for (int i = 0; i < title.length; i++) {
					ArrHeader[i][rsl.getRow()] = title[i];
				}
			}
			rs.setColumnNames(Zidm);
			while (rstmp.next()) {
				rs.getResultSetlist().add(rstmp.getArrString(Zidm));
			}
			rstmp.close();
			rsl.close();
			rsl = con
					.getResultSetList("select biaot from baobpzb where guanjz='"
							+ BAOBPZB_GUANJZ + "'");
			String Htitle = "";
			while (rsl.next()) {
				Htitle = rsl.getString("biaot");
			}
			rt.setTitle(Htitle, ArrWidth);
			rsl.close();
		} else {
			rs = rstmp;

			ArrHeader = new String[][] { { Local.hangc_zhuangcb,
					Local.chuanm_zhuangcb, Local.liux_zhuangcb,
					Local.ligsj_zhuangcb,Local.zhuangcsl_zhuangcb,
				    Local.quancf_zhilb, Local.kongqgzjsf_zhilb,
					Local.kongqgzjhf_zhilb,Local.ganzjhf_zhilb,
					Local.shoudjhf_zhilb,Local.kongqgzjhff_zhilb,
					Local.ganzwhjhff_zhilb,Local.tantfrl_zhilb,
					Local.shoudjdwfrl,Local.shoudjdwrz_zhilb,
					Local.ganzwhjl_zhilb,Local.kongqgzjl_zhilb,
					Local.ganzjql,Local.shoudjql,
					Local.ganzwhjq, Local.kongqgzjq,
					Local.gudt_zhilb,Local.ganjgwr_zhilb} };
//
//			ArrWidth = new int[] { 85, 100, 90, 50, 40, 40, 40, 40, 40, 40,
//					40, 40, 40, 40, 40, 40, 40 ,40,40,40,40,40,40};
//
//			rt.setTitle(Local.RptTitle_zhuangchytjcx, ArrWidth);
		}
		
		ArrWidth = new int[] { 85, 100, 90, 80, 40, 40, 40, 40, 40, 40,
				40, 40, 40, 40, 40, 40, 40 ,40,40,40,40,40,40};
		rt.setTitle(Local.RptTitle_zhuangchytjcx, ArrWidth);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 25);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "制表单位:"
				+ v.getDiancqc(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(5, 11, "离港日期:"
				+ getRiqi() + "至"
				+getRiq2(),
				Table.ALIGN_CENTER);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

		strFormat = new String[] { "", "", "", "","", "0.0",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
				"0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00" };

		rt.setBody(new Table(rs, 1, 0, 3));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(25);
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 22; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(11, 3, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 4, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
		con.Close();
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages> 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.body.setRowHeight(21);
		RPTInit.getInsertSql(v.getDiancxxb_id(), getBaseSql().toString(), rt,
				Local.RptTitle_zhuangchytjcx, "" + BAOBPZB_GUANJZ);
		return rt.getAllPagesHtml();

	}

	// 运输方式下拉框
	/*
	private boolean falg1 = false;

	private IDropDownBean YunsfsValue;

	public IDropDownBean getYunsfsValue() {
		if (YunsfsValue == null) {
			YunsfsValue = (IDropDownBean) YunsfsModel.getOption(0);
		}
		return YunsfsValue;
	}

	public void setYunsfsValue(IDropDownBean Value) {
		if (!(YunsfsValue == Value)) {
			YunsfsValue = Value;
			falg1 = true;
		}
	}

	private IPropertySelectionModel YunsfsModel;

	public void setYunsfsModel(IPropertySelectionModel value) {
		YunsfsModel = value;
	}

	public IPropertySelectionModel getYunsfsModel() {
		if (YunsfsModel == null) {
			getYunsfsModels();
		}
		return YunsfsModel;
	}

	public IPropertySelectionModel getYunsfsModels() {
		String sql="select id,mingc from yunsfsb";
		YunsfsModel = new IDropDownModel(sql);
		return YunsfsModel;
	}

	*/



	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setProSelectionModel1(null);
			visit.setDropDownBean1(null);
			setTreeid(null);
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

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}

	boolean riqchange = false;

	private String riq;

	public String getRiq() {
		if (riq == null || riq.equals("")) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {

		if (this.riq != null && !this.riq.equals(riq)) {
			this.riq = riq;
			riqchange = true;
		}

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
		getZhiltz();
	}


	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");

		tb1.addText(new ToolbarText("装船日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("riqi", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("riq2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		tb1.addField(tf);
		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("运输方式:"));
//		ComboBox meik = new ComboBox();
//		meik.setTransform("YUNSFSSelect");
//		meik.setEditable(true);
//		meik.setWidth(100);
//		meik.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(meik);
		


		ToolbarButton tb = new ToolbarButton(null, "刷新",
				"function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
		tb1.addItem(tb);


		setToolbar(tb1);

	}
	
//	添加电厂树
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("diancTree_text")).setValue
				(((IDropDownModel)getDiancmcModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	电厂名称
	private IPropertySelectionModel _IDiancModel;
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean1()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(_value);
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
//	得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
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

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}

}
