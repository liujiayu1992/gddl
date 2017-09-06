package com.zhiren.jt.zdt.monthreport.diaor01;

import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;

import org.apache.tapestry.contrib.palette.SortMode;
/* 
* ʱ�䣺2009-03-18 
* ���ߣ� ll
* ������ 
*     �޸�ҳ����ʾsql,�޸Ĺ���yuezbb_new��ȡ����ʽ
*/     
/* 
* ʱ�䣺2009-04-14
* ���ߣ� ll
* �޸����ݣ��޸ĵ�ȼ01��sql,yueshcyb���糧����
*			
*/  
/* 
* ʱ�䣺2009-05-4
* ���ߣ� ll
* �޸����ݣ�1���滻����,��yuezbb_zdt��Ϊyuezbb��
* 		   
*/ 
/* 
* ʱ�䣺2009-07-21
* ���ߣ� ll
* �޸����ݣ�1���޸ı�����⡣
* 		   
*/ 
/* 
* ʱ�䣺2009-07-22
* ���ߣ� sy
* �޸����ݣ�
* 		   �ж�xitxxb�й�������������ʾ���С������֡������ȵ糧
*/ 
/* 
* ʱ�䣺2009-07-27
* ���ߣ� ll
* �޸����ݣ�1����ȼ01����ͷ��ʽ������
* 		   
*/

/* 
* ʱ�䣺2009-08-21
* ���ߣ� sy
* �޸����ݣ�1�������п����ۼƲ���ʾ
* 		   
*/
public class Diaorb01report  extends BasePage implements PageValidateListener{
	
//	 �ж��Ƿ��Ǽ����û�
	public boolean isJitUserShow() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����

	}

	public boolean isGongsUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
	}

	public boolean isDiancUser() {
		return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
	}
	
	//��ʼ����
	private Date _BeginriqValue = new Date();
//	private boolean _BeginriqChange=false;
	public Date getBeginriqDate() {
		if (_BeginriqValue==null){
			_BeginriqValue = new Date();
		}
		return _BeginriqValue;
	}
	
	public void setBeginriqDate(Date _value) {
		if (_BeginriqValue.equals(_value)) {
//			_BeginriqChange=false;
		} else {
			_BeginriqValue = _value;
//			_BeginriqChange=true;
		}
	}
	
	private String _msg;

	public void setMsg(String _value) {
		_msg = _value;
	}

	public String getMsg() {
		if (_msg == null) {
			_msg = "";
		}
		return _msg;
	}
	
	private boolean _RefurbishChick = false;

	public void RefurbishButton(IRequestCycle cycle) {
		_RefurbishChick = true;
	}
	
	public void submit(IRequestCycle cycle) {
		if (_RefurbishChick) {
			_RefurbishChick = false;
			Refurbish();
		}
	}
	
	private void Refurbish() {
        //Ϊ "ˢ��" ��ť��Ӵ������
		isBegin=true;
		getSelectData();
	}

