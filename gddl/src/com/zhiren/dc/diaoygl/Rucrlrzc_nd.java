package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：夏峥
 * 时间：2013-10-10
 * 描述：增加水分差调整列
 * 		根据到厂煤全水分与入炉煤全水分的差值对入炉原煤量进行调整，
 * 		计算公式：水分差调整量=入炉原煤量*（1-（100-入炉加权平均水份Mt）/（100-到厂煤加权平均水份Mt）)
 */
public class Rucrlrzc_nd extends BasePage {
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

	public String getPrintTable() {
		if (!blnIsBegin) {
			return "";
		}
		blnIsBegin = false;
		return getHetltj();
	}
	//  
	private String getHetltj() {
		JDBCcon con = new JDBCcon();
		StringBuffer buffer = new StringBuffer("");

		buffer.append(
				"select riq, laimsl,\n" +
				"qnet_ar,\n" + 
				"round_new(qnet_ar/0.0041816,0)  qnet_ar1,\n" + 
				"mt,\n" + 
				"std,\n" + 
				"stad,\n" + 
				"aar,\n" + 
				"vdaf,\n" + 
				"meil,\n" + 
				"qnet_arl,\n" + 
				"round_new(qnet_arl/0.0041816,0) qnet_arl1,\n" + 
				"mtl,\n" + 
				"std1,\n" + 
				"stad1,\n" + 
				"aar1,\n" + 
				"vdaf1,\n" + 
				"  round(decode((100-nvl(mt,0)),0,meil,meil*(1-(100-mtl)/(100-nvl(mt,0)))),2) mttzl,\n" + 
				"qnet_ar- qnet_arl rzc,\n" + 
				"round_new(qnet_ar/0.0041816,0)-round_new(qnet_arl/0.0041816,0) rzc1\n" + 
				"from\n" + 
				"(select decode(grouping(fahb.daohrq),1,'合计',to_char(fahb.daohrq,'yyyy-mm-dd'))daohrq,\n" + 
				"round_new(sum(fahb.laimsl),2)laimsl,\n" + 
				"round_new(sum(zhilb.qnet_ar*fahb.laimsl)/sum(fahb.laimsl),2)qnet_ar,\n" + 
				"round_new(sum(zhilb.mt*fahb.laimsl)/sum(fahb.laimsl),1)mt,\n" + 
				"round_new(sum(zhilb.std*fahb.laimsl)/sum(fahb.laimsl),2)std,\n" + 
				"round_new(sum(zhilb.stad*fahb.laimsl)/sum(fahb.laimsl),2)stad,\n" +
				"round_new(sum(zhilb.aar*fahb.laimsl)/sum(fahb.laimsl),2)aar,\n" + 
				"round_new(sum(zhilb.vdaf*fahb.laimsl)/SUM(fahb.laimsl),2)vdaf\n" + 
				"from fahb,zhilb\n" + 
				"where fahb.laimsl<>0\n" + 
				"and fahb.zhilb_id=zhilb.id\n" + 
				"and fahb.daohrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and fahb.daohrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
				"group by rollup(fahb.daohrq)\n" + 
				")rucm ,\n" + 
				"(select decode(grouping(rulmzlb.rulrq),1,'合计',to_char(rulmzlb.rulrq,'yyyy-mm-dd'))rulrq,\n" + 
				"SUM(rulmzlb.meil)meil,\n" + 
				"round_new(decode(SUM(decode(nvl(rulmzlb.qnet_ar,0),0,0,rulmzlb.meil)),0,0,SUM(rulmzlb.qnet_ar*rulmzlb.meil)/sum(decode(nvl(rulmzlb.qnet_ar,0),0,0,rulmzlb.meil))),2)qnet_arl,\n" + 
				"round_new(decode(SUM(decode(nvl(rulmzlb.mt,0),0,0,rulmzlb.meil)),0,0,sum(rulmzlb.mt*rulmzlb.meil)/SUM(decode(nvl(rulmzlb.mt,0),0,0,rulmzlb.meil))),1)mtl,\n" +
				"round_new(decode(SUM(decode(nvl(rulmzlb.std,0),0,0,rulmzlb.meil)),0,0,sum(rulmzlb.std*rulmzlb.meil)/SUM(decode(nvl(rulmzlb.std,0),0,0,rulmzlb.meil))),2)std1,\n" + 
				"round_new(decode(SUM(decode(nvl(rulmzlb.stad,0),0,0,rulmzlb.meil)),0,0,sum(rulmzlb.stad*rulmzlb.meil)/SUM(decode(nvl(rulmzlb.stad,0),0,0,rulmzlb.meil))),2)stad1,\n" + 
//					"round_new(decode(sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.std/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0)),0,0,sum(((100-rulmzlb.mt)*rulmzlb.std/(100-rulmzlb.mad))*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.std/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0))),2) std1,\n" + 
//					"round_new(decode(sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0)),0,0,sum(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad))*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0))),2) stad1,\n" + 
				"round_new(decode(SUM(decode(nvl(rulmzlb.aar,0),0,0,rulmzlb.meil)),0,0,sum(rulmzlb.aar*rulmzlb.meil)/SUM(decode(nvl(rulmzlb.aar,0),0,0,rulmzlb.meil))),2)aar1,\n" + 
				"round_new(decode(SUM(decode(nvl(rulmzlb.vdaf,0),0,0,rulmzlb.meil)),0,0,sum(rulmzlb.vdaf*rulmzlb.meil)/SUM(decode(nvl(rulmzlb.vdaf,0),0,0,rulmzlb.meil))),2)vdaf1\n" + 
				"from rulmzlb\n" + 
				"where rulmzlb.meil<>0\n" + 
				"and rulmzlb.rulrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and rulmzlb.rulrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
				"group by rollup(rulmzlb.rulrq)\n" + 
				")rulm,\n" + 
				"(SELECT to_char((TO_DATE('"+ getRiq1()+ "', 'yyyy-mm-dd') - 1 + ROWNUM),'yyyy-mm-dd') RIQ\n" + 
				"FROM DUAL\n" + 
				"CONNECT BY ROWNUM <= (TO_DATE('"+ getRiq2()+ "', 'yyyy-mm-dd') - TO_DATE('"+ getRiq1()+ "', 'yyyy-mm-dd') + 1)\n" + 
				"union\n" + 
				"(select '合计' from dual)\n" + 
				")biaot\n" + 
				"where biaot.riq=rucm.daohrq(+) and   biaot.riq=rulm.rulrq(+)\n" + 
				" order by decode(riq,'合计',0,1),riq");

		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;

		ArrHeader = new String[3][20];
		ArrWidth = new int[] { 80,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60,60};
		ArrHeader[0] = new String[] { "日期", "入厂", "入厂","入厂","入厂","入厂","入厂","入厂","入厂","入炉","入炉","入炉","入炉","入炉","入炉","入炉","入炉","水分差<br>调整","热值差","热值差"};
		ArrHeader[1] = new String[] { "日期", "数量", "热值","热值","水分","硫份","硫份","灰份","挥发份","数量","热值","热值","水分","硫份","硫份","灰份","挥发份","水分差<br>调整","热值差","热值差"};
		ArrHeader[2] = new String[] { "日期", "(吨)", "(MJ/kg)","(Kcal/kg)","Mt(%)","St,d(%)","St,ad(%)","Ad(%)","Vdaf(%)","(吨)","(MJ/kg)","(Kcal/kg)","Mt(%)","St,d(%)","St,ad(%)","Ad(%)","Vdaf(%)","(吨)","(MJ/kg)","(Kcal/Kg)"};
		rt.setBody(new Table(rs, 3, 0, 1));
		//
		int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
		rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.setTitle("入 厂 入 炉 热 值 差", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 5, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(10, 11, "单位:(吨)" ,Table.ALIGN_RIGHT);
		rt.body.setPageRows(Report.PAPER_ROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 17, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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

	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
	}

	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean1()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}
	boolean riqichange = false;
	private String riq1;
	public String getRiq1() {
		if (riq1 == null || riq1.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			riq1 = DateUtil.FormatDate(stra.getTime());
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
		if (this.riq2!= null && !this.riq2.equals(riq)) {
			this.riq2 = riq;
			riqichange = true;
		}
	}
	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("入炉日期:"));
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// 与html页中的id绑定,并自动刷新
		df.setId("RIQ1");
		tb1.addField(df);
		
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Refurbish);
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
		}
		
		//begin方法里进行初始化设置
		visit.setString1(null);

			String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
			if (pagewith != null) {

				visit.setString1(pagewith);
			}
		//	visit.setString1(null);保存传递的非默认纸张的样式
		getToolbars();
		blnIsBegin = true;
	}
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
