package com.zhiren.dc.pandgd;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;

/*
 * 作者：夏峥
 * 时间：2012-07-25
 * 描述：附件名称以链接的形式显示，当用户点击链接时可以直接下载附件。
 */
/*
 * 作者：夏峥
 * 时间：2012-12-05
 * 描述：分厂别或有分厂时不显示生成按钮
 */
/*
 * 作者：夏峥
 * 时间：2012-12-20
 * 描述：盘点模块中的数据只能为整数
 */
public class Pand_GDJT extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}

	protected void initialize() {
		// TODO 自动生成方法存根
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

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _ShengcChick = false;

	public void ShengcButton(IRequestCycle cycle) {
		_ShengcChick = true;
	}
	
	private boolean _SubChick = false;

	public void SubmitButton(IRequestCycle cycle) {
		_SubChick = true;
	}

	private boolean _HuitChick = false;

	public void HuitButton(IRequestCycle cycle) {
		_HuitChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_Refreshclick) {
			_Refreshclick = false;
		}
		if (_ShengcChick) {
			_ShengcChick = false;
			Shengc();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if(_SubChick){
			_SubChick=false;
			submit();
		}
		if(_HuitChick){
			_HuitChick=false;
			Huit();
		}
	}

	private void Save() {
		String sql="";
		JDBCcon con=new JDBCcon();
		ResultSetList mdrsl = getExtGrid().getModifyResultSet(getChange());
		while(mdrsl.next()){
			sql="UPDATE PAND_GDJT\n" +
					"   SET ZHANGMKC  = "+mdrsl.getString("ZHANGMKC")+",\n" + 
					"       SHIPKC    = "+mdrsl.getString("SHIPKC")+",\n" + 
					"       CHANGSL   = "+mdrsl.getString("CHANGSL")+",\n" + 
					"       SHUIFCTZL = "+mdrsl.getString("SHUIFCTZL")+",\n" + 
					"       YINGKD    = "+mdrsl.getString("YINGKD")+"\n" + 
					" WHERE ID = "+mdrsl.getString("ID");
		}
		int flag=con.getUpdate(sql);
		if(flag==-1){
			setMsg("保存更新失败");
		}else{
			setMsg("保存更新成功");
		}
		con.Close();
	}
	
//	回退方法
	public void Huit() {
		JDBCcon con = new JDBCcon();
		String sql="UPDATE PAND_GDJT SET ZHUANGT  = 0 WHERE ID = "+getChange();

//		使用电厂树进行本地回退的判断
		String diancxxb_id = getTreeid();
		if (MainGlobal.getXitxx_item("月报上传", "是否开启本地回退",diancxxb_id, "否").equals("是")) {
			// 如果是本地回退的话 直接在本地库更新数据提交状态。
			int num = con.getUpdate(sql);
			if (num != -1) {
				setMsg("本地数据回退成功！");
			} else {
				setMsg("本地数据回退失败！");
			}
			con.Close();
		} else {
			// 如果是远程回退的话，先更新远程回退状态，再更新本地回退状态。
			InterCom_dt dt = new InterCom_dt();
			String[] sqls = new String[] { sql };
			String[] answer = dt.sqlExe(diancxxb_id, sqls, true);
			if (answer[0].equals("true")) {
				int num = con.getUpdate(sql);
				if (num == -1) {
					setMsg("本地数据回退发生异常！");
				} else {
					setMsg("数据回退成功！");
				}
			} else {
				setMsg("回退数据发生异常！");
			}
		}
	}
	
	private void submit() {
		String sql="";
		JDBCcon con=new JDBCcon();
		sql="UPDATE PAND_GDJT SET ZHUANGT  = 1 WHERE ID = "+getChange();
		int flag=con.getUpdate(sql);
		if(flag==-1){
			setMsg("提交失败");
		}else{
			setMsg("提交成功");
		}
		con.Close();
	}
	
	private void Shengc() {// 生成
		JDBCcon con = new JDBCcon();
		String strDate = getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		strDate=DateUtil.FormatOracleDate(strDate);
		String sql = "INSERT INTO pand_gdjt  (ID, riq, diancxxb_id)VALUES  (getnewid("+getTreeid()+"), "+strDate+", "+getTreeid()+")";
		int flag = con.getInsert(sql);
		if(flag==-1){
			setMsg("生成数据失败");
		}else{
			setMsg("生成数据成功");
		}
	}


	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		// 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {
			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}

//		获得文件保存路径
		String Imagelj=MainGlobal.getXitxx_item("盘点", "盘煤附件路径", "0", "D:\\\\zhiren\\\\pand");
		Imagelj=Imagelj.replaceAll("\\\\", "\\\\\\\\");
		Imagelj=Imagelj + "\\\\" + this.getTreeid() + "\\\\";
		
		String Sql="SELECT P.ID,\n" +
			"       DC.MINGC DIANCXXB_ID,\n" + 
			"       P.ZHANGMKC,\n" + 
			"       P.SHIPKC,\n" + 
			"       P.CHANGSL,\n" + 
			"       P.SHUIFCTZL,\n" + 
			"       P.YINGKD,\n" + 
			"       P.FUJZT,\n" + 
