package com.zhiren.heiljkhh;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Pandbgext extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	//盘点编号下拉框
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IDropDownModel value) {
		_pandModel = value; 
	}
	public IPropertySelectionModel getPandModel() {
		if (_pandModel == null) {
			String sql = "select id,bianm from pandb where diancxxb_id=" 
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " and zhuangt=0"
					+ " order by id desc";
		    _pandModel = new IDropDownModel(sql);
		}
	    return _pandModel;
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		_pandValue = value;
	}
	public IDropDownBean getPandValue() {
		return _pandValue;
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pandb where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() 
					+ " and zhuangt=0 order by id desc";
			ResultSetList rsl = con.getResultSetList(sql);
			if (rsl.next()) {
				pandbm = rsl.getString("bianm");
			}
			return pandbm;
		}
		return getPandValue().getValue();
	}
	public long getPandbID() {
		if (getPandValue() == null) {
			return -1;
		}
		return getPandValue().getId();
	}
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		} else if (_CreateChick) {
			_CreateChick = false;
			createData();
		} else if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
	}
	public String getPandRiq(JDBCcon con, String pandBm) throws SQLException  {
		Date riq = null;
		String sRiq = "";
		String sql = "select riq from pandb where bianm='" + pandBm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			riq = rs.getDate("riq");
			sRiq = DateUtil.FormatDate(riq);
		}
		rs.close();
		return sRiq;
	}
	public double getShangYKC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shangykc = 0.0;
		String sql = "select kuc from yueshchjb where riq=first_day(add_months(to_date('" + riq + "','yyyy-mm-dd'),-1))"
				+ " and fenx='本月' and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			shangykc = rs.getDouble("kuc");
		} else {
			riq = String.valueOf((Integer.parseInt(riq.substring(5,7)) - 1));
			setMsg(riq + "月库存为0！");
		}
		rs.close();
		return shangykc;
	}
	public double[] getShouHC(JDBCcon con, String riq, Visit visit) throws SQLException {
		double shouhc[] = null;
		String sql = "select sum(dangrgm)as jinm,sum(fady)as fady,sum(gongry)as gongry,\n"
				+ "sum(qity)as qity,sum(cuns)as cuns,sum(yuns)as yuns,sum(shuifctz)as shuifctz\n"
				+ "from shouhcrbb where riq between first_day(to_date('" + riq + "','yyyy-mm-dd'))\n"
				+ "and to_date('" + riq + "','yyyy-mm-dd') and diancxxb_id=" + visit.getDiancxxb_id();
		ResultSet rs = con.getResultSet(sql);
		shouhc = new double[7];
		if (rs.next()) {
//			 0:本月进煤  1:发电用  2:供热用  3:其它用  4:存损  5:运损  6:水分调整
			shouhc[0] = rs.getDouble("jinm");
			shouhc[1] = rs.getDouble("fady");
			shouhc[2] = rs.getDouble("gongry");
			shouhc[3] = rs.getDouble("qity");
			shouhc[4] = rs.getDouble("cuns");
			shouhc[5] = rs.getDouble("yuns");
			shouhc[6] = rs.getDouble("shuifctz");
		} else {
			for (int i = 0; i < shouhc.length; i++) {
				shouhc[i] = 0.0;
			}
		}
		rs.close();
		return shouhc;
	}
	public double getShiJKC(JDBCcon con, String bianm) throws SQLException {
		double cunml = 0.0;
		double qitcm = 0.0;
		String sql = "select sum(cunml)as cunml from pandtjb,pandb where pandb_id=pandb.id and bianm='" + bianm + "'";
		ResultSet rs = con.getResultSet(sql);
		if (rs.next()) {
			cunml = rs.getDouble("cunml");
		}
		rs.close();
		sql = "select sum(cunml)as qitcm from pandwzcmb,pandb where pandb_id=pandb.id and bianm='" + bianm + "' group by pandb_id";
		rs = con.getResultSet(sql);
		if (rs.next()) {
			qitcm = rs.getDouble("qitcm");
		}
		rs.close();
		return cunml + qitcm;
	}
	public void createData() {	
		String sql = "";
		String riq = "";
		String pandbm = getPandbm();
		long pandid = getPandbID();
		double shangykc = 0.0;
		double zhangmkc = 0.0;
		double shijkc = 0.0;
		double panyk = 0.0;
		double[] shouhc = null;
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		try {
//			盘煤日期
			riq = getPandRiq(con, pandbm);
			if (riq.equals("") || riq == null) {
				setMsg("盘点日期为空！");
				return;
			}
//			上月库存
			shangykc = getShangYKC(con, riq, visit);
//			收好存表中数据
			shouhc = getShouHC(con, riq, visit);
//			实际库存
			shijkc = getShiJKC(con, pandbm);
//			账面库存
//			 0:本月进煤  1:发电用  2:供热用  3:其它用  4:存损  5:运损  6:水分调整
			zhangmkc = shangykc + shouhc[0] - shouhc[1] - shouhc[2]- shouhc[3]- shouhc[4]- shouhc[5] + shouhc[6];
//			盘盈亏
			panyk = shijkc - zhangmkc;
//			删除 pandzmm 数据
			sql = "delete from pandzmm where pandb_id=" + pandid;
			con.getDelete(sql);
//			插入 pandzmm 数据 
			sql = "insert into pandzmm values ("
				+ "getnewid(" + visit.getDiancxxb_id() + "),"
				+ pandid + ","
				+ shouhc[0] + ","
				+ shouhc[1] + ","
				+ shouhc[2] + ","
				+ "0,"
				+ shouhc[3] + ","
				+ "0,"
				+ shouhc[4] + ","
				+ shouhc[5] + ","
				+ shouhc[6] + ","
				+ zhangmkc + ","
				+ shijkc + ","
				+ panyk 
				+ ")";
			con.getInsert(sql);
			con.commit();
		} catch(Exception e) {
			con.rollBack();
			setMsg("生成数据失败！");
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	public void delete() {
		String sSql = "";
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Zhangmm,
					"pandzmm",id);
		}
		sSql = "delete from pandzmm  where pandb_id=" + getPandbID();
		flag = con.getDelete(sSql);
		if (flag == -1) {
			setMsg("删除数据失败！");
		}
		con.Close();
	}
	public void save() {
		String sSql = "";
		String sql="";
		long id = 0;
		int flag = 0;
		int t=0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		con.setAutoCommit(false);
		ResultSetList rsl = null;
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "pandzmm.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			//进行修改操作时添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Zhangmm,
					"pandzmm",id+"");
