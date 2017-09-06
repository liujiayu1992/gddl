package com.zhiren.gs.bjdt.diaoygl;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shouhcreport_bjdt extends BasePage {
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

	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	
    public String getTianzdwQuanc() {
		return getTianzdwQuanc(getDiancxxbId());
	}

	public long getDiancxxbId() {

		return ((Visit) getPage().getVisit()).getDiancxxb_id();
	}
	
//	 得到单位全称
	public String getTianzdwQuanc(long gongsxxbID) {
		String _TianzdwQuanc = "";
		JDBCcon cn = new JDBCcon();

		try {
			ResultSet rs = cn
					.getResultSet(" select quanc from diancxxb where id="
							+ gongsxxbID);
			while (rs.next()) {
				_TianzdwQuanc = rs.getString("quanc");
			}
			if (_TianzdwQuanc.equals("北京大唐燃料有限公司")) {
				_TianzdwQuanc = "大唐国际发电股份有限公司燃料管理部";
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return _TianzdwQuanc;
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

//		String Start_riq=getBeginriqDate();
		String End_riq=getEndriqDate();

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

		String sbsql=
			"select decode(grouping(dc.mingc) + grouping(dc.leib),2,'北京大唐燃料有限公司',1,'★' || dc.leib || '小计',max(dc.mingc)) as diancmc,\n" +
			"       sum(dh.hej * 10000) as 本月计划,\n" + 
			"       sum(round(dh.hej * 10000 /daycount(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')),0)) as 日均计划,\n" + 
			"       sum(h.dangrgm) as 当日供煤,\n" + 
			"       sum(lj.dangrgm) as 累计供煤,\n" + 
			"       sum((round(dh.hej * 10000 /daycount(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')),0) * to_char(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd'), 'dd'))) as 累计应发,\n" + 
			"       sum(((round(dh.hej / daycount(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')),0) *to_char(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd'), 'dd')) - h.dangrgm)) as 超欠,\n" + 
			"       sum(decode(dh.hej, 0, 0, round(lj.dangrgm / (dh.hej * 10000), 2))) as 到货率,\n" + 
			"       sum(h.dangrgm) as 当日,\n" + 
			"       sum(lj.dangrgm) as 累计,\n" + 
			"       sum(h.haoyqkdr) as 当日耗用,\n" + 
			"       sum(lj.haoyqkdr) as 累计耗用,\n" + 
			"       sum(h.kuc) as 库存,\n" + 
			"       decode(grouping(dc.mingc),1,'',sum(decode(hm.meizrjhm, 0, 0, round(h.kuc / hm.meizrjhm)))) as 可用天数,\n" + 
			"       sum(dc.zhengccb) as 正常储备线\n" + 
			"  from (select *\n" + 
			"          from shouhcrbb h\n" + 
			"         where h.riq = to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')) h,\n" + 
			"       (select dh.diancxxb_id, sum(dh.hej) as hej\n" + 
			"          from niancgjhb dh\n" + 
			"         where dh.riq = First_day(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd'))\n" + 
			"         group by dh.diancxxb_id) dh,\n" + 
			"       (select h.diancxxb_id,\n" + 
			"               sum(h.dangrgm) as dangrgm,\n" + 
			"               sum(h.haoyqkdr) as haoyqkdr\n" + 
			"          from shouhcrbb h\n" + 
			"         where h.riq >= First_day(to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd'))\n" + 
			"           and h.riq <= to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')\n" + 
			"         group by h.diancxxb_id) lj,\n" + 
			"       (select dc.id, px.xuh, dc.mingc,lb.mingc as leib,lb.xuh as lbxh, dc.zhengccb\n" + 
			"          from diancxxb dc, dianckjpxb px,dianclbb lb\n" + 
			"         where dc.id = px.diancxxb_id\n" + 
			"           and dc.jib = 3 and dc.dianclbb_id=lb.id\n" + 
			"           and px.kouj = '月报') dc,\n" + 
			"       (select hc.diancxxb_id,\n" + 
			"               nvl(round(sum(hc.haoyqkdr) / 7), 0) as meizrjhm\n" + 
			"          from shouhcrbb hc\n" + 
			"         where hc.riq >= to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd') - 6\n" + 
			"           and hc.riq <= to_date('"+this.getEndriqDate()+"', 'yyyy-mm-dd')\n" + 
			"         group by (hc.diancxxb_id)) hm\n" + 
			" where h.diancxxb_id(+) = dc.id\n" + 
			"   and dc.id = dh.diancxxb_id(+)\n" + 
			"   and dc.id = lj.diancxxb_id(+)\n" + 
			"   and hm.diancxxb_id(+) = dc.id\n" + 
			" group by rollup((dc.leib,dc.lbxh), (dc.mingc, dc.xuh))\n" + 
			" order by grouping(dc.leib) desc,\n" + 
			"          dc.lbxh,\n" + 
			"          grouping(dc.mingc) desc,\n" + 
			"          dc.xuh";


		//System.out.println(sbsql);
		ResultSet rs = con.getResultSet(sbsql);
		Report rt = new Report();
		String ArrHeader[][] =new String[3][15];
		 ArrHeader[0]=new String[] {"单位名称","合同计划执行情况","合同计划执行情况","合同计划执行情况","合同计划执行情况","合同计划执行情况","合同计划执行情况","合同计划执行情况","供煤总量","供煤总量","耗用情况","耗用情况","库存","可用天数","正常储备线"};
		 ArrHeader[1]=new String[] {"单位名称","本月计划","日均计划","当日供煤","累计供煤","累计应发","超欠 + -","到货率 %","当日","累计","当日","累计","库存","可用天数","正常储备线"};
		 ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15"};
		 int ArrWidth[]=new int[] {150,60,60,60,60,60,60,60,60,60,60,60,60,60,60};
//		String arrFormat[]=new String[]{"","","0","0","0.00","0","0","0.00","0.00","0"};

		Table bt=new Table(rs,3,0,1);
		rt.setBody(bt);
		//
//		rt.body.setUseDefaultCss(true);
		rt.body.ShowZero=false;
//		rt.body.setColFormat(arrFormat);
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.mergeFixedRow();		//合并行
		rt.body.mergeFixedCols();		//和并列
		rt.setTitle(FormatDate(DateUtil.getDate(End_riq))+"收耗存日报", ArrWidth);
		rt.title.setRowHeight(2, 50);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		rt.setDefaultTitle(1, 4, "制表单位:" +getTianzdwQuanc(),Table.ALIGN_LEFT);

		rt.setDefaultTitle(14,2,"单位：吨",Table.ALIGN_RIGHT);
		rt.setDefaultTitle(6,3,FormatDate(DateUtil.getDate(End_riq)),Table.ALIGN_CENTER);
		rt.body.setPageRows(30);
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
		rt.setDefautlFooter(6, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "制表：", Table.ALIGN_LEFT);
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

	private boolean _UploadClick = false;

	public void UploadButton(IRequestCycle cycle) {
		_UploadClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_QueryClick) {
			_QueryClick = false;
		}
		if (_UploadClick) {
			_UploadClick = false;
			ShengcTXTFile();
		}
	}

	 public void ShengcTXTFile()
	    {
		 UploadData up = new UploadData();
	    	
		 up.setDate(DateUtil.getDate(getEndriqDate()));
		 up.ShangcTXTFile(true);//上传方式：true=手动,false=自动
	    	String msg = up.getMsg();
	    	if(msg.equals("") || msg==null){
	    		return;
	    	}else{
	    		setMsg(msg);
	    		getShouhcreport();
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

//		tb1.addText(new ToolbarText("单位:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);

		if(getShangbry(visit.getString1())){
			ToolbarButton tb3 = new ToolbarButton(null,"上报","function(){document.getElementById('UploadButton').click();}");
			tb1.addItem(tb3);
			tb1.addText(new ToolbarText("-"));
		}
		
		ToolbarButton tb = new ToolbarButton(null,"刷新","function(){document.Form0.submit();}");
		tb1.addItem(tb);

		setToolbar(tb1);
	}
	
	
	public boolean getShangbry(String visitUserName){//获取有日报上传权限的用户名
		JDBCcon con = new JDBCcon();
		boolean blUserName = false;
		String sql = "select xt.zhi from xitxxb xt where xt.mingc='日报上报人员' and xt.zhi='"+visitUserName+"'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			blUserName = true;
		}
		con.Close();
		return blUserName;
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
