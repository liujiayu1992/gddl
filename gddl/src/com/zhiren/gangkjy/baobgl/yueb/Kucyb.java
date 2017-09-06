package com.zhiren.gangkjy.baobgl.yueb;

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

import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.DateUtil;
import com.zhiren.gangkjy.Local;

import org.apache.tapestry.contrib.palette.SortMode;


public class Kucyb  extends BasePage implements PageValidateListener{

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
	
//	绑定日期
	private String briq;

	public String getBRiq() {
		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	
//	绑定日期
	private String eriq;

	public String getERiq() {
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
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
	


	private boolean _FindChick = false;

	public void FindButton(IRequestCycle cycle) {
		_FindChick = true;
	}
	public void submit(IRequestCycle cycle) {
		
		if (_FindChick) {
			_FindChick = false;
	    }
	}

	/**
	 * 发电集团电煤信息日报表
	 * @author xzy
	 */
	private String getSelectData(){
		_CurrentPage=1;
		_AllPages=1;
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		Report rt = new Report();
		StringBuffer strSQL = new StringBuffer();

		String strDianc = "";
		if(this.getDiancTreeJib()==1){//集团
			strDianc = "";
		}else if(this.getDiancTreeJib()==2){
			strDianc = " and (dc.id="+this.getTreeid()+" or dc.fuid="+this.getTreeid()+" ) ";
		}else{
			strDianc = " and dc.id="+this.getTreeid();
		}
		String benqrq = " and d.shij>="+DateUtil.FormatOracleDate(getBRiq())+"\n"
						+"and d.shij<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" ;
//		String leijrq = " and d.riq>=to_date('"+DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBRiq()))+"','yyyy-MM-dd')\n"
		String leijrq = " and d.shij>="+DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(getBRiq()))+"\n"
						+"and d.shij<"+DateUtil.FormatOracleDate(getERiq()) + "+1 \n" ;
		/*String sql = 

			"select --grouping(tb1.duowgs),grouping(tb1.duow), grouping(tb1.leix),\n" +
			"       decode(grouping(tb1.duowgs),1,'合计',tb1.duowgs) as duowgs,\n" + 
			"       tb1.duow as duow,\n" + 
			"       tb1.leix as leix ,\n" + 
			"       sum(t.qkucl) as qkucl,\n" + 
			"       sum(t.shour) as shour,\n" + 
			"       sum(t.quc) as quc,\n" + 
			"       sum(t.yingk) as yingk,\n" + 
			"       sum(t.kuc) as kuc,\n" + 
			"       sum(t.zaitml) as zaitml,\n" + 
			"       sum(t.beiz) as beiz\n" + 
			"        from (\n" + 
			"\n" + 
			"((select dw.mingc as duowgs,\n" + 
			"       v.mingc as duow,\n" + 
			"       nvl('本期','') as leix,\n" + 
			"       qc.kucl as qkucl,\n" + 
			"       sum(d.ruksl) as shour,\n" + 
			"       sum(d.chuksl) as quc,\n" + 
			"       sum(d.panyk) as yingk,\n" + 
			"       kc.kucl as kuc,\n" + 
			"       sum(d.zaitml) as zaitml,\n" + 
			"       null as beiz\n" + 
			"from duowkcb d,meicb v ,diancxxb dc, duowgsb dw,\n" + 
			"(select dw.meicb_id, dw.kucl from\n" + 
			"        (select m.id, m.mingc, max(d.shij) as shij\n" + 
			"         from duowkcb d, meicb m\n" + 
			"                 where m.id=d.meicb_id and  leib<>2 and leix in(1,-1)\n" + 
			benqrq+
			"                  group by (m.mingc,m.id)) ms, duowkcb dw\n" + 
			"          where dw.meicb_id=ms.id and dw.shij=ms.shij\n" + 
			" ) kc,\n" + 
			"(select dw.meicb_id, dw.kucl,dw.shij from\n" + 
			"        (select m.id, m.mingc, max(d.shij) as shij\n" + 
			"         from duowkcb d, meicb m\n" + 
			"                 where m.id=d.meicb_id and  leib<>2 and leix in(1,-1)\n" + 
			"         and d.shij<"+DateUtil.FormatOracleDate(getBRiq())+"\n"+
			"                  group by (m.mingc,m.id)) ms, duowkcb dw\n" + 
			"          where dw.meicb_id=ms.id and dw.shij=ms.shij\n" + 
			"          ) qc\n" + 
			"where d.meicb_id = v.id  and kc.meicb_id(+)=v.id and dw.id=v.duowgsb_id and qc.meicb_id(+)=v.id\n" + 
			"and d.diancxxb_id=dc.id "+strDianc+" and v.mingc<>'直达煤场'\n" + 
			" group by (dw.mingc,v.mingc,kc.kucl,qc.kucl)\n" + 
			")\n" + 
			"union\n" + 
			"(\n" + 
			"select dw.mingc as duowgs,\n" + 
			"       v.mingc as duow,\n" + 
			"       nvl('累计','') as leix,\n" + 
			"       sum(decode(0,0,0)) as qkucl,\n" + 
			"       sum(d.ruksl) as shour,\n" + 
			"       sum(d.chuksl) as quc,\n" + 
			"       sum(d.panyk) as yingk,\n" + 
			"       sum(decode(0,0,0)) as kuc,\n" + 
			"       sum(d.zaitml) as zaitml,\n" + 
			"       null as beiz\n" + 
			"from duowkcb d,meicb v,diancxxb dc, duowgsb dw\n" + 
			"where d.meicb_id = v.id and dw.id=v.duowgsb_id\n" + 
			leijrq+
			" and d.diancxxb_id=dc.id  "+strDianc+"  and v.mingc<>'直达煤场' group by (dw.mingc,v.mingc)\n" + 
			") ))t,\n" + 
			"(select * from (\n" + 
			"select  dw.mingc as duowgs,mc.mingc as duow\n" + 
			"from duowkcb d,meicb mc,diancxxb dc,duowgsb dw\n" + 
			"where d.meicb_id = mc.id and  mc.duowgsb_id=dw.id\n" + 
			"    and d.diancxxb_id=dc.id "+strDianc+"\n" + 
			"and mc.mingc<>'直达煤场'\n" + 
			leijrq+
			" group by (dw.mingc,mc.mingc)\n" + 
			")t1,(select decode(1,1,'本期','本期') as leix from dual union select decode(1,1,'累计','累计') as leix from dual) t2) tb1\n" + 
			"where t.duowgs(+)=tb1.duowgs and t.duow(+)=tb1.duow and t.leix(+)=tb1.leix\n" + 
			" group by rollup (tb1.leix,(tb1.duowgs,tb1.duow))\n" + 
			" having not grouping(tb1.leix)=1\n" + 
			" order by grouping (tb1.duowgs) desc, duowgs,duow,leix";*/

                String sql=
               " select --grouping(tb1.duowgs),grouping(tb1.duow), grouping(tb1.leix),\n"+
               " decode(grouping(tb1.duowgs), 1, '合计', tb1.duowgs) as duowgs,\n"+
              "  tb1.duow as duow,\n"+
              "  tb1.leix as leix,\n"+
              "  sum(t.qkucl) as qkucl,\n"+
              "  sum(t.biaoz) biaoz,\n"+
              "  sum(t.yingk) as yingk,\n"+
                
              "  sum(t.shour) as shour,\n"+
              "  sum(t.quc) as quc,\n"+
                
             "   sum(t.zaitml) as zaitml,\n"+
              "  sum(t.kuc) as kuc,\n"+
              "  sum(t.beiz) as beiz\n"+
             "    from (\n"+
               "        (select dw.mingc as duowgs,\n"+
                "               v.mingc as duow,\n"+
                "               nvl('本期', '') as leix,\n"+
               "                nvl(kc.biaoz, 0) as biaoz,\n"+
                "               nvl(qc.kucl, 0) as qkucl,\n"+
                 "              nvl(kc.ruksl, 0) as shour,\n"+
                "               nvl(kc.chuksl, 0) as quc,\n"+
                "              nvl(kc.panyk, 0) as yingk,\n"+
                "              kc.kucl as kuc,\n"+
                "               nvl(kc.zaitml, 0) as zaitml,\n"+
                "               null as beiz\n"+
                "          from meicb v,   \n"+
                "               duowgsb dw,\n"+
                "               (select dw.meicb_id,\n"+
                 "                      dw.kucl,\n"+
                 "                      dw.id,\n"+
               "                       ms.shij,\n"+
                 "                      ms.ruksl,\n"+
                 "                      ms.chuksl,\n"+
                 "                      ms.panyk,\n"+
                  "                     ms.zaitml,\n"+
                 "                      ms.biaoz\n"+
                 "                 from (select m.id,\n"+
                 "                              m.mingc,\n"+
                 "                              max(d.shij) as shij,\n"+
                  "                             sum(d.ruksl) ruksl,\n"+
                  "                             sum(d.chuksl) chuksl,\n"+
                  "                             sum(d.zaitml) zaitml,\n"+
                   "                            sum(d.panyk) panyk,\n"+
                  "                             sum(d.biaoz) biaoz\n"+
                  "                        from duowkcb d, meicb m\n"+
                 "                        where m.id = d.meicb_id\n"+
                 "                             --  and leib = 2\n"+
                 "                          and leix in (1, -1)\n"+
                 "                          and d.shij >= to_date('"+this.getBRiq()+"', 'yyyy-mm-dd')\n"+
                "                          and d.shij <\n"+
                "                               to_date('"+this.getERiq()+"', 'yyyy-mm-dd') + 1\n"+
                "                         group by (m.id, m.mingc)) ms,\n"+
                "                       duowkcb dw\n"+
                "                 where dw.meicb_id = ms.id\n"+
                "                   and dw.shij = ms.shij) kc,\n"+
                 "              (select dw.meicb_id, dw.kucl, dw.shij, dw.id\n"+
                 "                 from (select m.id, m.mingc, max(d.shij) as shij\n"+
                 "                         from duowkcb d, meicb m\n"+
                 "                        where m.id = d.meicb_id\n"+
                 "                             --  and leib = 2\n"+
                "                           and leix in (1, -1)\n"+
                "                           and d.shij < to_date('"+this.getBRiq()+"', 'yyyy-mm-dd')\n"+
                "                           and d.shij >= "+DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(this.getBRiq()))+"\n"+
                "                         group by (m.id, m.mingc)) ms,\n"+
               "                        duowkcb dw\n"+
               "                  where dw.meicb_id = ms.id\n"+
               "                    and dw.shij = ms.shij) qc\n"+
              "           where\n"+
              "           kc.meicb_id(+) = v.id\n"+
             "         and dw.id = v.duowgsb_id\n"+
              "        and qc.meicb_id(+) = v.id\n"+
             "         and v.mingc <> '直达煤场'\n"+
                 "       )\n"+
              "         union (select dw.mingc as duowgs,\n"+
               "                      v.mingc as duow,\n"+
               "                      nvl('累计', '') as leix,\n"+
                "                     sum(d.biaoz) biaoz,\n"+
               "                      sum(decode(0, 0, 0)) as qkucl,\n"+
              "                       sum(d.ruksl) as shour,\n"+
              "                       sum(d.chuksl) as quc,\n"+
              "                       sum(d.panyk) as yingk,\n"+
              "                       sum(decode(0, 0, 0)) as kuc,\n"+
              "                       sum(d.zaitml) as zaitml,\n"+
              "                       null as beiz\n"+
              "                  from duowkcb d, meicb v, diancxxb dc, duowgsb dw\n"+
               "                where d.meicb_id = v.id\n"+
              "                   and dw.id = v.duowgsb_id\n"+
              "                  and d.shij >= "+DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(this.getBRiq()))+"\n"+
               "                  and d.shij < to_date('"+this.getERiq()+"', 'yyyy-mm-dd') + 1\n"+
               "                  and d.diancxxb_id = dc.id\n"+
                                    
