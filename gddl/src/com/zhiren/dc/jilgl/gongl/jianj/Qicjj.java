package com.zhiren.dc.jilgl.gongl.jianj;

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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Qicjj extends BasePage implements PageValidateListener {
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
		setPTbmsg(null);
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	private String ptbmsg;
	public String getPTbmsg() {
		return ptbmsg;
	}
	public void setPTbmsg(String tbmsg) {
		this.ptbmsg = tbmsg; 
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
	
	private boolean _RefurbishPizChick = false;
	public void RefurbishPizButton(IRequestCycle cycle) {
		_RefurbishPizChick = true;
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
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qicjj.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = rsl.getDouble("piz");
			double biaoz = rsl.getDouble("biaoz");
			String fahbid = rsl.getString("fahb_id");
			sb.delete(0, sb.length());
//			更新车皮表将 皮重、票重、备注 更新入车皮表
			sb.append("update chepb set maoz = ").append(maoz).append(",biaoz = ").append(biaoz);
			sb.append(",zhongcsj = ").append(DateUtil.FormatOracleDateTime(new Date()));
			sb.append(",zhongcjjy = '").append(rsl.getString("zhongcjjy")).append("',zhongchh = '").append(rsl.getString("zhongchh"));
			sb.append("' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj001);
				setMsg(ErrorMessage.Qicjj001);
				return;
			}
			
//			根据单车id 调用jilcz 中CountChepbYuns 方法计算单车的运损盈亏
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ);
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj002);
				setMsg(ErrorMessage.Qicjj002);
				return;
			}
//			根据车皮所在fahid 调用Jilcz 中 updateFahb 方法更新发货表
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj003);
				setMsg(ErrorMessage.Qicjj003);
				return;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj004);
				setMsg(ErrorMessage.Qicjj004);
				return;
			}
		}
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
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Qicjj.SavePiz 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		int flag = 0;
		if(rsl.next()) {
			String id = rsl.getString("id");
			double maoz = rsl.getDouble("maoz");
			double piz = rsl.getDouble("piz");
			double biaoz = rsl.getDouble("biaoz");
			String fahbid = rsl.getString("fahb_id");
			if(biaoz == 0.0) {
				biaoz = maoz - piz;
			}
			sb.delete(0, sb.length());
//			更新车皮表将 皮重、票重、备注 更新入车皮表
			sb.append("update chepb set piz = ").append(piz).append(",biaoz = ").append(biaoz);
			sb.append(",qingcsj = ").append(DateUtil.FormatOracleDateTime(new Date()));
			sb.append(",qingcjjy = '").append(rsl.getString("qingcjjy")).append("',qingchh = '").append(rsl.getString("qingchh"));
			sb.append("' where id =").append(id);
			flag = con.getUpdate(sb.toString());
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj005);
				setMsg(ErrorMessage.Qicjj005);
				return;
			}
			
//			根据单车id 调用jilcz 中CountChepbYuns 方法计算单车的运损盈亏
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ);
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj006);
				setMsg(ErrorMessage.Qicjj006);
				return;
			}
