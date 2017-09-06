package com.zhiren.dc.diaoygl;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.zhiren.common.ResultSetList;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * 作者：陈泽天
 * 时间：2010-01-29 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。 
 */
/*
 * 作者：licj
 * 时间：2011-3-25
 * 描述：去除原来版本的数量。 
 */
public class Rucrlrzc_zh extends BasePage {
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
		boolean qusly=false;
		String Qufdxc="select zhi from xitxxb where mingc='入厂入炉热值差是否从rezcb取数据' and zhuangt=1";
		ResultSetList Daxcrs = con.getResultSetList(Qufdxc); 
		if(Daxcrs.next()){
			String zhi=Daxcrs.getString("zhi");
			if(zhi.equals("是")){
				qusly=true;
			}
		}
		boolean zhib=false;
		String Zhibsql="select zhi from xitxxb where mingc='入厂入炉热值差是否显示其他指标' and zhuangt=1";
		ResultSetList Xianszb = con.getResultSetList(Zhibsql); 
		if(Xianszb.next()){
			String zhi=Xianszb.getString("zhi");
			if(zhi.equals("是")){
				zhib=true;
			}
		}
		
		if(qusly){
			buffer.append(
					"select decode(r.riq,null,'累计',to_char(r.riq,'yyyy-mm-dd')) as riq,sum(r.rucsl) as rucsl,\n" +
					"decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)) as rucrl,\n" + 
					"decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl)*1000/4.1816,0)) as rucrldk,\n" + 
					"decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucsf)/sum(r.rucsl),1)) as rucsf,\n" + 
					"sum(r.rulsl) as rulsl,\n" + 
					"decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) as rulrl,\n" + 
					"decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl)*1000/4.1816,0)) as rulrldk,\n" + 
					"decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulsf)/sum(r.rulsl),1)) as  rulsf,\n" + 
					"nvl(decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)),0)-nvl(decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)),0)   as rezctzq,\n" + 
					"decode(sum(r.rulsl),0,0,round_new((nvl(decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)),0)-nvl(decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)),0))*1000/4.1816,0))  as rezcdk,\n" + 
					"nvl(decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)),0)-round_new(decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) *(100-decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucsf)/sum(r.rucsl),1)))/(100-decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulsf)/sum(r.rulsl),1))),2) as rezctzh,\n" + 
					"decode(sum(r.rulsl),0,0,round_new((nvl(decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucrl)/sum(r.rucsl),2)),0)-round_new(decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulrl)/sum(r.rulsl),2)) *(100-decode(sum(r.rucsl),0,0,round_new(sum(r.rucsl*r.rucsf)/sum(r.rucsl),1)))/(100-decode(sum(r.rulsl),0,0,round_new(sum(r.rulsl*r.rulsf)/sum(r.rulsl),1))),2))*1000/4.1816,0)) as rezcdk\n" + 
					" from rezcb r where r.riq>=to_date('"+getRiq1()+"','yyyy-mm-dd')\n" + 
					"and riq<=to_date('"+getRiq2()+"','yyyy-mm-dd')\n" + 
					"group by rollup (r.riq) order by r.riq");
			
		}else if(zhib){

			buffer.append(
			"select riq,\n" +
			"qnet_ar,\n" + 
			"round_new(qnet_ar/0.0041816,0)  qnet_ar1,--\n" + 
			"mt,\n" + 
			"star,\n" + 
			"aar,\n" + 
			"vdaf,\n" + 
			//"meil,\n" + 
			"qnet_arl,\n" + 
			"round_new(qnet_arl/0.0041816,0) qnet_arl1,--\n" + 
			"mtl,\n" + 
			"star1,\n" + 
			"aar1,\n" + 
			"vdaf1,\n" + 
			"qnet_ar- qnet_arl rzc,--\n" + 
			"round_new(qnet_ar/0.0041816,0)-round_new(qnet_arl/0.0041816,0) rzc1,--\n" + 
			"round_new(qnet_ar-qnet_arl*(100-mt)/(100-mtl),2) sfc,--\n" + 
			"round_new(qnet_ar/0.0041816-qnet_arl/0.0041816*(100-mt)/(100-mtl),0)sfc1--调整后热值差=round(入厂热量 -入炉热量 * (100 -入厂水分) / (100 - 入炉水分),2)\n" + 
			"\n" + 
			"from\n" + 
			/*
			*huochaoyuan
			*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
			*/		
			"(select decode(grouping(fahb.daohrq),1,'累计',to_char(fahb.daohrq,'yyyy-mm-dd'))daohrq,sum(round_new(fahb.laimsl,0))laimsl,round_new(sum(round_new(zhilb.qnet_ar,2)*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),2)qnet_ar,\n" + 
	//		end		 
			"round_new(sum(zhilb.mt*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)mt,\n" + 
			"round_new(sum(zhilb.star*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)star,\n" +
			"round_new(sum(zhilb.aar*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)aar,\n" + 
			"round_new(sum(zhilb.vdaf*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)vdaf\n"+
			"from fahb,zhilb\n" + 
			"where fahb.laimsl<>0 and fahb.zhilb_id=zhilb.id \n" + 
			"and fahb.daohrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and fahb.daohrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
			"group by rollup(fahb.daohrq)\n" + 
			")rucm ,\n" + 
			/*
			*huochaoyuan
			*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
			*/			
			"(select decode(grouping(rulmzlb.rulrq),1,'累计',to_char(rulmzlb.rulrq,'yyyy-mm-dd'))rulrq,sum(round_new(rulmzlb.meil,0))meil,round_new(sum(round_new(rulmzlb.qnet_ar,2)*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.qnet_ar,0),0,0,rulmzlb.meil),0)),2)qnet_arl,\n" + 
	//		end		 
			"round_new(sum(rulmzlb.mt*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.mt,0),0,0,rulmzlb.meil),0)),1)mtl,\n" +
			"round_new(sum(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad))*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0)),1) star1,\n" +
			"round_new(sum(rulmzlb.aar*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.aar,0),0,0,rulmzlb.meil),0)),1)aar1,\n" + 
			"round_new(sum(rulmzlb.vdaf*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.vdaf,0),0,0,rulmzlb.meil),0)),1)vdaf1\n"+
			"from rulmzlb\n" + 
			"where   rulmzlb.meil<>0\n" + 
			"and rulmzlb.rulrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and rulmzlb.rulrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
			"group by rollup(rulmzlb.rulrq)\n" + 
			")rulm,\n" + 
			"(select to_char(rulmzlb.rulrq,'yyyy-mm-dd')riq from rulmzlb where rulmzlb.rulrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and rulmzlb.rulrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')and rulmzlb.shenhzt=3 and rulmzlb.meil<>0\n" + 
			"union\n" + 
			"select distinct to_char(daohrq ,'yyyy-mm-dd')from   fahb,zhilb\n" + 
			"where fahb.laimsl<>0 and fahb.zhilb_id=zhilb.id \n" + 
			"and fahb.daohrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and fahb.daohrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
			"union\n" + 
			"(select '累计' from dual)\n" + 
			")biaot\n" + 
			"where biaot.riq=rucm.daohrq(+) and   biaot.riq=rulm.rulrq(+)\n" + 
			" order by decode(riq,'累计',0,1),riq");
		}else{
			buffer.append(
					"select riq,\n" +
					"qnet_ar,\n" + 
					"round_new(qnet_ar/0.0041816,0)  qnet_ar1,--\n" + 
					"mt,\n" + 
//					"star,\n" + 
//					"aar,\n" + 
//					"vdaf,\n" + 
					//"meil,\n" + 
					"qnet_arl,\n" + 
					"round_new(qnet_arl/0.0041816,0) qnet_arl1,--\n" + 
					"mtl,\n" + 
//					"star1,\n" + 
//					"aar1,\n" + 
//					"vdaf1,\n" + 
					"qnet_ar- qnet_arl rzc,--\n" + 
					"round_new(qnet_ar/0.0041816,0)-round_new(qnet_arl/0.0041816,0) rzc1,--\n" + 
					"round_new(qnet_ar-qnet_arl*(100-mt)/(100-mtl),2) sfc,--\n" + 
					"round_new(qnet_ar/0.0041816-qnet_arl/0.0041816*(100-mt)/(100-mtl),0)sfc1--调整后热值差=round(入厂热量 -入炉热量 * (100 -入厂水分) / (100 - 入炉水分),2)\n" + 
					"\n" + 
					"from\n" + 
					/*
					*huochaoyuan
					*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
					*/		
					"(select decode(grouping(fahb.daohrq),1,'累计',to_char(fahb.daohrq,'yyyy-mm-dd'))daohrq,sum(round_new(fahb.laimsl,0))laimsl,round_new(sum(round_new(zhilb.qnet_ar,2)*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),2)qnet_ar,\n" + 
			//		end		 
					"round_new(sum(zhilb.mt*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)mt\n" + 
//					"round_new(sum(zhilb.star*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)star,\n" +
//					"round_new(sum(zhilb.aar*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)aar,\n" + 
//					"round_new(sum(zhilb.vdaf*round_new(fahb.laimsl,0))/sum(round_new(fahb.laimsl,0)),1)vdaf\n"+
					"from fahb,zhilb\n" + 
					"where fahb.laimsl<>0 and fahb.zhilb_id=zhilb.id \n" + 
					"and fahb.daohrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and fahb.daohrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
					"group by rollup(fahb.daohrq)\n" + 
					")rucm ,\n" + 
					/*
					*huochaoyuan
					*2009-10-22,修改发热量计算时先规约再参与计算，统一计算方式
					*/			
					"(select decode(grouping(rulmzlb.rulrq),1,'累计',to_char(rulmzlb.rulrq,'yyyy-mm-dd'))rulrq,sum(round_new(rulmzlb.meil,0))meil,round_new(sum(round_new(rulmzlb.qnet_ar,2)*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.qnet_ar,0),0,0,rulmzlb.meil),0)),2)qnet_arl,\n" + 
			//		end		 
					"round_new(sum(rulmzlb.mt*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.mt,0),0,0,rulmzlb.meil),0)),1)mtl\n" +
//					"round_new(sum(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad))*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(((100-rulmzlb.mt)*rulmzlb.stad/(100-rulmzlb.mad)),0),0,0,rulmzlb.meil),0)),1) star1,\n" +
//					"round_new(sum(rulmzlb.aar*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.aar,0),0,0,rulmzlb.meil),0)),1)aar1,\n" + 
//					"round_new(sum(rulmzlb.vdaf*round_new(rulmzlb.meil,0))/sum(round_new(decode(nvl(rulmzlb.vdaf,0),0,0,rulmzlb.meil),0)),1)vdaf1\n"+
					"from rulmzlb\n" + 
					"where   rulmzlb.meil<>0\n" + 
					"and rulmzlb.rulrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and rulmzlb.rulrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
					"group by rollup(rulmzlb.rulrq)\n" + 
					")rulm,\n" + 
					"(select to_char(rulmzlb.rulrq,'yyyy-mm-dd')riq from rulmzlb where rulmzlb.rulrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and rulmzlb.rulrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')and rulmzlb.shenhzt=3 and rulmzlb.meil<>0\n" + 
					"union\n" + 
					"select distinct to_char(daohrq ,'yyyy-mm-dd')from   fahb,zhilb\n" + 
					"where fahb.laimsl<>0 and fahb.zhilb_id=zhilb.id \n" + 
					"and fahb.daohrq>=to_date('"+ getRiq1()+ "','yyyy-mm-dd') and fahb.daohrq<=to_date('"+ getRiq2()+ "','yyyy-mm-dd')\n" + 
					"union\n" + 
					"(select '累计' from dual)\n" + 
					")biaot\n" + 
					"where biaot.riq=rucm.daohrq(+) and   biaot.riq=rulm.rulrq(+)\n" + 
					" order by decode(riq,'累计',0,1),riq");
			
		}
			

