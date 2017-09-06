package com.zhiren.gangkjy.duicgl.xunjgl;

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
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.gangkjy.Local;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Xunjxx extends BasePage implements PageValidateListener {
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

	private void Save() {
		Visit visit = (Visit) this.getPage().getVisit();
		Save1(getChange(),visit);
	}
	
	public void Save1(String strchange, Visit visit) {
		
		String tableName = "xunjb";
		int diancxxb_id = 0;
		String riq = "";
		String xunjsj = "";
		String banc = "";
		String xunjr = "";
		String xunjtp = "";
		String beiz = "";
		
		int flag = 0;
		int flag1 = 0;
		
		JDBCcon con = new JDBCcon();
		StringBuffer sb1 = new StringBuffer("");
		StringBuffer sb = new StringBuffer("");
		
		
			ResultSetList delrsl = visit.getExtGrid1().getDeleteResultSet(strchange);
			sb1.append("begin\n");
			while (delrsl.next()) {
				sb1.append("delete ").append(tableName).append(" where id =")
						.append(delrsl.getString(0)).append(";").append("\n");
			}
			sb1.append("End;");
			if(delrsl.getRows()>0){
				flag1 = con.getDelete(sb1.toString());
			}
			ResultSetList mdrsl = visit.getExtGrid1().getModifyResultSet(strchange);
			sb.append("begin\n");
			while(mdrsl.next()){

				diancxxb_id = mdrsl.getInt("DIANCXXB_ID");
				riq = mdrsl.getString("RIQ");
				xunjsj = mdrsl.getString("XUNJRQ")+" "+mdrsl.getString("HOUR")+":"+mdrsl.getString("MI")+":00";
				banc = mdrsl.getString("BANC");
				xunjr = mdrsl.getString("XUNJR");
				xunjtp = mdrsl.getString("XUNJTP");
				beiz = mdrsl.getString("BEIZ");
				
				if ("0".equals(mdrsl.getString("ID"))) {
					int xj_id = Integer.parseInt(MainGlobal.getNewID(visit.getDiancxxb_id()));
					sb.append("insert into ").append(tableName).append(" values(")
						.append(xj_id).append(",")
						.append(diancxxb_id).append(",")
						.append("to_date('").append(riq).append("','yyyy-MM-dd'),")
						.append("to_date('").append(xunjsj).append("','yyyy-MM-dd hh24:mi:ss'),'")
						.append(banc).append("','")
						.append(xunjr).append("','")
						.append(xunjtp).append("','")
						.append(beiz).append("');\n");
				} else {
					
					sb.append("update ").append(tableName).append(" set xunjsj = to_date('").append(xunjsj).append("','yyyy-MM-dd hh24:mi:ss'),")
						.append("banc = '").append(banc).append("',")
						.append("xunjr = '").append(xunjr).append("',")
						.append("beiz = '").append(beiz).append("'\n")
						.append("where id = ").append(mdrsl.getInt("ID")).append(";\n");
				}
			}	
			sb.append("End;");
			if(mdrsl.getRows()>0){
				flag = con.getUpdate(sb.toString());
			}
			
		
		
		if(flag>=0&&flag1>=0){
			setMsg("保存成功!");
			con.commit();
		}else{
			setMsg("保存失败！");
			con.rollBack();
		}
		con.Close();
		
		
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _RefreshChick =false;
	
	public void RefreshButton(IRequestCycle cycle){
		_RefreshChick =true;
	}
	
	private boolean _GuanlChick = false;
    public void GuanlButton(IRequestCycle cycle) {
    	_GuanlChick = true;
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
		if(_GuanlChick){
			_GuanlChick = false;
			GotoShezfa(cycle);
		}
	}
	
	private void GotoShezfa(IRequestCycle cycle) {
		// TODO 自动生成方法存根
		((Visit) getPage().getVisit()).setString10(this.getParameters());
		cycle.activate("XunjxxReport");
	}
	
	private String Parameters;//记录项目ID
	public String getParameters() {
		return Parameters;
	}
	public void setParameters(String value) {
		Parameters = value;
	}

	public void getSelectData() {
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String sql = 
			"select x.id,diancxxb_id,to_char(riq,'yyyy.mm.dd') as riq,banc,xunjr,\n" +
			"       to_date(to_char(xunjsj,'yyyy.mm.dd'),'yyyy-MM-dd') as xunjrq,to_char(xunjsj,'hh') as hour,to_char(xunjsj,'mi') as mi,\n" + 
			"       xunjtp,beiz\n" + 
			"from xunjb x\n" +
			"where diancxxb_id = " + v.getDiancxxb_id() + "\n" +
			"	and riq = to_date('" + this.getRiqi() + "','yyyy-MM-dd') \n" +
			"order by id \n";

		ResultSetList rsl = con
				.getResultSetList(sql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		egu.setTableName("Xunjb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHeader("DIANCXXB_ID");
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").editor = null;
		egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(v.getDiancxxb_id()));
		egu.getColumn("riq").setHeader(Local.riq);
		egu.getColumn("riq").setHidden(true);
		egu.getColumn("riq").setDefaultValue(this.getRiqi());
		egu.getColumn("riq").editor = null;
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("banc").setHeader(Local.banc);
		egu.getColumn("banc").setDefaultValue("0");
		egu.getColumn("banc").setWidth(60);
		egu.getColumn("xunjr").setHeader(Local.xunjr);
//		egu.getColumn("xunjr").setDefaultValue("0");
		egu.getColumn("xunjr").setWidth(80);
		egu.getColumn("xunjrq").setHeader(Local.xunjrq);
		egu.getColumn("xunjrq").setDefaultValue(this.getRiqi());
		egu.getColumn("xunjrq").setWidth(100);
		egu.getColumn("hour").setHeader(Local.shi);
		egu.getColumn("hour").setWidth(80);
		egu.getColumn("mi").setHeader(Local.fen);
		egu.getColumn("mi").setWidth(95);
		egu.getColumn("xunjtp").setHeader(Local.duowtp);
		egu.getColumn("xunjtp").editor = null;
		egu.getColumn("xunjtp").setWidth(120);
		egu.getColumn("beiz").setHeader(Local.beiz);
		egu.getColumn("beiz").setWidth(100);

		//时间框
		egu.addTbarText("日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI","");// 与html页中的id绑定,并自动刷新
		egu.addToolbarItem(df.getScript());
		//时
		List hour = new ArrayList();
		for(int i=0;i<24;i++){
			hour.add(new IDropDownBean(1, i+""));
		}
		ComboBox c1 = new ComboBox();
		egu.getColumn("hour").setEditor(c1);
		c1.setEditable(true);
		egu.getColumn("hour").setComboEditor(egu.gridId,
				new IDropDownModel(hour));
		egu.getColumn("hour").setDefaultValue(0+"");
		egu.getColumn("hour").returnId = true;
		//分
		List mi = new ArrayList();
		for(int i=0;i<60;i++){
			mi.add(new IDropDownBean(1, i+""));
		}
		ComboBox c2 = new ComboBox();
		egu.getColumn("mi").setEditor(c2);
		c2.setEditable(true);
		egu.getColumn("mi").setComboEditor(egu.gridId,
				new IDropDownModel(mi));
		egu.getColumn("mi").setDefaultValue(0+"");
		egu.getColumn("mi").returnId = true;
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		egu.addToolbarButton(GridButton.ButtonType_Delete, "deletebutton");
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		String str2=
			"   var rec = gridDiv_sm.getSelected(); \n"
	        +"  if(rec==null){\n"
	        +"  	Ext.MessageBox.alert('提示信息','请选中一条记录!'); \n"
	        +"  	return;"
	        +"  }else if(rec.get('ID')=='0'){\n"
	        +"  	Ext.MessageBox.alert('提示信息','请先保存!'); \n"
	        +"  	return;"  
	        +"	}else{"
	        +"  	gridDiv_history = rec.get('ID');\n"
	        +"  	document.getElementById('PARAMETERS').value=gridDiv_history; \n"
	        +"  }"
	        +" document.getElementById('GuanlButton').click(); \n";
        egu.addToolbarItem("{"+new GridButton("查询","function(){"+str2+"}",SysConstant.Btn_Icon_Print).getScript()+"}");
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
	
	//日期下拉框
	boolean riqichange=false;
	private String riqi;
	public String getRiqi() {
		if(riqi==null||riqi.equals("")){
			riqi=DateUtil.FormatDate(DateUtil.AddDate(new Date(),-0,DateUtil.AddType_intDay));
		}
		return riqi;
	}
	public void setRiqi(String riqi) {
		
		if(this.riqi!=null &&!this.riqi.equals(riqi)){
			this.riqi = riqi;
			riqichange=true;
		}
		 
	}
}