//******************ҳ���ʼ����********************//
	public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {

		Visit visit = (Visit) getPage().getVisit();
		
		if (!visit.getActivePageName().toString().equals(
				this.getPageName().toString())) {
			// �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
			visit.setActivePageName(getPageName().toString());
			visit.setList1(null);
			setNianfValue(null);
			setYuefValue(null);
			getNianfModels();
			getYuefModels();
			this.setTreeid(null);
			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			isBegin=true;
			
		}
	
	
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Fadbmdjqkbreport";//�����ú���۱�
	private String mstrReportName="Fadbmdjqkbreport";
	
	public String getPrintTable(){
		if(!isBegin){
			return "";
		}
		isBegin=false;
		if (mstrReportName.equals(RT_HET)){
			return getSelectData();
		}else{
			return "�޴˱���";
		}
	}
	
	public int getZhuangt() {
		return 1;
	}

	private int intZhuangt=0;
	public void setZhuangt(int _value) {
		intZhuangt=1;
	}

	private boolean isBegin=false;
	private String getSelectData(){
		String strSQL="";
		_CurrentPage=1;
		_AllPages=1;
		
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon cn = new JDBCcon();
		String zhuangt="";
			if(visit.getRenyjb()==3){
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
			}else if(visit.getRenyjb()==1){
				zhuangt=" and y.zhuangt=2";
			}
		long intyear;
		if (getNianfValue() == null){
			intyear=DateUtil.getYear(new Date());
		}else{
			intyear=getNianfValue().getId();
		}
		long intMonth;
		if(getYuefValue() == null){
			intMonth = DateUtil.getMonth(new Date());
		}else{
			intMonth = getYuefValue().getId();
		}

		String strGongsID = "";
		String  notHuiz="";
		String guoltj="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			guoltj=" and max(dc.id) not in("+Guoldcid()+")\n";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//			strGongsID = "  and dc.fuid=  " +this.getTreeid();
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			notHuiz=" and not grouping(f.mingc)=1  ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
			guoltj=" and max(dc.id) not in("+Guoldcid()+")\n";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			notHuiz=" and not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
			
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			guoltj="";
		}
		
		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;
		
		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		//��������
		
				strSQL="select decode(grouping(f.mingc)+grouping(dc.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;'||dc.mingc) as danw\n" +
						"       ,decode(sum(jz.jizurl),0,0,sum(jz.jizurl)) as jizrl\n" + 
						"       ,fx.fenx\n" + 
						"       ,sum(bq.shouml) as shouml_b\n" + 
						"       ,sum(bq.fady+bq.gongry_m+bq.qith_m+bq.sunh_m) as hej_m\n" + 
						"       ,sum(bq.fady) as fady_b\n" + 
						"       ,sum(bq.gongry_m) as gongrym_b\n" + 
						"       ,sum(bq.qith_m) as qithm_b\n" + 
						"       ,sum(bq.sunh_m) as sunhm_b\n" + 
						"       ,sum(decode(fx.fenx,'����',bq.kuc_m)) as kucm_b\n" + 
						"       ,sum(bq.shouyl) as shouyl_b\n" + 
						"       ,sum(bq.fadyy+bq.gongry_y+bq.qithy+bq.sunh_y) as hej_y\n" + 
						"       ,sum(bq.fadyy) as fadyy_b\n" + 
						"       ,sum(bq.gongry_y) as gongryy_b\n" + 
						"       ,sum(bq.qithy) as qithy_b\n" + 
						"       ,sum(bq.sunh_y) as sunhy_b\n" + 
						"       ,sum(decode(fx.fenx,'����',bq.kuc_y)) as kucy_b\n" + 
						"       ,sum(bq.fadl) as fadl\n" + 
//						"       ,sum(bq.gongrl) as gongrl\n" + 
//						"       ,Round(decode(sum(bq.fadl),0,0,sum(bq.fadhbzml) / sum(bq.fadl) * 100), 0) as fadbzmh\n" + 
//						"       ,Round(decode(sum(bq.gongrl),0,0,sum(bq.gongrhbzml) / sum(bq.gongrl) * 1000), 1) as gongrbzmh\n" + 
//						"       ,Round(decode(sum(bq.fadl),0,0,(sum(bq.fadhml) + 2 * sum(bq.fadhy)) / sum(bq.fadl) * 100), 0) as fadtrmh\n" + 
//						"       ,Round(decode(sum(bq.gongrl),0,0,(sum(bq.gongrhml) + 2 * sum(bq.gongrhy)) / sum(bq.gongrl) * 1000),1) as gongrtrmh\n" + 
//						"		,sum(fadhbzml) as fadhbzml_b \n"+
//						"		,sum(gongrhbzml) as gongrhbzml_b \n"+

						"	    ,sum(bq.gongrl/10) as gongrl\n" +
						"		,Round(decode(sum(bq.fadl),0,0,sum(bq.fadhbzml+bq.fadyzbzml+bq.fadqzbzml) / sum(bq.fadl) * 100), 0) as fadbzmh--������ú�۱�׼ú�����������۱�׼ú�����������۱�׼ú����/������\n" + 
						"		,Round(decode(sum(bq.gongrl),0,0,sum(bq.gongrhbzml+bq.gongryhbzml+bq.gongrqhbzml) / sum(bq.gongrl) * 1000), 1) as gongrbzmh  --������ú�۱�׼ú�����������۱�׼ú�����������۱�׼ú����/������\n" + 
						"		,Round(decode(sum(bq.fadl),0,0,(sum(bq.fadhml) + 2 * sum(bq.fadhy)+sum(bq.fadhq)) / sum(bq.fadl) * 100), 0) as fadtrmh\n" + 
						"		,Round(decode(sum(bq.gongrl),0,0,(sum(bq.gongrhml) + 2 * sum(bq.gongrhy)+sum(bq.gongrhq)) / sum(bq.gongrl) * 1000),1) as gongrtrmh\n" + 
						"       ,sum(bq.fadhbzml+bq.fadyzbzml+bq.fadqzbzml) as fadhbzml_b\n" + 
						"       ,sum(bq.gongrhbzml+bq.gongryhbzml+bq.gongrqhbzml) as gongrhbzml_b\n" + 

//						-----------------------------------------------------------------------
						",Round(decode((sum(bq.fadhy) + sum(bq.gongrhy)),0,0,\n" +
						"              ((sum(bq.fadhbzml+bq.fadyzbzml+bq.fadqzbzml) + sum(bq.gongrhbzml+bq.gongryhbzml+bq.gongrqhbzml)) * 7000*0.0041816\n" + 
						"     / ((sum(bq.fadhy) + sum(bq.gongrhy))*2+sum(bq.fadhml)+sum(bq.gongrhml)))),2) as zonghrl\n" + 
//						-------------------------------�޸ĺ�sql----------------------------------------
						",Round(decode((sum(bq.fadhml) + sum(bq.gongrhml)),0,0,\n" +
						"             ((sum(bq.fadhbzml) + sum(bq.gongrhbzml)) * 7000 - (sum(bq.fadhy) + sum(bq.gongrhy)) * 10000) * 0.0041816\n" + 
						"                      / (sum(bq.fadhml) + sum(bq.gongrhml))),2) as zonghm \n" + 

//						",Round(decode((sum(bq.fadhbzml) + sum(bq.gongrhbzml)),0,0,\n" + 
//						"              ((sum(bq.fadhbzml) + sum(bq.gongrhbzml)) * 7000\n" + 
//						"    - (sum(bq.fadhy) + sum(bq.gongrhy)) * 10000) * 0.0041816 / (sum(bq.fadhbzml) + sum(bq.gongrhbzml))),2) as zonghm"+
						" from\n" + 
						"   (select diancxxb_id,fx.fenx,fx.xuh from\n" + 
						"     (select distinct diancxxb_id from yuezbb\n" + 
						"             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n" + 
						"     ) dcid,(select decode(1,1,'����','') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�','')  as fenx,2 as xhu from dual ) fx) fx,\n" + 
						" ((select decode(1,1,'����','') as fenx\n" + 
						"            ,yz.diancxxb_id\n" + 
						"            ,shcm.shouml as shouml\n" + 
						"            ,shcm.fady as fady\n" + 
						"            ,shcm.gongry as gongry_m\n" + 
						"            ,shcm.qith as qith_m\n" + 
						"            ,shcm.sunh as sunh_m\n" + 
						"            ,shcm.kuc as kuc_m\n" + 
						"            ,shcy.shouyl as shouyl\n" + 
						"            ,shcy.fadyy as fadyy\n" + 
						"            ,shcy.gongry as gongry_y\n" + 
						"            ,shcy.qithy as qithy\n" + 
						"            ,shcy.sunh as sunh_y\n" + 
						"            ,shcy.kuc as kuc_y\n" + 
						"            ,yz.fadl as fadl\n" + 
						"            ,yz.gongrl as gongrl\n" + 
						"			 ,yz.FADMZBML as fadhbzml--����ú�۱�ú��\n" +
						"			 ,yz.FADYZBZML as fadyzbzml--�������۱�׼ú��\n" + 
						"			 ,yz.FADQZBZML as fadqzbzml--�������۱�׼ú��\n" + 
						"			 ,yz.GONGRMZBML as gongrhbzml--����ú�۱�ú��\n" + 
						"			 ,yz.GONGRYZBZML as gongryhbzml--�������۱�׼ú��\n" + 
						"			 ,yz.GONGRQZBZML as gongrqhbzml--�������۱�׼ú��\n" + 
						"			 ,yz.FADYTRML as fadhml\n" + 
						"			 ,yz.FADYTRYL as fadhy\n" + 
						"			 ,yz.FADYTRQL as fadhq\n" + 
						"			 ,yz.GONGRYTRML as gongrhml\n" + 
						"			 ,yz.GONGRYTRYL as gongrhy\n" + 
						"			 ,yz.GONGRYTRQL as gongrhq \n"+
						"       from yuezbb yz,yueshchjb shcm,\n" +
						"       (select yshc.diancxxb_id,sum(shouyl) as shouyl,sum(fadyy) as fadyy,sum(gongry) as gongry,sum(qithy) as qithy,sum(sunh) as sunh,sum(kuc) as kuc\n" +
						"			from yueshcyb yshc,diancxxb dc\n" +
						"            where riq  = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and fenx='����'\n" + 
						"                   and yshc.diancxxb_id=dc.id\n" + 
						"                   group by (yshc.diancxxb_id)\n" + 
						"                   order by max(dc.xuh)\n" + 
						"           )shcy\n" + 
						"       where yz.diancxxb_id=shcm.diancxxb_id and yz.diancxxb_id=shcy.diancxxb_id\n" + 
						"             and yz.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and shcm.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n" + 
						"             and yz.fenx = '����' and shcm.fenx='����')\n" + 
						"   union\n" + 
						"     (select decode(1,1,'�ۼ�','') as fenx\n" + 
						"            ,yz.diancxxb_id\n" + 
						"            ,shcm.shouml as shouml\n" + 
						"            ,shcm.fady as fady\n" + 
						"            ,shcm.gongry as gongry_m\n" + 
						"            ,shcm.qith as qith_m\n" + 
						"            ,shcm.sunh as sunh_m\n" + 
						"            ,shcm.kuc as kuc_m\n" + 
						"            ,shcy.shouyl as shouyl\n" + 
						"            ,shcy.fadyy as fadyy\n" + 
						"            ,shcy.gongry as gongry_y\n" + 
						"            ,shcy.qithy as qithy\n" + 
						"            ,shcy.sunh as sunh_y\n" + 
						"            ,shcy.kuc as kuc_y\n" + 
						"            ,yz.fadl as fadl\n" + 
						"            ,yz.gongrl as gongrl\n" + 
						"			 ,yz.FADMZBML as fadhbzml--����ú�۱�ú��\n" +
						"			 ,yz.FADYZBZML as fadyzbzml--�������۱�׼ú��\n" + 
						"			 ,yz.FADQZBZML as fadqzbzml--�������۱�׼ú��\n" + 
						"			 ,yz.GONGRMZBML as gongrhbzml--����ú�۱�ú��\n" + 
						"			 ,yz.GONGRYZBZML as gongryhbzml--�������۱�׼ú��\n" + 
						"			 ,yz.GONGRQZBZML as gongrqhbzml--�������۱�׼ú��\n" + 
						"			 ,yz.FADYTRML as fadhml\n" + 
						"			 ,yz.FADYTRYL as fadhy\n" + 
						"			 ,yz.FADYTRQL as fadhq\n" + 
						"			 ,yz.GONGRYTRML as gongrhml\n" + 
						"			 ,yz.GONGRYTRYL as gongrhy\n" + 
						"			 ,yz.GONGRYTRQL as gongrhq \n"+
						"       from yuezbb yz,yueshchjb shcm,\n"+
						"       (select yshc.diancxxb_id,sum(shouyl) as shouyl,sum(fadyy) as fadyy,sum(gongry) as gongry,sum(qithy) as qithy,sum(sunh) as sunh,sum(kuc) as kuc\n" +
						"			from yueshcyb yshc,diancxxb dc\n" +
						"            where riq  = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and fenx='�ۼ�'\n" + 
						"                   and yshc.diancxxb_id=dc.id\n" + 
						"                   group by (yshc.diancxxb_id)\n" + 
						"                   order by max(dc.xuh)\n" + 
						"           )shcy\n" + 
						"       where yz.diancxxb_id=shcm.diancxxb_id and yz.diancxxb_id=shcy.diancxxb_id\n" + 
						"             and yz.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd') and shcm.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n" + 
						"         and yz.fenx = '�ۼ�' and shcm.fenx='�ۼ�')\n" + 
						"       ) bq,diancxxb dc,vwfengs  f\n" + 
						"        ,(select j.diancxxb_id as id ,sum(nvl(j.jizurl,0)) as jizurl from jizb j,diancxxb dc\n" + 
						"                      where j.diancxxb_id=dc.id "+strGongsID+" and j.toucrq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
								"				 group by(j.diancxxb_id) )jz\n" + 
						"where fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
						"and bq.diancxxb_id=jz.id(+)\n" + 
						"and   fx.diancxxb_id=dc.id\n" + 
						"and   fx.fenx=bq.fenx(+)\n" + 
						"and   dc.fuid=f.id(+) "+strGongsID+"\n" + 
						"group by rollup (fx.fenx,f.mingc,dc.mingc)\n" + 
						"having not grouping (fx.fenx)=1 "+notHuiz+""+guoltj+" \n" + 
						"order by  grouping(f.mingc) desc,f.mingc,grouping(dc.mingc) desc ,dc.mingc,grouping(fx.fenx) desc ,fx.fenx";

				 ArrHeader =new String[3][27];
				 ArrHeader[0]=new String[] {"�� λ �� ��","�����豸����","���»��ۼ�","ú    ̿","ú    ̿","ú    ̿","ú    ̿","ú    ̿","ú    ̿","�� ��","ʯ    ��","ʯ    ��","ʯ    ��","ʯ    ��","ʯ    ��","ʯ    ��","�� ��","ʵ�����","ʵ�����","ú  ��","ú  ��","ú  ��","ú  ��","��׼ú��","��׼ú��","������","������"};
				 ArrHeader[1]=new String[] {"�� λ �� ��","�����豸����","���»��ۼ�","ʵ ��","��    ��","��    ��","��    ��","��    ��","��    ��","�� ��","ʵ ��","��    ��","��    ��","��    ��","��    ��","��    ��","�� ��","��   ��<br>(�� ��)","��   ��<br>(ʮ�ڽ���)","��׼ú��","��׼ú��","��Ȼú��","��Ȼú��","��  ��<br>(��)","��  ��<br>(��)","�ۺ�ȼ��","�� ȼ ú"};
				 ArrHeader[2]=new String[] {"�� λ �� ��","�����豸����","���»��ۼ�","ʵ ��","��    ��","��    ��","��    ��","��    ��","��    ��","�� ��","ʵ ��","��    ��","��    ��","��    ��","��    ��","��    ��","�� ��","��   ��<br>(�� ��)","��   ��<br>(ʮ�ڽ���)","�� ��","�� ��","�� ��","�� ��","��  ��<br>(��)","��  ��<br>(��)","�ۺ�ȼ��","�� ȼ ú"};
				  
				 ArrWidth =new int[] {150,80,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70,70};
				 
	//		System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// ����
			
			Table tb = new Table(rs,3, 0, 1);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"�µ�ȼ01��", ArrWidth);
			rt.setDefaultTitle(1, 4, "���λ(����):"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 4, "��λ:�֡���ǧ��ʱ����/ǧ��ʱ", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(15, 3, "��ȼ01��", Table.ALIGN_RIGHT);
			
			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(18);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;

			tb.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(14,3,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
			 
		
			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	
	//�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
	public String getBiaotmc(){
		String biaotmc="";
		JDBCcon cn = new JDBCcon();
		String sql_biaotmc="select  zhi from xitxxb where mingc='������ⵥλ����'";
		ResultSet rs=cn.getResultSet(sql_biaotmc);
		try {
			while(rs.next()){
				 biaotmc=rs.getString("zhi");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return biaotmc;
		
	}
	
	
//	��ѯ�Ƿ����ù��˵糧id
	private String  Guoldcid(){
		JDBCcon con=new JDBCcon();
		String dcid="";
		ResultSetList rsl=con.getResultSetList("select zhi from xitxxb where mingc='�����Ϻ������ַ��硢�Ϻ������ȵ硢�Ϻ���������'\n");
		
		 while(rsl.next()){
			 dcid=rsl.getString("zhi");
		 }
	    con.Close();
			
		return dcid;
	}
	
	
//	�糧����
	public boolean _diancmcchange = false;
	private IDropDownBean _DiancmcValue;

	public IDropDownBean getDiancmcValue() {
		if(_DiancmcValue==null){
			_DiancmcValue=(IDropDownBean)getIDiancmcModel().getOption(0);
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
		
		String sql="";
		sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
		_IDiancmcModel = new IDropDownModel(sql);
		

	}
	
	
	
//	���
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
			for (int i = 0; i < _NianfModel.getOptionCount(); i++) {
				Object obj = _NianfModel.getOption(i);
				if (_nianf == ((IDropDownBean) obj).getId()) {
					_NianfValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _NianfValue;
	}
	public boolean nianfchanged = false;
	public void setNianfValue(IDropDownBean Value) {
		if (_NianfValue != Value) {
			nianfchanged = true;
		}
		_NianfValue = Value;
	}

	public IPropertySelectionModel getNianfModels() {
		List listNianf = new ArrayList();
		int i;
		for (i = 2000; i <= DateUtil.getYear(new Date())+1; i++) {
			listNianf.add(new IDropDownBean(i, String.valueOf(i)));
		}
		_NianfModel = new IDropDownModel(listNianf);
		return _NianfModel;
	}

	public void setNianfModel(IPropertySelectionModel _value) {
		_NianfModel = _value;
	}
	
	/**
	 * �·�
	 */
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
			for (int i = 0; i < _YuefModel.getOptionCount(); i++) {
				Object obj = _YuefModel.getOption(i);
				if (_yuef == ((IDropDownBean) obj).getId()) {
					_YuefValue = (IDropDownBean) obj;
					break;
				}
			}
		}
		return _YuefValue;
	}
	public boolean yuefchanged = false;
	public void setYuefValue(IDropDownBean Value) {
		if (_YuefValue != Value) {
			yuefchanged = true;
		}
		_YuefValue = Value;
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


	public SortMode getSort() {
		return SortMode.USER;
	}
	
	private String _pageLink;

	public boolean getRaw() {
		return true;
	}

	public String getpageLink() {
		if (!_pageLink.equals("")) {
			return _pageLink;
		} else {
			return "";
		}
	}

	// Page����
	protected void initialize() {
		_msg = "";
		_pageLink = "";
	}

	private String FormatDate(Date _date) {
		if (_date == null) {
			return "";
		}
		return DateUtil.Formatdate("yyyy��MM��dd��", _date);
	}
	
//	***************************�����ʼ����***************************//
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
		
	public Date getYesterday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.add(Calendar.DATE,-1);
		return cal.getTime();
	}
	
	public Date getMonthFirstday(Date dat){
		Calendar cal = Calendar.getInstance();
		cal.setTime(dat);
		cal.set(Calendar.DATE,cal.getActualMinimum(Calendar.DATE));
		return cal.getTime();
	}
	
	protected String getpageLinks() {
		String PageLink = "";
		IRequestCycle cycle = this.getRequestCycle();
		if (cycle.isRewinding())
			return "";
		String _servername = cycle.getRequestContext().getRequest()
				.getServerName();
		String _scheme = cycle.getRequestContext().getRequest().getScheme();
		int _ServerPort = cycle.getRequestContext().getRequest()
				.getServerPort();
		if (_ServerPort != 80) {
			PageLink = _scheme + "://" + _servername + ":" + _ServerPort
					+ this.getEngine().getContextPath();
		} else {
			PageLink = _scheme + "://" + _servername
					+ this.getEngine().getContextPath();
		}
		return PageLink;
	}
	 //	ҳ���ж�����
    public void pageValidate(PageEvent arg0) {
    	String PageName = arg0.getPage().getPageName();
    	String ValPageName = Login.ValidateLogin(arg0.getPage());
    	if (!PageName.equals(ValPageName)) {
    		ValPageName = Login.ValidateAdmin(arg0.getPage());
    		if(!PageName.equals(ValPageName)) {
    			IPage ipage = arg0.getRequestCycle().getPage(ValPageName);
        		throw new PageRedirectException(ipage);
    		}
		}
	}

	
	
	
	
//	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
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

	public void getToolBars() {
		Toolbar tb1 = new Toolbar("tbdiv");
		
		
		
		tb1.addText(new ToolbarText("���:"));
		ComboBox nianf = new ComboBox();
		nianf.setTransform("NIANF");
		nianf.setWidth(60);
		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
		
		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
		setTree(etu);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
		
		
		
		ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
		tb1.addItem(tb);
		
		setToolbar(tb1);
		
		
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

	private String treeid;

	/*public String getTreeid() {
		if (treeid == null || "".equals(treeid)) {
			return "-1";
		}
		return treeid;
	}

	public void setTreeid(String treeid) {
		this.treeid = treeid;
	}*/
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

	
}