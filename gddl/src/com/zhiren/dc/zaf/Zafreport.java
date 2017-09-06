package com.zhiren.dc.zaf;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
 

/*
 * 作者：夏峥
 * 时间：2012-01-06
 * 描述： 吨煤费用=杂费总合/入炉煤量总合
 * 		初始化电厂树为组合电厂， 当点击界面链接时弹出界面中显示明细信息
 */

/*
 * 作者：夏峥
 * 时间：2012-02-09
 * 描述: 报表中当访问用户为国电电力时只显示以上报的数据（其状态为1或3的数据）。
 * 		 并按照电厂序号进行排序
 */
/*
 * 作者：赵胜男
 * 时间：2012-12-14
 * 描述: 新增加同比一列 同比=本期-累计
 * 		 
 */
/*
 * 作者：夏峥
 * 时间：2013-03-15
 * 描述: 吨煤费用=杂费总合/(发电用煤量+供热用煤量)
 */
/*
 * 作者：夏正
 * 时间：2013-03-26
 * 描述: 调整同期bug
 */
/*
 * 作者：夏正
 * 时间：2013-03-27
 * 描述: 增加吨煤费用同比信息
 */
public class Zafreport  extends BasePage implements PageValidateListener{
	
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	
	private String diancxxb_id;
	public void setDiancxxb_id(String diancxxb_id){
		this.diancxxb_id = diancxxb_id;
	}
	public String getDiancxxb_id(){
		return diancxxb_id;
	}
	
	//开始日期
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
		}
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
        //为 "刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		setDiancxxb_id(cycle.getRequestContext().getParameter("strDiancxxb_id"));
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			initDiancTree();
			setToolbar(null);
			isBegin=true;
		}
	getToolBars() ;
	Refurbish();
	}
	
	private String RT_HET="Zafreport";//标煤耗用情况月报
	private String mstrReportName="Zafreport";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "无此报表";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

