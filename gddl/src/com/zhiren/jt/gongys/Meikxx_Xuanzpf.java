package com.zhiren.jt.gongys;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

public class Meikxx_Xuanzpf extends BasePage implements PageValidateListener {

	//	private final int columns_Length = 13;
	

	// 系统日志表中的状态字段
	private static final String ZhangTConstant1 = "成功";

	private static final String ZhangTConstant2 = "失败";

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String DataSource;

	public String getDataSource() {
		return DataSource;
	}

	public void setDataSource(String dataSource) {
		DataSource = dataSource;
	}

	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	// 被选择的记录的编码
	private String Meikdwmc_value;

	public String getMeikdwmc_value() {
		return Meikdwmc_value;
	}

	public void setMeikdwmc_value(String meikdwmc_value) {
		Meikdwmc_value = meikdwmc_value;
	}

	// 传递给煤矿信息批复(Meikxxpf)页面的煤矿地区名称
	private String Meikdqmc_value;

	public String getMeikdqmc_value() {
		return Meikdqmc_value;
	}

	public void setMeikdqmc_value(String meikdqmc_value) {
		Meikdqmc_value = meikdqmc_value;
	}

	private String Meikdwid_value;

	public String getMeikdwid_value() {
		return Meikdwid_value;
	}

	public void setMeikdwid_value(String meikdwid_value) {
		Meikdwid_value = meikdwid_value;
	}

	private boolean _FanHClick = false;

	public void FanHBt(IRequestCycle cycle) {
		_FanHClick = true;
	}

	private boolean _MoHCHClick = false;

	public void MoHCHBt(IRequestCycle cycle) {
		_MoHCHClick = true;
	}

	private boolean _TianjClick = false;

	public void TianjBt(IRequestCycle cycle) {
		_TianjClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_FanHClick) { // 返回按钮触发
			_FanHClick = false;
			cycle.activate("Meikxxpf");
		}
		if (_MoHCHClick) { // 模糊查询按钮触发
			_MoHCHClick = false;

		}
		if (_TianjClick) { // 添加所选煤矿按钮触发
			_TianjClick = false;
            tianj();
			Visit visit = (Visit) getPage().getVisit();
			visit.setString13(Meikdwmc_value); // 煤矿编码
			visit.setString14(Meikdwid_value); // 煤矿id
//			visit.setString15(Meikdqmc_value); // 煤矿地区名称
			cycle.activate("Meikxxpf");
		}
	}
	
    private String Meikxxmc_value;
	
	public String getGongysmc_value() {
		return Meikxxmc_value;
	}

	public void setMeikxxmc_value(String meikxxmc_value) {
		Meikxxmc_value = meikxxmc_value;
	}
	
	private long Meikdqbm_value;
	
	public long getMeikdqbm_value(){
		return Meikdqbm_value;
	}
	
	public void setMeikdqbm_value(long meikdqbm_value){
		Meikdqbm_value = meikdqbm_value;
	}
	
	public void tianj(){
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long meikdqbm = 0;
//		String sql1 = "select id,meikdqbm from meikxxbsqpfb"+visit.getString16()+"";
//		ResultSetList list=con.getResultSetList(sql1);
//		while(list.next()){
//			meikdqbm = list.getLong("meikdqbm");
//		}
//		meikdqbm = this.getMeikdqbm_value();
		if(GengGSJBM(con, "meikxxb", Meikdwmc_value, "更改", visit.getString16())){
			String sql = "update meikxxbsqpfb set pifzt = 1,bianm = '"+Meikdwmc_value+"' where id = "+ visit.getString16();
			int flag = con.getUpdate(sql.toString());
			if(-1 == flag){
				setMsg("批复失败！");
			}else{
				setMsg("批复成功！");
			}
		}else{
			setMsg("无法更新下级单位！");
		}
		visit.setString17(msg);         //传递提示信息到另一个页面
		
		con.Close();
	}
	
	private String logMsg = "";

	private String zhuangT = "";
	
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String id) {
        Visit visit = (Visit)getPage().getVisit();
		InterCom_dt dt = new InterCom_dt();
		String sql_diancxxb_id = "select g.diancxxb_id,d.mingc from meikxxbsqpfb g,diancxxb d where g.diancxxb_id=d.id and g.id="
				+ visit.getString16();
		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
		String diancxxb_id = "";
		String diancmc = "";
		if (rsl.next()) {
			diancxxb_id = rsl.getString("DIANCXXB_ID");
			diancmc = rsl.getString("mingc");
		}

		// 有下级电厂单位，需要更新

		boolean t;

		String[] sqls;
		// 为连城电厂特设置审批方法--Meikdqmc_value
		
			sqls = new String[] { "update meikxxb set SHANGJGSBM='" + bianm
					+ "' where id=" + visit.getString16()};
		
		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);

		if (answer[0].equals("true")) { // 更新下级字段成功
			zhuangT = ZhangTConstant1;
			t = true;
		} else {
			zhuangT = ZhangTConstant2;
			t = false;
		}

		logMsg = operation + "表" + tableName + "中编码"
				+ bianm.replaceAll("'", "") + "更新下级" + diancmc + zhuangT;

		return t;
	}
	
