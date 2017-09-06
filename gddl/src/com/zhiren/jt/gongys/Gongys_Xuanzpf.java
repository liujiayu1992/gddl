package com.zhiren.jt.gongys;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.webservice.InterCom_dt;
import com.zhiren.webservice.InterFac_dt;

public class Gongys_Xuanzpf extends BasePage {

	
private final int columns_Length=13;
	private String logMsg = "";
	
	private String zhuangT = "";
	
//	 系统日志表中的状态字段
	private static final String ZhangTConstant1 = "成功";

	private static final String ZhangTConstant2 = "失败";
	
// 系统日志表中的类别字段
	private static final String leiBConstant = "供应商批复";
	
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
	
	private String Parameters;//记录ID

	public String getParameters() {
		
		return Parameters;
	}

	public void setParameters(String value) {
		
		Parameters = value;
	}
	
	private String Gongysmc_value;
	
	public String getGongysmc_value() {
		return Gongysmc_value;
	}

	public void setGongysmc_value(String gongysmc_value) {
		Gongysmc_value = gongysmc_value;
	}

	private String Gongysid_value;

	public String getGongysid_value() {
		return Gongysid_value;
	}

	public void setGongysid_value(String gongysid_value) {
		Gongysid_value = gongysid_value;
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
		if (_FanHClick) {                    //返回按钮触发
			_FanHClick = false;
			cycle.activate("Gongyspf");
		}
		if (_MoHCHClick) {                   //模糊查询按钮触发
			_MoHCHClick = false;
			
		}
		if (_TianjClick) {                   //添加所选煤矿按钮触发
			_TianjClick = false;
		
			Visit visit = (Visit) getPage().getVisit();
			tianj();
//			visit.setString13(Gongysmc_value);
//			visit.setString14(Gongysid_value);
			cycle.activate("Gongyspf");
		}
		
	}
	
	public void tianj(){
		Visit visit = (Visit)this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if(GengGSJBM(con, "gongysb", Gongysmc_value, "更改", visit.getString15())){
			String sql = "update gongyssqpfb set pifzt = 1,bianm = '"+Gongysmc_value+"' where id = "+ visit.getString15();
			int flag = con.getUpdate(sql.toString());
			if(-1 == flag){
				setMsg("批复失败！");
			}else{
				setMsg("批复成功！");
			}
		}
		visit.setString14(msg);         //传递提示信息到另一个页面
		WriteLog(con);
		con.Close();
	}

	// 更改下级单位的上级公司编码字段返回标示
	private boolean GengGSJBM(JDBCcon con, String tableName, String bianm,
			String operation, String id) {

		InterCom_dt dt = new InterCom_dt();
		String sql_diancxxb_id = "select g.diancxxb_id,d.mingc from gongyssqpfb g,diancxxb d where g.diancxxb_id=d.id and g.id="
				+ id;
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
		// 为连城电厂特设置审批方法
		
			sqls = new String[] { "update gongysb set SHANGJGSBM='" + bianm
					+ "' where id=" + id };
		
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
	
	private void WriteLog(JDBCcon con) {

		Visit visit = (Visit) this.getPage().getVisit();

		if (!logMsg.equals("")) {// 不为空，需要写入日志记录

			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date_str = sdf.format(date);
			String sql = " insert into xitrzb(id,diancxxb_id,yonghm,leib,shij,zhuangt,beiz) values("
					+ " getnewid("
					+ visit.getDiancxxb_id()
					+ "),"
					+ visit.getDiancxxb_id()
					+ ",'"
					+ visit.getRenymc()
					+ "','"
					+ leiBConstant
					+ "',to_date('"
					+ date_str
					+ "','YYYY-MM-DD,HH24:mi:ss'),'"
					+ this.zhuangT
					+ "','"
					+ logMsg + "')";

			con.getInsert(sql);

		}
	}
	
	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql="";
		
		sql = 
			"select g.id,\n" +
			"       g.xuh,\n" + 
			"       g.bianm,\n" + 
			"       g.mingc,\n" + 
			"       g.quanc,\n" + 
			"       DECODE((SELECT mingc FROM gongyssqpfb WHERE ID = g.fuid),\n" + 
			"              NULL,\n" + 
			"              '请选择',\n" + 
			"              (SELECT mingc FROM gongyssqpfb WHERE ID = g.fuid)) AS fuid,\n" + 
			"       g.danwdz,\n" + 
			"       g.piny,\n" + 
			"       s.quanc as shengfb_id \n" + 
			"  from gongysb g, shengfb s \n" + 
			"  where g.shengfb_id = s.id\n" + 
			"    and g.leix = 1\n" + 
			"  order by xuh";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setTableName("gongyssqpfb");
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("xuh").setHeader("序号");
		egu.getColumn("xuh").setWidth(50);
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(50);
		egu.getColumn("mingc").setHeader("简称");
		egu.getColumn("mingc").setWidth(80);
		egu.getColumn("quanc").setHeader("全称");
		egu.getColumn("quanc").setWidth(200);
		egu.getColumn("fuid").setHeader("隶属于");
		egu.getColumn("fuid").setWidth(150);
		egu.getColumn("danwdz").setHeader("单位地址");
		egu.getColumn("danwdz").setWidth(150);
		egu.getColumn("piny").setHeader("拼音");
		egu.getColumn("piny").setWidth(50);
		egu.getColumn("shengfb_id").setHeader("省份");
		egu.getColumn("shengfb_id").setWidth(80);
		
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
//		egu.setWidth(1000);
		egu.setWidth("bodyWidth");
		egu.addTbarBtn(new GridButton("返回","function(){document.all.FanHBt.click();}"));
		egu.addTbarText("-");
		egu.addTbarText("供应商名称:");
		TextField tf=new TextField();
		tf.setId("Gongysmc");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarBtn(new GridButton("模糊查询","function(){ var mh_value=Gongysmc.getValue(); mohcx(mh_value,gridDiv_data,gridDiv_ds);} "));
		egu.addTbarText("-");
		egu.addTbarBtn(new GridButton("用选择供应商批复","function(){if(gridDiv_sm.getSelected()== null){Ext.MessageBox.alert('提示信息','请选中一个煤矿单位');}" +
				"else{ var rec=gridDiv_sm.getSelected(); var str=rec.get('BIANM');document.all.Gongysmc_value.value=str;document.all.Gongysid_value.value=rec.get('ID');" +
				" document.all.MsgAdd.value='toAdd';Ext.Msg.confirm('提示', '您确定要用该供应商批复吗?',function(button,text){" +
				"if(button=='yes'){document.all.TianjBt.click();}else{return;}});}}"));
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
/*
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
*/
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			//visit.setString9(null);
			Gongysmc_value="";
			getSelectData();
		}
		getSelectData();
	}

	
	
}
