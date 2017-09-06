package com.zhiren.dc.caiygl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 类名：采样化验次数维护
 */

public class Caiyhycswh extends BasePage implements PageValidateListener {

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	protected void initialize() {
		msg = "";
	}
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String riq; // 保存日期
	
	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	private boolean _RefreshClick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshClick = true;
	}
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}
	
//	"生成"按钮
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_CreateClick) {
			_CreateClick = false;
			createData();
		}
	}
	
	public void createData() {
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		String sql = 
			"select distinct fh.gongysb_id, fh.meikxxb_id, fh.laimsl\n" +
			"  from chepb cp, fahb fh, meikxxb mk, gongysb gys\n" + 
			" where to_char(cp.zhongcsj, 'yyyy-mm-dd') =\n" + 
			"       to_char(to_date('"+ getRiq() +"', 'yyyy-mm-dd'), 'yyyy-mm-dd')\n" + 
			"   and cp.fahb_id = fh.id\n" + 
			"   and fh.meikxxb_id = mk.id\n" + 
			"   and fh.gongysb_id = gys.id";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			while(rsl.next()) {
				sbsql.append("insert into caiyhycstjb(id, gongysb_id, meikxxb_id, riq, laimsl, caiycs, caiyzrr, huaycs, huayzrr) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append(rsl.getString("gongysb_id")).append(", ").append(rsl.getString("meikxxb_id")).append(", ")
				.append("to_date('").append(getRiq()).append("', 'yyyy-mm-dd'), ").append(rsl.getString("laimsl")).append(", ")
				.append("default, '焦毅', 1, '焦瑾辉');\n");
			}
			sbsql.append("end;\n");
			con.getInsert(sbsql.toString());
		} else {
			setMsg("没有来煤信息，无法生成！");
		}
		rsl.close();
		con.Close();
	}
	
	public void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin\n");
		
		ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		while (delrsl.next()) {
			sbsql.append("delete from caiyhycstjb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into caiyhycstjb(id, gongysb_id, meikxxb_id, riq, laimsl, caiycs, caiyzrr, huaycs, huayzrr) values(getnewid(")
				.append(visit.getDiancxxb_id()).append("), ")
				.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id"))).append(", ")
				.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))).append(", ")
				.append("to_date('").append(getRiq()).append("', 'yyyy-mm-dd'), ").append(getSqlValue(mdrsl.getString("laimsl"))).append(", ")
				.append(getSqlValue(mdrsl.getString("caiycs"))).append(", '").append(mdrsl.getString("caiyzrr")).append("', ")
				.append(mdrsl.getString("huaycs")).append(", '").append(mdrsl.getString("huayzrr")).append("');\n");
			} else {
				sbsql.append("update caiyhycstjb set ")
				.append("gongysb_id = ").append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(mdrsl.getString("gongysb_id"))).append(", ")
				.append("meikxxb_id = ").append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(mdrsl.getString("meikxxb_id"))).append(", ")
				.append("laimsl = ").append(getSqlValue(mdrsl.getString("laimsl"))).append(", ")
				.append("caiycs = ").append(getSqlValue(mdrsl.getString("caiycs")))
				.append(" where id = ").append(mdrsl.getString("id")).append(";\n");
			}
		}
		sbsql.append("end;");
		con.getUpdate(sbsql.toString());
		mdrsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		
		JDBCcon con = new JDBCcon();
		String sql = 
			"select chcs.id,\n" +
			"       gys.mingc gongysb_id,\n" + 
			"       mk.mingc meikxxb_id,\n" + 
			"       chcs.laimsl,\n" + 
			"       chcs.caiycs,\n" + 
			"       chcs.caiyzrr,\n" + 
			"       chcs.huaycs,\n" + 
			"       chcs.huayzrr\n" + 
			"  from caiyhycstjb chcs, gongysb gys, meikxxb mk\n" + 
			" where chcs.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and chcs.gongysb_id = gys.id\n" + 
			"   and chcs.meikxxb_id = mk.id" +
			" order by gys.mingc, mk.mingc";
		
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("laimsl").setHeader("来煤数量");
		egu.getColumn("caiycs").setHeader("采样次数");
		egu.getColumn("caiyzrr").setHeader("采样责任人");
		egu.getColumn("caiyzrr").setDefaultValue("焦毅");
		egu.getColumn("caiyzrr").setEditor(null);
		egu.getColumn("huaycs").setHeader("化验次数");
		egu.getColumn("huaycs").setDefaultValue("1");
		egu.getColumn("huaycs").setEditor(null);
		egu.getColumn("huayzrr").setHeader("化验责任人");
		egu.getColumn("huayzrr").setDefaultValue("焦瑾辉");
		egu.getColumn("huayzrr").setEditor(null);
		
		ComboBox gys_cob = new ComboBox();
		gys_cob.setListWidth(150);
		egu.getColumn("gongysb_id").setEditor(gys_cob);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from gongysb order by mingc"));
		
		ComboBox mk_cob = new ComboBox();
		mk_cob.setListWidth(150);
		egu.getColumn("meikxxb_id").setEditor(mk_cob);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
			new IDropDownModel("select id, mingc from meikxxb order by mingc"));
		
		egu.addTbarText("日期：");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.setId("riq");
		df.Binding("Riq", "");
		egu.addToolbarItem(df.getScript());
		egu.addOtherScript("riq.on('change',function(){document.forms[0].submit();});");
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新", "function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addTbarText("-");
		
		GridButton copyButton = new GridButton("生成", getButtonHandler(con, "CreateButton"), SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(copyButton);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 返回"生成"按钮的handler。当点击"生成"按钮时判断当前日期是否有数据，
	 * 如果有数据那么给出提示是否要覆盖原有数据，如果没有那么不给提示信息，
	 * 直接进行"生成"操作。
	 * @return
	 */
	public String getButtonHandler(JDBCcon con, String buttonName) {
		
		String handler = 
			"function (){\n" +
			"    document.getElementById('"+ buttonName +"').click();\n" + 
			"}";
		
		String sql = "select id from caiyhycstjb where riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')";
		ResultSetList rsl = con.getResultSetList(sql);
		
		if (rsl.next()) {
			handler = 
				"function (){\n" +
				"    Ext.MessageBox.confirm('提示信息','新数据将会覆盖原有数据，是否继续？',\n" + 
				"        function(btn){\n" + 
				"            if(btn == 'yes'){\n" + 
				"                document.getElementById('"+ buttonName +"').click();\n" + 
				"                Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...'," +
				"                width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"            };\n" + 
				"        }\n" + 
				"    );\n" + 
				"}";
		}
		rsl.close();
		return handler;
	}
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	/**
	 * 获取传入日期的前一天
	 * @param date
	 * @return
	 */
	public static String getYesterday(String date) {
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		if(date == null){
			date = DateUtil.FormatDate(new Date());
		}
		String[] strdate = date.split("-");
		Calendar dateTime = Calendar.getInstance();
		dateTime.set(Integer.parseInt(strdate[0]), Integer.parseInt(strdate[1])-1, Integer.parseInt(strdate[2])-1);
		Date d = dateTime.getTime();
		return dft.format(d);
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	
	public String getGridHtml() {
		return getExtGrid().getHtml();
	}
	
	public String getGridScript() {
		return getExtGrid().getGridScript();
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			visit.setActivePageName(getPageName().toString());
			setRiq(getYesterday(DateUtil.FormatDate(new Date())));
		}
		getSelectData();
	}

}