		ResultSet rs = con.getResultSet(buffer,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		Report rt = new Report();
		String[][] ArrHeader;
		int[] ArrWidth;
		if(zhib){
		ArrHeader = new String[3][17];
		ArrWidth = new int[] { 80,70,70,60,60,60,60,70,70,60,60,60,60,60,60,60,60};
		ArrHeader[0] = new String[] { "日期",  "入厂","入厂","入厂","入厂","入厂","入厂","入炉","入炉","入炉","入炉","入炉","入炉","热值差","热值差","热值差","热值差"};
		ArrHeader[1] = new String[] { "日期",  "热值(Qnet_ar)","热值(kcal/kg)","水分","硫份","灰份","挥发份","热值(Qnet_ar)","热值(kcal/kg)","水分","硫份","灰份","挥发份","水分调整前","水分调整前","水分调整后","水分调整后"};
		ArrHeader[2] = new String[] { "日期", "MJ/kg","Kcal/Kg","Mt(%)","Star(%)","Aar(%)","Vdaf(%)","MJ/kg","Kcal/Kg","Mt(%)","Star(%)","Aar(%)","Vdaf(%)","MJ/kg","Kcal/Kg","MJ/kg","Kcal/Kg"};
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
		rt.setDefaultTitle(10, 8, "单位:(吨)" ,Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
		rt.body.setPageRows(rt.PAPER_COLROWS);
//		增加长度的拉伸
		rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
		rt.body.mergeFixedCols();
		rt.body.mergeFixedRow();
		rt.body.ShowZero=true;
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
		}else{
			ArrHeader = new String[3][11];
			ArrWidth = new int[] { 80,70,70,60,70,70,60,60,60,60,60};
			ArrHeader[0] = new String[] { "日期", "入厂","入厂","入厂","入炉","入炉","入炉","热值差","热值差","热值差","热值差"};
			ArrHeader[1] = new String[] { "日期", "热值(Qnet_ar)","热值(kcal/kg)","水分","热值(Qnet_ar)","热值(kcal/kg)","水分","水分调整前","水分调整前","水分调整后","水分调整后"};
			ArrHeader[2] = new String[] { "日期", "MJ/kg","Kcal/Kg","Mt(%)","MJ/kg","Kcal/Kg","Mt(%)","MJ/kg","Kcal/Kg","MJ/kg","Kcal/Kg"};
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
			rt.setDefaultTitle(6, 6, "单位:(吨)" ,Table.ALIGN_RIGHT);
//			rt.setDefaultTitle(6, 2, "年份:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
			rt.body.setPageRows(rt.PAPER_COLROWS);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.mergeFixedCols();
			rt.body.mergeFixedRow();
			rt.body.ShowZero=true;
			rt.createDefautlFooter(ArrWidth);
			rt.setDefautlFooter(1, 11, "打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
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
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("RIQ2");
		tb1.addField(df1);
		ToolbarButton tb = new ToolbarButton(null, "刷新","function(){document.Form0.submit();}");
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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean2(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean3(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setDefaultTree(null);
			setTreeid(null);
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
    // 供应商
	
    public IDropDownBean getGongysDropDownValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean2()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongysDropDownModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean2();
    }
    public void setGongysDropDownValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean2()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean2().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean2(Value);
//    	}
    }
    public void setGongysDropDownModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getGongysDropDownModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getGongysDropDownModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }
    public void getGongysDropDownModels() {
    	String sql="select id,mingc\n" +
    	"from gongysb\n" + 
    	"where gongysb.fuid=0";
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"全部")) ;
        return ;
    }
    //年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit()).setDropDownBean3(getIDropDownBean(getNianfModel(),new Date().getYear()+1900));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}
	public void setNianfValue(IDropDownBean Value) {
//		if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean3()!=null){
//			if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean3().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData2(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData2(false);
//	    	}
			 ((Visit) getPage().getVisit()).setDropDownBean3(Value);
//		}
	}
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
    public void getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = DateUtil.getYear(new Date())-2; i <= DateUtil.getYear(new Date())+2; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		 ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(listNianf)) ;
	}
    private IDropDownBean getIDropDownBean(IPropertySelectionModel model,long id) {
        int OprionCount;
        OprionCount = model.getOptionCount();
        for (int i = 0; i < OprionCount; i++) {
            if (((IDropDownBean) model.getOption(i)).getId() == id) {
                return (IDropDownBean) model.getOption(i);
            }
        }
        return null;
    }
    //类型
    public IDropDownBean getLeixSelectValue() {
    	if( ((Visit) getPage().getVisit()).getDropDownBean4()==null){
   		 ((Visit) getPage().getVisit()).setDropDownBean4((IDropDownBean)getLeixSelectModel().getOption(0));
   	}
       return  ((Visit) getPage().getVisit()).getDropDownBean4();
    }
    public void setLeixSelectValue(IDropDownBean Value) {
//    	if(Value!=null&&((Visit) getPage().getVisit()).getDropDownBean4()!=null){
//	    	if(Value.getId()!=((Visit) getPage().getVisit()).getDropDownBean4().getId()){
//	    		((Visit) getPage().getVisit()).setBooleanData1(true);
//	    	}else{
//	    		((Visit) getPage().getVisit()).setBooleanData1(false);
//	    	}
	    	((Visit) getPage().getVisit()).setDropDownBean4(Value);
//    	}
    }
    public void setLeixSelectModel(IPropertySelectionModel value) {
    	((Visit) getPage().getVisit()).setProSelectionModel4(value);
    }

    public IPropertySelectionModel getLeixSelectModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
            getLeixSelectModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel4();
    }
    public void getLeixSelectModels() {
        List list=new ArrayList();
        list.add(new IDropDownBean(1,"分厂分矿"));
        list.add(new IDropDownBean(2,"分矿分厂"));
        list.add(new IDropDownBean(3,"分厂"));
        list.add(new IDropDownBean(4,"分矿"));
        ((Visit) getPage().getVisit()).setProSelectionModel4(new IDropDownModel(list)) ;
        return ;
    }

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
	//
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
