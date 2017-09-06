package com.zhiren.dc.diaoygl.meizyccl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
/**
 * 
 * @author Rock
 *
 */


/* 作者:王总兵
 * 日期:2010-5-16 16:19:56
 * 描述:修改保存的js,只属于主要的数据即可保存,不需要全部输入
 * 
 */
public abstract class Meizyclr extends BasePage {
    // 进行页面提示信息的设置
	private String _msg;
	public String getMsg() { 
        if (_msg == null) {
            _msg = "";
        }
        return _msg;
    }
	public void setMsg(String _value) {
	    _msg = MainGlobal.getExtMessageBox(_value, false);
	}
    protected void initialize() {
        super.initialize();
        _msg = "";
    }
    // 页面变化记录
    private String Change;
    public String getChange() {
    	return Change;
    }
    public void setChange(String change) {
    	Change = change;
    }
    // 页面初始化
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if(!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			visit.setActivePageName(this.getPageName().toString());
			
			setGongmdwModel(null);
			setGongmdwValue(null);
			
			setGongmkdModel(null);
			setGongmkdValue(null);
			
			setYunsdwModel(null);
			setYunsdwValue(null);
			
			setYicqkModel(null);
			setYicqkValue(null);
			
			visit.setDropDownBean5(null);
			visit.setProSelectionModel5(null);
			
			visit.setDropDownBean6(null);
			visit.setProSelectionModel6(null);
			
			setBianhModel(null);
			setBianhValue(null);
			
			visit.setDefaultTree(null);
			setTreeid(null);
		}
		getToolbars();
	}
	
    // 日期控件
	boolean riqichange = false;

	private String riqi;

	public String getRiqi() {
		if (riqi == null || riqi.equals("")) {
			riqi = DateUtil.FormatDate(new Date());
		}
		return riqi;
	}

	public void setRiqi(String riqi) {

		if (this.riqi != null && !this.riqi.equals(riqi)) {
			this.riqi = riqi;
			riqichange = true;
		}
	}
	
	// 记录meizycb的id
	private String meizycb_id;
	
	public String getMeizycb_id() {
		if (meizycb_id == null || meizycb_id.equals("")) {
			meizycb_id = "0";
		}
		return meizycb_id;
	}
	
	public void setMeizycb_id(String id) {
		meizycb_id = id;
	}
	
	private IDropDownBean getIDropDownBean(IPropertySelectionModel _Model,String value){		
		IDropDownBean _Value=null;	
		for(int i=0;i<_Model.getOptionCount();i++){			
			if(((IDropDownBean)_Model.getOption(i)).getValue().equals(value)){				
				_Value = ((IDropDownBean)_Model.getOption(i));
				break;
			}
		}		
		return _Value;
	}
	
    public String getData() {
    	JDBCcon con = new JDBCcon();
    	ResultSetList rsl = null;
    	IDropDownBean _Value = null;
    	String Data = "";
    	String id = "";
    	String gongmdw = "";       // 供煤单位
    	String gongmkd = "";       // 供煤矿点
    	String yunsdw = "";        // 运输单位
    	String yunsclch_hhc = "";  // 运输车辆车号(含火车)
    	double shiszl = 0.0;      // 实收重量
    	String daocsj = "";        // 到厂时间
    	String yicqk = "";         // 异常情况
    	String ranlbqz = "";       // 燃料部签字
    	String yicms = "";         // 异常描述
    	String huanbbqz = "";      // 环保部签字
    	double yicds = 0.0;       // 异常吨数
    	String gongmdwqz = "";     // 供煤单位签字
    	String faxr = "";          // 发现人
    	String yunsdwhsj = "";     // 运输单位或司机
    	String meizzgyj = "";      // 煤质主管意见
    	String meizzgqz = "";      // 煤质主管签字
    	String lingdyj = "";       // 领导意见
    	double lingdqrkd = 0.0;   // 领导确认扣吨
    	String lingdqz = "";       // 领导签字
    	String sql = "select yc.id, gys.mingc as gongmdw, mk.mingc as gongmkd, ys.mingc as yunsdw, yunsclch_hhc, \n" +
    			     "       shiszl, to_char(daocsj, 'yyyy-mm-dd') as daocsj, yicqk, ranlbqz, yicms, huanbbqz, yicds, gongmdwqz, \n" +
    			     "       faxr, yunsdwhsj, meizzgyj, meizzgqz, lingdyj, lingdqrkd, lingdqz" +
    			     "  from meizycb yc, gongysb gys, meikxxb mk, yunsdwb ys \n" +
    			     " where to_char(daocsj, 'yyyy') = '"+getNianf()+"' " +
    			     "   and to_char(daocsj, 'mm') = '"+getYuef()+"' and yc.diancxxb_id = " + getTreeid() + 
    			     "   and bianh = '" + getBianh() +
    			     "'   and yc.gongysb_id = gys.id\n" +
    			     "   and yc.meikxxb_id = mk.id\n" + 
    			     "   and yc.yunsdwb_id = ys.id";

    	rsl = con.getResultSetList(sql);
    	if (rsl.next()) {
    		id = rsl.getString("id");
    		setMeizycb_id(id);
    		gongmdw = rsl.getString("gongmdw");
    		_Value = getIDropDownBean(this.getGongmdwModel(), gongmdw);
    		if(_Value != null){    			
    			this.setGongmdwValue(_Value);
    		}    		
    		gongmkd = rsl.getString("gongmkd");
    		_Value = getIDropDownBean(this.getGongmkdModel(), gongmkd);
    		if(_Value != null){    			
    			this.setGongmkdValue(_Value);
    		}      		
    		yunsdw = rsl.getString("yunsdw");
    		_Value = getIDropDownBean(this.getYunsdwModel(), yunsdw);
    		if(_Value != null){    			
    			this.setYunsdwValue(_Value);
    		}
    		yunsclch_hhc = rsl.getString("yunsclch_hhc");
    		shiszl = rsl.getDouble("shiszl");
    		daocsj = rsl.getString("daocsj");
    		yicqk = rsl.getString("yicqk");
    		ranlbqz = rsl.getString("ranlbqz");
    		yicms = rsl.getString("yicms");
    		huanbbqz = rsl.getString("huanbbqz");
    		yicds = rsl.getDouble("yicds");
    		gongmdwqz = rsl.getString("gongmdwqz");
    		faxr = rsl.getString("faxr");
    		yunsdwhsj = rsl.getString("yunsdwhsj");
    		meizzgyj = rsl.getString("meizzgyj");
    		meizzgqz = rsl.getString("meizzgqz");
    		lingdyj = rsl.getString("lingdyj");
    		lingdqrkd = rsl.getDouble("lingdqrkd");
    		lingdqz = rsl.getString("lingdqz");
    	} else {
    		setMeizycb_id("0");
    	}
    	
        // 煤质异常供煤单位和运输单位情况
    	// 1、供煤单位
		ComboBox cb1_cbgmdw = new ComboBox();
		cb1_cbgmdw.setTransform("GONGMDW");
		cb1_cbgmdw.setId("cb1_cbgmdw");
		cb1_cbgmdw.setValue(gongmdw);
		cb1_cbgmdw.setLazyRender(true);
		cb1_cbgmdw.setEditable(false);
		cb1_cbgmdw.setWidth(120);
		cb1_cbgmdw.listeners = 
			"specialkey:function(field, e) {\n" +
			"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
			"\t\t    cb3_yunsdw.focus();\n" + 
			"\t  }\n" + 
			"}";
    	Data = Data + cb1_cbgmdw.getScript() + "\n";
    	
        // 2、供煤矿点
    	ComboBox cb2_gongmkd = new ComboBox();
    	cb2_gongmkd.setTransform("GONGMKD");
    	cb2_gongmkd.setId("cb2_gongmkd");
    	cb2_gongmkd.setValue(gongmkd);
    	cb2_gongmkd.setLazyRender(true);
    	cb2_gongmkd.setEditable(false);
    	cb2_gongmkd.setWidth(120);
    	cb2_gongmkd.listeners = 
			"specialkey:function(field, e) {\n" +
			"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
			"\t\t    t2x_daocsj.focus();\n" + 
			"\t  }\n" + 
			"}";
    	Data = Data + cb2_gongmkd.getScript() + "\n";
    	
        // 3、运输单位
    	ComboBox cb3_yunsdw = new ComboBox();
    	cb3_yunsdw.setTransform("YUNSDW");
    	cb3_yunsdw.setId("cb3_yunsdw");
    	cb3_yunsdw.setValue(yunsdw);
    	cb3_yunsdw.setLazyRender(true);
    	cb3_yunsdw.setEditable(false);
    	cb3_yunsdw.setWidth(120);
    	cb3_yunsdw.listeners = 
			"specialkey:function(field, e) {\n" +
			"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
			"\t\t    t1x_yunsclch_hhc.focus();\n" + 
			"\t  }\n" + 
			"}";
    	Data = Data + cb3_yunsdw.getScript() + "\n";
    	
        // 4、运输车辆车号(含火车)
    	TextField t1x_yunsclch_hhc = new TextField();
    	t1x_yunsclch_hhc.setFieldLabel("运输车辆车号(含火车)");
    	t1x_yunsclch_hhc.setId("t1x_yunsclch_hhc");
    	t1x_yunsclch_hhc.setValue(yunsclch_hhc);
    	t1x_yunsclch_hhc.setWidth(350);
    	t1x_yunsclch_hhc.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    cb2_gongmkd.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t1x_yunsclch_hhc.getScript() + "\n";
    	
        // 5、实收重量
    	NumberField nx1_shiszl = new NumberField();
    	nx1_shiszl.setFieldLabel("实收重量");
    	nx1_shiszl.setId("nx1_shiszl");
    	nx1_shiszl.setValue(shiszl + "");
    	nx1_shiszl.setWidth(120);
    	nx1_shiszl.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    cb4_yicqk.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + nx1_shiszl.getScript() + "\n";
  
        // 6、到厂时间
    	DateField t2x_daocsj = new DateField();
    	t2x_daocsj.setFieldLabel("到厂时间");
    	t2x_daocsj.setId("t2x_daocsj");
    	t2x_daocsj.setValue(daocsj);
    	t2x_daocsj.setWidth(120);
    	t2x_daocsj.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    nx1_shiszl.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t2x_daocsj.getScript() + "\n";
    	    	
        // 煤质查处过程
    	//1、异常情况
//    	ComboBox cb4_yicqk = new ComboBox();
//    	cb4_yicqk.setTransform("YICQK");
//    	cb4_yicqk.setId("cb4_yicqk");
//    	cb4_yicqk.setValue(yicqk);
//    	cb4_yicqk.setLazyRender(true);
//    	cb4_yicqk.setEditable(false);
//    	cb4_yicqk.setWidth(120);
//    	Data = Data + cb4_yicqk.getScript() + "\n";
    	
    	
    	Data = Data + 
    	"\t            cb4_yicqk= new Ext.zr.select.Selectcombo({\n" + 
    	"\t\t\t\t          multiSelect:true,\n" + 
    	"\t\t\t            //selectOnFocus:true,\n" + 
    	"    \t\t          transform:'YICQK',\n" + 
    	"    \t\t          lazyRender:true,\n" + 
    	"    \t\t\t        triggerAction:'all',\n" + 
    	"    \t\t\t        typeAhead:true,\n" + 
    	"    \t\t\t        width:120,\n" +
    	"				   value:'"+yicqk+"',\n" +
    	"             \t\t listeners :{'blur':function(){\n" + 
    	"    \t\t\t\t          var val;\n" + 
    	"        \t\t\t\t          if(this.getRawValue().substr(0,1)==','){\n" + 
    	"        \t\t\t\t        \t    val=this.getRawValue().substring(1,this.getRawValue().length);\n" + 
    	"        \t\t\t\t          }else{\n" + 
    	"        \t\t\t\t       \t\t   val=this.getRawValue();\n" + 
    	"        \t\t\t\t          }\n" + 
    	"                    \t\t\t this.setValue(val);\n" + 
    	"        \t\t\t\t      },\n" + 
    	"					   specialkey:function(field, e) { \n" +
		"					       if (e.getKey() == Ext.EventObject.ENTER) { \n" + 
		"	                           t4x_yicms.focus(); \n" +
	    "						   } \n" +
	    "					   } \n" +
    	"\t\t\t            },\n" + 
    	"\t\t        \t      forceSelection:true\n" + 
    	"\t\t\t         })\n" + 
    	"          ";

    	    	
        // 2、燃料部签字
    	TextField t3x_ranlbqz = new TextField();
    	t3x_ranlbqz.setFieldLabel("燃料部签字");
    	t3x_ranlbqz.setId("t3x_ranlbqz");
    	
    		t3x_ranlbqz.setValue(ranlbqz);
    	
    	t3x_ranlbqz.setWidth(120);
    	t3x_ranlbqz.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t5x_huanbbqz.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t3x_ranlbqz.getScript() + "\n";
    	
        // 3、异常描述
    	TextField t4x_yicms = new TextField();
    	t4x_yicms.setFieldLabel("异常描述");
    	t4x_yicms.setId("t4x_yicms");
    	t4x_yicms.setValue(yicms);
    	t4x_yicms.setWidth(350);
    	t4x_yicms.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    nx2_yicds.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t4x_yicms.getScript() + "\n";
    	
        // 4、环保部签字
    	TextField t5x_huanbbqz = new TextField();
    	t5x_huanbbqz.setFieldLabel("环保部签字");
    	t5x_huanbbqz.setId("t5x_huanbbqz");
    	t5x_huanbbqz.setValue(huanbbqz);
    	t5x_huanbbqz.setWidth(120);
    	t5x_huanbbqz.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t6x_gongmdwqz.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t5x_huanbbqz.getScript() + "\n";
    	
        // 5、异常吨数
    	NumberField nx2_yicds = new NumberField();
    	nx2_yicds.setFieldLabel("异常吨数");
    	nx2_yicds.setId("nx2_yicds");
    	nx2_yicds.setValue(yicds + "");
    	nx2_yicds.setWidth(120);
    	nx2_yicds.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t7x_faxr.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + nx2_yicds.getScript() + "\n";
  
        // 6、供煤单位签字
    	TextField t6x_gongmdwqz = new TextField();
    	t6x_gongmdwqz.setFieldLabel("供煤单位签字");
    	t6x_gongmdwqz.setId("t6x_gongmdwqz");
    	t6x_gongmdwqz.setValue(gongmdwqz);
    	t6x_gongmdwqz.setWidth(120);
    	t6x_gongmdwqz.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t8x_yunsdwhsj.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t6x_gongmdwqz.getScript() + "\n";
    	
        // 7、发现人
    	TextField t7x_faxr = new TextField();
    	t7x_faxr.setFieldLabel("发现人");
    	t7x_faxr.setId("t7x_faxr");
    	t7x_faxr.setValue(faxr);
    	t7x_faxr.setWidth(120);
    	t7x_faxr.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t9x_meizzgyj.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t7x_faxr.getScript() + "\n";
    	
        // 8、运输单位或司机
    	TextField t8x_yunsdwhsj = new TextField();
    	t8x_yunsdwhsj.setFieldLabel("运输单位或司机");
    	t8x_yunsdwhsj.setId("t8x_yunsdwhsj");
    	t8x_yunsdwhsj.setValue(yunsdwhsj);
    	t8x_yunsdwhsj.setWidth(120);
    	t8x_yunsdwhsj.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t10x_meizzgqz.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t8x_yunsdwhsj.getScript() + "\n";
    	
        // 9、煤质主管意见
    	TextField t9x_meizzgyj = new TextField();
    	t9x_meizzgyj.setFieldLabel("煤质主管意见");
    	t9x_meizzgyj.setId("t9x_meizzgyj");
    	t9x_meizzgyj.setValue(meizzgyj);
    	t9x_meizzgyj.setWidth(350);
    	t9x_meizzgyj.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t11x_lingdyj.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t9x_meizzgyj.getScript() + "\n";	
    	
        // 10、煤质主管签字
    	TextField t10x_meizzgqz = new TextField();
    	t10x_meizzgqz.setFieldLabel("煤质主管签字");
    	t10x_meizzgqz.setId("t10x_meizzgqz");
    	t10x_meizzgqz.setValue(meizzgqz);
    	t10x_meizzgqz.setWidth(120);
    	t10x_meizzgqz.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t12x_lingdqz.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t10x_meizzgqz.getScript() + "\n";
    	
        // 11、领导意见
    	TextField t11x_lingdyj = new TextField();
    	t11x_lingdyj.setFieldLabel("领导意见");
    	t11x_lingdyj.setId("t11x_lingdyj");
    	t11x_lingdyj.setValue(lingdyj);
    	t11x_lingdyj.setWidth(350);
    	t11x_lingdyj.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    nx3_lingdqrkd.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + t11x_lingdyj.getScript() + "\n";
    	
        // 12、领导确认扣吨
    	NumberField nx3_lingdqrkd = new NumberField();
    	nx3_lingdqrkd.setFieldLabel("领导确认扣吨");
    	nx3_lingdqrkd.setId("nx3_lingdqrkd");
    	nx3_lingdqrkd.setValue(lingdqrkd + "");
    	nx3_lingdqrkd.setWidth(120);
    	nx3_lingdqrkd.listeners = 
    		"specialkey:function(field, e) {\n" +
    		"    if (e.getKey() == Ext.EventObject.ENTER) {\n" + 
    		"\t\t    t3x_ranlbqz.focus();\n" + 
    		"\t  }\n" + 
    		"}";
    	Data = Data + nx3_lingdqrkd.getScript() + "\n";
    	
        // 13、领导签字
    	TextField t12x_lingdqz = new TextField();
    	t12x_lingdqz.setFieldLabel("领导签字");
    	t12x_lingdqz.setId("t12x_lingdqz");
    	t12x_lingdqz.setValue(lingdqz);
    	t12x_lingdqz.setWidth(120);
    	Data = Data + t12x_lingdqz.getScript() + "\n";
    	
    	rsl.close();
    	con.Close();
    	return Data;
    }
    
    private void getToolbars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		tb1.addText(new ToolbarText("年份:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));

		tb1.addText(new ToolbarText("月份:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",
				ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(),
				"-1".equals(getTreeid()) ? null : getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(120);
		tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
				.parseLong(getTreeid())));

		ToolbarButton tb2 = new ToolbarButton(null, null,
				"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);

		tb1.addText(new ToolbarText("单位:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		tb1.addText(new ToolbarText("编号:"));
		ComboBox bianh = new ComboBox();
		bianh.setTransform("BIANH");
		bianh.setWidth(90);
		bianh.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(bianh);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton refurbish = new ToolbarButton(null, "刷新", "function(){document.Form0.submit();}");
		ToolbarButton insert = new ToolbarButton(null, "添加", "function (){document.getElementById('InsertButton').click();}");
		ToolbarButton delete = new ToolbarButton(null, "删除", getBtnHandlerScript("DeleteButton"));
		ToolbarButton save = new ToolbarButton(null, "保存", "function(){ if(SaveCheck()){"
		+ " info = 'cb1_cbgmdw~'+ cb1_cbgmdw.getValue() + ';"
            + "cb2_gongmkd~'+ cb2_gongmkd.getValue() + ';"
			+ "cb3_yunsdw~' + cb3_yunsdw.getValue() + ';"
			+ "t1x_yunsclch_hhc~' + t1x_yunsclch_hhc.getValue() + ';"
			+ "nx1_shiszl~' + nx1_shiszl.getValue() + ';"
			+ "t2x_daocsj~' + t2x_daocsj.value + ';"
			+ "cb4_yicqk~' + cb4_yicqk.getRawValue() + ';"
			+ "t3x_ranlbqz~' +' '+ t3x_ranlbqz.getValue() + ';"
			+ "t4x_yicms~' +' '+ t4x_yicms.getValue() + ';"
			+ "t5x_huanbbqz~'+' ' + t5x_huanbbqz.getValue() + ';"
			+ "nx2_yicds~' +' '+ nx2_yicds.getValue() + ';"
			+ "t6x_gongmdwqz~' +' '+ t6x_gongmdwqz.getValue() + ';"
			+ "t7x_faxr~' +' '+ t7x_faxr.getValue() + ';"
			+ "t8x_yunsdwhsj~' +' '+ t8x_yunsdwhsj.getValue() + ';"
			+ "t9x_meizzgyj~' +' '+ t9x_meizzgyj.getValue() + ';"
			+ "t10x_meizzgqz~' +' '+ t10x_meizzgqz.getValue() + ';"  
			+ "t11x_lingdyj~' +' '+ t11x_lingdyj.getValue() + ';"
			+ "nx3_lingdqrkd~' +' '+ nx3_lingdqrkd.getValue() + ';"
			+ "t12x_lingdqz~' +' '+ t12x_lingdqz.getValue();"
			+ " var Cobj = document.getElementById('CHANGE');"
			+ " Cobj.value = info ;"
			+ " document.getElementById('SaveButton').click();}}");

		tb1.addItem(refurbish);
		tb1.addItem(insert);
		tb1.addItem(delete);
		tb1.addItem(save);
		setToolbar(tb1);
	}
    
    // 记录编号
	public String getBianh() {
		return getBianhValue().getValue();
	}
    
    public String getNianf() {
		long intyear;
		if (getNianfValue() == null) {
			intyear = DateUtil.getYear(new Date());
		} else {
			intyear = getNianfValue().getId();
		}
		return intyear + "";
	}

	public String getYuef() {
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
		return StrMonth;
	}
    
    // 按钮事件处理
	private boolean _RefurbishChick = false;
	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	private boolean _InsertChick = false;
	public void InsertButton(IRequestCycle cycle) {
		_InsertChick = true;
	}
	
	private boolean _DeleteChick = false;
	public void DeleteButton(IRequestCycle cycle) {
		_DeleteChick = true;
	}
	
	private boolean _SaveChick = false;
    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }
    
    public void submit(IRequestCycle cycle) {
    	if (_RefurbishChick) {
    		_RefurbishChick = false;
    		
    	}
    	
    	if (_InsertChick) {
    		_InsertChick = false;
    		Insert();
    		
    	}
    	
    	if (_DeleteChick) {
    		_DeleteChick = false;
    		Delete();
    		
    	}
    	
    	if (_SaveChick) {
    		_SaveChick = false;
    		Save();
        }
    	getData();
    }
    
    // 添加
    private void Insert() {
    	JDBCcon con = new JDBCcon();
    	ResultSetList rsl = null;
    	String bianh = "";
    	String sql = "select max(bianh) as bianh from meizycb where diancxxb_id = " + getTreeid() + " \n" +
    	             "and to_char(daocsj, 'yyyy') = " + getNianf() + " and to_char(daocsj, 'mm') = " + getYuef();
    	rsl = con.getResultSetList(sql);
    	if (rsl.next()) {
    		if (rsl.getString("bianh") == null || rsl.getString("bianh").equals("")) {
    			bianh = getRiqi().substring(0, 4) + getRiqi().substring(5, 7) + "001";
    		} else {
    			bianh = Long.parseLong(rsl.getString("bianh")) + 1 + "";
    		}
    	}
    	this.getIBianhModels(bianh);
    	this.setBianhValue(null);
    	setMeizycb_id("0");
    	rsl.close();
    	con.Close();
    }
    
	// 删除
    private void Delete() {
    	JDBCcon con = new JDBCcon();
    	String sql = "delete from meizycb where to_char(daocsj, 'yyyy') = '"+getNianf()+"' and to_char(daocsj, 'mm') = \n" +
    			     "'"+getYuef()+"' and diancxxb_id = " + getTreeid() + " and bianh = '" + getBianh()+"'";
    	con.getDelete(sql);
    	this.getIBianhModels();
    	setMsg("删除成功！");
    	con.Close();
    }
    
    public String getBtnHandlerScript(String btnName) {
		// 按钮的script
		StringBuffer btnsb = new StringBuffer();
		String cnDate = getNianf() + "年" + getYuef() + "月" + getBianh() + "号数据";
		btnsb.append("function (){Ext.MessageBox.confirm('提示信息','");
		if (btnName.endsWith("DeleteButton")) {
			btnsb.append("是否删除").append(cnDate).append("？");
		}
		btnsb.append("',function(btn){if(btn == 'yes'){").append(
				"document.getElementById('").append(btnName).append(
				"').click()").append("}; // end if \n").append("});}");
		return btnsb.toString();
	}
    
    // 保存
	private void Save() {
		
		String change[] = getChange().split(";");
		Visit visit = (Visit)this.getPage().getVisit();
    	JDBCcon con = new JDBCcon();
    	String sql = "";
    	
    	String id = "";
    	String gongmdw = "";       // 供煤单位
    	String gongmkd = "";       // 供煤矿点
    	String yunsdw = "";        // 运输单位
    	String yunsclch_hhc = "";  // 运输车辆车号(含火车)
    	double shiszl = 0.0;      // 实收重量
    	String daocsj = "";        // 到厂时间
    	String yicqk = "";         // 异常情况
    	String ranlbqz = "";       // 燃料部签字
    	String yicms = "";         // 异常描述
    	String huanbbqz = "";      // 环保部签字
    	double yicds = 0.0;       // 异常吨数
    	String gongmdwqz = "";     // 供煤单位签字
    	String faxr = "";          // 发现人
    	String yunsdwhsj = "";     // 运输单位或司机
    	String meizzgyj = "";      // 煤质主管意见
    	String meizzgqz = "";      // 煤质主管签字
    	String lingdyj = "";       // 领导意见
    	double lingdqrkd = 0.0;   // 领导确认扣吨
    	String lingdqz = "";       // 领导签字
    	for (int i = 0; i < change.length; i++) {
    		String[] ch = change[i].split("~");
    		if ("cb1_cbgmdw".equals(ch[0])) {
    			gongmdw = ch[1];
    		}
    		if ("cb2_gongmkd".equals(ch[0])) {
    			gongmkd = ch[1];
    		}
    		if ("cb3_yunsdw".equals(ch[0])) {
    			yunsdw = ch[1];
    		}
    		if ("t1x_yunsclch_hhc".equals(ch[0])) {
    			yunsclch_hhc = ch[1];
    		}
    		if ("nx1_shiszl".equals(ch[0])) {
    			shiszl = Double.parseDouble(ch[1]);
    		}
    		if ("t2x_daocsj".equals(ch[0])) {
    			daocsj = ch[1];
    		}
    		if ("cb4_yicqk".equals(ch[0])) {
    			yicqk = ch[1];
    		}
    		if ("t3x_ranlbqz".equals(ch[0])) {
    			ranlbqz = ch[1];
    		}
    		if ("t4x_yicms".equals(ch[0])) {
    			yicms = ch[1];
    		}
    		if ("t5x_huanbbqz".equals(ch[0])) {
    			huanbbqz = ch[1];
    		}
    		if ("nx2_yicds".equals(ch[0])) {
    			yicds = Double.parseDouble(ch[1]);
    		}
    		if ("t6x_gongmdwqz".equals(ch[0])) {
    			gongmdwqz = ch[1];
    		}
    		if ("t7x_faxr".equals(ch[0])) {
    			faxr = ch[1];
    		}
    		if ("t8x_yunsdwhsj".equals(ch[0])) {
    			yunsdwhsj = ch[1];
    		}
    		if ("t9x_meizzgyj".equals(ch[0])) {
    			meizzgyj = ch[1];
    		}
    		if ("t10x_meizzgqz".equals(ch[0])) {
    			meizzgqz = ch[1];
    		}
    		if ("t11x_lingdyj".equals(ch[0])) {
    			lingdyj = ch[1];
    		}
    		if ("nx3_lingdqrkd".equals(ch[0])) {
    			lingdqrkd = Double.parseDouble(ch[1]);
    		}
    		if ("t12x_lingdqz".equals(ch[0])) {
    			lingdqz = ch[1];
    		}
    	}
    	id = getMeizycb_id();
    	if ("0".equals(id)) {
    		sql = "insert into meizycb values(getNewId("+visit.getDiancxxb_id()+"), "+gongmdw+", "+gongmkd+", \n" +
    		      yunsdw+", '"+yunsclch_hhc+"', "+shiszl+", to_date('"+daocsj+"', 'yyyy-mm-dd'), '"+yicqk+"', '"+ranlbqz+"', \n'" +
    		      yicms+"', '"+huanbbqz+"', "+yicds+", '"+gongmdwqz+"', '"+faxr+"', '"+yunsdwhsj+"', '"+meizzgyj+"', \n'" +
    		      meizzgqz+"', '"+lingdyj+"', '"+lingdqz+"', '"+getBianh()+"', "+getTreeid()+", "+lingdqrkd+")";
    		con.getInsert(sql);
    	} else {
    		sql = "update meizycb set gongysb_id = "+gongmdw+", meikxxb_id = "+gongmkd+", yunsdwb_id = "+yunsdw+", \n"+
    			  "yunsclch_hhc = '"+yunsclch_hhc+"', shiszl = "+shiszl+", daocsj = to_date('"+daocsj+"', 'yyyy-mm-dd'), \n" +
    			  "yicqk = '"+yicqk+"', ranlbqz = '"+ranlbqz+"', yicms = '"+yicms+"', huanbbqz = '"+huanbbqz+"', yicds = \n" +
    			  yicds+", gongmdwqz = '"+gongmdwqz+"', faxr = '"+faxr+"', yunsdwhsj = '"+yunsdwhsj+"', meizzgyj = '"+meizzgyj+
    			  "', meizzgqz = '"+meizzgqz+"', lingdyj = '"+lingdyj+"', lingdqz = '"+lingdqz+"', bianh = '"+getBianh()+"', " +
    			  "diancxxb_id = "+getTreeid()+", lingdqrkd = "+lingdqrkd+" where id = " + id;
    		con.getUpdate(sql);
    	}
    	//this.getIBianhModels();
    	setMsg("保存成功！");
    	con.Close();
	}
	
    // 供煤单位
	public IDropDownBean getGongmdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
			if (getGongmdwModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getGongmdwModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean1();
	}

	public void setGongmdwValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean1(Value);
	}

	public void setGongmdwModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel1(value);
	}

	public IPropertySelectionModel getGongmdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
			getIGongmdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel1();
	}

	public void getIGongmdwModels() {		
		String sql = "select id, mingc from gongysb where leix=1 order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel1(new IDropDownModel(sql));
	}
	
	// 供煤矿点
	public IDropDownBean getGongmkdValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			if (getGongmkdModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getGongmkdModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setGongmkdValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongmkdModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongmkdModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIGongmkdModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void getIGongmkdModels() {		
		String sql = "select id, mingc from meikxxb order by mingc";
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql));
	}
	
    // 运输单位
	public IDropDownBean getYunsdwValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			if (getYunsdwModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getYunsdwModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setYunsdwValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setYunsdwModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getYunsdwModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getIYunsdwModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void getIYunsdwModels() {		
		String sql = "select id, mingc from yunsdwb order by id";
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));
	}
	
    //异常情况
	public IDropDownBean getYicqkValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getYicqkModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setYicqkValue(IDropDownBean Value) {

		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setYicqkModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getYicqkModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getYicqkModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void getYicqkModels() {
		String sql = "select id, mingc\n" +
					 "  from (select 1 as id, '矸石夹层' as mingc\n" + 
					 "          from dual\n" + 
					 "        union\n" + 
					 "        select 2 as id, '劣质煤夹层' as mingc\n" + 
					 "          from dual\n" + 
					 "        union\n" + 
					 "        select 3 as id, '杂物' as mingc\n" + 
					 "          from dual\n" + 
					 "        union\n" + 
					 "        select 4 as id, '多种煤质' as mingc\n" + 
					 "          from dual\n" + 
					 "        union\n" + 
					 "        select 5 as id, '煤质较湿' as mingc\n" + 
					 "          from dual\n" + 
					 "        union\n" + 
					 "        select 6 as id, '其他' as mingc from dual)\n" + 
					 " order by id";

		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(sql));
		return;
	}
	
    // 电厂名称
	public boolean _diancmcchange = false;

	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if (_DiancmcValue == null) {
			_DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
		}
		return _DiancmcValue;
	}

	public void setDiancmcValue(IDropDownBean Value) {
		long id = -2;
		if (_DiancmcValue != null) {
			id = _DiancmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_diancmcchange = true;
			} else {
				_diancmcchange = false;
			}
		}
		_DiancmcValue = Value;
	}

	private IPropertySelectionModel _IDiancmcModel;

	public void setIDiancmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIDiancmcModel() {
		if (_IDiancmcModel == null) {
			getIDiancmcModels();
		}
		return _IDiancmcModel;
	}

	public void getIDiancmcModels() {
		String sql = "";
		sql = "select d.id,d.mingc from diancxxb d order by d.mingc desc";

		_IDiancmcModel = new IDropDownModel(sql);
	}
	
    // 年份
	public IPropertySelectionModel getNianfModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {
			getNianfModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public IDropDownBean getNianfValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
				Object obj = getNianfModel().getOption(i);
				if (DateUtil.getYear(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean5((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();

	}

	public boolean nianfchanged;

	public void setNianfValue(IDropDownBean Value) {
		if (((Visit) getPage().getVisit()).getDropDownBean5() != Value) {
			nianfchanged = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel5(new IDropDownModel(listNianf));
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel5(_value);
	}

	// 月份
	public boolean Changeyuef = false;

	public IPropertySelectionModel getYuefModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {
			getYuefModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public IDropDownBean getYuefValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
				Object obj = getYuefModel().getOption(i);
				if (DateUtil.getMonth(new Date()) == ((IDropDownBean) obj)
						.getId()) {
					((Visit) getPage().getVisit())
							.setDropDownBean6((IDropDownBean) obj);
					break;
				}
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYuefValue(IDropDownBean Value) {
		long id = -2;
		if (((Visit) getPage().getVisit()).getDropDownBean6() != null) {
			id = getYuefValue().getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				Changeyuef = true;
			} else {
				Changeyuef = false;
			}
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);

	}

	public IPropertySelectionModel getYuefModels() {
		List listYuef = new ArrayList();
		// listYuef.add(new IDropDownBean(-1,"请选择"));
		for (int i = 1; i < 13; i++) {
			listYuef.add(new IDropDownBean(i, String.valueOf(i)));
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel6(new IDropDownModel(listYuef));
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setYuefModel(IPropertySelectionModel _value) {
		((Visit) getPage().getVisit()).setProSelectionModel6(_value);
	}
	
    // 编号下拉框
	public IDropDownBean getBianhValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean7()==null){
			if (getBianhModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean7((IDropDownBean)getBianhModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean7();
	}

	public void setBianhValue(IDropDownBean Value) {
		((Visit)getPage().getVisit()).setDropDownBean7(Value);
	}

	public void setBianhModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getBianhModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel7() == null) {
			getIBianhModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel7();
	}

	public void getIBianhModels() {		
		String sql = "select id, bianh from meizycb order by bianh";
		((Visit)getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql, "请选择"));
	}
	
    private void getIBianhModels(String bianh) {
		// TODO 自动生成方法存根
    	
    	String sql = "select id, bianh from meizycb order by bianh";
		((Visit)getPage().getVisit()).setProSelectionModel7(new IDropDownModel(sql, bianh));
	}
	
	public String getTreeid() {
		String treeid = ((Visit) getPage().getVisit()).getString2();
		if (treeid == null || treeid.equals("")) {
			((Visit) getPage().getVisit()).setString2(String
					.valueOf(((Visit) this.getPage().getVisit())
							.getDiancxxb_id()));
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
	
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}

	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
}
