package com.zhiren.shihs;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Shihsjjqc extends BasePage implements PageValidateListener {
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
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null || rsl.getRows() <= 0) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qicjjlr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		sb.append("begin \n");
		while(rsl.next()){
			sb.append("insert into shihcpb(id,diancxxb_id,xuh,fahrq,daohrq,gongysb_id,shihpzb_id,\n")
			.append("cheph,piaojh,yuanmz,maoz,biaoz,chebb_id,yuanghdw,yunsdwb_id,zhongcsj,\n")
			.append("zhongchh,zhongcjjy,lursj,lury,beiz) values(getnewid(").append(v.getDiancxxb_id())
			.append("),").append(v.getDiancxxb_id()).append(",").append(rsl.getString("xuh")).append(",")
			.append(DateUtil.FormatOracleDate(rsl.getString("fahrq"))).append(",")
			.append(DateUtil.FormatOracleDate(rsl.getString("daohrq"))).append(",")
			.append((getExtGrid().getColumn("gongysb_id").combo).getBeanId(rsl.getString("gongysb_id"))).append(",")
			.append((getExtGrid().getColumn("shihpzb_id").combo).getBeanId(rsl.getString("shihpzb_id"))).append(",'")
			.append(rsl.getString("cheph")).append("','").append(rsl.getString("piaojh")).append("',")
			.append(rsl.getDouble("maoz")).append(",").append(rsl.getDouble("maoz")).append(",").append(rsl.getDouble("biaoz"))
			.append(",").append(SysConstant.CHEB_QC).append(",'").append(rsl.getString("yuanghdw")).append("',")
			.append((getExtGrid().getColumn("yunsdwb_id").combo).getBeanId(rsl.getString("yunsdwb_id"))).append(",sysdate,'")
			.append(rsl.getString("zhongchh")).append("','").append(v.getRenymc()).append("',sysdate,'").append(v.getRenymc())
			.append("','").append(rsl.getString("beiz")).append("');\n");
		}
		sb.append("end;\n");
		int flag = con.getUpdate(sb.toString());
		if(flag == -1){
			setMsg("保存失败 ");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit(); 
		con.Close();
		setMsg("毛重保存成功");
	}
	private boolean _SavePizChick = false;
	public void SavePizButton(IRequestCycle cycle) {
		_SavePizChick = true;
	}
	private void SavePiz() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getPizGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qincjjlr.SavePiz 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		if(rsl.next()) {
			String id = rsl.getString("id");
			double piz = rsl.getDouble("piz");

			sb.delete(0, sb.length());
//			更新车皮表将 皮重、票重、轻车时间、轻车衡号、轻车人员 更新入车皮表
			sb.append("update shihcpb set piz = ").append(piz).append(",qingcsj=sysdate,qingchh='").append(rsl.getString("qingchh")).append("',");
			sb.append("qingcjjy='").append(v.getRenymc()).append("'");
			sb.append(" where id =").append(id);
			int flag = con.getUpdate(sb.toString());
			if(flag == -1){
				setMsg("保存失败 ");
				con.rollBack();
				con.Close();
				return;
			}
			v.setString1(id);
		}
		con.commit();
		con.Close();
		setMsg("皮重保存成功");
	}
	
	private boolean _AutoSaveChick = false;
	public void AutoSaveButton(IRequestCycle cycle) {
		_AutoSaveChick = true;
	}
	
	private void AutoSave() {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有做出任何更改！");
			return;
		}
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
		if (_SavePizChick) {
			_SavePizChick = false;
			SavePiz();
			init();
		}
		if (_AutoSaveChick) {
			_AutoSaveChick = false;
			AutoSave();
			init();
		}
	}
	
	public void CreateEgu4M(Visit v,JDBCcon con){
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl;
		sb.append("select nvl(max(xuh),1) xuh,max(gongysb_id) gongysb_id,max(shihpzb_id) shihpzb_id, \n")
		.append("max(cheph) cheph,max(biaoz) biaoz,max(maoz) maoz,max(zhongchh) zhongchh,max(piaojh) piaojh, \n")
		.append("max(yunsdwb_id) yunsdwb_id,max(yuanghdw) yuanghdw,sysdate fahrq,sysdate daohrq,max(beiz) beiz \n")
		.append("from (select c.xuh+1 xuh,g.piny||g.mingc gongysb_id,p.piny||p.mingc shihpzb_id,'' cheph, \n")
		.append("0 biaoz,0 maoz,'' piaojh,c.yuanghdw,yd.mingc yunsdwb_id,'' zhongchh,c.beiz \n")
		.append("from shihcpb c, shihgysb g, shihpzb p, yunsdwb yd \n")
		.append("where c.gongysb_id = g.id and c.shihpzb_id = p.id \n")
		.append("and c.yunsdwb_id = yd.id order by c.id desc) where rownum = 1");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu= new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(88);
		egu.addPaging(0);
		
		egu.getColumn("xuh").setHeader(Locale.xuh_chepb);
		egu.getColumn("xuh").setWidth(40);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("shihpzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("shihpzb_id").setWidth(60);
		egu.getColumn("shihpzb_id").setEditor(null);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu.getColumn("biaoz").setWidth(50);
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(50);
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(70);
		egu.getColumn("zhongchh").setEditor(null);
		egu.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu.getColumn("piaojh").setWidth(100);
		egu.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu.getColumn("yunsdwb_id").setWidth(80);
		egu.getColumn("yunsdwb_id").setEditor(null);
		egu.getColumn("yuanghdw").setHeader(Locale.yuanghdw);
		egu.getColumn("yuanghdw").setWidth(80);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu.getColumn("beiz").setWidth(100);
		
//		设置供应商下拉框
		ComboBox cgys= new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cgys);
		cgys.setEditable(true);
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId, new IDropDownModel(SysConstant.SQL_Shihgys));
//		设置品种下拉框
		ComboBox cpz=new ComboBox();
		egu.getColumn("shihpzb_id").setEditor(cpz);
		cpz.setEditable(true);
		egu.getColumn("shihpzb_id").setComboEditor(egu.gridId, new IDropDownModel(SysConstant.SQL_Shihpz));
