package com.zhiren.dc.jilgl.shujdr;

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
 * 时间：2009-12-25 10:10:32
 * 描述：本页面处理数据导入后发现导入错误.删除已经导入的chepb,fahb,采样编号等,可以再重新导入
 */
public class Shujdr_Delete extends BasePage implements PageValidateListener{	
//	界面用户提示
	private static final String customKey = "Shujdr_Delete";
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
		String gongys=getChange().substring(0, getChange().indexOf(','));
		String meik=getChange().substring(getChange().indexOf(',')+1,getChange().length());
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con=new JDBCcon();
		con.setAutoCommit(false);
		long hengh=this.getHengqValue().getId();
		String yunsfs="";
		if(hengh==1){
			yunsfs=" '公路'";
		}else if(hengh==2){
			yunsfs=" '铁路'";
		}else {
			yunsfs=" '公路'";
		}
		int flag=0;
		String riqTiaoj = this.getRiq();
		
		String chaxsql=
			"select f.id,f.zhilb_id from fahb f\n" +
			"where f.gongysb_id=(select id from gongysb where mingc='"+gongys+"')\n" + 
			"and f.meikxxb_id=(select id from meikxxb where mingc='"+meik+"')\n" + 
			"and f.yunsfsb_id=(select id from yunsfsb where mingc="+yunsfs+")\n" + 
			"and f.daohrq=to_date('"+riqTiaoj+"','yyyy-mm-dd')";
		
		ResultSetList rslc = con.getResultSetList(chaxsql);
		String fahb_id="";
		String zhilb_id="";
		while(rslc.next()){
			fahb_id=rslc.getString("id");
			flag=con.getDelete("delete chepb c where c.fahb_id="+fahb_id+"");
			if(flag==-1){
				setMsg("删除chepb表时发生错误!");
				rslc.close();
				con.rollBack();
				con.Close();
				return ;
			}
			
			zhilb_id=rslc.getString("zhilb_id");
			flag=con.getDelete("delete zhuanmb z where z.zhillsb_id in(select id from zhillsb where zhilb_id="+zhilb_id+")");
			if(flag==-1){
				setMsg("删除zhuanmb表时发生错误!");
				rslc.close();
				con.rollBack();
				con.Close();
				return ;
			}
			flag=con.getDelete("delete zhillsb l where  zhilb_id="+zhilb_id+"");
			if(flag==-1){
				setMsg("删除zhillsb表时发生错误!");
				rslc.close();
				con.rollBack();
				con.Close();
				return ;
			}
			
		}
		
		String sql=" update chepbtmp set fahb_id=0 where daohrq= to_date('"+ riqTiaoj
		+ "','yyyy-mm-dd') "+ Jilcz.filterDcid(visit, "")+" and gongysmc='"+gongys+"'   and meikdwmc='"+meik+"' and yunsfs= "+yunsfs+"";
		
		//System.out.println(sql);
		 flag=con.getUpdate(sql);
		con.commit();
		con.Close();
		
		if(flag!=-1){
			this.setMsg(this.getRiq()+meik+"采样编号删除成功!");
		}else{
			this.setMsg(this.getRiq()+meik+"采样编号删除失败!");
		}
	}
	
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		long hengh=this.getHengqValue().getId();
		String yunsfs="";
		if(hengh==1){
			yunsfs="and yunsfs = '公路' ";
		}else if(hengh==2){
			yunsfs=" and yunsfs = '铁路' ";
		}else {
			yunsfs="and yunsfs = '公路' ";
		}
		
		String sSql =
			"select diancxxb_id,gongysmc as gongysb_id,meikdwmc as meikxxb_id,faz as faz_id,daoz as daoz_id,pinz as pinzb_id,jihkj as jihkjb_id,fahrq,daohrq,caiybh,chec,nvl(max(FAHBTMP_ID),-1) as ID,\n" +
			"count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n" + 
			"from chepbtmp where daohrq = to_date('"+this.getRiq()+"','yyyy-mm-dd') and daohrq>=sysdate-3  and  diancxxb_id = "+visit.getDiancxxb_id()+" and fahb_id = 1  "+yunsfs+"\n" + 
			"group by diancxxb_id,gongysmc, meikdwmc,faz,daoz,pinz,jihkj,fahrq,daohrq,caiybh,chec\n" + 
			"union\n" + 
			"select -1 diancxxb_id,'合计' as gongysb_id,'' as meikxxb_id,'' as faz_id,'' as daoz_id,'' as pinzb_id,'' as jihkjb_id,to_date('','yyyy-mm-dd') fahrq,to_date('','yyyy-mm-dd') daohrq,'' caiybh,'' chec,0 as ID,\n" + 
			"hj.ches as ches,hj.maoz as maoz,hj.piz as piz,hj.koud as koud,hj.biaoz as biaoz,hj.jingz as  jingz\n" + 
			"from\n" + 
			"(select diancxxb_id,count(id) as ches,sum(maoz) as maoz,sum(piz) as piz,sum(koud) as koud,sum(biaoz) as biaoz,sum(maoz - piz - koud - kous - kouz) jingz\n" + 
			"from chepbtmp where daohrq = to_date('"+this.getRiq()+"','yyyy-mm-dd')  and daohrq>=sysdate-3 and  diancxxb_id = "+visit.getDiancxxb_id()+" and fahb_id = 1  "+yunsfs+" group by diancxxb_id) hj";


		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl,customKey);
		egu.setGridType(ExtGridUtil.Gridstyle_Read);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.addPaging(25);
		egu.setTableName("chepbtmp");
		egu.getColumn("diancxxb_id").setHeader("电厂");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("faz_id").setHeader("发站");
		egu.getColumn("daoz_id").setHeader("到站");
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("daohrq").setEditor(null);
		egu.getColumn("caiybh").setHeader("采样编号");
		egu.getColumn("chec").setHeader("车次");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setEditor(null);
		egu.getColumn("maoz").setHeader("毛重");
		egu.getColumn("maoz").setEditor(null);
		egu.getColumn("piz").setHeader("皮重");
		egu.getColumn("piz").setEditor(null);
		egu.getColumn("koud").setHeader("扣吨");
		egu.getColumn("koud").setEditor(null);
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(80);
		//egu.getColumn("jingz").setHidden(true);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("ID").setHeader("分组ID");
		egu.getColumn("ID").setHidden(true);
		
	
		
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
			+"	 Ext.MessageBox.alert('提示信息','请先单击要删除的煤矿所在行');"
			+"	 return;"
			+"}"
			+"var grid_rcd = gridDiv_sm.getSelected();"
		
			+"Ext.MessageBox.confirm('提示信息','您确定要删除&nbsp;煤矿:&nbsp;'+ grid_rcd.get('MEIKXXB_ID')+',&nbsp;&nbsp;&nbsp;'+grid_rcd.get('DAOHRQ')+'日已经生成的采样编码,重新生成吗?',function(btn){"
			+"	 if(btn == 'yes'){"
			+"		    grid_history = grid_rcd.get('GONGYSB_ID')+','+grid_rcd.get('MEIKXXB_ID');"
			+"			var Cobj = document.getElementById('CHANGE');"
			+"			Cobj.value = grid_history;"
			+"			document.getElementById('DeleteButton').click();"
			+"	       	}"
			+"	  })"
			+"}";
		GridButton delte = new GridButton("删除",sDeleteHandler);
		delte.setIcon(SysConstant.Btn_Icon_Delete);
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
		list.add(new IDropDownBean(2, "火车"));
		
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
