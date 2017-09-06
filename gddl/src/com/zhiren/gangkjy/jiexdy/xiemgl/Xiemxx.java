package com.zhiren.gangkjy.jiexdy.xiemgl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.CustomMaths;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumcbBean;
import com.zhiren.dtrlgs.shoumgl.shoumgs.ShoumzgInfo;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xiemxx extends BasePage implements PageValidateListener {
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
	}

	protected void initialize() {
		super.initialize();
		setMsg("");
	}

	// 页面刷新日期（卸煤日期）
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		this.riq = riq;
	}
//	 页面刷新日期（卸煤日期）
	private String riqe;

	public String getRiqe() {
		return riqe;
	}

	public void setRiqe(String riqe) {
		this.riqe = riqe;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
//	与电厂树有关
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}
	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
	}

	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getTreeScript() {
		return getTree().getWindowTreeScript();
	}
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _DetailClick = false;

	public void DetailButton(IRequestCycle cycle) {
		_DetailClick = true;
	}

	private boolean _XiemxxClick = false;

	public void XiemxxButton(IRequestCycle cycle) {
		_XiemxxClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_SaveClick) {
			_SaveClick = false;
			save();
			initGrid();
		}
		if (_DetailClick) {
			_DetailClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			cycle.activate("Chepghxx");
		}
		if (_XiemxxClick) {
			_XiemxxClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			((Visit) getPage().getVisit()).setString2(((Visit) getPage().getVisit()).getActivePageName());
			cycle.activate("Duimxx");
		}
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 卸煤日期的ora字符串格式
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String endxmrqOra= DateUtil.FormatOracleDate(getRiqe());
		// 电厂ID
		
//		long dcid = visit.getDiancxxb_id();
		String dcid="";
		if(this.getDiancTreeJib()==2){
		    dcid="and di.fuid="+this.getTreeid(); 
		}else if(this.getDiancTreeJib()==3){
			  dcid="and di.id ="+this.getTreeid();
		}
		String sql ="select f.id,\n" +
			"		di.mingc as dianc," +
			"       g.mingc fahr,\n" +
			"		m.mingc meikdw, \n" +
			"		j.mingc jihkj, \n" + 
			"       p.mingc pinz,\n" + 
			"       c.mingc faz,\n" + 
			"		dz.mingc as daoz,\n"+
			"       f.chec,\n" + 
			"		ys.mingc as yunsfs,\n"+
			"       (select distinct hetbh from hetb where id=  f.hetb_id) as hetb_id,\n"+
			"       decode(d.fahb_id,null,'未入库','已入库') as zhuangt,\n" + 
			"       f.ches,\n" + 
			"       f.biaoz,\n" + 
			"       f.maoz,\n" + 
			"       f.piz,\n" + 
