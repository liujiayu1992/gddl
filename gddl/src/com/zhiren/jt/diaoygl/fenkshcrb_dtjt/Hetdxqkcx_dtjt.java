package com.zhiren.jt.diaoygl.fenkshcrb_dtjt;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
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

public class Hetdxqkcx_dtjt extends BasePage {
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
		return getShouhcreport();
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getShouhcreport() {
		JDBCcon con = new JDBCcon();
		String sbsql = "";
		

		String End_riq=getEndriqDate();
		
		String strGongsID = "";
//		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+") ";
//			notHuiz=" and not grouping(gs.mingc)=1 ";//当电厂树是分公司时,去掉集团汇总
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
//			notHuiz=" and not  grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		long leix = getBaoblxValue().getId();
		
//		String strLeix = " and vw.gongyskjb_id="+leix+""; 


			sbsql=

				"select case when grouping(vw.quanc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||vw.quanc else case when grouping(dc.mingc)=0 then dc.mingc else\n" +
				"       case when grouping(fgs.mingc)=0 then fgs.mingc||'合计' else '集团公司' end end end as danwmc,\n" + 
				"       round(sum(nvl(nlj.jhl,0)),2) as nianlj,\n" + 
				"       round(sum(nvl(jhdr.jhl,0)/jhdr.yts),2) as jhdr,\n" + 
				"       round(sum(nvl(jhdr.jhl,0)/jhdr.yts*jhdr.jzts),2) as nianljjh,\n" + 
				"       round(sum(nvl(dr.dangrsg,0))/10000,2) as dangrsg,\n" + 
				"       round(sum(nvl(ylj.leijsg,0))/10000,2) as yueljsg,\n" + 
				"       decode(sum(nvl(jhdr.jhl,0)/jhdr.yts),0,0,round((sum(nvl(dr.dangrsg,0)/10000)/sum(nvl(jhdr.jhl,0)/jhdr.yts))*100,2)) as dangrdhl,\n" + 
				"       decode(sum(nvl(jhdr.jhl,0)/jhdr.yts*jhdr.jzts),0,0,round((sum(nvl(ylj.leijsg,0)/10000)/sum(nvl(jhdr.jhl,0)/jhdr.yts*jhdr.jzts))*100,2)) as yueljdhl\n" + 
				"  from\n" + 
				"      (select kj.id as id,gy.quanc as quanc,gl.diancxxb_id as diancxxb_id\n" + 
				"               from diancgysmykjb kj,diancgysglb gl,gongysb gy\n" + 
				"               where kj.diancgysglb_id=gl.id and gl.gongysb_id=gy.id and gl.shiyzt=1 and kj.gongyskjb_id=1) vw,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,nvl(fk.duns,0) as dangrsg\n" + 
				"        from fenkshcrb fk,diancgysmykjb vw\n" + 
				"        where fk.riq=to_date('"+End_riq+"','yyyy-mm-dd')  and vw.id=fk.gongysb_id  and vw.gongyskjb_id=1 )dr,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,sum(fk.duns) as leijsg\n" + 
				"        from fenkshcrb fk,diancgysmykjb vw\n" + 
				"        where fk.riq>=first_day(to_date('"+End_riq+"','yyyy-mm-dd')) and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') and vw.id=fk.gongysb_id\n" + 
				"           and vw.gongyskjb_id=1  group by fk.diancxxb_id,fk.gongysb_id ) ylj,\n" + 
				"\n" + 
				"      (select htqk.diancxxb_id,vw.id as gongysb_id,sum(htqk.hej) as jhl\n" + 
				"        from niandhtqkb htqk,diancgysmykjb vw,diancgysglb gl\n" + 
				"        where htqk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and htqk.riq<=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-12-1','yyyy-mm-dd')\n" + 
				"          and gl.gongysb_id=htqk.gongysb_id and vw.diancgysglb_id=gl.id\n" + 
				"          and vw.gongyskjb_id=1  group by htqk.diancxxb_id,vw.id ) nlj,\n" + 
				"\n" + 
				"      (select htqk.diancxxb_id,vw.id as gongysb_id,sum(htqk.hej) as jhl ,daycount(to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd')) as yts,\n" + 
				"              (to_date('"+End_riq+"','yyyy-mm-dd')-first_day(to_date('"+End_riq+"','yyyy-mm-dd'))) as jzts\n" + 
				"        from niandhtqkb htqk,diancgysmykjb vw,diancgysglb gl\n" + 
				"        where htqk.riq=first_day(to_date('"+End_riq+"','yyyy-mm-dd')) and vw.diancgysglb_id=gl.id\n" + 
				"          and gl.gongysb_id=htqk.gongysb_id\n" + 
				"          and vw.gongyskjb_id=1\n" + 
				"          group by htqk.diancxxb_id,vw.id ) jhdr,\n" + 
				"      (select distinct vw.id,fk.diancxxb_id\n" + 
				"         from fenkshcrb fk,diancgysmykjb vw\n" + 
				"         where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd')\n" + 
				"         and vw.gongyskjb_id=1 and fk.gongysb_id=vw.id ) gys ,diancxxb dc,diancxxb fgs\n" + 
				" where dr.gongysb_id(+)=gys.id and ylj.gongysb_id(+)=gys.id and nlj.gongysb_id(+)=gys.id and jhdr.gongysb_id(+)=gys.id\n" + 
				"    and gys.diancxxb_id=dc.id and gys.id=vw.id and dc.fuid=fgs.id \n" + strGongsID + 
				" group by rollup(fgs.mingc,dc.mingc,vw.quanc)\n" + 
				" order by grouping(fgs.mingc) desc,max(fgs.xuh),grouping(dc.mingc) desc,max(dc.xuh),grouping(vw.quanc) desc,vw.quanc\n" ;

				
				
			/*	"select case when grouping(vw.quanc)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||vw.quanc else case when grouping(dc.mingc)=0 then dc.mingc else\n" + 
				"       case when grouping(fgs.mingc)=1 then fgs.mingc||'合计' else '总计' end end end as danwmc,\n" + 
				"       round(sum(nvl(dr.dangrjh,0))/10000,2) as dangrjh,\n" + 
				"       round(sum(nvl(ylj.leijjh,0))/10000,2) as yueljjh,\n" + 
				"       round(sum(nvl(nlj.leijjh,0))/10000,2) as nianljjh,\n" + 
				"       round(sum(nvl(dr.dangrsg,0))/10000,2) as dangrsg,\n" + 
				"       round(sum(nvl(ylj.leijsg,0))/10000,2) as yueljsg,\n" + 
				"       round(sum(nvl(nlj.leijsg,0))/10000,2) as nianljsg,\n" + 
				"       decode(sum(nvl(dr.dangrjh,0)),0,0,round((sum(nvl(dr.dangrsg,0))/sum(nvl(dr.dangrjh,0)))*100,2)) as dangrdhl,\n" + 
				"       decode(sum(nvl(ylj.leijjh,0)),0,0,round((sum(nvl(ylj.leijsg,0))/sum(nvl(ylj.leijjh,0)))*100,2)) as yueljdhl,\n" + 
				"       decode(sum(nvl(nlj.leijjh,0)),0,0,round((sum(nvl(nlj.leijsg,0))/sum(nvl(nlj.leijjh,0)))*100,2)) as nianljdhl\n" + 
				"  from diancxxb dc,diancgysmykjb vw,diancxxb fgs--dianckjpxb px,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,nvl(fk.rijjhl,0) as dangrjh,nvl(fk.duns,0) as dangrsg from fenkshcrb fk,diancgysmykjb vw \n" + 
				"        where fk.riq=to_date('"+End_riq+"','yyyy-mm-dd')  and vw.id=fk.gongysb_id "+strLeix+" )dr,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk,diancgysmykjb vw\n" + 
				"        where fk.riq>=first_day(to_date('"+End_riq+"','yyyy-mm-dd')) and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') and vw.id=fk.gongysb_id\n" + 
				"          "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) ylj,\n" + 
				"      (select fk.diancxxb_id,fk.gongysb_id,sum(fk.rijjhl) as leijjh,sum(fk.duns) as leijsg from fenkshcrb fk,diancgysmykjb vw\n" + 
				"        where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') and vw.id=fk.gongysb_id\n" + 
				"         "+strLeix+"  group by fk.diancxxb_id,fk.gongysb_id ) nlj,\n" + 
				"\n" + 
				"       (select distinct vw.id,fk.diancxxb_id \n"+
				"		   from fenkshcrb fk,diancgysmykjb vw\n" + 
				"   where fk.riq>=to_date('"+DateUtil.getYear(DateUtil.getDate(End_riq))+"-01-01','yyyy-mm-dd') and fk.riq<=to_date('"+End_riq+"','yyyy-mm-dd') \n" + 
				"     "+strLeix+" and fk.gongysb_id=vw.id ) gys\n" + 
				"\n" + 
				"  where dr.gongysb_id(+)=gys.id and ylj.gongysb_id(+)=gys.id and nlj.gongysb_id(+)=gys.id --and gys.fuid=gy.id \n" + 
				"    and gys.diancxxb_id=dc.id and gys.id=vw.id dc.fuid=fgs.id--and px.diancxxb_id=dc.id and px.kouj='月报'\n" + strGongsID +
				"  group by rollup(fgs.mingc,dc.mingc,vw.quanc)\n" +
				"having round(sum(nvl(nlj.leijjh,0))/10000,2)<>0 or  round(sum(nvl(nlj.leijsg,0))/10000,2)<>0\n"+
				"  order by grouping(fgs.mingc) desc,max(fgs.xuh),grouping(dc.mingc) desc,max(dc.xuh),grouping(vw.quanc) desc,vw.quanc ";*/
		

		//System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		
		 String ArrHeader[][]=new String[2][8];
		 ArrHeader[0]=new String[] {"名称","","计划","计划","实供","实供","到货率","到货率"};
		 ArrHeader[1]=new String[] {"名称","年累计<br>（万吨）","当日<br>（万吨）","月累计<br>（万吨）","当日<br>（万吨）","月累计<br>（万吨）","当日<br>（%)","月累计<br>(%)"};

		 int ArrWidth[]=new int[] {350,70,70,70,70,70,70,70};

		
		
		
//		String ArrHeader[][]=new String[2][10];
//		ArrHeader[0]=new String[] {"单位","计划(万吨)","计划(万吨)","计划(万吨)","实供(万吨)","实供(万吨)","实供(万吨)","到货率(%)","到货率(%)","到货率(%)"};
//		ArrHeader[1]=new String[] {"单位","当日","月累计","年累计","当日","月累计","年累计","当日","月累计","年累计"};
//
//		int ArrWidth[]=new int[] {350,60,60,60,60,60,60,60,60,60};

		rt.setTitle("本月分矿点到货情况表", ArrWidth);
		rt.setDefaultTitle(2, 3, "时间："+FormatDate(DateUtil.getDate(End_riq)), Table.ALIGN_LEFT);
		rt.setBody(new Table(rs,2, 0, 1));
		
		rt.body.setHeaderData(ArrHeader);// 表头数据
		rt.body.setWidth(ArrWidth);
		rt.body.setPageRows(24);
		rt.body.mergeFixedRow();
		rt.body.mergeFixedCols();
		rt.body.ShowZero = true;
		
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1, Table.ALIGN_CENTER);
		}
		
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

