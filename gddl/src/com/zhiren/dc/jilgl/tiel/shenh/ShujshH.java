package com.zhiren.dc.jilgl.tiel.shenh;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class ShujshH extends BasePage implements PageValidateListener {
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
	public void setFahids(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
//	发货日期下拉框
	public IDropDownBean getFahrqValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getFahrqModel().getOptionCount()>0) {
				setFahrqValue((IDropDownBean)getFahrqModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setFahrqValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getFahrqModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setFahrqModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setFahrqModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setFahrqModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		String sql = "select rownum xuh,fahrq from (select distinct to_char(fahrq,'yyyy-mm-dd') fahrq from fahb where diancxxb_id = "
					+visit.getDiancxxb_id()+" and hedbz = "+SysConstant.HEDBZ_YJJ+" and yunsfsb_id = "+SysConstant.YUNSFS_HUOY+" order by fahrq) ";
		setFahrqModel(new IDropDownModel(sql));
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private void Save() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
		}
		int flag = 0;
		int ches = 0;
		int biaoz = 0;
		while(rsl.next()) {
			String fahbid = rsl.getString("id");
			ches += rsl.getInt("ches");
			biaoz += rsl.getInt("biaoz");
			sb.append("update chepb set hedbz = ").append(SysConstant.HEDBZ_YSH).append(" where fahb_id = ").append(fahbid);
			flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
						+"SQL:"+sb);
				setMsg(ErrorMessage.ShujshH001);
				return;
			}
			sb.delete(0, sb.length());
			sb.append("update fahb set liucztb_id =1,hedbz = ").append(SysConstant.HEDBZ_YSH).append(" where id = ").append(fahbid);
			flag = con.getUpdate(sb.toString());
			if(flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
						+"SQL:"+sb);
				setMsg(ErrorMessage.ShujshH002);
				return;
			}
			sb.delete(0, sb.length());
		}
		con.commit();
		con.Close();
		setMsg("审核成功！共审核了 "+ ches + " 车，总票重 "+biaoz+" 吨。");
	}
	
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择一个发货进行查看！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		StringBuffer fahids = new StringBuffer();
		while(rsl.next()) {
			fahids.append(rsl.getString("id")).append(",");
		}
		if(fahids.length()>1) {
			fahids.deleteCharAt(fahids.length()-1);
		}
		setFahids(fahids.toString());
		cycle.activate("Chepsh");
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			init();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		sb.append("select f.id, g.mingc gongysb_id, m.mingc meikxxb_id, \n")
		.append("p.mingc pinzb_id, f.daohrq, \n")
		.append("(select fc.mingc from chezxxb fc where fc.id = f.faz_id) faz_id, \n")
		.append("(select dc.mingc from chezxxb dc where dc.id = f.daoz_id) daoz_id, \n")
		.append("f.jingz, f.biaoz, f.zongkd, f.ches, f.chec, j.mingc jihkjb_id \n")
		.append("from fahb f, gongysb g, meikxxb m, pinzb p, jihkjb j \n")
		.append("where f.gongysb_id = g.id and f.meikxxb_id = m.id \n")
		.append("and f.pinzb_id = p.id and f.jihkjb_id = j.id \n")
		.append("and f.hedbz = ").append(SysConstant.HEDBZ_YJJ)
		.append(" and f.yunsfsb_id =").append(SysConstant.YUNSFS_HUOY).append(" \n");
		
		if(getFahrqValue()!=null) {
			sb.append("and f.fahrq=to_date('").append(getFahrqValue().getValue()).append("','yyyy-mm-dd')\n");
		}	
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
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
			egu.getColumn("id").setHidden(true);
			egu.getColumn("id").editor = null;
		}
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(90);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(90);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(50);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(77);
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(65);
		egu.getColumn("faz_id").setEditor(null);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(65);
		egu.getColumn("daoz_id").setEditor(null);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("zongkd").setHeader(Locale.zongkd_chepb);
		egu.getColumn("zongkd").setWidth(60);
		egu.getColumn("zongkd").setEditor(null);
		egu.getColumn("ches").setHeader(Locale.ches_fahb);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("chec").setHeader(Locale.chec_fahb);
		egu.getColumn("chec").setWidth(60);
		egu.getColumn("chec").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(65);
		egu.getColumn("jihkjb_id").setEditor(null);

		ComboBox fhrq = new ComboBox();
		fhrq.setTransform("FahrqSelect");
		fhrq.setWidth(130);
		fhrq.setId("FahrqSelect");
		fhrq.setListeners("select:function(own,rec,index){Ext.getDom('FahrqSelect').selectedIndex=index}");
		egu.addTbarText("发货日期：");
		egu.addToolbarItem(fhrq.getScript());
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_SubmitSel, "SaveButton");
		egu.addToolbarButton("查看",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Show);
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
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
			if(!visit.getActivePageName().toString().equals("Chepsh")) {
				init();
			}
			getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setFahrqModel(null);
		setFahrqValue(null);
		setExtGrid(null);
		setFahids(null);
		setFahrqModels();
		getSelectData();
	}
}