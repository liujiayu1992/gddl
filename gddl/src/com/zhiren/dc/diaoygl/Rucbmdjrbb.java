package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;

import com.zhiren.dc.hesgl.jiesd.Balances;
import com.zhiren.dc.hesgl.jiesd.Balances_variable;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
/*
 * 作者:tzf
 * 时间:2009-07-07
 * 内容:煤质检验日报
 */
public class Rucbmdjrbb extends BasePage implements PageValidateListener {

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
		setTbmsg(null);
	}

	private String tbmsg;

	public String getTbmsg() {
		return tbmsg;
	}

	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
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

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// 绑定日期
	private String briq;

	public String getBRiq() {
		if (briq == null || briq.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			briq = DateUtil.FormatDate(new Date());
		}

		return briq;
	}

	public void setBRiq(String briq) {
		this.briq = briq;
	}
	//	取得日期参数SQL





	// 绑定日期
	private String eriq;
	public String getERiq() {
		if (eriq == null || eriq.equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			eriq = DateUtil.FormatDate(new Date());
		}
		return eriq;
	}

	public void setERiq(String eriq) {
		this.eriq = eriq;
	}

	private String getTongjRq(JDBCcon con) {
		if (con == null){
			con = new JDBCcon();
		}
		String tongjrq=" select * from xitxxb where mingc='计量报表统计日期'  and zhuangt=1 and leib='数量' and diancxxb_id="+getTreeid_dc();
		ResultSetList rsl=con.getResultSetList(tongjrq);

		String strTongjrq="daohrq";

		if(rsl.next()){
			strTongjrq=rsl.getString("zhi");
		}

		rsl.close();
		con.Close();
		return strTongjrq;
	}

	//	取得日期参数SQL
	private String getDateParam(){

//		日期条件
		String rqsql = "";
		if(getBRiq() == null || "".equals(getBRiq())){
			rqsql = "and f." + getTongjRq(null) + " >= "+DateUtil.FormatOracleDate(new Date())+"\n";
		}else{
			rqsql = "and f." + getTongjRq(null) +" >= "+DateUtil.FormatOracleDate(getBRiq())+"\n";
		}
		if(getERiq() == null || "".equals(getERiq())){
			rqsql += "and f." + getTongjRq(null) +" < "+DateUtil.FormatOracleDate(new Date())+"+1\n";
		}else{
			rqsql += "and f." + getTongjRq(null) + " < "+DateUtil.FormatOracleDate(getERiq())+"+1\n";
		}
		return rqsql;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	// 页面变化记录2
/*	private String Change2;

	public String getChange2() {
		return Change2;
	}

	public void setChange2(String change2) {
		Change2 = change2;
	}*/

	public boolean getRaw() {
		return true;
	}

	// 厂别下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if (visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="
					+ visit.getDiancxxb_id());
		} else {
			sb.append("select id,mingc from diancxxb where id="
					+ visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}

	private String REPORT_NAME_MEIZJYRB = "Meizjyrb";// zhillsb中的数据

	private String REPORT_NAME_MEIZJYRB_ZHILB = "Meizjyrb_zhilb";// zhilb中的数据

	private String REPORT_NAME_MEIZJYRB_ZHILB_1="Meizjyrb_zhilb_1";

	private String REPORT_NAME_MEIZJYRB_ZHILB_hd="Meizjyrb_zhilb_hd";
	/**2008-10-18 huochaoyuan
	 *由于一个批次煤生成多个采样编号的情况，导致原先报表显示有问题，故新添加一个样式的报表；
	 *报表函数getMeizjyrb_zhilb_1()
	 **/
	private String mstrReportName = "";

	private boolean blnIsBegin = false;

	// private String leix = "";

	public String getPrintTable() {

			return getMeizjyrb();


	}
	public double getDanj(	String SelIds, long Diancxxb_id, long Jieslx, long Gongysb_id,
							  long Hetb_id, String Jieszbsftz, String Yansbh, double Jieskdl, double Jieskkje, long Yunsdwb_id,
							  double Shangcjsl, String Yunsdw, Visit visit, double Yujsjz){

		try{
			Balances bls = new Balances();
			Balances_variable bsv = new Balances_variable(); // Balances变量
			bls.setBsv(bsv);
			bls.getBalanceData( SelIds,  Diancxxb_id,  Jieslx,  Gongysb_id,
					Hetb_id,  Jieszbsftz,  Yansbh,  Jieskdl,  Jieskkje,  Yunsdwb_id,
					Shangcjsl,  Yunsdw,  visit,  Yujsjz
			);

			bsv = bls.getBsv();
			return bsv.getHansmj();
		}catch (Exception e){
			e.printStackTrace();
		}
		return 0;
	}


	//  判断电厂Tree中所选电厂时候还有子电厂
	private boolean hasDianc(String id){
		JDBCcon con=new JDBCcon();
		boolean mingc= false;
		String sql="select mingc from diancxxb where fuid = " + id;
		ResultSetList rsl=con.getResultSetList(sql);
		if(rsl.next()){
			mingc=true;
		}
		rsl.close();
		return mingc;
	}

	private String getMeizjyrb() {
		Visit v = (Visit) this.getPage().getVisit();
		Report rt = new Report();
		JDBCcon con = new JDBCcon();

		String s="";

		if(!this.hasDianc(this.getTreeid_dc())){
			s="	  and f.diancxxb_id="+this.getTreeid_dc()+" \n";//增加 厂别处理条件;
		}
		// DateUtil custom = new DateUtil();
		String sql = "select \n" +
				"decode(grouping(riq),1,'入厂合计',to_char(riq,'yyyy-mm-dd')) riq,\n" +
				"decode(grouping(p.mingc)+grouping(riq),1,'原煤合计',2,'入厂合计',p.mingc) pinz,\n" +
				"decode(grouping(riq)+grouping(m.mingc)+grouping(p.mingc),1,'小计'，2,'原煤合计',3,'入厂合计',m.mingc) meikdw,\n" +
				"sum(kuangfsl) kuangfsl,sum(changfsl),\n" +
				"decode(sum(kuangfsl),0,0,sum(kuangfrz*kuangfsl)/sum(kuangfsl)),\n" +
				"sum(changfrz),sum(mt),sum(aad),sum(vdaf),sum(stad),sum(RUCYMDJ),sum(yunf),sum(RUCBMDJ),sum(biaoml) \n" +
				"from RUCBMDJRBB d\n" +
				"inner join pinzb p on d.pinzb_id=p.id\n" +
				"inner join meikxxb m on d.meikxxb_id=m.id\n" +
				" group by rollup(riq,p.mingc,m.mingc)";
		ResultSetList rs = con.getResultSetList(sql);
		ResultSet rset = null;
		String[][] ArrHeader=null;
		//String[] strFormat=null;
		int[] ArrWidth=null;
		String insertSql=null;
		String delsql=null;

		 ArrHeader = new String[1][13];

		ArrHeader[0] = new String[]{"时间","入厂合计", "入厂合计","矿方数量", "厂收数量", "矿方热值（kcal/kg）", "入厂热值（kcal/kg",
				"收到基全水份（%）", "干燥基灰分（%）", "干燥无灰基挥发分（%）", "干燥基全硫（%）",  "入厂原煤单价（元/吨）",
				"运费（元/吨）","入厂标煤单价（不含税）（元/吨）", "标煤量（吨）" };

		ArrWidth = new int[13];

		ArrWidth = new int[]{80, 120, 60, 60, 60, 60, 60, 60, 60, 60, 60,
				60, 60};

		rt.setTitle("入厂原煤单价报表"
				+ ((getChangbValue().getId() > 0 && getChangbModel()
				.getOptionCount() > 2) ? "("
				+ getChangbValue().getValue() + ")" : ""), ArrWidth);
		rt.title.setRowHeight(2, 45);
		rt.title.setRowCells(2, Table.PER_FONTSIZE, 22);
		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
		//rt.setDefaultTitle(1, 5, "检验日期:" + getRiq(), Table.ALIGN_LEFT);
		rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		rt.body.setPageRows(22);
//		rt.body.mergeFixedRow();
//		rt.body.mergeFixedCols();
//		rt.body.mergeFixedRowCol();
		rt.body.merge(1,1,rt.body.getRows(),3);
		//rt.body.setColFormat(strFormat);
		for (int i = 1; i <= 13; i++) {
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		}

		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 3, "打印日期:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
		rt.setDefautlFooter(8, 2, "主管:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(10, 2, "审核:", Table.ALIGN_LEFT);
		rt.setDefautlFooter(12, 2, "制表:", Table.ALIGN_LEFT);
		rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

		// 设置页数
		_CurrentPage = 1;
		_AllPages = rt.body.getPages();
		if (_AllPages == 0) {
			_CurrentPage = 0;
		}
		con.Close();
		rt.body.setRowHeight(24);

		return rt.getAllPagesHtml();

	}


	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}


	private boolean _CreateButton = true;

	public void CreateButton(IRequestCycle cycle) {
		_CreateButton = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}else if(_CreateButton){
			_CreateButton = false;
			create();
			getSelectData();
		}
	}
