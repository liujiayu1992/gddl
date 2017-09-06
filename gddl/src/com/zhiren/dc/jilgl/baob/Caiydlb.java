package com.zhiren.dc.jilgl.baob;

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

public class Caiydlb extends BasePage implements PageValidateListener {
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
		String caiydh="";
		while(rsl.next()) {
			caiydh+="'"+rsl.getString("bianm")+"',";
			
			//this.setDiancxxb_id(rsl.getString("diancxxb_id"));
		}
		String aa=caiydh.substring(0,caiydh.length()-1);
		setChepid(aa);
		//System.out.println(aa);
		cycle.activate("Caiydlb_cx");
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
				
				
				
			/*	sb.append("select distinct\n");
				sb.append("       c.caiybm as bianm,\n");
				sb.append("       (select mingc from meikxxb where id = s.meikxxb_id) as kuangb,\n");
				sb.append("         (select mingc from chezxxb where id = (select faz_id from fahb where id = s.id)) as faz,\n");
				sb.append("         cy.caiyrq as zhiyrq,  (select s.huaylb from zhillsb s where id = s.zid) as huaylb,\n");
				sb.append("         (select h.hetbh from hetb h where h.id=s.hetb_id) as heth,\n");
				sb.append("         (select max(to_char(c.zhongcsj,'yyyy-hh-dd hh24:mi:ss')) from chepb c where c.fahb_id=s.id) as zhongcsj,\n");
				sb.append("        (select to_char(fahrq,'yyyy-mm-dd') from fahb where id = s.id) as fahrq,\n");
				sb.append("        (select ches from fahb where id = s.id) as ches,\n");
				sb.append("       GETHUAYBBCHEPS(s.zhilb_id) as cheh\n");
				sb.append("  from (select bianm as caiybm, zhillsb_id\n");
				sb.append("          from zhuanmb\n");
				sb.append("         where zhillsb_id in\n");
				sb.append("               (select zm.zhillsb_id as id\n");
				sb.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sb.append("                 where zm.zhuanmlb_id = lb.id\n");
				sb.append("                   and lb.jib = 1\n");
				sb.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sb.append("                   and f.zhilb_id = z.zhilb_id\n");
				sb.append("                   and z.id = zm.zhillsb_id)\n");
				sb.append("           and zhuanmlb_id =\n");
				sb.append("               (select id from zhuanmlb where mingc = '采样编码')) c,\n");
				sb.append("       (select distinct f.id,f.pinzb_id,\n");
				sb.append("                        f.diancxxb_id,\n");
				sb.append("                        f.gongysb_id,\n");
				sb.append("                        f.meikxxb_id,\n");
				sb.append("                        f.hetb_id,\n");
				sb.append("                        z.id as zid,\n");
				sb.append("                        f.zhilb_id\n");
				sb.append("          from zhillsb z, fahb f, chepb c,pinzb p\n");
				sb.append("         where f.zhilb_id = z.zhilb_id\n");
				sb.append("            and f.pinzb_id=p.id\n");
				sb.append("           and c.fahb_id = f.id\n");
				sb.append("             ) s,caiyb cy\n");
				sb.append(" where\n");
				sb.append("    c.zhillsb_id = s.zid\n");
				//sb.append(" and  s.diancxxb_id = 259\n");
				sb.append(" and s.zhilb_id=cy.zhilb_id\n");
				sb.append(" and cy.caiyrq BETWEEN \n");
				sb.append(DateUtil.FormatOracleDate(getBeginRiq()));
				sb.append(" AND  ");
				sb.append(DateUtil.FormatOracleDate(getEndRiq()));
				sb.append("+1 \n ");
				sb.append("  order by kuangb,fahrq,faz,ches,caiybm");
*/

		
				sb.append("select distinct\n");
				sb.append("       c.caiybm as bianm,\n");
				sb.append("       (select mingc from meikxxb where id = s.meikxxb_id) as kuangb,\n");
				sb.append("         (select mingc from chezxxb where id = s.faz_id) as faz,\n");
				sb.append("         cy.caiyrq as zhiyrq,  (select s.huaylb from zhillsb s where id = s.zid) as huaylb,\n");
				sb.append("         (select h.hetbh from hetb h where h.id=s.hetb_id) as heth,\n");
				sb.append("        to_char(s.zhongcsj,'yyyy-hh-dd hh24:mi:ss') as zhongcsj ,s.fahrq,s.ches,\n");
				sb.append("       GETHUAYBBCHEPS(s.zhilb_id) as cheh\n");
				sb.append("  from (select bianm as caiybm, zhillsb_id\n");
				sb.append("          from zhuanmb\n");
				sb.append("         where zhillsb_id in\n");
				sb.append("               (select zm.zhillsb_id as id\n");
				sb.append("                  from zhuanmb zm, zhuanmlb lb, yangpdhb y, zhillsb z, fahb f\n");
				sb.append("                 where zm.zhuanmlb_id = lb.id\n");
				sb.append("                   and lb.jib = 1\n");
				sb.append("                   and y.zhilblsb_id = zm.zhillsb_id\n");
				sb.append("                   and f.zhilb_id = z.zhilb_id\n");
				sb.append("                   and z.id = zm.zhillsb_id)\n");
				sb.append("           and zhuanmlb_id =\n");
				sb.append("               (select id from zhuanmlb where mingc = '采样编码')) c,\n");
				sb.append("             (select ls.id as zid,ls.zhilb_id,ww.gongys_id,ww.meikxxb_id,ww.hetb_id,ww.pinzb_id,ww.ches,ww.faz_id,\n");
				sb.append("ww.fahrq,ww.zhongcsj\n");
				sb.append("from\n");
				sb.append("(select f.zhilb_id,max(f.gongysb_id) as gongys_id,max(f.meikxxb_id) as meikxxb_id,max(f.hetb_id) as hetb_id,\n");
				sb.append("max(f.pinzb_id) as pinzb_id,count(c.id) as ches,max(f.faz_id) as faz_id,min(f.fahrq) as fahrq,min(c.zhongcsj) as zhongcsj\n");
				sb.append("from fahb f,chepb c\n");
				sb.append("where f.id=c.fahb_id\n");
				sb.append(" group by (f.zhilb_id)) ww,zhillsb ls\n");
				sb.append(" where ww.zhilb_id=ls.zhilb_id) s, caiyb cy\n");
				sb.append(" where c.zhillsb_id = s.zid\n");
				sb.append(" and s.zhilb_id=cy.zhilb_id\n");
				sb.append(" and cy.caiyrq BETWEEN \n");
				sb.append(DateUtil.FormatOracleDate(getBeginRiq()));
				sb.append(" AND  ");
				sb.append(DateUtil.FormatOracleDate(getEndRiq()));
				sb.append("+1 \n ");
				sb.append("   order by kuangb,fahrq,faz,ches,caiybm");


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
		egu.addPaging(100);
		
		
		