//			"       case when f.maoz-f.piz<0 then 0 else f.maoz-f.piz end  as jingz,\n" + 
			"		f.jingz as jingz, \n" +
			"		f.yingk as yingk, \n"+
			"       f.fahrq,\n" + 
			"       f.daohrq jiexrq,\n" + 
			"       to_char(f.daohrq, 'hh24') jiexs,\n" + 
			"       to_char(f.daohrq, 'mi') jiexf,\n" + 
			"       f.guohsj guohrq,\n" + 
			"       to_char(f.guohsj, 'hh24') guohs,\n" + 
			"       to_char(f.guohsj, 'mi') guohf," +
			"		f.zhilb_id \n" + 
			"  from fahb f, vwfahr g, vwpinz p, vwchez c,(select distinct fahb_id from duimxxb) d," +
			"	(select id,mingc from yunsfsb) ys\n" +
			"   ,diancxxb di, meikxxb m , jihkjb j, chezxxb dz "+
			" where f.gongysb_id = g.id and dz.id=f.daoz_id \n" + 
			"   and f.pinzb_id = p.id and f.yewlxb_id<>3 \n" + 
			"   and f.meikxxb_id=m.id  \n" +
			"   and f.jihkjb_id=j.id  \n"+
			"   and f.faz_id = c.id\n" + 
			"   and f.id=d.fahb_id(+)\n" +
			"	and f.yunsfsb_id=ys.id(+)\n" +
			"   and f.diancxxb_id=di.id\n"+
			"   "+dcid+   
            "   and f.daohrq >= "+strxmrqOra+" and f.daohrq < "+endxmrqOra+"+1 order by daohrq";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid为单选
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置grid列标题
		egu.getColumn("fahr").setHeader(Local.fahr);
		egu.getColumn("dianc").setHeader(Local.diancmc);
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("jihkj").setHeader("计划口径");
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("faz").setHeader(Local.faz);
		egu.getColumn("daoz").setHeader(Local.daoz);
		egu.getColumn("chec").setHeader(Local.chec);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("maoz").setHeader(Local.maoz);
		egu.getColumn("piz").setHeader(Local.piz);
		egu.getColumn("fahrq").setHeader(Local.fahrq);
		egu.getColumn("jiexrq").setHeader(Local.jiexrq);
		egu.getColumn("jiexs").setHeader(Local.shi);
		egu.getColumn("jiexf").setHeader(Local.fen);
		egu.getColumn("guohrq").setHeader(Local.guohrq);
		egu.getColumn("guohs").setHeader(Local.shi);
		egu.getColumn("guohf").setHeader(Local.fen);
		egu.getColumn("jingz").setHeader(Local.jingz);
		egu.getColumn("yingk").setHeader(Local.yingk);
		egu.getColumn("hetb_id").setHeader("合同号");
		egu.getColumn("zhuangt").setHeader("入库状态");
		egu.getColumn("yunsfs").setHeader("运输方式");
		egu.getColumn("zhilb_id").setHeader("质量信息");
		
		// 设置grid列宽度
		egu.getColumn("dianc").setWidth(100);
		egu.getColumn("fahr").setWidth(100);
		egu.getColumn("meikdw").setWidth(100);
		egu.getColumn("jihkj").setWidth(80);
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("daoz").setWidth(60);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("maoz").setWidth(60);
		egu.getColumn("piz").setWidth(60);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("jiexrq").setWidth(80);
		egu.getColumn("jiexs").setWidth(40);
		egu.getColumn("jiexf").setWidth(40);
		egu.getColumn("guohrq").setWidth(80);
		egu.getColumn("guohs").setWidth(40);
		egu.getColumn("guohf").setWidth(40);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("hetb_id").setWidth(120);
		egu.getColumn("zhuangt").setWidth(60);
		egu.getColumn("yunsfs").setWidth(60);
		egu.getColumn("zhilb_id").setWidth(50);
		//设置编辑状态
		egu.getColumn("dianc").setEditor(null);
		egu.getColumn("zhuangt").setEditor(null);
//		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("meikdw").editor.setAllowBlank(false);
		egu.getColumn("jihkj").editor.setAllowBlank(false);
		egu.getColumn("hetb_id").editor.setAllowBlank(false);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("maoz").setHidden(true);
		egu.getColumn("piz").setHidden(true);
		egu.getColumn("biaoz").editor.setAllowBlank(false);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("zhilb_id").setHidden(true);
		
		// 设置默认值
		if(getDiancTreeJib()==3){
			egu.getColumn("dianc").setHidden(true);
		}
		egu.getColumn("fahrq").setDefaultValue(getRiq());
		egu.getColumn("jiexrq").setDefaultValue(getRiq());
		egu.getColumn("guohrq").setDefaultValue(getRiq());
		egu.getColumn("jiexs").setDefaultValue("0");
		egu.getColumn("jiexf").setDefaultValue("0");
		egu.getColumn("guohs").setDefaultValue("0");
		egu.getColumn("guohf").setDefaultValue("0");
		egu.getColumn("yunsfs").setDefaultValue("海运");
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("zhilb_id").setDefaultValue("0");
		
		// 数据列下拉框设置
		// 发货人
		ComboBox fahrcb = new ComboBox();
		fahrcb.setEditable(true);
		egu.getColumn("fahr").setEditor(fahrcb);
	
		sql = "select id,mingc from vwfahr order by mingc";
		egu.getColumn("fahr").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		
//		 煤矿单位
		ComboBox meikdw = new ComboBox();
		meikdw.setEditable(true);
		egu.getColumn("meikdw").setEditor(meikdw);
	
		sql = "select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikdw").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