//			sSql = "update pandzmm set "
//				+ " pandb_id=" + getPandbID() + ","					
//				+ " benyjm=" + rsl.getDouble("benyjm") + ","
//				+ " fadh=" + rsl.getDouble("fadh") + ","
//				+ " gongrh=" + rsl.getDouble("gongrh") + ","
//				+ " feiscy=" + rsl.getDouble("feiscy") + ","
//				+ " qity=" + rsl.getDouble("qity") + ","
//				+ " diaocl=" + rsl.getDouble("diaocl") + ","
//				+ " cuns=" + rsl.getDouble("cuns") + ","
//				+ " yuns=" + rsl.getDouble("yuns") + ","
//				+ " shuifc=" + rsl.getDouble("shuifc") + ","
//				+ " zhangmkc=" + rsl.getDouble("zhangmkc") + ","
//				+ " shijkc=" + rsl.getDouble("shijkc") + ","
//				+ " panyk=" + rsl.getDouble("panyk")
//				+ " where id=" + id;
			sql=" update pandzmm pd set pd.zhangmkc= "+rsl.getString("zhangmkc")+"\n"
				+"  where pd.pandb_id in (select pa.id from pandb pa where pa.riq+1= \n"
				+" (select riq from pandb where id="+this.getPandbID()+") and pa.diancxxb_id="+visit.getDiancxxb_id()+")";
			
			t = con.getUpdate(sql);
			if (t == -1) {
				con.rollBack();
				con.Close();
			}
			
			sSql = "update pandzmm set "
				+ " fadh=" + rsl.getDouble("fadh") + ","
				+ " cuns=" + rsl.getDouble("cuns") 
				+ " where id=" + id;
