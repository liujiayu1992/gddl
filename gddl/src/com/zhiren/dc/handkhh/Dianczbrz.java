package com.zhiren.dc.handkhh;

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
import org.apache.tapestry.html.BasePage;

import com.ibm.icu.text.DateFormat;
import com.zhiren.common.DateUtil;
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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;

import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Dianczbrz extends BasePage {
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
	
	private String riq;
	public void setRiq(String value) {
		riq = value;
	}
	
	public String getRiq() {
		if ("".equals(riq) || riq == null) {
			ResultSetList rs = new JDBCcon().getResultSetList("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') time from dual");//.getString("sysdate");
			rs.next();
			riq = rs.getString("time");
		}
		return riq;
	}
	private String riq1;
	public void setRiq1(String value) {
		riq1 = value;
	}
	public String getRiq1() {
		if(riq1 == null){
			setRiq1(DateUtil.FormatDate(new Date()));
			
		}
		return riq1;
	}
	
	private String riq2;
	public void setRiq2(String value) {
		riq2 = value;
	}
	public String getRiq2() {
		if(riq2 == null){
			setRiq2(DateUtil.FormatDate(new Date()));
		}
		return riq2;
	}
	
	public void setOriRiq(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	public String getOriRiq() {
		return ((Visit) getPage().getVisit()).getString1();
	}
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
//按钮
	private boolean _SaveChick = false;
	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshButton=false;
	public void RefreshButton(IRequestCycle cycle){
		_RefreshButton = true;
	}
	
	private boolean _SearchButton=false;
	public void SearchButton(IRequestCycle cycle){
		_SearchButton=true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefreshButton) {
			_RefreshButton = false;
			getSelectData();
		}
		else if (_SaveChick) {
			_SaveChick = false;
			save();
			getSelectData();
		}
		else if(_SearchButton){
			_SearchButton = false;
			getSelectData();
		}
	}
	
	
	//时间
	
	
	private void save() {
		
		Visit visit = (Visit) this.getPage().getVisit();
//		visit.getExtGrid1().Save(getChange(), visit);
		
		
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		ResultSetList rsl1 = visit.getExtGrid1().getDeleteResultSet(getChange());
		if (rsl == null) {
			WriteLog
					.writeErrorLog(ErrorMessage.NullResult
							+ "ShujblH.Save 中 getExtGrid().getModifyResultSet(getChange()) 方法没有正确返回记录集。");
			setMsg(ErrorMessage.NullResult);
			return;
		}
		StringBuffer sql = new StringBuffer();
		// 插入电厂值班日志表
		sql.append("begin\n");
		
		while (rsl1.next()){
			sql.append("delete from dianczbrzb where\n");
			sql.append("id=" + rsl1.getString("id"));
			sql.append(";\n");
		}
		while (rsl.next()) {
			if("0".equals(rsl.getString("id"))){
sql.append("insert into dianczbrzb\n");
				
				sql
				.append("(id, diancxxb_id, riq, shij, bum, neir, caozyid, caozsj, beiz)\n");
				sql.append("values (getnewid(").append(visit.getDiancxxb_id())
				.append("),").append(visit.getDiancxxb_id());

		sql.append(",").append("to_date('"+rsl.getString("riq")+"','yyyy-mm-dd')");
				
				String shi_fen_miao=rsl.getString("shi")+":"+rsl.getString("fen")+":"+rsl.getString("miao");

				sql.append(",'").append(shi_fen_miao);
				
				sql.append("','").append(rsl.getString("bum"));
				
				sql.append("','").append(rsl.getString("neir"));
				
				sql.append("','").append(visit.getRenyID());
				
				sql.append("',").append("to_date('"+rsl.getString("caozsj")+"','yyyy-mm-dd hh24:mi:ss')");
				
				sql.append(",'").append(rsl.getString("beiz")).append("'");

				sql.append(");\n");
			}else{	
				System.out.println();
				sql.append("update ").append("dianczbrzb").append(" set ");
				sql.append("riq="+"to_date('"+rsl.getString("riq")+"','yyyy-mm-dd')");
				sql.append(",shij='"+rsl.getString("shi")+":"+rsl.getString("fen")+":"+rsl.getString("miao"));
				sql.append("',neir='"+rsl.getString("neir"));
				sql.append("',beiz='"+rsl.getString("beiz"));
				sql.append("' where id="+rsl.getString("id"));
				sql.append(";\n");
			}

		}
		sql.append("end;");
		JDBCcon con=new JDBCcon();
		con.getResultSet(sql.toString());
		
//		setMsg(Jilcz.SaveJilData(sql.toString(), visit.getDiancxxb_id(),
//				SysConstant.YUNSFS_HUOY, SysConstant.HEDBZ_YJJ, null, this
//						.getClass().getName(), Jilcz.SaveMode_BL));
	}


	public void getSelectData() {
		JDBCcon con = new JDBCcon();
		Visit visit = ((Visit) getPage().getVisit());
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("select a.id,b.mingc,a.riq," +
                "substr(a.shij,1,2) shi,substr(a.shij,4,2)fen,substr(a.shij,7,2) miao," +
                "a.bum,a.neir,r.quanc rquanc,to_char(a.caozsj,'yyyy-mm-dd hh24:mi:ss') caozsj,a.beiz " +
                "from dianczbrzb a,diancxxb b,renyxxb r " +
                "where a.diancxxb_id=b.id and a.caozyid=r.id " +
                "and r.id=" +visit.getRenyID() +
                " and a.riq >= to_date('"+ getRiq1() + "','yyyy-mm-dd')"+
                " and a.riq <= to_date('"+ getRiq2() + "','yyyy-mm-dd')"//添加时间段sql语句
                +"order by a.id asc");
		System.out.println("===="+sb.toString());
		ResultSetList rsl = con.getResultSetList(sb.toString());
		if(rsl==null){
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + sb);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv",rsl);
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setTableName("dianczbrzb");
		
		String riq1 = getRiq();
		
		egu.getColumn("mingc").setHeader("名称");
		egu.getColumn("mingc").setEditor(null);
		egu.getColumn("mingc").setDefaultValue(visit.getDiancmc());
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(70);
		egu.getColumn("riq").setDefaultValue(riq1.substring(0, 10).toString());
		
		egu.getColumn("shi").setHeader("时");
		egu.getColumn("shi").setWidth(38);
		getShu(egu,"shi",24);
		egu.getColumn("shi").setDefaultValue(riq1.substring(11, 13));

		egu.getColumn("fen").setHeader("分");
		egu.getColumn("fen").setWidth(38);
		getShu(egu,"fen",60);
		egu.getColumn("fen").setDefaultValue(riq1.substring(14, 16));

		egu.getColumn("miao").setHeader("秒");
		egu.getColumn("miao").setWidth(38);
		getShu(egu,"miao",60);
		egu.getColumn("miao").setDefaultValue(riq1.substring(17, 19));

		egu.getColumn("bum").setHeader("部门");
		egu.getColumn("bum").setEditor(null);
		ResultSet rs=con.getResultSet("select bum,quanc from renyxxb where id="+visit.getRenyID());
		String bum="";
		String quanc="";
		try {
			rs.next();
			bum = rs.getString("bum");
			quanc = rs.getString("quanc");
			if(bum==null){
				bum ="";
			}
			if(quanc==null){
				quanc = "";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		egu.getColumn("bum").setDefaultValue(bum);
		
		egu.getColumn("neir").setHeader("内容");
		egu.getColumn("neir").setWidth(200);

		egu.getColumn("rquanc").setHeader("操作员");
		egu.getColumn("rquanc").setEditor(null);
		egu.getColumn("rquanc").setWidth(70);
		egu.getColumn("rquanc").setDefaultValue(quanc);

		egu.getColumn("caozsj").setHeader("操作时间");
		egu.getColumn("caozsj").setEditor(null);
		egu.getColumn("caozsj").setDefaultValue(riq);
		egu.getColumn("caozsj").setWidth(120);
		
		
		egu.getColumn("beiz").setHeader("备注");
		egu.getColumn("beiz").setWidth(170);
		
		
		//工具栏
		//Toolbar tb1 = new Toolbar("tbdiv");
		egu.addTbarText("选择日期：");
		DateField df1 = new DateField();
		df1.setValue(getRiq1());
		df1.Binding("Riq1", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("至");
		DateField df2 = new DateField();
		df2.setValue(getRiq2());
		df2.Binding("Riq2", "");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df2.getScript());
		egu.addTbarText("-");
		
//		GridButton rbtn = new GridButton(null,"查询","function(){document.getElementById('SearchButton').click();}");
//		rbtn.setIcon(SysConstant.Btn_Icon_Search);
//		egu.addTbarBtn(rbtn);
		
//		egu.setWidth("bodyWidth");
//		egu.add.addFill();
//		egu.addText(new ToolbarText("<marquee width=300 scrollamount=2></marquee>"));
		//setToolbar(tb1);
		
		egu.addToolbarButton(GridButton.ButtonType_Refresh, "RefreshButton");
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str = " var url = 'http://'+document.location.host+document.location.pathname;"
			+ "var end = url.indexOf(';');"
			+ "url = url.substring(0,end);"
			+ "url = url + '?service=page/' + 'DianczbrzReport&lx=zbrz';"
			+ " window.open(url,'newWin');";
			egu.addToolbarItem("{"
			+ new GridButton("打印", "function (){" + str + "}",SysConstant.Btn_Icon_Print).getScript()
			+ "}");
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		setExtGrid(egu);
	}
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}
	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {		
		if(getTbmsg()!=null) {
			getToolbar().deleteItem();
			getToolbar().addText(new ToolbarText("<marquee width=300 scrollamount=2>"+getTbmsg()+"</marquee>"));
		}
//		((DateField)getToolbar().getItem("guohrq")).setValue(getRiq());
		return getToolbar().getRenderScript();
	}
	private String tbmsg;
	public String getTbmsg() {
		return tbmsg;
	}
	public void setTbmsg(String tbmsg) {
		this.tbmsg = tbmsg;
	}
	
	public void getShu(ExtGridUtil egu,String columnname,int k){
		List list = new ArrayList();
		for(int i=1;i<=k; i++){
			list.add(new IDropDownBean(i+"",i+""));
		}
		ComboBox bc = new ComboBox();
		bc.setListWidth(40);
		bc.setEditable(true);
		egu.getColumn(columnname).setEditor(bc);
		egu.getColumn(columnname).setComboEditor(egu.gridId,
				new IDropDownModel(list));		
	}
	
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		
		if (visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			setRiq1(DateUtil.FormatDate(new Date()));
			setRiq2(DateUtil.FormatDate(new Date()));
		}		
		init();
	}
	private void init() {
		setOriRiq(getRiq());
		getSelectData();
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
