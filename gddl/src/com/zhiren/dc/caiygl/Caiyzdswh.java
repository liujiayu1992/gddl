package com.zhiren.dc.caiygl;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caiyzdswh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}

	// 保存数据list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	boolean riqichange1 = false;

	boolean riqichange2 = false;

	private String riqi1; // 页面起始日期选择

	private String riqi2; // 页面结束日期选择

	public String getRiqi1() {
		if (riqi1 == null || riqi1.equals("")) {
			riqi1 = DateUtil.FormatDate(new Date());
		}
		return riqi1;
	}

	public void setRiqi1(String riqi1) {

		if (this.riqi1 != null && !this.riqi1.equals(riqi1)) {
			this.riqi1 = riqi1;
			riqichange1 = true;
		}
	}

	public String getRiqi2() {
		if (riqi2 == null || riqi2.equals("")) {
			riqi2 = DateUtil.FormatDate(new Date());
		}
		return riqi2;
	}

	public void setRiqi2(String riqi2) {
		if (this.riqi1 != null && !this.riqi2.equals(riqi2)) {
			this.riqi2 = riqi2;
			riqichange2 = true;

		}

	}

	public static String nvlStr(String strValue){
		if (strValue==null || strValue=="") {
			return "0";
		}
		return strValue;
	}
	
	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	private void 	Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		sql.append("begin \n");
		
		ResultSetList rsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()) {
			sql.append("delete from caiyzdsb where id = ").append(rsl.getString("id")).append("; \n");
		}
		rsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			if ("0".equals(mdrsl.getString("id"))) {
			
				sql.append("insert into caiyzdsb(id,diancxxb_id,riq,jincsj,zhilb_id,yunsfs,pinz,caiybm,meilzdbcld,ches,daob,pingj,zhibry,zhiyff,caiyry,caiyff,cheph,caiyfs,ziygs,ziyzl,laimqk) values(")
				.append("getnewid("+ getTreeid() +"),").append(getTreeid()).append(",")
				.append("to_date('").append(mdrsl.getString("caiyrq")).append("','yyyy-mm-dd')").append(",")
				.append("to_date('").append(mdrsl.getString("jincsj")).append("','hh24:mi')").append(",")
				.append(mdrsl.getString("zhilb_id")).append(", '")
				.append(mdrsl.getString("yunsfs")).append("', '")
				.append(mdrsl.getString("pinz")).append("',' ")
				.append(mdrsl.getString("caiybm")).append("', ")
				.append(nvlStr(mdrsl.getString("meilzdbcld"))).append(", ")
				.append(mdrsl.getString("ches")).append(",' ")
				.append(mdrsl.getString("daob")).append("',' ")
				.append(mdrsl.getString("pingj")).append("', '")
			   	.append(visit.getRenymc()).append("', '")
			   	.append(mdrsl.getString("zhiyff")).append("', '")
			   	.append(mdrsl.getString("caizry")).append("', '")
			   	.append(mdrsl.getString("caiyff")).append("', '")
			   	.append(mdrsl.getString("cheph")).append("', '")
			   	.append(mdrsl.getString("caiyfs")).append("', ")
				.append(nvlStr(mdrsl.getString("ziygs"))).append(", ")
			    .append(nvlStr(mdrsl.getString("ziyzl"))).append(",' ")
				.append(mdrsl.getString("laimqk")).append("'); \n");
			}else{
			sql.append("update caiyzdsb set").append(" ")
			.append("riq=").append("to_date('").append(mdrsl.getString("caiyrq")).append("','yyyy-mm-dd')").append(",")
			.append("jincsj=").append("to_date('").append(mdrsl.getString("jincsj")).append("','hh24:mi')").append(",")
			.append("zhilb_id='").append(mdrsl.getString("zhilb_id")).append("',")
			.append("yunsfs='").append(mdrsl.getString("yunsfs")).append("',")
			.append("pinz='").append(mdrsl.getString("pinz")).append("',")
			.append("caiybm='").append(mdrsl.getString("caiybm")).append("',")
			.append("meilzdbcld=").append(nvlStr(mdrsl.getString("meilzdbcld"))).append(",")
			.append("ches=").append(mdrsl.getString("ches")).append(",")
			.append("daob='").append(mdrsl.getString("daob")).append("',")
			.append("pingj='").append(mdrsl.getString("pingj")).append("',")
			.append("zhibry='").append(visit.getRenymc()).append("',")
			.append("zhiyff='").append(mdrsl.getString("zhiyff")).append("',")
			.append("caiyry='").append(mdrsl.getString("caiyry")).append("',")
			.append("caiyff='").append(mdrsl.getString("caiyff")).append("',")
			.append("cheph='").append(mdrsl.getString("cheph")).append("',")
			.append("caiyfs='").append(mdrsl.getString("caiyfs")).append("',")
			.append("ziygs=").append(nvlStr(mdrsl.getString("ziygs"))).append(",")
			.append("ziyzl=").append(nvlStr(mdrsl.getString("ziyzl"))).append(",")
			.append("laimqk='").append(mdrsl.getString("laimqk"))
			.append("' where id = ")
			.append(mdrsl.getString("id")).append("; \n");
	    }
	}
	sql.append("end;");
	con.getResultSet(sql.toString());
	}

	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}

	private boolean _Delete = false;

	public void DeleteButton(IRequestCycle cycle) {

		_Delete = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}

		if (_Delete) {
			_Delete = false;

			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String caiyrqb = DateUtil.FormatOracleDate(getRiqi1());
		String caiyrqe = DateUtil.FormatOracleDate(getRiqi2());
		String sql;
		sql ="select nvl(z.diancxxb_id,"+getTreeid()+")diancxxb_id,\n"+
		"           nvl(z.id,0)  id,\n" + 
		"           nvl(z.zhilb_id,s.zhilb_id) zhilb_id,\n"+
		"           nvl(z.riq,s.caiyrq) caiyrq,\n" + 
		"           nvl(to_char(z.jincsj,'hh24:mi'),s.sj ) jincsj,\n" + 
		"           nvl(z.caiybm, s.cybh) caiybm,\n" + 
		"           nvl(z.daob,s.db) daob,\n" + 
		"           z.ziygs,\n" + 
		"           z.ziyzl,\n" + 
		"           z.meilzdbcld ,\n" + 
		"           z.laimqk,\n" + 
		"           nvl(z.yunsfs,s.ysfs) yunsfs,\n" + 
		"           nvl(z.pinz,s.pz) pinz,\n" + 
		"           nvl(z.ches,s.cs) ches,\n" + 
		"           nvl(z.pingj,'正常') pingj,\n" + 
		"           s.cyry caizry,\n" + 
		"           nvl(z.zhibry,'"+visit.getRenymc()+"') zhibry,\n" + 
		"           nvl(z.caiyfs, '机械') caiyfs,\n" + 
		"           nvl(z.caiyff,s.caiyff) caiyff,\n" + 
		"           nvl(z.zhiyff,s.zhiyff) zhiyff,\n" + 
		"           nvl(z.cheph,s.cph) cheph\n" + 
		"  from (select c.zhilb_id,\n" + 
		"               c.caiyrq,\n" + 
		"               jcsj.sj,\n" + 
		"               getcaiybh4zl(c.zhilb_id) cybh,\n" + 
		"               fh.ysfs,\n" + 
		"               gethuaypz4zl(c.zhilb_id) pz,\n" + 
		"               fh.cs,\n" + 
		"               jcsj.db,\n" + 
		"               getcaiyry4zl(c.zhilb_id) cyry,\n" + 
		"               (select nvl(min(mingc), ' ') as mingc\n" + 
		"                  from guobb\n" + 
		"                 where xiangmmc = '采样方法') AS CAIYFF,\n" + 
		"               (select nvl(min(mingc), ' ') as mingc\n" + 
		"                  from guobb\n" + 
		"                 where xiangmmc = '制样方法') AS ZHIYFF,\n" + 
		"               gethuaybbcheps(c.zhilb_id) cph\n" + 
		"          from (select distinct c.id, c.caiyrq, c.zhilb_id\n" + 
		"                  from caiyb c, fahb f, diancxxb d\n" + 
		"                 where c.zhilb_id = f.zhilb_id\n" + 
		"                   and f.diancxxb_id = d.id\n" + 
		"                   and c.caiyrq >= "+caiyrqb+"\n" + 
		"                   and c.caiyrq < "+caiyrqe+" + 1\n" + 
		"                   and (d.fuid ="+getTreeid()+" or d.id ="+getTreeid()+")) c,\n" + 
		"               (select f.zhilb_id,\n" + 
		"                       min(to_char(zhongcsj, 'HH24:mi')) sj,\n" + 
		"                       min(p.zhongchh) db\n" + 
		"                  from chepb p, fahb f\n" + 
		"                 where p.fahb_id = f.id\n" + 
		"                 group by f.zhilb_id) jcsj,\n" + 
		"               (select f.zhilb_id, min(ys.mingc) ysfs, sum(f.ches) cs\n" + 
		"                  from fahb f, yunsfsb ys\n" + 
		"                 where f.yunsfsb_id = ys.id\n" + 
		"                 group by f.zhilb_id) fh\n" + 
		"         where c.zhilb_id = jcsj.zhilb_id\n" + 
		"           and c.zhilb_id = fh.zhilb_id) s,\n" + 
		"       caiyzdsb z\n" + 
		" where s.zhilb_id = z.zhilb_id(+)  order by caiyrq,jincsj";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// /设置显示列名称
		egu.setTableName("caiyzdsb");
		egu.getColumn("diancxxb_id").setHeader("电厂信息");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHeader("质量表id");
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("caiybm").setHeader("采样编码");
		egu.getColumn("caiybm").setWidth(110);
		egu.getColumn("caiybm").setEditor(null);
		egu.getColumn("jincsj").setHeader("进车时间");
		egu.getColumn("jincsj").setWidth(60);
		egu.getColumn("caiyfs").setHeader("采样方式");
		egu.getColumn("caiyfs").setWidth(60);
		egu.getColumn("caiyrq").setHeader("采样日期");
		egu.getColumn("caiyrq").setWidth(90);
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("yunsfs").setWidth(50);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		egu.getColumn("ches").setEditor(null);		
		egu.getColumn("pingj").setHeader("评价");
		egu.getColumn("pingj").setWidth(50);
		egu.getColumn("caizry").setHeader("采样人员");
		egu.getColumn("caizry").setWidth(70);
		egu.getColumn("caizry").setEditor(null);
		egu.getColumn("zhibry").setHeader("值班人员");
		egu.getColumn("zhibry").setWidth(70);
		egu.getColumn("zhibry").setDefaultValue(visit.getRenymc());
		egu.getColumn("zhibry").setEditor(null);
		egu.getColumn("caiyff").setHeader("采样方法");
		egu.getColumn("caiyff").setWidth(90);
		egu.getColumn("zhiyff").setHeader("制样方法");
		egu.getColumn("zhiyff").setWidth(90);
		egu.getColumn("daob").setHeader("道别");
		egu.getColumn("daob").setWidth(50);
		egu.getColumn("cheph").setHeader("车皮号");
		egu.getColumn("cheph").setWidth(120);
		egu.getColumn("cheph").setHidden(true);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("laimqk").setHeader("来煤情况");
		egu.getColumn("laimqk").setWidth(220);
		egu.getColumn("ziygs").setHeader("子样个数");
		egu.getColumn("ziygs").setWidth(70);
		egu.getColumn("ziyzl").setHeader("子样质量");
		egu.getColumn("ziyzl").setWidth(70);
		egu.getColumn("meilzdbcld").setHeader("来煤最大标称粒度");
		egu.getColumn("meilzdbcld").setWidth(120);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);//设定grid可以编辑
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);//设置分页
		//设置页面的宽度,当超过这个宽度时显示滚动条
		//*****************************************设置默认值****************************
		egu.getColumn("diancxxb_id").setDefaultValue(""+visit.getDiancxxb_id());
		
		//采样方式下拉框
		List cy = new ArrayList();
		cy.add(new IDropDownBean(1, "人工"));
		cy.add(new IDropDownBean(2, "机械"));
		ComboBox cyfs = new ComboBox();
		cyfs.setEditable(true);
	    egu.getColumn("caiyfs").setEditor(cyfs);
		egu.getColumn("caiyfs").setComboEditor(egu.gridId,new IDropDownModel(cy));
		egu.getColumn("caiyfs").setDefaultValue("机械");
		
