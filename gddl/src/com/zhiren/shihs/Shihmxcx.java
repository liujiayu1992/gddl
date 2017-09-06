package com.zhiren.shihs;

import java.sql.ResultSet;
import java.util.Date;

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
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Shihmxcx extends BasePage implements PageValidateListener {
	
	protected void initialize() {
		super.initialize();
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// 报表初始设置
	private int _CurrentPage = -1;

	public int getCurrentPage() {
		return _CurrentPage;
	}

	public void setCurrentPage(int _value) {
		_CurrentPage = _value;
	}

	private int _AllPages = -1;

	public int getAllPages() {
		return _AllPages;
	}

	public void setAllPages(int _value) {
		_AllPages = _value;
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
	
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// 工具栏使用的方法
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getRiqs() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiqs(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
		}
	}
	
	public String getRiqe() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiqe(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq1);
		}
	}
	
	public void SetOtherScript( String otherScript) {
		((Visit) getPage().getVisit()).setString3(otherScript);
	}
	
	public String getOtherScript() {
		if(((Visit) getPage().getVisit()).getString3()==null){
			
			((Visit) getPage().getVisit()).setString3("");
		}
		return ((Visit) getPage().getVisit()).getString3();
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
			visit.setExtGrid1(null);
			visit.setString2(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setString9(null);
			visit.setString10(null);
			visit.setString11(null);
			visit.setString12(null);
			visit.setString13(null);
			visit.setString14(null);
			visit.setString15(null);
			getGongysModels();
		}
		getYunsdwModels();
		getPinzModels();	
		getSelectData();
	}
	
	// 按钮的监听事件
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// 表单按钮提交监听
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		String strOtherScript = 
			"var HegbzCode = [\n" +
			"   ['0','全部'],['1','合格'],['2','不合格']\n" + 
			"];\n" +
			"var form =new Ext.FormPanel({\n" +
			"  labelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" + 
			"    frame:true,\n" + 
			"  items:[{\n" + 
			"    layout:'column',border:false,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.33},\n" + 
			"      frame:true,labelWidth:40,monitorValid:true,\n" + 
			"      height:50,\n" + 
			"      //bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;border=1px' : 'padding:10px 15px;',\n" + 
			"      items:[\n" + 
			"      {\n" + 
			"      items:PinzCb=new Ext.form.ComboBox({\n" + 
			"        width:80,\n" + 
			"         fieldLabel: '品种',\n" + 
			"        selectOnFocus:true,\n" + 
			"        transform:'PinzDropDown',\n" + 
			"        lazyRender:true,\n" + 
			"         triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"        forceSelection:true,\n" + 
			"        editable:false\n" + 
			"      })\n" + 
			"      },\n" + 
			"      {\n" + 
			"      labelWidth:55,\n" +
			"      items:YunsdwCb=new Ext.form.ComboBox({\n" + 
			"        width:80,\n" + 
			"         fieldLabel: '运输单位',\n" + 
			"        selectOnFocus:true,\n" + 
			"        transform:'YunsdwDropDown',\n" + 
			"        lazyRender:true,\n" + 
			"         triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"        forceSelection:true,\n" + 
			"        editable:false\n" + 
			"      })\n" + 
			"      },\n" + 
			"      {\n" + 
			"      items:HegbzCb=new Ext.form.ComboBox({\n" +
			"         width:80,\n" + 
			"         fieldLabel: '指标',\n" + 
			"         selectOnFocus:true,\n" + 
			"         //lazyRender:true,\n" + 
			"         store: new Ext.data.SimpleStore({\n" + 
			"             fields: ['value', 'text'],\n" + 
			"             data : HegbzCode\n" + 
			"         }),\n" + 
			"         valueField:'value',\n" + 
			"         displayField:'text',\n" + 
			"         mode: 'local',\n" + 
			"         value:'全部',\n" + 
			"         triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         //forceSelection:true,\n" + 
			"        editable:false\n" + 
			"       })\n" +
			"      }\n" + 
			"    ]\n" + 
			"    },\n" + 
			"    {\n" + 
			"\n" + 
			"      frame:true,monitorValid:true,\n" + 
			"      title:'质量',\n" + 
			"      height:150,\n" + 
			"      bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;border=1px' : 'padding:10px 15px;',\n" + 
			"    items:[{\n" + 
			"    layout:'column',border:true,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
			"      items:[\n" + 
			"        {\n" + 
			"        items:CaOd=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'CaO(%)下限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'CaOd',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"             'change':function(own,rec,index){\n" + 
			"                var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              qd.setRawValue(0);\n" + 
			"            }\n" + 
			"              }//function\n" + 
			"        }//listeners\n" + 
			"      })\n" + 
			"        },\n" + 
			"        {\n" + 
			"        items:CaOu=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'CaO(%)上限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'CaOu',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"             'change':function(own,rec,index){\n" + 
			"                var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              qu.setRawValue(0);\n" + 
			"            }\n" + 
			"              }\n" + 
			"        }\n" + 
			"      })\n" + 
			"        }\n" + 
			"      ]\n" + 
			"    },\n" + 
			"    {\n" + 
			"    layout:'column',border:false,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
			"      items:[\n" + 
			"        {\n" + 
			"        items:CaCO3d=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'CaCO3(%)下限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'CaCO3d',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"             'change':function(own,rec,index){\n" + 
			"                var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              stard.setRawValue(0);\n" + 
			"            }\n" + 
			"              }\n" + 
			"        }\n" + 
			"      })\n" + 
			"\n" + 
			"        },\n" + 
			"        {\n" + 
			"        items:CaCO3u=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'CaCO3(%)上限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'CaCO3u',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"            'change':function(own,rec,index){\n" + 
			"                var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              staru.setRawValue(0);\n" + 
			"            }\n" + 
			"              }\n" + 
			"        }\n" + 
			"      })\n" + 
			"        }\n" + 
			"      ]\n" + 
			"    },\n" + 
			"    {\n" + 
			"    layout:'column',border:false,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
			"      items:[\n" + 
			"        {\n" + 
			"        items:Xidd=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: '细度(%)下限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Xidd',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"             'change':function(own,rec,index){\n" + 
			"                var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              stdd.setRawValue(0);\n" + 
			"            }\n" + 
			"              }\n" + 
			"        }\n" + 
			"      })\n" + 
			"      },\n" + 
			"      {\n" + 
			"      items:Xidu=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: '细度(%)上限',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Xidu',\n" + 
			"         lazyRender:true,\n" + 
			"        triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"         forceSelection:true,\n" + 
			"         editable:false,\n" + 
			"        listeners:{\n" + 
			"             'change':function(own,rec,index){\n" + 
			"                 var obj1 = own.getRawValue();\n" + 
			"                var num = /^[0-9]{0,2}\\.[0-9]{0,2}$|^[0-9]{0,2}$/;\n" + 
			"            if(!MatchNum(obj1 ,num )){\n" + 
			"              stdu.setRawValue(0);\n" + 
			"            }\n" + 
			"              }\n" + 
			"        }\n" + 
			"      })\n" + 
			"      }\n" + 
			"      ]\n" + 
			"    }\n" + 
			"    ]}\n" + 
			"  ]\n" + 
			"});\n" + 
			"win = new Ext.Window({\n" + 
			"  el:'hello-win',\n" + 
			"  layout:'fit',\n" + 
			"  width:500,\n" + 
			"  height:300,\n" + 
			"  closeAction:'hide',\n" + 
			"  plain: true,\n" + 
			"  title:'条件',\n" + 
			"  items: [form],\n" + 
			"  buttons: [\n" + 
			"    {\n" + 
			"       text:'确定',\n" + 
			"       handler:function(){\n" + 
			"        win.hide();\n" + 
			"        document.getElementById('CaOd').value=CaOd.getRawValue();\n" + 
			"        document.getElementById('CaOu').value=CaOu.getRawValue();\n" + 
			"        document.getElementById('CaCO3d').value=CaCO3d.getRawValue();\n" + 
			"        document.getElementById('CaCO3u').value=CaCO3u.getRawValue();\n" + 
			"        document.getElementById('Xidd').value=Xidd.getRawValue();\n" + 
			"        document.getElementById('Xidu').value=Xidu.getRawValue();\n" +
			"		 document.getElementById('TEXT_YUNSDW_VALUE').value=YunsdwCb.getRawValue();\n" +
			"        document.getElementById('TEXT_PINZ_VALUE').value=PinzCb.getRawValue();\n" +
			"        document.getElementById('TEXT_HEGBZ_VALUE').value=HegbzCb.getRawValue();\n" +
			"        document.getElementById('RefurbishButton').click();\n" +
			"        }\n" + 
			"    },\n" + 
			"    {\n" + 
			"       text: '取消',\n" + 
			"       handler: function(){\n" + 
			"           win.hide();\n" + 
			"           document.getElementById('CaOd').value='';\n" + 
			"           document.getElementById('CaOu').value='';\n" + 
			"           document.getElementById('CaCO3d').value='';\n" + 
			"           document.getElementById('CaCO3u').value='';\n" + 
			"           document.getElementById('Xidd').value='';\n" + 
			"           document.getElementById('Xidu').value='';\n" +
			"           document.getElementById('TEXT_YUNSDW_VALUE').value='';\n" +
			"           document.getElementById('TEXT_PINZ_VALUE').value='';\n" +
			"           document.getElementById('TEXT_HEGBZ_VALUE').value='';\n" +
			"       }\n" + 
			"    }\n" + 
			"  ]\n" + 
			"});\n" + 
			"win.show();win.hide();\n";

		String riqTiaoj = this.getRiqs();
		String riqTiaoj2 = this.getRiqe();
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		if (riqTiaoj2 == null || riqTiaoj2.equals("")) {
			riqTiaoj2 = DateUtil.FormatDate(new Date());
		}
		Toolbar tb1 = new Toolbar("tbdiv");
		tb1.addText(new ToolbarText("开始日期:"));
		DateField df = new DateField();
		df.setValue(getRiqs());
		df.Binding("RIQS", "");// 与html页中的id绑定,并自动刷新
		df.setId("startrq");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("结束日期:"));
		DateField df2 = new DateField();
		df2.setValue(getRiqe());
		df2.Binding("RIQE", "forms[0]");// 与html页中的id绑定,并自动刷新
		df2.setId("endrq");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
			
		tb1.addText(new ToolbarText("供货单位:"));
		ComboBox cb1 = new ComboBox();
		cb1.setTransform("GongysDropDown");
		cb1.setWidth(80);
		tb1.addField(cb1);
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "查询",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		ToolbarButton tbtn = new ToolbarButton(null, "条件",
				"function(){ if(win){win.show(this);}}");
		tb1.addItem(rbtn);
		tb1.addItem(tbtn);
		tb1.addFill();
		
		this.SetOtherScript(strOtherScript);
		setToolbar(tb1);
	}
	
	private String getWhere(){
		
		String where="";
		
		//运输单位
		if(MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw())>-1){			
			where+=" and cp.yunsdwb_id="+MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) +"\n";
			
		}
    	//品种
    	if(MainGlobal.getProperId(getPinzModel(), this.getPinz())>-1){
    		where+=" and cp.SHIHPZB_ID = " + MainGlobal.getProperId(getPinzModel(), this.getPinz()) +"\n";
    	}
    	
    	//是否合格
    	if(this.getHegbz()!=null && !"".equals(this.getHegbz())&& !"全部".equals(this.getHegbz())){
    		where+=" and zl.hegbz = '" + this.getHegbz() + "'\n";
    	}
    	
    	//CaO
//    	下限
    	if(this.getCaOd()!=null&&!"".equals(this.getCaOd())&&!"0".equals(this.getCaOd())){
    		where+=" and zl.Cao>="+this.getCaOd()+"\n";
    	}
//    	上限
    	if(this.getCaOu()!=null&&!"".equals(this.getCaOu())&&!"0".equals(this.getCaOu())){
    		where+=" and zl.CaO<="+this.getCaOu()+"\n";
    	}
    	//CaCO3
//    	下限
    	if(this.getCaCO3d()!=null&&!"".equals(this.getCaCO3d())&&!"0".equals(this.getCaCO3d())){
    		where+=" and zl.CaCO3>="+this.getCaCO3d()+"\n";
    	}
//    	上限
    	if(this.getCaCO3u()!=null&&!"".equals(this.getCaCO3u())&&!"0".equals(this.getCaCO3u())){
    		where+=" and zl.CaCO3<="+this.getCaCO3u()+"\n";
    	}
    	//xid
//    	下限
    	if(this.getXidd()!=null&&!"".equals(this.getXidd())&&!"0".equals(this.getXidd())){
    		where+=" and zl.xid>="+this.getXidd()+"\n";
    	}
//    	上限
    	if(this.getXidu()!=null&&!"".equals(this.getXidu())&&!"0".equals(this.getXidu())){
    		where+=" and zl.xid<="+this.getXidu()+"\n";
    	}
    	return where;
	}
	
	public String getPrintTable() {
		Visit visit = (Visit) this.getPage().getVisit();
		long diancxxb_id = visit.getDiancxxb_id();
		JDBCcon con = new JDBCcon();
		String riqTiaoj = this.getRiqs();
		String riqTiaoj2 = this.getRiqe();
		String meikWhere = "";
		if (riqTiaoj == null || riqTiaoj.equals("")) {
			riqTiaoj = DateUtil.FormatDate(new Date());
		}
		if (riqTiaoj2 == null || riqTiaoj2.equals("")) {
			riqTiaoj2 = DateUtil.FormatDate(new Date());
		}
		
		if(getGongysValue()!=null && getGongysValue().getId()!=-1) {
			meikWhere = "AND GYS.ID= " + getGongysValue().getId();
		}
		String sql = 
			"SELECT DECODE(GYS.MINGC, NULL, '合计', GYS.MINGC) AS GONGYSMC,\n" +
			"       PZ.MINGC AS PINZMC,\n" + 
			"       CP.CHEPH,\n" + 
			"       TO_CHAR(CP.FAHRQ,'yyyy-mm-dd') AS FAHRQ,\n" + 
			"       TO_CHAR(CP.DAOHRQ,'yyyy-mm-dd') AS DAOHRQ,\n" + 
			"       COUNT(CP.CHEPH) AS CHES,\n" + 
			"       ZL.HEGBZ,\n" +
			"       ROUND_NEW(SUM(CP.MAOZ - CP.PIZ), 2) AS JINGZ,\n" + 
			"       ROUND_NEW(DECODE(SUM(DECODE(NVL(ZL.CAO, 0), 0, 0, CP.BIAOZ)),\n" + 
			"                        0,\n" + 
			"                        0,\n" + 
			"                        SUM(ZL.CAO * CP.BIAOZ) /\n" + 
			"                        SUM(DECODE(NVL(ZL.CAO, 0), 0, 0, CP.BIAOZ))),\n" + 
			"                 2) AS CAO,\n" + 
			"       ROUND_NEW(DECODE(SUM(DECODE(NVL(ZL.CACO3, 0), 0, 0, CP.BIAOZ)),\n" + 
			"                        0,\n" + 
			"                        0,\n" + 
			"                        SUM(ZL.CACO3 * CP.BIAOZ) /\n" + 
			"                        SUM(DECODE(NVL(ZL.CACO3, 0), 0, 0, CP.BIAOZ))),\n" + 
			"                 2) AS CACO3,\n" + 
			"       ROUND_NEW(DECODE(SUM(DECODE(NVL(ZL.XID, 0), 0, 0, CP.BIAOZ)),\n" + 
			"                        0,\n" + 
			"                        0,\n" + 
			"                        SUM(ZL.XID * CP.BIAOZ) /\n" + 
			"                        SUM(DECODE(NVL(ZL.XID, 0), 0, 0, CP.BIAOZ))),\n" + 
			"                 2) AS XID,\n" + 
			"       ROUND_NEW(DECODE(SUM(DECODE(NVL(ZL.MGO, 0), 0, 0, CP.BIAOZ)),\n" + 
			"                        0,\n" + 
			"                        0,\n" + 
			"                        SUM(ZL.MGO * CP.BIAOZ) /\n" + 
			"                        SUM(DECODE(NVL(ZL.MGO, 0), 0, 0, CP.BIAOZ))),\n" + 
			"                 2) AS MGO,\n" + 
			"       ROUND_NEW(DECODE(SUM(DECODE(NVL(ZL.MGCO3, 0), 0, 0, CP.BIAOZ)),\n" + 
			"                        0,\n" + 
			"                        0,\n" + 
			"                        SUM(ZL.MGCO3 * CP.BIAOZ) /\n" + 
			"                        SUM(DECODE(NVL(ZL.MGCO3, 0), 0, 0, CP.BIAOZ))),\n" + 
			"                 2) AS MGCO3\n" + 
			"  FROM SHIHCPB CP, SHIHCYB CY, SHIHZLB ZL, SHIHGYSB GYS, SHIHPZB PZ\n" + 
			" WHERE CP.SHIHCYB_ID = CY.ID\n" + 
			"   AND CY.SHIHZLB_ID = ZL.ID(+)\n" + 
			"   AND CP.GONGYSB_ID = GYS.ID\n" + 
			"   AND CP.SHIHPZB_ID = PZ.ID\n" + 
				meikWhere + "\n" +
			    getWhere() + "\n" +
			"   AND CP.DIANCXXB_ID = " + diancxxb_id + "\n" +
			"   AND CP.DAOHRQ BETWEEN TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd') AND\n" + 
			"       TO_DATE('" + riqTiaoj2 + "', 'yyyy-mm-dd')\n" + 
			" GROUP BY ROLLUP(GYS.MINGC, PZ.MINGC, CHEPH, ZL.HEGBZ, CP.FAHRQ, CP.DAOHRQ)\n" + 
			"HAVING GROUPING(GYS.MINGC) + GROUPING(DAOHRQ) = 2 OR GROUPING(GYS.MINGC) + GROUPING(CP.DAOHRQ) = 0\n" +
			" ORDER BY GYS.MINGC, PZ.MINGC, CP.FAHRQ, CHEPH";

		ResultSet rs=con.getResultSet(sql);
		Report rt=new Report();
		String ArrHeader[][]=new String[1][23];
		ArrHeader[0] = new String[] {"供应商","品种","车牌号","发货日期","到货日期","车数",
					"指标","净重","CaO(%)","CaCO3(%)","细度(%)","MgO(%)","MgCO3(%)"};
		int ArrWidth[]=new int[] {100,60,70,80,80,50,60,70,50,50,50,50,50};
		rt.setTitle("石灰石数量质量明细", ArrWidth);
		rt.setBody(new Table(rs, 1, 0, 0));
		rt.body.setWidth(ArrWidth);
		rt.body.setHeaderData(ArrHeader);
		for (int i=1;i<=rt.body.getCols();i++) 
			rt.body.setColAlign(i, Table.ALIGN_CENTER);
		for (int i=8;i<=rt.body.getCols();i++) {
			rt.body.setColFormat(i, "0.00");
		}
		rt.body.setPageRows(40);
		if (rt.body.getPages() > 0) {
			setCurrentPage(1);
			setAllPages(rt.body.getPages());
		}
		rt.createDefautlFooter(ArrWidth);
		rt.setDefautlFooter(1, 2, "打印日期："
				+ DateUtil.Formatdate("yyyy年MM月dd日", new Date()),
				Table.ALIGN_RIGHT);
		rt.setDefautlFooter(12, 2, "审核：", Table.ALIGN_LEFT);
		rt.setDefautlFooter(19, 2, "制表：", Table.ALIGN_LEFT);
		return rt.getAllPagesHtml();
	}
	
	
//	运输单位下拉框
	public String getYunsdw() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}
	public void setYunsdw(String changb) {
		((Visit) this.getPage().getVisit()).setString10(changb);
	}
	
