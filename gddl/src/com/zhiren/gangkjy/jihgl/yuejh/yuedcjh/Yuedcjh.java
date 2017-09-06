package com.zhiren.gangkjy.jihgl.yuejh.yuedcjh;

/**
 * @author 张琦
 */


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
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.*;

public class Yuedcjh extends BasePage implements PageValidateListener {
	public List gridColumns;
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
		if(getChange() == null || "".equals(getChange())){
			setMsg("error,修改记录为空！");
			return;
		}
		Visit v = (Visit) this.getPage().getVisit();
		JDBCcon con = new JDBCcon();
		int flag =0;
		con.setAutoCommit(false);
//		StringBuffer sql = new StringBuffer("begin \n");
		
		String sqldel ="";
		StringBuffer sb = new StringBuffer("begin \n");
		//删除
		ResultSetList rsldel = getExtGrid().getDeleteResultSet(getChange());
		while (rsldel.next()) {
			sqldel ="delete from yuedcjhb where id = "+rsldel.getString(0)+";\n";
			sb.append(sqldel);
		}
		
		
		ResultSetList rsl = getExtGrid().getModifyResultSet(getChange());
		
		String sql ="";

		while (rsl.next()) {
			
		
		
					int id = rsl.getInt("id");
					sql += "update yuedcjhb set "
						+ " riq = to_date('" +rsl.getString("riq")+"','yyyy-mm-dd')"
						+ ",xiemcs = " + rsl.getString("xiemcs")
						+ ", xiemds=" + rsl.getString("xiemds")
						+ ",zhuangccm = '" + rsl.getString("zhuangccm")
						+ "',zhuangcds = " + rsl.getString("zhuangcds")
						+ " ,duiccm = " + rsl.getString("duiccm")
						+ "  where id = " + id + ";\n";
				
		}
		
