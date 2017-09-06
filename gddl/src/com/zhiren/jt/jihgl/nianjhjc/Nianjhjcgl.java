package com.zhiren.jt.jihgl.nianjhjc;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Nianjhjcgl extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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
		StringBuffer sbuffer = new StringBuffer();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int flag =0;
		ResultSetList rsl;
		
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while (rsl.next()) {
			sbuffer.delete(0, sbuffer.length());
			sbuffer.append("delete from nianjhjcglb where id = " + rsl.getString("id")+"\n");
			flag = con.getDelete(sbuffer.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + sbuffer);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
		}
		try {
			rsl = getExtGrid().getModifyResultSet(getChange());
			//保存更新或新增
			while (rsl.next()){
				sbuffer.delete(0, sbuffer.length());
				if ("0".equals(rsl.getString("id"))) {
						sbuffer.append(
								"INSERT INTO NIANJHJCGLB\n" +
								"  (ID, NIANJHJCB_ID, GONGYSB_ID, MEIKXXB_ID, FAZ_ID, PINZB_ID, YUNSDWB_ID)\n" + 
								"VALUES\n" + 
								"  (GETNEWID(" + this.getTreeid() + "),\n" + 
								this.getMeikdqmcValue().getId() + ",\n" +
								MainGlobal.getTableCol("gongysb", "id", "mingc='" + rsl.getString("gongysb_id")+"'") + ",\n" +
								this.getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id")) + ",\n" +
								MainGlobal.getTableCol("chezxxb", "id", "mingc='" + rsl.getString("faz_id")+"'") + ",\n" +
								this.getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id")) + ",\n" +
								this.getExtGrid().getColumn("yunsdwb_id").combo.getBeanId(rsl.getString("yunsdwb_id")) + "\n" +
								"   )"
							);
					flag = con.getInsert(sbuffer.toString());
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sbuffer);
						setMsg(ErrorMessage.InsertDatabaseFail);
						return;
					}
				} else {
					sbuffer.append(
	
							"UPDATE NIANJHJCGLB SET MEIKXXB_ID = " + this.getExtGrid().getColumn("meikxxb_id").combo.getBeanId(rsl.getString("meikxxb_id")) + ",\n" +
							"PINZB_ID = " + this.getExtGrid().getColumn("pinzb_id").combo.getBeanId(rsl.getString("pinzb_id")) + ",\n" +
							"YUNSDWB_ID =" + this.getExtGrid().getColumn("yunsdwb_id").combo.getBeanId(rsl.getString("yunsdwb_id")) + ",\n" +
							"GONGYSB_ID =" + MainGlobal.getTableCol("gongysb", "id", "mingc='" + rsl.getString("gongysb_id")+"'") + ",\n" +
							"FAZ_ID = " + MainGlobal.getTableCol("chezxxb", "id", "mingc='" + rsl.getString("faz_id")+"'") + "\n" +
							"WHERE ID =" + rsl.getString("id")
						);
					flag = con.getUpdate(sbuffer.toString());
					if (flag == -1) {
						con.rollBack();
						con.Close();
						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + sbuffer);
						setMsg(ErrorMessage.InsertDatabaseFail);
						return;
					}
				}
				
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rsl.close();
		con.commit();
		con.Close();
		this.setMsg("保存成功!");
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
		if(_RefreshChick){
			_RefreshChick=false;
			getSelectData();
		}
	}
	
	public boolean isFuDianc(JDBCcon con) {
		return con.getHasIt("select * from diancxxb where fuid=" + this.getTreeid());
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		String strdiancTreeID = "";
		int jib = this.getDiancTreeJib();
		if(jib == 1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib == 2){//选分公司的时候刷新出分公司下所有的电厂
			if (isFuDianc(con)) {
				strdiancTreeID = " and dc.fuid= " +this.getTreeid();
			} else {
				strdiancTreeID = " and dc.id= " +this.getTreeid();
			}
			
		}else if (jib == 3){//选电厂只刷新出该电厂
			if (visit.isFencb()) {
				if (isFuDianc(con)) {
					strdiancTreeID = " and dc.fuid= " +this.getTreeid();
				} else {
					strdiancTreeID = " and dc.id= " +this.getTreeid();
				}
			} else {
				strdiancTreeID = " and dc.id= " + this.getTreeid();
			}			
		}
		String sSQL = 
			"SELECT gl.id,\n" +
			"(select dc.mingc from diancxxb dc where dc.id=hz.diancxxb_id) as diancxxb_id,\n" +
			"(SELECT g.mingc FROM gongysb g WHERE g.id=jh.gongysb_id) AS gongysb_id,\n" +
			"(SELECT c.mingc FROM chezxxb c WHERE c.id=jh.faz_id) AS faz_id,\n" +
			"(SELECT m.mingc FROM meikxxb m WHERE m.id=gl.meikxxb_id) AS meikxxb_id,\n" + 			 
			"(SELECT p.mingc FROM pinzb p WHERE p.id=gl.pinzb_id) AS pinzb_id,\n" + 
			"(decode(gl.yunsdwb_id,-1,'请选择',(SELECT nvl(y.mingc,'') FROM yunsdwb y WHERE y.id(+)=gl.yunsdwb_id)))  AS yunsdwb_id\n" + 
			"FROM nianjhjcb jh ,nianjhjcglb gl,nianjhjchzb hz,diancxxb dc\n" + 
			"WHERE jh.id=gl.nianjhjcb_id\n" + 
			"  AND jh.nianjhjchzb_id=hz.id\n" + 
			"  AND hz.diancxxb_id=dc.id\n" + 
			"  AND to_char(hz.riq,'yyyy')='" + intyear + "'\n" + 
			"  AND jh.id=" + this.getMeikdqmcValue().getId() + "\n" +
			strdiancTreeID;

		ResultSetList rsl = con.getResultSetList(sSQL);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		egu.setTableName("nianjhjcglb");
		egu.setWidth("bodyWidth");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setEditor(null);
		egu.getColumn("gongysb_id").setHeader("供应商名称");
		egu.getColumn("gongysb_id").setWidth(150);
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("faz_id").setWidth(80);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤款名称");
		egu.getColumn("meikxxb_id").setWidth(150);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setDefaultValue("请选择");
		
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from meikxxb order by mingc"));
		
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from pinzb order by mingc"));
		
		egu.getColumn("yunsdwb_id").setEditor(new ComboBox());
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, 
				new IDropDownModel("select id,mingc from yunsdwb order by mingc","请选择"));
		
		ResultSetList rs = con.getResultSetList(
			"SELECT (select dc.mingc from diancxxb dc where dc.id=hz.diancxxb_id) as diancxxb_id,\n" +
			"(SELECT g.mingc FROM gongysb g WHERE g.id=jh.gongysb_id) AS gongysb_id,\n" + 
			"(SELECT c.mingc FROM chezxxb c WHERE c.id=jh.faz_id) AS faz_id\n" + 
			"FROM nianjhjcb jh,nianjhjchzb hz WHERE jh.nianjhjchzb_id=hz.id AND jh.ID=" + this.getMeikdqmcValue().getId()
		);
		if (rs.next()) {			
			egu.getColumn("diancxxb_id").setDefaultValue(rs.getString("diancxxb_id"));
			egu.getColumn("gongysb_id").setDefaultValue(rs.getString("gongysb_id"));
			egu.getColumn("faz_id").setDefaultValue(rs.getString("faz_id"));
		}
		rs.close();
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		egu.addTbarText("供应商:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("MeikmcDropDown");
		comb3.setId("gongys");
//		comb3.setEditable(true);
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(200);
		egu.addToolbarItem(comb3.getScript());
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		setExtGrid(egu);
		rsl.close();
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
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
		}
			if (nianfchanged) {
				getIMeikdqmcModels();
			}
			if (isTreeChange) {
				getIMeikdqmcModels();
			}
			getSelectData();
	}
//	 年份
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
		} else {
			nianfchanged = false;
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

	
//	 矿别名称
	public boolean _meikdqmcchange = false;

	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if (_MeikdqmcValue == null) {
			_MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IMeikdqmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql =
				"SELECT jh.id,g.mingc FROM nianjhjcb jh,nianjhjchzb hz,gongysb g\n" +
				"WHERE jh.nianjhjchzb_id=hz.id\n" + 
				"AND jh.gongysb_id=g.id\n" + 
				"AND hz.diancxxb_id=" + this.getTreeid() + "\n" + 
				"AND to_char(hz.riq,'yyyy')='" + this.getNianfValue() + "'\n" + 
				"ORDER BY g.mingc";
			_IMeikdqmcModel = new IDropDownModel(sql, "请选择");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}
//	 得到登陆用户所在电厂或者分公司的名称
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
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		cn.Close();
		return diancmc;

	}
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	
	private String treeid;
	private boolean isTreeChange;
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		if (treeid!=null && !((Visit) getPage().getVisit()).getString2().equals(treeid)) {
			isTreeChange = true;
		} else {
			isTreeChange = false;
		}
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
		return getTree().getWindowTreeScript();
	}
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}
	
}
