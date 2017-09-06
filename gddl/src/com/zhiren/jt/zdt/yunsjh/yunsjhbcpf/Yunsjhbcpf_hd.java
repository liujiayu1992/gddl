package com.zhiren.jt.zdt.yunsjh.yunsjhbcpf;

import java.sql.ResultSet;
import java.sql.SQLException;
//import java.text.Format;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
//import com.zhiren.common.ext.form.Field;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunsjhbcpf_hd extends BasePage implements PageValidateListener {
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		int flag = visit.getExtGrid1().Save(getChange(), visit);
//		if (flag!=-1){
//			setMsg(ErrorMessage.SaveSuccessMessage);
//		}
//		
//		if (getChange() == null || "".equals(getChange())) {
//			setMsg("没有需要保存的记录！");
//			return;
//		}
		
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		
		ResultSetList rsl=visit.getExtGrid1().getDeleteResultSet(getChange());
		while (rsl.next()){
			//System.out.println("保存时进入删除");
			String cc ="delete from yunsjhb " +
			"where id="+rsl.getString("id")+";\n";
			sb.append(cc.toString());
		}
		
		rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			setMsg("没有需要保存的记录！");
			return;
		}

		while (rsl.next()) {
			long diancxxb_id=(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			if ("0".equals(rsl.getString("id"))) {
				
				sb.append("insert into yunsjhb (ID,RIQ,GONGYSB_ID,DIANCXXB_ID,FAZ_ID,DAOZ_ID,PINM,PIC,PID,\n");
				sb.append("PIZJHH,HUANZG,ZONGDG,ZIBCCC,SHUNH,TIELJ,LEIX,YUANC,YUAND,BEIZ,YUNSFSB_ID,JIHN,\n");
				sb.append("JIHW,ZONGCS,ZONGDS,SHIJDS,MEIKXXB_ID)\n");
				
				sb.append("values (getnewid(").append(diancxxb_id).append("),");
				sb.append(DateUtil.FormatOracleDate(rsl.getString("riq")));
				sb.append(",").append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
				sb.append(",").append(diancxxb_id);
				sb.append(",").append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id")));
				sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",").append((getExtGrid().getColumn("pinm").combo).getBeanId(rsl.getString("pinm")));
				sb.append(",0,0,'',0,0,'',0,'',0,0,0,''");
				sb.append(",").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				sb.append(",").append(rsl.getDouble("jihn"));
				sb.append(",").append(rsl.getDouble("jihw"));
				sb.append(",").append(rsl.getDouble("zongcs"));
				sb.append(",").append(rsl.getDouble("zongds"));
				sb.append(",0,").append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id"))).append(");\n");
				
			}else{
 				sb.append("update yunsjhb set DIANCXXB_ID=");
 				sb.append(diancxxb_id).append(",gongysb_id=");
				sb.append(((IDropDownModel) getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
				sb.append(",riq=").append(DateUtil.FormatOracleDate(rsl.getString("riq")));
				sb.append(",faz_id=").append(((IDropDownModel) getChezModel()).getBeanId(rsl.getString("faz_id")));
				sb.append(",daoz_id=").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(rsl.getString("daoz_id")));
				sb.append(",pinm=").append((getExtGrid().getColumn("pinm").combo).getBeanId(rsl.getString("pinm")));
				sb.append(",yunsfsb_id=").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(rsl.getString("yunsfsb_id")));
				sb.append(",jihn=").append(rsl.getDouble("jihn"));
				sb.append(",jihw=").append(rsl.getDouble("jihw"));
				sb.append(",zongcs=").append(rsl.getDouble("zongcs"));
				sb.append(",zongds=").append(rsl.getDouble("zongds"));
				sb.append(",meikxxb_id=").append(((IDropDownModel) getMeikModel()).getBeanId(rsl.getString("meikxxb_id")));
				sb.append(" where id=").append(rsl.getString("id")).append(";");
				
			}
			
		}
		sb.append("end;");
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyButton = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyButton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData(null);
		}
		if (_CopyButton) {
			_CopyButton = false;
			CoypLastYueData();
		}
	}

	// 复制上月
	public void CoypLastYueData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框
		String riq1="";
		long month=0;
		long year=0;
		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		month=intMonth;
		year=intyear;
		String month1="";
		// 当月份是1时上个月显示12
		if (intMonth - 1 == 0) {
			intMonth = 12;
			intyear = intyear - 1;
		} else {
			intMonth = intMonth - 1;
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			month1= "0" + month;
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
			month1= "" + month;
		}
		riq1=year+"-"+month1+"-01";

		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
					+ ")";

		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";

		}
		
		String chaxun = "select y.id,d.mingc as diancxxb_id,g.mingc as gongysb_id,m.mingc as meikxxb_id,y.riq,\n"
			+ " (select c.mingc from chezxxb c where y.faz_id=c.id) as faz_id,\n"
			+ " (select c.mingc from chezxxb c where y.daoz_id=c.id) as daoz_id,\n"
			+ " p.mingc as pinm,ys.mingc as yunsfsb_id,y.yuanc,y.yuand,y.pic,y.pid,y.pizjhh,y.jihn,y.jihw," 
			+ " y.zongcs,y.zongds,y.shijds,y.leix\n"
			+ " from yunsjhb y,diancxxb d,gongysb g,pinzb p,yunsfsb ys,meikxxb m\n"
			+ " where y.gongysb_id=g.id and y.diancxxb_id=d.id and y.meikxxb_id=m.id and y.pinm=p.id and y.yunsfsb_id=ys.id and y.leix=0 \n"
			+ " and to_char(y.riq,'yyyy-mm')='"
			+ intyear
			+ "-"
			+ StrMonth
			+ "'  \n"
			+ "    "
			+ str
			+ "  "
			+ "   order by y.id";
		
		// System.out.println("复制同期的数据:"+copyData);
		ResultSetList rslcopy = con.getResultSetList(chaxun);
		while (rslcopy.next()) {
			long diancxxb_id=(getExtGrid().getColumn("diancxxb_id").combo).getBeanId(rslcopy.getString("diancxxb_id"));
			
			String gongysb_id = rslcopy.getString("gongysb_id");
			String meikxxb_id = rslcopy.getString("meikxxb_id");
			Date riq = rslcopy.getDate("riq");
			String faz_id = rslcopy.getString("faz_id");
			String daoz_id = rslcopy.getString("daoz_id");
			String yunsfsb_id = rslcopy.getString("yunsfsb_id");
			String pinm = rslcopy.getString("pinm");
//			long yuanc = rslcopy.getLong("yuanc");
//			long yuand = rslcopy.getLong("yuand");
//			long pic = rslcopy.getLong("pic");
//			long pid = rslcopy.getLong("pid");
//			String pizjhh = rslcopy.getString("pizjhh");
//			long leix = rslcopy.getLong("leix");
			
			long jihn = rslcopy.getLong("jihn");
			long jihw = rslcopy.getLong("jihw");
			long zongcs = rslcopy.getLong("zongcs");
			long zongds = rslcopy.getLong("zongds");
			long shijds = rslcopy.getLong("shijds");

			StringBuffer sb = new StringBuffer();
			
			sb.append("insert into yunsjhb (ID,RIQ,GONGYSB_ID,DIANCXXB_ID,FAZ_ID,DAOZ_ID,PINM,PIC,PID,\n");
			sb.append("PIZJHH,HUANZG,ZONGDG,ZIBCCC,SHUNH,TIELJ,LEIX,YUANC,YUAND,BEIZ,YUNSFSB_ID,JIHN,\n");
			sb.append("JIHW,ZONGCS,ZONGDS,SHIJDS,MEIKXXB_ID)\n");
			sb.append("values (getnewid(").append(diancxxb_id).append("),");
			sb.append(DateUtil.FormatOracleDate(riq1));
			sb.append(",").append(((IDropDownModel) getGongysModel()).getBeanId(gongysb_id));
			sb.append(",").append(diancxxb_id);
			sb.append(",").append(((IDropDownModel) getChezModel()).getBeanId(faz_id));
			sb.append(",").append((getExtGrid().getColumn("daoz_id").combo).getBeanId(daoz_id));
			sb.append(",").append((getExtGrid().getColumn("pinm").combo).getBeanId(pinm));
			sb.append(",0,0,'',0,0,'',0,'',0,0,0,''");
			sb.append(",").append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(yunsfsb_id));
			sb.append(",").append(jihn);
			sb.append(",").append(jihw);
			sb.append(",").append(zongcs);
			sb.append(",").append(zongds);
			sb.append(",").append(shijds).append(",").append(((IDropDownModel) getMeikModel()).getBeanId(meikxxb_id)).append(")\n");
			
			con.getInsert(sb.toString());
		}
		getSelectData(null);
		con.Close();
	}

	/*private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}*/

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 工具栏的年份和月份下拉框

		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		// -----------------------------------
		String str = "";

		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (d.id = " + getTreeid() + " or d.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and d.id = " + getTreeid() + "";
			}
		}
		int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "and (d.id = " + getTreeid() + " or d.fuid = " + getTreeid()
