package com.zhiren.gangkjy.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
import com.zhiren.common.IDropDownBean;
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
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Diaoylr extends BasePage implements PageValidateListener{
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);;
	}
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			riq = DateUtil.FormatDate(new Date());
		}
		return riq;
	}
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
//	 页面变化记录
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
	
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CopyChick = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyChick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			setOriRiq(getRiq());
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}else if (_CopyChick) {
			_CopyChick = false;
			copy();
			getSelectData();
		}
	}
	
	public void copy(){
		JDBCcon con = new JDBCcon();
		String riq = getRiq();
		Visit visit = (Visit) getPage().getVisit();
		long luncxxb_id=0;
		long zhuangtb_id=0;
		double shul=0;
		long pinzb_id=0;
		long xiaosgysb_id=0;
		String caozy="";
		String beiz="";
		try{
		ResultSet rs = con.getResultSet("select * from luncdyb where to_char(riq+1,'yyyy-mm-dd')='"+riq+"'");
		while (rs.next()){
			luncxxb_id=rs.getLong("luncxxb_id");
			zhuangtb_id=rs.getLong("zhuangtb_id");
			shul=rs.getDouble("shul");
			pinzb_id=rs.getLong("pinzb_id");
			xiaosgysb_id=rs.getLong("xiaosgysb_id");
			caozy=rs.getString("caozy");
			beiz=rs.getString("beiz");
			con.getInsert("insert into luncdyb values(getNewId("+ visit.getDiancxxb_id()+"),"+luncxxb_id+","+zhuangtb_id
					 + ",to_date('"+riq+"','yyyy-mm-dd'),"+shul+","+pinzb_id+","+xiaosgysb_id+",'"+caozy+"','"+beiz+"')"); 
			
			}
		}catch(Exception e){
			
		}
		con.Close();
	}
	public void save() {
		String sSql = "";
		long id = 0;
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "zhuangclr.Save 中 getExtGrid().getDeleteResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getInt("id");
			sSql = "delete from luncdyb where id=" + id;
			flag = con.getDelete(sSql);
			if (flag == -1) {
				con.rollBack();
				con.Close();				
			}
		}
		rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "zhuangclr.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		while (rsl.next()) {
			id = rsl.getInt("id");
			if (id == 0) {
				sSql = "insert into luncdyb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ (getExtGrid().getColumn("luncxxb_id").combo).getBeanId(rsl.getString("luncxxb_id")) + "," 
					+ (getExtGrid().getColumn("zhuangtb_id").combo).getBeanId(rsl.getString("zhuangtb_id"))+ ",to_date('"
					+ rsl.getString("riq")+" "+rsl.getInt("riqshi")+":"+rsl.getInt("riqfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ rsl.getDouble("shul")+","+(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id")) + "," 
					+ (getExtGrid().getColumn("xiaosgysb_id").combo).getBeanId(rsl.getString("xiaosgysb_id")) + ",'"
					+ rsl.getString("caozy")+ "','"+rsl.getString("beiz")+"')";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				sSql = "update luncdyb set "
					+ " luncxxb_id="+(getExtGrid().getColumn("luncxxb_id").combo).getBeanId(rsl.getString("luncxxb_id"))
					+ ", zhuangtb_id="+ (getExtGrid().getColumn("zhuangtb_id").combo).getBeanId(rsl.getString("zhuangtb_id"))
					+ ", riq=to_date('"+ rsl.getString("riq")+" "+rsl.getInt("riqshi")+":"+rsl.getInt("riqfen")+":00','yyyy-mm-dd hh24:mi:ss'),shul="
					+ rsl.getDouble("shul") +",pinzb_id="+(getExtGrid().getColumn("pinzb_id").combo).getBeanId(rsl.getString("pinzb_id"))
					+ ",xiaosgysb_id="+(getExtGrid().getColumn("xiaosgysb_id").combo).getBeanId(rsl.getString("xiaosgysb_id"))
					+ ",caozy='"+rsl.getString("caozy")+"',beiz='"+rsl.getString("beiz")+"' where id="+id ;
					
				flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			}
		}
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
		String sSql = "select dy.id,lc.mingc as luncxxb_id,riq,to_char(riq,'hh24') as riqshi,to_char(riq,'mi') as riqfen,zt.mingc as zhuangtb_id,shul,vwss.mingc as xiaosgysb_id,\n"
					+ "pz.mingc as pinzb_id,dy.caozy,dy.beiz\n"
					+ "from luncdyb dy,luncxxb lc,zhuangtb zt,pinzb pz,vwxuqdw vwss\n"
					+ "where dy.zhuangtb_id=zt.id and dy.pinzb_id=pz.id and dy.xiaosgysb_id=vwss.id\n"
					+ "and dy.luncxxb_id=lc.id\n"
					+ "and to_char(riq,'yyyy-mm-dd')='"+getRiq()+"'";
		
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("luncxxb_id").setHeader("船名");
		egu.getColumn("luncxxb_id").setWidth(100);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(getRiq());
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("riqshi").setHeader("时");
		egu.getColumn("riqshi").setWidth(40);
		egu.getColumn("riqfen").setHeader("分");
		egu.getColumn("riqfen").setWidth(40);
		egu.getColumn("xiaosgysb_id").setHeader("流向(销售供应商)");
		egu.getColumn("xiaosgysb_id").setWidth(100);
		egu.getColumn("zhuangtb_id").setHeader("状态");
		egu.getColumn("zhuangtb_id").setWidth(100);
		egu.getColumn("shul").setHeader("装船数量");
		egu.getColumn("shul").setWidth(80);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(60);
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setWidth(80);
		egu.getColumn("caozy").setEditor(null);
		egu.getColumn("caozy").setHidden(true);
		egu.getColumn("caozy").setDefaultValue(visit.getRenymc());
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("luncxxb_id").setEditor(c1);
		c1.setEditable(true);
		String luncSql = "select id,mingc from luncxxb order by mingc";
		egu.getColumn("luncxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(luncSql));

		ComboBox c2 = new ComboBox();
		egu.getColumn("xiaosgysb_id").setEditor(c2);
		c2.setEditable(true);
		String xxgysSql = "select id,mingc from vwxuqdw order by mingc";
		egu.getColumn("xiaosgysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(xxgysSql));
		
		ComboBox c3 = new ComboBox();
		c3.setEditable(true);
		egu.getColumn("zhuangtb_id").setEditor(c3);
		c1.setEditable(true);
		String bowSql = "select id,mingc from zhuangtb order by mingc";
		egu.getColumn("zhuangtb_id").setComboEditor(egu.gridId,
				new IDropDownModel(bowSql));
		
		ComboBox c4 = new ComboBox();
		c4.setEditable(true);
		egu.getColumn("pinzb_id").setEditor(c4);
		c1.setEditable(true);
		String pinzSql = "select id,mingc from pinzb where leib='煤' order by mingc";
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel(pinzSql));
		
		egu.addTbarText("日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","Form0");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		egu.addToolbarButton(GridButton.ButtonType_Copy, "QumButton");
		StringBuffer cpb = new StringBuffer();
		cpb.append("function(){").append(
		"document.getElementById('CopyButton').click();}");
		GridButton qum = new GridButton("复制", cpb.toString());
		qum.setIcon(SysConstant.Btn_Icon_Copy);
		egu.addTbarBtn(qum);
		setExtGrid(egu);
		con.Close();
	}
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
		}
		init();
	}
	private void init() {
		setOriRiq(getRiq());
		getSelectData();
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
