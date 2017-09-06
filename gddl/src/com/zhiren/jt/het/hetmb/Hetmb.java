package com.zhiren.jt.het.hetmb;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
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

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Liuc;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/**
 * @author caolin2
 * 
 */
/*
 * 作者：songy
 * 时间：2011-03-23 
 * 描述：修改下拉菜单的排序，要求按照名称进行排序
 */
public class Hetmb extends BasePage implements PageValidateListener {
	// protected void initialize() {
	// danwglStr = "";
	// }
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
	
	public boolean isHetbl() {
		return ((Visit) getPage().getVisit()).getboolean5();
	}

	public void setHetbl(boolean value) {
		((Visit) getPage().getVisit()).setboolean5(value);
	}
	
	private String getDanwglStr() {
		;// 指标单位关联数组
		return ((Visit) getPage().getVisit()).getString3();
	}

	private void setDanwglStr(String value) {
		;// 指标单位关联数组
		((Visit) getPage().getVisit()).setString3(value);
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
				((Visit) getPage().getVisit()).setboolean1(true);
			} else {
				((Visit) getPage().getVisit()).setboolean1(false);
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
		
		String sql="";
		
//		if(this.isTijsh()){
//			 sql = "select id,mingc " + "from hetb_mb "
//			+ "where diancxxb_id="
//			+ ((Visit) getPage().getVisit()).getDiancxxb_id()
//			+" and nvl(liucztb_id,-1)<=0";
//		}else{
			 sql = "select id,mingc " + "from hetb_mb "
			+ "where diancxxb_id="
			+ ((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc";
//		}
		
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql, "新建模板"));
		return;
	}

	// 合同审核流程下拉框
	public IDropDownBean getliucSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getliucSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setliucSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setliucSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getliucSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getliucSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void getliucSelectModels() {
		String sql = "select liucb.id,liucb.mingc\n" + "from liucb,liuclbb\n"
				+ "where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同' order by liucb.mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql, ""));
		return;
	}

	// 新模板名称
	public String getmobmc() {
		return ((Visit) getPage().getVisit()).getString1();
	}

	public void setmobmc(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}

	// 合同信息
	public Hetxxbean gethetxxbean() {
		if (((Visit) getPage().getVisit()).getObject1() == null) {
			((Visit) getPage().getVisit()).setObject1(new Hetxxbean());
		}
		return (Hetxxbean) ((Visit) getPage().getVisit()).getObject1();
	}

	public void sethetxxbean(Hetxxbean value) {
		((Visit) getPage().getVisit()).setObject1(value);
	}

	// 计划口径
	public IDropDownBean getJihxzValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getIJihxzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJihxzValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setIJihxzModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getIJihxzModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
			getIJihxzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void getIJihxzModels() {
		String sql = "select id,mingc\n" + "from jihkjb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(sql, ""));
		return;
	}
	
//	基价类型
	public IDropDownBean getJijlxValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean24() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean24((IDropDownBean) getIJijlxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean24();
	}

	public void setJijlxValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean24(Value);
	}

	public void setIJijlxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel24(value);
	}

	public IPropertySelectionModel getIJijlxModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel24() == null) {
			getIJijlxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel24();
	}

	public void getIJijlxModels() {
		List Jijlx=new ArrayList();
		Jijlx.add(new IDropDownBean(-1,""));
		Jijlx.add(new IDropDownBean(0,"含税单价"));
		Jijlx.add(new IDropDownBean(1,"不含税单价"));
		((Visit) getPage().getVisit())
				.setProSelectionModel24(new IDropDownModel(Jijlx));
	}
//	下发模板
	public IDropDownBean getxiafValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean26() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean26((IDropDownBean) getxiafModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean26();
	}

	public void setxiafValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean26(Value);
	}

	public void setxiafModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel26(value);
	}

	public IPropertySelectionModel getxiafModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel26() == null) {
			getxiafModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel26();
	}

	public void getxiafModels() {
		List Jijlx=new ArrayList();
		Jijlx.add(new IDropDownBean(0,"请选择是否为下发模板"));
		Jijlx.add(new IDropDownBean(1,"是"));
		((Visit) getPage().getVisit())
				.setProSelectionModel26(new IDropDownModel(Jijlx));
	}
//	基价类型_end

	// 合同数量信息
	private int _editTableRows = -1;

	public int getEditTableRows() {
		return _editTableRows;
	}

	public void setEditTableRows(int _value) {
		_editTableRows = _value;
	}

	public List geteditValuess() {
		if (((Visit) getPage().getVisit()).getList1() == null) {
			((Visit) getPage().getVisit()).setList1(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList1();
	}

	private Fahxxbean editValues1;

	public Fahxxbean geteditValues1() {
		return editValues1;
	}

	public void seteditValues1(Fahxxbean value) {
		this.editValues1 = value;
	}

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
		String sql = "select id,mingc\n" + "from yunsfsb order by mingc";
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
		String sql = "select id,mingc\n"
				+ " from pinzb where instr(mingc,'油') = 0  order by mingc ";// where
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
		String sql = "select id,mingc\n" + "from chezxxb\n" + "order by mingc ";
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
				+ " )\n" + " order by jib ,mingc ";
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
	private int _editTableRowz = -1;

	public int getEditTableRowz() {
		return _editTableRowz;
	}

	public void setEditTableRowz(int _value) {
		_editTableRowz = _value;
	}

	public List geteditValuesz() {
		if (((Visit) getPage().getVisit()).getList2() == null) {
			((Visit) getPage().getVisit()).setList2(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList2();
	}

	public Zhilyqbean editValuez;

	public Zhilyqbean geteditValuez() {
		return this.editValuez;
	}

	public void seteditValuez(Zhilyqbean value) {
		this.editValuez = value;
	}

	// 指标
	public IDropDownBean getZHIBValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getZHIBModel().getOption(
							0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setZHIBValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setZHIBModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getZHIBModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getZHIBModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getZHIBModels() {
		String sql = "select id,mingc\n" + "from zhibb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel8(new IDropDownModel(sql, ""));
		return;
	}

	// 条件
	public IDropDownBean getTIAOJValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean9() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean9((IDropDownBean) getTIAOJModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean9();
	}

	public void setTIAOJValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean9(Value);
	}

	public void setTIAOJModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel9(value);
	}

	public IPropertySelectionModel getTIAOJModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel9() == null) {
			getTIAOJModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel9();
	}

	public void getTIAOJModels() {
		String sql = "select id,mingc\n" + "from tiaojb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel9(new IDropDownModel(sql, ""));
		return;
	}

	// 单位
	public IDropDownBean getDANWValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean10() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean10((IDropDownBean) getDANWModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean10();
	}

	public void setDANWValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean10(Value);
	}

	public void setDANWModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel10(value);
	}

	public IPropertySelectionModel getDANWModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel10() == null) {
			getDANWModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel10();
	}

	public void getDANWModels() {
		String sql = "select id,mingc\n" + "from danwb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel10(new IDropDownModel(sql, ""));
		return;
	}

	// 增扣价格
	private int _editTableRowj = -1;

	public int getEditTableRowj() {
		return _editTableRowj;
	}

	public void setEditTableRowj(int _value) {
		_editTableRowj = _value;
	}

	public List geteditValuesj() {
		if (((Visit) getPage().getVisit()).getList3() == null) {
			((Visit) getPage().getVisit()).setList3(new ArrayList());
		}
		return ((Visit) getPage().getVisit()).getList3();
	}

	public Zengkkbean editValuej;

	public Zengkkbean geteditValuej() {
		return this.editValuej;
	}

	public void seteditValuej(Zengkkbean value) {
		this.editValuej = value;
	}

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

	public jijbean editValueg;

	public jijbean geteditValueg() {
		return this.editValueg;
	}

	public void seteditValueg(jijbean value) {
		this.editValueg = value;
	}

	// 指标
	public IDropDownBean getzhilxmjSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean11() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean11((IDropDownBean) getzhilxmjSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean11();
	}

	public void setzhilxmjSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean11(Value);
	}

	public void setzhilxmjSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel11(value);
	}

	public IPropertySelectionModel getzhilxmjSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel11() == null) {
			getzhilxmjSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel11();
	}

	public void getzhilxmjSelectModels() {
		String sql = "select id,mingc\n" + "from zhibb\n"
				+ "where zhibb.leib=1 order by mingc";
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
				+ "where tiaojb.leib=1 order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel12(new IDropDownModel(sql, ""));
		return;
	}

	// 价格单位
	public IDropDownBean getjiagValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean13() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean13((IDropDownBean) getjiagModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean13();
	}

	public void setjiagValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean13(Value);
	}

	public void setjiagModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel13(value);
	}

	public IPropertySelectionModel getjiagModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel13() == null) {
			getjiagModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel13();
	}

	public void getjiagModels() {
		String sql = "select id,mingc\n" + "from danwb\n"
				+ "where danwb.zhibb_id=0 order by mingc";// zhibb_id=0为价格单位
		((Visit) getPage().getVisit())
				.setProSelectionModel13(new IDropDownModel(sql, ""));
		return;
	}

	// 结算用指标单位
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
		String sql = "select id,mingc " + "from danwb order by mingc \n";
		((Visit) getPage().getVisit())
				.setProSelectionModel14(new IDropDownModel(sql, ""));
		return;
	}

	// 合同结算方式表
	public IDropDownBean getjiesfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean15() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean15((IDropDownBean) getjiesfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean15();
	}

	public void setjiesfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean15(Value);
	}

	public void setjiesfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel15(value);
	}

	public IPropertySelectionModel getjiesfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel15() == null) {
			getjiesfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel15();
	}

	public void getjiesfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjsfsb  order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel15(new IDropDownModel(sql, ""));
		return;
	}

	// 合同计价方式
	public IDropDownBean getjijfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean16() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean16((IDropDownBean) getjijfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean16();
	}

	public void setjijfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean16(Value);
	}

	public void setjijfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel16(value);
	}

	public IPropertySelectionModel getjijfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel16() == null) {
			getjijfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel16();
	}

	public void getjijfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjjfsb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel16(new IDropDownModel(sql, ""));
		return;
	}

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
		list.add(new IDropDownBean(6, "四舍五入(0.001)"));
		list.add(new IDropDownBean(7, "四舍五入(0.0001)"));

		((Visit) getPage().getVisit())
				.setProSelectionModel17(new IDropDownModel(list));
		return;
	}

	// 文字
	String buffer;

	public String getWenz() {
		return buffer;
	}

	public void setWenz(String value) {
		this.buffer = value;
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
		String sql = "select id,mingc\n" + "from gongysb where leix=1 order by mingc";
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
		String sql = "select id,mingc " + "from diancxxb\n" + "where  id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id()+" order by mingc ";
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
		String sql = "select id,mingc\n" + "from gongysb order by mingc";
		((Visit) getPage().getVisit())
				.setProSelectionModel20(new IDropDownModel(sql, ""));
		return;
	}

	// 加权方式
	public IDropDownBean getjiaqfsgSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean21() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean21((IDropDownBean) getjiaqfsgSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean21();
	}

	public void setjiaqfsgSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean21(Value);
	}

	public void setjiaqfsgSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel21(value);
	}

	public IPropertySelectionModel getjiaqfsgSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel21() == null) {
			getjiaqfsgSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel21();
	}

	public void getjiaqfsgSelectModels() {
		String sql = "select id,mingc\n" + "from hetjsxsb order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel21(new IDropDownModel(sql, ""));
		return;
	}

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
	public IDropDownBean getMeiksselectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean23() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean23((IDropDownBean) getMeiksselectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean23();
	}

	public void setMeiksselectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean23(Value);
	}

	public void setMeiksselectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel23(value);
	}

	public IPropertySelectionModel getMeiksselectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel23() == null) {
			getMeiksselectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel23();
	}

	public void getMeiksselectModels() {
		String sql = "select id,mingc\n" + "from meikxxb  order by mingc ";
		((Visit) getPage().getVisit())
				.setProSelectionModel23(new IDropDownModel(sql, ""));
		return;
	}
	
	private boolean tijsh;//是否添加  提交进入流程审核功能
	
	public boolean isTijsh(){
		
		return tijsh;
	}
	
	public void setTijsh(){
		
		tijsh=false;
		
		String sql=" select * from xitxxb  where mingc='合同模板提交审核' and leib='合同模板' and zhi='是' and zhuangt=1 ";
	
		JDBCcon con=new JDBCcon();
		ResultSetList rsl=con.getResultSetList(sql);
		
		if(rsl.next()){
			tijsh=true;
		}
		
	}

	private boolean btDisabled=false;
	
	public boolean isBtDisabled(){
		return btDisabled;
	}
	
	public void setBtDisabled(){
		
		try {
			btDisabled=false;
			
			
			 String s=MainGlobal.getTableCol("hetb_mb", "liucztb_id", "id", this.getmobmcSelectValue().getStrId());
			 
			 if(s!=null && !s.equals("") && Integer.parseInt(s)>0){//模板已经进入流程 或者  已经被审核完成，禁用按钮
				 btDisabled=true;
			 }
			 
			 
		} catch (Exception e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1("");
			visit.setString2("");
			visit.setString3("");
			((Visit) getPage().getVisit()).setDropDownBean1(null);
			((Visit) getPage().getVisit()).setDropDownBean2(null);
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
			((Visit) getPage().getVisit()).setDropDownBean23(null);		//煤矿
			((Visit) getPage().getVisit()).setDropDownBean25(null);		
			((Visit) getPage().getVisit()).setDropDownBean26(null);		
			((Visit) getPage().getVisit()).setDropDownBean27(null);		//适用范围
			((Visit) getPage().getVisit()).setProSelectionModel1(null);
			((Visit) getPage().getVisit()).setProSelectionModel2(null);
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
			((Visit) getPage().getVisit()).setProSelectionModel23(null);	//煤矿
			((Visit) getPage().getVisit()).setProSelectionModel25(null);	
			((Visit) getPage().getVisit()).setProSelectionModel26(null);	
			((Visit) getPage().getVisit()).setProSelectionModel27(null);	//适用范围
			((Visit) getPage().getVisit()).setObject1(null);
			((Visit) getPage().getVisit()).setList1(null);
			((Visit) getPage().getVisit()).setList2(null);
			((Visit) getPage().getVisit()).setList3(null);
			((Visit) getPage().getVisit()).setList4(null);
			setTabbarSelect(0);
			((Visit) getPage().getVisit()).setExtGrid1(null);	//合同变量Grid定义
			setJijlxValue(null);	//24
			setIJijlxModel(null);	//24
			
			this.setHetxxbjFlag(false);
			setHetbl(false);	//是否显示合同变量配置的按钮
			// 更新多选框
			// String sql=
			// "select mingc\n" +
			// "from meikxxb";
			// String[] columns=new String[]{"名称"};
			// setMultiSel(sql,columns);
			getDanwGL();
			if(MainGlobal.getXitxx_item("合同", "是否定义合同变量", 
					String.valueOf(visit.getDiancxxb_id()), "否").equals("是")){
				
				setHetbl(true);	//显示合同变量配置按钮
			}
			
			this.setTijsh();
		}
		if (((Visit) getPage().getVisit()).getboolean1()) {
			getXuanzmb();
			// ((Visit) getPage().getVisit()).setBooleanData1(false);
		}
		if (((Visit) getPage().getVisit()).getboolean2()) {// 合同供方
			getGongf();
			// ((Visit) getPage().getVisit()).setBooleanData1(false);
		}
		if (((Visit) getPage().getVisit()).getboolean3()) {// 需方
			getXuf();
			// ((Visit) getPage().getVisit()).setBooleanData1(false);
		}
		if(this.isTijsh()){
			this.setBtDisabled();//被选择的模板是否禁用
		}
//		Dak(getmobmcSelectValue().getId());
	}
	
