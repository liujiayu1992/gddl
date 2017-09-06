package com.zhiren.dc.hesgl.meikjs;

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
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.Locale;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Meikjs extends BasePage implements PageValidateListener {
	private String msg = "";

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	// 绑定日期
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
	boolean riq2change = false;

	private String riq2;

	public String getRiq2() {
		if (riq2 == null || riq2.equals("")) {
			riq2 = DateUtil.FormatDate(new Date());
		}
		return riq2;
	}

	public void setRiq2(String riq2) {

		if (this.riq2 != null && !this.riq2.equals(riq2)) {
			this.riq2 = riq2;
			riq2change = true;
		}

	}
	private String Rbvalue;

	public String getRbvalue() {
		return Rbvalue;
	}

	public void setRbvalue(String rbvalue) {
		Rbvalue = rbvalue;
	}
//	长别下拉框取值

	public String getChangb() {
		return ((Visit) this.getPage().getVisit()).getString3();
	}
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}
	
//	验收编号下拉框取值
	
	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}
	public void setYansbh(String yansbh) {
		((Visit) this.getPage().getVisit()).setString4(yansbh);
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
	private boolean _RefurbishChick = false;
    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
            getSelectData();
    }
//
	private boolean _SaveChick = false;

	public void SaveButton(IRequestCycle cycle) {
		_SaveChick = true;
	}

	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		
		if (_SaveChick) {
			_SaveChick = false;
			Save();
			getSelectData();
		}
	}
	
	public void getSelectData() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