		egu.getColumn("bianm").setHeader("制样单号");
		egu.getColumn("bianm").setWidth(70);
		
		/*egu.getColumn("gonghdw").setHeader("供货单位");
		egu.getColumn("gonghdw").setWidth(90);
		egu.getColumn("gonghdw").hidden=true;*/
		
		egu.getColumn("kuangb").setHeader("煤矿单位");
		egu.getColumn("kuangb").setWidth(110);
		
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("faz").setWidth(70);
	
		egu.getColumn("zhiyrq").setHeader("制样日期");
		egu.getColumn("zhiyrq").setWidth(80);
		
		egu.getColumn("huaylb").setHeader("化验类别");
		egu.getColumn("huaylb").setWidth(80);
		
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("fahrq").setWidth(80);
		
		egu.getColumn("heth").setHeader("合同号");
		egu.getColumn("heth").setWidth(80);
		
		egu.getColumn("zhongcsj").setHeader("过衡时间");
		egu.getColumn("zhongcsj").setWidth(120);
		
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(50);
		
		egu.getColumn("cheh").setHeader("车号");
		egu.getColumn("cheh").setWidth(350);
		
		
	
		
		
		
		DateField df = new DateField();
		df.setValue(getBeginRiq());
		df.Binding("BeginRq", "");// 与html页中的id绑定,并自动刷新
		df.setId("BeginRq");
		egu.addTbarText("采样日期：");
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
		
//		egu.addOtherScript(" gridDiv_grid.addListener('cellclick',function(grid, rowIndex, columnIndex, e){});");
		
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