package com.zhiren.dc.jilgl.gongl.shujdr;

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
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：王伟
 * 时间：2009-03-26
 * 修改内容：
 * 		增加车号模糊查询
 */
/*
 * 作者：王伟
 * 时间：2009-04-01
 * 修改内容：
 * 		修改SQL语句
 * 			biao=maoz-piz-koud
 */
/*
 * 作者：王伟
 * 时间：2009-04-17
 * 修改内容：
 * 		修改SQL语句
 * 			添加备注（beiz）
 */
public class Jianjxxlr extends BasePage implements PageValidateListener {
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

//	 绑定日期
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

//	public String getRiq2() {
//		return ((Visit)this.getPage().getVisit()).getString4();
//	}
//
//	public void setRiq2(String riq2) {
//		((Visit)this.getPage().getVisit()).setString4(riq2);
//	}
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer SQL = new StringBuffer();
		//插入车皮临时表
		SQL.append("begin\n");
		ResultSetList delrsl = v.getExtGrid1().getDeleteResultSet(getChange());
        while (delrsl.next()) {
        	SQL.append("delete from qichzcb where id =")
			.append(delrsl.getString(0)).append(";\n");
          }
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Jianjxxlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
	
		long diancxxb_id = v.getDiancxxb_id();
		while(rsl.next()){
		    if("0".equals(rsl.getString("ID"))){
		    	SQL.append("insert into qichzcb\n"); 
		    	SQL.append("(id, diancxxb_id, gongysb_id, meikxxb_id,jihkjb_id, pinzb_id, yunsdwb_id,maozsj,maoz,pizsj,piz,cheh, beiz,caiyrq,koud,zhuangcdw_item_id,zhuangt)\n");
		    	SQL.append("values (getnewid(").append(diancxxb_id).append("),").append(diancxxb_id);
		    	SQL.append(",").append(((IDropDownModel)getGongysModel())
		    			.getBeanId(rsl.getString("gongysb_id")));
		    	SQL.append(",").append(((IDropDownModel)getMeikModel())
		    			.getBeanId(rsl.getString("meikxxb_id")));
		    	SQL.append(",").append((v.getExtGrid1().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
		    	SQL.append(",").append((v.getExtGrid1().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
		    	SQL.append(",").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
		    	SQL.append(",sysdate,0,sysdate,0,'");
		    	SQL.append(rsl.getString("chet")+rsl.getString("cheh")).append("','");
		    	SQL.append(rsl.getString("beiz")).append("'");
		    	SQL.append(",to_date('").append(rsl.getString("caiyrq")).append("','yyyy-mm-dd')")
		    	.append(",").append(rsl.getString("koud")).append(",").append((getExtGrid().getColumn("zhuangcdw_item_id").combo).getBeanId(rsl.getString("zhuangcdw_item_id")))
		    	.append(",0);\n");
		    }else{
		    	SQL.append("update qichzcb set\n");
		    	SQL.append("gongysb_id=").append(((IDropDownModel)getGongysModel()).getBeanId(rsl.getString("gongysb_id")));
		    	SQL.append(",meikxxb_id=").append(((IDropDownModel)getMeikModel()).getBeanId(rsl.getString("meikxxb_id")));
		    	SQL.append(",jihkjb_id=").append((v.getExtGrid1().getColumn("jihkjb_id").combo).getBeanId(rsl.getString("jihkjb_id")));
		    	SQL.append(",pinzb_id=").append((v.getExtGrid1().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")));
		    	SQL.append(",yunsdwb_id=").append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id")));
		    	SQL.append(",cheh='").append(rsl.getString("chet")+rsl.getString("cheh"));
		    	SQL.append("',beiz='").append(rsl.getString("beiz"));
		    	SQL.append("',koud=").append(rsl.getDouble("koud"));
		    	SQL.append(",zhuangcdw_item_id=").append((getExtGrid().getColumn("zhuangcdw_item_id").combo).getBeanId(rsl.getString("zhuangcdw_item_id"))).append(" where id=").append(rsl.getString("ID")).append(";\n");
		    }
		}
		SQL.append("end;");
		int flag = con.getInsert(SQL.toString());
		if(flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+SQL);
			setMsg("保存失败");
			return;
		}
		con.commit();  
		con.Close();
		setMsg("保存成功");
	}
 
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	/* 修改人：ww
	 * 修改时间：2009-09-03
	 * 修改内容：默认发货日期和到货日期为重车时间
	 */
	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql ="select q.id,\n" + 
		"       g.mingc as gongysb_id,\n" + 
		"       m.mingc as meikxxb_id,\n" + 
		"       j.mingc as jihkjb_id,\n" + 
		"       p.mingc as pinzb_id,\n" + 
		"       substr(q.cheh,1,2) as chet,\n"+
	    "       substr(q.cheh,3,5) as cheh,\n"+
		"       y.mingc as yunsdwb_id,\n" + 
		"       q.koud,\n" + 
		"       to_char(q.caiyrq, 'yyyy-mm-dd') as caiyrq,\n" + 
		"       i.mingc as zhuangcdw_item_id,\n"+
		"       q.beiz\n" + 
		"  from qichzcb q, gongysb g, meikxxb m, pinzb p, jihkjb j, yunsdwb y,item i\n" + 
		" where q.gongysb_id = g.id(+)\n" + 
		"   and q.meikxxb_id = m.id(+)\n" + 
		"   and q.jihkjb_id = j.id(+)\n" + 
		"   and q.pinzb_id = p.id(+)\n" + 
		"   and q.yunsdwb_id = y.id(+)\n" + 
		"   and q.zhuangcdw_item_id=i.id(+)\n"+
		"   and q.diancxxb_id ="+v.getDiancxxb_id()+
		"   and q.zhuangt = 0 and to_char(q.maozsj,'yyyy-mm-dd')= '"+getRiqi()+"'\n"+
		"   order by q.id desc ";
		
		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("qichzcb");
//		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		//设置多选框
//		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
//		暂时不判断分厂别的情况
//		if(v.isFencb()) {
//			ComboBox dc= new ComboBox();
//			egu.getColumn("diancxxb_id").setEditor(dc);
//			dc.setEditable(true);
//			String dcSql="select id,mingc from diancxxb where fuid="+v.getDiancxxb_id();
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
//			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
//			egu.getColumn("diancxxb_id").setWidth(70);
//		}else {
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").editor = null;
//		}
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setWidth(60);
		egu.getColumn("id").editor = null;
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(110);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(100);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(70);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("chet").setHeader("车头");
		egu.getColumn("chet").setWidth(60);
		egu.getColumn("cheh").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheh").setWidth(70);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(90);
		egu.getColumn("koud").setHeader(Locale.koud_chepb);
		egu.getColumn("koud").setWidth(60);
		egu.getColumn("koud").setDefaultValue("0");
		egu.getColumn("caiyrq").setHeader(Locale.caiyrq_caiyb);
		egu.getColumn("caiyrq").setWidth(70);
		egu.getColumn("zhuangcdw_item_id").setHeader("装车单位");
		egu.getColumn("zhuangcdw_item_id").setWidth(100);
		egu.getColumn("zhuangcdw_item_id").setHidden(true);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(80);
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("zhuangcdw_item_id").setEditor(c1);
		c1.setEditable(true);
		String sqlq = "select i.id,i.mingc from item i,itemsort t where i.itemsortid=t.id and t.bianm='ZHUANFGCDW' ";
		egu.getColumn("zhuangcdw_item_id").setComboEditor(egu.gridId,new IDropDownModel(sqlq));
		
		//设置装车单位默认可为空
		egu.getColumn("zhuangcdw_item_id").editor.allowBlank = true;
		//控制装车单位是否显示
		String SQL = "select zhi from xitxxb where mingc = '装车单位是否显示' and zhuangt = 1 and diancxxb_id = "
			+ v.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(SQL);
		String shifxs="否";
		while(rs.next()){
			shifxs=rs.getString("zhi");
		}
		if(shifxs.equals("是")){
//			设置装车单位显示切不可为空
			egu.getColumn("zhuangcdw_item_id").setHidden(false);
			egu.getColumn("zhuangcdw_item_id").editor.allowBlank = false;
		}
		
		
		//扣吨保留3位小数
		((NumberField) egu.getColumn("koud").editor).setDecimalPrecision(3);
		
		//设置发货日期和到货日期的默认值
		egu.getColumn("caiyrq").setDefaultValue(getRiqi());
		//设置品种下拉框
		ComboBox c5=new ComboBox();
		egu.getColumn("pinzb_id").setEditor(c5);
		c5.setEditable(true);
		String pinzSql=SysConstant.SQL_Pinz_mei;
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,new IDropDownModel(pinzSql));
//		设置车头
		ComboBox chet = new ComboBox();
		egu.getColumn("chet").setEditor(chet);
		chet.setEditable(true);
		String chetsql = "select id,mingc from qiccht order by mingc" ;
		egu.getColumn("chet").setComboEditor(egu.gridId,new IDropDownModel(chetsql));
		egu.getColumn("chet").setDefaultValue(
				"" + egu.getColumn("chet").combo.getLabel(0));
		egu.getColumn("chet").setReturnId(false);
		
		//设置口径下拉框
		ComboBox c6=new ComboBox();
		egu.getColumn("jihkjb_id").setEditor(c6);
		c6.setEditable(true);
		String jihkjSql=SysConstant.SQL_Kouj;
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,new IDropDownModel(jihkjSql));
//		运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ v.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
//		设置原收货单位下拉框
//		ComboBox c9=new ComboBox();
//		egu.getColumn("yuanshdwb_id").setEditor(c9);
//		c9.setEditable(true);//设置可输入
//		String Sql="select id,mingc from diancxxb order by mingc";
//		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId, new IDropDownModel(Sql));
//		egu.getColumn("yuanshdwb_id").setDefaultValue(""+((Visit) getPage().getVisit()).getDiancmc());
		
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());	
		egu.addTbarText("-");
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
	    egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('cellclick',function(own, irow, icol, e){ "
				+"row = irow; \n"
				+"if('GONGYSB_ID' == gridDiv_grid.getColumnModel().getDataIndex(icol)){"
				+"gongysTree_window.show();}});");
		egu.setDefaultsortable(false);
		setExtGrid(egu);
		DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_kj,"gongysTree"
				,""+v.getDiancxxb_id(),null,null,null);
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
		v.setDefaultTree(dt);
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
	

	
	
	
//供应商
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
			((Visit) getPage().getVisit()).setDropDownBean10(null);
			((Visit) getPage().getVisit()).setProSelectionModel10(null);
			setMeikModel(null);
			setMeikModels();
			setRiqi(DateUtil.FormatDate(new Date()));
			setTbmsg(null);
			getSelectData();
		}
	}
}