//	public String getPageHome() {
//		if (((Visit) getPage().getVisit()).getboolean1()) {
//			return "window.location = '" + MainGlobal.getHomeContext(this)
//			+ "';";
//		} else {
//			return "";
//		}
//	}
	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy年MM月dd日", _date);
	}
	///////
	public String getBeginriqDate(){
		if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
		}
		return ((Visit) getPage().getVisit()).getString4();
	}
	public void setBeginriqDate(String value){
		((Visit) getPage().getVisit()).setString4(value);
	}
	public String getEndriqDate(){
		if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay)));
		}
		return ((Visit) getPage().getVisit()).getString5();
	}
	public void setEndriqDate(String value){
		((Visit) getPage().getVisit()).setString5(value);
	}

	private void getToolbars(){
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("日期:"));
		DateField df = new DateField();

		DateField df1 = new DateField();
		df1.setValue(this.getEndriqDate());
		df1.Binding("qiandrq2","");// 与html页中的id绑定,并自动刷新
		df1.setWidth(80);
		tb1.addField(df1);
		tb1.addText(new ToolbarText("-"));

		Visit visit = (Visit) getPage().getVisit();
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

		
//		供煤类型
//		tb1.addText(new ToolbarText("统计类型:"));
//		ComboBox cb2 = new ComboBox();
//		cb2.setTransform("BaoblxDropDown");
//		cb2.setId("Baoblx");
//		cb2.setWidth(80);
//		tb1.addField(cb2);
//		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);

		
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
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
			visit.setString4(null);
			visit.setString5(null);

		}
		getToolbars();

		blnIsBegin = true;

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
//	分公司下拉框
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