//	合同变量设置_Grid_begin
	
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}
	
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		
		if(getExtGrid()!=null){
			
			StringBuffer sb = new StringBuffer();
			
			sb.append("if(cbo_mobmcSelect.getValue()>-1){ \n")
				.append("if(!win_bl){	\n").append(getExtGrid().getGridScript()).append("\n")
				.append(
					"win_bl = new Ext.Window({\n" +
					"\n" + 
					"   el:'hetbl-win',\n" + 
					"   layout:'fit',\n" + 
					"   width:600,\n" + 
					"   height:400,\n" + 
					"   closeAction:'hide',\n" + 
					"   plain: true,\n" + 
					"   title:'变量设置',\n" + 
					"          modal:true,\n" + 
					"   items: [gridDiv_grid]\n" + 
					" });\n" + 
					"}\n" + 
					"win_bl.show();}else{\n " +
					"	Ext.MessageBox.alert('提示信息','请先选择或保存合同模板!');return;	\n" +
					"}");
			return sb.toString();
			
		}else{
			
			return "Ext.MessageBox.alert('提示信息','请先选择或保存合同模板!');	\n";
		}
	}

	public String getGridHtml() {
		
		if(getExtGrid()!=null){
			
			return getExtGrid().getHtml();
		}else{
			
			return "";
		}
	}
//	合同变量设置_Grid_end
	
	private boolean HetxxbjFlag=false;
	
	public boolean isHetxxbjFlag() {
		return HetxxbjFlag;
	}

	public void setHetxxbjFlag(boolean hetxxbjFlag) {
		JDBCcon con=new JDBCcon();
		HetxxbjFlag=hetxxbjFlag;//初始化
		String sql=" select * from xitxxb where mingc='合同模板合同信息可录入' and leib='合同'  " +
				" and zhi='是' and zhuangt=1 and diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		
		if(con.getResultSetList(sql).next()){
			HetxxbjFlag = true;
		}else{
			HetxxbjFlag = false;
		}
		
	}

	//	流程名称 DropDownBean25
//  流程名称 ProSelectionModel25
	public IDropDownBean getLiucmcValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean25() == null) {
			((Visit) getPage().getVisit()).setDropDownBean25((IDropDownBean) getILiucmcModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean25();
	}

	public void setLiucmcValue(IDropDownBean value) {
		
		if(((Visit) getPage().getVisit()).getDropDownBean25()!=value){
			
			((Visit) getPage().getVisit()).setDropDownBean25(value);
		}
	}

	public void setILiucmcModel(IPropertySelectionModel value) {
		
		((Visit) getPage().getVisit()).setProSelectionModel25(value);
	}

	public IPropertySelectionModel getILiucmcModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel25() == null) {
			
			getILiucmcModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel25();
	}

	public IPropertySelectionModel getILiucmcModels() {
		
		String sql="";
		sql="select liucb.id,liucb.mingc from liucb,liuclbb where liucb.liuclbb_id=liuclbb.id and liuclbb.mingc='合同模板' order by mingc";

		((Visit) getPage().getVisit()).setProSelectionModel25(new IDropDownModel(sql,"请选择"));
		return ((Visit) getPage().getVisit()).getProSelectionModel25();
	}
//   流程名称 end
	