//		 计划口径
		ComboBox jihkj = new ComboBox();
		jihkj.setEditable(true);
		egu.getColumn("jihkj").setEditor(jihkj);
	
		sql = "select id,mingc from jihkjb order by id";
		egu.getColumn("jihkj").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// 品种
		ComboBox pinzcb = new ComboBox();
		pinzcb.setEditable(true);
		egu.getColumn("pinz").setEditor(pinzcb);
	
		sql = "select id,mingc from vwpinz where leib='煤' order by xuh";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// 发站
		ComboBox fazcb = new ComboBox();
		fazcb.setEditable(true);
		egu.getColumn("faz").setEditor(fazcb);
	
		sql = "select id,mingc from vwchez order by mingc";
		egu.getColumn("faz")
				.setComboEditor(egu.gridId, new IDropDownModel(sql));
		// 到站
		ComboBox daozcb = new ComboBox();
		daozcb.setEditable(true);
		egu.getColumn("daoz").setEditor(daozcb);
		
		sql = "select id,mingc from vwchez order by mingc";
		egu.getColumn("daoz")
				.setComboEditor(egu.gridId, new IDropDownModel(sql));
		// 接卸时
		ComboBox jxscb = new ComboBox();
		jxscb.setEditable(true);
		egu.getColumn("jiexs").setEditor(jxscb);
		
		List h = new ArrayList();
		for (int i = 0; i < 24; i++)
			h.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexs")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		// 接卸分
		ComboBox jxfcb = new ComboBox();
		jxfcb.setEditable(true);
		egu.getColumn("jiexf").setEditor(jxfcb);
	
		List m = new ArrayList();
		for (int i = 0; i < 60; i++)
			m.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexf")
				.setComboEditor(egu.gridId, new IDropDownModel(m));
		// 过衡时
		ComboBox ghscb = new ComboBox();
		ghscb.setEditable(true);
		egu.getColumn("guohs").setEditor(ghscb);
	
		List gh = new ArrayList();
		for (int i = 0; i < 24; i++)
			gh.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohs").setComboEditor(egu.gridId,
				new IDropDownModel(gh));
		// 过衡分
		ComboBox ghfcb = new ComboBox();
		ghfcb.setEditable(true);
		egu.getColumn("guohf").setEditor(ghfcb);
	
		List gm = new ArrayList();
		for (int i = 0; i < 60; i++)
			gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohf").setComboEditor(egu.gridId,
				new IDropDownModel(gm));
		
//		设置运输方式下拉框
		ComboBox c7 = new ComboBox();
		c7.setEditable(true);
		egu.getColumn("yunsfs").setEditor(c7);
		egu.getColumn("yunsfs").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb"));
		egu.getColumn("yunsfs").setReturnId(true);
		egu.getColumn("yunsfs").editor.setAllowBlank(false);
//      合同编号
		ComboBox  hetbh =new ComboBox();
		hetbh.setEditable(true);
		egu.getColumn("hetb_id").setEditor(hetbh);
		egu.getColumn("hetb_id").setComboEditor(egu.gridId, new IDropDownModel(" select id, hetbh from hetb h where leib=2  and  not(h.qisrq>"+endxmrqOra+" or h.guoqrq<"+strxmrqOra+")"));
		egu.getColumn("hetb_id").setReturnId(true);
		
		
//		设定不可编辑列的颜色
		egu.getColumn("zhuangt").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		// 设置grid可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		// 接卸日期选择
		egu.addTbarText(Local.jiexrq);
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());