//	品种下拉框
	public String getPinz() {
		return ((Visit) this.getPage().getVisit()).getString11();
	}
	public void setPinz(String pinz) {
		((Visit) this.getPage().getVisit()).setString11(pinz);
	}
	
//	是否合格下拉框
	public String getHegbz() {
		return ((Visit) this.getPage().getVisit()).getString9();
	}
	public void setHegbz(String hegbz) {
		((Visit) this.getPage().getVisit()).setString9(hegbz);
	}
	
//	运输单位_begin
	
	public IDropDownBean getYunsdwValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean6() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean6((IDropDownBean) getYunsdwModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean6();
	}

	public void setYunsdwValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean6()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean6(Value);
	}

	public IPropertySelectionModel getYunsdwModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel6() == null) {

			getYunsdwModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}

	public void setYunsdwModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel6(value);
	}

	public IPropertySelectionModel getYunsdwModels() {
		
		long Diancxxb_id=0;
//		if(((Visit) getPage().getVisit()).isFencb()){
//			
//			Diancxxb_id=this.getFencbValue().getId();
//		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
//		}
		
			String sql =
				"SELECT DISTINCT YS.ID, YS.MINGC\n" +
				"  FROM SHIHCPB CP, YUNSDWB YS\n" + 
				" WHERE CP.YUNSDWB_ID = YS.ID\n" + 
				"   AND CP.DAOHRQ >= TO_DATE('"+this.getRiqs()+"', 'yyyy-mm-dd')\n" + 
				"   AND CP.DAOHRQ <= TO_DATE('"+this.getRiqe()+"', 'yyyy-mm-dd')\n" + 
				"   AND CP.DIANCXXB_ID =" + Diancxxb_id + "\n" + 
				" ORDER BY YS.MINGC";
			((Visit) getPage().getVisit())
			.setProSelectionModel6(new IDropDownModel(sql,"全部"));
			return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}
