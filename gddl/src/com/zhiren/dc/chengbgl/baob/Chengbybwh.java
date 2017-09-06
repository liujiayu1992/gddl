package com.zhiren.dc.chengbgl.baob;

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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Chengbybwh extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql=
			"delete from chengbyb where riq = "
			+CurrODate+" and diancxxb_id="+visit.getDiancxxb_id();
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程中发生错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	
	public StringBuffer getBaseSql() {
		Visit visit = (Visit) this.getPage().getVisit();
		String CurrentDate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		
		String FirstDateOfYear = DateUtil.FormatOracleDate(getNianf()+"-01-01");
		StringBuffer sb = new StringBuffer();
		sb.append("select g.id gongysb_id,m.id meikxxb_id, j.id jihkjb_id,\n")
		.append("nvl(c.fenx,'') fenx, round(nvl(sum(jingz),0),2) jingz,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*meij)/sum(jingz)),0),2) meij,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*meijs)/sum(jingz)),0),2) meijs,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*yunj)/sum(jingz)),0),2) yunj,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*yunjs)/sum(jingz)),0),2) yunjs,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*jiaohqzf)/sum(jingz)),0),2) jiaohqzf,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*zaf)/sum(jingz)),0),2) zaf,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*daozzf)/sum(jingz)),0),2) daozzf,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*qitfy)/sum(jingz)),0),2) qitfy,\n")
		.append("round(nvl(decode(sum(jingz),0,0,sum(jingz*Qnet_ar)/sum(jingz)),0),2) Qnet_ar,\n")
		.append("round(nvl(decode(sum(jingz*Qnet_ar),0,0,sum((meij+meijs+yunj+yunjs+jiaohqzf+zaf+daozzf+qitfy)*jingz*29.271)/sum(jingz*Qnet_ar)),0),2) biaomdj,\n")
		.append("round(nvl(decode(sum(jingz*Qnet_ar),0,0,sum((meij+yunj+jiaohqzf+zaf+daozzf+qitfy)*jingz*29.271)/sum(jingz*Qnet_ar)),0),2) buhsbmdj\n")
		
		.append("from \n")
		.append("(select a.gongysb_id, a.meikxxb_id, a.jihkjb_id,'本月' fenx, nvl(b.jingz,0) jingz,\n")
		.append("nvl(b.meij,0) meij,nvl(b.meijs,0) meijs,nvl(b.yunj,0) yunj,\n")
		.append("nvl(b.yunjs,0) yunjs,nvl(b.jiaohqzf,0) jiaohqzf, nvl(b.zaf,0) zaf,\n")
		.append("nvl(b.daozzf,0) daozzf,nvl(b.qitfy,0) qitfy,nvl(b.Qnet_ar,0) Qnet_ar from \n")
		.append("(select distinct f.gongysb_id,f.meikxxb_id,f.jihkjb_id\n")
		.append("        from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfYear).append(" \n")
		.append("and f.daohrq < add_months(").append(CurrentDate).append(",1)\n")
		.append("and f.diancxxb_id=").append(visit.getDiancxxb_id()).append(") a,\n")
		.append("(select f.gongysb_id,f.meikxxb_id,f.jihkjb_id,sum(f.jingz) jingz,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.meij)/sum(f.jingz)) meij,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.meijs)/sum(f.jingz)) meijs,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.yunj)/sum(f.jingz)) yunj,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.yunjs)/sum(f.jingz)) yunjs,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.jiaohqzf)/sum(f.jingz)) jiaohqzf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.zaf)/sum(f.jingz)) zaf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.daozzf)/sum(f.jingz)) daozzf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.qitfy)/sum(f.jingz)) qitfy,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.Qnet_ar)/sum(f.jingz)) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(CurrentDate).append("\n")
		.append("and f.daohrq < add_months(").append(CurrentDate).append(",1)\n")
		.append("and f.diancxxb_id=").append(visit.getDiancxxb_id()).append("\n")
		.append("      group by f.gongysb_id,f.meikxxb_id,f.jihkjb_id) b\n")
		.append("where a.gongysb_id = b.gongysb_id(+) and a.meikxxb_id = b.meikxxb_id(+)\n")
		.append("and a.jihkjb_id = b.jihkjb_id(+)\n")
		.append("union\n")
		.append("select f.gongysb_id,f.meikxxb_id,f.jihkjb_id,'累计' fenx,sum(f.jingz) jingz,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.meij)/sum(f.jingz)) meij,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.meijs)/sum(f.jingz)) meijs,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.yunj)/sum(f.jingz)) yunj,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.yunjs)/sum(f.jingz)) yunjs,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.jiaohqzf)/sum(f.jingz)) jiaohqzf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.zaf)/sum(f.jingz)) zaf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.daozzf)/sum(f.jingz)) daozzf,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.qitfy)/sum(f.jingz)) qitfy,\n")
		.append("decode(sum(f.jingz),0,0,sum(f.jingz*r.Qnet_ar)/sum(f.jingz)) Qnet_ar\n")
		.append("      from fahb f, ruccb r\n")
		.append("where r.id = f.ruccbb_id  and f.daohrq >=").append(FirstDateOfYear).append("\n")
		.append("and f.daohrq < add_months(").append(CurrentDate).append(",1)\n")
		.append("and f.diancxxb_id =").append(visit.getDiancxxb_id()).append("\n")
		.append("      group by f.gongysb_id,f.meikxxb_id,f.jihkjb_id) c,gongysb g,meikxxb m, jihkjb j\n")
		.append("where c.gongysb_id = g.id and c.meikxxb_id = m.id and c.jihkjb_id = j.id\n")
		.append("group by rollup (c.fenx,j.id,g.id,m.id)\n")
		.append("having grouping(m.id) = 0\n")
		.append("order by grouping(j.id) desc,max(j.xuh),max(j.mingc),grouping(g.id) desc ,max(g.xuh),max(g.mingc),\n")
		.append("grouping(m.id) desc,max(m.xuh),max(m.mingc),c.fenx\n");
		return sb;
	}
	
	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrentDate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		StringBuffer sb = new StringBuffer();
		DelData();
		ResultSetList rsl = con.getResultSetList(getBaseSql().toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + getBaseSql());
			setMsg(ErrorMessage.NullResult);
			con.rollBack();
			con.Close();
			return;
		}
		while(rsl.next()) {
			sb.delete(0, sb.length());
			String id = MainGlobal.getNewID(visit.getDiancxxb_id());
			sb.append("insert into chengbyb(id, riq,diancxxb_id, gongysb_id, meikxxb_id, jihkjb_id, fenx, jingz, meij, meijs, yunj, yunjs, jiaohqzf, zaf, daozzf, qitfy, qnet_ar, biaomdj, buhsbmdj) values")
			.append("(").append(id).append(",").append(CurrentDate).append(",").append(visit.getDiancxxb_id()).append(",").append(rsl.getLong("gongysb_id"))
			.append(",").append(rsl.getLong("meikxxb_id")).append(",").append(rsl.getLong("jihkjb_id")).append(",'").append(rsl.getString("fenx")).append("',").append(rsl.getDouble("jingz"))
			.append(",").append(rsl.getDouble("meij")).append(",").append(rsl.getDouble("meijs")).append(",").append(rsl.getDouble("yunj")).append(",").append(rsl.getDouble("yunjs")).append(",")
			.append(rsl.getDouble("jiaohqzf")).append(",").append(rsl.getDouble("zaf")).append(",").append(rsl.getDouble("daozzf")).append(",").append(rsl.getDouble("qitfy")).append(",")
			.append(rsl.getDouble("qnet_ar")).append(",").append(rsl.getDouble("biaomdj")).append(",").append(rsl.getDouble("buhsbmdj")).append(")");
			con.getInsert(sb.toString());
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		StringBuffer sb = new StringBuffer();
		sb.append("select decode(grouping(g.mingc),1,'总计',g.mingc) gongysb_id,\n")
		.append("m.mingc meikxxb_id, j.mingc jihkjb_id, c.fenx, sum(c.jingz) jingz,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(meij*jingz)/sum(jingz)),0),2) meij,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(meijs*jingz)/sum(jingz)),0),2) meijs,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(yunj*jingz)/sum(jingz)),0),2) yunj,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(yunjs*jingz)/sum(jingz)),0),2) yunjs,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(jiaohqzf*jingz)/sum(jingz)),0),2) jiaohqzf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(zaf*jingz)/sum(jingz)),0),2) zaf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(daozzf*jingz)/sum(jingz)),0),2) daozzf,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(qitfy*jingz)/sum(jingz)),0),2) qitfy,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(qnet_ar*jingz)/sum(jingz)),0),2) qnet_ar,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(biaomdj*jingz)/sum(jingz)),0),2) biaomdj,\n")
		.append("round(nvl(decode(sum(c.jingz),0,0,sum(buhsbmdj*jingz)/sum(jingz)),0),2) buhsbmdj\n")
		.append("from chengbyb c, gongysb g, meikxxb m, jihkjb j\n")
		.append("where c.gongysb_id = g.id and c.meikxxb_id = m.id\n")
		.append("and c.jihkjb_id = j.id and c.diancxxb_id = ").append(visit.getDiancxxb_id()).append("\n")
		.append("and c.riq =").append(CurrODate).append(" \n")
		.append("group by rollup(c.fenx,g.mingc,m.mingc,j.mingc)\n")
		.append("having grouping(c.fenx)=0 and grouping(g.mingc)=1 or grouping(j.mingc)=0\n")
		.append("order by grouping(g.mingc) desc,max(g.xuh),g.mingc,max(m.xuh),\n")
		.append("m.mingc,max(j.xuh),j.mingc,c.fenx\n");
		
		JDBCcon con = new JDBCcon();
				
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// /设置显示列名称
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(100);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
		egu.getColumn("fenx").setWidth(40);
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("meij").setHeader(Locale.meij_chengbrb);
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setHeader(Locale.meijs_chengbrb);
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("yunj").setHeader(Locale.yunj_chengbrb);
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setHeader(Locale.yunjs_chengbrb);
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("jiaohqzf").setHeader(Locale.jiaohqzf_chengbrb);
		egu.getColumn("jiaohqzf").setWidth(70);
		egu.getColumn("zaf").setHeader(Locale.zaf_chengbrb);
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("daozzf").setHeader(Locale.daozzf_chengbrb);
		egu.getColumn("daozzf").setWidth(60);
		egu.getColumn("qitfy").setHeader(Locale.qitfy_chengbrb);
		egu.getColumn("qitfy").setWidth(60);
		egu.getColumn("qnet_ar").setHeader(Locale.Qnet_ar_chengbrb);
		egu.getColumn("qnet_ar").setWidth(60);
		egu.getColumn("biaomdj").setHeader(Locale.biaomdj_chengbrb);
		egu.getColumn("biaomdj").setWidth(60);
		egu.getColumn("buhsbmdj").setHeader(Locale.buhsbmdj_chengbrb);
		egu.getColumn("buhsbmdj").setWidth(90);
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(60);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("月份");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		//if(isLocked) {
		//	gbc.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
		//}
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
//		if(isLocked) {
//			gbd.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
//		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"年"+getYuef()+"月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
		if(getExtGrid() == null) {
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
			visit.setList1(null);
			setRiq();
			getSelectData();
		}
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString2();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	
	 // 年份下拉框
    private static IPropertySelectionModel _NianfModel;
    public IPropertySelectionModel getNianfModel() {
        if (_NianfModel == null) {
            getNianfModels();
        }
        return _NianfModel;
    }
    
	private IDropDownBean _NianfValue;
	
    public IDropDownBean getNianfValue() {
        if (_NianfValue == null) {
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }
}
