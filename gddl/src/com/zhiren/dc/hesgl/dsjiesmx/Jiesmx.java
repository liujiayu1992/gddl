package com.zhiren.dc.hesgl.dsjiesmx;

import java.sql.ResultSet;
import java.util.Calendar;
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
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Jiesmx extends BasePage implements PageValidateListener {
	
	protected void initialize() {
		super.initialize();
	}
	
	public boolean getRaw() {
		return true;
	}
	
	// �����ʼ����
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
	
	private boolean reportShowZero(){
		return ((Visit) getPage().getVisit()).isReportShowZero();
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
	
	public String getTreeid() {
		if(((Visit) getPage().getVisit()).getString2()==null
				||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}

	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
		}
	}
	
	public ExtTreeUtil getTree() {
		return ((Visit) this.getPage().getVisit()).getExtTree1();
	}

	public void setTree(ExtTreeUtil etu) {
		((Visit) this.getPage().getVisit()).setExtTree1(etu);
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
	
	public String getTreeHtml() {
		return getTree().getWindowTreeHtml(this);
	}

	public String getGridHtml() {
		if (getExtGrid() == null) {
			return "";
		}
		return getExtGrid().getHtml();
	}

	public String getTreeScript() {		
		return getTree().getWindowTreeScript();
	}
	
	public String getToolbarScript() {
		return getToolbar().getRenderScript();
	}
	
	// ������ʹ�õķ���
	public Toolbar getToolbar() {
		return ((Visit) this.getPage().getVisit()).getToolbar();
	}

	public void setToolbar(Toolbar tb1) {
		((Visit) this.getPage().getVisit()).setToolbar(tb1);
	}
	
	public String getRiqs() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			Calendar stra=Calendar.getInstance();
			stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
			stra.add(Calendar.MONTH,-1);
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(stra.getTime()));
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setExtGrid1(null);
			visit.setString2(null);
			visit.setString5(null);
			visit.setString6(null);
			visit.setString12(null);
			visit.setString13(null);
			visit.setString14(null);
			visit.setString15(null);
		}
		getYunsdwModels();
		getPinzModels();
		getSelectData();
	}
	
	// ��ť�ļ����¼�
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}

	// ����ť�ύ����
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) this.getPage().getVisit();
		String strOtherScript = 
			"var form =new Ext.FormPanel({\n" +
			"  labelAlign:'left',buttonAlign:'right',bodyStyle:'padding:5px;',\n" + 
			"    frame:true,\n" + 
			"  items:[{\n" + 
			"    layout:'column',border:false,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
			"      frame:true,labelWidth:60,monitorValid:true,\n" + 
			"      height:50,\n" + 
			"      bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;border=1px' : 'padding:10px 15px;',\n" + 
			"      items:[\n" + 
			"      {\n" + 
			"      items:PinzCb=new Ext.form.ComboBox({\n" + 
			"        width:120,\n" + 
			"         fieldLabel: 'Ʒ��',\n" + 
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
			"      items:YunsdwCb=new Ext.form.ComboBox({\n" + 
			"        width:120,\n" + 
			"         fieldLabel: '���䵥λ',\n" + 
			"        selectOnFocus:true,\n" + 
			"        transform:'YunsdwDropDown',\n" + 
			"        lazyRender:true,\n" + 
			"         triggerAction:'all',\n" + 
			"         typeAhead:true,\n" + 
			"        forceSelection:true,\n" + 
			"        editable:false\n" + 
			"      })\n" + 
			"      }\n" + 
			"    ]\n" + 
			"    },\n" + 
			"    {\n" + 
			"\n" + 
			"      frame:true,monitorValid:true,\n" + 
			"      title:'����',\n" + 
			"      height:150,\n" + 
			"      bodyStyle: Ext.isIE ? 'padding:0 0 5px 15px;border=1px' : 'padding:10px 15px;',\n" + 
			"    items:[{\n" + 
			"    layout:'column',labelWidth:120,border:true,labelSeparator:':',\n" + 
			"      defaults:{layout: 'form',border:false,columnWidth:.5},\n" + 
			"      items:[\n" + 
			"        {\n" + 
			"        items:qd=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Qnet,ar(mj/kg)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Qd',\n" + 
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
			"        items:qu=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Qnet,ar(mj/kg)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Qu',\n" + 
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
			"        items:stard=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Star(%)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Stard',\n" + 
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
			"        items:staru=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Star(%)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Staru',\n" + 
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
			"        items:stdd=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Std(%)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Stdd',\n" + 
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
			"      items:stdu=new Ext.form.Field({\n" + 
			"        width:70,\n" + 
			"         fieldLabel: 'Std(%)����',\n" + 
			"         selectOnFocus:true,\n" + 
			"         transform:'Stdu',\n" + 
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
			"  title:'����',\n" + 
			"  items: [form],\n" + 
			"  buttons: [\n" + 
			"    {\n" + 
			"       text:'ȷ��',\n" + 
			"       handler:function(){\n" + 
			"        win.hide();\n" + 
			"        document.getElementById('Qd').value=qd.getRawValue();\n" + 
			"        document.getElementById('Qu').value=qu.getRawValue();\n" + 
			"        document.getElementById('Stdd').value=stdd.getRawValue();\n" + 
			"        document.getElementById('Stdu').value=stdu.getRawValue();\n" + 
			"        document.getElementById('Stard').value=stard.getRawValue();\n" + 
			"        document.getElementById('Staru').value=staru.getRawValue();\n" +
			"		 document.getElementById('TEXT_YUNSDW_VALUE').value=YunsdwCb.getRawValue();\n" +
			"        document.getElementById('TEXT_PINZ_VALUE').value=PinzCb.getRawValue();\n" +
			"        document.getElementById('RefurbishButton').click();\n" +
			"        }\n" + 
			"    },\n" + 
			"    {\n" + 
			"       text: 'ȡ��',\n" + 
			"       handler: function(){\n" + 
			"           win.hide();\n" + 
			"           document.getElementById('Qd').value='';\n" + 
			"           document.getElementById('Qu').value='';\n" + 
			"           document.getElementById('Stdd').value='';\n" + 
			"           document.getElementById('Stdu').value='';\n" + 
			"           document.getElementById('Stard').value='';\n" + 
			"           document.getElementById('Staru').value='';\n" +
			"           document.getElementById('TEXT_YUNSDW_VALUE').value='';\n" +
			"           document.getElementById('TEXT_PINZ_VALUE').value='';\n" +
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
		tb1.addText(new ToolbarText("��ʼ����:"));
		DateField df = new DateField();
		df.setValue(getRiqs());
		df.Binding("RIQS", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("startrq");
		tb1.addField(df);
		
		tb1.addText(new ToolbarText("��������:"));
		DateField df2 = new DateField();
		df2.setValue(getRiqe());
		df2.Binding("RIQE", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df2.setId("endrq");
		tb1.addField(df2);
		tb1.addText(new ToolbarText("-"));
		
		String condition=" and fh.daohrq >= to_date('"+ riqTiaoj +"','yyyy-MM-dd')" +
			"and fh.daohrq <= to_date('"+ riqTiaoj2 +"','yyyy-mm-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid(),condition);
		setTree(etu);
		
		TextField tf = new TextField();
		tf.setId("gongysTree_text");
		tf.setWidth(100);
		this.SetOtherScript(strOtherScript + "gongysTree_text.setValue(" 
				+ "gongysTree_treePanel.getSelectionModel().getSelectedNode()==null?'':"
				+ "gongysTree_treePanel.getSelectionModel().getSelectedNode().text);");

		ToolbarButton tb2 = new ToolbarButton(null, null, "function(){gongysTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
			
		tb1.addText(new ToolbarText("������λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		
		tb1.addText(new ToolbarText("-"));

		ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
				"function(){document.getElementById('RefurbishButton').click();}");
		rbtn.setIcon(SysConstant.Btn_Icon_Search);
		ToolbarButton tbtn = new ToolbarButton(null, "����",
				"function(){ if(win){win.show(this);}}");
		tb1.addItem(rbtn);
		tb1.addItem(tbtn);
		tb1.addFill();
		setToolbar(tb1);
	}
	
	private String getWhere(){
		
		String where="";
		
		//���䵥λ
		if(MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw())>-1){			
			where+=" and cp.yunsdwb_id="+MainGlobal.getProperId(getYunsdwModel(), this.getYunsdw()) +"\n";
			
		}
    	//Ʒ��
    	if(MainGlobal.getProperId(getPinzModel(), this.getPinz())>-1){
    		where+=" and f.pinzb_id = " + MainGlobal.getProperId(getPinzModel(), this.getPinz()) +"\n";
    	}
    	
    	//��λ����
//    	����
    	if(this.getQd()!=null&&!"".equals(this.getQd())&&!"0".equals(this.getQd())){
    		where+=" and z.qnet_ar>="+this.getQd()+"\n";
    	}
//    	����
    	if(this.getQu()!=null&&!"".equals(this.getQu())&&!"0".equals(this.getQu())){
    		where+=" and z.qnet_ar<="+this.getQu()+"\n";
    	}
    	//Std
//    	����
    	if(this.getStdd()!=null&&!"".equals(this.getStdd())&&!"0".equals(this.getStdd())){
    		where+=" and z.std>="+this.getStdd()+"\n";
    	}
//    	����
    	if(this.getStdu()!=null&&!"".equals(this.getStdu())&&!"0".equals(this.getStdu())){
    		where+=" and z.std<="+this.getStdu()+"\n";
    	}
    	//Star
//    	����
    	if(this.getStard()!=null&&!"".equals(this.getStard())&&!"0".equals(this.getStard())){
    		where+=" and z.star>="+this.getStard()+"\n";
    	}
//    	����
    	if(this.getStaru()!=null&&!"".equals(this.getStaru())&&!"0".equals(this.getStaru())){
    		where+=" and z.star<="+this.getStaru()+"\n";
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
		
		if(getTreeid()!=null && !"".equals(getTreeid()) && !"0".equals(getTreeid())) {
			meikWhere = "AND M.ID= " + getTreeid();
		}
		String sql = 
			"SELECT " +
			"       DECODE(G.MINGC, NULL, '�ϼ�', G.MINGC) AS FAHDW,\n" + 
			"       M.MINGC AS MEIKDW,\n" + 
			"       PZ.MINGC AS PINZ,\n" +
			"       TO_CHAR(F.FAHRQ, 'yyyy-MM-dd') AS FAHRQ,\n" + 
			"       TO_CHAR(F.DAOHRQ, 'yyyy-MM-dd') AS DAOHRQ,\n" + 
			"       C.MINGC AS FAZ,\n" + 
			"       COUNT(CP.ID) AS CHES,\n" + 
			"       SUM(CP.BIAOZ) AS BIAOZ,\n" + 
			"       SUM(CP.YINGK) AS YINGK,\n" + 
			"       SUM(CP.YUNS) AS YUNS,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" +
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.QNET_AR) / SUM(CP.JINGZ), 2)) AS QNETAR,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.STAR) / SUM(CP.JINGZ), 2)) AS STAR,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.STAD) / SUM(CP.JINGZ), 2)) AS STAD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.STD) / SUM(CP.JINGZ), 2)) AS STD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.MT) / SUM(CP.JINGZ), 1)) AS MT,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.MAD) / SUM(CP.JINGZ), 2)) AS MAD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.AAR) / SUM(CP.JINGZ), 2)) AS AAR,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.AAD) / SUM(CP.JINGZ), 2)) AS AAD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.AD) / SUM(CP.JINGZ), 2)) AS AD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.VAD) / SUM(CP.JINGZ), 2)) AS VAD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.VDAF) / SUM(CP.JINGZ), 2)) AS VDAF,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.HAD) / SUM(CP.JINGZ), 2)) AS HAD,\n" + 
			"DECODE(SUM(CP.JINGZ),\n" + 
			"       0,\n" + 
			"       0,\n" + 
			"       ROUND_NEW(SUM(CP.JINGZ * Z.QBAD) / SUM(CP.JINGZ), 2)) AS QBAD\n" +
			"  FROM FAHB F, GONGYSB G, MEIKXXB M, HETB H, CHEZXXB C, ZHILB Z, PINZB PZ,\n" + 
			"(SELECT ID,\n" +
			"        FAHB_ID,\n" + 
			"        MAOZ,\n" + 
			"        PIZ,\n" + 
			"        BIAOZ,\n" + 
			"        YUNS,\n" + 
			"        YINGK,\n" + 
			"        (MAOZ - PIZ - ZONGKD) AS JINGZ\n" + 
			"   FROM CHEPB) CP\n" +
			" WHERE F.GONGYSB_ID = G.ID\n" + 
			"   AND F.MEIKXXB_ID = M.ID\n" + 
			"   AND F.PINZB_ID = PZ.ID\n" +
			"   AND F.HETB_ID = H.ID(+)\n" + 
			"   AND F.FAZ_ID = C.ID\n" + 
			"   AND F.ID = CP.FAHB_ID\n" + 
			"   AND F.LIUCZTB_ID = 1\n" + 
			"   AND F.JIESB_ID = 0\n" + 
			"   AND F.ZHILB_ID = Z.ID\n" + 
			"   AND Z.LIUCZTB_ID = 1\n" + 
			    meikWhere + "\n" +
			    getWhere() + "\n" +
			"   AND F.DIANCXXB_ID = " + diancxxb_id + "\n" +
			"   AND DAOHRQ BETWEEN TO_DATE('" + riqTiaoj + "', 'yyyy-mm-dd') AND\n" + 
			"       TO_DATE('" + riqTiaoj2 + "', 'yyyy-mm-dd')\n" + 
			" GROUP BY ROLLUP(F.LIE_ID,\n" + 
			"                 G.MINGC,\n" + 
			"                 M.MINGC,\n" + 
			"                 PZ.MINGC,\n" +
			"                 H.HETBH,\n" + 
			"                 F.FAHRQ,\n" + 
			"                 F.DAOHRQ,\n" + 
			"                 C.MINGC)\n" + 
			"HAVING NOT(GROUPING(C.MINGC) = 1 AND GROUPING(F.LIE_ID) = 0)\n" + 
			" ORDER BY G.MINGC, M.MINGC, PZ.MINGC, FAHRQ";
		ResultSet rs=con.getResultSet(sql);
		Report rt=new Report();
		String ArrHeader[][]=new String[1][23];
		ArrHeader[0] = new String[] {"��Ӧ��","ú��","Ʒ��","��������","��������","��վ","����",
					"Ʊ��","ӯ��","����","Qnet,ar","Star","Stad","Std","Mt","Mad","Aar",
					"Aad","Ad","Vad","Vdaf","Had","Qbad"};
		int ArrWidth[]=new int[] {100,100,50,80,80,50,50,70,50,50,40,40,40,40,40,40,40,40,40,40,40,40,40};
		rt.setTitle("��������������ϸ", ArrWidth);
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
		rt.setDefautlFooter(1, 2, "��ӡ���ڣ�"
				+ DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
				Table.ALIGN_RIGHT);
		rt.setDefautlFooter(12, 2, "��ˣ�", Table.ALIGN_LEFT);
		rt.setDefautlFooter(19, 2, "�Ʊ�", Table.ALIGN_LEFT);
		return rt.getAllPagesHtml();
	}
	
	
