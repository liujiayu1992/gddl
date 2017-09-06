/*
 * ���ߣ�zuoh
 * ʱ�䣺2010-11-27
 * ������ʵ��ҳ�治�Զ�ˢ��
 */
package com.zhiren.jt.zdt.monthreport.niandhtzxqkreport;
/* 
* ʱ�䣺2010-6-13
* ���ߣ� sy
* �޸����ݣ��޸Ĳ�ѯ������ά��ҳ�����ά���ĳɰ���ά��
* 		   
*/  
/* 
* ʱ�䣺2010-8-3
* ���ߣ� sy
* �޸����ݣ��޸Ķ�����û��decode�ж�
* 		   
*/
/* 
* ʱ�䣺2010-8-4
* ���ߣ� sy
* �޸����ݣ��޸�ֻ���±��ھ����ص������Ŀ�
*          �޸ĺ�ͬ�ʼۿ��˱�׼������λС������Ϊ��ֵ̫С���������ͻ���0
* 		   
*/
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
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
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import com.zhiren.common.Money;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;

import org.apache.tapestry.contrib.palette.SortMode;

public class Niandhtzxqkreport  extends BasePage implements PageValidateListener{

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
            //this.getSelectData();
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
            setBaoblxValue(null);
            getIBaoblxModels();
            this.setTreeid(null);
//			this.getTree();
            visit.setDropDownBean4(null);
            visit.setProSelectionModel4(null);
            visit.setDropDownBean2(null);
            visit.setProSelectionModel2(null);
            isBegin=true;

        }

        getToolBars() ;
        this.Refurbish();
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
        Visit visit = (Visit) getPage().getVisit();
        String strSQL="";
        _CurrentPage=1;
        _AllPages=1;

        JDBCcon cn = new JDBCcon();

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

        String zhuangt_sl="";
        String zhuangt_zl="";
        String zhuangt_dj="";
        String zhangt_ndht="";
        if(visit.getRenyjb()==3){
            zhuangt_sl="";
            zhuangt_zl="";
            zhuangt_dj="";
            zhangt_ndht="";
        }else if(visit.getRenyjb()==2){
            zhuangt_sl=" and (sl.zhuangt=1 or sl.zhuangt=2)";
            zhuangt_zl=" and (zl.zhuangt=1 or zl.zhuangt=2)";
            zhuangt_dj=" and (dj.zhuangt=1 or dj.zhuangt=2)";
            zhangt_ndht=" and (ndht.zhuangt=1 or ndht.zhuangt=2)";
        }else if(visit.getRenyjb()==1){
            zhuangt_sl=" and sl.zhuangt=2";
            zhuangt_zl=" and zl.zhuangt=2";
            zhuangt_dj=" and dj.zhuangt=2";
            zhangt_ndht=" and ndht.zhuangt=2";
        }

        String strGongsID = "";
        String notHuiz="";
        String notHuiz1="";
        String biaot="";
        String biaoti="";
        String group="";
        String order="";
        int jib=this.getDiancTreeJib();
        if(jib==1){//ѡ����ʱˢ�³����еĵ糧
            strGongsID=" ";

            biaot="select decode(grouping(g.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n" ;
            group="  group by rollup(f.mingc,g.mingc)\n" ;

            order=" order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

        }else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
            strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
            notHuiz=" having not grouping(f.mingc)=1 ";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
            notHuiz1=" and grouping(fgsmc)=0";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
            biaot="select decode(grouping(g.mingc)+grouping(f.mingc),2,'�ܼ�',1,f.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n" ;
            group="  group by rollup(f.mingc,g.mingc)\n" ;

            order=" order by grouping(f.mingc) desc ,max(f.xuh),f.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

        }else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
            strGongsID=" and dc.id= " +this.getTreeid();


            if(getBaoblxValue().getValue().equals("�ֳ�����")){
                notHuiz=" having not  grouping(dc.mingc)=1";
                notHuiz1=" and grouping(dc.mingc)=0";
            }else if(getBaoblxValue().getValue().equals("�ֿ����")){
                biaot="select decode(grouping(g.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dc.mingc,'&nbsp;&nbsp;'||g.mingc) as diancmc,\n" ;
                group="  group by rollup(dc.mingc,g.mingc)\n" ;

                order=" order by grouping(dc.mingc) desc ,max(dc.xuh),dc.mingc,grouping(g.mingc) desc ,max(g.xuh),g.mingc";

                notHuiz=" having not  grouping(dc.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
                notHuiz1=" and  grouping(dc.mingc)=0";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
            }else{
                notHuiz=" having not  grouping(f.mingc)=1";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
                notHuiz1=" and grouping(fgsmc)=0";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
            }
        }else if (jib==-1){
            strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
        }

        //�����ͷ����
        Report rt = new Report();
        int ArrWidth[] = null;
        String ArrHeader[][] = null;
        String arrFormat[]=null;
        int iFixedRows=0;//�̶��к�
        int iCol=0;//����
        //��������
        String dianckjmx_bm="";
        String dianckjmx_tj="";

        if(getYuebValue().getValue().equals("ȫ��")){
            dianckjmx_bm="";
            dianckjmx_tj="";
        }else{
            dianckjmx_bm=",dianckjmx kjmx";
            dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
        }


        biaoti="�ص��ִͬ������±���";

        strSQL ="select decode(grouping(fgsmc)+grouping(dc.mingc)+grouping(y.mingc),3,'�ܼ�',2,fgsmc,1,'&nbsp;&nbsp;'||dc.mingc,'&nbsp;&nbsp;&nbsp;&nbsp;'||y.mingc) as mingc,\n" +
                "     decode(1,1,fx.fenx,'') as fenx,\n" +
                "     --���ͬ\n" +
                "     sum(ht.hetl) as hetl,\n" +
                "     Round(decode(sum(ht.hetl),0,0,sum(ht.hetl*ht.hetjzrz)/sum(ht.hetl)),0) as hetjzrz,\n" +
                "     Round(decode(sum(ht.hetl),0,0,sum(ht.hetl*ht.hetjzckjg)/sum(ht.hetl)),0) as hetjzckjg,\n" +
                "     Round(decode(sum(ht.hetl),0,0,sum(ht.hetl*ht.hetjzdcjg)/sum(ht.hetl)),0) as hetjzdcjg,\n" +
                "     Round(decode(sum(ht.hetl),0,0,sum(ht.hetl*ht.hetzjkhbz)/sum(ht.hetl)),2) as hetzjkhbz,\n" +
                "     max(ht.yansfs) as yansfs,\n" +
                "	   sum(ht.pingjl)as pingjfjl,--��Ⱥ�ͬ����ƽ���ֽ���\n"+
                "     --�±�����\n" +
                "     sum(sm.laimsl) as laimsl,\n" +
                "     sum(sm.biaoz) as biaoz,\n" +
                "     sum(sm.jingz) as jingz,\n" +
                "     Round(decode(sum(sm.laimsl),0,0,round(sum(sm.laimsl*sm.farl_kf)/sum(sm.laimsl),2))*1000/4.1816,0) as farl_kf,\n" +
                "     Round(decode(sum(sm.laimsl),0,0,round(sum(sm.laimsl*sm.farl_dc)/sum(sm.laimsl),2))*1000/4.1816,0)  as farl_dc,\n" +
                "     Round(decode(sum(sm.laimsl),0,0,sum(sm.laimsl*sm.meij)/sum(sm.laimsl)),2) as meij ,\n" +
                "     Round(decode(sum(sm.laimsl),0,0,sum(sm.laimsl*sm.yunj)/sum(sm.laimsl)),2) as yunj ,\n" +
                "     Round(decode(sum(sm.laimsl),0,0,sum(sm.laimsl*sm.zaf)/sum(sm.laimsl)),2) as zaf ,\n" +
                "     Round(decode(sum(sm.farl_dc*sm.laimsl),0,0,round(sum(sm.buhs_daoczhj*sm.laimsl)/sum(sm.laimsl),2)*29.271/round(sum(sm.farl_dc*sm.laimsl)/sum(sm.laimsl),2)),2)  as bhs_biaomdj,\n" +
                "     --��ͬ������ͳһ��ͳ����ʵ�ʵ������ͺ�ͬ���������ͳ������ƽ���ֽ�������\n" +
                "     round(decode(nvl(sum(ht.pingjl),0),0,0,(sum(sm.laimsl)/(sum(ht.pingjl)*10000))*100),2)as daohl,\n" +
                "	   max(ht.beiz) as beiz\n" +
                " from (select dcid.diancxxb_id,dcid.gongysb_id,fx.fenx,fx.xuh from\n" +
                "     (select distinct kj.diancxxb_id,y.id as gongysb_id  from yueslb sl ,yuezlb zl,yuercbmdj dj,yuetjkjb kj,meikdqb y\n" +
                "             where  sl.yuetjkjb_id=kj.id   and  zl.yuetjkjb_id=kj.id  and  dj.yuetjkjb_id=kj.id\n" +
