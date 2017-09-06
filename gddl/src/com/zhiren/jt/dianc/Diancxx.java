package com.zhiren.jt.dianc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diancxx extends BasePage implements PageValidateListener {

	private int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}

	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	// private String isNullVal(String val) {
	// String result = "";
	// if (val != null) {
	// result = val;
	// }
	// return result;
	// }

	private void Save() {
		String strSql;
		List _value = getEditValues();
		JDBCcon con = new JDBCcon();
		long mid = -1;
		int mxuh = _value.size() + 1;
		String mbianm = "";
		String mmingc = "";
		String mquanc = "";
		String mpiny = "";
		String mshengfb_id = "";
		String mfuid = "";
		String mdiz = "";
		String myouzbm = "";
		String mzongj = "";
		String mranlcdh = "";
		int mzhuangjrl = 0;
		int mzuidkc = 0;
		int mzhengccb = 0;
		int mxianfhkc = 0;
		int mrijhm = 0;
		int mjingjcmsx = 0;
		int mjingjcmxx = 0;
		int mdongcmzb = 0;
		String mfaddbr = "";
		String mweitdlr = "";
		String mkaihyh = "";
		String mzhangh = "";
		String mdianh = "";
		String mshuih = "";
		String mjiexfs = "";
		int mjiexx = 0;
		int mjiexnl = 0;
		String mcaiyfs = "";
		String mjilfs = "";
		String mranlfzr = "";
		String mlianxdz = "";
		int mjingjcml = 0;
		String mjib = "";
		String mbeiz = "";
//		String mdianclbb_id = "";
//		String mdaoz = "";
//		String mdaog = "";
		for (int i = 0; i < _value.size(); i++) {
			mid = ((Diancxxbean) _value.get(i)).getId();
			mxuh = ((Diancxxbean) _value.get(i)).getXuh();
			mbianm = ((Diancxxbean) _value.get(i)).getBianm();
			mmingc = ((Diancxxbean) _value.get(i)).getMingc();
			mquanc = ((Diancxxbean) _value.get(i)).getQuanc();
			mpiny = ((Diancxxbean) _value.get(i)).getPiny();
			mshengfb_id = ((Diancxxbean) _value.get(i)).getShengfb_id();
			mfuid = ((Diancxxbean) _value.get(i)).getFuid();
			mdiz = ((Diancxxbean) _value.get(i)).getDiz();
			myouzbm = ((Diancxxbean) _value.get(i)).getYouzbm();
			mzongj = ((Diancxxbean) _value.get(i)).getZongj();
			mranlcdh = ((Diancxxbean) _value.get(i)).getRanlcdh();
			mzhuangjrl = ((Diancxxbean) _value.get(i)).getZhuangjrl();
			mzuidkc = ((Diancxxbean) _value.get(i)).getZuidkc();
			mzhengccb = ((Diancxxbean) _value.get(i)).getZhengccb();
			mxianfhkc = ((Diancxxbean) _value.get(i)).getXianfhkc();
			mrijhm = ((Diancxxbean) _value.get(i)).getRijhm();
			mjingjcmsx = ((Diancxxbean) _value.get(i)).getJingjcmsx();
			mjingjcmxx = ((Diancxxbean) _value.get(i)).getJingjcmxx();
			mdongcmzb = ((Diancxxbean) _value.get(i)).getDongcmzb();
			mfaddbr = ((Diancxxbean) _value.get(i)).getFaddbr();
			mweitdlr = ((Diancxxbean) _value.get(i)).getWeitdlr();
			mkaihyh = ((Diancxxbean) _value.get(i)).getKaihyh();
			mzhangh = ((Diancxxbean) _value.get(i)).getZhangh();
			mdianh = ((Diancxxbean) _value.get(i)).getDianh();
			mshuih = ((Diancxxbean) _value.get(i)).getShuih();
			mjiexfs = ((Diancxxbean) _value.get(i)).getJiexfs();
			mjiexx = ((Diancxxbean) _value.get(i)).getJiexx();
			mjiexnl = ((Diancxxbean) _value.get(i)).getJiexnl();
			mcaiyfs = ((Diancxxbean) _value.get(i)).getCaiyfs();
			mjilfs = ((Diancxxbean) _value.get(i)).getJilfs();
			mranlfzr = ((Diancxxbean) _value.get(i)).getRanlfzr();
			mlianxdz = ((Diancxxbean) _value.get(i)).getLianxdz();
			mjingjcml = ((Diancxxbean) _value.get(i)).getJingjcml();
			mjib = ((Diancxxbean) _value.get(i)).getJib();
			mbeiz = ((Diancxxbean) _value.get(i)).getBeiz();
			if (mid == -1) {
				strSql = "insert into diancxxb\n"
						+ " (ID,\n"
						+ " XUH,\n"
						+ " BIANM,\n"
						+ " MINGC,\n"
						+ " QUANC,\n"
						+ " PINY,\n"
						+ " SHENGFB_ID,\n"
						+ " FUID,\n"
						+ " DIZ,\n"
						+ " YOUZBM,\n"
						+ " ZONGJ,\n"
						+ " RANLCDH,\n"
						+ " ZHUANGJRL,\n"
						+ " ZUIDKC,\n"
						+ " ZHENGCCB,\n"
						+ " XIANFHKC,\n"
						+ " RIJHM,\n"
						+ " JINGJCMSX,\n"
						+ " JINGJCMXX,\n"
						+ " DONGCMZB,\n"
						+ " FADDBR,\n"
						+ " WEITDLR,\n"
						+ " KAIHYH,\n"
						+ " ZHANGH,\n"
						+ " DIANH,\n"
						+ " SHUIH,\n"
						+ " JIEXFS,\n"
						+ " JIEXX,\n"
						+ " JIEXNL,\n"
						+ " CAIYFS,\n"
						+ " JILFS,\n"
						+ " RANLFZR,\n"
						+ " LIANXDZ,\n"
						+ " JINGJCML,\n"
						+ " JIB,\n"
						+ " BEIZ)\n"
						+ "values\n"
						+ " ((select decode(id,0,100,(max(id)+1)) as id from diancxxb)"
						+ "," + mxuh + ",'" + mbianm + "','" + mmingc + "','"
						+ mquanc + "','" + mpiny + "'," + mshengfb_id + ","
						+ mfuid + ",'" + mdiz + "','" + myouzbm + "','"
						+ mzongj + "','" + mranlcdh + "'," + mzhuangjrl + ","
						+ mzuidkc + "," + mzhengccb + "," + mxianfhkc + ","
						+ mrijhm + "," + mjingjcmsx + "," + mjingjcmxx + ","
						+ mdongcmzb + ",'" + mfaddbr + "','" + mweitdlr + "','"
						+ mkaihyh + "','" + mzhangh + "','" + mdianh + "','"
						+ mshuih + "','" + mjiexfs + "'," + mjiexx + ","
						+ mjiexnl + ",'" + mcaiyfs + "','" + mjilfs + "','"
						+ mranlfzr + "','" + mlianxdz + "'," + mjingjcml + ","
						+ mjib + ",'" + mbeiz + "')";
				con.getInsert(strSql);
			} else {
				strSql = "update diancxxb set XUH = " + mxuh + ",BIANM = '"
						+ mbianm + "',MINGC = '" + mmingc + "',QUANC = '"
						+ mquanc + "'," + "PINY = '" + mpiny
						+ "',SHENGFB_ID = " + mshengfb_id + ",FUID = " + mfuid
						+ ",DIZ = '" + mdiz + "',YOUZBM = '" + myouzbm
						+ "',ZONGJ = '" + mzongj + "',RANLCDH = '" + mranlcdh
						+ "'," + "ZHUANGJRL = " + mzhuangjrl + ",ZUIDKC = "
						+ mzuidkc + ",ZHENGCCB = " + mzhengccb + ",XIANFHKC = "
						+ mxianfhkc + ",RIJHM = " + mrijhm + ",JINGJCMSX = "
						+ mjingjcmsx + ",JINGJCMXX = " + mjingjcmxx
						+ ",DONGCMZB = " + mdongcmzb + "," + "FADDBR = '"
						+ mfaddbr + "',WEITDLR = '" + mweitdlr + "',KAIHYH = '"
						+ mkaihyh + "',ZHANGH = '" + mzhangh + "',DIANH = '"
						+ mdianh + "',SHUIH = '" + mshuih + "',JIEXFS = '"
						+ mjiexfs + "'," + "JIEXX = " + mjiexx + ",JIEXNL = "
						+ mjiexnl + ",CAIYFS = '" + mcaiyfs + "',JILFS = '"
						+ mjilfs + "',RANLFZR = '" + mranlfzr + "',LIANXDZ = '"
						+ mlianxdz + "',JINGJCML = " + mjingjcml + ","
						+ "JIB = " + mjib + ",BEIZ = '" + mbeiz
						+ "' where id = " + mid;
				con.getUpdate(strSql);
			}
		}
		con.Close();
		getSelectData();
	}

	private void Delete() {

		JDBCcon con = new JDBCcon();
		int intRow = getEditTableRow();
		if (intRow != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (((Diancxxbean) _value.get(intRow)).getId() != -1) {
					String strSql = "delete from diancxxb where id="
							+ ((Diancxxbean) _value.get(intRow)).getId();
					con.getDelete(strSql);
					con.Close();
				}
				_value.remove(intRow);
				int t;
				int c = _value.size();
				for (int i = intRow; i < c; i++) {
					t = ((Diancxxbean) _value.get(i)).getXuh();
					((Diancxxbean) _value.get(i)).setXuh(t - 1);
				}
			}// 在数据库里在次更新，以防bug
			for (int j = 0; j < _value.size(); j++) {
				String sql = "update diancxxb set xuh="
						+ ((Diancxxbean) _value.get(j)).getXuh() + " where id="
						+ ((Diancxxbean) _value.get(j)).getId();
				con.getUpdate(sql);
			}
			con.Close();
		}

	}

	private void Insert() {
		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		long mid = -1;
		int mxuh = _value.size() + 1;
		String mbianm = "";
		String mmingc = "";
		String mquanc = "";
		String mpiny = "";
		String mshengfb_id = "";
		String mfuid = "";
		String mdiz = "";
		String myouzbm = "";
		String mzongj = "";
		String mranlcdh = "";
		int mzhuangjrl = 0;
		int mzuidkc = 0;
		int mzhengccb = 0;
		int mxianfhkc = 0;
		int mrijhm = 0;
		int mjingjcmsx = 0;
		int mjingjcmxx = 0;
		int mdongcmzb = 0;
		String mfaddbr = "";
		String mweitdlr = "";
		String mkaihyh = "";
		String mzhangh = "";
		String mdianh = "";
		String mshuih = "";
		String mjiexfs = "";
		int mjiexx = 0;
		int mjiexnl = 0;
		String mcaiyfs = "";
		String mjilfs = "";
		String mranlfzr = "";
		String mlianxdz = "";
		int mjingjcml = 0;
		String mjib = "";
		String mbeiz = "";
		String mdianclbb_id = "";
		String mdaoz = "";
		String mdaog = "";
		_value.add(new Diancxxbean(mid, mxuh, mbianm, mmingc, mquanc, mpiny,
				mshengfb_id, mfuid, mdiz, myouzbm, mzongj, mranlcdh,
				mzhuangjrl, mzuidkc, mzhengccb, mxianfhkc, mrijhm, mjingjcmsx,
				mjingjcmxx, mdongcmzb, mfaddbr, mweitdlr, mkaihyh, mzhangh,
				mdianh, mshuih, mjiexfs, mjiexx, mjiexnl, mcaiyfs, mjilfs,
				mranlfzr, mlianxdz, mjingjcml, mjib, mbeiz, mdianclbb_id,
				mdaoz, mdaog));
		setEditValues(_value);
	}

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Diancxxbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _EditionChick = false;

	public void EditionButton(IRequestCycle cycle) {
		_EditionChick = true;
	}

	private boolean _firstpagebutton = false;

	private boolean _uppagebutton = false;

	private boolean _downpagebutton = false;

	private boolean _lastpagebutton = false;

	private boolean _gopagebutton = false;

	public void FirstPageButton(IRequestCycle cycle) {
		_firstpagebutton = true;
	}

	public void UpPageButton(IRequestCycle cycle) {
		_uppagebutton = true;
	}

	public void DownPageButton(IRequestCycle cycle) {
		_downpagebutton = true;
	}

	public void LastPageButton(IRequestCycle cycle) {
		_lastpagebutton = true;
	}

	public void GoPageButton(IRequestCycle cycle) {
		_gopagebutton = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			 Save();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert();
		}
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_EditionChick) {
			_EditionChick = false;
			Edition(cycle);
		}
		if (_firstpagebutton) {
			ToFirstPage();
			_firstpagebutton = false;
		}
		if (_uppagebutton) {
			ToUpPage();
			_uppagebutton = false;
		}
		if (_downpagebutton) {
			ToDownPage();
			_downpagebutton = false;
		}
		if (_lastpagebutton) {
			ToLastPage();
			_lastpagebutton = false;
		}

		if (_gopagebutton) {
			GoPage();
			_gopagebutton = false;
		}
		if (ischanged) {
			getSelectData();
			ischanged = false;
		}
	}

	private Diancxxbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Diancxxbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Diancxxbean EditValue) {
		_EditValue = EditValue;
	}

	private void Edition(IRequestCycle cycle) {
		// 为 "编辑" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		// // ((Chezxxbean) _list.get(i)).getXXX();
		// int introw = getEditTableRow();
		// JDBCcon con = new JDBCcon();
		// boolean zhuangt = true;
		// if (introw != -1) {
		// if (((Xitxxbean) _list.get(introw)).getSel() == true) {
		// zhuangt = true;
		// long shangjdw = -1;
		// long _id = ((Xitxxbean) _list.get(introw)).getId();
		// long mdiancxxb_id = getProperId(getDiancxxb_idModel(),
		// ((Xitxxbean) _list.get(introw)).getDiancxxb_id());
		// long mzhuangt = getProperId(_IZhuangtModel, ((Xitxxbean) _list
		// .get(introw)).getZhuangt());
		// String sql = "select * from diancxxb where id = "
		// + mdiancxxb_id;
		// ResultSet rs = con.getResultSet(sql);
		// try {
		// while (rs.next()) {
		// shangjdw = rs.getLong("FUID");
		// }
		// rs.close();
		// } catch (SQLException e) {
		// // TODO 自动生成 catch 块
		// e.printStackTrace();
		// } finally {
		// con.Close();
		// }
		// ((Visit) getPage().getVisit()).setLong1(_id);
		// ((Visit) getPage().getVisit()).setLong2(shangjdw);
		// ((Visit) getPage().getVisit()).setLong3(mdiancxxb_id);
		// ((Visit) getPage().getVisit()).setLong4(mzhuangt);
		// ((Visit) getPage().getVisit()).setboolean1(zhuangt);
		// cycle.activate("YunslEdition");
		// }
		// } else {
		// setMsg("请选择要编辑的数据！");
		// }
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		ResultSet rs;
		StringBuffer SQL = new StringBuffer("");
		boolean hasData = false;
		try {
			getDaozModels();
			getDaogModels();
			StringBuffer sql = new StringBuffer("");
			int rowOfPage = this.getRowsOfPage();
			if (visit.isJTUser()) {
				if (getDiancValue().getId() == -1) {
					sql
							.append("select ceil(rownum/"
									+ rowOfPage
									+ ") as page,dc.*, sf.quanc as shengf, lb.mingc as leib\n");
					sql.append("  from diancxxb dc, shengfb sf, dianclbb lb\n");
					sql.append(" where dc.shengfb_id = sf.id\n");
					sql.append("   and dc.dianclbb_id = lb.id(+)");
				} else {
					sql.append("select ceil(rownum/" + rowOfPage
							+ ") as page,dianc.*\n");
					sql
							.append("  from (select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
					sql
							.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
					sql.append("         where dc.shengfb_id = sf.id\n");
					sql.append("           and dc.dianclbb_id = lb.id(+)\n");
					sql.append("           and dc.id = ");
					sql.append(getDiancValue().getId());
					sql.append("\n");
					sql.append("        union\n");
					sql
							.append("        select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
					sql
							.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
					sql.append("         where dc.shengfb_id = sf.id\n");
					sql.append("           and dc.dianclbb_id = lb.id(+)\n");
					sql.append("           and dc.fuid = ");
					sql.append(getDiancValue().getId() + ") dianc");
				}
			} else {
				if (visit.isGSUser()) {
					if (getDiancValue().getId() == -1) {
						sql.append("select ceil(rownum/" + rowOfPage
								+ ") as page,dianc.*\n");
						sql
								.append("  from (select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
						sql
								.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
						sql.append("         where dc.shengfb_id = sf.id\n");
						sql
								.append("           and dc.dianclbb_id = lb.id(+)\n");
						sql.append("           and dc.id = ");
						sql.append(visit.getDiancxxb_id());
						sql.append("\n");
						sql.append("        union\n");
						sql
								.append("        select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
						sql
								.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
						sql.append("         where dc.shengfb_id = sf.id\n");
						sql
								.append("           and dc.dianclbb_id = lb.id(+)\n");
						sql.append("           and dc.fuid = ");
						sql.append(visit.getDiancxxb_id() + ") dianc");
					} else {
						sql.append("select ceil(rownum/" + rowOfPage
								+ ") as page,dianc.*\n");
						sql
								.append("  from (select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
						sql
								.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
						sql.append("         where dc.shengfb_id = sf.id\n");
						sql
								.append("           and dc.dianclbb_id = lb.id(+)\n");
						sql.append("           and dc.id = ");
						sql.append(getDiancValue().getId());
						sql.append("\n");
						sql.append("        union\n");
						sql
								.append("        select dc.*, sf.quanc as shengf, lb.mingc as leib\n");
						sql
								.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
						sql.append("         where dc.shengfb_id = sf.id\n");
						sql
								.append("           and dc.dianclbb_id = lb.id(+)\n");
						sql.append("           and dc.fuid = ");
						sql.append(getDiancValue().getId() + ") dianc");
					}
				} else {
					sql
							.append("select ceil(rownum/"
									+ rowOfPage
									+ ") as page,dc.*, sf.quanc as shengf, lb.mingc as leib\n");
					sql
							.append("          from diancxxb dc, shengfb sf, dianclbb lb\n");
					sql.append("         where dc.shengfb_id = sf.id\n");
					sql.append("           and dc.dianclbb_id = lb.id(+)\n");
					sql.append("           and dc.id = ");
					sql.append(visit.getDiancxxb_id() + "");

				}
			}
			setSQL(sql);
			rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				int mpage = rs.getInt("PAGE");
				setPages(mpage);
				hasData = true;
			}
			this.setCurrentPage(0);
			this.setTotalPages(0);
			this.setGoPage(0);
			if (hasData) {
				this.setCurrentPage(1);
				this.setGoPage(1);
				SQL.delete(0, SQL.length());
				SQL.append("select count(*) as pages from (");
				SQL.append(sql);
				SQL.append(") t");
				CountPages(SQL);
				this.setEditValues(_editvalues);
				ToFirstPage();
			} else {
				this.setEditValues(_editvalues);
				this.setCurrentPage(1);
				this.setGoPage(1);
				this.setTotalPages(1);
				ToFirstPage();
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		setEditTableRow(-1);
	}

	private List getListValues(ResultSet rs) {
		List _editvalues = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			while (rs.next()) {

				long mid = rs.getLong("ID");
				int mxuh = rs.getInt("XUH");
				String mbianm = rs.getString("BIANM");
				String mmingc = rs.getString("MINGC");
				String mquanc = rs.getString("QUANC");
				String mpiny = rs.getString("PINY");
				String mshengfb_id = rs.getString("SHENGF");
				String mfuid = getProperValue(getDiancModel(), rs
						.getInt("FUID"));
				String mdiz = rs.getString("DIZ");
				String myouzbm = rs.getString("YOUZBM");
				String mzongj = rs.getString("ZONGJ");
				String mranlcdh = rs.getString("RANLCDH");
				int mzhuangjrl = rs.getInt("ZHUANGJRL");
				int mzuidkc = rs.getInt("ZUIDKC");
				int mzhengccb = rs.getInt("ZHENGCCB");
				int mxianfhkc = rs.getInt("XIANFHKC");
				int mrijhm = rs.getInt("RIJHM");
				int mjingjcmsx = rs.getInt("JINGJCMSX");
				int mjingjcmxx = rs.getInt("JINGJCMXX");
				int mdongcmzb = rs.getInt("DONGCMZB");
				String mfaddbr = rs.getString("FADDBR");
				String mweitdlr = rs.getString("WEITDLR");
				String mkaihyh = rs.getString("KAIHYH");
				String mzhangh = rs.getString("ZHANGH");
				String mdianh = rs.getString("DIANH");
				String mshuih = rs.getString("SHUIH");
				String mjiexfs = rs.getString("JIEXFS");
				int mjiexx = rs.getInt("JIEXX");
				int mjiexnl = rs.getInt("JIEXNL");
				String mcaiyfs = rs.getString("CAIYFS");
				String mjilfs = rs.getString("JILFS");
				String mranlfzr = rs.getString("RANLFZR");
				String mlianxdz = rs.getString("LIANXDZ");
				int mjingjcml = rs.getInt("JINGJCML");
				String mjib = getProperValue(getJibModel(), rs.getInt("JIB"));
				String mdianclbb_id = rs.getString("LEIB");
				String Sql = "select cz.quanc from chezxxb cz,diancdzb dz "
						+ " where cz.id = dz.chezxxb_id and dz.leib = '车站' and dz.diancxxb_id = "
						+ mid + "";
				ResultSet rss = con.getResultSet(Sql);
				String mdaoz = "";
				while (rss.next()) {
					mdaoz = rss.getString("quanc");
					String cy[] = mdaoz.split(",");
					mdaoz = "";
					for (int i = 0; i < cy.length; i++) {
						mdaoz += "" + cy[i] + ",";
					}
					mdaoz = mdaoz.substring(0, mdaoz.length() - 1);
				}
				rss.close();
				Sql = "select cz.quanc from chezxxb cz,diancdzb dz "
						+ " where cz.id = dz.chezxxb_id and dz.leib = '港口' and dz.diancxxb_id = "
						+ mid + "";
				rss = con.getResultSet(Sql);
				String mdaog = "";
				while (rss.next()) {
					mdaog = rss.getString("quanc");
					String cy[] = mdaog.split(",");
					mdaog = "";
					for (int i = 0; i < cy.length; i++) {
						mdaog += "" + cy[i] + ",";
					}
					mdaog = mdaog.substring(0, mdaog.length() - 1);
				}
				rss.close();
				String mbeiz = rs.getString("BEIZ");
				int mpage = rs.getInt("APAGE");
				Diancxxbean diancxx = new Diancxxbean(mid, mxuh, mbianm,
						mmingc, mquanc, mpiny, mshengfb_id, mfuid, mdiz,
						myouzbm, mzongj, mranlcdh, mzhuangjrl, mzuidkc,
						mzhengccb, mxianfhkc, mrijhm, mjingjcmsx, mjingjcmxx,
						mdongcmzb, mfaddbr, mweitdlr, mkaihyh, mzhangh, mdianh,
						mshuih, mjiexfs, mjiexx, mjiexnl, mcaiyfs, mjilfs,
						mranlfzr, mlianxdz, mjingjcml, mjib, mbeiz,
						mdianclbb_id, mdaoz, mdaog);
				setPages(mpage);
				_editvalues.add(diancxx);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _editvalues;
	}

	public IDropDownBean getDaozValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDaozModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDaozValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setDaozModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getDaozModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDaozModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getDaozModels() {
		String sql = "select id,quanc as daoz from chezxxb where leib='车站'";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "请选择"));
		return;
	}

	public IDropDownBean getDaogValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getDaogModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setDaogValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setDaogModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getDaogModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getDaogModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getDaogModels() {
		String sql = "select id,quanc as daoz from chezxxb where leib='港口'";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "请选择"));
		return;
	}

	private boolean ischanged;

	public IDropDownBean getDiancValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getDiancModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancValue(IDropDownBean Value) {
		if (Value != null && getDiancValue() != null) {
			if (Value.getId() != getDiancValue().getId()) {
				ischanged = true;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setDiancModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getDiancModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getDiancModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getDiancModels() {
		Visit visit = ((Visit) getPage().getVisit());
		String sql = "";
		if (visit.isJTUser()) {
			sql = "select id,mingc from diancxxb order by xuh,mingc,fuid";
		} else {
			if (visit.isGSUser()) {
				sql = "select dc.id, dc.mingc\n" + "     from (select *\n"
						+ "             from diancxxb\n"
						+ "            where id = " + visit.getDiancxxb_id()
						+ "\n" + "           union\n" + "           select *\n"
						+ "             from diancxxb\n"
						+ "            where jib > 1\n"
						+ "              and fuid = " + visit.getDiancxxb_id()
						+ ") dc\n" + "    order by dc.xuh, dc.mingc, dc.fuid";

			} else {
				sql = "select id,mingc from diancxxb where id = "
						+ visit.getDiancxxb_id();
			}
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, "全部"));
		return;
	}

	public IDropDownBean getDianclb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getDianclb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setDianclb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setDianclb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getDianclb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getDianclb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getDianclb_idModels() {
		String sql = "select id, mingc from dianclbb order by xuh, mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, "请选择"));
		return;
	}

	public IDropDownBean getJibValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getJibModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setJibValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setJibModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getJibModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getJibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getJibModels() {
		String sql = "select jb.id, jb.mingc\n"
				+ "  from (select 1 as id, decode(1, 1, '集团') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 2 as id, decode(1, 1, '公司') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 3 as id, decode(1, 1, '电厂') as mingc from dual) jb\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql));
		return;
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
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			getSelectData();

		}
	}

	// private String FormatDate(Date _date) {
	// if (_date == null) {
	// return MainGlobal.Formatdate("yyyy-MM-dd", new Date());
	// }
	// return MainGlobal.Formatdate("yyyy-MM-dd", _date);
	// }
	//
	private String getProperValue(IPropertySelectionModel _selectModel,
			long value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}

	// private long getProperId(IPropertySelectionModel _selectModel, String
	// value) {
	// int OprionCount;
	// OprionCount = _selectModel.getOptionCount();
	//
	// for (int i = 0; i < OprionCount; i++) {
	// if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
	// value)) {
	// return ((IDropDownBean) _selectModel.getOption(i)).getId();
	// }
	// }
	// return -1;
	// }

	private ResultSet getQueryResult(StringBuffer SQL) {
		ResultSet rs = null;
		JDBCcon con = new JDBCcon();
		rs = con.getResultSet(SQL);
		return rs;
	}

	private StringBuffer m_sql = new StringBuffer("");

	public StringBuffer getSQL() {
		return m_sql;
	}

	public void setSQL(StringBuffer sql) {
		m_sql = sql;
	}

	private int m_pages = 0;

	public int getPages() {
		return m_pages;
	}

	public void setPages(int pages) {
		m_pages = pages;
	}

	private int getRowsOfPage() {
		int rowsOfpage = 19;
		return rowsOfpage;
	}

	private int m_totalpages = 0;

	public int getTotalPages() {
		return m_totalpages;
	}

	public void setTotalPages(int _value) {
		m_totalpages = _value;
	}

	private int m_currentpage = 0;

	public int getCurrentPage() {
		return m_currentpage;
	}

	public void setCurrentPage(int _value) {
		m_currentpage = _value;
	}

	private int m_page = 1;

	public int getGoPage() {
		return m_page;
	}

	public void setGoPage(int page) {
		m_page = page;
	}

	private void ToFirstPage() {
		List _editvalues = new ArrayList();
		this.setEditValues(null);
		ResultSet rs = null;
		StringBuffer SQL = new StringBuffer("");
		int rowsOfpage = this.getRowsOfPage();
		SQL.append("select * from (select ceil(rownum / " + rowsOfpage
				+ ") as apage,t.* from (");
		SQL.append(this.getSQL());
		SQL.append(")t order by t.xuh,t.mingc) where apage = 1");
		rs = this.getQueryResult(SQL);
		_editvalues = getListValues(rs);
		this.setEditValues(_editvalues);
		this.setCurrentPage(1);
		rs = null;
	}

	private void ToUpPage() {
		int currentPage = this.getCurrentPage();
		if (1 < currentPage) {
			// currentPage = currentPage - 1;
			int startPage = currentPage - 1;
			int endPage = currentPage;
			// 显示上一页的内容
			List _editvalues = new ArrayList();
			this.setEditValues(null);
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage >= ");
			SQL.append(startPage);
			SQL.append(" and apage < ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(startPage);
			rs = null;
		} else {
			setMsg("已到达首页！");
		}
	}

	private void ToDownPage() {
		int currentPage = this.getCurrentPage();
		if (currentPage < this.getTotalPages()) {
			int startPage = currentPage;
			int endPage = currentPage + 1;
			// 显示下一页的内容
			List _editvalues = new ArrayList();
			this.setEditValues(null);
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage > ");
			SQL.append(startPage);
			SQL.append(" and apage <= ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(endPage);
		} else {
			setMsg("已到达末尾页！");
		}
	}

	private void ToLastPage() {
		// if (this.getCurrentPage() < this.getTotalPages()) {
		// 显示最后一页的内容
		int startPage = this.getTotalPages() - 1;
		int endPage = this.getTotalPages();
		this.setEditValues(null);
		List _editvalues = new ArrayList();
		this.setEditValues(null);
		ResultSet rs = null;
		StringBuffer SQL = new StringBuffer("");
		int rowsOfpage = this.getRowsOfPage();
		SQL.append("select * from (select ceil(rownum / " + rowsOfpage
				+ ") as apage,t.* from (");
		SQL.append(this.getSQL());
		SQL.append(")t order by t.xuh,t.mingc) where apage > ");
		SQL.append(startPage);
		SQL.append(" and apage <= ");
		SQL.append(endPage);
		rs = this.getQueryResult(SQL);
		_editvalues = getListValues(rs);
		this.setEditValues(_editvalues);
		this.setCurrentPage(this.getTotalPages());
		// }

	}

	private void GoPage() {
		int goPage = this.getGoPage();
		if (1 <= goPage && goPage <= this.getTotalPages()) {
			int startPage = goPage;
			int endPage = goPage + 1;
			this.setEditValues(null);
			List _editvalues = new ArrayList();
			ResultSet rs = null;
			StringBuffer SQL = new StringBuffer("");
			int rowsOfpage = this.getRowsOfPage();
			SQL.append("select * from (select ceil(rownum / " + rowsOfpage
					+ ") as apage,t.* from (");
			SQL.append(this.getSQL());
			SQL.append(")t order by t.xuh,t.mingc) where apage >= ");
			SQL.append(startPage);
			SQL.append(" and apage < ");
			SQL.append(endPage);
			rs = this.getQueryResult(SQL);
			_editvalues = getListValues(rs);
			this.setEditValues(_editvalues);
			this.setCurrentPage(this.getGoPage());
		}

	}

	private void CountPages(StringBuffer sql) {
		JDBCcon conn = new JDBCcon();
		ResultSet rs = null;
		rs = conn.getResultSet(sql);
		try {
			if (rs.next()) {
				this.setTotalPages(rs.getInt("pages") / this.getRowsOfPage()
						+ 1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}