package com.zhiren.heiljkhh.laihcrb;

import java.text.ParseException;
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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author yinjm
 * 来耗存日报维护
 */

public class Laihcrbwh extends BasePage implements PageValidateListener {
	
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
	
//	"复制同期"按钮
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshClick) {
			_RefreshClick = false;
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
		}
		if (_CopyClick) {
			_CopyClick = false;
			copyData();
		}
	}
	
	public void copyData() {
		
		JDBCcon con = new JDBCcon();
		StringBuffer sbsql = new StringBuffer("begin \n");
		
		String sql = 
			"select lhc.kouj, lhc.diq, lhc.leijlml\n" +
			"  from heiljlhcrbb lhc\n" + 
			" where lhc.riq = to_date('"+ getYesterday(getRiq()) +"', 'yyyy-mm-dd') and lhc.diancxxb_id = " + getTreeid();

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl.getRows() > 0) {
			sbsql.append("delete from heiljlhcrbb lhc where lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd') and lhc.diancxxb_id = "+ getTreeid() +";\n");
			if (getDay(getRiq()) > 1) {
				while(rsl.next()) {
					sbsql.append("insert into heiljlhcrbb(id, diancxxb_id, kouj, diq, riq, dangrlml, leijlml) values(getnewid(")
					.append(getTreeid()).append("), ").append(getTreeid()).append(", '").append(rsl.getString("kouj"))
					.append("', '").append(rsl.getString("diq")).append("', to_date('").append(getRiq()).append("','yyyy-mm-dd'), ")
					.append("default, ").append(rsl.getString("leijlml")).append(");\n");
				}
			} else {
				while(rsl.next()) {
					sbsql.append("insert into heiljlhcrbb(id, diancxxb_id, kouj, diq, riq, dangrlml, leijlml) values(getnewid(")
					.append(getTreeid()).append("), ").append(getTreeid()).append(", '").append(rsl.getString("kouj"))
					.append("', '").append(rsl.getString("diq")).append("', to_date('").append(getRiq()).append("','yyyy-mm-dd'), ")
					.append("default, default").append(");\n");
				}
			}
			sbsql.append("end;");
			con.getUpdate(sbsql.toString());
		} else {
			setMsg("同期没有数据，无法复制！");
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
			sbsql.append("delete from heiljlhcrbb where id = ").append(delrsl.getString("id")).append(";\n");
		}
		delrsl.close();
		
		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while(mdrsl.next()) {
			if("0".equals(mdrsl.getString("id"))) {
				sbsql.append("insert into heiljlhcrbb(id, diancxxb_id, kouj, diq, riq,  dangrlml, leijlml) values(getnewid(")
				.append(getTreeid()).append("), ").append(getTreeid()).append(", '").append(mdrsl.getString("kouj"))
				.append("', '").append(mdrsl.getString("diq")).append("', to_date('").append(getRiq()).append("', 'yyyy-mm-dd'), ")
				.append(getSqlValue(mdrsl.getString("dangrlml"))).append(", ").append(getSqlValue(mdrsl.getString("leijlml")))
				.append(");\n");
			} else {
				sbsql.append("update heiljlhcrbb set ")
				.append("kouj = '").append(mdrsl.getString("kouj")).append("', diq = '").append(mdrsl.getString("diq"))
				.append("', dangrlml = ").append(getSqlValue(mdrsl.getString("dangrlml")))
				.append(", leijlml = ").append(getSqlValue(mdrsl.getString("leijlml")))
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
		String zhi = MainGlobal.getXitxx_item("数量", "计划内是否有多口径", String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id()), "否");
		String kouj = "and i.mingc = decode(lhc.diq, '地方煤', '市场采购', '七台河', '龙煤计划内', '双鸭山', '龙煤计划内', '鸡西', '龙煤计划内', lhc.diq)\n";
		if (zhi.equals("是")) {
			kouj = 			
			"                      and jh.zhibmc_item_id = i.id\n" + 
			"                      and i.mingc = decode(lhc.diq, '地方煤', '市场采购', (\n" + 
			"                          select case\n" + 
			"                                   when (select it.mingc\n" + 
			"                                           from ranlxyjhb jh, item it\n" + 
			"                                          where jh.ranlxyjhzbb_id =\n" + 
			"                                                (select distinct zb.id\n" + 
			"                                                   from ranlxyjhzbb zb\n" + 
			"                                                  where zb.diancxxb_id = lhc.diancxxb_id\n" + 
			"                                                    and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
			"                                            and jh.zhibmc_item_id = it.id\n" + 
			"                                            and it.mingc = lhc.diq) is null then\n" + 
			"                                    '龙煤计划内'\n" + 
			"                                   else\n" + 
			"                                    (select it.mingc\n" + 
			"                                       from ranlxyjhb jh, item it\n" + 
			"                                      where jh.ranlxyjhzbb_id =\n" + 
			"                                            (select distinct zb.id\n" + 
			"                                               from ranlxyjhzbb zb\n" + 
			"                                              where zb.diancxxb_id = lhc.diancxxb_id\n" + 
			"                                                and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
			"                                        and jh.zhibmc_item_id = it.id\n" + 
			"                                        and it.mingc = lhc.diq)\n" + 
			"                                 end as a\n" + 
			"                         from dual))\n";
		}
		
		String sql = 
			"select rb.id, rb.diancxxb_id, rb.kouj, rb.diq, rb.yuejhl, rb.dangrlml, rb.leijlml,\n" +
			"       round(rb.leijlml / (decode(rb.yuejhl, 0, 1, rb.yuejhl) / "+ getLastDayOfMonth(getRiq()) +" * "+ getDay(getRiq()) +"), 4) * 100 as leijdhl from\n" + 
			"(select lhc.id,\n" + 
			"       lhc.diancxxb_id,\n" + 
			"       lhc.kouj,\n" + 
			"       lhc.diq,\n" + 
			"       nvl((select jh.y"+ getMonth(getRiq()) +" * 10000\n" + 
			"             from ranlxyjhb jh, item i\n" + 
			"            where jh.ranlxyjhzbb_id =\n" + 
			"                  (select distinct zb.id\n" + 
			"                     from heiljlhcrbb hc, ranlxyjhzbb zb\n" + 
			"                    where hc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"                      and zb.diancxxb_id = lhc.diancxxb_id\n" + 
			"                      and to_char(zb.nianf, 'yyyy') = '"+ getYear(getRiq()) +"')\n" + 
			"                      and jh.zhibmc_item_id = i.id\n" + kouj +
			"                     and lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')),\n" + 
			"           0) yuejhl,\n" + 
			"       lhc.dangrlml,\n" + 
			"       lhc.leijlml\n" + 
			"  from heiljlhcrbb lhc, diancxxb dc\n" + 
			" where lhc.diancxxb_id = dc.id\n" + 
			"   and lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')\n" + 
			"   and (lhc.diancxxb_id = "+ getTreeid() +" or dc.fuid = "+ getTreeid() +")\n" + 
			" order by lhc.diancxxb_id, lhc.kouj) rb";

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		
		egu.getColumn("diancxxb_id").setHeader("电厂ID");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("kouj").setHeader("口径");
		egu.getColumn("diq").setHeader("地区");
		egu.getColumn("yuejhl").setHeader("月计划量(吨)");
		egu.getColumn("yuejhl").setEditor(null);
		egu.getColumn("yuejhl").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		egu.getColumn("dangrlml").setHeader("当日来煤量(吨)");
		egu.getColumn("leijlml").setHeader("累计来煤量(吨)");
		egu.getColumn("leijdhl").setHeader("累计到货率(%)");
		egu.getColumn("leijdhl").setEditor(null);
		egu.getColumn("leijdhl").setRenderer("function(value,metadata){metadata.css='tdTextext1'; return value;}");
		
		egu.getColumn("kouj").setEditor(new ComboBox());
		egu.getColumn("kouj").setComboEditor(egu.gridId, 
			new IDropDownModel("select i.id, i.mingc from item i, itemsort its where " +
			"i.itemsortid = its.id and its.bianm = 'KOUJING'"));
		
		egu.getColumn("diq").setEditor(new ComboBox());
		egu.getColumn("diq").setComboEditor(egu.gridId, 
			new IDropDownModel("select i.id, i.mingc from item i, itemsort its where " +
			"i.itemsortid = its.id and its.bianm = 'DIQU'"));
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
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
		
		GridButton copyButton = new GridButton("复制同期", getButtonHandler(con, "CopyButton"), SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(copyButton);
		
		String script = 
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"    if (e.field=='DANGRLML') {\n" + 
			"        var dangrlml;\n" + 
			"        if(e.record.get('DANGRLML')=='' || e.record.get('DANGRLML')==null) {\n" + 
			"            dangrlml = 0;\n" + 
			"        }else{\n" + 
			"            dangrlml = Math.round(eval(e.record.get('DANGRLML'))*100)/100;\n" + 
			"        };\n" + 
			"        var leijlml;\n" + 
			"        if(e.record.get('LEIJLML')=='' || e.record.get('LEIJLML')==null) {\n" + 
			"            leijlml = 0;\n" + 
			"        }else{\n" + 
			"            leijlml = Math.round(eval(e.record.get('LEIJLML'))*100)/100;\n" + 
			"        };\n" + 
			"\n" + 
			"        if (e.record.get('ID')=='0') {\n" + 
			"           e.record.set('LEIJLML', dangrlml);\n" + 
			"        } else {\n" + 
			"           var new_leijlml = leijlml - e.originalValue + dangrlml;\n" + 
			"           e.record.set('LEIJLML', new_leijlml);\n" + 
			"        }\n" + 
			"    }\n" +  
			"});";
		egu.addOtherScript(script);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		setExtGrid(egu);
		rsl.close();
		con.Close();
	}
	
	/**
	 * 获取传日期的年份
	 * @param date
	 * @return
	 */
	public int getYear(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int year = DateUtil.getYear(new Date());
		try {
			Date tempdate = sdf.parse(date);
			year = DateUtil.getYear(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return year;
	}
	
	/**
	 * 获取传日期的月份
	 * @param date
	 * @return
	 */
	public int getMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int month = DateUtil.getMonth(new Date());
		try {
			Date tempdate = sdf.parse(date);
			month = DateUtil.getMonth(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return month;
	}
	
	/**
	 * 获取传入的日期是当月的第几天
	 * @param date
	 * @return
	 */
	public int getDay(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int day = DateUtil.getDay(new Date());
		try {
			Date tempdate = sdf.parse(date);
			day = DateUtil.getDay(tempdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return day;
	}
	
	/**
	 * 返回当月的最后一天
	 * @param strDate
	 * @return
	 */
	public String getLastDayOfMonth(String strDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd");
		Calendar ca = Calendar.getInstance();
		try {
			String date[] = strDate.split("-");
			ca.set(Integer.parseInt(date[0]), Integer.parseInt(date[1]), 0);
		}catch(NumberFormatException e) {
			e.printStackTrace();
			return null;
		}
		return sdf.format(ca.getTime());
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
	
	/**
	 * 如果在页面上取到的值为Null或是空串，那么向数据库保存字段的默认值
	 * @param value
	 * @return
	 */
	public String getSqlValue(String value) {
		return value == null || "".equals(value) ? "default" : value;
	}
	
	/**
	 * 返回"复制同期"按钮的handler。当点击"复制同期"按钮时判断当前日期是否有数据，
	 * 如果有数据那么给出提示是否要覆盖原有数据，如果没有那么不给提示信息，
	 * 直接进行"复制同期"操作。
	 * @return
	 */
	public String getButtonHandler(JDBCcon con, String buttonName) {
		
		String handler = 
			"function (){\n" +
			"    document.getElementById('"+ buttonName +"').click();\n" + 
			"}";
		
		String sql = 
			"select lhc.id\n" +
			"  from heiljlhcrbb lhc\n" + 
			" where lhc.riq = to_date('"+ getRiq() +"', 'yyyy-mm-dd')";
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
	
//	电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
//	电厂树_结束
	
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
			this.setTreeid(null);
			setRiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}