package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Rucrlrzc_dk extends BasePage {
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
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean blnIsBegin = false;

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");
		boolean qusly = false;
		String Qufdxc = "select zhi from xitxxb where mingc = '入厂入炉热值差是否从rezcb取数据' and zhuangt = 1";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc);
		if (Daxcrs.next()) {
			String zhi = Daxcrs.getString("zhi");
			if (zhi.equals("是")) {
				qusly = true;
			}
		}
		
		boolean zhib = false;
		String Zhibsql = "select zhi from xitxxb where mingc = '入厂入炉热值差是否显示其他指标' and zhuangt = 1";
		ResultSetList Xianszb = con.getResultSetList(Zhibsql);
		if (Xianszb.next()) {
			String zhi = Xianszb.getString("zhi");
			if (zhi.equals("是")) {
				zhib = true;
			}
		}
				
		if (qusly) {
			buffer.append(
				"select decode(r.riq, null, '累计', to_char(r.riq, 'yyyy-mm-dd')) as riq, sum(r.rucsl) as rucsl,\n" +
				"       decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl), 2)) as rucrl,\n" + 
				"       decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl) * 1000 / 4.1816,0)) as rucrldk,\n" + 
				"		decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucsf) / sum(r.rucsl), 1)) as rucsf,\n" + 
				"		sum(r.rulsl) as rulsl,\n" + 
				"		decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl), 2)) as rulrl,\n" + 
				"  		decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl) * 1000 / 4.1816, 0)) as rulrldk,\n" + 
				"		decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulsf) / sum(r.rulsl), 1)) as rulsf,\n" + 
				"		nvl(decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl), 2)), 0) - nvl(decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl), 2)), 0) as rezctzq,\n" + 
				"		decode(sum(r.rulsl), 0, 0, round_new((nvl(decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl), 2)), 0) - nvl(decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl), 2)), 0)) * 1000 / 4.1816, 0)) as rezcdk,\n" + 
				"		nvl(decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl), 2)), 0) - round_new(decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl), 2)) * (100 - decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucsf) / sum(r.rucsl), 1))) / (100 - decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulsf) / sum(r.rulsl), 1))), 2) as rezctzh,\n" + 
				"		decode(sum(r.rulsl), 0, 0, round_new((nvl(decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucrl) / sum(r.rucsl), 2)), 0) - round_new(decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulrl) / sum(r.rulsl), 2)) * (100 - decode(sum(r.rucsl), 0, 0, round_new(sum(r.rucsl * r.rucsf) / sum(r.rucsl), 1))) / (100 - decode(sum(r.rulsl), 0, 0, round_new(sum(r.rulsl * r.rulsf) / sum(r.rulsl), 1))), 2)) * 1000 / 4.1816, 0)) as rezcdk\n" + 
				"  from rezcb r, diancxxb dc \n" +
				" where r.riq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd')\n" + 
				"	and r.riq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"	and r.diancxxb_id = dc.id\n" + 
				"	and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				" group by rollup (r.riq) \n" +
				" order by r.riq");
			
		} else if (zhib) {
			buffer.append(
				"select riq, laimsl,\n" +
				"		qnet_ar,\n" + 
				"		round_new(qnet_ar / 0.0041816,0) qnet_ar1,--\n" + 
				"		mt,\n" + 
				"		star,\n" + 
				"		aar,\n" + 
				"		vdaf,\n" + 
				"		meil,\n" + 
				"		qnet_arl,\n" + 
				"		round_new(qnet_arl / 0.0041816,0) qnet_arl1,--\n" + 
				"		mtl,\n" + 
				"		star1,\n" + 
				"		aar1,\n" + 
				"		vdaf1,\n" + 
				"		qnet_ar - qnet_arl rzc,--\n" + 
				"		round_new(qnet_ar / 0.0041816, 0) - round_new(qnet_arl / 0.0041816, 0) rzc1,--\n" + 
				"		round_new(qnet_ar - qnet_arl * (100 - mt) / (100 - mtl), 2) sfc,--\n" + 
				"		round_new(qnet_ar / 0.0041816 - qnet_arl / 0.0041816 * (100 - mt) / (100 - mtl), 0) sfc1--调整后热值差=round(入厂热量 -入炉热量 * (100 -入厂水分) / (100 - 入炉水分),2)\n" + 
				"\n" + 
				"  from\n" + 
				/*
				*huochaoyuan
				*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
				*/		
				"(select decode(grouping(fahb.daohrq), 1, '累计', to_char(fahb.daohrq, 'yyyy-mm-dd')) daohrq, " +
				"		 sum(round_new(fahb.laimsl, 0)) laimsl, " +
				"		 round_new(sum(round_new(zhilb.qnet_ar, 2) * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 2) qnet_ar,\n" + 
				// end		 
				"		 round_new(sum(zhilb.mt * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 1)mt,\n" + 
				"		 round_new(sum(zhilb.star * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 1)star,\n" +
				"		 round_new(sum(zhilb.aar * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 1)aar,\n" + 
				"		 round_new(sum(zhilb.vdaf * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 1)vdaf\n"+
				"	from fahb, zhilb, diancxxb dc\n" + 
				"  where fahb.laimsl <> 0 " +
				"    and fahb.zhilb_id = zhilb.id \n" + 
				"	 and fahb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"    and fahb.daohrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"    and fahb.daohrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"  group by rollup(fahb.daohrq)\n" + 
				")rucm,\n" + 
				/*
				*huochaoyuan
				*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
				*/			
				"(select decode(grouping(rulmzlb.rulrq), 1, '累计', to_char(rulmzlb.rulrq, 'yyyy-mm-dd')) rulrq, " +
				"		 sum(round_new(rulmzlb.meil, 0)) meil, " +
				" 		 round_new(sum(round_new(rulmzlb.qnet_ar, 2) * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.qnet_ar, 0), 0, 0, rulmzlb.meil), 0)), 2) qnet_arl,\n" + 
				//	end		 
				"		 round_new(sum(rulmzlb.mt * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.mt, 0), 0, 0, rulmzlb.meil), 0)), 1) mtl,\n" +
				"		 round_new(sum(((100 - rulmzlb.mt) * rulmzlb.stad / (100 - rulmzlb.mad)) * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(((100 - rulmzlb.mt) * rulmzlb.stad / (100 - rulmzlb.mad)), 0), 0, 0, rulmzlb.meil), 0)), 1) star1,\n" +
				"		 round_new(sum(rulmzlb.aar * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.aar, 0), 0, 0, rulmzlb.meil), 0)), 1) aar1,\n" + 
				"		 round_new(sum(rulmzlb.vdaf * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.vdaf, 0), 0, 0, rulmzlb.meil), 0)), 1) vdaf1\n"+
				"	from rulmzlb, diancxxb dc\n" + 
				"  where rulmzlb.meil <> 0\n" + 
				"	 and rulmzlb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")" + 
				"	 and rulmzlb.rulrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"	 and rulmzlb.rulrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"  group by rollup(rulmzlb.rulrq)\n" + 
				")rulm,\n" + 
				"(select distinct to_char(rulmzlb.rulrq, 'yyyy-mm-dd') riq " +
				"   from rulmzlb, diancxxb dc\n" +
				"  where rulmzlb.rulrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " + 
				"	 and rulmzlb.rulrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd') " + 
				"	 and rulmzlb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"	 and rulmzlb.shenhzt = 3 " +
				"    and rulmzlb.meil <> 0\n" + 
				"union\n" + 
				"select distinct to_char(daohrq, 'yyyy-mm-dd') " +
				"  from fahb, zhilb, diancxxb dc\n" + 
				" where fahb.laimsl <> 0 " +
				"   and fahb.zhilb_id = zhilb.id \n" + 
				"	and fahb.diancxxb_id = dc.id\n" + 
				"	and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")" + 
				"	and fahb.daohrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"	and fahb.daohrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"union\n" + 
				"(select '累计' from dual)\n" + 
				")biaot\n" + 
				"where biaot.riq = rucm.daohrq(+) " +
				"  and biaot.riq = rulm.rulrq(+)\n" + 
				"order by decode(riq, '累计', 0, 1), riq");
		} else {
			buffer.append(
				"select riq, laimsl,\n" +
				"		qnet_ar,\n" + 
				"		round_new(qnet_ar / 0.0041816,0) qnet_ar1,--\n" + 
				"		mt,\n" + 
//				"		star,\n" + 
//				"		aar,\n" + 
//				"		vdaf,\n" + 
				"		meil,\n" + 
				"		qnet_arl,\n" + 
				"		round_new(qnet_arl / 0.0041816,0) qnet_arl1,--\n" + 
				"		mtl,\n" + 
//				"		star1,\n" + 
//				"		aar1,\n" + 
//				"		vdaf1,\n" + 
				"		qnet_ar - qnet_arl rzc,--\n" + 
				"		round_new(qnet_ar / 0.0041816, 0) - round_new(qnet_arl / 0.0041816, 0) rzc1,--\n" + 
				"		round_new(qnet_ar - qnet_arl * (100 - mt) / (100 - mtl), 2) sfc,--\n" + 
				"		round_new(qnet_ar / 0.0041816 - qnet_arl / 0.0041816 * (100 - mt) / (100 - mtl), 0) sfc1--调整后热值差=round(入厂热量 -入炉热量 * (100 -入厂水分) / (100 - 入炉水分),2)\n" + 
				"\n" + 
				"  from\n" + 
				/*
				*huochaoyuan
				*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
				*/		
				"(select decode(grouping(fahb.daohrq), 1, '累计', to_char(fahb.daohrq, 'yyyy-mm-dd')) daohrq, " +
				"		 sum(round_new(fahb.laimsl, 0)) laimsl, " +
				"		 round_new(sum(round_new(zhilb.qnet_ar, 2) * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 2) qnet_ar,\n" + 
				//	end		 
				"		 round_new(sum(zhilb.mt * round_new(fahb.laimsl, 0)) / sum(round_new(fahb.laimsl, 0)), 1) mt\n" + 
//				"round_new(sum(zhilb.star*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)star,\n" +
//				"round_new(sum(zhilb.aar*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)aar,\n" + 
//				"round_new(sum(zhilb.vdaf*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)vdaf\n"+
				"   from fahb, zhilb, diancxxb dc\n" + 
				"  where fahb.laimsl <>0 " +
				"    and fahb.zhilb_id = zhilb.id \n" + 
				"	 and fahb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"    and fahb.daohrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"	 and fahb.daohrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"  group by rollup(fahb.daohrq)\n" + 
				")rucm,\n" + 
				/*
				*huochaoyuan
				*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
				*/			
				"(select decode(grouping(rulmzlb.rulrq), 1, '累计', to_char(rulmzlb.rulrq, 'yyyy-mm-dd')) rulrq, " +
				"		 sum(round_new(rulmzlb.meil, 0)) meil, " +
				"		 round_new(sum(round_new(rulmzlb.qnet_ar, 2) * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.qnet_ar, 0), 0, 0, rulmzlb.meil), 0)), 2) qnet_arl,\n" + 
				// end		 
				"		 round_new(sum(rulmzlb.mt * round_new(rulmzlb.meil, 0)) / sum(round_new(decode(nvl(rulmzlb.mt, 0), 0, 0, rulmzlb.meil), 0)), 1) mtl\n" +
//				"		 round_new(sum(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad))*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0)),1) star1,\n" +
//				"		 round_new(sum(rulmzlb.aar*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.aar,0),0,0,rulmzlb.meil),0)),1)aar1,\n" + 
//				"		 round_new(sum(rulmzlb.vdaf*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.vdaf,0),0,0,rulmzlb.meil),0)),1)vdaf1\n"+
				"	from rulmzlb, diancxxb dc\n" + 
				"  where rulmzlb.meil <> 0\n" + 
				"	 and rulmzlb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"    and rulmzlb.rulrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"	 and rulmzlb.rulrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"group by rollup(rulmzlb.rulrq)\n" + 
				")rulm,\n" + 
				"(select distinct to_char(rulmzlb.rulrq, 'yyyy-mm-dd') riq " +
				"   from rulmzlb, diancxxb dc\n" +
				"  where rulmzlb.rulrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"    and rulmzlb.rulrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd') " + 
				"	 and rulmzlb.diancxxb_id = dc.id\n" + 
				"	 and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"	 and rulmzlb.shenhzt = 3 " +
				"    and rulmzlb.meil <> 0\n" + 
				"union\n" + 
				"select distinct to_char(daohrq, 'yyyy-mm-dd') " +
				"  from fahb, zhilb, diancxxb dc\n" + 
				" where fahb.laimsl <> 0 " +
				"   and fahb.zhilb_id = zhilb.id \n" + 
				"	and fahb.diancxxb_id = dc.id\n" + 
				"	and (dc.id = " + getTreeid() + " or dc.fuid = " + getTreeid() + ")\n" + 
				"   and fahb.daohrq >= to_date('" + getRiq1() + "', 'yyyy-mm-dd') " +
				"	and fahb.daohrq <= to_date('" + getRiq2() + "', 'yyyy-mm-dd')\n" + 
				"union\n" + 
				"(select '累计' from dual)\n" + 
				") biaot\n" + 
				"where biaot.riq = rucm.daohrq (+) " +
				"  and biaot.riq = rulm.rulrq(+)\n" + 
				"order by decode(riq, '累计', 0, 1), riq");
		}
		
		ResultSet rs = con.getResultSet(buffer, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		if (zhib) {
			ArrHeader = new String[3][19];
			ArrWidth = new int[] {70, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50};
			ArrHeader[0] = new String[] { "日期", "入厂", "入厂", "入厂", "入厂", "入厂",
					"入厂", "入厂", "入炉", "入炉", "入炉", "入炉", "入炉", "入炉", "入炉",
					"热值差", "热值差", "热值差", "热值差" };
			ArrHeader[1] = new String[] { "日期", "数量", "热值", "热值", "水分", "硫份",
					"灰份", "挥发份", "数量", "热值", "热值", "水分", "硫份", "灰份", "挥发份",
					"水分调整前", "水分调整前", "水分调整后", "水分调整后" };
			ArrHeader[2] = new String[] { "日期", "(吨)", "(MJ/kg)", "(kcal/kg)",
					"Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)", "(吨)", "(MJ/kg)",
					"(kcal/kg)", "Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)",
					"(MJ/kg)", "(kcal/Kg)", "(MJ/kg)", "(kcal/Kg)" };
			rt.setBody(new Table(rs, 3, 0, 1));
			//
			int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
					.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
					.getString1());// 取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("入 厂 入 炉 热 值 差", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 6, "制表单位:" + ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 10, "单位:(吨)", Table.ALIGN_RIGHT);
			// rt.setDefaultTitle(6, 2,
			// "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
			rt.body.setPageRows(Report.PAPER_COLROWS);
			// 增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.ShowZero = true;
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 19, "打印日期:" + DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
			// 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			con.Close();
			return rt.getAllPagesHtml();
		} else {
			ArrHeader = new String[3][13];
			ArrWidth = new int[] {80, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60};
			ArrHeader[0] = new String[] { "日期", "入厂", "入厂", "入厂", "入厂", "入炉",
					"入炉", "入炉", "入炉", "热值差", "热值差", "热值差", "热值差" };
			ArrHeader[1] = new String[] { "日期", "数量", "热值", "热值", "水分", "数量",
					"热值", "热值", "水分", "水分调整前", "水分调整前", "水分调整后", "水分调整后" };
			ArrHeader[2] = new String[] { "日期", "(吨)", "(MJ/kg)", "(kcal/kg)",
					"Mt(%)", "(吨)", "(MJ/kg)", "(kcal/kg)", "Mt(%)", "(MJ/kg)",
					"(kcal/kg)", "（MJ/kg）", "(kcal/kg)" };
			rt.setBody(new Table(rs, 3, 0, 1));
			//
			int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
					.getDiancxxb_id(), ((Visit) this.getPage().getVisit())
					.getString1());// 取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);// 添加报表的A4控制
			rt.body.setWidth(ArrWidth);
			rt.body.setHeaderData(ArrHeader);
			rt.setTitle("入 厂 入 炉 热 值 差", ArrWidth);
			rt.title.setRowHeight(2, 50);
			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
			rt.setDefaultTitle(1, 5, "制表单位:" + ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 10, "单位:(吨)", Table.ALIGN_RIGHT);
			// rt.setDefaultTitle(6, 2,
			// "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
			rt.body.setPageRows(Report.PAPER_COLROWS);
			// 增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.ShowZero = true;
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 19, "打印日期:" + DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
			// 设置页数
			_CurrentPage = 1;
			_AllPages = rt.body.getPages();
			if (_AllPages == 0) {
				_CurrentPage = 0;
			}
			con.Close();
			return rt.getAllPagesHtml();
		}
	}
	
	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this) + "';";
		} else {
			return "";
		}
	}
	
	boolean riqichange = false;

	private String riq1;

	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			riq1 = DateUtil.FormatDate(new Date());
		}
		return riq1;
	}

	public void setRiq1(String riq) {
		if (this.riq1 != null && !this.riq1.equals(riq)) {
			this.riq1 = riq;
			riqichange = true;
		}
	}
	
	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq) {
		if (this.riq2 != null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	
	private void getToolbars(){
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		// 电厂树
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win, "diancTree", "" + visit.getDiancxxb_id(), "", null, getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(getTreeid() == null || "".equals(getTreeid()) ? "-1" : getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null, null, "function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("电厂:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("入炉日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText(" 至 "));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null, "刷新", "function(){document.Form0.submit();}");
		tb1.addItem(tb);
		setToolbar(tb1);
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		visit.setboolean1(false);
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
			// visit.setDefaultTree(null);
			setDiancmcModels();
			setDiancjb();

			// setTreeid(null);
			// setTreeid_dc(visit.getDiancxxb_id() + "");
		}

		// begin方法里进行初始化设置
		visit.setString1(null);

		String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
		if (pagewith != null) {
			visit.setString1(pagewith);
		}
		// visit.setString1(null);保存传递的非默认纸张的样式
		getToolbars();
		blnIsBegin = true;
	}

    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
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

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			setDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setDiancmcModels() {
		String sql = "select id, mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}
	
	public IPropertySelectionModel Diancjb;

	public void setDiancjb() {
		String sql = "select id,jib from diancxxb";
		Diancjb = new IDropDownModel(sql);
	}
}