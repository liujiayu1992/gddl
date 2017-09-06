package com.zhiren.dtrlgs.shoumgl.shulgl;


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
import org.apache.tapestry.form.IPropertySelectionModel;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Caigslrk extends BasePage implements PageValidateListener {
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

	// 页面变化记录
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
	
//	gridInwidow
	public ExtGridUtil getExtGridInWindow() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setExtGridInWindow(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridInWindowScript() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getGridScript();
	}

	public String getGridInWindowHtml() {
		if (getExtGridInWindow() == null) {
			return "";
		}
		return getExtGridInWindow().getHtml();
	}
	//与电厂树有关
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
	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}

	private boolean _rukczButtonClick = false;

	public void rukczButton(IRequestCycle cycle) {
		_rukczButtonClick = true;
	}
/*
	private boolean _DetailClick = false;

	public void DetailButton(IRequestCycle cycle) {
		_DetailClick = true;
	}

	private boolean _XiemxxClick = false;

	public void XiemxxButton(IRequestCycle cycle) {
		_XiemxxClick = true;
	}
*/
	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			initGrid();
		}
		if (_rukczButtonClick) {
			_rukczButtonClick = false;
			Visit visit=(Visit)this.getPage().getVisit();
			visit.setString2(visit.getActivePageName());
			((Visit) getPage().getVisit()).setString10(getChange());
			 cycle.activate("Duimxx");
		//	initGrid();
		}
		/*
		if (_DetailClick) {
			_DetailClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			cycle.activate("Chepghxx");
		}
		if (_XiemxxClick) {
			_XiemxxClick = false;
			((Visit) getPage().getVisit()).setString10(getChange());
			cycle.activate("Duimxx");
		}
		*/
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		// 卸煤日期的ora字符串格式
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		// 电厂ID
//		long dcid = visit.getDiancxxb_id();
		
		String sql = "select f.id,\n" 
			    + "      (select mingc from diancxxb where id= f.diancxxb_id ) fahr,\n"
		        +"g.mingc gongys,\n"
		        +" (select mingc from meikxxb where id=f.meikxxb_id)meikxxb_id,\n"
				+ "       p.mingc pinz,\n" 
				+ "       c.mingc faz,\n"
				+"         (select mingc from vwchez where id=f.daoz_id)daoz,\n"
				+"(select mingc from diancxxb where id=f.YUANSHDWB_ID) shouhr,\n"
				+ "       f.chec,\n" 
				+"       (select mingc from yunsfsb where id=f.yunsfsb_id)yunsfsb_id ,"
				+"        f.chuanm,"
				+"(select case when count(*)>0  then '已入库' else '未入库' end  from duimxxb where fahb_id=f.fhfy_id)as rukzt,"
				+ "       f.ches,\n" + "       f.biaoz,\n"
				+ "       f.maoz,\n" + "       f.piz,\n"
				+"        f.jingz,"
				+"        f.yuns,"
				+"        f.yingk,"
				+ "       f.fahrq,\n"
				+ "       f.daohrq jiexrq,\n"
				+ "       to_char(f.daohrq, 'hh24') jiexs,\n"
				+ "       to_char(f.daohrq, 'mi') jiexf,\n"
				+ "       f.guohsj guohrq,\n"
				+ "       to_char(f.guohsj, 'hh24') guohs,\n"
				+ "       to_char(f.guohsj, 'mi') guohf,\n"
				+"        f.fhfy_id,"
				+"        (select hetbh from hetb where id=f.hetb_id)hetb_id,"
				+"       (select mingc from yewlxb where id=f.leix_id) leix_id"
				
				+ "  from fahbtmp f, vwfahr g, vwpinz p, vwchez c\n"
				+ " where f.gongysb_id = g.id\n" + "   and f.pinzb_id = p.id\n"
				+ "   and f.faz_id = c.id\n"// + "	and f.diancxxb_id = " + dcid
				+ "   and f.daohrq >= " + strxmrqOra + " and f.daohrq < "
				+ strxmrqOra + "+1\n"
				+"and f.leix_id=1\n";//只查采购的
