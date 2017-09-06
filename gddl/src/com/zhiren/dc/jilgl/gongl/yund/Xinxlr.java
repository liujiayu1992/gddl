package com.zhiren.dc.jilgl.gongl.yund;

import java.util.Date;

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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xinxlr extends BasePage implements PageValidateListener {
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
		setTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	//绑定日期
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

	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
		}
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Xinxlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer SQL = new StringBuffer();
		//插入车皮临时表
		SQL.append("begin\n");
		double biaoz = 0.0;
		while(rsl.next()){
			long diancxxb_id = 0;
			if(visit.isFencb()) {
				diancxxb_id = (visit.getExtGrid1().getColumn("diancxxb_id").combo).getBeanId(rsl.getString("diancxxb_id"));
			}else {
				diancxxb_id = visit.getDiancxxb_id();
			}
			biaoz += rsl.getDouble("biaoz");
			SQL.append("insert into cheplsb\n"); 
            SQL.append("(id, diancxxb_id, gongysb_id, meikxxb_id, pinzb_id, faz_id, daoz_id, jihkjb_id, fahrq, daohrq, yunsfsb_id, chec,cheph, biaoz, chebb_id, yuandz_id, yuanshdwb_id,  yuanmkdw, daozch, lury,xiecfsb_id, beiz,caiyrq)\n");
            SQL.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id);
            SQL.append(",").append(((IDropDownModel)getGongysModel())
					.getBeanId(rsl.getString("gongysb_id")));
            SQL.append(",").append(((IDropDownModel)getMeikModel())
					.getBeanId(rsl.getString("meikxxb_id")));
            SQL.append(",").append((visit.getExtGrid1().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
            SQL.append(",1,1,").append((visit.getExtGrid1().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
            SQL.append(",to_date('").append(rsl.getString("fahrq")).append("','yyyy-mm-dd')");
            SQL.append(",to_date('2050-12-31','yyyy-mm-dd')");
            SQL.append(",2,'").append(rsl.getString("chec"));
            SQL.append("','").append(rsl.getString("cheph")).append("'");
            SQL.append(",").append(rsl.getDouble("biaoz"));
            SQL.append(","+SysConstant.CHEB_QC+",1,").append((visit.getExtGrid1().getColumn("yuanshdwb_id").combo).getBeanId(rsl.getString("yuanshdwb_id")));
            SQL.append(",'").append(rsl.getString("yuanmkdw"));
            SQL.append("','").append(rsl.getString("daozch"));
            SQL.append("','").append(visit.getRenymc());
            Visit v=(Visit)this.getPage().getVisit();
			SQL.append("',").append("(select id from xiecfsb where mingc='"+rsl.getString("xiecfsb_id")+"' "+Jilcz.filterDcid(v,"xiecfsb")+")");
            SQL.append(",'").append(rsl.getString("beiz")).append("'");
            SQL.append(",to_date('").append(rsl.getString("caiyrq")).append("','yyyy-mm-dd')");
            SQL.append(");\n");
		}
		SQL.append("end;");
		int flag = con.getInsert(SQL.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+SQL);
			setMsg(ErrorMessage.Xinxlr001);
			return;
		}
		flag = Jilcz.Updatezlid(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, null);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr002);
			setMsg(ErrorMessage.Xinxlr002);
			return;
		}
		flag = Jilcz.INSorUpfahb(con, visit.getDiancxxb_id());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr003);
			setMsg(ErrorMessage.Xinxlr003);
			return;
		}
		flag = Jilcz.InsChepb(con, visit.getDiancxxb_id(), SysConstant.YUNSFS_QIY, SysConstant.HEDBZ_TJ);
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr004);
			setMsg(ErrorMessage.Xinxlr004);
			return;
		}
		SQL.delete(0, SQL.length());
		SQL.append("select distinct fahb_id from cheplsb");
		rsl = con.getResultSetList(SQL.toString());
		if (rsl == null) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.Xinxlr005);
			setMsg(ErrorMessage.Xinxlr005);
			return;
		}
		while (rsl.next()) {
			flag = Jilcz.updateLieid(con, rsl.getString("fahb_id"));
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Xinxlr006);
				setMsg(ErrorMessage.Xinxlr006);
				return;
			}
		}
		con.commit();  
		con.Close();
		setMsg("您保存了 "+rsl.getRows()+" 车的信息,共计票重 "+biaoz+" 吨。");
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
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id, c.diancxxb_id, c.cheph, c.biaoz, '' gongysb_id, '' meikxxb_id, \n");
		sb.append("'' as faz_id, '' as pinzb_id, '' as jihkjb_id, c.fahrq as fahrq,\n");
		sb.append("c.caiyrq as caiyrq, c.chec, '' as chebb_id, ")
				.append("(select mingc from xiecfsb xi where xi.id=c.xiecfsb_id) xiecfsb_id")
				.append(",'' as daoz_id, \n");
		sb.append("'' as yuandz_id, '' as yuanshdwb_id, c.yuanmkdw, c.daozch")
		   .append(",c.beiz\n");
		sb.append("from cheplsb c \n");
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("cheplsb");
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		if(visit.isFencb()) {
			ComboBox dc= new ComboBox();
			egu.getColumn("diancxxb_id").setEditor(dc);
			dc.setEditable(true);
			String dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
		}else {
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(65);
		egu.getColumn("cheph").editor.setListeners("specialkey:function(own,e){ gridDiv_grid.startEditing(row , 4); }");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(60);
		StringBuffer lins = new StringBuffer();
		lins.append("specialkey:function(own,e){ \n")
		.append("if(row+1 == gridDiv_grid.getStore().getCount()){ \n")
		.append("Ext.MessageBox.alert('提示信息','已到达数据末尾！');return; \n")
		.append("} \n").append("row = row+1; \n")
		.append("last = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("gridDiv_grid.getSelectionModel().selectRow(row); \n")
		.append("cur = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("copylastrec(last,cur); \n")
		.append("gridDiv_grid.startEditing(row , 3); }");
		
		egu.getColumn("biaoz").editor.setListeners(lins.toString());
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(110);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("faz_id").setDefaultValue("1");
		egu.getColumn("faz_id").setHidden(true);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(40);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("chebb_id").setDefaultValue("3");
		egu.getColumn("chebb_id").setHidden(true);
		egu.getColumn("chebb_id").setEditor(null);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(50);
		egu.getColumn("daoz_id").setDefaultValue("1");
		egu.getColumn("daoz_id").setHidden(true);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("yuandz_id").setHidden(true);
		egu.getColumn("yuandz_id").setDefaultValue("1");
		egu.getColumn("yuandz_id").setEditor(null);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(90);
		egu.getColumn("yuanmkdw").setHeader(Locale.yuanmkdw_chepb);
		egu.getColumn("yuanmkdw").setWidth(90);
		egu.getColumn("daozch").setHeader(Locale.daozch_chepb);
		egu.getColumn("daozch").setWidth(70);
		egu.getColumn("xiecfsb_id").setHeader("卸车方式");
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(80);
		//设置供应商下拉框
		/*ComboBox c1= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(c1);
		c1.setEditable(true);
		String gysSql="select id,mingc from gongysb order by mingc";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gysSql));
		//设置煤矿单位下拉框
		ComboBox c2= new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(c2);
		c2.setEditable(true);
		c2.setListeners("select:function(own,rec,index){gridDiv_grid.getSelectionModel().getSelected().set('YUANMKDW',own.getRawValue())}");
		String mkSql="select id,mingc from meikxxb order by mingc";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(mkSql));
        *///设置发货日期和到货日期的默认值
		egu.getColumn("fahrq").setDefaultValue(this.getRiq());
		egu.getColumn("caiyrq").setDefaultValue(this.getRiq());
		//设置品种下拉框
		ComboBox c5=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzSql));
		//设置口径下拉框
		ComboBox c6=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
		//设置卸车方式下拉框
		ComboBox c11=new ComboBox();
		   Visit v=(Visit)this.getPage().getVisit();
		   String XiecfsSql="select id,mingc from xiecfsb where "+Jilcz.filterDcid(v,null).substring(4)+" order by id";
		   c11.setEditable(true);
		   egu.getColumn("xiecfsb_id").setEditor(c11);
		   egu.getColumn("xiecfsb_id").setComboEditor(egu.gridId,new IDropDownModel(XiecfsSql));
		//设置原收货单位下拉框
		ComboBox c9=new ComboBox();
		egu.getColumn("yuanshdwb_id").setEditor(c9);
		c9.setEditable(true);//设置可输入
		String Sql="select id,mingc from diancxxb order by mingc";
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
		egu.getColumn("yuanshdwb_id").setDefaultValue(""+((Visit) getPage().getVisit()).getDiancmc());
	    egu.addToolbarButton(GridButton.ButtonType_Inserts, null);
//	    egu.addToolbarButton(GridButton.ButtonType_Copy, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+visit.getDiancxxb_id(),null,null,null);
		Toolbar bbar = dt.getTree().getTreePanel().getBbar();
		bbar.deleteItem();
		StringBuffer handler = new StringBuffer();
		handler.append("function() { \n")
		.append("var cks = gongysTree_treePanel.getSelectionModel().getSelectedNode(); \n")
		.append("if(cks==null){gongysTree_window.hide();return;} \n")
		.append("if(cks.getDepth() < 3){ \n")
		.append("Ext.MessageBox.alert('提示信息','请选择对应的计划口径！');")
		.append("return; } \n")
		.append("rec = gridDiv_grid.getSelectionModel().getSelected(); \n")
		.append("rec.set('GONGYSB_ID', cks.parentNode.parentNode.text); \n")
		.append("rec.set('MEIKXXB_ID', cks.parentNode.text); \n")
		.append("rec.set('YUANMKDW', cks.parentNode.text); rec.set('JIHKJB_ID', cks.text); \n")
		.append("gongysTree_window.hide(); \n")
		.append("return;")
		.append("}");
		ToolbarButton btn = new ToolbarButton(null, "确认", handler.toString());
		bbar.addItem(btn);
		visit.setDefaultTree(dt);
		con.Close();
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setRiq(DateUtil.FormatDate(new Date()));
			setTbmsg(null);
			getSelectData();
		}
	}
}