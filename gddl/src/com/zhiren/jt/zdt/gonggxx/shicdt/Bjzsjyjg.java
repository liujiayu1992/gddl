package com.zhiren.jt.zdt.gonggxx.shicdt;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Bjzsjyjg extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
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
		Visit visit = (Visit) this.getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sql = new StringBuffer("begin \n");
		String tableName="bjzsjyjgb";
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			String id=mdrsl.getString("id");
			String item_id=mdrsl.getString("item_id");
			String RIQ=DateUtil.FormatOracleDate(getEndRiq());
			String jiaoysj=DateUtil.FormatOracleDate(mdrsl.getString("jiaoysj"));
			String jiaoyjg=mdrsl.getString("jiaoyjg");
			String zhis=mdrsl.getString("zhis");
			
			if ("0".equals(id)) {
				id="getnewid("+visit.getDiancxxb_id()+")";
				sql.append("insert into ").append(tableName).append("(id,item_id,riq,jiaoysj,jiaoyjg,zhis");
				sql.append(") values("+id+","+item_id+","+RIQ+","+jiaoysj+","+jiaoyjg+","+zhis+");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");
				sql.append(" jiaoysj="+jiaoysj+", jiaoyjg="+jiaoyjg+", zhis="+zhis);
				sql.append(" where id =").append(id).append(";\n");
			}
		}
		mdrsl.close();
		sql.append("end;");
		int flag = con.getUpdate(sql.toString());
		if (flag>-1){
			setMsg("保存成功！");
			con.commit();
		}else{
			setMsg("保存失败！");
		}
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			System.out.print(getEndRiq());
	
		}
	}
	

//绑定结束日期
	private String endRiq;
	public String getEndRiq() {
		return endRiq;
	}
	public void setEndRiq(String riq) {
		this.endRiq = riq;
	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
		String sql=	"select nvl(s.id,0) id ,nvl(s.item_id,i.id) item_id , i.mingc mingc,\n" +
			"nvl(s.jiaoysj,"+DateUtil.FormatOracleDate(getEndRiq())+")jiaoysj ,s.jiaoyjg,s.zhis\n"+
			"from (select * from bjzsjyjgb where riq="+DateUtil.FormatOracleDate(getEndRiq()) +") s, item i, itemsort i2\n" + 
			"where i2.id=i.itemsortid and i2.mingc='BJ指数交易价格' and i.id=s.item_id(+)\n"+
			 " order by i.xuh";
//		System.out.print(sql);
		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("bjzsjyjgb");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor = null;
		egu.getColumn("item_id").setHeader("item_id");
		egu.getColumn("item_id").setHidden(true);
		egu.getColumn("item_id").editor = null;
		
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("mingc").editor = null;
		
		egu.getColumn("jiaoysj").setHeader("交易时间");
		egu.getColumn("jiaoysj").setWidth(100);
		
		egu.getColumn("jiaoyjg").setHeader("交易价格(元/吨)");
		egu.getColumn("jiaoyjg").setWidth(100);
		((NumberField)egu.getColumn("jiaoyjg").editor).setDecimalPrecision(0);
		
		egu.getColumn("zhis").setHeader("指数");
		egu.getColumn("zhis").setWidth(70);
//		((NumberField)egu.getColumn("zhis").editor).setDecimalPrecision(3);
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);

		egu.addTbarText("日期:");
		
		DateField dEnd = new DateField();
		dEnd.Binding("ENDRIQ","Form0");
		dEnd.setValue(getEndRiq());
		dEnd.setId("Endriq");
		egu.addToolbarItem(dEnd.getScript());
		egu.addTbarText("-");
		
		GridButton gRefresh = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
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
			setEndRiq(DateUtil.FormatDate(new Date()));
		}
		getSelectData();
	}
}
