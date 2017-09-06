package com.zhiren.dc.jilgl.gongl.guohsjxg;

import java.util.Calendar;
import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Guohsjxg extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	绑定日期
	private String riq;

	public String getRiq() {
		if (riq==null || "".equals(riq)) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
	
//	车号	
	private String cheh;
	public String getCheh() {
		return cheh;
	}
	public void setCheh(String cheh) {		
		this.cheh = cheh;
	}
	
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ChaxChick = false;
	public void ChaxButton(IRequestCycle cycle) {
		_ChaxChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_ChaxChick) {
			_ChaxChick = false;
			chax();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			if (!"".equals(this.getCheh())) {
				chax();
			}else {
				getSelectData();
			}
		}
	}
	
	private String getMeikdq(String meikmc) {
		JDBCcon con = new JDBCcon();
		String strMeikdq = "";
		con.setAutoCommit(false);
		String SQL = 
			"SELECT \"meikdq\" FROM \"QCH_MEIKXXB\" WHERE \"mingc\"= '" + meikmc + "'";
		ResultSetList rsl = con.getResultSetList(SQL);
		con.commit();
		if (rsl.next()) {
			strMeikdq = rsl.getString("meikdq");
		}
		
		rsl.close();
		return strMeikdq;
	}
	
	private String getPinzlb(String pinz) {
		JDBCcon con = new JDBCcon();
		String strPinzlb = "";
		con.setAutoCommit(false);
		String SQL = 
			  "SELECT \"leib\" FROM \"QCH_PINZB\"  WHERE \"mingc\"= '" + pinz + "'";
		ResultSetList rsl = con.getResultSetList(SQL);
		con.commit();
		if (rsl.next()) {
			strPinzlb = rsl.getString("leib");
		}
		rsl.close();
		return strPinzlb;
	}
	
	public void Save() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit)getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		double dblJingz = 0;
		String strMeikdq = "";
		String strPinzlb = "";
		Date   date   =   Calendar.getInstance().getTime(); 
		String strCurDate = DateUtil.Formatdate("yyyy-MM-dd HH:mm:ss", date);
		int flag = 0;
		con.setAutoCommit(false);
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		if (rsl==null) {
			return;
		}
		sb.append("begin \n");
		while (rsl.next()) {
			
			dblJingz=rsl.getDouble("maoz")-rsl.getDouble("piz")-rsl.getDouble("kous");
			dblJingz=CustomMaths.Round_New(dblJingz,2);
			strMeikdq = getMeikdq(rsl.getString("meikmc"));
			strPinzlb = getPinzlb(rsl.getString("pinz"));
			sb.append(
				"UPDATE \"QCH_GUOHB\"\n" +
				"SET \"cheh\"='" + rsl.getString("cheh") + "',\n" + 
				"\"maoz\"=" + rsl.getString("maoz") + ",\n" + 
				"\"piz\"=" + rsl.getString("piz") + ",\n" + 
				"\"kous\"=" + rsl.getString("kous") + ",\n" + 
				"\"jingz\"=" + dblJingz + ",\n" + 
				"\"biaoz\"=" + rsl.getString("biaoz") + ",\n" + 
				"\"meikmc\"='" + rsl.getString("meikmc") + "',\n" + 
				"\"yunsdw\"='" + rsl.getString("yunsdw") + "',\n" + 
				"\"pinz\"='" + rsl.getString("pinz") + "',\n" + 
				"\"meikdq\"='" + strMeikdq + "',\n" + 
				"\"pinzlb\"='" + strPinzlb + "',\n" + 
				"\"piaojh\"='" + rsl.getString("piaojh") +"',\n" + 
				"\"xiugry\"='" + visit.getRenymc() + "',\n" + 
				"\"xiugrq\"=to_date('" + strCurDate + "','yyyy-mm-dd hh24:mi:ss')\n" + 
				"WHERE \"id\"=" + rsl.getString("id") + "; \n"
				);
		}
		sb.append("end;\n");
		
		if (sb.length()>13) {
			flag = con.getUpdate(sb.toString());
			if (flag==-1) {
				con.rollBack();
				setMsg("保存失败");
				return;
			}
		}
		con.commit();
		rsl.close();
		setMsg("保存成功");
	}
	
	public void chax() {

		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		con.setAutoCommit(false);
		sb.append(
				"SELECT \"id\",\"cheh\",\"maoz\",\"piz\",\"jingz\",\"biaoz\",\"kous\",\"meikmc\",\n" +
				"\"yunsdw\",\"pinz\",\"piaojh\",to_char(\"maozsj\",'yyyy-mm-dd hh24:mi:ss') as maozsj,\"maozry\",to_char(\"pizsj\",'yyyy-mm-dd hh24:mi:ss') as pizsj,\"pizry\"\n" + 
				"FROM  \"QCH_GUOHB\" WHERE to_char(\"maozsj\",'yyyy-mm-dd')='" + this.getRiq() + "'\n");
		if (!"".equals(getCheh())) {
			sb.append("and \"cheh\" like '%" + getCheh() + "%'\n");
		}
		sb.append("ORDER BY \"maozsj\"");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		con.commit();
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setDefaultsortable(false);
		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(80);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(80);
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("kous").setHeader("扣吨");
		egu.getColumn("kous").setWidth(80);
		egu.getColumn("meikmc").setHeader("煤矿单位");
		egu.getColumn("meikmc").setEditor(null);
		egu.getColumn("yunsdw").setHeader("运输单位");
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("piaojh").setHeader("票号");
		egu.getColumn("piaojh").setWidth(80);
		egu.getColumn("maozsj").setHeader("过重时间");
		egu.getColumn("maozsj").setWidth(120);
		egu.getColumn("maozsj").setEditor(null);
		egu.getColumn("maozry").setHeader("重检员");
		egu.getColumn("maozry").setWidth(80);
		egu.getColumn("maozry").setEditor(null);
		egu.getColumn("pizsj").setHeader("回皮时间");
		egu.getColumn("pizsj").setWidth(120);
		egu.getColumn("pizsj").setEditor(null);
		egu.getColumn("pizry").setHeader("轻检员");	
		egu.getColumn("pizry").setWidth(80);
		egu.getColumn("pizry").setEditor(null);
		
//		设置煤矿下拉框
		ComboBox cMeik= new ComboBox();
		egu.getColumn("meikmc").setEditor(cMeik);
		cMeik.setEditable(true);
		String mkSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_MEIKXXB\" WHERE \"zhuangt\"=1 ORDER BY \"meikdq\",\"mingc\"";
		con.setAutoCommit(false);
		egu.getColumn("meikmc").setComboEditor(egu.gridId, new IDropDownModel(con,mkSql));
		con.commit();
		
//		设置运输单位下拉框
		ComboBox cYunsdw= new ComboBox();
		egu.getColumn("yunsdw").setEditor(cYunsdw);
		cYunsdw.setEditable(true);
		String ydSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_YUNSDWB\" WHERE \"zhuangt\"=1 ORDER BY \"mingc\"";
		con.setAutoCommit(false);
		egu.getColumn("yunsdw").setComboEditor(egu.gridId, new IDropDownModel(con,ydSql));
		con.commit();
		
//		设置品种下拉框
		ComboBox cPinz= new ComboBox();
		egu.getColumn("pinz").setEditor(cPinz);
		cPinz.setEditable(true);
		String pzSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_PINZB\" WHERE \"zhuangt\"=1 ORDER BY \"xuh\"";
		con.setAutoCommit(false);
		egu.getColumn("pinz").setComboEditor(egu.gridId, new IDropDownModel(con,pzSql));
		con.commit();
		
		egu.addTbarText("过衡日期:");
		DateField dfb = new DateField();
		dfb.setValue(getRiq());
		dfb.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("RIQ");
		egu.addToolbarItem(dfb.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("输入车号：");
		TextField tf = new TextField();
		tf.setId("cheh");
		tf.setWidth(100);
		tf.setValue(getCheh());
		egu.addToolbarItem(tf.getScript());
		
		ToolbarButton chazhao = new ToolbarButton(null, "(模糊)查找",
				"function() {document.getElementById('CHEH').value=cheh.getValue();" 
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200) 
				+ " document.getElementById('ChaxButton').click();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addToolbarItem(chazhao.getScript());
		egu.addTbarText("-");
		
		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function() {" 
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200) 
				+ " document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addToolbarItem(rbtn.getScript());
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ " +
				
				" var rec=e.record;\n" +
				" var MAOZ=eval(rec.get('maoz')||0);\n" +
				" var PIZ=eval(rec.get('piz')||0);\n"+
				" var KOUS=eval(rec.get('kous')||0);\n" +		
				
				"if (MAOZ<=PIZ+KOUS) {\n" +
				"	Ext.MessageBox.alert('提示信息','毛重的值必须比皮重大');\n" +
				"  return;\n" +
				"}\n" +
				
				"if( e.field=='maoz' || e.field=='piz' || e.field=='kous'){\n" +
				" rec.set('jingz',Round_new(MAOZ-PIZ-KOUS,2));"+
				"} \n" +							
			"\n});" );
		
		setExtGrid(egu);
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		con.setAutoCommit(false);
		sb.append(
				"SELECT \"id\",\"cheh\",\"maoz\",\"piz\",\"jingz\",\"biaoz\",\"kous\",\"meikmc\",\n" +
				"\"yunsdw\",\"pinz\",\"piaojh\",to_char(\"maozsj\",'yyyy-mm-dd hh24:mi:ss') as maozsj,\"maozry\",to_char(\"pizsj\",'yyyy-mm-dd hh24:mi:ss') as pizsj,\"pizry\"\n" + 
				"FROM  \"QCH_GUOHB\" WHERE to_char(\"maozsj\",'yyyy-mm-dd')='" + this.getRiq() + "'\n" + 
				"ORDER BY \"maozsj\""
				);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		con.commit();
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setDefaultsortable(false);
		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setWidth(80);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setWidth(80);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setWidth(80);
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setWidth(80);
		egu.getColumn("kous").setHeader("扣吨");
		egu.getColumn("kous").setWidth(80);
		egu.getColumn("meikmc").setHeader("煤矿单位");
		egu.getColumn("meikmc").setEditor(null);
		egu.getColumn("yunsdw").setHeader("运输单位");
		egu.getColumn("yunsdw").setEditor(null);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("piaojh").setHeader("票号");
		egu.getColumn("piaojh").setWidth(80);
		egu.getColumn("maozsj").setHeader("过重时间");
		egu.getColumn("maozsj").setWidth(120);
		egu.getColumn("maozsj").setEditor(null);
		egu.getColumn("maozry").setHeader("重检员");
		egu.getColumn("maozry").setWidth(80);
		egu.getColumn("maozry").setEditor(null);
		egu.getColumn("pizsj").setHeader("回皮时间");
		egu.getColumn("pizsj").setWidth(120);
		egu.getColumn("pizsj").setEditor(null);
		egu.getColumn("pizry").setHeader("轻检员");	
		egu.getColumn("pizry").setWidth(80);
		egu.getColumn("pizry").setEditor(null);
		
//		设置煤矿下拉框
		ComboBox cMeik= new ComboBox();
		egu.getColumn("meikmc").setEditor(cMeik);
		cMeik.setEditable(true);
		String mkSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_MEIKXXB\" WHERE \"zhuangt\"=1 ORDER BY \"meikdq\",\"mingc\"";
		con.setAutoCommit(false);
		egu.getColumn("meikmc").setComboEditor(egu.gridId, new IDropDownModel(con,mkSql));
		con.commit();
		
//		设置运输单位下拉框
		ComboBox cYunsdw= new ComboBox();
		egu.getColumn("yunsdw").setEditor(cYunsdw);
		cYunsdw.setEditable(true);
		String ydSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_YUNSDWB\" WHERE \"zhuangt\"=1 ORDER BY \"mingc\"";
		con.setAutoCommit(false);
		egu.getColumn("yunsdw").setComboEditor(egu.gridId, new IDropDownModel(con,ydSql));
		con.commit();
		
//		设置品种下拉框
		ComboBox cPinz= new ComboBox();
		egu.getColumn("pinz").setEditor(cPinz);
		cPinz.setEditable(true);
		String pzSql=
			"SELECT \"id\",\"mingc\" FROM \"QCH_PINZB\" WHERE \"zhuangt\"=1 ORDER BY \"xuh\"";
		con.setAutoCommit(false);
		egu.getColumn("pinz").setComboEditor(egu.gridId, new IDropDownModel(con,pzSql));
		con.commit();
		
		egu.addTbarText("过衡日期:");
		DateField dfb = new DateField();
		dfb.setValue(getRiq());
		dfb.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		dfb.setId("RIQ");
		egu.addToolbarItem(dfb.getScript());
		
		egu.addTbarText("-");
		egu.addTbarText("输入车号：");
		TextField tf = new TextField();
		tf.setId("cheh");
		tf.setWidth(100);
		tf.setValue("");
		this.setCheh("");
		egu.addToolbarItem(tf.getScript());

		ToolbarButton chazhao = new ToolbarButton(null, "(模糊)查找",
				"function() {document.getElementById('CHEH').value=cheh.getValue();" 
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200) 
				+ " document.getElementById('ChaxButton').click();}");
		chazhao.setIcon(SysConstant.Btn_Icon_Search);
		egu.addToolbarItem(chazhao.getScript());
		egu.addTbarText("-");
		
		ToolbarButton rbtn = new ToolbarButton(null, "刷新",
				"function() {" 
				+ MainGlobal.getExtMessageShow("正在处理数据,请稍后...", "处理中...", 200) 
				+ " document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addToolbarItem(rbtn.getScript());
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.addOtherScript("gridDiv_grid.addListener('afteredit',function(e){ " +
					
					" var rec=e.record;\n" +
					" var MAOZ=eval(rec.get('maoz')||0);\n" +
					" var PIZ=eval(rec.get('piz')||0);\n"+
					" var KOUS=eval(rec.get('kous')||0);\n" +		
					
					"if (MAOZ<=PIZ+KOUS) {\n" +
					"	Ext.MessageBox.alert('提示信息','毛重的值必须比皮重大');\n" +
					"  return;\n" +
					"}\n" +
					
					"if( e.field=='maoz' || e.field=='piz' || e.field=='kous'){\n" +
					" rec.set('jingz',Round_new(MAOZ-PIZ-KOUS,2));"+
					"} \n" +							
				"\n});" );
			
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
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq(DateUtil.FormatDate(new Date()));
			getSelectData();
		}		
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

}
