package com.zhiren.jt.jihgl.niancgjh;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.zhiren.common.DateUtil;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;

public class Niancgjh extends BasePage  implements PageValidateListener{

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

	private void Refurbish() {
		// 为 "刷新" 按钮添加处理程序
		// List _list =((Visit) getPage().getVisit()).getEditValues();
		// ((Dinghjhbbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Copylastyear() {
		((Visit) getPage().getVisit()).setList1(null);

		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			long mxuh = 0;

			if (this.getGongysmcValue() == null || getGongysmcValue().getId() == -1) {
				setMsg("请选择煤矿");
			} else {
				long intyear;
				if (getNianfValue() == null) {
					intyear = DateUtil.getYear(new Date()) - 1;
				} else {
					intyear = getNianfValue().getId() - 1;
				}
				StringBuffer sql = new StringBuffer();
				double yuef1 = 0.00;
				double yuef2 = 0.00;
				double yuef3 = 0.00;
				double yuef4 = 0.00;
				double yuef5 = 0.00;
				double yuef6 = 0.00;
				double yuef7 = 0.00;
				double yuef8 = 0.00;
				double yuef9 = 0.00;
				double yuef10 = 0.00;
				double yuef11 = 0.00;
				double yuef12 = 0.00;

				int leib = ((Visit) getPage().getVisit()).getRenyjb();
				String diancid = "";
				String tiaoj="";
				if (leib == 3) {//电厂用户
					diancid = " and n.diancxxb_id="
							+ ((Visit) getPage().getVisit()).getDiancxxb_id()
							+ " ";
				}else if(leib==2){//分公司
					 tiaoj=",diancxxb d";
					diancid="and n.diancxxb_id=d.id and d.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"";
				}else if(leib==1){//集团用户
					 tiaoj=",diancxxb d";
					 diancid="and n.diancxxb_id=d.id and d.fuid= "+this.getFengsValue().getId()+"";
				}

				sql.append("select distinct n.diancxxb_id from niancgjhb n "+tiaoj+"");
				sql.append(" where to_char(n.riq,'yyyy')='" + intyear
						+ "' and gongysb_id=" + getGongysmcValue().getId()
						+ " " + diancid + "");
				if (!JDBCcon.getHasIt(sql.toString())) {
					setMsg("找不到所需数据!");
				} else {
					ResultSet rs = JDBCcon.getResultSet(sql.toString());
					while (rs.next()) {
						StringBuffer sql1 = new StringBuffer();
						long dianchangid = rs.getLong("diancxxb_id");
						int yuef = 0;
						String dianchangmc = "";
						long meikdqid = 0;
						String meikdqmc = "";
						/*
						sql1.append("select dh.meikdqb_id,mk.meikdqmc,dh.diancxxb_id,dc.jianc,dh.hej,to_char(dh.riq,'mm') as yuef");
						sql1.append(" from dinghjhb dh,meikdqb mk,diancxxb dc");
						sql1.append(" where dh.meikdqb_id=mk.id and dh.diancxxb_id=dc.id and diancxxb_id="+ dianchangid + "");
						sql1.append(" and to_char(dh.riq,'yyyy')='" + intyear+ "' and meikdqb_id="+ getGongysmcValue().getId() + "");
						System.out.println(sql1.toString());
						*/
								sql1.append("select n.gongysb_id,\n");
								sql1.append("       g.mingc,\n");
								sql1.append("       n.diancxxb_id,\n");
								sql1.append("       dc.mingc as jianc,\n");
								sql1.append("       n.hej,\n");
								sql1.append("       to_char(n.riq, 'mm') as yuef\n");
								sql1.append("  from niancgjhb n, gongysb g, diancxxb dc\n");
								sql1.append(" where n.gongysb_id = g.id\n");
								sql1.append("   and n.diancxxb_id = dc.id\n");
								sql1.append("   and n.diancxxb_id = "+dianchangid+"\n");
								sql1.append("   and to_char(n.riq, 'yyyy') = '"+intyear+"'\n");
								sql1.append("   and n.gongysb_id = "+ getGongysmcValue().getId()+"");

						
						
						
						ResultSet rs1 = JDBCcon.getResultSet(sql1.toString());

						while (rs1.next()) {
							yuef = rs1.getInt("yuef");
							dianchangmc = rs1.getString("jianc");
							meikdqid = rs1.getLong("gongysb_id");
							meikdqmc = rs1.getString("mingc");
							switch (yuef) {
							case 1:
								yuef1 = rs1.getDouble("hej");
								break;
							case 2:
								yuef2 = rs1.getDouble("hej");
								break;
							case 3:
								yuef3 = rs1.getDouble("hej");
								break;
							case 4:
								yuef4 = rs1.getDouble("hej");
								break;
							case 5:
								yuef5 = rs1.getDouble("hej");
								break;
							case 6:
								yuef6 = rs1.getDouble("hej");
								break;
							case 7:
								yuef7 = rs1.getDouble("hej");
								break;
							case 8:
								yuef8 = rs1.getDouble("hej");
								break;
							case 9:
								yuef9 = rs1.getDouble("hej");
								break;
							case 10:
								yuef10 = rs1.getDouble("hej");
								break;
							case 11:
								yuef11 = rs1.getDouble("hej");
								break;
							case 12:
								yuef12 = rs1.getDouble("hej");
								break;

							default:
								break;
							}
						}
						_editvalues.add(new Niancgjhbean(++mxuh, dianchangid,
								dianchangmc, meikdqid, meikdqmc, intyear,
								yuef1, yuef2, yuef3, yuef4, yuef5, yuef6,
								yuef7, yuef8, yuef9, yuef10, yuef11, yuef12));
						rs1.close();
					}
					rs.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Niancgjhbean());
		}
		setEditTableRow(-1);
		((Visit) getPage().getVisit()).setList1(_editvalues);
		((Visit) getPage().getVisit()).getList1();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		if (getGongysmcValue() == null || getGongysmcValue().getId() == -1) {
			setMsg("请选择煤矿");
			return;
		}
		if (this.getFengsValue() == null || getFengsValue().getId() == -1) {
			setMsg("请选择分公司");
			return;
		}
		long meikdqid = getGongysmcValue().getId();
		String meikdqmc = getGongysmcValue().getValue();
		
		
		String fengsid = "";
		if (isDiancUser()) {// 电厂用户
			fengsid = " and d.id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		if (this.isJitUserShow()) {// 集团用户显示分公司的下拉框
			fengsid = "and d.fuid= " + this.getFengsValue().getId();
		}
		if (this.isGongsUser()) {// 分公司用户
			fengsid = "and d.fuid="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		long intyear = 0;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long mxuh = 0;

		StringBuffer sql = new StringBuffer();
		int leib = ((Visit) getPage().getVisit()).getRenyjb();
		String diancid = "";
		if (leib == 3) {
			diancid = " d.id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id() + " and ";
		}

		try {
			if(getMeikdqjh()){
				sql.append(" select d.id,d.mingc from diancxxb d,dianckjpxb px where " + diancid+ " d.id not in(");
				sql.append(" select distinct n.diancxxb_id from niancgjhb n where to_char(n.riq,'yyyy')='"+ intyear + "'");
				sql.append(" and n.gongysb_id=").append(getGongysmcValue().getId()).append(")");
				sql.append(" " + fengsid + " and d.id=px.diancxxb_id and px.kouj='月报' order by px.xuh ");
			}else{
				sql.append(" select d.id,d.mingc from diancxxb d where " + diancid+ " d.id not in(");
				sql.append(" select distinct n.diancxxb_id from niancgjhb n where to_char(n.riq,'yyyy')='"+ intyear + "'");
				sql.append(" and n.gongysb_id=").append(getGongysmcValue().getId()).append(")");
				sql.append(" " + fengsid + " order by d.id desc");
			}
			ResultSet rs = JDBCcon.getResultSet(sql.toString());
			while (rs.next()) {
				long dianchangid = rs.getLong("id");
				String dianchangmc = rs.getString("mingc");
				_editvalues.add(new Niancgjhbean(++mxuh, dianchangid,
						dianchangmc, meikdqid, meikdqmc, 0, 0.00, 0.00, 0.00,
						0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00));
			}
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		setEditTableRow(-1);
		((Visit) getPage().getVisit()).setList1(_editvalues);
		((Visit) getPage().getVisit()).getList1();
		if (_editvalues == null || _editvalues.size() == 0) {
			getSelectData();
		}
		// getSelectData();
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		int introw = getEditTableRow();
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (getEditValues() != null && !getEditValues().isEmpty()) {
					long diancxxb_id = ((Niancgjhbean) _value.get(introw))
							.getDianchangid();
					long meikdqb_id = ((Niancgjhbean) _value.get(introw))
							.getMeikdqid();
					long nianf = ((Niancgjhbean) _value.get(introw)).getNianf();
					if (nianf != 0) {
						JDBCcon con = new JDBCcon();
						con
								.getDelete("delete from niancgjhb n where n.diancxxb_id="
										+ diancxxb_id
										+ " and n.gongysb_id="
										+ meikdqb_id
										+ " and  to_char(n.riq,'yyyy')='"
										+ nianf + "'");
						con.Close();
					}
				}
				_value.remove(introw);
			}
			for (int i = 0; i < _value.size(); i++) {
				((Niancgjhbean) _value.get(i)).setXuh(i + 1);
			}
		}
	}