			sql += "end;\n";
			sb.append(sql);
			flag = con.getUpdate(sb.toString());
			if (flag == -1) {
				con.rollBack();
				con.Close();
				WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
						+ sql);
				setMsg(ErrorMessage.DeleteDatabaseFail);
				return;
			}
			if (flag !=-1){
				setMsg("保存成功！");
			}
		
		rsldel.close();
		rsl.close();
		con.Close();
		getSelectData();
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}
	private boolean _CreateChick = false;
	public void CreateButton(IRequestCycle cycle) {
		_CreateChick = true;
	}
	private boolean _CopyLast = false;
	public void CopyLast(IRequestCycle cycle) {
		_CopyLast = true;
	}

	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
    	getSelectData();
    }

	public void submit(IRequestCycle cycle) {
		Visit visit = (Visit) this.getPage().getVisit();
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
		if (_CreateChick) {
			_CreateChick = false;
			createData(); //生成当月数据
			getSelectData();
		}
		if(_CopyLast){
			_CopyLast = false;
			copyData();//复制上月数据
			getSelectData();
		}
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }

	}
	public void copyData(){
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		
//		 工具栏的年份和月份下拉框
		long intyear;
		String StrMonth = "";
		String laststr = "";
		long intMonth;
		int yues =0;
		int copynew = 0;
		long lastMonth = 1;
		String strdata = "";
		String strdata1 = "";
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
			if(intMonth >1){
				lastMonth = intMonth -1;
			}
		} else { 
			intMonth = getYuefValue().getId();
			if(intMonth >1){
				lastMonth = intMonth -1;
			}
		}
		
		
		
		if (intMonth < 10) {  //得到当月
			
			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
		}
		
		if (lastMonth < 10) {  //得到前月
			
			laststr= "0" + lastMonth;
			
		} else {
			laststr = "" + lastMonth;
		}
		strdata1 = intyear + "-" + laststr+"-01";
		strdata = intyear + "-" +StrMonth+"-01";
		
		String sql = "";
		
		// 当月为2月时，需要从前月取出前29或28号的数据存入当月，不需要补充数据
		if(intMonth == 2){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
				copynew = 0;
			}else{
				yues= 28;
				copynew = 0;
			}
		//当月为4 6 9 11月时，需要从前月取出30条的数据存入当月，不需要补充数据
		}else if(intMonth == 4||intMonth == 6||intMonth == 9||intMonth == 11){
			yues = 30;
			copynew = 1;
		
		//当月为1月时，直接生成数据，全部为默认值	
		}else if(intMonth == 1){
			createData();
			return ;
			
		//当月为3月时，判断，如果今年为闰年就取29条，如果不是闰年就取28条记录 补录2或3条记录	
		}else if(intMonth == 3){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
				copynew = 2;
			}else{
				yues = 28;
				copynew = 3;
			}
		//当月为 5 7 10 12时，需要从前月取30条记录，补充1条记录	
		}else if(intMonth == 5||intMonth == 7||intMonth == 10||intMonth == 12 ){
			yues = 30;
			copynew = 1;
		//当月为8月时，需要从前月取31条记录，不需要补充记录
		}else{
			yues = 31;
			copynew = 0;
		}
		sql = 
			"select y.id id,\n" +
			"       y.diancxxb_id diancxxb,\n" + 
			"       to_char(y.riq,'yyyy-mm-dd') riq,\n" + 
			"       y.meicb_id meicb_id,\n" + 
			"       y.xiemcs xiemcs,\n" + 
			"       y.xiemds xiemds,\n" + 
			"       nvl(y.zhuangccm,'') zhuangccm,\n" + 
			"       y.zhuangcds zhuangcds,\n" + 
			"       y.duiccm duiccm,\n" + 
			"       y.leib leib,\n" + 
			"       y.zhuangt zhuangt,\n" + 
			"       y.beiz beiz" +
			"  from (select *\n" + 
			"          from yuedcjhb\n" + 
			"         where riq >= to_date('"+strdata1+"', 'yyyy-mm-dd')\n" + 
			"           and riq < to_date('"+strdata+"', 'yyyy-mm-dd')\n" + 
			"         order by riq) y\n" + 
			" where rownum <= "+yues+" \n";
		StringBuffer sb = new StringBuffer("begin \n");
		sb.append(deleteData());
		try{
		
		int market = 0;
		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.next()){
			rsl.beforefirst();
		
		while(rsl.next()){
			market++ ;
			long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
			sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
			sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
			sb.append(",add_months(to_date('").append(rsl.getString("riq")).append("','yyyy-mm-dd'),1),0,").append(rsl.getInt("xiemcs"));
			sb.append(",").append(rsl.getDouble("xiemds")).append(",'").append(rsl.getString("zhuangccm"));
			sb.append("',").append(rsl.getDouble("zhuangcds")).append(",").append(rsl.getDouble("duiccm"));
			sb.append(",").append(rsl.getInt("leib")).append(",").append(rsl.getInt("zhuangt"));
			sb.append("); \n");
			
			}
		}else{
			createData();
			return;
		}
		if(copynew >0){
			for(int i= 1;i<=copynew;i++){
				long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
				sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
				sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
				sb.append(",to_date('").append(strdata).append("-").append(market+i).append(",0,");
				sb.append("0,0.0,'',0.0,0.0,0,1); \n");
				
			}
			
		}
		sb.append(" end ; \n");
		con.getInsert(sb.toString());
		con.commit();
		}catch(Exception e){
			con.rollBack();
			setMsg("复制前月数据失败！");
			e.printStackTrace();
		}finally{
			con.Close();
		}
		
	}

	public String deleteData(){
		long intyear;
		String StrMonth = "";
		long intMonth;

		long nextMonth = 0;
		String  monthstr;
		String strdate;
		String strdate1;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
			nextMonth = intMonth +1;
		} else { 
			intMonth = getYuefValue().getId();
			nextMonth = intMonth +1;
		}
		if(intMonth <12){
			if (intMonth < 9) {

				StrMonth = "0" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "0" + nextMonth;
			}else if(intMonth == 9){
				StrMonth = "0"+ intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
				
			}else {
				StrMonth = "" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
			}
			strdate = intyear+"-"+StrMonth+"-01";
			strdate1 = intyear + "-" +monthstr +"-01";
			
		}else{
			strdate = intyear+"-"+intMonth+"-01";
			strdate1 = (intyear+1) +"-01-01";
		}
		
		String ss = "delete from yuedcjhb where riq>=to_date('"+strdate+"','yyyy-mm-dd') and " +
					" riq<to_date('"+strdate1+"','yyyy-mm-dd');" ;
		
		return ss;
	}

	public void createData() {	
//		 工具栏的年份和月份下拉框
		long intyear;
		String StrMonth = "";
		long intMonth;
		int yues =0;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		if(intMonth == 2){
			if((intyear%4 == 0)&&((intyear%100 != 0)&&(intyear%400 == 0))) { 
				yues = 29;
			}else{
				yues= 28;
			}
		}else if(intMonth == 4||intMonth == 6||intMonth == 9||intMonth == 11){
			yues = 30;
		}else{
			yues = 31;
		}
		
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
			
		} else {
			StrMonth = "" + intMonth;
			
		}
		int xiemcs = 0;
		double xiemds = 0.0;

		double zhuangcds = 0.0;
		double duiccm = 0.0;
		Visit v = (Visit)getPage().getVisit();
		JDBCcon con = new JDBCcon();
		StringBuffer sb = new StringBuffer("begin \n");
		sb.append(deleteData());
		try{
		for(int i = 1;i<= yues ;i++){
			long new_id = Long.parseLong(MainGlobal.getNewID(v.getDiancxxb_id()));
			if(i < 10){
			sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
			sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
			sb.append(",").append("to_date('").append(intyear).append("-").append(StrMonth);
			sb.append("-0").append(i).append("','yyyy-mm-dd'),0,").append(xiemcs).append(",").append(xiemds);
			sb.append(",''").append(",").append(zhuangcds).append(",");
			sb.append(duiccm).append(",0,1); \n");
			}else{
				sb.append("insert into yuedcjhb(id,diancxxb_id,riq,meicb_id,xiemcs,xiemds,zhuangccm,zhuangcds,duiccm,leib,zhuangt)");
				sb.append(" values(").append(new_id).append(",").append(v.getDiancxxb_id());
				sb.append(",").append("to_date('").append(intyear).append("-").append(StrMonth);
				sb.append("-").append(i).append("','yyyy-mm-dd'),0,").append(xiemcs).append(",").append(xiemds);
				sb.append(",''").append(",").append(zhuangcds).append(",");
				sb.append(duiccm).append(",0,1);\n");
			}
		}
		sb.append("end ; \n");
		con.getInsert(sb.toString());
		con.commit();
		}catch(Exception e){
			con.rollBack();
			setMsg("生成数据失败！");
			e.printStackTrace();
		}finally{
			con.Close();
		}
	  
		
		
	}
	
	public void getSelectData() {
		
	
		
//		工具栏的年份和月份下拉框
		long intyear;
		String StrMonth = "";
		long intMonth;
		long nextMonth;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else { 
			intMonth = getYuefValue().getId();
		}
		/*String strdate;
		String strdate1;
		String monthstr;
		//当月份小于12月时，得到当月第一天和下个月第一天的值。
		//当月份等于12月时，得到12月第一天和最后一天的值。
		if(intMonth <12){
			if (intMonth < 9) {

				StrMonth = "0" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "0" + nextMonth;
			} else {
				StrMonth = "" + intMonth;
				nextMonth = intMonth + 1;
				monthstr = "" + nextMonth;
			}
			strdate = intyear+"-"+StrMonth+"-01";
			strdate1 = intyear + "-" +monthstr +"-01";
			
		}else{
			strdate = intyear+"-"+intMonth+"-01";
			strdate1 = intyear +"-" +intMonth +"-31";
		}*/

		

		
		Visit visit = (Visit) getPage().getVisit();
		String sql1 = "";
	
		JDBCcon con = new JDBCcon();
		
			
			
			 /*sql1= 
				 "select y.id          id,\n" +
				 "       y.diancxxb_id diancxxb_id,\n" + 
				 "       y.riq         riq,\n" + 
				 "       y.xiemcs      xiemcs,\n" + 
				 "       y.xiemds      xiemds,\n" + 
				 "       y.zhuangccm   zhuangccm,\n" + 
				 "       y.zhuangcds   zhuangcds,\n" + 
				 "       y.duiccm  duiccm \n"+
				 "  from yuedcjhb y \n" + 
				 " where  y.riq >= to_date('"+intyear+"-"+intMonth+"-01"+"', 'yyyy-mm-dd')-1\n" +
				 "and y.riq <add_months( to_date('"+intyear+"-"+intMonth+"-01"+"', 'yyyy-mm-dd'),1)";*/

     String shijq=intyear+"-"+intMonth+"-1";
   sql1="  select -1         id,\n"+
     "nvl(y.diancxxb_id,"+visit.getDiancxxb_id()+") diancxxb_id,\n"+
     "nvl(y.riq,dch.riq)         riq,\n"+
     "nvl(y.xiemcs,0)      xiemcs,\n"+
     "nvl(y.xiemds,0)      xiemds,\n"+
     "nvl(y.zhuangccm,'')   zhuangccm,\n"+
     "nvl(y.zhuangcds,0)   zhuangcds,\n"+
      " dch.kucl   duiccm\n"+
     "from yuedcjhb y ,\n"+
     "(\n"+
     "   select sum(kucl) kucl,to_date('"+shijq+"','yyyy-mm-dd')-1 riq from\n"+ 
     "   duowkcb dw1,\n"+
     "    (select meicb_id,max(shij)shij from duowkcb where shij<to_date('"+shijq+"','yyyy-mm-dd') and leib<>3   group by meicb_id)  gd\n"+
     "     ,meicb mei\n"+
     "      where dw1.meicb_id=gd.meicb_id and dw1.shij=gd.shij and mei.id=dw1.meicb_id and mei.diancxxb_id="+visit.getDiancxxb_id()+"\n"+
     " )  dch\n"+
     "where  y.riq(+)=dch.riq\n"+
	  "union\n"+
	 "select y.id          id,\n"+
	 "      y.diancxxb_id diancxxb_id,\n"+
	 "      y.riq         riq,\n"+
	   "    y.xiemcs      xiemcs,\n"+
	   "    y.xiemds      xiemds,\n"+
	   "    y.zhuangccm   zhuangccm,\n"+
	   "    y.zhuangcds   zhuangcds,\n"+
	   "    y.duiccm  duiccm \n"+
	 " from yuedcjhb y \n"+
	" where  y.riq >= to_date('"+shijq+"', 'yyyy-mm-dd')\n"+
"	and y.riq < add_months(to_date('"+shijq+"', 'yyyy-mm-dd'),1)\n";

		ResultSetList rsl = con.getResultSetList(sql1);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);

		//设置页面宽度
		//egu.setWidth(Locale.Grid_DefaultWidth);
		//设置GRID是否可以编辑
		
		egu.addPaging(0);//设置数据不分页
		egu.setTableName("yuedcjhb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(100);
		egu.getColumn("riq").setEditor(null);
//		egu.getColumn("xiemcs").setHeader("卸煤车数");
//		egu.getColumn("xiemcs").setWidth(80);
//		egu.getColumn("xiemds").setHeader("卸煤吨数");
//		egu.getColumn("xiemds").setWidth(80);
        egu.getColumn("zhuangccm").setHeader("装船船名");
//	    egu.getColumn("zhuangccm").setWidth(80);
//		egu.getColumn("zhuangcds").setHeader("装船吨数");
//		egu.getColumn("zhuangcds").setWidth(80);
		egu.getColumn("duiccm").setHeader("堆场存煤");
		egu.getColumn("duiccm").setWidth(70);
		
		egu.getColumn("zhuangccm").setEditor(new ComboBox());
		egu.getColumn("zhuangccm").setComboEditor(egu.gridId, new IDropDownModel(" select id,mingc from luncxxb order by mingc"));
		egu.getColumn("zhuangccm").setDefaultValue("永久号");
		
//		 工具栏
		egu.addTbarText("年份:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("NIANF");
		comb1.setId("NIANF");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(60);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("-");// 设置分隔符
		
		egu.addTbarText("月份:");
		ComboBox comb2=new ComboBox();
		comb2.setTransform("YUEF");
		comb2.setId("YUEF");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setWidth(50);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");// 设置分隔符
		

		
		//刷新按钮
		GridButton gbtr = new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}");
		gbtr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbtr);
		
		
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		
		egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		
		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
		