//				if(!this.getTreeid().equals("199")){
		        sql+="and f.diancxxb_id="+this.getTreeid()+"\n";
//	             }      
		  sql+=" and f.fhfy_id!=-2 and f.shujly='手工录入'\n";//此页面只处理手工录入的数据和排除作废的数据

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// 设置grid可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		// egu.setHeight("bodyHeight");
		// 设置数据不分页
		egu.addPaging(0);
		// 设置grid为单选
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);
		// 设置grid列标题
		egu.getColumn("fahr").setHeader(Local.fahr);
		egu.getColumn("gongys").setHeader("供应商");
		egu.getColumn("meikxxb_id").setHeader("煤矿名");
		egu.getColumn("pinz").setHeader(Local.pinz);
		egu.getColumn("faz").setHeader(Local.faz);
		egu.getColumn("daoz").setHeader(Local.daoz);
		egu.getColumn("shouhr").setHeader("收货人");
		egu.getColumn("chec").setHeader(Local.chec);
		egu.getColumn("ches").setHeader(Local.ches);
		egu.getColumn("biaoz").setHeader(Local.biaoz);
		egu.getColumn("maoz").setHeader(Local.maoz);
		egu.getColumn("piz").setHeader(Local.piz);
		egu.getColumn("fahrq").setHeader(Local.fahrq);
		egu.getColumn("jiexrq").setHeader(Local.daohrq_id_fahb);
		egu.getColumn("jiexs").setHeader(Local.shi);
		egu.getColumn("jiexf").setHeader(Local.fen);
		egu.getColumn("guohrq").setHeader(Local.guohrq);
		egu.getColumn("guohs").setHeader(Local.shi);
		egu.getColumn("guohf").setHeader(Local.fen);
		
		 egu.getColumn("hetb_id").setHeader("合同号");
		 egu.getColumn("jingz").setHeader(Local.jingz);
	      egu.getColumn("yuns").setHeader(Local.yuns);
	       egu.getColumn("yingk").setHeader(Local.yingk);
	       egu.getColumn("leix_id").setHeader("业务类型");
	       egu.getColumn("yunsfsb_id").setHeader("运输方式");
	       egu.getColumn("chuanm").setHeader("船名");
	       egu.getColumn("rukzt").setHeader("入库状态");
		// 设置grid列宽度
		egu.getColumn("fahr").setWidth(100);
		egu.getColumn("gongys").setWidth(80);
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("pinz").setWidth(80);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("daoz").setWidth(80);
		egu.getColumn("shouhr").setWidth(80);
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

		 egu.getColumn("hetb_id").setWidth(80);
		 egu.getColumn("jingz").setWidth(80);
	      egu.getColumn("yuns").setWidth(80);
	       egu.getColumn("yingk").setWidth(80);
	       egu.getColumn("leix_id").setWidth(80);
	       egu.getColumn("yunsfsb_id").setWidth(80);
	       egu.getColumn("chuanm").setWidth(80);
		// 设置默认值
		egu.getColumn("fahrq").setDefaultValue(getRiq());
		egu.getColumn("jiexrq").setDefaultValue(getRiq());
		egu.getColumn("guohrq").setDefaultValue(getRiq());
		egu.getColumn("jiexs").setDefaultValue("0");
		egu.getColumn("jiexf").setDefaultValue("0");
		egu.getColumn("guohs").setDefaultValue("0");
		egu.getColumn("guohf").setDefaultValue("0");
		
		 egu.getColumn("jingz").setDefaultValue("0");
	     egu.getColumn("yuns").setDefaultValue("0");
	     egu.getColumn("yingk").setDefaultValue("0");
	     egu.getColumn("fhfy_id").setDefaultValue("0");
	    
	     //设置不显示
	     egu.getColumn("fhfy_id").setHidden(true);
	     //设置不编辑
	    egu.getColumn("jingz").setEditor(null);
	    egu.getColumn("yingk").setEditor(null);
	    
		// 数据列下拉框设置
	    
	    //发货人
	    ComboBox fahr=new ComboBox();
	    egu.getColumn("fahr").setEditor(fahr);
	     fahr.setEditable(true);
	     String fahrsql="select id,mingc from diancxxb where cangkb_id<>1 and jib=3 ";
	     egu.getColumn("fahr").setComboEditor(egu.gridId, new IDropDownModel(fahrsql));
		// 供应商
		ComboBox gongys = new ComboBox();
		egu.getColumn("gongys").setEditor(gongys);
		gongys.setEditable(true);
		sql = "select id,mingc from vwfahr order by xuh";
		egu.getColumn("gongys").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		//煤矿信息
		ComboBox meikxx=new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(meikxx);
		meikxx.setEditable(true);
		String meikxxsql="select id,mingc from meikxxb";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(meikxxsql));
		// 品种
		ComboBox pinzcb = new ComboBox();
		egu.getColumn("pinz").setEditor(pinzcb);
		pinzcb.setEditable(true);
		sql = "select id,mingc from vwpinz order by xuh";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(sql));
		// 发站
		ComboBox fazcb = new ComboBox();
		egu.getColumn("faz").setEditor(fazcb);
		fazcb.setEditable(true);
		sql = "select id,mingc from vwchez order by xuh";
		egu.getColumn("faz")
				.setComboEditor(egu.gridId, new IDropDownModel(sql));
		//到站
		ComboBox daoz=new ComboBox();
		egu.getColumn("daoz").setEditor(daoz);
		daoz.setEditable(true);
		String daozsql="select id ,mingc from vwchez";
		egu.getColumn("daoz").setComboEditor(egu.gridId, new IDropDownModel(daozsql));
		//收货人
		ComboBox shouhr=new ComboBox();
		egu.getColumn("shouhr").setEditor(shouhr);
		shouhr.setEditable(true);
		String shouhrsql="select id,mingc from diancxxb where cangkb_id=1 and jib=3";
		egu.getColumn("shouhr").setComboEditor(egu.gridId, new IDropDownModel(shouhrsql));
		// 接卸时
		ComboBox jxscb = new ComboBox();
		egu.getColumn("jiexs").setEditor(jxscb);
		jxscb.setEditable(true);
		List h = new ArrayList();
		for (int i = 0; i < 24; i++)
			h.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexs")
				.setComboEditor(egu.gridId, new IDropDownModel(h));
		// 接卸分
		ComboBox jxfcb = new ComboBox();
		egu.getColumn("jiexf").setEditor(jxfcb);
		jxfcb.setEditable(true);
		List m = new ArrayList();
		for (int i = 0; i < 60; i++)
			m.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("jiexf")
				.setComboEditor(egu.gridId, new IDropDownModel(m));
		// 过衡时
		ComboBox ghscb = new ComboBox();
		egu.getColumn("guohs").setEditor(ghscb);
		ghscb.setEditable(true);
		List gh = new ArrayList();
		for (int i = 0; i < 24; i++)
			gh.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohs").setComboEditor(egu.gridId,
				new IDropDownModel(gh));
		// 过衡分
		ComboBox ghfcb = new ComboBox();
		egu.getColumn("guohf").setEditor(ghfcb);
		ghfcb.setEditable(true);
		List gm = new ArrayList();
		for (int i = 0; i < 60; i++)
			gm.add(new IDropDownBean(String.valueOf(i), String.valueOf(i)));
		egu.getColumn("guohf").setComboEditor(egu.gridId,
				new IDropDownModel(gm));
		//合同编号
		ComboBox hetbh=new ComboBox();
		egu.getColumn("hetb_id").setEditor(hetbh);
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,hetbh from hetb where to_date('"+this.getRiq()+"','yyyy-mm-dd')>=qisrq and guoqrq>=to_date('"+this.getRiq()+"','yyyy-mm-dd')"));
         egu.getColumn("hetb_id").setReturnId(true);
     	//业务类型
 		ComboBox yewlx=new ComboBox();
 		yewlx.setId("yewlx_combo");
 		yewlx.setListeners("select:function(combo,record,index) {if(combo.getRawValue()=='场地交割'){caozpd(gridDiv_sm.getSelected());}}");
 		egu.getColumn("leix_id").setEditor(yewlx);
 		egu.getColumn("leix_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from yewlxb"));
 		egu.getColumn("leix_id").setReturnId(true);
 		//yunsfsb_id运输方式
 		ComboBox yunsfs=new ComboBox();
 		egu.getColumn("yunsfsb_id").setEditor(yunsfs);
 		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from yunsfsb"));
 		egu.getColumn("yunsfsb_id").setReturnId(true);
 		//chuanm船名
 		ComboBox chuanm=new ComboBox();
 		egu.getColumn("chuanm").setEditor(chuanm);
 		egu.getColumn("chuanm").setComboEditor(egu.gridId,new IDropDownModel("select id,mingc from luncxxb"));
 		 egu.getColumn("chuanm").editor.setAllowBlank(true);
         // 到货日期选择
    	egu.addTbarText("到货日期");
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
		//设置电厂树
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
	
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		// 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		/*
		// 添加按钮
		GridButton insert = new GridButton(GridButton.ButtonType_Insert,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(insert);
		// 删除按钮
		GridButton delete = new GridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "");
		egu.addTbarBtn(delete);
		// 保存按钮
		String condition="\nvar rgs=gridDiv_ds.getRange();for(var i=0 ;i<rgs.length;i++){if(rgs[i].get('FHFY_ID')==0&&rgs[i].get('LEIX_ID')=='场地交割'){alert('第'+(i+1)+'行未匹配不能保存');return;}}\n";
		GridButton save = new GridButton(GridButton.ButtonType_Save_condition, "gridDiv",
				egu.getGridColumns(), "SaveButton",condition);
		
		egu.addTbarBtn(save);
		*/
		//入库操作
		GridButton getSelect = new GridButton("入库操作","Xiem");
		getSelect.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(getSelect);
		/*
		// 详细过衡按钮
		GridButton xiem = new GridButton("入库操作", "Xiem");
		xiem.setIcon(SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarBtn(xiem);
		// 详细过衡按钮
		GridButton Detail = new GridButton("详细过衡", "Detail");
		Detail.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(Detail);
         */
		

		//egu.addOtherScript("\n  gridDiv_grid.on('afteredit',function(e){var rd=e.record;rd.set('JINGZ',Number(rd.get('MAOZ'))-Number(rd.get('PIZ')));rd.set('YINGK',Number(rd.get('JINGZ'))+Number(rd.get('YUNS'))-Number(rd.get('BIAOZ')));if(e.column==10&&e.value!='海运'){rd.set('CHUANM','')} });");
		setExtGrid(egu);
		con.Close();

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
		setExtGridInWindow(null);
		initGrid();
		
		this.setYewlxModel(null);
		setYewlxValue(null);
	}
//构造业务类型下拉菜单
	public IPropertySelectionModel getYewlxModel(){
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null){
			this.setYewlxModel(this.getYewlxModels());
		}
			return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
		
	}
	public void setYewlxModel(IPropertySelectionModel YewlxModel){
		((Visit)this.getPage().getVisit()).setProSelectionModel1(YewlxModel);
	}
	public IDropDownBean getYewlxValue(){
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null){
		this.setYewlxValue( (IDropDownBean)this.getYewlxModel().getOption(0));
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setYewlxValue(IDropDownBean YewlxValue){
		((Visit)this.getPage().getVisit()).setDropDownBean11(YewlxValue);
	}
	public IPropertySelectionModel getYewlxModels(){
		return new IDropDownModel("select id,mingc from yewlxb");
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			if (getRiq() == null) {
				setRiq(DateUtil.FormatDate(new Date()));
			}
			if(!visit.getActivePageName().toString().equals("Duimxx")){
			this.setTreeid("");
			}
			visit.setActivePageName(getPageName().toString());
			init();
		}
		((Visit)this.getPage().getVisit()).setString2("");
		((Visit)this.getPage().getVisit()).setString10("");
		this.initGrid();
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
}