//		 ********************工具栏************************************************
	    String biaoq="发货日期";
	    String riq="fahrq";
	    String danx="";
        String danx1="";
        String fencb="";
        String changb_value="";
        String yansbh="";
        String qued="";
        if(getRbvalue()==null){
			
			biaoq="发货日期";
			riq="fahrq";
			danx="checked:true ,   \n";
		}else if(getRbvalue().equals("fahrq")){
			
			
			biaoq="发货日期";
			riq="fahrq";
			danx="checked:true ,   \n";
		}else if(getRbvalue().equals("daohrq")){
			biaoq="到货日期";
			riq="daohrq";
			danx1="checked:true ,   \n";
		
		}
        if(visit.isFencb()==true){
         fencb=" ,{ \n"
        	+"xtype:'textfield',\n" 
        	+"fieldLabel:'厂别选择',\n"
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
         changb_value="document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue()";
       }else{
        	fencb="";
        		
        }
    	if(MainGlobal.getXitxx_item("结算",Locale.shiyysbh_jies, String.valueOf(visit.isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):visit.getDiancxxb_id()), "否").equals("是")){
	        qued="document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();";
			yansbh=	" ,{  \n"
				+ " \txtype:'textfield',\n" 
				+ " \tfieldLabel:'验收编号',\n" 
				+ "	\twidth:0	\n"  
				+ " },	\n"
				+ "	\tyansbh=new Ext.form.ComboBox({	\n" 
				+ "	\twidth:150,	\n" 
				+ " \tselectOnFocus:true,	\n" 
				+ " \ttransform:'YansbhDropDown',	\n" 
				+ " \tlazyRender:true,	\n" 
				+ "	\ttriggerAction:'all',	\n" 
				+ " \ttypeAhead:true,	\n" 
				+ " \tforceSelection:true,	\n" 
				+ " \teditable:false,	\n"
				+ "	\tlisteners:{select:function(own,rec,index){Ext.getDom('YansbhDropDown').selectedIndex=index}}"
				+ "	\t})";
			}else{
				yansbh="";
			}

        
        
	    String Strtmpfunction="var form = new Ext.form.FormPanel({ "
	        + "baseCls: 'x-plain', \n"		        		
	        + "labelAlign:'right', \n"       	
	        + "defaultType: 'radio',\n"				
	        + "items: [ \n"			        
		    + "{ \n"
		    + "    	xtype:'textfield', \n"
		    + "    	fieldLabel:'日期选择',\n"
		    + "    	width:0 \n"
		    + "    },	\n"				
			+ " { \n"
			+ "		boxLabel:'发货日期', \n"                		
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
			
			+ "		boxLabel:'到货日期',\n"  
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
			+ fencb+"\n"
            +yansbh+"\n"
			+ "]\n"	
			+ " });\n"
        
			+ " win = new Ext.Window({\n"
			+ " el:'hello-win',\n"
			+ "layout:'fit',\n"
			+ "width:500,\n"
			+ "height:300,\n"
			+ "closeAction:'hide',\n"
			+ " plain: true,\n"
			+ "title:'条件',\n"
			+ "items: [form],\n"
            				
			+ "buttons: [{\n"
			+ "    text:'确定',\n"
			+ "   handler:function(){  \n"                  		
			+ "  		win.hide();\n"
			+ changb_value+"\n"
			+qued+"\n"
			//+ "		document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();	\n"
			+ "		document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 	document.getElementById('RefurbishButton').click(); \n"
			+ "  	}   \n"                
			+ "},{\n"
			+ "   text: '取消',\n"
			+ "   handler: function(){\n"
			+ "       win.hide();\n"
			+ "		document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
			+ "		document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"

			+ "   }\n"
			+ "}]\n"
           
			+ " });";
	    
	       
        String sql="";
        String tree="";
        String jieslx="";
        String fencb_id="";
      
       if(getTreeid()==null||getTreeid().equals("0")){
    		tree="";    			
    	}else{
    		tree="and (m.id="+getTreeid()+" or g.id="+getTreeid()+" )\n";
    	}
        if(this.getYansbh().equals("请选择")||this.getYansbh().equals("")){
        	if(getJieslxValue().getId()==0){
        		  jieslx="and f.jiesb_id=0";
        	}
        	else if(getJieslxValue().getId()==1){
        		jieslx="and f.jiesb_id=0";
        	}
        	else if(getJieslxValue().getId()==2){
        		jieslx="and f.id=cp.fahb_id and cp.id=d.chepb_id(+) and d.yunfjsb_id=0  ";
        	}else {
        		jieslx="";
        	}
            if(visit.isFencb()==true){
            	fencb_id="and f.diancxxb_id="+MainGlobal.getProperId(this.getFencbModel(),this.getChangb());
            }else{
            	fencb_id="and f.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id();
            }
            	
        	
        	 sql = 

     			"select f.lie_id as id,decode(g.mingc,null,'合计',g.mingc) as fahdw, m.mingc as meikdw,sum(f.ches) as ches,\n" +
     			"       h.hetbh,to_char(f.fahrq,'yyyy-MM-dd') as fahrq,to_char(f.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
     			"       c.mingc as faz,sum(f.biaoz) as biaoz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
     			"       sum(f.jingz) as jingz,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qnet_ar)/sum(f.biaoz),2)) as qnet_ar,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.std)/sum(f.biaoz),2)) as std,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.mt)/sum(f.biaoz),2)) as mt,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.mad)/sum(f.biaoz),2)) as mad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.aar)/sum(f.biaoz),2)) as aar,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.aad)/sum(f.biaoz),2)) as aad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.ad)/sum(f.biaoz),2)) as ad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.vad)/sum(f.biaoz),2)) as vad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.vdaf)/sum(f.biaoz),2)) as vdaf,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.stad)/sum(f.biaoz),2)) as stad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.had)/sum(f.biaoz),2)) as had,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qbad)/sum(f.biaoz),2)) as qbad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qgrad_daf)/sum(f.biaoz),2)) as qgrad_daf\n" + 
     			"       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z,chepb cp,danjcpb d\n" + 
     			"       where f.gongysb_id=g.id and f.meikxxb_id=m.id\n" + 
     			"       and f.hetb_id=h.id(+) and f.faz_id=c.id and f.zhilb_id=z.id\n" + 
     			 jieslx+
     			 fencb_id+
     		  //"                  --and z.liucztb_id=1\n" + 
     		  //"                  --and f.liucztb_id=1\n" + 
     		  //"and (m.id="+getTreeid()+" or g.id="+getTreeid()+" )\n"+
     			"and "+riq+" >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
                "and "+riq+" <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" +
                 tree+
                 
     			"       group by rollup(f.lie_id,g.mingc,m.mingc,h.hetbh,f.fahrq,f.daohrq,c.mingc)\n" + 
     			"       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)\n" + 
     			"       order by g.mingc,fahrq";
        }else{
        	 sql=
          	   
          	    " select f.lie_id as id,decode(g.mingc,null,'合计',g.mingc) as fahdw, m.mingc as meikdw,sum(f.ches) as ches,\n" +
      			"       h.hetbh,to_char(f.fahrq,'yyyy-MM-dd') as fahrq,to_char(f.daohrq,'yyyy-MM-dd') as daohrq,\n" + 
      			"       c.mingc as faz,sum(f.biaoz) as biaoz,sum(f.yingk) as yingk,sum(f.yuns) as yuns,\n" + 
      			"       sum(f.jingz) as jingz,\n" + 
      			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qnet_ar)/sum(f.biaoz),2)) as qnet_ar,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.std)/sum(f.biaoz),2)) as std,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.mt)/sum(f.biaoz),2)) as mt,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.mad)/sum(f.biaoz),2)) as mad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.aar)/sum(f.biaoz),2)) as aar,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.aad)/sum(f.biaoz),2)) as aad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.ad)/sum(f.biaoz),2)) as ad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.vad)/sum(f.biaoz),2)) as vad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.vdaf)/sum(f.biaoz),2)) as vdaf,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.stad)/sum(f.biaoz),2)) as stad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.had)/sum(f.biaoz),2)) as had\n," + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qbad)/sum(f.biaoz),2)) as qbad,\n" + 
     			"       decode(sum(f.biaoz),0,0,round(sum(f.biaoz*z.qgrad_daf)/sum(f.biaoz),2)) as qgrad_daf\n" + 
      			"       from fahb f,gongysb g,meikxxb m,hetb h,chezxxb c,zhilb z\n" + 
      			"       where f.gongysb_id=g.id and f.meikxxb_id=m.id\n" + 
      			"       and f.hetb_id=h.id(+) and f.faz_id=c.id and f.zhilb_id=z.id\n" + 
      			        fencb_id+
      		  //"       and jiesdid=0 \n"+
      		  //"       --and z.liucztb_id=1\n" + 
      		  //"       --and f.liucztb_id=1\n" + 
              //"       and (m.id="+getTreeid()+" or g.id="+getTreeid()+" )\n"+
              //"       and "+riq+" >= to_date('"+getRiqi()+"', 'yyyy-mm-dd')\n" + 
              //"       and "+riq+" <= to_date('"+getRiq2()+"', 'yyyy-mm-dd')\n" +
      			"       and f.yansbhb_id='"+MainGlobal.getProperId(this.getYansbhModel(),this.getYansbh())+"'\n"+
      			"       group by rollup(f.lie_id,g.mingc,m.mingc,h.hetbh,f.fahrq,f.daohrq,c.mingc)\n" + 
      			"       having not (grouping(c.mingc)=1 and grouping(f.lie_id)=0)\n" + 
      			"       order by g.mingc,fahrq";
        }
         
        ResultSetList rsl = con.getResultSetList(sql);  
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		egu.setTableName("fahb");
		egu.getColumn("id").setHidden(true);
		egu.getColumn("fahdw").setHeader("发货单位");
		egu.getColumn("meikdw").setHeader("煤矿单位");
		egu.getColumn("ches").setHeader("车数");
		egu.getColumn("hetbh").setHeader("合同");
		egu.getColumn("fahrq").setHeader("发货日期");
		egu.getColumn("daohrq").setHeader("到货日期");
		egu.getColumn("faz").setHeader("发站");
		egu.getColumn("biaoz").setHeader("票重");
		egu.getColumn("yingk").setHeader("盈亏");
		egu.getColumn("yuns").setHeader("运损");
		egu.getColumn("jingz").setHeader("净重");		
		egu.getColumn("std").setHeader("Std(%)");
		egu.getColumn("mt").setHeader("Mt(%)");
		egu.getColumn("mad").setHeader("Mad(%)");
		egu.getColumn("aar").setHeader("Aar(%)");
		egu.getColumn("aad").setHeader("Aad(%)");
		egu.getColumn("ad").setHeader("Ad(%)");
		egu.getColumn("vad").setHeader("Vad(%)");
		egu.getColumn("vdaf").setHeader("Vdaf(%)");
		egu.getColumn("stad").setHeader("Stad(%)");		
		egu.getColumn("had").setHeader("Had(%)");
		egu.getColumn("qnet_ar").setHeader("Qnetar(MJ/Kg)");
		egu.getColumn("qbad").setHeader("Qbad(MJ/Kg)");
		egu.getColumn("qgrad_daf").setHeader("Qgrad_daf(MJ/Kg)");
		


		// 设定列初始宽度