//		生成按钮
		GridButton gbt = new GridButton("生成","function(){Ext.MessageBox.alert('','生成会将当月已输入的数据覆盖');document.getElementById('CreateButton').click();}");
		gbt.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbt);
		
		//复制上月数据
//		gbt = new GridButton("复制上月数据","function(){document.getElementById('CopyLast').click();}");
//		gbt.setIcon(SysConstant.Btn_Icon_Copy);
//		egu.addTbarBtn(gbt);
		
		StringBuffer sb = new StringBuffer();
		String Headers = 

			"		 [\n" +
			"         {header:'<table><tr><td width=6 align=center></td></tr></table>', align:'center',rowspan:2},\n" + 
			"         {header:'ID', align:'center',rowspan:2},\n" + 
			"         {header:'DIANCXXB_ID', align:'center',rowspan:2},\n" +
			"		  {header:'日期',align:'center',rowspan:2},\n" + 
			"         {header:'卸煤', colspan:2},\n" + 
			"         {header:'装船', colspan:2},\n" +
			"		  {header:'堆场数<br>吨数',align:'center',rowspan:2}" + 
			"        ],\n" + 
			"        [\n" + 
			"	      {header:'<table><tr><td width=80 align=center style=border:0>车数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>吨数</td></tr></table>', align:'center'},\n" + 
			"         {header:'<table><tr><td width=80 align=center style=border:0>船名</td></tr></table>', align:'center'},\n" + 
			"	      {header:'<table><tr><td width=70 align=center style=border:0>吨数</td></tr></table>', align:'center'}\n" + 
			"        ]"; 

		sb.append(Headers);
		egu.setHeaders(sb.toString());
		egu.setPlugins("new Ext.ux.plugins.XGrid()");
