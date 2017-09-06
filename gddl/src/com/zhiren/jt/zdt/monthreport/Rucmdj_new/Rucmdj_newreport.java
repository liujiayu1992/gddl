/*
 *���ߣ�zuoh
 *ʱ�䣺2010-11-19 
 *������ʵ��ҳ�治�Զ�ˢ��
 */
package com.zhiren.jt.zdt.monthreport.Rucmdj_new;

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
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

/* 
* ʱ�䣺2009-05-20
* ���ߣ� sy
* �޸����ݣ�����ƽ�׵糧��һ�����ƣ�ƽ��һ���ƽ�׶���ͬ��һ������ϵͳ����������ݡ�
* 		   �������糧�ͬһ���±�ҳ��ʱҳ�����������beginResponse()���������û�����Ϊ�糧����
  �жϵ�½�糧��糧���Ƿ�һ�£������¼���ˢ��ҳ�档
* 		   
*/ 
/* 
* ʱ�䣺2009-06-12
* ���ߣ� ll
* �޸����ݣ�1����������˾��½ʱͳ�ƿھ�Ĭ�ϡ�����˾ͳ�ơ���
*          2��������˾��½ʱȥ���ܼơ��С�
* 		   
*/ 

/* 
* ʱ�䣺2009-07-10
* ���ߣ� chh
* �޸����ݣ�1����������û������ʱ���������㡱��
* 		   
*/ 
/* 
* ʱ�䣺2009-12-03
* ���ߣ� ll
* �޸����ݣ� �±�-cpi10�� ����3���ӱ� 
		1.ֻ�������������ı��º��ۼ�
		2.ֻ�������ص㡱�ı��º��ۼ�
		3.ֻ�������г����ı��º��ۼ�		 
		����=�ص�+�г�+����
		�ص�=�ص�+����
		�г�=�г�		 
		ԭcpi10����.
* 		   
*/ 

