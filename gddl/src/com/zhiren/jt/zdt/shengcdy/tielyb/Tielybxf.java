package com.zhiren.jt.zdt.shengcdy.tielyb;



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
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.InterCom_dt;
import com.zhiren.webservice.InterFac_dt;

public class Tielybxf extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg, false);
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

	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}
//	日期控件
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
	
	public String getValueSql(GridColumn gc, String value) {
		if ("string".equals(gc.datatype)) {
			if (gc.combo != null) {
				if (gc.returnId) {
					return "" + gc.combo.getBeanId(value);
				} else {
					return "'" + value + "'";
				}
			} else {
				return "'" + value + "'";
			}

		} else if ("date".equals(gc.datatype)) {
			return "to_date('" + value + "','yyyy-mm-dd')";
		} else {
			return value;
		}
	}

	private boolean Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().setMokmc(SysConstant.RizOpMokm_Tielyb);//设置模块名称，在visit.getExtGrid1().Save(getChange(), visit)调用
		int flag = visit.getExtGrid1().Save(getChange(), visit);
		if(flag!=-1){
			setMsg(ErrorMessage.SaveSuccessMessage);
		}else{
			return false;
		}
		return true;
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _RefreshChick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	private boolean xiafButton = false;
	public void xiafButton(IRequestCycle cycle) {
		xiafButton = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (xiafButton) {
			xiafButton = false;
//			if(Save()){
				xiaf();
				getSelectData();
//			}
		}
	}
	private void xiaf(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		
		String str1 ="";
		if(visit.getRenyjb()==1){
			str1 = "";
		}else if(visit.getRenyjb()==2){
			str1 = "  and (dc.fuid=  " +visit.getDiancxxb_id()+" or dc.shangjgsid="+visit.getDiancxxb_id()+")";
		}else if(visit.getRenyjb()==3){
			str1 = " and dc.id="+visit.getDiancxxb_id();
		}
	    ResultSetList rsl = con
				.getResultSetList(" select t.id,dc.id as diancxxb_id, t.riq as riq,cz.mingc as chezxxb_id,t.banjh,t.ches,t.beiz" +
						"			 from tielyb t,diancxxb dc,chezxxb cz where t.diancxxb_id=dc.id  and xiaf=0  and t.chezxxb_id=cz.id" +
						" and riq = to_date('"+riqTiaoj+"','yyyy-mm-dd') "+str1+" order by diancxxb_id");
		//先提示必须先保存
		//组合成语句先删除、后插入begin end；
		//下发并处理异常alter table tielyb add  xiaf number(1) default 0;
//		String diancmc1="";
		int i=0;
		String[] sqls =null;
		String diancxxb_id="";
		while(rsl.next()){
			
			//取电厂信息表_id(select id from diancxxb where mingc='"+rsl.getString("diancxxb_id")+"')
			if(diancxxb_id.equals(rsl.getString("diancxxb_id"))){//同组
				sqls[i++]="insert into tielyb(ID,DIANCXXB_ID,RIQ,CHEZXXB_ID,BANJH,CHES,BEIZ)\n" + 
						"values\n" + 
						"  ("+rsl.getLong("id")+","+diancxxb_id+" , "
						+DateUtil.FormatOracleDate(rsl.getDate("riq"))+
						",(select id from chezxxb where mingc='"+rsl.getString("chezxxb_id")+"'),'"+
						rsl.getString("banjh")+"',"+rsl.getString("ches")+
						",'"+rsl.getString("beiz")+"');\n";
			
			}else {//新组
				//发送上个组的语句
				if(!diancxxb_id.equals("")){
					InterCom_dt xiaf=new InterCom_dt();
					String[] resul=xiaf.sqlExe( diancxxb_id, sqls, true);
					if(resul[0].equals("true")){
						//1置当前数据状态为2
						String sql1="update tielyb set xiaf= 1 where id="+rsl.getLong("id");
						con.getUpdate(sql1);
						return;//没有日志
					}else{
						System.out.print("上传失败："+resul[0]);
						return ;//缺陷：因为出错可能下发一部分数据
					}
				}
				//初始
				i=0;
				sqls = new String[50];
				sqls[i++]="delete from tielyb where to_char(riq,'yyyy-mm-dd')='"+DateUtil.FormatDate(rsl.getDate("riq"))+"';\n";
				diancxxb_id=rsl.getString("diancxxb_id");
				sqls[i++]="insert into tielyb(ID,DIANCXXB_ID,RIQ,CHEZXXB_ID,BANJH,CHES,BEIZ)\n" + 
				"values\n" + 
				"  ("+rsl.getLong("id")+","+diancxxb_id+" , "
				+DateUtil.FormatOracleDate(rsl.getDate("riq"))+
				",(select id from chezxxb where mingc='"+rsl.getString("chezxxb_id")+"'),'"+
				rsl.getString("banjh")+"',"+rsl.getString("ches")+
				",'"+rsl.getString("beiz")+"');\n";
			}
		}
		if(rsl!=null){
			rsl.close();
		}
	}

	public void getSelectData() {
		 Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String riqTiaoj=this.getRiqi();
		
		String str1 ="";
		if(visit.getRenyjb()==1){
			str1 = "";
		}else if(visit.getRenyjb()==2){
			str1 = "  and (dc.fuid=  " +visit.getDiancxxb_id()+" or dc.shangjgsid="+visit.getDiancxxb_id()+")";
		}else if(visit.getRenyjb()==3){
			str1 = " and dc.id="+visit.getDiancxxb_id();
		}
		
	    ResultSetList rsl = con
				.getResultSetList(" select t.id,dc.mingc as diancxxb_id, t.riq as riq,cz.mingc as chezxxb_id,t.banjh,t.ches,t.beiz" +
						"			 from tielyb t,diancxxb dc,chezxxb cz where t.diancxxb_id=dc.id  and xiaf=0  and t.chezxxb_id=cz.id" +
						" and riq = to_date('"+riqTiaoj+"','yyyy-mm-dd') "+str1+"");
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("tielyb");
		// /设置显示列名称
		
		egu.getColumn("id").setHeader("id");
		egu.getColumn("id").setHidden(true);
		
		egu.getColumn("diancxxb_id").setHeader("电厂名称");
		ComboBox diancmc =new ComboBox();
		diancmc.setEditable(true);
		egu.getColumn("diancxxb_id").setEditor(diancmc);
		if(visit.getRenyjb()==1){
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.jib=3 order by id"));
			egu.getColumn("diancxxb_id").setReturnId(true);
			
		}else if(visit.getRenyjb()==2){
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.fuid="+visit.getDiancxxb_id()+" order by id"));
			
			egu.getColumn("diancxxb_id").setReturnId(true);
			
		}else if(visit.getRenyjb()==3){
			egu.getColumn("diancxxb_id").setComboEditor(egu.gridId,
					new IDropDownModel("select dc.id, dc.mingc from diancxxb dc where dc.id="+visit.getDiancxxb_id()+" order by id"));
			egu.getColumn("diancxxb_id").setReturnId(true);
			egu.getColumn("diancxxb_id").setDefaultValue(visit.getDiancmc());
			
		}
		//egu.getColumn("diancxxb_id").update=false;
		
		
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		egu.getColumn("chezxxb_id").setHeader("发站名称");
		ComboBox fazmc =new ComboBox();
		fazmc.setEditable(true);
		egu.getColumn("chezxxb_id").setEditor(fazmc);
		egu.getColumn("chezxxb_id").setComboEditor(egu.gridId,
				new IDropDownModel("select id, mingc from chezxxb"));
		egu.getColumn("banjh").setHeader("班计划");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("ches").setDefaultValue("0");
		egu.getColumn("beiz").setHeader("备注");	
//		// //设置列宽度
		egu.getColumn("diancxxb_id").setWidth(120);
		egu.getColumn("chezxxb_id").setWidth(90);
		egu.getColumn("banjh").setWidth(90);
		egu.getColumn("ches").setWidth(90);
		egu.getColumn("beiz").setWidth(90);
		
//		// //设置当前列是否编辑
//		egu.getColumn("piny").setEditor(null);
		// /设置当前grid是否可编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		// //设置分页行数（缺省25行可不设）
		egu.addPaging(25);
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		// /设置按钮
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		egu.addToolbarItem("{"+new GridButton("下发","function(){document.getElementById('xiafButton').click();}").getScript()+"}");
		/*
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        "var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
   	    "url = url + '?service=page/' + 'Bumreport&lx=rezc';" +
   	    " window.open(url,'newWin');";
	egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");
	*/
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
			getSelectData();
		}
	}
}
