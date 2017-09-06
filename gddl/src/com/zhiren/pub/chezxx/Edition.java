package com.zhiren.pub.chezxx;

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
public class Edition extends BasePage implements PageValidateListener {

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

	private int m_xuh;// 序号

	private long m_id;

	private String m_bianm;// 车站编码

	private String m_mingc;// 简称

	private String m_quanc;// 全称

	private String m_piny;// 拼音

	private String m_leib;// 类别(车站、港口)

	private String m_lujxxb_id;// 路局

	private String m_beiz;// 备注

	public long getMid() {
		return m_id;
	}

	public void setMid(long id) {
		this.m_id = id;
	}

	public int getXuh() {
		return m_xuh;
	}

	public void setXuh(int xuh) {
		this.m_xuh = xuh;
	}

	public String getBianm() {
		return m_bianm;
	}

	public void setBianm(String bianm) {
		this.m_bianm = bianm;
	}

	public String getMingc() {
		return m_mingc;
	}

	public void setMingc(String mingc) {
		this.m_mingc = mingc;
	}

	public String getQuanc() {
		return m_quanc;
	}

	public void setQuanc(String quanc) {
		this.m_quanc = quanc;
	}

	public String getPiny() {
		return m_piny;
	}

	public void setPiny(String piny) {
		this.m_piny = piny;
	}

	public String getLeib() {
		return m_leib;
	}

	public void setLeib(String leib) {
		this.m_leib = leib;
	}

	public String getLujxxb_id() {
		return m_lujxxb_id;
	}

