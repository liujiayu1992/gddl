package com.zhiren.dc.jilgl.jicxx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 2009-10-12
 * 李鹏
 * 发货信息编码
 * */
public class Fahxxbm extends BasePage {
	// 界面用户提示
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
	}
	
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	//-----------------------------------------------

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
			save();
			init();
		}
	}

	private void save() {
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer();
		sql.append("begin\n");
		JDBCcon con=new JDBCcon();
		String xuh=new String();
		xuh="";
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		if (drsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Fahxxbm.save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (drsl.next()){//删除 
			sql.append("delete from fahxxbmb where\n");
			sql.append("id=" + drsl.getString("id"));
			sql.append(";\n");
		}

		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult
					+ "Fahxxbm.save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			if("0".equals(rsl.getString("id"))){//增加
				ResultSetList r=con.getResultSetList("select xuh from (select rownum xuh,bianm from fahxxbmb  order by bianm ) where bianm='ss'");
				if(r.next()){
					xuh += "(" + r.getInt("xuh") + ")";
				}else{
					sql.append("insert into fahxxbmb(id,bianm,gongysb_id,meikxxb_id,pinzb_id,faz_id,jihkjb_id,yunsdwb_id,meicb_id,\n");
					sql.append("                     yunsfsb_id,daoz_id,zhuangt,beiz)\n");
					sql.append("values(\n");
					sql.append("getnewid("+visit.getDiancxxb_id()+"),\n");
					sql.append("'"+rsl.getString("bianm")+"',\n");
					sql.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(piny_mingc("gongysb",rsl
							.getString("gongysb_id")))+",\n");
					sql.append((getExtGrid().getColumn("meikxxb_id").combo).getBeanId(piny_mingc("meikxxb",rsl
							.getString("meikxxb_id")))+",\n");
					sql.append((getExtGrid().getColumn("pinzb_id").combo).getBeanId(piny_mingc("pinzb",rsl
							.getString("pinzb_id")))+",\n");
					sql.append((getExtGrid().getColumn("faz_id").combo).getBeanId(piny_mingc("chezxxb",rsl
							.getString("faz_id")))+",\n");
					sql.append((getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl
							.getString("jihkjb_id"))+",\n");
					sql.append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl
							.getString("yunsdwb_id"))+",\n");
					sql.append((getExtGrid().getColumn("meicb_id").combo).getBeanId(piny_mingc("meicb",rsl
							.getString("meicb_id")))+",\n");
					sql.append((getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(piny_mingc("yunsfsb",rsl
							.getString("yunsfsb_id")))+",\n");
					sql.append((getExtGrid().getColumn("daoz_id").combo).getBeanId(piny_mingc("chezxxb",rsl
							.getString("daoz_id")))+",\n");
					sql.append((getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl
							.getString("zhuangt"))+",\n");
					sql.append("'"+rsl.getString("beiz")+"');\n");
				}
			}else{	//修改
				ResultSetList r=con.getResultSetList("select xuh from (select id,rownum xuh,bianm from fahxxbmb) where bianm='"+rsl.getString("bianm") + "' and id <>" + rsl.getString("id") + "  order by bianm");
				if(r.next()){
					xuh += "(" + r.getInt("xuh") + ")";
				}else{
					sql.append("update fahxxbmb set  ");
					sql.append("	bianm ='" + rsl.getString("bianm") + "',");
					sql.append("	gongysb_id ="+(getExtGrid().getColumn("gongysb_id").combo).getBeanId(piny_mingc("gongysb",rsl
							.getString("gongysb_id")))+",\n");
					sql.append("	meikxxb_id ="+(getExtGrid().getColumn("meikxxb_id").combo).getBeanId(piny_mingc("meikxxb",rsl
							.getString("meikxxb_id")))+",\n");
					sql.append("	pinzb_id ="+(getExtGrid().getColumn("pinzb_id").combo).getBeanId(piny_mingc("pinzb",rsl
							.getString("pinzb_id")))+",\n");
					sql.append("	faz_id ="+(getExtGrid().getColumn("faz_id").combo).getBeanId(piny_mingc("chezxxb",rsl
							.getString("faz_id")))+",\n");
					sql.append("	jihkjb_id ="+(getExtGrid().getColumn("jihkjb_id").combo).getBeanId(rsl
							.getString("jihkjb_id"))+",\n");
					sql.append("	yunsdwb_id ="+(getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl
							.getString("yunsdwb_id"))+",\n");
					sql.append("	meicb_id ="+(getExtGrid().getColumn("meicb_id").combo).getBeanId(piny_mingc("meicb",rsl
							.getString("meicb_id")))+",\n");
					sql.append("	yunsfsb_id ="+(getExtGrid().getColumn("yunsfsb_id").combo).getBeanId(piny_mingc("yunsfsb",rsl
							.getString("yunsfsb_id")))+",\n");
					sql.append("	daoz_id ="+(getExtGrid().getColumn("daoz_id").combo).getBeanId(piny_mingc("chezxxb",rsl
							.getString("daoz_id")))+",\n");
					sql.append("	zhuangt ="+(getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl
							.getString("zhuangt"))+",\n");
					sql.append("	beiz='").append(rsl.getString("beiz")+"'\n");
					sql.append("where id="+rsl.getString("id"));
					sql.append(";\n");
				}
			}
		}
		sql.append("end;");
		if(xuh.equals("")){
			int flag=con.getUpdate(sql.toString());
			if(flag==-1){
				setMsg("保存失败！");
			}else{
				setMsg("保存成功！");
			}
		}else{
			setMsg("&nbsp;&nbsp;&nbsp;与序号" + xuh + "的编码有重复，请重新维护！&nbsp;&nbsp;&nbsp;");
		}		
	}
		
	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer();
		
		sql.append(
				"select f.id,f.bianm,g.mingc gongysb_id,mk.mingc meikxxb_id,p.mingc pinzb_id,c.mingc faz_id,\n" +
				"       j.mingc jihkjb_id,yw.mingc yunsdwb_id,mc.mingc meicb_id,yf.mingc yunsfsb_id,c.mingc daoz_id,\n" + 
				"       decode(f.zhuangt,1,'使用','未使用') zhuangt,f.beiz\n" + 
				"from fahxxbmb f,chezxxb c,gongysb g,meikxxb mk,pinzb p,jihkjb j,yunsdwb yw,meicb mc,yunsfsb yf,chezxxb dz\n" + 
				"where f.faz_id=c.id(+)\n" + 
				"  and f.daoz_id=dz.id(+)\n" + 
				"  and f.gongysb_id=g.id\n" + 
				"  and f.meikxxb_id=mk.id\n" + 
				"  and f.pinzb_id=p.id(+)\n" + 
				"  and f.jihkjb_id=j.id(+)\n" + 
				"  and f.yunsdwb_id=yw.id(+)\n" + 
				"  and f.meicb_id=mc.id(+)\n" + 
				"  and f.yunsfsb_id=yf.id(+)"+
				"order by bianm asc");
		
		ResultSetList rsl = con.getResultSetList(sql.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
				
		egu.getColumn("bianm").setHeader("编码");
		egu.getColumn("bianm").setWidth(80);
		
		egu.getColumn("gongysb_id").setHeader("供应商单位");
		egu.getColumn("gongysb_id").setWidth(120);
		ComboBox gongysb_id_combobox=new ComboBox();
		gongysb_id_combobox.setListWidth(150);
		gongysb_id_combobox.setEditable(true);
/**
 * huochaoyuan
 * 2009-11-29修改供应商下拉框，过滤煤矿地区信息；
 */	
		String gongysb_id_sql="select id,piny || '-' ||mingc mingc from gongysb where leix=1 order by mingc";
//end		
		egu.getColumn("gongysb_id").setEditor(gongysb_id_combobox);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(gongysb_id_sql));
