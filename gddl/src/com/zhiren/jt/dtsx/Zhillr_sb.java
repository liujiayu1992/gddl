package com.zhiren.jt.dtsx;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author huochaoyuan
 *陕西分公司日报程序用
 *新建验收管理“质量填报”(Zhillr_sb)
 *根据分矿录入的数量信息录入质量信息，在原有页面基础上修改，
 *分电厂取数，添加数量列(fahb.jingz)，修改车数字段库来源改为(fahb.ches)；
 *为“保存”按钮添加修改时限控制；
 */
public class Zhillr_sb extends BasePage implements PageValidateListener {
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

	// 绑定日期
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
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
		Visit visit = (Visit) this.getPage().getVisit();
		// visit.getExtGrid1().Save(getChange(), visit);
		Save1(getChange(), visit);
	}

	public void Save1(String strchange, Visit visit) {
		JDBCcon con = new JDBCcon();
		ExtGridUtil egu = getExtGrid();
		String zhilbid = "";
		String fahbid = "";

		try {

			StringBuffer sql = new StringBuffer("begin \n");

			String tableName = "zhilb";
			String tableName2 = "fahb";

			ResultSetList delrsl = egu.getDeleteResultSet(strchange);
			while (delrsl.next()) {
				sql.append(" delete from ").append(tableName).append(
						" where id =").append(delrsl.getString(0)).append(
						";	\n");
			}

			ResultSetList mdrsl = egu.getModifyResultSet(strchange);
			while (mdrsl.next()) {
				StringBuffer sql2 = new StringBuffer();
				StringBuffer sql3 = new StringBuffer();
				zhilbid = MainGlobal.getNewID(visit.getDiancxxb_id());
				sql2.append(zhilbid);
				if ("0".equals(mdrsl.getString("ID"))) {
					sql.append("insert into ").append(tableName).append("(id");
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {
						if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("JIHKJB_ID")
								|| mdrsl.getColumnNames()[i].equals("FAHRQ")
								|| mdrsl.getColumnNames()[i].equals("DAOHRQ")
								|| mdrsl.getColumnNames()[i]
										.equals("YUNSFSB_ID")
								|| mdrsl.getColumnNames()[i].equals("CHES")) {

						} else {
							if (mdrsl.getColumnNames()[i].equals("FAHBID")) {
								fahbid = getValueSql(egu.getColumn(mdrsl
										.getColumnNames()[i]), mdrsl
										.getString(i));

							} else if (mdrsl.getColumnNames()[i]
									.equals("HUAYSJ")) {
								sql.append(",").append(
										mdrsl.getColumnNames()[i]);
								sql2.append(",").append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i)));
							} else {
								sql.append(",").append(
										mdrsl.getColumnNames()[i]);
								sql2.append(",").append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i)));
							}
						}
					}
					sql.append(") values(").append(sql2).append(");\n");
					sql3.append("update ").append(tableName2).append(
							" set zhilb_id=").append(zhilbid).append(
							" where id=").append(fahbid).append(";\n");
					sql.append(sql3.toString());
				} else {
					sql.append("update ").append(tableName).append(" set ");
					for (int i = 1; i < mdrsl.getColumnCount(); i++) {
						if (mdrsl.getColumnNames()[i].equals("GONGYSB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("MEIKXXB_ID")
								|| mdrsl.getColumnNames()[i]
										.equals("JIHKJB_ID")
								|| mdrsl.getColumnNames()[i].equals("FAHRQ")
								|| mdrsl.getColumnNames()[i].equals("DAOHRQ")
								|| mdrsl.getColumnNames()[i]
										.equals("YUNSFSB_ID")
								|| mdrsl.getColumnNames()[i].equals("CHES")) {

						} else {
							if (mdrsl.getColumnNames()[i].equals("FAHBID")) {
								fahbid = getValueSql(egu.getColumn(mdrsl
										.getColumnNames()[i]), mdrsl
										.getString(i));
							} else if (mdrsl.getColumnNames()[i]
									.equals("HUAYSJ")) {
								sql.append(" HUAYSJ = ");
								sql.append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i))).append(
										",");
							} else {
								sql.append(mdrsl.getColumnNames()[i]).append(
										" = ");
								sql.append(
										getValueSql(egu.getColumn(mdrsl
												.getColumnNames()[i]), mdrsl
												.getString(i))).append(",");
							}
						}
					}
					sql.deleteCharAt(sql.length() - 1);
					zhilbid = mdrsl.getString("ID");
					sql.append(" where id =").append(mdrsl.getString("ID"))
							.append(";\n");
				}
				sql3.append("update ").append(tableName2).append(
						" set zhilb_id=").append(zhilbid).append(" where id=")
						.append(fahbid).append(";\n");
				sql.append(sql3.toString());
			}

			sql.append("end;");
			con.getUpdate(sql.toString());
			con.Close();

		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		getSelectData();
	}

	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else if ("float".equals(gc.datatype)) {
			return value == null || "".equals(value) ? "null" : value;
		} else {
			return value;
		}
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	private boolean _ShenhClick = false;

	public void ShenhButton(IRequestCycle cycle) {
		_ShenhClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ShenhClick) {
			_ShenhClick = false;
			Shenh();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
	}

	private void Shenh() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "Shujsh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
		}
