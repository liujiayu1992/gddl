/*
 * ���ߣ�zuoh
 * ʱ�䣺2010-11-19
 * ������ʵ��ҳ�治�Զ�ˢ��
 */
package com.zhiren.jt.zdt.monthreport.yuefdbmdjqkbreport;

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
* ʱ�䣺2009-05-4
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql��yuezbb�����ֶε�����,����yuezbb���µĹ�ʽ��ȡ�µ��ֶ�����
* 		   
*/ 
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
* ʱ�䣺2010-01-4
* ���ߣ� ll
* �޸����ݣ�1�����ӡ����ֹ�˾ͳ�ơ�������ѯ��
* 			2�����ӡ��±��ھ��������򣬶����ݽ��в�ѯ��
*/
/* 
* ʱ�䣺2010-09-19
* sy
* �޸����ݣ�1�޸�sql����ʹ��having not��������oracle�汾bug�������
*/ 
/* 
* ʱ�䣺2010-11-05
* ���ߣ�liufl
* �޸����ݣ�1������"�ھ�����"�����˵�����ѡ��"�ȱȿھ�"��"�±��糧�ھ�"
*          2��δ��������ú�ɫ������ʾ         
*/
/* 
* ʱ�䣺2010-12-31
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql����λѡ�糧ʱ����ʾ��ͳ�ƿھ���������
*            2����λѡ�ֹ�˾+������ͳ��ʱ��ɫ��ʾ���ԣ��޸�
*/
/* 
* ʱ�䣺2011-01-11
* ���ߣ�liufl
* �޸����ݣ��޸ĵ�������ʱ��������Ҳ����������
*            
*/
/* 
* ʱ�䣺2011-01-21
* ���ߣ�liufl
* �޸����ݣ��޸�sql,���ҳ���ָ��bug
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
public class Yuefdbmdjqkbreport  extends BasePage implements PageValidateListener{
	
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
//			this.getTree();
			visit.setDropDownBean4(null);
			visit.setProSelectionModel4(null);
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			isBegin=true;
				
		}
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
		String guolzj="";
		String yb="";
			if(visit.getRenyjb()==3){
				yb="yuezbb";
				zhuangt="";
			}else if(visit.getRenyjb()==2){
				ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
				try {
					if(rs.next()) {
						String dcids=rs.getString("zhi");
						if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
							yb="vwyuezbb";
						}else {
							yb="yuezbb";
						}
					}else {
						yb="yuezbb";
					}
					zhuangt=" and (y.zhuangt=1 or y.zhuangt=2)";
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
							yb="vwyuezbb";
						}else {
							yb="yuezbb";
						}
					}else {
						yb="yuezbb";
					}
					zhuangt=" and y.zhuangt=2";
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
		String  notHuiz="";
		int jib=this.getDiancTreeJib();
		if(jib==1){//ѡ����ʱˢ�³����еĵ糧
			strGongsID=" ";
			strDiancFID="'',";
			guolzj="";
		}else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
			strGongsID = "  and (dc.fuid= "+this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
			strDiancFID=this.getTreeid()+",";
			guolzj=" and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
		}else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
			strGongsID=" and dc.id= " +this.getTreeid();
			strDiancFID="'',";
			guolzj="  and a.fgs=0 --grouping(dc.mingc)=0\n";
		}else if (jib==-1){
			strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
			strDiancFID="'',";
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
		String grouping="";
        String shul="";
        String dianckjmx_bm=""; 
		String dianckjmx_tj="";
		String strzt="";
		String koujid="";
		/*String strFunctionName ="";
		if(getYuebValue().getValue().equals("ȫ��")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			strFunctionName = "getShenhzt";
		}else{
			dianckjmx_bm=",dianckjmx kjmx";
			dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
			strFunctionName = "getShenhzt_fenkj";
		}
		String strzt=strFunctionName+"(dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','����'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+" )as bqby,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','�ۼ�'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as bqlj,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','����'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as tqby,\n" + 
		             strFunctionName+"(dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','�ۼ�'"+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?","+getYuebValue().getId()+"":"")+") as tqlj\n" ;*/
		
		if(getYuebValue().getValue().equals("ȫ��")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			koujid="'',";
	    }else{
	    	dianckjmx_bm=",dianckjmx kjmx";
	    	dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
	    	koujid=getYuebValue().getId()+",";
	    }
		
		strzt="nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','����'"+","+visit.getRenyjb()+"),0)as bqby,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yuezbb','�ۼ�'"+","+visit.getRenyjb()+"),0) as bqlj,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','����'"+","+visit.getRenyjb()+"),0) as tqby,\n" + 
		      "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yuezbb','�ۼ�'"+","+visit.getRenyjb()+"),0) as tqlj\n" ;
		if(jib==3){
			biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
			"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
			"from ( select"+
			" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
			dianc=" vwdianc dc \n";
			tiaoj="";
			fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
				 "--having not grouping(fx.fenx)=1"+ guolzj+ 
				 "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
				 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
				 " )a   where a.f=0 and a.dcmc=0 ";
			grouping="   ,grouping(dc.fgsmc)  fgs \n";
		}else{
		if(getBaoblxValue().getValue().equals("������ͳ��")){
				/*biaot=
					"a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
					"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
					"from ( select"+
                    " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";
				dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
				tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
				fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" + 
					 "--having not grouping(fx.fenx)=1\n" + 
					 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
				 " )a   where a.f=0 ";*/
			biaot=
				"a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
                " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode("+jib+",2,'',dq.mingc) as dqmc,decode("+jib+",1,'',(select mingc from diancxxb where id="+this.getTreeid()+")) fgsmc,dc.mingc as diancmc";
			dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
			tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
			fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" + 
				 "--having not grouping(fx.fenx)=1\n" + 
				 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" + 
				 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
			 " )a   where a.f=0 ";
			}else if(getBaoblxValue().getValue().equals("���糧ͳ��")){
				biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
				" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
				dianc=" vwdianc dc \n";
				tiaoj="";
				fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
					 "--having not grouping(fx.fenx)=1"+ guolzj+ 
					 "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
					 " )a   where a.f=0 "+guolzj;
				grouping="   ,grouping(dc.fgsmc)  fgs \n";
			}else if(getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")){
				biaot="a.DANWMC,a.FENX,a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
				"a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ,"+strzt+" \n" + 
				"from ( select"+
					" decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
				dianc=" vwdianc dc \n";
				tiaoj="";
				fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
				      "--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)"+guolzj+ 
					 " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
					 " )a   where a.f=0  and   dcmc=1  "+guolzj;
				grouping="   ,grouping(dc.fgsmc)  fgs \n";
			}
		}
			if(getKoujlxValue().getValue().equals("�ȱȿھ�")){
				shul="         sum(y.FADYTRML*dc.konggbl) as fadhml,sum(y.GONGRYTRML*dc.konggbl) as gongrhml,\n"
				+ "            sum(y.FADMZBML*dc.konggbl+y.FADYZBZML*dc.konggbl+y.FADQZBZML*dc.konggbl) as fadzhbml,\n"
				+ "            sum(y.FADMZBML*dc.konggbl) as fadbml,sum(y.FADYZBZML*dc.konggbl) as fadybml,\n"
				+ "            sum(y.GONGRMZBML*dc.konggbl+y.GONGRYZBZML*dc.konggbl+y.GONGRQZBZML*dc.konggbl) as gongrzhbml,\n"
				+ "            sum(y.GONGRMZBML*dc.konggbl) as grhmbml,sum(y.GONGRYZBZML*dc.konggbl) as grhybml,\n"
				+ "            sum(y.QIZ_FADTRMDJ) as fadtrmdj,sum(y.QIZ_GONGRTRMDJ) as gongrtrmdj,\n"
				+ "            decode(sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),0,0, Round(sum((y.FADMCB+y.FADYCB+y.FADRQCB+Y.GONGRCYDFTRLF)*dc.konggbl)*10000/sum(y.FADMZBML*dc.konggbl+y.FADYZBZML*dc.konggbl+y.FADQZBZML*dc.konggbl),2)) as fadzhbmdj,\n"
				+ "            decode(sum(y.FADMZBML),0,0,Round(sum((y.FADMCB+y.QIZ_RANM)*dc.konggbl)*10000/sum(y.FADMZBML*dc.konggbl),2)) as fdmzbmdj,\n"
				+ "            decode(sum(y.FADYZBZML),0,0, Round(sum((y.FADYCB+y.QIZ_RANY)*dc.konggbl)*10000/sum(y.FADYZBZML*dc.konggbl),2)) as fdyzbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),0,0,Round(sum((y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB-y.GONGRCYDFTRLF)*dc.konggbl)*10000/sum(y.GONGRMZBML*dc.konggbl+y.GONGRYZBZML*dc.konggbl+y.GONGRQZBZML*dc.konggbl),2)) as grzhbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML),0,0,Round(sum((y.GONGRMCB-QIZ_RANM)*dc.konggbl)*10000/sum(y.GONGRMZBML*dc.konggbl),2)) as grmzbmdj,\n"
				+ "            decode(sum(y.GONGRYZBZML),0,0,Round(sum((y.GONGRYCB-QIZ_RANY)*dc.konggbl)*10000/sum(y.GONGRYZBZML*dc.konggbl),2)) as gryzbmdj\n";
			}else{
				shul="         sum(y.FADYTRML) as fadhml,sum(y.GONGRYTRML) as gongrhml,\n"
				+ "            sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML) as fadzhbml,\n"
				+ "            sum(y.FADMZBML) as fadbml,sum(y.FADYZBZML) as fadybml,\n"
				+ "            sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML) as gongrzhbml,\n"
				+ "            sum(y.GONGRMZBML) as grhmbml,sum(y.GONGRYZBZML) as grhybml,\n"
				+ "            sum(y.QIZ_FADTRMDJ) as fadtrmdj,sum(y.QIZ_GONGRTRMDJ) as gongrtrmdj,\n"
				+ "            decode(sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),0,0, Round(sum(y.FADMCB+y.FADYCB+y.FADRQCB+Y.GONGRCYDFTRLF)*10000/sum(y.FADMZBML+y.FADYZBZML+y.FADQZBZML),2)) as fadzhbmdj,\n"
				+ "            decode(sum(y.FADMZBML),0,0,Round(sum(y.FADMCB+y.QIZ_RANM)*10000/sum(y.FADMZBML),2)) as fdmzbmdj,\n"
				+ "            decode(sum(y.FADYZBZML),0,0, Round(sum(y.FADYCB+y.QIZ_RANY)*10000/sum(y.FADYZBZML),2)) as fdyzbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),0,0,Round(sum(y.GONGRMCB+y.GONGRYCB+y.GONGRRQCB-y.GONGRCYDFTRLF)*10000/sum(y.GONGRMZBML+y.GONGRYZBZML+y.GONGRQZBZML),2)) as grzhbmdj,\n"
				+ "            decode(sum(y.GONGRMZBML),0,0,Round(sum(y.GONGRMCB-QIZ_RANM)*10000/sum(y.GONGRMZBML),2)) as grmzbmdj,\n"
				+ "            decode(sum(y.GONGRYZBZML),0,0,Round(sum(y.GONGRYCB-QIZ_RANY)*10000/sum(y.GONGRYZBZML),2)) as gryzbmdj\n";
			}
				 strSQL = "select "+biaot+",\n"
				+ "       fx.fenx,\n"
				+ "       decode(sum(bq.fadhml),0,0,Round(sum(bq.fadtrmdj*bq.fadhml)/sum(bq.fadhml),2)) as bq_fdtrmdj,\n"
				+ "       decode(sum(tq.fadhml),0,0,Round(sum(tq.fadtrmdj*tq.fadhml)/sum(tq.fadhml),2)) as tq_fdtrmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.gongrhml),0,0,Round(sum(bq.gongrtrmdj*bq.gongrhml)/sum(bq.gongrhml),2)) as bq_grtrmdj,\n"
				+ "       decode(sum(tq.gongrhml),0,0,Round(sum(tq.gongrtrmdj*tq.gongrhml)/sum(tq.gongrhml),2)) as tq_grtrmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadzhbml),0,0,Round(sum(bq.fadzhbmdj*bq.fadzhbml)/sum(bq.fadzhbml),2))  as bq_fdzhbmdj,\n"
				+ "       decode(sum(tq.fadzhbml),0,0,Round(sum(tq.fadzhbmdj*tq.fadzhbml)/sum(tq.fadzhbml),2))  as tq_fdzhbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadbml),0,0,Round(sum(bq.fdmzbmdj*bq.fadbml)/sum(bq.fadbml),2)) as bq_fadbmdj,\n"
				+ "       decode(sum(tq.fadbml),0,0,Round(sum(tq.fdmzbmdj*tq.fadbml)/sum(tq.fadbml),2)) as tq_fadbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.fadybml),0,0,Round(sum(bq.fdyzbmdj*bq.fadybml)/sum(bq.fadybml),2)) as bq_fadybmdj,\n"
				+ "       decode(sum(tq.fadybml),0,0,Round(sum(tq.fdyzbmdj*tq.fadybml)/sum(tq.fadybml),2)) as tq_fadybmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.gongrzhbml),0,0,Round(sum(bq.grzhbmdj*bq.gongrzhbml)/sum(bq.gongrzhbml),2)) as bq_grzhbmdj,\n"
				+ "       decode(sum(tq.gongrzhbml),0,0,Round(sum(tq.grzhbmdj*tq.gongrzhbml)/sum(tq.gongrzhbml),2)) as tq_grzhbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.grhmbml),0,0,Round(sum(bq.grmzbmdj*bq.grhmbml)/sum(bq.grhmbml),2)) as bq_grmbmdj,\n"
				+ "       decode(sum(tq.grhmbml),0,0,Round(sum(tq.grmzbmdj*tq.grhmbml)/sum(tq.grhmbml),2)) as tq_grmbmdj,\n"
				+ "\n"
				+ "       decode(sum(bq.grhybml),0,0,Round(sum(bq.gryzbmdj*bq.grhybml)/sum(bq.grhybml),2)) as bq_grhybmdj,\n"
				+ "       decode(sum(tq.grhybml),0,0,Round(sum(tq.gryzbmdj*tq.grhybml)/sum(tq.grhybml),2)) as tq_grhybmdj\n"
				+" \n         , grouping(dc.mingc) dcmc,grouping(fx.fenx) f"+grouping
				+ "  \n from\n"
				+ "          (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n"
				+ "     (select distinct diancxxb_id from "+yb+"\n"
				+ "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=last_year_today(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')))\n"
				+ "     ) dcid,(select decode(1,1,'����','') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�','')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n"  
				+ "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+"  ) fx,\n" 
				+ "      ((select decode(1,1,'����','') as fenx,y.diancxxb_id,"+shul+"\n"
				+ "        from "+yb+" y,diancxxb dc\n"
				+ "       where y.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n"
				+ "         and y.fenx = '����' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "       group by (y.diancxxb_id))\n"
				+ "       union\n"
				+ "       (select decode(1,1,'�ۼ�','') as fenx, y.diancxxb_id,"+shul+"\n"
				+ "        from "+yb+" y,diancxxb dc\n"
				+ "       where y.riq = to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd')\n"
				+ "         and y.fenx = '�ۼ�' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "       group by (y.diancxxb_id))) bq,\n"
				+ "\n"
				+ "       ((select decode(1,1,'����','') as fenx,y.diancxxb_id,"+shul+"\n"
				+ "      from "+yb+" y,diancxxb dc\n"
				+ "     where y.riq = last_year_today(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'))\n"
				+ "       and y.fenx = '����' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "     group by (y.diancxxb_id))\n"
				+ "     union\n"
				+ "     (select decode(1,1,'�ۼ�','') as fenx, y.diancxxb_id,"+shul+"\n"
				+ "      from "+yb+" y,diancxxb dc\n"
				+ "     where y.riq = last_year_today(to_date('"+intyear+"-"+intMonth+"-01', 'yyyy-mm-dd'))\n"
				+ "       and y.fenx = '�ۼ�' "+zhuangt+" and y.diancxxb_id=dc.id\n"
				+ "     group by (y.diancxxb_id))) tq,\n" + dianc
				+ "where fx.diancxxb_id=bq.diancxxb_id(+)\n"
				+ "and   fx.diancxxb_id=tq.diancxxb_id(+)\n"
				+ "and   fx.diancxxb_id=dc.id  "+tiaoj+" \n"
				+ "and   fx.fenx=bq.fenx(+)\n"
				+ "and   fx.fenx=tq.fenx(+)\n"
				+strGongsID+"\n" 
				 +  fenz;