//		egu.getColumn("gongysb_id").setReturnId(true);
		
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(120);
		ComboBox meikxxb_id_combobox=new ComboBox();
		meikxxb_id_combobox.setListWidth(150);
		meikxxb_id_combobox.setEditable(true);
		String meikxxb_id_sql="select id,piny || '-' ||mingc mingc from meikxxb where zhuangt=1 order by mingc";
		egu.getColumn("meikxxb_id").setEditor(meikxxb_id_combobox);
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId, new IDropDownModel(meikxxb_id_sql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(60);
		ComboBox pinzb_id_combobox=new ComboBox();
		pinzb_id_combobox.setListWidth(60);
		pinzb_id_combobox.setEditable(true);
		pinzb_id_combobox.allowBlank=true;
		String pinzb_id_sql="select id,piny || '-' ||mingc mingc from pinzb order by mingc";
		egu.getColumn("pinzb_id").setEditor(pinzb_id_combobox);
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId, new IDropDownModel(pinzb_id_sql));
		egu.getColumn("pinzb_id").setReturnId(true);
		
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("faz_id").setWidth(60);
		ComboBox faz_id_combobox=new ComboBox();
		faz_id_combobox.setListWidth(80);
		faz_id_combobox.setEditable(true);
		faz_id_combobox.allowBlank=true;
		String faz_id_sql="select id,piny || '-' ||mingc mingc from chezxxb order by mingc";
		egu.getColumn("faz_id").setEditor(faz_id_combobox);
		egu.getColumn("faz_id").setComboEditor(egu.gridId, new IDropDownModel(faz_id_sql));
		egu.getColumn("faz_id").setReturnId(true);
		
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(60);
		ComboBox jihkjb_id_combobox=new ComboBox();
		jihkjb_id_combobox.setListWidth(60);
		jihkjb_id_combobox.allowBlank=true;
