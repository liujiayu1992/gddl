package com.zhiren.jt.zdt.shengcdy.kuccbb;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
/*
 * 作者：王磊
 * 时间：2009-11-30
 * 描述：增加对分公司的左连接 以便没有分公司时可查询数据。
 */
/*
 * 作者：陈泽天
 * 时间：2010-01-21 
 * 描述：修改关于报表的A4纸的格式问题，
 * 		 把报表的格式设定为A4打印形式。
 */

/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */

public class Kuccbb extends BasePage implements PageValidateListener{
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

	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.quanc as mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}
	private String userName="";
	
	public void setUserName(String value) {
		userName=((Visit) getPage().getVisit()).getRenymc();
	}
	public String getUserName() {
		return userName;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	
	//开始日期v
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue =DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange=true;
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
	
	private boolean _ShangcChick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if(_ShangcChick){
			_ShangcChick=false;
		}
	}
	

	private void Refurbish() {
        //为"刷新" 按钮添加处理程序
		isBegin=true;
		getSelectData();
	}

	
//******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());

			_BeginriqValue = DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay);
			isBegin=true;
			visit.setList1(null);
			visit.setDropDownBean1(null);
			visit.setProSelectionModel1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.setTreeid(null);
			
			//begin方法里进行初始化设置
			visit.setString1(null);

				String pagewith = cycle.getRequestContext().getParameter("pw");// 判断是否有特殊设置
				if (pagewith != null) {

					visit.setString1(pagewith);
				}
			//	visit.setString1(null);保存传递的非默认纸张的样式
				
				
//			this.getShouhcrb();
//			this.getSelectData();
			
		}
		if (_BeginriqChange){
			_BeginriqChange=false;
			Refurbish();
		}
//		if(_Baoblxchange){
//			_Baoblxchange=false;
//			Refurbish();
//		}
		if(_fengschange){
			_fengschange=false;
			Refurbish();
		}
		this.getSelectData();
		setUserName(visit.getRenymc());
		getToolBars();
		Refurbish();
	}
	private String RT_HET="zhuangcyb";
	private String mstrReportName="zhuangcyb";
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

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;

