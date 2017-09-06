package com.zhiren.jt.jiesgl.jieslc;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.main.Visit;

public class Jieslc extends BasePage {
	
	public String getPageHome() {
		if (((Visit) getPage().getVisit()).getboolean5()) {
			return "window.location = '" + MainGlobal.getHomeContext(this)
					+ "';";
		} else {
			return "";
		}
	}

	public boolean isQuanxkz() {
		return ((Visit) getPage().getVisit()).getboolean4();
	}

	public void setQuanxkz(boolean value) {
		((Visit) getPage().getVisit()).setboolean4(value);
	}

	public int getEditTableRow() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	public void setEditTableRow(int _value) {
		((Visit) getPage().getVisit()).setInt1(_value);
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getXiaox() {
		if (((Visit) getPage().getVisit()).getString1() == null) {
			((Visit) getPage().getVisit()).setString1("");
		}
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setXiaox(String xiaox) {
		((Visit) getPage().getVisit()).setString1(xiaox);
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}

	private boolean TijButton = false;

	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}

	private boolean HuitButton = false;

	public void HuitButton(IRequestCycle cycle) {
		HuitButton = true;
	}

	private boolean chakwbButton = false;

	public void chakwbButton(IRequestCycle cycle) {
		chakwbButton = true;
	}

	// private boolean ChexButton = false;
	// public void ChexButton(IRequestCycle cycle) {
	// ChexButton = true;
	// }
	public void submit(IRequestCycle cycle) {
		if (TijButton) {
			TijButton = false;
			tij();
		}
		if (HuitButton) {
			HuitButton = false;
			huit();
		}
		if (chakwbButton) {
			chakwbButton = false;
			chakwb();
		}
		// if (ChexButton) {
		// ChexButton = false;
		// Chex();
		// }
	}

	private Jieslcbean _EditValue;

	public List getEditValues() {
		if (((Visit) getPage().getVisit()).getList1() == null) {
			((Visit) getPage().getVisit()).setList1(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	public Jieslcbean getEditValue() {
		return _EditValue;
	}

	public void setEditValue(Jieslcbean EditValue) {
		_EditValue = EditValue;
	}

	public void getSelectData() {

		if (!(getEditValues() == null || getEditValues().isEmpty())) {

			getEditValues().clear();
		}
		String sql = "";
		JDBCcon con = new JDBCcon();
		ResultSet rs = null;

		String leib = "结算";
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();

		if (getWeizSelectValue().getId() == 1) {// 自己的任务
			sql = " select * from "
					+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
					+ "          dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
					+ "         decode(dm.jieslx,1,'两票结算','煤款结算') as leib,decode(1,1,'厂方结算单') as zhongl"
					+ "          from diancjsmkb dm,liucztb lz,leibztb "
					+ "          where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.id in ("
					+ Liuc.getWodrws("diancjsmkb", renyxxb_id, leib)
					+ "))"
					+ "          union"
					+ " (select dy.diancjsmkb_id as id,dy.bianm,dy.gongysmc,dy.xianshr,dy.jiessl,dy.jiesrq,"
					+ "           dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
					+ "         decode(dy.jieslx,1,'两票结算','运费结算') as leib,decode(1,1,'厂方结算单') as zhongl"
					+ "          from diancjsyfb dy,liucztb lz,leibztb "
					+ "          where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.id in ("
					+ Liuc.getWodrws("diancjsyfb", renyxxb_id, leib)
					+ "))"
					+ "			union"
					+ " (select km.id,km.bianm,km.gongysmc,km.xianshr,km.jiessl,km.jiesrq,"
					+ " 		km.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
					+ " 	   decode(km.jieslx,1,'两票结算','煤款结算') as leib,decode(1,1,'矿方结算单') as zhongl"
					+ " 		from kuangfjsmkb km,liucztb lz,leibztb "
					+ " 		where km.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and km.id in ("
					+ Liuc.getWodrws("kuangfjsmkb", renyxxb_id, leib)
					+ "))"
					+ " 		union"
					+ " (select ky.kuangfjsmkb_id as id,ky.bianm,ky.gongysmc,ky.xianshr,ky.jiessl,ky.jiesrq,"
					+ " 		ky.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
					+ " 	   decode(ky.jieslx,1,'两票结算','运费结算') as leib,decode(1,1,'矿方结算单') as zhongl"
					+ " 	    from kuangfjsyfb ky,liucztb lz,leibztb "
					+ " 		where ky.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and ky.id in ("
					+ Liuc.getWodrws("kuangfjsyfb", renyxxb_id, leib) + "))";
			rs = con.getResultSet(sql);
			
			try {
				while (rs.next()) {
					long id = rs.getLong("id");
					String jiesbh = rs.getString("bianm");
					String gongfdwmc = rs.getString("gongysmc");
					String xianshr = rs.getString("xianshr");
					double jiessl = rs.getLong("jiessl");
					Date jiesrq = rs.getDate("jiesrq");
					String zhuangt = rs.getString("zhuangt");
					String leix = rs.getString("leib");
					long liucztb_id = rs.getLong("liucztb_id");
					long liucb_id = rs.getLong("liucb_id");
					String zhongl=rs.getString("zhongl");//结算种类，厂方、矿方
					int Shend = Liuc.getShendId(liucb_id, liucztb_id);// //1,是审定状态0表示不是审定状态
					getEditValues().add(
							new Jieslcbean(id, jiesbh, gongfdwmc, xianshr,
									jiessl, jiesrq, zhuangt, leix, Shend,
									liucztb_id, liucb_id,zhongl));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			con.Close();
			
		} else {// 流程中的任务（不包括自己的）
			sql = " select * from "
				+ " (select dm.id,dm.bianm,dm.gongysmc,dm.xianshr,dm.jiessl,dm.jiesrq,"
				+ "          dm.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id,"
				+ "         decode(dm.jieslx,1,'两票结算','煤款结算') as leib,decode(1,1,'厂方结算单') as zhongl"
				+ "          from diancjsmkb dm,liucztb lz,leibztb "
				+ "          where dm.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dm.id in ("
				+ Liuc.getLiuczs("diancjsmkb", renyxxb_id, leib)
				+ "))"
				+ "          union"
				+ " (select dy.diancjsmkb_id as id,dy.bianm,dy.gongysmc,dy.xianshr,dy.jiessl,dy.jiesrq,"
				+ "           dy.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
				+ "         decode(dy.jieslx,1,'两票结算','运费结算') as leib,decode(1,1,'厂方结算单') as zhongl"
				+ "          from diancjsyfb dy,liucztb lz,leibztb "
				+ "          where dy.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and dy.id in ("
				+ Liuc.getLiuczs("diancjsyfb", renyxxb_id, leib)
				+ "))"
				+ "			union"
				+ " (select km.id,km.bianm,km.gongysmc,km.xianshr,km.jiessl,km.jiesrq,"
				+ " 		km.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
				+ " 	   decode(km.jieslx,1,'两票结算','煤款结算') as leib,decode(1,1,'矿方结算单') as zhongl"
				+ " 		from kuangfjsmkb km,liucztb lz,leibztb "
				+ " 		where km.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and km.id in ("
				+ Liuc.getLiuczs("kuangfjsmkb", renyxxb_id, leib)
				+ "))"
				+ " 		union"
				+ " (select ky.kuangfjsmkb_id as id,ky.bianm,ky.gongysmc,ky.xianshr,ky.jiessl,ky.jiesrq,"
				+ " 		ky.liucztb_id,leibztb.mingc as zhuangt,lz.liucb_id, "
				+ " 	   decode(ky.jieslx,1,'两票结算','运费结算') as leib,decode(1,1,'矿方结算单') as zhongl"
				+ " 	    from kuangfjsyfb ky,liucztb lz,leibztb "
				+ " 		where ky.liucztb_id=lz.id and lz.leibztb_id=leibztb.id and ky.id in ("
				+ Liuc.getLiuczs("kuangfjsyfb", renyxxb_id, leib) + "))";
			rs = con.getResultSet(sql);
			try {
				while (rs.next()) {
					long id = rs.getLong("id");
					String jiesbh = rs.getString("bianm");
					String gongfdwmc = rs.getString("gongysmc");
					String xianshr = rs.getString("xianshr");
					double jiessl = rs.getLong("jiessl");
					Date jiesrq = rs.getDate("jiesrq");
					String zhuangt = rs.getString("zhuangt");
					String leix = rs.getString("leib");
					long liucztb_id = rs.getLong("liucztb_id");
					long liucb_id = rs.getLong("liucb_id");
					String zhongl=rs.getString("zhongl");//结算种类，厂方、矿方
					int Shend = Liuc.getShendId(liucb_id, liucztb_id);// //1,是审定状态0表示不是审定状态
					getEditValues().add(
							new Jieslcbean(id, jiesbh, gongfdwmc, xianshr,
									jiessl, jiesrq, zhuangt, leix, Shend,
									liucztb_id, liucb_id,zhongl));
				}
				rs.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			con.Close();
		}
		setEditTableRow(-1);
		setXiaox("");
	}

	/**
	 * 1, 根据合同状态动作表找出下一个状态，进行更新 2,在更新合同状态的同时书写日志
	 */
	private void tij() {
		
		List list = getEditValues();
		Jieslcbean bean = (Jieslcbean) list.get(getEditTableRow());
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if(bean.getLeib().equals("两票结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.tij("diancjsmkb", bean.getId(), renyxxb_id, getXiaox());
				Liuc.tij("diancjsyfb", getYunfbId("diancjsmkb","diancjsyfb",bean.getId()), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.tij("kuangfjsmkb",bean.getId(), renyxxb_id, getXiaox());
				Liuc.tij("kuangfjsyfb", getYunfbId("kuangfjsmkb","kuangfjsyfb",bean.getId()), renyxxb_id, getXiaox());
			}
			
		}else if(bean.getLeib().equals("煤款结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.tij("diancjsmkb", bean.getId(), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.tij("kuangfjsmkb",bean.getId(), renyxxb_id, getXiaox());
			}
			
		}else if(bean.getLeib().equals("运费结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.tij("diancjsyfb", bean.getId(), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.tij("kuangfjsyfb",bean.getId(), renyxxb_id, getXiaox());
			}
		}
		
		getSelectData();
	}

	private long getYunfbId(String MkTableName,String YfTableName,long MkId) {
		// TODO 自动生成方法存根
		JDBCcon con=new JDBCcon();
		long Yfid=0;
		try{
			
			String sql="select id from "+YfTableName+" where "+MkTableName+"_id="+MkId+"";
			ResultSet rs=con.getResultSet(sql);
			if(rs.next()){
				
				Yfid=rs.getLong("id");
			}
			
			rs.close();
		}catch(Exception e){
			
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return Yfid;
	}
	/**
	 * 1, 根据合同状态动作表找出下一个状态，进行更新 2,在更新合同状态的同时书写日志
	 */
	private void huit() {
		List list = getEditValues();
		Jieslcbean bean = (Jieslcbean) list.get(getEditTableRow());
		long renyxxb_id = ((Visit) getPage().getVisit()).getRenyID();
		
		if(bean.getLeib().equals("两票结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.huit("diancjsmkb", bean.getId(), renyxxb_id, getXiaox());
				Liuc.huit("diancjsyfb", getYunfbId("diancjsmkb","diancjsyfb",bean.getId()), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.huit("kuangfjsmkb",bean.getId(), renyxxb_id, getXiaox());
				Liuc.huit("kuangfjsyfb", getYunfbId("kuangfjsmkb","kuangfjsyfb",bean.getId()), renyxxb_id, getXiaox());
			}
			
		}else if(bean.getLeib().equals("煤款结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.huit("diancjsmkb", bean.getId(), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.huit("kuangfjsmkb",bean.getId(), renyxxb_id, getXiaox());
			}
			
		}else if(bean.getLeib().equals("运费结算")){
			
			if(bean.getZhongl().equals("厂方结算单")){
				
				Liuc.huit("diancjsyfb", bean.getId(), renyxxb_id, getXiaox());
			
			}else if(bean.getZhongl().equals("矿方结算单")){
				
				Liuc.huit("kuangfjsyfb",bean.getId(), renyxxb_id, getXiaox());
			}
		}
		getSelectData();
	}

	private void chakwb() {

	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (visit.getRenyID() == -1) {
			visit.setboolean5(true);
			return;
		} else {
			visit.setboolean5(false);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			// 单位名称 1
			setDanwSelectValue(null);
			setIDanwSelectModel(null);
			getIDanwSelectModels();

			// 位置 2
			setWeizSelectValue(null);
			setIWeizSelectModel(null);
			getIWeizSelectModels();

			// 结算类型 3
			setJieslxValue(null);
			setIJieslxModel(null);
			getIJieslxModels();

			setXiaox(null);
			getSelectData();
			visit.setboolean4(true);
		}
		if (((Visit) getPage().getVisit()).getboolean1()
				|| ((Visit) getPage().getVisit()).getboolean2()
				|| ((Visit) getPage().getVisit()).getboolean3()) {// 如果合同位置改变
			// 1, 位置2, 结算类型3, 单位
			if (((Visit) getPage().getVisit()).getboolean1() == true) {
				if (getWeizSelectValue().getId() == 1) {
					visit.setboolean4(true);
				} else {
					visit.setboolean4(false);
				}
			}
			getSelectData();
		}
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return DateUtil.Formatdate("yyyy-MM-dd", new Date());
		}
		return DateUtil.Formatdate("yyyy-MM-dd", _date);
	}

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

	// 单位
	public IDropDownBean getDanwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getIDanwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setDanwSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean1().getId()) {
				((Visit) getPage().getVisit()).setboolean3(true);
			} else {
				((Visit) getPage().getVisit()).setboolean3(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public void setIDanwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getIDanwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getIDanwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getIDanwSelectModels() {
		String sql = "select id,mingc,jib,xuh\n" + "from(\n"
				+ " select id,mingc,0 as jib,0 as xuh\n" + " from diancxxb\n"
				+ " where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " union\n" + " select *\n" + " from(\n"
				+ " select id,mingc,level as jib,rownum as xuh\n" + "  from diancxxb\n"
				+ " start with fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " connect by fuid=prior id\n" + " order SIBLINGS by mingc)\n"
				+ " )\n" + " order by xuh";
		List dropdownlist = new ArrayList();
		JDBCcon con = new JDBCcon();
		try {
			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {
				int id = rs.getInt("id");
				String mc = rs.getString("mingc");
				int jib = rs.getInt("jib");
				String nbsp = String.valueOf((char) 0xA0);
				for (int i = 0; i < jib; i++) {
					mc = nbsp + nbsp + nbsp + nbsp + mc;
				}
				dropdownlist.add(new IDropDownBean(id, mc));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(dropdownlist));
		return;
	}

	// 位置
	public IDropDownBean getWeizSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getIWeizSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setWeizSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean2() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean2().getId()) {
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				((Visit) getPage().getVisit()).setboolean1(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public void setIWeizSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getIWeizSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getIWeizSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getIWeizSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(1, "我的任务"));
		list.add(new IDropDownBean(2, "流程中"));
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(list));
	}

	// 结算leix
	public IDropDownBean getJieslxValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean Value) {

		if (((Visit) getPage().getVisit()).getDropDownBean3() != Value) {

			((Visit) getPage().getVisit()).setboolean2(true);
			((Visit) getPage().getVisit()).setDropDownBean3(Value);
		}
	}

	public IPropertySelectionModel getIJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getIJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setIJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel13(value);
	}

	public void getIJieslxModels() {

		List list = new ArrayList();
		list.add(new IDropDownBean(0, "全部"));
		list.add(new IDropDownBean(1, "厂方结算单"));
		list.add(new IDropDownBean(2, "矿方结算单"));
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(list));
	}
}