               "                  and v.mingc <> '直达煤场'\n"+
                "               group by (dw.mingc, v.mingc))\n"+
                 "     ) t,\n"+
                  "    (select *\n"+
                 "        from (select dw.mingc as duowgs, mc.mingc as duow\n"+
                   "              from duowkcb d, meicb mc, diancxxb dc, duowgsb dw\n"+
                   "             where d.meicb_id = mc.id\n"+
                   "               and mc.duowgsb_id = dw.id\n"+
                   "               and d.diancxxb_id = dc.id\n"+
                   strDianc+
                   "               and mc.mingc <> '直达煤场'\n"+
                  "                and d.shij >= "+DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(this.getBRiq()))+"\n"+
                  "                and d.shij < to_date('"+this.getERiq()+"', 'yyyy-mm-dd') + 1\n"+
                  "                  and (d.chuksl<>0 or d.ruksl<>0)\n"+
                  "              group by (dw.mingc, mc.mingc)) t1,\n"+
                  "            (select decode(1, 1, '本期', '本期') as leix\n"+
                 "                from dual\n"+
                  "             union\n"+
                "               select decode(1, 1, '累计', '累计') as leix from dual) t2) tb1\n"+
              "  where t.duowgs(+) = tb1.duowgs\n"+
               "   and t.duow(+) = tb1.duow\n"+
              "    and t.leix(+) = tb1.leix\n"+
              "  group by rollup(tb1.leix, (tb1.duowgs, tb1.duow))\n"+
            "   having not grouping(tb1.leix) = 1\n"+
           "     order by grouping(tb1.duowgs) desc, duowgs, duow, leix\n";
		strSQL.append(sql);
		String ArrHeader[][]=new String[1][11];
		ArrHeader[0]=new String[] {"垛位公司",Local.duow,Local.dangyue_leij_yueb,"期初库存","票重",Local.yingk_yueb,Local.shour_yueb,Local.quc_yueb,"在途煤量(吨)",Local.kuc_yueb,Local.beiz};

		int ArrWidth[]=new int[] {100,100,55,75,75,75,75,75,75,75,100};
		/*int gr=0;
            for(int i=0;i<ArrWidth.length;i++){
            gr+=ArrWidth[i];	
            }
            System.out.println(gr);*/
		ResultSet rs = cn.getResultSet(strSQL.toString());

		// 数据
		
		Table tb=new Table(rs, 1, 0, 1);
		rt.setBody(tb);
		
		rt.setTitle("库 存 月 报", ArrWidth);
		rt.setDefaultTitle(1, 3, "制表单位:"+getZhibdwmc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, "查询日期:"+DateUtil.FormatDate(DateUtil.getFirstDayOfYear(getBRiq()))+"至"+getERiq(), Table.ALIGN_CENTER);
		
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(20);
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.mergeFixedRowCol();
		rt.body.setColAlign(1, Table.ALIGN_CENTER);
		rt.body.setColAlign(2, Table.ALIGN_CENTER);
		rt.body.setColAlign(3, Table.ALIGN_CENTER);
		rt.body.setColAlign(4, Table.ALIGN_RIGHT);
		rt.body.setColAlign(5, Table.ALIGN_RIGHT);
		rt.body.setColAlign(6, Table.ALIGN_RIGHT);
		rt.body.setColAlign(7, Table.ALIGN_RIGHT);
		rt.body.setColAlign(8, Table.ALIGN_RIGHT);
		rt.body.setColAlign(9, Table.ALIGN_RIGHT);
		rt.body.setColAlign(10, Table.ALIGN_RIGHT);
		rt.body.setColAlign(11, Table.ALIGN_LEFT);
		
		
		rt.createDefautlFooter(ArrWidth);

		rt.setDefautlFooter(1, 3, "打印日期:"
				+ DateUtil.FormatDate(new Date()),
				Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "制表:", Table.ALIGN_LEFT);
			
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();
			return rt.getAllPagesHtml();
