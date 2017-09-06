package com.zhiren.shihs.hesgl;

import java.sql.ResultSet;
import java.sql.SQLException;
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
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.dc.hesgl.jiesd.Jiesdcz;

public class Jiesxz_Shih extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
	protected void initialize() {
		msg = "";
	}

	// ������
	public String getRiq1() {
		if (((Visit) this.getPage().getVisit()).getString5() == null || ((Visit) this.getPage().getVisit()).getString5().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString5(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRiq1(String riq1) {

		if (((Visit) this.getPage().getVisit()).getString5() != null && !((Visit) this.getPage().getVisit()).getString5().equals(riq1)) {
			
			((Visit) this.getPage().getVisit()).setString5(riq1);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}
	}
	

	public String getRiq2() {
		if (((Visit) this.getPage().getVisit()).getString6() == null || ((Visit) this.getPage().getVisit()).getString6().equals("")) {
			
			((Visit) this.getPage().getVisit()).setString6(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString6();
	}

	public void setRiq2(String riq2) {

		if (((Visit) this.getPage().getVisit()).getString6() != null && !((Visit) this.getPage().getVisit()).getString6().equals(riq2)) {
			
			((Visit) this.getPage().getVisit()).setString6(riq2);
			((Visit) this.getPage().getVisit()).setboolean1(true);
		}

	}

//	��������ѡ��
	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString1();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString1(rbvalue);
	}
	
//	����������ȡֵ

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}
	
	//����۶���ȡֵ
	public String getJieskdl() {
		return ((Visit) this.getPage().getVisit()).getString7();
	}
	public void setJieskdl(String jieskdl) {
		((Visit) this.getPage().getVisit()).setString7(jieskdl);
	}

	
	// ҳ��仯��¼
	private String Change;

	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //Ϊ  "ˢ��"  ��ť��Ӵ������
    	getSelectData();
    }
//
	private boolean _JiesChick = false;

	public void JiesButton(IRequestCycle cycle) {
		_JiesChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		
		if (_JiesChick) {
			_JiesChick = false;
			GotoJiesd(cycle);
		}
	}
	
	private void GotoJiesd(IRequestCycle cycle) {
		// TODO �Զ����ɷ������
		
		ExtGridUtil egu=this.getExtGrid();
    	ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
    	String mstr = "";
    	String mstr_id="";
    	
    	while(mdrsl.next()){
    		
    		mstr+=mdrsl.getString("ID")+",";
    	}
    	mstr_id = mstr.substring(0, mstr.length()-1);
    	
//    	�糧Id��ֵ
    	if(((Visit) getPage().getVisit()).isFencb()){
    		
    		((Visit) getPage().getVisit()).setLong1(MainGlobal.getProperId(this.getFencbModel(),this.getChangb()));
    	}
    	else{
    		((Visit) getPage().getVisit()).setLong1(((Visit) getPage().getVisit()).getDiancxxb_id());
    	}
    	
//    	��ƤId
    	((Visit) getPage().getVisit()).setString1(mstr_id);
    	
//    	��������
    	((Visit) getPage().getVisit()).setLong2(Locale.shihsjs_feiylbb_id);
    	  	
//		����ʱ�Ŀ۶������ò��������Ѽ���������
    	if(((Visit) getPage().getVisit()).getString7().trim().equals("")){
    		((Visit) getPage().getVisit()).setString7("0");
    	}
    	
//    	����ѡ�г�Ƥ���Shihhtb_idΪgetHthValue().getId()
    	if(setChepb_HetbId(((Visit) getPage().getVisit()).getString1(),this.getHthValue().getId())){
    		
//    		��Shihhtb_id����Long8
    		((Visit) getPage().getVisit()).setLong8(this.getHthValue().getId());
    		cycle.activate("Dcbalance_Shih");
    	}
    	mdrsl.close();
	}
	
	private boolean setChepb_HetbId(String ChepbSelLieId, long HetbId) {
		// TODO �Զ����ɷ������
//		����ѡʯ�ҳ�Ƥ���е�Shihhtb_idΪgetHthValue().getId()
		JDBCcon con=new JDBCcon(); 
		boolean flag=false;
		String sql="update shihcpb set shihhtb_id="+HetbId+" where id in ("+ChepbSelLieId+")";
		if(con.getUpdate(sql)>=0){
			
			flag=true;
		}
		con.Close();
		return flag;
	}

	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 ********************������************************************************
	    String biaoq="��������";
	    String riq="fahrq";
	    String danx="";
        String danx1="";
        String fencb="";
        String tiaoj_returnvalue_changb="";

        String jieskdl=" ,{  \n"
			+ " \txtype:'textfield',\n" 
			+ " \tfieldLabel:'����۶���',\n" 
			+ "	\twidth:0	\n"  
			+ " },	\n"
			+ "	\tjieskdl=new Ext.form.Field({	\n" 
			+ "	\twidth:135,	\n" 
			+ " \tselectOnFocus:true,	\n" 
			+ " \ttransform:'Jieskdl',	\n" 
			+ " \tlazyRender:true,	\n" 
			+ "	\ttriggerAction:'all',	\n" 
			+ " \ttypeAhead:true,	\n" 
			+ " \tforceSelection:true,	\n" 
			+ " \teditable:false,	\n"
			+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('Jieskdl').selectedIndex=index}}"
			
			+ "	\t})";

		if(getRbvalue().equals("fahrq")||this.getRbvalue().equals("")){
			
			
			biaoq="��������";
			riq="fahrq";
			danx="checked:true ,   \n";
		}else if(getRbvalue().equals("daohrq")){
			biaoq="��������";
			riq="daohrq";
			danx1="checked:true ,   \n";
		
		}
        if(visit.isFencb()==true){
	         fencb=" ,{ \n"
	        	+"xtype:'textfield',\n" 
	        	+"fieldLabel:'����ѡ��',\n"
	        	+"width:0 \n" 
	        +"},\n"
	    	    +"Changb=new Ext.form.ComboBox({ \n"
	    		+"width:100,\n"
	    		+"selectOnFocus:true,\n"
	    		+"transform:'FencbDropDown',\n"
	    		+"lazyRender:true,\n"
	    		+"triggerAction:'all',\n"
	    		+"typeAhead:true,\n"
	    		+"forceSelection:true,\n"
	    		+"editable:false \n"
	    	+"})";
	         
	        tiaoj_returnvalue_changb="	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n";
       }else{
        	fencb="";
        	tiaoj_returnvalue_changb="";	
        }
        
	    String Strtmpfunction="var form = new Ext.form.FormPanel({ "
	        + "baseCls: 'x-plain', \n"		        		
	        + "labelAlign:'right', \n"       	
	        + "defaultType: 'radio',\n"				
	        + "items: [ \n"			        
		    + "{ \n"
		    + "    	xtype:'textfield', \n"
		    + "    	fieldLabel:'����ѡ��',\n"
		    + "    	width:0 \n"
		    + "    },	\n"				
			+ " { \n"
			+ "		boxLabel:'��������', \n"                		
		    + "     anchor:'95%', \n"
		    +       danx 
		    + "     Value:'fahrq', \n"			            			            												
			+ "		name:'test',\n"
			+ "		listeners:{ \n"
			+ "				'focus':function(r,c){\n"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				},\n"
			+ "				'check':function(r,c){ \n"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
			+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
			+ "				}\n"
			+ "		} \n"
			
			+ "	},\n"
			+ "	{  \n"   
			
			+ "		boxLabel:'��������',\n"  
			+ "		Value:'daohrq', \n"             		
			+ "     anchor:'95%',	\n"	
			+       danx1
			+ "		name:'test',\n"	
			+ "		listeners:{ \n"
			+ "				'focus':function(r,c){ \n"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "				}, \n"
			+ "				'check':function(r,c){\n"
			+ "					document.getElementById('rbvalue').value=r.Value;\n"
			+ "					\tif(document.getElementById('TEXT_RADIO_RQSELECT_VALUE')==''){	\n"
			+ " 				\t	document.getElementById('TEXT_RADIO_RQSELECT_VALUE')=document.getElementById('rbvalue').value;} \n"
			+ "				}\n"

			+ "		}	\n"																	        
			+ "	}		\n"	
			+ 	fencb+"\n"
            +   jieskdl+"\n"
			+ "]\n"	
			+ " });\n"
        
			+ " win = new Ext.Window({\n"
			+ " el:'hello-win',\n"
			+ "layout:'fit',\n"
			+ "width:500,\n"
			+ "height:400,\n"
			+ "closeAction:'hide',\n"
			+ " plain: true,\n"
			+ "title:'����',\n"
			+ "items: [form],\n"
            				
			+ "buttons: [{\n"
			+ "   text:'ȷ��',\n"
			+ "   handler:function(){  \n"                  		
			+ "  	win.hide();\n"
			+ 		tiaoj_returnvalue_changb+"\n"
			+ "      	document.getElementById('Jieskdl').value=jieskdl.getRawValue();\n"
			+ "			document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 		document.getElementById('RefurbishButton').click(); \n"
			+ "  	}   \n"                
			+ "},{\n"
			+ "   text: 'ȡ��',\n"
			+ "   handler: function(){\n"
			+ "       win.hide();\n"
			+ "		document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
            + "     document.getElementById('Jieskdl').value='';  \n"
			+ "   }\n"
			+ "}]\n"
           
			+ " });win.show();win.hide();";
	    
	       
        String sql="";
     
        sql = 
        	"select cp.id,nvl(zl.id,0) as shihzlb_id,\n" +
        	"       decode(g.mingc,null,'�ϼ�',g.mingc) as gonghdw,\n" + 
        	"       to_char(cp.fahrq,'yyyy-MM-dd') as fahrq,to_char(cp.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
        	"       decode(g.mingc,null,'',sum(cp.biaoz)) as biaoz,\n" + 
        	"       decode(g.mingc,null,'',sum(cp.yingk)) as yingk,\n" + 
        	"       decode(g.mingc,null,'',sum(cp.yuns)) as yuns,\n" + 
        	"       decode(g.mingc,null,'',sum(j.jingz)) as jingz,\n" + 
        	"       decode(g.mingc,null,'',h.bianh) as hetbh,\n" + 
        	"       decode(g.mingc,null,'',decode(sum(j.jingz),0,0,sum(j.jingz*zl.caco3)/sum(j.jingz))) as caco3,\n" + 
        	"       decode(g.mingc,null,'',decode(sum(j.jingz),0,0,sum(j.jingz*zl.xid)/sum(j.jingz))) as xid,\n" + 
        	"       decode(g.mingc,null,'',decode(sum(j.jingz),0,0,sum(j.jingz*zl.mgco3)/sum(j.jingz))) as mgco3\n" + 
        	"from shihcpb cp,shihgysb g,shihhtb h,shihcyb cy,shihzlb zl,chebb c,\n" + 
        	"      (select (maoz-piz-koud) as jingz\n" + 
        	"      from shihcpb cp,shihgysb g\n" + 
        	"	 		where "+riq+" >= to_date('"+getRiq1()+"', 'yyyy-mm-dd')				\n" + 
            "			and "+riq+" <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')				\n" +
            "			and cp.gongysb_id = g.id(+)"	+
        			this.getWhere()+ 
        	"		) j \n" +
        	"where cp.gongysb_id = g.id\n" + 
        	"      and cp.shihhtb_id = h.id(+)\n" + 
        	"      and cp.shihcyb_id = cy.id(+)\n" + 
        	"      and cy.shihzlb_id = zl.id(+)\n" + 
        	"      and cp.chebb_id = c.id(+)\n" + 
        	"      and cp.shihjsb_id = 0\n" + 
        		this.getWhere()+ 
        	"	   and "+riq+" >= to_date('"+getRiq1()+"', 'yyyy-mm-dd')				\n" + 
            "	   and "+riq+" <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')				\n" +
        	"group by rollup(cp.id,zl.id,g.mingc,cp.fahrq,cp.daohrq,h.bianh)\n" + 
        	"having not (grouping(cp.id)=0 and grouping(h.bianh)=1)\n" + 
        	"order by g.mingc,fahrq";


        	
        ResultSetList rsl = con.getResultSetList(sql);  
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		egu.setDefaultsortable(false);
		egu.setWidth(Locale.Grid_DefaultWidth);// ����ҳ��Ŀ��,������������ʱ��ʾ������
		egu.getColumn("id").setHidden(true);
		egu.getColumn("shihzlb_id").setHidden(true);
		egu.getColumn("gonghdw").setHeader("������λ");
		egu.getColumn("fahrq").setHeader("��������");
		egu.getColumn("daohrq").setHeader("��������");
		egu.getColumn("biaoz").setHeader("Ʊ��");
		egu.getColumn("yingk").setHeader("ӯ��");
		egu.getColumn("yuns").setHeader("����");
		egu.getColumn("jingz").setHeader("����");
		egu.getColumn("hetbh").setHeader("��ͬ��");
		egu.getColumn("caco3").setHeader("CaO(%)");
		egu.getColumn("xid").setHeader("ϸ��(%)");
		egu.getColumn("mgco3").setHeader("MaO(%)");		
		
		
		// �趨�г�ʼ���
		egu.getColumn("gonghdw").setWidth(100);
		egu.getColumn("hetbh").setWidth(80);
		egu.getColumn("fahrq").setWidth(80);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("jingz").setWidth(60);
		egu.getColumn("hetbh").setWidth(80);
		egu.getColumn("caco3").setWidth(60);
		egu.getColumn("xid").setWidth(60);
		egu.getColumn("mgco3").setWidth(60);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);// �趨grid���Ա༭
		egu.addPaging(0);// ���÷�ҳ
        
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText(biaoq);
		DateField df = new DateField();
		df.setReadOnly(true);
		df.setValue(this.getRiq1());
		df.Binding("RIQ1", "");// ��htmlҳ�е�id��,���Զ�ˢ��
		df.setId("riq1");
		
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("��:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "forms[0]");// ��htmlҳ�е�id��,���Զ�ˢ��
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("��Ӧ��:");
		ComboBox comb6 = new ComboBox();
		comb6.setTransform("GongysDropDown");
		comb6.setId("gonghdw");
		comb6.setEditable(false);
		comb6.setLazyRender(true);// ��̬��
		comb6.setWidth(135);
		comb6.setReadOnly(true);
		egu.addToolbarItem(comb6.getScript());
		egu.addOtherScript("gonghdw.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("-");// ���÷ָ���

		egu.addTbarText("��ͬ��:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("HthDropDown");
		comb5.setId("Heth");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// ��̬��
		comb5.setWidth(135);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		

		// �趨�������������Զ�ˢ��
//		egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// ���÷ָ���
		//egu.addToolbarButton(GridButton.ButtonType_ShowDIV, null);
		egu.addToolbarItem("{"+new GridButton("ˢ��","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		egu.addTbarText("-");		
		egu.addToolbarItem("{"+new GridButton("����","function(){ if(win){ win.show(this);"+"}"
//				+ " win.show(this);	\n" 
				+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n" 
		 		+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
		 		+ "	\t}	\n"
				+ "}").getScript()+"}");  
        egu.addTbarText("-");
        
//      �߼�������㰴ťʱ�����ж���û��ѡ���ͬ�����ѡ�ˣ����ж���û����ˣ����û�о���ʾ(confirm)��  
        egu.addToolbarButton("����",GridButton.ButtonType_SubmitSel_condition,"JiesButton","if(Heth.getRawValue()=='��ѡ��'){	\n	"
									+ " Ext.MessageBox.alert('��ʾ��Ϣ','��ѡ���ͬ��');	\n"
									+ " return ;	\n"
									+ " }"
									+ "var flag=true;"
									+ "for (var i=0;i<Het.length;i++){\n" 
									+ "		if(Heth.getRawValue()==Het[i][0] && Het[i][1]==0){\n"  
									+ "			Ext.MessageBox.confirm('��ʾ��Ϣ','��ͬû����˲��ܱ�����㵥',Shenhzt_y_n);\n"  
        							+ "	  		flag=false;\n break;										\n" 
									+ "		}else if(Heth.getRawValue()==Het[i][0] && Het[i][1]==1){\n" 
									+ "			flag=true;		\n"		
									+ "		}	\n"
									+ "}	\n"
									+ "if(!flag){	\n"
									+ "		return;	\n"
									+ "}	\n"
									+ "function Shenhzt_y_n(fb){ \n"
									+ "	\n	"
									+ "		if(fb=='yes'){ \n"
									+ "			var Mrcd = gridDiv_grid.getSelectionModel().getSelections();\n" +
									"			for(i = 0; i< Mrcd.length; i++){\n" + 
									"  				if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n" + 
									"  				gridDiv_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace('<','&lt;').replace('>','&gt;')+ '</ID>'+ '<SHIHZLB_ID update=\"true\">' + Mrcd[i].get('SHIHZLB_ID')+ '</SHIHZLB_ID>'+ '<GONGHDW update=\"true\">' + Mrcd[i].get('GONGHDW').replace('<','&lt;').replace('>','&gt;')+ '</GONGHDW>'+ '<FAHRQ update=\"true\">' + Mrcd[i].get('FAHRQ').replace('<','&lt;').replace('>','&gt;')+ '</FAHRQ>'+ '<DAOHRQ update=\"true\">' + Mrcd[i].get('DAOHRQ').replace('<','&lt;').replace('>','&gt;')+ '</DAOHRQ>'+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ').replace('<','&lt;').replace('>','&gt;')+ '</BIAOZ>'+ '<YINGK update=\"true\">' + Mrcd[i].get('YINGK').replace('<','&lt;').replace('>','&gt;')+ '</YINGK>'+ '<YUNS update=\"true\">' + Mrcd[i].get('YUNS').replace('<','&lt;').replace('>','&gt;')+ '</YUNS>'+ '<JINGZ update=\"true\">' + Mrcd[i].get('JINGZ').replace('<','&lt;').replace('>','&gt;')+ '</JINGZ>'+ '<HETBH update=\"true\">' + Mrcd[i].get('HETBH').replace('<','&lt;').replace('>','&gt;')+ '</HETBH>'+ '<CACO3 update=\"true\">' + Mrcd[i].get('CACO3').replace('<','&lt;').replace('>','&gt;')+ '</CACO3>'+ '<XID update=\"true\">' + Mrcd[i].get('XID').replace('<','&lt;').replace('>','&gt;')+ '</XID>'+ '<MGCO3 update=\"true\">' + Mrcd[i].get('MGCO3').replace('<','&lt;').replace('>','&gt;')+ '</MGCO3>' + '</result>' ; }\n" + 
									"			if(gridDiv_history==''){\n" + 
									"  				Ext.MessageBox.alert('��ʾ��Ϣ','û��ѡ��������Ϣ');\n" + 
									"			}else{\n" + 
									"  				var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+'</result>';document.getElementById('JiesButton').click();\n" + 
									"  				Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" + 
									"			}"
									+ "     } \n"
									+ "} \n"
									);
    
        egu.addOtherScript("function gridDiv_save(rec){	\n" +
				"	\tif(rec.get('GONGHDW')=='�ϼ�'){	\n" +
				"	\treturn 'continue';	\n" +
				"	\t}}");

		// ---------------ҳ��js�ļ��㿪ʼ------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//�趨��8�в��ɱ༭,�����Ǵ�0��ʼ��
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// �趨��Ӧ���в��ɱ༭
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// �趨�糧��Ϣ�в��ɱ༭
		sb.append("});");
		
		egu.addOtherScript(sb.toString());*/
//		egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
//				+ " 	var ches=0,biaoz=0,yingk=0,yuns=0,jingz=0;	\n"
//				+ "		var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"	
//				+ " 	for(var i=0;i<rec.length;i++){ \n"
//				+ " 		if(rec[i].get('SHIHZLB_ID')==0){ \n"
//				+ " 			Ext.MessageBox.alert('��ʾ��Ϣ','û����������ѡ��');	\n"
//				+ "				gridDiv_grid.getSelectionModel().deselectRow(row);	\n"	//ȡ��ѡ����
//				+ " 		}else {	\n"
//				+ " 			if(''!=rec[i].get('ID')){ \n"
//				+ "					ches++;	\n"		
//				+ "					biaoz+=eval(rec[i].get('BIAOZ'));	\n"
//				+ " 				yingk+=eval(rec[i].get('YINGK')); 	\n" 
//				+ " 				yuns+=eval(rec[i].get('YUNS'));		\n"	
//				+ " 				jingz+=eval(rec[i].get('JINGZ'));	\n"
//				+ " 			}	\n"
//				+ "			}	\n"
//				+ " 	}	\n"
//				+ " 	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('DAOHRQ',ches);	\n"
//				+ " 	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
//				+ " 	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
//				+ " 	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);		\n"
//				+ " 	gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"	
//				+ " });		\n");
		
		
//		��ѡ��ȫѡ�����¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript("gridDiv_grid.on('click',function(){		\n"
				+ "	var rec = gridDiv_grid.getStore();	\n"
				+ "	var a = 0; \n"
				+ " if(rec.getCount()>0){	\n"
			 	+ "		for(var i=0;i<rec.getCount();i++){ \n"
			 	+ "			if(rec.getAt(i).get('SHIHZLB_ID')==0){ \n"
				+ "				gridDiv_grid.getSelectionModel().deselectRow(i); \n"
				+ "			}else{"
				+ " 			a++;	\n" 
				+ "			} \n"
			 	+ "		} \n"
			 	+ "		if(a<1){ \n"
			 	+ "			Ext.MessageBox.alert('��ʾ��Ϣ','û����������ѡ��'); \n"
				+ "		} \n"
				+ "		reCountToolbarNum(this); \n"
				+ "	}	\n"
				+ "});	\n");
		
		
//		���¼���ѡ����ĺϼ�ֵ
		egu.addOtherScript(" function reCountToolbarNum(obj){	\n "
 				+ " 	var rec;	\n"
 				+ " 	var ches=0,biaoz=0,yingk=0,yuns=0,jingz=0;	\n"
 				+ " 	rec = obj.getSelectionModel().getSelections();				\n"		
 				+ " 	for(var i=0;i<rec.length;i++){								\n" 
 				+ " 		if(0!=rec[i].get('ID')){									\n"
 				+ " 			ches++;												\n"
				+ " 			biaoz+=eval(rec[i].get('BIAOZ'));						\n" 
				+ " 			yingk+=eval(rec[i].get('YINGK'));						\n"
				+ " 			yuns+=eval(rec[i].get('YUNS'));						\n"	
				+ " 			jingz+=eval(rec[i].get('JINGZ'));						\n"
				+ " 		}															\n"
				+ " 	}																\n"
				+ " 	if(gridDiv_ds.getCount()>0){									\n"	
				+ " 		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('DAOHRQ',ches);	\n"
				+ " 		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
				+ " 		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
				+ " 		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
				+ " 		gridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"	
				+ "		}\n"
				+ " } \n");
				
		
		// ---------------ҳ��js�������--------------------------
		egu.addOtherScript(Strtmpfunction);
		setExtGrid(egu);
		con.Close();
	}
	
	private String getWhere(){
		
		String where="";
		String mstr_gongys_id=getGongysValue().getStrId();
		//��Ӧ��
//		����û�û���������������г�ȫ���ķ�����Ϣ
		if(!(getGongysValue().getStrId()==null||getGongysValue().getStrId().equals("-1"))){//ȫ��
			
			where+=" and g.id="+mstr_gongys_id+"\n";
		}
        	
    	
        if(((Visit) getPage().getVisit()).isFencb()){
        	
        	where+="and cp.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
        }else{
        	
        	where+="and cp.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
        }
		
		return where;
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
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
//			��Ҫע�ͣ���ҳ��ı��淽����һ������Ϊд�ɣ�Ҫ�����ֶ�ʱ��ע��
			visit.setActivePageName(getPageName().toString());
			
			visit.setString1("");	//Rbvalue��������ѡ��
			visit.setString3("");	//���e
			visit.setString5("");	//riq1
			visit.setString6("");	//riq2
			visit.setString7("");	//����۶���
			visit.setString9("");	//���ú˶�״̬
			visit.setLong1(0);		//�糧��Ϣ��id
			visit.setLong2(0);		//��������
			visit.setLong8(0);		//��ͬ��id
			visit.setList1(null);
			setHthValue(null);		//4
			setHthModel(null);
			setGongysValue(null);	//2
			setGongysModel(null);
			setFencbValue(null);	//5
			setFencbModel(null);
			
			
			getGongysModels();		//2
			getHthModels();			//4
			getFencbModels();		//5
			
			visit.setboolean1(false);	//����change
			visit.setboolean2(false);	//������λchange
		}
		if(visit.getboolean1()){
			visit.setboolean1(false);
			getGongysModels();
			getHthModels();
		}
		if(visit.getboolean2()){
			visit.setboolean2(false);
			getHthModels();
		}

		getSelectData();
	}
	
    public String rb1(){
    	if(getRbvalue()==null||getRbvalue().equals("")){
    		
    		
    	    	return "fahrq";
    	    	
    		
    	}else{
    		if(getRbvalue().equals("fahrq")){
    			return "fahrq";
    		}
    		else return "daohrq";
    	}
    }

	//��ͬ�� 
	public boolean _Hthchange = false;

	public IDropDownBean getHthValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getHthModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setHthValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean4()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public void setHthModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public IPropertySelectionModel getHthModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getHthModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public IPropertySelectionModel getHthModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

//			һ����û�й�Ӧ��
//			�����ж�ϵͳ����û�к�ͬ

			
			String sql="";
			List.add(new IDropDownBean(-1, "��ѡ��"));
			
			if(getGongysValue().getStrId()==null||getGongysValue().getStrId().equals("-1")){	//ȫ��
				
				if(((Visit) getPage().getVisit()).isFencb()){
					
//					�ֳ���
					sql = "select distinct h.id,h.bianh from shihhtb h where h.diancxxb_id="+MainGlobal.getProperId(getFencbModel(),this.getChangb())
					+ "	and jiesrq>to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by bianh ";
					
				}else{
					
					sql = "select distinct h.id,h.bianh from shihhtb h where h.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()
					+" 	and jiesrq>to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by bianh ";
				}
			
			}else{
//				ѡ���˹�Ӧ��
				String mstr_gongys_id=getGongysValue().getStrId();
				
				if(mstr_gongys_id.equals("")){
					
					setMsg("��ú��û�ж�Ӧ�Ĺ�Ӧ����Ϣ��");
				}else{
					
					if(((Visit) getPage().getVisit()).isFencb()){
						
						sql = "select distinct h.id,h.bianh from shihhtb h where h.diancxxb_id="+MainGlobal.getProperId(getFencbModel(),this.getChangb())
						+" and shihgysb_id="+mstr_gongys_id+" and jiesri>to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by bianh ";
						
					}else{
						
						sql = "select distinct h.id,h.bianh from shihhtb h where h.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()
						+" and shihgysb_id="+mstr_gongys_id+" and jiesrq>to_date('"+getRiq2()+"', 'yyyy-mm-dd') order by bianh ";
					}
				}
			}
			ResultSetList rsl = con.getResultSetList(sql);
			if(rsl.getRows()==0&&!getGongysValue().getStrId().equals("-1")){
				
				setMsg("�ù�Ӧ����ϵͳ��û�ж�Ӧ�ĺ�ͬ��");
			}
			
			while (rsl.next()) {

				List.add(new IDropDownBean(rsl.getLong("id"), rsl.getString("bianh")));
			}
			rsl.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	//״̬
	public String getHetArrayScript() {
		JDBCcon con = new JDBCcon();
        StringBuffer HetArrayScript = new StringBuffer();
        StringBuffer sb = new StringBuffer();
        String sql = "";
        String tmp="";
        int i=0;
        try {
        	if(getGongysValue().getStrId()==null||getGongysValue().getStrId().equals("-1")){	//ȫ��
    			
    			if(((Visit) getPage().getVisit()).isFencb()){
//    				�ֳ���
    				sb.append("select bianh,shenhzt from shihhtb h")
    					.append(" where h.diancxxb_id = ").append(MainGlobal.getProperId(getFencbModel(),this.getChangb()))
    					.append(" and jiesrq>to_date('").append(getRiq2()).append("', 'yyyy-mm-dd') order by bianh ");
    			}else{
    				sb.append("select bianh,shenhzt from shihhtb h")
    					.append(" where h.diancxxb_id = ").append(((Visit) getPage().getVisit()).getDiancxxb_id())
    					.append(" and jiesrq>to_date('").append(getRiq2()).append("', 'yyyy-mm-dd') order by bianh ");
    			}
    		}else{
    			String mstr_gongys_id=getGongysValue().getStrId();
    			
    			if(((Visit) getPage().getVisit()).isFencb()){
    				sb.append("select bianh,shenhzt from shihhtb h")
    					.append(" where h.diancxxb_id = ").append(MainGlobal.getProperId(getFencbModel(),this.getChangb()))
    					.append(" and shihgysb_id=").append(mstr_gongys_id)
    					.append(" and jiesrq>to_date('").append(getRiq2()).append("', 'yyyy-mm-dd') order by bianh ");
    			}else{
    				sb.append("select bianh,shenhzt from shihhtb h")
    					.append(" where h.diancxxb_id = ").append(((Visit) getPage().getVisit()).getDiancxxb_id())
    					.append(" and shihgysb_id=").append(mstr_gongys_id)
    					.append(" and jiesrq>to_date('").append(getRiq2()).append("', 'yyyy-mm-dd') order by bianh ");
    			}
    		}
        	ResultSet rstmp=con.getResultSet(sb.toString());
            
            while(rstmp.next()){
               i++;
            }
            rstmp.close();
           
            for(int j=0;j<i;j++){
              
               if(j==0){
                   tmp="new Array()";                   
               }else{
                   tmp+=",new Array()";
               }
               
            }
            i=0;
            HetArrayScript.append("var Het=new Array("+tmp+");");
           
            ResultSet rs=con.getResultSet(sb.toString());
            while(rs.next()){
               
               String bianh=rs.getString("bianh");
               String shenhzt=rs.getString("shenhzt");
               
               HetArrayScript.append("Het["+i+"][0] ='"+bianh+"';");
               HetArrayScript.append("Het["+i+"][1] ='"+shenhzt+"';");
               i++;
           }
           rs.close();           
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        con.Close();
        return HetArrayScript.toString();
	}

	// �õ���½�û����ڵ糧���߷ֹ�˾������
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
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}
	// �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����d
	public String getIDropDownDiancmc(String diancmcId) {
		if (diancmcId == null || diancmcId.equals("")) {
			diancmcId = "1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="
				+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

//����
	public boolean _Fencbchange = false;
	public IDropDownBean getFencbValue() {
		
		if (((Visit) getPage().getVisit()).getDropDownBean5() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean5((IDropDownBean) getFencbModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean5();
	}

	public void setFencbValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean5()){
			
			((Visit) getPage().getVisit()).setboolean3(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean5(Value);
	}

	public IPropertySelectionModel getFencbModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel5() == null) {

			getFencbModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	public void setFencbModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel5(value);
	}

	public IPropertySelectionModel getFencbModels() {
		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql,"��ѡ��"));
			return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

//	��Ӧ�� 
	public boolean _GYSchange = false;

	public IDropDownBean getGongysValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getGongysModel().getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setGongysValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean2(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setGongysModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getGongysModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getGongysModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getGongysModels() {
		String sql="select distinct gys.id,gys.mingc from shihgysb gys,shihcpb cp\n" +
				"       where cp.gongysb_id=gys.id \n" + 
				"             and "+this.rb1()+">=to_date('"+this.getRiq1()+"','yyyy-MM-dd')\n" + 
				"             and "+this.rb1()+"<=to_date('"+this.getRiq2()+"','yyyy-MM-dd')\n" + 
				"             order by mingc ";
		
		((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql,"��ѡ��"));
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	
	public long getTreeid(){
		return getGongysValue().getId();
	}

	// �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧

	public String getWunScript(){
		
		return "";
	}
}