//	发电集团电煤信息日报表
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		JDBCcon cn = new JDBCcon();
		String riq=OraDate(_BeginriqValue);//当前日期
		String riq1=FormatDate(_BeginriqValue);
		//报表表头定义
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		String titlename="库存成本";
		int iFixedRows=0;//固定行号
		int iCol=0;//列数
		String danwmc="";//汇总名称
		String strGongsID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";
			danwmc="集团公司合计";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			danwmc=getTreeDiancmc(this.getTreeid())+"合计";
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			 
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		//danwmc=getTreeDiancmc(this.getTreeid());
		//报表内容
				titlename=titlename+"";
		if(jib==1){//选择集团的时候按照分公司统计,其它的按照到达类型统计
			strSQL=
					"SELECT SUM(ROUND(V.RULSLYM,2)),\n" +
							"       SUM(ROUND(V.BUHSDJYM,2)),\n" +
							"       SUM(ROUND(V.BUHSJEYM,2)),\n" +
							"       SUM(ROUND(V.RULSLMN,2)),\n" +
							"       SUM(ROUND(V.BUHSDJMN,2)),\n" +
							"       SUM(ROUND(V.BUHSJEMN,2)),\n" +
							"       ROUND(V.RULMLHJ,2),\n" +
							"       ROUND(V.MT,2),\n" +
							"       ROUND(V.AD,2),\n" +
							"       ROUND(V.VDAF,2),\n" +
							"       ROUND(V.STAD,2),\n" +
							"       ROUND(V.BUHSJEHJ,2),\n" +
							"       ROUND(V.BUHSDJHJ,2),\n" +
							"       ROUND(V.MEIZBMDJ,2)\n" +
							"  FROM (SELECT DECODE(A.MEIZ, '原煤', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEYM,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEMN,\n" +
							"               SUM(NVL(B.RULMLHJ, 0)) AS RULMLHJ,\n" +
							"               SUM(NVL(C.MT, 0)) AS MT,\n" +
							"               SUM(NVL(C.AD, 0)) AS AD,\n" +
							"               SUM(NVL(C.VDAF, 0)) AS VDAF,\n" +
							"               SUM(NVL(C.STAD, 0)) AS STAD,\n" +
							"               SUM(NVL(B.BUHSJEHJ, 0)) AS BUHSJEHJ,\n" +
							"               SUM(NVL(B.BUHSDJHJ, 0)) AS BUHSDJHJ,\n" +
							"               SUM(NVL(B.MEIZBMDJ, 0)) AS MEIZBMDJ\n" +
							"          FROM (SELECT L.ID,\n" +
							"                       L.RIQ,\n" +
							"                       L.RULRZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          '原煤'\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          '煤泥'\n" +
							"                       END AS MEIZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                       END AS RULSL,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                       END AS BUHSDJ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                       END AS BUHSJE\n" +
							"                  FROM KUCCBB L,\n" +
							"                       (SELECT CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                               END AS KUCJE,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                               END AS KUCSL,\n" +
							"                               M.RUKDJ,\n" +
							"                               M.RULSL,\n" +
							"                               M.RULTZSL\n" +
							"                          FROM KUCCBB M\n" +
							"                         WHERE M.RIQ =  " + riq + "  - 1\n" +
							"                         GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                 WHERE L.RIQ =  " + riq + " \n" +
							"                   AND L.MEIZ = N.MEIZ(+)\n" +
							"                 GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) A,\n" +
							"               (SELECT SUM(NVL(RULSL, 0)) AS RULMLHJ,\n" +
							"                       SUM(NVL(BUHSJE, 0)) AS BUHSJEHJ,\n" +
							"                       DECODE(SUM(NVL(RULSL, 0)),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(NVL(BUHSJE, 0)) / SUM(NVL(RULSL, 0))) AS BUHSDJHJ,\n" +
							"                       SUM(NVL(RULSL, 0)) * SUM(NVL(RULRZ, 0)) / 7000 AS MEIZBMDJ\n" +
							"                  FROM (SELECT L.ID,\n" +
							"                               L.RIQ,\n" +
							"                               L.RULRZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                               END AS RULSL,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                               END AS BUHSDJ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                               END AS BUHSJE\n" +
							"                          FROM KUCCBB L,\n" +
							"                               (SELECT CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          '原煤'\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          '煤泥'\n" +
							"                                       END AS MEIZ,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                       END AS KUCJE,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                       END AS KUCSL,\n" +
							"                                       M.RUKDJ,\n" +
							"                                       M.RULSL,\n" +
							"                                       M.RULTZSL\n" +
							"                                  FROM KUCCBB M\n" +
							"                                 WHERE M.RIQ =\n" +
							"                                        " + riq + "  - 1\n" +
							"                                 GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                         WHERE L.RIQ =  " + riq + " \n" +
							"                           AND L.MEIZ = N.MEIZ(+)\n" +
							"                         GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) TEMP_KUCCBB_VM) B,\n" +
							"               (SELECT X.RULRQ AS RIQ,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.MT * Y.MEIL) / SUM(Y.MEIL)) AS MT,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.AD * Y.MEIL) / SUM(Y.MEIL)) AS AD,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.VDAF * Y.MEIL) / SUM(Y.MEIL)) AS VDAF,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.STAD * Y.MEIL) / SUM(Y.MEIL)) AS STAD\n" +
							"                  FROM MEIHYB X,\n" +
							"                       (SELECT *\n" +
							"                          FROM RULMZLB Y1\n" +
							"                         WHERE Y1.RULRQ =  " + riq + " ) Y\n" +
							"                 WHERE X.RULRQ =  " + riq + " \n" +
							"                   AND X.RULMZLB_ID = Y.ID(+)\n" +
							"                 GROUP BY X.RULRQ) C\n" +
							"         WHERE A.RIQ =  " + riq + " \n" +
							"           AND A.RIQ = C.RIQ(+)\n" +
							"         GROUP BY A.MEIZ, A.RULRZ) V,\n" +
							"       (SELECT DECODE(A.MEIZ, '原煤', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEYM,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEMN,\n" +
							"               SUM(NVL(B.RULMLHJ, 0)) AS RULMLHJ,\n" +
							"               SUM(NVL(C.MT, 0)) AS MT,\n" +
							"               SUM(NVL(C.AD, 0)) AS AD,\n" +
							"               SUM(NVL(C.VDAF, 0)) AS VDAF,\n" +
							"               SUM(NVL(C.STAD, 0)) AS STAD,\n" +
							"               SUM(NVL(B.BUHSJEHJ, 0)) AS BUHSJEHJ,\n" +
							"               SUM(NVL(B.BUHSDJHJ, 0)) AS BUHSDJHJ,\n" +
							"               SUM(NVL(B.MEIZBMDJ, 0)) AS MEIZBMDJ\n" +
							"          FROM (SELECT L.ID,\n" +
							"                       L.RIQ,\n" +
							"                       L.RULRZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          '原煤'\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          '煤泥'\n" +
							"                       END AS MEIZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                       END AS RULSL,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                       END AS BUHSDJ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                       END AS BUHSJE\n" +
							"                  FROM KUCCBB L,\n" +
							"                       (SELECT CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                               END AS KUCJE,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                               END AS KUCSL,\n" +
							"                               M.RUKDJ,\n" +
							"                               M.RULSL,\n" +
							"                               M.RULTZSL\n" +
							"                          FROM KUCCBB M\n" +
							"                         WHERE M.RIQ =  " + riq + "  - 1\n" +
							"                         GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                 WHERE L.RIQ =  " + riq + " \n" +
							"                   AND L.MEIZ = N.MEIZ(+)\n" +
							"                 GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) A,\n" +
							"               (SELECT SUM(NVL(RULSL, 0)) AS RULMLHJ,\n" +
							"                       SUM(NVL(BUHSJE, 0)) AS BUHSJEHJ,\n" +
							"                       DECODE(SUM(NVL(RULSL, 0)),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(NVL(BUHSJE, 0)) / SUM(NVL(RULSL, 0))) AS BUHSDJHJ,\n" +
							"                       SUM(NVL(RULSL, 0)) * SUM(NVL(RULRZ, 0)) / 7000 AS MEIZBMDJ\n" +
							"                  FROM (SELECT L.ID,\n" +
							"                               L.RIQ,\n" +
							"                               L.RULRZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                               END AS RULSL,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                               END AS BUHSDJ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                               END AS BUHSJE\n" +
							"                          FROM KUCCBB L,\n" +
							"                               (SELECT CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          '原煤'\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          '煤泥'\n" +
							"                                       END AS MEIZ,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                       END AS KUCJE,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                       END AS KUCSL,\n" +
							"                                       M.RUKDJ,\n" +
							"                                       M.RULSL,\n" +
							"                                       M.RULTZSL\n" +
							"                                  FROM KUCCBB M\n" +
							"                                 WHERE M.RIQ =\n" +
							"                                        " + riq + "  - 1\n" +
							"                                 GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                         WHERE L.RIQ =  " + riq + " \n" +
							"                           AND L.MEIZ = N.MEIZ(+)\n" +
							"                         GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) TEMP_KUCCBB_VM) B,\n" +
							"               (SELECT X.RULRQ AS RIQ,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.MT * Y.MEIL) / SUM(Y.MEIL)) AS MT,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.AD * Y.MEIL) / SUM(Y.MEIL)) AS AD,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.VDAF * Y.MEIL) / SUM(Y.MEIL)) AS VDAF,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.STAD * Y.MEIL) / SUM(Y.MEIL)) AS STAD\n" +
							"                  FROM MEIHYB X,\n" +
							"                       (SELECT *\n" +
							"                          FROM RULMZLB Y1\n" +
							"                         WHERE Y1.RULRQ =  " + riq + " ) Y\n" +
							"                 WHERE X.RULRQ =  " + riq + " \n" +
							"                   AND X.RULMZLB_ID = Y.ID(+)\n" +
							"                 GROUP BY X.RULRQ) C\n" +
							"         WHERE A.RIQ =  " + riq + " \n" +
							"           AND A.RIQ = C.RIQ(+)\n" +
							"         GROUP BY A.MEIZ, A.RULRZ) V2\n" +
							" WHERE V.RULMLHJ = V2.RULMLHJ\n" +
							"   AND V.MT = V2.MT\n" +
							"   AND V.AD = V2.AD\n" +
							"   AND V.VDAF = V2.VDAF\n" +
							"   AND V.STAD = V2.STAD\n" +
							"   AND V.BUHSJEHJ = V2.BUHSJEHJ\n" +
							"   AND V.BUHSDJHJ = V2.BUHSDJHJ\n" +
							"   AND V.MEIZBMDJ = V2.MEIZBMDJ\n" +
							" GROUP BY V.RULMLHJ,\n" +
							"          V.MT,\n" +
							"          V.AD,\n" +
							"          V.VDAF,\n" +
							"          V.STAD,\n" +
							"          V.BUHSJEHJ,\n" +
							"          V.BUHSDJHJ,\n" +
							"          V.MEIZBMDJ";
		}else{
			strSQL=
					"SELECT SUM(ROUND(V.RULSLYM,2)),\n" +
							"       SUM(ROUND(V.BUHSDJYM,2)),\n" +
							"       SUM(ROUND(V.BUHSJEYM,2)),\n" +
							"       SUM(ROUND(V.RULSLMN,2)),\n" +
							"       SUM(ROUND(V.BUHSDJMN,2)),\n" +
							"       SUM(ROUND(V.BUHSJEMN,2)),\n" +
							"       ROUND(V.RULMLHJ,2),\n" +
							"       ROUND(V.MT,2),\n" +
							"       ROUND(V.AD,2),\n" +
							"       ROUND(V.VDAF,2),\n" +
							"       ROUND(V.STAD,2),\n" +
							"       ROUND(V.BUHSJEHJ,2),\n" +
							"       ROUND(V.BUHSDJHJ,2),\n" +
							"       ROUND(V.MEIZBMDJ,2)\n" +
							"  FROM (SELECT DECODE(A.MEIZ, '原煤', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEYM,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEMN,\n" +
							"               SUM(NVL(B.RULMLHJ, 0)) AS RULMLHJ,\n" +
							"               SUM(NVL(C.MT, 0)) AS MT,\n" +
							"               SUM(NVL(C.AD, 0)) AS AD,\n" +
							"               SUM(NVL(C.VDAF, 0)) AS VDAF,\n" +
							"               SUM(NVL(C.STAD, 0)) AS STAD,\n" +
							"               SUM(NVL(B.BUHSJEHJ, 0)) AS BUHSJEHJ,\n" +
							"               SUM(NVL(B.BUHSDJHJ, 0)) AS BUHSDJHJ,\n" +
							"               SUM(NVL(B.MEIZBMDJ, 0)) AS MEIZBMDJ\n" +
							"          FROM (SELECT L.ID,\n" +
							"                       L.RIQ,\n" +
							"                       L.RULRZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          '原煤'\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          '煤泥'\n" +
							"                       END AS MEIZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                       END AS RULSL,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                       END AS BUHSDJ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                       END AS BUHSJE\n" +
							"                  FROM KUCCBB L,\n" +
							"                       (SELECT CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                               END AS KUCJE,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                               END AS KUCSL,\n" +
							"                               M.RUKDJ,\n" +
							"                               M.RULSL,\n" +
							"                               M.RULTZSL\n" +
							"                          FROM KUCCBB M\n" +
							"                         WHERE M.RIQ =  " + riq + "  - 1\n" +
							"                         GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                 WHERE L.RIQ =  " + riq + " \n" +
							"                   AND L.MEIZ = N.MEIZ(+)\n" +
							"                 GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) A,\n" +
							"               (SELECT SUM(NVL(RULSL, 0)) AS RULMLHJ,\n" +
							"                       SUM(NVL(BUHSJE, 0)) AS BUHSJEHJ,\n" +
							"                       DECODE(SUM(NVL(RULSL, 0)),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(NVL(BUHSJE, 0)) / SUM(NVL(RULSL, 0))) AS BUHSDJHJ,\n" +
							"                       SUM(NVL(RULSL, 0)) * SUM(NVL(RULRZ, 0)) / 7000 AS MEIZBMDJ\n" +
							"                  FROM (SELECT L.ID,\n" +
							"                               L.RIQ,\n" +
							"                               L.RULRZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                               END AS RULSL,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                               END AS BUHSDJ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                               END AS BUHSJE\n" +
							"                          FROM KUCCBB L,\n" +
							"                               (SELECT CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          '原煤'\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          '煤泥'\n" +
							"                                       END AS MEIZ,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                       END AS KUCJE,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                       END AS KUCSL,\n" +
							"                                       M.RUKDJ,\n" +
							"                                       M.RULSL,\n" +
							"                                       M.RULTZSL\n" +
							"                                  FROM KUCCBB M\n" +
							"                                 WHERE M.RIQ =\n" +
							"                                        " + riq + "  - 1\n" +
							"                                 GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                         WHERE L.RIQ =  " + riq + " \n" +
							"                           AND L.MEIZ = N.MEIZ(+)\n" +
							"                         GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) TEMP_KUCCBB_VM) B,\n" +
							"               (SELECT X.RULRQ AS RIQ,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.MT * Y.MEIL) / SUM(Y.MEIL)) AS MT,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.AD * Y.MEIL) / SUM(Y.MEIL)) AS AD,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.VDAF * Y.MEIL) / SUM(Y.MEIL)) AS VDAF,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.STAD * Y.MEIL) / SUM(Y.MEIL)) AS STAD\n" +
							"                  FROM MEIHYB X,\n" +
							"                       (SELECT *\n" +
							"                          FROM RULMZLB Y1\n" +
							"                         WHERE Y1.RULRQ =  " + riq + " ) Y\n" +
							"                 WHERE X.RULRQ =  " + riq + " \n" +
							"                   AND X.RULMZLB_ID = Y.ID(+)\n" +
							"                 GROUP BY X.RULRQ) C\n" +
							"         WHERE A.RIQ =  " + riq + " \n" +
							"           AND A.RIQ = C.RIQ(+)\n" +
							"         GROUP BY A.MEIZ, A.RULRZ) V,\n" +
							"       (SELECT DECODE(A.MEIZ, '原煤', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJYM,\n" +
							"               DECODE(A.MEIZ, '原煤', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEYM,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.RULSL, 0)), NULL) AS RULSLMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSDJ, 0)), NULL) AS BUHSDJMN,\n" +
							"               DECODE(A.MEIZ, '煤泥', SUM(NVL(A.BUHSJE, 0)), NULL) AS BUHSJEMN,\n" +
							"               SUM(NVL(B.RULMLHJ, 0)) AS RULMLHJ,\n" +
							"               SUM(NVL(C.MT, 0)) AS MT,\n" +
							"               SUM(NVL(C.AD, 0)) AS AD,\n" +
							"               SUM(NVL(C.VDAF, 0)) AS VDAF,\n" +
							"               SUM(NVL(C.STAD, 0)) AS STAD,\n" +
							"               SUM(NVL(B.BUHSJEHJ, 0)) AS BUHSJEHJ,\n" +
							"               SUM(NVL(B.BUHSDJHJ, 0)) AS BUHSDJHJ,\n" +
							"               SUM(NVL(B.MEIZBMDJ, 0)) AS MEIZBMDJ\n" +
							"          FROM (SELECT L.ID,\n" +
							"                       L.RIQ,\n" +
							"                       L.RULRZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          '原煤'\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          '煤泥'\n" +
							"                       END AS MEIZ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                       END AS RULSL,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          DECODE((SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                 0,\n" +
							"                                 0,\n" +
							"                                 (SUM(NVL(N.KUCJE, 0)) + SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                 (SUM(NVL(N.KUCSL, 0)) + SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                 SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                       END AS BUHSDJ,\n" +
							"                       CASE\n" +
							"                         WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                         WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                          SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                          SUM(NVL(N.RULTZSL, 0))\n" +
							"                       END AS BUHSJE\n" +
							"                  FROM KUCCBB L,\n" +
							"                       (SELECT CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCJE, 0))\n" +
							"                               END AS KUCJE,\n" +
							"                               CASE\n" +
							"                                 WHEN M.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                                 WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(M.KUCSL, 0))\n" +
							"                               END AS KUCSL,\n" +
							"                               M.RUKDJ,\n" +
							"                               M.RULSL,\n" +
							"                               M.RULTZSL\n" +
							"                          FROM KUCCBB M\n" +
							"                         WHERE M.RIQ =  " + riq + "  - 1\n" +
							"                         GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                 WHERE L.RIQ =  " + riq + " \n" +
							"                   AND L.MEIZ = N.MEIZ(+)\n" +
							"                 GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) A,\n" +
							"               (SELECT SUM(NVL(RULSL, 0)) AS RULMLHJ,\n" +
							"                       SUM(NVL(BUHSJE, 0)) AS BUHSJEHJ,\n" +
							"                       DECODE(SUM(NVL(RULSL, 0)),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(NVL(BUHSJE, 0)) / SUM(NVL(RULSL, 0))) AS BUHSDJHJ,\n" +
							"                       SUM(NVL(RULSL, 0)) * SUM(NVL(RULRZ, 0)) / 7000 AS MEIZBMDJ\n" +
							"                  FROM (SELECT L.ID,\n" +
							"                               L.RIQ,\n" +
							"                               L.RULRZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  '原煤'\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  '煤泥'\n" +
							"                               END AS MEIZ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(L.RULSL, 0)) + SUM(NVL(L.RULTZSL, 0))\n" +
							"                               END AS RULSL,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  DECODE((SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))),\n" +
							"                                         0,\n" +
							"                                         0,\n" +
							"                                         (SUM(NVL(N.KUCJE, 0)) +\n" +
							"                                         SUM(NVL(L.RUKJE, 0))) /\n" +
							"                                         (SUM(NVL(N.KUCSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKSL, 0)) +\n" +
							"                                         SUM(NVL(L.RUKTZSL, 0))))\n" +
							"                               END AS BUHSDJ,\n" +
							"                               CASE\n" +
							"                                 WHEN L.MEIZ = '原煤' AND N.MEIZ = '原煤' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                                 WHEN L.MEIZ = '煤泥' AND N.MEIZ = '煤泥' THEN\n" +
							"                                  SUM(NVL(N.RUKDJ, 0)) * SUM(NVL(N.RULSL, 0)) +\n" +
							"                                  SUM(NVL(N.RULTZSL, 0))\n" +
							"                               END AS BUHSJE\n" +
							"                          FROM KUCCBB L,\n" +
							"                               (SELECT CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          '原煤'\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          '煤泥'\n" +
							"                                       END AS MEIZ,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCJE, 0))\n" +
							"                                       END AS KUCJE,\n" +
							"                                       CASE\n" +
							"                                         WHEN M.MEIZ = '原煤' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                         WHEN M.MEIZ = '煤泥' THEN\n" +
							"                                          SUM(NVL(M.KUCSL, 0))\n" +
							"                                       END AS KUCSL,\n" +
							"                                       M.RUKDJ,\n" +
							"                                       M.RULSL,\n" +
							"                                       M.RULTZSL\n" +
							"                                  FROM KUCCBB M\n" +
							"                                 WHERE M.RIQ =\n" +
							"                                        " + riq + "  - 1\n" +
							"                                 GROUP BY M.MEIZ, M.RUKDJ, M.RULSL, M.RULTZSL) N\n" +
							"                         WHERE L.RIQ =  " + riq + " \n" +
							"                           AND L.MEIZ = N.MEIZ(+)\n" +
							"                         GROUP BY L.ID, L.RIQ, L.MEIZ, N.MEIZ, L.RULRZ) TEMP_KUCCBB_VM) B,\n" +
							"               (SELECT X.RULRQ AS RIQ,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.MT * Y.MEIL) / SUM(Y.MEIL)) AS MT,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.AD * Y.MEIL) / SUM(Y.MEIL)) AS AD,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.VDAF * Y.MEIL) / SUM(Y.MEIL)) AS VDAF,\n" +
							"                       DECODE(SUM(Y.MEIL),\n" +
							"                              0,\n" +
							"                              0,\n" +
							"                              SUM(Y.STAD * Y.MEIL) / SUM(Y.MEIL)) AS STAD\n" +
							"                  FROM MEIHYB X,\n" +
							"                       (SELECT *\n" +
							"                          FROM RULMZLB Y1\n" +
							"                         WHERE Y1.RULRQ =  " + riq + " ) Y\n" +
							"                 WHERE X.RULRQ =  " + riq + " \n" +
							"                   AND X.RULMZLB_ID = Y.ID(+)\n" +
							"                 GROUP BY X.RULRQ) C\n" +
							"         WHERE A.RIQ =  " + riq + " \n" +
							"           AND A.RIQ = C.RIQ(+)\n" +
							"         GROUP BY A.MEIZ, A.RULRZ) V2\n" +
							" WHERE V.RULMLHJ = V2.RULMLHJ\n" +
							"   AND V.MT = V2.MT\n" +
							"   AND V.AD = V2.AD\n" +
							"   AND V.VDAF = V2.VDAF\n" +
							"   AND V.STAD = V2.STAD\n" +
							"   AND V.BUHSJEHJ = V2.BUHSJEHJ\n" +
							"   AND V.BUHSDJHJ = V2.BUHSDJHJ\n" +
							"   AND V.MEIZBMDJ = V2.MEIZBMDJ\n" +
							" GROUP BY V.RULMLHJ,\n" +
							"          V.MT,\n" +
							"          V.AD,\n" +
							"          V.VDAF,\n" +
							"          V.STAD,\n" +
							"          V.BUHSJEHJ,\n" +
							"          V.BUHSDJHJ,\n" +
							"          V.MEIZBMDJ";

		}

