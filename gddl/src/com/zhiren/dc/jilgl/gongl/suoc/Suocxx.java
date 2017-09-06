package com.zhiren.dc.jilgl.gongl.suoc;

import java.util.Date;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 2009-02-19
 * 王磊
 * 增加运输方式的过滤、 锁车时不存储解锁人。
 */
public class Suocxx extends BasePage implements PageValidateListener {
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
	
	
	public Toolbar getToolbar() {
		return ((Visit)this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit)this.getPage().getVisit()).setToolbar(tb1);
	}

//	 绑定日期
	public String getRiqi() {
		return ((Visit)this.getPage().getVisit()).getString3();
	}

	public void setRiqi(String riqi) {
		((Visit)this.getPage().getVisit()).setString3(riqi);
	}

	public String getAfterriqi() {
		return ((Visit)this.getPage().getVisit()).getString4();
	}

	public void setAfterriqi(String riq2) {
		((Visit)this.getPage().getVisit()).setString4(riq2);
	}
//车头号	
	private String cheth="";
	public String getCheth() {
		return cheth;
	}
	public void setCheth(String cheth) {		
		this.cheth = cheth;
	}
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
	
	
	public String getValueSql(GridColumn gc, String value) {
		if("string".equals(gc.datatype)) {
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}else {
				return "'"+value+"'";
			}
			
		}else if("date".equals(gc.datatype)) {
			return "to_date('"+value+"','yyyy-mm-dd')";
		}else if("float".equals(gc.datatype)){
			if(gc.combo != null) {
				if(gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				}else {
					return "'"+value+"'";
				}
			}
			else {
				return value==null||"".equals(value)?"null":value;
			}
//			return value==null||"".equals(value)?"null":value;
		}else{
			return value;
		}
	}

	private void Save(boolean Switch) {
		String Operation = "解锁";
		String islocked = "0";
		if(Switch){
			Operation = "锁定";
			islocked = "1";
		}
		if(getChange()==null || "".equals(getChange())) {
			setMsg("没有需要保存的记录！");
		}
		
		
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl=getExtGrid().getModifyResultSet(getChange());
		if(rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult 
					+ "Suoc.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer sql = new StringBuffer("begin \n");
		String cheph = "";
		//JDBCcon sqlserver = new JDBCcon(JDBCcon.ConnectionType_ODBC,"","jdbc:odbc:qchData_m","","");
		while(rsl.next()){
			String cheh = rsl.getString("cheph");
			cheph += "," + cheh;
			sql.append("update chelxxb set islocked = "+islocked+" where cheph ='"+cheh+"' and diancxxb_id = "+ v.getDiancxxb_id()).append(";\n");			
			
			StringBuffer sql2 = new StringBuffer();
			StringBuffer sql3 = new StringBuffer();
			StringBuffer sql4 = new StringBuffer();
			StringBuffer sql5 = new StringBuffer();
			StringBuffer sql6 = new StringBuffer();	
			StringBuffer sql7 = new StringBuffer();
			StringBuffer sql8 = new StringBuffer();	
			StringBuffer sql9 = new StringBuffer();
			if(islocked=="0"){
				if(rsl.getString("ZT").equals("未锁定")){
					setMsg("该车未被锁定,不用解锁!!");
					return;
				}
				sql.append("update suocztb set zt=0,jiessj=");
				sql2.append("TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS')");
				sql3.append(",jiesry='").append(((Visit) getPage().getVisit()).getRenymc()).append("' ");
				sql4.append("where chelxxb_id=");			    
				sql5.append(rsl.getString("CHELXXB_ID"));
				sql6.append(" and zt=1");		
				sql.append(sql2).append(sql3).append(sql4).append(sql5).append(sql6).append(";\n");
			}else{
				if(rsl.getString("SUOCYY").equals("")||rsl.getString("SUOCYY").equals(null)){
					setMsg("锁车原因不能为空!");
					return;
				}
				if(rsl.getString("ZT").equals("已锁定")){
					setMsg("该车已被锁定,请先解锁!!");
					return;
				}
				
				sql.append("insert into suocztb")
				.append("(id,chelxxb_id,suocsj,suocyy,suocry,jiesry,zt,beiz)");
				
			    sql2.append("getnewid(").append(v.getDiancxxb_id()).append(")");
				sql3.append(",").append(rsl.getString("CHELXXB_ID"));
				sql4.append(",").append("TO_DATE(TO_CHAR(SYSDATE, 'YYYY-MM-DD HH24:MI:SS'),'YYYY-MM-DD HH24:MI:SS')");
				sql5.append(",").append(getValueSql(v.getExtGrid1().getColumn(rsl.getColumnNames()[9]),rsl.getString("SUOCYY")));
				sql6.append(",'").append(((Visit) getPage().getVisit()).getRenymc()).append("'");
				sql7.append(",''");
				sql8.append(",").append(islocked);
				sql9.append(",").append("''");			
				sql.append(" values(").append(sql2).append(sql3).append(sql4).append(sql5).append(sql6).append(sql7).append(sql8).append(sql9).append(");\n");
			}
				
		}
		sql.append("end;");
		con.getUpdate(sql.toString());
		rsl.close();
		con.Close();
		//sqlserver.Close();
		cheph = cheph.substring(1);
		setMsg("您对 "+ cheph +" 进行了"+Operation+"操作。");
	}
 
	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _DeleteChick = false;

	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save(true);
			getSelectData();
		}
		if (_DeleteChick) {
			_DeleteChick = false;
			Save(false);
			getSelectData();
		}
	}

	public void getSelectData() {
		Visit v = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		boolean iboolean=false;
		
		StringBuffer sql =new StringBuffer();
			sql.append("select cp.cheph cheph,max(cp.maoz) maxm,max(cp.piz) maxp " +
				"from fahb f,chepb cp where f.id=cp.fahb_id " +
				"and f.daohrq>="+DateUtil.FormatOracleDate(getRiqi())+"\n" + 
				"and f.daohrq <"+DateUtil.FormatOracleDate(getAfterriqi())+" + 1 ");
			if(getCheth().equals("")||getCheth().equals(null)){
				
			}else{
				sql.append("and cp.cheph like '%"+getCheth()+"%' ");
				
			}
			sql.append("group by cp.cheph");  
		String sql2="select * from chelxxb";
		ResultSetList rsl2 = con.getResultSetList(sql2);
		ResultSetList rsl = con.getResultSetList(sql.toString());
		String isql = "begin\n";
		while(rsl.next()){
			
			if(rsl2.getRows()>0){	
				rsl2.beforefirst();
				while(rsl2.next()){
					
					if(rsl.getString("CHEPH").equals(rsl2.getString("CHEPH"))){
						break;
					}
					else{
						if((rsl2.getRow()+1)==rsl2.getRows()){
							
							iboolean=true;
							isql += "insert into chelxxb(id,diancxxb_id,yunsdwb_id,yunsfsb_id,cheph,maoz,piz,islocked)\n" +
							" values(getnewid("+v.getDiancxxb_id()+"),"+v.getDiancxxb_id()+
							",-1,"+SysConstant.YUNSFS_QIY+",'"+rsl.getString("cheph")+"',"+rsl.getString("maxm")+","+rsl.getString("maxp")+",0);\n";
							
						}	
					}
				}
			}else{
				iboolean=true;
				isql += "insert into chelxxb(id,diancxxb_id,yunsdwb_id,yunsfsb_id,cheph,maoz,piz,islocked)\n" +
				" values(getnewid("+v.getDiancxxb_id()+"),"+v.getDiancxxb_id()+
				",-1,"+SysConstant.YUNSFS_QIY+",'"+rsl.getString("cheph")+"',"+rsl.getString("maxm")+","+rsl.getString("maxp")+",0);\n";
				
			}
		}
			
		if(rsl.getRows()>0&&iboolean==true){
			isql += "end;";
			
			con.getInsert(isql);
			iboolean=false;
		}
		rsl.close();
		rsl2.close();
		StringBuffer showsql=new StringBuffer();
		showsql.append( "select a.id chelxxb_id,cheph,decode(max(a.islocked),1,'已锁定','未锁定') zt,maxm,maxp,minm,minp,TO_CHAR(s.suocsj,'YYYY-MM-DD HH24:MI:SS') suocsj,s.suocyy,s.suocry,TO_CHAR(s.jiessj,'YYYY-MM-DD HH24:MI:SS') jiessj,s.jiesry " +
				"from(select c.id,cp.cheph cheph,c.islocked,max(cp.maoz) maxm,max(cp.piz) maxp,min(cp.maoz) minm,min(cp.piz) minp " +
				"from fahb f,chepb cp,chelxxb c where f.id=cp.fahb_id and f.yunsfsb_id= "+SysConstant.YUNSFS_QIY+" and cp.cheph=c.cheph " +
				"and f.daohrq>="+DateUtil.FormatOracleDate(getRiqi())+"\n" + 
				"and f.daohrq <"+DateUtil.FormatOracleDate(getAfterriqi())+" + 1 ");
				if(getCheth().equals("")){
					
				}else{
					
					showsql.append("and cp.cheph like '%"+getCheth()+"%' ");
					
				}
				showsql.append("group by cp.cheph,c.id,c.islocked) " +
				"a  LEFT JOIN (select sz.chelxxb_id,sz.suocsj,sz.suocyy,sz.suocry,sz.jiessj,sz.jiesry from (select s.id id from (select s.chelxxb_id,max(s.suocsj) suocsj,max(s.jiessj) jiessj from suocztb s group by s.chelxxb_id) ss,suocztb s where s.chelxxb_id=ss.chelxxb_id and s.suocsj in(ss.suocsj)) ids,suocztb sz where ids.id=sz.id and sz.zt=1) s " +
				"on a.id=s.chelxxb_id "+
				"group by a.id,cheph,maxm,maxp,minm,minp,s.suocsj,s.suocyy,s.suocry,s.jiessj,s.jiesry order by cheph");
		rsl = con.getResultSetList(showsql.toString());
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("chepb");
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		//设置多选框
		egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置每页显示行数
		egu.addPaging(25);
		egu.getColumn("chelxxb_id").setHidden(true);
		egu.getColumn("cheph").setHeader(Locale.cheph_chepb);
		egu.getColumn("cheph").setWidth(100);
		egu.getColumn("cheph").setEditor(null);
		egu.getColumn("zt").setHeader("状态");
		egu.getColumn("zt").setWidth(100);
		egu.getColumn("zt").setEditor(null);
		egu.getColumn("maxm").setHeader("最大毛重");
		egu.getColumn("maxm").setWidth(65);
		egu.getColumn("maxm").setEditor(null);
		egu.getColumn("maxp").setHeader("最大皮重");
		egu.getColumn("maxp").setWidth(65);
		egu.getColumn("maxp").setEditor(null);
		egu.getColumn("minm").setHeader("最小毛重");
		egu.getColumn("minm").setWidth(65);
		egu.getColumn("minm").setEditor(null);
		egu.getColumn("minp").setHeader("最小皮重");
		egu.getColumn("minp").setWidth(65);
		egu.getColumn("minp").setEditor(null);
		egu.getColumn("suocsj").setHeader("锁车时间");
		egu.getColumn("suocsj").setWidth(120);
		egu.getColumn("suocsj").setEditor(null);
		egu.getColumn("suocyy").setHeader("锁车原因");
		egu.getColumn("suocyy").setWidth(65);
		egu.getColumn("suocyy").editor.setReadOnly(false);
		egu.getColumn("suocry").setHeader("锁车人");
		egu.getColumn("suocry").setWidth(65);
		egu.getColumn("suocry").setEditor(null);
		egu.getColumn("jiessj").setHeader("解锁时间");
		egu.getColumn("jiessj").setWidth(120);
		egu.getColumn("jiessj").setEditor(null);	
		egu.getColumn("jiessj").setHidden(true);
		egu.getColumn("jiesry").setHeader("解锁人");
		egu.getColumn("jiesry").setWidth(65);
		egu.getColumn("jiesry").setEditor(null);
		egu.getColumn("jiesry").setHidden(true);

		
		egu.addTbarText("到货日期:");
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getAfterriqi());
		df1.Binding("AFTERRIQI", "");// 与html页中的id绑定,并自动刷新
		df1.setId("after");
		egu.addToolbarItem(df1.getScript());
		
		egu.addTbarText("-");
		
		egu.addTbarText("车头号");
		TextField tf = new TextField();
		tf.setId("cheth");
		tf.setWidth(100);
		tf.setValue(getCheth());
		egu.addToolbarItem(tf.getScript());
		
		GridButton gbt = new GridButton("查询","function(){document.getElementById('CHETH').value=cheth.getValue(); document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		egu.addToolbarButton("锁车",GridButton.ButtonType_SubmitSel, "SaveButton");
		egu.addToolbarButton("解锁",GridButton.ButtonType_SubmitSel, "DeleteButton");
		egu.setDefaultsortable(false);
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
	
	public String getTreeScript() {
//		System.out.print(((Visit) this.getPage().getVisit()).getDefaultTree().getScript());
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
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
	
	public IPropertySelectionModel getGongysModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
			setGongysModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
	}

	public void setGongysModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel1(_value);
	}

	public void setGongysModels() {
		String sql = "select id,mingc from gongysb order by xuh,mingc";
		setGongysModel(new IDropDownModel(sql));
	}
	public IPropertySelectionModel getMeikModel() {
		if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
			setMeikModels();
		}
		return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
	}

	public void setMeikModel(IPropertySelectionModel _value) {
		((Visit) this.getPage().getVisit()).setProSelectionModel2(_value);
	}

	public void setMeikModels() {
		String sql = "select id,mingc from meikxxb order by xuh,mingc";
		setMeikModel(new IDropDownModel(sql));
	}

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setGongysModel(null);
			setGongysModels();
			setMeikModel(null);
			setMeikModels();
			setRiqi(DateUtil.FormatDate(new Date()));
			setAfterriqi(DateUtil.FormatDate(new Date()));
			setTbmsg(null);
			getSelectData();
		}
	}
}