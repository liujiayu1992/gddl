package com.zhiren.zidy.duibcx;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.TreeNode;
import com.zhiren.common.ext.tree.TreeOperation;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.zidy.Aotcr;
import com.zhiren.zidy.ZidyCss;
import com.zhiren.zidy.ZidyOperation;
import com.zhiren.zidy.ZidyParam;

/*
 * 作者：王磊
 * 时间：2009-08-18 10：12
 * 描述：增加共享、复制按钮 将控制面板改为不可collapse extend
 */
/**
 * @author Rock
 * @since 2009-02-11
 * @version 2.0
 * 
 */
public class DuibcxIndex extends BasePage implements PageValidateListener{
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
	
	private boolean isShowReport = false;
	
	private int _CurrentPage = 1;
	public int getCurrentPage() {
		return _CurrentPage;
	}
	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = 1;
	public int getAllPages() {
		return _AllPages;
	}
	public void setAllPages(int _value) {
		_AllPages = _value;
	}
	
//	页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	private String FieldChange;
	
	public String getFieldChange() {
		return FieldChange;
	}
	public void setFieldChange(String value) {
		FieldChange = value;
	}
	
	private String html;
	
	public String getHtml() {
		return html;
	}
	public void setHtml(String value) {
		html = value;
	}
	
	public boolean getRaw() {
		return true;
	}
	
	public SortMode getSort() {
		return SortMode.USER;
	}
	String groupId;
	public String getGroupId(){
		return groupId;
	}
	public void setGroupId(String gid){
		groupId = gid;
	}
	
//	public String getCurrentAotcrId(){
//		return ((Visit)this.getPage().getVisit()).getString2();
//	}
//	
//	public void setCurrentAotcrId(String nav){
//		((Visit)this.getPage().getVisit()).setString2(nav);
//	}
	
	public String getNavigetion(){
		return ((Visit)this.getPage().getVisit()).getString3();
	}
	
	public void setNavigetion(String nav){
		((Visit)this.getPage().getVisit()).setString3(nav);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	public Aotcr getAotcr() {
		return ((Visit)this.getPage().getVisit()).getAotcr();
	}
	public void setAotcr(Aotcr aotcr) {
		((Visit)this.getPage().getVisit()).setAotcr(aotcr);
	}

	private boolean _StrQitClick = false;
	public void StrQitButton(IRequestCycle cycle) {
		_StrQitClick = true;
	}
	private boolean _SearchClick = false;
	public void SearchButton(IRequestCycle cycle) {
		_SearchClick = true;
	}
	private boolean _SelectRptClick = false;
	public void SelectRptButton(IRequestCycle cycle) {
		_SelectRptClick = true;
	}
	private boolean _DeleteClick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteClick = true;
	}
	private boolean _InsertClick = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertClick = true;
	}
	private boolean _ModifyClick = false;
	public void ModifyButton(IRequestCycle cycle) {
		_ModifyClick = true;
	}
	private boolean _ShareClick = false;
	public void ShareButton(IRequestCycle cycle) {
		_ShareClick = true;
	}
	private boolean _CopyClick = false;
	public void CopyButton(IRequestCycle cycle) {
		_CopyClick = true;
	}
	
	private String _kjValue;
	public void setKjValue(String value){
		this._kjValue=value;
	}
	public String getKjValue(){
		return this._kjValue;
	}
	