/*
		 ArrHeader =new String[2][15];
		 ArrHeader[0]=new String[] {"原煤","原煤","原煤",
				 "煤泥","煤泥","煤泥",
				 "合计","合计","合计","合计","合计","合计","合计","合计","合计"};
		 ArrHeader[1]=new String[] {"入炉煤量（吨）","不含税单价（元/吨）","不含税金额(元)",
				 "入炉煤量（吨）","不含税单价（元/吨）","不含税金额(元)",
				 "合计入炉煤量（吨）","入炉煤热值（kcal/kg）","收到基全水份（%）","干燥基灰分（%）","干燥无灰基挥发分（%）","干燥基全硫（%）","不含税单价（元/吨）","不含税金额(元)","煤折标煤单价（元/吨）"};
		 ArrWidth =new int[] {150,120,120,100,100,100,100,100,100,100,100,100,100,100,100};
*/
		 ArrHeader =new String[2][14];
		 ArrHeader[0]=new String[] {"原煤","原煤","原煤","煤泥","煤泥","煤泥","合计","合计","合计","合计","合计","合计","合计","合计"};
		 ArrHeader[1]=new String[] {"入炉煤量（吨）","不含税单价（元/吨）","不含税金额(元)",
				 "入炉煤量（吨）","不含税单价（元/吨）","不含税金额(元)",
				 "合计入炉煤量（吨）","入炉煤热值（kcal/kg）","收到基全水份（%）","干燥基灰分（%）","干燥无灰基挥发分（%）","干燥基全硫（%）","不含税单价（元/吨）","不含税金额(元)","煤折标煤单价（元/吨）"};
		 ArrWidth =new int[] {120,120,120,120,120,120,100,100,100,100,100,100,100,100};


			ResultSet rs = cn.getResultSet(strSQL);

			// 数据, IFixedRows定义表头行数.
			rt.setBody(new Table(rs,2, 0, 1));

			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//取得报表纸张类型
			rt.getArrWidth(ArrWidth, aw);//添加报表的A4控制
			rt.setTitle(titlename, ArrWidth);
	    //  rt.setTitle(intyear+"年"+intMonth+"月"+titlename, ArrWidth);
			// iCol: 定义开始的column的位置. iMergeCols: 定义混合列数,即改内容占用几列.
			rt.setDefaultTitle(1, 3,"制表单位:"+getDiancName(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(13, 2, riq1, Table.ALIGN_RIGHT);
		//	rt.setDefaultTitle(5, 2,"单位:吨", Table.ALIGN_RIGHT);

			rt.body.setWidth(ArrWidth);
//			rt.body.setPageRows(12);
			rt.body.setPageRows(rt.PAPER_ROWS);
//			增加长度的拉伸
			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
			rt.body.setHeaderData(ArrHeader);// 表头数据
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
			rt.body.setColAlign(2, Table.ALIGN_LEFT);
			rt.body.setColAlign(3, Table.ALIGN_LEFT);
			//页脚 
			
			  rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,2,"打印日期:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,2,"审核:",Table.ALIGN_LEFT);
			  if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
					
				  rt.setDefautlFooter(13,2, "制表:",Table.ALIGN_RIGHT);
					}else{
						
						rt.setDefautlFooter(13,2, "制表:"+((Visit) getPage().getVisit()).getDiancmc

			(),Table.ALIGN_RIGHT);
					}
//			  rt.setDefautlFooter(5,0,"制表:",Table.ALIGN_RIGHT);
//			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
			
			
			//设置页数
//		    rt.createDefautlFooter(ArrWidth);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
		
	}
