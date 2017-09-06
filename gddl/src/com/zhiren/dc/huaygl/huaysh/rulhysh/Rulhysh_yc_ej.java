package com.zhiren.dc.huaygl.huaysh.rulhysh;

/**
 * @author 王总兵
 * @version 2010-5-11 
 */


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
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

import com.zhiren.dc.huaygl.Caiycl;

public class Rulhysh_yc_ej extends BasePage implements PageValidateListener {
	private String CustomSetKey = "Rulhyejsh";
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO Auto-generated method stub
		super.initialize();
		setMsg(null);
	}
	
	private boolean xiansztq = true;

	private boolean xiansztl = true;

	// 保存数据list
	public List getEditValues() {
		return ((Visit) getPage().getVisit()).getList1();
	}

	public void setEditValues(List editList) {
		((Visit) getPage().getVisit()).setList1(editList);
	}

	// 记录页面选中行的内容
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}

	

	private String Parameters;// 记录ID

	public String getParameters() {

		return Parameters;
	}

	public void setParameters(String value) {

		Parameters = value;
	}

	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {

		_RefurbishChick = true;
	}
	private boolean _ShowChick = false;
	public void ShowButton(IRequestCycle cycle) {
		_ShowChick = true;
	}
	
	
	private void Show(IRequestCycle cycle) {
		if(getChange()==null || "".equals(getChange())) {
			setMsg("请选择数据进行打印！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String caiyid="";
		while(rsl.next()) {
			caiyid+=""+rsl.getString("id")+",";
			
			//this.setDiancxxb_id(rsl.getString("diancxxb_id"));
		}
		String aa=caiyid.substring(0,caiyid.length()-1);
		((Visit)this.getPage().getVisit()).setString1(aa);
		
		cycle.activate("Fenycx_dy");
	}
	
	
	//审核
	public void Save() {
		String tableName = "rulmzlb";
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {

			sql.append("update ").append(tableName).append(" set ");
			sql.append("erjshzt").append(" = 1 ,shenhryej='"+visit.getRenymc()+"',shenhsjej=sysdate  where id =");
			sql.append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		this.setMsg("二级审核成功!");
		con.Close();
	}
	
	//回退
	public void Huit() {
		String tableName = "rulmzlb";
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sql = new StringBuffer("begin \n");

		ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(getChange());
		while (mdrsl.next()) {
			

			sql.append("update ").append(tableName).append(" set ");
			sql.append("shenhzt").append(" = 1 , shenhryyj='', shenhsjyj=null ");
			sql.append(" where id =").append(mdrsl.getString("ID")).append(
					";\n");
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		this.setMsg("二级回退成功!");
		con.Close();
	}
	
	
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}
	
	public void submit(IRequestCycle cycle) {

		
		if (_RefurbishChick) {
			_RefurbishChick = false;

			getSelectData();
		}
		if (_ShowChick) {
			_ShowChick = false;
			Show(cycle);
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_HuitChick) {
			_HuitChick = false;
			Huit();
			getSelectData();
		}
	}

	public void getSelectData() {

		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rs = new ResultSetList();
		
		
		//是否根据diancxxb_id过滤
		String IsGulsql = "select zhi from xitxxb where mingc = '入炉审核是否根据diancxxb_id过滤'  and zhuangt = 1";
		String IsGul = "";
		rs = con.getResultSetList(IsGulsql);
		while (rs.next()) {
			if (rs.getString("zhi").equals("是")) {
				IsGul = "      and r.diancxxb_id="+ visit.getDiancxxb_id()+" \n";
			}
		}
		String sql;
		sql=
				"select r.id,to_char(r.rulrq,'yyyy-mm-dd')as rulrq,to_char(r.fenxrq,'yyyy-mm-dd') as fenxrq,rb.mingc as rulbzb_id ," +
				"      j.mingc as jizfzb_id,round_new( r.qnet_ar*1000/4.1816,0) as qnet_ar_daka,r.qnet_ar as qnet_ar ,formatxiaosws(r.aar,2) as aar," +
				"      formatxiaosws(r.ad,2) as ad,formatxiaosws(r.vdaf,2) as vdaf,formatxiaosws(r.mt,2) as mt," +
				"      formatxiaosws(r.stad,2) as stad,\n" +
				"      formatxiaosws(r.aad,2) as aad,formatxiaosws(r.mad,2) as mad,formatxiaosws(r.qbad,2) as qbad," +
				"      formatxiaosws(r.had,2) as had,formatxiaosws(r.vad,2) as vad,formatxiaosws(r.fcad,2) as fcad," +
				"      formatxiaosws(r.std,2) as std,formatxiaosws(r.qgrad,2) as qgrad,formatxiaosws(r.hdaf,2) as hdaf," +
				"      formatxiaosws(r.sdaf,2) as sdaf,formatxiaosws(r.var,2) as var,formatxiaosws(r.har,2) as har,\n" + 
				"      formatxiaosws(round_new(r.qgrd,2),2) as qgrd,formatxiaosws(round_new(r.qgrad_daf,2),2) as qgrad_daf,  " +
				"      r.huayy,r.lury,to_char(r.lursj,'yyyy-mm-dd') as lursj,r.bianm,r.beiz\n" + 
				"      from rulmzlb r  ,rulbzb rb  ,jizfzb j \n" + 
				"      where r.rulbzb_id=rb.id and r.jizfzb_id=j.id \n" + 
				       IsGul+
				"      and r.shenhzt=3  and erjshzt=0 and r.meil!=0 order by r.rulrq,j.mingc,rb.xuh";
		
		ResultSetList rsl = con.getResultSetList(sql);

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl,CustomSetKey);
		egu.setTableName("rulmzlb");
//		 设置页面宽度以便增加滚动条
		egu.setWidth(1000);
		// /设置显示列名称

		 egu.getColumn("id").setHidden(true);
		 egu.getColumn("rulrq").setHeader("入炉日期");
		 egu.getColumn("fenxrq").setHeader("分析日期");
		 egu.getColumn("rulbzb_id").setHeader("入炉班组");
		 egu.getColumn("jizfzb_id").setHeader("机组");
		 egu.getColumn("qnet_ar_daka").setHeader("Qnet,ar(K/g)");
		 egu.getColumn("qnet_ar").setHeader("Qnet,ar(Mj/kg)");
		 egu.getColumn("aar").setHeader("Aar(%)");
	     egu.getColumn("ad").setHeader("Ad(%)");
		 egu.getColumn("vdaf").setHeader("Vdaf(%)");
		 egu.getColumn("mt").setHeader("Mt(%)");
		 egu.getColumn("stad").setHeader("St,ad(%)");
		 egu.getColumn("aad").setHeader("Aad(%)");
		 egu.getColumn("mad").setHeader("Mad(%)");
		 egu.getColumn("qbad").setHeader("Qb,ad(Mj/kg)");
		 egu.getColumn("had").setHeader("Had(%)");
		 egu.getColumn("vad").setHeader("Vad(%)");
		 egu.getColumn("fcad").setHeader("FCad(%)");
		 egu.getColumn("std").setHeader("St,d(%)");
		 egu.getColumn("qgrad").setHeader("Qgr,ad(Mj/kg)");
		 egu.getColumn("hdaf").setHeader("Hdaf(%)");
		 egu.getColumn("qgrad_daf").setHeader("Qgad,daf(Mj/kg)");
		 egu.getColumn("sdaf").setHeader("Sdaf(%)");
		 egu.getColumn("var").setHeader("Var(%)");
		 egu.getColumn("har").setHeader("Har(%)");
		 egu.getColumn("qgrd").setHeader("Qgrd(%)");
		 egu.getColumn("huayy").setHeader("化验员");
		 egu.getColumn("lury").setHeader("化验录入员");
		 egu.getColumn("lursj").setHeader("录入时间");
		 egu.getColumn("bianm").setHeader("编码");
		 egu.getColumn("beiz").setHeader("化验备注");
		//宽度
		 egu.getColumn("rulrq").setWidth(80);
		 egu.getColumn("fenxrq").setWidth(80);
		 egu.getColumn("rulbzb_id").setWidth(80);
		 egu.getColumn("jizfzb_id").setWidth(80);
		 egu.getColumn("qnet_ar_daka").setWidth(85);
		 egu.getColumn("qnet_ar").setWidth(85);
		 egu.getColumn("aar").setWidth(60);
	     egu.getColumn("ad").setWidth(60);
		 egu.getColumn("vdaf").setWidth(60);
		 egu.getColumn("mt").setWidth(60);
		 egu.getColumn("stad").setWidth(60);
		 egu.getColumn("aad").setWidth(60);
		 egu.getColumn("mad").setWidth(60);
		 egu.getColumn("qbad").setWidth(85);
		 egu.getColumn("had").setWidth(60);
		 egu.getColumn("vad").setWidth(60);
		 egu.getColumn("fcad").setWidth(60);
		 egu.getColumn("std").setWidth(60);
		 egu.getColumn("qgrad").setWidth(85);
		 egu.getColumn("hdaf").setWidth(60);
		 egu.getColumn("qgrad_daf").setWidth(80);
		 egu.getColumn("sdaf").setWidth(60);
		 egu.getColumn("var").setWidth(60);
		 egu.getColumn("har").setWidth(60);
		 egu.getColumn("qgrd").setWidth(85);
		 egu.getColumn("huayy").setWidth(60);
		 egu.getColumn("lury").setWidth(60);
		 egu.getColumn("lursj").setWidth(90);
		 egu.getColumn("bianm").setWidth(60);
		 egu.getColumn("beiz").setWidth(90);
		

		egu.addTbarText("-");// 设置分隔符

		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(0);
		
		
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		

		
//		 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		egu.addTbarText("-");
		egu.addToolbarButton("审核",GridButton.ButtonType_Sel, "SaveButton", null, SysConstant.Btn_Icon_SelSubmit);
		egu.addTbarText("-");
		egu.addToolbarButton("回退",GridButton.ButtonType_Sel, "HuitButton", null, SysConstant.Btn_Icon_Return);
		egu.addTbarText("-");
//		egu.addToolbarButton("打印",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
//		egu.addTbarText("-");
	


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
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		return getExtGrid().getHtml();
	}


	// 页面判定方法
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
		if (!visit.getActivePageName().toString().equals("Fenycx_dy")) {
			visit.setList1(null);
		
			visit.setLong1(0);
		}
		visit.setActivePageName(getPageName().toString());

		}
		
		getSelectData();
	}
	

}
