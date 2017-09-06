package com.zhiren.jingjfx;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王磊
 * 维护年指标数据
 *
 */
public class Nianzbwh extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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

	private void Save() {
		Visit visit = (Visit)this.getPage().getVisit();
		getExtGrid().Save(getChange(), visit);
	}
 
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb.append("select * from jjfxnzbwh where riq = to_date('")
		.append(getYearValue().getId())
		.append("-01-01','yyyy-mm-dd')")
		.append(" and diancxxb_id="+this.getTreeid());
		if(!con.getHasIt(sb.toString())) {
			StringBuffer Isql = new StringBuffer();  
			Isql.append("insert into jjfxnzbwh\n")
			.append("(id, diancxxb_id, riq) values(getnewId(").append(visit.getDiancxxb_id()).append("),")
			.append(visit.getDiancxxb_id()).append(",to_date('").append(getYearValue().getId()).append("-01-01','yyyy-mm-dd'))");
			con.getInsert(Isql.toString());
		}
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setTableName("jjfxnzbwh");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").editor = null;
		egu.getColumn("fadl").setHeader(Locale.fadl);
		egu.getColumn("ructrml").setHeader("入厂天然煤量");
		egu.getColumn("haoytrml").setHeader("耗用天然煤量");
		egu.getColumn("cunml").setHeader("存煤量");
		egu.getColumn("rucyl").setHeader("入厂油量");
		egu.getColumn("haoyyl").setHeader("耗用油量");
		egu.getColumn("cunyl").setHeader("存油量");
		egu.getColumn("rucmrz").setHeader("入厂煤热值");
		egu.getColumn("rulmrz").setHeader("入炉煤热值");
		egu.getColumn("rezc").setHeader("热值差");
		egu.getColumn("ructrmj").setHeader("入厂天然煤价");
		egu.getColumn("rucbmdj").setHeader("入厂标煤单价");
		egu.getColumn("rulmzbmdj").setHeader("入炉煤折标煤单价");
		egu.getColumn("rulzhbmdj").setHeader("入炉综合标煤单价");
		egu.getColumn("rulmtcb").setHeader("入炉煤炭成本");
		egu.getColumn("fadycb").setHeader("发电用成本");
		egu.getColumn("ranlzcb").setHeader("燃料总成本");
		egu.getColumn("ructrmdjws").setHeader("入厂天然煤（未税）");
		egu.getColumn("rucbmdjws").setHeader("入厂标煤单价（未税）");
		egu.getColumn("rulmzbmdjws").setHeader("入炉煤折标煤单价（未税）");
		egu.getColumn("rulzhbmdjws").setHeader("入炉综合标煤单价（未税）");
		egu.getColumn("rulmtcbws").setHeader("入炉煤炭成本（未税）");
		egu.getColumn("rulycbws").setHeader("入炉油成本（未税）");
		egu.getColumn("ranlzcbws").setHeader("燃料总成本（未税）");
		egu.getColumn("faddwrlcb").setHeader("发电单位燃料还能");
		egu.getColumn("jiesml").setHeader("结算煤量");
		egu.getColumn("meitjsfkje").setHeader("煤炭结算付款金额");
		
		egu.addTbarText("年:");
		ComboBox sec=new ComboBox();
		sec.setWidth(60);
		sec.setTransform("YEAR");
		sec.setEditable(true);
		egu.addToolbarItem(sec.getScript());
//		添加电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		this.setTree(etu);
		
		
		
	
		
		
		egu.addTbarTreeBtn("diancTree");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新 '+Ext.getDom('YEAR').value+' 年的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");

		
		
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
//	 设置秒下拉框
	public IDropDownBean getYearValue() {
		if (((Visit)this.getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getYearModel().getOptionCount(); i++) {
				Object obj = getYearModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					setYearValue((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean3();
	}

	public void setYearValue(IDropDownBean Value) {
		((Visit)this.getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getYearModel() {
		if (((Visit)this.getPage().getVisit()).getProSelectionModel3() == null) {
			getYearModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel3();
	}
	
	public void setYearModel(IPropertySelectionModel _value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel3(_value);
	}

	

	public void getYearModels() {
		List listMin = new ArrayList();
		for (int i = 0; i < 60; i++) {
			if(i<10)
				listMin.add(new IDropDownBean(i, "0"+i));
			else
				listMin.add(new IDropDownBean(i, String.valueOf(i)));
		}
		setYearModel(new IDropDownModel("select yvalue,ylabel from nianfb where to_char(sysdate,'yyyy')+1 >= yvalue"));
	}
//	电厂树相关_开始
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
		if (!((Visit) getPage().getVisit()).getString2().equals(treeid)) {
		((Visit) getPage().getVisit()).setString2(treeid);
		}
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
//
//	public String getTreeScript() {
//		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
//	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
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
//	电厂树相关_结束
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
			this.setTreeid(null);
			visit.setList1(null);
			visit.setString1(null);
			setYearValue(null);
			setYearModel(null);
			getYearModels();
			
			
		}
		getSelectData();
	}
}