//		
	}
//	******************************************************************************
	
	
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

	
	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");		

		tb1.addText(new ToolbarText("库存日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("xiemrqb");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("xiemrqe");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));
		
//		 树
		tb1.addText(new ToolbarText("单位名称:"));
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+((Visit)getPage().getVisit()).getDiancxxb_id(),"",null,getTreeid());
		((Visit)getPage().getVisit()).setDefaultTree(dt);
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
		
		ToolbarButton tb = new ToolbarButton(null,"查询","function(){document.Form0.submit();}");
		tb.setIcon(SysConstant.Btn_Icon_Print);
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
	
	private String getZhibdwmc(){
		Visit visit = (Visit)getPage().getVisit();
		String danwmc = "";
		String sql = "select quanc from diancxxb where id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			danwmc = rsl.getString("quanc");
		}
		return danwmc;
	}


//	******************页面初始设置********************//
		public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

			Visit visit = (Visit) getPage().getVisit();
			
			if (!visit.getActivePageName().toString().equals(
					this.getPageName().toString())) {
				// 在此添加，在页面第一次加载时需要置为空的变量或方法
				visit.setActivePageName(getPageName().toString());
				setBRiq(DateUtil.FormatDate(getMonthFirstday(new Date())));
				setERiq(DateUtil.FormatDate(new Date()));
				visit.setProSelectionModel1(null);
				visit.setDropDownBean1(null);
				setTreeid(null);
				this.getSelectData();
			}
			isBegin=true;
			getToolBars();
			
		}

		public String getPrintTable(){
			if(!isBegin){
				return "";
			}
			isBegin=false;
		
			return getSelectData();
		
		}

		private boolean isBegin=false;


}