package com.zhiren.dc.huaygl.huaybb.huayjg;

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Huayjg extends BasePage implements PageValidateListener {
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
	public void setChepid(String fahids) {
		((Visit)this.getPage().getVisit()).setString1(fahids);
	}
//	绑定日期
	public String getBeginRiq() {
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setBeginRiq(String riq) {
		((Visit) getPage().getVisit()).setString3(riq);
	}
	

	public String getEndRiq() {
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setEndRiq(String riq) {
		((Visit) getPage().getVisit()).setString2(riq);
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
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
			setMsg("请选择一行数据进行查看！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "ShujshQ.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		String zhiyh="";
		while(rsl.next()) {
			zhiyh+="'"+rsl.getString("zhiybh")+"',";
			
		}
		String aa=zhiyh.substring(0,zhiyh.length()-1);
		setChepid(aa);
		cycle.activate("Huayjg_mx");
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
	}
	

	private String filterDcid(Visit v){
		
		String sqltmp = " ("+ v.getDiancxxb_id()+")";
		if(v.isFencb()){
			JDBCcon con = new JDBCcon();
			ResultSetList rsl = con.getResultSetList("select id from diancxxb where fuid="+v.getDiancxxb_id());
			sqltmp = "";
			while(rsl.next()) {
				sqltmp += ","+rsl.getString("id");
			}
			sqltmp ="("+ sqltmp.substring(1) + ") ";
			rsl.close();
			con.Close();
		}
		return sqltmp;
	}
	
	private void setDiancxxb_id(String id){
		
		((Visit)this.getPage().getVisit()).setString13(id);
		
	}
	
	

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer();
		
	
	

		
				sb.append("select l.id,TO_CHAR(l.huaysj, 'yyyy-mm-dd') AS huaysj,\n");
				sb.append("       TO_CHAR(z.zhiybm) AS zhiybh,\n");
				sb.append("       TO_CHAR(h.huaybm) AS huaybh,\n");
				sb.append("       l.mt,\n");
				sb.append("       l.mad,\n");
				sb.append("       l.vdaf,\n");
				sb.append("       l.aad,\n");
				sb.append("       l.stad,\n");
				sb.append("       l.qbad,\n");
				sb.append("       l.qgrad,\n");
				sb.append("       l.qnet_ar,\n");
				sb.append("       huayy\n");
				sb.append("  from zhillsb l,\n");
				sb.append("       (select bianm as zhiybm, zhillsb_id\n");
				sb.append("          from zhuanmb\n");
				sb.append("         where zhillsb_id in\n");
				sb.append("               (select zm.zhillsb_id as id\n");
				sb.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sb.append("                 where zm.zhuanmlb_id = lb.id\n");
				sb.append("                   and lb.jib = 2\n");
				sb.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sb.append("                   and f.zhilb_id = z.zhilb_id\n");
				sb.append("                   and z.id = zm.zhillsb_id)\n");
				sb.append("           and zhuanmlb_id =\n");
				sb.append("               (select id from zhuanmlb where mingc = '制样编码')) z,\n");
				sb.append("       (select bianm as huaybm, zhillsb_id\n");
				sb.append("          from zhuanmb\n");
				sb.append("         where zhillsb_id in\n");
				sb.append("               (select zm.zhillsb_id as id\n");
				sb.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sb.append("                 where zm.zhuanmlb_id = lb.id\n");
				sb.append("                   and lb.jib = 3\n");
				sb.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sb.append("                   and f.zhilb_id = z.zhilb_id\n");
				sb.append("                   and z.id = zm.zhillsb_id)\n");
				sb.append("           and zhuanmlb_id =\n");
				sb.append("               (select id from zhuanmlb where mingc = '化验编码')) h\n");
				sb.append(" where l.id = z.zhillsb_id\n");
				sb.append("   and l.id = h.zhillsb_id\n");
				sb.append("   and l.huaysj >= "+DateUtil.FormatOracleDate(getBeginRiq())+"\n");
				sb.append("   and l.huaysj <= "+DateUtil.FormatOracleDate(getEndRiq())+"   order by l.huaysj,zhiybh");

		
		

		ResultSetList rsl = con.getResultSetList(sb.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
//		设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置每页显示行数
		egu.addPaging(500);
		
		
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").editor=null;
		egu.getColumn("huaysj").setHeader("化验日期");
		egu.getColumn("huaysj").setWidth(75);
		egu.getColumn("zhiybh").setEditor(null);
		egu.getColumn("zhiybh").setHeader("制样编码");
		egu.getColumn("zhiybh").setWidth(75);
		egu.getColumn("huaybh").setHeader("化验编码");
		egu.getColumn("huaybh").setWidth(75);
		egu.getColumn("huaybh").setEditor(null);
		
		egu.getColumn("mt").setHeader("Mt");
		egu.getColumn("mt").setWidth(45);
		
		egu.getColumn("mad").setHeader("Mad");
		egu.getColumn("mad").setWidth(45);
		
		egu.getColumn("vdaf").setHeader("Vdaf");
		egu.getColumn("vdaf").setWidth(45);
		
		egu.getColumn("aad").setHeader("Aad");
		egu.getColumn("aad").setWidth(45);
		
		egu.getColumn("stad").setHeader("Stad");
		egu.getColumn("stad").setWidth(45);
		
		egu.getColumn("qbad").setHeader("Qb,ad");
		egu.getColumn("qbad").setWidth(55);
		
		egu.getColumn("qgrad").setHeader("Qg,rad");
		egu.getColumn("qgrad").setWidth(55);
		
		egu.getColumn("qnet_ar").setHeader("Qnet,ar");
		egu.getColumn("qnet_ar").setWidth(55);
		
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setWidth(100);
		
		
		
		egu.addTbarText("制样编码:");
		TextField tf=new TextField();
		tf.setWidth(100);
		tf.setId("TIAOJ");
		egu.addToolbarItem(tf.getScript());
		egu.addTbarText("-");
		
		String Gongysstr="function (){"+
   		" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
		"url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Huayjg_son&tiaoj='+TIAOJ.getValue();\n"+
   	    "var rewin =  window.showModalDialog(url,'newWin','dialogWidth=600px;dialogHeight=300px;status:no;scroll:yes;');\n" +
  
   	   "if   (rewin != null){gridDiv_sm.getSelected().set('GONGHDWBM',rewin.bianh);}" +
   	    "" +
   	    "}";
	
		egu.addToolbarItem("{"+new GridButton("查询",""+Gongysstr+"").getScript()+"}");
		egu.addTbarText("-");
		
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// 与html页中的id绑定,并自动刷新
		df.setId("BeginRq");
		egu.addTbarText("化验日期：");
		egu.addToolbarItem(df.getScript());
		
		DateField dfe = new DateField();
		dfe.setValue(getEndRiq());
		dfe.Binding("EndRq", "");// 与html页中的id绑定,并自动刷新
		dfe.setId("EndRq");
		egu.addTbarText("至");
		egu.addToolbarItem(dfe.getScript());
		

	
		
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefurbishButton').click();}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
	egu.addToolbarButton("打印",GridButton.ButtonType_Sel, "ShowButton", null, SysConstant.Btn_Icon_Print);
		
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
			if(!visit.getActivePageName().toString().equals("Qicjjd")){
				setBeginRiq(DateUtil.FormatDate(new Date()));
				setEndRiq(DateUtil.FormatDate(new Date()));
				init();
				String dianclb= cycle.getRequestContext().getParameter("lx");
				if(dianclb!=null){
					visit.setString15(dianclb);
				}else{
					visit.setString15("PRINT_MOR");
				}
			}
			getSelectData();
			visit.setActivePageName(getPageName().toString());
		}
	} 
	
	private void init() {
		setExtGrid(null);
		setChepid(null);
		getSelectData();
	}
}