//	private int intZhuangt=0;
	public void setZhuangt(int _value) {
//		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getSelectData(){
		boolean huiz=(null==getDiancxxb_id()||"".equals(getDiancxxb_id()));
		if(huiz){    //查看汇总数据
			return getHuiz();
		}else{
			return getMingx();
		}
		
	}
	
	
	private String getHuiz(){
		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		
		Visit visit = (Visit) getPage().getVisit();
		String tiaoj="";
		if(visit.getDiancxxb_id()==112){tiaoj="AND (z.ZHUANGT=1 or z.ZHUANGT=3)";}
		
		//报表内容
			strSQL.append(
				"SELECT decode(grouping(je.fuid) + grouping(je.mc),2,'公司合计',getLinkZaf(min(je.diancxxb_id),decode(grouping(je.fuid) + grouping(je.mc),2,'公司合计',je.mc))) dc,\n" +
//				"SELECT decode(grouping(je.fuid) + grouping(je.mc),2,'公司合计',je.mc) dc,\n" +
				" nvl('小计', '') zf,\n"+
				"  sum(jine_bq_by) jine_bq_by,\n" + 
				"  round(decode(nvl(sum(ml.bbyml),0),0,0,sum(jine_bq_by)/sum(ml.bbyml)),2) bbydmfy,\n" + 
				"  sum(jine_tq_by) jine_tq_by,\n" + 
				"   round(decode(nvl(sum(ml.tbyml),0),0,0,sum(jine_tq_by)/sum(ml.tbyml)),2) tbydmfy,\n" +
				"(sum(nvl(jine_bq_by,0))- sum(nvl(jine_tq_by,0)) )jine_tb_by,\n"+
				"  round(decode(nvl(sum(ml.bbyml),0),0,0,sum(jine_bq_by)/sum(ml.bbyml)),2)-round(decode(nvl(sum(ml.tbyml),0),0,0,sum(jine_tq_by)/sum(ml.tbyml)),2) bbydmfytb,\n" +
				"  sum(jine_bq_lj) jine_bq_lj ,\n" + 
				"    round(decode(nvl(sum(ml.bljml),0),0,0,sum(jine_bq_lj)/sum(ml.bljml)),2) bljdmfy,\n" + 
				"  sum(jine_tq_lj) jine_tq_lj,\n" + 
				"  round( decode(nvl(sum(ml.tljml),0),0,0,sum(jine_tq_lj)/sum(ml.tljml)),2) tljdmfy,\n" +
				"(sum(nvl(jine_bq_lj,0))-sum(nvl(jine_tq_lj,0)))jine_tb_lj,\n"+
				"    round(decode(nvl(sum(ml.bljml),0),0,0,sum(jine_bq_lj)/sum(ml.bljml)),2)-round( decode(nvl(sum(ml.tljml),0),0,0,sum(jine_tq_lj)/sum(ml.tljml)),2) bljdmfytb\n" + 
				"    FROM\n" + 
				"( SELECT  min(dc.id) diancxxb_id,dc.mingc,\n" + 
				" sum(benq_by.jine) jine_bq_by,\n" + 
				" sum(tongq_by.jine) jine_tq_by,\n" + 
				" sum(benq_lj.jine) jine_bq_lj,\n" + 
				" sum(tongq_lj.jine) jine_tq_lj,\n" + 
				" (dc.fuid) fuid , (dc.mingc) mc,(dc.xuh)xuh,\n" + 
				"grouping(dc.fuid) + grouping(dc.mingc) gp\n" + 
				"from (select diancxxb_id, zf.mingc zfmc\n" + 
				"    from (select distinct diancxxb_id, z.mingc\n" + 
				"            from zafb z\n" + 
				"           where z.riq >= last_year_today(date '"+intyear+"-1-1') and z.riq <= date'"+intyear+"-"+intMonth+"-01' " +
				tiaoj+
				") zf WHERE zf.diancxxb_id IN ("+getTreeid()+")) fx,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq = date '"+intyear+"-"+intMonth+"-01'\n" + 
				"   group by z.diancxxb_id, z.mingc) benq_by,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq >= date '"+intyear+"-1-1'\n" + 
				"     and z.riq <= date '"+intyear+"-"+intMonth+"-01'\n" + 
				"   group by z.diancxxb_id, z.mingc) benq_lj,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"   group by z.diancxxb_id, z.mingc) tongq_by,\n" + 
				" (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n" + 
				"    from zafb z\n" + 
				"   where z.riq >= last_year_today(date '"+intyear+"-1-1')\n" + 
				"     and z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"   group by z.diancxxb_id, z.mingc) tongq_lj,diancxxb dc\n" + 
				"where fx.diancxxb_id = benq_by.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = benq_lj.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = tongq_by.diancxxb_id(+)\n" + 
				"  and fx.diancxxb_id = tongq_lj.diancxxb_id(+)\n" + 
				"  and fx.zfmc = benq_by.zafmc(+)\n" + 
				"  and fx.zfmc = benq_lj.zafmc(+)\n" + 
				"  and fx.zfmc = tongq_by.zafmc(+)\n" + 
				"  and fx.zfmc = tongq_lj.zafmc(+)\n" + 
				"  and fx.diancxxb_id = dc.id(+)\n" + 
				" group by  (dc.fuid,dc.xuh, dc.mingc))je,\n" + 
				" (   SELECT fx.diancxxb_id,bby.rulml bbyml,blj.rulml bljml,tby.rulml tbyml,tlj.rulml tljml\n" + 
				"  FROM\n" + 
				"  (select distinct diancxxb_id\n" + 
				"            from yuezbb z\n" + 
				"           where z.riq >= last_year_today(date '"+intyear+"-1-1') and z.riq <= last_year_today(date'"+intyear+"-"+intMonth+"-01') " +
				tiaoj+
				") fx,\n" + 
				" (SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML ) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '本月'\n" + 
				"      AND YZ.RIQ = date '"+intyear+"-"+intMonth+"-01'   GROUP BY YZ.DIANCXXB_ID)bby,\n" + 
				"\n" + 
				"     (SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML ) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '本月'\n" + 
				"      AND  yz.riq >= date '"+intyear+"-1-1'\n" + 
				"     and yz.riq <= date '"+intyear+"-"+intMonth+"-01'\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)blj,\n" + 
				"\n" + 
				"    ( SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '本月'\n" + 
				"      AND  yz.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)tby,\n" + 
				"\n" + 
				"    ( SELECT YZ.DIANCXXB_ID,\n" + 
				"          SUM(YZ.FADYTRML + YZ.GONGRYTRML) RULML\n" + 
				"     FROM YUEZBB YZ\n" + 
				"    WHERE FENX = '本月'\n" + 
				"      AND  yz.riq >= last_year_today(date '"+intyear+"-1-1')\n" + 
				"     and yz.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n" + 
				"    GROUP BY YZ.DIANCXXB_ID)tlj\n" + 
				"    WHERE fx.diancxxb_id= bby.diancxxb_id(+)\n" + 
				"          AND  fx.diancxxb_id=blj.diancxxb_id(+)\n" + 
				"        AND  fx.diancxxb_id=tby.diancxxb_id(+)\n" + 
				"        AND  fx.diancxxb_id=tlj.diancxxb_id(+)\n" + 
				
				")ml\n" + 
				"WHERE ml.diancxxb_id=je.diancxxb_id\n" + 
				"group by ROLLUP (je.fuid,je.xuh, je.mc)\n" + 
				"having not grouping(je.fuid) + grouping(je.mingc) = 1\n" + 
				"order by grouping(je.fuid) desc,je.fuid,grouping(je.xuh)desc, je.xuh,grouping(je.mingc) desc,je.mingc");
			 ArrHeader=new String[2][12];
			 ArrHeader[0]=new String[] {"单位名称","杂费名称","当月金额","当月金额","当月金额","当月金额","当月金额","当月金额","累计金额","累计金额","累计金额","累计金额","累计金额","累计金额"};
			 ArrHeader[1]=new String[] {"单位名称","杂费名称","本期费用","本期<br>吨煤<br>费用","同期费用","同期<br>吨煤<br>费用","费用同比","吨煤<br>费用<br>同比","本期费用","本期<br>吨煤<br>费用","同期费用","同期<br>吨煤<br>费用","费用同比","吨煤<br>费用<br>同比"};
			 
			 ArrWidth=new int[] {100,100,80,60,80,60,80,60,80,60,80,60,80,60};
