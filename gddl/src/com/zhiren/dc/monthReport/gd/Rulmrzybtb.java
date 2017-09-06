package com.zhiren.dc.monthReport.gd;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.ibm.icu.text.SimpleDateFormat;
import com.zhiren.common.DateUtil;
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Rulmrzybtb extends BasePage implements PageValidateListener {

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
	// 页面变化记录
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	private void Save() {
//		 工具栏的年份和月份下拉框
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		long intMonth;
		if (getYuefValue() == null) {
			intMonth = DateUtil.getMonth(new Date());
		} else {
			intMonth = getYuefValue().getId();
		}
		// 当月份是1的时候显示01,
		String StrMonth = "";
		if (intMonth < 10) {

			StrMonth = "0" + intMonth;
		} else {
			StrMonth = "" + intMonth;
		}
		//--------------------------------
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		JDBCcon con = new JDBCcon();
		Visit visit = (Visit) this.getPage().getVisit();
		int count=0;
		ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
		StringBuffer sql = new StringBuffer();

		while (drsl.next()) {
			count++;
			sql.append("delete from ").append("rulmrzybb").append(
					" where id =").append(drsl.getLong("id")).append(";\n");
			
		}
		ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
		
	
		while (rsl.next()) {
			count++;
			if ("0".equals(rsl.getString("ID"))) {
				
				sql.append(" insert into rulmrzybb values(getnewid("+rsl.getString("diancxxb_id")+")," +
						rsl.getString("diancxxb_id") +",'"+rsl.getString("fenx")+"',"+rsl.getString("rulml")+","+
						rsl.getString("qnet_ar") +","+rsl.getString("mt")+","+rsl.getString("aar")+","+
						rsl.getString("vdaf")+","+rsl.getString("std")+","+CurrODate+
						");");
			} else {
				 sql.append("update rulmrzybb set fenx='"
					 + rsl.getString("fenx")+"',rulml="
					 + rsl.getDouble("rulml")+",qnet_ar="
					 + rsl.getDouble("qnet_ar")+",mt="
					 + rsl.getDouble("mt")+",aar="
					 + rsl.getDouble("aar")+",vdaf="
					 + rsl.getDouble("vdaf")+",std="
					 + rsl.getDouble("std")
					 + "  where id=" + rsl.getLong("id")+";\n");
			}
		}
			
		String bs="";
		if(count>1){
			bs=" begin \n"+sql.toString()+" end;";
		}else{
			bs=sql.toString().substring(0,sql.toString().lastIndexOf(";"));
		}
		
		int flag=con.getUpdate(bs);
		
		if(flag>=0){
			setMsg("数据操作成功!");
		}else{
			setMsg("数据操作失败!");
		}
		
		con.Close();
	}

	private boolean _SaveClick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveClick = true;
	}

	private boolean _Refreshclick = false;

	public void RefreshButton(IRequestCycle cycle) {
		_Refreshclick = true;
	}
	
	
	private boolean _CreateClick = false;
	
	public void CreateButton(IRequestCycle cycle) {
		_CreateClick = true;
	}
	
	private boolean _DelClick = false;
	
	public void DelButton(IRequestCycle cycle) {
		_DelClick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
//			getSelectData(null);
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
//			getSelectData(null);
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
//			getSelectData(null);
		}
		getSelectData(null);
	}
	


	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrZnDate = getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		int intYuef=Integer.parseInt(getYuef());

		String dcid=this.getDCid(con, this.getTreeid());
		ResultSetList  rsl=null;
		
		rsl=con.getResultSetList(" select min(riq) from rulmrzybb ");//初始化时  从初始数的时间往后算，前面的数据不计算
		
		if(rsl.next()){
			
		}
		
		
		
		StringBuffer bf=new StringBuffer();
		bf.append(" select nvl('本月','') leix,m.diancxxb_id, round_new(sum(meil),0) meil,\n");
		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.qnet_ar*r.meil)/sum(r.meil),2) ) qnet_ar,\n");
		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.mt*r.meil)/sum(r.meil),2) )  mt,\n");
		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.aar*r.meil)/sum(r.meil),2) )  aar,\n");
		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.vdaf*r.meil)/sum(r.meil),2) )  vdaf,\n");
		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.std*r.meil)/sum(r.meil),2) )  std \n");
		bf.append("  from meihyb m ,rulmzlb r\n");
		bf.append(" where  m.rulmzlb_id=r.id \n");
		bf.append(" and to_date(to_char(m.rulrq,'yyyy-MM')||'-01','yyyy-MM-dd')="+CurrODate+" \n");
		bf.append(" and m.diancxxb_id  in ("+dcid+") \n");
		bf.append(" group by m.diancxxb_id\n");
		
		