//		运输单位
		ComboBox cysdw = new ComboBox();
		egu.getColumn("yunsdwb_id").setEditor(cysdw);
		cysdw.setEditable(true);
		String yunsdwSql = "select id,mingc from yunsdwb where diancxxb_id="+ v.getDiancxxb_id();
		egu.getColumn("yunsdwb_id").setComboEditor(egu.gridId,new IDropDownModel(yunsdwSql));
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='nosel';DataIndex='MAOZ';});");
		setExtGrid(egu);
	}
	public void CreateEgu4P(Visit v,JDBCcon con){
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl;
		sb.append("select c.id,c.xuh xuh,g.mingc gongysb_id,p.mingc shihpzb_id,c.cheph,c.piz, \n")
		.append("c.qingchh,c.biaoz,c.maoz,c.piaojh,yd.mingc yunsdwb_id,c.yuanghdw,c.beiz \n")
		.append("from shihcpb c, shihgysb g, shihpzb p, yunsdwb yd \n")
		.append("where c.gongysb_id = g.id and c.shihpzb_id = p.id \n")
		.append("and c.yunsdwb_id = yd.id and c.qingcsj is null order by c.id \n")
		.append("\n");
		rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl);
//		设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		设置为只读
		egu1.setGridType(ExtGridUtil.Gridstyle_Read);
//		增加分页
		egu1.addPaging(12);
		egu1.getColumn("xuh").setHeader(Locale.xuh_chepb);
		egu1.getColumn("xuh").setWidth(40);
		egu1.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu1.getColumn("gongysb_id").setWidth(100);
		egu1.getColumn("gongysb_id").setEditor(null);
		egu1.getColumn("shihpzb_id").setHeader(Locale.pinzb_id_fahb);
		egu1.getColumn("shihpzb_id").setWidth(60);
		egu1.getColumn("shihpzb_id").setEditor(null);
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("cheph").setWidth(60);
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("piz").setWidth(50);
		egu1.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu1.getColumn("qingchh").setWidth(70);
		egu1.getColumn("biaoz").setHeader(Locale.biaoz_chepb);
		egu1.getColumn("biaoz").setWidth(50);
		egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu1.getColumn("maoz").setWidth(50);
		egu1.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu1.getColumn("piaojh").setWidth(100);
		egu1.getColumn("yunsdwb_id").setHeader(Locale.yunsdw_id_chepb);
		egu1.getColumn("yunsdwb_id").setWidth(80);
		egu1.getColumn("yuanghdw").setHeader(Locale.yuanghdw);
		egu1.getColumn("yuanghdw").setWidth(80);
		egu1.getColumn("beiz").setHeader(Locale.beiz_chepb);
		egu1.getColumn("beiz").setWidth(100);
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu1.addTbarBtn(refurbish);
		egu1.addToolbarButton(GridButton.ButtonType_Save, "SavePizButton");
		//egu1.addToolbarButton("保存历史皮重",GridButton.ButtonType_Save, "AutoSaveButton", null, SysConstant.Btn_Icon_Save);
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("Qicjjd")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu1.addTbarBtn(gbp);
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		setPizGrid(egu1);
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		CreateEgu4M(v,con);
		CreateEgu4P(v,con);
		con.Close();
	}

	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
	}

	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getPizGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid2();
	}

	public void setPizGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid2(extgrid);
	}

	public String getGridScript() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridScriptPiz() {
		if (getExtGrid() == null) {
			return "";
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
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
			visit.setString1(null);
			init();
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setPizGrid(null);
		getSelectData();
	}
}