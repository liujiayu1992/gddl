package com.zhiren.pub.duanxin;

/*
 * 时间：2007-09-25
 * 作者：Qiuzuwei
 * 修改原因：修改修改方案时，存储历史记录错误
 * 修改位置：
 * 			private void Save() {...}
 * */

/*
 * 时间：2007-09-22
 * 作者：Qiuzuwei
 * 修改原因：修改短信设置的存储方法，以满足短信的群发功能
 * 修改位置：
 * 			private void Save() {...}
 * */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Zidfdxsz extends BasePage {
	
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
	
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	
	
	private boolean _RefurbishChick = false;
	
	
	
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	
	
	private boolean _InsertChick = false;
	
	
	
	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	
	
	private boolean _EditChick = false;
	
	
	
	public void EditButton(IRequestCycle cycle) {
		_EditChick = true;
	}
	
	
	
	private boolean _DeleteChick = false;
	
	
	
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	
	
	private boolean _SaveChick = false;
	
	
	
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	
	
	private boolean _ReturnChick = false;
	
	
	private boolean _xiangmChick = false;
	
	
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	
	private void Save() {
		
		JDBCcon conn = new JDBCcon();
		try {
			String _Send = getSendSet();
			if (_Send == null) {
				setMsg("请设置发送的内容");
				return;
			}
			if (_Send.equals("")) {
				setMsg("请设置发送的内容");
				return;
			}
			if (getMingc().equals("")) {
				setMsg("请输入名称");
				return;
			}
			String _reny = "";
			String _renyid = "";
			String _meikid = "";
			if (getRenySelected() == null) {
				if (getRenyinput() == null) {
					setMsg("请设置接收人");
					return;
				}
				if (getRenyinput().equals("")) {
					setMsg("请设置接收人");
					return;
				}
			}
			if (getRenySelected().size() > 0) {
				
				for (int i = 0; i < getRenySelected().size(); i++) {
					_renyid = _renyid
							+ ((IDropDownBean) getRenySelected().get(i))
									.getId() + ",";
				}
				if (_renyid.length() > 0) {
					_renyid = _renyid.substring(0, _renyid.length() - 1);
				}
				String sql = "select yiddh from renyxxb  where id in("
						+ _renyid + ")";
				ResultSet rs = conn.getResultSet(sql);
				while (rs.next()) {
					_reny = _reny + rs.getString("yiddh") + ",";
				}
				if (_reny.length() > 0) {
					_reny = _reny.substring(0, _reny.length() - 1);
				}
			}
			String _renysr = "";
			if (getRenyinput() != null) {
				_renysr = getRenyinput();
				if (_renysr.indexOf("，") > 0) {
					_renysr = _renysr.replaceAll("，", ",");
				}
				if (_renysr.indexOf("、") > 0) {
					_renysr = _renysr.replaceAll("、", ",");
				}
				if (_renysr.indexOf("；") > 0) {
					_renysr = _renysr.replaceAll("；", ",");
				}
				if (_renysr.indexOf(";") > 0) {
					_renysr = _renysr.replaceAll(";", ",");
				}
				if (_renysr.length() > 0) {
					if (_reny.length() > 0) {
						_reny = _reny + "," + _renysr;
					} else {
						_reny = _reny + _renysr;
					}
				}
			}
			if (getMeikSelected() != null) {
				for (int i = 0; i < getMeikSelected().size(); i++) {
					_meikid = _meikid
							+ ((IDropDownBean) getMeikSelected().get(i))
									.getId() + ",";
				}
				
				if (_meikid.length() > 0) {
					_meikid = _meikid.substring(0, _meikid.length() - 1);
				}
				
			}
			String _sqls = "";
			String _sqlww = "";
			String _sqlwh = "";
			if (getXiangmValue() != null) {
				if (((IDropDownBean) getXiangmValue()).getId() != -1) {
					String sql = "select * from duanxsjb where id = "
							+ ((IDropDownBean) getXiangmValue()).getId();
					ResultSet rs = conn.getResultSet(sql);
					while (rs.next()) {
						String[] _xiangm;
						String _xm = "";
						_xm = rs.getString("keyzd");
						_sqls = rs.getString("chaxyj");
						_sqlwh = rs.getString("tiaojzd");
						if (_xm.equals("")) {
							return;
						}
						_xiangm = _xm.split(";");
						_sqlww = _Send;
						for (int i = 0; i < _xiangm.length; i++) {
							if (_xiangm[i].indexOf(",") >= 0) {
								String[] _m = _xiangm[i].split(",");
								_sqlww = _sqlww.replaceAll(_m[1], "'||" + _m[0]
										+ "||'");
								// _RenyList.add(new IDropDownBean(i, _m[1]));
							} else {
								_sqlww = _sqlww.replaceAll(_xiangm[i], "'||"
										+ _xiangm[i] + "||'");
								// _RenyList.add(new IDropDownBean(i,
								// _xiangm[i]));
							}
						}
					}
					if (_sqlww.indexOf("'||") == 0) {
						_sqlww = _sqlww.substring(3);
					} else {
						_sqlww = "'" + _sqlww;
					}
					if (_sqlww.lastIndexOf("||'") == _sqlww.length() - 3) {
						_sqlww = _sqlww.substring(0, _sqlww.length() - 3);
					} else {
						_sqlww = _sqlww + "'";
					}
				}
			}
			//拆分人员列表
			
			String[] ry = null;
			String phoneNo = "";
			if (_reny.length() > 0) {
				ry = _reny.split(",");
				_reny = "";
				for (int i = 0; i < ry.length; i++) {
					if (_reny.equals("")) {
						_reny = "'" + ry[i] + "'";
					} else {
						_reny = _reny + "," + "'" + ry[i] + "'";
					}
					if (phoneNo.equals("")) {
						phoneNo = ry[i];
					} else {
						phoneNo = phoneNo + "," + ry[i];
					}
				}
				
			}
			String sql_I1 = "";
			String sql_I2 = "";
			
			if (!_sqls.equals("")) {
				//修改sql，
				if (!_meikid.equals("")) {
					sql_I1 = "select * from (select Distinct "
							+ _sqlww
							+ " as send from ( select * from ("
							+ _sqls
							+ ") where "
							+ _sqlwh
							+ " in("
							+ _meikid
							+ "))  minus　(select neir as send  from duanxrz where shoujh in (";
					sql_I2 = "))) ";
					
					_sqls = "select * from (select Distinct "
							+ _sqlww
							+ " as send from ( select * from ("
							+ _sqls
							+ ") where "
							+ _sqlwh
							+ " in("
							+ _meikid
							+ "))  minus　(select neir as send  from duanxrz where shoujh in ("
							+ _reny + "))) ";
					
				} else {
					sql_I1 = "select * from (select Distinct "
							+ _sqlww
							+ " as send from ("
							+ _sqls
							+ ")  minus　(select neir as send  from duanxrz where shoujh in(";
					sql_I2 = "))) ";
					
					_sqls = "select * from (select Distinct "
							+ _sqlww
							+ " as send from ("
							+ _sqls
							+ ")  minus　(select neir as send  from duanxrz where shoujh in("
							+ _reny + "))) ";
					
				}
				
			} else {
				return;
			}
			//			ResultSet rs = conn.getResultSet(_sqls);
			//			if (rs.next()) {
			//			登记历史数据
			for (int i = 0; i < ry.length; i++) {
				//					String Sql = "insert into duanxrz(ID,NEIR,SHOUJH,LEIX,FASSJ) values(XL_duanxrz_ID.nextval,'"
				//							+ rs.getString("send")
				//							+ "','"
				//							+ _reny
				//							+ "',0,sysdate)";
				String Sql = "insert into duanxrz(id,neir,shoujh,leix,fassj) ("
						+ " select XL_duanxrz_ID.nextval,send,'" + ry[i]
						+ "',0,sysdate from (" + sql_I1 + "'" + ry[i] + "'"
						+ sql_I2 + ") )";
				int flag = conn.getInsert(Sql);
				if (flag == -1) {
					setMsg("数据保存失败。");
					return;
				}
				//					System.out.println("flag = " + flag);
				//					System.out.println(Sql);
			}
			//				rs.close();
			//				rs = null;
			//			}
			_sqls = _sqls.replaceAll("'", "''");
			if (getViewRow() == 1) {
				int flag = conn
						.getDelete("delete from duanxfsb where id="
								+ ((Zidfdxszbean) getEditValues().get(
										getEditTableRow())).getId());
				if (flag == -1) {
					setMsg("数据保存失败。");
					return;
				}
			}
			
			String sql = "insert into　duanxfsb (ID,MINGC,xiangmmc,CHAXYJ,FASNR,MEIKID,RENYXZ,RENYSR,fassjh,FASSJ)values(XL_DUANXFSB_ID.nextval,'"
					+ getMingc()
					+ "','"
					+ ((IDropDownBean) getXiangmValue()).getValue()
					+ "','"
					+ _sqls
					+ "','"
					+ _Send
					+ "','"
					+ _meikid
					+ "','"
					+ _renyid
					+ "','"
					+ _renysr
					+ "','"
					+ (ry == null ? "" : phoneNo)
					+ "',0)";
			int flag = conn.getInsert(sql);
			if (flag == -1) {
				setMsg("数据保存失败。");
				return;
			}
			setMsg("保存成功");
		} catch (SQLException e) {
			e.printStackTrace();
			setMsg("数据保存失败。");
		} finally {
			conn.Close();
			conn = null;
		}
	}
	
	
	private void InsertButton() {
		((Visit) getPage().getVisit()).setInt1(1);
		
		setRenyinput("");
		setMingc("");
		setSendSet("");
		((Visit) getPage().getVisit()).setList1(null);
		((Visit) getPage().getVisit()).setList2(null);
		
		setViewRow(0);
	}
	
	
	private void DeleteButton() {
		if (getEditTableRow() != -1) {
			JDBCcon conn = new JDBCcon();
			conn.getDelete("delete from duanxfsb where id="
					+ ((Zidfdxszbean) getEditValues().get(getEditTableRow()))
							.getId());
			conn.Close();
			getSelectData();
			
		} else {
			setMsg("请选择要删除的行");
		}
		
	}
	
	
	private void RefurbishButton() {
		getSelectData();
	}
	
	
	private void ReturnButton() {
		getSelectData();
		((Visit) getPage().getVisit()).setInt1(0);
	}
	
	
	private void EditButton() {
		JDBCcon conn = new JDBCcon();
		try {
			if (getEditTableRow() == -1) {
				setMsg("请选择要编辑的数据");
				return;
			}
			long _id = ((Zidfdxszbean) getEditValues().get(getEditTableRow()))
					.getId();
			String Sql = "select * from duanxfsb where id=" + _id;
			ResultSet rs = conn.getResultSet(Sql);
			String _MINGC = "";
			String _XIANGMMC = "";
			String _meik = "";
			String _RENYSR = "";
			String _FASNR = "";
			String _RENYXZ = "";
			if (rs.next()) {
				_MINGC = rs.getString("MINGC");
				_XIANGMMC = rs.getString("XIANGMMC");
				_meik = rs.getString("MEIKID");
				_RENYSR = rs.getString("RENYSR");
				_FASNR = rs.getString("FASNR");
				_RENYXZ = rs.getString("RENYXZ");
			}
			setRenyinput(_RENYSR);
			setMingc(_MINGC);
			setSendSet(_FASNR);
			IDropDownBean _xiangm = null;
			for (int i = 0; i < getIXiangmModel().getOptionCount(); i++) {
				if (((IDropDownBean) getIXiangmModel().getOption(i)).getValue()
						.equals(_XIANGMMC)) {
					_xiangm = (IDropDownBean) getIXiangmModel().getOption(i);
				}
			}
			setXiangmValue(_xiangm);
			if (_meik != null) {
				if (!_meik.equals("")) {
					String[] _meikid;
					_meikid = _meik.split(",");
					List _meiklis = new ArrayList();
					for (int i = 0; i < _meikid.length; i++) {
						for (int j = 0; j < getMeikSelectModel()
								.getOptionCount(); j++) {
							long _ida = ((IDropDownBean) getMeikSelectModel()
									.getOption(j)).getId();
							long _intmkid = 0;
							_intmkid = Long.parseLong(_meikid[i]);
							if (_ida == _intmkid) {
								_meiklis
										.add((IDropDownBean) getMeikSelectModel()
												.getOption(j));
								break;
							}
						}
					}
					setMeikSelected(_meiklis);
				}
			}
			if (_RENYXZ != null) {
				if (!_RENYXZ.equals("")) {
					String[] _RENYXZid;
					_RENYXZid = _RENYXZ.split(",");
					List _RENYXZlis = new ArrayList();
					for (int i = 0; i < _RENYXZid.length; i++) {
						for (int j = 0; j < getRenySelectModel()
								.getOptionCount(); j++) {
							long _ida = ((IDropDownBean) getRenySelectModel()
									.getOption(j)).getId();
							long _intmkid = 0;
							_intmkid = Long.parseLong(_RENYXZid[i]);
							if (_ida == _intmkid) {
								_RENYXZlis
										.add((IDropDownBean) getRenySelectModel()
												.getOption(j));
								break;
							}
						}
					}
					
					setRenySelected(_RENYXZlis);
				}
			}
			((Visit) getPage().getVisit()).setInt1(1);
			setViewRow(1);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			conn.Close();
		}
	}
	
	
	public void submit(IRequestCycle cycle) {
		if (_xiangmChick) {
			_xiangmChick = false;
			getIZidSelectModels();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			RefurbishButton();
			
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			DeleteButton();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			
		}
		if (_InsertChick) {
			_InsertChick = false;
			InsertButton();
		}
		if (_EditChick) {
			_EditChick = false;
			EditButton();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			ReturnButton();
		}
	}
	
	
	public IDropDownBean getXiangmValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	
	
	public void setXiangmValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (!((IDropDownBean) value).getValue().equals(
					((IDropDownBean) (((Visit) getPage().getVisit())
							.getDropDownBean1())).getValue())) {
				_xiangmChick = true;
			}
		} else {
			_xiangmChick = true;
		}
		
		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}
	
	
	public IPropertySelectionModel getIXiangmModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIXiangmModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	
	
	public void setIXiangmModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	
	
	public void getIXiangmModels() {
		String sql = "select id,mingc from duanxsjb";
		setIXiangmModel(new IDropDownModel(sql));
	}
	
	
	
	// 设置人员选择
	public IPropertySelectionModel getRenySelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			setRenySelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	
	public void setRenySelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}
	
	
	private void setRenySelectModels() {
		List _RenyList = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			String sql = "select id,quanc from renyxxb where yiddh is not null";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				_RenyList.add(new IDropDownBean(rs.getLong("id"), rs
						.getString("quanc")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
			((Visit) getPage().getVisit())
					.setProSelectionModel2(new IDropDownModel(
							_RenyList));
			setRenySelected(null);
		}
	}
	
	
	public List getRenySelected() {
		return ((Visit) getPage().getVisit()).getList2();
	}
	
	
	public void setRenySelected(List RenySelect) {
		((Visit) getPage().getVisit()).setList2(RenySelect);
	}
	
	
	
	// 设置煤矿选择
	public IPropertySelectionModel getMeikSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			setMeikSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
	
	public void setMeikSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}
	
	
	public String getRenyinput() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	
	
	public void setRenyinput(String _value) {
		((Visit) getPage().getVisit()).setString1(_value);
	}
	
	
	public String getMingc() {
		return ((Visit) getPage().getVisit()).getString3();
	}
	
	
	public void setMingc(String _value) {
		((Visit) getPage().getVisit()).setString3(_value);
	}
	
	
	public String getSendSet() {
		return ((Visit) getPage().getVisit()).getString2();
	}
	
	
	public void setSendSet(String _value) {
		((Visit) getPage().getVisit()).setString2(_value);
	}
	
	
	private void setMeikSelectModels() {
		List _RenyList = new ArrayList();
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		try {
			String sql = " select  m.id,m.mingc from meikxxb m,gongysmkglb gl,gongysdcglb gd,gongysb g "
						+"	 where gl.gongysb_id=g.id "
						+"	 and gl.meikxxb_id=m.id "
						+"	 and gd.gongysb_id=g.id"
						+"	 and gd.diancxxb_id="+visit.getDiancxxb_id()+""
						+"	 order by m.xuh,m.mingc";
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				_RenyList.add(new IDropDownBean(rs.getLong("id"), rs
						.getString("mingc")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
			((Visit) getPage().getVisit())
					.setProSelectionModel3(new IDropDownModel(
							_RenyList));
			setMeikSelected(null);
		}
	}
	
	
	public List getMeikSelected() {
		return ((Visit) getPage().getVisit()).getList1();
	}
	
	
	public void setMeikSelected(List RenySelect) {
		((Visit) getPage().getVisit()).setList1(RenySelect);
	}
	
	
	
	// 设置项目字段选择
	
	public IDropDownBean getZidSelectValue() {
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}
	
	
	public void setZidSelectValue(IDropDownBean value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() != null) {
			if (!((IDropDownBean) value).getValue().equals(
					((IDropDownBean) (((Visit) getPage().getVisit())
							.getDropDownBean2())).getValue())) {
				_xiangmChick = true;
			}
		} else {
			_xiangmChick = true;
		}
		
		((Visit) getPage().getVisit()).setDropDownBean2(value);
	}
	
	
	public IPropertySelectionModel getIZidSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getIZidSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	
	
	public void setIZidSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}
	
	public void getIZidSelectModels() {
		List _RenyList = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			if (getXiangmValue() != null) {
				if (((IDropDownBean) getXiangmValue()).getId() != -1) {
					
					String sql = "select keyzd from duanxsjb where id = "
							+ ((IDropDownBean) getXiangmValue()).getId();
					
					ResultSet rs = con.getResultSet(sql);
					
					while (rs.next()) {
						String[] _xiangm;
						String _xm = "";
						_xm = rs.getString("keyzd");
						if (_xm.equals("")) {
							return;
						}
						_xiangm = _xm.split(";");
						_RenyList.add(new IDropDownBean(-1, "-请选择项目-"));
						for (int i = 0; i < _xiangm.length; i++) {
							if (_xiangm[i].indexOf(",") >= 0) {
								String[] _m = _xiangm[i].split(",");
								_RenyList.add(new IDropDownBean(i, _m[1]));
							} else {
								_RenyList.add(new IDropDownBean(i, _xiangm[i]));
							}
						}
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			con.Close();
			setIZidSelectModel(new IDropDownModel(_RenyList));
		}
	}
	
	public SortMode getSort() {
	 	return SortMode.USER;
	}
	
	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt4(value);
	}
	
	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt4();
	}
	
	public int getEditTableRow() {
		return ((Visit) getPage().getVisit()).getInt3();
	}
	
	public void setEditTableRow(int value) {
		((Visit) getPage().getVisit()).setInt3(value);
	}
	
	public int getViewRow() {
		return ((Visit) getPage().getVisit()).getInt2();
	}
	
	public void setViewRow(int _value) {
		((Visit) getPage().getVisit()).setInt2(_value);
	}
	
	public boolean isEditSelectShow() {
		return ((Visit) getPage().getVisit()).getInt1() == 0;
	}
	
	public boolean isEditSelectShow1() {
		return ((Visit) getPage().getVisit()).getInt1() == 0;
	}
	
	public boolean isEditShow() {
		return ((Visit) getPage().getVisit()).getInt1() != 0;
	}
	
	public boolean isEditShow1() {
		return ((Visit) getPage().getVisit()).getInt1() != 0;
	}
	
	// 短信设置列表
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList3();
	}
	
	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList3(editList);
	}
	
	private Zidfdxszbean _EditValue;
	
	public Zidfdxszbean getEditValue() {
		return _EditValue;
	}
	
	public void setEditValue(Zidfdxszbean EditValue) {
		_EditValue = EditValue;
	}
	
	public void getSelectData() {
		JDBCcon conn = new JDBCcon();
//		Visit visit = (Visit) getPage().getVisit();
		List _returnl = new ArrayList();
		try {
			String sql = "select mingc,fassjh,id from duanxfsb";
			ResultSet rs = conn.getResultSet(sql);
			while (rs.next()) {
				String _mingc = rs.getString("mingc");
				String _sjh = rs.getString("fassjh");
				long _id = rs.getLong("id");
				_returnl.add(new Zidfdxszbean(_mingc, _sjh, _id));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (_returnl == null) {
				_returnl.add(new Zidfdxszbean());
			}
			if (_returnl != null && !_returnl.isEmpty()) {
				setEditTableRow(0);
			}
			setEditValues(_returnl);
			conn.Close();
		}
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setEditTableRow(-1);
			visit.setList1(null);
			visit.setList2(null);
			visit.setList3(null);
			visit.setList4(null);
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel5(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setInt4(0);
			visit.setInt1(0);
			getSelectData();
	
		}
	}
}