//	得到系统信息表中配置的报表标题的单位名称
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='报表标题单位名称'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return biaotmc;

	}

//	报表类型
	public boolean _Baoblxchange = false;
	private IDropDownBean _BaoblxValue;

	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		JDBCcon con = new JDBCcon();
		try{
		List leixList = new ArrayList();
		leixList.add(new IDropDownBean(1,"重点"));
		leixList.add(new IDropDownBean(2,"补充"));


		_IBaoblxModel = new IDropDownModel(leixList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
	
//	报表统计口径
	public boolean _Tongjkjchange = false;
	private IDropDownBean _TongjkjValue;

	public IDropDownBean getTongjkjValue() {
		if(_TongjkjValue==null){
			_TongjkjValue=(IDropDownBean)getITongjkjModels().getOption(0);
		}
		return _TongjkjValue;
	}

	public void setTongjkjValue(IDropDownBean Value) {
		long id = -2;
		if (_TongjkjValue != null) {
			id = _TongjkjValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Tongjkjchange = true;
			} else {
				_Tongjkjchange = false;
			}
		}
		_TongjkjValue = Value;
	}

	private IPropertySelectionModel _ITongjkjModel;

	public void setITongjkjModel(IPropertySelectionModel value) {
		_ITongjkjModel = value;
	}

	public IPropertySelectionModel getITongjkjModel() {
		if (_ITongjkjModel == null) {
			getITongjkjModels();
		}
		return _ITongjkjModel;
	}

	public IPropertySelectionModel getITongjkjModels() {
		JDBCcon con = new JDBCcon();
		try{
		List tongjkjList = new ArrayList();
		tongjkjList.add(new IDropDownBean(0,"分厂分矿"));
		tongjkjList.add(new IDropDownBean(1,"分矿分厂"));

		_ITongjkjModel = new IDropDownModel(tongjkjList);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _ITongjkjModel;
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