//			"       P.FUJMC,\n" + 
			"	    '<a href=\""+MainGlobal.getHomeContext(this)+"/downfile.jsp?filepath="+Imagelj+"&filename='||GETFILENAMEVIAID(P.ID)||'\" >'||P.FUJMC||'</a>' FUJMC,"+
			"       P.ZHUANGT\n" + 
			"  FROM PAND_GDJT P, DIANCXXB DC\n" + 
			" WHERE P.DIANCXXB_ID = DC.ID\n" + 
			"   AND RIQ = date'"+intyear+"-"+StrMonth+"-01'\n" + 
			"   AND DC.ID = "+this.getTreeid();

		ResultSetList rsl = con.getResultSetList(Sql);

		String zhuangt="0";
		int FUJMC=0;
		while(rsl.next()){
			zhuangt=rsl.getString("zhuangt");
			FUJMC=rsl.getString("FUJMC").length();
		}
		rsl.beforefirst();
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("PAND_GDJT");
		egu.setWidth("bodyWidth");

		egu.setDefaultsortable(false);//设置每列表头点击后不可以排序。
		egu.getColumn("diancxxb_id").setCenterHeader("电厂名称");
		egu.getColumn("diancxxb_id").setWidth(100);
		egu.getColumn("diancxxb_id").setEditor(null);
		
		egu.getColumn("zhangmkc").setCenterHeader("账面库存");
		egu.getColumn("zhangmkc").setWidth(60);
		((NumberField)egu.getColumn("zhangmkc").editor).setDecimalPrecision(0);
		
		egu.getColumn("shipkc").setCenterHeader("实盘库存");
		egu.getColumn("shipkc").setWidth(70);
		((NumberField)egu.getColumn("shipkc").editor).setDecimalPrecision(0);
		
		egu.getColumn("changsl").setCenterHeader("场损量");
		egu.getColumn("changsl").setWidth(60);
		((NumberField)egu.getColumn("changsl").editor).setDecimalPrecision(0);
		
		egu.getColumn("shuifctzl").setCenterHeader("水分差调整量");
		egu.getColumn("shuifctzl").setWidth(60);
		((NumberField)egu.getColumn("shuifctzl").editor).setDecimalPrecision(0);
		
		egu.getColumn("yingkd").setCenterHeader("盈亏吨");	
		egu.getColumn("yingkd").setWidth(80);
		((NumberField)egu.getColumn("yingkd").editor).setDecimalPrecision(0);
		
		egu.getColumn("fujzt").setCenterHeader("附件状态");
		egu.getColumn("fujzt").setWidth(45);
		egu.getColumn("fujzt").setHidden(true);
		egu.getColumn("fujzt").setEditor(null);
		
		egu.getColumn("fujmc").setCenterHeader("附件名称");
		egu.getColumn("fujmc").setWidth(200);
		egu.getColumn("fujmc").setEditor(null);
		egu.getColumn("fujmc").setUpdate(false);
		
		egu.getColumn("zhuangt").setCenterHeader("状态");
		egu.getColumn("zhuangt").setHidden(true);
		egu.getColumn("zhuangt").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(23);// 设置分页
	
		// ********************工具栏************************************************
		egu.addTbarText("年份:");
		ComboBox comb1 = new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");// 和自动刷新绑定
		comb1.setLazyRender(true);// 动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		egu.addTbarText("-");
		egu.addTbarText("月份:");
		ComboBox comb2 = new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");// 和自动刷新绑定
		comb2.setLazyRender(true);// 动态绑定
		comb2.setWidth(60);
		egu.addToolbarItem(comb2.getScript());
		
		// 设置树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), this.getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");
		
		egu.addTbarText("-");
		egu.addToolbarButton("刷新", GridButton.ButtonType_Refresh, "RefreshButton");
		
		if(visit.getDiancxxb_id()!=112){
			if(visit.isFencb() && isParentDc(con)){
//				分厂别或有分厂时不显示生成按钮
			}else{
				if(!con.getHasIt(Sql)){
					GridButton gbr = new GridButton("生成", "function (){document.getElementById('ShengcButton').click();}");
					gbr.setIcon(SysConstant.Btn_Icon_Insert);
					egu.addTbarBtn(gbr);
				}else{
//					只有未提交时才显示保存添加附件等按钮
					if(zhuangt.equals("0")){
						egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//						添加附件
						GridButton gbphoto = new GridButton("添加附件","function (){"+MainGlobal.getOpenWinScript("PandFujUpLoad&id='+gridDiv_ds.getAt(0).get(\"ID\")+'","480","140")+ "}");
						gbphoto.setIcon(SysConstant.Btn_Icon_Create);
						egu.addTbarBtn(gbphoto);
						GridButton gbt = new GridButton("提交", "function (){document.getElementById('CHANGE').value=gridDiv_ds.getAt(0).get('ID'); document.getElementById('SubmitButton').click();}");
						gbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
						if(FUJMC==0){
							gbt.setDisabled(true);
						}
						egu.addTbarBtn(gbt);
					}
				}
			}
		}
		
//		使用参数配置是否能使用回退按钮。
		if(visit.getString1()!=null && !visit.getString1().equals("") &&visit.getString1().equals("return") && zhuangt.equals("1")){
			GridButton gbt = new GridButton("回退", "function (){document.getElementById('CHANGE').value=gridDiv_ds.getAt(0).get('ID'); document.getElementById('HuitButton').click();}");
			gbt.setIcon(SysConstant.Btn_Icon_SelSubmit);
			egu.addTbarBtn(gbt);
		}
		
		setExtGrid(egu);
		con.Close();
	}

	private boolean isParentDc(JDBCcon con){
		String sql = "select * from diancxxb where fuid = " + getTreeid();
		return con.getHasIt(sql);
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
		if(visit.getActivePageName().toString().equals("PandFujUpLoad")){
			visit.setActivePageName(getPageName().toString());
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			visit.setString1(null);
			this.setNianfValue(null);
			this.getNianfModels();
			this.setTreeid(null);
			this.setYuefValue(null);
			this.setMsg(null);
			this.getYuefModels();
//			通过参数对是否可以回退进行配置
			if(cycle.getRequestContext().getParameter("lx") != null){
				visit.setString1(cycle.getRequestContext().getParameter("lx"));
			}
		}
		getSelectData();
	}

	// 年份
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;

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