//		---------------页面js的计算开始------------------------------------------
		StringBuffer sbjs = new StringBuffer();
		sbjs.append("gridDiv_grid.on('afteredit', function(e){");
		//计算累计供煤		
		sbjs.append("if(e.field=='ZHUANGCCM'){" +
					"var chuanm=e.record.get('ZHUANGCCM');"+
					"var records=gridDiv_ds.getRange(e.row);"+
					"for (var i=0;i<records.length;i++)" +
					"	{records[i].set('ZHUANGCCM',chuanm);}}\n");
		
		 String Jsscript="if(e.field=='DUICCM'||e.field=='XIEMDS'||e.field=='ZHUANGCDS'){\n"+
		 " var currentrownumber=gridDiv_ds.indexOf(gridDiv_sm.getSelected())\n"+
          "if(e.field=='XIEMDS'||e.field=='ZHUANGCDS')\n"+
           " e.record.set('DUICCM',Number(gridDiv_ds.getAt(currentrownumber-1).get('DUICCM'))+Number(e.record.get('XIEMDS'))-Number(e.record.get('ZHUANGCDS')));\n"+
          "if(currentrownumber+1<=gridDiv_ds.getTotalCount()-1){ \n"+
			"				 var records = gridDiv_ds.getRange(currentrownumber+1,gridDiv_ds.getTotalCount()-1);\n"+
 			"			  for(var i=0;i<records.length;i++){\n"+
		"		              records[i].set('DUICCM',Number(e.record.get('DUICCM'))+Number(records[i].get('XIEMDS'))-Number(records[i].get('ZHUANGCDS')));\n"+
		"		              }\n"+
	     "      }\n"+

        " }\n";
		sbjs.append(Jsscript);
		sbjs.append("});");
		
		egu.addOtherScript(sbjs.toString());
	    String	beforeScript=" gridDiv_grid.on('beforeedit',function(e){\n"+ 
							    "if(Number(e.record.get('ID'))<0){\n"+
							    "    e.cancel=true;\n"+
							    " }\n"+
							   

						     "});\n";

		egu.addOtherScript(beforeScript);
//		---------------页面js计算结束--------------------------
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

//	日期控件
//	 年份
	
	private static IPropertySelectionModel _NianfModel;
	
	public IPropertySelectionModel getNianfModel() {
		if (_NianfModel == null) {
			getNianfModels();
		}
		return _NianfModel;
	}

	private IDropDownBean _NianfValue;

	public IDropDownBean getNianfValue() {
		if (_NianfValue == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}

	// 月份
	public boolean Changeyuef = false;

	private static IPropertySelectionModel _YuefModel;

	public IPropertySelectionModel getYuefModel() {
		if (_YuefModel == null) {
			getYuefModels();
		}
		return _YuefModel;
	}

	private IDropDownBean _YuefValue;


	public IDropDownBean getYuefValue(){
		if (_YuefValue == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (_YuefValue!= null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		_YuefValue = Value;
		
	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_YuefModel = new IDropDownModel(listYuef);
		return _YuefModel;
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		_YuefModel = _value;
	}
}




