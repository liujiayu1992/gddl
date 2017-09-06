package com.zhiren.jingjfx;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王磊
 * 增加综合分析项
 *
 */
public class Gongssz extends BasePage implements PageValidateListener {
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
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshButton = false;
	
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshButton = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshButton) {
			_RefreshButton = false;
			getSelectData();
		}
	}

	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb.append("select * from jjfxdxfxszb ");
//		if(!con.getHasIt(sb.toString())) {
//			sb.delete(0, sb.length());
//			sb.append("insert into jjfxdxfxszb(id,diancxxb_id,jjfxdxmk_id) ")
//			.append("(select getnewid(").append(visit.getDiancxxb_id())
//			.append("),").append(visit.getDiancxxb_id())
//			.append(",id from jjfxdxmk)");
//			con.getInsert(sb.toString());
//		}
		sb.delete(0, sb.length());
		sb.append("select d.id, d.diancxxb_id, m.mingc jjfxdxmk_id,\n")
		  .append("       g.mingc gongsb_id, z.z_name zidyfa_id \n")
		  .append("  from jjfxdxfxszb d,jjfxdxmk m,gongsb g,zidyfa z\n")
		  .append(" where d.jjfxdxmk_id = m.id and d.gongsb_id = g.id\n")
		  .append("   and d.zidyfa_id = z.id\n");
		  //.append("   and d.diancxxb_id = ").append(visit.getDiancxxb_id());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.pagsize = 0;
		egu.setTableName("jjfxdxfxszb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(visit.getDiancxxb_id()));
		egu.getColumn("jjfxdxmk_id").setHeader("分析项目");
		egu.getColumn("jjfxdxmk_id").setWidth(200);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("gongsb_id").setHeader("文字说明");
		egu.getColumn("gongsb_id").setWidth(200);
		egu.getColumn("zidyfa_id").setHeader("方案");
		egu.getColumn("zidyfa_id").setWidth(200);
		
		ComboBox d = new ComboBox();
		egu.getColumn("jjfxdxmk_id").setEditor(d);
		d.setEditable(false);
		String sql = "select id,mingc from jjfxdxmk";
		egu.getColumn("jjfxdxmk_id").setComboEditor(egu.gridId,new IDropDownModel(sql));
		
		ComboBox r = new ComboBox();
		egu.getColumn("zidyfa_id").setEditor(r);
		r.setEditable(true);
		sql = "select id,z_name from zidyfa where z_remark ='经济分析'";
		egu.getColumn("zidyfa_id").setComboEditor(egu.gridId,new IDropDownModel(sql));
		
		ComboBox danx = new ComboBox();
		egu.getColumn("gongsb_id").setEditor(danx);
		danx.setEditable(true);
		sql = "select id,mingc from gongsb where leix = '经济分析' " +
				"and zhuangt =1";
				//" and diancxxb_id = " + visit.getDiancxxb_id();
		egu.getColumn("gongsb_id").setComboEditor(egu.gridId,new IDropDownModel(sql));
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
}