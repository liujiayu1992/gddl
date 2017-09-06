package com.zhiren.dc.caiygl;

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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.huaygl.Caiycl;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/**
 * 
 * @author Rock
 * @version v1.1.2.4
 * @since 2009-05-20
 * 
 */
/*
 * 作者：王磊
 * 时间：2009-05-19
 * 描述：调整运输方式为火运或全部时增加发站列,质量不按照发货拆分。
 */
/*
 * 作者：王磊
 * 时间：2009-05-19 18:20
 * 描述：增加时间选择的参数
 */
/*
 * 作者：王磊
 * 时间：2009-05-25 17:39
 * 描述：取消页面分页设置
 */
/*
 * 作者：王磊
 * 时间：2009-06-16 17：02
 * 描述：增加不拆分发货时对列ID 的生成算法的调用
 */
public class Caiybm_sd extends BasePage implements PageValidateListener {
	/* 资源配置参数 模式 （生成）*/
	private static final String MOD_SC = "SC";
//	private static final String MOD_XG = "XG";
	/* 资源配置参数 运输方式（全部）*/
	private static final String YUNSFS_ALL = "ALL";
	/* 资源配置参数 运输方式（铁路）*/
	private static final String YUNSFS_HY = "HY";
	/* 资源配置参数 运输方式（公路）*/
	private static final String YUNSFS_QY = "QY";
	/* 按钮类型 生成 */
	private static final String BtnType_SC = "生成";
	/* 按钮类型 合并 */
	private static final String BtnType_HB = "合并";
	/* 按钮类型 调整 */
	private static final String BtnType_TZ = "调整";
	/* 按钮类型 删除 */
	private static final String BtnType_DL = "删除";
	// 界面用户提示
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
		;
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
	
	private boolean riqchange;
//	 页面刷新日期（卸煤日期）
	private String riq;

	public String getRiq() {
		return riq;
	}

	public void setRiq(String riq) {
		if(this.riq !=null){
			if(!this.riq.equals(riq)){
				riqchange = true;
			}
		}
		this.riq = riq;
	}
//	 页面刷新日期（卸煤日期）
	private String riqe;

	public String getRiqe() {
		return riqe;
	}
	public void setRiqe(String riqe) {
		if(this.riqe !=null){
			if(!this.riqe.equals(riqe)){
				riqchange = true;
			}
		}
		this.riqe = riqe;
	}
//	日期设置下拉框(发货、到货)
	public IDropDownBean getRiqsetValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
			if (getRiqsetModel().getOptionCount() > 0) {
				setRiqsetValue((IDropDownBean) getRiqsetModel().getOption(0));
			}else{
				setRiqsetValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean3();
	}

	public void setRiqsetValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean3(value);
	}

	public IPropertySelectionModel getRiqsetModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
			setRiqsetModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
	}

	public void setRiqsetModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
	}

	public void setRiqsetModels() {
		List riqlist = new ArrayList();
		riqlist.add(new IDropDownBean(1,"到货"));
		riqlist.add(new IDropDownBean(2,"发货"));
		setRiqsetModel(new IDropDownModel(riqlist));
	}
//	 煤矿下拉框
	public IDropDownBean getChangbValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
			if (getChangbModel().getOptionCount() > 0) {
				setChangbValue((IDropDownBean) getChangbModel().getOption(0));
			}else{
				setChangbValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean2();
	}

	public void setChangbValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean2(value);
	}

	public IPropertySelectionModel getChangbModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setChangbModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setChangbModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
	}

	public void setChangbModels() {
		String sql = "select distinct m.id,m.mingc from meikxxb m,fahb f\n" +
		"where f.meikxxb_id = m.id "+getStrWhere("meik");
		setChangbModel(new IDropDownModel(sql,"全部"));
	}