//		bf.append(" union \n");
//		
//		
//		bf.append(" select nvl('累计','') leix,m.diancxxb_id, round_new(sum(meil),0) meil,\n");
//		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.qnet_ar*r.meil)/sum(r.meil),2) ) qnet_ar,\n");
//		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.mt*r.meil)/sum(r.meil),2) )  mt,\n");
//		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.aar*r.meil)/sum(r.meil),2) )  aar,\n");
//		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.vdaf*r.meil)/sum(r.meil),2) )  vdaf,\n");
//		bf.append(" decode(sum(r.meil),0,0,round_new(sum(r.std*r.meil)/sum(r.meil),2) )  std \n");
//		bf.append("  from meihyb m ,rulmzlb r\n");
//		bf.append(" where  m.rulmzlb_id=r.id \n");
//		bf.append(" and m.rulrq>="+DateUtil.FormatOracleDate(DateUtil.getFirstDayOfYear(getNianf()+"-"+getYuef()+"-01"))+" and  m.rulrq<="+DateUtil.FormatOracleDate(DateUtil.getLastDayOfMonth(getNianf()+"-"+getYuef()+"-01"))+" \n");
//		bf.append(" and m.diancxxb_id  in ("+dcid+") \n");
//		bf.append(" group by m.diancxxb_id\n");
		
		
		
		rsl=con.getResultSetList(bf.toString());
		
		bf=new StringBuffer();
		
		bf.append(" delete from rulmrzybb r where r.riq="+CurrODate+" and r.diancxxb_id in ("+dcid+")");
		con.getDelete(bf.toString());
		
		
		while(rsl.next()){
			
			bf=new StringBuffer();
			bf.append(" insert into rulmrzybb values(getnewid("+rsl.getString("diancxxb_id")+")," +
					rsl.getString("diancxxb_id") +",'"+rsl.getString("leix")+"',"+rsl.getString("meil")+","+
					rsl.getString("qnet_ar") +","+rsl.getString("mt")+","+rsl.getString("aar")+","+
					rsl.getString("vdaf")+","+rsl.getString("std")+","+CurrODate+
					")");
			
			con.getInsert(bf.toString());
		
		}
		
		
        GregorianCalendar c1 = new GregorianCalendar();
        c1.setTime(DateUtil.getDate(getNianf()+"-"+getYuef()+"-01"));
        c1.add(GregorianCalendar.MONTH,-1);
        
		String lastMonth=DateUtil.FormatOracleDate(DateUtil.getFirstDayOfMonth(c1.getTime()));//上一个月时间
		
		bf=new StringBuffer();
		bf.append(" select nvl('累计','') leix,r.diancxxb_id, round_new(sum(r.rulml),0) meil,\n");
		bf.append(" decode(sum(r.rulml),0,0,round_new(sum(r.qnet_ar*r.rulml)/sum(r.rulml),2) ) qnet_ar,\n");
		bf.append(" decode(sum(r.rulml),0,0,round_new(sum(r.mt*r.rulml)/sum(r.rulml),2) )  mt,\n");
		bf.append(" decode(sum(r.rulml),0,0,round_new(sum(r.aar*r.rulml)/sum(r.rulml),2) )  aar,\n");
		bf.append(" decode(sum(r.rulml),0,0,round_new(sum(r.vdaf*r.rulml)/sum(r.rulml),2) )  vdaf,\n");
		bf.append(" decode(sum(r.rulml),0,0,round_new(sum(r.std*r.rulml)/sum(r.rulml),2) )  std \n");
		bf.append("  from ( \n");
		
		bf.append(" select * from rulmrzybb where riq= "+CurrODate+" and fenx='本月' and diancxxb_id  in ("+dcid+") \n");//本月的数据
		bf.append(" union\n");
		bf.append(" select * from rulmrzybb where riq= "+lastMonth+" and fenx='累计' and diancxxb_id  in ("+dcid+") \n");
		
		bf.append(" ) r  \n");
		bf.append("  group by r.diancxxb_id \n");
		
