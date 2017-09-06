package com.zhiren.jt.het.yunsht.yunsht;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
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
import com.zhiren.common.Liuc;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.jt.het.yunsht.yunsht.Yunsjijbean;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author caolin2
 * 
 */

/**
 * @author yangzl 修改时间：2010-3-19 描述：修改合同复制，选择多个合同
 * 
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Yunsht extends BasePage implements PageValidateListener {
    private static Logger logger = org.apache.log4j.LogManager.getLogger(Yunsht.class);
	private String getDanwglStr() {
		;// 指标单位关联数组
		return ((Visit) getPage().getVisit()).getString3();
	}

	private void setDanwglStr(String value) {
		;// 指标单位关联数组
		((Visit) getPage().getVisit()).setString3(value);
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	// 增扣款页面变化记录
	private String Changek;

	public String getChangek() {
		return Changek;
	}

	public void setChangek(String change) {
		Changek = change;
	}

	// 收货人页面变化记录
	private String Changes;

	public String getChanges() {
		return Changes;
	}

	public void setChanges(String change) {
		Changes = change;
	}

	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
		// setTbmsg(null);
	}

	public String getpageLink() {
		return " var context='" + MainGlobal.getHomeContext(this) + "';"
				+ getDanwglStr();
	}

	public void setTabbarSelect(int value) {
		((Visit) getPage().getVisit()).setInt1(value);
	}

	public int getTabbarSelect() {
		return ((Visit) getPage().getVisit()).getInt1();
	}

	// 功能区 新建模板 打开 保存 选择流程 模板名称
	// 模板名称下拉框
	public IDropDownBean getmobmcSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getmobmcSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setmobmcSelectValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean1() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean1().getId()) {
				((Visit) getPage().getVisit()).setboolean6(true);
			} else {
				((Visit) getPage().getVisit()).setboolean6(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean1(Value);

		}
	}

	public void setmobmcSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getmobmcSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getmobmcSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void getmobmcSelectModels() {

		String sql = "";
		if (this.isTijsh()) {
			sql = "select id,mingc " + "from hetys_mb " + "where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
					+ " and nvl(liucztb_id,0)=1  order by mingc ";

		} else {
			sql = "select id,mingc " + "from hetys_mb " + "where diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc ";

		}
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "选择模板"));
		return;
	}

	// 合同
	public IDropDownBean gethetSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) gethetSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void sethetSelectValue(IDropDownBean Value) {
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

	public void sethetSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel gethetSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			gethetSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	/*
	 * huochaoyuan 2009-10-22修改合同编号下拉框取数sql， zsj 2009-11-20修改合同编号sql
	 */
	public void gethetSelectModels() {
		String sql = "";
		sql = "select h.id,h.hetbh\n"
				+ "	from hetys h,diancxxb d where h.fuid=0 and h.liucztb_id=0 and to_char(h.qiandrq,'YYYY')="
				+ getNianfValue().getId()
				+ "	and h.diancxxb_id=d.id and (diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " 	or d.fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " 	or d.id in (select fuid from diancxxb where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "))	\n"
				+ " order by h.hetbh ";

		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
	}