//				 "             " +  zhuangt_sl	+ zhuangt_zl+ zhuangt_dj +
                " and kj.gongysb_id=y.id and kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') and (kj.jihkjb_id=1 or kj.jihkjb_id=3)\n" +
                "       ) dcid,\n" +
                "     (select decode(1,1,'����') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�')  as fenx,2 as xhu from dual ) fx,vwdianc dc"+dianckjmx_bm+"\n" +
                "     where dc.id=dcid.diancxxb_id   "+dianckjmx_tj +strGongsID+"\n"+
                "     ) fx,\n" +
                " (  select decode(1,1,'����') as fenx,kj.diancxxb_id as diancxxb_id,kj.gongysb_id as gongysb_id,\n" +
                "          sum(sl.laimsl) as laimsl,\n" +
                "          sum(sl.biaoz) as biaoz,\n" +
                "          sum(sl.jingz) as jingz,\n" +
                "          decode(sum(sl.laimsl),0,0,sum(sl.laimsl*zl.qnet_ar_kf)/sum(sl.laimsl)) as farl_kf,\n" +
                "          decode(sum(sl.laimsl),0,0,sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))  as farl_dc,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*dj.meij)/sum(sl.laimsl)),2) as meij ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*dj.yunj)/sum(sl.laimsl)),2) as yunj ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*(dj.zaf+dj.daozzf+dj.qit+dj.jiaohqzf))/sum(sl.laimsl)),2) as zaf ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum((dj.meij+dj.yunj+dj.zaf+dj.daozzf+dj.qit+dj.jiaohqzf-dj.meijs-dj.yunjs)*sl.laimsl)/sum(sl.laimsl)),4) as buhs_daoczhj\n" +
                "        from yueslb sl ,yuezlb zl,yuercbmdj dj,yuetjkjb kj, meikdqb g, vwdianc dc\n" +
                "        where sl.yuetjkjb_id=kj.id\n" +
                "             and  zl.yuetjkjb_id=kj.id\n" +
                "             and  dj.yuetjkjb_id=kj.id\n" +
                "             and sl.fenx='����'\n" +
                "             and zl.fenx(+)=sl.fenx\n" +
                "             and dj.fenx(+)=sl.fenx\n" +
                "             " +  zhuangt_sl	+ zhuangt_zl+ zhuangt_dj +"and kj.diancxxb_id(+) = dc.id\n" +
                "             and kj.gongysb_id=g.id\n" + strGongsID+"\n"+
                "             and kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                "        group by(kj.diancxxb_id,kj.gongysb_id )\n" +
                "    union\n" +
                "    select decode(1,1,'�ۼ�') as fenx,kj.diancxxb_id as diancxxb_id,kj.gongysb_id as gongysb_id,\n" +
                "          sum(sl.laimsl) as laimsl,\n" +
                "          sum(sl.biaoz) as biaoz,\n" +
                "          sum(sl.jingz) as jingz,\n" +
                "          decode(sum(sl.laimsl),0,0,sum(sl.laimsl*zl.qnet_ar_kf)/sum(sl.laimsl)) as farl_kf,\n" +
                "          decode(sum(sl.laimsl),0,0,sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))  as farl_dc,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*dj.meij)/sum(sl.laimsl)),2) as meij ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*dj.yunj)/sum(sl.laimsl)),2) as yunj ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum(sl.laimsl*(dj.zaf+dj.daozzf+dj.qit+dj.jiaohqzf))/sum(sl.laimsl)),2) as zaf ,\n" +
                "          Round(decode(sum(sl.laimsl),0,0,sum((dj.meij+dj.yunj+dj.zaf+dj.daozzf+dj.qit+dj.jiaohqzf-dj.meijs-dj.yunjs)*sl.laimsl)/sum(sl.laimsl)),4) as buhs_daoczhj\n" +
                "        from yueslb sl ,yuezlb zl,yuercbmdj dj,yuetjkjb kj, meikdqb g, vwdianc dc\n" +
                "        where sl.yuetjkjb_id=kj.id\n" +
                "             and  zl.yuetjkjb_id=kj.id\n" +
                "             and  dj.yuetjkjb_id=kj.id\n" +
                "             and sl.fenx='�ۼ�'\n" +
                "             and zl.fenx(+)=sl.fenx\n" +
                "             and dj.fenx(+)=sl.fenx\n" +
                "             " +  zhuangt_sl	+ zhuangt_zl+ zhuangt_dj +"and kj.diancxxb_id(+) = dc.id\n" + strGongsID+"\n"+
                "             and kj.gongysb_id=g.id and kj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                "        group by(kj.diancxxb_id,kj.gongysb_id )) sm,\n" +

                "(select  decode(1,1,'����') as fenx, ndht.diancxxb_id,ndht.gongysb_id,ndht.hetl as hetl,ndht.hetjzrz,ndht.hetjzckjg,ndht.hetjzdcjg\n" +
                "   ,ndht.hetzjkhbz,ndht.yansfs,round(ndht.hetl,2)as pingjl,ndht.beiz \n"+

                "         from niandhtzxqkb ndht,vwdianc dc\n" +
                "         where  ndht.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')  " +  zhangt_ndht +" and ndht.diancxxb_id(+) = dc.id\n" + strGongsID+"\n" +
                "   union\n" +

                "select  decode(1,1,'�ۼ�') as fenx, ndht.diancxxb_id,ndht.gongysb_id,\n" +
                "   --ndht.hetl as hetl,round(ndht.hetl,2)as pingjl,ndht.hetjzrz,ndht.hetjzckjg,ndht.hetjzdcjg,ndht.hetzjkhbz,ndht.yansfs,ndht.beiz\n" +
                "    sum(ndht.hetl) as hetl,\n" +
                "    Round(decode(sum(ndht.hetl),0,0,sum(ndht.hetl*ndht.hetjzrz)/sum(ndht.hetl)),0) as hetjzrz,\n" +
                "    Round(decode(sum(ndht.hetl),0,0,sum(ndht.hetl*ndht.hetjzckjg)/sum(ndht.hetl)),0) as hetjzckjg,\n" +
                "    Round(decode(sum(ndht.hetl),0,0,sum(ndht.hetl*ndht.hetjzdcjg)/sum(ndht.hetl)),0) as hetjzdcjg,\n" +
                "    Round(decode(sum(ndht.hetl),0,0,sum(ndht.hetl*ndht.hetzjkhbz)/sum(ndht.hetl)),2) as hetzjkhbz,\n" +
                "    max(ndht.yansfs) as yansfs,\n" +
                "  sum(round(ndht.hetl,2))as pingjfjl,--��Ⱥ�ͬ����ƽ���ֽ���\n" +
                "  max(ndht.beiz) beiz\n"+
                "         from niandhtzxqkb ndht,vwdianc dc\n" +
                "         where  ndht.riq>=getyearfirstdate(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')) and ndht.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') " +  zhangt_ndht +" and ndht.diancxxb_id(+) = dc.id\n" + strGongsID+"\n" + " group by ndht.diancxxb_id,ndht.gongysb_id)ht,vwdianc dc,meikdqb y\n" +
                "where dc.id=fx.diancxxb_id\n" +
                "     and fx.gongysb_id=y.id(+)\n" +
                "     and fx.diancxxb_id=sm.diancxxb_id(+)\n" +
                "     and fx.gongysb_id=sm.gongysb_id(+)\n" +
                "     and fx.fenx=sm.fenx(+)\n" +
                "     and fx.diancxxb_id=ht.diancxxb_id(+)\n" +
                "     and fx.gongysb_id=ht.gongysb_id(+)\n" +
                "     and fx.fenx=ht.fenx(+)\n" +
                "group by rollup(fx.fenx,fgsmc,dc.mingc,y.mingc)\n" +
                " having not grouping(fx.fenx)=1"+notHuiz1+"\n" +
                " order by grouping(fgsmc) desc,max(fgsxh) ,fgsmc,\n" +
                "          grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,\n" +
                "          grouping(y.mingc) desc,max(y.xuh) ,y.mingc,\n" +
                "          grouping(fx.fenx) desc,max(fx.xuh),fx.fenx";

        ArrHeader=new String[4][20];

        ArrHeader[0]=new String[] {"��λ����","����","��Ⱥ�ͬ���","��Ⱥ�ͬ���","��Ⱥ�ͬ���","��Ⱥ�ͬ���","��Ⱥ�ͬ���","��Ⱥ�ͬ���","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","ͳ���ں�ִͬ�����","��ע"};
        ArrHeader[1]=new String[] {"��λ����","����","��ͬ����","��ͬ��׼��ֵ","��ͬ��׼����/ƽ��/����۸�","��ͬ��׼�����۸�","��ͬ�ʼۿ��˱�׼","���շ�ʽ����/������","��Ⱥ�ͬ����ƽ���ֽ���","ʵ�ʵ���/��ʵ����","ʵ�ʵ���/�ۻ�Ʊ��","����/�����վ���","��/������������ֵ","����/��������ֵ","�Ѹ���Ӧ��ú��","�Ѹ���Ӧ���˼�","�Ѹ���Ӧ���ӷ�","����˰�볧��ú����","������","��ע"};
        ArrHeader[2]=new String[] {"��λ����","����","���","ǧ��/ǧ��","Ԫ/��","Ԫ/��","Ԫ/ǧ��","","���","��","��","��","ǧ��/ǧ��","ǧ��/ǧ��","Ԫ/��","Ԫ/��","Ԫ/��","Ԫ/��","%",""};
        ArrHeader[3]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};

        ArrWidth=new int[] {150,50,60,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50,50};
        arrFormat= new String []{"","","0.00","0","0.00","0.00","0.00","","0.00","0","0","0","0","0","0.00","0.00","0.00","0.00","0.00",""};

        iFixedRows=1;
        iCol=10;


        // System.out.println(strSQL);
        ResultSet rs = cn.getResultSet(strSQL);

        // ����

        Table tb = new Table(rs, 4, 0, 1);
        rt.setBody(tb);

        rt.setTitle( getBiaotmc()+intyear+"��"+intMonth+biaoti, ArrWidth);
        rt.setDefaultTitle(1, 3, "���λ:"+((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);

        rt.setDefaultTitle(9, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
//			rt.setDefaultTitle(15, 5, "�ص��ͬͳ�Ʊ�", Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(20);
        rt.body.setHeaderData(ArrHeader);// ��ͷ����
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.body.ShowZero = false;
        if(rt.body.getRows()>4){
            rt.body.setCellAlign(5, 1, Table.ALIGN_CENTER);
        }

        rt.body.setColFormat(arrFormat);
        //ҳ��
        rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1,3,"��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_LEFT);
        rt.setDefautlFooter(10,2,"���:",Table.ALIGN_LEFT);
        rt.setDefautlFooter(13,3,"�Ʊ�:",Table.ALIGN_LEFT);
        rt.setDefautlFooter(rt.footer.getCols()-1,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);

        _CurrentPage=1;
        _AllPages=rt.body.getPages();
        if (_AllPages==0){
            _CurrentPage=0;
        }
        cn.Close();

        return rt.getAllPagesHtml();
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

    //	�󱨱�����
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
            List fahdwList = new ArrayList();
            if(visit.getRenyjb()==1){
                fahdwList.add(new IDropDownBean(0,"�ֹ�˾����"));
                fahdwList.add(new IDropDownBean(1,"�ֳ��ֿ����"));
                fahdwList.add(new IDropDownBean(2,"�ֳ�����"));
            }else{
                fahdwList.add(new IDropDownBean(0,"�ֳ�����"));
                fahdwList.add(new IDropDownBean(1,"�ֳ��ֿ����"));
            }
            _IBaoblxModel = new IDropDownModel(fahdwList);

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            con.Close();
        }
        return _IBaoblxModel;
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
		
		/*tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
		ComboBox cb = new ComboBox();
		cb.setTransform("BaoblxDropDown");
		cb.setWidth(120);
		cb.setListeners("select:function(){document.Form0.submit();}");
		tb1.addField(cb);
		tb1.addText(new ToolbarText("-"));*/
        if(getDiancTreeJib()!=3){
            tb1.addText(new ToolbarText("�±��ھ�:"));
            ComboBox cb2 = new ComboBox();
            cb2.setTransform("YuebDropDown");
            cb2.setWidth(120);
//			cb2.setListeners("select:function(){document.Form0.submit();}");
            tb1.addField(cb2);
            tb1.addText(new ToolbarText("-"));
        }


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