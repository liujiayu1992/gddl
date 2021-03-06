package com.zhiren.dc.jingjfx;

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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Zhuyzbwcqk extends BasePage implements PageValidateListener {
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
		Visit visit = (Visit) this.getPage().getVisit();
		visit.getExtGrid1().Save(getChange(), visit);
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
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			setRiq();
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
		if (_DelClick) {
			_DelClick = false;
			DelData();
			getSelectData();
		}
	}
	public void DelData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String CurrZnDate=getNianf()+getYuef();
		String strSql=
			"delete\n" +
			"from yuedfxsj\n" + 
			"where to_char(riq,'YYYYMM')='"+CurrZnDate+"' and diancxxb_id="+visit.getDiancxxb_id();
		int flag = con.getDelete(strSql);
		if(flag == -1) {
			WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
			setMsg("删除过程中发生错误！");
		}else {
			setMsg(CurrZnDate+"的数据被成功删除！");
		}
		con.Close();
	}
	public void CreateData() {
		String CurrZnDate=getNianf()+getYuef();
		JDBCcon con = new JDBCcon();
		con.setAutoCommit(false);
		String strSql="";
		Visit visit = (Visit) getPage().getVisit();
	//先删除后插入
	//删除
		strSql=
		"delete\n" +
		"from yuedfxsj y\n" + 
		"where to_char(riq,'YYYYMM')='"+CurrZnDate+"' and diancxxb_id=" + visit.getDiancxxb_id();
		con.getDelete(strSql);
		strSql=
		"insert into yuedfxsj(id,riq,fadl,shouml,haoy,zhangmkc,shijkc,rucmrz,rulmrz,rezc,rucymdj,\n" +
		"rucbmdj,fadmzbmdj,fadzhbmdj,fadmtcb,fadycb,ranlzcb,faddwrlcb,diancxxb_id)(\n" + 
		"select "+MainGlobal.getNewID(visit.getDiancxxb_id())+",sysdate,1000,20000,20000,0,100,3400,3500,100,300,100,100,100,100,100,100,100,"+visit.getDiancxxb_id()+" from dual\n" + 
		"\n" + 
		")";
		con.getInsert(strSql);
		con.commit();
		con.Close();
		setMsg(CurrZnDate+"的数据成功生成！");
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strSql="";
		strSql=
			"select id,riq,fadl,shouml,haoy,zhangmkc,shijkc,rucmrz,rulmrz,rezc,rucymdj,\n" +
			"rucbmdj,fadmzbmdj,fadzhbmdj,fadmtcb,fadycb,ranlzcb,faddwrlcb\n" + 
			"from yuedfxsj" +
			" where to_char(riq,'YYYYMM')='"+getNianf()+getYuef()+"' and diancxxb_id="+visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		ResultSetList rsl = con.getResultSetList(strSql);
		if (rsl == null) {
			WriteLog.writeErrorLog(ErrorMessage.NullResult + "引发错误SQL:" + strSql);
			setMsg(ErrorMessage.NullResult);
			return;
		}
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuedfxsj");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
		egu.addPaging(0);
		egu.setGridType(ExtGridUtil.Gridstyle_Edit);
		//egu.getColumn("xuh").setHeader("序号");
		//egu.getColumn("xuh").setWidth(50);
		egu.getColumn("riq").setHeader("日期");
		egu.getColumn("riq").setWidth(80);
		egu.getColumn("fadl").setHeader("发电量");
		egu.getColumn("shouml").setWidth(60);
		egu.getColumn("shouml").setHeader("实收量");
		egu.getColumn("shouml").setWidth(60);
		egu.getColumn("haoy").setHeader("耗用量");
		egu.getColumn("haoy").setWidth(60);
		egu.getColumn("zhangmkc").setHeader("账面库存");
		egu.getColumn("zhangmkc").setWidth(60);
		egu.getColumn("shijkc").setHeader("实际库存");
		egu.getColumn("shijkc").setWidth(60);
		egu.getColumn("rucmrz").setHeader("入厂煤热值");
		egu.getColumn("rucmrz").setWidth(60);
		egu.getColumn("rulmrz").setHeader("入炉煤热值");
		egu.getColumn("rulmrz").setWidth(60);
		egu.getColumn("rezc").setHeader("热值差");
		egu.getColumn("rezc").setWidth(60);
		egu.getColumn("rucymdj").setHeader("入厂原煤单价");
		egu.getColumn("rucymdj").setWidth(60);
		egu.getColumn("rucbmdj").setHeader("入厂标煤单价");
		egu.getColumn("rucbmdj").setWidth(60);
		egu.getColumn("fadmzbmdj").setHeader("发电煤折表煤单价");
		egu.getColumn("fadmzbmdj").setWidth(60);
		egu.getColumn("fadzhbmdj").setHeader("发电综合标煤单价");
		egu.getColumn("fadzhbmdj").setWidth(60);
		egu.getColumn("fadmtcb").setHeader("发电煤炭成本");
		egu.getColumn("fadmtcb").setWidth(60);
		egu.getColumn("fadycb").setHeader("发电油成本");
		egu.getColumn("fadycb").setWidth(60);
		egu.getColumn("ranlzcb").setHeader("燃料总成本");
		egu.getColumn("ranlzcb").setWidth(60);
		egu.getColumn("faddwrlcb").setHeader("发电单位燃料成本");
		//NumberField nf = new NumberField();
		
//		egu.getColumn("gongysb_id").setEditor(null);
//		egu.getColumn("jihkjb_id").setEditor(null);
//		egu.getColumn("pinzb_id").setEditor(null);
//		egu.getColumn("yunsfsb_id").setEditor(null);
//		egu.getColumn("fenx").setEditor(null);
//		egu.getColumn("jingz").setEditor(null);
//		egu.getColumn("biaoz").setEditor(null);
//		egu.getColumn("yingd").setEditor(null);
//		egu.getColumn("kuid").setEditor(null);
//		egu.getColumn("yuns").setEditor(null);
//		egu.getColumn("koud").setEditor(null);
//		egu.getColumn("kous").setEditor(null);
//		egu.getColumn("kouz").setEditor(null);
//		egu.getColumn("koum").setEditor(null);
//		egu.getColumn("zongkd").setEditor(null);
//		egu.getColumn("sanfsl").setEditor(null);
//		egu.getColumn("jianjl").setEditor(null);
		
//		String Sql="select x.zhi from xitxxb x where x.leib='月报' and x.danw='数量' and x.beiz='使用'";
//		ResultSetList rs = con.getResultSetList(Sql);
//		if(rs != null) {
//			while (rs.next()){
//				String zhi = rs.getString("zhi");
//				if(egu.getColByHeader(zhi)!=null){
//					egu.getColByHeader(zhi).hidden=true;
//				}
//			}
//		}
//		egu.setDefaultsortable(false);             
		
//		egu.getColumn("gongysb_id").update=false;
//		egu.getColumn("jihkjb_id").update=false;
//		egu.getColumn("pinzb_id").update=false;
//		egu.getColumn("yunsfsb_id").update=false;
//		egu.getColumn("fenx").update=false;
		
//		StringBuffer sb = new StringBuffer();
//		sb.append("gridDiv_grid.on('afteredit',function(e){ \n")
//			.append("if(e.field=='RUCTZL'){ \n")
//			.append("\t chv = eval(e.value||0) - eval(e.originalValue||0); \n")
//			.append("\t recs = new Array();\n")
//			.append("\t recs[0] = gridDiv_ds.getAt(0);\n")
//			.append("\t recs[1] = gridDiv_ds.getAt(1);\n")
//			.append("\t recs[2] = gridDiv_ds.getAt(e.row+1);\n")
//			.append("for(m = 0;m < recs.length;m++){")
//			.append("\t recs[m].set('RUCTZL', eval(recs[m].get('RUCTZL')||0) + chv);")
//			.append("}//end for \n")
//			.append("}//end if \n});//end afteredit\n ");
//		
//		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('FENX')=='累计'){e.cancel=true;}");//当"累计"时,这一行不允许编辑
//		sb.append("});");
//		
//		
//		sb.append("gridDiv_grid.on('beforeedit',function(e){");
//		sb.append("if(e.record.get('GONGYSB_ID')=='总计'){e.cancel=true;}");//当电厂列的值是"合计"时,这一行不允许编辑
//		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");//电厂列不允许编辑
//		sb.append("});");
		
		 //设定合计列不保存
//		sb.append("function gridDiv_save(record){if(record.get('gongysb_id')=='总计') return 'continue';}");
		
//		egu.addOtherScript(sb.toString());
		
		// /是否返回下拉框的值或ID
		// egu.getColumn("").setReturnId(true);
		// egu.getColumn("").setReturnId(true);
		// /设置按钮
		egu.addTbarText("年份");
		ComboBox comb1=new ComboBox();
		comb1.setWidth(60);
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
////		判断数据是否被锁定
//		boolean isLocked = isLocked(con);
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
//		if(isLocked) {
//			gbc.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
//		}
		gbc.setIcon(SysConstant.Btn_Icon_Create);
		egu.addTbarBtn(gbc);
//		删除按钮
		GridButton gbd = new GridButton("删除",getBtnHandlerScript("DelButton"));
//		if(isLocked) {
//			gbd.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
//		}
		gbd.setIcon(SysConstant.Btn_Icon_Delete);
		egu.addTbarBtn(gbd);
//		保存按钮
		GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//		if(isLocked) {
//			gbs.setHandler("function (){"+MainGlobal.getExtMessageBox(ErrorMessage.DataLocked_Yueslb,false)+"return;}");
//		}
		egu.addTbarBtn(gbs);
//		打印按钮
		GridButton gbp = new GridButton("打印","function (){"+MainGlobal.getOpenWinScript("MonthReport&lx=yueslb")+"}");
		gbp.setIcon(SysConstant.Btn_Icon_Print);
		egu.addTbarBtn(gbp);
		
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
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getGridScript();
	}

	public String getGridHtml() {
		if(getExtGrid() == null) {
			return "";
		}
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
}
