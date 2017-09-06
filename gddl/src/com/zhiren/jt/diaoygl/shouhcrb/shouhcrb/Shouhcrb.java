package com.zhiren.jt.diaoygl.shouhcrb.shouhcrb;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;


import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;

public class Shouhcrb extends BasePage implements PageValidateListener{

	// 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
	}
	 public void pageValidate(PageEvent arg0) {
	    	String PageName = arg0.getPage().getPageName();
	    	String ValPageName = Login.ValidateLogin(arg0.getPage());
	    	if (!PageName.equals(ValPageName)) {
	    		ValPageName = Login.ValidateAdmin(arg0.getPage());
	    		if(!PageName.equals(ValPageName)) {
	    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
	        		throw new PageRedirectException(ipage);
	    		}
			}
		}
	public String getDiancName() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		String diancmc = "";
		// long diancID = -1;
		String sql = "select dc.id,dc.mingc from diancxxb dc where dc.id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			rs = con.getResultSet(sql);
			while (rs.next()) {
				// diancID = rs.getLong("id");
				diancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return diancmc;
	}

	private String _msg;

	protected void initialize() {
		_msg = "";
	}

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	// 开始日期
	private Date _BeginriqValue = DateUtil.AddDate(new Date(), -1,
			DateUtil.AddType_intDay);

	private boolean _BeginriqChange = false;

	public Date getBeginriqDate() {
		if (_BeginriqValue == null) {
			_BeginriqValue = DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay);
		}
		return _BeginriqValue;
	}

	public void setBeginriqDate(Date _value) {
		if (FormatDate(_BeginriqValue).equals(FormatDate(_value))) {
			_BeginriqChange = false;
		} else {
			_BeginriqValue = _value;
			_BeginriqChange = true;
		}
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Shouhcrbbean) _list.get(i)).getXXX();
		getSelectData();
	}

	
	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		String Sql = "";
		String riq = FormatDate(_BeginriqValue);
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		try {
			// 直接保存
			for (int i = 0; i < _list.size(); i++) {
				long _id = ((Shouhcrbbean) _list.get(i)).getId();
				long diancxxb_id = ((Shouhcrbbean) _list.get(i))
						.getDianchangid();
				double dangrm = ((Shouhcrbbean) _list.get(i)).getGongm();
				double haoy = ((Shouhcrbbean) _list.get(i)).getHaoy();
				double kuc = ((Shouhcrbbean) _list.get(i)).getKuc();
				double tiaozl=((Shouhcrbbean) _list.get(i)).getTiaozl();
				String checksql = "select id from shouhcrbb where diancxxb_id="
						+ diancxxb_id + " and riq=to_date('" + riq
						+ "','yyyy-MM-dd') ";
				rs = con.getResultSet(checksql);
				if (rs.next()) {
					_id = rs.getLong("id");
				}
				if (_id == 0) {

					Sql = "insert into shouhcrbb (ID,RIQ,DIANCXXB_ID,DANGRGM,HAOYQKDR,KUC,BEIZ,TIAOZL) values ("
							+ MainGlobal
									.getNewID(((Visit) getPage().getVisit())
											.getDiancxxb_id())
							+ ",to_date('"
							+ riq
							+ "','yyyy-mm-dd')"
							+ ","
							+ diancxxb_id
							+ ","
							+ dangrm + "," + haoy + "," + kuc + ",'0',"+tiaozl+")";
					con.getInsert(Sql);
				} else {
					Sql = "update shouhcrbb set riq=to_date('" + riq
							+ "','yyyy-mm-dd'),DIANCXXB_ID=" + diancxxb_id
							+ "," + "DANGRGM=" + dangrm + ",HAOYQKDR=" + haoy
							+ ",KUC=" + kuc + ",BEIZ='0',TIAOZL="+tiaozl+" where id=" + _id;
					con.getUpdate(Sql);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public List getSelectData() {
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		String riq = FormatDate(_BeginriqValue);
		try {
			long mxuh = 0;
			String strdiancID = "";
			if (isDiancUser()) {// 电厂用户
				strdiancID = " and d.id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			String strGongsID = "";
			if (this.isJitUserShow()) {// 集团用户显示分公司的下拉框
				strGongsID = "and dc.fuid= " + this.getFengsValue().getId();
			}
			if (this.isGongsUser()) {// 分公司用户
				strGongsID = "and dc.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			}

			sql.append("select dt.*, d.id as diancid, d.mingc, d.xuh,zt.kuc as SHANGYKC,lj.leijgm as leijgm,lj.leijhm as leijhm ");
			sql.append("  from (select * from shouhcrbb h  where h.riq = to_date('"+ riq + "', 'yyyy-mm-dd')) dt, ");
			sql.append("       (select h.diancxxb_id,h.kuc from shouhcrbb h, diancxxb dc ");
			sql.append("         where h.diancxxb_id = dc.id and riq = to_date('"+ riq + "', 'yyyy-mm-dd')-1) zt, ");
			sql.append("       (select dc.id, dc.mingc, px.xuh from diancxxb dc, dianckjpxb px ");
			sql.append("         where dc.id = px.diancxxb_id(+) "+ strGongsID);
			sql.append("        and (px.kouj = '发电燃料日报' or px.kouj = '收耗存日报')) d, ");// 按日报口径排序
			sql.append("	   (select rb.diancxxb_id,sum(rb.dangrgm) as leijgm,sum(rb.haoyqkdr) as leijhm from shouhcrbb rb ");
			sql.append("           where rb.riq>=First_day(to_date('" + riq+ "','yyyy-mm-dd')) and rb.riq<=to_date('" + riq+ "','yyyy-mm-dd') ");
			sql.append("           group by rb.diancxxb_id) lj ");
			sql.append(" where dt.diancxxb_id(+) = d.id and zt.diancxxb_id(+) = d.id and lj.diancxxb_id(+)=d.id "+ strdiancID);
			sql.append(" order by xuh, mingc ");
			System.out.println(sql.toString());
			ResultSet rs = JDBCcon.getResultSet(sql.toString());
			while (rs.next()) {
				long id = rs.getLong("ID");
				String mriq = rs.getString("RIQ");
				long dianchangid = rs.getLong("diancid");
				String dianchangmc = rs.getString("mingc");
				long gongm = rs.getLong("DANGRGM");
				long haoy = rs.getLong("HAOYQKDR");
				long kuc = rs.getLong("KUC");
				long shangykc = rs.getLong("SHANGYKC");
				long leijgm = rs.getLong("leijgm");
				long leijhm = rs.getLong("leijhm");
				long tiaozl=rs.getLong("TIAOZL");
				_editvalues
						.add(new Shouhcrbbean(id, ++mxuh, mriq, dianchangid,
								dianchangmc, gongm, haoy, kuc, shangykc,
								leijgm, leijhm,tiaozl));

			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Shouhcrbbean());
		}
		setEditTableRow(-1);
		setEditValues(_editvalues);
		return getEditValues();
	}

	// ///////////////////////////////////////////////////////////////////////////
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// private boolean _InsertChick = false;
	//
	// public void InsertButton(IRequestCycle cycle) {
	// _InsertChick = true;
	// }

	

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}

	// //////////////////////////////////////////////////////////////////
	private Shouhcrbbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Shouhcrbbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Shouhcrbbean EditValue) {
		_EditValue = EditValue;
	}

	// ///////////////////////////////////////////////////////////////

	public int getEditTableRow() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}

	// 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModels().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public IPropertySelectionModel getIDiancmcModels() {
		JDBCcon con = new JDBCcon();
		try {
			List fahdwList = new ArrayList();
			fahdwList.add(new IDropDownBean(-1, "请选择"));

			String sql = "";
			sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
			// System.out.println(sql);
			ResultSet rs = con.getResultSet(sql);
			for (int i = 0; rs.next(); i++) {
				fahdwList.add(new IDropDownBean(rs.getLong("id"), rs
						.getString("jianc")));
			}

			_IDiancmcModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IDiancmcModel;
	}

	// //////////////////////////////////////////////////////////////////////////
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());
			_BeginriqValue = DateUtil.AddDate(new Date(), -1,
					DateUtil.AddType_intDay);
			visit.setList1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			
			this.getFengsModels();
			
			this.getIDiancmcModels();
			getSelectData();
		}
		if (_BeginriqChange) {
			_BeginriqChange = false;
			getSelectData();
		}
		if (_fengschange) {
			_fengschange = false;
			this.getSelectData();
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

	// 分公司下拉框
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql));
	}
}