	public void setLujxxb_id(String lujxxb_id) {
		this.m_lujxxb_id = lujxxb_id;
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

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		// ((Chezxxbean) _list.get(i)).getXXX();
		getSelectData();
		// setMsg();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		// List _list = ((Visit) getPage().getVisit()).getEditValues();
		// ((Chezxxbean) _list.get(i)).getXXX();
		JDBCcon con = new JDBCcon();
		try {
			long mid = -1;
			String str = "select decode(max(xuh), null, 1, 0, 1, max(xuh) + 1) as xuh from chezxxb";
			ResultSet rs = con.getResultSet(str);
			int mxuh = 0;
			while (rs.next()) {
				mxuh = rs.getInt("xuh");
			}
			rs.close();
			String mbianm = "";
			String mmingc = "";
			String mquanc = "";
			String mpiny = "";
			String mlujxxb_id = "";
			String mleib = "";
			String mbeiz = "";
			setMid(mid);
			setXuh(mxuh);
			setBianm(mbianm);
			setMingc(mmingc);
			setQuanc(mquanc);
			setPiny(mpiny);
			setLujxxb_id(mlujxxb_id);
			setLeib(mleib);
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
		con.getDelete("delete chezxxb where id = " + _id + " and "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id());
		con.getDelete("delete diancdzb where chezxxb_id = " + _id
				+ " and diancxxb_id = "
				+ ((Visit) getPage().getVisit()).getDiancxxb_id());
	}

	private void Save() {
		JDBCcon con = new JDBCcon();
		ResultSet rs;
		long diancxxb_id = ((Visit) getPage().getVisit()).getDiancxxb_id();
		try {
			long _id = getMid();
			int mxuh = getXuh();
			String mbianm = getBianm();
			String mmingc = getMingc();
			String mquanc = getQuanc();
			String mpiny = getPiny();
			String mleib = getLeibValue().getValue();
			long mlujxxb_id = getLujxxb_idValue().getId();
			String mbeiz = getBeiz();
			String Sql = "";
			String Sql1 = "";
			int flag = -1;
			setZt(false);
			if (_id == -1) {
				// 判断编码是否存在有重复数据
				Sql = "select bianm\n" + "  from chezxxb c\n"
						+ " where EXISTS (select *\n"
						+ "          from chezxxb\n"
						+ "         where c.bianm = bianm\n"
						+ "           and bianm = '" + mbianm + "')";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("编码存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				// 判断名称是否空数据
				if (isNull(getMingc())) {
					setMsg("名称不能为空！");
					con.rollBack();
					con.Close();
					return;
				} else {
					// 判断名称是否存在有重复数据
					Sql = "select mingc\n" + "  from chezxxb c\n"
							+ " where EXISTS (select *\n"
							+ "          from chezxxb\n"
							+ "         where c.mingc = mingc\n"
							+ "           and mingc = '" + mmingc + "')";
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
				}

				// 判断全称是否存在有重复数据
				Sql = "select quanc\n" + "  from chezxxb c\n"
						+ " where EXISTS (select *\n"
						+ "          from chezxxb\n"
						+ "         where c.quanc = quanc\n"
						+ "           and quanc = '" + mquanc + "')";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("全称存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}
				// 判断拼音是否存在有重复数据
				Sql = "select piny\n" + "  from chezxxb c\n"
						+ " where EXISTS (select *\n"
						+ "          from chezxxb\n"
						+ "         where c.piny = piny\n"
						+ "           and piny = '" + mpiny + "')";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("拼音存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					rs.close();
				}

				// 判断类别是否空数据
				if (isNull(mleib)) {
					setMsg("请选择类别！");
					con.rollBack();
					con.Close();
					return;
				}
				// 判断路局是否空数据
				if (mlujxxb_id == -1) {
					setMsg("请选择路局！");
					con.rollBack();
					con.Close();
					return;
				}
				_id = Long.parseLong(MainGlobal.getNewID(diancxxb_id));
				Sql = " insert  into chezxxb (ID,XUH,BIANM,MINGC,QUANC,PINY,LUJXXB_ID,LEIB,BEIZ) values("
						+ _id
						+ ","
						+ mxuh
						+ ",'"
						+ mbianm
						+ "','"
						+ mmingc
						+ "','"
						+ mquanc
						+ "','"
						+ mpiny
						+ "',"
						+ mlujxxb_id
						+ ",'" + mleib + "','" + mbeiz + "')";
				flag = con.getInsert(Sql);
				Sql1 = "insert into DIANCDZB (id,diancxxb_id,chezxxb_id,leib) values ("
						+ Long.parseLong(MainGlobal.getNewID(diancxxb_id))
						+ "," + diancxxb_id + "," + _id + ",'" + mleib + "')";
				flag = con.getInsert(Sql1);
			} else {
				Sql = "select mingc\n" + "  from chezxxb c\n"
						+ " where EXISTS (select *\n"
						+ "          from chezxxb\n"
						+ "         where c.mingc = mingc\n"
						+ "           and mingc = '" + mmingc + "' and id!="
						+ _id + ")";
				rs = con.getResultSet(Sql);
				if (rs.next()) {
					setMsg("名称存在重复！");
					rs.close();
					con.rollBack();
					con.Close();
					return;
				} else {
					Sql = " update   chezxxb  set  XUH=" + mxuh + ",BIANM='"
							+ mbianm + "',MINGC='" + mmingc + "',QUANC='"
							+ mquanc + "',PINY='" + mpiny + "',LUJXXB_ID="
							+ mlujxxb_id + ",LEIB='" + mleib + "',BEIZ='"
							+ mbeiz + "' where id=" + _id;
					flag = con.getUpdate(Sql);
					Sql1 = "update diancdzb set leib = '" + mleib
							+ "' where chezxxb_id = " + _id;
					flag = con.getUpdate(Sql1);
				}
				rs.close();
			}
			if (flag == -1) {
				setZt(false);
				System.out.println("数据存储错误!");
				setMsg("保存失败！");
				con.rollBack();
				con.Close();
				return;
			} else {
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
	}

	private void Skipping(IRequestCycle cycle) {
		cycle.activate("Chez");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon JDBCcon = new JDBCcon();
		try {
			getLujxxb_idModels();
			getLeibModels();
			long _id = visit.getLong1();
			String sql = "select * from chezxxb where id = " + _id;
			ResultSet rs = JDBCcon.getResultSet(sql);
			while (rs.next()) {
				setMid(rs.getLong("ID"));
				setXuh(rs.getInt("XUH"));
				setBianm(rs.getString("BIANM"));
				setMingc(rs.getString("MINGC"));
				setQuanc(rs.getString("QUANC"));
				setPiny(rs.getString("PINY"));
				setLujxxb_id(getProperValue(getLujxxb_idModel(), rs
						.getInt("LUJXXB_ID")));
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

	public IDropDownBean getLujxxb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			for (int i = 0; i < getLujxxb_idModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getLujxxb_idModel()
						.getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong3()) {
					((Visit) getPage().getVisit()).setDropDownBean1(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setLujxxb_idValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getLujxxb_idModel()
							.getOption(0));
		}
		((Visit) getPage().getVisit()).setDropDownBean1(Value);
	}

	public IPropertySelectionModel getLujxxb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getLujxxb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setLujxxb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public void getLujxxb_idModels() {
		String sql = "select id, mingc from lujxxb order by xuh,mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, null));
	}

	public IDropDownBean getLeibValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			for (int i = 0; i < getLeibModel().getOptionCount(); i++) {
				IDropDownBean idb = (IDropDownBean) getLeibModel().getOption(i);
				if (idb.getId() == ((Visit) getPage().getVisit()).getLong2()) {
					((Visit) getPage().getVisit()).setDropDownBean2(idb);
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setLeibValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getLeibModel().getOption(
							0));
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public IPropertySelectionModel getLeibModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getLeibModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setLeibModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getLeibModels() {
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
			visit.setDropDownBean1(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			visit.setList1(null);
			visit.getboolean1();
			if (visit.getboolean1() == true) {
				getSelectData();
			} else {
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