//	运输单位_end
	
//	品种_begin
	
	public IDropDownBean getPinzValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean7() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean7((IDropDownBean) getPinzModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean7();
	}

	public void setPinzValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean7()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean7(Value);
	}

	public IPropertySelectionModel getPinzModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel7() == null) {

			getPinzModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}

	public void setPinzModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel7(value);
	}

	public IPropertySelectionModel getPinzModels() {
		
		long Diancxxb_id=0;
//		if(((Visit) getPage().getVisit()).isFencb()){
//			
//			Diancxxb_id=this.getFencbValue().getId();
//		}else{
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
//		}
		
			String sql =
				"SELECT DISTINCT PZ.ID, PZ.MINGC\n" +
				"  FROM SHIHCPB CP, SHIHPZB PZ\n" + 
				" WHERE CP.SHIHPZB_ID = PZ.ID\n" + 
				"   AND CP.DAOHRQ >= TO_DATE('"+this.getRiqs()+"', 'yyyy-mm-dd')\n" + 
				"   AND CP.DAOHRQ <= TO_DATE('"+this.getRiqe()+"', 'yyyy-mm-dd')\n" + 
				"   AND CP.DIANCXXB_ID = " + Diancxxb_id + "\n" +
				" ORDER BY PZ.MINGC";

			((Visit) getPage().getVisit())
			.setProSelectionModel7(new IDropDownModel(sql,"全部"));
			return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//	品种_end
	
//	供货单位
	public IDropDownBean getGongysValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean8() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean8((IDropDownBean) getGongysModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean8();
	}

	public void setGongysValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean8()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean8(Value);
	}

	public IPropertySelectionModel getGongysModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel8() == null) {

			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}

	public void setGongysModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel8(value);
	}

	public IPropertySelectionModel getGongysModels() {
		
		long Diancxxb_id=0;
			
			Diancxxb_id=((Visit) getPage().getVisit()).getDiancxxb_id();
		
			String sql = "SELECT ID,mingc FROM shihgysb";

			((Visit) getPage().getVisit())
			.setProSelectionModel8(new IDropDownModel(sql,"全部"));
			return ((Visit) getPage().getVisit()).getProSelectionModel8();
	}
	