//		接卸日期范围2
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "");// 与html页中的id绑定,并自动刷新
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
//		设置电厂树
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
	   // System.out.println(etu.getWindowTreeScript());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		// 添加按钮
	String condition=" if(diancTree_text.value=='大唐燃料公司'||document.getElementById('diancTree_id').value=='199'){Ext.MessageBox.alert('提示信息','请选择子厂进行添加');return;} \n";
		GridButton insert = new GridButton(GridButton.ButtonType_Insert_condition,
				"gridDiv", egu.getGridColumns(), "",condition);
		egu.addTbarBtn(insert);
		// 删除按钮
		String insertcondition="var rec=gridDiv_sm.getSelected();if(rec.get('ZHUANGT')=='已入库'){Ext.MessageBox.alert('提示信息','已入库数据不能删除');return;}\n" 
				+"else if(eval(rec.get('ZHILB_ID'))>0){Ext.MessageBox.alert('提示信息','请先删除采样和化验信息！');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",insertcondition);
		egu.addTbarBtn(delete);
		// 保存按钮
		GridButton save = new GridButton(GridButton.ButtonType_Save, "gridDiv",
				egu.getGridColumns(), "SaveButton");
		egu.addTbarBtn(save);
		// 详细过衡按钮
		GridButton xiem = new GridButton("入库操作", "Xiem");
		xiem.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(xiem);
//		// 详细过衡按钮
//		GridButton Detail = new GridButton("详细过衡", "Detail");
//		Detail.setIcon(SysConstant.Btn_Icon_Copy);
//		egu.addTbarBtn(Detail);
		
		StringBuffer sbJingz = new StringBuffer();
		sbJingz.append("gridDiv_grid.on('afteredit',function(e){ ChangeChk='是';");