//***************************************************************************//
	
	public String _liucmc;
	
	public void setLiucmc(String _value) {
		_liucmc = _value;
	}
	
	public String getLiucmc() {
		if (_liucmc == null) {
			_liucmc = "";
		}
		return _liucmc;
	}
	
	private void getDanwGL() {
		String sql = "";
		List list = new ArrayList();
		StringBuffer Tem = new StringBuffer();
		JDBCcon con = new JDBCcon();
		sql = "select zhibb_id zhibid,danwb.id danwid,danwb.mingc,zhibb.mingc\n"
				+ "from danwb,zhibb\n"
				+ "where danwb.zhibb_id=zhibb.id and zhibb_id<>0\n"
				+ "order by zhibb_id ,zhibb.mingc ";
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
			rs.close();
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

	// 数量
	private boolean _InsertButtons = false;

	public void InsertButtons(IRequestCycle cycle) {
		_InsertButtons = true;
	}

	private boolean _DeleteButtons = false;

	public void DeleteButtons(IRequestCycle cycle) {
		_DeleteButtons = true;
	}

	// 质量
	private boolean _InsertButtonz = false;

	public void InsertButtonz(IRequestCycle cycle) {
		_InsertButtonz = true;
	}

	private boolean _DeleteButtonz = false;

	public void DeleteButtonz(IRequestCycle cycle) {
		_DeleteButtonz = true;
	}

	// 增扣价格
	private boolean _InsertButtonj = false;

	public void InsertButtonj(IRequestCycle cycle) {
		_InsertButtonj = true;
	}

	private boolean _DeleteButtonj = false;

	public void DeleteButtonj(IRequestCycle cycle) {
		_DeleteButtonj = true;
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

	// 数据保存成功，页面提示信息
	private String baoccgMsg;

	public String getBaoccgMsg() {
		return baoccgMsg;
	}

	public void setBaoccgMsg(String baoccgMsg) {
		this.baoccgMsg = baoccgMsg;
	}
	
	protected void initialize() {
		super.initialize();
		baoccgMsg = "";
		_liucmc="";
		msg = "";
	}
	
	private boolean fuzButton = false;

	public void fuzButton(IRequestCycle cycle) {
		fuzButton = true;
	}
	
	
	private boolean tijshButton=false;
	
	public void TijshButton(IRequestCycle cycle){
		tijshButton=true;
	}
	
	private boolean _SaveButton=false;
	
	public void SaveButton(IRequestCycle cycle){
		_SaveButton=true;
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
		if (fuzButton) {
			fuzButton = false;
			if(getmobmcSelectValue().getId()==-1){
				return;
			}
			fuz(String.valueOf(getmobmcSelectValue().getId()));
		}
		if (_ShancButton) {
			_ShancButton = false;
			
			Shanc();
		}
		if (_BaocButton) {
			_BaocButton = false;
			
			baoccgMsg = "保存成功!";
			Baoc();
		}
		// 数量
		if (_InsertButtons) {
			_InsertButtons = false;
			
			Inserts();
		}
		if (_DeleteButtons) {
			_DeleteButtons = false;
			
			Deletes();
		}
		// 质量
		if (_InsertButtonz) {
			_InsertButtonz = false;
			
			Insertz();
		}
		if (_DeleteButtonz) {
			_DeleteButtonz = false;
			
			Deletez();
		}
		// 增扣价格
		if (_InsertButtonj) {
			_InsertButtonj = false;
			
			Insertj();
		}
		if (_DeleteButtonj) {
			_DeleteButtonj = false;
			
			Deletej();
		}
		// 价格
		if (_InsertButtong) {
			_InsertButtong = false;
			
			Insertg();
		}
		if (_DeleteButtong) {
			_DeleteButtong = false;
				
			Deleteg();
		}
		
		if(tijshButton){
			tijshButton=false;
			
			this.tijshlc();
		}
		
		if(_SaveButton){
			_SaveButton = false;
			
			Save((Visit) this.getPage().getVisit());//保存合同变量Grid
			Dak_Hetbl(getmobmcSelectValue().getId());
		}
	}
	
	private void Save(Visit visit) {
//		保存合同变量方法
		
		long hetb_mb_id = getmobmcSelectValue().getId();
		
		if(hetb_mb_id>-1){
//			已保存了合同模板
			JDBCcon con = new JDBCcon();
			StringBuffer sbSql = new StringBuffer();
			sbSql.append("begin \n");
			
			ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet((getChange()));
			while (delrsl.next()) {
				// 删除操作
				
				sbSql.append("	delete from hetblb where id=").append(delrsl.getString("ID")).append(";	\n");
			}
			delrsl.close();
			
			ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
			while (mdrsl.next()) {
				
				if ("0".equals(mdrsl.getString("ID"))) {
					
					sbSql.append("	insert into hetblb(id,hetb_id,bianlmc,value)")
						.append(" values(getnewid("+visit.getDiancxxb_id()+"), ")
						.append(hetb_mb_id).append(", '")
						.append(mdrsl.getString("BIANLMC").trim()).append("', '")
						.append(mdrsl.getString("VALUE").trim()).append("');	\n");
				}else{
					
					sbSql.append(" update hetblb set bianlmc='")
						.append(mdrsl.getString("BIANLMC").trim()).append("',")
						.append(" value='").append(mdrsl.getString("VALUE").trim())
						.append("' where id=").append(mdrsl.getString("ID"))
						.append(";	\n");
				}
			}
			sbSql.append("end;");
//			System.out.println("Licext save() sbSql: \n" + sbSql);
			mdrsl.close();
			if(sbSql.length()>13){
				
				con.getUpdate(sbSql.toString());
			}
			con.Close();
			
		}else{
			
			this.setMsg("请先保存合同模板!");
		}
	}
	
	private void tijshlc(){
		
		if(this.isTijsh()){
			
			
			Visit visit=(Visit)this.getPage().getVisit(); 
			long hetb_mb_id = getmobmcSelectValue().getId();
			
			if(hetb_mb_id==-1){
				baoccgMsg="请选择一个已建模板!";
				return;
			}
			
			long Liuc_id=MainGlobal.getProperId(getILiucmcModel(), getLiucmc()); 
			Liuc.tij("hetb_mb", hetb_mb_id, visit.getRenyID(), "", Liuc_id);
			
			baoccgMsg="数据操作成功!";
			return;
		}
		baoccgMsg="请在系统中预先设置!";
	}
	private void fuz(String hetxxb_id) {
		JDBCcon con=new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		String newid=MainGlobal.getNewID(visit.getDiancxxb_id());
		String sql="begin\n" +
		"  insert into hetb_mb( ID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH"+
		",GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID"+
		",GONGYSB_ID,QISRQ,GUOQRQ,JIHKJB_ID,LIUCB_ID,MINGC,MEIKMCS)\n" + 
		"  (select "+newid+",DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,GONGFDBGH,GONGFKHYH" + 
		"  ,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID\n" + 
		"  ,GONGYSB_ID,QISRQ,GUOQRQ,JIHKJB_ID,LIUCB_ID,MINGC||'副本',MEIKMCS\n" + 
		"  from hetb_mb where hetb_mb.id="+hetxxb_id+");\n" + 
		"  insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID,ZHUANGT)\n" + 
		"  (select ID+10000000000,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,"+newid+",ZHUANGT\n" + 
		"  from hetslb where hetb_id="+hetxxb_id+");\n" + 
		"  insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)\n" + 
		"  (select getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,"+newid+"\n" + 
		"  from hetzlb where hetb_id="+hetxxb_id+");\n" + 
		"  insert into hetzkkb(JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID\n" + 
		"  ,YUNSFSB_ID,BEIZ,HETB_ID,ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,PINZB_ID,SHIYFW,JIJLX)\n" + 
		"  (select JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID\n" + 
		"  ,YUNSFSB_ID,BEIZ,"+newid+",getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,PINZB_ID,SHIYFW,JIJLX\n" + 
		"  from hetzkkb where hetb_id="+hetxxb_id+");\n" + 
		"  insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID\n" + 
		"  ,YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,HETB_ID,HETJJFSB_ID)\n" + 
		"  (select getnewid("+visit.getDiancxxb_id()+"),ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID\n" + 
		"  ,YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,"+newid+",HETJJFSB_ID\n" + 
		"  from hetjgb where hetb_id="+hetxxb_id+");\n" + 
		"  insert into hetwzb(ID,WENZNR,HETB_ID)\n" + 
		"  (select getnewid("+visit.getDiancxxb_id()+"),WENZNR,"+newid+"\n" + 
		"  from hetwzb\n" + 
		"  where hetb_id="+hetxxb_id+");\n" +
		"  insert into hetblb(ID,HETB_ID,BIANLMC,VALUE) \n" +
		"	(select getnewid("+visit.getDiancxxb_id()+"),"+newid+",BIANLMC,VALUE \n" +
		"		from hetblb where hetb_id="+hetxxb_id+"); \n" +
		"end;";
		con.getInsert(sql);
		Dak(Long.parseLong(newid));
		getmobmcSelectModels();
		setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),
				Long.parseLong(newid)));