//			根据车皮所在fahid 调用Jilcz 中 updateFahb 方法更新发货表
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj007);
				setMsg(ErrorMessage.Qicjj007);
				return;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Qicjj008);
				setMsg(ErrorMessage.Qicjj008);
				return;
			}
		}
		con.Close();
		setPTbmsg("皮重保存成功");
	}
	public void submit(IRequestCycle cycle) {
		if (_RefurbishPizChick) {
			_RefurbishPizChick = false;
			getSelectData();
		}
		
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
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long cid = -1;
		if(getLiushValue() != null) {
			cid = getLiushValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.fahb_id,c.biaoz,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') lursj,\n");
		sb.append("c.piaojh,y.bianm,c.cheph,c.maoz,c.piz,nvl('"+visit.getRenymc()+"','') zhongcjjy,c.zhongchh,c.qingcjjy,c.qingchh \n");
		sb.append("from chepb c, fahb f, caiyb y \n");
		sb.append("where c.fahb_id = f.id and f.zhilb_id = y.zhilb_id and c.maoz = 0\n");
		sb.append("and c.chebb_id = ").append(SysConstant.CHEB_QC).append(" and c.id = ").append(cid);
//		下面注释这句话是判定 汽车
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu;
		double piz=0;
		if (rsl.next()){
			piz = rsl.getDouble("piz");
		}
		rsl.beforefirst();
		egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(212);
		egu.addPaging(0);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").editor=null;
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(60);
		egu.getColumn("biaoz").setHidden(true);
		egu.getColumn("biaoz").editor=null;
//		录入时间
		egu.getColumn("lursj").setHeader(Locale.lursj_chepb);
		egu.getColumn("lursj").setWidth(130);
		egu.getColumn("lursj").setEditor(null);
//		进厂编号
		egu.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu.getColumn("piaojh").setWidth(110);
		egu.getColumn("piaojh").setEditor(null);
//		采样编号
		egu.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu.getColumn("bianm").setWidth(90);
		egu.getColumn("bianm").setEditor(null);
//		车皮号
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(90);
		egu.getColumn("cheph").setEditor(null);
//		毛重
		egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
		egu.getColumn("maoz").setWidth(70);
		egu.getColumn("maoz").setEditor(null);
//		皮重
		if(piz==0){
			egu.getColumn("piz").setHidden(true);
			egu.getColumn("piz").editor=null;
			egu.getColumn("qingcjjy").setHidden(true);
			egu.getColumn("qingcjjy").editor=null;
			egu.getColumn("qingchh").setHidden(true);
			egu.getColumn("qingchh").editor=null;
		}else{
			egu.getColumn("piz").setHeader(Locale.piz_chepb);
			egu.getColumn("piz").setWidth(70);
			egu.getColumn("piz").setEditor(null);
//			轻车检斤员
			egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
			egu.getColumn("qingcjjy").setWidth(90);
			egu.getColumn("qingcjjy").setEditor(null);
//			轻车衡号
			egu.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
			egu.getColumn("qingchh").setWidth(80);
			egu.getColumn("qingchh").setEditor(null);
		}
//		重车检斤员
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(90);
		egu.getColumn("zhongcjjy").setEditor(null);
//		重车衡号
		egu.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
		egu.getColumn("zhongchh").setWidth(80);
		egu.getColumn("zhongchh").setEditor(null);
//		流水号
		ComboBox liush = new ComboBox();
		liush.setTransform("LiushSelect");
		liush.setWidth(130);
		liush.setEditable(true);
		liush.setListeners("select:function(own,rec,index){Ext.getDom('LiushSelect').selectedIndex=index}");
		egu.addToolbarItem(liush.getScript());
				
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(){Mode='nosel';DataIndex='MAOZ';});");
		setExtGrid(egu);
		
		long did = 0;
		if(getLiush1Value() != null) {
			did = getLiush1Value().getId();
		}
		sb.delete(0, sb.length());
		sb.append("select c.id,c.fahb_id,c.biaoz,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') lursj,\n");
		sb.append("c.piaojh,y.bianm,c.cheph,c.maoz,c.piz,c.zhongcjjy,c.zhongchh,'"+visit.getRenymc()+"' qingcjjy,c.qingchh \n");
		sb.append("from chepb c, fahb f, caiyb y \n");
		sb.append("where c.fahb_id = f.id and f.zhilb_id = y.zhilb_id and c.piz = 0\n");
		sb.append("and c.chebb_id = ").append(SysConstant.CHEB_QC).append(" and c.id = ").append(did);
		ResultSetList rsl1 = con.getResultSetList(sb.toString());
		if (rsl1 == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		double maoz = 0;
		if(rsl1.next()){
			maoz = rsl1.getDouble("maoz");
		}
		rsl1.beforefirst();
		ExtGridUtil egu1 = new ExtGridUtil("gridDivPiz", rsl1);
		//设置页面宽度
		egu1.setWidth(Locale.Grid_DefaultWidth);
//		egu1.setHeight(240);
		egu1.addPaging(12);
		egu1.getColumn("id").setHidden(true);
		egu1.getColumn("id").editor=null;
		egu1.getColumn("fahb_id").setHidden(true);
		egu1.getColumn("fahb_id").editor=null;
		if(maoz==0){
			egu1.getColumn("maoz").setHidden(true);
			egu1.getColumn("maoz").editor=null;
			egu1.getColumn("zhongcjjy").setHidden(true);
			egu1.getColumn("zhongcjjy").editor=null;
			egu1.getColumn("zhongchh").setHidden(true);
			egu1.getColumn("zhongchh").editor=null;
		}else{
			egu1.getColumn("maoz").setHeader(Locale.maoz_chepb);
			egu1.getColumn("maoz").setWidth(70);
			egu1.getColumn("maoz").setEditor(null);
//			重车检斤员
			egu1.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
			egu1.getColumn("zhongcjjy").setWidth(90);
			egu1.getColumn("zhongcjjy").setEditor(null);
//			重车衡号
			egu1.getColumn("zhongchh").setHeader(Locale.zhongchh_chepb);
			egu1.getColumn("zhongchh").setWidth(90);
			egu1.getColumn("zhongchh").setEditor(null);
		}
		egu1.getColumn("biaoz").setHidden(true);
		egu1.getColumn("biaoz").editor=null;
//		录入时间
		egu1.getColumn("lursj").setHeader(Locale.lursj_chepb);
		egu1.getColumn("lursj").setWidth(130);
		egu1.getColumn("lursj").setEditor(null);
//		进厂编号
		egu1.getColumn("piaojh").setHeader(Locale.piaojh_chepb);
		egu1.getColumn("piaojh").setWidth(110);
		egu1.getColumn("piaojh").setEditor(null);
//		采样编号
		egu1.getColumn("bianm").setHeader(Locale.caiybm_caiyb);
		egu1.getColumn("bianm").setWidth(90);
		egu1.getColumn("bianm").setEditor(null);
//		车皮号
		egu1.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu1.getColumn("cheph").setWidth(90);
		egu1.getColumn("cheph").setEditor(null);
//		皮重
		egu1.getColumn("piz").setHeader(Locale.piz_chepb);
		egu1.getColumn("piz").setWidth(70);
		egu1.getColumn("piz").setEditor(null);
//		轻车检斤员
		egu1.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu1.getColumn("qingcjjy").setWidth(90);
		egu1.getColumn("qingcjjy").setEditor(null);
//		轻车衡号
		egu1.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu1.getColumn("qingchh").setWidth(80);
		egu1.getColumn("qingchh").setEditor(null);
//		流水号
		ComboBox liush1 = new ComboBox();
		liush1.setTransform("Liush1Select");
		liush1.setWidth(130);
		liush1.setEditable(true);
		liush1.setListeners("select:function(own,rec,index){Ext.getDom('Liush1Select').selectedIndex=index}");
		egu1.addToolbarItem(liush1.getScript());
		
		//egu1.addToolbarItem("{"+new GridButton("刷新","function (){document.getElementById('RefurbishPizButton').click();}").getScript()+"}");
		egu1.addTbarBtn(refurbish);
		egu1.addToolbarButton(GridButton.ButtonType_Save, "SavePizButton");
		egu1.addOtherScript("gridDivPiz_grid.on('rowclick',function(){Mode='sel';DataIndex='PIZ';});");
		setPizGrid(egu1);
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
		if(getExtGrid() == null) {
			return "";
		}
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}
	
	public String getGridScriptPiz() {
		if(getExtGrid() == null) {
			return "";
		}
		if(getPTbmsg()!=null) {
			getPizGrid().addToolbarItem("'->'");
			getPizGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getPTbmsg()+"</marquee>'");
		}
		return getPizGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
//   毛重流水号
	public IDropDownBean getLiushValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean1()==null) {
			if(getLiushModel().getOptionCount()>0) {
				setLiushValue((IDropDownBean)getLiushModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean1();
	}
	public void setLiushValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean1(value);
	}
	
	public IPropertySelectionModel getLiushModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel1()==null) {
			setLiushModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel1();
	}
	public void setLiushModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	
	public void setLiushModels() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,piaojh from chepb where piaojh is not null and chebb_id = "+SysConstant.CHEB_QC+" and maoz=0");
		setLiushModel(new IDropDownModel(sb.toString()));
	}
//	皮重流水号
	public IDropDownBean getLiush1Value() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getLiush1Model().getOptionCount()>0) {
				setLiush1Value((IDropDownBean)getLiush1Model().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setLiush1Value(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getLiush1Model() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setLiush1Models();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setLiush1Model(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setLiush1Models() {
		StringBuffer sb = new StringBuffer();
		sb.append("select id,piaojh from chepb where piaojh is not null and chebb_id = "+SysConstant.CHEB_QC+" and piz=0");
		setLiush1Model(new IDropDownModel(sb.toString()));
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
			init();
		}
	} 
	
	private void init() {
		setLiushValue(null);
		setLiushModel(null);
		setLiushModels();
		setLiush1Value(null);
		setLiush1Model(null);
		setLiush1Models();
		setExtGrid(null);
		setPizGrid(null);
		getSelectData();
	}
}