//		System.out.println(bf.toString());
		
		rsl=con.getResultSetList(bf.toString());
		
		while(rsl.next()){
			
			bf=new StringBuffer();
			bf.append(" insert into rulmrzybb values(getnewid("+rsl.getString("diancxxb_id")+")," +
					rsl.getString("diancxxb_id") +",'"+rsl.getString("leix")+"',"+rsl.getString("meil")+","+
					rsl.getString("qnet_ar") +","+rsl.getString("mt")+","+rsl.getString("aar")+","+
					rsl.getString("vdaf")+","+rsl.getString("std")+","+CurrODate+
					")");
			
			con.getInsert(bf.toString());
		
		}

	
		setMsg(CurrZnDate+"的数据成功生成！");
		
		
		con.Close();
		
		
	}
	
	private String getDCid(JDBCcon con,String id){
		String s="";
		
//		String sql=" select * from diancxxb where fuid="+id;
//		
//		ResultSetList rsl=con.getResultSetList(sql);
//		
		int count=0;
//		while(rsl.next()){
//			
//			count++;
//			if(count!=1){
//				s+=",";
//			}
//			s+=rsl.getString("id");
//		}
		
		if(count==0){
			s+=id;
		}
		
		return s;
	}

	public void getSelectData(ResultSetList rsl) {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
	
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");

		// ----------电厂树--------------
		String str = "";
			
			
			
		str =getDCid(con,getTreeid());
			

	
		if(rsl==null){
			
			String sql=" select r.id,r.diancxxb_id,r.fenx,r.rulml,r.qnet_ar,r.mt,r.aar,r.vdaf,r.std  from rulmrzybb r \n" +
					" where fenx='本月' and r.riq="+CurrODate+" and r.diancxxb_id in ( "+str+" ) ";
			rsl=con.getResultSetList(sql);
		}
	
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //设置表名称用于保存
		egu.setTableName("rulmrzybb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");

		egu.getColumn("id").setHidden(true);
		egu.getColumn("diancxxb_id").setHidden(true);
		egu.getColumn("diancxxb_id").setDefaultValue(this.getTreeid());
		
		ComboBox fx=new ComboBox();
		
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setEditor(fx);
		egu.getColumn("fenx").setDefaultValue("本月");
		egu.getColumn("fenx").setComboEditor(egu.gridId, new IDropDownModel(" select 1 id,'本月' mingc from dual union " +
				" select 2 id,'累计' mingc from dual"));
		
		
		egu.getColumn("rulml").setHeader("入炉煤量");
		egu.getColumn("rulml").setDefaultValue(0+"");
		egu.getColumn("rulml").setWidth(60);
		
		
		egu.getColumn("qnet_ar").setHeader("发热量");
		egu.getColumn("qnet_ar").setDefaultValue(0+"");
		egu.getColumn("qnet_ar").setWidth(80);
		
		
		egu.getColumn("mt").setHeader("全水分");
		egu.getColumn("mt").setDefaultValue(0+"");
		egu.getColumn("mt").setWidth(80);
		
		
		egu.getColumn("aar").setHeader("收到灰");
		egu.getColumn("aar").setDefaultValue(0+"");
		egu.getColumn("aar").setWidth(80);
		
		
		egu.getColumn("VDAF").setHeader("挥发份");
		egu.getColumn("VDAF").setDefaultValue(0+"");
		egu.getColumn("VDAF").setWidth(80);
		
		
		egu.getColumn("STD").setHeader("干基硫");
		egu.getColumn("STD").setDefaultValue(0+"");
		egu.getColumn("STD").setWidth(80);
		
		
//		设定列的小数位
		((NumberField)egu.getColumn("rulml").editor).setDecimalPrecision(3);
		((NumberField)egu.getColumn("qnet_ar").editor).setDecimalPrecision(3);
		((NumberField)egu.getColumn("mt").editor).setDecimalPrecision(3);
		((NumberField)egu.getColumn("aar").editor).setDecimalPrecision(3);
		((NumberField)egu.getColumn("VDAF").editor).setDecimalPrecision(3);
		((NumberField)egu.getColumn("STD").editor).setDecimalPrecision(3);
		
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(50);
		comb1.setTransform("NianfDropDown");
		comb1.setId("NianfDropDown");//和自动刷新绑定
		comb1.setLazyRender(true);//动态绑定
		comb1.setEditable(true);
		egu.addToolbarItem(comb1.getScript());
		
		egu.addTbarText("月份");
		ComboBox comb2=new ComboBox();
		comb2.setWidth(50);
		comb2.setTransform("YuefDropDown");
		comb2.setId("YuefDropDown");//和自动刷新绑定
		comb2.setLazyRender(true);//动态绑定
		comb2.setEditable(true);
		egu.addToolbarItem(comb2.getScript());
		egu.addTbarText("-");
//		电厂树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree"); 
		
		egu.addTbarText("-");// 设置分隔符
		
//		判断数据是否被锁定
//		boolean isLocked = isLocked(con);
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
		
				GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
//				gbc.setDisabled(isNotReady);
				gbc.setIcon(SysConstant.Btn_Icon_Create);
				egu.addTbarBtn(gbc);
//				}
		//		删除按钮
				egu.addToolbarButton(GridButton.ButtonType_Delete, null);
		//		添加按钮
				egu.addToolbarButton(GridButton.ButtonType_Insert, null);
		//		保存按钮
				GridButton gbs = new GridButton(GridButton.ButtonType_SaveAll,"gridDiv",egu.getGridColumns(),"SaveButton");
				egu.addTbarBtn(gbs);
	
//		---------------页面js的计算开始------------------------------------------
//		StringBuffer sb = new StringBuffer();
//
//		sb.append("gridDiv_grid.on('afteredit',function(e){");
//		
//		sb.append("if(e.field == 'QICKC'||e.field == 'SHOUYL'||e.field == 'FADYY'||e.field == 'GONGRY'||e.field == 'QITHY'||e.field=='SUNH'||e.field=='DIAOCL'||e.field=='PANYK'||e.field=='KUC')" +
//				"{e.record.set('KUC',parseFloat(e.record.get('QICKC')==''?0:e.record.get('QICKC'))+parseFloat(e.record.get('SHOUYL')==''?0:e.record.get('SHOUYL'))-parseFloat(e.record.get('FADYY')==''?0:e.record.get('FADYY'))-parseFloat(e.record.get('GONGRY')==''?0:e.record.get('GONGRY'))" +
//					" -parseFloat(e.record.get('QITHY')==''?0:e.record.get('QITHY'))-parseFloat(e.record.get('SUNH')==''?0:e.record.get('SUNH'))-parseFloat(e.record.get('DIAOCL')==''?0:e.record.get('DIAOCL'))+parseFloat(e.record.get('PANYK')==''?0:e.record.get('PANYK')))};");
//		
//		sb.append("});");
//				
//		egu.addOtherScript(sb.toString());

		//---------------页面js计算结束--------------------------
		
		setExtGrid(egu);
		con.Close();
	}
	
	public String getBtnHandlerScript(String btnName) {
//		按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf()+"年"+getYuef()+"月";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if(btnName.endsWith("CreateButton")) {
			btnsb.append("新生成数据将覆盖")
			.append(cnDate).append("的已存数据，是否继续？");
		}else {
			btnsb.append("是否删除").append(cnDate).append("的数据？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){")
		.append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
		.append(";Ext.MessageBox.show({msg:'正在处理数据,请稍后...',progressText:'处理中...',")
		.append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
		.append("}; // end if \n").append("});}");
		return btnsb.toString();
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
			visit.setString1(null);
			visit.setString2(null);
			visit.setString3(null);
			visit.setShifsh(true);
			this.setTreeid(null);
			setYuefValue(null);
			setNianfValue(null);
			this.getYuefModels();
			this.getNianfModels();
			setRiq();
		}
