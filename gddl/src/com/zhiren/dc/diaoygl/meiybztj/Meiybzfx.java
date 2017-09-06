package com.zhiren.dc.diaoygl.meiybztj;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meiybzfx extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	
	// 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

    // 复制昨日数据按钮
	private boolean _CopyClick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}

		if (_RefurbishChick) {
			_RefurbishChick = false;;
		}
		
		if (_CopyClick) {
			_CopyClick = false;
			copy();
		}
		getSelectData();
	}

	// 复制昨日数据
	public void copy() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql = "select * from meiybztjb where diancxxb_id = " + getTreeid() + 
					 "   and to_char(caiysj, 'yyyy-mm-dd') = '" + getRiqi() + "'";
		ResultSetList rsl = con.getResultSetList(sql);
		sql = "delete from meiybztjb where diancxxb_id = "+getTreeid()+" and to_char(caiysj,'yyyy-mm-dd') = '"+getRiqi()+"'";
		if (rsl.next()) {
			con.getDelete(sql);
		}

		String riq=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiqi()), -1, DateUtil.AddType_intDay));
		sql = "select * from meiybztjb where diancxxb_id = " + getTreeid() + " and to_char(caiysj,'yyyy-mm-dd') = '" +riq + "'";
		rsl = con.getResultSetList(sql);

		StringBuffer sb = new StringBuffer();
		sb.append("begin \n");
		while (rsl.next()) {
			sql = "insert into meiybztjb(id, diancxxb_id, meikxxb_id, caiysj, leib, proportion, qnet_ar, ad, mt, std,yuc_qnet_ar)\n"
					+ "values(getNewID("+ visit.getDiancxxb_id() + "), "
					+ getTreeid()
					+ ", "
					+ rsl.getLong("meikxxb_id")
					+ ", to_date('"
					+ getRiqi()
					+ "', 'yyyy-mm-dd'), '"
					+ rsl.getString("leib")
					+ "', "
					+ rsl.getDouble("proportion")
					+ ", "
					+ rsl.getLong("qnet_ar")
					+ ", "
					+ rsl.getDouble("ad")
					+ ", "
					+ rsl.getDouble("mt")
					+ ", "
					+ rsl.getDouble("std")
					+ ", "
					+ rsl.getDouble("yuc_qnet_ar")
					+ ");";
			sb.append(sql);
		}
		sb.append("end;");

		if (sb.length() < 13) {
			con.Close();
		} else {
			con.getInsert(sb.toString());
		}
		rsl.close();
		con.Close();
	}
	
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		
		String sql = "select tj.id, tj.diancxxb_id, mk.mingc as meikxxb_id, caiysj, tj.leib, proportion, qnet_ar, ad, mt, std,yuc_qnet_ar " +
				     "  from meiybztjb tj, meikxxb mk " +
				     " where to_char(caiysj, 'yyyy-mm-dd') = '" + getRiqi() + 
		             "'  and tj.meikxxb_id = mk.id" +
		             "   and diancxxb_id = " + getTreeid();

		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("meiybztjb");
		egu.setWidth("bodyWidth");

		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(getTreeid());
		egu.getColumn("meikxxb_id").setHeader("煤矿");
        // 煤矿下拉框
		ComboBox meik = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meik);
		meik.setEditable(false);
		sql = "select id, mingc from meikxxb order by id";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("caiysj").setHeader("采样时间");
		egu.getColumn("caiysj").setDefaultValue(getRiqi());
		egu.getColumn("leib").setHeader("类别");
		egu.getColumn("proportion").setHeader("比重");
		egu.getColumn("qnet_ar").setHeader("低位发热量（K/g)");
		egu.getColumn("ad").setHeader("灰分");
		egu.getColumn("mt").setHeader("水分");
		egu.getColumn("std").setHeader("硫分");
		egu.getColumn("yuc_qnet_ar").setHeader("预测发热量(K/g)");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

        // 日期
		egu.addTbarText("采样时间:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		egu.addTbarText("-");
		
		// 电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");

		GridButton refurbish = new GridButton("刷新",
				"function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save,"SaveButton");
        // 复制昨日数据按钮
		GridButton CopyButton = new GridButton("复制昨日数据",
				getBtnHandlerScript("CopyButton"));
		egu.addTbarBtn(CopyButton);
		
		setExtGrid(egu);
		con.Close();
	}

	public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getRiqi();
		cnDate = cnDate.substring(0, 4) + "年" + cnDate.substring(5, 7) + "月" + cnDate.substring(8, 10);
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("CopyButton")) {
			btnsb.append("新生成数据将覆盖").append(cnDate).append("的已存数据，是否继续？");
		} else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			
		}
		getSelectData();
	}

	boolean treechange = false;

	private String treeid = "";

	public String getTreeid() {

		if (treeid == null || treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}

	public void setTreeid(String treeid) {

		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
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

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
}
