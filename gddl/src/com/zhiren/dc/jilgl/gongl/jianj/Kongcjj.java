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

public class Kongcjj extends BasePage implements PageValidateListener {
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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	入厂流水号列表
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
		sb.append("select id,piaojh from chepb where piaojh is not null and chebb_id = "+SysConstant.CHEB_QC+" and piz=0");
		setLiushModel(new IDropDownModel(sb.toString()));
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
		StringBuffer sb = new StringBuffer();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Kongcjj.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
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
				WriteLog.writeErrorLog(ErrorMessage.UpdateDatabaseFail+"SQL:"+sb);
				setMsg(ErrorMessage.Kongcjj001);
				return;
			}
			
//			根据单车id 调用jilcz 中CountChepbYuns 方法计算单车的运损盈亏
			flag = Jilcz.CountChepbYuns(con, id, SysConstant.HEDBZ_YJJ);
			if(flag==-1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjj002);
				setMsg(ErrorMessage.Kongcjj002);
				return;
			}
//			根据车皮所在fahid 调用Jilcz 中 updateFahb 方法更新发货表
			flag = Jilcz.updateFahb(con, fahbid);
			if(flag == -1){
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjj003);
				setMsg(ErrorMessage.Kongcjj003);
				return;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Kongcjj004);
				setMsg(ErrorMessage.Kongcjj004);
				return;
			}
		}
		con.Close();
		setMsg("保存成功！");
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
	}

	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long cid = -1;
		if(getLiushValue() != null) {
			cid = getLiushValue().getId();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("select c.id,c.fahb_id,c.biaoz,to_char(c.lursj,'yyyy-mm-dd hh24:mi:ss') lursj,\n");
		sb.append("c.piaojh,y.bianm,c.cheph,c.maoz,c.piz,c.zhongcjjy,c.qingchh,nvl('"+visit.getRenymc()+"','') qingcjjy\n");
		sb.append("from chepb c, fahb f, caiyb y \n");
		sb.append("where c.fahb_id = f.id and f.zhilb_id = y.zhilb_id and c.piz = 0\n");
		sb.append("and c.chebb_id = ").append(SysConstant.CHEB_QC).append(" and c.id = ").append(cid);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		double maoz = 0;
		if(rsl.next()){
			maoz = rsl.getDouble("maoz");
		}
		rsl.beforefirst();
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.GridselModel_Row_single);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight(453);
		egu.addPaging(16);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("fahb_id").setHidden(true);
		egu.getColumn("fahb_id").editor=null;
		if(maoz==0){
			egu.getColumn("maoz").setHidden(true);
			egu.getColumn("maoz").editor=null;
		}else{
			egu.getColumn("maoz").setHeader(Locale.maoz_chepb);
			egu.getColumn("maoz").setWidth(70);
			egu.getColumn("maoz").setEditor(null);
		}
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
//		皮重
		egu.getColumn("piz").setHeader(Locale.piz_chepb);
		egu.getColumn("piz").setWidth(70);
		egu.getColumn("piz").setEditor(null);
//		重车检斤员
		egu.getColumn("zhongcjjy").setHeader(Locale.zhongcjjy_chepb);
		egu.getColumn("zhongcjjy").setWidth(90);
		egu.getColumn("zhongcjjy").setEditor(null);
//		轻车检斤员
		egu.getColumn("qingcjjy").setHeader(Locale.qingcjjy_chepb);
		egu.getColumn("qingcjjy").setWidth(90);
		egu.getColumn("qingcjjy").setEditor(null);
//		轻车衡号
		egu.getColumn("qingchh").setHeader(Locale.qingchh_chepb);
		egu.getColumn("qingchh").setWidth(80);
		egu.getColumn("qingchh").setEditor(null);
		
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
			visit.setActivePageName(getPageName().toString());
			init();
		}
	} 
	
	private void init() {
		setLiushValue(null);
		setLiushModel(null);
		setLiushModels();
		setExtGrid(null);
		getSelectData();
	}
}