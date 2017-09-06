package com.zhiren.dc.monthReport_mt;

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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * 作者：刘雨
 * 时间：2010-07-06
 * 描述：马头月耗存维护
 */

public class Yuehcb_mt extends BasePage implements PageValidateListener {
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
		visit.getExtGrid1().Save(getChange(), visit);
	}

	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
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
		if (_SaveChick) {
			_SaveChick = false;
			Save();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
		}
		getSelectData();
	}
	public void DelData() {
//		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql=
			"delete from yuehcb where yuetjkjb_id in (select id from yuetjkjb_mt where riq="
			+CurrODate+" and diancxxb_id="+diancxxb_id+")";
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程中发生错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	private String mingc = "jingz";
//	取得收煤量的定义 现已作废
	/*private void getShoumlDy() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strSql=" select zhi from xitxxb where leib='月报' and mingc='收煤量定义' and beiz='使用' and diancxxb_id="+visit.getDiancxxb_id();
		ResultSetList rs=con.getResultSetList(strSql);
		if (rs == null) {//判断是否连接失败
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
		}
		if(rs.next()){
			mingc=rs.getString("zhi");//从系统信息表中取出收煤量的定义
		}
	}
	*/
	public void CreateData() {
//		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String CurrZnDate=getNianf()+"年"+getYuef()+"月";
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		int intYuef=Integer.parseInt(getYuef());
		//getShoumlDy();
		String strshouml = mingc;
		String sql = "select zhi from xitxxb where mingc ='月报耗用收煤量定义' and zhuangt = 1 and leib = '月报' and diancxxb_id =" + diancxxb_id;
		ResultSetList sm = con.getResultSetList(sql);
		if(sm.next()){
			if("票重+盈吨-亏吨-运损".equals(sm.getString("zhi"))){
				strshouml = "biaoz + yingd - kuid - yuns";
			}
		}
		sm.close();
		DelData();
		StringBuffer sb = new StringBuffer();
		sb.append("select nvl(y.id,0) id,oy.* from yuetjkjb_mt y, \n")
		.append("(select distinct diancxxb_id,gongysb_id,meikxxb_id,jihkjb_id,pinzb_id,yunsfsb_id from yuehcb h,yuetjkjb_mt t \n")
		.append("where h.yuetjkjb_id = t.id and h.kuc <>0 and t.diancxxb_id =").append(diancxxb_id).append(" \n")
		.append("and t.riq = add_months(").append(CurrODate).append(",-1)) oy\n")
		.append("where y.diancxxb_id = oy.diancxxb_id and y.gongysb_id = oy.gongysb_id\n")
		.append("and y.jihkjb_id = oy.jihkjb_id and y.pinzb_id = oy.pinzb_id\n")
		.append("and y.yunsfsb_id = oy.yunsfsb_id and y.riq=").append(CurrODate);
		ResultSetList rsl = con.getResultSetList(sb.toString());
		while(rsl.next()) {
			if(rsl.getLong("id") == 0) {
				sb.delete(0, sb.length());
				sb.append("insert into yuetjkjb_mt(id,riq,diancxxb_id,xuh,gongysb_id,meikxxb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n")
				.append("getnewid(").append(diancxxb_id).append("),").append(CurrODate).append(",")
				.append(diancxxb_id).append(",0,").append(rsl.getLong("gongysb_id")).append(",").append(rsl.getLong("meikxxb_id")).append(",")
				.append(rsl.getLong("pinzb_id")).append(",").append(rsl.getLong("jihkjb_id")).append(",").append(rsl.getLong("yunsfsb_id")).append(")");
				con.getInsert(sb.toString());
			}
		}
		sb.delete(0, sb.length());
		sb.append("insert into yuehcb(id,fenx,yuetjkjb_id,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc)")
		.append("(select getnewid(").append(diancxxb_id).append("),k.fenx,k.yuetjkjb_id,k.qickc,k.shouml,k.fady,k.gongry,k.qith,k.sunh,k.diaocl,k.panyk,k.kuc from ")
		.append("(select ")
		.append(" rownum,hz.id yuetjkjb_id,hz.fenx,");
		if(intYuef == 1) {
			sb.append("nvl(h.kuc,0) qickc,nvl(")
			.append(strshouml).append(",0) shouml,\n")
			.append("0 fady,0 gongry,0 qith,0 sunh,0 diaocl,0 panyk,0 kuc\n");
		}else {
			sb.append("nvl(decode(hz.fenx,'本月',h.kuc,h.qickc),0) qickc,nvl(")
			.append(strshouml).append(",0) shouml,\n")
			.append("nvl(decode(hz.fenx,'本月',0,h.fady),0) fady, nvl(decode(hz.fenx,'本月',0,h.gongry),0) gongry,\n")
			.append("nvl(decode(hz.fenx,'本月',0,h.qith),0) qith, nvl(decode(hz.fenx,'本月',0,h.sunh),0) sunh,\n")
			.append("nvl(decode(hz.fenx,'本月',0,h.diaocl),0) diaocl, nvl(decode(hz.fenx,'本月',0,h.panyk),0) panyk,\n")
			.append("nvl(decode(hz.fenx,'本月',0,h.kuc),0) kuc\n");
		}
		sb.append("from (select * from yuetjkjb_mt,(select '本月' fenx from dual union select '累计' from dual) where diancxxb_id =").append(diancxxb_id)
		.append("     and riq = ").append(CurrODate).append(" order by id,fenx) hz,yueslb s,").append(" (select y.yuetjkjb_id_new,h.* from ")
		.append("(select nvl(y.id,0) yuetjkjb_id_new,oy.yuetjkjb_id from yuetjkjb_mt y, \n")
		.append("(select distinct t.id yuetjkjb_id,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id from yuehcb h,yuetjkjb_mt t \n")
		.append("where h.yuetjkjb_id = t.id  and t.diancxxb_id =").append(diancxxb_id).append(" \n")//and h.kuc <>0
		.append("and t.riq = add_months(").append(CurrODate).append(",-1)) oy \n").append("where y.diancxxb_id = oy.diancxxb_id and y.gongysb_id = oy.gongysb_id \n")
		.append("and y.jihkjb_id = oy.jihkjb_id and y.pinzb_id = oy.pinzb_id \n").append("and y.yunsfsb_id = oy.yunsfsb_id and y.riq=")
		.append(CurrODate).append(") y, yuehcb h \n").append("where h.yuetjkjb_id = y.yuetjkjb_id ) h\n")
		.append("where hz.id = s.yuetjkjb_id(+) and hz.id = h.yuetjkjb_id_new(+)")
		.append("and hz.fenx = s.fenx(+) and hz.fenx = h.fenx(+) order by hz.id,hz.fenx) k) \n");

		int flag = con.getUpdate(sb.toString());
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+sb.toString());
			setMsg("生成过程出现错误！月耗存未插入成功！");
			con.rollBack();
			con.Close();
			return;
		}
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}
	public void getSelectData() {
//		Visit visit = (Visit) getPage().getVisit();
		String diancxxb_id = getTreeid();
		JDBCcon con = new JDBCcon();
		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
		String strSql="";
		strSql = "select * from yueshchjb where riq = "+CurrODate;
		boolean isLocked = !con.getHasIt(strSql);
		if(isLocked) {
			setMsg("请在使用本模块之前，首先完成本月耗存合计数据的计算！");
		}
			strSql = "select id,'总计' as gongysb_id,'-' meikxxb_id,'-' jihkjb_id,'-' pinzb_id,'-' yunsfsb_id,fenx,\n"+
			"       qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc\n"+
			"       from yueshchjb\n"+
			"where riq ="+CurrODate+" and diancxxb_id = "+diancxxb_id+"\n"+
			" union select * from\n"+
			"(select yh.id ,gongysb.mingc as gongysb,meikxxb.mingc as meikxxb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n"+
			"		yunsfsb.mingc as yunsfsb_id,fenx,qickc,shouml,fady,gongry,qith,sunh,diaocl,panyk,kuc\n"+
			"		from yuetjkjb_mt tj, yuehcb yh, gongysb, jihkjb, pinzb, yunsfsb, meikxxb\n"+
			"where tj.id = yh.yuetjkjb_id and tj.gongysb_id = gongysb.id and tj.meikxxb_id = meikxxb.id\n"+
			"      and tj.jihkjb_id = jihkjb.id and tj.pinzb_id = pinzb.id \n"+
			"      and tj.yunsfsb_id = yunsfsb.id  and tj.diancxxb_id="+diancxxb_id+"\n"+
			"	   and riq="+CurrODate+" order by yh.id) s";
//		}else {
//			strSql = "";
//		}
		
		ResultSetList rsl = con.getResultSetList(strSql);
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		egu.addPaging(0);
		// //设置表名称用于保存
		egu.setTableName("yuehcb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("gongysb_id").setUpdate(false);
		egu.getColumn("gongysb_id").setEditor(null);
		egu.getColumn("meikxxb_id").setHeader("煤矿单位");
		egu.getColumn("meikxxb_id").setWidth(120);
		egu.getColumn("meikxxb_id").setUpdate(false);
		egu.getColumn("meikxxb_id").setEditor(null);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(60);
		egu.getColumn("jihkjb_id").setUpdate(false);
		egu.getColumn("jihkjb_id").setEditor(null);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(45);
		egu.getColumn("pinzb_id").setUpdate(false);
		egu.getColumn("pinzb_id").setEditor(null);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(60);
		egu.getColumn("yunsfsb_id").setUpdate(false);
		egu.getColumn("yunsfsb_id").setEditor(null);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("fenx").setUpdate(false);
		egu.getColumn("fenx").setEditor(null);
		egu.getColumn("qickc").setHeader("期初库存");
		egu.getColumn("qickc").setWidth(70);
		egu.getColumn("qickc").setEditor(null);
		egu.getColumn("shouml").setHeader("收煤量");
		egu.getColumn("shouml").setWidth(70);
		egu.getColumn("shouml").setEditor(null);
		egu.getColumn("fady").setHeader("发电耗");
		egu.getColumn("fady").setWidth(70);
		egu.getColumn("gongry").setHeader("供热耗");
		egu.getColumn("gongry").setWidth(70);
		egu.getColumn("qith").setHeader("其它耗");
		egu.getColumn("qith").setWidth(70);
		egu.getColumn("sunh").setHeader("损耗");
		egu.getColumn("sunh").setWidth(60);
		egu.getColumn("diaocl").setHeader("调出量");
		egu.getColumn("diaocl").setWidth(60);	
		egu.getColumn("panyk").setHeader("盘盈亏");
		egu.getColumn("panyk").setWidth(60);
		egu.getColumn("kuc").setHeader("库存");
		egu.getColumn("kuc").setWidth(60);
		egu.getColumn("kuc").setEditor(null);
		String Sql="select zhi from xitxxb x where x.leib='月报' and x.danw='耗存' and beiz='使用'";
		ResultSetList rs = con.getResultSetList(Sql);
		
		while (rs.next()){
			String zhi = rs.getString("zhi");
			if(egu.getColByHeader(zhi)!=null){
				egu.getColByHeader(zhi).hidden=true;
			}
		}
		
		egu.setDefaultsortable(false);  
		// /设置按钮
		StringBuffer sb = new StringBuffer();	
		sb.append("\ngridDiv_grid.on('afteredit',function(e){");
		sb.append("gridDiv_ds.getAt(e.row+1).set(e.field,eval(eval(gridDiv_ds.getAt(e.row+1).get(e.field)) + eval(e.value-e.originalValue)));CountKuc(gridDiv_ds,e.row);CountKuc(gridDiv_ds,e.row+1);});\n");
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('FENX')=='累计'){e.cancel=true;}");//当"累计"时,这一行不允许编辑
		sb.append("});");
		
		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		sb.append("if(e.record.get('GONGYSB_ID')=='总计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");//电厂列不允许编辑
		sb.append("});");
		
		 //设定合计列不保存
		sb.append("function gridDiv_save(record){if(record.get('gongysb_id')=='总计') return 'continue';}");
		
		egu.addOtherScript(sb.toString());
		
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
		egu.addTbarText("-");// 设置分隔符
		// 设置树
		egu.addTbarText("单位:");
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("diancTree");
		egu.addTbarText("-");// 设置分隔符
//		刷新按钮
		StringBuffer rsb = new StringBuffer();
		rsb.append("function (){")
		.append(MainGlobal.getExtMessageBox("'正在刷新'+Ext.getDom('NianfDropDown').value+'年'+Ext.getDom('YuefDropDown').value+'月的数据,请稍候！'",true))
		.append("document.getElementById('RefreshButton').click();}");
		GridButton gbr = new GridButton("刷新",rsb.toString());
		gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
		egu.addTbarBtn(gbr);
//		生成按钮
		GridButton gbc = new GridButton("生成",getBtnHandlerScript("CreateButton"));
		gbc.setDisabled(isLocked);
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		计算按钮
		GridButton gbct = new GridButton("计算","function(){CountShc(gridDiv_grid);this.setDisabled(true);}") ;
		gbct.setDisabled(isLocked);
		gbct.setIcon(SysConstant.Btn_Icon_Count);
		egu.addTbarBtn(gbct);
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
		gbd.setDisabled(isLocked);
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//		保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save_condition,"gridDiv",egu.getGridColumns(),"SaveButton","if(validateHy(gridDiv_ds)){return;};\n");
		gbs.setDisabled(isLocked);
		egu.addTbarBtn(gbs);
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yuehcb")+"}");
		gbp.setDisabled(isLocked);
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
//		String script = "Ext.getCmp('CountButton').on('click', function(btn, e) {btn.disabled=true;});";
//		egu.addOtherScript(script);
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
			//getShoumlDy();
			setRiq();
			getSelectData();
		}
	}
	
	public String getNianf() {
		return ((Visit) getPage().getVisit()).getString1();
	}
	public void setNianf(String value) {
		((Visit) getPage().getVisit()).setString1(value);
	}
	
	public String getYuef() {
		int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString2());
		if (intYuef<10){
			return "0"+intYuef;
		}else{
			return ((Visit) getPage().getVisit()).getString2();
		}
	}
	public void setYuef(String value) {
		((Visit) getPage().getVisit()).setString2(value);
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
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}

	public String getTreedcScript() {
		return getTree().getWindowTreeScript();
	}
}
