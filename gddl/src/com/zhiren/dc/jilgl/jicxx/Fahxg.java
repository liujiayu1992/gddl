package com.zhiren.dc.jilgl.jicxx;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.Locale;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.dc.jilgl.Jilcz;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.SysConstant;

/*
 * 作者：张乐
 * 时间：2010-05-13
 * 描述：增加日期、厂别的过滤条件，增加汇总行
*	
*/
/*
 * 作者：张乐
 * 时间：2009-11-20
 * 描述：通过参数，可对毛重，皮重进行修改，净重和票重随毛皮修改而更新
 * 		insert into xitxxb values (
			getnewid(diancxxb_id),                  --电厂信息表ID
			1,
			diancxxb_id,                            --电厂信息表ID
			'发货修改可编辑毛皮','是','','数量',1,'使用'
		)
*	
*/
/*
 * 作者：夏峥
 * 时间：2014-02-27
 * 描述：增加车数字段。
 */
public class Fahxg extends BasePage implements PageValidateListener {
	
	public static final String YUNSFS_QY = "QY";//汽运
	public static final String YUNSFS_HY = "HY";//火运
	public static final String YUNSFS_All = "ALL";//全部
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
	}
	
	public String getRiq(){
		return ((Visit) this.getPage().getVisit()).getString5();
	}
	public void setRiq(String rq){
		((Visit) this.getPage().getVisit()).setString5(rq);
	}
	
	public String getYunsfs(){
		return ((Visit)this.getPage().getVisit()).getString1();
	}
	public void setYunsfs(String yunsfs){
		((Visit)this.getPage().getVisit()).setString1(yunsfs);
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	设置电厂树_开始
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if(treeid==null||treeid.equals("")){
			((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id()));
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
//	设置电厂树_结束
	
	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Fahxg.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		List fhlist = new ArrayList();
		while(rsl.next()) {
			Jilcz.addFahid(fhlist, rsl.getString("id"));
			MainGlobal.LogOperation(con,visit.getDiancxxb_id(),visit.getRenymc(),
					SysConstant.RizOpType_UP,SysConstant.RizOpMokm_Fahxg,
					"fahb",rsl.getString("id"));
		}
		rsl.close();
		
		//如果删除了发货，删除发货对应的chep
		rsl = getExtGrid().getDeleteResultSet(getChange());
		while(rsl.next()) {
			con.getDelete("delete from chepb where fahb_id=" +rsl.getString("id"));
		}
		
		Chengbjs.CountChengb(visit.getDiancxxb_id(), fhlist);
		con.Close();
		getExtGrid().Save(getChange(), visit);
		setMsg("保存成功");
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		getSelectData();
	}

	public void getSelectData() {
		
		String sql_yunsfs = "";
		if(YUNSFS_HY.equals(getYunsfs())) {
			sql_yunsfs = " and f.yunsfsb_id = "+SysConstant.YUNSFS_HUOY;
		}else
			if(YUNSFS_QY.equals(getYunsfs())) {
				sql_yunsfs = " and f.yunsfsb_id = "+SysConstant.YUNSFS_QIY;
			}
		JDBCcon con = new JDBCcon(); 
		Visit visit = ((Visit) getPage().getVisit());
		StringBuffer sb = new StringBuffer();
		sb.append("select * from (select  distinct f.id,d.mingc as diancxxb_id,g.mingc as gongysb_id, \n");
		sb.append(" m.mingc as meikxxb_id,(select mingc from chezxxb c where c.id=f.faz_id) as faz_id, \n");
		sb.append(" j.mingc as jihkjb_id, y.mingc as yunsfsb_id, p.mingc as pinzb_id, daohrq, fahrq, chec, \n");
		sb.append(" decode(h.hetbh, null, '', h.hetbh) as hetb_id, \n");
		sb.append(" (select mingc from vwyuanshdw d where d.id=f.yuanshdwb_id) as yuanshdwb_id, \n");
		sb.append(" (select mingc from chezxxb c where c.id=f.daoz_id) as daoz_id, \n");
		sb.append(" (select mingc from chezxxb c where c.id=f.yuandz_id) as yuandz_id, \n");
		sb.append("  maoz,piz,jingz,biaoz,yuns,ches \n");
		sb.append(" from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,  \n");
		sb.append(" jihkjb j,hetb h,yunsfsb y where f.gongysb_id=g.id "+Jilcz.filterDcid(visit, "f")+" \n");
		sb.append(" and f.diancxxb_id=d.id  \n");
		sb.append(" and f.meikxxb_id=m.id and f.pinzb_id=p.id  \n");
		sb.append(" and f.jihkjb_id=j.id and f.yunsfsb_id=y.id  \n");
		sb.append(" and f.hedbz <"+SysConstant.HEDBZ_YSH+" \n");
		sb.append(sql_yunsfs+" \n");
		if(visit.isFencb()){
			sb.append("and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n");
		}
		sb.append(" and f.daohrq = to_date('"+getRiq()+"','yyyy-mm-dd')");
		sb.append(" and f.hetb_id=h.id(+)\n");
		sb.append("union \n");
		sb.append(" select  -1 as id,'合计' as diancxxb_id,'' as gongysb_id,  \n");
		sb.append(" '' as meikxxb_id,'' as faz_id,  \n");
		sb.append(" '' as jihkjb_id, '' as yunsfsb_id,'' as pinzb_id, to_date('','yyyy-mm-dd') as daohrq, to_date('','yyyy-mm-dd') as fahrq, '' chec,  \n");
		sb.append("'' as hetb_id,  \n");
		sb.append(" '' as yuanshdwb_id, '' as daoz_id, '' as yuandz_id,  \n");
		sb.append(" sum(maoz) as maoz,sum(piz) as  piz,sum(jingz) as jingz,sum(biaoz),0 yuns,0 ches  \n");
		sb.append(" from fahb f,diancxxb d,gongysb g,meikxxb m,pinzb p,  \n");
		sb.append(" jihkjb j,hetb h,yunsfsb y where f.gongysb_id=g.id "+Jilcz.filterDcid(visit, "f")+" \n");
		sb.append(" and f.diancxxb_id=d.id  \n");
		sb.append(" and f.meikxxb_id=m.id and f.pinzb_id=p.id  \n");
		sb.append(" and f.jihkjb_id=j.id and f.yunsfsb_id=y.id  \n");
		sb.append(" and f.hedbz <"+SysConstant.HEDBZ_YSH+" \n");
		sb.append(sql_yunsfs+" \n");
		if(visit.isFencb()){
			sb.append("and f.diancxxb_id in(select id from diancxxb where id=" + getTreeid() + " or fuid=" + getTreeid() + ")\n");
		}
		sb.append(" and f.daohrq = to_date('"+getRiq()+"','yyyy-mm-dd')");
		sb.append(" and f.hetb_id=h.id(+)) order by id desc");
		
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		ComboBox dc= new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(dc);
		egu.getColumn("diancxxb_id").returnId = true;
		dc.setEditable(true);
		String dcSql;
		if(visit.isFencb()) {
			dcSql="select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setHeader(Locale.diancxxb_id_fahb);
			egu.getColumn("diancxxb_id").setWidth(70);
			
		}else {
			dcSql="select id,mingc from diancxxb where id="+visit.getDiancxxb_id();
			egu.getColumn("diancxxb_id").setHidden(true);
			egu.getColumn("diancxxb_id").editor = null;
		}
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel(dcSql));
		
		egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
		egu.getColumn("gongysb_id").setWidth(80);
		egu.getColumn("meikxxb_id").setHeader(Locale.meikxxb_id_fahb);
		egu.getColumn("meikxxb_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
		egu.getColumn("pinzb_id").setWidth(45);
		egu.getColumn("maoz").setHeader(Locale.maoz_fahb);
		egu.getColumn("maoz").setWidth(55);
		
		egu.getColumn("maoz").setDefaultValue("0");
		egu.getColumn("piz").setHeader(Locale.piz_fahb);
		egu.getColumn("piz").setWidth(55);
		egu.getColumn("piz").setDefaultValue("0");
		egu.getColumn("jingz").setHeader(Locale.jingz_fahb);
		egu.getColumn("jingz").setWidth(55);
		egu.getColumn("jingz").setHidden(true);
		egu.getColumn("jingz").setEditor(null);
		egu.getColumn("jingz").setDefaultValue("0");
		egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
		egu.getColumn("biaoz").setWidth(55);
		egu.getColumn("biaoz").setEditor(null);
		egu.getColumn("biaoz").setDefaultValue("0");
		egu.getColumn("yuns").setHeader(Locale.yuns_fahb);
		egu.getColumn("yuns").setWidth(45);
		egu.getColumn("yuns").setEditor(null);
		egu.getColumn("yuns").setDefaultValue("0");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setWidth(45);
		
		egu.getColumn("faz_id").setHeader(Locale.faz_id_fahb);
		egu.getColumn("faz_id").setWidth(50);
		egu.getColumn("daoz_id").setHeader(Locale.daoz_id_fahb);
		egu.getColumn("daoz_id").setWidth(50);
		egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("hetb_id").setHeader(Locale.hetb_id_fahb);
		egu.getColumn("hetb_id").setWidth(80);
		
		egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
		egu.getColumn("yunsfsb_id").setWidth(40);
		egu.getColumn("yuandz_id").setHeader(Locale.yuandz_id_fahb);
		egu.getColumn("yuandz_id").setWidth(50);
		egu.getColumn("yuanshdwb_id").setHeader(Locale.yuanshdwb_id_fahb);
		egu.getColumn("yuanshdwb_id").setWidth(80);
		egu.getColumn("daohrq").setHeader(Locale.daohrq_id_fahb);
		egu.getColumn("daohrq").setWidth(80);
		egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
		egu.getColumn("fahrq").setWidth(80);
		
		//石嘴山电厂的个性设置
		String sql="select zhi from xitxxb where mingc='发货修改显示车次字段' and zhuangt=1 and leib='数量' and beiz='列名显示为化验编码' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList sqlrs = con.getResultSetList(sql);
		egu.getColumn("chec").setHidden(true);
		egu.getColumn("chec").setEditor(null);
		if(sqlrs.next()){
			if("是".equals(sqlrs.getString("zhi"))){
				egu.getColumn("chec").setHeader("化验编码");
				egu.getColumn("chec").setHidden(false);
				egu.getColumn("chec").setWidth(80);
			}
		}
		
		
		String sQl="select zhi from xitxxb where mingc='发货修改可编辑毛皮' and zhuangt=1 and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rs = con.getResultSetList(sQl);
		if(rs.next()){
			egu.addOtherScript(
					"gridDiv_grid.on('afteredit',\n" +
					"function(e){\n" + 
					"  if(e.field=='MAOZ' || e.field=='PIZ'){\n" + 
					"    biaoz = eval(e.record.get('MAOZ')||0)-eval(e.record.get('PIZ')||0);\n" + 
					"    if(biaoz>0){\n" + 
					"      var xz = eval(e.value||0) - eval(e.originalValue||0);\n" + 
					"      e.record.set('BIAOZ',Math.round(biaoz*100)/100);\n" + 
					"      e.record.set('JINGZ',Math.round(biaoz*100)/100);\n" + 
					"      for (i=e.row+1;i<gridDiv_ds.getCount();i++){\n" + 
					"        var rec = gridDiv_ds.getAt(i);\n" + 
					"        if(rec.get('DIANCXXB_ID')=='合计') {\n" + 
					"            var tValue = eval(rec.get(e.field)||0);\n" + 
					"            var tBiaoz = eval(rec.get('BIAOZ')||0);\n" + 
					"            gridDiv_ds.getAt(i).set(e.field,Math.round((tValue+xz)*100)/100);\n" + 
					"\n" + 
					"            if (e.field=='MAOZ') {\n" + 
					"            gridDiv_ds.getAt(i).set('BIAOZ',Math.round((tBiaoz +xz)*100)/100);\n" + 
					"            gridDiv_ds.getAt(i).set('JINGZ',Math.round((tBiaoz +xz)*100)/100);\n" + 
					"            } else {\n" + 
					"              gridDiv_ds.getAt(i).set('BIAOZ',Math.round((tBiaoz -xz)*100)/100);\n" + 
					"              gridDiv_ds.getAt(i).set('JINGZ',Math.round((tBiaoz -xz)*100)/100);\n" + 
					"            }\n" + 
					"        }\n" + 
					"      }\n" + 
					"\n" + 
					"    }else{\n" + 
					"      Ext.MessageBox.alert('提示信息','毛重的值必须比皮重大');return;\n" + 
					"    }\n" + 
					"  }\n" + 
					"});"
				);
		}else{
			egu.getColumn("maoz").setEditor(null);
			egu.getColumn("piz").setEditor(null);
		}
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setWidth(Locale.Grid_DefaultWidth);
		
//		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
//		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, mingc from diancxxb"));
//		egu.getColumn("diancxxb_id").setDefaultValue(((Visit) getPage().getVisit()).getDiancmc());
		egu.getColumn("gongysb_id").setEditor(new ComboBox());
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from gongysb where leix=1"));
		egu.getColumn("meikxxb_id").setEditor(new ComboBox());
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from meikxxb"));
		egu.getColumn("pinzb_id").setEditor(new ComboBox());
		egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from pinzb"));
		egu.getColumn("faz_id").setEditor(new ComboBox());
		egu.getColumn("faz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("daoz_id").setEditor(new ComboBox());
		egu.getColumn("daoz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("jihkjb_id").setEditor(new ComboBox());
		egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from jihkjb"));
		egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
		egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from yunsfsb"));
		egu.getColumn("yuandz_id").setEditor(new ComboBox());
		egu.getColumn("yuandz_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("yuanshdwb_id").setEditor(new ComboBox());
		egu.getColumn("yuanshdwb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from vwyuanshdw"));
		egu.getColumn("hetb_id").setEditor(new ComboBox());
//		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
//				new IDropDownModel("select id, hetbh from hetb"));
		egu.getColumn("hetb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select 0 id, '' hetbh from dual union select id, hetbh from hetb"));
		egu.getColumn("hetb_id").setDefaultValue("");
		egu.getColumn("hetb_id").editor.setAllowBlank(true);
		
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setValue(this.getRiq());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("电厂：");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, 
			((Visit)this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");
		
		GridButton refurbish = new GridButton("刷新","function (){document.getElementById('RefreshButton').click();"+MainGlobal.getExtMessageShow("请等待","处理中",200)+"}");
		refurbish.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(refurbish);
//		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
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
		if(getExtGrid() == null) {
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
		String reportType = cycle.getRequestContext().getParameter("lx");
		if(reportType != null) {
			setYunsfs(reportType);
		}
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			if(reportType == null) {
				setYunsfs(YUNSFS_All);
			}
			setRiq(DateUtil.FormatDate(new Date()));
			setTreeid(visit.getDiancxxb_id()+"");
			visit.setList1(null);
			getSelectData();
		}
	}
	
}