/* 
* ʱ�䣺2009-12-03
* ���ߣ� chh
* �޸����ݣ�ԭ��糧���ھ�ͳ�ƣ��볧�ͼ�ͳ�Ʋ���ȷ����
* 		   ѡ��ֹ�˾������ȷ����
* 		   �ص�=�ص�+���� ,�ϲ��ص������������Ϊ�ص�
* 		   
*/ 
/* 
* ʱ�䣺2010-01-4
* ���ߣ� ll
* �޸����ݣ�1�����ӡ����ֹ�˾ͳ�ơ�������ѯ��
* 			2�����ӡ��±��ھ��������򣬶����ݽ��в�ѯ��
*/ 
/* 
* ʱ�䣺2010-03-10
* ���ߣ� sy
* �޸����ݣ�1���ڱ����м���ھ���Ϣ��ʾ
*/
/* 
* ʱ�䣺2010-10-8
* ���ߣ� sy
* �޸����ݣ�1���޸��㷨������ݿ�汾���⵼�µ�bug
*/
/* 
* ʱ�䣺2010-11-05
* ���ߣ�liufl
* �޸����ݣ�1������"�ھ�����"�����˵�����ѡ��"�ȱȿھ�"��"�±��糧�ھ�"
*          2��δ��������ú�ɫ������ʾ         
*/
/* 
* ʱ�䣺2010-11-23
* ���ߣ� liufl
* �޸����ݣ�1����ͷ̫����ˢ�°�ť��ʾ���������޸ı����ͷ��������ʾ
*/
/* 
* ʱ�䣺2010-12-15
* ���ߣ� liufl
* �޸����ݣ�1���޸�sql�����λ�����������
*/
/* 
* ʱ�䣺2010-12-21
* ���ߣ� liufl
* �޸����ݣ�1���糧��ѡ�ֹ�˾+���ֹ�˾ͳ��ʱ���˼����ܼƣ��޸�sql
*           2���糧��ѡ�糧+���ֹ�˾ͳ��ʱ���˼����ܼƣ��޸�sql
*            3���糧��ѡ��˾+���糧ͳ�ƴ��󣺲���ʾ�糧���޸�sql
*            
*/
/* 
* ʱ�䣺2010-12-31
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql����λѡ�糧ʱ����ʾ��ͳ�ƿھ���������
*            2���޸�sql,ͳ�ƿھ�ѡ��������ͳ�ơ�ʱ��ɫ��ʾ����ȷ
*            
*           
*/
/* 
* ʱ�䣺2011-01-10
* ���ߣ�liufl
* �޸����ݣ��޸ĵ�������ʱ��������Ҳ����������
*            
*/
/* 
* ʱ�䣺2011-02-17
* ���ߣ�liufl
* �޸����ݣ��޸�ҳüҳ�ţ�ʹ�������λ�ʹ�ӡ����һ��
*            
*/
/* 
* ʱ�䣺2012-01-12
* ���ߣ�liufl
* �޸����ݣ����ϵͳ���������Ƿ��ѯ��ͼ
*            
*/
public class Rucmdj_newreport  extends BasePage implements PageValidateListener{
	
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
//			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			this.setBaoblxValue2(null);
			this.getIBaoblxModels2();			
			isBegin=true;
			
		}
		if(visit.getRenyjb()==3){
			if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
				visit.setActivePageName(getPageName().toString());
				visit.setList1(null);
				setNianfValue(null);
				setYuefValue(null);
				getNianfModels();
				getYuefModels();
			
				this.setTreeid(null);
//				this.getTree();
				visit.setDropDownBean4(null);
				visit.setProSelectionModel4(null);
				visit.setProSelectionModel2(null);
				visit.setDropDownBean2(null);
				this.setBaoblxValue(null);
				this.getIBaoblxModels();
				this.setBaoblxValue2(null);
				this.getIBaoblxModels2();
				isBegin=true;
			}
		}
	
		getToolBars() ;
		Refurbish();
	}
	
	private String RT_HET="Yuedmjgmxreport";//�¶�ú�۸���ϸ
	private String mstrReportName="Yuedmjgmxreport";
	
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
		String shulzt="";
		String zhilzt="";
		String youzt="";
		String guolzj="";
		String yb_sl="";
		String yb_zl="";
		String yb_dj="";
		String yb_rcy="";
			if(visit.getRenyjb()==3){
				yb_sl="yueslb";
				yb_zl="yuezlb";
				yb_dj="yuercbmdj";
				yb_rcy="rucycbb";
				zhuangt="";
				shulzt="";
				zhilzt="";
				youzt="";
			}else if(visit.getRenyjb()==2){
				ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
				try {
					if(rs.next()) {
						String dcids=rs.getString("zhi");
						if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
							yb_sl="vwyueslb";
							yb_zl="vwyuezlb";
							yb_dj="vwyuercbmdj";
							yb_rcy="vwrucycbb";
						}else {
							yb_sl="yueslb";
							yb_zl="yuezlb";
							yb_dj="yuercbmdj";
							yb_rcy="rucycbb";
						}
					}else {
						yb_sl="yueslb";
						yb_zl="yuezlb";
						yb_dj="yuercbmdj";
						yb_rcy="rucycbb";
					}
					zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
					shulzt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
					zhilzt=" and (zl.zhuangt=1 or zl.zhuangt=2)";
					youzt=" and (r.zhuangt=1 or r.zhuangt=2)";
					cn.Close();
					rs.close();
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}

			}else if(visit.getRenyjb()==1){
				ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
				try {
					if(rs.next()) {
						String dcids=rs.getString("zhi");
						if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
							yb_sl="vwyueslb";
							yb_zl="vwyuezlb";
							yb_dj="vwyuercbmdj";
							yb_rcy="vwrucycbb";
						}else {
							yb_sl="yueslb";
							yb_zl="yuezlb";
							yb_dj="yuercbmdj";
							yb_rcy="rucycbb";
						}
					}else {
						yb_sl="yueslb";
						yb_zl="yuezlb";
						yb_dj="yuercbmdj";
						yb_rcy="rucycbb";
					}
					zhuangt=" and y.zhuangt=2";
					shulzt=" and sl.zhuangt=2";
					zhilzt=" and zl.zhuangt=2";
					youzt=" and r.zhuangt=2";
					cn.Close();
					rs.close();
				} catch (SQLException e) {
					// TODO �Զ����� catch ��
					e.printStackTrace();
				}

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
		String strDiancFID="";
		String notHuiz="";
		String notHuiz1="";
		String fenz_bt="";
		String where="";
		String tj="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			strDiancFID="'',";
			notHuiz="";
			notHuiz1="";
			guolzj="";
			tj="  grouping(fx.fenx)f,  grouping(dc.mingc)mc,  grouping(dc.fgsmc)fgs,";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			strDiancFID=this.getTreeid()+",";
			notHuiz1=" --where a.fgs=0\n  having not (grouping(dc.fgsmc)=1 )";
			notHuiz=" and a.fgs=0 -- having not (grouping(dc.fgsmc)=1 and grouping(dc.mingc)=1 )";
			guolzj=" and grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
			fenz_bt=" grouping(dc.mingc)mc, grouping(dc.fgsmc)fgs,";
			where="  where c.fgs+c.mc=1 ";
			tj="  grouping(fx.fenx)f,  grouping(dc.mingc)mc,  grouping(dc.fgsmc)fgs,";
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			strDiancFID="'',";
			notHuiz1=" --where a.fgs=0 \n having not( grouping(dc.fgsmc) =1 )";
			notHuiz=" and a.mc=0--having not grouping(dc.mingc)=1";
			//guolzj=" and grouping(dc.mingc)=0\n";
			guolzj=" and (grouping(dc.mingc)=0 or grouping(dc.fgsmc)=0)\n";
			fenz_bt=" grouping(dc.mingc)mc,";
			where="  where  c.mc=0 ";
			tj="  grouping(fx.fenx)f,  grouping(dc.mingc)mc, ";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			strDiancFID="'',";
			fenz_bt=" grouping(dc.mingc)mc";
			where="  where  c.mc=1 ";
			tj="  grouping(fx.fenx)f,  grouping(dc.mingc)mc ,";
		}

		//�����ͷ����
		Report rt = new Report();
		int ArrWidth[] = null;
		String ArrHeader[][] = null;

		int iFixedRows=0;//�̶��к�
		int iCol=0;//����
		//��������
		String biaot="";
		String dianc="";
		String tiaoj="";
		String fenz="";
		String mc="";
		String jihkj="";
		String rucycbb_bq="";
		String rucycbb_tq="";
		String rucycbb_tj="";
		String rucycbb_bt="";
		String fx="";
		String biaot0="";
		String shul="";
		String strYouzb="";
		String dianckjmx_bm="";
		String dianckjmx_tj="";
		String strzt="";
		String koujid="";
		String diancdwmc="";
		/*String strFunctionName="";
		if(getYuebValue().getValue().equals("ȫ��")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			strFunctionName = "getShenhzt";
		}else{
			dianckjmx_bm=",dianckjmx kjmx";
			dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
			strFunctionName = "getShenhzt_fenkj";
		}
		String strzt=strFunctionName+"(dqmc,fgsmc,diancmc1,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','����'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" ) as bqby," +
        		strFunctionName+"(dqmc,fgsmc,diancmc1,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" ) as bqlj," +
        		strFunctionName+"(dqmc,fgsmc,diancmc1,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuercbmdj','����'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" ) as tqby," +
        		strFunctionName+"(dqmc,fgsmc,diancmc1,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuercbmdj','�ۼ�'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" ) as tqlj " ;*/

		if(getYuebValue().getValue().equals("ȫ��")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			koujid="'',";
	    }else{
	    	dianckjmx_bm=",dianckjmx kjmx";
	    	dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
	    	koujid=getYuebValue().getId()+",";
	    }

		if(getBaoblxValue2().getValue().equals("�糧���ھ�ͳ��")){
			diancdwmc="dqmc,fgsmc,diancmc1,";
		}else{
			diancdwmc="dqmc,fgsmc,diancmc0,";

		}

		strzt="nvl(getShenhzt("+koujid+strDiancFID+diancdwmc+"to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','����'"+","+visit.getRenyjb()+"),0)as bqby,\n" +
		      "nvl(getShenhzt("+koujid+strDiancFID+diancdwmc+"to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�'"+","+visit.getRenyjb()+"),0) as bqlj,\n" +
		      "nvl(getShenhzt("+koujid+strDiancFID+diancdwmc+"add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuercbmdj','����'"+","+visit.getRenyjb()+"),0) as tqby,\n" +
		      "nvl(getShenhzt("+koujid+strDiancFID+diancdwmc+"add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuercbmdj','�ۼ�'"+","+visit.getRenyjb()+"),0) as tqlj\n" ;


			if(getBaoblxValue2().getValue().equals("�糧���ھ�ͳ��")){
		    	biaot0="select   c.aa,c.fenx,(bq_chebj+bq_yunj+bq_zaf)bq_daoczhj,(tq_chebj+tq_yunj+tq_zaf)tq_daoczhj,bq_chebj,tq_chebj,\n" +
			      "bq_yunj,tq_yunj,bq_zaf,tq_zaf,bq_rucbmdj,tq_rucbmdj,a.tianrypjdj,b.tianrypjdj, \n";
		    	if(jib==3){
					biaot="case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
						"  case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
						"  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end  end aa,'' as dqmc,dc.mingc as diancmc1,dc.fgsmc as fgsmc,";
					mc = "dc.fgsmc";
					dianc=" vwdianc dc \n";
					tiaoj="";
					fenz= "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx),(dc.fgsmc,dc.xuh,dc.mingc,fx.fenx),(dc.fgsxh,dc.fgsmc,dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n"
					+ "    order by\n"
					+ "    decode(grouping(dc.mingc)+grouping(dc.fgsmc)+grouping(fx.kouj),3,3,0) desc,\n"
					+ "    decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,2,0) desc,\n"
					+ "    max(dc.fgsxh),dc.fgsmc,\n"
					+ "    grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n"
					+ "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";
				}else{
					if(getBaoblxValue().getValue().equals("������ͳ��")){
						/*//diancdwmc="dqmc,fgsmc,diancmc1,";
						biaot=	"case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
							"        case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
							"        case when grouping(dq.mingc)=0 then dq.mingc||'�ϼ�' else '�ܼ�' end end  end aa,decode(grouping(dc.mingc),1,dq.mingc,'') as dqmc,'' as fgsmc,dc.mingc as diancmc1,";

						mc = "dq.mingc";
						dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
						tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
						fenz="group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dq.mingc,fx.fenx),(dq.mingc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n" +
						"order by  decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(fx.kouj),3,3,0) desc,\n" +
						"   decode(grouping(dc.mingc)+grouping(dq.mingc),2,2,0) desc,\n" +
						"   min(dq.xuh), dq.mingc,\n" +
						"   grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n" +
						"   grouping(fx.kouj) desc,fx.kouj ,\n" +
						"   fx.fenx";
						fenz_bt="";
						where="";*/

//						diancdwmc="dqmc,fgsmc,diancmc1,";
						biaot=	"case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
							"        case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
							"        case when grouping(dq.mingc)=0 then dq.mingc||'�ϼ�' else '�ܼ�' end end  end aa,decode(grouping(dc.mingc),1,dq.mingc,decode("+jib+",2,'',dq.mingc)) as dqmc,decode("+jib+",2,'',(select mingc from diancxxb where id="+this.getTreeid()+")) as fgsmc,dc.mingc as diancmc1,";
						mc = "dq.mingc";
						dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
						tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
						fenz="group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dq.mingc,fx.fenx),(dq.mingc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n" +
							"order by  decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(fx.kouj),3,3,0) desc,\n" +
							"   decode(grouping(dc.mingc)+grouping(dq.mingc),2,2,0) desc,\n" +
							"   min(dq.xuh), dq.mingc,\n" +
							"   grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n" +
							"   grouping(fx.kouj) desc,fx.kouj ,\n" +
							"   fx.fenx";
						fenz_bt="";
						where="";

					}else if(getBaoblxValue().getValue().equals("���糧ͳ��")){
						//diancdwmc="dqmc,fgsmc,diancmc1,";
						biaot="case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
							"  case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
							"  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end  end aa,'' as dqmc,dc.mingc as diancmc1,dc.fgsmc as fgsmc,";
						mc = "dc.fgsmc";
						dianc=" vwdianc dc \n";
						tiaoj="";
						fenz= "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n"
						+"    \n"
						+ "order by\n"
						+ "    decode(grouping(dc.mingc)+grouping(dc.fgsmc)+grouping(fx.kouj),3,3,0) desc,\n"
						+ "    decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,2,0) desc,\n"
						//+ "    min(dc.fgsmc), dc.fgsxh,dc.fgsmc,\n"
						+ "    max(dc.fgsxh),dc.fgsmc,\n"
						+ "    grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n"
						+ "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";
						//fenz_bt=" grouping(dc.mingc)mc, grouping(dc.fgsmc)fgs,";
					}else if(getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")){
						//diancdwmc="dqmc,fgsmc,diancmc1,";
						biaot="case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
							"  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end aa,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc1,";
						mc = "dc.fgsmc";
						dianc=" vwdianc dc \n";
						tiaoj="";
						fenz= "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx))\n"
						+notHuiz1+
						"\n"
						+ "order by\n"
						+ "    decode(grouping(dc.fgsmc)+grouping(fx.kouj),2,2,0) desc,\n"
						+ "    decode(grouping(dc.fgsmc),1,1,0) desc,\n"
						//+ "    min(dc.fgsmc), dc.fgsxh,dc.fgsmc,\n"
						+ "    max(dc.fgsxh),dc.fgsmc,\n"
						+ "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";

						fenz_bt="";
						where="";
					}
		}
					if(getKoujlxValue().getValue().equals("�ȱȿھ�")){
						shul="  sum(sl.laimsl*dc.konggbl) as laimsl,\n"
							+"			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl)) as farl,\n"
							+"			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as buhs_daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as chebj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as yunj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as zaf ";
						strYouzb="sum(r.shul*dianc.konggbl) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul*dianc.konggbl)/sum(r.shul*dianc.konggbl),2)) as tianrypjdj";
					}else{
						shul="  sum(sl.laimsl) as laimsl,\n"
							+"			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl,\n"
							+"			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*sl.laimsl)/sum(sl.laimsl),4)) as buhs_daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*sl.laimsl)/sum(sl.laimsl),2)) as chebj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as zaf  ";
						strYouzb="sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj";
					}
					strSQL=biaot0+strzt+"\n"
						+ " from ( select rownum as xuh,c.* from(select  "+biaot+fenz_bt+" fx.fenx as fenx,\n"
						+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n"
						+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n"
						+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n"
						+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n"
						+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n"
						+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n"
						+ "      Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n"
						+ "      Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n"
						+ "		 Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n"
						+ "      Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj\n"
						+ " from\n"
						+ "     (select distinct dcid.diancxxb_id,kouj,fx.fenx,fx.xuh from\n"
						+ "             (select distinct diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj\n"
						+ "                     from  "+yb_dj+" y,yuetjkjb kj\n"
						+ "                     where y.yuetjkjb_id=kj.id\n"
						+ "                     and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
						+ "                     or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'))) ) dcid,vwfenxYue fx,diancxxb dc"+dianckjmx_bm+" \n"
						+ "               where dc.id=dcid.diancxxb_id   "+dianckjmx_tj+strGongsID+"   ) fx,\n"
						+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
						+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
						+ "           where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') \n"
						+ "           and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id and kj.diancxxb_id=dc.id\n"
						+ "           and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
						+ "           group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) bq,\n"

						+ "     (select y.fenx,kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id) as kouj," +shul+ "\n"
						+ "      from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n"
						+ "      	where kj.riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) \n"
						+ "         and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id  and kj.diancxxb_id=dc.id\n"
						+ "         and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+" \n"
						+ "      	group by (kj.diancxxb_id,decode(kj.jihkjb_id,3,1,kj.jihkjb_id),y.fenx)) tq,"+dianc+",jihkjb j\n"
						+ " where fx.diancxxb_id=tq.diancxxb_id(+)\n"
						+ "   and fx.diancxxb_id=bq.diancxxb_id(+)\n"
						+ "   and fx.diancxxb_id=dc.id\n"
						+ "   and fx.fenx=bq.fenx(+)\n"
						+ "   and fx.fenx=tq.fenx(+)\n"
						+ "   and fx.kouj=bq.kouj(+)\n"
						+ "   and fx.kouj=tq.kouj(+)\n"
						+ "   and fx.kouj=j.id\n" + tiaoj
						+strGongsID+"\n"
						+ fenz +")c "
                         + where+") c,"

						+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'�ܼ�',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n"
						//+"     r.fenx,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n"
						+"r.fenx,"+strYouzb+"\n"
						+"      from  "+yb_rcy+" r ,diancxxb dianc,"+dianc +"\n"
						+"      where r.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n"
						+"      and r.diancxxb_id=dc.id\n" +tiaoj  +youzt +strGongsID
						+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"
						+"      having not grouping(fenx)=1) a,"

						+"(select decode(grouping(dc.mingc)+grouping("+mc+"),2,'�ܼ�',1,"+mc+", '&nbsp;&nbsp;'||dc.mingc) as diancmc,\n"
						//+"     	r.fenx,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n"
						+" r.fenx,"+strYouzb+"\n"
						+" from  "+yb_rcy+" r ,diancxxb dianc,"+dianc +"\n"
						+"      where r.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) \n"
						+"      and r.diancxxb_id=dc.id\n" +tiaoj +youzt +strGongsID
						+"      group by rollup (r.fenx,"+mc+",dc.mingc)\n"
						+"      having not grouping(fenx)=1) b"

						+"    where c.aa=a.diancmc (+) and c.fenx=a.fenx(+) and c.aa=b.diancmc(+) and c.fenx=b.fenx(+) \n  order by c.xuh";


		    }else{
		    	biaot0="select a.DANWMC,a.FENX,a.BQ_DAOCZHJ,a.TQ_DAOCZHJ,a.BQ_CHEBJ,a.TQ_CHEBJ,a.BQ_YUNJ,a.TQ_YUNJ,a.BQ_ZAF,a.TQ_ZAF,a.BQ_RUCBMDJ,\n" +
			    		 "a.TQ_RUCBMDJ,a.BQ_TIANRMDJ,a.TQ_TIANRMDJ,  \n";
			    	if(jib==3){
			    		mc = "dc.fgsmc";
						biaot=" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
						dianc=" vwdianc dc \n";
						tiaoj="";
						fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
						      " having not grouping(fx.fenx)=1 "+guolzj+
							 " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
							 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
						fx="  where a.f=0 and a.mc=0  \n ";
			    	}else{
			    		if(getBaoblxValue().getValue().equals("������ͳ��")){
			    		//diancdwmc="dqmc,'',diancmc0";
			    		/*strzt="getshenhzt(dqmc,'',diancmc0,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','����') as bqby,\n" +
					      "getshenhzt(dqmc,'',diancmc0,last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','����') as tqby,\n" +
					      "getshenhzt(dqmc,'',diancmc0,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�') as bqlj,\n" +
					      "getshenhzt(dqmc,'',diancmc0,last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','�ۼ�') as tqlj\n" ;*/
			    		if(jib!=3){
			    			notHuiz=" ";
			    		}
			    		/*mc="dq.mingc";
						biaot="  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(grouping(dc.mingc),1,dq.mingc,'') as dqmc,'' as fgsmc,dc.mingc as diancmc0";
						dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
						tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
						fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
							 "--having not grouping(fx.fenx)=1\n" +
							 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
							 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
						fx=" where a.f=0 \n";
						tj=" grouping(fx.fenx)f, ";*/
			    		mc="dq.mingc";
						biaot="  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(grouping(dc.mingc),1,dq.mingc,decode("+jib+",2,'',dq.mingc)) as dqmc,decode("+jib+",2,'',(select mingc from diancxxb where id="+this.getTreeid()+")) fgsmc,dc.mingc as diancmc0";
						dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
						tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
						fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
							 "--having not grouping(fx.fenx)=1\n" +
							 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
							 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
						fx=" where a.f=0 \n";
						tj=" grouping(fx.fenx)f, ";
					}else if(getBaoblxValue().getValue().equals("���糧ͳ��")){
						//diancdwmc="'',fgsmc,diancmc0";diancdwmc="'',fgsmc,''";
						/*strzt="getshenhzt('',fgsmc,diancmc0,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','����') as bqby,\n" +
					      "getshenhzt('',fgsmc,diancmc0,last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','����') as tqby,\n" +
					      "getshenhzt('',fgsmc,diancmc0,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�') as bqlj,\n" +
					      "getshenhzt('',fgsmc,diancmc0,last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','�ۼ�') as tqlj\n" ;*/
						mc = "dc.fgsmc";
						biaot=" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
						dianc=" vwdianc dc \n";
						tiaoj="";
						fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
						      " having not grouping(fx.fenx)=1 "+guolzj+
							 " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
							 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
						fx="  where a.f=0  --and a.fgs=0 \n ";
						/*if(jib==3){
							notHuiz=" and a.mc=0 ";
						}else{
							notHuiz="";
						}*/
					}else if(getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")){
						//diancdwmc="'',fgsmc,''";
						/*strzt="getshenhzt('',fgsmc,'',to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','����') as bqby,\n" +
					      "getshenhzt('',fgsmc,'',last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','����') as tqby,\n" +
					      "getshenhzt('',fgsmc,'',to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�') as bqlj,\n" +
					      "getshenhzt('',fgsmc,'',last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')),'yuercbmdj','�ۼ�') as tqlj\n" ;*/

						mc = "dc.fgsmc";
						biaot=" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
						dianc=" vwdianc dc \n";
						tiaoj="";
						fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
						   //   " --having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)"+guolzj+
						"having not grouping(fx.fenx)=1 "+guolzj +
							 " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
							 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
						if(jib==1||jib==2){
							fx="  where a.f=0 and a.mc=1  ";
						}else{
							fx="  where (a.f=0 or a.mc=1)  ";
						}

						if(jib==3){
							notHuiz=" and a.mc=1 ";
						}else{
							notHuiz=" ";
						}

					}
			    	}
			    	if(getBaoblxValue2().getValue().equals("�ص㶩��")){
			    		jihkj=" and (kj.jihkjb_id=1 or kj.jihkjb_id=3)";
			    		rucycbb_bq="";
			    		rucycbb_tq="";
			    		rucycbb_tj="";
			    		rucycbb_bt=" '' as bq_tianrmdj,\n" + "        '' as tq_tianrmdj\n" ;
			    	}else if(getBaoblxValue2().getValue().equals("�г��ɹ�")){
			    		jihkj=" and kj.jihkjb_id=2";
			    		rucycbb_bq="";
			    		rucycbb_tq="";
			    		rucycbb_tj="";
			    		rucycbb_bt=" '' as bq_tianrmdj,\n" +  "        '' as tq_tianrmdj\n" ;
			    	}else{
			    		jihkj="";
			    		rucycbb_bq= "           ,( select r.fenx,r.diancxxb_id,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n" +
			    		 "             from  "+yb_rcy+" r where r.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') "+youzt+" group by (r.diancxxb_id,r.fenx)) a,\n" ;

			    		rucycbb_tq="           ( select r.fenx,r.diancxxb_id,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n" +
			    		 "             from  "+yb_rcy+" r where r.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)  "+youzt+" group by (r.diancxxb_id,r.fenx)) b\n" ;

			    		rucycbb_tj= " and fx.diancxxb_id=a.diancxxb_id(+)\n" +
			    		 "        and fx.fenx=a.fenx(+)\n" +
			    		 "        and fx.diancxxb_id=b.diancxxb_id(+)\n" +
			    		 "        and fx.fenx=b.fenx(+)\n";
			    		rucycbb_bt=
			    			"decode(grouping("+mc+")+grouping (dc.mingc),2,\n" +
			    			"           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2),1,\n" +
			    			"           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2),\n" +
			    			"           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2)) as bq_tianrmdj,\n" +
			    			"     decode(grouping("+mc+")+grouping (dc.mingc),2,\n" +
			    			"\t      \t  Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2),1 ,\n" +
			    			"          Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2),\n" +
			    			"          Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2)) as tq_tianrmdj\n";
			    	}

			    	if(getKoujlxValue().getValue().equals("�ȱȿھ�")){
						shul="sum(sl.laimsl*dc.konggbl) as laimsl,\n"
							+"			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl)) as farl,\n"
							+"			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as buhs_daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as chebj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as yunj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as zaf,\n"
			    		    + "         decode(sum(sl.laimsl),0,0,Round(sum(y.buhsbmdj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as rucbmdj";
					}else{
						shul="  sum(sl.laimsl) as laimsl,\n"
							+"			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl,\n"
							+"			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*sl.laimsl)/sum(sl.laimsl),4)) as buhs_daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as daoczhj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*sl.laimsl)/sum(sl.laimsl),2)) as chebj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n"
							+ "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as zaf ,\n"
						    + "         decode(sum(sl.laimsl),0,0,Round(sum(y.buhsbmdj*sl.laimsl)/sum(sl.laimsl),4)) as rucbmdj";
					}
			    	  strSQL= biaot0+strzt+"from (select "+tj+biaot+",\n" +
			    		 "        decode(1,1,fx.fenx,'') as fenx,\n" +
			    		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.daoczhj*bq.laimsl)/sum(bq.laimsl)),2) as bq_daoczhj ,\n" +
			    		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.daoczhj*tq.laimsl)/sum(tq.laimsl)),2) as tq_daoczhj  ,\n" +
			    		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.chebj*bq.laimsl)/sum(bq.laimsl)),2) as bq_chebj,\n" +
			    		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.chebj*tq.laimsl)/sum(tq.laimsl)),2) as tq_chebj,\n" +
			    		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.yunj*bq.laimsl)/sum(bq.laimsl)),2) as bq_yunj,\n" +
			    		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.yunj*tq.laimsl)/sum(tq.laimsl)),2) as tq_yunj,\n" +
			    		 "        Round(decode(sum(bq.laimsl),0,0,sum(bq.zaf*bq.laimsl)/sum(bq.laimsl)),2) as bq_zaf,\n" +
			    		 "        Round(decode(sum(tq.laimsl),0,0,sum(tq.zaf*tq.laimsl)/sum(tq.laimsl)),2) as tq_zaf,\n" +
			    		 "        Round(decode(sum(bq.farl*bq.laimsl),0,0,round(sum(bq.buhs_daoczhj*bq.laimsl)/sum(bq.laimsl),2)*29.271/round(sum(bq.farl*bq.laimsl)/sum(bq.laimsl),2)),2)  as bq_rucbmdj,\n" +
			    		 "        Round(decode(sum(tq.farl*tq.laimsl),0,0,round(sum(tq.buhs_daoczhj*tq.laimsl)/sum(tq.laimsl),2)*29.271/round(sum(tq.farl*tq.laimsl)/sum(tq.laimsl),2)),2)  as tq_rucbmdj,\n" +
			    		 rucycbb_bt +
			    		 " from\n" +
			    		 "     ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" +
			    		 "                   (select distinct diancxxb_id from  "+yb_dj+" y,yuetjkjb kj\n" +
			    		 "                       where y.yuetjkjb_id=kj.id and (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" +
			    		 "                    ) yid,vwfenxYue fx ,diancxxb dc"+dianckjmx_bm+"\n" +
			    		 "               where dc.id=yid.diancxxb_id "+dianckjmx_tj+strGongsID+" ) fx,\n" +
			    		 "     (select  decode(1,1,'����') as fenx, kj.diancxxb_id," +shul+ "\n"+
			    		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" +
			    		 "           where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and y.fenx = '����' and kj.diancxxb_id=dc.id\n" +
			    		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" +
			    		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" +
			    		 "           group by (kj.diancxxb_id)\n" +
			    		 "       union\n" +
			    		 "       select  decode(1,1,'�ۼ�') as fenx,kj.diancxxb_id," +shul+ "\n"+
			    		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" +
			    		 "           where kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  and y.fenx = '�ۼ�' and kj.diancxxb_id=dc.id\n" +
			    		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" +
			    		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" +
			    		 "           group by (kj.diancxxb_id)\n" +
			    		 "           ) bq,\n" +
			    		 "   (select  decode(1,1,'����') as fenx, kj.diancxxb_id," +shul+ "\n"+
			    		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl, "+yb_zl+" zl,diancxxb dc\n" +
			    		 "           where kj.riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and y.fenx = '����' and kj.diancxxb_id=dc.id\n" +
			    		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" +
			    		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" +
			    		 "           group by (kj.diancxxb_id)\n" +
			    		 "       union\n" +
			    		 "       select  decode(1,1,'�ۼ�') as fenx,kj.diancxxb_id," +shul+ "\n"+
			    		 "           from  "+yb_dj+" y,yuetjkjb kj, "+yb_sl+" sl,"+yb_zl+" zl,diancxxb dc\n" +
			    		 "           where kj.riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and y.fenx = '�ۼ�' and kj.diancxxb_id=dc.id\n" +
			    		 "                 and y.yuetjkjb_id=kj.id and sl.yuetjkjb_id=kj.id and zl.yuetjkjb_id=kj.id "+jihkj+"\n" +
			    		 "                 and y.fenx=sl.fenx(+) and y.fenx=zl.fenx(+) "+zhuangt+shulzt+zhilzt+"\n" +
			    		 "           group by (kj.diancxxb_id)\n" +
			    		 "           ) tq,"+dianc+"\n" + rucycbb_bq+rucycbb_tq+
			    		 " where  dc.id=fx.diancxxb_id and fx.diancxxb_id=tq.diancxxb_id(+)\n" +
			    		 "        and fx.diancxxb_id=bq.diancxxb_id(+)\n" +
			    		 "        and fx.diancxxb_id=dc.id\n" +
			    		 "        and fx.fenx=bq.fenx(+)\n" +
			    		 "        and fx.fenx=tq.fenx(+)\n" + rucycbb_tj+ tiaoj +strGongsID+"  \n"+ fenz+ " )a"+fx+notHuiz;
		    }