//			 iFixedRows=1;
			 arrFormat=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
		
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// 数据
			Table tb = new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(intyear+"年"+intMonth+"月杂费查询表", ArrWidth);
			String[] str=getTreeid().split(",");
			if(str.length>1){
				rt.setDefaultTitle(1, 4, "填报单位(盖章):组合电厂", Table.ALIGN_LEFT);
			}else{
				rt.setDefaultTitle(1, 4, "填报单位(盖章):"+getDiancmcById(str[0]), Table.ALIGN_LEFT);
			}
			rt.setDefaultTitle(6, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(12, 3, "单位:元、元/吨", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.merge(2,1,rt.body.getRows(),1);   //对第一列进行合并
			rt.body.merge(1,1,2,rt.body.getCols());   //对第一、二行进行合并
			rt.body.ShowZero = true;
			rt.body.setColFormat(arrFormat);
		 
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(14,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
	}
	
	private String getMingx(){

		StringBuffer strSQL= new StringBuffer();
		_CurrentPage=1;
		_AllPages=1;
		
		JDBCcon cn = new JDBCcon();
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}
		
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String arrFormat[]=null;
		
		strSQL.append("select decode(grouping(dc.mingc) + grouping(fx.zfmc),1,dc.mingc || '合计',dc.mingc) dc,\n"+
			      " decode(grouping(fx.zfmc), 1, '小计', fx.zfmc) zf,\n"+
			      " sum(benq_by.jine) jine_bq_by,\n"+
			      " sum(tongq_by.jine) jine_tq_by,\n"+
			      "  (sum(nvl(benq_by.jine,0))-sum(nvl(tongq_by.jine,0)))jine_tb_by,\n"+
			      " sum(benq_lj.jine) jine_bq_lj,\n"+
			      " sum(tongq_lj.jine) jine_tq_lj,\n"+
                 "(sum(nvl(benq_lj.jine,0))- sum(nvl(tongq_lj.jine,0))) jine_tb_lj\n"+
			 " from (select diancxxb_id, zf.mingc zfmc\n"+
			      "    from (select distinct diancxxb_id, z.mingc\n"+
			      "            from zafb z\n"+
			      "           where ((z.riq >= date '"+intyear+"-1-1' and z.riq <= date'"+intyear+"-"+intMonth+"-01') or\n"+
			      "                 (z.riq >= last_year_today(date '"+intyear+"-1-1') and\n"+
			      "                 z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')))) zf) fx,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq = date '"+intyear+"-"+intMonth+"-01'\n"+
			      "   group by z.diancxxb_id, z.mingc) benq_by,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "   from zafb z\n"+
			      "   where z.riq >= date '"+intyear+"-1-1'\n"+
			      "     and z.riq <= date '"+intyear+"-"+intMonth+"-01'\n"+
			      "   group by z.diancxxb_id, z.mingc) benq_lj,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq = last_year_today(date '"+intyear+"-"+intMonth+"-01')\n"+
			      "   group by z.diancxxb_id, z.mingc) tongq_by,\n"+
			      " (select z.diancxxb_id, z.mingc zafmc, sum(z.jine) jine\n"+
			      "    from zafb z\n"+
			      "   where z.riq >= last_year_today(date '"+intyear+"-1-1')\n"+
			      "     and z.riq <= last_year_today(date '"+intyear+"-"+intMonth+"-01')\n"+
			      "   group by z.diancxxb_id, z.mingc) tongq_lj,diancxxb dc\n"+
			 "where fx.diancxxb_id = benq_by.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = benq_lj.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = tongq_by.diancxxb_id(+)\n"+
			 "  and fx.diancxxb_id = tongq_lj.diancxxb_id(+)\n"+
			 "  and fx.zfmc = benq_by.zafmc(+)\n"+
			 "  and fx.zfmc = benq_lj.zafmc(+)\n"+
			 "  and fx.zfmc = tongq_by.zafmc(+)\n"+
			 "  and fx.zfmc = tongq_lj.zafmc(+)\n"+
			 "  and (dc.id in( "+getDiancxxb_id()+"))\n"+
			 "  and fx.diancxxb_id = dc.id(+)\n"+
			 "group by rollup((dc.mingc, dc.xuh), fx.zfmc)\n"+
			"having not grouping(dc.mingc) + grouping(fx.zfmc) = 2\n"+
			" order by grouping(dc.mingc) desc,dc.mingc,grouping(fx.zfmc) desc,fx.zfmc");
			 ArrHeader=new String[2][6];
			 ArrHeader[0]=new String[] {"单位名称","杂费名称","当月金额","当月金额","当月金额","累计金额","累计金额","累计金额"};
			 ArrHeader[1]=new String[] {"单位名称","杂费名称","本期","同期","同比","本期","同期","同比"};
			 
			 ArrWidth=new int[] {150,100,80,80,80,80,80,80};
			 arrFormat=new String[]{"","","0.000","0.000","0.000","0.000","0.000","0.000"};
		
			ResultSet rs = cn.getResultSet(strSQL.toString());
			 
			// 数据
			Table tb = new Table(rs,2, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(intyear+"年"+intMonth+"月杂费查询表", ArrWidth);
			rt.setDefaultTitle(1, 3, "填报单位(盖章):"+getDiancmcById(getDiancxxb_id()), Table.ALIGN_LEFT);

			rt.setDefaultTitle(4, 3, "填报日期:"+intyear+"年"+intMonth+"月", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(15, 3, "单位:元", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.merge(2,1,rt.body.getRows(),1);   //对第一列进行合并
			rt.body.merge(1,1,2,rt.body.getCols());   //对第一、二行进行合并
			rt.body.ShowZero = false;
			rt.body.setColFormat(arrFormat);
		 
			rt.body.setColAlign(1, Table.ALIGN_CENTER);
			rt.body.setColAlign(2, Table.ALIGN_CENTER);
			
			//页脚 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"审核:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(14,3,"制表:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
	}
	
//	电厂名称
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
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
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);

	}
	
//	年份
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
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * 月份
	 */
	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
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

	
//	***************************报表初始设置***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
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
	 //	页面判定方法
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}

//	得到电厂的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
    public int getDiancmcJib(String diancId){
    	JDBCcon con = new JDBCcon();
    	int jib = -1;
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.jib from diancxxb d where d.id = '"+diancId+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	jib = rsl.getInt("jib");
    	con.Close();
    	return jib;
    }
    
    public String getDiancmcById(String diancid){
    	JDBCcon con = new JDBCcon();
    	String quanc = "";
    	ResultSetList rsl = new ResultSetList();
    	StringBuffer sqlJib = new StringBuffer();
    	sqlJib.append("select d.quanc from diancxxb d where d.id = '"+diancid+"'");
    	rsl = con.getResultSetList(sqlJib.toString());
    	rsl.next();
    	quanc = rsl.getString("quanc");
    	con.Close();
    	return quanc;
    }

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),null,true);
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		String[] str=getTreeid().split(",");
		if(str.length>1){
			tf.setValue("组合电厂");
		}else{
			tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
		}

		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
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
		if(null==getDiancxxb_id()||"".equals(getDiancxxb_id())){   //新窗口不显示工具栏
			return getToolbar().getRenderScript()+getOtherScript("diancTree");
		}else{
			return "";
		}
	}