//	CaO上限
	public String getCaOu() {
		return ((Visit) this.getPage().getVisit()).getString12();
	}
	public void setCaOu(String qu) {
		((Visit) this.getPage().getVisit()).setString12(qu);
	}
//	CaO下限
	public String getCaOd() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}
	public void setCaOd(String qd) {
		((Visit) this.getPage().getVisit()).setString13(qd);
	}
//	CaCo3上限
	public String getCaCO3u() {
		return ((Visit) this.getPage().getVisit()).getString14();
	}
	public void setCaCO3u(String su) {
		((Visit) this.getPage().getVisit()).setString14(su);
	}
//	CaCo3下限
	public String getCaCO3d() {
		return ((Visit) this.getPage().getVisit()).getString15();
	}
	public void setCaCO3d(String sd) {
		((Visit) this.getPage().getVisit()).setString15(sd);
	}
//	细度上限
	public String getXidu() {
		return getString16();
	}
	public void setXidu(String su) {
		setString16(su);
	}
//	细度下限
	public String getXidd() {
		return getString17();
	}
	public void setXidd(String sd) {
		setString17(sd);
	}
	private String string16;
	
	public String getString16() {
		return string16;
	}
	public void setString16(String string) {
		string16 = string; 
	}	
	
	private String string17;
	public String getString17() {
		return string17;
	}
	public void setString17(String string) {
		string17 = string; 
	}
}
