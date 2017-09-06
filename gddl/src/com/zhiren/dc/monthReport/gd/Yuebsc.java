package com.zhiren.dc.monthReport.gd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;


/*
 * 作者：李琛基
 * 时间：2011-3-29
 * 描述：增加月报提交页面。
 *  本页面在电厂用户登录时，只有一个提交功能，(个别特殊电厂除外)，公司用户多了一个回退功能。
 *  其中回退功能除了公司级用户有权限外，一些采用VPN上传数据的电厂也可以通过在xitxxb中配置，实现本地回退。
 *  具体配置如下：
 *  insert into xitxxb (id, xuh, diancxxb_id, mingc, zhi, leib, zhuangt, beiz) values (getnewid(电厂ID), 1, 电厂ID, '是否开启本地回退', '是', '月报上传', 1, '使用');
 */
/*
 * 作者：夏峥
 * 时间：2012-01-05
 * 描述：月报上报界面中增加厂内费用的上报功能
 */
/*
 * 作者：夏峥
 * 时间：2012-02-02
 * 描述：月报上报界面中取消入厂标煤单价行并将结算表面单价行变更为入厂标煤单价
 */
/*
 * 作者：夏峥
 * 时间：2012-02-13
 * 描述：使用参数对可显示的界面进行控制，只有当传入参数lx=return时才可显示回退按钮。
 * 		 在回退时使用电厂树得到回退的SQL进行判断（判断其是否采用本地回退的方式进行回退）。
 */
public abstract class Yuebsc extends BasePage {
//	界面用户提示
	private String msg="";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		super.initialize();
		setMsg("");
	}
//	绑定日期
//	 年份下拉框
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
//		if (_NianfValue != Value) {
			_NianfValue = Value;