//		sbJingz.append("if(e.field == 'MAOZ' || e.field=='PIZ')" +
//				"{if(eval(e.record.get('MAOZ'))>eval(e.record.get('PIZ'))){e.record.set('JINGZ',parseFloat(e.record.get('MAOZ'))" +
//				" - parseFloat(e.record.get('PIZ')));}else{e.record.set('JINGZ',0);}}");
		sbJingz.append("})");
		sbJingz.append("\n gridDiv_grid.on('beforeedit',function(e){");
		sbJingz.append("if(e.record.get('ZHUANGT')=='已入库' || eval(e.record.get('ZHILB_ID'))>0){e.cancel=true;}})");//当入库状态列的值是"已入库"时,这一行不允许编辑
		egu.addOtherScript(sbJingz.toString());
		
		setExtGrid(egu);
		con.Close();

	}

	private void save() {
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String sql;
		int flag;
		// 删除数据
		ResultSetList rs = getExtGrid().getDeleteResultSet(getChange());
		sql = "begin \n";
		while (rs.next()) {
			sql += "delete from chepb where fahb_id=" + rs.getString("id")
					+ ";\n";
			sql += "delete from fahb where id =" + rs.getString("id") + ";\n";
		}
		sql += "end;\n";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.DeleteDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				con.rollBack();
				return;
			}
		}
		rs.close();
		// 修改数据
		rs = getExtGrid().getModifyResultSet(getChange());
		sql = "begin \n";
		List fahb_idList=new ArrayList();
		while (rs.next()) {
			long id = rs.getLong("id");
			long gongysb_id = getExtGrid().getColumn("fahr").combo.getBeanId(rs
					.getString("fahr"));
			long meikxxb_id = getExtGrid().getColumn("meikdw").combo.getBeanId(rs
					.getString("meikdw"));
			long jihkjb_id = getExtGrid().getColumn("jihkj").combo.getBeanId(rs
					.getString("jihkj"));
			long pinzb_id = getExtGrid().getColumn("pinz").combo.getBeanId(rs
					.getString("pinz"));
			long faz_id = getExtGrid().getColumn("faz").combo.getBeanId(rs
					.getString("faz"));
			long daoz_id = getExtGrid().getColumn("daoz").combo.getBeanId(rs
					.getString("daoz"));
			long  hetb_id  =getExtGrid().getColumn("hetb_id").combo.getBeanId(rs.getString("hetb_id"));
			String fahrq = DateUtil.FormatOracleDate(rs.getString("fahrq"));

			String strriq = rs.getString("jiexrq") + " "
					+ rs.getString("jiexs") + ":" + rs.getString("jiexf")
					+ ":00";
			String daohrq = DateUtil.FormatOracleDateTime(strriq);
			strriq = rs.getString("guohrq") + " " + rs.getString("guohs") + ":"
					+ rs.getString("guohf") + ":00";
			String guohsj = DateUtil.FormatOracleDateTime(strriq);
			String chec = rs.getString("chec");
			int ches = rs.getInt("ches");
			double maoz = rs.getDouble("maoz");
			double piz = rs.getDouble("piz");
			double biaoz = rs.getDouble("biaoz");
			double jingz = rs.getDouble("jingz");//CustomMaths.sub(maoz, piz);
			double yunsl = getYunsl(visit.getDiancxxb_id(), pinzb_id, SysConstant.YUNSFS_HUOY);
//			double yingd = 0.0;
//			double kuid = 0.0;
			double yingk = rs.getDouble("yingk");//CustomMaths.sub(jingz, biaoz);
			double yuns = 0.0;//CustomMaths.mul(biaoz, yunsl);
			String yunsfs=getExtGrid().getValueSql(getExtGrid().getColumn("yunsfs"),
					rs.getString("yunsfs"));
			
// 			计算盈亏运损
//			if (yingk >= 0) {
//				yuns = 0;
//				yingd = yingk;
//				kuid = 0;
//			} else {
//				if (Math.abs(yingk) <= yuns) {
//					yuns = Math.abs(yingk);
//					yingd = 0;
//					kuid = 0;
//				} else {
//					// Yuns = Yuns;
//					yingd = 0;
//					kuid = CustomMaths.sub(Math.abs(yingk), yuns);
//				}
//			}
//			yingk = yingd - kuid;
			
			if (id == 0) {
		
				String dcid=this.getTreeid();
			
				String newId = MainGlobal.getNewID(visit.getDiancxxb_id());
				fahb_idList.add(new ShoumcbBean(new Long(dcid).longValue(),new Long(newId).longValue()));
				sql += "insert into fahb(id,diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,faz_id,daoz_id,"
						+ "fahrq,daohrq,guohsj,chec,ches,maoz,piz,biaoz,jingz,"
						+ "yingk," +
//								"yingd," +
								"yuns,yunsl,yunsfsb_id,yewlxb_id,lie_id,hetb_id,neibcg) values("
						+ newId
						+ ","
						
						+ dcid
						
						+ ","
						+ gongysb_id
						+ ","
						+ meikxxb_id
						+ ","
						+ jihkjb_id
						+ ","
						+ pinzb_id
						+ ","
						+ faz_id
						+ ","
						+ daoz_id
						+ ","
						+ fahrq
						+ ","
						+ daohrq
						+ ","
						+ guohsj
						+ ",'"
						+ chec
						+ "',"
						+ ches
						+ ","
						+ maoz
						+ ","
						+ piz
						+ ", "
						+ biaoz
						+ ","
						+ jingz
						+ ","
						+ yingk
//						+ ","
//						+ yingd
						+ ","
						+ yuns
						+ ","
						+ yunsl+ ","+ yunsfs + ",2,to_number(199||"+newId+ "),"+hetb_id+",(select neibcg from gongysb where id="+gongysb_id+"));\n";
			} else {
				sql += "update fahb set \n" + " gongysb_id = " + gongysb_id
						+ ",\n" + " pinzb_id = " + pinzb_id + ",\n"
						+ " faz_id = " + faz_id + ",\n" + " daoz_id = "
						+ daoz_id + ",\n" + " fahrq = " + fahrq + ",\n"
						+ " daohrq = " + daohrq + ",\n" + " guohsj = " + guohsj
						+ ",\n" + " chec = '" + chec + "',\n" + " ches = "
						+ ches + ",\n" + " maoz = " + maoz + ",\n" + " piz = "
						+ piz + ",\n" + " biaoz = " + biaoz + ",\n"
						+ " jingz = " + jingz + ",\n" + " yingk = " + yingk	+ ",\n" +
//						" yingd = " + yingd + ",\n" + 
						" yuns = "+ yuns + ",\n" + " yunsl = " + yunsl + ",\n"+
						"yunsfsb_id="+yunsfs+", meikxxb_id="+meikxxb_id+",\n" +
						"jihkjb_id="+jihkjb_id+",hetb_id="+hetb_id+"\n"
						+",neibcg=(select neibcg from gongysb where id="+gongysb_id+")"
						+ " where id=" + id + ";\n";
			}

		}
		sql += "end;";
		if (rs.getRows() > 0) {
			flag = con.getUpdate(sql);
			if (flag == -1) {
				WriteLog.writeErrorLog(this.getClass().getName() + "\n"
						+ ErrorMessage.UpdateDatabaseFail + "\nSQL:" + sql);
				setMsg(ErrorMessage.UpdateDatabaseFail);
				con.rollBack();
				return;
			}else{
				//收煤估收插入数据
				con.commit();
				ShoumzgInfo.CountChengb(fahb_idList,true);
			}
		}
		rs.close();
		con.Close();
		setMsg(ErrorMessage.SaveSuccessMessage);
	}

	public static double getYunsl(long diancxxb_id, long pinzb_id,
			int yunsfsb_id) {
		JDBCcon con = new JDBCcon();
		String sql = "";
		ResultSetList rsl;
		double yunsl = 0.012;
		String fs = "";
		if (yunsfsb_id == SysConstant.YUNSFS_HUOY) {
			yunsl = 0.012;
			fs = "火车";
		} else if (yunsfsb_id == SysConstant.YUNSFS_QIY) {
			yunsl = 0.01;
			fs = "汽车";
		}
		sql = "select * from xitxxb where mingc ='默认运损率' and danw ='"
				+ fs
				+ "' and leib = '数量' and beiz ='使用' and zhuangt =1 and diancxxb_id="
				+ diancxxb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			yunsl = rsl.getDouble("zhi");
		}
		sql = "select yunsl from yunslb where diancxxb_id=" + diancxxb_id
				+ " and pinzb_id=" + pinzb_id + " and yunsfsb_id ="
				+ yunsfsb_id;
		rsl = con.getResultSetList(sql);
		if (rsl.next()) {
			yunsl = rsl.getDouble("yunsl");
		}
		return yunsl;
	}

	private void init() {
		
		setExtGrid(null);
		
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
							
			}
			if(getRiqe()==null){
				this.setRiqe(DateUtil.FormatDate(new Date()));
			}
			if(!(visit.getActivePageName().toString().equals("Duimxx")||visit.getActivePageName().toString().equals("Chepghxx"))){
				this.setTreeid("");
			}
			
		
			visit.setActivePageName(getPageName().toString());
			init();
		}
		visit.setString10("");
		visit.setString2("");
		initGrid();
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
	
	private	class IGridButton extends GridButton{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4609793083358356414L;
		public IGridButton(int btnType,String parentId,List columns,String tapestryBtnId,String condition){
			super( btnType, parentId, columns, tapestryBtnId,condition);
		}
		public String getDeleteScript() {
			StringBuffer record = new StringBuffer();
			record.append("function() {\n");
			record.append( super.condition+"\n");
			record.append("for(i=0;i<"+parentId+"_sm.getSelections().length;i++){\n");
			record.append("	record = "+parentId+"_sm.getSelections()[i];\n");
			
			StringBuffer sb = new StringBuffer();
			//sb.append(b);
			sb.append(parentId).append("_history += '<result>' ")
			.append("+ '<sign>D</sign>' ");
			
			for(int c=0;c<columns.size();c++) {
				if(((GridColumn)columns.get(c)).coltype == GridColumn.ColType_default) {
					GridColumn gc = ((GridColumn)columns.get(c));
					if(gc.update) {
						if("date".equals(gc.datatype)) {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ('object' != typeof(").append("record.get('")
							.append(gc.dataIndex).append("'))?").append("record.get('")
							.append(gc.dataIndex).append("'):").append("record.get('")
							.append(gc.dataIndex).append("').dateFormat('Y-m-d'))")
							.append("+ '</").append(gc.dataIndex).append(">'\n");
						}else {
							sb.append("+ '<"+gc.dataIndex+" update=\""+gc.update+"\">' + ").append("record.get('").append(gc.dataIndex).append("')");
							if(!gc.datatype.equals(GridColumn.DataType_Float)) {
								sb.append(".replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')");
							}
							sb.append("+ '</").append(gc.dataIndex).append(">'\n");
						}
					}
				}
			}
			sb.append(" + '</result>' ;");
			record.append(sb);
			
			record.append("	"+parentId+"_ds.remove("+parentId+"_sm.getSelections()[i--]);}}");
			return record.toString();
		}
	}
	
}