//					+ ")";
			str = "  and (d.fuid=  " +this.getTreeid()+" or d.shangjgsid="+this.getTreeid()+")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and d.id = " + getTreeid() + "";

		}
		
		long zongcs_cs=0;//设置总车数的参数-在js中用到！
		
		String sql_ts =" select zhi from xitxxb where leib like '%月运输计划%' and mingc = '设置总数的变量' and diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		
		ResultSet rs1=con.getResultSet(sql_ts);
		try {
			if(rs1.next()){
				zongcs_cs=rs1.getLong("zhi");
			}
			rs1.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		if (rsl == null) {
			
			String chaxun = "select y.id,d.mingc as diancxxb_id,g.mingc as gongysb_id,m.mingc as meikxxb_id,y.riq,\n"
				+ " (select c.mingc from chezxxb c where y.faz_id=c.id) as faz_id,\n"
				+ " (select c.mingc from chezxxb c where y.daoz_id=c.id) as daoz_id,\n"
				+ " p.mingc as pinm,ys.mingc as yunsfsb_id,y.jihn,y.jihw," //y.yuanc,y.yuand,y.pic,y.pid,y.pizjhh,y.shijds,
				+ " y.zongcs,y.zongds,y.leix\n"
				+ " from yunsjhb y,diancxxb d,gongysb g,pinzb p,yunsfsb ys,meikxxb m\n"
				+ " where y.gongysb_id=g.id and y.diancxxb_id=d.id and y.meikxxb_id=m.id and y.pinm=p.id and y.yunsfsb_id=ys.id and y.leix=0 \n"
				+ " and to_char(y.riq,'yyyy-mm')='"
				+ intyear
				+ "-"
				+ StrMonth
				+ "'  \n"
				+ "    "
				+ str
				+ "  "
				+ "   order by y.id";

			//System.out.println(chaxun);
			rsl = con.getResultSetList(chaxun);
		}
		boolean showBtn = false;
		if (rsl.next()) {
			rsl.beforefirst();
			showBtn = false;
		} else {
			showBtn = true;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yunsjhb");

		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("pinm").setHeader("品种");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
//		egu.getColumn("yuanc").setHeader("原车数");
//		egu.getColumn("yuand").setHeader("原吨数");
//		egu.getColumn("pic").setHeader("批车数");
//		egu.getColumn("pid").setHeader("批吨数");
//		egu.getColumn("pizjhh").setHeader("批准号");
		
		egu.getColumn("jihn").setHeader("计划内");
		egu.getColumn("jihw").setHeader("计划外");
		egu.getColumn("zongcs").setHeader("总车数");
		egu.getColumn("zongds").setHeader("总吨数");
		//egu.getColumn("shijds").setHeader("实际吨数");
		

		// 设置不可编辑的颜色
		// egu.getColumn("daohldy").setRenderer("function(value,metadata){metadata.css='tdTextext';
		// return value;}");

		// 设定列初始宽度
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("diancxxb_id").setWidth(90);
		egu.getColumn("faz_id").setWidth(50);
		egu.getColumn("daoz_id").setWidth(60);
		egu.getColumn("pinm").setWidth(50);
		egu.getColumn("yunsfsb_id").setWidth(60);
//		egu.getColumn("yuanc").setWidth(50);
//		egu.getColumn("yuanc").setDefaultValue("0");
//		egu.getColumn("yuand").setWidth(50);
//		egu.getColumn("yuand").setDefaultValue("0");
//		egu.getColumn("pic").setWidth(50);
//		egu.getColumn("pic").setDefaultValue("0");
//		egu.getColumn("pid").setWidth(50);
//		egu.getColumn("pid").setDefaultValue("0");
//		egu.getColumn("pizjhh").setWidth(50);
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setWidth(50);
		egu.getColumn("leix").setHidden(true);
		egu.getColumn("leix").setDefaultValue("0");// 补充计划
		
		egu.getColumn("jihn").setWidth(50);
		egu.getColumn("jihn").setDefaultValue("0");
		egu.getColumn("jihw").setWidth(50);
		egu.getColumn("jihw").setDefaultValue("0");
		egu.getColumn("zongcs").setWidth(50);
		egu.getColumn("zongcs").setDefaultValue("0");
		egu.getColumn("zongds").setWidth(50);
		egu.getColumn("zongds").setDefaultValue("0");
//		egu.getColumn("shijds").setWidth(60);
//		egu.getColumn("shijds").setDefaultValue("0");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		
		egu.addPaging(100);// 设置分页
		egu.setWidth(1000);// 设置页面的宽度,当超过这个宽度时显示滚动条

		// *****************************************设置默认值****************************
		
//		电厂下拉框
		int treejib2 = this.getDiancTreeJib();

		if (treejib2 == 1) {// 选集团时刷新出所有的电厂
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where (fuid="
									+ getTreeid() + " or shangjgsid ="+getTreeid()+") order by mingc"));
			egu.getColumn("diancxxb_id").setReturnId(true);
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			//egu.getColumn("diancxxb_id").setEditor(new ComboBox());
			ComboBox cb_diancxxb = new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(cb_diancxxb);
			egu.getColumn("diancxxb_id").setDefaultValue("");
			cb_diancxxb.setEditable(true);
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
			String mingc="";
			if(r.next()){
				mingc=r.getString("mingc");
			}
			egu.getColumn("diancxxb_id").setReturnId(true);
			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
			
		}		
		
		ComboBox cb_yunsfsb = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(cb_yunsfsb);
		egu.getColumn("yunsfsb_id").setDefaultValue("");
		cb_yunsfsb.setEditable(true);
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from yunsfsb order by mingc"));
		egu.getColumn("yunsfsb_id").setReturnId(true);
		
//		egu.getColumn("diancxxb_id").setDefaultValue(getDiancmc());
		
		// 设置日期的默认值,
		egu.getColumn("riq").setDefaultValue(intyear + "-" + StrMonth + "-01");

		// *************************下拉框*****************************************88
		// 设置供应商的下拉框
		//egu.getColumn("gongysb_id").setEditor(new ComboBox());
		/*ComboBox cb_gongysb = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongysb);
		egu.getColumn("gongysb_id").setDefaultValue("");
		cb_gongysb.setEditable(true);
		String GongysSql = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(GongysSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// 设置发站下拉框
		ComboBox cb_faz = new ComboBox();
		egu.getColumn("faz_id").setEditor(cb_faz);
		cb_faz.setEditable(true);
		String fazSql = "select id ,mingc from chezxxb c where c.leib='车站' order by c.mingc";
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel(fazSql));
		egu.getColumn("faz_id").setReturnId(true);*/
		// 设置到站下拉框
		ComboBox cb_daoz = new ComboBox();
		egu.getColumn("daoz_id").setEditor(cb_daoz);
		cb_daoz.setEditable(true);

		String daozSql = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel(daozSql));
		// 设置电厂默认到站
		egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		egu.getColumn("daoz_id").setReturnId(true);
		// 设置品种下拉框
		ComboBox c5 = new ComboBox();
		egu.getColumn("pinm").setEditor(c5);
		c5.setEditable(true);

		String pinzSql = "select id,mingc from pinzb order by mingc";
		egu.getColumn("pinm").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		egu.getColumn("pinm").setReturnId(true);
		// 设置运输方式下拉框
		/*ComboBox c6 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c6);
		c6.setEditable(true);

		String yunsfsSql = "select id,mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsfsSql));
		egu.addToolbarItem(c6.getScript());
		egu.getColumn("yunsfsb_id").setReturnId(true);*/		
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());

		egu.addTbarText("-");// 设置分隔符

		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		System.out.println(getTreeid());
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		// 设定工具栏下拉框自动刷新
		egu
				.addOtherScript("NIANF.on('select',function(){document.forms[0].submit();});YUEF.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert, egu.gridId,
				egu.getGridColumns(), null);
				gb_insert.setId("INSERT");
				egu.addTbarBtn(gb_insert);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId,
				egu.getGridColumns(), "SaveButton");
				ght.setId("SAVE");
				egu.addTbarBtn(ght);


		if (showBtn) {
//			egu.addToolbarItem("{"
//				+ new GridButton("复制同期计划",
//				"function(){document.getElementById('CopyButton').click();}")
//				.getScript() + "}");
			
			StringBuffer cpb = new StringBuffer();
			cpb.append("function(){").append(
					"document.getElementById('CopyButton').click();}");
			GridButton cpr = new GridButton("复制前月数据", cpb.toString());
			cpr.setIcon(SysConstant.Btn_Icon_Copy);
			egu.addTbarBtn(cpr);
		}
