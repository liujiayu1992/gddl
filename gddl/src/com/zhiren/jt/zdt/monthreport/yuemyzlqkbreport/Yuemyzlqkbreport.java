package com.zhiren.jt.zdt.monthreport.yuemyzlqkbreport;

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

import org.apache.tapestry.contrib.palette.SortMode;
/* 
* ʱ�䣺2009-04-16
* ���ߣ� ll
* �޸����ݣ��޸Ĳ�ѯsql���жϵ糧idΪ141(�ߺ��糧)���볧��λ��ֵȡyuezlb�е�diancrz�ֶΣ������糧�볧��λ��ֵȡyuezlb�е�qnet_ar�ֶ�.
* 		   
*/ 
/* 
* ʱ�䣺2009-05-4
* ���ߣ� ll
* �޸����ݣ�1���޸Ĳ�ѯsql���жϵ糧�볧��λ��ֵΪ0ʱ���볧����¯��ֵ��Ϊ0.
* 			2���޸Ĳ�ѯsql��yuezbb�����ֶε�����,����yuezbb���µĹ�ʽ��ȡ�µ��ֶ�����
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
* ʱ�䣺2009-12-03
* ���ߣ� ll
* �޸����ݣ�1���±�-cpi09���볧��ֵ��������  �������ص㡱���������г���
			�����ص�=�ص�+����
			�����г�=�г�
* 		   
*/ 
/* 
* ʱ�䣺2010-01-4
* ���ߣ� ll
* �޸����ݣ�1�����ӡ����ֹ�˾ͳ�ơ�������ѯ��
* 			2�����ӡ��±��ھ��������򣬶����ݽ��в�ѯ��
*/
/* 
* ʱ�䣺2010-03-31
* ���ߣ� ll
* �޸����ݣ�1���޸�ҳ����ʾsql��ԭsql�и����ȫ���Ӹ�Ϊ�����ӡ�
*/
/* 
* ʱ�䣺2010-09-19
* sy
* �޸����ݣ�1�޸�sql����ʹ��having not��������oracle�汾bug�������
*/ 
/* 
* ʱ�䣺2010-11-04
* ���ߣ�liufl
* �޸����ݣ�1������"�ھ�����"�����˵�����ѡ��"�ȱȿھ�"��"�±��糧�ھ�"
*          2��δ��������ú�ɫ������ʾ
*/ 
/* 
* ʱ�䣺2010-12-20
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql���糧��ѡ�糧+���糧ͳ��ʱ���˹�˾�ܼ�
*/ 
/* 
* ʱ�䣺2010-12-31
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql����λѡ�糧ʱ����ʾ��ͳ�ƿھ���������
*            2����λѡ�ֹ�˾+������ͳ��ʱ��ɫ��ʾ���ԣ��޸�sql
*/
/* 
* ʱ�䣺2011-01-10
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
* ʱ�䣺2012-01-12
* ���ߣ�liufl
* �޸����ݣ����ϵͳ���������Ƿ��ѯ��ͼ
*            
*/
public class Yuemyzlqkbreport  extends BasePage implements PageValidateListener{
	
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
			visit.setProSelectionModel2(null);
			visit.setDropDownBean2(null);
			this.setBaoblxValue(null);
			this.getIBaoblxModels();
			isBegin=true;
			this.getSelectData();
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
				this.getTree();
				visit.setDropDownBean4(null);
				visit.setProSelectionModel4(null);
				visit.setProSelectionModel2(null);
				visit.setDropDownBean2(null);
				this.setBaoblxValue(null);
				this.getIBaoblxModels();
				isBegin=true;
				this.getSelectData();	
			}
		}
		if(nianfchanged){
			nianfchanged=false;
			Refurbish();
		}
		if(yuefchanged){
			yuefchanged=false;
			Refurbish();
		}

		if(_fengschange){
			
			_fengschange=false;
			Refurbish();
		}
		getToolBars() ;
		Refurbish();
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
	//--------------------------
	private String RT_HET="yunsjhcx";
	private String mstrReportName="yunsjhcx";
	
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
	    long intyear;
	    long intMonth;
	    Visit visit = (Visit)getPage().getVisit();
	    String strSQL = "";
	    this._CurrentPage = 1;
	    this._AllPages = 1;

	    JDBCcon cn = new JDBCcon();
	    String zhuangt = "";
	    String shulzt = "";
	    String zhilzt = "";
	    String guolzj = "";
	    String guolzj2 = "";
	    String yb_sl = "";
	    String yb_zl = "";
	    String yb_zb = "";
	    String yb_rcy = "";
	    if (visit.getRenyjb() == 3) {
	      yb_sl = "yueslb";
	      yb_zl = "yuezlb";
	      yb_zb = "yuezbb";
	      yb_rcy = "rucycbb";
	      zhuangt = "";
	      shulzt = "";
	      zhilzt = ""; } else {
	      ResultSet rs;
	      String dcids;
	      if (visit.getRenyjb() == 2) {
	        rs = cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
	        try {
	          if (rs.next()) {
	            dcids = rs.getString("zhi");
	            if (dcids.indexOf(visit.getDiancxxb_id()+"") >= 0) {
	              yb_sl = "vwyueslb";
	              yb_zl = "vwyuezlb";
	              yb_zb = "vwyuezbb";
	              yb_rcy = "vwrucycbb";
	            } else {
	              yb_sl = "yueslb";
	              yb_zl = "yuezlb";
	              yb_zb = "yuezbb";
	              yb_rcy = "rucycbb";
	            }
	          } else {
	            yb_sl = "yueslb";
	            yb_zl = "yuezlb";
	            yb_zb = "yuezbb";
	            yb_rcy = "rucycbb";
	          }
	          zhuangt = " and (zb.zhuangt=1 or zb.zhuangt=2) ";
	          shulzt = " and (sl.zhuangt=1 or sl.zhuangt=2) ";
	          zhilzt = " and (zl.zhuangt=1 or zl.zhuangt=2) ";
	          cn.Close();
	          rs.close();
	        }
	        catch (SQLException e) {
	          e.printStackTrace();
	        }
	      }
	      else if (visit.getRenyjb() == 1) {
	        rs = cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
	        try {
	          if (rs.next()) {
	        	  String e = rs.getString("zhi");
	            if (e.indexOf(visit.getDiancxxb_id()+"") >= 0) {
	              yb_sl = "vwyueslb";
	              yb_zl = "vwyuezlb";
	              yb_zb = "vwyuezbb";
	              yb_rcy = "vwrucycbb";
	            } else {
	              yb_sl = "yueslb";
	              yb_zl = "yuezlb";
	              yb_zb = "yuezbb";
	              yb_rcy = "rucycbb";
	            }
	          } else {
	            yb_sl = "yueslb";
	            yb_zl = "yuezlb";
	            yb_zb = "yuezbb";
	            yb_rcy = "rucycbb";
	          }
	          zhuangt = " and zb.zhuangt=2 ";
	          shulzt = " and sl.zhuangt=2 ";
	          zhilzt = " and zl.zhuangt=2 ";
	          cn.Close();
	          rs.close();
	        }
	        catch (SQLException e) {
	          e.printStackTrace();
	        }
	      }
	    }

	    if (getNianfValue() == null)
	      intyear = DateUtil.getYear(new Date());
	    else {
	      intyear = getNianfValue().getId();
	    }

	    if (getYuefValue() == null)
	      intMonth = DateUtil.getMonth(new Date());
	    else {
	      intMonth = getYuefValue().getId();
	    }

	    String strGongsID = "";
	    String strDiancFID = "";
	    String danwmc = "";
	    int jib = getDiancTreeJib();
	    if (jib == 1) {
	      strGongsID = " ";
	      guolzj = "";
	      strDiancFID = "'',";
	    } else if (jib == 2) {
	      strGongsID = "  and (dc.fuid=  " + getTreeid() + " or dc.shangjgsid=" + getTreeid() + ")";
	      guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";
	      guolzj2 = "";
	      strDiancFID = getTreeid() + ",";
	    } else if (jib == 3) {
	      strGongsID = " and dc.id= " + getTreeid();
	      guolzj = "   and a.fgs=0 --grouping(dc.mingc)=0\n";
	      guolzj2 = " and a.dcmc=0";
	      strDiancFID = "'',";
	    } else if (jib == -1) {
	      strGongsID = " and dc.id = " + ((Visit)getPage().getVisit()).getDiancxxb_id();
	      strDiancFID = "'',";
	    }

	    danwmc = getTreeDiancmc(getTreeid());

	    Report rt = new Report();
	    int[] ArrWidth = null;
	    String[][] ArrHeader = null;
	    String titlename = "ú(��)���������";
	    int iFixedRows = 0;
	    int iCol = 0;

	    String biaot = "";
	    String dianc = "";
	    String tiaoj = "";
	    String fenz = "";
	    String grouping = "";
	    String dianckjmx_bm = "";
	    String dianckjmx_tj = "";
	    String strzt = "";
	    String koujid = "";

	    if (getYuebValue().getValue().equals("ȫ��")) {
	      dianckjmx_bm = "";
	      dianckjmx_tj = "";
	      koujid = "'',";
	    } else {
	      dianckjmx_bm = ",dianckjmx kjmx";
	      dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
	      koujid = getYuebValue().getId() + ",";
	    }

	    strzt = "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),'rucycbb','����'" + "," + visit.getRenyjb() + "),0)as bqby,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),'rucycbb','�ۼ�'" + "," + visit.getRenyjb() + "),0) as bqlj,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),'rucycbb','����'" + "," + visit.getRenyjb() + "),0) as tqby,\n" + 
	      "nvl(getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),'rucycbb','�ۼ�'" + "," + visit.getRenyjb() + "),0) as tqlj\n";
	    if (jib == 3)
	    {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0 and a.dcmc=0";
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    }
	    else if (getBaoblxValue().getValue().equals("������ͳ��"))
	    {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select \n" + 
	        " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + getTreeid() + ")) fgsmc,dc.mingc as diancmc1";
	      dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
	      tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
	      fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n--having not grouping(fx.fenx)=1\n \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\ngrouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) )a   where a.f=0 ";
	    }
	    else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0 " + guolzj + guolzj2;
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
	      biaot = "a.DANWMC,a.FENX,a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\na.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + 
	        strzt + "  from (select" + 
	        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc1";
	      dianc = " vwdianc dc \n";
	      tiaoj = "";
	      fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + 
	        guolzj + 
	        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" + 
	        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" + 
	        " )a   where a.f=0  and   dcmc=1  " + guolzj;
	      grouping = "   ,grouping(dc.fgsmc)  fgs \n";
	    }

	    String shul = "";
	    String zhib = "";
	    String zhib2 = "";
	    if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
	      shul = "decode(sum(sl.laimsl),0,0,sum((sl.laimsl*dc.konggbl) *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl*dc.konggbl)) as rcfal,\nsum(sl.laimsl*dc.konggbl) as laimsl";

	      zhib = "sum(zb.FADGRYTRML*dc.konggbl) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz";
	      zhib2 = "sum(rcy.shul*dc.konggbl) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul*dc.konggbl) * rcy.youfrl) /sum(rcy.shul*dc.konggbl)) as rucyrl";
	    } else {
	      shul = "decode(sum(sl.laimsl),0,0,sum(sl.laimsl *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl)) as rcfal,\nsum(sl.laimsl) as laimsl";

	      zhib = "sum(zb.FADGRYTRML) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz";
	      zhib2 = "sum(rcy.shul) as rucysl,decode(sum(rcy.shul),0,0,sum(rcy.shul * rcy.youfrl) /sum(rcy.shul)) as rucyrl";
	    }

	    strSQL = 
	      "select  " + biaot + ",\n" + 
	      "  decode(1,1,fx.fenx,'') as fenx,\n" + 
	      "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
	      "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
	      "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
	      "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
	      "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
	      "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
	      "\n" + 
	      "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
	      "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
	      "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
	      "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
	      "                    decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
	      "                    decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq\n" + 
	      "                   , grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n" + grouping + 
	      "      from\n" + 
	      "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
	      "                   (select distinct diancxxb_id from  " + yb_sl + " sl, " + yb_zl + " zl,yuetjkjb tj\n" + 
	      "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	      "                    union\n" + 
	      "                     select distinct diancxxb_id from  " + yb_zb + "\n" + 
	      "                        where  (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	      "                    ) yid,\n" + 
	      "               (select decode(1,1,'����') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�') as fenx,2 as xhu from dual ) fx,diancxxb dc" + dianckjmx_bm + "\n" + 
	      "               where dc.id=yid.diancxxb_id   " + dianckjmx_tj + strGongsID + "   ) fx,\n" + 
	      "\n" + 
	      "             (select  decode(1,1,'����') as fenx,dc.id as diancxxb_id,\n" + 
	      "                    r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='����'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='����'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "  and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "            union\n" + 
	      "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
	      "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='�ۼ�'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "     )bq,\n" + 
	      "          (\n" + 
	      "         select  decode(1,1,'����') as fenx,dc.id as diancxxb_id,\n" + 
	      "                  r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='����'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='����'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "            union\n" + 
	      "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
	      "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	      "              from (\n" + 
	      "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	      "                                                 group by (tj.diancxxb_id) )a,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	      "                                                 group by (tj.diancxxb_id) )b,\n" + 
	      "\n" + 
	      "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	      "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
	      "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	      "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	      "                                                 group by (tj.diancxxb_id) )c\n" + 
	      "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	      "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	      "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	      "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	      "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='�ۼ�'\n" + 
	      "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	      "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	      "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	      "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      "              )tq, \n" + dianc + 
	      "          where dc.id=fx.diancxxb_id " + tiaoj + "\n" + 
	      "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
	      "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
	      "                 and fx.fenx=tq.fenx(+)\n" + 
	      "                 and fx.fenx=bq.fenx(+)\n" + fenz;

	    ArrHeader = new String[3][18];
	    ArrHeader[0] = new String[]{ "��λ����", "��λ����", "�볧úƽ����λ������", "�볧úƽ����λ������", "���У��ص㶩��", "���У��ص㶩��", "���У��г��ɹ�", "���У��г��ɹ�", "��¯úƽ����λ������", "��¯úƽ����λ������", 
	      "�볧����¯��ֵ��", "�볧����¯��ֵ��", "��Ȼ��ƽ��������", "��Ȼ��ƽ��������", "���״̬", "���״̬", "���״̬", "���״̬" };
	    ArrHeader[1] = new String[]{ "��λ����", "��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��" };
	    ArrHeader[2] = new String[]{ "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18" };

	    ArrWidth = new int[] { 150, 60, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70 };

	    iFixedRows = 1;
	    iCol = 10;
	//---------------qin
	    String SQL = "";
	    long intMonth2 = intMonth + 1;
	    long intyear2 = intyear;
	    if (intMonth2 > 12) {
	      intyear2 = intyear + 1;
	      intMonth2 =intMonth2 - 12;
	    }

	    if (intyear >= 2016)
	    {
	      String strSQL2 = 
	        "select  " + biaot + ",\n" + 
	        "  decode(1,1,fx.fenx,'') as fenx,\n" + 
	        "                   decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000) as ruc_frl_bq ,\n" + 
	        "                   decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000) as ruc_frl_tq,\n" + 
	        "                   decode(sum(bq.laimsl_zd),0,0,round(sum(bq.rc_farl_zd*bq.laimsl_zd)/sum(bq.laimsl_zd),2)*1000) as ruc_frl_zd_bq ,\n" + 
	        "                   decode(sum(tq.laimsl_zd),0,0,round(sum(tq.rc_farl_zd*tq.laimsl_zd)/sum(tq.laimsl_zd),2)*1000) as ruc_frl_zd_tq,\n" + 
	        "                   decode(sum(bq.laimsl_sc),0,0,round(sum(bq.rc_farl_sc*bq.laimsl_sc)/sum(bq.laimsl_sc),2)*1000) as ruc_frl_sc_bq ,\n" + 
	        "                   decode(sum(tq.laimsl_sc),0,0,round(sum(tq.rc_farl_sc*tq.laimsl_sc)/sum(tq.laimsl_sc),2)*1000) as ruc_frl_sc_tq,\n" + 
	        "\n" + 
	        "                   decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)*1000) as rul_frl_bq,\n" + 
	        "                   decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)*1000) as rul_frl_tq,\n" + 
	        "                   decode(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2)*1000),0,0,(decode(sum(bq.laimsl),0,0,round(sum(bq.rc_farl*bq.laimsl)/sum(bq.laimsl),2))-decode(sum(bq.rulml),0,0,round(sum(bq.rul_farl*bq.rulml)/sum(bq.rulml),2)))*1000) as rezc_bq,\n" + 
	        "                   decode(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2)*1000),0,0,(decode(sum(tq.laimsl),0,0,round(sum(tq.rc_farl*tq.laimsl)/sum(tq.laimsl),2))-decode(sum(tq.rulml),0,0,round(sum(tq.rul_farl*tq.rulml)/sum(tq.rulml),2)))*1000) as rezc_tq,\n" + 
	        "                    decode(sum(bq.rulyl),0,0,round(sum(bq.sy_farl*bq.rulyl)/sum(bq.rulyl),2)*1000) as sy_farl_bq,\n" + 
	        "                    decode(sum(tq.rulyl),0,0,round(sum(tq.sy_farl*tq.rulyl)/sum(tq.rulyl),2)*1000) as sy_farl_tq\n" + 
	        "                   , grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n" + grouping + 
	        "      from\n" + 
	        "       ( select yid.diancxxb_id,fx.fenx,fx.xuh from\n" + 
	        "                   (select distinct diancxxb_id from  " + yb_sl + " sl, " + yb_zl + " zl,yuetjkjb tj\n" + 
	        "                       where sl.yuetjkjb_id=tj.id and sl.yuetjkjb_id=zl.yuetjkjb_id(+) and (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	        "                    union\n" + 
	        "                     select distinct diancxxb_id from  " + yb_zb + "\n" + 
	        "                        where  (riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') or riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12))\n" + 
	        "                    ) yid,\n" + 
	        "               (select decode(1,1,'����') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�') as fenx,2 as xhu from dual ) fx,diancxxb dc" + dianckjmx_bm + "\n" + 
	        "               where dc.id=yid.diancxxb_id   " + dianckjmx_tj + strGongsID + "   ) fx,\n" + 
	        "\n" + 
	        "             (select  decode(1,1,'����') as fenx,dc.id as diancxxb_id,\n" + 
	        "                    r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='����'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='����'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "  and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "            union\n" + 
	       //----------------qinlizhong    ���㷨  jisff =1 
	        "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
	        "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.fenx='�ۼ�'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        //-----------------qin
	        "                                ,suanfpzb sf   --------qin \n" +	 //-----------------qin    
	        "               where R.DIANCXXB_ID(+)=dc.id \n"+
	        //-----------------qin
	        "              and sf.diancxxb_id(+) = dc.id     ------------qin\n" +       //-----------------qin
	        "               and sf.yuebrq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')   \n" + //-----------------qin
	        "                 and sf.jisff = 1     \n" +            //-----------------qin
	        "and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	       //-----------------qin
	        "    UNION    \n" + 
	      //----------------qinlizhong    ���㷨  jisff = 2
	        "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
	        "                                          r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id,\n" + 
	        "                   sum(zb.FADGRYTRML * dc.konggbl) as rulml,\n" + 
	        "                   sum(c.rucysl) as rulyl,\n" + 
	        "                   sum(c.rucyrl) as rulyrz,\n" + 
	        //--------------------qin
	        "                   sum(d.rulmrz ) as rulmrz\n" +  //--------------------qin
	        "              from yuezbb zb,\n" + 
	        "                   diancxxb dc,\n" + 
	        "                   (select rcy.diancxxb_id,\n" + zhib2 + "\n" + 
	        "                      from rucycbb rcy, diancxxb dc\n" + 
	        "                     where rcy.riq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')\n" + 
	        "                       and rcy.fenx = '�ۼ�'\n" + 
	        "                       and rcy.diancxxb_id = dc.id\n" + strGongsID + 
	        "                     group by (rcy.diancxxb_id)) c\n" + 
	      //--------------------qin
	        "   ,( select diancxxb_id,\n" + 
	        "      sum(meil),\n" + 
	        "      round_new(sum(meil * qnet_ar) / sum(meil), 2)  as rulmrz\n" + 
	        " from (select diancxxb_id,\n" + 
	        "              rulrq,\n" + 
	        "              fenxrq,\n" + 
	        "              sum(meil) meil,\n" + 
	        "              round_new(sum(meil * round_new(qnet_ar, 2)) / sum(meil), 2) qnet_ar\n" + 
	        "         from rulmzlb, diancxxb dc\n" + 
	        "        where diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "          and rulrq >= date '" + intyear + "-01-01'\n" + 
	        "          and rulrq < date '" + intyear2 + "-" + intMonth2 + "-01'\n" + 
	        "          and shenhzt = 3\n" + 
	        "        group by diancxxb_id, rulrq, fenxrq)\n" + 
	        "group by diancxxb_id  ) d\n" + 
	      //--------------------qin	        
	        "             where zb.riq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')\n" + 
	        "               and zb.fenx = '�ۼ�'\n" + 
	        "               and zb.diancxxb_id = dc.id\n" + strGongsID + zhuangt + 
	        "               and zb.diancxxb_id = c.diancxxb_id(+)\n" + 
	        //--------------------qin
	        "               and zb.diancxxb_id = d.diancxxb_id(+)       \n" +    //--------------------qin
	        "             group by (zb.diancxxb_id))  y,vwdianc dc \n" + 
	        "             ,suanfpzb sf    -------------qin\n" +           //--------------------qin   suanfpzb
	        "               where R.DIANCXXB_ID(+)=dc.id  \n"+
	      //--------------------qin
	      "              and sf.diancxxb_id(+) = dc.id     ------------qin\n" + 
	      "                and sf.yuebrq = to_date('" + intyear + "-" + intMonth + "-01', 'yyyy-mm-dd')   ------------qin\n" + 
	      "                  and sf.jisff = 2     ------------qin\n" +      //--------------------qin
	        "        and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	                
	       //-----------------------qin 
	        "     )bq,\n" + 
	        "          (\n" + 
	        "         select  decode(1,1,'����') as fenx,dc.id as diancxxb_id,\n" + 
	        "                  r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                   select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '����' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='����'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id   " + strGongsID + "  group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='����'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        "               where R.DIANCXXB_ID(+)=dc.id and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "            union\n" + 
	  //---------ͬ���ۼ�-----���㷨    	        
	        "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
	        "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
	        "              from (\n" + 
	        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
	        "                                                 group by (tj.diancxxb_id) )a,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
	        "                                                 group by (tj.diancxxb_id) )b,\n" + 
	        "\n" + 
	        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
	        "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
	        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
	        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
	        "                                                 group by (tj.diancxxb_id) )c\n" + 
	        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
	        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
	        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
	        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
	        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='�ۼ�'\n" + 
	        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
	        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
	        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
	        //---------------qin
	        "             ,suanfpzb sf    -------------qin8  \n" +
	        "               where R.DIANCXXB_ID(+)=dc.id \n"+
	      //---------------qin
	      "      and sf.diancxxb_id(+) = dc.id     ------------qin2\n" + //---------------qin
	        "    and sf.yuebrq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)  ------------qin3\n" +  //---------------qin
	        "    and sf.jisff = 1    ------------qin4\n" +       //---------------qin
	        "    and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	      //---------------qin
	        "    union\n" +
	      //---------------qin  ----ͬ���ۼ�-----���㷨  	        
        "              select  decode(1,1,'�ۼ�') as fenx,dc.id as diancxxb_id,\n" + 
        "                        r.laimsl,r.rcfal as rc_farl,r.rcfal_zd as rc_farl_zd,r.laimsl_zd,r.rcfal_sc as rc_farl_sc,r.laimsl_sc,y.rulml,y.rulyl,y.rulmrz as rul_farl,y.rulyrz as sy_farl\n" + 
        "              from (\n" + 
        "                     select a.diancxxb_id,a.rcfal,a.laimsl,b.rcfal as rcfal_zd,b.laimsl as laimsl_zd,c.rcfal as rcfal_sc,c.laimsl as laimsl_sc from\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + "\n" + 
        "                                                 group by (tj.diancxxb_id) )a,\n" + 
        "\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj,  " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and (tj.jihkjb_id=1 or tj.jihkjb_id=3)\n" + 
        "                                                 group by (tj.diancxxb_id) )b,\n" + 
        "\n" + 
        "                        (select tj.diancxxb_id as diancxxb_id," + shul + "\n" + 
        "                                                 from  " + yb_sl + " sl, yuetjkjb tj, " + yb_zl + " zl,diancxxb dc\n" + 
        "                                                 where zl.yuetjkjb_id(+)=sl.yuetjkjb_id and sl.yuetjkjb_id = tj.id and tj.riq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and tj.diancxxb_id = dc.id " + strGongsID + "\n" + 
        "                                                       and sl.fenx = '�ۼ�' and zl.fenx(+)=sl.fenx  " + shulzt + zhilzt + " and  tj.jihkjb_id=2\n" + 
        "                                                 group by (tj.diancxxb_id) )c\n" + 
        "                    where a.diancxxb_id=b.diancxxb_id(+) and a.diancxxb_id=c.diancxxb_id(+) ) r\n" + 
        "              ,(select zb.diancxxb_id," + zhib + "\n" + 
        "                                from  " + yb_zb + " zb,diancxxb dc,\n" + 
        "\t\t\t\t\t\t\t\t( select rcy.diancxxb_id," + zhib2 + "\n" + 
        "                                     from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.fenx='�ۼ�'\n" + 
        "                                     and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" + 
      //---------------qin  d
        
"-------qin6\n" +
"                                     ,(select diancxxb_id,\n" + 
"                       sum(meil),\n" + 
"                       round_new(sum(meil * qnet_ar) / sum(meil), 2) as rulmrz\n" + 
"                  from (select diancxxb_id,\n" + 
"                               rulrq,\n" + 
"                               fenxrq,\n" + 
"                               sum(meil) meil,\n" + 
"                               round_new(sum(meil * round_new(qnet_ar, 2)) /\n" + 
"                                         sum(meil),\n" + 
"                                         2) qnet_ar\n" + 
"                          from rulmzlb, diancxxb dc\n" + 
"                         where diancxxb_id = dc.id " + strGongsID + "\n" +
"                           and rulrq >= add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)\n" + 
"                           and rulrq < add_months(to_date( '" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12)\n" + 
"                           and shenhzt = 3\n" + 
"                         group by diancxxb_id, rulrq, fenxrq)\n" + 
"                 group by diancxxb_id) d\n" + 
//---------------qin  d
        "                                where zb.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id   " + strGongsID + zhuangt + "   and zb.diancxxb_id=c.diancxxb_id(+)\n" + 
        "                                group by (zb.diancxxb_id) )  y,vwdianc dc\n" + 
        //---------------qin
        "             ,suanfpzb sf    -------------qin8  \n" +
        "               where R.DIANCXXB_ID(+)=dc.id \n"+
      //---------------qin
      "      and sf.diancxxb_id(+) = dc.id     ------------qin2\n" + //---------------qin
        "    and sf.yuebrq = add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)  ------------qin3\n" +  //---------------qin
        "    and sf.jisff = 2    ------------qin4\n" +       //---------------qin
        
        "    and  Y.DIANCXXB_ID(+) =dc.id " + strGongsID + " \n" + 
	        "              )tq, \n" + dianc + 	        
	        "          where dc.id=fx.diancxxb_id " + tiaoj + "\n" +       
	        "                 and  fx.diancxxb_id=tq.diancxxb_id(+)\n" + 
	        "                 and  fx.diancxxb_id=bq.diancxxb_id(+)\n" + 
	        "                 and fx.fenx=tq.fenx(+)\n" + 
	        "                 and fx.fenx=bq.fenx(+)\n" + fenz ;
	      SQL = strSQL2;
	    }
	    else
	    {
	      SQL = strSQL;
	    }