//	编号下拉框
	public IDropDownBean getBianhValue() {
		if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
			if (getBianhModel().getOptionCount() > 0) {
				setBianhValue((IDropDownBean) getBianhModel().getOption(0));
			}else{
				setBianhValue(new IDropDownBean());
			}
		}
		return ((Visit) this.getPage().getVisit()).getDropDownBean1();
	}

	public void setBianhValue(IDropDownBean value) {
		((Visit) this.getPage().getVisit()).setDropDownBean1(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setBianhModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
	}

	public void setBianhModels() {
		String sql = 
			"select c.zhilb_id,c.bianm from fahb f, caiyb c\n" +
			"where f.zhilb_id = c.zhilb_id\n" + 
			"and f.hedbz <" + SysConstant.HEDBZ_YSH + getStrWhere("bianm") ;
		setBianhModel(new IDropDownModel(sql,null));
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	private boolean _RefurbishClick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishClick = true;
	}
	
	private boolean _CreateClick = false;

	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _HebingClick = false;

	public void HebingButton(IRequestCycle cycle) {
		_HebingClick = true;
	}
	
	private boolean _DeleteClick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	
	private boolean _UpdateClick = false;

	public void UpdateButton(IRequestCycle cycle) {
		_UpdateClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishClick) {
			_RefurbishClick = false;
			setBianhModels();
			initGrid();
		}
		if (_CreateClick) {
			_CreateClick = false;
			Save(BtnType_SC);
			init();
		}
		if (_HebingClick) {
			_HebingClick = false;
			Save(BtnType_HB);
			init();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			Save(BtnType_DL);
			init();
		}
		if (_UpdateClick) {
			_UpdateClick = false;
			Save(BtnType_TZ);
			init();
		}
	}
	/**
	 * 
	 * @param mod	构造SQL的地点
	 * @return		返回SQL的where过滤条件
	 * @description	如果mod=bianm则不加载质量ID过滤
	 * 				如果mod=meik则不加载煤矿ID过滤
	 */
	private String getStrWhere(String mod){
		Visit visit = (Visit) getPage().getVisit();
		String where = "";
		String strxmrqOra = DateUtil.FormatOracleDate(getRiq());
		String strxmrqeOra = DateUtil.FormatOracleDate(getRiqe());
		if(!"bianm".equalsIgnoreCase(mod))
			if(MOD_SC.equals(visit.getString2())){
				where += " and f.zhilb_id = 0 \n";
			}else{
				where += " and f.zhilb_id != 0 \n";
			}
		if(YUNSFS_HY.equals(visit.getString1())){
			where += " and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY + "\n";
		}else if(YUNSFS_QY.equals(visit.getString1())){
			where += " and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY + "\n";
		}
		if(!"meik".equalsIgnoreCase(mod)){
			if(getChangbValue()!=null && getChangbValue().getId() !=-1){
				where += " and f.meikxxb_id = " + getChangbValue().getId();
			}
		}
		String rq = "daohrq";
		if(getRiqsetValue()!=null){
			if(getRiqsetValue().getId() == 1){
				rq = "daohrq";
			}else{
				rq = "fahrq";
			}
		}
		where += " and f."+rq+" >= " + strxmrqOra 
		+"\n and f."+rq+" < " + strxmrqeOra + " +1 \n";
		where += " and f.diancxxb_id =" + visit.getDiancxxb_id() + "\n";
		return where;
	}

	private void initGrid() {
		Visit visit = (Visit) getPage().getVisit();
//		 卸煤日期的ora字符串格式
		JDBCcon con = new JDBCcon();
		String sql = 
			"select c.id,c.fahb_id,c.xuh, m.mingc meik, cz.mingc faz, c.cheph,\n " +
			"to_char(c.zhongcsj,'yyyy-mm-dd hh24:mi:ss') guohsj\n " +
			"from fahb f, chepb c, meikxxb m,chezxxb cz\n" + 
			"where f.id = c.fahb_id and f.faz_id = cz.id\n" + 
			"and f.meikxxb_id = m.id\n" + getStrWhere("") +
			"order by c.xuh";

		ResultSetList rsl = con.getResultSetList(sql);
		if (rsl == null) {
			WriteLog.writeErrorLog(this.getClass().getName() + "\n"
					+ ErrorMessage.NullResult + "\nSQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		// 新建grid
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		// 设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		设置grid不分页
		egu.addPaging(0);
//		设置grid隐藏列
		egu.getColumn("fahb_id").setHidden(true);
		// 设置grid列标题
		egu.getColumn("xuh").setHeader(Local.xuh);
		egu.getColumn("meik").setHeader(Local.meikxxb_id_fahb);
		egu.getColumn("faz").setHeader(Local.faz_id_fahb);
		egu.getColumn("cheph").setHeader(Local.cheph);
		egu.getColumn("guohsj").setHeader(Local.guohrq);
		
//		设置列宽度
		egu.getColumn("xuh").setWidth(60);
		egu.getColumn("meik").setWidth(120);
		egu.getColumn("faz").setWidth(120);
		egu.getColumn("cheph").setWidth(100);
		egu.getColumn("guohsj").setWidth(120);
//		设定grid列可否编辑
		egu.getColumn("xuh").setEditor(null);
		egu.getColumn("meik").setEditor(null);
		egu.getColumn("faz").setEditor(null);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("guohsj").setEditor(null);
		
		egu.getColumn("faz").setHidden(YUNSFS_QY.equals(visit.getString1()));
		// 数据列下拉框设置
		
		ComboBox riqset = new ComboBox();
		riqset.setTransform("RiqsetSelect");
		riqset.setWidth(60);
		riqset
				.setListeners("select:function(own,rec,index){Ext.getDom('RiqsetSelect').selectedIndex=index}");
		egu.addToolbarItem(riqset.getScript());
//		 接卸日期选择
		DateField df = new DateField();
		df.setValue(getRiq());
		df.Binding("RIQ", "Form0");// 与html页中的id绑定,并自动刷新
		df.setId("jiexrq");
		egu.addToolbarItem(df.getScript());
//		 接卸日期选择
		egu.addTbarText(Local.riqz);
		DateField df1 = new DateField();
		df1.setValue(getRiqe());
		df1.Binding("RIQE", "Form0");// 与html页中的id绑定,并自动刷新
		df1.setId("jiexrqe");
		egu.addToolbarItem(df1.getScript());
		
		
		egu.addTbarText("煤矿:");
		ComboBox changbcb = new ComboBox();
		changbcb.setTransform("ChangbSelect");
		changbcb.setWidth(130);
		changbcb
				.setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
		egu.addToolbarItem(changbcb.getScript());

//		 刷新按钮
		GridButton refurbish = new GridButton(GridButton.ButtonType_Refresh,
				"gridDiv", egu.getGridColumns(), "RefurbishButton");
		egu.addTbarBtn(refurbish);
		ComboBox bianm = new ComboBox();
		bianm.setTransform("BianhSelect");
		bianm.setWidth(100);
		bianm
				.setListeners("select:function(own,rec,index){Ext.getDom('BianhSelect').selectedIndex=index}");
//		加载不同模式下的按钮
		if(MOD_SC.equals(visit.getString2())){
//			 生成按钮
			GridButton create = new GridButton("生成",GridButton.ButtonType_SubmitSel,
					"gridDiv", egu.getGridColumns(), "CreateButton");
			egu.addTbarBtn(create);
			egu.addTbarText("编码:");
			egu.addToolbarItem(bianm.getScript());

			// 合并按钮
			GridButton save = new GridButton("合并",GridButton.ButtonType_SubmitSel, "gridDiv",
					egu.getGridColumns(), "HebingButton");
			egu.addTbarBtn(save);
		}else{
//			 删除按钮
			GridButton delete = new GridButton("删除",GridButton.ButtonType_SubmitSel,
					"gridDiv", egu.getGridColumns(), "DeleteButton");
			egu.addTbarBtn(delete);
			egu.addTbarText("编码:");
			egu.addToolbarItem(bianm.getScript());
			// 调整按钮
			GridButton save = new GridButton("调整",GridButton.ButtonType_SubmitSel, "gridDiv",
					egu.getGridColumns(), "UpdateButton");
			egu.addTbarBtn(save);
		}
		
		setExtGrid(egu);
		con.Close();

	}
	/**
	 * 
	 * @param con	JDBCConnection
	 * @param buttonType	按钮类型
	 * @param diancxxb_id	电厂ID
	 * @return	得到的质量表ID
	 * @description	根据不同的按钮类型得到质量ID 
	 * 				如果按钮为生成则直接插入一个新增质量
	 * 				如果按钮为合并或调整则得到页面所选的质量ID 
	 * 				如果按钮为删除则得到的质量ID为0
	 */
	private long getZhilid(JDBCcon con, String buttonType, long diancxxb_id){
		long zhilb_id = 0;
		if(BtnType_SC.equals(buttonType)){
			zhilb_id = Jilcz.getZhilbid(con, null, new Date(), diancxxb_id);
			int flag = Caiycl.CreatBianh(con,zhilb_id,diancxxb_id);
			if(flag ==-1) {
				return flag;
			}
		}else if(BtnType_HB.equals(buttonType) || BtnType_TZ.equals(buttonType)){
			if(getBianhValue()!=null&& getBianhValue().getId() != -1){
				zhilb_id = getBianhValue().getId();
			}else{
				return -1;
			}
		}else if(BtnType_DL.equals(buttonType)){
			
		}
		return zhilb_id;
	}
	
	private boolean Save(String buttonType){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有未提交改动！");
			return false;
		}
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		con.setAutoCommit(false);
		String chepbids = ""; //记录更改过的车皮ID
		String sql = ""; //sql语句
		List fahlist = new ArrayList(); //记录发货id
		//得到选中的所有车皮ID
		ResultSetList rs = getExtGrid().getModifyResultSet(getChange());
		if(rs.getRows()>0){
			while(rs.next()){
				chepbids += "," + rs.getString("id");
			}
			rs.close();
			chepbids = chepbids.substring(1);
		}
		sql = 
			"select c.fahb_id,max(f.ches) zcs,count(c.id) xcs from fahb f, chepb c\n" +
			"where f.id = c.fahb_id\n" + 
			"and c.id in ("+chepbids+")\n" + 
			"group by c.fahb_id";
		rs = con.getResultSetList(sql);
//		得到质量
		long zhilb_id = getZhilid(con,buttonType,visit.getDiancxxb_id());
//		循环发货
		while(rs.next()){
			long upfahb_id = rs.getLong("fahb_id");
			Jilcz.addFahid(fahlist, String.valueOf(upfahb_id));
//			是否拆分发货
			if(rs.getInt("zcs") != rs.getInt("xcs")){
//				复制发货
				String newfahid = Jilcz.CopyFahb(con, String.valueOf(upfahb_id), visit.getDiancxxb_id());
//				记录已更改发货ID
				Jilcz.addFahid(fahlist, newfahid);
//				根据页面设置的模式来决定是未选车还是已选车更新至发货
				String sqltmp = "";
				if(MOD_SC.equals(visit.getString2())){
					sqltmp = "not";
				}
				sql = " update chepb set fahb_id =" + newfahid + " where id "+sqltmp+" in (" + chepbids + ") and fahb_id =" +upfahb_id;
				con.getUpdate(sql);
			}
//			更新发货质量ID
			if(zhilb_id != -1){
				sql = "update fahb set zhilb_id = " + zhilb_id + " where id = " + upfahb_id;
				con.getUpdate(sql);
			}else{
				setMsg("操作失败！");
				return false;
			}
		}
		rs.close();
//		判断是否单车计算运损
		boolean isDancYuns = SysConstant.CountType_Yuns_dc.equals(MainGlobal.getXitxx_item("数量", "运损计算方法", String.valueOf(visit.getDiancxxb_id()), "单车"));
//		循环
		for (int i = 0; i < fahlist.size(); i++) {
			String fahbid = (String) fahlist.get(i);
			if(isDancYuns){
				sql = "select id,hedbz from chepb where fahb_id =" + fahbid;
				rs = con.getResultSetList(sql);
				while(rs.next()){
					Jilcz.CountChepbYuns(con, rs.getString("id"), rs.getInt("hedbz"));
				}
				rs.close();
			}
			int flag = Jilcz.updateFahb(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg007);
				setMsg(ErrorMessage.Yundxg007);
				return false;
			}
			flag = Jilcz.updateLieid(con, fahbid);
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.Yundxg008);
				setMsg(ErrorMessage.Yundxg008);
				return false; 
			}
		}
		con.commit();
		con.Close();
		return true;
	}

	private void init() {
		setChangbModels();
		setBianhModels();
		setExtGrid(null);
		initGrid();
	} 

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		String yunsfs = cycle.getRequestContext().getParameter("lx");
		if (yunsfs != null) {
			visit.setString1(yunsfs);
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
		String MOD = cycle.getRequestContext().getParameter("mod");
		if (MOD != null) {
			visit.setString2(MOD);
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			if (yunsfs == null) {
				visit.setString1(YUNSFS_ALL);
			}
			if (MOD == null) {
				visit.setString2(MOD_SC);
			}
			setRiq(DateUtil.FormatDate(new Date()));
			setRiqe(DateUtil.FormatDate(new Date()));
			setRiqsetModels();
			riqchange = true;
		}
		if(riqchange){
			riqchange = false;
			init();
		}
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