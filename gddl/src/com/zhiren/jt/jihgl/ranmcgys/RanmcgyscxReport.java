package com.zhiren.jt.jihgl.ranmcgys;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class RanmcgyscxReport extends BasePage {
	public boolean getRaw() {
		return true;
	}

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

	private boolean blnIsBegin = false;

	public String getPrintTable() throws SQLException {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getZhiltj();
	}

	private String getZhiltj() throws SQLException {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sbsql = new StringBuffer("");
		String riq = "";
		int jib = this.getDiancTreeJib();

		String strGongsID = "";
		if (jib == 1) {// 选集团时刷新出所有的电厂
			strGongsID = " ";

		} else if (jib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " + this.getTreeid()
					+ " or dc.shangjgsid= " + this.getTreeid() + ")";

		} else if (jib == 3) {// 选电厂只刷新出该电厂
			strGongsID = " and dc.id= " + this.getTreeid();

		} else if (jib == -1) {
			strGongsID = " and dc.id = "
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		// 年，月
		long intyear;
		if (getNianf1Value() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianf1Value().getId();
		}
		riq = "" + getNianf1Value() + "年燃煤预算";
		int nian = Integer.parseInt(getNianf1Value().getValue());
		;
		String qisyf="0";
		String zhongzyf="0";
		int nianf=0;
		String iid="0";
		String sqll="select id,c.nianf, c.beginyuef,c.endyuef from chaxtjb c where c.id="+getCHAXTJB_IDDropDownValue()+" and c.nianf ="+getNianf1Value()+"";
//		System.out.println(sqll);
		ResultSet rs1 = con.getResultSet(sqll);
		while(rs1.next()){
			iid=rs1.getString("id");
			nianf=Integer.parseInt(rs1.getString("nianf"));
			qisyf=rs1.getString("beginyuef");
			zhongzyf=rs1.getString("endyuef");
		}
		String sql =

		"\n"
				+ "select dmingc,\n"
				+ "       decode(jihkjb_id,'','燃煤小计','燃煤小计','燃煤小计','"
				+ (nianf )
				+ "年预算') ,\n"
				+ "       decode(gmingc,'',decode(jihkjb_id,'','"
				+ (nianf )
				+ "年预算',jihkjb_id),gmingc) ,\n"
				+ "       CAIGL,\tYUNS,\tDAOCRZ,\tCHUKJG,\tYUNF,\tZAF,\tQITFY,\tDAOCZHJHS,\tDAOCZHJBHS,\tDAOCBMDJBHS,\tQUANNDHL,\tHETL,\tDAOHL,\tHETCKJG,\tHETRZ,\tBEIZ\n"
				+ "\n"
				+ "from\n"
				+ "\n"
				+ "(\n"
				+ "\n"
				+ "   (---实际发生分公司下\n"
				+ "    select d.mingc as dmingc,'燃煤小计' JIHKJB_ID,'"
				+ (nianf-1)
				+ "年"
				+ qisyf
				+ "-"
				+ zhongzyf
				+ "月"
				+ "' gmingc, sum(caigl) caigl,sum(yuns) yuns,\n"
				+ "\n"
				+ "        sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs\n"
				+ "\n"
				+ "    , 0 as quanndhl,0 as hetl,0 as daohl,0 as hetckjg,0 as hetrz, '' beiz\n"
				+ "     from ranmzbsjb ranm ,diancxxb d ,chaxtjb c\n"
				+ " where\n"
				+ " ranm.chaxtjb_id=c.id and\n"
				+ "  --c.id='传递的'\n"
				+ "  c.id="
				+ iid
				+ "\n"
				+ " and ranm.diancxxb_id=d.id\n"
				+ " group by (d.mingc)\n"
				+ "\n"
				+ "\n"
				+ "   )\n"
				+ "\n"
				+ "   union(\n"
				+ "   --去年计算预计09\n"
				+ "\n"
				+ "    select d.mingc as dmingc ,'燃煤小计' JIHKJB_ID,'"
				+ (nianf-1)
				+ "年预计' gmingc, sum(caigl) caigl,sum(yuns) yuns,\n"
				+ "\n"
				+ "        sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs ,\n"
				+ "     sum(quanndhl) quanndhl ,sum(hetl) hetl,sum(daohl) daohl,sum(hetckjg) hetckjg,sum(hetrz) hetrz ,'' beiz\n"
				+ "\n"
				+ "     from ranmcgysb ran ,diancxxb d\n"
				+ " where to_char(ran.nianf, 'yyyy') = '"
				+ (nianf-1)
				+ "'and ran.diancxxb_id=d.id\n"
				+ " group by (d.mingc)\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "   )\n"
				+ "\n"
				+ "      union(---电厂的预算10\n"
				+ "\n"
				+ "   select\n"
				+ "   d.mingc,j.mingc,g.mingc,\n"
				+ "\n"
				+ "\n"
				+ "      sum(caigl) caigl,sum(yuns) yuns,\n"
				+ "      sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs ,\n"
				+ "      sum(quanndhl) quanndhl ,sum(hetl) hetl,sum(daohl) daohl,sum(hetckjg) hetckjg,sum(hetrz) hetrz,  r.beiz\n"
				+ "  from ranmcgysb r,jihkjb j,gongysb g,diancxxb d\n"
				+ " where r.jihkjb_id=j.id and\n"
				+ "       r.diancxxb_id=d.id and\n"
				+ "       r.gongysb_id=g.id and\n"
				+ "\n"
				+ "     to_char(r.nianf, 'yyyy') = '"
				+ (nianf )
				+ "'\n"
				+ " group by rollup(d.mingc, j.mingc, g.mingc,r.beiz )\n"
				+
				// " having not grouping(d.mingc)=1\n" +
				" having not (grouping(d.mingc)=1 or grouping(g.mingc)+grouping(r.beiz) = 1)\n"
				+ ")\n"
				+ "\n"
				+ "\n"
				+ "\n"
				+ "    union\n"
				+ "    (\n"
				+ "select '分子公司'as  dmingc,'燃煤小计'as JIHKJB_ID ,'"
				+ (nianf-1)
				+ "年"
				+ qisyf
				+ "-"
				+ zhongzyf
				+ "月"
				+ "' as gmingc,sum(caigl) caigl,sum(yuns) yuns, sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs ,0 as quanndhl,0 as hetl,0 as daohl,0 as hetckjg,0 as hetrz,''  as beiz from\n"
				+ "(\n"
				+ "select d.mingc as dmingc,'' xiaoj,\n"
				+ "       '' gmingc,\n"
				+ "       sum(r1.caigl) as caigl,\n"
				+ "       sum(r1.yuns)  as yuns,\n"
				+ "       sum(r1.daocrz)  as daocrz,\n"
				+ "       sum(r1.chukjg)  as chukjg,\n"
				+ "       sum(r1.yunf)  as yunf,\n"
				+ "       sum(r1.zaf)  as zaf,\n"
				+ "       sum(r1.qitfy)  as qitfy,\n"
				+ "      sum(r1.daoczhjhs)  as daoczhjhs,\n"
				+ "      sum( r1.daoczhjbhs)  as daoczhjbhs,\n"
				+ "      sum( r1.daocbmdjbhs)  as daocbmdjbhs,0 as quanndhl,0 as hetl,0 as daohl,0 as hetckjg,0 as hetrz,'' as beiz\n"
				+ "  from ranmzbsjb r1,  diancxxb d, chaxtjb c\n"
				+ " where\n"
				+ "   r1.diancxxb_id = d.id and\n"
				+ "\n"
				+ "    d.fuid = 108   and r1.chaxtjb_id=c.id and r1.chaxtjb_id="
				+ iid
				+ "\n"
				+ "\n"
				+ " group by (d.mingc,caigl,yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs)\n"
				+ ")\n"
				+ "   group by (dmingc,caigl,yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs)\n"
				+ "   )\n"
				+ "   union\n"
				+ "  ( select '分子公司'as  dmingc,'燃煤小计'as xiaoj,'"
				+ (nianf-1)
				+ "年预计' as gmingc,sum(caigl) caigl,sum(yuns) yuns, sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs ,\n"
				+ "     sum(quanndhl) quanndhl ,sum(hetl) hetl,sum(daohl) daohl,sum(hetckjg) hetckjg,sum(hetrz) hetrz,''as beiz  from\n"
				+ "   (\n"
				+ "   select\n"
				+ "       d.mingc dmingc,\n"
				+ "       g.mingc gmingc,\n"
				+ "       nvl(r2.caigl,0)  caigl,\n"
				+ "       nvl(r2.yuns,0)  yuns,\n"
				+ "       nvl(r2.daocrz,0)  daocrz,\n"
				+ "       nvl(r2.chukjg,0)  chukjg,\n"
				+ "       nvl(r2.yunf,0)  yunf,\n"
				+ "       nvl(r2.zaf,0)  zaf,\n"
				+ "       nvl(r2.qitfy,0)  qitfy,\n"
				+ "       nvl(r2.daoczhjhs,0)  daoczhjhs,\n"
				+ "       nvl(r2.daoczhjbhs,0)  daoczhjbhs,\n"
				+ "       nvl(r2.daocbmdjbhs,0)  daocbmdjbhs,nvl(r2.quanndhl,0) quanndhl ,nvl(r2.hetl,0) hetl ,nvl(r2.daohl,0) daohl ,nvl(r2.hetckjg,0) hetckjg ,nvl(r2.hetrz,0) hetrz ,r2.beiz beiz\n"
				+ "    from ranmcgysb r2, gongysb g, diancxxb d\n"
				+ "    where\n"
				+ "   r2.diancxxb_id = d.id\n"
				+ "   and r2.gongysb_id = g.id\n"
				+ "   and d.fuid = 108\n"
				+ "   and to_char(r2.nianf,'yyyy')='"
				+ (nianf-1)
				+ "'\n"
				+ "\n"
				+ "    group by (d.mingc,g.mingc ,caigl,yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs,quanndhl,hetl,daohl,hetckjg,hetrz)\n"
				+ "   )\n"
				+ "\n"
				+ "\n"
				+ "   )\n"
				+ "   union\n"
				+ "   (\n"
				+ "   select '分子公司'as  dmingc,'燃煤小计' as xiaoj,'"
				+ (nianf )
				+ "年预算'as gmingc,sum(caigl) caigl,sum(yuns) yuns, sum(daocrz) daocrz,sum (chukjg) chukjg,sum(yunf) yunf,sum(zaf) zaf, sum(qitfy) qitfy,sum(daoczhjhs) daoczhjhs,sum(daoczhjbhs) daoczhjbhs,sum(daocbmdjbhs) daocbmdjbhs ,\n"
				+ "     sum(quanndhl) quanndhl ,sum(hetl) hetl,sum(daohl) daohl,sum(hetckjg) hetckjg,sum(hetrz) hetrz,''as beiz  from\n"
				+ "   (\n"
				+ "   select\n"
				+ "       d.mingc dmingc,\n"
				+ "       g.mingc gmingc,\n"
				+ "        nvl(r2.caigl,0)  caigl,\n"
				+ "       nvl(r2.yuns,0)  yuns,\n"
				+ "       nvl(r2.daocrz,0)  daocrz,\n"
				+ "       nvl(r2.chukjg,0)  chukjg,\n"
				+ "       nvl(r2.yunf,0)  yunf,\n"
				+ "       nvl(r2.zaf,0)  zaf,\n"
				+ "       nvl(r2.qitfy,0)  qitfy,\n"
				+ "       nvl(r2.daoczhjhs,0)  daoczhjhs,\n"
				+ "       nvl(r2.daoczhjbhs,0)  daoczhjbhs,\n"
				+ "       nvl(r2.daocbmdjbhs,0)  daocbmdjbhs,nvl(r2.quanndhl,0) quanndhl ,nvl(r2.hetl,0) hetl ,nvl(r2.daohl,0) daohl ,nvl(r2.hetckjg,0) hetckjg ,nvl(r2.hetrz,0) hetrz ,r2.beiz beiz\n"
				+ "    from ranmcgysb r2, gongysb g, diancxxb d\n"
				+ "    where\n"
				+ "   r2.diancxxb_id = d.id\n"
				+ "   and r2.gongysb_id = g.id\n"
				+ "   and d.fuid = 108\n"
				+ "   and to_char(r2.nianf,'yyyy')='"
				+ (nianf)
				+ "'\n"
				+ "\n"
				+ "    group by (d.mingc,g.mingc ,caigl,yuns,daocrz,chukjg,yunf,zaf,qitfy,daoczhjhs,daoczhjbhs,daocbmdjbhs,quanndhl,hetl,daohl,hetckjg,hetrz)\n"
				+ "   )\n"
				+ "\n"
				+ "   )\n"
				+ ")\n"
				+ "\n"
				+ "order by decode(dmingc,'分子公司',1,2),dmingc,decode(jihkjb_id,'燃煤小计',1,'',2,3),jihkjb_id,decode(gmingc,'"
				+ (nianf-1) + "年" + qisyf + "-" + zhongzyf
				+ "月" + "',1,'" + (nianf-1) + "年预计',2,'',3,4),gmingc";
		
		ResultSet  rs=null;
//		if(rs1.getRow()>0){
			
			rs = con.getResultSet(sql);
//		}else{
//			rs=con.getResultSet("select '无' as d from dual");
//		}
//			System.out.println(sql);
		Report rt = new Report();

		String ArrHeader[][] = new String[3][19];
		ArrHeader[0] = new String[] { "单位名称", "矿别及地区", "矿别及地区", "采购预算", "采购预算",
				"采购预算", "采购预算", "采购预算", "采购预算", "采购预算", "采购预算", "采购预算", "采购预算",
				"09年订货及价格情况", "09年订货及价格情况", "09年订货及价格情况", "09年订货及价格情况",
				"09年订货及价格情况", "09年订货及价格情况" };
		ArrHeader[1] = new String[] { "单位名称", "矿别及地区", "矿别及地区", "采购量", "运损",
				"到厂热值", "出矿价格", "运费", "杂费", "其它费用", "到厂综合价格<br>(含税)",
				"到场综合价<br>(不含税)", "到厂标煤单价<br>(不含税)", "全年到货量", "合同量", "到货率",
				"合同<br>出矿价格", "合同热值", "备注" };
		ArrHeader[2] = new String[] { "单位名称", "矿别及地区", "矿别及地区", "万吨", "万吨",
				"KJ/kg", "元/吨", "元/吨", "元/吨", "元/吨", "元/吨", "元/吨", "元/吨", "万吨",
				"万吨", "％", "万吨", "KJ/kg", "备注" };

		int ArrWidth[] = new int[] { 100, 70, 70, 70, 70, 70, 70, 70, 60, 60,
				60, 60, 60, 60, 60, 60, 60, 60, 60 };

		Table bt = new Table(rs, 3, 0, 19);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);

		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle((nianf + 1) + "燃煤预算报表", ArrWidth);// getBiaotmc()+

		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt
				.setDefaultTitle(1, 2, "制表单位:"
						+ ((Visit) getPage().getVisit()).getDiancqc(),
						Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, riq, Table.ALIGN_CENTER);
		rt.body.setPageRows(21);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3,
				"打印日期:"
						+ FormatDate(DateUtil.getDate(DateUtil
								.FormatDate(new Date()))), Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 4, "审核人：", Table.ALIGN_CENTER);
		rt.setDefautlFooter(8, 4, "填报人：", Table.ALIGN_RIGHT);

		rt.body.mergeFixedRow();// 合并行
		rt.body.mergeFixedCols();// 和并列
		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		return rt.getAllPagesHtml();

	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