//		if(visit.getRenyjb()==3){
//			if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
//				visit.setActivePageName(getPageName().toString());
//				visit.setList1(null);
//				visit.setString1(null);
//				visit.setString2(null);
//				visit.setString3(null);
//				visit.setShifsh(true);
//				this.setTreeid(null);
//				setYuefValue(null);
//				setNianfValue(null);
//				this.getYuefModels();
//				this.getNianfModels();
//				setRiq();
//			}
//		}
		getSelectData(null);
	}
	
	
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString3());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString3();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString3(value);
	}
	
	public void setRiq() {
		setNianf(getNianfValue().getValue());
		setYuef(getYuefValue().getValue());
	}
	
	 // 年份下拉框
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
            int _nianf = DateUtil.getYear(new Date());
            int _yuef = DateUtil.getMonth(new Date());
            if (_yuef == 1) {
                _nianf = _nianf - 1;
            }
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }
	
    public void setNianfValue(IDropDownBean Value) {
    	if  (_NianfValue!=Value){
    		_NianfValue = Value;
    	}
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date())+1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

	// 月份下拉框
	private static IPropertySelectionModel _YuefModel;
	
	public IPropertySelectionModel getYuefModel() {
	    if (_YuefModel == null) {
	        getYuefModels();
	    }
	    return _YuefModel;
	}
	
	private IDropDownBean _YuefValue;
	
	public IDropDownBean getYuefValue() {
	    if (_YuefValue == null) {
	        int _yuef = DateUtil.getMonth(new Date());
	        if (_yuef == 1) {
	            _yuef = 12;
	        } else {
	            _yuef = _yuef - 1;
	        }
	        for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
	            Object obj = getYuefModel().getOption(i);
	            if (_yuef == ((IDropDownBean) obj).getId()) {
	                _YuefValue = (IDropDownBean) obj;
	                break;
	            }
	        }
	    }
	    return _YuefValue;
	}
	
	public void setYuefValue(IDropDownBean Value) {
    	if  (_YuefValue!=Value){
    		_YuefValue = Value;
    	}
	}

    public IPropertySelectionModel getYuefModels() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel = new IDropDownModel(listYuef);
        return _YuefModel;
    }

    public void setYuefModel(IPropertySelectionModel _value) {
        _YuefModel = _value;
    }

//  电厂树

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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return diancmc;

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
			// TODO 自动生成 catch 块
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
	
	private String treeid;

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
	
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
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
}