//		egu.getColumn("id").setWidth(80);
		egu.getColumn("fahdw").setWidth(100);
		egu.getColumn("meikdw").setWidth(100);
		egu.getColumn("ches").setWidth(60);
		egu.getColumn("hetbh").setWidth(70);
		egu.getColumn("fahrq").setWidth(70);
		egu.getColumn("daohrq").setWidth(70);
		egu.getColumn("faz").setWidth(60);
		egu.getColumn("biaoz").setWidth(60);
		egu.getColumn("yingk").setWidth(60);
		egu.getColumn("yuns").setWidth(60);
		egu.getColumn("jingz").setWidth(60);		
		egu.getColumn("std").setWidth(60);
		egu.getColumn("mt").setWidth(60);
		egu.getColumn("mad").setWidth(60);
		egu.getColumn("aar").setWidth(60);
		egu.getColumn("aad").setWidth(60);
		egu.getColumn("ad").setWidth(60);
		egu.getColumn("vad").setWidth(60);
		egu.getColumn("vdaf").setWidth(60);
		egu.getColumn("stad").setWidth(60);		
		egu.getColumn("had").setWidth(60);
		egu.getColumn("qnet_ar").setWidth(80);
		egu.getColumn("qbad").setWidth(80);
		egu.getColumn("qgrad_daf").setWidth(100);

		egu.setGridType(ExtGridUtil.Gridstyle_Read);// 设定grid可以编辑
		egu.addPaging(100);// 设置分页
		egu.setWidth(1000);// 设置页面的宽度,当超过这个宽度时显示滚动条
        
		egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
		egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));
		// *****************************************设置默认值****************************
		// egu.getColumn("diancxxb_id").setDefaultValue(String.valueOf(((Visit)
		// getPage().getVisit()).getDiancxxb_id()));//设置电厂信息表的默认值