//		//供应货煤矿车站关联
//		egu
//		.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
//						+ "row = irow; \n"
//						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
//						+ "gongysTree_window.show();}});\n");
//		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
//		egu.setDefaultsortable(false);

		// ---------------页面js的计算开始------------------------------------------
		String sb_str=
			"gridDiv_grid.on('afteredit',function(e){\n" +
			"rec=gridDiv_ds.getAt(e.row);\n" + 
			"var falge=0;\n" + 
			"var diancxxb_id=rec.get('DIANCXXB_ID');\n"+
			"var gonghdw=rec.get('GONGYSB_ID');\n" + 
			"var pinzb=rec.get('PINZB_ID');\n" + 
			"var yunsfsb=rec.get('YUNSFSB_ID');\n" + 
			"var faz_id=rec.get('FAZ_ID');\n"+
			"var daoz_id=rec.get('DAOZ_ID');\n"+
			"var jihn=rec.get('JIHN');\n"+
			"var jihw=rec.get('JIHW');\n"+
			"if (yunsfsb=='铁路'){\n"+
			"	e.record.set('ZONGCS',parseFloat(e.record.get('JIHN'))+parseFloat(e.record.get('JIHW')));\n"+
			"	e.record.set('ZONGDS',parseFloat(e.record.get('ZONGCS'))*"+zongcs_cs+");\n"+
			"}\n"+
			"var rows=gridDiv_ds.getTotalCount();\n" + 
			"for (var i=0;i<rows;i++){\n" + 
			"var rec1=gridDiv_ds.getAt(i);\n" + 
			"var diancxxb_id1=rec1.get('DIANCXXB_ID');\n"+
			"var gonghdw1=rec1.get('GONGYSB_ID');\n" + 
			"var pinzb1=rec1.get('PINZB_ID');\n" + 
			"var yunsfsb1=rec1.get('YUNSFSB_ID');\n" + 
			"var faz_id1=rec1.get('FAZ_ID');\n"+
			"var daoz_id1=rec1.get('DAOZ_ID');\n"+
			"if(i==e.row){\n" + 
			"continue;\n" + 
			"}else if (gonghdw==gonghdw1 && diancxxb_id==diancxxb_id1 && pinzb==pinzb1 && yunsfsb==yunsfsb1 && faz_id==faz_id1 && daoz_id==daoz_id1){\n" + 
			"falge=1;\n Ext.MessageBox.alert('提示信息',\"您录入的数据与第\"+(i+1)+\"行数据条件完全相同，请您修改数据！\");\n break;\n" + 
			"}else{\n" + 
			" continue;\n" + 
			"}\n" + 
			"}\n" + 
			"if(falge==1){\n" +
			"Ext.getCmp(\"SAVE\").setDisabled(true) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(true) ;\n" +
			"}else{\n" + 
			"Ext.getCmp(\"SAVE\").setDisabled(false) ;\n" + 
			"Ext.getCmp(\"INSERT\").setDisabled(false) ;\n" +
			"}"+
			"});";

		StringBuffer sb = new StringBuffer(sb_str);
		egu.addOtherScript(sb.toString());
		// ---------------页面js计算结束--------------------------
		
		// -----------------------供应商-煤矿单位-发站关连设置-------------------------------- //
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
						+ "row = irow; \n"
						+ "if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
						+ "gongysTree_window.show();}});\n");
		// egu.addOtherScript("gridDiv_grid.on('afteredit',function(e){alert();if(e.Field=='MEIKXXB_ID'){e.record.set('YUANMKDW',e.value)}});\n");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz_kj,
				"gongysTree", "" + visit.getDiancxxb_id(), null, null, null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler
				.append("function() { \n")
				.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
				.append("if(cks==null){gongysTree_window.hide();return;} \n")
				.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
				.append("if(cks.getDepth() == 4){ \n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.parentNode.text);\n")
				.append("rec.set('FAZ_ID', cks.parentNode.text);rec.set('JIHKJB_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 3){\n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.parentNode.text);\n")
				.append("rec.set('YUANMKDW', cks.parentNode.text);\n")
				.append("rec.set('FAZ_ID', cks.text);\n")
				.append("}else if(cks.getDepth() == 2){\n")
				.append("rec.set('GONGYSB_ID', cks.parentNode.text);\n")
				.append("rec.set('MEIKXXB_ID', cks.text);\n")
				.append("rec.set('YUANMKDW', cks.text);\n")
				.append("}else if(cks.getDepth() == 1){\n")
				.append("rec.set('GONGYSB_ID', cks.text); }\n")
				.append("gongysTree_window.hide();\n")
				.append("return;")
				.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		
		
		
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
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			//供应商，煤矿，车站
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setChezModel(null);
			setChezModels();
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();

		}
		getSelectData(null);

	}

	// 年份
	private static IPropertySelectionModel _NianfModel;

	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}

	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

	// 得到电厂的默认到站
	public String getDiancDaoz() {
		String daoz = "";
		String treeid = this.getTreeid();
		if (treeid == null || treeid.equals("")) {
			treeid = "1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = " + treeid + "");

			ResultSet rs = con.getResultSet(sql.toString());

			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return daoz;
	}

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
	
	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}

	//集联下拉框内容，供应商，煤矿，车站
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public IPropertySelectionModel getChezModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setChezModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setChezModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void setChezModels() {
		String sql = "select id,mingc from chezxxb order by xuh,mingc";
		setChezModel(new IDropDownModel(sql));
	}
	
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}

		return jib;
	}
}