//	private boolean _ReturnChick = false;
//
//	public void ReturnButton(IRequestCycle cycle) {
//		_ReturnChick = true;
//	}
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
//		if (_ReturnChick) {
//			_ReturnChick = false;
//			cycle.activate("Chaxtj");
//		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}

	// /////
	public String getBeginriqDate() {
		if (((Visit) getPage().getVisit()).getString4() == null
				|| ((Visit) getPage().getVisit()).getString4() == "") {
			Calendar stra = Calendar.getInstance();
			// stra.set(DateUtil.getYear(new Date()), 0, 1);
			stra.setTime(new Date());
			stra.add(Calendar.DATE, -1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra
					.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setBeginriqDate(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

//		Visit visit = (Visit) getPage().getVisit();
//		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,
//				"diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
//		visit.setDefaultTree(dt);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long
//				.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1"
//						: getTreeid())));
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF1");
		nianf.setId("NIANF1");// 和自动刷新绑定
		nianf.setLazyRender(true);
		nianf.setEditable(false);
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("查询条件:"));
		ComboBox chaxtj = new ComboBox();
		chaxtj.setTransform("CHAXTJB_IDDropDown");
		chaxtj.setWidth(150);
		chaxtj.setListeners("select:function(){document.Form0.submit();}");
		chaxtj.setLazyRender(true);
		chaxtj.setEditable(false);
		tb1.addField(chaxtj);
		tb1.addText(new ToolbarText("-"));
//		ToolbarButton tb3 = new ToolbarButton(null,"返回","function(){document.getElementById('ReturnButton').click();}");
//		tb1.addItem(tb3);
		setToolbar(tb1);
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean1(true);
			return;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);

			this.setNianf1Value(null);
			this.getNianf1Models();

			this.setTreeid(null);
			this.getTreeid();
			visit.setString4(null);
		}
		getToolbars();

		blnIsBegin = true;

	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		if (treeid != null) {
			if (!treeid.equals(this.treeid)) {
				((TextField) getToolbar().getItem("diancTree_text"))
						.setValue(((IDropDownModel) getDiancmcModel())
								.getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
						.setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}

//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}

	// 电厂名称
	private IPropertySelectionModel _IDiancModel;

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	private boolean _DiancmcChange = false;

	public IDropDownBean getDiancmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancmcModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == Value) {
			_DiancmcChange = false;
		} else {
			_DiancmcChange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
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
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
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

	// 开始 年份
	private static IPropertySelectionModel _Nianf1Model;

	public IPropertySelectionModel getNianf1Model() {
		if (_Nianf1Model == null) {
			getNianf1Models();
		}
		return _Nianf1Model;
	}

	private IDropDownBean _Nianf1Value;

	public IDropDownBean getNianf1Value() {
		if (_Nianf1Value == null || _Nianf1Value.equals("")) {
			for (int i = 0; i < _Nianf1Model.getOptionCount(); i++) {
				Object obj = _Nianf1Model.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_Nianf1Value = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _Nianf1Value;
	}

	public boolean nianfchanged;

	public void setNianf1Value(IDropDownBean Value) {
		if (_Nianf1Value != Value) {
			nianfchanged = true;
		}
		_Nianf1Value = Value;
	}

	public IPropertySelectionModel getNianf1Models() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_Nianf1Model = new IDropDownModel(listNianf);
		return _Nianf1Model;
	}

	public void setNianf1Model(IPropertySelectionModel _value) {
		_Nianf1Model = _value;
	}
	
////	 开始 年份
//	private static IPropertySelectionModel _CHAXTJB_IDModel;
//
//	public IPropertySelectionModel getCHAXTJB_IDModel() {
//		if (_CHAXTJB_IDModel == null) {
//			getCHAXTJB_IDModels();
//		}
//		return _CHAXTJB_IDModel;
//	}
//
//	private IDropDownBean _CHAXTJB_IDValue;
//
//	public IDropDownBean getCHAXTJB_IDValue() {
//		if (_CHAXTJB_IDValue == null || _CHAXTJB_IDValue.equals("")) {
//			for (int i = 0; i < _CHAXTJB_IDModel.getOptionCount(); i++) {
//				Object obj = _CHAXTJB_IDModel.getOption(i);
//				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
//						.getId()) {
//					_CHAXTJB_IDValue = (IDropDownBean) obj;
//					break;
//				}
//			}
//		}
//		return _CHAXTJB_IDValue;
//	}
//
//	public boolean nianfchanged1;
//
//	public void setCHAXTJB_IDValue(IDropDownBean Value) {
//		if (_CHAXTJB_IDValue != Value) {
//			nianfchanged1 = true;
//		}
//		_CHAXTJB_IDValue = Value;
//	}
//
//	public IPropertySelectionModel getCHAXTJB_IDModels() {
//		List listNianf = new ArrayList();
//		int i;
//		String GongysSql = "select id,mingc from chaxtjb ";
////		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
////			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
////		}
//		setCHAXTJB_IDModel(new IDropDownModel(GongysSql,"请选择"));
////		_CHAXTJB_IDModel = new IDropDownModel(GongysSql);
//		return _CHAXTJB_IDModel;
//	}
//
//	public void setCHAXTJB_IDModel(IPropertySelectionModel _value) {
//		_CHAXTJB_IDModel = _value;
//	}
	
	
//	 供应商
	public IDropDownBean getCHAXTJB_IDDropDownValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getCHAXTJB_IDDropDownModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setCHAXTJB_IDDropDownValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setCHAXTJB_IDDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getCHAXTJB_IDDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getCHAXTJB_IDDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getCHAXTJB_IDDropDownModels() {
		String sql = "select id,mingc\n" + "from CHAXTJB\n"
			;
		// +"where gongysb.fuid=0";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "请选择"));
		return;
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

}