//	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
//			String operation, String id,long meikdqbm) {
//
//		InterCom_dt dt = new InterCom_dt();
//		Visit visit = (Visit)getPage().getVisit();
//
//		String sql_diancxxb_id = "select b.diancxxb_id,d.mingc from meikxxbsqpfb b,diancxxb d where b.diancxxb_id=d.id and b.id="
//				+ visit.getString16();
//		ResultSetList rsl = con.getResultSetList(sql_diancxxb_id);
//		String diancxxb_id = "";
//		String diancmc = "";
//		if (rsl.next()) {
//			diancxxb_id = rsl.getString("DIANCXXB_ID");
//			diancmc = rsl.getString("mingc");
//		}
//
//		// 有下级电厂单位，需要更新
//		boolean t;
//
//		String[] sqls;
//		// 为连城电厂特设置审批方法
////		if (diancxxb_id.equals("266")) {
////			sqls = new String[] { "update meikxxb set SHANGJGSBM="
////					+ bianm
////					+ "where id=replace("
////					+ id
////					+ ",266,'') "
////					+ " and MEIKDQB_ID in (select id from meikdqb where shangjgsbm='"
////					+ meikdqbm + "') " };
////		} else {
//			sqls = new String[] { "update meikxxb set SHANGJGSBM=" + bianm
//					+ ", MEIKDQ_ID = (select id from gongysb where bianm='"
//					+ meikdqbm + "' and leix=0 and rownum=1) where id=" + visit.getString16() };
////		}
//		String[] answer = dt.sqlExe(diancxxb_id, sqls, true);
//
//		if (answer[0].equals("true")) { // 更新下级字段成功
//			zhuangT = ZhangTConstant1;
//			t = true;
//		} else {
//			zhuangT = ZhangTConstant2;
//			t = false;
//		}
//
//		logMsg += operation + "表" + tableName + "中编码"
//				+ bianm.replaceAll("'", "") + "更新下级" + diancmc + zhuangT;
//
//		return t;
//	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		//		sql = "  select m.id,m.xuh,m.bianm,m.mingc,m.quanc,s.quanc as shengfb_id,m.leib,m.leix,j.mingc as jihkjb_id,m.danwdz " +
		//				"from meikxxb m,shengfb s,jihkjb j where m.shengfb_id=s.id(+) and m.jihkjb_id=j.id(+) ";

		String sql = "select m.id,\n" + "       m.xuh,\n"
				+ "       g.mingc gongysb_id,\n" + "       m.bianm,\n"
				+ "       m.mingc,\n" + "       m.quanc,\n"
				+ "       s.quanc as shengfb_id,\n" + "       m.leib,\n"
				+ "       m.leix,\n" + "       j.mingc as jihkjb_id,\n"
				+ "       m.danwdz\n"
				+ "  from meikxxb m, shengfb s, jihkjb j, gongysb g\n"
				+ " where m.shengfb_id = s.id(+)\n"
				+ "   and m.jihkjb_id = j.id(+)\n"
				+ "   and m.meikdq_id = g.id";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.setTableName("meikxxb");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("gongysb_id").setHeader("煤矿地区");
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(150);
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("leib").setWidth(80);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setWidth(80);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("danwdz").setWidth(80);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		//		egu.setWidth(1000);
		egu.addTbarBtn(new GridButton("返回",
				"function(){document.all.FanHBt.click();}"));
		egu.addTbarText("-");
		egu.addTbarText("煤矿单位名称:");
		TextField tf = new TextField();
		tf.setId("Meikdwmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("模糊查询", "function(){ "
				+ "	var mh_value=Meikdwmc.getValue(); "
				+ "	mohcx(mh_value,gridDiv_data,gridDiv_ds);" + "} "));
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("用所选煤矿进行批复", "function(){"
				+ "	if(gridDiv_sm.getSelected()== null){"
				+ "		Ext.MessageBox.alert('提示信息','请选中一个煤矿单位');" + "	}else{ "
				+ "		var rec=gridDiv_sm.getSelected(); "
				+ "		var str=rec.get('BIANM');"
				+ "		document.all.Meikdwmc_value.value=str;"
				+ "		document.all.Meikdwid_value.value=rec.get('ID'); "
				+ "		document.all.Meikdqmc_value.value=rec.get('GONGYSB_ID'); "
				+ "		document.all.MsgAdd.value='toAdd';" +
						"Ext.Msg.confirm('提示', '您确定要用该供应商批复吗?',function(button,text){" +
				"if(button=='yes'){" +
				"document.all.TianjBt.click();" +
				"}else{" +
				"return;" +
				"}});}}"));
		setExtGrid(egu);

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
			visit.setString17(null);
			// visit.setString9(null);
			Meikdwmc_value = "";
			getSelectData();
		}
		getSelectData();
	}

}