private void create(){
	Visit v = (Visit) this.getPage().getVisit();
	JDBCcon con = new JDBCcon();
	String sql = "select wm_concat(distinct fh.lie_id) SelIds ,h.id hetb_id,g.id gongsyb_id,sum(fh.koud) koud, fh.pinzb_id,g.fuid meikdqb_id,\n" +
			"fh.meikxxb_id,\n" +
			"to_char(fh.daohrq,'yyyy-mm-dd') riq, \n" +
			"sum(hb.hetl) kuangfsl,\n" +
			"sum(fh.laimsl) changfsl,\n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(kb.qnet_ar*hb.hetl)/sum(hb.hetl))/0.0041816,0) as  kuangfrz,\n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(zhi.qnet_ar*hb.hetl)/sum(hb.hetl))/0.0041816,0) as  changfrz,\n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(zhi.mt*hb.hetl)/sum(hb.hetl)),2) as mt, \n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(zhi.aad*hb.hetl)/sum(hb.hetl)),2) as aad,\n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(zhi.vdaf*hb.hetl)/sum(hb.hetl)),2) as  vdaf,\n" +
			"round_new(decode(sum(hb.hetl),0,0,sum(zhi.stad*hb.hetl)/sum(hb.hetl)),2) as  stad,\n" +
			"--round_new(ru.rucymdj,2) as\n" +
			"round_new(sum(jgb.yunj),2) as  yunf,\n" +
			"--round_new(rucbmdj,\n" +
			"round(sum(fh.laimsl)*round_new(decode(sum(hb.hetl),0,0,sum(zhi.qnet_ar*hb.hetl)/sum(hb.hetl)),2)/0.0041816/7000,2) as biaoml\n" +
			"from  hetb h\n" +
			"left join  hetslb hb on h.id=hb.hetb_id\n" +
			"left join hetjgb jgb on h.id=jgb.hetb_id\n" +
			"left join fahb fh on h.id=fh.hetb_id\n" +
			"left join zhilb zhi on fh.zhilb_id=zhi.id\n" +
			"left join kuangfzlb kb on fh.zhilb_id=zhi.id\n" +
			"left join gongysb g on fh.gongysb_id =g.id \n" +
			"where  fh.daohrq between date'"+getBRiq()+"'and date'"+getERiq() +
			"and fh.pinzb_id in (21,62)\n" +
			"group by fh.daohrq,fh.pinzb_id,g.fuid,fh.meikxxb_id,h.id,g.id";
	ResultSetList rs = con.getResultSetList(sql);
		String delsql = "delete from RUCBMDJRBB where  riq between date'"+getBRiq()+ "'and date'"+getERiq()+"'";
		con.getUpdate(delsql);
		String insertSql=null;

//
////logger.info("Jieslx "+((Visit) getPage().getVisit()).getLong2());
////logger.info("Gongysb_id"+((Visit) getPage().getVisit()).getLong3());
////logger.info("Hetb_id"+((Visit) getPage().getVisit()).getLong8());
//
//
//	logger.info("Jieszbsftz"+((Visit) getPage().getVisit()).getString2());
////logger.info("Yansbh"+((Visit) getPage().getVisit()).getString4());//""
////logger.info("Jieskdl"+Double.parseDouble(((Visit) getPage().getVisit()).getString7()));//扣吨
//	logger.info("Jieskkje"+Double.parseDouble(((Visit) getPage().getVisit()).getString18()));//0
////logger.info("Yunsdwb_id"+((Visit) getPage().getVisit()).getLong9());//
//	logger.info("Shangcjsl"+Double.parseDouble(((Visit) getPage().getVisit()).getString12()));//0
//
////logger.info("Yunsdw"+((Visit) getPage().getVisit()).gdetString10());
////logger.info("visit"+visit);
//
//	logger.info("Yujsjz"+yujsjz);//0


		while (rs.next()) {
			double danj=getDanj(rs.getString("selids"),
					Long.parseLong(this.getTreeid()),
					2,rs.getLong("gongysb_id"),
					rs.getLong("hetb_id"),"否","",rs.getDouble("koud"),
					0,-1,0,"",v,0
			);
			insertSql = "insert into RUCBMDJRBB (id,MEIKDQB_ID,MEIKXXB_ID,RIQ,KUANGFSL,CHANGFSL,KUANGFRZ,CHANGFRZ,MT,AAD,VDAF,STAD,RUCYMDJ,YUNF,RUCBMDJ,BIAOML,pinzb_id)values("
					+ "getnewid("+this.getTreeid()+"),"
					+ rs.getLong("meikdqb_id")+","
					+ rs.getLong("MEIKXXB_ID")+",date'"
					+ rs.getString("RIQ")+"',"
					+ rs.getDouble("KUANGFSL")+"," + rs.getDouble("CHANGFSL")+"," + rs.getDouble("KUANGFRZ")+","+rs.getDouble("CHANGFRZ")+"," + rs.getDouble("MT")+"," +
					rs.getDouble("AAD")+"," + rs.getDouble("VDAF")+"," + rs.getDouble("STAD")+"," + danj+"," +
					rs.getDouble("YUNF")+"," + "round(("+danj+"/1.17+"+rs.getDouble("YUNF")+"/1.11)*7000/"+rs.getDouble("CHANGFSL")+",2)," + rs.getDouble("BIAOML")+","+ rs.getLong("pinzb_id")+")";
			con.getUpdate(insertSql);
		}
		rs.close();
		con.Close();
}
	//	-------------------------电厂Tree-----------------------------------------------------------------
	public IPropertySelectionModel getDiancmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDiancmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setDiancmcModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getDiancmcModels() {
		Visit visit=(Visit)this.getPage().getVisit();
		String sql = " select d.id,d.mingc from diancxxb d where d.id="+visit.getDiancxxb_id()+" \n";
		sql+=" union \n";
		sql+="  select d.id,d.mingc from diancxxb d where d.fuid="+visit.getDiancxxb_id()+" \n";
		setDiancmcModel(new IDropDownModel(sql));
	}

	public String getTreeid_dc() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid_dc(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
	}
	DefaultTree dc ;

	public DefaultTree getTree_dc() {
		return dc;
	}

	public void setTree_dc(DefaultTree etu) {
		dc= etu;
	}



	// 刷新衡单列表
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");


		tb1.addText(new ToolbarText("到货日期:"));
		DateField dfb = new DateField();
		dfb.setValue(getBRiq());
		dfb.Binding("BRIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("BRIQ");
		tb1.addField(dfb);
		tb1.addText(new ToolbarText("-"));
		tb1.addText(new ToolbarText(" 至 "));
		DateField dfe = new DateField();
		dfe.setValue(getERiq());
		dfe.Binding("ERIQ", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("ERIQ");
		tb1.addField(dfe);
		tb1.addText(new ToolbarText("-"));


		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn);
		//tb1.addFill();

		ToolbarButton rbtn2 = new ToolbarButton(null, "生成",
				"function(){document.getElementById('CreateButton').click();}");
		rbtn2.setIcon(SysConstant.Btn_Icon_Insert);
		tb1.setWidth("bodyWidth");
		tb1.addItem(rbtn2);
		//tb1.addFill();
		setToolbar(tb1);

	}


	//------------运输单位----------

	private String meikid = "";
	public String getMeikid() {
		if (meikid==null || meikid.equals("")) {

			meikid = "0";
		}
		return meikid;
	}
	public void setMeikid(String meikid) {
		if(meikid!=null) {
			if(!meikid.equals(this.meikid)) {
				((TextField)getToolbar().getItem("meikTree_text")).setValue
						(((IDropDownModel)this.getMeikModel()).getBeanValue(Long.parseLong(meikid)));
				this.getTree().getTree().setSelectedNodeid(meikid);
			}
		}
		this.meikid = meikid;
	}



	//	获得运输单位 树形结构sql
	private StringBuffer getDTsql(){

		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();

		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from yunsdwb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");

		return bf;
	}


	DefaultTree mktr;

	public DefaultTree getTree() {
		return mktr;
	}
	public void setTree(DefaultTree etu) {
		mktr=etu;
	}

	public String getTreeScriptMK() {
		return this.getTree().getScript();
	}




	public IPropertySelectionModel getMeikModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getMeikModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getMeikModels() {
		String sql = "select 0 id,'全部' mingc from dual union select id,mingc  from yunsdwb";
		setMeikModel(new IDropDownModel(sql));
	}

	//-------------------------------------------------


	//	获取煤矿
	private String treeid;
	public String getTreeid() {
		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
			treeid="0";
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if(treeid!=null) {
			if(!treeid.equals(this.treeid)) {
				((TextField)getToolbar().getItem("gongysTree_text")).setValue
						(((IDropDownModel)getGongysDropDownModel()).getBeanValue(Long.parseLong(treeid)));
				((Visit) this.getPage().getVisit()).getDefaultTree().getTree().setSelectedNodeid(treeid);
			}
		}
		this.treeid = treeid;
	}
	public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	private StringBuffer getMKSql(){
		Visit visit=(Visit)this.getPage().getVisit();
		StringBuffer bf=new StringBuffer();

		bf.append(" select distinct * from ( \n");
		bf.append(" select 0 id,'全部' mingc,0 jib from dual\n");
		bf.append(" union\n");
		bf.append(" select id ,mingc,1 jib from meikxxb");
		bf.append(" ) \n");
		bf.append(" order by id,mingc\n");

		return bf;
	}
	public IPropertySelectionModel getGongysDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getGongysDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	public void setGongysDropDownModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	public void getGongysDropDownModels() {
		String sql="";

		sql+="  select 0 id, '全部' mingc from dual union select id ,mingc from meikxxb";

		setGongysDropDownModel(new IDropDownModel(sql)) ;
		return ;
	}

	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);

	}

	public String getToolbarScript() {

		return getToolbar().getRenderScript();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			visit.setString1("");
			setChangbValue(null);
			setChangbModel(null);
			this.setMeikid(null);
			this.setTreeid(null);
			this.getMeikModels();
			this.getGongysDropDownModels();
			this.getDiancmcModels();
			setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
		if (cycle.getRequestContext().getParameters("lx") != null) {
			visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
			mstrReportName = visit.getString1();
		} else {
			if (visit.getString1().equals("")) {
				mstrReportName = visit.getString1();
			}
		}

		blnIsBegin = true;
	}

	//	页面登陆验证
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

}