//		getmobmcSelectModels();
	}
	private void Xinj() {
		// 合同信息
		Hetxxbean bean = gethetxxbean();
		setliucSelectValue(getIDropDownBean(getliucSelectModel(), -1));
		setJihxzValue(getIDropDownBean(getIJihxzModel(), -1));
		setxiafValue(getIDropDownBean(getxiafModel(), 0));
		
		setmobmc("新建模板");
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(), -1));
		setxufValue(getIDropDownBean(getshijgfSelectModel(), -1));
		setgongfValue(getIDropDownBean(getshijgfSelectModel(), -1));
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
		// 数量信息
		geteditValuess().clear();
		// 质量信息
		geteditValuesz().clear();
		// 增扣款
		geteditValuesj().clear();
		// 价格信息
		geteditValuesg().clear();
		// 文字信息
		setWenz("");
	}

	private void Dak(long hetmb_id) {
		String sql = "";
		JDBCcon con = new JDBCcon();
		try {
			// 合同信息
			sql = "select ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,"
					+ "GONGFWTDLR,GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,"
					+ "XUFDH,XUFDBGH,XUFKHYH,XUFZH,XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,"
					+ "JIHKJB_ID,LIUCB_ID,MINGC,diancxxb_id,meikmcs,xiafmb "
					+ "from hetb_mb" + " where ID=" + hetmb_id;
			ResultSet rs = con.getResultSet(sql);
			if (rs.next()) {
				Hetxxbean bean = gethetxxbean();
				setliucSelectValue(getIDropDownBean(getliucSelectModel(), rs
						.getLong("LIUCB_ID")));
				setJihxzValue(getIDropDownBean(getIJihxzModel(), rs
						.getLong("JIHKJB_ID")));
				setmobmc(rs.getString("MINGC"));
				setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),
						rs.getLong("GONGYSB_ID")));
				setxufValue(getIDropDownBean(getxufModel(), rs
						.getLong("diancxxb_id")));
				setgongfValue(getIDropDownBean(getgongfModel(), rs
						.getLong("HETGYSBID")));
				setxiafValue(getIDropDownBean(getxiafModel(), rs
						.getLong("xiafmb")));
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
				// bean.setHetyj(rs.getString("Hetyj"));
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
			// 数量信息
			List list = geteditValuess();
			list.clear();
			sql = "select aa.hetb_id,aa.id,y1, y2,y3,y4, y5, y6, y7, y8,\n"
					+ " y9, y10,y11,y12,pinzb.mingc pinz,yunsfsb.mingc yunsfs,faz.mingc faz,daoz.mingc daoz\n"
					+ ",diancxxb.mingc shouhr\n"
					+ "from(\n"
					+ "\n"
					+ "select a.hetb_id,a.id,a.pinzb_id,a.yunsfsb_id,a.faz_id,a.daoz_id,a.diancxxb_id,y1.hetl as y1,y2.hetl as y2,y3.hetl as y3,y4.hetl as y4,y5.hetl as y5,y6.hetl as y6,y7.hetl as y7,y8.hetl as y8,\n"
					+ "y9.hetl as y9,y10.hetl as y10,y11.hetl as y11,y12.hetl as y12\n"
					+ "from\n"
					+ "    (select hetb_id,id,to_char(max(hetslb.riq),'MM')Y,max(hetl)hetl,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    from hetslb\n"
					+ "    where hetb_id="
					+ hetmb_id
					+ "\n"
					+ "    group by hetb_id,id,hetslb.pinzb_id,hetslb.yunsfsb_id,hetslb.faz_id,hetslb.daoz_id,hetslb.diancxxb_id\n"
					+ "    )a,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,1 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='01'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y1,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,2 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='02'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y2,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,3 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='03'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y3,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,4 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='04'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y4,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,5 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='05'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y5,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,6as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='06'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y6,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,7 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='07'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y7,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,8 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='08'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y8,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,9 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='09'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y9,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,10 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='10'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y10,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,11 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='11'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y11,\n"
					+ "    ----------\n"
					+ "    (select hetb_id,id,12 as y,hetl\n"
					+ "    from hetslb\n"
					+ "    where to_char(riq,'MM')='12'and hetslb.hetb_id="
					+ hetmb_id
					+ ")y12\n"
					+ "\n"
					+ "------------------------------------\n"
					+ "where a.hetb_id=y1.hetb_id(+) and a.id=y1.id(+)\n"
					+ "and a.hetb_id=y2.hetb_id(+) and a.id=y2.id(+)\n"
					+ "and a.hetb_id=y3.hetb_id(+) and a.id=y3.id(+)\n"
					+ "and a.hetb_id=y4.hetb_id(+) and a.id=y4.id(+)\n"
					+ "and a.hetb_id=y5.hetb_id(+) and a.id=y5.id(+)\n"
					+ "and a.hetb_id=y6.hetb_id(+) and a.id=y6.id(+)\n"
					+ "and a.hetb_id=y7.hetb_id(+) and a.id=y7.id(+)\n"
					+ "and a.hetb_id=y8.hetb_id(+) and a.id=y8.id(+)\n"
					+ "and a.hetb_id=y9.hetb_id(+) and a.id=y9.id(+)\n"
					+ "and a.hetb_id=y10.hetb_id(+) and a.id=y10.id(+)\n"
					+ "and a.hetb_id=y11.hetb_id(+) and a.id=y11.id(+)\n"
					+ "and a.hetb_id=y12.hetb_id(+) and a.id=y12.id(+)\n"
					+ ")aa,pinzb,yunsfsb,chezxxb faz,chezxxb daoz,diancxxb\n"
					+ "where  aa.pinzb_id=pinzb.id and aa.yunsfsb_id=yunsfsb.id\n"
					+ "and faz.id=aa.faz_id and aa.daoz_id=daoz.id and diancxxb.id=aa.diancxxb_id";
			rs = con.getResultSet(sql);
			while (rs.next()) {
				String pinz = rs.getString("pinz");
				String yunsfs = rs.getString("yunsfs");
				String faz = rs.getString("faz");
				String daoz = rs.getString("daoz");
				String shouhr = rs.getString("shouhr");
				long id = rs.getLong("id");
				long Y1 = rs.getLong("Y1");
				long Y2 = rs.getLong("Y2");
				long Y3 = rs.getLong("Y3");
				long Y4 = rs.getLong("Y4");
				long Y5 = rs.getLong("Y5");
				long Y6 = rs.getLong("Y6");
				long Y7 = rs.getLong("Y7");
				long Y8 = rs.getLong("Y8");
				long Y9 = rs.getLong("Y9");
				long Y10 = rs.getLong("Y10");
				long Y11 = rs.getLong("Y11");
				long Y12 = rs.getLong("Y12");
				long hej = Y1 + Y2 + Y3 + Y4 + Y5 + Y6 + Y7 + Y8 + Y9 + Y10
						+ Y11 + Y12;
				list
						.add(new Fahxxbean(id, pinz, yunsfs, faz, daoz, shouhr,
								hej, Y1, Y2, Y3, Y4, Y5, Y6, Y7, Y8, Y9, Y10,
								Y11, Y12));
			}
			// 质量信息
			list = geteditValuesz();
			list.clear();
			sql = "select hetzlb.id,zhibb.mingc zhib,tiaojb.mingc tiaoj,shangx,xiax,danwb.mingc danw\n"
					+ "from hetzlb,zhibb,tiaojb,danwb\n"
					+ "where hetzlb.zhibb_id=zhibb.id and hetzlb.tiaojb_id=tiaojb.id and hetzlb.danwb_id=danwb.id and hetzlb.hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			int i = 0;
			while (rs.next()) {
				String zhib = rs.getString("zhib");
				String tiaoj = rs.getString("tiaoj");
				double shangx = rs.getDouble("shangx");
				double xiax = rs.getDouble("xiax");
				String danw = rs.getString("danw");
				long id = rs.getLong("id");
				list.add(new Zhilyqbean(++i, id, zhib, tiaoj, shangx, xiax,
						danw));
			}
			// 价格信息
			list = geteditValuesg();
			list.clear();
			sql = "select hetjgb.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,SHANGX,XIAX,zhibdw.mingc zhibdwmc,JIJ,jijdw.mingc jijdwmc,jijgs,\n"
					+ "hetjjfsb.mingc hetjjfsmc ,hetjsfsb.mingc hetjsfsmc,hetjsxsb.mingc hetjsxsmc,YUNJ,yunjdw.mingc yunjdwmc,decode(YINGDKF,1,'是',2,'否','')YINGDKF,\n"
					+ "yunsfsb.mingc yunsfsmc,ZUIGMJ,zuigmjdw.mingc zuigmjdw,fengsjj,fengsjjdw.mingc fengsjjdw,decode(jijlx,0,'含税单价',1,'不含税单价') as jijlx  ,pinzb.mingc pinz\n"
					+ "from hetjgb,zhibb,tiaojb,danwb zhibdw,danwb jijdw,hetjsfsb,hetjsxsb,hetjjfsb,danwb yunjdw,danwb zuigmjdw,danwb fengsjjdw,yunsfsb,pinzb\n"
					+ "where hetjgb.pinzb_id=pinzb.id(+) and hetjgb.zhibb_id=zhibb.id and hetjgb.tiaojb_id=tiaojb.id and hetjgb.danwb_id=zhibdw.id\n"
					+ "and hetjgb.jijdwid=jijdw.id and hetjgb.hetjsfsb_id=hetjsfsb.id(+) and hetjgb.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and hetjgb.yunjdw_id=yunjdw.id(+) and hetjgb.yunsfsb_id=yunsfsb.id(+) and hetjgb.hetjjfsb_id=hetjjfsb.id(+) and hetjgb.zuigmjdw = zuigmjdw.id(+) and hetjgb.fengsjjdw = fengsjjdw.id(+) and hetjgb.hetb_id="
					+ hetmb_id +" order by zhibb.mingc,xiax";
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIJ = rs.getDouble("JIJ");
				String jijdwmc = rs.getString("jijdwmc");
				String jijgs = rs.getString("jijgs");
				String hetjjfsmc = rs.getString("hetjjfsmc");
				String hetjsfsmc = rs.getString("hetjsfsmc");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				double YUNJ = rs.getDouble("YUNJ");
				String yunjdwmc = rs.getString("yunjdwmc");
				String YINGDKF = rs.getString("YINGDKF");
				String yunsfsmc = rs.getString("yunsfsmc");
				double ZUIGMJ = rs.getDouble("ZUIGMJ");
				String zuigmjdw = rs.getString("zuigmjdw");
				double fengsjj = rs.getDouble("fengsjj");
				String fengsjjdw = rs.getString("fengsjjdw");
				String jijlx = rs.getString("jijlx");
				String pinz=rs.getString("pinz");
				list.add(new jijbean(++i, id, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIJ, jijdwmc, jijgs, hetjsfsmc, hetjjfsmc,
						hetjsxsmc, YUNJ, yunjdwmc, YINGDKF, yunsfsmc, 
						ZUIGMJ, zuigmjdw, fengsjj, fengsjjdw, jijlx,pinz));
			}
			// 增扣款信息
			list = geteditValuesj();
			list.clear();
			sql = "select z.ID,zhibb.mingc zhibmc,tiaojb.mingc tiaojmc,z.SHANGX,z.XIAX,zhibdw.mingc zhibdwmc,JIS,jisdw.mingc jisdwmc,\n"
					+ "KOUJ,koujgs,koujdw.mingc koujdwmc,ZENGFJ,zengfjgs,zengfjdw.mingc zengfjdwmc,\n"
					+ "decode(XIAOSCL,1,'进位',2,'舍去',3,'四舍五入',4,'四舍五入(0.1)',5,'四舍五入(0.01)',6,'四舍五入(0.001)',7,'四舍五入(0.0001)''')XIAOSCL,JIZZKJ,JIZZB\n"
					+ ",CANZXM.Mingc canzxmmc,CANZXMDW.Mingc canzxmdwmc,CANZSX,CANZXX,hetjsxsb.mingc hetjsxsmc,yunsfsb.mingc yunsfsmc,\n"
					+ "pz.mingc as pinz,decode(z.SHIYFW,0,'全部','超出部分') as SHIYFW,z.BEIZ,decode(z.jijlx, 0, '含税单价', '不含税单价') jijlx\n"
					+ "from hetzkkb z,zhibb,tiaojb,danwb zhibdw,danwb jisdw,danwb koujdw,danwb zengfjdw,zhibb canzxm,danwb canzxmdw,hetjsxsb,yunsfsb,pinzb pz\n"
					+ "where z.zhibb_id=zhibb.id  and z.tiaojb_id=tiaojb.id and z.danwb_id=zhibdw.id and z.jisdwid=jisdw.id(+) and z.koujdw=koujdw.id(+)\n"
					+ "and z.zengfjdw=zengfjdw.id(+) and z.canzxm=canzxm.id(+) and z.canzxmdw=canzxmdw.id(+) and z.hetjsxsb_id=hetjsxsb.id(+)\n"
					+ "and z.yunsfsb_id=yunsfsb.id(+) and z.pinzb_id=pz.id(+) and z.hetb_id="
					+ hetmb_id +" order by zhibb.mingc,z.xiax";
			rs = con.getResultSet(sql);
			i = 0;
			while (rs.next()) {
				long id = rs.getLong("id");
				String zhibmc = rs.getString("zhibmc");
				String tiaojmc = rs.getString("tiaojmc");
				double XIAX = rs.getDouble("XIAX");
				double SHANGX = rs.getDouble("SHANGX");
				String zhibdwmc = rs.getString("zhibdwmc");
				double JIS = rs.getDouble("JIS");
				String jisdwmc = rs.getString("jisdwmc");
//				DecimalFormat df = new DecimalFormat();
//				df.setMaximumFractionDigits(getXiaosw());
//				String kouj = df.format(zengkkbean.getKOUJ());
				double KOUJ = rs.getDouble("KOUJ");
				String koujgs = rs.getString("koujgs");
				String koujdwmc = rs.getString("koujdwmc");
				double ZENGFJ = rs.getDouble("ZENGFJ");
				String zengfjgs = rs.getString("zengfjgs");
				String zengfjdwmc = rs.getString("zengfjdwmc");
				String XIAOSCL = rs.getString("XIAOSCL");
				double JIZZKJ = rs.getDouble("JIZZKJ");
				double JIZZB = rs.getDouble("JIZZB");
				String canzxmmc = rs.getString("canzxmmc");
				String canzxmdwmc = rs.getString("canzxmdwmc");
				double CANZSX = rs.getDouble("CANZSX");
				double CANZXX = rs.getDouble("CANZXX");
				String hetjsxsmc = rs.getString("hetjsxsmc");
				String yunsfsmc = rs.getString("yunsfsmc");
				String pinz = rs.getString("pinz");
				String SHIYFW = rs.getString("SHIYFW");
				String BEIZ = rs.getString("BEIZ");
				String jijlx = rs.getString("jijlx");
				list.add(new Zengkkbean(id, ++i, zhibmc, tiaojmc, SHANGX, XIAX,
						zhibdwmc, JIS, jisdwmc, KOUJ, koujgs,koujdwmc, ZENGFJ,
						zengfjgs,zengfjdwmc, XIAOSCL, JIZZKJ, JIZZB, canzxmmc,
						canzxmdwmc, CANZSX, CANZXX, hetjsxsmc, yunsfsmc, 
						pinz, SHIYFW, BEIZ, jijlx));
			}

			// 文字信息
			sql = "select id,wenznr\n" + "from hetwzb\n" + "where hetb_id="
					+ hetmb_id;
			rs = con.getResultSet(sql);
			if (rs.next()) {
				setWenz(rs.getString("wenznr"));
			}
			rs.close();