//	public void gethetSelectModels() {
//		String sql = "";
//		if (value != null && !value.equals("") && !value.equals("fanhui")) {
//			String new_ID = value;
//			sql = "select h.id,h.hetbh from hetys h\n"
//					+ "where h.liucztb_id=0 and h.id in("
//					+ new_ID.substring(0, new_ID.lastIndexOf(",")) + ")";
//			sql += " union select h.id,h.hetbh\n"
//					+ "	from hetys h,diancxxb d where h.fuid=0 and h.liucztb_id=0 and to_char(h.qiandrq,'YYYY')="
//					+ getNianfValue().getId()
//					+ "	and h.diancxxb_id=d.id and (diancxxb_id="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " 	or d.fuid="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " 	or d.id in (select fuid from diancxxb where id="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "))	\n"
//					+ " order by hetbh ";
//			// 获取复制合同ID后置空
//			// ((Visit) getPage().getVisit()).setString12("");
//			((Visit) getPage().getVisit())
//					.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
//		} else {
//			sql = "select h.id,h.hetbh\n"
//					+ "	from hetys h,diancxxb d where h.fuid=0 and h.liucztb_id=0 and to_char(h.qiandrq,'YYYY')="
//					+ getNianfValue().getId()
//					+ "	and h.diancxxb_id=d.id and (diancxxb_id="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " 	or d.fuid="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//					+ " 	or d.id in (select fuid from diancxxb where id="
//					+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "))	\n"
//					+ " order by h.hetbh ";
//			// "select id,hetys.hetbh\n" +
//			// "from hetys where fuid=0 and liucztb_id=0 and
//			// to_char(hetys.qiandrq,'YYYY')="+getNianfValue().getId()+" and
//			// (diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"
//			// or diancxxb_id=(select fuid from diancxxb where id="+((Visit)
//			// getPage().getVisit()).getDiancxxb_id()+"))";
//
//			((Visit) getPage().getVisit())
//					.setProSelectionModel2(new IDropDownModel(sql, "新建合同"));
//		}
//	}

	// end
	// 新模板名称
	// public String getmobmc(){
	// return ((Visit) getPage().getVisit()).getString1();
	// }
	// public void setmobmc(String value){
	// ((Visit) getPage().getVisit()).setString1(value);
	// }
	// 合同信息
	public Hetysxxbean gethetysxxbean() {
		if (((Visit) getPage().getVisit()).getObject1() == null) {
			((Visit) getPage().getVisit()).setObject1(new Hetysxxbean());
		}
		return (Hetysxxbean) ((Visit) getPage().getVisit()).getObject1();
	}

	public void sethetysxxbean(Hetysxxbean value) {
		((Visit) getPage().getVisit()).setObject1(value);
	}

	// 价格方案
	public IDropDownBean getYunsjgfaValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIYunsjgfaModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setYunsjgfaValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setIYunsjgfaModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIYunsjgfaModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIYunsjgfaModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getIYunsjgfaModels() {
		String sql = "select id,mingc\n" + "from yunsjgfab  order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, ""));
		return;
	}

	// //合同数量信息
	// private int _editTableRows = -1;
	// public int getEditTableRows() {
	// return _editTableRows;
	// }
	// public void setEditTableRows(int _value) {
	// _editTableRows = _value;
	// }
	// public List geteditValuess(){
	// if(((Visit) getPage().getVisit()).getList1()==null){
	// ((Visit) getPage().getVisit()).setList1(new ArrayList());
	// }
	// return ((Visit) getPage().getVisit()).getList1();
	// }
	// private Fahxxbean editValues1;
	// public Fahxxbean geteditValues1(){
	// return editValues1;
	// }
	// public void seteditValues1(Fahxxbean value){
	// this.editValues1=value;
	// }
	// 运输方式
	public IDropDownBean getyunsfsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getyunsfsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setyunsfsValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setyunsfsModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getyunsfsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getyunsfsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getyunsfsModels() {
		String sql = "select id,mingc\n" + "from yunsfsb   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 燃料品种
	public IDropDownBean getRanlpzb_idValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getRanlpzb_idModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setRanlpzb_idValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public void setRanlpzb_idModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getRanlpzb_idModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getRanlpzb_idModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void getRanlpzb_idModels() {
		String sql = "select id,mingc\n" + "from pinzb  order by mingc  ";// where
		// pinzb.zhangt=1
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 车站
	public IDropDownBean getchezValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getchezModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setchezValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public void setchezModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getchezModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getchezModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void getchezModels() {
		String sql = "select id,mingc\n" + "from chezxxb\n" + "order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(sql, ""));
		return;
	}

	//
	// 收货人
	public IDropDownBean getshouhrValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getshouhrModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setshouhrValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setshouhrModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getshouhrModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {
			getshouhrModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	// public void getshouhrModels() {
	// String sql =
	// "select id,mingc\n" +
	// "from diancxxb";
	// ((Visit) getPage().getVisit()).setProSelectionModel7(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getshouhrModels() {
		String sql = "select id,mingc,jib\n" + "from(\n"
				+ " select id,mingc,0 as jib\n" + " from diancxxb\n"
				+ " where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " union\n" + " select *\n" + " from(\n"
				+ " select id,mingc,level as jib\n" + "  from diancxxb\n"
				+ " start with fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ " connect by fuid=prior id\n" + " order SIBLINGS by  xuh)\n"
				+ " )\n" + " order by jib,mingc";
		List dropdownlist = new ArrayList();
		dropdownlist.add(new IDropDownBean(0, ""));
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
				.setProSelectionModel7(new IDropDownModel(dropdownlist));
		return;
	}

	// 质量
	/*
	 * private int _editTableRowz = -1; public int getEditTableRowz() { return
	 * _editTableRowz; } public void setEditTableRowz(int _value) {
	 * _editTableRowz = _value; } public List geteditValuesz(){ if(((Visit)
	 * getPage().getVisit()).getList2()==null){ ((Visit)
	 * getPage().getVisit()).setList2(new ArrayList()); } return ((Visit)
	 * getPage().getVisit()).getList2(); } public Zhilyqbean editValuez; public
	 * Zhilyqbean geteditValuez(){ return this.editValuez; } public void
	 * seteditValuez(Zhilyqbean value){ this.editValuez=value; } //指标 public
	 * IDropDownBean getZHIBValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean8()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean8((IDropDownBean)getZHIBModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean8(); }
	 * 
	 * public void setZHIBValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean8(Value); }
	 * 
	 * 
	 * public void setZHIBModel(IPropertySelectionModel value) { ((Visit)
	 * getPage().getVisit()).setProSelectionModel8(value); }
	 * 
	 * public IPropertySelectionModel getZHIBModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel8() == null) { getZHIBModels(); }
	 * return ((Visit) getPage().getVisit()).getProSelectionModel8(); }
	 * 
	 * public void getZHIBModels() { String sql = "select id,mingc\n" + "from
	 * zhibb"; ((Visit) getPage().getVisit()).setProSelectionModel8(new
	 * IDropDownModel(sql,"")) ; return ; } //条件 public IDropDownBean
	 * getTIAOJValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean9()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean9((IDropDownBean)getTIAOJModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean9(); }
	 * 
	 * public void setTIAOJValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean9(Value); }
	 * 
	 * 
	 * public void setTIAOJModel(IPropertySelectionModel value) { ((Visit)
	 * getPage().getVisit()).setProSelectionModel9(value); }
	 * 
	 * public IPropertySelectionModel getTIAOJModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel9() == null) {
	 * getTIAOJModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel9(); }
	 * 
	 * public void getTIAOJModels() { String sql = "select id,mingc\n" + "from
	 * tiaojb"; ((Visit) getPage().getVisit()).setProSelectionModel9(new
	 * IDropDownModel(sql,"")) ; return ; }
	 */
	// 单位
	public IDropDownBean getYunjudwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getYunjudwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setYunjudwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public void setYunjudwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getYunjudwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getYunjudwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getYunjudwModels() {
		String sql = "select id,mingc\n" + "from danwb where zhibb_id=19   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, ""));
		return;
	}

	// 增扣价格
	/*
	 * private int _editTableRowj = -1; public int getEditTableRowj() { return
	 * _editTableRowj; } public void setEditTableRowj(int _value) {
	 * _editTableRowj = _value; } public List geteditValuesj(){ if(((Visit)
	 * getPage().getVisit()).getList3()==null){ ((Visit)
	 * getPage().getVisit()).setList3(new ArrayList()); } return ((Visit)
	 * getPage().getVisit()).getList3(); } public Zengkkbean editValuej; public
	 * Zengkkbean geteditValuej(){ return this.editValuej; } public void
	 * seteditValuej(Zengkkbean value){ this.editValuej=value; }
	 */
	// 价格
	private int _editTableRowg = -1;

	public int getEditTableRowg() {
		return _editTableRowg;
	}

	public void setEditTableRowg(int _value) {
		_editTableRowg = _value;
	}

	public List geteditValuesg() {
		if (((Visit) getPage().getVisit()).getList4() == null) {
			((Visit) getPage().getVisit()).setList4(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList4();
	}

	public Yunsjijbean editValueg;

	public Yunsjijbean geteditValueg() {
		return this.editValueg;
	}

	public void seteditValueg(Yunsjijbean value) {
		this.editValueg = value;
	}

	// 指标
	public IDropDownBean getZhibSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getZhibSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setZhibSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setZhibSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getZhibSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
			getZhibSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public void getZhibSelectModels() {
		String sql = "select id,mingc\n" + "from zhibb\n"
				+ "where zhibb.leib=1 and zhibb.id in (18,19)  order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel11(new IDropDownModel(sql, ""));
		return;
	}

	// 结算用条件
	public IDropDownBean gettiaojjSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean12() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean12((IDropDownBean) gettiaojjSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean12();
	}

	public void settiaojjSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean12(Value);
	}

	public void settiaojjSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel12(value);
	}

	public IPropertySelectionModel gettiaojjSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel12() == null) {
			gettiaojjSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel12();
	}

	public void gettiaojjSelectModels() {
		String sql = "select id,mingc\n" + "from tiaojb\n"
				+ "where tiaojb.leib=1   order by mingc  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel12(new IDropDownModel(sql, ""));
		return;
	}

	// 价格单位
	public IDropDownBean getYunjiadwValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean13() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean13((IDropDownBean) getYunjiadwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean13();
	}

	public void setYunjiadwValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean13(Value);
	}

	public void setYunjiadwModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel13(value);
	}

	public IPropertySelectionModel getYunjiadwModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel13() == null) {
			getYunjiadwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel13();
	}

	public void getYunjiadwModels() {
		String sql = "select id,mingc\n" + "from danwb\n"
				+ "where danwb.zhibb_id=18   order by mingc  ";// zhibb_id=0为价格单位
		((Visit) getPage().getVisit())
				.setProSelectionModel13(new IDropDownModel(sql, ""));
		return;
	}

	// 指标单位
	public IDropDownBean getzhibdwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean14() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean14((IDropDownBean) getzhibdwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean14();
	}

	public void setzhibdwSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean14(Value);
	}

	public void setzhibdwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel14(value);
	}

	public IPropertySelectionModel getzhibdwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel14() == null) {
			getzhibdwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel14();
	}

	public void getzhibdwSelectModels() {// zhibb_id=
		String sql = "select id,mingc " + "from danwb   order by mingc \n";
		((Visit) getPage().getVisit())
				.setProSelectionModel14(new IDropDownModel(sql, ""));
		return;
	}

	/*
	 * // 合同结算方式表 public IDropDownBean getjiesfsgSelectValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean15()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean15((IDropDownBean)getjiesfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean15(); }
	 * 
	 * public void setjiesfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean15(Value); }
	 * 
	 * 
	 * public void setjiesfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel15(value); }
	 * 
	 * public IPropertySelectionModel getjiesfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel15() == null) {
	 * getjiesfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel15(); }
	 * 
	 * public void getjiesfsgSelectModels() { String sql = "select id,mingc\n" +
	 * "from hetjsfsb"; ((Visit)
	 * getPage().getVisit()).setProSelectionModel15(new IDropDownModel(sql,"")) ;
	 * return ; } // 合同计价方式 public IDropDownBean getjijfsgSelectValue() { if(
	 * ((Visit) getPage().getVisit()).getDropDownBean16()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean16((IDropDownBean)getjijfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean16(); }
	 * 
	 * public void setjijfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean16(Value); }
	 * 
	 * 
	 * public void setjijfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel16(value); }
	 * 
	 * public IPropertySelectionModel getjijfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel16() == null) {
	 * getjijfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel16(); } public void
	 * getjijfsgSelectModels() { String sql="select id,mingc\n" + "from
	 * hetjjfsb"; ((Visit) getPage().getVisit()).setProSelectionModel16(new
	 * IDropDownModel(sql,"")) ; return ; }
	 */
	// 小数处理
	public IDropDownBean getxiaoswcljSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean17() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean17((IDropDownBean) getxiaoswcljSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean17();
	}

	public void setxiaoswcljSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean17(Value);
	}

	public void setxiaoswcljSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel17(value);
	}

	public IPropertySelectionModel getxiaoswcljSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel17() == null) {
			getxiaoswcljSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel17();
	}

	public void getxiaoswcljSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, " "));
		list.add(new IDropDownBean(1, "进位"));
		list.add(new IDropDownBean(2, "舍去"));
		list.add(new IDropDownBean(3, "四舍五入"));
		list.add(new IDropDownBean(4, "四舍五入(0.1)"));
		list.add(new IDropDownBean(5, "四舍五入(0.01)"));

		((Visit) getPage().getVisit())
				.setProSelectionModel17(new IDropDownModel(list));
		return;
	}

	// 文字
	String buffer;

	public String getWenz() {
		return ((Visit) getPage().getVisit()).getString4();
	}

	public void setWenz(String value) {
		((Visit) getPage().getVisit()).setString4(value);
	}

	// 合同供方
	public IDropDownBean getgongfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean18() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean18((IDropDownBean) getgongfModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean18();
	}

	public void setgongfValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean18() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean18().getId()) {
				((Visit) getPage().getVisit()).setboolean2(true);
			} else {
				((Visit) getPage().getVisit()).setboolean2(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean18(Value);
		}
	}

	public void setgongfModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel18(value);
	}

	public IPropertySelectionModel getgongfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel18() == null) {
			getgongfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel18();
	}

	public void getgongfModels() {
		String sql = "select * from (select id,mingc\n"
				+ "from yunsdwb order by mingc)  ";
		((Visit) getPage().getVisit())
				.setProSelectionModel18(new IDropDownModel(sql, ""));
		return;
	}

	// 需方
	public IDropDownBean getxufValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean19() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean19((IDropDownBean) getxufModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean19();
	}

	public void setxufValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean19() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean19().getId()) {
				((Visit) getPage().getVisit()).setboolean3(true);
			} else {
				((Visit) getPage().getVisit()).setboolean3(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean19(Value);
		}
	}

	public void setxufModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel19(value);
	}

	public IPropertySelectionModel getxufModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel19() == null) {
			getxufModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel19();
	}

	// public void getxufModels() {//显示该用户单位下所有孩子
	// String sql=
	// "select id,mingc " +
	// "from diancxxb\n" +
	// "where diancxxb.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+"
	// or id="+((Visit) getPage().getVisit()).getDiancxxb_id();
	// ((Visit) getPage().getVisit()).setProSelectionModel19(new
	// IDropDownModel(sql,"")) ;
	// return ;
	// }
	public void getxufModels() {// 显示该用户单位下所有孩子
		String diancqc=((Visit) getPage().getVisit()).getDiancqc();
		String sql="";
		//专为外二设定需方单位
		if (diancqc.equals("上海外高桥第二发电有限责任公司")) {
			sql="select id,mingc from diancxxb where quanc='上海外高桥第二发电有限责任公司'";
		}else{
				sql = "select id,mingc from diancxxb where  fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "or  id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()
				+ " or id in(select fuid from diancxxb where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + ")  order by xuh  ";
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel19(new IDropDownModel(sql, ""));
		return;
	}

	// 供方
	public IDropDownBean getshijgfSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean20() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean20((IDropDownBean) getshijgfSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean20();
	}

	public void setshijgfSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean20(Value);
	}

	public void setshijgfSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel20(value);
	}

	public IPropertySelectionModel getshijgfSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel20() == null) {
			getshijgfSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel20();
	}

	public void getshijgfSelectModels() {
		String sql = "select id,mingc\n" + "from YUNSDWB   order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel20(new IDropDownModel(sql, ""));
		return;
	}

	// 加权方式
	/*
	 * public IDropDownBean getjiaqfsgSelectValue() { if( ((Visit)
	 * getPage().getVisit()).getDropDownBean21()==null){ ((Visit)
	 * getPage().getVisit()).setDropDownBean21((IDropDownBean)getjiaqfsgSelectModel().getOption(0)); }
	 * return ((Visit) getPage().getVisit()).getDropDownBean21(); }
	 * 
	 * public void setjiaqfsgSelectValue(IDropDownBean Value) { ((Visit)
	 * getPage().getVisit()).setDropDownBean21(Value); }
	 * 
	 * 
	 * public void setjiaqfsgSelectModel(IPropertySelectionModel value) {
	 * ((Visit) getPage().getVisit()).setProSelectionModel21(value); }
	 * 
	 * public IPropertySelectionModel getjiaqfsgSelectModel() { if (((Visit)
	 * getPage().getVisit()).getProSelectionModel21() == null) {
	 * getjiaqfsgSelectModels(); } return ((Visit)
	 * getPage().getVisit()).getProSelectionModel21(); }
	 * 
	 * public void getjiaqfsgSelectModels() { String sql= "select id,mingc\n" +
	 * "from hetjsxsb"; ((Visit)
	 * getPage().getVisit()).setProSelectionModel21(new IDropDownModel(sql,"")) ;
	 * return ; }
	 */
	// 拒付亏吨运费
	public IDropDownBean getyingdkfgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean22() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean22((IDropDownBean) getyingdkfgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean22();
	}

	public void setyingdkfgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean22(Value);
	}

	public void setyingdkfgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel22(value);
	}

	public IPropertySelectionModel getyingdkfgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel22() == null) {
			getyingdkfgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel22();
	}

	public void getyingdkfgSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, ""));
		list.add(new IDropDownBean(1, "是"));
		list.add(new IDropDownBean(2, "否"));
		((Visit) getPage().getVisit())
				.setProSelectionModel22(new IDropDownModel(list));
		return;
	}

	// //煤矿
	public IDropDownBean getMeikxxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean23() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean23((IDropDownBean) getMeikxxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean23();
	}

	public void setMeikxxValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean23(Value);
	}

	public void setMeikxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel23(value);
	}

	public IPropertySelectionModel getMeikxxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel23() == null) {
			getMeikxxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel23();
	}

	public void getMeikxxModels() {
		String sql = "select * from (select id,mingc\n"
				+ "from meikxxb order by mingc)   ";
		((Visit) getPage().getVisit())
				.setProSelectionModel23(new IDropDownModel(sql, ""));
		return;
	}

	// 煤矿
	public IDropDownBean getMeikxx2Value() {
		if (((Visit) getPage().getVisit()).getDropDownBean24() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean24((IDropDownBean) getMeikxx2Model()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean24();
	}

	public void setMeikxx2Value(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean24(Value);
	}

	public void setMeikxx2Model(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel24(value);
	}

	public IPropertySelectionModel getMeikxx2Model() {
		if (((Visit) getPage().getVisit()).getProSelectionModel24() == null) {
			getMeikxx2Models();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel24();
	}

	public void getMeikxx2Models() {
		String sql = "select * from (select id,mingc\n"
				+ "from meikxxb order by mingc) ";
		((Visit) getPage().getVisit())
				.setProSelectionModel24(new IDropDownModel(sql, ""));
		return;
	}

	public boolean isXinjht() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setXinjht(boolean value) {
		((Visit) getPage().getVisit()).setboolean5(value);
	}

	public ExtGridUtil getExtGrid() {
		if (((Visit) this.getPage().getVisit()).getExtGrid1() == null) {
		}
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		// if(getTbmsg()!=null) {
		// getExtGrid().addToolbarItem("'->'");
		// getExtGrid().addToolbarItem("'<marquee width=300
		// scrollamount=2>"+getTbmsg()+"</marquee>'");
		// }
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private String value = "";

	// 是否为从复制合同页后返回
	boolean isFuz = false;

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();

		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setString1("");
			visit.setString2(""); // 发货人
			visit.setString3(""); // 指标单位关联数组
			visit.setString4(""); // 文字描述

			isFuz = false;

			if (visit.getActivePageName().toString().equals("Hetfz")) {
				value = visit.getString13();
				if (value != null) {
					isFuz = value.equals("") ? false : true;
				}
			}

			if (!visit.getActivePageName().toString().equals("Hetfz")) {
				visit.setString13(null);
				value = null;
			}

			visit.setActivePageName(getPageName().toString());

			// 当为复制合同返回页面，保存原来部分信息
			if (!isFuz) {
				// 模板
				((Visit) getPage().getVisit()).setProSelectionModel1(null);
				((Visit) getPage().getVisit()).setDropDownBean1(null);
				// 年份
				((Visit) getPage().getVisit()).setProSelectionModel25(null);
				((Visit) getPage().getVisit()).setDropDownBean25(null);
			}

			// 合同
			((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);

			// ((Visit) getPage().getVisit()).setDropDownBean1(null); //模板名称
			// ((Visit) getPage().getVisit()).setProSelectionModel1(null);
			// //模板名称

			// ((Visit) getPage().getVisit()).setDropDownBean2(null);
			((Visit) getPage().getVisit()).setDropDownBean3(null);
			((Visit) getPage().getVisit()).setDropDownBean4(null);
			((Visit) getPage().getVisit()).setDropDownBean5(null);
			((Visit) getPage().getVisit()).setDropDownBean6(null);
			((Visit) getPage().getVisit()).setDropDownBean7(null);
			((Visit) getPage().getVisit()).setDropDownBean8(null);
			((Visit) getPage().getVisit()).setDropDownBean9(null);
			((Visit) getPage().getVisit()).setDropDownBean10(null);
			((Visit) getPage().getVisit()).setDropDownBean11(null);
			((Visit) getPage().getVisit()).setDropDownBean12(null);
			((Visit) getPage().getVisit()).setDropDownBean13(null);
			((Visit) getPage().getVisit()).setDropDownBean14(null);
			((Visit) getPage().getVisit()).setDropDownBean15(null);
			((Visit) getPage().getVisit()).setDropDownBean16(null);
			((Visit) getPage().getVisit()).setDropDownBean17(null);
			((Visit) getPage().getVisit()).setDropDownBean18(null);
			((Visit) getPage().getVisit()).setDropDownBean19(null);
			((Visit) getPage().getVisit()).setDropDownBean20(null);
			((Visit) getPage().getVisit()).setDropDownBean21(null);
			((Visit) getPage().getVisit()).setDropDownBean22(null);
			((Visit) getPage().getVisit()).setDropDownBean23(null);
			((Visit) getPage().getVisit()).setDropDownBean24(null);
			// ((Visit) getPage().getVisit()).setDropDownBean25(null);

			// ((Visit) getPage().getVisit()).setProSelectionModel2(null);
			((Visit) getPage().getVisit()).setProSelectionModel3(null);
			((Visit) getPage().getVisit()).setProSelectionModel4(null);
			((Visit) getPage().getVisit()).setProSelectionModel5(null);
			((Visit) getPage().getVisit()).setProSelectionModel6(null);
			((Visit) getPage().getVisit()).setProSelectionModel7(null);
			((Visit) getPage().getVisit()).setProSelectionModel8(null);
			((Visit) getPage().getVisit()).setProSelectionModel9(null);
			((Visit) getPage().getVisit()).setProSelectionModel10(null);
			((Visit) getPage().getVisit()).setProSelectionModel11(null);
			((Visit) getPage().getVisit()).setProSelectionModel12(null);
			((Visit) getPage().getVisit()).setProSelectionModel13(null);
			((Visit) getPage().getVisit()).setProSelectionModel14(null);
			((Visit) getPage().getVisit()).setProSelectionModel15(null);
			((Visit) getPage().getVisit()).setProSelectionModel16(null);
			((Visit) getPage().getVisit()).setProSelectionModel17(null);
			((Visit) getPage().getVisit()).setProSelectionModel18(null);
			((Visit) getPage().getVisit()).setProSelectionModel19(null);
			((Visit) getPage().getVisit()).setProSelectionModel20(null);
			((Visit) getPage().getVisit()).setProSelectionModel21(null);
			((Visit) getPage().getVisit()).setProSelectionModel22(null);
			((Visit) getPage().getVisit()).setProSelectionModel23(null);
			((Visit) getPage().getVisit()).setProSelectionModel24(null);
			// ((Visit) getPage().getVisit()).setProSelectionModel25(null);
			((Visit) getPage().getVisit()).setObject1(null);
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).setList2(null);
			((Visit) getPage().getVisit()).setList3(null);
			((Visit) getPage().getVisit()).setList4(null);

			((Visit) getPage().getVisit()).setboolean1(false);
			((Visit) getPage().getVisit()).setboolean2(false);
			((Visit) getPage().getVisit()).setboolean3(false);
			((Visit) getPage().getVisit()).setboolean4(false);
			((Visit) getPage().getVisit()).setboolean6(false);

			setTabbarSelect(0);
			setXinjht(true);
			getDanwGL();
			getshouhrModels();
			getHetysjgExt("mb", -1);
			getYunshtzkkExt("mb", -1);
			getShouhrExt("mb", -1);
			// 更新多选框
			// String sql=
			// "select mingc\n" +
			// "from meikxxb";
			// String[] columns=new String[]{"名称"};
			// setMultiSel(sql,columns);

			this.setTijsh();
		}
		// getToolbars();
		if (((Visit) getPage().getVisit()).getboolean1()) {// 选择合同
			getXuanzht();
		} else {
			if (((Visit) getPage().getVisit()).getboolean2()) {// 合同供方
				getGongf();
			}
			if (((Visit) getPage().getVisit()).getboolean3()) {// 需方
				getXuf();
			}
		}
		if (((Visit) getPage().getVisit()).getboolean4()) {// 年份刷新合同
			Xinj();
			getmobmcSelectModels();
			gethetSelectModels();
		}
		if (((Visit) getPage().getVisit()).getboolean6()) {// 加载模板
			gethetysmb(getmobmcSelectValue().getId());
		}
		getToolbars();
		getChengyfArrayScript();
	}

	// ***************************************************************************//

	private boolean tijsh;// 是否添加 提交进入流程审核功能

	public boolean isTijsh() {

		return tijsh;
	}

	public void setTijsh() {

		tijsh = false;

		String sql = " select * from xitxxb  where mingc='运输合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sql);

		if (rsl.next()) {
			tijsh = true;
		}

	}

	private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");

		// 结算编号
		tb1.addText(new ToolbarText("签订年份:"));
		ComboBox nianfDropDown = new ComboBox();
		nianfDropDown.setId("NianfDropDown");
		nianfDropDown
				.setListeners("select:function(){document.Form0.submit();}");
		nianfDropDown.setWidth(80);
		nianfDropDown.setLazyRender(true);
		nianfDropDown.setTransform("NianfDropDown");
		tb1.addField(nianfDropDown);
		tb1.addText(new ToolbarText("-"));
		if (isXinjht()) {
			tb1.addText(new ToolbarText("选择模板:"));
			ComboBox mobmcDropDown = new ComboBox();
			mobmcDropDown.setId("mobmcSelect");
			mobmcDropDown
					.setListeners("select:function(){document.Form0.submit();}");
			mobmcDropDown.setWidth(150);
			mobmcDropDown.setLazyRender(true);
			mobmcDropDown.setTransform("mobmcSelect");
			tb1.addField(mobmcDropDown);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("选择合同:"));
		ComboBox hetDropDown = new ComboBox();
		hetDropDown.setId("hetSelect");
		hetDropDown.setListeners("select:function(){document.Form0.submit();}");
		hetDropDown.setWidth(150);
		hetDropDown.setLazyRender(true);
		hetDropDown.setListWidth(200);
		hetDropDown.setTransform("hetSelect");
		tb1.addField(hetDropDown);
		tb1.addText(new ToolbarText("-"));

		// 刷新
		ToolbarButton querybt = new ToolbarButton(null, "文本",
				"function(){chax();}");
		querybt.setId("QueryButton");
		tb1.addItem(querybt);
		tb1.addText(new ToolbarText("-"));
		// 删除
		ToolbarButton deletebt = new ToolbarButton(
				null,
				"删除",
				"function(){ Ext.MessageBox.confirm('提示信息','您确定要删除此合同吗？',function(fn){if(fn=='yes'){document.Form0.ShancButton.click();}})}");
		deletebt.setId("ShancButton");
		tb1.addItem(deletebt);
		tb1.addText(new ToolbarText("-"));

		// 保存
		StringBuffer strExt = new StringBuffer();
		strExt.append(" \n");
		strExt.append("if(document.all.item('hetmc').value==''){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','合同名称不能为空！');return ;\n");
		strExt.append("} \n");

		strExt.append("if(document.all.item('hetbh').value==''){ \n");
		strExt.append("	Ext.MessageBox.alert('提示信息','合同编号不能为空!');return ;\n");
		strExt.append("		return false;	}\n");
		strExt.append("if(document.all.item('qiandrq').value==''){\n");
		strExt
				.append("		Ext.MessageBox.alert('提示信息','合同签定日期不能为空!');return ;\n");
		strExt.append("		return false;	}\n");
		strExt.append("if(document.all.item('hetSelect').value==-1){\n");// 如果是新建合同必须套用模板
		strExt.append("	if(document.all.item('mobmcSelect').value==-1){\n");
		strExt.append("		Ext.MessageBox.alert('提示信息','请选择合同模板!');return ;\n");
		strExt.append("		return false;		}}\n");
		strExt.append("if(document.all.item('shengxsj').value==''){\n");
		strExt.append("	Ext.MessageBox.alert('提示信息','合同生效日期不能为空!');return ;\n");
		strExt.append("	return false;		}\n");
		strExt.append("if(document.all.item('guoqsj').value==''){\n");
		strExt.append("	Ext.MessageBox.alert('提示信息','合同过期时间不能为空!');return ;\n");
		strExt.append("	return false;	}\n");
		strExt.append("if(document.all.item('gongfdwmc').value==''){\n");
		strExt.append("	Ext.MessageBox.alert('提示信息','合同承运方不能为空!');return ;	\n");
		strExt.append("	return false;		}\n");
		strExt.append("if(document.all.item('xufdwmc').value==''){\n");
		strExt.append("	Ext.MessageBox.alert('提示信息','合同托运方不能为空!');	return ;\n");
		strExt.append("	return false;	}\n");

		strExt.append("var Mrcd = gridDiv_ds.getRange(); \n");
		strExt.append("for(i = 0; i< Mrcd.length; i++){ \n");
		strExt.append("if(typeof(gridDiv_save)=='function'){  \n");
		strExt
				.append("		var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;} \n");
		strExt.append("} \n");
		// strExt.append("if(Mrcd[i].get('ZHIBB_ID') == ''){ \n");
		// strExt.append(" Ext.MessageBox.alert('提示信息','字段 指标 不能为空');return;
		// \n");
		// strExt.append("} \n");
		// strExt.append("if(Mrcd[i].get('DANWB_ID') == ''){ \n");
		// strExt.append(" Ext.MessageBox.alert('提示信息','字段 指标单位 不能为空');return;
		// \n");
		// strExt.append("} \n");
		strExt
				.append("if(Mrcd[i].get('YUNJA')!=0 && Mrcd[i].get('YUNJA') == ''){ \n");
		strExt.append("		Ext.MessageBox.alert('提示信息','字段 运价 不能为空');return; \n");
		strExt.append("} \n");
		strExt.append("if(Mrcd[i].get('YUNJDW_ID') == ''){ \n");
		strExt
				.append("		Ext.MessageBox.alert('提示信息','字段 运价单位 不能为空');return; \n");
		strExt.append("} \n");
		strExt
				.append("gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HETYS_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('HETYS_ID')+ '</HETYS_ID>'+ '<MEIKXXB_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('MEIKXXB_ID').replace('<','&lt;').replace('>','&gt;')+ '</MEIKXXB_ID>'+ '<ZHIBB_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('ZHIBB_ID').replace('<','&lt;').replace('>','&gt;')+ '</ZHIBB_ID>'+ '<DANWB_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('DANWB_ID').replace('<','&lt;').replace('>','&gt;')+ '</DANWB_ID>'+ '<TIAOJB_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('TIAOJB_ID').replace('<','&lt;').replace('>','&gt;')+ '</TIAOJB_ID>'+ '<XIAX update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('XIAX')+ '</XIAX>'+ '<SHANGX update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('SHANGX')+ '</SHANGX>'+ '<YUNJA update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('YUNJA')+ '</YUNJA>'+ '<YUNJDW_ID update=\"true\">'  \n");
		strExt
				.append("+ Mrcd[i].get('YUNJDW_ID').replace('<','&lt;').replace('>','&gt;')+ '</YUNJDW_ID>' + '</result>' ;  \n");
		strExt.append("} \n");
		strExt.append("if(gridDiv_history==''){  \n");
		// strExt.append("// Ext.MessageBox.alert('提示信息','没有进行改动无需保存'); \n");
		strExt.append("}else{ \n");
		strExt.append("var Cobj = document.getElementById('CHANGE'); \n");
		strExt
				.append("Cobj.value = '<result>'+gridDiv_history+'</result>'; } \n");

		// 新加合同增扣款项
		strExt.append("var Mrcdk = gridDivk_ds.getRange(); \n");
		strExt.append("for(i = 0; i< Mrcdk.length; i++){ \n");
		strExt
				.append("if(typeof(gridDivk_save)=='function'){ var revalue = gridDivk_save(Mrcdk[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} \n");
		strExt
				.append("if(Mrcdk[i].get('ZHIBB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 项目 不能为空');return; \n");
		strExt
				.append("}if(Mrcdk[i].get('TIAOJB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 条件 不能为空');return; \n");
		strExt
				.append("}if(Mrcdk[i].get('DANWB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 项目单位 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('JISDWID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 基数单位 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('KOUJDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 扣价单位 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('ZENGFJDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 增付价单位 不能为空');return; \n");
		strExt
				.append("}if(Mrcdk[i].get('XIAOSCL') == ''){Ext.MessageBox.alert('提示信息','字段 小数处理方式 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('CANZXM') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 参照项目 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('CANZXMDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 参照项目单位 不能为空');return; \n");
		strExt
				.append("}if(Mrcdk[i].get('HETJSXSB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 加权方式 不能为空');return; \n");
		strExt
				.append("}if(Mrcdk[i].get('YUNSFSB_ID') == ''){Ext.MessageBox.alert('提示信息','字段 运输方式 不能为空');return; \n");
		strExt
				.append("}gridDivk_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">'"
						+ " + Mrcdk[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HETYS_ID update=\"true\">' "
						+ " + Mrcdk[i].get('HETYS_ID')+ '</HETYS_ID>'+ '<ZHIBB_ID update=\"true\">'"
						+ " + Mrcdk[i].get('ZHIBB_ID').replace('<','&lt;').replace('>','&gt;')+ '</ZHIBB_ID>'+ '<TIAOJB_ID update=\"true\">'"
						+ " + Mrcdk[i].get('TIAOJB_ID').replace('<','&lt;').replace('>','&gt;')+ '</TIAOJB_ID>'+ '<XIAX update=\"true\">'"
						+ " + Mrcdk[i].get('XIAX')+ '</XIAX>'+ '<SHANGX update=\"true\">'"
						+ " + Mrcdk[i].get('SHANGX')+ '</SHANGX>'+ '<DANWB_ID update=\"true\">'"
						+ " + Mrcdk[i].get('DANWB_ID').replace('<','&lt;').replace('>','&gt;')+ '</DANWB_ID>'+ '<JIS update=\"true\">'"
						+ " + Mrcdk[i].get('JIS')+ '</JIS>'+ '<JISDWID update=\"true\">'"
						+ " + Mrcdk[i].get('JISDWID').replace('<','&lt;').replace('>','&gt;')+ '</JISDWID>'+ '<KOUJ update=\"true\">'"
						+ " + Mrcdk[i].get('KOUJ')+ '</KOUJ>'+ '<KOUJDW update=\"true\">'"
						+ " + Mrcdk[i].get('KOUJDW').replace('<','&lt;').replace('>','&gt;')+ '</KOUJDW>'+ '<ZENGFJ update=\"true\">'"
						+ " + Mrcdk[i].get('ZENGFJ')+ '</ZENGFJ>'+ '<ZENGFJDW update=\"true\">'"
						+ " + Mrcdk[i].get('ZENGFJDW').replace('<','&lt;').replace('>','&gt;')+ '</ZENGFJDW>'+ '<XIAOSCL update=\"true\">'"
						+ " + Mrcdk[i].get('XIAOSCL').replace('<','&lt;').replace('>','&gt;')+ '</XIAOSCL>'+ '<JIZZKJ update=\"true\">'"
						+ " + Mrcdk[i].get('JIZZKJ')+ '</JIZZKJ>'+ '<JIZZB update=\"true\">'"
						+ " + Mrcdk[i].get('JIZZB')+ '</JIZZB>'+ '<CANZXM update=\"true\">'"
						+ " + Mrcdk[i].get('CANZXM').replace('<','&lt;').replace('>','&gt;')+ '</CANZXM>'+ '<CANZXMDW update=\"true\">'"
						+ " + Mrcdk[i].get('CANZXMDW').replace('<','&lt;').replace('>','&gt;')+ '</CANZXMDW>'+ '<CANZXX update=\"true\">'"
						+ " + Mrcdk[i].get('CANZXX')+ '</CANZXX>'+ '<CANZSX update=\"true\">'"
						+ " + Mrcdk[i].get('CANZSX')+ '</CANZSX>'+ '<HETJSXSB_ID update=\"true\">'"
						+ " + Mrcdk[i].get('HETJSXSB_ID').replace('<','&lt;').replace('>','&gt;')+ '</HETJSXSB_ID>'+ '<YUNSFSB_ID update=\"true\">'"
						+ " + Mrcdk[i].get('YUNSFSB_ID').replace('<','&lt;').replace('>','&gt;')+ '</YUNSFSB_ID>'+ '<BEIZ update=\"true\">'"
						+ " + Mrcdk[i].get('BEIZ').replace('<','&lt;').replace('>','&gt;')+ '</BEIZ>' + '</result>' ; } \n");

		strExt.append("if(gridDivk_history==''){  \n");
		// strExt.append("// Ext.MessageBox.alert('提示信息','没有进行改动无需保存'); \n");
		strExt.append("}else{ \n");
		strExt.append("var Cobjk = document.getElementById('CHANGEK'); \n");
		strExt
				.append("Cobjk.value = '<result>'+gridDivk_history+'</result>'; } \n");

		// 新加合同收货人项
		strExt.append("var Mrcds = gridDivs_ds.getRange(); \n");
		strExt.append("for(i = 0; i< Mrcds.length; i++){ \n");
		strExt
				.append("if(typeof(gridDivs_save)=='function'){ var revalue = gridDivs_save(Mrcds[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}} \n");
		strExt
				.append("if(Mrcds[i].get('SHOUHR') == ''){Ext.MessageBox.alert('提示信息','字段 收货人 不能为空');return;}\n");
		// strExt.append("}if(Mrcdk[i].get('TIAOJB_ID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 条件 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('DANWB_ID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 项目单位 不能为空');return; \n");
		// // strExt.append("}if(Mrcdk[i].get('JISDWID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 基数单位 不能为空');return; \n");
		// // strExt.append("}if(Mrcdk[i].get('KOUJDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 扣价单位 不能为空');return; \n");
		// // strExt.append("}if(Mrcdk[i].get('ZENGFJDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 增付价单位 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('XIAOSCL') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 小数处理方式 不能为空');return; \n");
		// // strExt.append("}if(Mrcdk[i].get('CANZXM') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 参照项目 不能为空');return; \n");
		// // strExt.append("}if(Mrcdk[i].get('CANZXMDW') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 参照项目单位 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('HETJSXSB_ID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 加权方式 不能为空');return; \n");
		// strExt.append("}if(Mrcdk[i].get('YUNSFSB_ID') ==
		// ''){Ext.MessageBox.alert('提示信息','字段 运输方式 不能为空');return; \n");
		strExt
				.append("gridDivs_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">'"
						+ " + Mrcds[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<HETYSB_ID update=\"true\">' "
						+ " + Mrcds[i].get('HETYSB_ID')+ '</HETYSB_ID>'+ '<SHOUHR update=\"true\">'"
						+ " + Mrcds[i].get('SHOUHR').replace('<','&lt;').replace('>','&gt;')+ '</SHOUHR>' + '</result>' ; } \n");

		strExt.append("if(gridDivs_history==''){  \n");
		// strExt.append("// Ext.MessageBox.alert('提示信息','没有进行改动无需保存'); \n");
		strExt.append("}else{ \n");
		strExt.append("var Cobjs = document.getElementById('CHANGES'); \n");
		strExt
				.append("Cobjs.value = '<result>'+gridDivs_history+'</result>'; } \n");

		strExt.append("document.Form0.BaocButton.click(); \n");
		strExt
				.append("Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig:  \n");
		strExt.append("{interval:200},icon:Ext.MessageBox.INFO}); \n");
		strExt.append("} \n");

		ToolbarButton savebt = new ToolbarButton(null, "保存", "function(){ "
				+ strExt.toString());
		// ToolbarButton savebt=new ToolbarButton(null,"保存","function(){
		// document.Form0.BaocButton.click(); "+strExt.toString());
		savebt.setId("BaocButton");
		tb1.addItem(savebt);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton fuzbt = new ToolbarButton(null, "复制合同",
				"function(){ document.Form0.FuzButton.click();}");
		fuzbt.setId("FuzButton");
		tb1.addItem(fuzbt);
		tb1.addText(new ToolbarText("-"));
		ToolbarButton tijbt = new ToolbarButton(null, "提交进入流程",
				"function(){ document.Form0.TijButton.click();}");
		tijbt.setId("TijButton");
		tb1.addItem(tijbt);
		tb1.addText(new ToolbarText("-"));

		setToolbar(tb1);
	}

	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}

	private void getXuanzht() {
		if (gethetSelectValue().getId() == -1) {
			Xinj();
		} else {
			Dak(gethetSelectValue().getId());

		}
	}

	private void getDanwGL() {
		String sql = "";
		List list = new ArrayList();
		StringBuffer Tem = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sql = "select zhibb_id zhibid,danwb.id danwid,danwb.mingc,zhibb.mingc\n"
				+ "from danwb,zhibb\n"
				+ "where danwb.zhibb_id=zhibb.id and zhibb_id<>0\n"
				+ "order by zhibb_id,zhibb.mingc ";
		ResultSet rs = con.getResultSet(sql);
		try {
			while (rs.next()) {
				String[] gl = new String[4];
				gl[0] = rs.getString(1);
				gl[1] = rs.getString(2);
				gl[2] = rs.getString(3);
				gl[3] = rs.getString(4);
				list.add(gl);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		Tem.append("var zhib_danw=new Array();");
		for (int i = 0; i < list.size(); i++) {
			Tem.append("zhib_danw[" + i + "]=new Array("
					+ ((String[]) list.get(i))[0] + ","
					+ ((String[]) list.get(i))[1] + ",'"
					+ ((String[]) list.get(i))[2] + "','"
					+ ((String[]) list.get(i))[3] + "');");// +
		}
		setDanwglStr(Tem.toString());
	}

	// 页面判定方法
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

	// 模板管理
	// private boolean _XinjButton = false;
	//
	// public void XinjButton(IRequestCycle cycle) {
	// _XinjButton = true;
	// }
	//
	// private boolean _DakButton = false;
	//
	// public void DakButton(IRequestCycle cycle) {
	// _DakButton = true;
	// }

	private boolean _ShancButton = false;

	public void ShancButton(IRequestCycle cycle) {
		_ShancButton = true;
	}

	private boolean _BaocButton = false;

	public void BaocButton(IRequestCycle cycle) {
		_BaocButton = true;
	}

	// 价格
	private boolean _InsertButtong = false;

	public void InsertButtong(IRequestCycle cycle) {
		_InsertButtong = true;
	}

	private boolean _DeleteButtong = false;

	public void DeleteButtong(IRequestCycle cycle) {
		_DeleteButtong = true;
	}

	// 增扣款
	// private boolean _InsertButtonk = false;

	public void InsertButtonk(IRequestCycle cycle) {
		// _InsertButtonk = true;
	}

	// private boolean _DeleteButtonk = false;

	public void DeleteButtonk(IRequestCycle cycle) {
		// _DeleteButtonk = true;
	}

	// 收货人
	private boolean _InsertButtons = false;

	public void InsertButtons(IRequestCycle cycle) {
		_InsertButtons = true;
	}

	private boolean _DeleteButtons = false;

	public void DeleteButtons(IRequestCycle cycle) {
		_DeleteButtons = true;
	}

	private boolean TijButton = false;

	public void TijButton(IRequestCycle cycle) {
		TijButton = true;
	}

	private boolean FuzButton = false;

	public void FuzButton(IRequestCycle cycle) {
		FuzButton = true;
	}

	public void submit(IRequestCycle cycle) {
		// 模板管理操作
		// if (_XinjButton) {
		// _XinjButton = false;
		// Xinj();
		// }
		// if (_DakButton) {
		// _DakButton = false;
		// Dak();
		// }
		if (TijButton) {
			TijButton = false;
			Tij();
		}
		if (_ShancButton) {
			_ShancButton = false;
			Shanc();
		}
		if (_BaocButton) {
			_BaocButton = false;
			Baoc();
		}
		if (FuzButton) {
			FuzButton = false;
			// if (gethetSelectValue().getId() == -1) {
			// return;
			// }
			fuz(cycle);
		}

		// 价格
		/*
		 * if (_InsertButtong) { _InsertButtong = false; Insertg(); } if
		 * (_DeleteButtong) { _DeleteButtong = false; Deleteg(); }
		 */
	}

	public void fuz(IRequestCycle cycle) {
		cycle.activate("Hetfz");
	}

	private void fuz(String hetxxb_id) {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String newid = MainGlobal.getNewID(visit.getDiancxxb_id());
		String sql = "begin\n"
				+ "  insert into hetys( HETBH,QIANDRQ,QIANDDD,YUNSDWB_ID,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR, \n"
				+ "  GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR, \n"
				+ "  XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,HETYS_MB_ID,MEIKMCS,LIUCZTB_ID, \n"
				+ "  LIUCGZID,ID,MINGC,FUID,DIANCXXB_ID,YUNSJGFAB_ID) \n"
				+ "  (select HETBH||'副本',QIANDRQ,QIANDDD,YUNSDWB_ID,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR, \n"
				+ "  GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR, \n"
				+ "  XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,HETYS_MB_ID,MEIKMCS,0, \n"
				+ "  LIUCGZID,"
				+ newid
				+ ",MINGC,FUID,DIANCXXB_ID,YUNSJGFAB_ID \n"
				+ "  from hetys where hetys.id="
				+ hetxxb_id
				+ ");  \n"
				+ "  insert into hetysjgb(id,hetys_id,meikxxb_id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id,yunja,yunjdw_id)  \n"
				+ "  (select getnewid("
				+ visit.getDiancxxb_id()
				+ "),"
				+ newid
				+ ",meikxxb_id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id,yunja,yunjdw_id from hetysjgb  \n"
				+ "  where hetys_id="
				+ hetxxb_id
				+ ");  \n"
				+ "  insert into hetyszkkb(jis,jisdwid,kouj,koujdw,zengfj,zengfjdw,xiaoscl,jizzkj,jizzb,canzxm, \n"
				+ "  canzxmdw,canzsx,canzxx,hetjsxsb_id,yunsfsb_id,beiz,hetys_id,id,zhibb_id,tiaojb_id,shangx,xiax,danwb_id) \n"
				+ "  (select jis,jisdwid,kouj,koujdw,zengfj,zengfjdw,xiaoscl,jizzkj,jizzb,canzxm, \n"
				+ "  canzxmdw,canzsx,canzxx,hetjsxsb_id,yunsfsb_id,beiz,"
				+ newid + ",getnewid(" + visit.getDiancxxb_id()
				+ "),zhibb_id,tiaojb_id,shangx,xiax,danwb_id \n"
				+ "  from hetyszkkb where hetyszkkb.hetys_id=" + hetxxb_id
				+ ");  \n" + "  insert into hetyswzb(id,wenznr,hetys_id) \n"
				+ "  (select getnewid(" + visit.getDiancxxb_id() + "),wenznr,"
				+ newid + " from hetyswzb where hetyswzb.hetys_id=" + hetxxb_id
				+ ");  \n"
				+ "  insert into hetysshrb (id, hetysb_id, shouhr_id)  \n"
				+ "  (select getnewid(" + visit.getDiancxxb_id() + ")," + newid
				+ ", shouhr_id from hetysshrb where hetysshrb.hetysb_id="
				+ hetxxb_id + ");   \n" + "end;";
		con.getInsert(sql);
		Dak(Long.parseLong(newid));
		gethetSelectModels();
		sethetSelectValue(getIDropDownBean(getmobmcSelectModel(), Long
				.parseLong(newid)));
		// getmobmcSelectModels();
		con.Close();
	}

	private void Tij() {
		Baoc();// 提交先进行保存工作
		String sql = "";
		long hetys_id = gethetysxxbean().getId();
		JDBCcon con = new JDBCcon();
		long liucb_id = 0;
		// 提交后进入相应流程的初始状态（xuh＝1）
		// long chuszt=0;
		// sql="select liucztb.id\n" +
		// "from hetb,hetb_mb,liucztb\n" +
		// "where hetb.hetb_mb_id=hetb_mb.id and
		// hetb_mb.liucb_id=liucztb.liucb_id and xuh=1 and hetb.id="+hetb_id;
		// ResultSet rs=con.getResultSet(sql);
		// try{
		// if(rs.next()){
		// chuszt=rs.getLong("id");
		// }
		// }catch(Exception e){
		// e.printStackTrace();
		// return;
		// }
		// sql="update hetb set liucztb_id=" +chuszt
		// +" where hetb.id="+hetb_id;
		// con.getUpdate(sql);
		// con.Close();

		sql = "select liucb.id\n"
				+ "from hetys,hetys_mb,liucb\n"
				+ "where hetys.hetys_mb_id=hetys_mb.id and hetys_mb.liucb_id=liucb.id and hetys.id="
				+ hetys_id;
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				liucb_id = rs.getLong("id");
			}
			Liuc.tij("hetys", hetys_id, ((Visit) getPage().getVisit())
					.getRenyID(), "", liucb_id);
			gethetSelectModels();
			Xinj();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			con.Close();
		}
	}

	private void Xinj() {
		setXinjht(true);
		// 合同信息
		Hetysxxbean bean = gethetysxxbean();
		// setliucSelectValue(getIDropDownBean(getliucSelectModel(),-1));
		setYunsjgfaValue(getIDropDownBean(getIYunsjgfaModel(), -1));
		// setmobmc("新建模板");
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(), -1));
		setxufValue(getIDropDownBean(getxufModel(), -1));
		setgongfValue(getIDropDownBean(getgongfModel(), -1));
		bean.setHetmc("");
		bean.setGONGFDBGH("");
		bean.setGONGFDH("");
		bean.setGONGFDWDZ("");
		bean.setGONGFDWMC("");
		bean.setGONGFFDDBR("");
		bean.setGONGFKHYH("");
		bean.setGongfsh("");
		bean.setGONGFWTDLR("");
		bean.setGONGFYZBM("");
		bean.setGONGFZH("");
		bean.setGuoqsj(null);
		bean.setHetbh("");
		// bean.setHetyj("");
		bean.setQianddd("");
		bean.setQiandsj(null);
		bean.setShengxsj(null);
		bean.setXUFDBGH("");
		bean.setXUFDH("");
		bean.setXUFDWDZ("");
		bean.setXUFDWMC("");
		bean.setXUFFDDBR("");
		bean.setXUFKHYH("");
		bean.setXufsh("");
		bean.setXUFWTDLR("");
		bean.setXUFYZBM("");
		bean.setXUFZH("");
		setFahr("");
		// 价格信息
		getHetysjgExt("mb", -1);
		// geteditValuesg().clear();

		// 文字信息
		setWenz("");
	}

	// 增扣款
	public ExtGridUtil getExtGridk() {
		if (((Visit) this.getPage().getVisit()).getExtGrid2() == null) {
			return null;
		}
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGridk(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScriptk() {
		if (getExtGridk() == null) {
			return "";
		}
		return getExtGridk().getGridScript();
	}

	public String getGridHtmlk() {
		if (getExtGridk() == null) {
			return "";
		}
		return getExtGridk().getHtml();
	}

	// 收货人
	public ExtGridUtil getExtGrids() {
		if (((Visit) this.getPage().getVisit()).getExtGrid3() == null) {
			return null;
		}
		return ((Visit) this.getPage().getVisit()).getExtGrid3();
	}

	public void setExtGrids(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid3(extgrid);
	}

	public String getGridScripts() {
		if (getExtGrids() == null) {
			return "";
		}
		return getExtGrids().getGridScript();
	}

	public String getGridHtmls() {
		if (getExtGrids() == null) {
			return "";
		}
		return getExtGrids().getHtml();
	}

	// 保存增扣款
	public void saveHetyszkk(JDBCcon con, long hetys_id) {

		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb.append("begin ");
		int i = 0;

		int id = 0;

		ResultSetList rssb = getExtGridk().getDeleteResultSet(getChangek());
		if (rssb == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetyszkkb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		ResultSetList rsl = getExtGridk().getModifyResultSet(getChangek());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetyszkkb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		while (rssb.next()) {
			id = rssb.getInt("id");
			sb.append("delete from hetyszkkb where id=" + id + ";\n");
			i++;
		}
		while (rsl.next()) {
			id = rsl.getInt("id");
			if (id == 0) {
				sb
						.append("insert into hetyszkkb (id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, jis, jisdwid,"
								+ " kouj, koujdw, zengfj, zengfjdw, xiaoscl, jizzkj, jizzb, canzxm, canzxmdw, canzsx, canzxx, hetjsxsb_id, yunsfsb_id, beiz, hetys_id) \n");
				sb.append("values (getnewid(").append(visit.getDiancxxb_id())
						.append("),").append(
								(getExtGridk().getColumn("zhibb_id").combo)
										.getBeanId(rsl.getString("zhibb_id")))
						.append(",");
				sb.append(
						(getExtGridk().getColumn("tiaojb_id").combo)
								.getBeanId(rsl.getString("tiaojb_id"))).append(
						",");
				sb.append(rsl.getDouble("shangx")).append(",").append(
						rsl.getDouble("xiax")).append(",").append(
						(getExtGridk().getColumn("danwb_id").combo)
								.getBeanId(rsl.getString("danwb_id"))).append(
						",");
				sb.append(rsl.getDouble("jis")).append(",").append(
						(getExtGridk().getColumn("jisdwid").combo)
								.getBeanId(rsl.getString("jisdwid"))).append(
						",");
				sb.append(rsl.getDouble("kouj")).append(",").append(
						(getExtGridk().getColumn("koujdw").combo).getBeanId(rsl
								.getString("koujdw"))).append(",");
				sb.append(rsl.getDouble("zengfj")).append(",").append(
						(getExtGridk().getColumn("zengfjdw").combo)
								.getBeanId(rsl.getString("zengfjdw"))).append(
						",");
				sb.append(
						(getExtGridk().getColumn("xiaoscl").combo)
								.getBeanId(rsl.getString("xiaoscl"))).append(
						",");
				sb.append(rsl.getDouble("jizzkj")).append(",").append(
						rsl.getDouble("jizzb")).append(",");
				sb.append(
						(getExtGridk().getColumn("canzxm").combo).getBeanId(rsl
								.getString("canzxm"))).append(",");
				sb.append(
						(getExtGridk().getColumn("canzxmdw").combo)
								.getBeanId(rsl.getString("canzxmdw"))).append(
						",");
				sb.append(rsl.getDouble("canzsx")).append(",").append(
						rsl.getDouble("canzxx")).append(",");
				sb.append(
						(getExtGridk().getColumn("hetjsxsb_id").combo)
								.getBeanId(rsl.getString("hetjsxsb_id")))
						.append(",");
				sb.append(
						(getExtGridk().getColumn("yunsfsb_id").combo)
								.getBeanId(rsl.getString("yunsfsb_id")))
						.append(",'");
				sb.append(rsl.getString("beiz")).append("',").append(hetys_id)
						.append(");\n");

			} else {

				sb.append("update hetyszkkb\n");
				sb.append("   set jis = ").append(rsl.getDouble("jis")).append(
						",\n");
				sb.append("       jisdwid = ").append(
						(getExtGridk().getColumn("jisdwid").combo)
								.getBeanId(rsl.getString("jisdwid"))).append(
						",\n");
				sb.append("       kouj = ").append(rsl.getDouble("kouj"))
						.append(",\n");
				sb.append("       koujdw = ").append(
						(getExtGridk().getColumn("koujdw").combo).getBeanId(rsl
								.getString("koujdw"))).append(",\n");
				sb.append("       zengfj = ").append(rsl.getDouble("zengfj"))
						.append(",\n");
				sb.append("       zengfjdw = ").append(
						(getExtGridk().getColumn("zengfjdw").combo)
								.getBeanId(rsl.getString("zengfjdw"))).append(
						",\n");
				sb.append("       xiaoscl = ").append(
						(getExtGridk().getColumn("xiaoscl").combo)
								.getBeanId(rsl.getString("xiaoscl"))).append(
						",\n");
				sb.append("       jizzkj = ").append(rsl.getDouble("jizzkj"))
						.append(",\n");
				sb.append("       jizzb = ").append(rsl.getDouble("jizzb"))
						.append(",\n");
				sb.append("       canzxm = ").append(
						(getExtGridk().getColumn("canzxm").combo).getBeanId(rsl
								.getString("canzxm"))).append(",\n");
				sb.append("       canzxmdw = ").append(
						(getExtGridk().getColumn("canzxmdw").combo)
								.getBeanId(rsl.getString("canzxmdw"))).append(
						",\n");
				sb.append("       canzsx = ").append(rsl.getDouble("canzsx"))
						.append(",\n");
				sb.append("       canzxx = ").append(rsl.getDouble("canzxx"))
						.append(",\n");
				sb.append("       hetjsxsb_id = ").append(
						(getExtGridk().getColumn("hetjsxsb_id").combo)
								.getBeanId(rsl.getString("hetjsxsb_id")))
						.append(",\n");
				sb.append("       yunsfsb_id = ").append(
						(getExtGridk().getColumn("yunsfsb_id").combo)
								.getBeanId(rsl.getString("yunsfsb_id")))
						.append(",\n");
				sb.append("       beiz = '").append(rsl.getString("beiz"))
						.append("',\n");
				sb.append("       zhibb_id = ").append(
						(getExtGridk().getColumn("zhibb_id").combo)
								.getBeanId(rsl.getString("zhibb_id"))).append(
						",\n");
				sb.append("       tiaojb_id = ").append(
						(getExtGridk().getColumn("tiaojb_id").combo)
								.getBeanId(rsl.getString("tiaojb_id"))).append(
						",\n");
				sb.append("       shangx = ").append(rsl.getDouble("shangx"))
						.append(",\n");
				sb.append("       xiax = ").append(rsl.getDouble("xiax"))
						.append(",\n");
				sb.append("       danwb_id = ").append(
						(getExtGridk().getColumn("danwb_id").combo)
								.getBeanId(rsl.getString("danwb_id"))).append(
						"\n");
				sb.append(" where id = " + id + ";");

			}
			i++;
		}
		sb.append("end;");
		if (i > 0) {
			con.getInsert(sb.toString());
		}
		setChangek(null);
	}

	public void saveHetysjg(JDBCcon con, long hetys_id) {

		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb.append("begin 	\n");
		int i = 0;

		long id = 0;

		ResultSetList rssb = getExtGrid().getDeleteResultSet(getChange());
		if (rssb == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetysjgb.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetysjgb.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		while (rssb.next()) {
			id = rssb.getLong("id");
			sb.append("delete from hetysjgb where id=" + id + ";\n");
			i++;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sb
						.append("insert into hetysjgb (id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax, danwb_id, yunja, yunjdw_id) \n");
				sb
						.append("values (getnewid(")
						.append(visit.getDiancxxb_id())
						.append("),")
						.append(hetys_id)
						.append(",")
						.append(
								(getExtGrid().getColumn("meikxxb_id").combo)
										.getBeanId(rsl.getString("meikxxb_id")))
						.append(",");
				sb.append(
						(getExtGrid().getColumn("zhibb_id").combo)
								.getBeanId(rsl.getString("zhibb_id"))).append(
						",").append(
						(getExtGrid().getColumn("tiaojb_id").combo)
								.getBeanId(rsl.getString("tiaojb_id"))).append(
						",");
				sb.append(rsl.getDouble("shangx")).append(",").append(
						rsl.getDouble("xiax")).append(",").append(
						(getExtGrid().getColumn("danwb_id").combo)
								.getBeanId(rsl.getString("danwb_id"))).append(
						",");
				sb.append(rsl.getDouble("yunja")).append(",").append(
						(getExtGrid().getColumn("yunjdw_id").combo)
								.getBeanId(rsl.getString("yunjdw_id"))).append(
						");\n");

			} else {

				sb.append("update HETYSJGB set meikxxb_id = "
						+ (getExtGrid().getColumn("meikxxb_id").combo)
								.getBeanId(rsl.getString("meikxxb_id")));
				sb.append(",       zhibb_id = "
						+ (getExtGrid().getColumn("zhibb_id").combo)
								.getBeanId(rsl.getString("zhibb_id")));
				sb.append(",       tiaojb_id = "
						+ (getExtGrid().getColumn("tiaojb_id").combo)
								.getBeanId(rsl.getString("tiaojb_id")));
				sb.append(",       shangx = " + rsl.getDouble("shangx")
						+ ",       xiax = " + rsl.getDouble("xiax"));
				sb.append(",       danwb_id = "
						+ (getExtGrid().getColumn("danwb_id").combo)
								.getBeanId(rsl.getString("danwb_id")));
				sb.append(",       yunja = " + rsl.getDouble("yunja"));
				sb.append(",       yunjdw_id = "
						+ (getExtGrid().getColumn("yunjdw_id").combo)
								.getBeanId(rsl.getString("yunjdw_id")));
				sb.append(" where id=" + id + ";\n");

			}
			i++;
		}
		sb.append("end;");
		if (i > 0) {
			con.getInsert(sb.toString());
		}
		setChange(null);
	}

	// 保存收货人
	public void saveHetysshr(JDBCcon con, long hetys_id) {

		Visit visit = (Visit) getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		sb.append("begin ");
		int i = 0;

		long id = 0;

		ResultSetList rssb = getExtGrids().getDeleteResultSet(getChanges());
		if (rssb == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetyszshr.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		ResultSetList rsl = getExtGrids().getModifyResultSet(getChanges());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "hetyszshr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			// setMsg(ErrorMessage.NullResult);
			return;
		}

		while (rssb.next()) {
			id = rssb.getLong("id");
			sb.append("delete from hetysshrb where id=" + id + ";\n");
			i++;
		}
		while (rsl.next()) {
			id = rsl.getLong("id");
			if (id == 0) {
				sb
						.append("insert into hetysshrb (id, hetysb_id, shouhr_id) \n");
				sb.append("values (getnewid(").append(visit.getDiancxxb_id())
						.append("),").append(hetys_id).append(",").append(
								(getExtGrids().getColumn("shouhr").combo)
										.getBeanId(rsl.getString("shouhr")))
						.append(");\n");
			} else {

				sb.append("update hetysshrb\n");
				sb.append("   set hetysb_id = ").append(hetys_id).append(",\n");
				sb.append("       shouhr_id = ").append(
						(getExtGrids().getColumn("shouhr").combo).getBeanId(rsl
								.getString("shouhr"))).append("\n");
				sb.append(" where id = " + id + ";");
			}
			i++;
		}
		sb.append("end;");
		if (i > 0) {
			con.getInsert(sb.toString());
		}
		setChanges(null);
	}

	public void getHetysjgExt(String leix, long hetys_id) {
		JDBCcon con = new JDBCcon();

		String sql = "select decode('"
				+ leix
				+ "','mb',0,j.id) as id, j.hetys_id, m.mingc as meikxxb_id, z.mingc as zhibb_id, zd.mingc as danwb_id, t.mingc as tiaojb_id, j.xiax, j.shangx,j. yunja, yd.mingc as yunjdw_id \n "
				+ " from hetysjgb j,meikxxb m,zhibb z,tiaojb t,danwb zd, danwb yd \n"
				+ " where j.meikxxb_id=m.id(+) and j.zhibb_id=z.id(+) and j.tiaojb_id=t.id(+) and j.danwb_id=zd.id(+) and j.yunjdw_id=yd.id(+) and hetys_id="
				+ hetys_id + " order by z.mingc,j.xiax";
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("hetysjgb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight-135");

		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);

		egu.getColumn("hetys_id").setHidden(true);
		egu.getColumn("hetys_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("zhibb_id").setHeader("指标");
		egu.getColumn("danwb_id").setHeader("指标单位");
		egu.getColumn("tiaojb_id").setHeader("条件");
		egu.getColumn("xiax").setHeader("下限");
		egu.getColumn("xiax").setDefaultValue("0");
		egu.getColumn("shangx").setHeader("上限");
		egu.getColumn("shangx").setDefaultValue("0");
		egu.getColumn("yunja").setHeader("运价");
		egu.getColumn("yunja").setDefaultValue("0");
		egu.getColumn("yunjdw_id").setHeader("运价单位");

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);

		// 设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql1 = "select id,mingc from\n"
				+ "((select 0 as xuh,0 as id,'' as mingc from dual\n"
				+ " union\n" + "select 1 as xuh,id, mingc from meikxxb)\n"
				+ " order by xuh,mingc)   ";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql1));
		egu.getColumn("meikxxb_id").setReturnId(true);

		ComboBox c2 = new ComboBox();
		egu.getColumn("zhibb_id").setEditor(c2);
		c2.setEditable(true);

		String Sql2 = "select id,mingc from\n"
				+ "((select 0 as xuh,0 as id,'' as mingc from dual\n"
				+ "        union\n"
				+ "	select 1 as xuh,id,mingc from zhibb where zhibb.leib=1\n"
				+ "		and zhibb.bianm in ('" + Locale.Yunju_zhibb + "'))\n"
				+ "		order by xuh,mingc)";
		egu.getColumn("zhibb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql2));
		egu.getColumn("zhibb_id").setReturnId(true);

		ComboBox c3 = new ComboBox();
		egu.getColumn("danwb_id").setEditor(c3);
		c3.setEditable(true);
		String Sql3 = "select id,mingc from \n"
				+ "((select 0 as xuh,0 as id,'' as mingc from dual\n"
				+ "		union\n"
				+ "	select 1 as xuh,id,mingc from danwb d where zhibb_id\n"
				+ "       in (select id from zhibb where leib=1 and bianm in ('"
				+ Locale.Yunju_zhibb + "')))\n" + "order by xuh,mingc)";
		egu.getColumn("danwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql3));
		egu.getColumn("danwb_id").setReturnId(true);

		ComboBox c4 = new ComboBox();
		egu.getColumn("tiaojb_id").setEditor(c4);
		c4.setEditable(true);
		String Sql4 = "select id,mingc from \n"
				+ "((select 0 as xuh,0 as id,'' as mingc from dual\n"
				+ "		union\n"
				+ "	select 1 as xuh,id,mingc from tiaojb where tiaojb.leib=1)\n"
				+ "	order by xuh,mingc)";
		egu.getColumn("tiaojb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql4));
		egu.getColumn("tiaojb_id").setReturnId(true);

		ComboBox c5 = new ComboBox();
		egu.getColumn("yunjdw_id").setEditor(c5);
		c5.setEditable(true);
		String Sql5 = "select id,mingc from danwb d where zhibb_id\n"
				+ "       in (select id from zhibb where leib=1 and bianm in ('"
				+ Locale.Yunja_zhibb + "'))\n" + "order by mingc";
		egu.getColumn("yunjdw_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql5));
		egu.getColumn("yunjdw_id").setReturnId(true);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		// egu.addTbarText("-");
		// egu.addToolbarButton(GridButton.ButtonType_SaveAll, null);

		setExtGrid(egu);
		con.Close();
	}

	private void Dak(long hetys_id) {
		setXinjht(false);
		String sql = "";
		JDBCcon con = new JDBCcon();
		try {
			// 合同信息
			sql = "select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,"
					+ "GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,"
					+ "XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,yunsdwb_id,"
					+ "MINGC,diancxxb_id,meikmcs,yunsjgfab_id "
					+ "from hetys"
					+ " where ID=" + hetys_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				Hetysxxbean bean = gethetysxxbean();
				// setmobmc(rs.getString("MINGC"));
				bean.setId(rs.getLong("id"));
				setgongfValue(getIDropDownBean(getgongfModel(), rs
						.getLong("YUNSDWB_ID")));
				setxufValue(getIDropDownBean(getxufModel(), rs
						.getLong("diancxxb_id")));
				setYunsjgfaValue(getIDropDownBean(getIYunsjgfaModel(), rs
						.getLong("yunsjgfab_id")));
				bean.setHetmc(rs.getString("MINGC"));
				bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
				bean.setGONGFDH(rs.getString("GONGFDH"));
				bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
				bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
				bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
				bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
				bean.setGongfsh(rs.getString("Gongfsh"));
				bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
				bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
				bean.setGONGFZH(rs.getString("GONGFZH"));
				bean.setGuoqsj(rs.getDate("GUOQRQ"));
				bean.setHetbh(rs.getString("Hetbh"));
				bean.setQianddd(rs.getString("Qianddd"));
				bean.setQiandsj(rs.getDate("QIANDRQ"));
				bean.setShengxsj(rs.getDate("QISRQ"));
				bean.setXUFDBGH(rs.getString("XUFDBGH"));
				bean.setXUFDH(rs.getString("XUFDH"));
				bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
				bean.setXUFDWMC(rs.getString("XUFDWMC"));
				bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
				bean.setXUFKHYH(rs.getString("XUFKHYH"));
				bean.setXufsh(rs.getString("Xufsh"));
				bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
				bean.setXUFYZBM(rs.getString("XUFYZBM"));
				bean.setXUFZH(rs.getString("XUFZH"));
				setFahr(rs.getString("meikmcs"));
			}

			// 价格信息
			getHetysjgExt("ht", hetys_id);
			getYunshtzkkExt("ht", hetys_id);
			getShouhrExt("ht", hetys_id);
			// 文字信息
			sql = "select id,wenznr\n" + "from hetyswzb\n" + "where hetys_id="
					+ hetys_id;
			ResultSet rs4 = con.getResultSet(sql);
			if (rs4.next()) {
				setWenz(rs4.getString("wenznr"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	// 新加增扣款项
	public void getYunshtzkkExt(String leix, long hetys_id) {

		JDBCcon con = new JDBCcon();

		String sql = "select  decode('"
				+ leix
				+ "','mb',0,z.id) as id,z.hetys_id,zhibb.mingc as zhibb_id,tiaojb.mingc as tiaojb_id,z.xiax,z.shangx,zhibdw.mingc as danwb_id,\n"
				+ "       z.jis,jisdw.mingc as jisdwid,z.kouj, koujdw.mingc as koujdw,z.ZENGFJ,zengfjdw.mingc as zengfjdw,\n"
				+ "       decode(z.xiaoscl,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)','') as xiaoscl,\n"
				+ "       z.jizzkj,z.jizzb,CANZXM.Mingc as canzxm,CANZXMDW.Mingc as canzxmdw,z.canzxx,z.canzsx,hetjsxsb.mingc as hetjsxsb_id,\n"
				+ "       yunsfsb.mingc as yunsfsb_id, z.BEIZ\n"
				+ "  from hetyszkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb\n"
				+ " where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n"
				+ "   and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n"
				+ "   and z.yunsfsb_id=yunsfsb.id(+) and z.hetys_id="
				+ hetys_id + " order by zhibb.mingc,z.xiax";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDivk", rsl);
		egu.setTableName("hetyszkkb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight-135");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hetys_id").setHidden(true);
		egu.getColumn("hetys_id").setEditor(null);
		egu.getColumn("zhibb_id").setHeader("项目");
		egu.getColumn("tiaojb_id").setHeader("条件");
		egu.getColumn("xiax").setHeader("下限");
		egu.getColumn("xiax").setDefaultValue("0");
		egu.getColumn("shangx").setHeader("上限");
		egu.getColumn("shangx").setDefaultValue("0");

		egu.getColumn("danwb_id").setHeader("项目单位");
		egu.getColumn("jis").setHeader("基数");
		egu.getColumn("jis").setDefaultValue("0");
		egu.getColumn("jisdwid").setHeader("基数单位");

		egu.getColumn("kouj").setHeader("扣价");
		egu.getColumn("kouj").setDefaultValue("0");
		egu.getColumn("koujdw").setHeader("扣价单位");

		egu.getColumn("zengfj").setHeader("增付价");
		egu.getColumn("zengfj").setDefaultValue("0");
		egu.getColumn("zengfjdw").setHeader("增付价单位");

		egu.getColumn("xiaoscl").setHeader("小数处理方式");

		egu.getColumn("jizzkj").setHeader("基准增扣价");
		egu.getColumn("jizzkj").setDefaultValue("0");
		egu.getColumn("jizzb").setHeader("基准指标");
		egu.getColumn("jizzb").setDefaultValue("0");
		egu.getColumn("canzxm").setHeader("参照项目");
		egu.getColumn("canzxmdw").setHeader("参照项目单位");
		egu.getColumn("canzxx").setHeader("下限");
		egu.getColumn("canzxx").setDefaultValue("0");
		egu.getColumn("canzsx").setHeader("上限");
		egu.getColumn("canzsx").setDefaultValue("0");

		egu.getColumn("hetjsxsb_id").setHeader("加权方式");
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("beiz").setHeader("备注");

		egu.getColumn("zhibb_id").setWidth(100);
		egu.getColumn("tiaojb_id").setWidth(60);
		egu.getColumn("xiax").setWidth(50);
		egu.getColumn("shangx").setWidth(50);
		egu.getColumn("danwb_id").setWidth(70);
		egu.getColumn("jis").setWidth(50);
		egu.getColumn("jisdwid").setWidth(70);
		egu.getColumn("kouj").setWidth(50);
		egu.getColumn("koujdw").setWidth(70);
		egu.getColumn("zengfj").setWidth(60);
		egu.getColumn("zengfjdw").setWidth(80);
		egu.getColumn("xiaoscl").setWidth(80);
		egu.getColumn("jizzkj").setWidth(70);
		egu.getColumn("jizzb").setWidth(60);
		egu.getColumn("canzxm").setWidth(80);
		egu.getColumn("canzxmdw").setWidth(80);
		egu.getColumn("canzxx").setWidth(60);
		egu.getColumn("canzsx").setWidth(60);
		egu.getColumn("hetjsxsb_id").setWidth(70);
		egu.getColumn("yunsfsb_id").setWidth(70);
		egu.getColumn("beiz").setWidth(200);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(30);

		// 设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("zhibb_id").setEditor(c1);
		c1.setEditable(true);
		String Sql1 = "select id,mingc from zhibb order by mingc ";
		egu.getColumn("zhibb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql1));
		egu.getColumn("zhibb_id").setReturnId(true);

		ComboBox c2 = new ComboBox();
		egu.getColumn("tiaojb_id").setEditor(c2);
		c2.setEditable(true);
		String Sql2 = "select id,mingc from tiaojb where tiaojb.leib=1   order by mingc ";
		egu.getColumn("tiaojb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql2));
		egu.getColumn("tiaojb_id").setReturnId(true);

		ComboBox c3 = new ComboBox();
		egu.getColumn("danwb_id").setEditor(c3);
		c3.setEditable(true);
		String Sql3 = "select id,mingc from danwb   order by mingc ";
		egu.getColumn("danwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql3));
		egu.getColumn("danwb_id").setReturnId(true);

		ComboBox c4 = new ComboBox();
		egu.getColumn("jisdwid").setEditor(c4);
		c4.setEditable(true);
		String Sql4 = "select id,mingc from danwb  order by mingc  ";
		egu.getColumn("jisdwid").setComboEditor(egu.gridId,
				new IDropDownModel(Sql4));
		egu.getColumn("jisdwid").setReturnId(true);

		ComboBox c5 = new ComboBox();
		egu.getColumn("koujdw").setEditor(c5);
		c5.setEditable(true);
		String Sql5 = "select id,mingc from danwb   order by mingc  ";
		egu.getColumn("koujdw").setComboEditor(egu.gridId,
				new IDropDownModel(Sql5));
		egu.getColumn("koujdw").setReturnId(true);

		ComboBox c6 = new ComboBox();
		egu.getColumn("zengfjdw").setEditor(c6);
		c6.setEditable(true);
		String Sql6 = "select id,mingc from danwb  order by mingc  ";
		egu.getColumn("zengfjdw").setComboEditor(egu.gridId,
				new IDropDownModel(Sql6));
		egu.getColumn("zengfjdw").setReturnId(true);

		ComboBox c7 = new ComboBox();
		egu.getColumn("xiaoscl").setEditor(c7);
		c7.setEditable(true);
		List list = new ArrayList();
		list.add(new IDropDownBean(0, " "));
		list.add(new IDropDownBean(1, "进位"));
		list.add(new IDropDownBean(2, "舍去"));
		list.add(new IDropDownBean(3, "四舍五入"));
		list.add(new IDropDownBean(4, "四舍五入(0.1)"));
		list.add(new IDropDownBean(5, "四舍五入(0.01)"));
		egu.getColumn("xiaoscl").setComboEditor(egu.gridId,
				new IDropDownModel(list));
		egu.getColumn("xiaoscl").setReturnId(true);

		ComboBox c8 = new ComboBox();
		egu.getColumn("canzxm").setEditor(c8);
		c8.setEditable(true);
		String Sql8 = "select id,mingc from zhibb order by mingc ";
		egu.getColumn("canzxm").setComboEditor(egu.gridId,
				new IDropDownModel(Sql8));
		egu.getColumn("canzxm").setReturnId(true);

		ComboBox c9 = new ComboBox();
		egu.getColumn("canzxmdw").setEditor(c9);
		c9.setEditable(true);
		String Sql9 = "select id,mingc from danwb  order by mingc  ";
		egu.getColumn("canzxmdw").setComboEditor(egu.gridId,
				new IDropDownModel(Sql9));
		egu.getColumn("canzxmdw").setReturnId(true);

		ComboBox c10 = new ComboBox();
		egu.getColumn("hetjsxsb_id").setEditor(c10);
		c10.setEditable(true);
		String Sql10 = "select id,mingc from hetjsxsb order by mingc ";
		egu.getColumn("hetjsxsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql10));
		egu.getColumn("hetjsxsb_id").setReturnId(true);

		ComboBox c11 = new ComboBox();
		egu.getColumn("yunsfsb_id").setEditor(c11);
		c11.setEditable(true);
		String Sql11 = "select id,mingc from yunsfsb order by mingc ";
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel(Sql11));
		egu.getColumn("yunsfsb_id").setReturnId(true);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		// egu.addTbarText("-");
		// egu.addToolbarButton(GridButton.ButtonType_Save, null);

		setExtGridk(egu);
		con.Close();

	}

	// 新加收货人项
	public void getShouhrExt(String leix, long hetys_id) {

		JDBCcon con = new JDBCcon();

		String sql = "select  decode('" + leix
				+ "','mb',0,h.id) as id,h.hetysb_id,d.mingc as shouhr\n"
				+ "from hetysshrb h,diancxxb d\n"
				+ "where h.shouhr_id = d.id\n" + "      and h.hetysb_id ="
				+ hetys_id + "\n" + "order by d.id";

		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDivs", rsl);
		egu.setTableName("hetysshrb");
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight-135");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hetysb_id").setHeader("hetysb_id");
		egu.getColumn("hetysb_id").setHidden(true);
		egu.getColumn("hetysb_id").setEditor(null);
		egu.getColumn("shouhr").setHeader("收货人");
		egu.getColumn("shouhr").setWidth(200);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(30);

		// 设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("shouhr").setEditor(c1);
		c1.setEditable(true);
		// String Sql1 = "select id,mingc from diancxxb order by mingc ";
		/*
		 * huochaoyuan 2009-10-22修改到站下拉框取数
		 */
		String Sql1 = "select id,mingc from vwdianc\n" + "where id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "\n"
				+ "or\n" + "fuid="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id() + "   order by mingc \n";
		// end
		egu.getColumn("shouhr").setComboEditor(egu.gridId,
				new IDropDownModel(Sql1));
		egu.getColumn("shouhr").setReturnId(true);

		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		// egu.addTbarText("-");
		// egu.addToolbarButton(GridButton.ButtonType_Save, null);

		setExtGrids(egu);
		con.Close();

	}

	private void getGongf() {
		Hetysxxbean htxxbean = gethetysxxbean();
		htxxbean.setGONGFDH("");
		htxxbean.setGONGFDWDZ("");
		htxxbean.setGONGFDWMC("");
		htxxbean.setGONGFFDDBR("");
		htxxbean.setGONGFZH("");
		htxxbean.setGONGFKHYH("");
		htxxbean.setGONGFYZBM("");
		htxxbean.setGONGFWTDLR("");
		htxxbean.setGongfsh("");
		htxxbean.setGONGFDBGH("");
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,danwdz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH,chuanz \n"
				+ "from yunsdwb where id=" + getgongfValue().getId() +"  order by  quanc ";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String Gongfdn = rs.getString("DIANH");
				String Gongfdwdz = rs.getString("danwdz");
				String Gongfdwmc = rs.getString("quanc");
				String Gongffddbr = rs.getString("FADDBR");
				String Gongfzh = rs.getString("ZHANGH");
				String Gongfkhyh = rs.getString("KAIHYH");
				String Gongfyzbm = rs.getString("YOUZBM");
				String Gongfwtdlr = rs.getString("WEITDLR");
				String shuih = rs.getString("shuih");
				String chuanz = rs.getString("chuanz");
				htxxbean.setGONGFDH(Gongfdn);
				htxxbean.setGONGFDWDZ(Gongfdwdz);
				htxxbean.setGONGFDWMC(Gongfdwmc);
				htxxbean.setGONGFFDDBR(Gongffddbr);
				htxxbean.setGONGFZH(Gongfzh);
				htxxbean.setGONGFKHYH(Gongfkhyh);
				htxxbean.setGONGFYZBM(Gongfyzbm);
				htxxbean.setGONGFWTDLR(Gongfwtdlr);
				htxxbean.setGongfsh(shuih);
				htxxbean.setGONGFDBGH(chuanz);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		// 发货人同时相同
		// setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),getgongfValue().getId()));

	}

	private void getXuf() {
		Hetysxxbean htxxbean = gethetysxxbean();
		htxxbean.setXUFDH("");
		htxxbean.setXUFDWDZ("");
		htxxbean.setXUFDWMC("");
		htxxbean.setXUFFDDBR("");
		htxxbean.setXUFZH("");
		htxxbean.setXUFKHYH("");
		htxxbean.setXUFYZBM("");
		htxxbean.setXUFWTDLR("");
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
				+ "from diancxxb where id=" + getxufValue().getId()+" order by  xuh ";
		ResultSet rs = con.getResultSet(sql);
		try {
			if (rs.next()) {
				String XUFDH = rs.getString("DIANH");
				String XUFDWDZ = rs.getString("DIZ");
				String XUFDWMC = rs.getString("QUANC");
				String XUFFDDBR = rs.getString("FADDBR");
				String XUFZH = rs.getString("ZHANGH");
				String XUFKHYH = rs.getString("KAIHYH");
				String XUFYZBM = rs.getString("YOUZBM");
				String XUFWTDLR = rs.getString("WEITDLR");
				htxxbean.setXUFDH(XUFDH);
				htxxbean.setXUFDWDZ(XUFDWDZ);
				htxxbean.setXUFDWMC(XUFDWMC);
				htxxbean.setXUFFDDBR(XUFFDDBR);
				htxxbean.setXUFZH(XUFZH);
				htxxbean.setXUFKHYH(XUFKHYH);
				htxxbean.setXUFYZBM(XUFYZBM);
				htxxbean.setXUFWTDLR(XUFWTDLR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	private void Shanc() {
		String sql = "";
		JDBCcon con = new JDBCcon();
		long hetys_id = gethetSelectValue().getId();
		// 删除合同信息表
		con.setAutoCommit(false);
		try {
			sql = "delete\n" + "from hetys\n" + "where id=" + hetys_id;
			con.getDelete(sql);

			sql = "delete \n" + "from hetysjgb\n" + "where hetysjgb.hetys_id="
					+ hetys_id;
			con.getDelete(sql);
			sql = "delete\n" + "from hetyswzb\n" + "where hetyswzb.hetys_id="
					+ hetys_id;
			con.getDelete(sql);
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
		} finally {
			con.Close();
		}
		Xinj();
		gethetSelectModels();
	}

	private void Baoc() {
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		int result = -1;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Hetysxxbean bean = gethetysxxbean();
		String sql = "";
		try {

            String hsql="select id from hetys where hetbh='"+bean.getHetbh()+"'";
            ResultSet hrs=con.getResultSet(hsql);
            logger.info(hsql);
			if ((!hrs.next())&&gethetSelectValue().getId() == -1) {// 如果是新建模板
				// 插入（新增）保存
				// 保存合同信息（包括供方需方）

				long hetys_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit())
								.getDiancxxb_id()));
				sql = "insert into hetys(id, mingc, diancxxb_id, hetbh, qiandrq, qianddd, yunsdwb_id, gongfdwmc, gongfdwdz,"
						+ " gongfdh, gongffddbr, gongfwtdlr, gongfdbgh, gongfkhyh, gongfzh, gongfyzbm, gongfsh, xufdwmc, xufdwdz,"
						+ " xuffddbr, xufwtdlr, xufdh, xufdbgh, xufkhyh, xufzh, xufyzbm, xufsh, qisrq, guoqrq, hetys_mb_id, meikmcs, liucztb_id, liucgzid,yunsjgfab_id)\n"
						+ "values("
						+ hetys_id
						+ ",'"
						+ bean.getHetmc()
						+ "',"
						// + ((Visit)
						// getPage().getVisit()).getDiancxxb_id()//getxufValue().getId()
						+ getxufValue().getId()
						+ ",'"
						+ bean.getHetbh()
						+ "',to_date('"
						+ format.format(bean.getQiandsj())
						+ "','YYYY-MM-DD'),'"
						+ bean.getQianddd()
						+ "',"
						+ getgongfValue().getId()
						+ ",'"
						+ bean.getGONGFDWMC()
						+ "','"
						+ bean.getGONGFDWDZ()
						+ "','"
						+ bean.getGONGFDH()
						+ "','"
						+ bean.getGONGFFDDBR()
						+ "','"
						+ bean.getGONGFWTDLR()
						+ "','"
						+ bean.getGONGFDBGH()
						+ "','"
						+ bean.getGONGFKHYH()
						+ "','"
						+ bean.getGONGFZH()
						+ "','"
						+ bean.getGONGFYZBM()
						+ "','"
						+ bean.getGongfsh()
						+ "','"
						+ bean.getXUFDWMC()
						+ "','"
						+ bean.getXUFDWDZ()
						+ "','"
						+ bean.getXUFFDDBR()
						+ "','"
						+ bean.getXUFWTDLR()
						+ "','"
						+ bean.getXUFDH()
						+ "','"
						+ bean.getXUFDBGH()
						+ "','"
						+ bean.getXUFKHYH()
						+ "','"
						+ bean.getXUFZH()
						+ "','"
						+ bean.getXUFYZBM()
						+ "','"
						+ bean.getXufsh()
						+ "',"
						+ "to_date('"
						+ format.format(bean.getShengxsj())
						+ "','YYYY-MM-DD'),to_date('"
						+ format.format(bean.getGuoqsj())
						+ "','YYYY-MM-DD'),"
						+ getmobmcSelectValue().getId()
						+ ",'"
						+ getFahr()
						+ "'," + 0 // 流程状态表ID
						+ "," + 0 // 流程跟踪表ID
						+ "," + getYunsjgfaValue().getId() + ")";
				result = con.getInsert(sql);

				if (result < 0) {
					con.rollBack();
					con.Close();
					System.out.println(DateUtil.FormatDateTime(new Date())
							+ "运输合同保存失败！-----------("
							+ ((Visit) getPage().getVisit()).getRenymc() + ")");
					return;
				}

				// 保存价格
				if (getChange() != null && !getChange().equals("")) {
					saveHetysjg(con, hetys_id);
				}

				// 保存增扣款
				if (getChangek() != null && !getChangek().equals("")) {
					saveHetyszkk(con, hetys_id);
				}

				// 保存收货人
				if (getChanges() != null && !getChanges().equals("")) {
					saveHetysshr(con, hetys_id);
				}

				// 保存文字
				result = -1;
				long hetwzb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit())
								.getDiancxxb_id()));
				sql = "insert into HETYSWZB (id,wenznr,HETYS_id)values("
						+ hetwzb_id + ",'" + getWenz() + "'," + hetys_id + ")";
				result = con.getInsert(sql);
				if (result < 0) {
					con.rollBack();
					con.Close();
					System.out.println(DateUtil.FormatDateTime(new Date())
							+ "运输合同文字保存失败！-----------("
							+ ((Visit) getPage().getVisit()).getRenymc() + ")");
					return;
				}
				con.commit();
				// 刷新模板下拉框
				gethetSelectModels();
				sethetSelectValue(getIDropDownBean(gethetSelectModel(),
						hetys_id));
			} else {// 如果不是新建模板则是已经存在的模板，进行更新操作
				// 保存合同信息（包括供方需方）
				result = -1;
				long hetys_id = gethetSelectValue().getId();
				sql = "update hetys set HETBH='" + bean.getHetbh()
						+ "',mingc='" + bean.getHetmc() + "',QIANDRQ=to_date('"
						+ format.format(bean.getQiandsj())
						+ "','YYYY-MM-DD'),QIANDDD='" + bean.getQianddd()
						+ "',GONGFDWMC='" + bean.getGONGFDWMC()
						+ "',GONGFDWDZ='" + bean.getGONGFDWDZ() + "',GONGFDH='"
						+ bean.getGONGFDH() + "',GONGFFDDBR='"
						+ bean.getGONGFFDDBR() + "',GONGFWTDLR='"
						+ bean.getGONGFWTDLR() + "',GONGFDBGH='"
						+ bean.getGONGFDBGH() + "',GONGFKHYH='"
						+ bean.getGONGFKHYH() + "',GONGFZH='"
						+ bean.getGONGFZH() + "',GONGFYZBM='"
						+ bean.getGONGFYZBM() + "',GONGFSH='"
						+ bean.getGongfsh() + "',XUFDWMC='" + bean.getXUFDWMC()
						+ "',XUFDWDZ='" + bean.getXUFDWDZ() + "',XUFFDDBR='"
						+ bean.getXUFFDDBR() + "',XUFWTDLR='"
						+ bean.getXUFWTDLR() + "',XUFDH='" + bean.getXUFDH()
						+ "',XUFDBGH='" + bean.getXUFDBGH()
						+ "',XUFKHYH='"
						+ bean.getXUFKHYH()
						+ "',XUFZH='"
						+ bean.getXUFZH()
						+ "',XUFYZBM='"
						+ bean.getXUFYZBM()
						+ "',XUFSH='"
						+ bean.getXufsh()
						// +"',HETGYSBID=" + getgongfValue().getId()
						+ "',YUNSDWB_ID=" + getgongfValue().getId()
						+ ",QISRQ=to_date('"
						+ format.format(bean.getShengxsj())
						+ "','YYYY-MM-DD'),GUOQRQ=to_date('"
						+ format.format(bean.getGuoqsj()) + "','YYYY-MM-DD')"
						+ ",meikmcs='" + getFahr()

						+ "',diancxxb_id="
						// +((Visit) getPage().getVisit()).getDiancxxb_id()
						/*
						 * huochaoyuan 2009-10-22修改保存合同时，hetys表中的diancxxb保存需方id;
						 */
						+ getxufValue().getId() + ",yunsjgfab_id="
						+ getYunsjgfaValue().getId() + " where id=" + hetys_id;

				result = con.getUpdate(sql);
				if (result < 0) {
					con.rollBack();
					con.Close();
					System.out.println(DateUtil.FormatDateTime(new Date())
							+ "运输合同更新失败！-----------("
							+ ((Visit) getPage().getVisit()).getRenymc() + ")");
					return;
				}
				// 保存价格
				if (getChange() != null && !getChange().equals("")) {
					saveHetysjg(con, hetys_id);
				}
				// 保存增扣款
				if (getChangek() != null && !getChangek().equals("")) {
					saveHetyszkk(con, hetys_id);
				}
				// 保存收货人
				if (getChanges() != null && !getChanges().equals("")) {
					saveHetysshr(con, hetys_id);
				}
				/*
				 * List list = geteditValuesg();
				 * 
				 * for (int i = 0; i < list.size(); i++) {// 遍历容器 result = -1;
				 * long hetjgb_id = ((Yunsjijbean) list.get(i)).getId(); long
				 * meikxxb_id = getProperId(getMeikxxModel(),((Yunsjijbean)
				 * list.get(i)).getMeikxxb_id()); long zhibb_id =
				 * getProperId(getZhibSelectModel(),((Yunsjijbean)
				 * list.get(i)).getZhibb_id()); long tiaoj_id =
				 * getProperId(gettiaojjSelectModel(),((Yunsjijbean)
				 * list.get(i)).getTiaoj_id()); double shangx = ((Yunsjijbean)
				 * list.get(i)).getShangx(); double xiax = ((Yunsjijbean)
				 * list.get(i)).getXiax(); double yunjia = ((Yunsjijbean)
				 * list.get(i)).getYunjia(); long zhibdw_id =
				 * getProperId(getzhibdwSelectModel(),((Yunsjijbean)
				 * list.get(i)).getZhibdw_id()); // double yunj = ((Yunsjijbean)
				 * list.get(i)).getYunj(); long yunjiadw_id =
				 * getProperId(getYunjiadwModel(),((Yunsjijbean)
				 * list.get(i)).getYunjdw_id()); if(hetjgb_id==0){
				 * hetjgb_id=Long.parseLong(MainGlobal.getNewID(((Visit)
				 * getPage().getVisit()).getDiancxxb_id())); sql= "insert into
				 * HETYSJGB(id, hetys_id, meikxxb_id, zhibb_id, tiaojb_id,
				 * shangx, xiax, danwb_id, yunja, yunjdw_id) values("
				 * +"xl_xul_id.nextval" +"," +hetys_id +"," +meikxxb_id +","
				 * +zhibb_id +"," +tiaoj_id +"," +shangx +"," +xiax +","
				 * +zhibdw_id +"," +yunjia +"," +yunjiadw_id +")"; result =
				 * con.getInsert(sql); if(result<0){ con.rollBack();
				 * con.Close(); System.out.println(DateUtil.FormatDateTime(new
				 * Date())+"运输合同价格添加失败！-----------("+((Visit)getPage().getVisit()).getRenymc()+")");
				 * return; }else{ ((Yunsjijbean) list.get(i)).setId(hetjgb_id); }
				 * 
				 * }else{ sql= "update HETYSJGB set meikxxb_id = "+meikxxb_id+ ",
				 * zhibb_id = "+zhibb_id+ ", tiaojb_id = "+tiaoj_id+ ", shangx =
				 * "+shangx+ ", xiax = "+xiax+ ", danwb_id = "+zhibdw_id+ ",
				 * yunja = "+yunjia+ ", yunjdw_id = "+yunjiadw_id+ " where
				 * id="+hetjgb_id; result = con.getUpdate(sql); if(result<0){
				 * con.rollBack(); con.Close();
				 * System.out.println(DateUtil.FormatDateTime(new
				 * Date())+"运输合同价格更新失败！-----------("+((Visit)getPage().getVisit()).getRenymc()+")");
				 * return; } } }
				 */
				// 保存文字
				result = -1;
				sql = "update HETYSWZB\n" + "set wenznr='" + getWenz()
						+ "'where HETYS_id=" + hetys_id;
				result = con.getUpdate(sql);
				if (result < 0) {
					con.rollBack();
					con.Close();
					System.out.println(DateUtil.FormatDateTime(new Date())
							+ "运输合同文字更新失败！-----------("
							+ ((Visit) getPage().getVisit()).getRenymc() + ")");
					return;
				}
				con.commit();
				gethetSelectModels();
				sethetSelectValue(getIDropDownBean(gethetSelectModel(),
						hetys_id));

				getHetysjgExt("ht", hetys_id);
				getYunshtzkkExt("ht", hetys_id);
				getShouhrExt("ht", hetys_id);
			}
		} catch (Exception e) {
			con.rollBack();
			e.printStackTrace();
		} finally {
			con.Close();
		}
		// 合同
		((Visit) getPage().getVisit()).setDropDownBean2(null);
		((Visit) getPage().getVisit()).setProSelectionModel2(null);
	}

	private IDropDownBean getIDropDownBean(IPropertySelectionModel model,
			long id) {
		int OprionCount;
		OprionCount = model.getOptionCount();
		for (int i = 0; i < OprionCount; i++) {
			if (((IDropDownBean) model.getOption(i)).getId() == id) {
				return (IDropDownBean) model.getOption(i);
			}
		}
		return null;
	}

	public String getFahr() {
		if (((Visit) getPage().getVisit()).getString2() == null) {
			((Visit) getPage().getVisit()).setString2("");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setFahr(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}

	private String MultiSel;

	public String getMultiSel() {
		return MultiSel;
	}

	private void gethetysmb(long hetysmb_id) {
		String sql = "";
		JDBCcon con = new JDBCcon();
		try {
			// 合同信息
			sql = "select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,"
					+ "GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,"
					+ "XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,QISRQ,GUOQRQ,"
					+ "MINGC,diancxxb_id,yunsjgfab_id \n "
					+ "from hetys_mb"
					+ " where ID=" + hetysmb_id;
			ResultSet rs = con.getResultSet(sql);

			Hetysxxbean bean = gethetysxxbean();
			bean.setHetmc("");
			setxufValue(getIDropDownBean(getxufModel(), -1));
			if (rs.next()) {
				sethetSelectValue(getIDropDownBean(gethetSelectModel(), -1));
				setxufValue(getIDropDownBean(getxufModel(), -1));
				setgongfValue(getIDropDownBean(getgongfModel(), -1));
				setYunsjgfaValue(getIDropDownBean(getIYunsjgfaModel(), rs
						.getLong("yunsjgfab_id")));
				bean.setHetmc(rs.getString("MINGC"));
				bean.setGONGFDBGH(rs.getString("GONGFDBGH"));
				bean.setGONGFDH(rs.getString("GONGFDH"));
				bean.setGONGFDWDZ(rs.getString("GONGFDWDZ"));
				bean.setGONGFDWMC(rs.getString("GONGFDWMC"));
				bean.setGONGFFDDBR(rs.getString("GONGFFDDBR"));
				bean.setGONGFKHYH(rs.getString("GONGFKHYH"));
				bean.setGongfsh(rs.getString("Gongfsh"));
				bean.setGONGFWTDLR(rs.getString("GONGFWTDLR"));
				bean.setGONGFYZBM(rs.getString("GONGFYZBM"));
				bean.setGONGFZH(rs.getString("GONGFZH"));
				// bean.setGuoqsj(rs.getDate("GUOQRQ"));
				bean.setHetbh(rs.getString("Hetbh"));
				// bean.setHetyj(rs.getString("Hetyj"));
				bean.setQianddd(rs.getString("Qianddd"));
				// bean.setQiandsj(rs.getDate("QIANDRQ"));
				// bean.setShengxsj(rs.getDate("QISRQ"));
				bean.setXUFDBGH(rs.getString("XUFDBGH"));
				bean.setXUFDH(rs.getString("XUFDH"));
				bean.setXUFDWDZ(rs.getString("XUFDWDZ"));
				bean.setXUFDWMC(rs.getString("XUFDWMC"));
				bean.setXUFFDDBR(rs.getString("XUFFDDBR"));
				bean.setXUFKHYH(rs.getString("XUFKHYH"));
				bean.setXufsh(rs.getString("Xufsh"));
				bean.setXUFWTDLR(rs.getString("XUFWTDLR"));
				bean.setXUFYZBM(rs.getString("XUFYZBM"));
				bean.setXUFZH(rs.getString("XUFZH"));

			}

			// 价格信息
			getHetysjgExt("mb", hetysmb_id);
			getYunshtzkkExt("mb", hetysmb_id);
			getShouhrExt("mb", hetysmb_id);
			/*
			 * List list = geteditValuesg(); list.clear(); sql= "select id,
			 * hetys_id, meikxxb_id, zhibb_id, tiaojb_id, shangx, xiax,
			 * danwb_id, yunja, yunjdw_id from hetysjgb where
			 * hetys_id="+hetysmb_id; ResultSet rs3=con.getResultSet(sql); int
			 * i=0; while(rs3.next()){ long id=rs3.getLong("id"); String
			 * meikxxb_id =
			 * getProperValue(getMeikxxModel(),rs3.getLong("meikxxb_id"));
			 * double yunjia = rs3.getDouble("yunja"); String yunjdw_id =
			 * getProperValue(getYunjiadwModel(),rs3.getLong("yunjdw_id"));
			 * 
			 * String zhibb_id =
			 * getProperValue(getZhibSelectModel(),rs3.getLong("zhibb_id"));
			 * String tiaoj_id =
			 * getProperValue(gettiaojjSelectModel(),rs3.getLong("tiaojb_id"));
			 * double shangx = rs3.getDouble("shangx"); double xiax =
			 * rs3.getDouble("xiax"); String zhibdw_id =
			 * getProperValue(getzhibdwSelectModel(),rs3.getLong("danwb_id")); //
			 * double yunju = rs3.getDouble("yunju"); // String yunjudw_id =
			 * getProperValue(getYunjudwModel(),rs3.getLong("yunjudw_id"));
			 * 
			 * list.add(new
			 * Yunsjijbean(++i,id,meikxxb_id,yunjia,yunjdw_id,zhibb_id,zhibdw_id,tiaoj_id,shangx,xiax)); }
			 */

			// 文字信息
			sql = "select id,wenznr\n" + "from hetyswzb\n" + "where hetys_id="
					+ hetysmb_id;
			ResultSet rs4 = con.getResultSet(sql);
			if (rs4.next()) {
				setWenz(rs4.getString("wenznr"));
			} else {
				setWenz("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}

	// 年份
	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean25() == null) {
			((Visit) getPage().getVisit()).setDropDownBean25(getIDropDownBean(
					getNianfModel(), DateUtil.getYear(new Date())));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean25();
	}

	public void setNianfValue(IDropDownBean Value) {
		if (Value != null
				&& ((Visit) getPage().getVisit()).getDropDownBean25() != null) {
			if (Value.getId() != ((Visit) getPage().getVisit())
					.getDropDownBean25().getId()) {
				((Visit) getPage().getVisit()).setboolean4(true);
			} else {
				((Visit) getPage().getVisit()).setboolean4(false);
			}
			((Visit) getPage().getVisit()).setDropDownBean25(Value);
		}
	}

	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel25() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel25();
	}

	public void getNianfModels() {
		List listNianf = new ArrayList();

		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()); i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel25(new IDropDownModel(listNianf));
	}

	public String getChengyfArrayScript() {
		JDBCcon con = new JDBCcon();
		StringBuffer Chengydwarray = new StringBuffer();
		String sql = "";
		String tmp = "";
		int i = 0;
		String mingc = "";
		String quanc = "";
		String danwdz = "";
		String youzbm = "";
		String shuih = "";
		String faddbr = "";
		String weitdlr = "";
		String kaihyh = "";
		String zhangh = "";
		String dianh = "";
		String chuanz = "";

		try {
			sql = " select mingc,quanc,danwdz,youzbm,shuih,faddbr,weitdlr,kaihyh,zhangh,dianh,chuanz from yunsdwb order by mingc";
			// 承运单位
			ResultSetList rs = con.getResultSetList(sql);

			if (rs.getRows() > 0) {

				for (int j = 0; j < rs.getRows(); j++) {

					if (j == 0) {
						tmp = "new Array()";
					} else {
						tmp += ",new Array()";
					}

				}
			}

			Chengydwarray.append("var Chengydw =new Array(" + tmp + ");");

			while (rs.next()) {
				mingc = rs.getString("mingc");
				quanc = rs.getString("quanc");
				danwdz = rs.getString("danwdz");
				youzbm = rs.getString("youzbm");
				shuih = rs.getString("shuih");
				faddbr = rs.getString("faddbr");
				weitdlr = rs.getString("weitdlr");
				kaihyh = rs.getString("kaihyh");
				zhangh = rs.getString("zhangh");
				dianh = rs.getString("dianh");
				chuanz = rs.getString("chuanz");

				Chengydwarray
						.append("Chengydw[" + i + "][0] ='" + quanc + "';");
				Chengydwarray.append("Chengydw[" + i + "][1] ='" + danwdz
						+ "';");
				Chengydwarray.append("Chengydw[" + i + "][2] ='" + youzbm
						+ "';");
				Chengydwarray
						.append("Chengydw[" + i + "][3] ='" + shuih + "';");
				Chengydwarray.append("Chengydw[" + i + "][4] ='" + faddbr
						+ "';");
				Chengydwarray.append("Chengydw[" + i + "][5] ='" + weitdlr
						+ "';");
				Chengydwarray.append("Chengydw[" + i + "][6] ='" + kaihyh
						+ "';");
				Chengydwarray.append("Chengydw[" + i + "][7] ='" + zhangh
						+ "';");
				Chengydwarray
						.append("Chengydw[" + i + "][8] ='" + dianh + "';");
				Chengydwarray.append("Chengydw[" + i + "][9] ='" + chuanz
						+ "';");
				Chengydwarray.append("Chengydw[" + i + "][10] ='" + mingc
						+ "';");

				i++;
			}

			sql = " select mingc,quanc,diz,youzbm,shuih,faddbr,weitdlr,kaihyh,zhangh,dianh,chuanz \n"
					+ "from diancxxb order by mingc";
			//	           
			rs = con.getResultSetList(sql);

			if (rs.getRows() > 0) {

				for (int j = 0; j < rs.getRows(); j++) {

					if (j == 0) {
						tmp = "new Array()";
					} else {
						tmp += ",new Array()";
					}
				}
			}
			i = 0;
			Chengydwarray.append("\n var xuf =new Array(" + tmp + ");");

			while (rs.next()) {
				mingc = rs.getString("mingc");
				quanc = rs.getString("quanc");
				danwdz = rs.getString("diz");
				youzbm = rs.getString("youzbm");
				shuih = rs.getString("shuih");
				faddbr = rs.getString("faddbr");
				weitdlr = rs.getString("weitdlr");
				kaihyh = rs.getString("kaihyh");
				zhangh = rs.getString("zhangh");
				dianh = rs.getString("dianh");
				chuanz = rs.getString("chuanz");

				Chengydwarray.append("xuf[" + i + "][0] ='" + quanc + "';");
				Chengydwarray.append("xuf[" + i + "][1] ='" + danwdz + "';");
				Chengydwarray.append("xuf[" + i + "][2] ='" + youzbm + "';");
				Chengydwarray.append("xuf[" + i + "][3] ='" + shuih + "';");
				Chengydwarray.append("xuf[" + i + "][4] ='" + faddbr + "';");
				Chengydwarray.append("xuf[" + i + "][5] ='" + weitdlr + "';");
				Chengydwarray.append("xuf[" + i + "][6] ='" + kaihyh + "';");
				Chengydwarray.append("xuf[" + i + "][7] ='" + zhangh + "';");
				Chengydwarray.append("xuf[" + i + "][8] ='" + dianh + "';");
				Chengydwarray.append("xuf[" + i + "][9] ='" + chuanz + "';");
				Chengydwarray.append("xuf[" + i + "][10] ='" + mingc + "';");

				i++;
			}

			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}

		return Chengydwarray.toString();
	}

}