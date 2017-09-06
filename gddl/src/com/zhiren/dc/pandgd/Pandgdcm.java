package com.zhiren.dc.pandgd;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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

public class Pandgdcm extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
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
//	盘点编号下拉框
	private IPropertySelectionModel _pandModel;
	public void setPandModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value); 
	}
	public IPropertySelectionModel getPandModel() {
		Visit v = (Visit) getPage().getVisit();
		String dcsql = "and d.id = " + v.getDiancxxb_id();
		if(!v.isDCUser()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}else if(v.isFencb()){
			dcsql = "and (d.fuid = " + v.getDiancxxb_id() + " or d.id =" + v.getDiancxxb_id() + ")";
		}
		if (v.getProSelectionModel10() == null) {
			String sql = "select p.id,p.bianm from pand_gd p, diancxxb d " +
					"where p.diancxxb_id=d.id " 
					+ dcsql 
					+ " order by p.bianm desc";
			JDBCcon cn=new JDBCcon();
			ResultSetList rs=cn.getResultSetList(sql);
			if(rs.getRows()==0){
				v.setProSelectionModel10(new IDropDownModel(sql,"请添加盘点编码"));
			}else{
		    v.setProSelectionModel10(new IDropDownModel(sql));
			}
			rs.close();
			cn.Close();
		}
	    return v.getProSelectionModel10();
	}
	private IDropDownBean _pandValue;
	public void setPandValue(IDropDownBean value) {
		((Visit) getPage().getVisit()).setDropDownBean10(value);
	}
	public IDropDownBean getPandValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean10()==null){
			((Visit)getPage().getVisit()).setDropDownBean10((IDropDownBean)getPandModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}
	public String getPandbm() {
		String pandbm = "";
		if (getPandValue() == null) {
			JDBCcon con = new JDBCcon();
			String sql = "select id,bianm from pand_gd where diancxxb_id=" + ((Visit) getPage().getVisit()).getDiancxxb_id() + "  order by bianm desc";
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
	private boolean _AddChick = false;
	public void AddButton(IRequestCycle cycle) {
		_AddChick = true;
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
		} else if (_AddChick) {
			getSelectData();
		} else if (_DeleteChick) {
			_DeleteChick = false;
			delete();
			getSelectData();
		}
	}
	public void delete() {
		String sSql = "";
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		while(rsl.next()){
			String id = rsl.getString("id");
			//进行删除操作时添加日志
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_DEL,SysConstant.RizOpMokm_Qitcm,
					"pandwzcmb",id);
		}
		sSql = "delete from pandgdcmb  where pand_gd_id=" + getPandbID();
		con.getDelete(sSql);
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		ResultSetList rsl = null;
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
//		rsl = getExtGrid().getDeleteResultSet(getChange());
//		if (rsl == null) {
//			WriteLog.writeErrorLog(ErrorMessage.NullResult 
//					+ "Pandqtcm.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
//			setMsg(ErrorMessage.NullResult);
//			return;
//		}
//		while (rsl.next()) {
//			id = rsl.getInt("id");
//			sSql = "delete from pandwzcmb where id=" + id;
//			flag = con.getDelete(sSql);
//			if (flag == -1) {
//				con.rollBack();
//				con.Close();				
//			}
//		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Pandqtcm.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sSql = "insert into pandgdcmb(id,pand_gd_id,meicclb_id,shul) values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ getPandbID() + ","
					+ rsl.getString("meicclb_id") +","
					//+ getPandcmwzID(con, rsl.getString("meicclb_id")) + ","
					+ rsl.getString("shul") + " "
					+ " )";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else 
			{
//				进行修改操作时添加日志
				MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
						SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Qitcm,
						"pandwzcmb",id+"");
				sSql = "update pandgdcmb set "
					+ " pand_gd_id=" + getPandbID() + ","					
					+ " meicclb_id="+rsl.getString("meicclb_id")  + ","
					+ " shul=" + rsl.getString("shul") + " "
					+ " where id=" + id;
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				   }
			      }
		   }
	}
	public String getPandcmwzID(JDBCcon con, String cunmwzBM) {
		String yougbID = "";
		String sql = "select id from meicclb where pand_gd_id='" + cunmwzBM + "'"; 
		ResultSetList rs = con.getResultSetList(sql);
		while (rs.next()) {
			yougbID = rs.getString("id");
		}
		return yougbID;
	}
	public void getSelectData() {
		String sSql = "";
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		if (_AddChick) {
			sSql = 

				"select nvl(pb.id, 0) as id,pa.id as meicclb_id,\n" +
				"       pa.jizbh as jizbh,\n" + 
				"       pa.mingc as mingc,\n" + 
				"       pb.shul as shul,\n" + 
				"       round_new((pb.shul / w.beiz) * 100,1) as meiw\n" + " "+
				"  from (select g.id gid,p.id, b.jizbh as jizbh, m.mingc as mingc\n" +" "+ 
				"          from jizb b, item m, meicclb p, pand_gd g\n" + " "+
				"         where p.jizb_id = b.id\n" + " "+
				"           and p.item_id = m.id\n" + " "+
				"           and g.bianm = '"+getPandbm()+"'\n" +" "+ 
				"			and g.diancxxb_id = b.diancxxb_id\n" +
				"         order by p.id) pa,\n" + 
				"       pandgdcmb pb,\n" + 
				"       (select beiz, mingc from item where itemsortid = (select itemsortid from itemsort where bianm='MEIC')) w\n" +" "+ 
				" where  pb.meicclb_id(+)=pa.id\n" + " "+
				"   and pb.pand_gd_id(+)=pa.gid\n" + "" +
				"   and w.mingc = pa.mingc\n" + " "+
				" order by pa.id";

	 	 
		} else {
			sSql = 

				"select nvl(pb.id, 0) as id, pa.id as meicclb_id,\n" +
				"       pa.jizbh as jizbh,\n" + 
				"       pa.mingc as mingc,\n" + 
				"       pb.shul as shul,\n" + 
				"       round_new((pb.shul / w.beiz) * 100,1) as meiw\n" + " "+
				"  from (select p.id, b.jizbh as jizbh, m.mingc as mingc\n" +" "+ 
				"          from jizb b, item m, meicclb p, pand_gd g\n" + " "+
				"         where p.jizb_id = b.id\n" + " "+
				"           and p.item_id = m.id\n" + " "+
				"           and g.bianm = '"+getPandbm()+"'\n" +" "+ 
				"			and g.diancxxb_id = b.diancxxb_id\n" +
				"         order by p.id) pa,\n" + 
				"       pandgdcmb pb,\n" + 
				"		pand_gd g,\n" +
				"       (select beiz, mingc from item where itemsortid = (select itemsortid from itemsort where bianm='MEIC')) w\n" +" "+ 
				" where pb.meicclb_id = pa.id \n" + " "+

				"   and w.mingc = pa.mingc\n" + " "+
				"and pb.pand_gd_id=g.id\n" +
				"and g.bianm = '"+getPandbm()+"'" +
				" order by pa.id";
		}

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
		egu.getColumn("meicclb_id").setHeader("meicclb_id");
		egu.getColumn("meicclb_id").setHidden(true);
		egu.getColumn("jizbh").setHeader("机组");
		egu.getColumn("jizbh").setEditor(null);
		egu.getColumn("mingc").setHeader("煤仓");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("shul").setHeader("数量");
		egu.getColumn("meiw").setHeader("煤位");
        egu.getColumn("meiw").setHidden(true);
		String flag = MainGlobal.getXitxx_item("盘点", "煤仓存煤的煤位是否可编辑", String.valueOf(visit.getDiancxxb_id()), "否");
		if(!flag.equals("是")){
			egu.getColumn("meiw").setEditor(null);
		}
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
		if (rsl.getRows() == 0) {
			gbt = new GridButton("添加","function(){document.getElementById('AddButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_Insert);
			egu.addTbarBtn(gbt);
		}
		gbt = new GridButton("删除",GridButton.ButtonType_SaveAll,"gridDiv",egu.gridColumns,"DeleteButton");
		gbt.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbt);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String script = "\nvar tmpIndex = PandDropDown.getValue();\n";
		script = script + "PandDropDown.on('select', function(o,record,index) {if (tmpIndex != PandDropDown.getValue()) {document.forms[0].submit();}});\n";
		egu.addOtherScript(script);
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
		_AddChick = false;
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
