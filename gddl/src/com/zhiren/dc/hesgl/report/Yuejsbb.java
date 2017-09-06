package com.zhiren.dc.hesgl.report;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.ChessboardTable;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.webservice.InterFac_dt;
import com.zhiren.common.DateUtil;
import com.zhiren.common.SysConstant;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.LovComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;


public class Yuejsbb extends BasePage implements PageValidateListener {

	private String _msg;

	private int _CurrentPage = -1;

	private int _AllPages = -1;
	
	private int paperStyle = Report.PAPER_A4;
	
	private String Change = "1";	//结算方案多选结果值

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	public boolean getRaw() {
		return true;
	}

	private boolean reportShowZero() {
		return ((Visit) getPage().getVisit()).isReportShowZero();
	}

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	private String riq;
	public String getRiq(){
		
		return riq;
	}
	public void setRiq(String riq){
		this.riq=riq;
	}

	// ***************设置消息框******************//
	// 页面判定方法
	public void pageValidate(PageEvent arg0) {
		String PageName = arg0.getPage().getPageName();
		String ValPageName = Login.ValidateLogin(arg0.getPage());
		if (!PageName.equals(ValPageName)) {
			ValPageName = Login.ValidateAdmin(arg0.getPage());
			if (!PageName.equals(ValPageName)) {
				IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
				throw new PageRedirectException(ipage);
			}
		}
	}

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = MainGlobal.getExtMessageBox(msg, false);
	}

	public String _miaos;

	public String getMiaos() {
		return _miaos;
	}

	public void setMiaos(String miaos) {
		_miaos = miaos;
	}

	public SortMode getSort() {
		return SortMode.USER;
	}

	public String getReportType() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public boolean isJTUser() {
		return ((Visit) getPage().getVisit()).isJTUser();
	}

	// 得到单位全称
	public String getTianzdwQuanc(String dcid) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();
		ResultSetList rs = cn
				.getResultSetList(" select quanc from diancxxb where id="
						+ dcid);
		if (rs.next()) {
			_TianzdwQuanc = rs.getString("quanc");
		}
		rs.close();
		return _TianzdwQuanc;
	}

	public String getPrintTable() {
		JDBCcon cn = new JDBCcon();
		String sql=	"SELECT quanc\n" +
					"\tFROM DIANCXXB\n" + 
					" WHERE ID IN (SELECT JIESDW_ID FROM JIESFAB WHERE ID IN ("+this.getChange()+"))";
	    StringBuffer danw=new StringBuffer("");
	    String title = "";
	    ResultSetList rs1=cn.getResultSetList(sql);
	    while(rs1.next()){
	    	
	    	danw.append(rs1.getString("QUANC")).append(",");
	    }
	    
	    if(danw.length()>0){
	    	
	    	danw.deleteCharAt(danw.length()-1);
	    }
	       
	    sql = 
				"SELECT TO_CHAR(min(FA.DAOHJZSJ), 'yyyy') || '年' || TO_CHAR(max(FA.DAOHJZSJ), 'MM') || '月' AS TITLE\n" +
				"\tFROM JIESFAB FA\n" + 
				" WHERE FA.ID in ("+this.getChange()+")";

	    rs1 = cn.getResultSetList(sql);
		if(rs1.next()){
			
			title = rs1.getString("TITLE");
		}
	    //时间设置
		String []sj = getRiq().split("-");
		String a = sj[0];
		String b = sj[1];
	    //在jiesfab 中取得电厂信息表id

		
		sql=


			"   select rownum,c.mingc,c.xianshr,c.mingc1,c.yunj,c.yunjl,c.jiessl,c.rez,c.buhsmk,c.buhsdj,c.shoukdw,c.ches,c.jiessl1,c.buhsyf,c.buhsdj1,c.zaf,c.buhansmk from\n" +
			"  (\n" + 
			"   select\n" + 
			"          decode(y.mingc,'','总合计',y.mingc) as mingc,\n" + 
			"          decode(js.xianshr,'','汽车合计',js.xianshr) as xianshr,\n" + 
			"          m.mingc as mingc1,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(sum(to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1))*jy.jiessl)/sum(jy.jiessl),2)) as yunj,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(\n" + 
			"          sum(decode(to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)), 0, 0, jy.hansdj / to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)))*jy.jiessl)/sum(jy.jiessl),2)\n" + 
			"          ) as yunjl,\n" + 
			"          sum(js.jiessl) as jiessl,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(sum(Round_new(js.jiesrl*4.1816/1000,2)*jy.jiessl)/sum(jy.jiessl),2)) as rez,\n" + 
			"          sum(js.buhsmk) as buhsmk,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(sum(js.buhsdj*jy.jiessl)/sum(jy.jiessl),2)) as buhsdj,\n" + 
			"          decode(jy.shoukdw,'','车队合计',jy.shoukdw) as shoukdw,\n" + 
			"          sum(jy.ches) as ches,\n" + 
			"          sum(jy.jiessl) as jiessl1,\n" + 
			"          sum(jy.buhsyf) as buhsyf,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(sum(jy.buhsyf)/sum(jy.jiessl),2)) as buhsdj1,\n" + 
			"          '' as zaf,\n" + 
			"          sum(js.buhsmk) as buhansmk\n" + 
			"           from jiesb js,yunsfsb y,jiesyfb jy,jiesfab ff,meikxxb m\n" + 
			"       where js.yunsfsb_id = y.id\n" + 
			"             and js.jiesfab_id = ff.id\n" + 
			"             and jy.meikxxb_id = m.id\n" + 
			"             and js.id = jy.diancjsmkb_id(+)\n" + 
			"             and js.jiesfab_id in ("+this.getChange()+")\n" + 
			"           group by rollup(y.mingc,js.xianshr, m.mingc,jy.yunj,jy.shoukdw)\n" + 
			"           having not (grouping(js.xianshr)+grouping( m.mingc)+grouping(jy.shoukdw)=2 or grouping(jy.yunj)+grouping(jy.shoukdw)=1 or grouping(y.mingc)=1)\n" + 
			"     union all\n" + 
			"              select '' as mingc,\n" + 
			"                  decode(b.bujlx,'','补价合计',b.bujlx) as bujlx,\n" + 
			"                  b.miaos,\n" + 
			"                  0 as yunj,\n" + 
			"                  0 as yunjl,\n" + 
			"                  0 as jiessl,\n" + 
			"                  0 as rez,\n" + 
			"                  0 as buhsmk,\n" + 
			"                  0 as buhsdj,\n" + 
			"                  y.mingc,\n" + 
			"                  0 as ches,\n" + 
			"                  sum(b.meil) as meil,\n" + 
			"                  sum(b.yunf) as yunf,\n" + 
			"                  decode(nvl(sum(b.meil),0),0,0,Round_new(sum(b.danj*b.meil)/sum(b.meil),2)) as danj,\n" + 
			"                  '' as zaf,\n" + 
			"                  sum(b.yunf) as yunf\n" + 
			"                 from bujb b,yunsdwb y\n" + 
			"                 where b.yunsdwb_id = y.id\n" + 
			"                  and b.bujlx='运费补价'\n" + 
			"                  and b.JIESFAB_ID in ("+this.getChange()+")\n" + 
			"                 group by rollup(b.bujlx,b.miaos,y.mingc)\n" + 
			"                 having not(grouping(y.mingc)=1)\n" + 
			" union all\n" + 
			" select * from\n" + 
			" (\n" + 
			"select  decode(a.mingc,'','总合计',a.mingc) as mingc,\n" + 
			"       a.xianshr,\n" + 
			"       a.mingc1,\n" + 
			"       decode(nvl(sum(a.jiessl1),0),0,0,Round_new(sum(a.yunj*a.jiessl1)/sum(a.jiessl1),2)) as yunj,\n" + 
			"        decode(nvl(sum(a.jiessl1),0),0,0,Round_new(sum(a.yunjl*a.jiessl1)/sum(a.jiessl1),2)) as yunjl,\n" + 
			"          sum(a.jiessl) as jiessl,\n" + 
			"          decode(nvl(sum(a.jiessl1),0),0,0,Round_new(sum(a.rez*a.jiessl1)/sum(a.jiessl1),2)) as rez,\n" + 
			"          sum(a.buhsmk) as buhsmk,\n" + 
			"          decode(nvl(sum(a.jiessl1),0),0,0,Round_new(sum(a.buhsdj*a.jiessl1)/sum(a.jiessl1),2)) as buhsdj,\n" + 
			"          decode(a.shoukdw,'','车队合计',a.shoukdw) as shoukdw,\n" + 
			"          sum(a.ches) as ches,\n" + 
			"          sum(a.jiessl1) as jiessl1,\n" + 
			"          sum(a.buhsyf) as buhsyf,\n" + 
			"          decode(nvl(sum(a.jiessl1),0),0,0,Round_new(sum(a.buhsdj1*a.jiessl1)/sum(a.jiessl1),2)) as buhsdj1,\n" + 
			"          '' as zaf,\n" + 
			"          sum(a.buhansmk) as buhansmk\n" + 
			"       from\n" + 
			"(\n" + 
			"select\n" + 
			"          decode(y.mingc,'','总合计',y.mingc) as mingc,\n" + 
			"          decode(js.xianshr,'','汽车合计',js.xianshr) as xianshr,\n" + 
			"          m.mingc as mingc1,\n" + 
			"          to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)) as yunj,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(\n" + 
			"          sum(decode(to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)), 0, 0, jy.hansdj / to_number(substr(jy.yunj,INSTR(jy.yunj,':')+1)))*jy.jiessl)/sum(jy.jiessl),2)\n" + 
			"          ) as yunjl,\n" + 
			"          sum(js.jiessl) as jiessl,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,sum((js.jiesrl*4.1816/1000)*jy.jiessl)/sum(jy.jiessl)) as rez,\n" + 
			"          sum(js.buhsmk) as buhsmk,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,sum(js.buhsdj*jy.jiessl)/sum(jy.jiessl)) as buhsdj,\n" + 
			"          decode(jy.shoukdw,'','车队合计',jy.shoukdw) as shoukdw,\n" + 
			"          sum(jy.ches) as ches,\n" + 
			"          sum(jy.jiessl) as jiessl1,\n" + 
			"          sum(jy.buhsyf) as buhsyf,\n" + 
			"          decode(nvl(sum(jy.jiessl),0),0,0,Round_new(sum(jy.buhsyf)/sum(jy.jiessl),2)) as buhsdj1,\n" + 
			"          '' as zaf,\n" + 
			"          sum(js.buhsmk) as buhansmk\n" + 
			"           from jiesb js,yunsfsb y,jiesyfb jy,jiesfab ff,meikxxb m\n" + 
			"       where js.yunsfsb_id = y.id\n" + 
			"             and js.jiesfab_id = ff.id\n" + 
			"             and jy.meikxxb_id = m.id\n" + 
			"             and js.id = jy.diancjsmkb_id(+)\n" + 
			"             and js.jiesfab_id in ("+this.getChange()+")\n" + 
			"           group by rollup(y.mingc,js.xianshr, m.mingc,jy.yunj,jy.shoukdw)\n" + 
			"           having not (grouping(js.xianshr)+grouping( m.mingc)+grouping(jy.shoukdw)=2 or grouping(jy.yunj)+grouping(jy.shoukdw)=1 or grouping(y.mingc)=1 or grouping(jy.yunj)=1)\n" + 
			"     union all\n" + 
			"              select '' as mingc,\n" + 
			"                  decode(b.bujlx,'','补价合计',b.bujlx) as bujlx,\n" + 
			"                  b.miaos,\n" + 
			"                  0 as yunj,\n" + 
			"                  0 as yunjl,\n" + 
			"                  0 as jiessl,\n" + 
			"                  0 as rez,\n" + 
			"                  0 as buhsmk,\n" + 
			"                  0 as buhsdj,\n" + 
			"                  y.mingc,\n" + 
			"                  0 as ches,\n" + 
			"                  sum(b.meil) as meil,\n" + 
			"                  sum(b.yunf) as yunf,\n" + 
			"                  decode(nvl(sum(b.meil),0),0,0,Round_new(sum(b.danj*b.meil)/sum(b.meil),2)) as danj,\n" + 
			"                  '' as zaf,\n" + 
			"                  sum(b.yunf) as yunf\n" + 
			"                 from bujb b,yunsdwb y\n" + 
			"                 where b.yunsdwb_id = y.id\n" + 
			"                  and b.bujlx='运费补价'\n" + 
			"                  and b.JIESFAB_ID in ("+this.getChange()+")\n" + 
			"                 group by rollup(b.bujlx,b.miaos,y.mingc)\n" + 
			"                 having not(grouping(y.mingc)=1)\n" + 
			" ) a\n" + 
			"  group by rollup(a.mingc,a.xianshr, a.mingc1,a.yunj,a.shoukdw)\n" + 
			"  ) b where b.mingc ='总合计'\n" + 
			") c";



		ResultSetList rs = cn.getResultSetList(sql);
		Report rt = new Report();
		String ArrHeader[][] = new String[2][17];
		ArrHeader[0]=new String[] {"序号","运输单位","结款单位","煤矿名称","运距","运价率","煤量",
				"热值","除税煤费","除税煤费","车队","车数","煤量","除税运费","除税运费","杂费","费用合计"};
		ArrHeader[1]=new String[] {"序号","运输单位","结款单位","煤矿名称","运距","运价率","煤量",
				"热值","煤费","单价","车队","车数","煤量","运费","单价","杂费","费用合计"};
		int ArrWidth[] = new int[]{25,50,100,80,50,50,50,60,60,60,50,60,60,60,60,60,60};
		ArrWidth = rt.getArrWidth(ArrWidth, this.paperStyle);
		rt.setTitle(a+"年"+b+"月"+"结算SAP表", ArrWidth);
		rt.setDefaultTitle(1, 4, "填报单位:"+danw, Table.ALIGN_LEFT);
		rt.setDefaultTitle(13, 4, "填报年月:"+a+"年"+b+"月", Table.ALIGN_RIGHT);
		rt.setBody(new Table(rs, 2, 0, 2));
		
        //	设置数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(40);
		//rt.body.mergeFixedCol(1);
		rt.body.setHeaderData(ArrHeader);
		rt.body.merge(1, 8, 1, 9);
		rt.body.merge(1, 1, 2, 1);
		rt.body.merge(1, 2, 2, 2);
		rt.body.merge(1, 3, 2, 3);
		rt.body.merge(1, 4, 2, 4);
		rt.body.merge(1, 5, 2, 5);
		rt.body.merge(1, 6, 2, 6);
		rt.body.merge(1, 7, 2, 7);
		rt.body.merge(1, 8, 2, 8);
		rt.body.merge(1, 9, 1, 10);
		rt.body.merge(1, 11, 2,11);
		rt.body.merge(1, 12, 2, 12);
		rt.body.merge(1, 13, 2, 13);
		rt.body.merge(1, 14, 1, 15);		
		rt.body.merge(1, 16, 2, 16);
		rt.body.merge(1, 17, 2, 17);
		for(int i=0;i<=rt.body.getRows();i++){
			if(rt.body.getCellValue(i, 11).equals("车队合计")){
				if(!rt.body.getCellValue(i, 3).equals("汽车合计")){
					rt.body.setCellValue(i, 7, rt.body.getCellValue(i-1, 7));
					rt.body.setCellValue(i, 9, rt.body.getCellValue(i-1, 9));
					rt.body.setCellValue(i, 5, rt.body.getCellValue(i-1, 5));
					rt.body.setCellValue(i, 17, rt.body.getCellValue(i-1, 17));
				}
			}
		    if(rt.body.getCellValue(i, 3).equals("汽车合计")){
		    	rt.body.setCellValue(i, 11, "");
		    }
		    if(rt.body.getCellValue(i, 2).equals("总合计")){
		    	rt.body.setCellValue(i, 11, "");
		    }
		}
//		rt.body.mergeFixedCol(1);
//		rt.body.mergeFixedCol(2);
//		rt.body.mergeFixedCol(3);
//		rt.body.mergeFixedCol(4);
//		rt.body.mergeFixedCol(5);
//		rt.body.mergeFixedCol(6);
//		rt.body.mergeFixedCol(7);
//		rt.body.mergeFixedCol(8);
//		rt.body.mergeFixedCol(9);
//		rt.body.mergeFixedCol(10);
		rt.body.merge(3, 2, rt.body.getRows(), 2);
		rt.body.merge(3, 3, rt.body.getRows(), 3);
		rt.body.merge(3, 4, rt.body.getRows(), 4);
		rt.body.merge(3, 5, rt.body.getRows(), 5);
		rt.body.merge(3, 6, rt.body.getRows(), 6);
		rt.body.merge(3, 7, rt.body.getRows(), 7);
		rt.body.merge(3, 8, rt.body.getRows(), 8);
		rt.body.merge(3, 9, rt.body.getRows(), 9);
		rt.body.merge(3, 10, rt.body.getRows(), 10);
		rt.body.merge(3, 16, rt.body.getRows(), 16);
		rt.body.merge(3, 17,rt.body.getRows(), 17);
		//rt.body.mergeFixedCols();
		rt.body.setPageRows(40);
		rt.getPages();
		rt.createDefautlFooter(ArrWidth);	
		rt.setDefautlFooter(2, 2, "主管领导:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(7, 2, "审核:" , Table.ALIGN_LEFT);
		rt.setDefautlFooter(14, 3, "制表:", Table.ALIGN_LEFT);
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		cn.Close();
		return rt.getAllPagesHtml();
	}

	private void ArrWidth(int i) {
		// TODO 自动生成方法存根

	}
	private boolean _SbClick = false;

	public void SbButton(IRequestCycle cycle) {
		_SbClick = true;
	}

	private boolean _SqxgClick = false;

	public void SqxgButton(IRequestCycle cycle) {
		_SqxgClick = true;
	}

	private boolean _QueryClick = false;

	public void QueryButton(IRequestCycle cycle) {
		_QueryClick = true;
	}

	public void submit(IRequestCycle cycle) {
		
		if (_QueryClick) {
			_QueryClick = false;
		    getPrintTable();		
			getSelectData();
		}

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		//setTreeid(visit.getDiancxxb_id() + "");

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			init();
		}
		getSelectData();
		if(visit.getboolean1()){
			
			visit.setboolean1(false);
			getIJiesfaModels();
		}
	}

	private void init() {
		
		setJiesfaValue(null);	//2
		setIJiesfaModel(null);	//2
		getIJiesfaModels();	//2
		//visit.setDefaultTree(null);
		((Visit) getPage().getVisit()).setboolean1(false);
		((Visit) getPage().getVisit()).setString3("");	//电厂树
		setDiancmcModel(null);
		paperStyle();
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
//   时间下拉框
		tb1.addText(new ToolbarText("结算日期:"));
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("riq", "Form0");
		df.setId("riq");
	    tb1.addField(df);
	    tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText("结算方案:"));
		LovComboBox jiesfa = new LovComboBox();
		jiesfa.setId("jiesfa");
