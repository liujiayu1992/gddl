package com.zhiren.gangkjy.jiexdy.zhuangcgl;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhuangclr extends BasePage implements PageValidateListener{
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
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _QumChick = false;
	public void QumButton(IRequestCycle cycle) {
		_QumChick = true;
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
		}else if (_QumChick) {
			_QumChick = false;
			((Visit) getPage().getVisit()).setString11(getChange());
			((Visit) getPage().getVisit()).setString2(((Visit) getPage().getVisit()).getActivePageName());
			cycle.activate("Qumxx");
		}
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
			sSql = "delete from zhuangcb where id=" + id;
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
				sSql = "insert into zhuangcb values(getNewId(" + visit.getDiancxxb_id() + "),"
					+ rsl.getInt("diancxxb_id") + ",'"
					+ rsl.getString("hangc")+"',"+(getExtGrid().getColumn("luncxxb_id").combo).getBeanId(rsl.getString("luncxxb_id"))+","
					+ (getExtGrid().getColumn("xiaosgysb_id").combo).getBeanId(rsl.getString("xiaosgysb_id")) + ","+rsl.getInt("dunw") + ",'"
					+ rsl.getString("jihcq") + "',to_date('"+rsl.getString("digsj")+" "+rsl.getInt("digsjshi")+":"+rsl.getInt("digsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rsl.getString("kaobsj")+" "+rsl.getInt("kaobsjshi")+":"+rsl.getInt("kaobsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ getExtGrid().getColumn("bowxxb_id").combo.getBeanId(rsl.getString("bowxxb_id")) + ","
					+ "to_date('"+rsl.getString("zhuangckssj")+" "+rsl.getInt("zhuangckssjshi")+":"+rsl.getInt("zhuangckssjfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rsl.getString("zhuangcjssj")+" "+rsl.getInt("zhuangcjssjshi")+":"+rsl.getInt("zhuangcjssjfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rsl.getString("ligsj")+" "+rsl.getInt("ligsjshi")+":"+rsl.getInt("ligsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),to_date('"+rsl.getString("daohrq")+"','yyyy-mm-dd'),"
					+ rsl.getDouble("zhuangcl")+","+ "to_date('"+rsl.getString("daodgdhsj")+" "+rsl.getInt("daodgdhsjshi")+":"+rsl.getInt("daodgdhsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),"
					+ "to_date('"+rsl.getString("daodglgsj")+" "+rsl.getInt("daodglgsjshi")+":"+rsl.getInt("daodglgsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),'"
					+ rsl.getString("caozy") +"',sysdate,'"+(getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl.getString("zhuangt"))+"','"+rsl.getString("beiz")
					+ "',0 "+","+(getExtGrid().getColumn("pinz").combo).getBeanId(rsl.getString("pinz"))+")";
				flag = con.getInsert(sSql);
				if (flag == -1) {
					con.rollBack();
					con.Close();
				}
			} else {
				sSql = "update zhuangcb set "
//					+ " diancxxb_id=" + rsl.getInt("diancxxb_id") + ","					
					+ " hangc='" + rsl.getString("hangc") + "',luncxxb_id="+(getExtGrid().getColumn("luncxxb_id").combo).getBeanId(rsl.getString("luncxxb_id"))
					+ ",xiaosgysb_id=" + (getExtGrid().getColumn("xiaosgysb_id").combo).getBeanId(rsl.getString("xiaosgysb_id")) + ",dunw="+rsl.getInt("dunw")
					+ ",jihcq='" + rsl.getString("jihcq") + "',digsj=to_date('"+rsl.getString("digsj")+" "+rsl.getInt("digsjshi")+":"+rsl.getInt("digsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),kaobsj=to_date('"
					+ rsl.getString("kaobsj")+" "+rsl.getInt("kaobsjshi")+":"+rsl.getInt("kaobsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),bowxxb_id="
					+ getExtGrid().getColumn("bowxxb_id").combo.getBeanId(rsl.getString("bowxxb_id"))+",zhuangckssj=to_date('"
					+ rsl.getString("zhuangckssj")+" "+rsl.getInt("zhuangckssjshi")+":"+rsl.getInt("zhuangckssjfen")+":00','yyyy-mm-dd hh24:mi:ss'),zhuangcjssj=to_date('"
					+ rsl.getString("zhuangcjssj")+" "+rsl.getInt("zhuangcjssjshi")+":"+rsl.getInt("zhuangcjssjfen")+":00','yyyy-mm-dd hh24:mi:ss'),ligsj=to_date('"
					+ rsl.getString("ligsj")+" "+rsl.getInt("ligsjshi")+":"+rsl.getInt("ligsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),daohrq=to_date('"+rsl.getString("daohrq")+"','yyyy-mm-dd'),zhuangcl="+
					+ rsl.getDouble("zhuangcl")+",daodgdhsj=to_date('"+rsl.getString("daodgdhsj")+" "+rsl.getInt("daodgdhsjshi")+":"+rsl.getInt("daodgdhsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),daodglgsj=to_date('"
					+ rsl.getString("daodglgsj")+" "+rsl.getInt("daodglgsjshi")+":"+rsl.getInt("daodglgsjfen")+":00','yyyy-mm-dd hh24:mi:ss'),caozy='"
					+ rsl.getString("caozy")+"',caozsj=sysdate,zhuangt='"
					+ (getExtGrid().getColumn("zhuangt").combo).getBeanId(rsl.getString("zhuangt"))+"',beiz='"+rsl.getString("beiz")+"',"
					+ " pinzb_id=" + getExtGrid().getColumn("pinz").combo.getBeanId(rsl.getString("pinz")) 
					+ " where id=" + id;
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
		if (visit.isJTUser()) {
			str = "";
		} else {
			if (visit.isGSUser()) {
				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
						+ getTreeid() + ")";
			} else {
				str = "and dc.id = " + getTreeid() + "";
			}
		} 
//		int treejib = this.getDiancTreeJib();
//		if (treejib == 1) {// 选集团时刷新出所有的电厂
//			str = "";
//		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
//			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//					+ getTreeid() + ")";
//		} else if (treejib == 3) {// 选电厂只刷新出该电厂
//			str = "and dc.id = " + getTreeid() + "";
//		}
		String sSql = "select z.id,z.diancxxb_id as diancxxb_id,lc.mingc as luncxxb_id,vwxq.mingc as xiaosgysb_id,bw.mingc as bowxxb_id, \n"
					+ "z.hangc,z.dunw,p.mingc as pinz,z.zhuangcl, z.jihcq,z.digsj,to_char(z.digsj,'hh24') as digsjshi,to_char(z.digsj,'mi') as digsjfen,\n" 
					+ "z.kaobsj,to_char(z.kaobsj,'hh24') as kaobsjshi,to_char(z.kaobsj,'mi') as kaobsjfen,\n"
					+ "z.zhuangckssj,to_char(z.zhuangckssj,'hh24') as zhuangckssjshi,to_char(z.zhuangckssj,'mi') zhuangckssjfen,\n"
					+ "z.zhuangcjssj,to_char(z.zhuangcjssj,'hh24') as zhuangcjssjshi,to_char(z.zhuangcjssj,'mi') as zhuangcjssjfen, \n"
					+ "z.ligsj,to_char(z.ligsj,'hh24') as ligsjshi,to_char(z.ligsj,'mi') as ligsjfen,z.daohrq,\n"
					+ "z.daodgdhsj,to_char(z.daodgdhsj,'hh24') as daodgdhsjshi,to_char(z.daodgdhsj,'mi') as daodgdhsjfen,\n"
					+ "z.daodglgsj,to_char(z.daodglgsj,'hh24') as daodglgsjshi,to_char(z.daodglgsj,'mi') as daodglgsjfen,z.caozy,decode(z.zhuangt,1,'是','否') as zhuangt,z.beiz\n"
					+ "from zhuangcb z,vwxuqdw vwxq,luncxxb lc,diancxxb dc,bowxxb bw,vwpinz p \n"
					+ "where z.diancxxb_id = dc.id and z.luncxxb_id = lc.id and z.pinzb_id = p.id \n"
					+ "and z.xiaosgysb_id = vwxq.id and z.bowxxb_id = bw.id \n"
					+ "and to_char(z.zhuangcjssj,'yyyy-mm-dd') = '"+getRiq()+"'"
					+ str + " order by z.id";
		
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
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(getTreeid()));
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("luncxxb_id").setHeader("船名");
		egu.getColumn("luncxxb_id").setWidth(60);
		egu.getColumn("xiaosgysb_id").setHeader("流向(销售供应商)");
		egu.getColumn("xiaosgysb_id").setWidth(100);
		egu.getColumn("hangc").setHeader("航次");
		egu.getColumn("hangc").setWidth(40);
		egu.getColumn("dunw").setHeader("吨位");
		egu.getColumn("dunw").setWidth(40);
		egu.getColumn("pinz").setHeader("品种");
		egu.getColumn("pinz").setWidth(60);
		egu.getColumn("zhuangcl").setHeader("装船量");
		egu.getColumn("zhuangcl").setWidth(60);
		egu.getColumn("zhuangcl").setEditor(null);
		egu.getColumn("zhuangcl").setDefaultValue("0");
		egu.getColumn("jihcq").setHeader("计划船期");
		egu.getColumn("jihcq").setWidth(60);
		egu.getColumn("digsj").setHeader("抵港时间");
		egu.getColumn("digsj").setWidth(80);
		egu.getColumn("digsjshi").setHeader("时");
		egu.getColumn("digsjshi").setWidth(30);
		egu.getColumn("digsjfen").setHeader("分");
		egu.getColumn("digsjfen").setWidth(30);
		egu.getColumn("kaobsj").setHeader("靠泊时间");
		egu.getColumn("kaobsj").setWidth(80);
		egu.getColumn("kaobsjshi").setHeader("时");
		egu.getColumn("kaobsjshi").setWidth(40);
		egu.getColumn("kaobsjfen").setHeader("分");
		egu.getColumn("kaobsjfen").setWidth(40);
		egu.getColumn("bowxxb_id").setHeader("泊位");
		egu.getColumn("bowxxb_id").setWidth(60);
		egu.getColumn("zhuangckssj").setHeader("装船开始时间");
		egu.getColumn("zhuangckssj").setWidth(80);
		egu.getColumn("zhuangckssjshi").setHeader("时");
		egu.getColumn("zhuangckssjshi").setWidth(40);
		egu.getColumn("zhuangckssjfen").setHeader("分");
		egu.getColumn("zhuangckssjfen").setWidth(40);
		egu.getColumn("zhuangcjssj").setHeader("装船时间");
		egu.getColumn("zhuangcjssj").setWidth(80);
		egu.getColumn("zhuangcjssjshi").setHeader("时");
		egu.getColumn("zhuangcjssjshi").setWidth(40);
		egu.getColumn("zhuangcjssjfen").setHeader("分");
		egu.getColumn("zhuangcjssjfen").setWidth(40);
		egu.getColumn("ligsj").setHeader("离港时间");
		egu.getColumn("ligsj").setWidth(80);
		egu.getColumn("ligsjshi").setHeader("时");
		egu.getColumn("ligsjshi").setWidth(40);
		egu.getColumn("ligsjfen").setHeader("分");
		egu.getColumn("ligsjfen").setWidth(40);
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setWidth(80);

		egu.getColumn("daodgdhsj").setHeader("到达港到货时间");
		egu.getColumn("daodgdhsj").setWidth(80);
		egu.getColumn("daodgdhsjshi").setHeader("时");
		egu.getColumn("daodgdhsjshi").setWidth(40);
		egu.getColumn("daodgdhsjfen").setHeader("分");
		egu.getColumn("daodgdhsjfen").setWidth(40);
		egu.getColumn("daodglgsj").setHeader("到达港离港时间");
		egu.getColumn("daodglgsj").setWidth(80);
		egu.getColumn("daodglgsjshi").setHeader("时");
		egu.getColumn("daodglgsjshi").setWidth(40);
		egu.getColumn("daodglgsjfen").setHeader("分");
		egu.getColumn("daodglgsjfen").setWidth(40);
		egu.getColumn("caozy").setHeader("操作员");
		egu.getColumn("caozy").setWidth(50);
		egu.getColumn("caozy").setDefaultValue(visit.getRenymc());
		egu.getColumn("zhuangt").setHeader("状态");
		egu.getColumn("zhuangt").setWidth(50);
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(80);
		
		
		List lsShi = new ArrayList();
		for(int i=0;i<24;i++){
			if(i<10){
				lsShi.add(new IDropDownBean(i, "0"+i));
			}else{
				lsShi.add(new IDropDownBean(i, ""+i));
			}
		}
		List lsFen = new ArrayList();
		for(int i=0;i<60;i++){
			if(i<10){
				lsFen.add(new IDropDownBean(i, "0"+i));
			}else{
				lsFen.add(new IDropDownBean(i, ""+i));
			}
		}
		
		
		egu.getColumn("digsjshi").setHeader("时");
		ComboBox digsjshi = new ComboBox();
		egu.getColumn("digsjshi").setEditor(digsjshi);
		digsjshi.setEditable(true);
		egu.getColumn("digsjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("digsjshi").setDefaultValue("00");
		
		egu.getColumn("digsjfen").setHeader("分");
		ComboBox digsjfen = new ComboBox();
		egu.getColumn("digsjfen").setEditor(digsjfen);
		digsjfen.setEditable(true);
		egu.getColumn("digsjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("digsjfen").setDefaultValue("00");
		
		egu.getColumn("kaobsjshi").setHeader("时");
		ComboBox kaobsjshi = new ComboBox();
		egu.getColumn("kaobsjshi").setEditor(kaobsjshi);
		kaobsjshi.setEditable(true);
		egu.getColumn("kaobsjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("kaobsjshi").setDefaultValue("00");
		egu.getColumn("kaobsjfen").setHeader("分");
		ComboBox kaobsjfen = new ComboBox();
		egu.getColumn("kaobsjfen").setEditor(kaobsjfen);
		kaobsjfen.setEditable(true);
		egu.getColumn("kaobsjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("kaobsjfen").setDefaultValue("00");
		egu.getColumn("zhuangckssjshi").setHeader("时");
		ComboBox zhuangckssjshi = new ComboBox();
		egu.getColumn("zhuangckssjshi").setEditor(zhuangckssjshi);
		zhuangckssjshi.setEditable(true);
		egu.getColumn("zhuangckssjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("zhuangckssjshi").setDefaultValue("00");
		egu.getColumn("zhuangckssjfen").setHeader("分");
		ComboBox zhuangckssjfen = new ComboBox();
		egu.getColumn("zhuangckssjfen").setEditor(zhuangckssjfen);
		zhuangckssjfen.setEditable(true);
		egu.getColumn("zhuangckssjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("zhuangckssjfen").setDefaultValue("00");
		egu.getColumn("zhuangcjssjshi").setHeader("时");
		ComboBox zhuangcjssjshi = new ComboBox();
		egu.getColumn("zhuangcjssjshi").setEditor(zhuangcjssjshi);
		zhuangcjssjshi.setEditable(true);
		egu.getColumn("zhuangcjssjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("zhuangcjssjshi").setDefaultValue("00");
		egu.getColumn("zhuangcjssjfen").setHeader("分");
		ComboBox zhuangcjssjfen = new ComboBox();
		egu.getColumn("zhuangcjssjfen").setEditor(zhuangcjssjfen);
		zhuangcjssjfen.setEditable(true);
		egu.getColumn("zhuangcjssjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("zhuangcjssjfen").setDefaultValue("00");
		egu.getColumn("ligsjshi").setHeader("时");
		ComboBox ligsjshi = new ComboBox();
		egu.getColumn("ligsjshi").setEditor(ligsjshi);
		ligsjshi.setEditable(true);
		egu.getColumn("ligsjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("ligsjshi").setDefaultValue("00");
		egu.getColumn("ligsjfen").setHeader("分");
		ComboBox ligsjfen = new ComboBox();
		egu.getColumn("ligsjfen").setEditor(ligsjfen);
		ligsjfen.setEditable(true);
		egu.getColumn("ligsjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("ligsjfen").setDefaultValue("00");
		egu.getColumn("daodgdhsjshi").setHeader("时");
		ComboBox daodgdhsjshi = new ComboBox();
		egu.getColumn("daodgdhsjshi").setEditor(daodgdhsjshi);
		daodgdhsjshi.setEditable(true);
		egu.getColumn("daodgdhsjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("daodgdhsjshi").setDefaultValue("00");
		egu.getColumn("daodgdhsjfen").setHeader("分");
		ComboBox daodgdhsjfen = new ComboBox();
		egu.getColumn("daodgdhsjfen").setEditor(daodgdhsjfen);
		daodgdhsjfen.setEditable(true);
		egu.getColumn("daodgdhsjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("daodgdhsjfen").setDefaultValue("00");
		egu.getColumn("daodglgsjshi").setHeader("时");
		ComboBox daodglgsjshi = new ComboBox();
		egu.getColumn("daodglgsjshi").setEditor(daodglgsjshi);
		daodglgsjshi.setEditable(true);
		egu.getColumn("daodglgsjshi").setComboEditor(egu.gridId,
				new IDropDownModel(lsShi));
		egu.getColumn("daodglgsjshi").setDefaultValue("00");
		egu.getColumn("daodglgsjfen").setHeader("分");
		ComboBox daodglgsjfen = new ComboBox();
		egu.getColumn("daodglgsjfen").setEditor(daodglgsjfen);
		daodglgsjfen.setEditable(true);
		egu.getColumn("daodglgsjfen").setComboEditor(egu.gridId,
				new IDropDownModel(lsFen));
		egu.getColumn("daodglgsjfen").setDefaultValue("00");
		
		ComboBox c1 = new ComboBox();
		egu.getColumn("luncxxb_id").setEditor(c1);
		c1.setEditable(true);
		String luncSql = "select id,mingc from luncxxb order by mingc";
		egu.getColumn("luncxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(luncSql));

		ComboBox c2 = new ComboBox();
		egu.getColumn("xiaosgysb_id").setEditor(c2);
		c1.setEditable(true);
		String xxgysSql = "select id,mingc from vwxuqdw order by mingc";
		egu.getColumn("xiaosgysb_id").setComboEditor(egu.gridId,
				new IDropDownModel(xxgysSql));
		
		ComboBox c3 = new ComboBox();
		egu.getColumn("bowxxb_id").setEditor(c3);
		c1.setEditable(true);
		String bowSql = "select id,mingc from bowxxb order by mingc";
		egu.getColumn("bowxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel(bowSql));
		
		List ls = new ArrayList();
		ls.add(new IDropDownBean(1, "是"));
		ls.add(new IDropDownBean(2, "否"));
		ComboBox c4 = new ComboBox();
		egu.getColumn("zhuangt").setEditor(c4);
		c4.setEditable(true);
		egu.getColumn("zhuangt").setComboEditor(egu.gridId,
				new IDropDownModel(ls));
		egu.getColumn("zhuangt").setDefaultValue("是");
		
		ComboBox c5 = new ComboBox();
		egu.getColumn("pinz").setEditor(c5);
		c5.setEditable(true);
		String pz = "select id,mingc from vwpinz order by mingc";
		egu.getColumn("pinz").setComboEditor(egu.gridId,
				new IDropDownModel(pz));
		
//		if (treejib == 1) {
//			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select dc.id,dc.mingc from diancxxb dc where dc.jib=3 order by dc.mingc"));
//			egu.getColumn("diancxxb_id").setReturnId(true);
//
//		} else if (treejib == 2) {
//			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb where fuid=" + getTreeid() + " order by mingc"));
//			egu.getColumn("diancxxb_id").setReturnId(true);
//		} else {
//			egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, 
//					new IDropDownModel("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc"));
//			ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id="+getTreeid()+" order by mingc");			
//			String mingc="";
//			if(r.next()){
//				mingc=r.getString("mingc");
//			}
//			egu.getColumn("diancxxb_id").setHidden(true);
//			egu.getColumn("diancxxb_id").setDefaultValue(mingc);
//		}
		egu.addTbarText("装船日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");
		dStart.setValue(getRiq());
		egu.addToolbarItem(dStart.getScript());
		egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
//		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		String DelCondition="var rec=gridDiv_sm.getSelected();if(rec.get('ZHUANGCL')!='0'){Ext.MessageBox.alert('提示信息','已取煤数据不能删除!');return;}\n";
		IGridButton delete = new IGridButton(GridButton.ButtonType_Delete,
				"gridDiv", egu.getGridColumns(), "",DelCondition);
		egu.addTbarBtn(delete);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton qum = new GridButton("取煤", "Qum");
		qum.setIcon(SysConstant.Btn_Icon_SelSubmit);
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
		visit.setString2("");
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
	
	private	class IGridButton extends GridButton{
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
