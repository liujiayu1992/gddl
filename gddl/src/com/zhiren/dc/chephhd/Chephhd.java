package com.zhiren.dc.chephhd;

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
import com.zhiren.common.ResultSetList;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.Locale;

public class Chephhd extends BasePage implements PageValidateListener {
	private String msg = "";
	private String Change;
	private boolean _RefurbishChick = false;
	private boolean _SbChick = false;
	
//	长别下拉框取值
	private boolean _RbChick = false;
	private String treeid="";
	
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = MainGlobal.getExtMessageBox(msg,false);
	}
	
//	验收编号下拉框取值
	
	protected void initialize() {
		super.initialize();
		msg = "";
	}

	public String getChangb() {

		if(((Visit) this.getPage().getVisit()).getString3().equals("")){

			((Visit) this.getPage().getVisit()).setString3(
					((IDropDownBean) getChangbSelectModel().getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString3();
	}
	
	public void setChangb(String changb) {
		((Visit) this.getPage().getVisit()).setString3(changb);
	}

//	主结算单位下拉框
	public String getMainjsdw(){

		if(((Visit) this.getPage().getVisit()).getString11().equals("")){

			((Visit) this.getPage().getVisit()).setString11(
					((IDropDownBean) getChangbSelectModel().getOption(0)).getValue());
		}

		return ((Visit) this.getPage().getVisit()).getString11();
	}

	public void setMainjsdw(String value){

		((Visit) this.getPage().getVisit()).setString11(value);
	}
	
	public String getYansbh() {
		return ((Visit) this.getPage().getVisit()).getString4();
	}

	public void setYansbh(String yansbh) {
		((Visit) this.getPage().getVisit()).setString4(yansbh);
	}
	
	public String getChange() {
		return Change;
	}

	public void setChange(String change) {
		Change = change;
	}

	public String getRbvalue() {
		return ((Visit) this.getPage().getVisit()).getString5();
	}

	public void setRbvalue(String rbvalue) {
		((Visit) this.getPage().getVisit()).setString5(rbvalue);
	}

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }
	
    private void Refurbish() {
            //为  "刷新"  按钮添加处理程序
            getSelectData();
    }

	public void SbButton(IRequestCycle cycle) {
		_SbChick = true;
	}

	public void RbButton(IRequestCycle cycle) {
		_RbChick = true;
	}

	public void submit(IRequestCycle cycle){
		if (_RefurbishChick) {
            _RefurbishChick = false;
            Refurbish();
        }
		if (_SbChick) {
			_SbChick = false;
			//Tiaoj();
			getSelectData();
		}

		if (_RbChick) {
			_RbChick = false;
			//Hedfy();
			gotoDanchhd(cycle);
		}
	}

    private void gotoDanchhd(IRequestCycle cycle) {
		// TODO 自动生成方法存根
//    	需要传给下个页面取值	diancxxb_id、lie_id、feiylbb_id、meikxxb_id、faz_id.daoz_id,fahrq,yansbh
    	ExtGridUtil egu=this.getExtGrid();
    	ResultSetList mdrsl = egu.getModifyResultSet(this.getChange());
    	String mstr_lieid="";
    	String mstr_meikxxbid="";
    	String mstr_fazid="";
    	String mstr_daozid="";

    	while(mdrsl.next()){

    		for(int i=1;i<mdrsl.getColumnCount();i++) {

    			if(i==1){

    				mstr_lieid+=mdrsl.getString("ID")+",";
    			}
    			if(mdrsl.getColumnNames()[i].equals("MEIKXXB_ID")){

					mstr_meikxxbid=mdrsl.getString(i);
				}else if(mdrsl.getColumnNames()[i].equals("FAZ_ID")){

					mstr_fazid=mdrsl.getString(i);
				}else if(mdrsl.getColumnNames()[i].equals("DAOZ_ID")){

					mstr_daozid=mdrsl.getString(i);
				}
			}
    	}

//    	电厂Id赋值
    	if(((Visit) getPage().getVisit()).isFencb()){

    		((Visit) getPage().getVisit()).setLong1(MainGlobal.getProperId(this.getChangbSelectModel(),this.getMainjsdw()));
    	}
    	else{
    		((Visit) getPage().getVisit()).setLong1(((Visit) getPage().getVisit()).getDiancxxb_id());
    	}

//    	列Id赋值
    	((Visit) getPage().getVisit()).setString6(mstr_lieid.substring(0,mstr_lieid.lastIndexOf(",")));


//    	煤矿信息表
    	((Visit) getPage().getVisit()).setLong3(Long.parseLong(mstr_meikxxbid));

//    	发站Id
    	((Visit) getPage().getVisit()).setLong4(Long.parseLong(mstr_fazid));

//    	到站Id
    	((Visit) getPage().getVisit()).setLong5(Long.parseLong(mstr_daozid));

//    	发货日期
    	((Visit) getPage().getVisit()).setString7(MainGlobal.getFahb_fahrq(((Visit) getPage().getVisit()).getString6()));

//    	验收编号
    	if(MainGlobal.getXitxx_item("结算", "使用验收编号", String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(getChangbSelectModel(), this.getMainjsdw()):((Visit) getPage().getVisit()).getDiancxxb_id()), "否").equals("是")){
    		if(this.getYansbh().equals("请选择")||this.getYansbh().equals("")){
    			((Visit) getPage().getVisit()).setString8(MainGlobal.getYansbh());
    		}else{
        		((Visit) getPage().getVisit()).setString8(this.getYansbh());

        	}

    	}else{

    		((Visit) getPage().getVisit()).setString8("");
    	}

     cycle.activate("Danchhd");
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
    
    public String rb2(){
    	if(getRbvalue()==null||getRbvalue().equals("")){


        		return Locale.fahrq_fahb+":";//发货日期

    	}else {
    		if(getRbvalue().equals("fahrq")){
    			return Locale.fahrq_fahb+":";
    		}
    		else return Locale.daohrq_id_fahb+":";//到货日期
    	}



    }

    public String riq1(){
    	String riqTiaoq=this.getQisriqi();
		if(riqTiaoq==null||riqTiaoq.equals("")){
			riqTiaoq=DateUtil.FormatDate(new Date());

		}
		return riqTiaoq;
    }

    public String riq2(){
    	String riqTiaoj=this.getJiezriqi();
		if(riqTiaoj==null||riqTiaoj.equals("")){
			riqTiaoj=DateUtil.FormatDate(new Date());

		}
		return riqTiaoj;
    }
    
    public String TreeID(){
       String gonghdw= this.getTreeid();
        String con="";
       if(!gonghdw.equals("0"))
       con=" and (m.id="+gonghdw+" or gys.id="+gonghdw+")";
    	return con;
    }

	public void getSelectData(){
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();

		String str="";
		String tiaoj_fahrq="";
		String tiaoj_daohrq="";
		String tiaoj_changb="";
		String tiaoj_yansbh="";
		String tiaoj_returnvalue_changb="";
		String tiaoj_returnvalue_yansbh="";

		if(getRbvalue()==null||getRbvalue().equals("fahrq")||getRbvalue().equals("")){

			tiaoj_fahrq="checked:true, \n";

		}else if(getRbvalue().equals("daohrq")){

			tiaoj_daohrq="checked:true, \n";
		}
//		if(visit.isFencb()){
		tiaoj_changb=", \n"
		             + "{  \n"
	                 + "xtype:'textfield', \n"
	                 + "fieldLabel:'厂别', \n"
	                 + "width:0  \n"
                     + "}, \n"
                     + "Changb=new Ext.zr.select.Selectcombo({ \n"
                     + "	multiSelect:true,\n"
	                 + "	width:150, \n"
	                 + "	transform:'ChangbDropDown', \n"
	                 + "	lazyRender:true, \n"
	                 + "	triggerAction:'all', \n"
	                 + "	typeAhead:true, \n"
	                 + "	forceSelection:true \n"
                     + "}),	\n"
                     + "{	\n"
                     + "	xtype:'textfield', \n"
                     + "	fieldLabel:'主结算单位', \n"
                     + "	width:0  \n"
                     + "}, \n"
                     + "Mainjsdw=new Ext.form.ComboBox({	\n"
                     + "	width:150,	\n"
                     + "	selectOnFocus:true,	\n"
                     + "	transform:'MainjsdwDropDown',	\n"
                     + "	lazyRender:true, \n"
                     + "	triggerAction:'all', \n"
                     + "	forceSelection:true \n"
                     + "})	\n";

		tiaoj_returnvalue_changb="	document.getElementById('TEXT_CHANGB_VALUE').value=Changb.getRawValue();	\n"
								+"	document.getElementById('TEXT_MAINJSDW_VALUE').value=Mainjsdw.getRawValue();	\n";
//		}else{
//			tiaoj_changb="";
//			tiaoj_returnvalue_changb="";
//		}

		if(MainGlobal.getXitxx_item("结算", "使用验收编号", String.valueOf(visit.isFencb()?MainGlobal.getProperId(getChangbSelectModel(), this.getMainjsdw()):visit.getDiancxxb_id()), "否").equals("是")){

			tiaoj_yansbh=" ,{  \n"
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

			tiaoj_returnvalue_yansbh="	document.getElementById('TEXT_YANSBH_VALUE').value=yansbh.getRawValue();	\n";
		}

		 str="var form = new Ext.form.FormPanel({ "
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
			+ "		boxLabel:'"+Locale.fahrq_fahb+"', \n"
		    + "     anchor:'95%', \n";

		 str+=tiaoj_fahrq;

		 str+="     Value:'fahrq', \n"
			+ "		id:'fahrq',\n"
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

			+ "		boxLabel:'"+Locale.daohrq_id_fahb+"',\n" ;
		 str+=tiaoj_daohrq;

		 str+="		Value:'daohrq', \n"
			+ "     anchor:'95%',	\n"
			+ "		id:'daohrq',\n"
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

		    + tiaoj_changb
		    + tiaoj_yansbh
		    + "]\n"
			+ " });\n"

			+ " win = new Ext.Window({\n"
			+ " el:'hello-win',\n"
			+ " layout:'fit',\n"
			+ " width:500,\n"
			+ " height:300,\n"
			+ " closeAction:'hide',\n"
			+ " plain: true,\n"
			+ " title:'条件',\n"
			+ " items: [form],\n"

			+ "buttons: [{\n"
			+ "		text:'确定',\n"
			+ "   	handler:function(){  \n"
			+ "   		win.hide();\n"
		 	+			tiaoj_returnvalue_changb
		 	+			tiaoj_returnvalue_yansbh
			+ "			document.getElementById('TEXT_RADIO_RQSELECT_VALUE').value=document.getElementById('rbvalue').value;	\n"
			+ " 		document.forms[0].submit(); \n"
			+ "  }   \n"
			+ "},{\n"
			+ "   text: '取消',\n"
			+ "   handler: function(){\n"
			+ "   		win.hide();\n"
			+ "			document.getElementById('TEXT_MAINJSDW_VALUE').value='';	\n"
			+ "			document.getElementById('TEXT_CHANGB_VALUE').value='';	\n"
			+ "			document.getElementById('TEXT_YANSBH_VALUE').value='';	\n"
			+ "   }\n"
			+ "}]\n"
			+ " });";

		   /*String chaxun="select zongb.id as id, decode(zongb.fahrq,null,'合计',zongb.fahrq) as fahrq,zongb.gongys as fahdw,zongb.meikdw as meikdw,zongb.meikxxb_id,zongb.faz_id,zongb.daoz_id,zongb.faz as faz,\n"
			   		   + " decode(zongb.fahrq,null,0,sum(zongb.ches)) as ches,decode(zongb.fahrq,null,0,sum(nvl(yihd.ches, 0))) as yihdcs,decode(zongb.fahrq,null,0,sum(zongb.ches - nvl(yihd.ches, 0))) as weihdcs,	\n"
			   		   + " decode(zongb.fahrq,null,0,sum(zongb.biaoz)) as biaoz,decode(zongb.fahrq,null,0,sum(nvl(yihd.biaoz, 0))) as yihdbz,	\n"
			   		   + " decode(zongb.fahrq,null,0,sum(zongb.biaoz - nvl(yihd.biaoz, 0))) as weihdbz,decode(zongb.fahrq,null,'',decode(sum(zongb.ches - nvl(yihd.ches,0)), 0, '已完成','未完成')) as shifwc"
			   		   +" from"
					   + " (select to_char(fh.fahrq, 'yyyy-MM-dd') as fahrq,gys.mingc as gongys,m.mingc as meikdw,fh.meikxxb_id,count(cp.id) as ches,fh.lie_id as id,c.mingc as faz,fh.faz_id,daozb.mingc as daoz,fh.daoz_id,"
		               + " to_char(fh.daohrq, 'yyyy-MM-dd') as daohrq,sum(cp.biaoz) as biaoz "
		               + " from chepb cp,fahb fh,zhilb z,gongysb gys,meikxxb m,chezxxb c,yunfdjb yd,danjcpb dj,yansbhb,"
		               + " (select distinct c.id, c.mingc from fahb fh, chezxxb c  where fh.daoz_id = c.id) daozb "
		               + " where fh.id = cp.fahb_id and  cp.chebb_id <> 3  and fh.zhilb_id = z.id(+) and fh.gongysb_id = gys.id and cp.id=dj.chepb_id(+) and dj.yunfdjb_id = yd.id(+) and fh.yansbhb_id = yansbhb.id(+) and (dj.yunfjsb_id is null or dj.yunfjsb_id=0) "
		               + " and fh.meikxxb_id = m.id and fh.faz_id = c.id and fh.daoz_id = daozb.id "+getWhere()+""
		               + " group by to_char(fh.fahrq, 'yyyy-MM-dd'),gys.mingc,m.mingc,fh.meikxxb_id,fh.lie_id,c.mingc,fh.faz_id,daozb.mingc,fh.daoz_id,to_char(fh.daohrq, 'yyyy-MM-dd')) zongb,"

		               + " (select to_char(fh.fahrq, 'yyyy-MM-dd') as fahrq,gys.mingc as gongys,m.mingc as meikdw,fh.meikxxb_id,count(cp.id) as ches,fh.lie_id as id,c.mingc as faz,fh.faz_id,daozb.mingc as daoz,fh.daoz_id,"
		               + " to_char(fh.daohrq, 'yyyy-MM-dd') as daohrq,sum(cp.biaoz) as biaoz "
		               + " from chepb cp,fahb fh,zhilb z,gongysb gys,meikxxb m,chezxxb c,yunfdjb yd,danjcpb dj,yansbhb, "
		               + " (select distinct c.id, c.mingc from fahb fh, chezxxb c  where fh.daoz_id = c.id) daozb "
		               + " where fh.id = cp.fahb_id and cp.chebb_id <> 3 and fh.zhilb_id = z.id(+) and fh.gongysb_id = gys.id and cp.id=dj.chepb_id and dj.yunfdjb_id = yd.id and fh.yansbhb_id = yansbhb.id(+) and dj.yunfjsb_id = 0 "
		               + " and fh.meikxxb_id = m.id and fh.faz_id = c.id and fh.daoz_id = daozb.id "+getWhere()+""
		               + " group by to_char(fh.fahrq, 'yyyy-MM-dd'),gys.mingc,m.mingc,fh.meikxxb_id,fh.lie_id,c.mingc,fh.faz_id,daozb.mingc,fh.daoz_id,to_char(fh.daohrq, 'yyyy-MM-dd')) yihd"

		               +" where zongb.id=yihd.id(+) and zongb.faz_id=yihd.faz_id(+) and zongb.daoz_id=yihd.daoz_id(+)"
			           +" group by rollup(zongb.id,zongb.fahrq,zongb.gongys,zongb.meikdw,zongb.meikxxb_id,zongb.faz_id,zongb.daoz_id,zongb.faz)"
		               +" having not (grouping(zongb.faz)=1 and  grouping(zongb.id)=0)";
		   */
		 String chaxun=
			 "select zongb.id as id,\n" +
			 "       decode(zongb.fahrq, null, '合计', zongb.fahrq) as fahrq,\n" +
			 "       zongb.gongys as fahdw,\n" +
			 "       zongb.meikdw as meikdw,\n" +
			 "       zongb.meikxxb_id,\n" +
			 "       zongb.faz_id,\n" +
			 "       zongb.daoz_id,\n" +
			 "       zongb.faz as faz,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(zongb.ches)) as ches,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(nvl(yihd.ches, 0))) as yihdcs,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(zongb.ches - nvl(yihd.ches, 0))) as weihdcs,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(zongb.biaoz)) as biaoz,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(nvl(yihd.biaoz, 0))) as yihdbz,\n" +
			 "       decode(zongb.fahrq, null, 0, sum(zongb.biaoz - nvl(yihd.biaoz, 0))) as weihdbz,\n" +
			 "       decode(zongb.fahrq,\n" +
			 "              null,\n" +
			 "              '',\n" +
			 "              decode(sum(zongb.ches - nvl(yihd.ches, 0)),\n" +
			 "                     0,\n" +
			 "                     '已完成',\n" +
			 "                     '未完成')) as shifwc\n" +
			 "  from (select to_char(fh.fahrq, 'yyyy-MM-dd') as fahrq,	\n"
                         + " gys.mingc as gongys,m.mingc as meikdw,	\n"
                         + " fh.meikxxb_id,count(cp.id) as ches,	\n"
                         + " fh.lie_id as id,c.mingc as faz,		\n"
                         + " fh.faz_id,daozb.mingc as daoz,			\n"
                         + " fh.daoz_id,sum(cp.biaoz) as biaoz,		\n"
                         + " to_char(fh.daohrq, 'yyyy-MM-dd') as daohrq	\n"
                         + " from chepb cp,fahb fh,zhilb z,gongysb gys,meikxxb m,chezxxb c,yansbhb,chezxxb daozb	\n"
                         + " where fh.id = cp.fahb_id	\n"
                         + " and fh.zhilb_id = z.id(+)	\n"
                         + " and fh.gongysb_id = gys.id	\n"
                         + " and cp.hedbz=3				\n"
                         + " and fh.yansbhb_id = yansbhb.id(+)	\n"
                         + " and fh.meikxxb_id = m.id			\n"
                         + " and fh.faz_id = c.id				\n"
                         + " and fh.daoz_id = daozb.id			\n"
                         + " and fh.liucztb_id=1				\n"
			             + getWhere()
			             + " group by to_char(fh.fahrq, 'yyyy-MM-dd'),gys.mingc,m.mingc,fh.meikxxb_id,	\n"
			             + " fh.lie_id,c.mingc,fh.faz_id,daozb.mingc,fh.daoz_id,to_char(fh.daohrq, 'yyyy-MM-dd')) zongb,\n"
			             + " (select to_char(fh.fahrq, 'yyyy-MM-dd') as fahrq,gys.mingc as gongys,	\n"
			             + " m.mingc as meikdw,fh.meikxxb_id,count(cp.id) as ches,fh.lie_id as id,		\n"
			             + " c.mingc as faz,fh.faz_id,daozb.mingc as daoz,fh.daoz_id,		\n"
			             + " to_char(fh.daohrq, 'yyyy-MM-dd') as daohrq,sum(cp.biaoz) as biaoz			\n"
			             + " from chepb cp,fahb fh,zhilb z,gongysb gys,meikxxb m,chezxxb c,yansbhb,		\n"
			             + " 		chezxxb daozb              	\n"
			             + " where fh.id = cp.fahb_id		   	\n"
			             + " and fh.zhilb_id = z.id(+)			\n"
			             + " and fh.gongysb_id = gys.id			\n"
			             + " and cp.hedbz=3						\n"
			             + " and cp.yansbhb_id= yansbhb.id		\n"
			             + " and fh.meikxxb_id = m.id			\n"
			             + " and fh.faz_id = c.id				\n"
			             + " and fh.daoz_id = daozb.id			\n"
			             + " and fh.liucztb_id=1				\n"
			             + getWhere()
			             + " group by to_char(fh.fahrq, 'yyyy-MM-dd'),gys.mingc,m.mingc,fh.meikxxb_id,fh.lie_id,c.mingc,	\n"
			             + " fh.faz_id,daozb.mingc,fh.daoz_id,to_char(fh.daohrq, 'yyyy-MM-dd')) yihd	\n"
			            +" where zongb.id = yihd.id(+)\n" +
							"   and zongb.fahrq=yihd.fahrq(+)\n" +
							"   and zongb.gongys=yihd.gongys(+)\n" +
							"   and zongb.meikdw=yihd.meikdw(+)\n" +
							"   and zongb.meikxxb_id=yihd.meikxxb_id(+)\n" +
							"   and zongb.faz=yihd.faz(+)\n" +
							"   and zongb.faz_id = yihd.faz_id(+)\n" +
							"   and zongb.daoz=yihd.daoz(+)\n" +
							"   and zongb.daoz_id = yihd.daoz_id(+)\n" +
							"   and zongb.daohrq=yihd.daohrq(+)"
			             + " group by rollup(zongb.id,zongb.fahrq,zongb.gongys,zongb.meikdw,zongb.meikxxb_id,zongb.faz_id,\n"
			             + "                 zongb.daoz_id, zongb.faz)\n"
			             + " having not(grouping(zongb.faz) = 1 and grouping(zongb.id) = 0)\n";

			ResultSetList rsl = con.getResultSetList(chaxun);
		    ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
		    egu.setWidth("bodyWidth");
		    egu.getColumn("id").setHidden(true);

		    egu.getColumn("fahrq").setHeader(Locale.fahrq_fahb);
			egu.getColumn("fahdw").setHeader(Locale.gongysb_id_fahb);
			egu.getColumn("meikdw").setHeader(Locale.meikxxb_id_fahb);
			egu.getColumn("faz").setHeader(Locale.faz_id_fahb);
			egu.getColumn("ches").setHeader("车数");
			egu.getColumn("yihdcs").setHeader("已核对车数");
			egu.getColumn("weihdcs").setHeader("未核对车数");
			egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb);
			egu.getColumn("yihdbz").setHeader("已核对票重");
			egu.getColumn("weihdbz").setHeader("未核对票重");
			egu.getColumn("shifwc").setHeader("核对状态");
			egu.getColumn("shifwc").setRenderer("renderHdzt");

			egu.getColumn("faz").setWidth(60);
			egu.getColumn("ches").setWidth(60);
			egu.getColumn("yihdcs").setWidth(80);
			egu.getColumn("weihdcs").setWidth(80);
			egu.getColumn("biaoz").setWidth(60);
			egu.getColumn("yihdbz").setWidth(80);
			egu.getColumn("weihdbz").setWidth(80);

			egu.getColumn("meikxxb_id").setHidden(true);
			egu.getColumn("faz_id").setHidden(true);
			egu.getColumn("daoz_id").setHidden(true);

			egu.addPaging(0);//设置分页
			egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
			egu.getGridColumns().add(2, new GridColumn(GridColumn.ColType_Check));

		egu.addTbarText(rb2());
		DateField df1 = new DateField();
		df1.setValue(this.getQisriqi());
		df1.Binding("QISRIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		df1.setId("fahrq");
		egu.addToolbarItem(df1.getScript());
		egu.addTbarText("-");

		egu.addTbarText("至:");
		DateField df2 = new DateField();
		df2.setValue(this.getJiezriqi());
		df2.Binding("JIEZRIQI","forms[0]");// 与html页中的id绑定,并自动刷新
		df2.setId("fahrq2");
		egu.addToolbarItem(df2.getScript());
		egu.addTbarText("-");
		///

		egu.setDefaultsortable(false);
		//设置树
		egu.addTbarText(Locale.gongysb_id_fahb);
		String condition=" and "+rb1()+">=to_date('"+this.getQisriqi()+"','yyyy-MM-dd') and "+rb1()+"<=to_date('"+this.getJiezriqi()+"','yyyy-MM-dd')";
		ExtTreeUtil etu = new ExtTreeUtil("gongysTree",ExtTreeUtil.treeWindowCheck_gongys,((Visit)this.getPage().getVisit()).getDiancxxb_id(),getTreeid(),condition);
		etu.setWidth(60);
		setTree(etu);
		egu.addTbarTreeBtn("gongysTree");

		egu.addTbarText("发站:");
		ComboBox comb1=new ComboBox();
		comb1.setTransform("FazDropDown");
		comb1.setId("faz");
		//comb1.setEditable(true);
		comb1.setLazyRender(true);//动态绑定
		comb1.setWidth(80);
		egu.addToolbarItem(comb1.getScript());
		//egu.addOtherScript("faz.on('select',function(){document.forms[0].submit();});");
		egu.addTbarText("-");// 设置分隔符


		egu.addToolbarItem("{"+new GridButton("刷新","function(){document.getElementById('RefurbishButton').click();}").getScript()+"}");
		egu.addToolbarItem("{"+new GridButton("条件","function(){ if(!win){ "+str+"}"
				+ " win.show(this);	\n"
				+ " \tif(document.getElementById('TEXT_CHANGB_VALUE').value!=''){	\n"
		 		+ "		\tChangb.setRawValue(document.getElementById('TEXT_CHANGB_VALUE').value);	\n"
		 		+ "	\t}	\n"
		 		+ " \tif(document.getElementById('TEXT_MAINJSDW_VALUE').value!=''){	\n"
		 		+ "		\tMainjsdw.setRawValue(document.getElementById('TEXT_MAINJSDW_VALUE').value);	\n"
		 		+ "	\t}	\n"
			 	+ " \tif(document.getElementById('TEXT_YANSBH_VALUE').value!=''){	\n"
			 	+ "		\tyansbh.setRawValue(document.getElementById('TEXT_YANSBH_VALUE').value);	\n"
			 	+ "	\t}	\n"
				+ "}").getScript()+"}");
		egu.addToolbarButton("核对车号",GridButton.ButtonType_SubmitSel,"RbButton");

		egu.addOtherScript("gridDiv_grid.on('rowclick',function(own,row,e){ \n"
						+ " \tvar yihdcs=0,weihdcs=0,yihdbz=0,weihdbz=0,ches=0,biaoz=0;	\n"
						+ "	var rec = gridDiv_grid.getSelectionModel().getSelections();	\n"
						+ " for(var i=0;i<rec.length;i++){ \n"
						+ " \tif(''!=rec[i].get('ID')){ \n"
						+ "		\tches+=eval(rec[i].get('CHES'));"
						+ " 	\tyihdcs+=eval(rec[i].get('YIHDCS')); \n"
						+ " 	\tweihdcs+=eval(rec[i].get('WEIHDCS'));	\n"
						+ "		\tbiaoz+=eval(rec[i].get('BIAOZ'));"
						+ " 	\tyihdbz+=eval(rec[i].get('YIHDBZ'));	\n"
						+ " 	\tweihdbz+=eval(rec[i].get('WEIHDBZ'));	\n"
						+ " \t}	\n"
						+ " }	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('CHES',ches);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHDCS',yihdcs);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('WEIHDCS',weihdcs);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('BIAOZ',biaoz);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('YIHDBZ',yihdbz);	\n"
						+ " \tgridDiv_ds.getAt(gridDiv_ds.getCount()-1).set('WEIHDBZ',weihdbz);	\n"
						+ " });	");

		egu.addOtherScript("function gridDiv_save(rec){	\n" +
				"	\tif(rec.get('FAHRQ')=='合计'){	\n" +
				"	\treturn 'continue';	\n" +
				"	\t}}");

		setExtGrid(egu);
		con.Close();
	}

	private String getWhere() {
		// TODO 自动生成方法存根
//		条件设置
		String where=" ";

//		日期设置
		where+="and "+rb1()+">= to_date('"+riq1()+"', 'yyyy-mm-dd') and "+rb1()+"<= to_date('"+riq2()+"', 'yyyy-mm-dd')		\n";

//		供应商
		where+=TreeID();

//		发站
		if(getFazSelectValue().getId()>-1){

			where+=" and fh.faz_id="+getFazSelectValue().getId()+"	\n";
		}

//		测试时注掉，正式环境要用
		if(((Visit) getPage().getVisit()).isFencb()){

			where+=" and fh.diancxxb_id in ("+MainGlobal.getProperIds(this.getChangbSelectModel(),this.getChangb())+")	\n";
		}
		else{
			where+=" and fh.diancxxb_id="+((Visit) getPage().getVisit()).getDiancxxb_id()+"		\n";
		}

		if(!this.getYansbh().equals("请选择")&&!this.getYansbh().equals("")){

			where=" and cp.yansbhb_id="+MainGlobal.getProperId(this.getYansbhSelectModel(),this.getYansbh())+"	\n";
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

	////////供应商下拉框
	public IDropDownBean getYansbhSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean1() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean1((IDropDownBean) getYansbhSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean1();
	}

	public void setYansbhSelectValue(IDropDownBean Value) {

		if(Value!=((Visit) getPage().getVisit()).getDropDownBean1()){

			((Visit) getPage().getVisit()).setDropDownBean1(Value);
		}
	}

	public IPropertySelectionModel getYansbhSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel1() == null) {
			getYansbhSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel1();
	}

	public void setYansbhSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel1(value);
	}
	///////发站下拉框
	
	public void getYansbhSelectModels() {
		String sql = "";

//		sql = "select y.id,y.bianm from yansbhb y,fahb fh where fh.yansbhb_id=y.id "
//			+ " and "+rb1()+">= to_date('"+riq1()+"', 'yyyy-mm-dd') and "+rb1()+"<= to_date('"+riq2()+"', 'yyyy-mm-dd') "
//			+ " and fh.diancxxb_id="+String.valueOf(((Visit) getPage().getVisit()).isFencb()?MainGlobal.getProperId(getChangbSelectModel(), this.getChangb()):((Visit) getPage().getVisit()).getDiancxxb_id())+" order by bianm";

		  sql="select id,bianm from yansbhb order by bianm";
		((Visit) getPage().getVisit())
				.setProSelectionModel1(new IDropDownModel(sql,"请选择"));
	}

	public IDropDownBean getFazSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean2((IDropDownBean) getFazSelectModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean2();
	}

	public void setFazSelectValue(IDropDownBean Value) {

		if(Value!=((Visit) getPage().getVisit()).getDropDownBean2()){

			((Visit) getPage().getVisit()).setDropDownBean2(Value);
		}
	}

	public IPropertySelectionModel getFazSelectModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
			getFazSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel2();
	}

	public void setFazSelectModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel2(value);
	}

	public void getFazSelectModels() {
		JDBCcon con = new JDBCcon();
		String sql = "";
		try {
            if(getTreeid().equals("0")){
            	sql ="select id,mingc from chezxxb order by mingc";
            }
            else{
			    sql = "select distinct c.id,c.mingc from chezxxb c,fahb fh,gongysb gys,meikxxb m  where "+rb1()+">=to_date('"+riq1()+"', 'yyyy-mm-dd') and "+rb1()+"<=to_date('"+riq2()+"', 'yyyy-mm-dd') and fh.faz_id=c.id and fh.gongysb_id=gys.id and fh.meikxxb_id=m.id"+TreeID()
			    		+" order by mingc";
            }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.Close();
		}
		((Visit) getPage().getVisit())
				.setProSelectionModel2(new IDropDownModel(sql,"请选择"));
	}

	///场别
	public IDropDownBean getChangbSelectValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {

			if(getChangbSelectModel().getOptionCount()>0){

				((Visit) getPage().getVisit())
				.setDropDownBean4((IDropDownBean) getChangbSelectModel()
						.getOption(0));
			}
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setChangbSelectValue(IDropDownBean Value) {

		if(Value!=((Visit) getPage().getVisit()).getDropDownBean4()){

			((Visit) getPage().getVisit()).setDropDownBean4(Value);
		}
	}

	public IPropertySelectionModel getChangbSelectModel() {
		
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getChangbSelectModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setChangbSelectModel(IPropertySelectionModel value) {

		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getChangbSelectModels() {
		String sql = "";
		JDBCcon con = new JDBCcon();

		sql ="select id,mingc from diancxxb d where d.fuid="+((Visit) getPage().getVisit()).getDiancxxb_id()+" \n " +
		 	"order by xuh,mingc";

		ResultSetList rsl = con.getResultSetList(sql);
		if(rsl.getRows()==0){

			sql = "select id,mingc from diancxxb where id="+((Visit) getPage().getVisit()).getDiancxxb_id();
		}
		rsl.close();
		con.Close();

		((Visit) getPage().getVisit())
			.setProSelectionModel4(new IDropDownModel(sql));
	}

//	 得到登陆用户所在电厂或者分公司的名称
	public String getDiancmc() {
		String diancmc = "";
		JDBCcon cn = new JDBCcon();
		long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc = "select d.quanc from diancxxb d where d.id="
				+ diancid+" order by d.quanc";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				diancmc = rs.getString("quanc");
			}
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

		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId+" order by d.mingc";
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
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

	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
		Visit visit = (Visit) getPage().getVisit();
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// 在此添加，在页面第一次加载时需要置为空的变量或方法
			visit.setActivePageName(getPageName().toString());
			visit.setInt1(-1);
			visit.setList1(null);
			visit.setString2("");	//Treeid
			visit.setString3("");	//长别下拉框取值
			visit.setString4("");	//验收编号下拉框取值
			visit.setString5("");	//Radio日期选择取值

//			页面传值初始化开始
			visit.setLong1(0);		//Diancxxb_id
			visit.setLong2(0);		//Feiylbb_id
			visit.setLong3(0);		//Meikxxb_id
			visit.setLong4(0);		//Faz_id
			visit.setLong5(0);		//Daoz_id
			visit.setString6("");	//Lie_id
			visit.setString7("");	//Fahrq
			visit.setString8("");	//验收编号

			visit.setString9("");	//riq1
			visit.setString10("");	//riq2
			visit.setString11("");	//主结算单位
//			页面传值初始化结束

			setYansbhSelectValue(null);		//Dropd1
			setYansbhSelectModel(null);

			setFazSelectValue(null);
			setFazSelectModel(null);

			setChangbSelectValue(null);
			setChangbSelectModel(null);


			getYansbhSelectModels();
			getFazSelectModel();
			getChangbSelectModel();
		}

		getSelectData();

	}
	/*public String getTreeid() {
		return treeid;
	}
	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
	
	public String getTreeid() {
		
		if(((Visit) getPage().getVisit()).getString2().equals("")){
			
			((Visit) getPage().getVisit()).setString2("0");
		}
		return ((Visit) getPage().getVisit()).getString2();
	}
	public void setTreeid(String treeid) {
		
		if(!((Visit) getPage().getVisit()).getString2().equals(treeid)){
			
			((Visit) getPage().getVisit()).setString2(treeid);
			getFazSelectModels();
		}
		
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
	public int getEditTableRow() {
		return ((Visit) this.getPage().getVisit()).getInt1();
	}
	
	public void setEditTableRow(int value){
		
		((Visit) this.getPage().getVisit()).setInt1(value);
	}
	
//	得到电厂树的值的级别,(jib:1,2,3,) 1是集团,2是分公司,3是电厂
	public int getDiancTreeJib() {
		JDBCcon con = new JDBCcon();
		int jib = -1;
		String DiancTreeJib = this.getTreeid();
//		System.out.println("jib:" + DiancTreeJib);
		if (DiancTreeJib == null || DiancTreeJib.equals("")) {
			DiancTreeJib = "0";
		}
		String sqlJib = "select d.jib from diancxxb d where d.id="+ DiancTreeJib;
		ResultSet rs = con.getResultSet(sqlJib.toString());
		
		try {
			while (rs.next()) {
				jib = rs.getInt("jib");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		}finally{
			con.Close();
		}

		return jib;
	}

	public String getQisriqi() {
		if(((Visit) this.getPage().getVisit()).getString9()==null||((Visit) this.getPage().getVisit()).getString9().equals("")){
			
			((Visit) this.getPage().getVisit()).setString9(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString9();
	}
	
	public void setQisriqi(String qisriqi) {
		
		if(((Visit) this.getPage().getVisit()).getString9()!=null &&!((Visit) this.getPage().getVisit()).getString9().equals(qisriqi)){
			
			((Visit) this.getPage().getVisit()).setString9(qisriqi);
		}
		
		
	}
	public String getJiezriqi() {
		if(((Visit) this.getPage().getVisit()).getString10()==null||((Visit) this.getPage().getVisit()).getString10().equals("")){
			
			((Visit) this.getPage().getVisit()).setString10(DateUtil.FormatDate(new Date()));
		}
		return ((Visit) this.getPage().getVisit()).getString10();
	}
	public void setJiezriqi(String jiezriqi) {
		
		if(((Visit) this.getPage().getVisit()).getString10()!=null &&!((Visit) this.getPage().getVisit()).getString10().equals(jiezriqi)){
			
			((Visit) this.getPage().getVisit()).setString10(jiezriqi);
		}
		
	}

	public void pageValidate(PageEvent arg0) {
		// TODO 自动生成方法存根
		
	}
}