//		jihkjb_id_combobox.setEditable(true);
		String jihkjb_id_sql="select id,mingc from jihkjb order by mingc";
		egu.getColumn("jihkjb_id").setEditor(jihkjb_id_combobox);
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId, new IDropDownModel(jihkjb_id_sql));
		egu.getColumn("jihkjb_id").setReturnId(true);
		
		egu.getColumn("yunsdwb_id").setHeader("运输单位");
		egu.getColumn("yunsdwb_id").setWidth(80);
		ComboBox yunsdwb_id_combobox=new ComboBox();
		yunsdwb_id_combobox.setListWidth(100);
		yunsdwb_id_combobox.allowBlank=true;
//		yunsdwb_id_combobox.setEditable(true);
		String yunsdwb_id_sql="select id,mingc from yunsdwb order by mingc";
		egu.getColumn("yunsdwb_id").setEditor(yunsdwb_id_combobox);
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsdwb_id_sql));
		egu.getColumn("yunsdwb_id").setReturnId(true);
		
		egu.getColumn("meicb_id").setHeader("煤场");
		egu.getColumn("meicb_id").setWidth(100);
		ComboBox meicb_id_combobox=new ComboBox();
		meicb_id_combobox.setListWidth(100);
		meicb_id_combobox.allowBlank=true;
		meicb_id_combobox.setEditable(true);
		String meicb_id_sql="select id,piny || '-' ||mingc mingc from meicb order by mingc";
		egu.getColumn("meicb_id").setEditor(meicb_id_combobox);
		egu.getColumn("meicb_id").setComboEditor(egu.gridId, new IDropDownModel(meicb_id_sql));
		egu.getColumn("meicb_id").setReturnId(true);
		
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(60);
		ComboBox yunsfsb_id_combobox=new ComboBox();
		yunsfsb_id_combobox.setListWidth(60);
		yunsfsb_id_combobox.allowBlank=true;
		yunsfsb_id_combobox.setEditable(true);
		String yunsfsb_id_sql="select id,piny || '-' ||mingc mingc from yunsfsb order by mingc";
		egu.getColumn("yunsfsb_id").setEditor(yunsfsb_id_combobox);
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId, new IDropDownModel(yunsfsb_id_sql));
		egu.getColumn("yunsfsb_id").setReturnId(true);
		
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("daoz_id").setWidth(60);
		ComboBox daoz_id_combobox=new ComboBox();
		daoz_id_combobox.setListWidth(80);
		daoz_id_combobox.allowBlank=true;
		daoz_id_combobox.setEditable(true);
		String daoz_id_sql="select id,piny || '-' ||mingc mingc from chezxxb order by mingc";
		egu.getColumn("daoz_id").setEditor(daoz_id_combobox);
		egu.getColumn("daoz_id").setComboEditor(egu.gridId, new IDropDownModel(daoz_id_sql));
		egu.getColumn("daoz_id").setReturnId(true);
		
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(60);
		List zhuangt = new ArrayList();
		zhuangt.add(new IDropDownBean("1","使用"));
		zhuangt.add(new IDropDownBean("0","未使用"));
		ComboBox cb=new ComboBox();
		cb.allowBlank=true;
		egu.getColumn("zhuangt").setEditor(cb);
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(zhuangt));
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(150);
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, "");
		egu.addToolbarButton(GridButton.ButtonType_Delete, "");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		setExtGrid(egu);
		
		rsl.close();
		con.Close();
		
	}
	
	private String piny_mingc(String table_name,String value){
		
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList("select piny || '-'|| mingc pmingc from " + table_name + " where mingc ='" + value + "'");
		if(rsl.next()){
			return rsl.getString("pmingc");
		}else{
			return "";
		}
	}

	private void init() {
		setExtGrid(null);
		getSelectData();
	}	
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setString1(null);
			init();
		}
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
