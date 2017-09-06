package com.zhiren.gs.bjdt.diaoygl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.tools.FtpCreatTxt;
import com.zhiren.common.tools.FtpUpload;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：张少君
 * 时间：2010-03-20 
 * 描述：修改大唐国际收耗存日报保存方法，
 * 		 1、将当日供煤(DANGRGM)同时存人“JINGZ”中，
 *		 2、将HAOYQKDR同时存入FADY
 */
public  class Shouhc extends BasePage implements PageValidateListener {
	
	
	
	private boolean returnMsg=false;
	private boolean hasSaveMsg=false;
	private String _msg;
	public String getMsg() {
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value,false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

//	private void Save() {
//		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
//	}
	private void Save() {
		if(!this.isZuorkc()){
		
			return;
		}
		
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		StringBuffer sql = new StringBuffer("begin \n");
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
		while (rsl.next()) {
			if(rsl.getString("DIANCXXB_ID").equals("合计")){

			}else{
				StringBuffer sql2 = new StringBuffer();
				sql2.append("getnewid(").append(visit.getExtGrid1().getValueSql(
						visit.getExtGrid1().getColumn(rsl.getColumnNames()[1]),rsl.getString(1))).append(")");
				if ("0".equals(rsl.getString("ID"))) {
					sql.append("insert into ").append("shouhcrbb").append("(id");
					for (int i = 1; i < rsl.getColumnCount(); i++) {
						sql.append(",").append(rsl.getColumnNames()[i]);
						sql2.append(",").append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i)));
					}
					sql.append(",").append("JINGZ").append(",").append("FADY");
					sql2.append(",").append(rsl.getString("DANGRGM")).append(",").append(rsl.getString("HAOYQKDR"));
					sql.append(") values(").append(sql2).append(");\n");
				} else {
					sql.append("update ").append("shouhcrbb").append(" set ");
					for (int i = 1; i < rsl.getColumnCount(); i++) {
						sql.append(rsl.getColumnNames()[i]).append(" = ");
						sql.append(
								visit.getExtGrid1().getValueSql(
										visit.getExtGrid1().getColumn(
												rsl.getColumnNames()[i]),
										rsl.getString(i))).append(",");
							}
					sql.deleteCharAt(sql.length() - 1);
					sql.append(",").append("JINGZ").append(" = ").append(rsl.getString("DANGRGM")).append(",")
						.append("FADY").append(" = ").append(rsl.getString("HAOYQKDR"));
					sql.append(" where id =").append(rsl.getString("ID")).append(";\n");
					}
				}
			}
		
		sql.append("end;");
		con.getUpdate(sql.toString());
		con.Close();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}


	private boolean _Refreshclick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		returnMsg=false;
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		
		if (_Refreshclick) {
			_Refreshclick = false;
			
			getSelectData();
		}
	}

	public void getSelectData() {
		
		if(!this.isZuorkc()){
			this.setMsg("昨日数据没有填写,请先填写昨日数据!");
			returnMsg=true;
		}else{
			this.setMsg("");
		}
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		String chaxun="";
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());
		}
		String strdiancTreeID = "";
		int jib=this.getDiancTreeJib();
		if(jib==1){//选集团时刷新出所有的电厂
			strdiancTreeID="";
		}else if (jib==2){//选分公司的时候刷新出分公司下所有的电厂
			strdiancTreeID = " and  (dc.fuid = "+ getTreeid() + " or dc.shangjgsid= "+this.getTreeid()+")";
		}else if (jib==3){//选电厂只刷新出该电厂
			strdiancTreeID=" and dc.id= " +this.getTreeid();
		} 
		
		
			
	
		   	chaxun=" select nvl(sj.id, 0) as id,\n" +
					"       dc.mingc as diancxxb_id,\n" + 
					"       to_date('"+this.getRiqi()+"', 'yyyy-mm-dd') as riq,\n" + 
					"       nvl(sj.dangrgm, 0) as dangrgm,\n" + 
					"       nvl(sj.leijgm, 0) as leijgm,\n" + 
					"       nvl(sj.haoyqkdr, 0) as haoyqkdr,\n" + 
					"       nvl(sj.leijhm, 0) as leijhm,\n" + 
					"       nvl(sj.tiaozl, 0) as tiaozl,\n" + 
					"       decode(nvl(sj.kuc, 0),0,nvl(zr.zuorkc,0),nvl(sj.kuc,0)) as kuc,\n" + 
					"       nvl(zr.zuorkc,0) as zuorkc\n" + 
					"  from (select rb.id,\n" + 
					"               lj.diancxxb_id,\n" + 
					"               rb.riq,\n" + 
					"               rb.dangrgm,\n" + 
					"               lj.leijgm,\n" + 
					"               rb.haoyqkdr,\n" + 
					"               lj.leijhm,\n" + 
					"               rb.tiaozl,\n" + 
					"               rb.kuc\n" + 
					"          from (select rb.id,rb.diancxxb_id,rb.riq,rb.dangrgm,rb.haoyqkdr,rb.tiaozl,rb.kuc\n " +
					"               from shouhcrbb rb where rb.riq = to_date('"+this.getRiqi()+"', 'yyyy-mm-dd')) rb,\n" + 
					"               (select rb.diancxxb_id,\n" + 
					"                       sum(rb.dangrgm) as leijgm,\n" + 
					"                       sum(rb.haoyqkdr) as leijhm\n" + 
					"                  from shouhcrbb rb\n" + 
					"                 where rb.riq >= (to_date('"+DateUtil.FormatDate(DateUtil.getFDOfMonth(this.getRiqi()))+"', 'yyyy-mm-dd'))\n" + 
					"                   and rb.riq <= to_date('"+this.getRiqi()+"', 'yyyy-mm-dd')\n" + 
					"                 group by rb.diancxxb_id) lj\n" + 
					"         where rb.diancxxb_id(+) = lj.diancxxb_id\n" + 
					"           ) sj,\n" + 
					"  \n" + 
					"           (select rb.diancxxb_id,rb.kuc as zuorkc\n" + 
					"                  from shouhcrbb rb\n" + 
					"                 where rb.riq = (to_date('"+this.getRiqi()+"', 'yyyy-mm-dd')-1)\n" + 
					"  \n" + 
					"         ) zr,\n" + 
					"  \n" + 
					"       (select dc.id, px.xuh, dc.mingc,dc.fuid,dc.shangjgsid \n" + 
					"          from diancxxb dc, dianckjpxb px\n" + 
					"         where dc.id = px.diancxxb_id\n" + 
					"           and dc.jib = 3\n" + 
					"           and px.kouj = '月报') dc\n" + 
					"  where sj.diancxxb_id(+) = dc.id and dc.id=zr.diancxxb_id(+)\n" + strdiancTreeID +
					"  order by dc.xuh";

                 
		//System.out.println(chaxun);
		ResultSetList rsl = con.getResultSetList(chaxun);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("shouhcrbb");
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("diancxxb_id").setHeader("单位");
		//egu.getColumn("fadl").setHeader("发电量(万kWh)");
		egu.getColumn("dangrgm").setHeader("当日供煤(t)");
		egu.getColumn("leijgm").setHeader("累计供煤(t)");
		egu.getColumn("haoyqkdr").setHeader("当日耗用(t)");
		egu.getColumn("leijhm").setHeader("累计耗用(t)");
		egu.getColumn("leijhm").setUpdate(false);
		egu.getColumn("leijhm").setEditor(null);
		egu.getColumn("leijgm").setEditor(null);
		
		egu.getColumn("leijgm").setUpdate(false);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setEditor(null);
		egu.getColumn("kuc").setHeader("库存(t)");
		egu.getColumn("tiaozl").setHeader("调整量(t)");
		egu.getColumn("tiaozl").setHidden(true);
		
		//新增加的昨日库存
		egu.getColumn("zuorkc").setHeader("昨日库存(t)");
		egu.getColumn("zuorkc").setHidden(true);
		egu.getColumn("zuorkc").setEditor(null);
		egu.getColumn("zuorkc").setUpdate(false);
		
		
		//设定列的初始宽度
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("diancxxb_id").setWidth(110);
		egu.getColumn("dangrgm").setWidth(70);
		egu.getColumn("leijgm").setWidth(80);
		egu.getColumn("haoyqkdr").setWidth(70);
		egu.getColumn("leijhm").setWidth(80);
		egu.getColumn("tiaozl").setWidth(60);
		egu.getColumn("kuc").setWidth(70);
		
		//设定不可编辑列的颜色
		egu.getColumn("leijgm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		egu.getColumn("leijhm").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);// 设定grid可以编辑
		egu.addPaging(1000);
		egu.setWidth(1000);
		egu.setDefaultsortable(false);//设定页面不自动排序
		// *****************************************设置默认值****************************
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		
		
		
		//*************************下拉框*****************************************88
		//电厂下拉框
		egu.getColumn("diancxxb_id").setEditor(new ComboBox());
		egu.getColumn("diancxxb_id").setComboEditor(egu.gridId, new IDropDownModel("select id,mingc from diancxxb order by mingc"));
		
		// 工具栏
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		
		// 电厂树
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarText("-");// 设置分隔符
		egu.addTbarText("单位:");
		egu.addTbarTreeBtn("diancTree");

		
		//---------------页面js的计算开始------------------------------------------
		StringBuffer sb = new StringBuffer();
		sb.append("gridDiv_grid.on('afteredit', function(e){");
			//计算累计供煤
			sb.append("if(e.field == 'DANGRGM'){e.record.set('LEIJGM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJGM')));}");
			sb.append("if(e.field == 'DANGRGM'){e.record.set('KUC',parseFloat(e.value-e.originalValue)+parseFloat(e.record.get('KUC')));}");
			//计算累计耗煤
			sb.append("if(e.field == 'HAOYQKDR'){e.record.set('LEIJHM',parseFloat(e.value - e.originalValue) + parseFloat(e.record.get('LEIJHM')));}");
			sb.append("if(e.field == 'HAOYQKDR'){e.record.set('KUC',parseFloat(e.originalValue-e.value)+parseFloat(e.record.get('KUC')));}");
			
		sb.append("});");
		
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('DIANCXXB_ID')=='合计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");//电厂列不允许编辑
		sb.append("});");
		
		
       //设定合计列不保存
		sb.append("function gridDiv_save(record){if(record.get('diancxxb_id')=='合计') return 'continue';}");
		 
		egu.addOtherScript(sb.toString());
		//---------------页面js计算结束--------------------------
		
		egu.addTbarText("-");
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		egu.addTbarText("-");
		egu.addToolbarButton(GridButton.ButtonType_SaveAll, "SaveButton");
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
			this.setTreeid(null);
			this.setRiqi(null);
		}
		
		getSelectData();
		if (!returnMsg){
			setMsg("");
		}
		returnMsg=false;
	
		
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

	}