// ֱ���ֳ�����
				 ArrHeader=new String[3][18];
				 ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","�����ۺϼ�","�����ۺϼ�","����(ƽ��)��","����(ƽ��)��","�˷�","�˷�","�ӷ�","�ӷ�","�볧��ú����","�볧��ú����",
						                    "��Ȼ��ƽ������","��Ȼ��ƽ������","���״̬","���״̬","���״̬","���״̬"};
				 ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				 ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};

				 ArrWidth=new int[] {150,60,70,70,70,70,70,70,70,70,70,70,70,70,0,0,0,0};
				 //String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","","","",""};
				 String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
				 iFixedRows=1;

//			System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);

			// ����

			Table tb = new Table(rs,3, 0, 1,4);
			rt.setBody(tb);

			rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"���볧ú("+getBaoblxValue2().getValue()+")�۸������", ArrWidth, 4);
			rt.setDefaultTitle(1, 3, "���λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(5, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
			rt.setDefaultTitle(8, 2, "��λ:Ԫ/��", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(11, 3, "cpiȼ�Ϲ���10��", Table.ALIGN_RIGHT);

			rt.body.setWidth(ArrWidth);
			rt.body.setPageRows(22);
			rt.body.setHeaderData(ArrHeader);// ��ͷ����
			rt.body.mergeFixedRow();
			rt.body.mergeFixedCols();
			rt.body.ShowZero = false;
//			��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
			int rows=rt.body.getRows();
			int cols=rt.body.getCols();
			if(visit.getRenyjb()!=3){
			try {
				rs.beforeFirst();
				for(int i=4;i<rows+1;i++){
					rs.next();
					   for(int k=0;k<cols+1;k++){
//					     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//					    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
						   if(!(rs.getString(cols+1).equals("0")&&rs.getString(cols+2).equals("0")&&
								   rs.getString(cols+3).equals("0")&&rs.getString(cols+4).equals("0"))){
							rt.body.getCell(i, k).backColor="red";
						 }
				       }
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			}
			rt.body.setColFormat(arrFormat);

			tb.setColAlign(2, Table.ALIGN_CENTER);

			//ҳ��
			 rt.createDefautlFooter(ArrWidth);
			 rt.setDefautlFooter(1,3,"��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(5,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(10,2,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(rt.footer.getCols()-5,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);


			_CurrentPage=1;
			_AllPages=rt.body.getPages();
			if (_AllPages==0){
				_CurrentPage=0;
			}
			cn.Close();

			return rt.getAllPagesHtml();
	}
	
//	�õ���½��Ա�����糧��ֹ�˾������
	public String getDiancmc(){
		String diancmc="";
		JDBCcon cn = new JDBCcon();
		long diancid=((Visit) getPage().getVisit()).getDiancxxb_id();
		String sql_diancmc="select d.quanc from diancxxb d where d.id="+diancid;
		ResultSet rs=cn.getResultSet(sql_diancmc);
		try {
			while(rs.next()){
				 diancmc=rs.getString("quanc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}
			
		return diancmc;
		
	}
	
//	�õ�ϵͳ��Ϣ�������õı������ĵ�λ����
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
//	ͳ������
	
	public boolean _Baoblxchange = false;
	
	private IDropDownBean _BaoblxValue;
	public IDropDownBean getBaoblxValue() {
		if(_BaoblxValue==null){
			_BaoblxValue=(IDropDownBean)getIBaoblxModels().getOption(0);
		}
		return _BaoblxValue;
	}

	public void setBaoblxValue(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue != null) {
			id = _BaoblxValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange = true;
			} else {
				_Baoblxchange = false;
			}
		}
		_BaoblxValue = Value;
	}

	private IPropertySelectionModel _IBaoblxModel;

	public void setIBaoblxModel(IPropertySelectionModel value) {
		_IBaoblxModel = value;
	}

	public IPropertySelectionModel getIBaoblxModel() {
		if (_IBaoblxModel == null) {
			getIBaoblxModels();
		}
		return _IBaoblxModel;
	}

	public IPropertySelectionModel getIBaoblxModels() {
		Visit visit = (Visit) getPage().getVisit();
		JDBCcon con = new JDBCcon();
		try{
			List baoblxList = new ArrayList();
			if(visit.getRenyjb()==1){
				baoblxList.add(new IDropDownBean(0,"���ֹ�˾ͳ��"));
				baoblxList.add(new IDropDownBean(1,"���糧ͳ��"));
				baoblxList.add(new IDropDownBean(2,"������ͳ��"));
			}else{
				baoblxList.add(new IDropDownBean(0,"���糧ͳ��"));
				baoblxList.add(new IDropDownBean(1,"������ͳ��"));
			}
			_IBaoblxModel = new IDropDownModel(baoblxList);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel;
	}
//	��������
	
	public boolean _Baoblxchange2 = false;
	
	private IDropDownBean _BaoblxValue2;
	public IDropDownBean getBaoblxValue2() {
		if(_BaoblxValue2==null){
			_BaoblxValue2=(IDropDownBean)getIBaoblxModels2().getOption(0);
		}
		return _BaoblxValue2;
	}

	public void setBaoblxValue2(IDropDownBean Value) {
		long id = -2;
		if (_BaoblxValue2 != null) {
			id = _BaoblxValue2.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_Baoblxchange2 = true;
			} else {
				_Baoblxchange2 = false;
			}
		}
		_BaoblxValue2 = Value;
	}

	private IPropertySelectionModel _IBaoblxModel2;

	public void setIBaoblxModel2(IPropertySelectionModel value) {
		_IBaoblxModel2 = value;
	}

	public IPropertySelectionModel getIBaoblxModel2() {
		if (_IBaoblxModel2 == null) {
			getIBaoblxModels2();
		}
		return _IBaoblxModel2;
	}

	public IPropertySelectionModel getIBaoblxModels2() {
		JDBCcon con = new JDBCcon();
		try{
			List baoblx2List = new ArrayList();
			baoblx2List.add(new IDropDownBean(0,"�糧���ھ�ͳ��"));
			baoblx2List.add(new IDropDownBean(1,"�糧����"));
			baoblx2List.add(new IDropDownBean(2,"�ص㶩��"));
			baoblx2List.add(new IDropDownBean(3,"�г��ɹ�"));
			_IBaoblxModel2 = new IDropDownModel(baoblx2List);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IBaoblxModel2;
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
//		nianf.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(nianf);
		tb1.addText(new ToolbarText("-"));
		
		

		tb1.addText(new ToolbarText("�·�:"));
		ComboBox yuef = new ComboBox();
		yuef.setTransform("YUEF");
		yuef.setWidth(60);
//		yuef.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(yuef);
		tb1.addText(new ToolbarText("-"));
		
		
		
	    Visit visit = (Visit) getPage().getVisit();
		DefaultTree dt = new DefaultTree(DefaultTree.tree_dianc_win,"diancTree",""+visit.getDiancxxb_id(),"",null,getTreeid());
		visit.setDefaultTree(dt);
		TextField tf = new TextField();
		tf.setId("diancTree_text");
		tf.setWidth(100);
		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid()==null||"".equals(getTreeid())?"-1":getTreeid())));
		
		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
		tb2.setIcon("ext/resources/images/list-items.gif");
		tb2.setCls("x-btn-icon");
		tb2.setMinWidth(20);
		
		tb1.addText(new ToolbarText("��λ:"));
		tb1.addField(tf);
		tb1.addItem(tb2);
		tb1.addText(new ToolbarText("-"));
//		ExtTreeUtil etu = new ExtTreeUtil("diancTree",ExtTreeUtil.treeWindowType_Dianc,((Visit)this.getPage().getVisit()).getDiancxxb_id(),"-1".equals(getTreeid())?null:getTreeid());
//		setTree(etu);
//		TextField tf = new TextField();
//		tf.setId("diancTree_text");
//		tf.setWidth(100);
//		tf.setValue(((IDropDownModel)getIDiancmcModel()).getBeanValue(Long.parseLong(getTreeid())));
//		
//		ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
//		tb2.setIcon("ext/resources/images/list-items.gif");
//		tb2.setCls("x-btn-icon");
//		tb2.setMinWidth(20);
//		
//		tb1.addText(new ToolbarText("��λ:"));
//		tb1.addField(tf);
//		tb1.addItem(tb2);
//		tb1.addText(new ToolbarText("-"));
		if(getDiancTreeJib()!=3){
			tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxDropDown");
			cb.setWidth(120);
//			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		}
		tb1.addText(new ToolbarText("��������:"));
		ComboBox cb2 = new ComboBox();
		cb2.setTransform("BaoblxDropDown2");
		cb2.setWidth(120);
//		cb2.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb2);
		tb1.addText(new ToolbarText("-"));
		
		/*if(getDiancTreeJib()!=3){
			tb1.addText(new ToolbarText("�±��ھ�:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("YuebDropDown");
			cb3.setWidth(120);
//			cb3.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb3);
			tb1.addText(new ToolbarText("-"));
		}*/
		
			tb1.addText(new ToolbarText("�ھ�����:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("KoujlxDropDown");
			cb3.setWidth(100);
//			cb3.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb3);
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
//	�±��ھ�
	private boolean _yuebchange = false;
	public IDropDownBean getYuebValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean2()==null){
			if (getYuebModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean2((IDropDownBean)getYuebModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean2();
	}

	public void setYuebValue(IDropDownBean Value) {
		if (getYuebValue().getId() != Value.getId()) {
			_yuebchange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean2(Value);
	}

	public void setYuebModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel2(value);
	}

	public IPropertySelectionModel getYuebModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel2() == null) {
			getIYuebModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel2();
	}

	public void getIYuebModels() {
	
		String sql ="select kj.id as id,kj.mingc as mingc from dianckjb kj\n" +
			"\t\twhere kj.fenl_id in (select distinct id from item i where i.bianm='YB' and i.zhuangt=1)\n" + 
			"    and kj.diancxxb_id=" +((Visit) getPage().getVisit()).getDiancxxb_id();
		
		((Visit)getPage().getVisit()).setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
	}
//	0------------------------
//	�±��ھ�����
	private boolean _koujlxchange = false;
	public IDropDownBean getKoujlxValue() {
		if(((Visit)getPage().getVisit()).getDropDownBean3()==null){
			if (getKoujlxModel().getOptionCount()>0){
				((Visit)getPage().getVisit()).setDropDownBean3((IDropDownBean)getKoujlxModel().getOption(0));
			}
		}
		return ((Visit)getPage().getVisit()).getDropDownBean3();
	}

	public void setKoujlxValue(IDropDownBean Value) {
		if (getKoujlxValue().getId()!= Value.getId()) {
			_koujlxchange = true;
		}
		((Visit)getPage().getVisit()).setDropDownBean3(Value);
	}

	public void setKoujlxModel(IPropertySelectionModel value) {
		((Visit)getPage().getVisit()).setProSelectionModel3(value);
	}

	public IPropertySelectionModel getKoujlxModel() {
		if (((Visit)getPage().getVisit()).getProSelectionModel3() == null) {
			getIKoujlxModels();
		}
		return ((Visit)getPage().getVisit()).getProSelectionModel3();
	}

	public void getIKoujlxModels() {
	
		String sql ="select id,mingc from item where bianm='YB'"; 
		
		((Visit)getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));
	}
	/////////////////////////////////////////////////////
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

    public String getTreeScript() {
		return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
	}
//	public ExtTreeUtil getTree() {
//		return ((Visit) this.getPage().getVisit()).getExtTree1();
//	}
//
//	public void setTree(ExtTreeUtil etu) {
//		((Visit) this.getPage().getVisit()).setExtTree1(etu);
//	}
//
//	public String getTreeHtml() {
//		return getTree().getWindowTreeHtml(this);
//	}
//
//	public String getTreeScript() {
//		return getTree().getWindowTreeScript();
//	}

	
}