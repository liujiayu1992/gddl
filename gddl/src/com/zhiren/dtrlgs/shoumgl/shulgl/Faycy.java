package com.zhiren.dtrlgs.shoumgl.shulgl;

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

import com.zhiren.common.CustomMaths;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * 
 * @author 孙一博
 * 
 */
public class Faycy extends BasePage implements PageValidateListener {
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
	}

	// 页面刷新日期（卸煤日期）
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}

	// 页面刷新日期（卸煤日期）
	private String riqe;

	public String getRiqe() {
		return riqe;
	}

	public void setRiqe(String riqe) {
		this.riqe = riqe;
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String yangpbh;

	public String getYangpbh() {
		return yangpbh;
	}

	public void setYangpbh(String _yangpbh) {
		yangpbh = _yangpbh;
	}

	private double yangpzl;

	public double getYangpzl() {
		return yangpzl;
	}

	public void setYangpzl(double _yangpzl) {
		yangpzl = _yangpzl;
	}

	private String yangpfs;

	public String getYangpfs() {
		return yangpfs;
	}

	public void setYangpfs(String _yangpfs) {
		yangpfs = _yangpfs;
	}

	private String jieyr;

	public String getJieyr() {
		return jieyr;
	}

	public void setJieyr(String _jieyr) {
		jieyr = _jieyr;
	}

	private String beiz;

	public String getBeiz() {
		return beiz;
	}

	public void setBeiz(String _beiz) {
		beiz = _beiz;
	}

	private String jydw;

	public String getJianydw() {
		return jydw;
	}

	public void setJianydw(String _jydw) {
		jydw = _jydw;
	}

	// 送样单位数据源
	IDropDownBean songjdw;

	public IDropDownBean getSongjdw() {
		if (songjdw == null) {
			if (getSongjdwModel() != null) {
				setSongjdw((IDropDownBean) getSongjdwModel().getOption(0));
			} else {
				songjdw = new IDropDownBean();
			}
		}
		return songjdw;
	}

	public void setSongjdw(IDropDownBean _songjdw) {
		songjdw = _songjdw;
	}

	IPropertySelectionModel songjdwmodel;

	public IPropertySelectionModel getSongjdwModel() {
		if (songjdwmodel == null) {
			setSongjdwModelData();
		}
		return songjdwmodel;
	}

	public void setSongjdwModel(IPropertySelectionModel _songjdwmodel) {
		songjdwmodel = _songjdwmodel;
	}

	public void setSongjdwModelData() {
		String sql = "select t.id,t.mingc from item t, itemsort s\n"
				+ "where t.itemsortid = s.id and s.mingc = '质量检验单位'";
		setSongjdwModel(new IDropDownModel(sql));
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_UpdateClick) {
			_UpdateClick = false;
			((Visit) getPage().getVisit()).setString7(getRiq());
			((Visit) getPage().getVisit()).setString8(getRiqe());
			((Visit)this.getPage().getVisit()).setString9(this.getPageName());
			cycle.activate("Faycyxg");
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 卸煤日期的ora字符串格式
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		// 电厂ID
		String dcid="";
		if(visit.getRenyjb()==3){
		 dcid= " and f.diancxxb_id="+visit.getDiancxxb_id()+"\n";
		}
		String sql = " select    f.id ,d1.mingc diancxxb_id,g.mingc gongysb_id, y.mingc yunsfsb_id,l.mingc luncxxb_id,\n"
				+ " f.chec,f.fahrq,c.mingc faz_id,d2.mingc shr_diancxxb_id,p.mingc pinzb_id,f.meil,f.lurry,f.beiz\n"
				+ "from fayslb f,diancxxb d1,diancxxb d2,gongysb g,yunsfsb y,luncxxb l,pinzb p,chezxxb c\n"
				+ "where f.diancxxb_id=d1.id and d1.cangkb_id<>1 and d1.jib=3\n"
				+ "and f.gongysb_id=g.id \n"
				+ "and f.yunsfsb_id=y.id \n"
				+ "and f.luncxxb_id=l.id(+) \n"
				+ "and f.shr_diancxxb_id=d2.id and d2.cangkb_id=1 and d2.jib=3 \n"
				+ "and f.pinzb_id=p.id\n"
				+dcid
				+" and f.zhilb_id =0\n"
				+ "and f.faz_id=c.id\n"
				+ "and f.fahrq between "
				+ strxmrqOra
				+ " and "
				+ strxmrqeOra
				+ "+1\n";
		/*
		 * String sql = "select f.id id,\n" + " l.mingc chuanm,\n" + " z.hangc
		 * hangc ,\n" + " x.mingc gongysb_id,\n" + " z.jihcq jihcq,\n" + "
		 * z.pinzb_id pinz,\n" + " z.zhuangcl zhunagcl,\n" + " z.dunw dunw,\n" + "
		 * to_char(z.zhuangcjssj, 'yyyy-mm-dd hh24:mi:ss') zhuangcsj,\n" + "
		 * to_char(z.ligsj,'yyyy-mm-dd hh24:mi:ss') ligsj,\n" + " b.mingc bow\n" + "
		 * from fayslb f, vwpinz p,bowxxb b ,luncxxb l,vwxuqdw x\n" + " where
		 * x.id =z.xiaosgysb_id\n" + " and z.diancxxb_id = "+dcid+ "\n" + " and
		 * p.id = z.pinzb_id\n" + " and z.luncxxb_id = l.id\n" + " and
		 * b.id=z.bowxxb_id\n" + " and z.zhilb_id = 0\n" + " and z.zhuangcjssj >=
		 * "+strxmrqOra+"\n" + " and z.zhuangcjssj <"+strxmrqeOra+"+1";
		 */

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置grid可编辑
		// egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置多选框
		egu.addColumn(1, new GridColumn(GridColumn.ColType_Check));
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid列标题
		egu.getColumn("diancxxb_id").setHeader(Local.fahr);
		egu.getColumn("gongysb_id").setHeader(Local.gongysb_id_fahb);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("luncxxb_id").setHeader("轮船信息表");
		egu.getColumn("chec").setHeader("车次");
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("shr_diancxxb_id").setHeader("收货人");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("meil").setHeader("煤量");
		egu.getColumn("lurry").setHeader("录入人员");
		egu.getColumn("lurry").setHidden(true);
		egu.getColumn("lurry").setEditor(null);
		egu.getColumn("beiz").setHeader("备注");
		// 设置grid列宽度
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("luncxxb_id").setWidth(80);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("faz_id").setWidth(60);
		egu.getColumn("shr_diancxxb_id").setWidth(80);
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("meil").setWidth(80);
		egu.getColumn("lurry").setWidth(90);
		egu.getColumn("beiz").setWidth(90);
		// 工具栏里面的东西
		// 发货日期选择
		egu.addTbarText(Local.fahrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
		// 发货日期选择
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// 与html页中的id绑定,并自动刷新
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// 生成按钮
		GridButton Create = new GridButton("生成", "Create");
		Create.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(Create);
		// 修改按钮
		GridButton Update = new GridButton("修改", "Update");
		Update.setIcon(SysConstant.Btn_Icon_Save);
		egu.addTbarBtn(Update);

		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		String zhilbid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String caiybid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String caiylbid = getCaiylbid();
		String songjdwid = getJianydw();
		sql = "update fayslb set zhilb_id=" + zhilbid + " where id in("
				+ getChange() + ")";
		flag = con.getUpdate(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.UpdateDatabaseFail);
			con.rollBack();
			return;
		}
		sql = "insert into caiyb(id,zhilb_id,caiylbb_id,xuh,bianm,caiyrq,yangplb,"
				+ "yangpzl,songjdwb_id,jieyr,lury,beiz) values("
				+ caiybid
				+ ","
				+ zhilbid
				+ ","
				+ caiylbid
				+ ",0,'"
				+ getYangpbh()
				+ "',sysdate,'"
				+ getYangpfs()
				+ "',"
				+ getYangpzl()
				+ ","
				+ songjdwid
				+ ",'"
				+ getJieyr()
				+ "','"
				+ visit.getRenymc()
				+ "','" + getBeiz() + "')";
		flag = con.getInsert(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.InsertDatabaseFail);
			con.rollBack();
			return;
		}
		sql = "insert into zhillsb(id,caiyb_id, zhilb_id) values(getnewid("
				+ visit.getDiancxxb_id() + ")," + caiybid + "," + zhilbid + ")";
		flag = con.getInsert(sql);
		if (flag == -1) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.InsertDatabaseFail + "\nSQL:" + sql);
			setMsg(ErrorMessage.InsertDatabaseFail);
			con.rollBack();
			return;
		}
		con.commit();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	private String getCaiylbid() {
		String caiylbid = "0";
		JDBCcon con = new JDBCcon();
		String sql = "select id from caiylbb where mingc = '发运化验'";
		ResultSetList rs = con.getResultSetList(sql);
		if (rs.next()) {
			caiylbid = rs.getString("id");
		}
		rs.close();
		return caiylbid;
	}

	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			if (visit.getActivePageName().toString().equals("Faycyxg")) {
				setRiq(visit.getString7());
				setRiqe(visit.getString8());
			}
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			visit.setActivePageName(getPageName().toString());
			init();
		}
		visit.setString9("");
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