//		int flag = 0;
//		int ches = 0;
//		int biaoz = 0;
		while (rsl.next()) {
			String zhilb_id = rsl.getString("id");
//			ches += rsl.getInt("ches");
//			biaoz += rsl.getInt("biaoz");
			sb.append("update zhilb set shenhzt = 1 where id="+zhilb_id);
			con.getUpdate(sb.toString());
			}
		con.commit();
		con.Close();
	//	setMsg("审核成功！共审核了 " + ches + " 车，总票重 " + biaoz + " 吨。");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		String riq = getRiq();// 查看

		sb.append("select nvl(zl.id,0) as id,");// ,f.jingz
		sb
				.append(" f.id as fahbid,g.mingc as gongysb_id,mk.mingc as meikxxb_id,jh.mingc as jihkjb_id,f.fahrq,f.daohrq,y.mingc as yunsfsb_id,");
		sb.append(" f.ches,round(f.jingz,0) as jingz,zl.huaybh,nvl(zl.caiyb_id,0) as caiyb_id,");
		sb
				.append(" decode(zl.huaysj,null,to_date('"
						+ riq
						+ "','yyyy-mm-dd'),zl.huaysj) as huaysj,");
		// --低位热、收到基灰分、挥发分、全水、干基硫
		sb.append(" zl.qnet_ar,zl.aar,zl.vdaf,zl.mt,zl.std,");
		// 不可显示的字段
		sb
				.append(" nvl(zl.ad,0) as ad,nvl(zl.stad,0) as stad,nvl(zl.aad,0) aad,nvl(zl.mad,0) as mad,nvl(zl.qbad,0) as qbad,nvl(zl.had,0) had,");
		sb
				.append(" nvl(zl.vad,0) as vad,nvl(zl.fcad,0) as fcad,nvl(zl.qgrad,0) as qgrad,nvl(zl.hdaf,0) as hdaf,nvl(zl.qgrad_daf,0) as qgrad_daf,nvl(zl.sdaf,0) as sdaf,");
		sb
				.append(" nvl(zl.var,0) as var,nvl(zl.jijsf,0) as jijsf,nvl(zl.jijfrl,0) as jijfrl,zl.beiz");
		sb
				.append(" from zhilb zl,fahb f,gongysb g,meikxxb mk,jihkjb jh,yunsfsb y,diancxxb d where f.zhilb_id=zl.id(+) and f.gongysb_id=g.id \n");
		sb.append("and f.diancxxb_id=d.id(+)\n");
		sb.append("and (d.id="+visit.getDiancxxb_id()+" or d.fuid="+visit.getDiancxxb_id()+")\n");
		sb
				.append(" and f.meikxxb_id=mk.id and f.jihkjb_id=jh.id and f.yunsfsb_id=y.id");
		sb.append(" and to_char(f.daohrq,'yyyy-mm-dd')='").append(riq).append(
				"'");

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			// setMsg(ErrorMessage.NullResult);
			setMsg("请确认发货表是否有相关数据！");
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		// 设置多选框
		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		egu.setTableName("zhilb");
		egu.getColumn("fahbid").setHeader("发货表ID");
		egu.getColumn("fahbid").setHidden(true);
		egu.getColumn("fahbid").setWidth(80);
		egu.getColumn("fahbid").editor = null;
		// 发货表信息
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("gongysb_id").editor = null;
		egu.getColumn("gongysb_id").setUpdate(true);
		egu.getColumn("meikxxb_id").setHeader("煤矿名称");
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("meikxxb_id").editor = null;
		egu.getColumn("meikxxb_id").setUpdate(true);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("jihkjb_id").setHidden(true);
		egu.getColumn("jihkjb_id").editor = null;
		egu.getColumn("jihkjb_id").setUpdate(true);
		egu.getColumn("fahrq").setHeader("发货时间");
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("fahrq").editor = null;
		egu.getColumn("fahrq").setUpdate(true);
		egu.getColumn("daohrq").setHeader("到货时间");
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("daohrq").setHidden(true);
		egu.getColumn("daohrq").editor = null;
		egu.getColumn("daohrq").setUpdate(true);
		// egu.getColumn("biaoz").setHeader("标重");
		// egu.getColumn("biaoz").setWidth(80);
		// egu.getColumn("biaoz").editor = null;
		// egu.getColumn("biaoz").setUpdate(true);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").editor = null;
		egu.getColumn("yunsfsb_id").setUpdate(true);
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(80);
		egu.getColumn("ches").editor = null;
		egu.getColumn("ches").setUpdate(true);
     	 egu.getColumn("jingz").setHeader("净重");
		 egu.getColumn("jingz").setWidth(60);
		 egu.getColumn("jingz").editor = null;
		 egu.getColumn("jingz").update=false;
		// 必填项
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("caiyb_id").setHeader("采样表ID");
		egu.getColumn("caiyb_id").setHidden(true);
		egu.getColumn("caiyb_id").setDefaultValue("-1");
		egu.getColumn("caiyb_id").editor = null;
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setWidth(80);
		// egu.getColumn("jingz").setHeader("净重");
		// egu.getColumn("jingz").setWidth(60);
		// egu.getColumn("jingz").update=false;
		egu.getColumn("qnet_ar").setHeader("低位热(Qnet,ar)(MJ/Kg)");
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("aar").setHeader("收到基灰分(Aar)%");
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("vdaf").setHeader("挥发分(Vdaf)%");
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("mt").setHeader("全水(Mt)%");
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("std").setHeader("干基硫(St,d)%");
		egu.getColumn("std").setWidth(60);
		// 可不填项
		egu.getColumn("ad").setHeader("干基灰(Ad)");
		egu.getColumn("ad").setWidth(70);
		egu.getColumn("ad").setHidden(true);
		egu.getColumn("ad").setDefaultValue("0");
		egu.getColumn("stad").setHeader("空干基硫(Stad)");
		egu.getColumn("stad").setWidth(80);
		// egu.getColumn("stad").setHidden(true);
		egu.getColumn("stad").setDefaultValue("0");
		egu.getColumn("aad").setHeader("空干基灰(Aad)");
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("aad").setHidden(true);
		egu.getColumn("aad").setDefaultValue("0");
		egu.getColumn("mad").setHeader("空干基水(Mad)");
		egu.getColumn("mad").setWidth(80);
		// egu.getColumn("mad").setHidden(true);
		egu.getColumn("mad").setDefaultValue("0");
		egu.getColumn("qbad").setHeader("弹筒热(Qbad)");
		egu.getColumn("qbad").setWidth(80);
		// egu.getColumn("qbad").setHidden(true);
		egu.getColumn("qbad").setDefaultValue("0");
		egu.getColumn("had").setHeader("空干基氢(Had)");
		egu.getColumn("had").setWidth(80);
		// egu.getColumn("had").setHidden(true);
		egu.getColumn("had").setDefaultValue("0");
		egu.getColumn("vad").setHeader("空干基挥发分(Vad)");
		egu.getColumn("vad").setWidth(100);
		// egu.getColumn("vad").setHidden(true);
		egu.getColumn("vad").setDefaultValue("0");
		egu.getColumn("fcad").setHeader("固定碳(Fcad)");
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("fcad").setHidden(true);
		egu.getColumn("fcad").setDefaultValue("0");
		egu.getColumn("qgrad").setHeader("空干基高位热(Qbrad)");
		egu.getColumn("qgrad").setWidth(100);
		// egu.getColumn("qgrad").setHidden(true);
		egu.getColumn("qgrad").setDefaultValue("0");
		egu.getColumn("hdaf").setHeader("干燥无灰基氢(Hdaf)");
		egu.getColumn("hdaf").setWidth(80);
		// egu.getColumn("hdaf").setHidden(true);
		egu.getColumn("hdaf").setDefaultValue("0");
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热(Qgrad,daf)");
		egu.getColumn("qgrad_daf").setWidth(100);
		// egu.getColumn("qgrad_daf").setHidden(true);
		egu.getColumn("qgrad_daf").setDefaultValue("0");
		egu.getColumn("sdaf").setHeader("干燥无灰基硫(Sdaf)");
		egu.getColumn("sdaf").setWidth(80);
		// egu.getColumn("sdaf").setHidden(true);
		egu.getColumn("sdaf").setDefaultValue("0");
		egu.getColumn("var").setHeader("收到基挥发份(Var)");
		egu.getColumn("var").setWidth(80);
		// egu.getColumn("var").setHidden(true);
		egu.getColumn("var").setDefaultValue("0");
		egu.getColumn("jijsf").setHeader("计价水分(%)");
		egu.getColumn("jijsf").setWidth(80);
		// egu.getColumn("jijsf").setHidden(true);
		egu.getColumn("jijsf").setDefaultValue("0");
		egu.getColumn("jijfrl").setHeader("计价发热量(Mj/kg)");
		egu.getColumn("jijfrl").setWidth(80);
		// egu.getColumn("jijfrl").setHidden(true);
		egu.getColumn("jijfrl").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(50);

		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("guohrq");
		egu.addToolbarItem(df.getScript());

		// 设置化验日期的默认值
