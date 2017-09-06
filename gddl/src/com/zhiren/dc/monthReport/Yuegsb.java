package com.zhiren.dc.monthReport;

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
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuegsb extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
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
	
	public void submit(IRequestCycle cycle) {
		if (_SaveClick) {
			_SaveClick = false;
			Save();
			getSelectData();
		}
		if (_Refreshclick) {
			_Refreshclick = false;
			getSelectData();
		}
		
		if (_CreateClick) {
			_CreateClick = false;
			CreateData();
			getSelectData();
		}
	}

	public void CreateData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		String strDate="";
		int intYuef=Integer.parseInt(getYuefValue().getValue());
		if (intYuef<10){
			strDate=getNianfValue().getValue()+"-0"+getYuefValue().getValue();
		}else{
			strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue();
		}
		
		con.getDelete("delete from yuegsb where to_char(riq,'yyyy-mm')='" + strDate + "'");
		String strSql="";
		//本月
		strSql=
			"select distinct gongysb_id as gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,sum(jingz)as jingz,sum(biaoz)as biaoz,sum(yingd)as yingd,sum(kuid)as kuid,\n" +
			"decode(sum(jingz),0,0,round(sum(hetj)/sum(jingz),2))as hetj,decode(sum(jingz),0,0,round(sum(relzj)/sum(jingz),2))as relzj,\n" + 
			"decode(sum(jingz),0,0,round(sum(liufzj)/sum(jingz),2))as liufzj,decode(sum(jingz),0,0,round(sum(huifzj)/sum(jingz),2))as huifzj,decode(sum(jingz),0,0,round(sum(huiffzj)/sum(jingz),2))as huiffzj,\n" + 
			"decode(sum(jingz),0,0,round(sum(shuifzj)/sum(jingz),2))as shuifzj,decode(sum(jingz),0,0,round(sum(meij)/sum(jingz),2))as meij,decode(sum(jingz),0,0,round(sum(meijs)/sum(jingz),2))as meijs,\n" + 
			"decode(sum(jingz),0,0,round(sum(yunj)/sum(jingz),2))as yunj,decode(sum(jingz),0,0,round(sum(yunjs)/sum(jingz),2))as yunjs,decode(sum(jingz),0,0,round(sum(daozzf)/sum(jingz),2))as daozzf,\n" + 
			"decode(sum(jingz),0,0,round(sum(zaf)/sum(jingz),2))as zaf,decode(sum(jingz),0,0,round(sum(qit)/sum(jingz),2))as qit,decode(sum(jingz),0,0,round(sum(biaomdj)/sum(jingz),2))as biaomdj,decode(sum(jingz),0,0,round(sum(buhsbmdj)/sum(jingz),2))as buhsbmdj\n" + 
			"from(\n" + 
			"  select gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,sum(maoz-piz)as jingz,sum(biaoz)as biaoz,sum(yingd)as yingd,sum(yingk-yingd)as kuid,sum(hetj*(maoz-piz))as hetj,\n" + 
			"  sum(relzj*(maoz-piz))as relzj,sum(liufzj*(maoz-piz))as liufzj,sum(huifzj*(maoz-piz))as huifzj,\n" + 
			"  sum(huiffzj*(maoz-piz))as huiffzj,sum(shuifzj*(maoz-piz))as shuifzj,sum(meij*(maoz-piz))as meij,sum(meijs*(maoz-piz))as meijs,\n" + 
			"  sum(yunj*(maoz-piz))as yunj,sum(yunjs*(maoz-piz))as yunjs,\n" + 
			"  sum(daozzf*(maoz-piz))as daozzf,sum(zaf*(maoz-piz))as zaf,\n" + 
			"  sum(qit*(maoz-piz))as qit,sum(biaomdj*(maoz-piz))as biaomdj,sum(buhsbmdj*(maoz-piz))as buhsbmdj\n" + 
			"  from ribmdjb,fahb\n" + 
			"  where ribmdjb.fahb_id=fahb.id and  to_char(daohrq,'yyyy-mm')='" + strDate + "'  and diancxxb_id=" + visit.getDiancxxb_id() + " and meikjsbz=0\n" + 
			"  group by gongysb_id ,pinzb_id,jihkjb_id,yunsfsb_id\n" + 
			")f\n" + 
			"group by gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id";

		ResultSetList rs=con.getResultSetList(strSql);
		while (rs.next()){
			String sql="";
			sql="insert into yuegsb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,fenx,jingz,biaoz,yingd,kuid,hetj,relzj,liufzj,huifzj,huiffzj,shuifzj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,biaomdj,buhsbmdj) values("
				+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
				+","
				+"to_date('" + strDate + "-01" + "','yyyy-mm-dd'),"
				+visit.getDiancxxb_id() + ","
				+rs.getInt("gongysb_id") + ","
				+rs.getInt("jihkjb_id") + ","
				+rs.getInt("pinzb_id") + ","
				+rs.getInt("yunsfsb_id") + ","
				+"'本月',"
				+rs.getDouble("jingz") + ","
				+rs.getDouble("biaoz") + ","
				+rs.getDouble("yingd") + ","
				+rs.getDouble("kuid") + ","
				+rs.getDouble("hetj")+","
				+rs.getDouble("relzj")+","
				+rs.getDouble("liufzj")+","
				+rs.getDouble("huifzj")+","
				+rs.getDouble("huiffzj")+","
				+rs.getDouble("shuifzj")+","
				+rs.getDouble("meij")+","
				+rs.getDouble("meijs")+","
				+rs.getDouble("yunj")+","
				+rs.getDouble("yunjs")+","
				+rs.getDouble("daozzf")+","
				+rs.getDouble("zaf")+","
				+rs.getDouble("qit")+","
				+rs.getDouble("biaomdj")+","
				+rs.getDouble("buhsbmdj")
				+")";
				
			con.getInsert(sql);		
		}
		// 累计
		strSql = 
			"select distinct gongysb_id as gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,sum(jingz)as jingz,sum(biaoz)as biaoz,sum(yingd)as yingd,sum(kuid)as kuid,\n" +
			"decode(sum(jingz),0,0,round(sum(hetj)/sum(jingz),2))as hetj,decode(sum(jingz),0,0,round(sum(relzj)/sum(jingz),2))as relzj,\n" + 
			"decode(sum(jingz),0,0,round(sum(liufzj)/sum(jingz),2))as liufzj,decode(sum(jingz),0,0,round(sum(huifzj)/sum(jingz),2))as huifzj,decode(sum(jingz),0,0,round(sum(huiffzj)/sum(jingz),2))as huiffzj,\n" + 
			"decode(sum(jingz),0,0,round(sum(shuifzj)/sum(jingz),2))as shuifzj,decode(sum(jingz),0,0,round(sum(meij)/sum(jingz),2))as meij,decode(sum(jingz),0,0,round(sum(meijs)/sum(jingz),2))as meijs,\n" + 
			"decode(sum(jingz),0,0,round(sum(yunj)/sum(jingz),2))as yunj,decode(sum(jingz),0,0,round(sum(yunjs)/sum(jingz),2))as yunjs,decode(sum(jingz),0,0,round(sum(daozzf)/sum(jingz),2))as daozzf,\n" + 
			"decode(sum(jingz),0,0,round(sum(zaf)/sum(jingz),2))as zaf,decode(sum(jingz),0,0,round(sum(qit)/sum(jingz),2))as qit,decode(sum(jingz),0,0,round(sum(biaomdj)/sum(jingz),2))as biaomdj,decode(sum(jingz),0,0,round(sum(buhsbmdj)/sum(jingz),2))as buhsbmdj\n" + 
			"from(\n" + 
			"  select gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,sum(maoz-piz)as jingz,sum(biaoz)as biaoz,sum(yingd)as yingd,sum(yingk-yingd)as kuid,sum(hetj*(maoz-piz))as hetj,\n" + 
			"  sum(relzj*(maoz-piz))as relzj,sum(liufzj*(maoz-piz))as liufzj,sum(huifzj*(maoz-piz))as huifzj,\n" + 
			"  sum(huiffzj*(maoz-piz))as huiffzj,sum(shuifzj*(maoz-piz))as shuifzj,sum(meij*(maoz-piz))as meij,sum(meijs*(maoz-piz))as meijs,\n" + 
			"  sum(yunj*(maoz-piz))as yunj,sum(yunjs*(maoz-piz))as yunjs,\n" + 
			"  sum(daozzf*(maoz-piz))as daozzf,sum(zaf*(maoz-piz))as zaf,\n" + 
			"  sum(qit*(maoz-piz))as qit,sum(biaomdj*(maoz-piz))as biaomdj,sum(buhsbmdj*(maoz-piz))as buhsbmdj\n" + 
			"  from ribmdjb,fahb\n" + 
			"  where ribmdjb.fahb_id=fahb.id and  to_char(daohrq,'yyyy-mm')>='" + getNianfValue().getValue() + "-01" + "' and to_char(daohrq,'yyyy-mm')<='" + strDate + "'  and diancxxb_id=" + visit.getDiancxxb_id() + " and meikjsbz=0\n" + 
			"  group by gongysb_id ,pinzb_id,jihkjb_id,yunsfsb_id\n" + 
			")f\n" + 
			"group by gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id";

		ResultSetList rsL=con.getResultSetList(strSql);
		while (rsL.next()){
			String sql = "";
			sql="insert into yuegsb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,fenx,jingz,biaoz,yingd,kuid,hetj,relzj,liufzj,huifzj,huiffzj,shuifzj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,biaomdj,buhsbmdj) values("
				+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
				+","
				+"to_date('" + strDate + "-01" + "','yyyy-mm-dd'),"
				+visit.getDiancxxb_id() + ","
				+rsL.getInt("gongysb_id") + ","
				+rsL.getInt("jihkjb_id") + ","
				+rsL.getInt("pinzb_id") + ","
				+rsL.getInt("yunsfsb_id") + ","
				+"'累计',"
				+rsL.getDouble("jingz") + ","
				+rsL.getDouble("biaoz") + ","
				+rsL.getDouble("yingd") + ","
				+rsL.getDouble("kuid") + ","
				+rsL.getDouble("hetj")+","
				+rsL.getDouble("relzj")+","
				+rsL.getDouble("liufzj")+","
				+rsL.getDouble("huifzj")+","
				+rsL.getDouble("huiffzj")+","
				+rsL.getDouble("shuifzj")+","
				+rsL.getDouble("meij")+","
				+rsL.getDouble("meijs")+","
				+rsL.getDouble("yunj")+","
				+rsL.getDouble("yunjs")+","
				+rsL.getDouble("daozzf")+","
				+rsL.getDouble("zaf")+","
				+rsL.getDouble("qit")+","
				+rsL.getDouble("biaomdj")+","
				+rsL.getDouble("buhsbmdj")
				+")";
				
			con.getInsert(sql);	
		// 插入累计有，本月没有的地区数据
			String sqlB = "";
			sqlB = 
				"select id from yuegsb where to_char(riq,'yyyy-mm')='" + strDate + "' and fenx='本月'\n" +
				"  and gongysb_id=" + rsL.getInt("gongysb_id") + " and pinzb_id=" + rsL.getInt("pinzb_id") + " and jihkjb_id=" + rsL.getInt("jihkjb_id") + " and yunsfsb_id=" + rsL.getInt("yunsfsb_id");

			ResultSetList rsB = con.getResultSetList(sqlB);
			if (!rsB.next()) {
				sql="insert into yuegsb(id,riq,diancxxb_id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,fenx,jingz,biaoz,yingd,kuid,hetj,relzj,liufzj,huifzj,huiffzj,shuifzj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,biaomdj,buhsbmdj) values("
					+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
					+","
					+"to_date('" + strDate + "-01" + "','yyyy-mm-dd'),"
					+visit.getDiancxxb_id() + ","
					+rsL.getInt("gongysb_id") + ","
					+rsL.getInt("jihkjb_id") + ","
					+rsL.getInt("pinzb_id") + ","
					+rsL.getInt("yunsfsb_id") + ","
					+"'本月',"
					+ "0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0"
					+ ")";
				con.getInsert(sql);	
			}
		}
	}


	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		String strDate="";
		strDate=getNianfValue().getValue()+"-"+getYuefValue().getValue()+"-01";
		String strSql="";
		strSql=
			"select yuegsb.id,gongysb.mingc as gongysb_id,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,yunsfsb.mingc as yunsfsb_id,\n" +
			"fenx,jingz,biaoz,yingd,kuid,hetj,relzj,liufzj,huifzj,huiffzj,shuifzj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,biaomdj,buhsbmdj\n" + 
			"from yuegsb,gongysb,jihkjb,pinzb,yunsfsb\n" + 
			"where yuegsb.gongysb_id=gongysb.id and yuegsb.pinzb_id=pinzb.id\n" + 
			"and yuegsb.jihkjb_id=jihkjb.id and yuegsb.yunsfsb_id=yunsfsb.id and diancxxb_id=" +visit.getDiancxxb_id() + "\n" + 
			"and riq=to_date('" + strDate + "','yyyy-mm-dd') order by gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id,fenx";

		JDBCcon con = new JDBCcon();				
		ResultSetList rsl = con.getResultSetList(strSql);
		
		ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		// //设置表名称用于保存
		egu.setTableName("yuegsb");
		// /设置显示列名称
		egu.setWidth("bodyWidth");
		egu.setHeight("bodyHeight");

		egu.getColumn("gongysb_id").setHeader("供货单位");
		egu.getColumn("gongysb_id").setWidth(120);
		egu.getColumn("jihkjb_id").setHeader("计划口径");
		egu.getColumn("jihkjb_id").setWidth(80);
		egu.getColumn("pinzb_id").setHeader("品种");
		egu.getColumn("pinzb_id").setWidth(80);
		egu.getColumn("yunsfsb_id").setHeader("运输方式");
		egu.getColumn("yunsfsb_id").setWidth(80);
		egu.getColumn("fenx").setHeader("分项");
		egu.getColumn("fenx").setWidth(60);
		egu.getColumn("jingz").setHeader("净重");
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("biaoz").setHeader("标重");
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingd").setHeader("盈吨");
		egu.getColumn("yingd").setWidth(60);
		egu.getColumn("kuid").setHeader("亏吨");
		egu.getColumn("kuid").setWidth(60);
		egu.getColumn("hetj").setHeader("合同价");
		egu.getColumn("hetj").setWidth(60);
		egu.getColumn("relzj").setHeader("热量折价");
		egu.getColumn("relzj").setWidth(60);
		egu.getColumn("liufzj").setHeader("硫分折价");
		egu.getColumn("liufzj").setWidth(60);
		egu.getColumn("huifzj").setHeader("灰分折价");
		egu.getColumn("huifzj").setWidth(60);
		egu.getColumn("huiffzj").setHeader("挥发分折价");
		egu.getColumn("huiffzj").setWidth(70);
		egu.getColumn("shuifzj").setHeader("水分折价");
		egu.getColumn("shuifzj").setWidth(60);
		egu.getColumn("meij").setHeader("煤价");
		egu.getColumn("meij").setWidth(60);
		egu.getColumn("meijs").setHeader("煤价税");
		egu.getColumn("meijs").setWidth(60);
		egu.getColumn("yunj").setHeader("运价");
		egu.getColumn("yunj").setWidth(60);
		egu.getColumn("yunjs").setHeader("运价税");
		egu.getColumn("yunjs").setWidth(60);
		egu.getColumn("daozzf").setHeader("到站杂费");
		egu.getColumn("daozzf").setWidth(60);
		egu.getColumn("zaf").setHeader("杂费");
		egu.getColumn("zaf").setWidth(60);
		egu.getColumn("qit").setHeader("其他");
		egu.getColumn("qit").setWidth(60);
		egu.getColumn("biaomdj").setHeader("标煤单价");
		egu.getColumn("biaomdj").setWidth(90);
		egu.getColumn("buhsbmdj").setHeader("不含税标煤单价");
		egu.getColumn("buhsbmdj").setWidth(100);

		egu.setDefaultsortable(false);             
		
		egu.getColumn("gongysb_id").update=false;
		egu.getColumn("jihkjb_id").update=false;
		egu.getColumn("pinzb_id").update=false;
		egu.getColumn("yunsfsb_id").update=false;
		egu.getColumn("fenx").update=false;
		
		
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
		
		egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("刷新","function (){document.getElementById('RefreshButton').click()}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("生成","function (){document.getElementById('CreateButton').click()}").getScript()+"}");
//		egu.addToolbarItem("{"+new GridButton("删除","function (){document.getElementById('DelButton').click()}").getScript()+"}");
//		egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton");
//		egu.addToolbarItem("{"+new GridButton("保存","function (){document.getElementById('SaveButton').click()}").getScript()+"}");
		String str=" var url = 'http://'+document.location.host+document.location.pathname;"+
        		"var end = url.indexOf(';');"+
                 "url = url.substring(0,end);"+
                 "url = url + '?service=page/' + 'MonthReport&lx=yuegsb';" +
                 " window.open(url,'newWin');";
		egu.addToolbarItem("{"+new GridButton("打印","function (){"+str+"}").getScript()+"}");

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