//	******************************************************************************

	
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
		_IDiancmcModel = new IDropDownModel("select d.id,d.mingc from diancxxb d order by d.mingc desc");
	}
	
//	矿别名称
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
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
		try{

		
		String sql="";
		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
	}
	
//	矿报表类型
//	public boolean _Baoblxchange = false;
//	public IDropDownBean getBaoblxValue() {
//		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
//			((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getIBaoblxModels().getOption(0));
//		}
//		return ((Visit)getPage().getVisit()).getDropDownBean1();
//	}
//
//	public void setBaoblxValue(IDropDownBean Value) {
//		long id = -2;
//		if (((Visit)getPage().getVisit()).getDropDownBean1() != null) {
//			id = (((Visit)getPage().getVisit()).getDropDownBean1()).getId();
//		}
//		if (Value != null) {
//			if (Value.getId() != id) {
//				_Baoblxchange = true;
//			} else {
//				_Baoblxchange = false;
//			}
//		}
//		((Visit)getPage().getVisit()).setDropDownBean1(Value);
//	}
//
//	public void setIBaoblxModel(IPropertySelectionModel value) {
//		((Visit)getPage().getVisit()).setProSelectionModel1(value);
//	}
//
//	public IPropertySelectionModel getIBaoblxModel() {
//		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
//			getIBaoblxModels();
//		}
//		return ((Visit)getPage().getVisit()).getProSelectionModel1();
//	}
//
//	public IPropertySelectionModel getIBaoblxModels() {
//
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(0,"电监会电煤信息日报"));
//		
//		fahdwList.add(new IDropDownBean(1,"电监会电煤信息日报(省)"));
//		fahdwList.add(new IDropDownBean(2,"电煤电量日报表"));
//		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(fahdwList));
//		
//		return ((Visit)getPage().getVisit()).getProSelectionModel1();
//	}
	
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
//	 分公司下拉框
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
		setDiancxxModel(new IDropDownModel(sql,"中国大唐集团"));
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
	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();
		df.setValue(DateUtil.FormatDate(this.getBeginriqDate()));
		df.Binding("riqDateSelect","forms[0]");// 与html页中的id绑定,并自动刷新
		df.setWidth(100);
		//df.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(df);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
//		tb1.addText(new ToolbarText("报表查询:"));
//		ComboBox cb = new ComboBox();
//		cb.setTransform("BaoblxDropDown");
//		cb.setId("Tongjkj");
//		cb.setWidth(240);
//		tb1.addField(cb);
//		tb1.addText(new ToolbarText("-"));
//		
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
		return getToolbar().getRenderScript();
	}

	private String treeid;
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