//		String huaysj = DateUtil.FormatDate(new Date());
//		egu.getColumn("huaysj").setDefaultValue(huaysj);
		// 设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// 设置每页显示行数
		egu.addPaging(25);

		// egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		//egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		boolean tag=true;
		if(visit.isDCUser()){
			try {
				ResultSet kzsql=con.getResultSet(
				"select decode(beiz,null,-1,(to_date(substr(beiz,23,10),'yyyy-mm-dd')-to_date('"+riq+"','yyyy-mm-dd'))) as riq from diancxxb where id="+visit.getDiancxxb_id());
				int a;
				if(kzsql.next()){
					a=Integer.parseInt(kzsql.getString("riq"));
					if(a>=0){
						tag=false;
					}
				}
			} catch (SQLException e) {
				// TODO 自动生成 catch 块
				e.printStackTrace();
			}
		}
		if(tag){
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		}
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append(
				"document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton("审核", GridButton.ButtonType_SubmitSel,
				"ShenhButton");
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
		if (getExtGrid() == null) {
			return "";
		}
		if (getTbmsg() != null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem(
					"'<marquee width=300 scrollamount=2>" + getTbmsg()
							+ "</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	// 设置小时下拉框
	public IDropDownBean getHourValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getHourModel().getOptionCount(); i++) {
				Object obj = getHourModel().getOption(i);
				if (DateUtil.getHour(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setHourValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setHourValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getHourModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			getHourModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setHourModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void getHourModels() {
		List listHour = new ArrayList();
		for (int i = 0; i < 24; i++) {
			if (i < 10)
				listHour.add(new IDropDownBean(i, "0" + i));
			else
				listHour.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setHourModel(new IDropDownModel(listHour));
	}

	// 设置分钟下拉框
	public IDropDownBean getMinValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getMinModel().getOptionCount(); i++) {
				Object obj = getMinModel().getOption(i);
				if (DateUtil.getMinutes(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setMinValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setMinValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getMinModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			getMinModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMinModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void getMinModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setMinModel(new IDropDownModel(listMin));
	}

	// 设置秒下拉框
	public IDropDownBean getSecValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getSecModel().getOptionCount(); i++) {
				Object obj = getSecModel().getOption(i);
				if (DateUtil.getSeconds(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setSecValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setSecValue(IDropDownBean Value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getSecModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			getSecModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setSecModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	public void getSecModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if (i < 10)
				listMin.add(new IDropDownBean(i, "0" + i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setSecModel(new IDropDownModel(listMin));
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
			setRiq(DateUtil.FormatDate(new Date()));
			setHourValue(null);
			setHourModel(null);
			setMinValue(null);
			setMinModel(null);
			setSecValue(null);
			setSecModel(null);
			getSelectData();
		}
	}
}