//		ComboBox jiesfa = new ComboBox();
		jiesfa.setTransform("JiesfaDropDown");
		jiesfa.setWidth(160);
		jiesfa.setForceSelection(false);
		jiesfa.setListeners("select:function(e){document.getElementById('CHANGE').value = e.getValue();}");
		tb1.addField(jiesfa);
		tb1.addText(new ToolbarText("-"));
	
		ToolbarButton tb = new ToolbarButton(null, "查询",
				"function(){document.getElementById('QueryButton').click();}");
		tb.setIcon(SysConstant.Btn_Icon_Search);
			
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

//	结算方案名称
	public IDropDownBean getJiesfaValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			if (getIJiesfaModels().getOptionCount() > 1) {
				
				((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getIJiesfaModels().getOption(0));
			} else {
				((Visit) getPage().getVisit()).setDropDownBean2(new IDropDownBean(-1, "1"));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setJiesfaValue(IDropDownBean Value) {
		
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean2()!= null) {
			
			id = ((Visit) getPage().getVisit()).getDropDownBean2().getId();
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setIJiesfaModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIJiesfaModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			
			getIJiesfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getIJiesfaModels() {

			String sql = 
//				"SELECT ID, BIANM\n" +
//				"\tFROM JIESFAB\n" + 
//				" WHERE SHIFJS = 1\n" + 
//				"\t AND JIESLX = "+Locale.guotyf_feiylbb_id+"\n" + 
////				"\t AND JIESDW_ID = "+this.getTreeid()+"\n" + 
//				" ORDER BY BIANM desc";

				"select id,j.bianm from jiesfab j\n" +
				"where j.daohjzsj= to_date('"+this.getRiq()+"','yyyy-MM-dd')\n"+
				"  and j.jieslx=2 ";

			
			((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));

		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
//	结算方案名称_end

	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getDiancmcModels() {
		String sql = "select id,mingc from diancxxb";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).equals(treeid)){
			
			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setString3(treeid);
	}

	public DefaultTree getTree() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree();
	}

	public void setTree(DefaultTree etu) {
		((Visit) this.getPage().getVisit()).setDefaultTree(etu);
	}
	private void paperStyle() {
		JDBCcon con = new JDBCcon();
		int paperStyle = Report.PAPER_A4_WIDTH;
		ResultSetList rsl = con
				.getResultSetList("select zhi from xitxxb  where zhuangt=1 and mingc='纸张类型' and diancxxb_id="
						+ ((Visit) this.getPage().getVisit()).getDiancxxb_id());
		if (rsl.next()) {
			paperStyle = rsl.getInt("zhi");
		}

		this.paperStyle = paperStyle;
	}

	public int getJibbyDCID(JDBCcon con, String dcid) {
		int jib = 3;
		ResultSetList rsl = con
				.getResultSetList("select jib from diancxxb where id =" + dcid);
		if (rsl.next()) {
			jib = rsl.getInt("jib");
		}
		rsl.close();
		return jib;
	}
}