//		道别下拉框
		List db = new ArrayList();
		db.add(new IDropDownBean(1, "1"));
		db.add(new IDropDownBean(2, "2"));
		db.add(new IDropDownBean(3, "3"));
		ComboBox daob = new ComboBox();
		daob.setEditable(true);
	    egu.getColumn("daob").setEditor(daob);
		egu.getColumn("daob").setComboEditor(egu.gridId,new IDropDownModel(db));
		
//		//评价下拉框
		List pj = new ArrayList();
		pj.add(new IDropDownBean(0, "好"));
		pj.add(new IDropDownBean(1, "正常"));
		pj.add(new IDropDownBean(2, "不好"));
		pj.add(new IDropDownBean(3, "差"));
		egu.getColumn("pingj").setEditor(new ComboBox());
		egu.getColumn("pingj").setComboEditor(egu.gridId,new IDropDownModel(pj));
		egu.getColumn("pingj").setDefaultValue("正常");
		
//		 设置品种下拉框
		ComboBox pinz = new ComboBox();
		egu.getColumn("pinz").setEditor(pinz);
		pinz.setEditable(true);
		String pinzSql ="select id,mingc from pinzb where leib = '煤' order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,new IDropDownModel(pinzSql));
		
//		 设置运输方式下拉框
		ComboBox yunsfs = new ComboBox();
		egu.getColumn("yunsfs").setEditor(yunsfs);
		yunsfs.setEditable(true);
		String yunsfsSql ="select id,mingc from yunsfsb  where mingc ='公路'or mingc='铁路'";
		egu.getColumn("yunsfs").setComboEditor(egu.gridId,new IDropDownModel(yunsfsSql));

		// 工具栏
		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi1());
		df.setReadOnly(true);
		df.Binding("RIQI1", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi1");
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("至:");

		DateField df1 = new DateField();
		df1.setValue(this.getRiqi2());
		df1.setReadOnly(true);
		df1.Binding("RIQI2", "forms[0]");
		df1.setId("riqi2");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");// 设置分隔符
		
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// 只能选中单行
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
    	// 删除按钮
		egu.addToolbarButton(GridButton.ButtonType_Delete,"DeleteButton");
		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}

	private String treeid;

	public String getTreeid() {
		if (treeid == null || treeid.equals("")) {
			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
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


	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setLong1(0);
			setExtGrid(null);
			visit.setString1(null);
		}
		getSelectData();
	}

}