//------------qinlizhong
	    //System.out.println("-------------------------------****************--------------------------------");
	    System.out.println("SQL="+SQL);
	    ResultSet rs = cn.getResultSet(SQL);

	    Table tb = new Table(rs, 3, 0, 1, 4);
	    rt.setBody(tb);

	    rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��" + titlename, ArrWidth, 4);
	    rt.setDefaultTitle(1, 2, "���λ:" + getDiancmc(), -1);
	    rt.setDefaultTitle(6, 2, "�����:" + intyear + "��" + intMonth + "��", 2);
	    rt.setDefaultTitle(9, 2, "��λ:ǧ��/ǧ��", 2);
	    rt.setDefaultTitle(12, 2, "cpiȼ�Ϲ���09��", 2);

	    rt.body.setWidth(ArrWidth);
	    rt.body.setPageRows(22);
	    rt.body.setHeaderData(ArrHeader);
	    rt.body.mergeFixedRow();
	    rt.body.mergeFixedCols();
	    rt.body.ShowZero = false;

	    int rows = rt.body.getRows();
	    int cols = rt.body.getCols();
	    if (visit.getRenyjb() != 3)
	      try {
	        rs.beforeFirst();
	        for (int i = 4; i < rows + 1; ++i) {
	          rs.next();
	          for (int k = 0; k < cols + 1; ++k)
	          {
	            if ((!(rs.getString(cols + 1).equals("0"))) || (!(rs.getString(cols + 2).equals("0"))) || 
	              (!(rs.getString(cols + 3).equals("0"))) || (!(rs.getString(cols + 4).equals("0"))))
	              rt.body.getCell(i, k).backColor = "red";
	          }
	        }
	      }
	      catch (SQLException e) {
	        e.printStackTrace();
	      }

	    if (rt.body.getRows() > 3) {
	      rt.body.setCellAlign(4, 1, 1);
	    }

	    rt.createDefautlFooter(ArrWidth);

	    rt.setDefautlFooter(1, 2, "��ӡ����:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), -1);
	    rt.setDefautlFooter(5, 3, "���:", -1);
	    rt.setDefautlFooter(10, 2, "�Ʊ�:", -1);
	    tb.setColAlign(2, 1);
	    rt.setDefautlFooter(rt.footer.getCols() - 5, 2, "(��Page/Pagesҳ)", 2);

	    this._CurrentPage = 1;
	    this._AllPages = rt.body.getPages();
	    if (this._AllPages == 0)
	      this._CurrentPage = 0;

	    cn.Close();
	    return rt.getAllPagesHtml();
	  }
	//�õ���½��Ա�����糧��ֹ�˾������
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
	
	
//	�������
	public boolean _meikdqmcchange = false;
	private IDropDownBean _MeikdqmcValue;

	public IDropDownBean getMeikdqmcValue() {
		if(_MeikdqmcValue==null){
			_MeikdqmcValue=(IDropDownBean)getIMeikdqmcModels().getOption(0);
		}
		return _MeikdqmcValue;
	}

	public void setMeikdqmcValue(IDropDownBean Value) {
		long id = -2;
		if (_MeikdqmcValue != null) {
			id = _MeikdqmcValue.getId();
		}
		if (Value != null) {
			if (Value.getId() != id) {
				_meikdqmcchange = true;
			} else {
				_meikdqmcchange = false;
			}
		}
		_MeikdqmcValue = Value;
	}

	private IPropertySelectionModel _IMeikdqmcModel;

	public void setIMeikdqmcModel(IPropertySelectionModel value) {
		_IDiancmcModel = value;
	}

	public IPropertySelectionModel getIMeikdqmcModel() {
		if (_IMeikdqmcModel == null) {
			getIMeikdqmcModels();
		}
		return _IMeikdqmcModel;
	}

	public IPropertySelectionModel getIMeikdqmcModels() {
		JDBCcon con = new JDBCcon();
		try{
//		List fahdwList = new ArrayList();
//		fahdwList.add(new IDropDownBean(-1,"��ѡ��"));
//		
//		String sql="";
//		sql = "select id,meikdqmc from meikdqb order by meikdqmc";
////		System.out.println(sql);
//		ResultSet rs = con.getResultSet(sql);
//		for(int i=0;rs.next();i++){
//			fahdwList.add(new IDropDownBean(i,rs.getString("meikdqmc")));
//		}
		
		String sql="";
		sql = "select id,mingc from gongysb order by mingc";
		_IMeikdqmcModel = new IDropDownModel(sql);
		
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			con.Close();
		}
		return _IMeikdqmcModel;
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
//	 �ֹ�˾������
	private boolean _fengschange = false;

	public IDropDownBean getFengsValue() {
		if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
			((Visit) getPage().getVisit())
					.setDropDownBean4((IDropDownBean) getFengsModel()
							.getOption(0));
		}
		return ((Visit) getPage().getVisit()).getDropDownBean4();
	}

	public void setFengsValue(IDropDownBean Value) {
		if (getFengsValue().getId() != Value.getId()) {
			_fengschange = true;
		}
		((Visit) getPage().getVisit()).setDropDownBean4(Value);
	}

	public IPropertySelectionModel getFengsModel() {
		if (((Visit) getPage().getVisit()).getProSelectionModel4() == null) {
			getFengsModels();
		}
		return ((Visit) getPage().getVisit()).getProSelectionModel4();
	}

	public void setDiancxxModel(IPropertySelectionModel value) {
		((Visit) getPage().getVisit()).setProSelectionModel4(value);
	}

	public void getFengsModels() {
		String sql;
		sql = "select id ,mingc from diancxxb where jib=2 order by id";
		setDiancxxModel(new IDropDownModel(sql,"�й����Ƽ���"));
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
//	�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
	public String getTreeDiancmc(String diancmcId) {
		if(diancmcId==null||diancmcId.equals("")){
			diancmcId="1";
		}
		String IDropDownDiancmc = "";
		JDBCcon cn = new JDBCcon();
		
		String sql_diancmc = "select d.mingc from diancxxb d where d.id="+ diancmcId;
		ResultSet rs = cn.getResultSet(sql_diancmc);
		try {
			while (rs.next()) {
				IDropDownDiancmc = rs.getString("mingc");
			}
			rs.close();
		} catch (SQLException e) {
			// TODO �Զ����� catch ��
			e.printStackTrace();
		}finally{
			cn.Close();
		}

		return IDropDownDiancmc;

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
		if(getDiancTreeJib()!=3){
			tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
			ComboBox cb = new ComboBox();
			cb.setTransform("BaoblxDropDown");
			cb.setWidth(120);
			cb.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb);
			tb1.addText(new ToolbarText("-"));
		
			tb1.addText(new ToolbarText("�±��ھ�:"));
			ComboBox cb2 = new ComboBox();
			cb2.setTransform("YuebDropDown");
			cb2.setWidth(120);
			cb2.setListeners("select:function(){document.Form0.submit();}");
			tb1.addField(cb2);
			tb1.addText(new ToolbarText("-"));
		}
		
			tb1.addText(new ToolbarText("�ھ�����:"));
			ComboBox cb3 = new ComboBox();
			cb3.setTransform("KoujlxDropDown");
			cb3.setWidth(120);
			cb3.setListeners("select:function(){document.Form0.submit();}");
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
	/////////////////////////////////////////////////////
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