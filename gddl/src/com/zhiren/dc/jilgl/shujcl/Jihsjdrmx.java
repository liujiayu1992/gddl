package com.zhiren.dc.jilgl.shujcl;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Jihsjdrmx extends BasePage implements PageValidateListener {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private void Return(IRequestCycle cycle) {
		Visit v = (Visit) this.getPage().getVisit();
		cycle.activate(v.getString10());
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id, c.diancxxb_id, c.gongysmc, c.meikdwmc, c.faz,\n");
		sb.append("c.pinz, c.jihkj, c.fahrq, c.daohrq, c.jianjfs,c.chebb_id,\n");
		sb.append("c.chec,caiybh, c.qingcsj, c.zhongcjjy, c.cheph, c.maoz, c.piz, c.biaoz,\n");
		sb.append("c.koud, c.kous, c.kouz, c.sanfsl, c.daoz,yuandz,c.yuanshdw,\n");
		sb.append("c.meikdwmc yuanmkdw, c.yunsdwb_id, c.daozch, c.beiz ,c.fahbtmp_id from chepbtmp c where diancxxb_id = ");
		sb.append(visit.getDiancxxb_id() + " and c.qicrjhb_id = "+visit.getLong1()+" and fahb_id = 0");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chepbtmp");
		
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcsb="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcsb));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("gongysmc").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysmc").setEditor(null);
		egu.getColumn("meikdwmc").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikdwmc").setEditor(null);
		egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz").setWidth(65);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("pinz").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinz").setWidth(50);
		egu.getColumn("pinz").setEditor(null);
		egu.getColumn("jihkj").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkj").setWidth(65);
		egu.getColumn("jihkj").setEditor(null);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("fahrq").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("jianjfs").setHeader(Locale.jianjfs_chepb);
		egu.getColumn("jianjfs").setWidth(60);
		egu.getColumn("jianjfs").setEditor(null);
		egu.getColumn("chebb_id").setHeader(Locale.chebb_id_chepb);
		egu.getColumn("chebb_id").setWidth(60);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chebb_id").returnId=false;
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("caiybh").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("qingcsj").setHidden(true);
		egu.getColumn("qingcsj").editor = null;
		egu.getColumn("qingcsj").setEditor(null);
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(80);
		egu.getColumn("zhongcjjy").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(65);
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(65);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(65);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setDefaultValue("0");//设置默认值
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("kous").setHeader(Locale.kous_chepb);
		egu.getColumn("kous").setDefaultValue("0");
		egu.getColumn("kous").setWidth(60);
		egu.getColumn("kouz").setHeader(Locale.kouz_chepb);
		egu.getColumn("kouz").setDefaultValue("0");
		egu.getColumn("kouz").setWidth(60);
		egu.getColumn("sanfsl").setHeader(Locale.sanfsl_chepb);
		egu.getColumn("sanfsl").setDefaultValue("0");
		egu.getColumn("sanfsl").setWidth(60);
		egu.getColumn("daoz").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz").setWidth(65);
		egu.getColumn("yuandz").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz").setWidth(65);
		egu.getColumn("yuanshdw").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("fahbtmp_id").setHidden(true);
		egu.getColumn("fahbtmp_id").setEditor(null);
		
		//设置供应商下拉框
		ComboBox c8 = new ComboBox();
		egu.getColumn("gongysmc").setEditor(c8);
		c8.setEditable(true);
		String gyssb = "select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysmc").setComboEditor(egu.gridId,
				new IDropDownModel(gyssb));
		egu.getColumn("gongysmc").returnId=false;
		//设置煤矿单位下拉框
		ComboBox c9 = new ComboBox();
		egu.getColumn("meikdwmc").setEditor(c9);
		c9.setEditable(true);
		c9.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mksb = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikdwmc").setComboEditor(egu.gridId,
				new IDropDownModel(mksb));
		egu.getColumn("meikdwmc").returnId=false;
		//设置发站下拉框
		ComboBox c0 = new ComboBox();
		egu.getColumn("faz").setEditor(c0);
		c0.setEditable(true);
		String Fazsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("faz").setComboEditor(egu.gridId,
				new IDropDownModel(Fazsb));
		egu.getColumn("faz").returnId=false;

		//		设置到站下拉框
		ComboBox c1 = new ComboBox();
		egu.getColumn("daoz").setEditor(c1);
		c1.setEditable(true);
		String daozsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("daoz").setComboEditor(egu.gridId,
				new IDropDownModel(daozsb));
		egu.getColumn("daoz").returnId=false;
		//设置品种下拉框
		ComboBox c2 = new ComboBox();
		egu.getColumn("pinz").setEditor(c2);
		c2.setEditable(true);
		String pinzsb = SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pinzsb));
		egu.getColumn("pinz").returnId=false;
		//设置口径下拉框
		ComboBox c3 = new ComboBox();
		egu.getColumn("jihkj").setEditor(c3);
		c3.setEditable(true);
		String jihkjsb = SysConstant.SQL_Kouj;
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(jihkjsb));
		egu.getColumn("jihkj").returnId=false;
		//设置检斤方式下拉框
		List l = new ArrayList();
		l.add(new IDropDownBean(0, "过衡"));
		l.add(new IDropDownBean(1, "检尺"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("jianjfs").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("jianjfs").setComboEditor(egu.gridId,
				new IDropDownModel(l));
		egu.getColumn("jianjfs").returnId=false;

		//设置车别下拉框
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1,"路车"));
		ls.add(new IDropDownBean(2, "自备车"));
		ls.add(new IDropDownBean(3, "汽车"));
		ComboBox c5 = new ComboBox();
		egu.getColumn("chebb_id").setEditor(c5);
		c5.setEditable(true);
		egu.getColumn("chebb_id").setComboEditor(egu.gridId,
				new IDropDownModel(ls));

		//设置原到站下拉框
		ComboBox c6 = new ComboBox();
		egu.getColumn("yuandz").setEditor(c6);
		c6.setEditable(true);
		String Yuandzsb = "select id,mingc from chezxxb order by mingc";
		egu.getColumn("yuandz").setComboEditor(egu.gridId,
				new IDropDownModel(Yuandzsb));
		egu.getColumn("yuandz").setDefaultValue(visit.getDaoz());
		egu.getColumn("yuandz").returnId=false;
		
		//设置原收货单位下拉框
		ComboBox c7 = new ComboBox();
		egu.getColumn("yuanshdw").setEditor(c7);
		c7.setEditable(true);//设置可输入
		String sql = "select id,mingc from vwyuanshdw order by mingc";
		egu.getColumn("yuanshdw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		egu.getColumn("yuanshdw").returnId=false;

		//设置运输单位下拉框
		ComboBox comb = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(comb);
		comb.setEditable(true);
		String yunsdwsb = "select id,mingc from yunsdwb where diancxxb_id="
				+ ((Visit) getPage().getVisit()).getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel(yunsdwsb));
		egu.getColumn("yunsdwb_id").returnId=false;
		
		egu.setWidth(1024);

		//设置GRID是否可以编辑
//		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(0);

//		GridButton sab = new GridButton("保存","function(){ " +
//		" document.getElementById('SaveButton').click();}");
//		egu.addTbarBtn(sab);
		
		GridButton bc = new GridButton("返回","function(){ " +
				" document.getElementById('ReturnButton').click();}");
		egu.addTbarBtn(bc);
		

		setExtGrid(egu);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid()==null){
			return "";
		}
		return getExtGrid().getHtml();
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
			getSelectData();
		}
	}
}