//	���䵥λ������
	public String getYunsdw() {
		return ((Visit) this.getPage().getVisit()).getString10();
	}
	public void setYunsdw(String changb) {
		((Visit) this.getPage().getVisit()).setString10(changb);
	}
	
//	Ʒ��������
	public String getPinz() {
		return ((Visit) this.getPage().getVisit()).getString11();
	}
	public void setPinz(String pinz) {
		((Visit) this.getPage().getVisit()).setString11(pinz);
	}
	
//	���䵥λ_begin
	
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
		
			String sql ="select distinct y.id,y.mingc\n" +
"               from fahb fh,chepb c,zhilb z,yunsdwb y\n" + 
"               where fh.id=c.fahb_id and c.yunsdwb_id=y.id\n" + 
"                     and "+rb1()+">=to_date('"+this.getRiqs()+"','yyyy-MM-dd')\n" + 
"                     and "+rb1()+"<=to_date('"+this.getRiqe()+"','yyyy-MM-dd')\n" + 
"					  and fh.diancxxb_id="+Diancxxb_id+"	\n"+
"                     and fh.liucztb_id=1 and z.liucztb_id=1\n" + 
"               order by y.mingc";

			((Visit) getPage().getVisit())
			.setProSelectionModel6(new IDropDownModel(sql,"ȫ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel6();
	}
//	���䵥λ_end
	
//	Ʒ��_begin
	
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
		
			String sql ="select distinct p.id,p.mingc\n" +
"               from fahb fh,zhilb z,pinzb p\n" + 
"               where fh.pinzb_id = p.id\n" + 
"                     and "+rb1()+">=to_date('"+this.getRiqs()+"','yyyy-MM-dd')\n" + 
"                     and "+rb1()+"<=to_date('"+this.getRiqe()+"','yyyy-MM-dd')\n" + 
"					  and fh.diancxxb_id="+Diancxxb_id+"	\n"+
"                     and fh.liucztb_id=1 and z.liucztb_id=1\n" + 
"               order by p.mingc";

			((Visit) getPage().getVisit())
			.setProSelectionModel7(new IDropDownModel(sql,"ȫ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel7();
	}
//	Ʒ��_end
	
    public String rb1(){ 		
  		
    	return "fh.fahrq";	    	
    		
    }
//	Qnet,ar����
	public String getQu() {
		return ((Visit) this.getPage().getVisit()).getString12();
	}
	public void setQu(String qu) {
		((Visit) this.getPage().getVisit()).setString12(qu);
	}
//	Qnet,ar����
	public String getQd() {
		return ((Visit) this.getPage().getVisit()).getString13();
	}
	public void setQd(String qd) {
		((Visit) this.getPage().getVisit()).setString13(qd);
	}
//	Std����
	public String getStdu() {
		return ((Visit) this.getPage().getVisit()).getString14();
	}
	public void setStdu(String su) {
		((Visit) this.getPage().getVisit()).setString14(su);
	}
//	Std����
	public String getStdd() {
		return ((Visit) this.getPage().getVisit()).getString15();
	}
	public void setStdd(String sd) {
		((Visit) this.getPage().getVisit()).setString15(sd);
	}
//	Star����
	public String getStaru() {
		return getString16();
	}
	public void setStaru(String su) {
		setString16(su);
	}
//	Std����
	public String getStard() {
		return getString17();
	}
	public void setStard(String sd) {
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
