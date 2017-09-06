package com.zhiren.pub.xitxx;

import java.sql.ResultSet;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author 王刚
 * 
 */
public class XitxxEdition extends BasePage implements PageValidateListener {

	private static int _editTableRow = -1;// 编辑框中选中的行

	public int getEditTableRow() {
		return _editTableRow;
	}

	public void setEditTableRow(int _value) {
		_editTableRow = _value;
	}

	boolean zt = true;

	public boolean getZt() {
		return zt;
	}

	public void setZt(boolean zt) {
		this.zt = zt;
	}

	private long m_mid;//

	private int m_xuh;// 序号

	private String m_diancxxb_id;//

	private String m_shangjdw;

	private String m_mingc;// 名称

	private String m_zhi;// 值

	private String m_danw;// 单位

	private String m_leib;// 类别

	private String m_zhuangt;// 状态

	private String m_beiz;// 备注

	public long getMid() {
		return m_mid;
	}

	public void setMid(long id) {
		this.m_mid = id;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public String getDiancxxb_id() {
		return m_diancxxb_id;
	}

	public void setDiancxxb_id(String diancxxb_id) {
		this.m_diancxxb_id = diancxxb_id;
	}

	public String getShangjdw() {
		return m_shangjdw;
	}

	public void setShangjdw(String shangjdw) {
		this.m_shangjdw = shangjdw;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getZhi() {
		return m_zhi;
	}

	public void setZhi(String zhi) {
		this.m_zhi = zhi;
	}

	public String getDanw() {
		return m_danw;
	}

	public void setDanw(String danw) {
		this.m_danw = danw;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getZhuangt() {
		return m_zhuangt;
	}

	public void setZhuangt(String zhuangt) {
		this.m_zhuangt = zhuangt;
	}

	public String getBeiz() {
		return m_beiz;
	}

	public void setBeiz(String beiz) {
		this.m_beiz = beiz;
	}

	private boolean m_sel;

	public boolean getSel() {
		return m_sel;
	}

	public void setSel(boolean sel) {
		this.m_sel = sel;
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

	public static boolean isNull(String value) {
		if (value == null) {
			return true;
		} else if (value.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	// private Xitxxbean _EditValue;
	//
	// public List getEditValues() {
	// return ((Visit) getPage().getVisit()).getList2();
	// }
	//
	// public void setEditValues(List editList) {
	// ((Visit) getPage().getVisit()).setList2(editList);
	// }
	//
	// public Xitxxbean getEditValue() {
	// return _EditValue;
	// }
	//
	// public void setEditValue(Xitxxbean EditValue) {
	// _EditValue = EditValue;
	// }

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		getSelectData();
		// setMsg();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList2();
		JDBCcon con = new JDBCcon();
		try {
			String str = "select decode(max(xuh), null, 1, 0, 1, max(xuh) + 1) as xuh from chezxxb";
			ResultSet rs = con.getResultSet(str);
			int mxuh = 0;
			while (rs.next()) {
				mxuh = rs.getInt("xuh");
			}
			rs.close();
			long mid = -1;
			String mdiancxxb_id = "";
			String mmingc = "";
			String mzhi = "";
			String mdanw = "";
			String mleib = "";
			String mzhuangt = "";
			String mbeiz = "";
			setMid(mid);
			setXuh(mxuh);
			setMingc(mmingc);
			setZhi(mzhi);
			setDanw(mdanw);
			setDiancxxb_id(mdiancxxb_id);
			setLeib(mleib);
			setZhuangt(mzhuangt);
			setBeiz(mbeiz);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long _id = 0;
		_id = ((Visit) getPage().getVisit()).getLong1();
		con.getDelete("delete xitxxb where id = " + _id);
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			int mxuh = getXuh();
			long mid = getMid();
			long mdiancxxb_id = getDiancxxbDropDownValue().getId();
			String mmingc = getMingc();
			String mzhi = getZhi();
			String mdanw = getDanw();
			long mzhuangt = getZhuangtValue().getId();
			String mleib = getLeib();
			String mbeiz = getBeiz();
			String Sql = "";
			int flag = -1;
			ResultSet rs;
			setZt(false);
			if (mid == -1) {

				if (getDiancxxb_idValue().getId() == -1) {
					setMsg("请选择上级单位！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mdiancxxb_id == -1) {
					setMsg("请选择使用电厂！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mzhuangt == -1) {
					setMsg("请选择状态！");
					con.rollBack();
					con.Close();
					return;
				}
				Sql = "select *\n" + "  from xitxxb x\n"
						+ " where EXISTS (select *\n"
						+ "          from xitxxb\n"
						+ "         where diancxxb_id = " + mdiancxxb_id + "\n"
						+ "           and leib = '" + mleib + "'\n"
						+ "           and x.mingc = '" + mmingc + "')";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("名称存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				mid = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
				Sql = " insert  into xitxxb (ID,XUH,DIANCXXB_ID,MINGC,ZHI,DANW,LEIB,ZHUANGT,BEIZ) values("
						+ mid
						+ ","
						+ mxuh
						+ ","
						+ mdiancxxb_id
						+ ",'"
						+ mmingc
						+ "','"
						+ mzhi
						+ "','"
						+ mdanw
						+ "','"
						+ mleib
						+ "',"
						+ mzhuangt + ",'" + mbeiz + "')";
				flag = con.getInsert(Sql);
			} else {
				if (getDiancxxb_idValue().getId() == -1) {
					setMsg("请选择上级单位！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mdiancxxb_id == -1) {
					setMsg("请选择使用电厂！");
					con.rollBack();
					con.Close();
					return;
				}
				if (mzhuangt == -1) {
					setMsg("请选择状态！");
					con.rollBack();
					con.Close();
					return;
				}
				Sql = "select *\n" + "  from xitxxb x\n"
						+ " where EXISTS (select *\n"
						+ "          from xitxxb\n"
						+ "         where diancxxb_id = " + mdiancxxb_id + "\n"
						+ "           and leib = '" + mleib + "'\n"
						+ "           and x.mingc = '" + mmingc + "')";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("名称存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				Sql = " update   xitxxb  set  XUH=" + mxuh + ",DIANCXXB_ID="
						+ mdiancxxb_id + ",MINGC='" + mmingc + "',ZHI='" + mzhi
						+ "',DANW='" + mdanw + "',LEIB='" + mleib
						+ "',ZHUANGT=" + mzhuangt + ",BEIZ='" + mbeiz
						+ "' where id=" + mid;
				flag = con.getUpdate(Sql);
			}
			if (flag == -1) {
				setZt(false);
				System.out.println("数据存储错误!");
				setMsg("保存失败！");
				con.rollBack();
				con.Close();
				return;
			} else {
				setMsg("保存成功！");
				setZt(true);
				con.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public String getArrayScript() {
		StringBuffer array = new StringBuffer();
		Visit visit = (Visit) getPage().getVisit();
		boolean xianszt = visit.getboolean1();
		array.append("var xianszt = " + xianszt + ";\n");
		return array.toString();
	}

	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ReturnsChick = false;

	public void ReturnsButton(IRequestCycle cycle) {
		_ReturnsChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
			Skipping(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			if (getZt() == true) {
				Skipping(cycle);
			}
		}
		if (_ReturnsChick) {
			_ReturnsChick = false;
			Skipping(cycle);
		}
		if (ischanged) {
			// setDiancxxbDropDownValue(this.getDiancxxbDropDownValue());
			// this.getDiancxxb_idModels();
			getSelectData();
			ischanged = false;
		}
	}

	private void Skipping(IRequestCycle cycle) {
		cycle.activate("Xitxx");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon JDBCcon = new JDBCcon();
		try {
			// getDiancxxbDropDownModels();
			getDiancxxb_idModels();
			getZhuangtModels();
			long _id = visit.getLong1();
			String sql = "select x.*, d.id as dcid, g.id as fuid\n"
					+ "  from xitxxb x,\n"
					+ "       diancxxb d,\n"
					+ "       (select id, mingc as GTMC\n"
					+ "          from diancxxb d\n"
					+ "         where EXISTS\n"
					+ "         (select distinct fuid from diancxxb where fuid = d.id)\n"
					+ "         order by xuh, mingc) g\n"
					+ " where x.diancxxb_id = d.id\n"
					+ "   and g.id = d.fuid\n" + "   and x.id = " + _id + "\n"
					+ " order by x.leib, x.xuh, x.mingc";
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				setMid(rs.getLong("ID"));
				setXuh(rs.getInt("XUH"));
				setMingc(rs.getString("MINGC"));
				setZhi(rs.getString("ZHI"));
				setDanw(rs.getString("DANW"));
				setZhuangt(getProperValue(getZhuangtModel(), rs
						.getInt("ZHUANGT")));
				setShangjdw(getProperValue(getDiancxxbDropDownModel(), rs
						.getInt("FUID")));
				setDiancxxb_id(getProperValue(getDiancxxb_idModel(), rs
						.getInt("DCID")));
				setLeib(rs.getString("LEIB"));
				setBeiz(rs.getString("BEIZ"));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
	}

	private boolean ischanged;

	public IDropDownBean getDiancxxbDropDownValue() {
		// ((Visit) getPage().getVisit()).getLong2();
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			for (int i = 0; i < getDiancxxbDropDownModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getDiancxxbDropDownModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong2()) {
					((Visit) getPage().getVisit()).setDropDownBean3(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setDiancxxbDropDownValue(IDropDownBean Value) {
		if (Value != null && getDiancxxbDropDownValue() != null) {
			if (Value.getId() != getDiancxxbDropDownValue().getId()) {
				ischanged = true;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getDiancxxbDropDownModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getDiancxxbDropDownModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getDiancxxbDropDownModels() {
		Visit visit = ((Visit) getPage().getVisit());
		String sql = "";
		if (visit.isJTUser()) {
			sql = "select id,mingc\n"
					+ "  from diancxxb d\n"
					+ " where EXISTS (select distinct fuid from diancxxb where fuid = d.id)\n"
					+ " order by xuh, mingc";
		} else {
			if (visit.isGSUser()) {
				sql = "select dc.id, dc.mingc\n"
						+ "  from (select id, mingc, fuid\n"
						+ "          from diancxxb d\n"
						+ "         where EXISTS (select distinct fuid from diancxxb where fuid = d.id)\n"
						+ "         order by xuh, mingc) qu,\n"
						+ "       diancxxb dc\n" + " where dc.fuid = qu.id\n"
						+ "   and dc.fuid = " + visit.getDiancxxb_id() + "\n"
						+ " order by dc.xuh, dc.mingc";

			} else {
				sql = "select id, mingc from diancxxb where id = "
						+ visit.getDiancxxb_id();

			}
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, null));
	}

	public IDropDownBean getDiancxxb_idValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getDiancxxb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getDiancxxb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong3()) {
					((Visit) getPage().getVisit()).setDropDownBean1(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDiancxxb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getDiancxxb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getDiancxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getDiancxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setDiancxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getDiancxxb_idModels() {
		long mid = -2;
		if (getDiancxxbDropDownValue() != null) {
			mid = getDiancxxbDropDownValue().getId();
		}
		String sql = "select id,mingc from diancxxb where fuid = " + mid
				+ " order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	public IDropDownBean getZhuangtValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getZhuangtModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getZhuangtModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong4()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setZhuangtValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getZhuangtModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getZhuangtModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getZhuangtModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setZhuangtModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getZhuangtModels() {
		String sql = "select z.*\n"
				+ "  from (select 0 as id, decode(1,1,'不可编辑') as mingc\n"
				+ "          from dual\n"
				+ "        union\n"
				+ "        select 1 as id, decode(1,1,'可以编辑') as mingc from dual) z\n"
				+ " order by id, mingc";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, null));
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
			visit.setProSelectionModel1(null);
			visit.setProSelectionModel2(null);
			visit.setProSelectionModel3(null);
			visit.setDropDownBean1(null);
			visit.setDropDownBean2(null);
			visit.setDropDownBean3(null);
			visit.setList1(null);
			boolean zt = visit.getboolean1();
			// List list = visit.getList2();
			if (zt == true) {
				getSelectData();
			} else {
				// setEditValues(list);
				Insert();
			}
		}
	}

	private String getProperValue(IPropertySelectionModel _selectModel,
			int value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getId() == value) {
				return ((IDropDownBean) _selectModel.getOption(i)).getValue();
			}
		}
		return null;
	}
	//
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
}