//		egu.getColumn("diancxxb_id").setDefaultValue(
//				this.getIDropDownDiancmc(this.getTreeid()));
//		egu.getColumn("leix").setDefaultValue("1");
		// 设置电厂默认到站
		//egu.getColumn("daoz_id").setDefaultValue(this.getDiancDaoz());
		// 设置日期的默认值,
		// egu.getColumn("riq").setDefaultValue(intyear+"-"+StrMonth+"-01");
		// 设置供应商的默认值
		// egu.getColumn("gongysb_id").setDefaultValue(this.getFazValue().getValue());

		// *************************下拉框*****************************************88


		// 设置发站下拉框
//		ComboBox cb_faz = new ComboBox();
//		egu.getColumn("faz_id").setEditor(cb_faz);
//		cb_faz.setEditable(true);
//		String fazSql = "select id ,mingc from chezxxb c where c.leib='车站' order by c.mingc";
//		egu.getColumn("faz_id").setComboEditor(egu.gridId,
//				new IDropDownModel(fazSql));

		egu.addTbarText(biaoq);
		DateField df = new DateField();
		df.setReadOnly(true);
		
		df.setValue(this.getRiqi());
		df.Binding("RIQI", "");// 与html页中的id绑定,并自动刷新
		df.setId("riqi");
		egu.addToolbarItem(df.getScript());

		egu.addTbarText("至:");
		DateField df1 = new DateField();
		df1.setReadOnly(true);
		df1.setValue(this.getRiq2());
		df1.Binding("RIQ2", "");// 与html页中的id绑定,并自动刷新
		df1.setId("riq2");
		egu.addToolbarItem(df1.getScript());

		
		egu.addTbarText("-");// 设置分隔符


		// 设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",
				ExtTreeUtil.treeWindowCheck_gongys, ((Visit) this.getPage()
						.getVisit()).getDiancxxb_id(), getTreeid());
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");
		

		egu.addTbarText("-");// 设置分隔符
	
		//验收编号根据不同用户可以隐藏

		//
		egu.addTbarText("结算类型:");
		ComboBox comb4 = new ComboBox();
		comb4.setTransform("JieslxDropDown");
		comb4.setId("Jieslx");
		comb4.setEditable(false);
		comb4.setLazyRender(true);// 动态绑定
		comb4.setWidth(100);
		comb4.setReadOnly(true);
		egu.addToolbarItem(comb4.getScript());
		egu.addOtherScript("Jieslx.on('select',function(){document.forms[0].submit();});");
		
		egu.addTbarText("合同号:");
		ComboBox comb5 = new ComboBox();
		comb5.setTransform("HthDropDown");
		comb5.setId("Heth");
		comb5.setEditable(false);
		comb5.setLazyRender(true);// 动态绑定
		comb5.setWidth(80);
		comb5.setReadOnly(true);
		egu.addToolbarItem(comb5.getScript());
		

						
		

		// 设定工具栏下拉框自动刷新
