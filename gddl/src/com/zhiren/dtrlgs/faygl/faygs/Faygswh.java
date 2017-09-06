package com.zhiren.dtrlgs.faygl.faygs;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.chengbgl.Chengbjs;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Faygswh extends BasePage implements PageValidateListener{
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
		this.setMsg("");
  }
//	绑定日期字段
	public String getRiq() {
		if (((Visit) this.getPage().getVisit()).getString1() == null || ((Visit) this.getPage().getVisit()).getString1().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString1(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRiq(String riq) {

		if (((Visit) this.getPage().getVisit()).getString1() != null && !((Visit) this.getPage().getVisit()).getString1().equals(riq)) {
			
			((Visit) this.getPage().getVisit()).setString1(riq);
		}
	}
	
/*	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString3() == null || ((Visit) this.getPage().getVisit()).getString3().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString3(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString3();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString3() != null && !((Visit) this.getPage().getVisit()).getString3().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString3(riq2);
		}
	}
	*/
	
//	 页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}
	public void setExtGrid(ExtGridUtil extgrid) {
		((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
	}
	public ExtGridUtil getExtGrid() {
		return ((Visit) this.getPage().getVisit()).getExtGrid1();
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
	/*public ExtTreeUtil getTree() {
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
	
	private String treeid = "";
	public String getTreeid() {
		if (treeid.equals("")) {

			treeid = String.valueOf(((Visit) getPage().getVisit())
					.getDiancxxb_id());
		}
		return treeid;
	}
	public void setTreeid(String treeid) {
		if (!this.treeid.equals(treeid)) {

			((Visit) getPage().getVisit()).setboolean3(true);
			this.treeid = treeid;
		}
	}
	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		// System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="
				+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());

		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			con.Close();
		}
		return jib;
	}*/
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}	
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
    private boolean _ChongxjszgButtonClick=false;
    public void ChongxjszgButton(IRequestCycle  cycle){
    	_ChongxjszgButtonClick=true;
    }
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getRiq();
			getSelectData();
		} else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}else if(_ChongxjszgButtonClick){
			_ChongxjszgButtonClick=false;
			Chongxjszg();
			getSelectData();
		}
	}
	public void save() {
		String sSql = "";
		int flag = 0;
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) getPage().getVisit();
		if (getChange() == null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
			return;
		}
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Faygswh.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		List fay_list=new ArrayList();
		
		while (rsl.next()) {
			if(!rsl.getString("heth").equals(rsl.getString("hethy"))){
				sSql="update fayslb set hetb_id=(select id from hetb where hetbh='"+rsl.getString("heth")+"')  where id="+rsl.getLong("fayslb_id");
				fay_list.add(new FaycbBean(this.getDiancxxb_idFromfayslb(rsl.getLong("fayslb_id")) ,rsl.getLong("fayslb_id")));
				
			}else{
				sSql="insert into faygslsb(id, fayslb_id, rez,leix, heth,hetjg, hetbz, hetzk, meij, meis,\n" + 
				"riq, yunf, zaf, fazzf, yunfs) values( getnewid(\n" +visit.getDiancxxb_id()+"),"+rsl.getString("FAYSLB_ID")+
				","+rsl.getString("REZ")+",3,'"+rsl.getString("HETH")+"',"+rsl.getString("HETJG")+
				",'"+rsl.getString("HETBZ")+"',"+rsl.getString("HETZK")+","+rsl.getString("MEIJ")+
				","+rsl.getString("MEIS")+", to_date('"+rsl.getString("RIQ")+"','yyyy-mm-dd')" +
				"," +rsl.getString("YUNF")+","+rsl.getString("ZAF")+","+rsl.getString("FAZZF")+","+rsl.getString("YUNFS")+") \n"; 
			}
			flag = con.getUpdate(sSql);
				if (flag == -1) {
					con.rollBack();
				}else{
					FayzgInfo.CountChengb(fay_list,true);
				}
				con.Close();
		}
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String str = "";
//		if (visit.isJTUser()) {
//			str = "";
//		} else {
//			if (visit.isGSUser()) {
//				str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
//						+ getTreeid() + ")";
//			} else {
//				str = "and dc.id = " + getTreeid() + "";
//			}
//		} 
		/*int treejib = this.getDiancTreeJib();
		if (treejib == 1) {// 选集团时刷新出所有的电厂
			str = "";
		} else if (treejib == 2) {// 选分公司的时候刷新出分公司下所有的电厂
			str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
					+ getTreeid() + ")";
		} else if (treejib == 3) {// 选电厂只刷新出该电厂
			str = "and dc.id = " + getTreeid() + "";
		}*/
		String sSql ="select f.id as id, f.fayslb_id as fayslb_id, d.mingc as fahd,g.mingc as gongys,s.mingc as fahdw,f2.chec as chec,lc.mingc as chuanm,f2.meil, \n" +
			" f.rez as rez,f.leix as leix, f.heth as heth,\n" +
			" f.hetjg as  hetjg,f.hetbz as hetbz, f.hetzk as hetzk, f.meij as meij, f.meis as meis,\n" + 
			" f.riq as riq, f.yunf as yunf, f.zaf as zaf,f.fazzf as fazzf, f.yunfs as yunfs\n" + 
			" ,f.heth hethy\n"+
			"  , round_new((f.meij + f.yunf +f.zaf) * f2.meil, 2) as buhsmk\n"+
			"    ,round_new(round_new((f.meij + f.yunf + f.zaf) * f2.meil, 2) * 0.17, 2) as shuik,\n"+
			"    round_new(round_new((f.meij + f.yunf + f.zaf) * f2.meil, 2) * 1.17, 2) as  hansmk\n"+
			" from faygslsb f, fayslb f2,(select max(id) as id from faygslsb where leix<>4 group by (fayslb_id)) f3, diancxxb dc, \n" + 
			" (select id,mingc from diancxxb where cangkb_id<>1) d, (select id,mingc from gongysb) g," +
			" (select id,mingc from diancxxb where cangkb_id=1) s,luncxxb lc\n"+
			
			" where f3.id=f.id and f2.diancxxb_id=dc.id and lc.id(+)=f2.luncxxb_id\n" + 
			" and f2.diancxxb_id=d.id and g.id=f2.gongysb_id and s.id=f2.shr_diancxxb_id \n"+
			" and f2.id=f.fayslb_id \n"+
			" and f.leix<>4 \n"+
//			" and f2.FAHRQ>=to_date('"+getRiq()+"','yyyy-mm-dd') \n"+
			" and f2.FAHRQ<to_date('"+getRiq()+"','yyyy-mm-dd')+1 \n"+		
//			str + " order by f.id desc";
			"order by f.id desc";
				
		ResultSetList rsl = con.getResultSetList(sSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);

		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setHeight("bodyHeight");
		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("id").setEditor(null);
		
		egu.getColumn("fayslb_id").setHeader("发运数量表ID");
		egu.getColumn("fayslb_id").setHidden(true);
		egu.getColumn("fayslb_id").setEditor(null);
		
		egu.getColumn("fahd").setHeader("发货地点");
		egu.getColumn("fahd").setWidth(100);
		egu.getColumn("fahd").setEditor(null);
		
		egu.getColumn("gongys").setHeader("供应商");
		egu.getColumn("gongys").setWidth(100);
		egu.getColumn("gongys").setEditor(null);
		
		egu.getColumn("fahdw").setHeader("收货单位");
		egu.getColumn("fahdw").setWidth(80);
		egu.getColumn("fahdw").setEditor(null);
		
		egu.getColumn("chec").setHeader("车次/船次");
		egu.getColumn("chec").setWidth(80);
		egu.getColumn("chec").setEditor(null);
		
		egu.getColumn("chuanm").setHeader("船名");
		egu.getColumn("chuanm").setWidth(80);
		egu.getColumn("chuanm").setEditor(null);
		
		egu.getColumn("meil").setHeader("煤量");
		egu.getColumn("meil").setWidth(70);
		egu.getColumn("meil").setEditor(null);
		
		egu.getColumn("rez").setHeader("估收热值(大卡)");
		egu.getColumn("rez").setWidth(100);
		egu.getColumn("rez").setEditor(null);
		
		egu.getColumn("leix").setHeader("类型");
		egu.getColumn("leix").setHidden(true);
		egu.getColumn("leix").setEditor(null);
		
		egu.getColumn("heth").setHeader("合同号");
		egu.getColumn("heth").setWidth(130);
		ComboBox het = new ComboBox();
		egu.getColumn("heth").setEditor(het);
		het.setEditable(true);
		String hetSql = "select id,hetbh from hetb where leib<>2 order by hetbh";
		egu.getColumn("heth").setComboEditor(egu.gridId,
				new IDropDownModel(hetSql));
		egu.getColumn("heth").setReturnId(false);

		egu.getColumn("hetjg").setHeader("合同价格");
		egu.getColumn("hetjg").setWidth(70);
		egu.getColumn("hetjg").setEditor(null);
		
		egu.getColumn("hetbz").setHeader("合同标准");
		egu.getColumn("hetbz").setWidth(80);
		egu.getColumn("hetbz").setEditor(null);
		
		egu.getColumn("hetzk").setHeader("合同增扣");
		egu.getColumn("hetzk").setWidth(80);
		egu.getColumn("hetzk").setEditor(null);
		
		egu.getColumn("meij").setHeader("煤价(不含税)");
		egu.getColumn("meij").setWidth(80);
		
		egu.getColumn("meis").setHeader("煤价税");
		egu.getColumn("meis").setWidth(60);
		egu.getColumn("meis").setEditor(null);
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("riq").setEditor(null);
		
		egu.getColumn("yunf").setHeader("运费");
		egu.getColumn("yunf").setWidth(50);
		egu.getColumn("yunf").setEditor(null);
		
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(50);
		egu.getColumn("zaf").setEditor(null);
		
		egu.getColumn("fazzf").setHeader("发站杂费");
		egu.getColumn("fazzf").setWidth(70);
		egu.getColumn("fazzf").setEditor(null);
		
		egu.getColumn("yunfs").setHeader("运费税");
		egu.getColumn("yunfs").setWidth(70);
		egu.getColumn("yunfs").setEditor(null);
		
		egu.getColumn("hethy").setHeader("合同标准");
		egu.getColumn("hethy").setWidth(70);
		egu.getColumn("hethy").setEditor(null);
		
		egu.getColumn("buhsmk").setHeader("不含税煤款");
		egu.getColumn("buhsmk").setWidth(70);
		egu.getColumn("buhsmk").setEditor(null);
		
		egu.getColumn("shuik").setHeader("税款");
		egu.getColumn("shuik").setWidth(70);
		egu.getColumn("shuik").setEditor(null);
		
		egu.getColumn("hansmk").setHeader("含税煤款");
		egu.getColumn("hansmk").setWidth(70);
		egu.getColumn("hansmk").setEditor(null);
//		设定不可编辑列的颜色
		egu.getColumn("fahd").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("gongys").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fahdw").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("chec").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("chuanm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meil").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("rez").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leix").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hetjg").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hetbz").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hetzk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("meis").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("riq").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("zaf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("fazzf").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("yunfs").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.getColumn("buhsmk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("shuik").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("hansmk").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.addTbarText("发货日期:");
		DateField dStart = new DateField();
		dStart.Binding("RIQ","");// 与html页中的id绑定,并自动刷新
		dStart.setValue(this.getRiq());
		egu.addToolbarItem(dStart.getScript());
		/*egu.addTbarText("至:");
		DateField df2 = new DateField();
		df2.setValue(this.getRiq2());
		df2.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df2.getScript());*/
		/*egu.addTbarText("-");
		egu.addTbarText("单位名称:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc,
				((Visit) getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");*/
		egu.addTbarText("-");
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addTbarText("-");
		
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		GridButton Chongxjszggbt = new GridButton("重新计算暂估","function(){Ext.MessageBox.confirm('警告', '确定重新计算暂估吗？', function(btn) { if(btn=='yes'){ "+MainGlobal.getExtMessageShow("提示","请等待...", 200)+";document.getElementById('ChongxjszgButton').click();}})}");
//		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(Chongxjszggbt);
		StringBuffer script = new StringBuffer();
		script.append( "gridDiv_grid.on('beforeedit', function(e) {\n") //判断，与原来的比较。
				.append("	if(e.field=='MEIJ'){\n")
				.append("		if(e.record.get('HETH')!=e.record.get('HETHY')){\n")
				.append("			e.cancel=true;\n")
				.append("		}")
				.append("	}else{e.cancel=false;}\n")
				.append("});");
		egu.addOtherScript(script.toString());
		setExtGrid(egu);
		con.Close();
	}
	public long getDiancxxb_idFromfayslb(long fayslb_id){
		    JDBCcon con=new JDBCcon();
		    ResultSetList rsl=con.getResultSetList("select diancxxb_id  from fayslb where id="+fayslb_id);
		    if(rsl.next()){
		    	return rsl.getLong("diancxxb_id");
		    }
		    return 0;
	}
     public void Chongxjszg(){
    	 JDBCcon con = new JDBCcon();
// 		String strxmrqeOra = DateUtil.FormatOracleDate(this.getRiq());
 		String sSql =
 			"select f.id as fayslb_id,f.diancxxb_id from fayslb f where f.id not in\n" +
 			" ( select gs.fayslb_id from faygslsb gs,fayslb f where gs.leix=4 and gs.fayslb_id=f.id and f.fahrq<to_date('"+getRiq()+"','yyyy-mm-dd'))\n" + 
// 			"and f.fahrq>=to_date('2009-08-20','yyyy-mm-dd') and f.fahrq<to_date('"+getRiq()+"','yyyy-mm-dd')";//2009年08月26日以前的历史数据不做暂估
 			" and f.fahrq<to_date('"+getRiq()+"','yyyy-mm-dd') and f.hetb_id<>0 and "
 			+"(f.yewlxb_id<>3 or (f.yewlxb_id<>3 and f.zhilb_id=0) or (f.id not in (select fayslb_id from faygslsb g,fayslb fy where g.leix=2 and g.fayslb_id=fy.id and fy.yewlxb_id=3)))";

 			
// 			"select f.id as id, f.fayslb_id as fayslb_id, d.mingc as fahd,g.mingc as gongys,s.mingc as fahdw,f2.chec as chec,lc.mingc as chuanm,f2.meil, \n" +
//		" f.rez as rez,f.leix as leix, f.heth as heth,\n" +
//		" f.hetjg as  hetjg,f.hetbz as hetbz, f.hetzk as hetzk, f.meij as meij, f.meis as meis,\n" + 
//		" f.riq as riq, f.yunf as yunf, f.zaf as zaf,f.fazzf as fazzf, f.yunfs as yunfs\n" + 
//		" ,f.heth hethy\n"+
//		" from faygslsb f, fayslb f2,(select max(id) as id from faygslsb where leix<>4 group by (fayslb_id)) f3, diancxxb dc, \n" + 
//		" (select id,mingc from diancxxb where cangkb_id<>1) d, (select id,mingc from gongysb) g," +
//		" (select id,mingc from diancxxb where cangkb_id=1) s,luncxxb lc"+
//		" where f3.id=f.id and f2.diancxxb_id=dc.id and lc.id(+)=f2.luncxxb_id\n" + 
//		" and f2.diancxxb_id=d.id and g.id=f2.gongysb_id and s.id=f2.shr_diancxxb_id \n"+
//		" and f2.id=f.fayslb_id \n"+
//		" and f.leix<>4 \n"+
////		" and f2.FAHRQ>=to_date('"+getRiq()+"','yyyy-mm-dd') \n"+
//		" and f2.FAHRQ<to_date('"+getRiq()+"','yyyy-mm-dd')+1 \n"+		
////		str + " order by f.id desc";
//		"order by f.id desc";
 		
 		
 		ResultSetList rsl = con.getResultSetList(sSql);
 		List faysl_list=new ArrayList();
 		while(rsl.next()){
// 			faysl_list.add(new FaycbBean(this.getDiancxxb_idFromfayslb(rsl.getLong("diancxxb_id")) ,rsl.getLong("fayslb_id")));
 			faysl_list.add(new FaycbBean(rsl.getLong("diancxxb_id") ,rsl.getLong("fayslb_id")));
 		}
 		FayzgInfo.CountChengb(faysl_list,true);
 		setMsg("重新暂估完成");
     }
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			init();
//			setTreeid(null);
			setRiq(null);
//			setRiq2(null);
		}
		getSelectData();
	}
	private void init() {
		((Visit) getPage().getVisit()).setString1("");			//riq1
		((Visit) getPage().getVisit()).setString3("");			//riq2
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