//				+ "group by rollup (fx.fenx,f.mingc,dc.mingc)\n"
//				+ "having not grouping (fx.fenx)=1  "+notHuiz+"\n"
//				+ "order by  grouping(f.mingc) desc,max(f.xuh),f.mingc,grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(fx.fenx) desc ,fx.fenx";


// ֱ���ֳ�����
				 ArrHeader=new String[3][22];
				 ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","������Ȼú����","������Ȼú����","������Ȼú����","������Ȼú����","�����ۺϱ�ú����","�����ۺϱ�ú����","����:ú�۱�ú����","����:ú�۱�ú����","����:���۱�ú����","����:���۱�ú����","�����ۺϱ�ú����","�����ۺϱ�ú����",
						                    "����:ú�۱�ú����","����:ú�۱�ú����","����:���۱�ú����","����:���۱�ú����","���״̬","���״̬","���״̬","���״̬"};
				 ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				 ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};
				 
				 ArrWidth=new int[] {150,40,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,0,0,0,0};
				 iFixedRows=1;
				 //String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0","0","0","0"};
				 String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00"};
					
			//} 
			
			//System.out.println(strSQL);
			ResultSet rs = cn.getResultSet(strSQL);
			 
			// ����
			
			Table tb = new Table(rs,3, 0, 1,4);
			rt.setBody(tb);
			
			rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"�·����ú���������", ArrWidth, 4);
			rt.setDefaultTitle(1, 4, "���λ(����):"+this.getDiancmc(), Table.ALIGN_LEFT);
			rt.setDefaultTitle(6, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
			rt.setDefaultTitle(10, 4, "��λ:Ԫ/��", Table.ALIGN_RIGHT);
			rt.setDefaultTitle(15, 3, "cpiȼ�Ϲ���11��", Table.ALIGN_RIGHT);
			
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
			//rt.body.setColVAlign(1, Table.ALIGN_CENTER);
			//tb.setColAlign(1, Table.ALIGN_LEFT);
			tb.setColAlign(2, Table.ALIGN_CENTER);
			if(rt.body.getRows()>3){
				rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
			}
			
			//ҳ�� 
			 rt.createDefautlFooter(ArrWidth);
			  rt.setDefautlFooter(1,3,"��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,2,"���:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(12,3,"�Ʊ�:",Table.ALIGN_LEFT);
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
//	��������
	
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
		
			tb1.addText(new ToolbarText("�±��ھ�:"));
			ComboBox cb2 = new ComboBox();
			cb2.setTransform("YuebDropDown");
			cb2.setWidth(120);
//			cb2.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb2);
			tb1.addText(new ToolbarText("-"));
		}
		
			tb1.addText(new ToolbarText("�ھ�����:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("KoujlxDropDown");
			cb3.setWidth(120);
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
		if (getKoujlxValue().getId() != Value.getId()) {
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