//		}
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int _nianf = DateUtil.getYear(new Date());
		int i;
		for (i = _nianf-2; i <= _nianf+2; i++) {
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
//		if (_YuefValue != Value) {
			_YuefValue = Value;
//		}
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
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
//	厂别下拉框
	public IDropDownBean getChangbValue() {
		if(((Visit)this.getPage().getVisit()).getDropDownBean2()==null) {
			if(getChangbModel().getOptionCount()>0) {
				setChangbValue((IDropDownBean)getChangbModel().getOption(0));
			}
		}
		return ((Visit)this.getPage().getVisit()).getDropDownBean2();
	}
	public void setChangbValue(IDropDownBean value) {
		((Visit)this.getPage().getVisit()).setDropDownBean2(value);
	}
	
	public IPropertySelectionModel getChangbModel() {
		if(((Visit)this.getPage().getVisit()).getProSelectionModel2()==null) {
			setChangbModels();
		}
		return ((Visit)this.getPage().getVisit()).getProSelectionModel2();
	}
	public void setChangbModel(IPropertySelectionModel value) {
		((Visit)this.getPage().getVisit()).setProSelectionModel2(value);
	}
	
	public void setChangbModels() {
		Visit visit = (Visit)this.getPage().getVisit();
		StringBuffer sb = new StringBuffer();
		if(visit.isFencb()) {
			sb.append("select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id());
		}else {
			sb.append("select id,mingc from diancxxb where id="+visit.getDiancxxb_id());
		}
		setChangbModel(new IDropDownModel(sb.toString()));
	}
	
	private boolean _ShangcClick = false;

	public void ShangcButton(IRequestCycle cycle) {
		_ShangcClick = true;
	}
	
	private boolean _HuitClick = false;
	
	public void HuitButton(IRequestCycle cycle) {
		_HuitClick = true;
	}
	
	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_ShangcClick) {
			_ShangcClick = false;
			uplaodData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		
		if (_HuitClick) {
			_HuitClick = false;
			rollbackData();
		}
		getSelectData();
	}
	//上报方法
	public void uplaodData(){
		ResultSetList rs=this.getExtGrid().getModifyResultSet(Change);
		JDBCcon con=new JDBCcon();
		int err=rs.getRows();
		StringBuffer strSql=new StringBuffer();
		strSql.append("begin\n");
		while(rs.next()){
			int zt=rs.getInt("zhuangt");
			String tblName=rs.getString("id");
			if(zt==0){
				strSql.append(updateSql(tblName,1)).append("\n");
			}else if(zt==2){
				strSql.append(updateSql(tblName,3)).append("\n");
			}else{
				err--;
			}
		}
		if (err == 0) {
			setMsg("数据已提交，不能重复提交！");
			return;
		}
		strSql.append("end;");
		int result=con.getUpdate(strSql.toString());
		if(result!=-1){
			setMsg("提交成功！");
		}else{
			setMsg("提交失败！");
		}
		con.Close();
	}
	//回退方法
	public void rollbackData() {
		Visit visit = (Visit) this.getPage().getVisit();
		ResultSetList rs = this.getExtGrid().getModifyResultSet(
				this.getChange());
		JDBCcon con = new JDBCcon();
		int err=rs.getRows();
		StringBuffer strSql = new StringBuffer();
		strSql.append("begin\n");
		while (rs.next()) {
			if(rs.getInt("zhuangt")==1||rs.getInt("zhuangt")==3){
				strSql.append(updateSql(rs.getString("id"), 2));
			}else{
				err--;
			}
		}
		if(err==0){
			setMsg("不能回退未提交或已经回退的数据！");
			return;
		}
		strSql.append("end;");
//		使用电厂树进行本地回退的判断
		String diancxxb_id = getTreeid();
		if (MainGlobal.getXitxx_item("月报上传", "是否开启本地回退",diancxxb_id, "否").equals("是")) {
			// 如果是本地回退的话 直接在本地库更新数据提交状态。
			int num = con.getUpdate(strSql.toString());
			if (num != -1) {
				setMsg("本地数据回退成功！");
			} else {
				setMsg("本地数据回退失败！");
			}
			con.Close();
		} else {
			// 如果是远程回退的话，先更新远程回退状态，再更新本地回退状态。
			InterCom_dt dt = new InterCom_dt();
			String[] sqls = new String[] { strSql.toString() };
			String[] answer = dt.sqlExe(getTreeid(), sqls, true);
			if (answer[0].equals("true")) {
				int num = con.getUpdate(strSql.toString());
				if (num == -1) {
					setMsg("本地数据回退发生异常！");
				} else {
					setMsg("对" + ((IDropDownModel)getChangbModel()).getBeanValue(getTreeid()) + "电厂数据回退成功！");
				}
			} else {
				setMsg("对" + ((IDropDownModel)getChangbModel()).getBeanValue(getTreeid()) + "电厂回退数据发生异常！");
			}
		}
	}
	
	/**
	 * 返回更新提交状态SQL
	 * @param tblName 表名
	 * @param zhuangt 状态值:  0未提交 1已提交 2已回退 3回退后提交
	 * @return
	 */
	public String updateSql(String tblName,int zhuangt){
		String str="";
		String CurDate = DateUtil.FormatOracleDate(getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01");
		if (tblName.equals("yueslb") || tblName.equals("yuezlb")
				|| tblName.equals("yuehcb") || tblName.equals("yuejsbmdj")
				|| tblName.equals("yuercbmdj")){
			str="update "+tblName+" set zhuangt="+zhuangt+" where yuetjkjb_id in " +
					"(select id from yuetjkjb where diancxxb_id="+getTreeid()+" and riq="+CurDate+");";
		}else{
			str="update "+tblName+" set zhuangt="+zhuangt+" where diancxxb_id="+getTreeid()+" and riq="+CurDate+";";
		}
		return str;
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String CurDate = DateUtil.FormatOracleDate(getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01");
		String diancxxb_id = getTreeid();
		String sb ="";
		sb=	"select shuj.id,\n" +
			"       shuj.mingc,\n" +
			"       shuj.zhuangt zhuangt, \n" + 
			"       decode(shuj.zhuangt, null, '未填写', 0, '<font color=\"red\">未提交</font>',2, '<font color=\"Blue\">已回退</font>','已上报') color\n" + 
			"  from (select 1 xuh, 'yueslb' id,\n" + 
			"               '数量信息' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yueslb s, yuetjkjb k\n" + 
			"         where s.yuetjkjb_id = k.id\n" + 
			"           and k.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and k.riq = "+CurDate+"\n" + 
			"        union\n" + 
			"        select 2 xuh,'yuezlb' id,\n" + 
			"               '质量信息' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yuezlb s, yuetjkjb k\n" + 
			"         where s.yuetjkjb_id = k.id\n" + 
			"           and k.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and k.riq = "+CurDate+"\n" + 
			"        union\n" + 
			"        select 3 xuh,'yueshchjb' id,\n" + 
			"               '耗存合计' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yueshchjb s\n" + 
			"         where s.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and s.riq = "+CurDate+"\n" + 
			"        union\n" + 
			"        select 4 xuh,'yuehcb' id,\n" + 
			"               '耗存情况' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yuehcb s, yuetjkjb k\n" + 
			"         where s.yuetjkjb_id = k.id\n" + 
			"           and k.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and k.riq = "+CurDate+"\n" + 
			"        union\n" + 
			"        select 5 xuh,'yuejsbmdj' id,\n" + 
			"               '入厂标煤单价' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yuejsbmdj s, yuetjkjb k\n" + 
			"         where s.yuetjkjb_id = k.id\n" + 
			"           and k.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and k.riq = "+CurDate+"\n" + 
//			"        union\n" + 
//			"        select 6 xuh,'yuercbmdj' id,\n" + 
//			"               '入厂标煤单价' mingc,\n" + 
//			"               min(s.zhuangt) zhuangt\n" + 
//			"          from yuercbmdj s, yuetjkjb k\n" + 
//			"         where s.yuetjkjb_id = k.id\n" + 
//			"           and k.diancxxb_id = "+diancxxb_id+"\n" + 
//			"           and k.riq = "+CurDate+"\n" + 
			"        union\n" + 
			"        select 6 xuh,'yuezbb' id,\n" + 
			"               '指标情况' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yuezbb s\n" + 
			"         where s.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and s.riq = "+CurDate+
			"        union\n" + 
			"        select 7 xuh,'yueshcyb' id,\n" + 
			"               '收耗存（油）' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from yueshcyb s\n" + 
			"         where s.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and s.riq = "+CurDate+"\n" +
			"        union\n" + 
			"        select 8 xuh,'zafb' id,\n" + 
			"               '厂内费用' mingc,\n" + 
			"               min(s.zhuangt) zhuangt\n" + 
			"          from zafb s\n" + 
			"         where s.diancxxb_id = "+diancxxb_id+"\n" + 
			"           and s.riq = "+CurDate+") shuj\n" +
			"     order by xuh";

		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(sb);
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb.toString());
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		
		egu.setWidth("bodyWidth");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(1, new GridColumn(GridColumn.ColType_Check));
		egu.addPaging(0);
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("mingc").setHeader("月报");
		egu.getColumn("mingc").setWidth(120);
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("color").setHeader("状态");
		egu.getColumn("color").setWidth(120);
		
		egu.addTbarText("年份:");
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NianfDropDown");
		nianf.setWidth(60);
		egu.addToolbarItem(nianf.getScript());
		
		egu.addTbarText("月份:");
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YuefDropDown");
		yuef.setWidth(60);
		egu.addToolbarItem(yuef.getScript());
		egu.addTbarText("-");
		
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		
		
		egu.addToolbarButton("刷新", GridButton.ButtonType_Refresh, "RefreshButton");

		if(visit.getString1()!=null && !visit.getString1().equals("") &&visit.getString1().equals("return")){
			egu.addToolbarButton("回退", GridButton.ButtonType_SubmitSel, "HuitButton");
		}else{
			egu.addToolbarButton("提交", GridButton.ButtonType_SubmitSel, "ShangcButton");
		}
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
		if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setChangbModels();
			setNianfModel(null);
			setNianfValue(null);
			setYuefModel(null);
			setYuefValue(null);
			setTreeid(null);
			visit.setString1(null);
//			通过参数对是否可以回退进行配置
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString1(cycle.getRequestContext().getParameter("lx"));
			}
		}
		getSelectData();
	}
	
	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString2(treeid);
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
}
