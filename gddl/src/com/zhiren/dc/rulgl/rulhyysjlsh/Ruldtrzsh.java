package com.zhiren.dc.rulgl.rulhyysjlsh;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Judge;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Ruldtrzsh extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 绑定日期
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

	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

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
		Update(getChange(), visit);
		Save1(getChange(), visit);
		Update1(getChange(), visit);

	}

	public void Save1(String strchange, Visit visit) {
		String tableName = "rulmzlb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList delrsl = visit.getExtGrid1()
				.getDeleteResultSet(strchange);
		while (delrsl.next()) {
			sql.append("delete from ").append(tableName).append(" where id =")
					.append(delrsl.getString(0)).append(";\n");
		}

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();//页面上没有的字段
			StringBuffer sql4 = new StringBuffer();//页面上没有的字段的内容
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();
			StringBuffer sql7 = new StringBuffer();
			StringBuffer sql8 = new StringBuffer();
			StringBuffer sql9 = new StringBuffer();
			StringBuffer sql10 = new StringBuffer();
			StringBuffer sql11 = new StringBuffer();
			StringBuffer sql12 = new StringBuffer();

			sql2.append(mdrsl.getString("ZHILLSB_ID"));

			String rulid = "select id from rulmzlb where id="
					+ mdrsl.getString("ZHILLSB_ID");
			ResultSetList rsl = con.getResultSetList(rulid);
			if (rsl.getRows() == 0) {
				sql.append("insert into ").append(tableName).append("(id");
				sql3.append(",diancxxb_id");//可变
				sql4.append(",").append(visit.getDiancxxb_id());
				sql5.append(",").append("qnet_ar");
				sql6.append(",").append(mdrsl.getString("ZHI"));
				sql7.append(",").append("fenxrq");
				sql8.append(",").append(
						"to_date('" + mdrsl.getString("FENXRQ") + "','"
								+ "yyyy-mm-dd')");
				sql9.append(",rulrq");
				sql10.append(",").append(
						"to_date('" + mdrsl.getString("RULRQ") + "','"
								+ "yyyy-mm-dd')");
				sql11.append(",bianm,rulbzb_id,JIZFZB_ID,LURSJ,shenhzt");
				sql12.append(",").append(mdrsl.getString("BIANM")).append(",0")
						.append(",0,").append(
								"to_date('" + mdrsl.getString("FENXRQ") + "','"
										+ "yyyy-mm-dd'),").append("1");

				sql.append(sql3).append(sql5).append(sql7).append(sql9).append(
						sql11).append(") values(").append(sql2).append(sql4)
						.append(sql6).append(sql8).append(sql10).append(sql12)
						.append(");\n");
			} else {
				sql.append("update ").append(tableName).append(" set ");

				sql.append("QNET_AR").append(" = ");

				sql.append(mdrsl.getString("ZHI")).append(",");

				sql.deleteCharAt(sql.length() - 1);
				sql.append(" where id =").append(mdrsl.getString("ZHILLSB_ID"))
						.append(";\n");
			}
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
	}

	public void Update(String strchange, Visit visit) {

		String tableName = "danthyb";

		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();

			sql2.append(mdrsl.getString("ZHILLSB_ID"));

			sql.append("update ").append(tableName).append(" set ");

			sql.append("SHENHZT").append(" = ");

			sql.append("1").append(",");

			sql.deleteCharAt(sql.length() - 1);
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");

		}

		sql.append("end;");
		con.getUpdate(sql.toString());

	}

	public void Update1(String strchange, Visit visit) {

		String tableName = "danthyb";

		JDBCcon con = new JDBCcon();

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);

		while (mdrsl.next()) {
			StringBuffer sql2 = new StringBuffer();
			String gyid = "select id from danthyb where zhillsb_id="
					+ mdrsl.getString("ZHILLSB_ID") + " and id<>"
					+ mdrsl.getString("ID") + " and fenxxmb_id=5";
			ResultSetList rsl = con.getResultSetList(gyid);

			sql2.append(mdrsl.getString("ZHILLSB_ID"));
			if (rsl.getRows() != 0) {

				StringBuffer sql = new StringBuffer("begin \n");
				for (int i = 0; i <= rsl.getRows(); i++) {
					rsl.next();
					sql.append("update ").append(tableName).append(" set ");

					sql.append("SHENHZT").append(" = ");

					sql.append("-1").append(",");

					sql.deleteCharAt(sql.length() - 1);
					sql.append(" where id =").append(rsl.getLong("id")).append(
							";\n");
				}

				sql.append("end;");
				con.getUpdate(sql.toString());
			}
		}
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
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return value == null || "".equals(value) ? "null" : value;
			}
			//			return value==null||"".equals(value)?"null":value;
		} else {
			return value;
		}
	}
	private void Auto(){
		Visit visit = (Visit) this.getPage().getVisit();
		AutoFix(getChange(), visit);
	}

	private void AutoFix(String strchange, Visit visit) {
		
		JDBCcon con = new JDBCcon();
		//int size = getEditValues().size();

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
	
		int size=mdrsl.getRows();
		double[] array = new double[size];
		
		for (int i = 0; i < size; i++) {
			mdrsl.next();
			
			if (mdrsl.getString("SHENHZT").equals("1")) {
				setMsg("已结束数据未能进行审核操作!");
				return;
			}
			array[i] =mdrsl.getDouble("ZHI");
			}
		
		Judge ju = new Judge();
		double[] dbarrjudge = ju.getJudgeData(Judge.T_TYPE_Qbad, array);
		if(dbarrjudge==null){
			return;
		}
		for (int j = 0; j < dbarrjudge.length; j++) {
		
			for (int i = 0; i < mdrsl.getRows(); i++) {
				mdrsl.next();
			if ( mdrsl.getDouble("ZHI") == dbarrjudge[j]) {
					String sql = "update danthyb set shenhzt=1 where id = "
							+ mdrsl.getString("ID");

			
					mdrsl.Remove(i);
					
					con.getUpdate(sql);

					break;
				}
			}
		}
		for(int i=0;i<mdrsl.getRows();i++){
			mdrsl.next();
			String sql1 = "select id from danthyb where zhillsb_id="
				+ mdrsl.getString("ZHILLSB_ID") + " and id<>"
				+ mdrsl.getString("ID");
		ResultSetList rsl = con.getResultSetList(sql1);
		rsl.next();
		String sql3 = "update danthyb set shenhzt=-1 where id = "
			+ rsl.getLong("ID");
		con.getUpdate(sql3);
		
		break;
		}
		String rulid = "select id from rulmzlb where id="
			+ mdrsl.getString("ZHILLSB_ID");
	ResultSetList rsl = con.getResultSetList(rulid);
	
		for (int i=0;i<mdrsl.getRows();i++){
			mdrsl.next();
			if(rsl.getRows()==0){
			String sql2="inster into rulmzlb (id,diancxxb_id,qbad,fenxrq,rulrq,bianm,rulbzb_id,JIZFZB_ID,LURSJ,shenhzt)values(" +
					mdrsl.getString("ZHILLSB_ID")+","+visit.getDiancxxb_id()+","+mdrsl.getString("ZHI")+","+mdrsl.getString("FENXRQ")+","+
					mdrsl.getString("RULRQ")+","+mdrsl.getString("BIANM")+"0,0"+mdrsl.getString("FENXRQ")+"1)";	
			con.getUpdate(sql2);
	
	
		}else{
			String sql2="update zhillsb set qbad="+mdrsl.getDouble("ZHI")+",shenhzt=1 where id="+mdrsl.getString("ZHILLSB_ID");	
			con.getUpdate(sql2);
		}
		
	}
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _AutoChick = false;

	public void AutoButton(IRequestCycle cycle) {
		_AutoChick = true;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if(_AutoChick){
			_AutoChick=false;
			Auto();
			getSelectData();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}

	}

	public void getSelectData() {
		// Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = "select distinct g.id, g.bianm,g.rulrq,g.fenxrq,g.xuh,g.qimmyzl,g.qimzl,g.meiyzl,\n"
				+ "g.zhi,g.lury,g.zhillsb_id,g.shenhzt\n"
				+ " from danthyb g\n"
				+ " where fenxxmb_id=5 and shenhzt=0"
				+ " and g.bianm='"
				+ getHuaybhValue().getValue() + "'";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		egu.getColumn("id").setHidden(true);
		egu.getColumn("zhillsb_id").setHidden(true);
		egu.getColumn("shenhzt").setHidden(true);
		egu.getColumn("rulrq").setHeader("入炉日期");
		egu.getColumn("bianm").setHeader("入炉编号");
		egu.getColumn("fenxrq").setHeader("分析日期");
		egu.getColumn("lury").setHeader("录入人员");
		egu.getColumn("bianm").setHeader("入炉编号");
		egu.getColumn("xuh").setHeader("检验次数");
		egu.getColumn("qimmyzl").setHeader("称量瓶（盘）试样重量");
		egu.getColumn("qimzl").setHeader("称量瓶（盘）重量");
		egu.getColumn("meiyzl").setHeader("试样重量");

		egu.getColumn("zhi").setHeader("发热量");

		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.addPaging(25);
		egu.setWidth(1000);


		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df.Binding("RIQ2", "forms[0]");// 与html页中的id绑定,并自动刷新
		df.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("化验编号");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("HuaybhDropDown");
		comb4.setId("Huaybh");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(130);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());

		egu.addTbarText("-");

		egu
				.addToolbarItem("{"
						+ new GridButton("刷新",
								"function(){document.getElementById('RefurbishButton').click();}")
								.getScript() + "}");
		egu.addTbarText("-");
		egu.addToolbarButton("审核", GridButton.ButtonType_Sel, "SaveButton",
				null, SysConstant.Btn_Icon_Show);
		egu.addToolbarButton("自动审核", GridButton.ButtonType_SaveAll, "AutoButton",
				null, SysConstant.Btn_Icon_Show);
		setExtGrid(egu);

		con.Close();
	}

	//化验编码

	public IDropDownBean getHuaybhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getHuaybhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setHuaybhValue(IDropDownBean value) {

		if (((Visit) getPage().getVisit()).getDropDownBean10() != value) {

			((Visit) getPage().getVisit()).setDropDownBean10(value);
		}
	}

	public void setHuaybhModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getHuaybhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {

			getHuaybhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public IPropertySelectionModel getHuaybhModels() {

		String sql = "select rownum,bianm from (select distinct bianm from danthyb "
				+ "where rulrq>=to_date('"
				+ this.getRiqi()
				+ "','yyyy-mm-dd')and rulrq<=to_date('"
				+ this.getRiq2()
				+ "',\n" + "'yyyy-mm-dd') and fenxxmb_id=5)";

		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, "请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
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

			getHuaybhValue();
			setHuaybhValue(null);
			getHuaybhModels();
			setRiqi(null);
			setRiq2(null);

		}
		if (riqichange) {
			riqichange = false;
			getHuaybhModels();
		}
		if (riq2change) {
			riq2change = false;
			getHuaybhModels();
		}

		getSelectData();
	}

}