package com.zhiren.dc.jilgl.huaiccl;

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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.DateUtil;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：王总兵
 * 时间：2010-1-29 16:27:05
 * 描述：煤场坏车处理,这个车坏到煤场上了,2天以后才回皮,需要把这一车新生成的化验编号改为这一车过重车
 *       那一天的化验编号
 */
public class Huaiccl extends BasePage implements PageValidateListener{	
//	界面用户提示
	private static final String customKey = "Huaiccl";
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
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
//	刷新数据日期绑定
	private String riq;
	public void setRiq(String riq) {
		this.riq = riq;
	}
	public String getRiq() {
		return riq;
	}
//	页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
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
		if(getTbmsg()!=null) {
			getExtGrid().addToolbarItem("'->'");
			getExtGrid().addToolbarItem("'<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>'");
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}
	
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	
	
	private boolean _DeleteChick=false;
	public void DeleteButton(IRequestCycle cycle){
		_DeleteChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if(_DeleteChick){
				_DeleteChick=false;
				delete();
				getSelectData();
			}
	}

	private void delete(){
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有做出任何更改哦！");
			return;
		}
		String fahb_id=getChange().substring(0, getChange().indexOf(','));
		String zhilb_id=getChange().substring(getChange().indexOf(',')+1,getChange().length());
		
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		
		int flag=0;
		
		
		flag=con.getDelete("delete zhuanmb z where z.zhillsb_id in(select id from zhillsb where zhilb_id="+zhilb_id+")");
			if(flag==-1){
				setMsg("删除zhuanmb表时发生错误!");
				
				con.rollBack();
				con.Close();
				return ;
		}
		flag=con.getDelete("delete zhillsb l where  zhilb_id="+zhilb_id+"");
			if(flag==-1){
				setMsg("删除zhillsb表时发生错误!");
				
				con.rollBack();
				con.Close();
				return ;
		}
		
			
		String chaxtj=
				"select f.zhilb_id from fahb f\n" +
				"where f.daohrq=to_date((select to_char(min(c.zhongcsj),'yyyy-mm-dd') as zhongcsj from chepb c where c.fahb_id="+fahb_id+"),'yyyy-mm-dd')\n" + 
				"and f.gongysb_id=(select f.gongysb_id from fahb f where f.id="+fahb_id+")\n" + 
				"and f.meikxxb_id=((select f.meikxxb_id from fahb f where f.id="+fahb_id+"))"+
				"and f.yunsfsb_id=2";

		ResultSetList rslc = con.getResultSetList(chaxtj);	
		if(rslc.next()){
			long old_zhilb_id=rslc.getLong("zhilb_id");
			if(zhilb_id.equals(String.valueOf(old_zhilb_id))){
				setMsg("要调整的采样编号和目前的采样编号一致,不需要调整");
				con.rollBack();
				con.Close();
				return ;
				
			}
			 flag=con.getUpdate("update fahb set zhilb_id="+old_zhilb_id+" where id="+fahb_id);
		}else{
			setMsg("重车过衡当天无该矿已经生成的采样编号,合并失败");
			con.rollBack();
			con.Close();
			return ;
		}
			
		
		con.commit();
		con.Close();
		
		if(flag!=-1){
			this.setMsg("调整采样编号成功!");
		}else{
			this.setMsg("调整采样编号失败!");
		}
	}
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long hengh=this.getHengqValue().getId();
		String yunsfs="";
		if(hengh==1){
			yunsfs="and yunsfs = '公路' ";
		}else {
			yunsfs="and yunsfs = '公路' ";
		}
		
		String sSql ="select f.id as id, f.zhilb_id,d.mingc as diancxxb_id,g.mingc as gongysb_id,m.mingc as meikxxb_id,f.daohrq,f.ches,f.maoz,f.piz,f.jingz\n" +
			"from fahb f ,diancxxb d,gongysb g,meikxxb m\n" + 
			" where f.diancxxb_id=d.id\n" + 
			" and f.gongysb_id=g.id\n" + 
			" and f.meikxxb_id=m.id\n" + 
			" and f.diancxxb_id="+visit.getDiancxxb_id()+"\n" + 
			" and f.yunsfsb_id=2\n" + 
			" and f.daohrq=to_date('"+this.getRiq()+"','yyyy-mm-dd')\n" + 
			" order by f.diancxxb_id,f.gongysb_id,f.meikxxb_id";

		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		egu.setTableName("fahb");
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		egu.getColumn("zhilb_id").setHeader("zhilb_id");
		egu.getColumn("zhilb_id").setHidden(true);
		egu.getColumn("zhilb_id").setEditor(null);
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setEditor(null);
		
		
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(80);
		
		egu.getColumn("jingz").setEditor(null);
		
	
		
		egu.addTbarText("日期:");
		DateField dfRIQ = new DateField();
		dfRIQ.Binding("RIQ", "");
		dfRIQ.setValue(getRiq());
		egu.addToolbarItem(dfRIQ.getScript());
		egu.addTbarText("-");
		
		egu.addTbarText("类别:");
		ComboBox comb3 = new ComboBox();
		comb3.setTransform("HENGQ");
		comb3.setId("HENGQ");
		comb3.setLazyRender(true);// 动态绑定
		comb3.setWidth(80);
		egu.addToolbarItem(comb3.getScript());
		egu.addTbarText("-");
		String sRefreshHandler = 
				"function(){var grid_Mrcd = gridDiv_ds.getModifiedRecords();" +
				"if (grid_Mrcd.length>0) {" +
				"Ext.MessageBox.confirm('消息提示','刷新界面后您所做的更改将不被保存,是否继续?',function(btn){" +
				"if (btn == 'yes') {" +
				"document.getElementById('RefreshButton').click();" +
				"}" +
				"})" +
				"}else {document.getElementById('RefreshButton').click();}" +
				"}";

		GridButton gRefresh = new GridButton("刷新",sRefreshHandler);
		gRefresh.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gRefresh);
		egu.addTbarText("-");
		egu.addTbarText("-");
		String sDeleteHandler = 
			"function(){"
			+"if(gridDiv_sm.getSelected() == null){"
			+"	 Ext.MessageBox.alert('提示信息','请先单击要合并的煤矿所在行');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDiv_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('提示信息','您确定要调整&nbsp;煤矿:&nbsp;'+ grid_rcd.get('MEIKXXB_ID')+',&nbsp;&nbsp;&nbsp;'+grid_rcd.get('DAOHRQ')+'日已经生成的采样编码合并至重车过衡当天的采样编吗?',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('ID')+','+grid_rcd.get('ZHILB_ID');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('DeleteButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		GridButton delte = new GridButton("调整采样编号",sDeleteHandler);
		delte.setIcon(SysConstant.Btn_Icon_Count);
		egu.addTbarBtn(delte);
		setExtGrid(egu);
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

	
//	 新衡旧衡
	public IDropDownBean getHengqValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getHengqModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setHengqValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public void setHengqModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getHengqModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {
			getHengqModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void getHengqModels() {
		List list = new ArrayList();
		
		list.add(new IDropDownBean(1, "汽车"));
		
		
		((Visit) getPage().getVisit()).setProSelectionModel8(new IDropDownModel(list));
		return;
	}
	
	
	public void beginResponse(IMarkupWriter writer,IRequestCycle cycle){
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setDropDownBean8(null);
			visit.setProSelectionModel8(null);
			init();
		}
	}
	
	private void init() {
		setExtGrid(null);
		setRiq(DateUtil.FormatDate( new Date()));
		
		getSelectData();
	}
}
