package com.zhiren.gangkjy.zhilgl.huayxx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者:童忠付
 * 时间:2009-3-30
 * 内容:质量管理 入场化验报告
 */
public class Ruchybg extends BasePage implements PageValidateListener {

	public boolean getRaw() {
		return true;
	}

	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
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

	public String getPrintTable() {
		return getHuaybgd();
	}
	
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
		
		String zhuangcjssj="";
		if(this.getRiq()!=null && !this.getRiq().equals("")){
			zhuangcjssj+=" z.huaysj="+DateUtil.FormatOracleDate(getRiq());
		}
		
		String huaybh=" z.id='"+this.getBianmValue().getId()+"'";  //化验编号唯一吗?  或者换成是 对应的id?
		
		
		
		buffer.append(
				"select  fhdw,\n" +
				"        mkdw,\n" + 
				"        daohrq,\n" + 
				"		 fahrq,\n"+
				"        pz,\n" + 
				"       fz,\n" + 
				"		 beiz,\n"+
				"        jingz,\n" + 
				"		 biaoz,\n"+
				"		 yuns,\n"+
				"		 yingk,\n"+
				"       ches,\n" + 
				"       nvl(mt,'') mt,\n" + 
				"       nvl(mad,'') mad,\n" + 
				"       nvl(aad,'') aad,\n" + 
				"       nvl(ad,'') ad,\n" + 
				"       nvl(aar,'') aar,\n" + 
				"       nvl(vad,'') vad,\n" + 
				"       nvl(vdaf,'') vdaf,\n" + 
				"       nvl(qbad*1000,'') qbad,\n" + 
				"       nvl(farl*1000,'') farl,\n" + 
				"       nvl(qbar,'') qbar,\n" + 
				
				"       nvl(sdaf,'') sdaf,\n" + 
				"		nvl(stad,'') stad,\n"+
				"       nvl(std,'') std,\n" + 
				"       nvl(star,'') star,\n" + 
				"       nvl(hdaf,'') hdaf,\n" + 
				"       nvl(had,'') had,\n" + 
				"       nvl(fcad,'') fcad,\n" + 
				"       nvl(qgrd,'') qgrd \n" + 
				"  from (select nvl(g.mingc,' ') as fhdw,\n" + 
				"               nvl(m.mingc,' ') as mkdw,\n" + 
				
				"               nvl(to_char(f.daohrq, 'yyyy-mm-dd'),'') daohrq,\n " + 
			
				"				nvl(to_char(f.fahrq, 'yyyy-mm-dd'),'') fahrq,\n"+
				"               nvl(p.mingc,' ') pz,\n" + 
				"               nvl(c.mingc,' ') fz,\n" + 
				"				nvl(f.beiz,' ') beiz,\n"+
				"               sum(round_new(f.laimsl,"+v.getShuldec()+")) jingz,\n" + 
				"               sum(round_new(f.biaoz,"+v.getShuldec()+")) biaoz,\n" + 
				"               sum(round_new(f.yuns,"+v.getShuldec()+")) yuns,\n" + 
				"               sum(round_new(f.yingk,"+v.getShuldec()+")) yingk,\n" + 
				"               sum(round_new(f.zongkd,"+v.getShuldec()+")) zongkd,\n" + 
				"               sum(round_new(f.ches,"+v.getShuldec()+")) ches,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.mt,"+v.getMtdec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getMtdec()+")) as mt,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.mad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as mad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.ad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as ad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.aar * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as aar,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.vdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as vdaf,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qbad,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as qbad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) /\n" + 
				"                                          sum(round_new(f.laimsl,"+v.getShuldec()+"))\n" + 
				"                                           * 1000 / 4.1816,\n" + 
				"                                0)) as qbar,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.qnet_ar,"+v.getFarldec()+") * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), "+v.getFarldec()+")) as farl,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.sdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as sdaf,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.stad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as stad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.std * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as std,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2) * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as star,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.hdaf * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as hdaf,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.had * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as had,\n" + 
				
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.fcad * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as fcad,\n" + 
				"               decode(sum(round_new(f.laimsl,"+v.getShuldec()+")),\n" + 
				"                      0,\n" + 
				"                      0,\n" + 
				"                      round_new(sum(z.qgrd * round_new(f.laimsl,"+v.getShuldec()+")) / sum(round_new(f.laimsl,"+v.getShuldec()+")), 2)) as qgrd,\n" + 
				"               decode(grouping(g.mingc), 1, 0, nvl(max(g.xuh), 99999)) dx,\n" + 
				"               decode(grouping(m.mingc), 1, 0, nvl(max(m.xuh), 99999)) mx\n" + 
				"          from fahb f, vwfahr g, meikxxb m, vwpinz p, vwchez c, zhillsb z,caiyb ca,caiylbb cb, diancxxb dc \n" +              //质量表 还是质量临时表?
				"         where f.gongysb_id = g.id and f.diancxxb_id=dc.id \n" + 
				"           and f.meikxxb_id = m.id(+)\n" + 
				"			and ca.id=z.caiyb_id and ca.caiylbb_id=cb.id and cb.bianm='JC'\n"+
				"           and f.pinzb_id = p.id\n" + 
				"           and f.faz_id = c.id(+)\n" + 
				"           and f.zhilb_id = z.zhilb_id \n" + 
//				"           and " + zhuangcjssj+
				"           and " +huaybh+
				str+
				"           and z.huaysj >= to_date('"+getRiq()+"', 'yyyy-mm-dd')\n" + 
				"           and z.huaysj <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" + 
//				"           and f.yunsfsb_id = "+getYunsfsValue().getId()+"\n" + 
//				"			and f.diancxxb_id="+v.getDiancxxb_id()+
				"         group by g.mingc, m.mingc, f.daohrq,f.fahrq, p.mingc, c.mingc,f.beiz\n" + 
	//			"        having grouping(f.daohrq) = 1 or grouping(c.mingc) = 0\n" + 
				"         order by dx, fhdw, mx, mkdw, daohrq desc)");
			return buffer;
	}
	// 燃料采购部指标完成情况日报
	private String getHuaybgd() {
		Report rt = new Report();
		JDBCcon con = new JDBCcon();
		StringBuffer sqlHuaybgd = new StringBuffer();
		sqlHuaybgd=this.getBaseSql();
//		System.out.println(sqlHuaybgd.toString());
		ResultSet rs = con.getResultSet(sqlHuaybgd);
		String shangjshry = "";
		String yingk = "";
		String beiz="";
		String[][] ArrHeader = new String[22][6];
		try {
			if (rs.next()) {
				yingk=rs.getString("YINGK");
				beiz=rs.getString("BEIZ");
//				StringBuffer buffer = new StringBuffer();
//				String cheph = rs.getString("CHEPH");
//				String[] list = cheph.split(",");
//				for (int i = 1; i <= list.length; i++) {
//					if (i % 4 == 0) {
//						buffer.append(list[i - 1] + ",<br>");
//					} else {
//						buffer.append(list[i - 1] + ",");
//					}
//				}
//				cheph = buffer.toString().substring(0, buffer.length() - 1);
		
				ArrHeader[0] = new String[] { Local.gongysb_id_fahb,
						"" + rs.getString("FHDW") + "", Local.meikxxb_id_fahb,
						"" + rs.getString("MKDW") + "", Local.pinz,
						"" + rs.getString("PZ") + "" };

				ArrHeader[1] = new String[] { Local.fahrq,
						"" + rs.getString("FAHRQ") + "", Local.daohrq_id_fahb,
						"" + rs.getString("DAOHRQ") + "", Local.faz,
						"" + rs.getString("FZ") + "" };
				ArrHeader[2] = new String[] { Local.huaysl_zhilb,
						"" + rs.getString("JINGZ") + "", Local.ches,
						"" + rs.getString("CHES") + "",
						Local.yuns,
						"" + rs.getString("YUNS") + "" };
				ArrHeader[3] = new String[] { Local.yingk, "" +yingk + "",
						"" + yingk + "", "" + yingk + "", "" + yingk + "",
						"" + yingk + "" };
				ArrHeader[4] = new String[] { Local.quancfbg_zhilb, Local.quancfbg_zhilb,
						Local.quancfbg_zhilb, "" + rs.getDouble("MT") + "", Local.beiz, Local.beiz };
				ArrHeader[5] = new String[] { Local.kongqgzjsfbg_zhilb,  Local.kongqgzjsfbg_zhilb,
						 Local.kongqgzjsfbg_zhilb, "" + rs.getDouble("MAD") + "", "" + beiz + "", beiz };
				ArrHeader[6] = new String[] { Local.kongqgzjhfbg_zhilb, Local.kongqgzjhfbg_zhilb,
						Local.kongqgzjhfbg_zhilb, "" + rs.getDouble("AAD") + "", "" + beiz + "", beiz };
				ArrHeader[7] = new String[] { Local.ganzjhfbg_zhilb, Local.ganzjhfbg_zhilb,
						Local.ganzjhfbg_zhilb, "" + rs.getDouble("AD") + "", "" + beiz + "", beiz };
				ArrHeader[8] = new String[] { Local.shoudjhfbg_zhilb, Local.shoudjhfbg_zhilb,
						Local.shoudjhfbg_zhilb, "" + rs.getDouble("AAR") + "","" + beiz + "", beiz };
				ArrHeader[9] = new String[] { Local.kongqgzjhffbg_zhilb,
						Local.kongqgzjhffbg_zhilb, Local.kongqgzjhffbg_zhilb,
						"" + rs.getDouble("VAD") + "", "" + beiz + "", beiz };
				ArrHeader[10] = new String[] { Local.ganzwhjhffbg_zhilb,
						Local.ganzwhjhffbg_zhilb, Local.ganzwhjhffbg_zhilb,
						"" + rs.getDouble("VDAF") + "", "" + beiz + "", beiz };
				ArrHeader[11] = new String[] { Local.tantfrlbg_zhilb,
						Local.tantfrlbg_zhilb, Local.tantfrlbg_zhilb,
						"" + rs.getDouble("QBAD") + "", "" + beiz + "", beiz };
				ArrHeader[12] = new String[] { Local.shoudjdwfrlbg, Local.shoudjdwfrlbg,
						Local.shoudjdwfrlbg, "" + rs.getDouble("FARL") + "", "" + beiz + "", beiz };
				ArrHeader[13] = new String[] { Local.shoudjdwrzbg_zhilb,
						 Local.shoudjdwrzbg_zhilb,  Local.shoudjdwrzbg_zhilb,
						"" + rs.getDouble("QBAR") + "", "" + beiz + "", beiz };
				ArrHeader[14] = new String[] { Local.ganzwhjlbg_zhilb,  Local.ganzwhjlbg_zhilb,
						 Local.ganzwhjlbg_zhilb, "" + rs.getDouble("SDAF") + "", "" + beiz + "", beiz };
				ArrHeader[15] = new String[] { Local.kongqgzjlbg_zhilb, Local.kongqgzjlbg_zhilb,
						Local.kongqgzjlbg_zhilb, "" + rs.getDouble("STAD") + "", "" + beiz + "",beiz };
				ArrHeader[16] = new String[] { Local.ganzjqlbg,
						Local.ganzjqlbg, Local.ganzjqlbg,
						"" + rs.getDouble("STD") + "", "" + beiz + "", beiz };
				ArrHeader[17] = new String[] { Local.shoudjqlbg,
						Local.shoudjqlbg, Local.shoudjqlbg,
						"" + rs.getDouble("STAR") + "", "" + beiz + "", beiz };
				ArrHeader[18] = new String[] { Local.ganzwhjqbg,
						 Local.ganzwhjqbg,  Local.ganzwhjqbg,
						"" + rs.getDouble("HDAF") + "", beiz, beiz };
				ArrHeader[19] = new String[] { Local.kongqgzjqbg,
						Local.kongqgzjqbg, Local.kongqgzjqbg,
						"" + rs.getDouble("HAD") + "", beiz, beiz };
				ArrHeader[20] = new String[] { Local.gudtbg_zhilb,
						Local.gudtbg_zhilb,Local.gudtbg_zhilb,
						"" + rs.getDouble("FCAD") + "",
						beiz, beiz };
				
				ArrHeader[21] = new String[] { Local.ganjgwrbg_zhilb,
						Local.ganjgwrbg_zhilb,Local.ganjgwrbg_zhilb,
						"" + rs.getDouble("QGRD") + "",
						beiz, beiz };
			} else
				return null;
		} catch (Exception e) {
			System.out.println(e);
		}
		int[] ArrWidth = new int[] { 100, 95, 95, 155, 80, 80 };

		rt.setTitle(Local.ruchybg, ArrWidth);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 21);
		rt.title.setRowHeight(2, 40);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

		ResultSetList rslBT =con.getResultSetList("select to_char(huaysj,'yyyy-mm-dd') as huaysj, huaybh from zhillsb where id="+this.getBianmValue().getId());
		String riq="";
		String bh="";
		
		while(rslBT.next()){
			riq=rslBT.getString("HUAYSJ");
			bh=rslBT.getString("HUAYBH");
		}
		rslBT.close();
		
		rt.setDefaultTitle(1, 2, "制表单位:"
				+ getDiancmcValue().getValue(),
				Table.ALIGN_LEFT);
		rt.setDefaultTitle(3, 2, "化验日期:"
				+riq ,
				Table.ALIGN_CENTER);
		rt.setDefaultTitle(5, 2, "化验编号:"
				+bh ,
				Table.ALIGN_RIGHT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		
		String str = DateUtil.FormatDate(new Date());
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:" + str, -1);
		//rt.setDefautlFooter(3, 1, "负责人：", -1);
		rt.setDefautlFooter(4, 1, "审核：" + "", -1);
		rt.setDefautlFooter(3, 2, "审核：" + "", Table.ALIGN_CENTER);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		rt.setBody(new Table(22, 6));
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		String[][] ArrHeader1 = new String[1][6];
		ArrHeader1[0] = ArrHeader[0];
		rt.body.setHeaderData(ArrHeader1);// 表头数据
		for (int i = 1; i < 22; i++) {
			for (int j = 0; j < 6; j++) {
				if (ArrHeader[i][j] == null || ArrHeader[i][j].length() == 0) {
					ArrHeader[i][j] = "0";
				}
				rt.body.setCellValue(i + 1, j + 1, ArrHeader[i][j]);
			}
		}
		for (int i = 1; i <= 22; i++) {
			rt.body.setRowCells(i, Table.PER_FONTSIZE, 9);
		}
		rt.body.setCellValue(5, 4, rt.body.format(rt.body.getCellValue(5, 4),
				"0.0"));
		for (int i = 6; i < 23; i++) {
			rt.body.setCellValue(i, 4, rt.body.format(rt.body
					.getCellValue(i, 4), "0.00"));
		}
//		for (int i = 17; i < 23; i++) {
//			rt.body.setCellValue(i, 4, rt.body.format(rt.body
//					.getCellValue(i, 4), "0"));
//		}
		// rt.body.setCellValue(i, j, strValue);
		rt.body.setRowHeight(40);
		//for (int i=6;i<19;i++){
			
			//  rt.body.setRowHeight(5, 70);
			 
		//}
	 
		
		rt.body.setCellFontSize(4, 2, 9);
		rt.body.setCells(2, 1, 22, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
		rt.body.setCells(4, 2, 4, 6, Table.PER_ALIGN, Table.ALIGN_CENTER);
	//	rt.body.setCells(6, 5, 18, 6, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.body.setCells(6, 5, 22, 6,Table.PER_ALIGN, Table.ALIGN_CENTER);
		//rt.body.merge(2, 1, 21, 3);
		//rt.body.merge(2, 5, 21, 6);
		//rt.body.merge(4, 2, 4, 6);
		rt.body.merge(4, 2, 4, 6);
		rt.body.merge(5, 1, 22, 3);
		rt.body.merge(4, 5, 22, 6);
		rt.body.ShowZero = false;

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		//rt.body.setRowHeight(43);

		return rt.getAllPagesHtml();

	}

	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}

	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("化验日期:"));
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("huayrq");
		tb1.addField(df);
		tb1.addText(new ToolbarText("至:"));
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		tb1.addField(df1);
//		tb1.addText(new ToolbarText("-"));
		
		
//		tb1.addText(new ToolbarText("运输方式:"));
//		ComboBox meik = new ComboBox();
//		meik.setTransform("YUNSFSSelect");
//		meik
//				.setListeners("select:function(){document.Form0.submit();}");
//		tb1.addField(meik);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"forms[0]",null,getTreeid());
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
	
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("化验编号:"));
		ComboBox shij = new ComboBox();
		shij.setEditable(true);
		shij.setTransform("BianmSelect");
		shij.setWidth(130);
		shij
				.setListeners("select:function(own,rec,index){Ext.getDom('BianmSelect').selectedIndex=index}");
		tb1.addField(shij);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		tb1.addFill();
		// tb1.addText(new ToolbarText("<marquee width=300
		// scrollamount=2></marquee>"));
		setToolbar(tb1);
	}



	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		// if(getTbmsg()!=null) {
		// getToolbar().deleteItem();
		// getToolbar().addText(new ToolbarText("<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>"));
		// }
		((DateField) getToolbar().getItem("huayrq")).setValue(getRiq());
		
		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			setTreeid(null);
			setBianmValue(null);
			setBianmModel(null);
		}
		if (riqchange) {
			riqchange = false;
			setBianmValue(null);
			setBianmModel(null);
		}
		getSelectData();
	}

	// 日期是否变化
	private boolean riqchange = false;

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if (this.riq != null) {
			if (!this.riq.equals(riq))
				riqchange = true;
		}
		this.riq = riq;
	}

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
			riqchange = true;
		}

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
				riqchange = true;
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
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	private boolean _DiancmcChange=false;
	public IDropDownBean getDiancmcValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getDiancmcModel().getOption(0));
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setDiancmcValue(IDropDownBean Value) {
		if (((Visit)getPage().getVisit()).getDropDownBean2()==Value) {
			_DiancmcChange = false;
		}else{
			_DiancmcChange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql)) ;
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(_value);
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
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public IDropDownBean getBianmValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianmModel().getOptionCount() > 0) {
				setBianmValue((IDropDownBean) getBianmModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianmValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianmModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianmModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianmModel(IPropertySelectionModel model) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(model);
	}

	private void setBianmModels() {
		StringBuffer sb = new StringBuffer();
		sb
				.append("select distinct z.id,z.huaybh from fahb f,zhillsb z ,caiyb ca,caiylbb cb, diancxxb dc " +
						" where f.zhilb_id=z.zhilb_id  and f.diancxxb_id=dc.id " +
						" and   ca.id=z.caiyb_id and ca.caiylbb_id=cb.id and cb.bianm='JC'  " +
						" and dc.id="+getTreeid()+
						" and z.huaysj>=\n")
				.append(DateUtil.FormatOracleDate(getRiq()))
				.append(" and z.huaysj<=\n")
				.append(DateUtil.FormatOracleDate(getRiq2()))
				.append("\n");
				
			
		setBianmModel(new IDropDownModel(sb.toString(), "请选择"));
	}


	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}

}
