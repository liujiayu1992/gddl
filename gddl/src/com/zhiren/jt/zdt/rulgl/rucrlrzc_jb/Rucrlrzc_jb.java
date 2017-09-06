//2008-10-12 chh 
//修改内容 :入厂、入炉没有化验数据的数量不参加加权

//2008-10-13 chh 
//修改内容 :加入检质量


//2009-8-10 sy
//修改内容 :入厂入炉热值取getFarldec中的小数位再计算


package com.zhiren.jt.zdt.rulgl.rucrlrzc_jb;

import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * chenzt
 * 2010-04-06
 * 描述：修改河北分公司报表的制表人一项 ，并且设置为没有（null）
 */


public class Rucrlrzc_jb extends BasePage {
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
//		return getQibb();
//		if (mstrReportName.equals(REPORT_NAME_Hetltj)) {
		return getZhiltj();
//		} else {
//		return "无此报表";
//		}
	}
	// 合同量分厂分矿分矿分厂统计报表
	private String getZhiltj() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String Start_riq=getBeginriqDate();
		String End_riq=getEndriqDate();

		String strConditonTitle="";
		if (Start_riq==End_riq){
			strConditonTitle=DateUtil.Formatdate("yyyy年MM月dd日",DateUtil.getDate(Start_riq));
		}else{
			strConditonTitle=DateUtil.Formatdate("yyyy年MM月dd日",DateUtil.getDate(Start_riq))+"-"+DateUtil.Formatdate("yyyy年MM月dd日",DateUtil.getDate(End_riq));
		}

		String strGongsID = "";
		String notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strGongsID=" ";

		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strGongsID = "  and (dc.fuid=  " +this.getTreeid()+"or dc.shangjgsid="+this.getTreeid()+") ";
			notHuiz=" and not grouping(gs.mingc)=1 ";//当电厂树是分公司时,去掉集团汇总
		}else if (jib==3){//选电厂只刷新出该电厂
			strGongsID=" and dc.id= " +this.getTreeid();
			notHuiz=" and not  grouping(dc.mingc)=1";//当电厂树是电厂时,去掉分公司和集团汇总
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
		}

		String danw="";
		String table="";
		String where="";
		String groupby = "";
		String ordeby ="";

		JDBCcon cn = new JDBCcon();
		if (jib==2){
			String ranlgs = "select id from diancxxb where shangjgsid= "+this.getTreeid();

			try{
				ResultSet rl = cn.getResultSet(ranlgs);
				if(rl.next()){
					danw="select decode(grouping(vdc.rlgsmc)+grouping(vdc.fgsmc)+grouping(vdc.mingc),3,'总计',2,vdc.rlgsmc,1,vdc.fgsmc,'&nbsp;&nbsp;&nbsp;&nbsp;'||vdc.mingc) as danw,\n";
					table=",vwdianc vdc ";
					where="and dc.id=vdc.id";
					groupby ="group by rollup (fx.fenx,vdc.rlgsmc,vdc.fgsmc,vdc.mingc)\n";
					notHuiz =" and not grouping(vdc.rlgsmc)=1 ";
					ordeby="order by  grouping(vdc.rlgsmc) desc,vdc.rlgsmc,grouping(vdc.fgsmc) desc,vdc.fgsmc,grouping(vdc.mingc) desc ,vdc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}else{
					danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
					table="";
					where="";
					groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
					ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
				}
				rl.close();
			}catch(Exception e){
				e.printStackTrace();
			}finally{
				cn.Close();
			}
		}else{
			danw="select decode(grouping(gs.mingc)+grouping(dc.mingc),2,'总计',1,gs.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||dc.mingc) as danw,\n";
			table="";
			where="";
			groupby ="group by rollup (fx.fenx,gs.mingc,dc.mingc)\n";
			ordeby="order by  grouping(gs.mingc) desc,gs.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx\n";
		}


		String sbsql = "select l.danw,l.fenx,l.rc_laimsl,rc_laimzl,l.rc_farl,l.rl_jingz,rl_rulmzl,l.rl_farl,\n"
			+ "       (l.rc_farl-l.rl_farl) as tiaozq_mj,Round((l.rc_farl-l.rl_farl)*1000/4.1816,0) as tiaozq_dk\n"
			+ "from("+ danw+ " fx.fenx,\n"
			+ "       Round(sum(rc.laimsl),0) as rc_laimsl,sum(rc.laimzl) as rc_laimzl,\n"
			+ "       decode(sum(rc.laimzl),0,0,Round(sum(rc.farl*rc.laimzl)/sum(rc.laimzl),2)) as rc_farl,\n"
			+ "       Round(sum(rl.rulml),0) as rl_jingz,sum(rulmzl) as rl_rulmzl,\n"
			+ "       decode(sum(rl.rulmzl),0,0,Round(sum(rl.farl*rl.rulmzl)/sum(rl.rulmzl),2)) as rl_farl\n"
			+ "from  (select diancxxb_id,fx.fenx,fx.xuh from\n"
			+ "     	(select distinct f.diancxxb_id from fahb f  where f.daohrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and  f.daohrq<=to_date('"+End_riq+"','yyyy-mm-dd')  \n"
			+ "        union     \n"
			+ "        select distinct hy.diancxxb_id from meihyb hy  where hy.rulrq>=to_date('"+Start_riq+"','yyyy-mm-dd') and hy.rulrq<=to_date('"+End_riq+"','yyyy-mm-dd') \n"
			+ "     	) dcid,(select decode(1,1,'当日') as fenx,1 as xuh  from dual union select decode(1,1,'累计')  as fenx,2 as xhu from dual ) fx) fx,\n"
			+ "((select f.diancxxb_id,decode(1,1,'当日') as fenx,sum(f.laimsl) as laimsl,sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)) as laimzl,\n"
			+ "      decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(round(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl\n"
			+ "from fahb f,zhilb z\n"
			+ "where f.zhilb_id=z.id(+)\n"
			+ " and f.daohrq=to_date('"+End_riq+"','yyyy-mm-dd')\n"
			+ "group  by f.diancxxb_id)\n"
			+ "union\n"
			+ "(select f.diancxxb_id,decode(1,1,'累计') as fenx,sum(f.laimsl) as laimsl,sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)) as laimzl,\n"
			+ "       decode(sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl)),0,0,sum(round_new(z.qnet_ar,"+((Visit) getPage().getVisit()).getFarldec()+")*f.laimzl)/sum(decode(nvl(z.qnet_ar,0),0,0,f.laimzl))) as farl\n"
			+ "from fahb f,zhilb z\n"
			+ "	where f.zhilb_id=z.id(+)\n"
			+ " 	and f.daohrq>=to_date('"+Start_riq+"','yyyy-mm-dd')\n"
			+ " 	and f.daohrq<=to_date('"+End_riq+"','yyyy-mm-dd')\n"
			+ "group  by f.diancxxb_id)) rc,\n"
			+ "\n"
			+ "((select hy.diancxxb_id,decode(1,1,'当日') as fenx,sum(hy.fadhy+hy.gongrhy) as rulml,sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)) as rulmzl,\n"
			+ "    decode(sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)),0,0,sum(round_new(mz.qnet_ar,"+visit.getFarldec()+")*(hy.fadhy+hy.gongrhy))/sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy))) as farl\n"
			+ " from rulmzlb mz,meihyb hy\n"
			+ "		where hy.rulrq=to_date('"+End_riq+"','yyyy-mm-dd')\n"
			+ "		and hy.rulmzlb_id=mz.id(+)\n"
			+ "		group by hy.diancxxb_id)\n"
			+ "union\n"
			+ "(select hy.diancxxb_id,decode(1,1,'累计') as fenx,sum(hy.fadhy+hy.gongrhy) as rulml,sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)) as rulmzl,\n"
			+ "   decode(sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy)),0,0,sum(round_new(mz.qnet_ar,"+visit.getFarldec()+")*(hy.fadhy+hy.gongrhy))/sum(decode(nvl(mz.qnet_ar,0),0,0,hy.fadhy+hy.gongrhy))) as farl\n"
			+ " from rulmzlb mz,meihyb hy\n"
			+ "	where hy.rulrq>=to_date('"+Start_riq+"','yyyy-mm-dd')\n"
			+ " 	and hy.rulrq<=to_date('"+End_riq+"','yyyy-mm-dd')\n"
			+ "		and hy.rulmzlb_id=mz.id(+)\n"
			+ "		group by hy.diancxxb_id)) rl,diancxxb dc, vwfengs gs"+table+"\n"
			+ "where fx.diancxxb_id=rc.diancxxb_id(+)\n"
			+ "	and   fx.diancxxb_id=rl.diancxxb_id(+)\n"
			+ "	and   fx.diancxxb_id=dc.id\n"
			+ "	and   fx.fenx=rc.fenx(+)\n"
			+ "	and   fx.fenx=rl.fenx(+)\n"
			+ "	and   dc.fuid=gs.id "+where+" "+strGongsID+"\n"
			+ groupby
			+ "having not grouping (fx.fenx)=1 "+notHuiz+"\n"
			+ ordeby + ") l";

	//	System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		String ArrHeader[][]=new String[2][10];
		ArrHeader[0]=new String[] {"单位","当日<br>累计","入厂煤质","入厂煤质","入厂煤质","入炉煤质","入炉煤质","入炉煤质","热值差","热值差"};
		ArrHeader[1]=new String[] {"单位","当日<br>累计","实收量","检质量","热值<br>Qnet_ar","入炉煤量","检质量","热值<br>Qnet_ar","Mj/kg","kcal/kg"};

		int ArrWidth[]=new int[] {150,40,60,60,60,60,60,60,60,60};
		String arrFormat[]=new String[]{"","","0","0","0.00","0","0","0.00","0.00","0"};

		Table bt=new Table(rs,2,0,2);
		rt.setBody(bt);
		bt.setColAlign(2, Table.ALIGN_CENTER);
		if(rt.body.getRows()>2){
			rt.body.setCellAlign(3, 1,Table.ALIGN_CENTER);
		}
		//
