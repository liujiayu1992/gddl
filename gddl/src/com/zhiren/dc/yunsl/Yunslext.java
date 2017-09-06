package com.zhiren.dc.yunsl;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yunslext extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		String sql = "begin \n";
		while(rs.next()){
			sql += "delete from yunslb where id = "+ rs.getString("id")+";\n";
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getDelete(sql);
		}
		rs.close();
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		while(rs.next()){
			if(rs.getInt("id") == 0){
				sql += "insert into yunslb(id,diancxxb_id,pinzb_id,yunsfsb_id,meikxxb_id,yunsl) values(getnewId("
				+v.getDiancxxb_id() + "),"+v.getDiancxxb_id() + "," 
				+ getExtGrid().getColumn("pinzb_id").combo.getBeanId(rs.getString("pinzb_id")) +","
				+ getExtGrid().getColumn("yunsfsb_id").combo.getBeanId(rs.getString("yunsfsb_id")) +","
				+ getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rs.getString("meikxxb_id")) +","
				+ CustomMaths.div(rs.getDouble("yunsl"), 100.0) + ");\n";
			}else{
				sql += "update yunslb set pinzb_id=" 
					+getExtGrid().getColumn("pinzb_id").combo.getBeanId(rs.getString("pinzb_id")) 
					+",yunsfsb_id ="
					+getExtGrid().getColumn("yunsfsb_id").combo.getBeanId(rs.getString("yunsfsb_id")) 
					+",meikxxb_id ="
					+getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rs.getString("meikxxb_id")) 
					+",yunsl=" + CustomMaths.div(rs.getDouble("yunsl"), 100.0) 
					+" where id="+rs.getString("id") + " ;\n";
			}
		}
		if(rs.getRows()>0){
			sql += "end;";
			con.getInsert(sql);
		}
		rs.close();
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con
				.getResultSetList("SELECT ysl.ID,ysl.diancxxb_id, pz.mingc AS pinzb_id, sf.mingc AS yunsfsb_id,\n"
						+ "       xx.mingc AS meikxxb_id, (yunsl*100) as yunsl\n"
						+ "  FROM yunslb ysl, pinzb pz, yunsfsb sf, meikxxb xx, diancxxb dc\n"
						+ " WHERE ysl.diancxxb_id = dc.ID\n"
						+ "   AND ysl.pinzb_id = pz.ID\n"
						+ "   AND ysl.yunsfsb_id = sf.ID\n"
						+ "   AND ysl.meikxxb_id = xx.ID\n"
						+ "   AND dc.ID = "
						+ visit.getDiancxxb_id() + "");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("yunslb");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("meikxxb_id").setWidth(220);
		egu.getColumn("yunsl").setHeader("运损率(%)");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("yunsl").defaultvalue = "1.2";
		((NumberField) egu.getColumn("yunsl").editor).setDecimalPrecision(1);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(18);
		String sql = "select id from diancxxb where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		String diancid = "";
		try {
			while (rs.next()) {
				diancid = rs.getString("id");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		egu.getColumn("diancxxb_id").setDefaultValue(diancid);
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu
				.getColumn("pinzb_id")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(
								"select id,mingc from pinzb where zhuangt = 1 order by xuh,mingc"));
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(
				egu.gridId,
				new IDropDownModel(
						"select id,mingc from yunsfsb order by mingc"));
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu
				.getColumn("meikxxb_id")
				.setComboEditor(
						egu.gridId,
						new IDropDownModel(

								"SELECT mk.id,mk.quanc\n"
										+ "  FROM gongysdcglb cgl, gongysb gys, gongysmkglb kgl, meikxxb mk, diancxxb dc\n"
										+ " WHERE gys.ID = cgl.gongysb_id\n"
										+ "   AND kgl.gongysb_id = gys.ID\n"
										+ "   AND mk.ID = kgl.meikxxb_id\n"
										+ "   AND dc.ID = cgl.diancxxb_id\n"
										+ "   AND dc.ID = "
										+ visit.getDiancxxb_id() + ""));
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
				+ "var end = url.indexOf(';');"
				+ "url = url.substring(0,end);"
				+ "url = url + '?service=page/' + 'Yunslreport&lx=rezc';"
				+ " window.open(url,'newWin');";
		egu.addToolbarItem("{"
				+ new GridButton("打印", "function (){" + str + "}").getScript()
				+ "}");
//		将运损率除以100的方法放在后台进行处理
//		egu
//				.addOtherScript("function gridDiv_save(record){record.set('YUNSL',Round(record.get('YUNSL')/100,4))};");
		egu
				.addOtherScript("function Round(Num , scale){ "
						+ " var floorNum = Math.floor(Num * Math.pow(10,scale ));"
						+ " var floorNum1 = Math.floor(Num * Math.pow(10,scale -1))*10;"
						+ " var floorNum2 = Math.floor(Num * Math.pow(10,scale +1));"
						+ " var BitNum = floorNum - floorNum1;"
						+ " var scaleNum = floorNum2 - floorNum*10;"
						+ " return Math.round(Num * Math.pow(10,scale))/Math.pow(10,scale);} ");
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
			getSelectData();
		}
	}
}