	private void Save() {
		List _list = ((Visit) getPage().getVisit()).getList1();
		JDBCcon con = new JDBCcon();
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		try {
			// 直接保存
			long diancxxb_id = 0;
			long gongysbid = 0;
			double yuef1 = 0.00;
			double yuef2 = 0.00;
			double yuef3 = 0.00;
			double yuef4 = 0.00;
			double yuef5 = 0.00;
			double yuef6 = 0.00;
			double yuef7 = 0.00;
			double yuef8 = 0.00;
			double yuef9 = 0.00;
			double yuef10 = 0.00;
			double yuef11 = 0.00;
			double yuef12 = 0.00;
			String nianf = "";
			if (_list != null || _list.size() != 0) {
				for (int i = 0; i < _list.size(); i++) {
					diancxxb_id = ((Niancgjhbean) _list.get(i))
							.getDianchangid();
					gongysbid = ((Niancgjhbean) _list.get(i)).getMeikdqid();
					yuef1 = ((Niancgjhbean) _list.get(i)).getYue1();
					yuef2 = ((Niancgjhbean) _list.get(i)).getYue2();
					yuef3 = ((Niancgjhbean) _list.get(i)).getYue3();
					yuef4 = ((Niancgjhbean) _list.get(i)).getYue4();
					yuef5 = ((Niancgjhbean) _list.get(i)).getYue5();
					yuef6 = ((Niancgjhbean) _list.get(i)).getYue6();
					yuef7 = ((Niancgjhbean) _list.get(i)).getYue7();
					yuef8 = ((Niancgjhbean) _list.get(i)).getYue8();
					yuef9 = ((Niancgjhbean) _list.get(i)).getYue9();
					yuef10 = ((Niancgjhbean) _list.get(i)).getYue10();
					yuef11 = ((Niancgjhbean) _list.get(i)).getYue11();
					yuef12 = ((Niancgjhbean) _list.get(i)).getYue12();
					double yuef[] = { yuef1, yuef2, yuef3, yuef4, yuef5, yuef6,
							yuef7, yuef8, yuef9, yuef10, yuef11, yuef12 };

					for (int j = 0; j < yuef.length; j++) {
						nianf = "" + intyear + "-" + (j + 1) + "-01";

						String sql = "select * from niancgjhb n where n.gongysb_id="
								+ gongysbid
								+ " and n.diancxxb_id="
								+ diancxxb_id
								+ " and "
								+ " riq=to_date('"
								+ nianf + "','yyyy-mm-dd')";
						String Sql = "";
						if (!con.getHasIt(sql)) {
							String _id = MainGlobal.getNewID(((Visit) getPage()
									.getVisit()).getDiancxxb_id());
							Sql = " insert into niancgjhb (id,gongysb_id,diancxxb_id,hej,riq) values ("
									+ _id
									+ ","
									+ gongysbid
									+ ","
									+ diancxxb_id
									+ ","
									+ yuef[j]
									+ ",to_date('"
									+ nianf
									+ "','yyyy-mm-dd'))";
							con.getInsert(Sql);
						} else {
							Sql = "update niancgjhb set hej=" + yuef[j]
									+ " where gongysb_id=" + gongysbid
									+ " and diancxxb_id=" + diancxxb_id
									+ " and riq=to_date('" + nianf
									+ "','yyyy-mm-dd')";
							con.getUpdate(Sql);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	public List getSelectData() {
		((Visit) getPage().getVisit()).setList1(null);
		List _editvalues = new ArrayList();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			long mxuh = 0;
			//long fengsid = this.getFengsValue().getId();
			String fengsid = "";
			if (isDiancUser()) {// 电厂用户
				fengsid = " and d.id="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			
			if (this.isJitUserShow()) {// 集团用户显示分公司的下拉框
				fengsid = "and d.fuid= " + this.getFengsValue().getId();
			}
			if (this.isGongsUser()) {// 分公司用户
				fengsid = "and d.fuid="
						+ ((Visit) getPage().getVisit()).getDiancxxb_id();
			}
			long intyear;
			if (getNianfValue() == null) {
				intyear = DateUtil.getYear(new Date());
			} else {
				intyear = getNianfValue().getId();
			}
			StringBuffer sql = new StringBuffer();
			double yuef1 = 0.00;
			double yuef2 = 0.00;
			double yuef3 = 0.00;
			double yuef4 = 0.00;
			double yuef5 = 0.00;
			double yuef6 = 0.00;
			double yuef7 = 0.00;
			double yuef8 = 0.00;
			double yuef9 = 0.00;
			double yuef10 = 0.00;
			double yuef11 = 0.00;
			double yuef12 = 0.00;
			
			if(getMeikdqjh()){
				sql.append("select distinct px.xuh,n.diancxxb_id from niancgjhb n,diancxxb d,dianckjpxb px ");
				sql.append(" where to_char(n.riq,'yyyy')='" + intyear+ "' and n.gongysb_id=" + getGongysmcValue().getId() + "");
				sql.append(" and n.diancxxb_id=d.id and d.id=px.diancxxb_id and px.kouj='月报' " + fengsid + "  order by px.xuh ");
			}else{
				sql.append("select distinct n.diancxxb_id from niancgjhb n,diancxxb d ");
				sql.append(" where to_char(n.riq,'yyyy')='" + intyear+ "' and n.gongysb_id=" + getGongysmcValue().getId() + "");
				sql.append(" and n.diancxxb_id=d.id " + fengsid + "");
			}
			
//			System.out.println(sql.toString());

			if (!JDBCcon.getHasIt(sql.toString())) {

			} else {
				ResultSet rs = JDBCcon.getResultSet(sql.toString());
				while (rs.next()) {
					StringBuffer sql1 = new StringBuffer();
					long dianchangid = rs.getLong("diancxxb_id");
					int yuef = 0;
					String dianchangmc = "";
					long gongysbid = 0;
					String meikdqmc = "";

					if(getMeikdqjh()){
						sql1.append("select n.gongysb_id,\n");
						sql1.append("       g.mingc as gongysmc,\n");
						sql1.append("       n.diancxxb_id,\n");
						sql1.append("       dc.mingc,\n");
						sql1.append("       n.hej,\n");
						sql1.append("       to_char(n.riq, 'mm') as yuef\n");
						sql1.append("  from niancgjhb n, gongysb g, diancxxb dc,dianckjpxb px \n");
						sql1.append(" where n.gongysb_id = g.id\n");
						sql1.append("   and n.diancxxb_id = dc.id and dc.id=px.diancxxb_id and px.kouj='月报'\n");
						sql1.append("   and n.diancxxb_id = " + dianchangid + "\n");
						sql1.append("   and to_char(n.riq, 'yyyy') = '" + intyear+ "'\n");
						sql1.append("   and gongysb_id = "	+ getGongysmcValue().getId()+ " order by px.xuh");
					}else{
						sql1.append("select n.gongysb_id,\n");
						sql1.append("       g.mingc as gongysmc,\n");
						sql1.append("       n.diancxxb_id,\n");
						sql1.append("       dc.mingc,\n");
						sql1.append("       n.hej,\n");
						sql1.append("       to_char(n.riq, 'mm') as yuef\n");
						sql1.append("  from niancgjhb n, gongysb g, diancxxb dc\n");
						sql1.append(" where n.gongysb_id = g.id\n");
						sql1.append("   and n.diancxxb_id = dc.id\n");
						sql1.append("   and diancxxb_id = " + dianchangid + "\n");
						sql1.append("   and to_char(n.riq, 'yyyy') = '" + intyear+ "'\n");
						sql1.append("   and gongysb_id = "	+ getGongysmcValue().getId()+ " order by dc.id desc");
					}
//					 System.out.println(sql1.toString());
					ResultSet rs1 = JDBCcon.getResultSet(sql1.toString());

					while (rs1.next()) {
						yuef = rs1.getInt("yuef");
						dianchangmc = rs1.getString("mingc");
						gongysbid = rs1.getLong("gongysb_id");
						meikdqmc = rs1.getString("gongysmc");
						switch (yuef) {
						case 1:
							yuef1 = rs1.getDouble("hej");
							break;
						case 2:
							yuef2 = rs1.getDouble("hej");
							break;
						case 3:
							yuef3 = rs1.getDouble("hej");
							break;
						case 4:
							yuef4 = rs1.getDouble("hej");
							break;
						case 5:
							yuef5 = rs1.getDouble("hej");
							break;
						case 6:
							yuef6 = rs1.getDouble("hej");
							break;
						case 7:
							yuef7 = rs1.getDouble("hej");
							break;
						case 8:
							yuef8 = rs1.getDouble("hej");
							break;
						case 9:
							yuef9 = rs1.getDouble("hej");
							break;
						case 10:
							yuef10 = rs1.getDouble("hej");
							break;
						case 11:
							yuef11 = rs1.getDouble("hej");
							break;
						case 12:
							yuef12 = rs1.getDouble("hej");
							break;

						default:
							break;
						}
					}
					_editvalues.add(new Niancgjhbean(++mxuh, dianchangid,
							dianchangmc, gongysbid, meikdqmc, intyear, yuef1,
							yuef2, yuef3, yuef4, yuef5, yuef6, yuef7, yuef8,
							yuef9, yuef10, yuef11, yuef12));
					rs1.close();
				}
				rs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Niancgjhbean());
		}
		setEditTableRow(-1);
		((Visit) getPage().getVisit()).setList1(_editvalues);
		return ((Visit) getPage().getVisit()).getList1();
	}

	// ///////////////////////////////////////////////////////////////////////////
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	private boolean _InsertChick = false;

	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}

	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _CopyChick = false;

	public void CopyButton(IRequestCycle cycle) {
		_CopyChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
		if (_InsertChick) {
			_InsertChick = false;
			Insert();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Delete();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CopyChick) {
			_CopyChick = false;
			Copylastyear();
		}
	}

	// //////////////////////////////////////////////////////////////////
	private Niancgjhbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Niancgjhbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Niancgjhbean EditValue) {
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
			sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";
			// System.out.println(sql);
			ResultSet rs = con.getResultSet(sql);
			for (int i = 0; rs.next(); i++) {
				fahdwList.add(new IDropDownBean(i, rs.getString("mingc")));
			}

			_IDiancmcModel = new IDropDownModel(fahdwList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IDiancmcModel;
	}

	// 供应商名称
	public boolean _gongysmcchange = false;

	private IDropDownBean _GongysmcValue;

	public IDropDownBean getGongysmcValue() {
		if (_GongysmcValue == null) {
			_GongysmcValue = (IDropDownBean) getGongysmcModels().getOption(0);
		}
		return _GongysmcValue;
	}

	public void setGongysmcValue(IDropDownBean Value) {
		long id = -2;
		if (_GongysmcValue != null) {
			id = _GongysmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_gongysmcchange = true;
			} else {
				_gongysmcchange = false;
			}
		}
		_GongysmcValue = Value;
	}

	private IPropertySelectionModel _GongysmcModel;

	public void setGongysmcModel(IPropertySelectionModel value) {
		_GongysmcModel = value;
	}

	public IPropertySelectionModel getGongysmcModel() {
		if (_GongysmcModel == null) {
			getGongysmcModels();
		}
		return _GongysmcModel;
	}

	public IPropertySelectionModel getGongysmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
/*
*huochaoyuan
*2009-10-22修改供应商下拉框排列顺序，方便录入，估计将来会被拼音索引功能取代
*/			
/*
 * lizengqiang
 * 2010-02-08
 * 修改供应商下拉框可以只显示六位编码的供应商名称，并且按照名称排序
 */			
			if(getMeikdqjh()){
				sql = "select id,mingc from gongysb where length(bianm)=6 order by mingc";
			}else{
				sql = "select id,mingc from gongysb order by fuid";
			}
			_GongysmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _GongysmcModel;
	}
	
	
	public void setMeikkdqjh(String value){
		((Visit)getPage().getVisit()).setString5(value);
	}
	public boolean getMeikdqjh(){
		if(((Visit)getPage().getVisit()).getString5().equals("")){
			getMeikdqjhs();
		}
		if(("是").equals(((Visit)getPage().getVisit()).getString5())){
			return true;
		}else{
			return false;
		}
	}
	public void getMeikdqjhs(){
		JDBCcon con = new JDBCcon();
		String strYesOrNo = "";
		String sql = "select * from xitxxb xt where xt.mingc='年计划分解录入供应商下拉框只显示六位编码供应商'";
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			strYesOrNo = rsl.getString("zhi");
		}
		setMeikkdqjh(strYesOrNo);
	}

	// 年份
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

	// //////////////////////////////////////////////////////////////////////////
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).getList1();
			((Visit) getPage().getVisit()).setString5("");
			visit.setList1(null);
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			setGongysmcValue(null);
			getGongysmcModels();
			getFengsModels();
			setNianfValue(null);
			getNianfModels();
			
			this.getSelectData();
		}
		if (_gongysmcchange) {
			getSelectData();
			_gongysmcchange = false;
		}
		if (_fengschange) {
			this.getSelectData();
			_fengschange = false;
		}
		if (nianfchanged) {
			this.getSelectData();
			nianfchanged = false;
		}

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
	// private String FormatDate(Date _date) {
	// if (_date == null) {
	// return MainGlobal.Formatdate("yyyy-MM-dd", new Date());
	// }
	// return MainGlobal.Formatdate("yyyy-MM-dd", _date);
	// }
}