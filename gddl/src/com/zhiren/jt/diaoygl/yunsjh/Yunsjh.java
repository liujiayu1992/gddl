package com.zhiren.jt.diaoygl.yunsjh;

import org.apache.tapestry.html.BasePage;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.pub.lujxx.Lujxxbean;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;

public class Yunsjh extends BasePage implements PageValidateListener {
//	 判断是否是集团用户
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// 集团

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// 公司
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// 电厂
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
		// ((Yunsjhhtbean) _list.get(i)).getXXX();
		getSelectData();
	}

	private void Insert() {
		// 为 "添加" 按钮添加处理程序
		JDBCcon JDBCcon = new JDBCcon();
		int leib = ((Visit) getPage().getVisit()).getRenyjb();

		long diancid = -1;
		String diancmc = "";
		long meikdqid = 0;
		String meikdqmc = "";

		if (getMeikdqmcValue() == null || getMeikdqmcValue().getId() == -1) {
			setMsg("请选择煤矿");
			return;
		} else {
			meikdqid = getMeikdqmcValue().getId();
			meikdqmc = getMeikdqmcValue().getValue();
		}

		long intYear;
		if (getNianfValue() == null) {
			intYear = DateUtil.getYear(new Date());
		} else {
			intYear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		
		if (getDiancmcValue() == null || getDiancmcValue().getId() == -1) {
			setMsg("请选择电厂");
			return;
		} else {
			diancid = getDiancmcValue().getId();
			diancmc = getDiancmcValue().getValue();
		}
		
		String daoz = "";
		try {
			StringBuffer sql = new StringBuffer();
			
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+diancid+"");

			ResultSet rs = JDBCcon.getResultSet(sql.toString());
			
			while (rs.next()) {
				daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}

		List _value = getEditValues();
		if (_value == null) {
			_value = new ArrayList();
		}
		Yunsjhbean yb = new Yunsjhbean();
		yb.setNianf(intYear);
		yb.setYuef(intMonth);
		yb.setDianchangmc(diancmc);
		yb.setDianchangid(diancid);
		yb.setMeikdqid(meikdqid);
		yb.setMeikdqmc(meikdqmc);
		yb.setTielj("请选择");
		yb.setFazmc("请选择");
		yb.setDaozmc(daoz);
		yb.setPinm("请选择");
		yb.setHuanzg("请选择");
		yb.setZhongdg("请选择");
		_value.add(yb);
		((Visit) getPage().getVisit()).setList1(_value);

		int size = getEditValues().size();
		for (int i = 0; i < size; i++) {
			((Yunsjhbean) getEditValues().get(i)).setXuh(i + 1);
		}
	}

	private void Delete() {
		// 为 "删除" 按钮添加处理程序
		int introw = getEditTableRow();
		JDBCcon con = new JDBCcon();
		if (introw != -1) {
			List _value = getEditValues();
			if (_value != null) {
				if (getEditValues() != null && !getEditValues().isEmpty()) {
					long id = ((Yunsjhbean) getEditValues().get(introw))
							.getId();
					if (id != 0) {

						con.getDelete("delete from yunsjhb where id=" + id);
						con.Close();
					}
				}
				_value.remove(introw);
			}
			for (int i = 0; i < _value.size(); i++) {
				((Yunsjhbean) _value.get(i)).setXuh(i + 1);
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
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		getIMeizModels();
		getIChezmcModels();
		getIGangkmcModels();
		try {
			long id = 0;
			long dianchangid = 0;
			long meikdqid = 0;
			// long nianf=0;
			// long yuef=0;
			long fazid = 0;
			long daozid = 0;
			long pinm = 0;
			long pic = 0;
			long pid = 0;
			String pizjhh = "";
			long huanzg = 0;
			long zhongdg = 0;
			String zibbcc = "";
			long shunh = 0;
			long tielj = 0;

			String sql = "";
			// 直接保存
	
			if (_list != null || _list.size() != 0) {
				for (int i = 0; i < _list.size(); i++) {
					id = ((Yunsjhbean) _list.get(i)).getId();
					dianchangid = getDiancmcValue().getId();
					meikdqid = ((Yunsjhbean) _list.get(i)).getMeikdqid();
					// nianf=((Yunsjhhtbean) _list.get(i)).getNianf();
					// yuef=((Yunsjhhtbean) _list.get(i)).getYuef();
					tielj = getProperId(_ITieljModel, ((Yunsjhbean) _list
							.get(i)).getTielj());
					
					fazid = getProperId(_IChezmcModel, ((Yunsjhbean) _list
							.get(i)).getFazmc());
					
					daozid = getProperId(_IChezmcModel, ((Yunsjhbean) _list
							.get(i)).getDaozmc());
					pinm = getProperId(_IMeizModel, ((Yunsjhbean) _list.get(i))
							.getPinm());
					
					pic = ((Yunsjhbean) _list.get(i)).getPic();
					pid = ((Yunsjhbean) _list.get(i)).getPid();
					pizjhh = ((Yunsjhbean) _list.get(i)).getPizjhh();
					huanzg = getProperId(_IGangkmcModel, ((Yunsjhbean) _list
							.get(i)).getHuanzg());
					zhongdg = getProperId(_IGangkmcModel, ((Yunsjhbean) _list
							.get(i)).getZhongdg());
					zibbcc = ((Yunsjhbean) _list.get(i)).getZibbcc();
					shunh = ((Yunsjhbean) _list.get(i)).getShunh();
					

					if (id == 0) {

						sql = " insert into yunsjhb (ID,RIQ,GONGYSB_ID,DIANCXXB_ID,FAZ_ID,DAOZ_ID,PINM,PIC,PID,PIZJHH,HUANZG,ZONGDG,ZIBCCC,SHUNH,TIELJ,leix) values ( "
								+ MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id())
								+ ",to_date('"
								+ intyear
								+ "-"
								+ intMonth
								+ "-01','yyyy-mm-dd'),"
								+ meikdqid
								+ ","
								+ dianchangid
								+ ","
								+ fazid
								+ ","
								+ daozid
								+ ","
								+ pinm
								+ ","
								+ pic
								+ ","
								+ pid
								+ ",'"
								+ pizjhh
								+ "',"
								+ huanzg
								+ ","
								+ zhongdg
								+ ",'"
								+ zibbcc + "'," + shunh + "," + tielj + ",1)";
						con.getInsert(sql);
					} else {
						sql = " update yunsjhb set riq=to_date('" + intyear
								+ "-" + intMonth
								+ "-01','yyyy-mm-dd'),GONGYSB_ID=" + meikdqid
								+ ",DIANCXXB_ID=" + dianchangid + ",FAZ_ID="
								+ fazid + ",DAOZ_ID=" + daozid + ",PINM="
								+ pinm + ",PIC=" + pic + ",PID=" + pid
								+ ",PIZJHH='" + pizjhh + "',HUANZG=" + huanzg
								+ ",ZONGDG=" + zhongdg + ",ZIBCCC='" + zibbcc
								+ "',SHUNH=" + shunh + ",TIELJ=" + tielj
								+ " where id=" + id;
						con.getUpdate(sql);
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
		int leib = ((Visit) getPage().getVisit()).getRenyjb();
		JDBCcon JDBCcon = new JDBCcon();
		try {

			long mxuh = 0;
			long diancid = 0;
			
			if (getDiancmcValue() == null
					|| getDiancmcValue().getId() == -1) {
				setMsg("请选择电厂");
			} else {
				diancid = getDiancmcValue().getId();
			}
			

			if (getMeikdqmcValue() == null || getMeikdqmcValue().getId() == -1) {
				setMsg("请选择煤矿");
			} else {
				long intyear;
				if (getNianfValue() == null) {
					intyear = DateUtil.getYear(new Date());
				} else {
					intyear = getNianfValue().getId();
				}
				long intMonth;
				if (getYuefValue() == null) {
					intMonth = DateUtil.getMonth(new Date());
				} else {
					intMonth = getYuefValue().getId();
				}
				StringBuffer sql = new StringBuffer();
				
				sql.append("select yh.id,yh.gongysb_id as meikdqb_id,mk.mingc as meikdqmc,yh.diancxxb_id,dc.mingc as jianc,lj.mingc as tielj,\n");
				sql.append("yh.faz_id,(select cz.mingc from chezxxb cz where yh.faz_id = cz.id) as fazmc,yh.daoz_id,\n");
				sql.append("(select cz.mingc from chezxxb cz where yh.daoz_id = cz.id) as daozmc,mz.mingc as pinm,yh.pic,yh.pid,yh.pizjhh,yh.zibccc,\n");
				sql.append("yh.shunh,(select cz.mingc from chezxxb cz where yh.huanzg = cz.id and leib = '港口') as huanzg,\n");
				sql.append("(select cz.mingc from chezxxb cz where yh.zongdg = cz.id and leib = '港口') as zongdg\n");
				sql.append("  from yunsjhb yh, diancxxb dc, gongysb mk, lujxxb lj, meizb mz\n");
				sql.append(" where yh.diancxxb_id = dc.id and yh.gongysb_id = mk.id and yh.tielj = lj.id and yh.pinm = mz.id and riq = to_date('" + intyear + "-" + intMonth+ "-01','yyyy-mm-dd')\n");
				sql.append("   and yh.diancxxb_id = " + diancid+ " and yh.gongysb_id = " + getMeikdqmcValue().getId()+ "  and yh.leix=1 order by yh.id ");

				ResultSet rs = JDBCcon.getResultSet(sql.toString());

				while (rs.next()) {
					long id = rs.getLong("id");
					long dianchangid = rs.getLong("diancxxb_id");
					String diancmc = rs.getString("jianc");
					long meikdqid = rs.getLong("meikdqb_id");
					String meikmc = rs.getString("meikdqmc");
					// long nianf=0;
					// long yuef=0;
					long fazid = rs.getLong("faz_id");
					String fazmc = rs.getString("fazmc");
					long daozid = rs.getLong("daoz_id");
					String daozmc = rs.getString("daozmc");
					String pinm = rs.getString("pinm");
					long pic = rs.getLong("pic");
					long pid = rs.getLong("pid");
					String pizjhh = rs.getString("pizjhh");
					String huanzg = rs.getString("huanzg");
					String zhongdg = rs.getString("zongdg");
					String zibbcc = rs.getString("zibccc");
					long shunh = rs.getLong("shunh");
					String tielj = rs.getString("tielj");

					_editvalues.add(new Yunsjhbean(id, ++mxuh, dianchangid,
							diancmc, meikdqid, meikmc, intyear, intMonth,
							fazid, fazmc, daozid, daozmc, pinm, pic, pid,
							pizjhh, huanzg, zhongdg, zibbcc, shunh, tielj));
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			JDBCcon.Close();
		}
		if (_editvalues == null) {
			_editvalues.add(new Yunsjhbean());
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

	}

	// //////////////////////////////////////////////////////////////////
	private Yunsjhbean _EditValue;

	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Yunsjhbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Yunsjhbean EditValue) {
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
		String sql = "";
		String fenggsId="";
		if(isJitUserShow()){
			 fenggsId ="d.fuid="+ this.getFengsValue().getId();
		}else if(isGongsUser()){
			 fenggsId = "d.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}else if(isDiancUser()){
			 fenggsId = "d.id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		
		
		sql = "select d.id,d.mingc from diancxxb d where " + fenggsId
				+ " order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
		return _IDiancmcModel;
	}

	// 矿别名称
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
			sql = "select id,mingc from gongysb order by mingc";
			_IMeikdqmcModel = new IDropDownModel(sql);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeikdqmcModel;
	}

	// 路局名称
	public boolean _tieljchange = false;

	private IDropDownBean _TieljValue;

	public IDropDownBean getTieljValue() {
		if (_TieljValue == null) {
			_TieljValue = (IDropDownBean) getITieljModels().getOption(0);
		}
		return _TieljValue;
	}

	public void setTieljValue(IDropDownBean Value) {
		long id = -2;
		if (_TieljValue != null) {
			id = _TieljValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_tieljchange = true;
			} else {
				_tieljchange = false;
			}
		}
		_TieljValue = Value;
	}

	private IPropertySelectionModel _ITieljModel;

	public void setITieljModel(IPropertySelectionModel value) {
		_ITieljModel = value;
	}

	public IPropertySelectionModel getITieljModel() {
		if (_ITieljModel == null) {
			getITieljModels();
		}
		return _ITieljModel;
	}

	public IPropertySelectionModel getITieljModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from lujxxb order by mingc";
			_ITieljModel = new IDropDownModel(sql,null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _ITieljModel;
	}

	// 车站名称
	public boolean _Chezmcchange = false;

	private IDropDownBean _ChezmcValue;

	public IDropDownBean getChezmcValue() {
		if (_ChezmcValue == null) {
			_ChezmcValue = (IDropDownBean) getIChezmcModels().getOption(0);
		}
		return _ChezmcValue;
	}

	public void setChezmcValue(IDropDownBean Value) {
		long id = -2;
		if (_ChezmcValue != null) {
			id = _ChezmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Chezmcchange = true;
			} else {
				_Chezmcchange = false;
			}
		}
		_ChezmcValue = Value;
	}

	private IPropertySelectionModel _IChezmcModel;

	public void setIChezmcModel(IPropertySelectionModel value) {
		_IChezmcModel = value;
	}

	public IPropertySelectionModel getIChezmcModel() {
		if (_IChezmcModel == null) {
			getIChezmcModels();
		}
		return _IChezmcModel;
	}

	public IPropertySelectionModel getIChezmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from chezxxb order by mingc";
			_IChezmcModel = new IDropDownModel(sql,null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IChezmcModel;
	}

	// 港口名称
	public boolean _Gangkmcchange = false;

	private IDropDownBean _GangkmcValue;

	public IDropDownBean getGangkmcValue() {
		if (_GangkmcValue == null) {
			_GangkmcValue = (IDropDownBean) getIGangkmcModels().getOption(0);
		}
		return _GangkmcValue;
	}

	public void setGangkmcValue(IDropDownBean Value) {
		long id = -2;
		if (_GangkmcValue != null) {
			id = _GangkmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Gangkmcchange = true;
			} else {
				_Gangkmcchange = false;
			}
		}
		_GangkmcValue = Value;
	}

	private IPropertySelectionModel _IGangkmcModel;

	public void setIGangkmcModel(IPropertySelectionModel value) {
		_IGangkmcModel = value;
	}

	public IPropertySelectionModel getIGangkmcModel() {
		if (_IGangkmcModel == null) {
			getIGangkmcModels();
		}
		return _IGangkmcModel;
	}

	public IPropertySelectionModel getIGangkmcModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from chezxxb where leib ='港口' order by mingc";
			_IGangkmcModel = new IDropDownModel(sql,null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IGangkmcModel;
	}

	// 煤种名称
	public boolean _meizchange = false;

	private IDropDownBean _MeizValue;

	public IDropDownBean getMeizValue() {
		if (_MeizValue == null) {
			_MeizValue = (IDropDownBean) getIMeizModels().getOption(0);
		}
		return _TieljValue;
	}

	public void setMeizValue(IDropDownBean Value) {
		long id = -2;
		if (_MeizValue != null) {
			id = _MeizValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meizchange = true;
			} else {
				_meizchange = false;
			}
		}
		_MeizValue = Value;
	}

	private IPropertySelectionModel _IMeizModel;

	public void setIMeizModel(IPropertySelectionModel value) {
		_IMeizModel = value;
	}

	public IPropertySelectionModel getIMeizModel() {
		if (_IMeizModel == null) {
			getIMeizModels();
		}
		return _IMeizModel;
	}

	public IPropertySelectionModel getIMeizModels() {
		JDBCcon con = new JDBCcon();
		try {

			String sql = "";
			sql = "select id,mingc from meizb order by mingc";
			_IMeizModel = new IDropDownModel(sql,null);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return _IMeizModel;
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

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;

	public IDropDownBean getYuefValue() {
		if (_YuefValue == null) {
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (getYuefValue() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}

	// //////////////////////////////////////////////////////////////////////////
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {

			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setMeikdqmcValue(null);
			getIMeikdqmcModels();
			setDiancmcValue(null); 
			getIDiancmcModels();
			setMeizValue(null);
			getIMeizModels();
			setChezmcValue(null);
			getIChezmcModels();
			setGangkmcValue(null);
			getIGangkmcModels();
			this.setNianfValue(null);
			this.getNianfModels();
			
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			this.getYuefModel();
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).getList1();
			getSelectData();
		}
		if (_fengschange) {

			this.setDiancmcValue(null);
			getIDiancmcModels();
			getSelectData();
			_fengschange = false;

		}
		if(nianfchanged){
			this.getSelectData();
			nianfchanged=false;
		}
		if(Changeyuef){
			this.getSelectData();
			Changeyuef=false;
		}
		if(_diancmcchange){
			this.getSelectData();
			_diancmcchange=false;
		}
		if(_meikdqmcchange){
			this.getSelectData();
			_meikdqmcchange=false;
		}
	
	}

	// private String FormatDate(Date _date) {
	// if (_date == null) {
	// return MainGlobal.Formatdate("yyyy-MM-dd", new Date());
	// }
	// return MainGlobal.Formatdate("yyyy-MM-dd", _date);
	// }

	/**
	 * 路局下框
	 * 
	 * @return
	 */
	public String getArrayScript() {
		JDBCcon con = new JDBCcon();
		StringBuffer array = new StringBuffer();
		StringBuffer sbSql = new StringBuffer();
		try {
			int i = 0;
			array.append(" arrZhan = new Array();\n");
			array.append(" arrluj = new Array();\n");
			sbSql = new StringBuffer();
			sbSql.append("select 0,id,mingc from lujxxb order by mingc");
			ResultSet ljrs = con.getResultSet(sbSql.toString());
			i = 0;
			while (ljrs.next()) {
				array.append("arrluj[");
				array.append(i++);
				array.append("] = new Array(\"");
				array.append(ljrs.getString(1));
				array.append("\",\"");
				array.append(ljrs.getString(2));
				array.append("\",\"");
				array.append(ljrs.getString(3));
				array.append("\");\n");
			}
			ljrs.close();
			array.append(" arrZhan[0] = new Array(\"LujDropDown\",arrluj);\n");
			array.append(" arrchez = new Array();\n");
			sbSql = new StringBuffer();
			sbSql
					.append("select lujxxb_id,id,mingc from chezxxb order by lujxxb_id,mingc");
			ResultSet czrs = con.getResultSet(sbSql.toString());
			i = 0;
			while (czrs.next()) {
				array.append("arrchez[");
				array.append(i++);
				array.append("] = new Array(\"");
				array.append(czrs.getString(1));
				array.append("\",\"");
				array.append(czrs.getString(2));
				array.append("\",\"");
				array.append(czrs.getString(3));
				array.append("\");\n");
			}
			czrs.close();
			array.append(" arrZhan[1] = new Array(\"FAZselect\",arrchez);\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		return array.toString();
	}

	private long getProperId(IPropertySelectionModel _selectModel, String value) {
		int OprionCount;
		OprionCount = _selectModel.getOptionCount();

		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) _selectModel.getOption(i)).getValue().equals(
					value)) {
				return ((IDropDownBean) _selectModel.getOption(i)).getId();
			}
		}
		return -1;
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

	// 得到登陆用户所在电厂或者分公司的名称
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
		}
		cn.Close();
		return diancmc;

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