//		egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符
		//egu.addToolbarButton(GridButton.ButtonType_ShowDIV, null);
		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		egu.addTbarText("-");		
		egu.addToolbarItem("{"+new GridButton("条件","function(){ if(!win){ "+Strtmpfunction+"}"
				+ " win.show(this);	\n" 
				+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n" 
		 		+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
		 		+ "	\t}	\n"
			 	+ " \tif(document.getElementById('TEXT_YANSBH_VALUE').value!=''){	\n"
			 	+ "		\tyansbh.setRawValue(document.getElementById('TEXT_YANSBH_VALUE').value);	\n"
			 	+ "	\t}	\n"
				+ "}").getScript()+"}");  
        egu.addTbarText("-");
		egu.addToolbarItem("{"+new GridButton("结算","function(){}").getScript()+"}");
		


		// ---------------页面js的计算开始------------------------------------------
	/*	StringBuffer sb = new StringBuffer();

		sb.append("gridDiv_grid.on('beforeedit',function(e){");
		// sb.append("if (e.row==7){e.cancel=true;}");//设定第8列不可编辑,索引是从0开始的
		sb.append(" if(e.field=='GONGYSB_ID'){ e.cancel=true;}");// 设定供应商列不可编辑
		sb.append(" if(e.field=='DIANCXXB_ID'){ e.cancel=true;}");// 设定电厂信息列不可编辑
		sb.append("});");
		
		egu.addOtherScript(sb.toString());*/
		egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
				+ " \tvar ches=0,biaoz=0,yingk=0,yuns=0,jingz=0,std=0, mt=0,mad=0,aar=0,aad=0,ad=0,vad=0,vdaf=0,stad=0,had=0,qnet_ar=0,qbad=0,qgrad_daf=0;	\n"
				+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"	
				+ " for(var i=0;i<rec.length;i++){ \n"
				+ " \tif(''!=rec[i].get('ID')){ \n"
				+ "		\tches+=eval(rec[i].get('CHES'));"		
				+ "		\tbiaoz+=eval(rec[i].get('BIAOZ'));"
				+ " 	\tyingk+=eval(rec[i].get('YINGK')); \n" 
				+ " 	\tyuns+=eval(rec[i].get('YUNS'));	\n"	
				+ " 	\tjingz+=eval(rec[i].get('JINGZ'));	\n"
				+ " 	\tstd+=eval(rec[i].get('STD'));	\n"
				+ "		\tmt+=eval(rec[i].get('MT'));"
				+ " 	\tmad+=eval(rec[i].get('MAD')); \n" 
				+ " 	\taar+=eval(rec[i].get('AAR'));	\n"	
				+ " 	\taad+=eval(rec[i].get('AAD'));	\n"
				+ " 	\tad+=eval(rec[i].get('AD'));	\n"
				+ "		\tvad+=eval(rec[i].get('VAD'));"
				+ " 	\tvdaf+=eval(rec[i].get('VDAF')); \n" 
				+ " 	\tstad+=eval(rec[i].get('STAD'));	\n"	
				+ " 	\thad+=eval(rec[i].get('HAD'));	\n"
				+ " 	\tqnet_ar+=eval(rec[i].get('QNET_AR'));	\n"
				+ "		\tqbad+=eval(rec[i].get('QBAD'));"
				+ " 	\tqgrad_daf+=eval(rec[i].get('QGRAD_DAF')); \n" 

				+ " \t}	\n"
				+ " }	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YINGK',yingk);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YUNS',yuns);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('JINGZ',jingz);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STD',std);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MT',mt);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('MAD',mad);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AAR',aar);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AAD',aad);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('AD',ad);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('VAD',vad);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('VDAF',vdaf);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('STAD',stad);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('HAD',had);	\n"
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QNET_AR',qnet_ar);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QBAD',qbad);	\n"	
				+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('QGRAD_DAF',qgrad_daf);	\n"
				+ " });	");
		// ---------------页面js计算结束--------------------------

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
			visit.setString2("");
			visit.setString3("");
			visit.setString4("");
			visit.setList1(null);
			this.setTreeid(null);
			setJieslxValue(null);
			setJieslxModel(null);
			getJieslxModels();
			setHthValue(null);
			setHthModel(null);
			getHthModels();
			setYansbhValue(null);
			setYansbhModel(null);
			getYansbhModels();		
			setFencbValue(null);
			setFencbModel(null);
			getFencbModels();
			
		}