//		rt.body.setUseDefaultCss(true);
		rt.body.ShowZero=false;
		rt.body.setColFormat(arrFormat);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();		//合并行
		rt.body.mergeFixedCols();		//和并列
		rt.setTitle("入厂入炉热值差", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 3, "制表单位:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
		rt.setDefaultTitle(4, 4, strConditonTitle,Table.ALIGN_CENTER);
		rt.setDefaultTitle(9, 2,"单位:吨",Table.ALIGN_RIGHT);
		
		rt.body.setPageRows(36);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "制表时间:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(4, 4, "审核:", Table.ALIGN_CENTER);
		if(((Visit) getPage().getVisit()).getDiancmc().equals("河北大唐")){
			
			rt.setDefautlFooter(9, 2, "制表:",Table.ALIGN_RIGHT);
			}else{
				
			rt.setDefautlFooter(9, 2, "制表:"+((Visit) getPage().getVisit()).getDiancmc

	(),Table.ALIGN_RIGHT);
			}
//		rt.setDefautlFooter(9, 2, "制表:", Table.ALIGN_RIGHT);
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
			((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
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

		df.setValue(this.getBeginriqDate());
		df.Binding("qiandrq1","");// 与html页中的id绑定,并自动刷新
		df.setWidth(80);
		tb1.addField(df);
		tb1.addText(new ToolbarText("至"));
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
			this.setTreeid(null);

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