//报表口径 (电厂口径)
	public IDropDownBean getBaobkjValue(){
		if(((Visit) getPage().getVisit()).getDropDownBean1()==null){
			for(int i=0;i<getBaobkjModel().getOptionCount();i++){
				 Object obj = getBaobkjModel().getOption(i);
				 if(getKjValue().equals(((IDropDownBean)obj).getStrId())){
					 ((Visit) getPage().getVisit()).setDropDownBean1((IDropDownBean)obj);
				 }
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}
	public void setBaobkjValue(IDropDownBean value){
		((Visit) getPage().getVisit()).setDropDownBean1(value);
	}
	public IPropertySelectionModel getBaobkjModel(){
		if(((Visit) getPage().getVisit()).getProSelectionModel1() == null){
			setBaobkjModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}
	public void setBaobkjModel(IPropertySelectionModel value){
		((Visit)this.getPage().getVisit()).setProSelectionModel1(value);
	}
	public void setBaobkjModels(){
		JDBCcon con=new JDBCcon();
		String sql="select id,mingc from dianckjb order by id";
		if(con.getHasIt(sql)){
			setBaobkjModel(new IDropDownModel(sql));
		}else{
			setBaobkjModel(new IDropDownModel(sql,"请先设置报表口径！"));
		}
		con.Close();
	}
	
	public void submit(IRequestCycle cycle) {
		if (_StrQitClick) {
			_StrQitClick = false;
			setParameters();
		}
		if (_SearchClick) {
			_SearchClick = false;
			setParameters();
			Search();
			isShowReport = true;
		}
		if (_SelectRptClick) {
			_SelectRptClick = false;
			showReport();
			initNavigation();
			isShowReport = true;
		}
		
		if (_InsertClick) {
			_InsertClick = false;
			Insert();
			initNavigation();
		}
		if (_ModifyClick) {
			_ModifyClick = false;
			Modify(cycle);
		}
		if (_ShareClick) {
			_ShareClick = false;
			Share();
			initNavigation();
		}
		if (_DeleteClick) {
			_DeleteClick = false;
			Delete();
			initNavigation();
		}
		if (_CopyClick) {
			_CopyClick = false;
			Copy();
			initNavigation();
		}
	}
	private void Insert(){
		if(getChange()==null || "".equals(getChange())
		||	getFieldChange()==null || "".equals(getFieldChange())) {
			return;
		}
		Visit v = (Visit) getPage().getVisit();
		DuibcxOperation.SaveDesign(v.getDiancxxb_id(),v.getRenyID(),v.getRenymc(),getChange(),getFieldChange());
	}
	
	private void Modify(IRequestCycle cycle){
		Visit v = (Visit) getPage().getVisit();
		if(getChange()!=null && !"".equals(getChange())) {
			String sql = "select * from zonghfxdbglb where zidyfa_id = " +
			getChange() + " and renyxxb_id = " + v.getRenyID();
			JDBCcon con = new JDBCcon();
			if(con.getHasIt(sql)){
				v.setString1(getChange());
				con.Close();
			}else{
				setMsg("此模版不是您创建的,不能进行编辑,如需编辑请先复制。");
				con.Close();
				return;
			}
		}else{
			v.setString1("");
		}
		cycle.activate("DuibcxModify");
	}
	
	private void Delete(){
		if(getChange()!=null && !"".equals(getChange())) {
			Visit v = (Visit) getPage().getVisit();
			String sql = "select * from zonghfxdbglb where zidyfa_id = " +
			getChange() + " and renyxxb_id = " + v.getRenyID();
			JDBCcon con = new JDBCcon();
			if(con.getHasIt(sql)){
				v.setString1(getChange());
			}else{
				setMsg("此模版不是您创建的,不能进行删除。");
				con.Close();
				return;
			}
			con.setAutoCommit(false);
			StringBuffer s=new StringBuffer();
			s.append("begin \n");
			s.append("delete from ZONGHFXDBGLB where diancxxb_id = ").append(v.getDiancxxb_id())
			.append(" and renyxxb_id = ").append(v.getRenyID())
			.append(" and zidyfa_id = ").append(getChange()).append(";\n");
			s.append("delete from zidyfa where id="+getChange()+";\n");
			s.append("delete from zidyfacsb where zidyfa_id="+getChange()+";\n");
			s.append("delete from duibcxsjlpzb where duibcxsjlb_id in (select id from duibcxsjlb where zidyfa_id="+getChange()+") ;\n");
			s.append("delete from duibcxsjlb where zidyfa_id="+getChange()+" ;\n");
			s.append("end;");
			int re=con.getDelete(s.toString());
			if(re==-1){
				setMsg("删除失败！");
				con.rollBack();
				con.Close();
				return;
			}
			setMsg("删除成功！");
			con.commit();
			con.Close();
		}
	}
	
	private void Share(){
		if(getChange()!=null && !"".equals(getChange())) {
			Visit v = (Visit) getPage().getVisit();
			JDBCcon con = new JDBCcon();
			String sql = "select distinct z.wenjwz\n" +
			"from ziyxxb z,zuqxb zq,renyzqxb r\n" + 
			"where r.zuxxb_id = zq.zuxxb_id\n" + 
			"and r.renyxxb_id = "+v.getRenyID()+"\n" + 
			"and z.id = zq.ziyxxb_id and z.wenjwz = 'Share'";
			if(!con.getHasIt(sql)){
				setMsg("您无权做共享操作。");
				con.Close();
				return;
			}
			sql = "select * from zonghfxdbglb where zidyfa_id = " +
			getChange() + " and renyxxb_id = " + v.getRenyID() + " and zhuangt = 0";
			ResultSetList rs = con.getResultSetList(sql);
			if(rs.next()){
				sql = "update ZONGHFXDBGLB set zhuangt = 1 where id = " +
				rs.getString("id");
				con.getUpdate(sql);
				con.Close();
			}else{
				setMsg("此模版已经处于共享状态。");
				con.Close();
				return;
			}
		}
	}
	
	private void Copy(){
		if(getChange()==null || "".equals(getChange())) {
			return;
		}
		Visit v = (Visit) getPage().getVisit();
		DuibcxOperation.CopyDesign(v.getDiancxxb_id(), v.getRenyID(), v.getRenymc(), getChange());
			
	}
	
	private void showReport(){
		if(getChange()!=null && !"".equals(getChange())) {
			Visit v = (Visit) getPage().getVisit();
			//setCurrentAotcrId(getChange());
			setAotcr(new Aotcr(getChange()));
			getAotcr().setIPage(this);
			ZidyParam zdc = new ZidyParam();
			zdc.setId(null);
			zdc.setName("电厂ID");
			zdc.setSvalue(String.valueOf(v.getDiancxxb_id()));
			getAotcr().getParaMeters().add(zdc);
//			
//			getAotcr().setData();
			v.setString1(getChange());
			initToolBars();
		}else{
			setMsg("此报表已被删除或定义缺失！");
		}
	}
	
	private void Search() {
		if(getChange()!=null && !"".equals(getChange())) {
			List plist = getAotcr().getParaMeters();
			String[] params = getChange().split(";");
			for(int i=0;i<plist.size();i++) {
				ZidyParam p = (ZidyParam)plist.get(i);
				for(int j =0; j< params.length ; j++) {
					if(p.getName().equals(params[j].split("=")[0])) {
						p.setSvalue(params[j].split("=")[1]);
						break;
					}
				}
			}
		}
	}
	
	public void initItems(){
//		初始化报表ID
		((Visit) getPage().getVisit()).setString1("-99999");
//		初始化左侧导航栏数据
		initNavigation();
//		初始化报表
		setAotcr(new Aotcr(((Visit) getPage().getVisit()).getString1()));
//		初始化工具栏
		initToolBars();
	}
	
	public void initNavigation(){
		setNavigetion("");
		Visit v = (Visit) getPage().getVisit();
//		导航栏树的查询SQL
		String sql = 

			"select id,mingc,jib,fuid from\n" +
			"(select 0 xh, 0 id,'根' mingc,1 jib ,-1 fuid from dual\n" + 
			"union\n" + 
			"select 1,1,'月报对比',2,0 from dual\n" + 
			"union\n" + 
			"select 2,z.id,z.z_name,3,1 from zidyfa z,ZONGHFXDBGLB g\n" + 
			"where z.z_remark = '月报对比' and z.id = g.zidyfa_id and g.zhuangt = 0\n" + 
			"and g.diancxxb_id = "+v.getDiancxxb_id()+" and g.renyxxb_id = "+v.getRenyID()+" union\n" + 
			"select 3,2,'日期段对比',2,0 from dual\n" + 
			"union\n" + 
			"select 4,z.id,z.z_name,3,2 from zidyfa z,ZONGHFXDBGLB g\n" + 
			"where z.z_remark = '日期段对比' and z.id = g.zidyfa_id and g.zhuangt = 0\n" + 
			"and g.diancxxb_id = "+v.getDiancxxb_id()+" and g.renyxxb_id = "+v.getRenyID()+"\n" +
			"union\n" + 
			"select 5,3,'共享',2,0 from dual\n" +
			"union\n" + 
			"select 6,z.id,z.z_name,5,2 from zidyfa z,ZONGHFXDBGLB g\n" + 
			"where z.id = g.zidyfa_id and g.zhuangt = 1\n" + 
			"and g.diancxxb_id = "+v.getDiancxxb_id() + ")";

		TreeOperation dt = new TreeOperation();
		TreeNode node = dt.getTreeRootNode(sql);
		
		((TreeNode)node.getNodeById("1")).setExpanded(true);
		((TreeNode)node.getNodeById("2")).setExpanded(true);
		((TreeNode)node.getNodeById("3")).setExpanded(true);
//		if(getAotcr() != null && !"-99999".equals(v.getString1())){
//			((TreeNode)(node.getNodeById(v.getString1()).getParentNode())).setExpanded(true);
//		}
		String treeNodeScript = dt.getTreeNodeScript("navTree", node);
		setNavigetion(treeNodeScript);
	}
	
	public void initToolBars() {
		Visit v = (Visit) getPage().getVisit();
		Toolbar tb1 = new Toolbar("tbdiv");
		StringBuffer StrSearch = new StringBuffer("change = \"\"");
		//StringBuffer StrRefurbish = new StringBuffer("fieldchange = \"\"");
		StringBuffer StrHtml = new StringBuffer();
		List plist = getAotcr().getParaMeters();
		for(int i=0; i< plist.size(); i++) {
			ZidyParam p = (ZidyParam)plist.get(i);
			String id = p.getId();
			if(id == null) {
				continue;
			}
			String paramName = p.getName();
			String ctrl = p.getCtrl();
			String cwidth = p.getCtrlWidth();
			String ptext = p.getPtext();
			String value = p.getFvalue();
			String sql = p.getDssql();
			if(!"".equals(ptext)) {
				ToolbarText tt = new ToolbarText(ptext);
				tb1.addText(tt);
			}
			StrSearch.append("+ \"" + paramName + "=\"+");
			//StrRefurbish.append("+ \"" + paramName + "=\"+");
			if(ZidyOperation.param_Ctrl_textField.equals(ctrl)) {
				TextField tf = new TextField();
				tf.setId("tf"+id);
				tf.setValue(value);
				tf.setWidth(cwidth);
				tb1.addField(tf);
				StrSearch.append("\"'\"+"+"tf"+id+".getValue()"+"+\"'=\"");
				StrSearch.append("+tf"+id+".getValue()"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_numberField.equals(ctrl)) {
				NumberField nf = new NumberField();
				nf.setId("nf"+id);
				nf.setValue(value);
				nf.setWidth(cwidth);
				tb1.addField(nf);
				StrSearch.append("nf"+id+".getValue()"+"+\"=\"");
				StrSearch.append("+nf"+id+".getValue()"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_dateField.equals(ctrl)) {
				DateField df = new DateField();
				df.setId("df"+id);
				df.setValue(value);
				df.setWidth(cwidth);
				tb1.addField(df);
				StrSearch.append("\"to_date('\"+"+"df"+id+".getValue().dateFormat('Y-m-d')"+"+\"','yyyy-mm-dd')=\"");
				StrSearch.append("+df"+id+".getValue().dateFormat('Y-m-d')"+"+\";\"");
			}else if(ZidyOperation.param_Ctrl_combo.equals(ctrl) ) {
				ComboBox cb = new ComboBox();
				cb.setId("db"+id);
				cb.setTransform("cb_db"+id);
				sql = ZidyOperation.getDataSourceSql(plist,sql);
				if("".equals(sql)){
					continue;
				}
				IDropDownModel idd = new IDropDownModel(sql);
				StrHtml.append("<select style='display:none' name = 'cb_db").append(id).append("'>");
				for(int m = 0 ; m < idd.getOptionCount() ; m++) {
					StrHtml.append("<option value='").append(((IDropDownBean)idd.getOption(m)).getStrId()).append("'");
					if(value.equals(((IDropDownBean)idd.getOption(m)).getValue())) {
						StrHtml.append(" selected");
					}
					StrHtml.append(">").append(((IDropDownBean)idd.getOption(m)).getValue()).append("</option>\n");
				}
				StrHtml.append("</select>");
				cb.setValue(value);
				cb.setWidth(cwidth);
				tb1.addField(cb);
				StrSearch.append("db"+id+".getValue()"+"+\"=\"");
				StrSearch.append("+db"+id+".getRawValue()"+"+\";\"");
			}
		}
		
		StrSearch.append(";\n");
		
//		此处添加分组的选择
		if(v.getString1() != null && !"-99999".equals(v.getString1())){
			String sql = "select distinct p.z_value\n" +
			"       from duibcxsjlb d, duibcxsjlpzb p\n" + 
			"where d.zidyfa_id = "+v.getString1()+" and d.id = p.duibcxsjlb_id\n" + 
			"and p.z_code = '"+SysConstant.CustomAttribute_DataSource+"'";
			JDBCcon con = new JDBCcon();
			ResultSetList rs = con.getResultSetList(sql);
			int num = rs.getRows();
			rs.close();
			sql = 
				"select min(z.id) id,count(z.id) num,groupname\n" +
				"from zidyjcsjyfz z\n" + 
				"where z.zidyjcsjy_id in (" +
				"select distinct p.z_value\n" +
				"       from duibcxsjlb d, duibcxsjlpzb p\n" + 
				"where d.zidyfa_id = "+v.getString1()+" and d.id = p.duibcxsjlb_id\n" + 
				"and p.z_code = '"+SysConstant.CustomAttribute_DataSource+"'"+") group by groupname\n";;
			rs = con.getResultSetList(sql);
			List GroupModel = new ArrayList();
			while(rs.next()){
				if(num == rs.getInt("num")){
					GroupModel.add(new IDropDownBean(rs.getString("id"),rs.getString("groupname")));
				}
			}
			rs.close();
			ComboBox cb = new ComboBox();
			cb.setId("db_groupModel");
			cb.setTransform("cb_db_groupModel");
			cb.setListeners("'select':function(own,rec,row){document.getElementById(\"GROUPID\").value = own.value;}");
			IDropDownModel idd = new IDropDownModel(GroupModel);
			StrHtml.append("<select style='display:none' name = 'cb_db_groupModel").append("'>");
			for(int m = 0 ; m < idd.getOptionCount() ; m++) {
				if(getGroupId() == null || "".equals(getGroupId())){
					setGroupId(((IDropDownBean)idd.getOption(m)).getStrId());
				}
				StrHtml.append("<option value='").append(((IDropDownBean)idd.getOption(m)).getStrId()).append("'");
				if(getGroupId()!=null && getGroupId().equals(((IDropDownBean)idd.getOption(m)).getStrId())) {
					StrHtml.append(" selected");
				}
				StrHtml.append(">").append(((IDropDownBean)idd.getOption(m)).getValue()).append("</option>\n");
			}
			StrHtml.append("</select>");
	//		cb.setValue(value);
			cb.setWidth(80);
			tb1.addField(cb);
		}
		setHtml(StrHtml.toString());
		
		tb1.addText(new ToolbarText("报表口径："));
		ComboBox cb=new ComboBox();
		cb.setTransform("Baobkj");
		cb.setId("Baobkj");
		cb.setLazyRender(true);
		cb.setListeners("change:function(own,nv,ov){document.getElementById('kjValue').value=nv}");
		cb.setEditable(false);
		cb.setWidth(80);
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));
		
		String StrSearchClick =  StrSearch + "document.getElementById('CHANGE').value = change; document.getElementById('SearchButton').click();MsgShow();";
		ToolbarButton sbtn = new ToolbarButton(null,"查询","function(){"+StrSearchClick+"}");
		sbtn.setIcon(SysConstant.Btn_Icon_Search);
		tb1.addItem(sbtn);
		tb1.setWidth("bodyWidth");
		setToolbar(tb1);
		
	}

	public String getPrintTable() {
		if(isShowReport){
			isShowReport = false;
			if(getAotcr() == null) {
				return "";
			}
			if(getGroupId() == null || "".equals(getGroupId())){
				return "";
			}
			Visit v = (Visit) getPage().getVisit();
			Report rt = DuibcxOperation.CreateReport(v.getString1(), getGroupId(), getAotcr().getParaMeters(),getBaobkjValue().getStrId());//v.isJTUser()?2:v.isGSUser()?1:v.isFencb()&&v.isDCUser()?-1:0,
			if(rt == null){
				return "";
			}
			setCurrentPage(1);
			setAllPages(rt.getPages());
			return rt.getAllPagesHtml();
		}
		return "";
	}
	
	public void setParameters() {
		if(getChange()!=null && !"".equals(getChange())) {
			List plist = getAotcr().getParaMeters();
			String[] params = getChange().split(";");
			for(int i=0;i<plist.size();i++) {
				ZidyParam p = (ZidyParam)plist.get(i);
				for(int j =0; j< params.length ; j++) {
					if(p.getName().equals(params[j].split("=")[0])) {
						p.setSvalue(params[j].split("=")[1]);
						p.setFvalue(params[j].split("=")[2]);
						break;
					}
				}
			}
		}
		initToolBars();
	}
	
	public String getAotcrCss() {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<getAotcr().getCss().size();i++) {
			sb.append(((ZidyCss)getAotcr().getCss().get(i)).getText()).append("\n");
		}
		return sb.toString();
	}
	
//	 ******************页面初始设置********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit v = (Visit) getPage().getVisit();
		if (!v.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			v.setActivePageName(getPageName().toString());
			v.setDropDownBean1(null);
			v.setProSelectionModel1(null);
			setBaobkjModels();
			setKjValue(((IDropDownBean)getBaobkjModel().getOption(0)).getStrId());
			initItems();
		}
		//setKjValue(getBaobkjValue().getValue());
	}
	
	public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}
	
}