//			System.out.println(sSql);
			flag = con.getUpdate(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();
			}
		}
		con.commit();
		con.Close();
	}
	public void getSelectData() {
		String sSql = "";
//		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		sSql = " select id,diancxxb_id,zhangmkc,laimwsbkc,cuns,fadh,zhangmkc+laimwsbkc-cuns-fadh as zhangmhj from (select pd.id id,pa.diancxxb_id diancxxb_id,nvl((select p1.zhangmkc from pandzmm p1, pandb p where p.riq+1=pa.riq and p1.pandb_id=p.id ),0) as zhangmkc,\n" 
			+ " nvl((select nvl(sh.kuc,0)+nvl(sh.dangrgm,0)-nvl(sh.shangbkc,0)  from shouhcrbb sh where sh.riq=pa.riq),0) as laimwsbkc,\n"
			+ " nvl(pd.cuns,0) cuns,nvl(pd.fadh,0) fadh,nvl(pd.zhangmkc,0) zhangmhj \n"
			+" from pandzmm pd,pandb pa where pd.pandb_id=pa.id and pa.bianm='"+this.getPandbm()+"')";
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("diancxxb_id");
		egu.getColumn("diancxxb_id").setHidden(true);
		
		egu.getColumn("zhangmkc").setHeader("前一日账面煤量");
		//egu.getColumn("zhangmkc").setEditor(null);
		
		egu.getColumn("laimwsbkc").setHeader("加来煤未报");
		egu.getColumn("laimwsbkc").setEditor(null);
		
		
		egu.getColumn("cuns").setHeader("去储损");
		egu.getColumn("fadh").setHeader("去盘点前当日耗");
	
		egu.getColumn("zhangmhj").setHeader("盘点时账面合计");
		egu.getColumn("zhangmhj").setEditor(null);
		
		egu.addTbarText("盘点编码：");
		ComboBox cobPand = new ComboBox();
		cobPand.setWidth(100);
		cobPand.setTransform("PandDropDown");
		cobPand.setId("PandDropDown");
		cobPand.setLazyRender(true);
		egu.addToolbarItem(cobPand.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("生成","function(){document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		gbt = new GridButton("删除",GridButton.ButtonType_SaveAll,"gridDiv",egu.gridColumns,"DeleteButton");
		gbt.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		StringBuffer script = new StringBuffer();
		script.append("\nvar tmpIndex = PandDropDown.getValue();\n");
		script.append("PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n");
		script.append( "gridDiv_grid.on('afteredit', function(e) {\n") 
				.append("var record = gridDiv_ds.getAt(e.row);\n")
				.append("var changeValue = eval(e.originalValue||0) - (eval(e.value||0)<0 ? 0 : eval(e.value||0));\n")
				.append("var zhangmkc = eval(record.get('ZHANGMKC')||0);\n")
				.append("if (eval(e.value||0)<0) {\n")
				.append("\trecord.set(e.field,0);\n")
				.append("}\n")
				.append("record.set('ZHANGMHJ',parseFloat(record.get('ZHANGMKC'))+parseFloat(record.get('LAIMWSBKC'))-parseFloat(record.get('CUNS'))-parseFloat(record.get('FADH')));\n")
				.append("});");
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		con.Close();
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setPandModel(null);
			setPandValue(null);
		}
		init();
	}
	private void init() {
		getSelectData();
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
