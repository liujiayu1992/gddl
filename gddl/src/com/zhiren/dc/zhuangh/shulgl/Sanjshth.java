package com.zhiren.dc.zhuangh.shulgl;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.DatetimeField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Sanjshth extends BasePage implements PageValidateListener {
	
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	protected void initialize() {
		// TODO 自动生成方法存根
		super.initialize();
		setMsg("");
	}

//	 绑定日期
	private String riqi;
	public String getRiqi() {
//		if (riqi == null || riqi.equals("")) {
//			setRiqi(DateUtil.FormatDate(new Date()));
//		}
//		return riqi;
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}
	public void setRiqi(String riqi) {
//		this.riqi = riqi;
		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riqi)) {
			
			((Visit) this.getPage().getVisit()).setString5(riqi);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}

	private String riq2;
	public String getRiq2() {
//		if (riq2 == null || riq2.equals("")) {
//			setRiq2(DateUtil.FormatDate(new Date()));
//		}
//		return riq2;
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}
	public void setRiq2(String riq2) {
//		this.riq2 = riq2;
		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}

	private String Changeid;

	public String getChangeid() {
		return Changeid;
	}

	public void setChangeid(String changeid) {
		Changeid = changeid;
	}
    //页面变化记录
	private String Change;
	public String getChange() {
		return Change;
	}
	public void setChange(String change) {
		Change = change;
	}

	private boolean _RefreshChick = false;
	public void RefreshButton(IRequestCycle cycle) {
		_RefreshChick = true;
	}
	
	private boolean _SaveChick = false;
	public void Save1Button(IRequestCycle cycle) {
		_SaveChick = true;
	}
	
	private boolean _ReturnChick = false;
	
	public void ReturnButton(IRequestCycle cycle) {
		_ReturnChick = true;
	}
	
	private void Return(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
	
			
			cycle.activate("Sanjsh_zh");
		
	}

    
	public void submit(IRequestCycle cycle) {
		if (_RefreshChick) {
			_RefreshChick = false;
			getSelectData();
		}
		if (_SaveChick) {
			_SaveChick = false;
			Save(cycle);
		}
		if (_ReturnChick) {
			_ReturnChick = false;
			Return(cycle);
		}
	}
	
	//取得最大的采样XUH
	public String creatcaiybm(JDBCcon con,String date,long diancxxb_id) {
		String sql="select To_number(Substr(max(bianm), 9, 2))+1 as xuh from caiyb " +
				"where caiyrq=to_date('"+date+"','yyyy-mm-dd') and zhilb_id="+diancxxb_id+"";
		
		String xuh="01";
		ResultSetList rsl = con.getResultSetList(sql);
		
		while(rsl.next()){
			int xuh1=rsl.getInt("xuh");
			if (xuh1<10){
				xuh="0"+xuh1;
			}
		}
		rsl.close();
		
		return xuh;
	}
	
	private String OraDate(Date _date){
		if (_date == null) {
			return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", new Date())+"','yyyy-mm-dd')";
		}
		return "to_date('"+DateUtil.Formatdate("yyyy-MM-dd", _date)+"','yyyy-mm-dd')";
	}
	
	public void Save(IRequestCycle cycle) {
		
		Visit visit = (Visit) this.getPage().getVisit();
		
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		
		String endriq=getRiq2();
		
		int count_fah=0;
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("begin\n");
		

				//更新原来关联的和fahb关联的质量临时表的zhilb id是0，质量临时表的原id是一开始的zhilb id
				sb.append(" update zhillsb set zhilb_id=0 where id="+visit.getStringBuffer1().toString()).append(";\n");
				sb.append(" update zhillsb set zhilb_id="+visit.getStringBuffer2().toString()+" where id="+getChangeid()).append(";\n");
			String sql=" select c.id as cid,zn.bianm zbianm,z.huaysj,"+
			"z.qnet_ar,z.aar,z.ad,z.vdaf,\n" +
			" z.mt,z.stad,z.aad,z.mad,z.qbad,z.had,z.vad,z.fcad,z.std,z.qgrad,z.hdaf,z.qgrad_daf,z.sdaf,\n" + 
			" z.var,z.t1,z.t2,z.t3,z.t4,z.huayy,z.lury,z.beiz,z.shenhzt,z.banz\n" + 
			" ,z.har,z.qgrd"+
			"  from caiyb c, zhillsb z,zhuanmb zn,zhuanmlb zb " +
					"where z.id=zn.zhillsb_id and zn.zhuanmlb_id=zb.id and zb.jib=3 and c.zhilb_id=zn.zhillsb_id and z.id="+getChangeid();
			ResultSetList rsl = con.getResultSetList(sql);
			while(rsl.next()){
				
				sb.append(" update zhilb set  "+
						"huaybh='"+rsl.getString("zbianm")+"',caiyb_id="+rsl.getString("cid")+",huaysj=to_date('"+rsl.getDateTimeString("huaysj")
						+"','yyyy-mm-dd hh24:mi:ss')"+",qnet_ar="+rsl.getDouble("qnet_ar")+",aar="+rsl.getDouble("aar")+
						",ad="+rsl.getDouble("ad")+",vdaf="+rsl.getDouble("vdaf")+",\n" +
						" mt="+rsl.getDouble("mt")+",stad="+rsl.getDouble("stad")+",aad="+rsl.getDouble("aad")+
						",mad="+rsl.getDouble("mad")+",qbad="+rsl.getDouble("qbad")+",had="+rsl.getDouble("had")+
						",vad="+rsl.getDouble("vad")+",fcad="+rsl.getDouble("fcad")+",std="+rsl.getDouble("std")+
						",qgrad="+rsl.getDouble("qgrad")+",hdaf="+rsl.getDouble("hdaf")+",qgrad_daf="+rsl.getDouble("qgrad_daf")+
						",sdaf="+rsl.getDouble("sdaf")+",\n" + 
						" var="+rsl.getDouble("var")+",t1="+rsl.getDouble("t1")+",t2="+rsl.getDouble("t2")+
						",t3="+rsl.getDouble("t3")+",t4="+rsl.getDouble("t4")+""
						+",huayy='"+rsl.getString("huayy")+"',lury='"+rsl.getString("lury")+
						"',beiz='"+rsl.getString("beiz")+"',banz='"+rsl.getString("banz")+"'\n" + 
						" ,har="+rsl.getDouble("har")+",qgrd="+rsl.getDouble("qgrd")+""+
						" where id="+visit.getStringBuffer2().toString()).append(";\n");
			}
			

		sb.append("end;");
		if(sb.length()>13){
		
		int flag = con.getInsert(sb.toString());
		
		if (flag == -1) {
			con.rollBack();
			con.Close();
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
					+ sb);
			return;
		}
		setMsg(ErrorMessage.SaveSuccessMessage);
		

		}
		con.commit();
		con.Close();
		Return(cycle);
	}

	public void getSelectData() {
		Visit visit = ((Visit) this.getPage().getVisit());
		JDBCcon con = new JDBCcon();
		String beginriq=visit.getStringBuffer3().toString();
		String endriq=visit.getString10();
		String fbid="";
		if("".equals(visit.getStringBuffer9().toString())||visit.getStringBuffer9().toString()==null){
		fbid="0";	
		}else{
			fbid=visit.getStringBuffer9().toString();
		}
		StringBuffer sbsql = new StringBuffer("");
		

		sbsql.append(" select z.id,z.huaysj, zn.bianm huaybh,z.qnet_ar, z.aar, z.ad, z.vdaf, round_new(z.mt,2) as mt," +
				" z.stad, z.aad, z.mad, z.qbad, z.had, z.vad, z.fcad, z.std, z.qgrad," +
				"   z.hdaf, z.qgrad_daf, z.sdaf,z.qgrd, z.huayy, z.lury,z.beiz " +"from zhillsb z,zhuanmb zn,zhuanmlb zb where z.zhilb_id=0 and z.id=zn.zhillsb_id and zn.zhuanmlb_id=zb.id and zb.jib=3 and   to_char(z.caiysj,'YYYY-MM-DD')>='"+getRiqi()+"'and to_char(z.caiysj,'YYYY-MM-DD')<='"+getRiq2()+"'  ");

		
		//		String sql=" select * from QITYMXB q where q.songysj";
		ResultSetList rsl = con.getResultSetList(sbsql.toString());
		int count=0;
		int to=0;

		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl); 
		//设置页面宽度
		egu.setWidth(Locale.Grid_DefaultWidth);
		egu.setDefaultsortable(false);
		//设置GRID是否可以编辑
		

		egu.getColumn("id").setHeader("ID");
		egu.getColumn("id").setWidth(70);
		egu.getColumn("id").setHidden(true);
		egu.getColumn("huaysj").setHeader("化验时间");
		egu.getColumn("huaysj").setEditor(null);
		egu.getColumn("qnet_ar").setHeader("收到基低位热量<p>Qnet,ar(Mj/kg)</p>");
		egu.getColumn("qnet_ar").setEditor(null);
		egu.getColumn("qnet_ar").setHidden(true);
		egu.getColumn("aar").setHeader("收到基灰分<p>Aar(%)</p>");
		egu.getColumn("aar").setEditor(null);
		egu.getColumn("ad").setHeader("干燥基灰分<p>Ad(%)</p>");
		egu.getColumn("ad").setEditor(null);
		egu.getColumn("vdaf").setHeader("干燥无灰基挥发分<p>Vdaf(%)</p>");
		egu.getColumn("vdaf").setEditor(null);
		egu.getColumn("mt").setHeader("全水分<p>Mt(%)</p>");
		egu.getColumn("mt").setEditor(null);
		egu.getColumn("huaybh").setHeader("化验编号");
		egu.getColumn("huaybh").setEditor(null);
		egu.getColumn("stad").setHeader("空气干燥基全硫<p>St,ad(%)</p>");
		egu.getColumn("stad").setEditor(null);
		egu.getColumn("aad").setHeader("空气干燥基灰分<p>Aad(%)</p>");
		egu.getColumn("aad").setEditor(null);
		egu.getColumn("mad").setHeader("空气干燥基水分<p>Mad(%)</p>");
		egu.getColumn("mad").setEditor(null);
		egu.getColumn("qbad").setHeader("空气干燥基弹筒热值<p>Qb,ad(Mj/kg)</p>");
		egu.getColumn("qbad").setEditor(null);
		egu.getColumn("had").setHeader("空气干燥基氢<p>Had(%)</p>");
		egu.getColumn("had").setEditor(null);
		egu.getColumn("vad").setHeader("空气干燥基挥发分<p>Vad(%)</p>");
		egu.getColumn("vad").setEditor(null);
		egu.getColumn("fcad").setHeader("固定碳<p>FCad(%)</p>");
		egu.getColumn("fcad").setEditor(null);
		egu.getColumn("std").setHeader("干燥基全硫<p>St,d(%)</p>");
		egu.getColumn("std").setEditor(null);
		egu.getColumn("qgrad").setHeader("空气干燥基高位热值<p>Qgr,ad(Mj/kg)</p>");
		egu.getColumn("qgrad").setEditor(null);
		egu.getColumn("hdaf").setHeader("干燥无灰基氢<p>Hdaf(%)</p>");
		egu.getColumn("hdaf").setEditor(null);
		egu.getColumn("sdaf").setHeader("干燥无灰基全硫<p>Sdaf(%)</p>");
		egu.getColumn("sdaf").setEditor(null);
		egu.getColumn("qgrd").setHeader("干燥基高位热值<p>Qgr,d(Mj/kg)</p>");
		egu.getColumn("qgrd").setEditor(null);
		egu.getColumn("huayy").setHeader("化验员");
		egu.getColumn("huayy").setEditor(null);
		egu.getColumn("lury").setHeader("化验录入员");
		egu.getColumn("lury").setEditor(null);
		egu.getColumn("beiz").setHeader("化验备注");
		egu.getColumn("beiz").setEditor(null);
		egu.getColumn("huaybh").setWidth(80);
		egu.getColumn("mt").setWidth(80);
		egu.getColumn("aar").setWidth(80);
		egu.getColumn("ad").setWidth(80);
		egu.getColumn("vdaf").setWidth(80);
		egu.getColumn("stad").setWidth(80);
		egu.getColumn("qnet_ar").setWidth(110);
		egu.getColumn("aad").setWidth(80);
		egu.getColumn("mad").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("had").setWidth(80);
		egu.getColumn("vad").setWidth(80);
		egu.getColumn("fcad").setWidth(80);
		egu.getColumn("std").setWidth(80);
		egu.getColumn("hdaf").setWidth(80);
		egu.getColumn("qgrad_daf").setHeader("干燥无灰基高位热值<p>Qgr,daf(Mj/kg)</p>");
		egu.getColumn("qgrad_daf").setEditor(null);
		egu.getColumn("qgrad_daf").setWidth(110);
		egu.getColumn("sdaf").setWidth(80);
		egu.getColumn("qgrad").setWidth(80);
		egu.getColumn("qgrd").setWidth(80);
		egu.getColumn("huayy").setWidth(80);
		egu.getColumn("lury").setWidth(80);
		egu.getColumn("beiz").setWidth(80);
		egu.getColumn("had").setHidden(true);
		egu.getColumn("hdaf").setHidden(true);
		

		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(25);
		

		egu.addTbarText("采样日期:");
		DateField df = new DateField();
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riq");
		egu.addToolbarItem(df.getScript());
		
		egu.addTbarText("-");
		
		
		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());
		
	
		egu.addTbarText("-");
		
		GridButton gbt = new GridButton("刷新","function(){document.getElementById('RefreshButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbt);
		
		GridButton gb = new GridButton("化验值替换", "function(){ "
				+ " var rec = gridDiv_sm.getSelected(); "
				+ " if(rec != null){var id = rec.get('ID');"//zhillsb_id
				+ " var Cobjid = document.getElementById('CHANGEID');"
				+ " Cobjid.value = id; }"
				+
				"   var gridDivsave_history = '';\n" +

				"    if(rec == null){\n" + 
				"    Ext.MessageBox.alert('提示信息','请选择要被替换的数据');\n" + 
				"\t\t\t}else{\n" + 
				"\t\t\t\tdocument.getElementById('Save1Button').click();\n" + 
				"\t\t\t\tExt.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
				"\t\t\t}\n" + 
				"\t}"

				+ " ");
		egu.addTbarBtn(gb);
		

		
		String sql_cond=
			"select mingc\n" +
			"  from xitxxb\n" + 
			" where diancxxb_id = 202\n" + 
			"   and zhi = '是'\n" + 
			"   and leib = '运算' and zhuangt = 1\n" + 
			"   and beiz = '使用'";


		String condition= 
			" var  total="+to+";" + 

			"   var Mrcd = gridDiv_ds.getModifiedRecords();\n" + 
			"  for(i = 0; i< Mrcd.length; i++){\n" + 
			"  total += eval(Mrcd[i].get('FBML'),0);"+
			"}\n" + 
			"if(total > "+1+"){ Ext.MessageBox.alert('提示信息','各个煤场煤堆的煤量总和要小于等于发货的数量');return;}\n" + 
			"";
		
		
//	
		GridButton bc = new GridButton("返回","function(){ " +
		" document.getElementById('ReturnButton').click();}");
        bc.setIcon(SysConstant.Btn_Icon_Return);
        egu.addTbarBtn(bc);
        egu.setDefaultsortable(false);
		egu.setGridSelModel(ExtGridUtil.GridselModel_Row_single);// GridselModel_Check
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
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
//			visit.setString1("");	//传合同id	
//			visit.setString5("");
//			visit.setString6("");
//			setRiqi(null);
//			setRiq2(null);
//			getRiq2();
//			getRiqi();


			getSelectData();
		}
		visit.getStringBuffer20();
	}
}
