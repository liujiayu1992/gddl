package com.zhiren.dc.jilgl.jih;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/*
 * 作者：张乐
 * 时间：2010-08-19
 * 描述：增加进厂计划功能————马头电厂个性需求
 */
public class Jincjh extends BasePage implements PageValidateListener {
//	客户端的消息框
	private String msg = "";
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
	}
//	页面初始化(每次刷新都执行)
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
//	 日期控件
	private String riqi;
	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			JDBCcon con = new JDBCcon();
			int zhi=0;
			String sql = "select zhi from xitxxb where  leib='入炉' and mingc ='煤耗用默认日期' and zhuangt =1 ";
			ResultSetList rsl=con.getResultSetList(sql);
			while(rsl.next()){
			   zhi=rsl.getInt("zhi");	
			}
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),+zhi,DateUtil.AddType_intDay));
			con.Close();
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		this.riqi = riqi;
	}

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _CreateChick;
	
	public void CreateButton(IRequestCycle cycle){
		_CreateChick=true;
	}
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
		}
		if(_CreateChick){
			_CreateChick=false;
			Create();
		}
		getSelectData();
	}
	
	private String getFenc(JDBCcon con){
		String sql=" select * from diancxxb where fuid="+this.getTreeid();
		ResultSetList rsl=con.getResultSetList(sql);
		String s="";
		while(rsl.next()){
			s+=rsl.getString("id")+",";
		}
		if(s.equals("")){
			s=this.getTreeid()+",";
		}
		
		return s.substring(0,s.lastIndexOf(","));
	}
	
	private void Create(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		Date riqs ;
		Date riqe ;
		long gongysid;
		long meikid;
		long diancid = visit.getDiancxxb_id();
		int year=0;
		int month=0;
		Date date = new Date();
		riqs = DateUtil.getFirstDayOfNextMonth(date);
		riqe = DateUtil.getLastDayOfMonth(riqs);
		year = DateUtil.getYear(date);
		month = DateUtil.getMonth(date);
		String delSql = "DELETE FROM jincjhb WHERE qisrq>=to_date('"+DateUtil.FormatDate(riqs)+"','yyyy-mm-dd') AND jiesrq<=to_date('"+DateUtil.FormatDate(riqe)+"','yyyy-mm-dd')";
		con.getDelete(delSql);
		String sql = "SELECT DISTINCT gongysb_id,meikxxb_id,diancxxb_id FROM fahb WHERE daohrq<=to_date('"+year+"-"+month+"-1','yyyy-mm-dd')-1 \n"
					+"AND daohrq>=to_date('"+year+"-"+(month-1)+"-1','yyyy-mm-dd') AND fahb.yunsfsb_id=1\n"
					+"ORDER BY gongysb_id,meikxxb_id";
		ResultSetList rs = con.getResultSetList(sql);
		while(rs.next()){
			gongysid = rs.getLong("gongysb_id");
			meikid	 = rs.getLong("meikxxb_id");
			diancid  = rs.getLong("diancxxb_id");
			sql = "insert into jincjhb (id,gongysb_id,meikxxb_id,diancxxb_id,qisrq,jiesrq,lurrq,lury,beiz) values(\n"
				+"getnewid("+diancid+"),"+gongysid+","+meikid+","+diancid+",to_date('"+DateUtil.FormatDate(riqs)+"','yyyy-mm-dd'),to_date('"
				+DateUtil.FormatDate(riqe)+"','yyyy-mm-dd'),to_date('"+DateUtil.FormatDate(date)+"','yyyy-mm-dd'),'"+visit.getRenymc()+"','')";
			int flag = con.getInsert(sql);
			if(flag==-1){
				setMsg("保存失败");
				return;
			}
		}
		rs.close();
		con.Close();
	}
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String rulrq = DateUtil.FormatOracleDate(this.getRiqi());	//入炉日期 
		//电厂Tree刷新条件
		String diancxxb_id="";
		if(getDiancTreeJib()==1){
			diancxxb_id = "";
		}else if(getDiancTreeJib()==2){
			diancxxb_id = "and (d.id = " + this.getTreeid() + " or d.fuid = " + this.getTreeid() + ")\n";
		}else if(getDiancTreeJib()==3){
			diancxxb_id = "and (d.id = " + this.getTreeid() + ")\n";
		}
		
		String chaxun ="SELECT j.ID,g.mingc gongysb_id,m.mingc meikxxb_id,d.mingc diancxxb_id,\n" + 
		"j.qisrq,j.jiesrq,j.lurrq,j.lury,j.beiz\n" + 
		"FROM jincjhb j,gongysb g,meikxxb m,diancxxb d\n" + 
		"WHERE j.gongysb_id = g.id AND j.meikxxb_id = m.id\n" + 
		"AND j.diancxxb_id = d.id AND lurrq = "+DateUtil.FormatOracleDate(this.getRiqi());
		
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("jincjhb");
		egu.setWidth("bodyWidth");
		egu.getColumn("diancxxb_id").setHeader("厂别");
		egu.getColumn("gongysb_id").setHeader("供应商");
		egu.getColumn("meikxxb_id").setHeader("煤矿");
		egu.getColumn("qisrq").setHeader("启用时间");
		egu.getColumn("jiesrq").setHeader("结束日期");
		egu.getColumn("lurrq").setHeader("录入日期");
		egu.getColumn("lurrq").setHidden(true);
		egu.getColumn("lurrq").setEditor(null);
		egu.getColumn("lury").setHeader("录入员");
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("lury").setEditor(null);

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(25);// 设置分页
		// *****************************************设置默认值****************************
		egu.getColumn("qisrq").setDefaultValue(this.getRiqi());
		egu.getColumn("jiesrq").setDefaultValue(this.getRiqi());
		egu.getColumn("lurrq").setDefaultValue(this.getRiqi());
		egu.getColumn("lury").setDefaultValue(visit.getRenymc());

		// 设置下拉框供应商
		ComboBox cb_gongys = new ComboBox();
		egu.getColumn("gongysb_id").setEditor(cb_gongys);
		cb_gongys.setEditable(true);
		String gongys_idSql = "select id,mingc from gongysb where leix = 1";
		egu.getColumn("gongysb_id").setComboEditor(egu.gridId,new IDropDownModel(gongys_idSql));
		egu.getColumn("gongysb_id").setReturnId(true);
		// 设置下拉框煤矿
		ComboBox cb_meik = new ComboBox();
		egu.getColumn("meikxxb_id").setEditor(cb_meik);
		cb_meik.setEditable(true);
		String meik_idSql = "select id,mingc from meikxxb";
		egu.getColumn("meikxxb_id").setComboEditor(egu.gridId,new IDropDownModel(meik_idSql));
		egu.getColumn("meikxxb_id").setReturnId(true);
		
        // 设置下拉框制样人
		ComboBox cb_dianc = new ComboBox();
		egu.getColumn("diancxxb_id").setEditor(cb_dianc);
		cb_dianc.setEditable(true);
		String dianc_idSql = "select id,mingc from diancxxb where fuid="+visit.getDiancxxb_id();
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,new IDropDownModel(dianc_idSql));
		egu.getColumn("diancxxb_id").setReturnId(true);
		

		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
//		 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		egu.addTbarText("-");

		// ************************************************************
		// 刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){").append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新", rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		//生成按钮
//		StringBuffer sc = new StringBuffer();
//		sc.append("function (){").append("document.getElementById('CreateButton').click();}");
//		GridButton ssc = new GridButton("生成", sc.toString());
//		ssc.setIcon(SysConstant.Btn_Icon_Create);
//		egu.addTbarBtn(ssc);
		GridButton daor=new GridButton("生成","function(){ Ext.MessageBox.confirm('提示信息','确定要覆盖掉下月的计划吗?',function(btn){if(btn=='yes'){document.all.CreateButton.click();} });}");
		egu.addTbarBtn(daor);
		
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
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
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setRiqi(null);
			setTreeid(String.valueOf(visit.getDiancxxb_id()));
			getSelectData();
		}
	}

	
//	 得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}
		return IDropDownDiancmc;
	}


	boolean treechange = false;

	private String treeid;

	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString3();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString3(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
		}
		return ((Visit) getPage().getVisit()).getString3();
	}

	public void setTreeid(String treeid) {
		((Visit) getPage().getVisit()).setString3(treeid);
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
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}

		return jib;
	}

}