//	增加电厂多选树的级联
	public String getOtherScript(String treeid){
		String str=" var "+treeid+"_history=\"\";\n"+
			treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
				"    if(checked){\n" + 
				"      addNode(node);\n" + 
				"    }else{\n" + 
				"      subNode(node);\n" + 
				"    }\n" + 
				"    node.expand();\n" + 
				"    node.attributes.checked = checked;\n" + 
				"    node.eachChild(function(child) {\n" + 
				"      if(child.attributes.checked != checked){\n" + 
				"        if(checked){\n" + 
				"          addNode(child);\n" + 
				"        }else{\n" + 
				"          subNode(child);\n" + 
				"        }\n" + 
				"        child.ui.toggleCheck(checked);\n" + 
				"              child.attributes.checked = checked;\n" + 
				"              child.fireEvent('checkchange', child, checked);\n" + 
				"      }\n" + 
				"    });\n" + 
				"  },"+treeid+"_treePanel);\n" + 
				"  function addNode(node){\n" + 
				"    var history = '+,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"\n" + 
				"  function subNode(node){\n" + 
				"    var history = '-,'+node.id+\";\";\n" + 
				"    writesrcipt(node,history);\n" + 
				"  }\n" + 
				"function writesrcipt(node,history){\n" +
				"\t\tif("+treeid+"_history==\"\"){\n" + 
				"\t\t\t"+treeid+"_history = history;\n" + 
				//" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
				"\t\t}else{\n" + 
				"\t\t\tvar his = "+treeid+"_history.split(\";\");\n" + 
				"\t\t\tvar reset = false;\n" + 
				"\t\t\tfor(i=0;i<his.length;i++){\n" + 
				"\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" + 
				"\t\t\t\t\this[i] = \"\";\n" + 
				"\t\t\t\t\treset = true;\n" + 
				"\t\t\t\t\tbreak;\n" + 
				"\t\t\t\t}\n" + 
				"\t\t\t}\n" + 
				"\t\tif(reset){\n" + 
				"\t\t\t  "+treeid+"_history = his.join(\";\");\n" + 
				//"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }else{\n" + 
				"      	 "+treeid+"_history += history;\n" + 
				//"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" + 
				"    }\n" + 
				"  }\n" + 
				"\n" + 
				"}";
		return str;
	}
	
//	初始化多选电厂树中的默认值
	private void initDiancTree(){
		Visit visit = (Visit) getPage().getVisit();
		String sql="SELECT ID\n" +
			"  FROM DIANCXXB\n" + 
			" WHERE JIB > 2\n" + 
			" START WITH ID = "+visit.getDiancxxb_id()+"\n" + 
			"CONNECT BY FUID = PRIOR ID";

		JDBCcon con=new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql); 
		String TreeID="";
		while(rsl.next()){
			TreeID+=rsl.getString("ID")+",";
		}
		if(TreeID.length()>1){
			TreeID=TreeID.substring(0, TreeID.length()-1);
			setTreeid(TreeID);
		}else{
			setTreeid(visit.getDiancxxb_id() + "");
		}
		rsl.close();
		con.Close();
	}
	
//	private String treeid;

	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
}