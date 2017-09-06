package com.zhiren.gangkjy.zhilgl.caiyxx;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhuangccyxg extends BasePage implements PageValidateListener {
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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	 页面刷新日期（卸煤日期）
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	 页面刷新日期（卸煤日期）
	private String riqe;

	public String getRiqe() {
		return riqe;
	}
	public void setRiqe(String riqe) {
		this.riqe = riqe;
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
	
	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _ReturnClick = false;

	public void ReturnButton(IRequestCycle cycle) {
		_ReturnClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_ReturnClick) {
			_ReturnClick = false;
			((Visit) getPage().getVisit()).setString7(getRiq());
			((Visit) getPage().getVisit()).setString8(getRiqe());
			cycle.activate(((Visit) getPage().getVisit()).getString9());
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
//		 卸煤日期的ora字符串格式
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		String str = "";
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + visit.getDiancxxb_id() + " or dc.fuid = "
						+ visit.getDiancxxb_id() + ")";
			} else {
				str = "and dc.id = " + visit.getDiancxxb_id() + "";
			}
		} 
		JDBCcon con = new JDBCcon();
		String sql = 
			"select c.id, z.zhilb_id,l.huaybh,to_char(z.zhuangcjssj,'yyyy-mm-dd') zhuangcsj,\n" +
			"       x.mingc jiehr ,p.mingc pinz ,c.bianm bianm,c.yangplb,c.yangpzl,\n" + 
			"       s.mingc jydw,c.jieyr,b.mingc caiylb ,c.beiz\n" + 
			"       from caiyb c,\n" + 
			"       (select t.id, t.mingc\n" + 
			"          from item t, itemsort s\n" + 
			"         where t.itemsortid = s.id\n" + 
			"           and s.mingc = '质量检验单位') s,\n" + 
			"       zhuangcb z,\n" + 
			"       zhillsb l,\n" + 
			"       vwpinz p,\n" + 
			"       caiylbb b,\n" + 
			"       vwxuqdw x,\n" + 
			"       diancxxb dc \n" + 
			"       where z.diancxxb_id= dc.id \n" + 
			"             and z.zhilb_id=l.zhilb_id\n" + 
			"             and b.mingc='装船化验'\n" + 
			"             and b.id=c.caiylbb_id\n" + 
			"             and c.songjdwb_id=s.id(+)\n" + 
			"             and c.id=l.caiyb_id\n" + 
			str+" \n"+
			"             and z.zhuangcjssj >= "+strxmrqOra+"\n" + 
			"             and z.zhuangcjssj <"+strxmrqeOra+"+ 1\n" + 
			"             and x.id=z.xiaosgysb_id\n" + 
			"             and p.id=z.pinzb_id";

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
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid为单选
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
//		设置grid隐藏列
		egu.getColumn("id").setHidden(true);
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("huaybh").setHidden(true);
		// 设置grid列标题
		egu.getColumn("zhuangcsj").setHeader(Local.zhuangcsj_zhuangcb);
		egu.getColumn("jiehr").setHeader(Local.shouhr_jies);
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("bianm").setHeader(Local.cybm);
		egu.getColumn("yangplb").setHeader(Local.yangplb);
		egu.getColumn("yangpzl").setHeader(Local.yangpzl);
		egu.getColumn("jydw").setHeader(Local.songjdw);
		egu.getColumn("jieyr").setHeader(Local.jieyr);
		egu.getColumn("caiylb").setHeader(Local.caiylb);
		egu.getColumn("beiz").setHeader(Local.beiz);
//		设置列宽度
		egu.getColumn("zhuangcsj").setWidth(80);
		egu.getColumn("jiehr").setWidth(100);
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("bianm").setWidth(80);
		egu.getColumn("yangplb").setWidth(60);
		egu.getColumn("yangpzl").setWidth(80);
		egu.getColumn("jydw").setWidth(140);
		egu.getColumn("jieyr").setWidth(60);
		egu.getColumn("caiylb").setWidth(60);
		egu.getColumn("beiz").setWidth(180);
//		设定grid列可否编辑
		egu.getColumn("zhuangcsj").setEditor(null);
		egu.getColumn("jiehr").setEditor(null);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("caiylb").setEditor(null);
		
		// 数据列下拉框设置
//		 接卸日期选择
		egu.addTbarText(Local.jiexrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 接卸日期选择
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// 与html页中的id绑定,并自动刷新
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		
//		 采样方式
		ComboBox caiyfs = new ComboBox();
		egu.getColumn("yangplb").setEditor(caiyfs);
		caiyfs.setEditable(true);
		List h = new ArrayList();
		h.add(new IDropDownBean("人工","人工"));
		h.add(new IDropDownBean("机械","机械"));
		h.add(new IDropDownBean("混合","混合"));
		egu.getColumn("yangplb")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		
		// 送检单位
		ComboBox songjdw = new ComboBox();
		egu.getColumn("jydw").setEditor(songjdw);
		songjdw.setEditable(true);
		sql = "select t.id,t.mingc from item t, itemsort s\n" +
		"where t.itemsortid = s.id and s.mingc = '质量检验单位'";
		egu.getColumn("jydw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
//		 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// 删除按钮
		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(delete);
		// 保存按钮
		GridButton save = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(save);
		
//		 详细过衡按钮
		GridButton Return = new GridButton("返回", "Returnfun");
		Return.setIcon(SysConstant.Btn_Icon_Return);
		egu.addTbarBtn(Return);

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
		// 删除数据
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		sql = "begin \n";
		String _msg = "";
		while (rs.next()) {
			String zhilbid = rs.getString("zhilb_id");
			String caiybid = rs.getString("id");
			String huaybh = rs.getString("huaybh");
			String caiybm = rs.getString("bianm");
			if(huaybh != null && !"".equals(huaybh)){
				_msg += "采样 " + caiybm + " 删除失败;(已有化验值)";
				continue;
			}
			sql += "delete from zhillsb where zhilb_id =" + zhilbid + ";\n";
			sql += "update zhuangcb set zhilb_id = 0 where zhilb_id =" + zhilbid + ";\n";
			sql += "delete from zhilb where id =" + zhilbid + ";\n";
			sql += "delete from caiyb where id =" + caiybid + ";\n";
		}
		sql += "end;\n";
		if (rs.getRows() > 0 && sql.length() > 14) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		// 修改数据
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			String caiybid = rs.getString("id");
			String caiybm = rs.getString("bianm");
			String yangplb = getExtGrid().getColumn("yangplb").combo.getBeanStrId(rs
					.getString("yangplb"));
			String songjdw = getExtGrid().getColumn("jydw").combo.getBeanStrId(rs
					.getString("jydw"));
			double yangpzl = rs.getDouble("yangpzl");
			String jieyr = rs.getString("jieyr");
			String beiz = rs.getString("beiz");
			
			sql += "update caiyb set \n" + " yangplb = '" + yangplb
					+ "',\n" + " yangpzl = " + yangpzl + ",\n"
					+ " bianm = '" + caiybm + "',\n" + " songjdwb_id = "
					+ songjdw + ",\n" + " jieyr = '" + jieyr+ "',\n" 
					+ " lury = '" + visit.getRenymc()+ "',\n" 
					+ " beiz = '" + beiz + "'\n"
					+ " where id=" + caiybid + ";\n";
		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		con.commit();
		con.Close();
		if("".equals(_msg)){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}else{
			setMsg(_msg);
		}
		
	}

	private void init() {
		setExtGrid(null);
		initGrid();
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if(visit.getActivePageName().toString().equals(
					"Zhuangccy")){
				setRiq(visit.getString7());
				setRiqe(visit.getString8());
			}
			visit.setActivePageName(getPageName().toString());
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if (getRiqe() == null) {
				setRiqe(DateUtil.FormatDate(new Date()));
			}
			init();
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