//		if(	_Jieslxchange=false){
//		
//			setYansbhValue(null);
//			getYansbhModels();
//			getSelectData();
//			}

		
		getSelectData();

	}


     //结算类型
	public boolean _Jieslxchange = false;
	public IDropDownBean getJieslxValue() {

		if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean3((IDropDownBean) getJieslxModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean3();
	}

	public void setJieslxValue(IDropDownBean Value) {
		
		if(Value!=((Visit) getPage().getVisit()).getDropDownBean3()){
			
			((Visit) getPage().getVisit()).setboolean2(true);
			
		}
		((Visit) getPage().getVisit()).setDropDownBean3(Value);
	}

	public IPropertySelectionModel getJieslxModel() {

		if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {

			getJieslxModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}

	public void setJieslxModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getJieslxModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

			int i = -1;
		
			String sql =
				"select -1 as id,'两票结算' as mingc from dual\n" +
				"union\n" + 
				"select 0 as id,'煤款结算' as mingc from dual\n" + 
				"union\n" + 
				"select id, mingc from feiylbb";

			ResultSet rs = con.getResultSet(sql);
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("mingc")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel3(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel3();
	}
	
    public String rb1(){
    	if(getRbvalue()==null||getRbvalue().equals("")){
    		
    		
    	    	return "fh.fahrq";
    	    	
    		
    	}else{
    		if(getRbvalue().equals("fahrq")){
    			return "fh.fahrq";
    		}
    		else return "fh.daohrq";
    	}
    }
	// 验收编号
	public boolean _Yansbhchange = false;

	public IDropDownBean getYansbhValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getYansbhModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setYansbhValue(IDropDownBean Value) {

		if (Value != ((Visit) getPage().getVisit()).getDropDownBean2()) {

			((Visit) getPage().getVisit()).setboolean1(true);
		}
		((Visit) getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setYansbhModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getYansbhModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getYansbhModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public IPropertySelectionModel getYansbhModels() {
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		//String feiyid="";
//		if(getJieslxValue().getId()==-1||getJieslxValue().getId()==0){
//			feiyid="jiesdid=0";
//		}
//		else{
//			feiyid="feiylbb_id="+getJieslxValue().getId();
//		}
//		
	
			//String sql = "select id, bianm from yansbhb order by bianm ";
	String	sql = "select y.id,y.bianm from yansbhb y,fahb fh where fh.yansbhb_id=y.id " 
			+ " and "+rb1()+">= to_date('"+getRiqi()+"', 'yyyy-mm-dd') and "+rb1()+"<= to_date('"+getRiq2()+"', 'yyyy-mm-dd') "
			+ " and fh.diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(getFencbModel(), this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id())+" order by bianm";

			((Visit) getPage().getVisit())
			.setProSelectionModel2(new IDropDownModel(sql, "请选择"));
	return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}
	//合同号 
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
	private long getGongysb_id(String treeid) {
		long gongysb_id=0;
		JDBCcon cn =new JDBCcon();
		String sql4="select id from gongysb where id="+treeid+" /n"+
                        "union /n"+
                        "select glb.gongysb_id id from meikxxb m,gongysmkglb glb /n"+
                       "where m.id=glb.meikxxb_id and m.id="+treeid;
		ResultSet rs = cn.getResultSet(sql4);
		try {
			while (rs.next()) {
				gongysb_id = rs.getLong("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return gongysb_id;
	}

	public IPropertySelectionModel getHthModels() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();
		try {

//			一、煤矿有没有供应商
//			二、判断系统里有没有合同
			int i = -1;
			String sql="";
			List.add(new IDropDownBean(i++, "请选择"));
			if(this.getTreeid()==null||this.getTreeid().equals("")||this.getTreeid()=="0"){
				
				sql = "select h.id,h.hetbh from hetb h where h.diancxxb_id="+visit.getDiancxxb_id()+" order by hetbh ";
			
			}else{
				
				long gongys_id=getGongysb_id(this.getTreeid());
				
				if(gongys_id==0){
					
					setMsg("该煤矿没有对应的供应商");
				}else{
					
					sql = "select h.id,h.hetbh from hetb h where h.diancxxb_id="+visit.getDiancxxb_id()+" and h.gongysb_id="+getGongysb_id(this.getTreeid())+" order by hetbh ";
				}
				
				
			}
			
			
			
			ResultSet rs = con.getResultSet(sql);
			if(con.getRow(rs)==0){
				setMsg("系统里没有所选的合同");
			}
			
			
			while (rs.next()) {

				List.add(new IDropDownBean(i++, rs.getString("hetbh")));
			}
			rs.close();
		} catch (Exception e) {

			e.printStackTrace();
		} finally {

			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel4(new IDropDownModel(List));
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}
	


	// 得到登陆用户所在电厂或者分公司的名称
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return diancmc;

	}
	// 得到电厂树下拉框的电厂名称或者分公司,集团的名称d
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
			// TODO 自动生成 catch 块
			e.printStackTrace();
		} finally {
			cn.Close();
		}

		return IDropDownDiancmc;

	}

//厂别
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
		JDBCcon con = new JDBCcon();
		List List = new ArrayList();

		
			String sql ="select id,mingc from diancxxb d where d.fuid="+
			((Visit) getPage().getVisit()).getDiancxxb_id()+"order by mingc";


			((Visit) getPage().getVisit())
			.setProSelectionModel5(new IDropDownModel(sql, "请选择"));
	return ((Visit) getPage().getVisit()).getProSelectionModel5();
	}

	

	private String treeid;

	/*
	 * public String getTreeid() { return treeid; } public void setTreeid(String
	 * treeid) { this.treeid = treeid; }
	 */
	public String getTreeid() {
		// if(treeid==null||treeid.equals("")){
		// ((Visit) getPage().getVisit()).setString2(String.valueOf(((Visit)
		// this.getPage().getVisit()).getDiancxxb_id()));
		// }
		if(((Visit) getPage().getVisit()).getString2()==null||((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
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

	// 得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂

	public String getWunScript(){
		
		return "";
	}
}