//			打开合同变量定义
			Dak_Hetbl(hetmb_id);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
	}
	
	public void Dak_Hetbl(long hetmb_id){
		
//		合同变量定义Grid_begin
		JDBCcon con = new JDBCcon();
		String sql = "select id,hetb_id,bianlmc,value from hetblb where hetb_id="+hetmb_id;
		ResultSetList rsl = con.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("hetb_id").setHidden(true);
		egu.getColumn("hetb_id").setEditor(null);
		egu.getColumn("bianlmc").setHeader("变量名称");
		egu.getColumn("value").setHeader("值");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);	//添加
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);	//删除
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");	//删除
		setExtGrid(egu);
		rsl.close();
		con.Close();
//		合同变量定义Grid_end
	}
	
	public String _xiaosw;
	
	public void setXiaosw(String _value) {
		_xiaosw = _value;
	}
	
	public String getXiaosw(){
		Visit visit=(Visit)this.getPage().getVisit();
		String xsw = "3";
		xsw = MainGlobal.getXitxx_item("合同", "扣价和增付价字段要保留的小数位", String.valueOf(visit.getDiancxxb_id()), "3");
		return xsw;
	}

	private void getGongf() {
		Hetxxbean htxxbean = gethetxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,danwdz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
				+ "from gongysb where id=" + getgongfValue().getId();
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
				htxxbean.setGONGFDH(Gongfdn);
				htxxbean.setGONGFDWDZ(Gongfdwdz);
				htxxbean.setGONGFDWMC(Gongfdwmc);
				htxxbean.setGONGFFDDBR(Gongffddbr);
				htxxbean.setGONGFZH(Gongfzh);
				htxxbean.setGONGFKHYH(Gongfkhyh);
				htxxbean.setGONGFYZBM(Gongfyzbm);
				htxxbean.setGONGFWTDLR(Gongfwtdlr);
				htxxbean.setGongfsh(shuih);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.Close();
		// 发货人同时相同
		setshijgfSelectValue(getIDropDownBean(getshijgfSelectModel(),
				getgongfValue().getId()));

	}

	private void getXuf() {
		Hetxxbean htxxbean = gethetxxbean();
		JDBCcon con = new JDBCcon();
		String sql = "select quanc,diz,YOUZBM,shuih,FADDBR,WEITDLR,KAIHYH,ZHANGH,DIANH\n"
				+ "from diancxxb where id=" + getxufValue().getId();
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
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		con.Close();
	}

	private void getXuanzmb() {
		if (getmobmcSelectValue().getId() == -1) {
			Xinj();
		} else {
			Dak(getmobmcSelectValue().getId());
		}
	}

	private void Shanc() {
		String sql = "";
		JDBCcon con = new JDBCcon();
		long hetmb_id = getmobmcSelectValue().getId();
		// 删除合同信息表
		con.setAutoCommit(false);
		try {
			sql = "delete\n" + "from hetb_mb\n" + "where id=" + hetmb_id;
			con.getDelete(sql);
			sql = "delete\n" + "from hetslb\n" + "where hetslb.hetb_id="
					+ hetmb_id;
			con.getDelete(sql);
			sql = "delete \n" + "from hetzlb\n" + "where hetzlb.hetb_id="
					+ hetmb_id;
			con.getDelete(sql);
			sql = "delete \n" + "from hetjgb\n" + "where hetjgb.hetb_id="
					+ hetmb_id;
			con.getDelete(sql);
			sql = "delete\n" + "from hetwzb\n" + "where hetwzb.hetb_id="
					+ hetmb_id;
			con.getDelete(sql);
			con.commit();

		} catch (Exception e) {
			e.printStackTrace();
			con.rollBack();
		}
		Xinj();
		getmobmcSelectModels();
	}

	private void Baoc() {
		JDBCcon con = new JDBCcon();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Hetxxbean bean = gethetxxbean();
		String sql = "";
		if (getmobmcSelectValue().getId() == -1) {// 如果是新建模板
			// 插入（新增）保存
			// 保存合同信息（包括供方需方）

			long hetb_mb_id = Long.parseLong(MainGlobal
					.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			sql = "insert into hetb_mb(ID,DIANCXXB_ID,HETBH,QIANDRQ,QIANDDD,GONGFDWMC,GONGFDWDZ,GONGFDH,GONGFFDDBR,GONGFWTDLR,"
					+ "GONGFDBGH,GONGFKHYH,GONGFZH,GONGFYZBM,GONGFSH,XUFDWMC,XUFDWDZ,XUFFDDBR,XUFWTDLR,XUFDH,XUFDBGH,XUFKHYH,XUFZH,"
					+ "XUFYZBM,XUFSH,HETGYSBID,GONGYSB_ID,QISRQ,GUOQRQ,JIHKJB_ID,LIUCB_ID,MINGC,meikmcs,xiafmb)\n"
					+ "values("
					+ hetb_mb_id
					+ ","
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()// getxufValue().getId()
					+ ",'"
					+ bean.getHetbh()
					+ "',to_date('"
					+ format.format(bean.getQiandsj())
					+ "','YYYY-MM-DD'),'"
					+ bean.getQianddd()
					+ "','"
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
					+ getgongfValue().getId()
					+ ","
					+ getshijgfSelectValue().getId()
					+ ",to_date('"
					+ format.format(bean.getShengxsj())
					+ "','YYYY-MM-DD'),to_date('"
					+ format.format(bean.getGuoqsj())
					+ "','YYYY-MM-DD'),"
					+ getJihxzValue().getId()
					+ ","
					+ getliucSelectValue().getId()
					+ ",'"
					+ getmobmc()
					+ "','"
					+ getFahr() 
					+ "'," +
					+ getxiafValue().getId()+
					")";
			con.getInsert(sql);
			// 保存数量
			List list = geteditValuess();
			long[] Y = new long[12];
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				String Pinz = ((Fahxxbean) list.get(i)).getPinz();
				String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
				String Faz = ((Fahxxbean) list.get(i)).getFaz();
				String Daoz = ((Fahxxbean) list.get(i)).getDaoz();
				String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
				Shouhr = Shouhr.substring(Shouhr.lastIndexOf(";") + 1, Shouhr
						.length());
				((Fahxxbean) list.get(i)).setShouhr(Shouhr);
				Y[0] = ((Fahxxbean) list.get(i)).getY1();
				Y[1] = ((Fahxxbean) list.get(i)).getY2();
				Y[2] = ((Fahxxbean) list.get(i)).getY3();
				Y[3] = ((Fahxxbean) list.get(i)).getY4();
				Y[4] = ((Fahxxbean) list.get(i)).getY5();
				Y[5] = ((Fahxxbean) list.get(i)).getY6();
				Y[6] = ((Fahxxbean) list.get(i)).getY7();
				Y[7] = ((Fahxxbean) list.get(i)).getY8();
				Y[8] = ((Fahxxbean) list.get(i)).getY9();
				Y[9] = ((Fahxxbean) list.get(i)).getY10();
				Y[10] = ((Fahxxbean) list.get(i)).getY11();
				Y[11] = ((Fahxxbean) list.get(i)).getY12();
				long hetslb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit())
								.getDiancxxb_id()));
				for (int j = 0; j < 12; j++) {
					if (Y[j] != 0) {
						sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
								+ +hetslb_id
								+ ",(select id from pinzb where mingc='"
								+ Pinz
								+ "'),(select id from yunsfsb where mingc='"
								+ Yunsfs
								+ "'),(select id from chezxxb where mingc='"
								+ Faz
								+ "'),(select id from chezxxb where mingc='"
								+ Daoz
								+ "'),(select id from diancxxb where mingc='"
								+ Shouhr
								+ "'),to_date('"
								+ String
										.valueOf(DateUtil.getYear(bean.getShengxsj()))
								+ (j + 1)
								+ "','YYYYMM'),"
								+ Y[j]
								+ ","
								+ hetb_mb_id + ")";
						con.getInsert(sql);
					}
				}
			}
			// 保存质量
			list = geteditValuesz();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				long hetzlb_id = Long.parseLong(MainGlobal
						.getNewID(((Visit) getPage().getVisit())
								.getDiancxxb_id()));
				String zhib = ((Zhilyqbean) list.get(i)).getMingc();
				String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
				double shangx = ((Zhilyqbean) list.get(i)).getShangx();
				double xiax = ((Zhilyqbean) list.get(i)).getXiax();
				String danw = ((Zhilyqbean) list.get(i)).getDanw();
				sql = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
						+ hetzlb_id
						+ ",(select id from zhibb where mingc='"
						+ zhib
						+ "' and leib=1),(select id from tiaojb where mingc='"
						+ tiaoj
						+ "'),"
						+ shangx
						+ ","
						+ xiax
						+ ",(select max(id) from danwb where mingc='"
						+ danw
						+ "')," + hetb_mb_id + ")";
				con.getInsert(sql);
			}
			// 保存价格
			list = geteditValuesg();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
				String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
				double shangx = ((jijbean) list.get(i)).getShangx();
				double xiax = ((jijbean) list.get(i)).getXiax();
				String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
				double jij = ((jijbean) list.get(i)).getJij();
				String jijdw = ((jijbean) list.get(i)).getJijdwid();
				String jijgs = ((jijbean) list.get(i)).getJijgs();
				String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
				String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
				double yunj = ((jijbean) list.get(i)).getYunj();
				String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
				String yingdkf = ((jijbean) list.get(i)).getYingdkf();
				String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
				double zuigmj = ((jijbean) list.get(i)).getZuigmj();
				String zuigmjdw = ((jijbean) list.get(i)).getZuigmjdw();
				String hetjjfsb_id = ((jijbean) list.get(i)).getHetjjfsb_id();
				double fengsjj = ((jijbean) list.get(i)).getFengsjj();
				String fengsjjdw = ((jijbean) list.get(i)).getFengsjjdw();
				String jijlx=((jijbean) list.get(i)).getJijlx();
				String pinz =  ((jijbean) list.get(i)).getPinz();//品种
				sql = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n"
						+ "YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,HETB_ID,HETJJFSB_ID,FENGSJJ,FENGSJJDW,JIJLX,pinzb_id) values("
						+ "getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+")"
						+ ",(select id from zhibb where mingc='"
						+ zhibb_id
						+ "' and leib=1),(select id from tiaojb where mingc='"
						+ tiaojb_id
						+ "'),"
						+ shangx
						+ ","
						+ xiax
						+ ",(select max(id) from danwb where mingc='"
						+ zhibdw
						+ "'),"
						+ jij
						+ ",(select max(id) from danwb where mingc='"
						+ jijdw
						+ "'),'"
						+jijgs
						+"',(select id from hetjsfsb where mingc='"
						+ hetjsfsb_id
						+ "'),(select id from hetjsxsb where mingc='"
						+ Hetjsxsb_id
						+ "'),"
						+ yunj
						+ ",(select max(id) from danwb where mingc='"
						+ yunjdw
						+ "'),"
						+ getProperId(getyingdkfgSelectModel(), yingdkf)
						+ ",(select id from yunsfsb where mingc='"
						+ Yunsfsb_id
						+ "'),"
						+ zuigmj
						+ ",(select max(id) from danwb where mingc='"
						+ zuigmjdw
						+ "'),"
						+ hetb_mb_id
						+ ",(select id from hetjjfsb where mingc='"
						+ hetjjfsb_id + "'),"
						+ fengsjj
						+ ",(select max(id) from danwb where mingc='"
						+ fengsjjdw
						+ "')," 
						+ this.getProperId(this.getIJijlxModel(), jijlx)
						+",(select max(id) from pinzb where mingc='"+pinz
						+ "'))";
				con.getInsert(sql);
			}
			// 保存增扣价格
			list = geteditValuesj();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
				String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
				double shangx = ((Zengkkbean) list.get(i)).getSHANGX();
				double xiax = ((Zengkkbean) list.get(i)).getXIAX();
				String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
				double jis = ((Zengkkbean) list.get(i)).getJIS();
				String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
				double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
				String koujgs = ((Zengkkbean) list.get(i)).getKoujgs();
				String koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
				double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
				String zengfjgs = ((Zengkkbean) list.get(i)).getZengfjgs();
				String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
				String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
				double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
				double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
				String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
				String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
				double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
				double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
				String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
				String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
				String pinzb_id = ((Zengkkbean) list.get(i)).getPinzb_id();
				String SHIYFW = ((Zengkkbean) list.get(i)).getShiyfw();
				String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
				String jijlx = ((Zengkkbean) list.get(i)).getJIJLX();
				sql = "insert into hetzkkb( ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,"
						+ "ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID,PINZB_ID,SHIYFW,JIJLX)"
						+ "values("
						+ "getnewid("+((Visit) getPage().getVisit()).getDiancxxb_id()+")"
						+ ",(select id from zhibb where mingc='"
						+ zhibb_id
						+ "' and leib=1),(select id from tiaojb where mingc='"
						+ tiaojb_id
						+ "'),"
						+ shangx
						+ ","
						+ xiax
						+ ",(select max(id) from danwb where mingc='"
						+ zhibdw
						+ "'),"
						+ jis
						+ ",(select max(id) from danwb where mingc='"
						+ jisdwb_id
						+ "'),"
						+ kouj
						+ ",'"
						+ koujgs
						+ "',(select max(id) from danwb where mingc='"
						+ koujdw
						+ "'),"
						+ zengfj
						+ ",'"
						+ zengfjgs
						+ "',(select max(id) from danwb where mingc='"
						+ zengfjdw
						+ "'),"
						+ getProperId(getxiaoswcljSelectModel(), xiaoscl)
						+ ","
						+ jizzkj
						+ ","
						+ jizzb
						+ ",(select id from zhibb where mingc='"
						+ canzxm
						+ "' and leib=1),(select max(id) from danwb where mingc='"
						+ canzxmdw
						+ "'),"
						+ canzsx
						+ ","
						+ canzxx
						+ ",(select id from hetjsxsb where mingc='"
						+ hetjsxsb_id
						+ "'),(select id from yunsfsb where mingc='"
						+ yunsfsb_id + "'),'" 
						+ beiz 
						+ "'," 
						+ hetb_mb_id 
						+ ",(select max(id) from pinzb where mingc='"
						+pinzb_id
						+"'),decode('"+ SHIYFW+ "','超出部分',1,0),"
						+ this.getProperId(this.getIJijlxModel(), jijlx) + ")";
				con.getInsert(sql);
			}
			// 保存文字
			long hetwzb_id = Long.parseLong(MainGlobal
					.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
			sql = "insert into hetwzb(id,wenznr,hetb_id)values(" + hetwzb_id
					+ ",'" + getWenz() + "'," + hetb_mb_id + ")";
			con.getInsert(sql);
			// 刷新模板下拉框
			getmobmcSelectModels();
			setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),
					hetb_mb_id));
		} else {// 如果不是新建模板则是已经存在的模板，进行更新操作
			// 保存合同信息（包括供方需方）
			long hetb_mb_id = getmobmcSelectValue().getId();
			sql = "update hetb_mb set HETBH='" + bean.getHetbh()
					+ "',QIANDRQ=to_date('" + format.format(bean.getQiandsj())
					+ "','YYYY-MM-DD'),QIANDDD='" + bean.getQianddd()
					+ "',GONGFDWMC='" + bean.getGONGFDWMC() + "',GONGFDWDZ='"
					+ bean.getGONGFDWDZ() + "',GONGFDH='" + bean.getGONGFDH()
					+ "',GONGFFDDBR='" + bean.getGONGFFDDBR()
					+ "',GONGFWTDLR='" + bean.getGONGFWTDLR() + "',GONGFDBGH='"
					+ bean.getGONGFDBGH() + "',GONGFKHYH='"
					+ bean.getGONGFKHYH() + "',GONGFZH='" + bean.getGONGFZH()
					+ "',GONGFYZBM='" + bean.getGONGFYZBM() + "',GONGFSH='"
					+ bean.getGongfsh() + "',XUFDWMC='" + bean.getXUFDWMC()
					+ "',XUFDWDZ='" + bean.getXUFDWDZ() + "',XUFFDDBR='"
					+ bean.getXUFFDDBR() + "',XUFWTDLR='" + bean.getXUFWTDLR()
					+ "',XUFDH='" + bean.getXUFDH() + "',XUFDBGH='"
					+ bean.getXUFDBGH() + "',XUFKHYH='" + bean.getXUFKHYH()
					+ "',XUFZH='" + bean.getXUFZH() + "',XUFYZBM='"
					+ bean.getXUFYZBM() + "',XUFSH='" + bean.getXufsh()
					+ "',HETGYSBID=" + getgongfValue().getId() + ",GONGYSB_ID="
					+ getshijgfSelectValue().getId() + ",QISRQ=to_date('"
					+ format.format(bean.getShengxsj())
					+ "','YYYY-MM-DD'),GUOQRQ=to_date('"
					+ format.format(bean.getGuoqsj())
					+ "','YYYY-MM-DD'),JIHKJB_ID="
					+ getJihxzValue().getId()
					+ ",LIUCB_ID="
					+ getliucSelectValue().getId()
					// +",HETJSFSB_ID=" +0// 暂时为空
					// +",HETJSXSB_ID=" + 0
					// +",HETYJ=" + 0
					+ ",diancxxb_id="
					+ ((Visit) getPage().getVisit()).getDiancxxb_id()// getxufValue().getId()
					+ ",xiafmb="
					+ getxiafValue()// getxufValue().getId()
					+ ",MINGC='" + getmobmc() + "',meikmcs='" + getFahr()

					+ "'where id=" + hetb_mb_id;
			con.getUpdate(sql);
			// 保存数量
			List list = geteditValuess();
			long[] Y = new long[12];
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				long hetslb_id = ((Fahxxbean) list.get(i)).getId();
				String Pinz = ((Fahxxbean) list.get(i)).getPinz();
				String Yunsfs = ((Fahxxbean) list.get(i)).getYunsfs();
				String Faz = ((Fahxxbean) list.get(i)).getFaz();
				String Daoz = ((Fahxxbean) list.get(i)).getDaoz();
				String Shouhr = ((Fahxxbean) list.get(i)).getShouhr();
				Shouhr = Shouhr.substring(Shouhr.lastIndexOf(";") + 1, Shouhr
						.length());
				((Fahxxbean) list.get(i)).setShouhr(Shouhr);
				Y[0] = ((Fahxxbean) list.get(i)).getY1();
				Y[1] = ((Fahxxbean) list.get(i)).getY2();
				Y[2] = ((Fahxxbean) list.get(i)).getY3();
				Y[3] = ((Fahxxbean) list.get(i)).getY4();
				Y[4] = ((Fahxxbean) list.get(i)).getY5();
				Y[5] = ((Fahxxbean) list.get(i)).getY6();
				Y[6] = ((Fahxxbean) list.get(i)).getY7();
				Y[7] = ((Fahxxbean) list.get(i)).getY8();
				Y[8] = ((Fahxxbean) list.get(i)).getY9();
				Y[9] = ((Fahxxbean) list.get(i)).getY10();
				Y[10] = ((Fahxxbean) list.get(i)).getY11();
				Y[11] = ((Fahxxbean) list.get(i)).getY12();
				if (hetslb_id == 0) {
					hetslb_id = Long.parseLong(MainGlobal
							.getNewID(((Visit) getPage().getVisit())
									.getDiancxxb_id()));
					for (int j = 0; j < 12; j++) {
						if (Y[j] != 0) {
							sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
									+ +hetslb_id
									+ ",(select id from pinzb where mingc='"
									+ Pinz
									+ "'),(select id from yunsfsb where mingc='"
									+ Yunsfs
									+ "'),(select id from chezxxb where mingc='"
									+ Faz
									+ "'),(select id from chezxxb where mingc='"
									+ Daoz
									+ "'),(select id from diancxxb where mingc='"
									+ Shouhr
									+ "'),to_date('"
									+ String.valueOf(DateUtil.getYear(bean.getShengxsj()))
									+ (j + 1)
									+ "','YYYYMM'),"
									+ Y[j]
									+ ","
									+ hetb_mb_id
									+ ")";
							con.getInsert(sql);
						}
					}
					((Fahxxbean) list.get(i)).setId(hetslb_id);
				} else {// 对于非新增的记录
					sql = "delete\n" + "from hetslb\n" + "where id="
							+ hetslb_id;
					con.getDelete(sql);
					for (int j = 0; j < 12; j++) {
						if (Y[j] != 0) {
							sql = "insert into hetslb(ID,PINZB_ID,YUNSFSB_ID,FAZ_ID,DAOZ_ID,DIANCXXB_ID,RIQ,HETL,HETB_ID)values("
									+ +hetslb_id
									+ ",(select id from pinzb where mingc='"
									+ Pinz
									+ "'),(select id from yunsfsb where mingc='"
									+ Yunsfs
									+ "'),(select id from chezxxb where mingc='"
									+ Faz
									+ "'),(select id from chezxxb where mingc='"
									+ Daoz
									+ "'),(select id from diancxxb where mingc='"
									+ Shouhr
									+ "'),to_date('"
									+ String.valueOf(DateUtil.getYear(bean.getShengxsj()))
									+ (j + 1)
									+ "','YYYYMM'),"
									+ Y[j]
									+ ","
									+ hetb_mb_id
									+ ")";
							con.getInsert(sql);
						}
					}
				}

			}
			// 保存质量
			list = geteditValuesz();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				long hetzlb_id = ((Zhilyqbean) list.get(i)).getId(); //
				String zhib = ((Zhilyqbean) list.get(i)).getMingc();
				String tiaoj = ((Zhilyqbean) list.get(i)).getTiaoj();
				double shangx = ((Zhilyqbean) list.get(i)).getShangx();
				double xiax = ((Zhilyqbean) list.get(i)).getXiax();
				String danw = ((Zhilyqbean) list.get(i)).getDanw();
				if (hetzlb_id == 0) {
					hetzlb_id = Long.parseLong(MainGlobal
							.getNewID(((Visit) getPage().getVisit())
									.getDiancxxb_id()));
					sql = "insert into hetzlb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,HETB_ID)values("
							+ hetzlb_id
							+ ",(select id from zhibb where mingc='"
							+ zhib
							+ "' and leib=1),(select id from tiaojb where mingc='"
							+ tiaoj
							+ "'),"
							+ shangx
							+ ","
							+ xiax
							+ ",(select max(id) from danwb where mingc='"
							+ danw + "')," + hetb_mb_id + ")";
					con.getInsert(sql);
					((Zhilyqbean) list.get(i)).setId(hetzlb_id);
				} else {
					sql = "update hetzlb "
							+ "set zhibb_id=(select id from zhibb where mingc='"
							+ zhib
							+ "' and leib=1),tiaojb_id=(select id from tiaojb where mingc='"
							+ tiaoj
							+ "'),shangx="
							+ shangx
							+ ",xiax="
							+ xiax
							+ ",danwb_id=(select max(id) from danwb where mingc='"
							+ danw + "')where id=" + hetzlb_id;
					con.getUpdate(sql);
				}

			}
			// 保存价格
			list = geteditValuesg();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				long hetjgb_id = ((jijbean) list.get(i)).getId();
				String zhibb_id = ((jijbean) list.get(i)).getZhibb_id();
				String tiaojb_id = ((jijbean) list.get(i)).getTiaojb_id();
				double shangx = ((jijbean) list.get(i)).getShangx();
				double xiax = ((jijbean) list.get(i)).getXiax();
				String zhibdw = ((jijbean) list.get(i)).getDanwb_id();
				double jij = ((jijbean) list.get(i)).getJij();
				String jijdw = ((jijbean) list.get(i)).getJijdwid();
				String jijgs = ((jijbean) list.get(i)).getJijgs();
				String hetjsfsb_id = ((jijbean) list.get(i)).getHetjsfsb_id();
				String Hetjsxsb_id = ((jijbean) list.get(i)).getHetjsxsb_id();
				double yunj = ((jijbean) list.get(i)).getYunj();
				String yunjdw = ((jijbean) list.get(i)).getYunjdw_id();
				String yingdkf = ((jijbean) list.get(i)).getYingdkf();
				String Yunsfsb_id = ((jijbean) list.get(i)).getYunsfsb_id();
				double zuigmj = ((jijbean) list.get(i)).getZuigmj();
				String zuigmjdw = ((jijbean) list.get(i)).getZuigmjdw();
				String hetjjfsb = ((jijbean) list.get(i)).getHetjjfsb_id();
				double fengsjj = ((jijbean) list.get(i)).getFengsjj();
				String fengsjjdw = ((jijbean) list.get(i)).getFengsjjdw();
				String jijlx = ((jijbean) list.get(i)).getJijlx();
				String pinz =  ((jijbean) list.get(i)).getPinz();//品种
				if (hetjgb_id == 0) {
					hetjgb_id = Long.parseLong(MainGlobal
							.getNewID(((Visit) getPage().getVisit())
									.getDiancxxb_id()));
					sql = "insert into hetjgb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIJ,JIJDWID,JIJGS,HETJSFSB_ID,HETJSXSB_ID,YUNJ,YUNJDW_ID,\n"
							+ "YINGDKF,YUNSFSB_ID,ZUIGMJ,ZUIGMJDW,HETB_ID,HETJJFSB_ID,FENGSJJ,FENGSJJDW,JIJLX,pinzb_id) values("
							+ hetjgb_id
							+ ",(select id from zhibb where mingc='"
							+ zhibb_id
							+ "' and leib=1),(select id from tiaojb where mingc='"
							+ tiaojb_id
							+ "'),"
							+ shangx
							+ ","
							+ xiax
							+ ",(select max(id) from danwb where mingc='"
							+ zhibdw
							+ "'),"
							+ jij
							+ ",(select max(id) from danwb where mingc='"
							+ jijdw
							+ "'),'"
							+jijgs
							+"',(select id from hetjsfsb where mingc='"
							+ hetjsfsb_id
							+ "'),(select id from hetjsxsb where mingc='"
							+ Hetjsxsb_id
							+ "'),"
							+ yunj
							+ ",(select max(id) from danwb where mingc='"
							+ yunjdw
							+ "'),"
							+ getProperId(getyingdkfgSelectModel(), yingdkf)
							+ ",(select id from yunsfsb where mingc='"
							+ Yunsfsb_id
							+ "'),"
							+ zuigmj
							+ ",(select max(id) from danwb where mingc='"
							+ zuigmjdw
							+ "'),"
							+ hetb_mb_id
							+ ",(select id from hetjjfsb where  mingc='"
							+ hetjjfsb+"'),"
							+ fengsjj
							+ ",(select max(id) from danwb where mingc='"
							+ fengsjjdw
							+ "'),"
							+ this.getProperId(this.getIJijlxModel(), jijlx)
							+",(select max(id) from pinzb where mingc='"+pinz
							+ "'))";
					con.getInsert(sql);
					((jijbean) list.get(i)).setId(hetjgb_id);
				} else {
					sql = "update hetjgb set ZHIBB_ID=(select id from zhibb where mingc='"
							+ zhibb_id
							+ "' and leib=1),TIAOJB_ID=(select id from tiaojb where mingc='"
							+ tiaojb_id
							+ "'),SHANGX="
							+ shangx
							+ ",XIAX="
							+ xiax
							+ ",DANWB_ID=(select max(id) from danwb where mingc='"
							+ zhibdw
							+ "'),JIJ="
							+ jij
							+ ",JIJDWID=(select max(id) from danwb where mingc='"
							+ jijdw
							+ "'),JIJGS='"
							+jijgs
							+"',HETJSFSB_ID=(select id from hetjsfsb where mingc='"
							+ hetjsfsb_id
							+ "'),HETJSXSB_ID=(select id from hetjsxsb where mingc='"
							+ Hetjsxsb_id
							+ "'),YUNJ="
							+ yunj
							+ ",YUNJDW_ID=(select max(id) from danwb where mingc='"
							+ yunjdw
							+ "'),YINGDKF="
							+ getProperId(getyingdkfgSelectModel(), yingdkf)
							+ ",YUNSFSB_ID=(select id from yunsfsb where mingc='"
							+ Yunsfsb_id
							+ "'),ZUIGMJ="
							+ zuigmj
							+ ", ZUIGMJDW = (select max(id) from danwb where mingc='" + zuigmjdw
							+ "'),HETJJFSB_ID=(select id from hetjjfsb where mingc='"
							+ hetjjfsb+"'),fengsjj="
							+ fengsjj
							+", FENGSJJDW = (select max(id) from danwb where mingc='"+ fengsjjdw +"'), jijlx="
							+ this.getProperId(this.getIJijlxModel(), jijlx)
							+",pinzb_id=(select max(id) from pinzb where mingc='"+pinz
							+ "') where hetjgb.id="+hetjgb_id;
					con.getUpdate(sql);
				}
			}
			// 保存增扣款
			list = geteditValuesj();
			for (int i = 0; i < list.size(); i++) {// 遍历容器
				long hetzkkb_id = ((Zengkkbean) list.get(i)).getId();
				String zhibb_id = ((Zengkkbean) list.get(i)).getZHIBB_ID();
				String tiaojb_id = ((Zengkkbean) list.get(i)).getTIAOJB_ID();
				double shangx = ((Zengkkbean) list.get(i)).getSHANGX();
				double xiax = ((Zengkkbean) list.get(i)).getXIAX();
				String zhibdw = ((Zengkkbean) list.get(i)).getDANWB_ID();
				double jis = ((Zengkkbean) list.get(i)).getJIS();
				String jisdwb_id = ((Zengkkbean) list.get(i)).getJISDWID();
				double kouj = ((Zengkkbean) list.get(i)).getKOUJ();
				String koujgs = ((Zengkkbean) list.get(i)).getKoujgs();
				String koujdw = ((Zengkkbean) list.get(i)).getKOUJDW();
				double zengfj = ((Zengkkbean) list.get(i)).getZENGFJ();
				String zengfjgs = ((Zengkkbean) list.get(i)).getZengfjgs();
				String zengfjdw = ((Zengkkbean) list.get(i)).getZENGFJDW();
				String xiaoscl = ((Zengkkbean) list.get(i)).getXIAOSCL();
				double jizzb = ((Zengkkbean) list.get(i)).getJIZZB();
				double jizzkj = ((Zengkkbean) list.get(i)).getJIZZKJ();
				String canzxm = ((Zengkkbean) list.get(i)).getCANZXM();
				String canzxmdw = ((Zengkkbean) list.get(i)).getCANZXMDW();
				double canzsx = ((Zengkkbean) list.get(i)).getCANZSX();
				double canzxx = ((Zengkkbean) list.get(i)).getCANZXX();
				String hetjsxsb_id = ((Zengkkbean) list.get(i)).getJIESXXB_ID();
				String yunsfsb_id = ((Zengkkbean) list.get(i)).getYUNSFSB_ID();
				String pinzb_id = ((Zengkkbean) list.get(i)).getPinzb_id();
				String SHIYFW = ((Zengkkbean) list.get(i)).getShiyfw();
				String beiz = ((Zengkkbean) list.get(i)).getBEIZ();
				String jijlx = ((Zengkkbean) list.get(i)).getJIJLX();
				if (hetzkkb_id == 0) {
					hetzkkb_id = Long.parseLong(MainGlobal
							.getNewID(((Visit) getPage().getVisit())
									.getDiancxxb_id()));
					sql = "insert into hetzkkb(ID,ZHIBB_ID,TIAOJB_ID,SHANGX,XIAX,DANWB_ID,JIS,JISDWID,KOUJ,KOUJGS,KOUJDW,ZENGFJ,ZENGFJGS,"
							+ "ZENGFJDW,XIAOSCL,JIZZKJ,JIZZB,CANZXM,CANZXMDW,CANZSX,CANZXX,HETJSXSB_ID,YUNSFSB_ID,BEIZ,HETB_ID,PINZB_ID,SHIYFW,JIJLX)"
							+ "values("
							+ hetzkkb_id
							+ ",(select max(id) from zhibb where mingc='"
							+ zhibb_id
							+ "' and leib=1),(select max(id) from tiaojb where mingc='"
							+ tiaojb_id
							+ "'),"
							+ shangx
							+ ","
							+ xiax
							+ ",(select max(id) from danwb where mingc='"
							+ zhibdw
							+ "'),"
							+ jis
							+ ",(select max(id) from danwb where mingc='"
							+ jisdwb_id
							+ "'),"
							+ kouj
							+ ",'"
							+ koujgs
							+ "',(select max(id) from danwb where mingc='"
							+ koujdw
							+ "'),"
							+ zengfj
							+ ",'"
							+ zengfjgs
							+ "',(select max(id) from danwb where mingc='"
							+ zengfjdw
							+ "'),"
							+ getProperId(getxiaoswcljSelectModel(), xiaoscl)
							+ ","
							+ jizzkj
							+ ","
							+ jizzb
							+ ",(select max(id) from zhibb where mingc='"
							+ canzxm
							+ "' and leib=1),(select max(id) from danwb where mingc='"
							+ canzxmdw
							+ "'),"
							+ canzsx
							+ ","
							+ canzxx
							+ ",(select max(id) from hetjsxsb where mingc='"
							+ hetjsxsb_id
							+ "'),(select max(id) from yunsfsb where mingc='"
							+ yunsfsb_id
							+ "'),'"
							+ beiz
							+ "',"
							+ hetb_mb_id
							+ ",(select max(pinzb_id) from pinzb where mingc='"
							+ pinzb_id
							+ "'),decode('"+ SHIYFW+ "','超出部分',1,0),"
							+ this.getProperId(this.getIJijlxModel(), jijlx) + ")";
					con.getInsert(sql);
					((Zengkkbean) list.get(i)).setId(hetzkkb_id);
				} else {
					sql = "update hetzkkb set ZHIBB_ID=(select id from zhibb where mingc='"
							+ zhibb_id
							+ "' and leib=1),TIAOJB_ID=(select id from tiaojb where mingc='"
							+ tiaojb_id
							+ "'),SHANGX="
							+ shangx
							+ ",XIAX="
							+ xiax
							+ ",DANWB_ID=(select max(id) from danwb where mingc='"
							+ zhibdw
							+ "'),JIS="
							+ jis
							+ ",JISDWID=(select max(id) from danwb where mingc='"
							+ jisdwb_id
							+ "'),KOUJ="
							+ kouj
							+ ",KOUJGS='"
							+ koujgs
							+ "',KOUJDW=(select max(id) from danwb where mingc='"
							+ koujdw
							+ "'),ZENGFJ="
							+ zengfj
							+ ",ZENGFJGS='"
							+ zengfjgs
							+ "',ZENGFJDW=(select max(id) from danwb where mingc='"
							+ zengfjdw
							+ "'),XIAOSCL="
							+ getProperId(getxiaoswcljSelectModel(), xiaoscl)
							+ ",JIZZKJ="
							+ jizzkj
							+ ",JIZZB="
							+ jizzb
							+ ",CANZXM=(select max(id) from zhibb where mingc='"
							+ canzxm
							+ "'),CANZXMDW=(select max(id) from danwb where mingc='"
							+ canzxmdw
							+ "'),CANZSX="
							+ canzsx
							+ ",CANZXX="
							+ canzxx
							+ ",HETJSXSB_ID=(select max(id) from hetjsxsb where mingc='"
							+ hetjsxsb_id
							+ "'),YUNSFSB_ID=(select max(id) from yunsfsb where mingc='"
							+ yunsfsb_id
							+ "'),"
							+ "pinzb_id=(select max(id) from pinzb where mingc='"
							+ pinzb_id
							+"'),BEIZ='"
							+ beiz
							+ "',shiyfw=decode('"+ SHIYFW+ "','超出部分',1,0), jijlx="+ this.getProperId(this.getIJijlxModel(), jijlx) +" where id="
							+ hetzkkb_id;
					con.getUpdate(sql);
				}
			}
			// 保存文字
			sql = "update hetwzb\n" + "set wenznr='" + getWenz()
					+ "'where hetb_id=" + hetb_mb_id;
			con.getUpdate(sql);
			getmobmcSelectModels();
			setmobmcSelectValue(getIDropDownBean(getmobmcSelectModel(),
					hetb_mb_id));
		}
	}

	private void Inserts() {
		List _value = geteditValuess();
		_value.add(new Fahxxbean("", ""));// 缺省到站,收货人
	}

	private void Deletes() {
		List _value = geteditValuess();
		if (_editTableRows != -1) {
			long id = ((Fahxxbean) _value.get(_editTableRows)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetslb where id=" + id;
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRows);
		}
	}

	private void Insertz() {
		List _value = geteditValuesz();
		_value.add(new Zhilyqbean(_value.size() + 1));
	}

	private void Deletez() {
		List _value = geteditValuesz();
		if (_editTableRowz != -1) {
			long id = ((Zhilyqbean) _value.get(_editTableRowz)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetzlb where id=" + id;
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowz);
			int c = _value.size();
			for (int a = _editTableRowz; a < c; a++) {
				((Zhilyqbean) _value.get(a))
						.setXuh(((Zhilyqbean) _value.get(a)).getXuh() - 1);
			}
		}
	}

	private void Insertj() {
		List _value = geteditValuesj();
		_value.add(new Zengkkbean(_value.size() + 1, "元/吨"));
	}

	private void Deletej() {
		List _value = geteditValuesj();
		if (_editTableRowj != -1) {
			long id = ((Zengkkbean) _value.get(_editTableRowj)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetzkkb where id=" + id;
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowj);
			int c = _value.size();
			for (int a = _editTableRowj; a < c; a++) {
				((Zengkkbean) _value.get(a))
						.setXuh(((Zengkkbean) _value.get(a)).getXuh() - 1);
			}
		}
	}

	private void Insertg() {
		List _value = geteditValuesg();
		_value.add(new jijbean(_value.size() + 1, "元/吨"));
	}

	private void Deleteg() {
		List _value = geteditValuesg();
		if (_editTableRowg != -1) {
			long id = ((jijbean) _value.get(_editTableRowg)).getId();
			if (id != 0) {
				JDBCcon con = new JDBCcon();
				String sql = "delete from hetjgb where id=" + id;
				con.getDelete(sql);
				con.Close();
			}
			_value.remove(_editTableRowg);
			int c = _value.size();
			for (int a = _editTableRowg; a < c; a++) {
				((jijbean) _value.get(a)).setXuh(((jijbean) _value.get(a))
						.getXuh() - 1);
			}
		}
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
	
//	 适用范围
	public IDropDownBean getshiyfwSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean27() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean27((IDropDownBean) getshiyfwSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean27();
	}

	public void setshiyfwSelectValue(IDropDownBean Value) {
		((Visit) getPage().getVisit()).setDropDownBean27(Value);
	}

	public void setshiyfwSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel27(value);
	}

	public IPropertySelectionModel getshiyfwSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel27() == null) {
			getshiyfwSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel27();
	}

	public void getshiyfwSelectModels() {
		List list = new ArrayList();
		list.add(new IDropDownBean(0, "全部"));
		list.add(new IDropDownBean(1, "超出部分"));
		((Visit) getPage().getVisit())
				.setProSelectionModel27(new IDropDownModel(list));
		return;
	}
}