//	 得到是否包含运损系统设置参数
	private String getBaohys() {
		String baohys = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql= "select zhi from xitxxb where mingc='是否包含运损' and diancxxb_id="
				+ diancid;
		ResultSet rs = cn.getResultSet(sql);
		try {
			while (rs.next()) {
				baohys = rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return baohys;

	}
	
	//得到电厂树下拉框的电厂名称或者分公司,集团的名称
	public String getIDropDownDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			cn.Close();
		}
		return IDropDownDiancmc;
	}
	
	//得到电厂的默认到站
	public String getDiancDaoz(){
		String daoz = "";
		String treeid=this.getTreeid();
		if(treeid==null||treeid.equals("")){
			treeid="1";
		}
		JDBCcon con = new JDBCcon();
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("select dc.mingc, cz.mingc  as daoz\n");
			sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
			sql.append(" where dd.diancxxb_id=dc.id\n");
			sql.append(" and  dd.chezxxb_id=cz.id\n");
			sql.append("   and dc.id = "+treeid+"");

			ResultSet rs = con.getResultSet(sql.toString());
			
			while (rs.next()) {
				 daoz = rs.getString("daoz");
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
		return daoz;
	}
	
	boolean treechange=false;
	private String treeid;
//	public String getTreeid() {
//		if(treeid==null||treeid.equals("")){
//			treeid=String.valueOf(((Visit) getPage().getVisit()).getDiancxxb_id());
//		}
//		return treeid;
//	}
//	public void setTreeid(String treeid) {
//		this.treeid = treeid;
//		treechange=true;
//	}
	public String getTreeid() {
		String treeid=((Visit) getPage().getVisit()).getString2();
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

	//日期控件
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-1,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
	//得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
		//System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
			rs.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			
			con.Close();
		}

		return jib;
	}
	public boolean isZuorkc(){//判断昨日库存是否有数据
		boolean isZuorkc=false;
		int treejib = this.getDiancTreeJib();
		
		if (treejib == 3) {
			JDBCcon con = new JDBCcon();
			String riqTiaoj=DateUtil.FormatDate(DateUtil.AddDate(DateUtil.getDate(getRiqi()), -1, DateUtil.AddType_intDay));
			
			String sqlJib = "select s.kuc from shouhcrbb s where s.riq=to_date('"+riqTiaoj +"','yyyy-mm-dd') and s.diancxxb_id="+this.getTreeid()+"";
			ResultSet rs = con.getResultSet(sqlJib.toString());

			try {
				while (rs.next()) {
					isZuorkc = true;
				}
				rs.close();
			} catch (SQLException e) {

				e.printStackTrace();
			} finally {
				con.Close();
			}

		}else{
			isZuorkc=true;
		}
		
		return isZuorkc;
	}
	
}
