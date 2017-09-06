/*
* ���ߣ�zuoh
* ʱ�䣺2010-11-19
* ������ʵ��ҳ�治�Զ�ˢ��
*/
package com.zhiren.jt.zdt.monthreport.yuemtgyqkbreport;

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
* ʱ�䣺2010-01-4
* ���ߣ� ll
* �޸����ݣ�1�����ӡ����ֹ�˾ͳ�ơ�������ѯ��
* 			2�����ӡ��±��ھ��������򣬶����ݽ��в�ѯ��
*/
/*
* ʱ�䣺2010-7-29
* ���ߣ�����
* �޸����ݣ�1���ھ������ص�������ⶼ�����г��ɹ���
*/
/*
* ʱ�䣺2010-8-4
* ���ߣ�����
* �޸����ݣ�1���г������ھ����飬�����ص��ú���п����ظ�
*/
/*
* ʱ�䣺2010-09-19
* sy
* �޸����ݣ�1�޸�sql����ʹ��having not��������oracle�汾bug�������
*/
/*
* ʱ�䣺2010-11-02
* ���ߣ�liufl
* �޸����ݣ�1������"�ھ�����"�����˵�����ѡ��"�ȱȿھ�"��"�±��糧�ھ�"
*          2��δ��������ú�ɫ������ʾ
*/
/*
* ʱ�䣺2010-12-21
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql����λѡ�糧+���糧ͳ��ʱ���˹�˾�ܼ�
*/
/*
* ʱ�䣺2010-12-31
* ���ߣ�liufl
* �޸����ݣ�1���޸�sql����λѡ�糧ʱ����ʾ��ͳ�ƿھ���������
*            2����λѡ����+������ͳ��ʱ��ɫ��ʾ���ԣ��޸�sql
*
*/
/*
* ʱ�䣺2011-01-10
* ���ߣ�liufl
* �޸����ݣ��޸ĵ�������ʱ��������Ҳ����������
*
*/
/*
* ʱ�䣺2011-09-28
* ���ߣ�Zhangx
* �޸����ݣ������ҳ�������˶����ʣ�����͵õ��Ǹ������ʲ�ͨ���������һ���ƻ���������ö���ú��/����ƻ���*100�ó������ʣ�
* 		   ��������ҳ����û������ʣ��ͻ���ԭ��ϵͳ�еļ��㷽���ó������ʡ�
*/

/*
* ʱ�䣺2011-11-1
* ���ߣ�songy
* �޸����ݣ��޸Ķ����ʼ��㷽��,ͨ�������õ�,�Ϻ������Ķ�����ֱ��ȡduixlb*/
/*
* ʱ�䣺2012-01-12
* ���ߣ�liufl
* �޸����ݣ����ϵͳ���������Ƿ��ѯ��ͼ
*
*/
public class Yuemtgyqkbreport  extends BasePage implements PageValidateListener{

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
            visit.setDropDownBean5(null);
            visit.setProSelectionModel5(null);
            visit.setProSelectionModel2(null);
            visit.setProSelectionModel3(null);
            visit.setDropDownBean2(null);
            visit.setDropDownBean3(null);
            this.setBaoblxValue(null);
            this.getIBaoblxModels();
            //	this.setKoujlxValue(null);
            //	this.getIKoujlxModels();
            isBegin=true;
            this.getSelectData();
        }
        if (visit.getRenyjb()==3){
            if(!this.getTreeid().equals(visit.getDiancxxb_id()+"")){
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                setNianfValue(null);
                setYuefValue(null);
                getNianfModels();
                getYuefModels();
                this.setTreeid(null);
//				this.getTree();
                visit.setDropDownBean5(null);
                visit.setProSelectionModel5(null);
                visit.setProSelectionModel2(null);
                visit.setProSelectionModel3(null);
                visit.setDropDownBean2(null);
                visit.setDropDownBean3(null);
                this.setBaoblxValue(null);
                this.getIBaoblxModels();
                //	this.setKoujlxValue(null);
                //	this.getIKoujlxModels();
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
        Visit visit = (Visit) getPage().getVisit();
        String strSQL="";
        _CurrentPage=1;
        _AllPages=1;

        JDBCcon cn = new JDBCcon();

        String zhuangt="";
        String guolzj="";
        String guolzj2="";
        String yb_sl="";
        String yb_cg="";
        if(visit.getRenyjb()==3){
            yb_sl="yueslb";
            yb_cg="yuecgjhb";
            zhuangt="";
        }else if(visit.getRenyjb()==2){
            ResultSet rs=cn.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
            try {
                if(rs.next()) {
                    String dcids=rs.getString("zhi");
                    if(dcids.indexOf(visit.getDiancxxb_id()+"")>=0) {
                        yb_sl="vwyueslb";
                        yb_cg="vwyuecgjhb";
                    }else {
                        yb_sl="yueslb";
                        yb_cg="yuecgjhb";
                    }
                }else {
                    yb_sl="yueslb";
                    yb_cg="yuecgjhb";
                }
                zhuangt=" and (sl.zhuangt=1 or sl.zhuangt=2)";
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
                        yb_cg="vwyuecgjhb";
                    }else {
                        yb_sl="yueslb";
                        yb_cg="yuecgjhb";
                    }
                }else {
                    yb_sl="yueslb";
                    yb_cg="yuecgjhb";
                }
                zhuangt=" and sl.zhuangt=2";
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
        String danwmc="";//��������

        int jib=this.getDiancTreeJib();

        if(jib==1){//ѡ����ʱˢ�³����еĵ糧
            strGongsID=" ";
            guolzj="";
            strDiancFID="'',";
        }else if (jib==2){//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
            strGongsID = "  and (dc.fuid=  " +this.getTreeid()+" or dc.shangjgsid="+this.getTreeid()+")";
            guolzj=" and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
            guolzj2="";
            strDiancFID=this.getTreeid()+",";
        }else if (jib==3){//ѡ�糧ֻˢ�³��õ糧
            strGongsID=" and dc.id= " +this.getTreeid();
            guolzj="   and a.fgs=0 --grouping(dc.mingc)=0\n";
            guolzj2="  and a.dcmc=0 \n";
            strDiancFID="'',";
        }else if (jib==-1){
            strGongsID=" and dc.id = "+((Visit) getPage().getVisit()).getDiancxxb_id();
            strDiancFID="'',";
        }
        danwmc=getTreeDiancmc(this.getTreeid());

        //�����ͷ����
        Report rt = new Report();
        int ArrWidth[] = null;
        String ArrHeader[][] = null;
        String titlename="ú̿��Ӧ�����";
        int iFixedRows=0;//�̶��к�
        int iCol=0;//����
        //��������
        String biaot="";
        String dianc="";
        String tiaoj="";
        String fenz="";
        String grouping="";
        String dianckjmx_bm="";
        String dianckjmx_tj="";
        String strzt="";
        String koujid="";
		/*String strFunctionName ="";
		    if(getYuebValue().getValue().equals("ȫ��")){
			dianckjmx_bm="";
			dianckjmx_tj="";
			strFunctionName = "getShenhzt";
			strzt=strFunctionName+"("+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','����'"+","+visit.getRenyjb()+")as bqby,\n" +
            strFunctionName+"("+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','�ۼ�'"+","+visit.getRenyjb()+") as bqlj,\n" +
            strFunctionName+"("+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','����'"+","+visit.getRenyjb()+") as tqby,\n" +
            strFunctionName+"("+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','�ۼ�'"+","+visit.getRenyjb()+") as tqlj\n" ;
		}else{
			dianckjmx_bm=",dianckjmx kjmx";
			dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
			strFunctionName = "getShenhzt_fenkj";
			strzt=strFunctionName+"("+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?+getYuebValue().getId()+"":"")+","+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','����'"+","+visit.getRenyjb()+")as bqby,\n" +
            strFunctionName+"("+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?+getYuebValue().getId()+"":"")+","+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','�ۼ�'"+","+visit.getRenyjb()+") as bqlj,\n" +
            strFunctionName+"("+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?+getYuebValue().getId()+"":"")+","+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','����'"+","+visit.getRenyjb()+") as tqby,\n" +
            strFunctionName+"("+(strFunctionName.equalsIgnoreCase("getShenhzt_fenkj")?+getYuebValue().getId()+"":"")+","+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','�ۼ�'"+","+visit.getRenyjb()+") as tqlj\n" ;
		}*/
        if(getYuebValue().getValue().equals("ȫ��")){
            dianckjmx_bm="";
            dianckjmx_tj="";
            koujid="'',";
        }else{
            dianckjmx_bm=",dianckjmx kjmx";
            dianckjmx_tj=" and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id="+getYuebValue().getId()+" ";
            koujid=getYuebValue().getId()+",";
        }

        strzt="nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','����'"+","+visit.getRenyjb()+"),0) as bqby,\n" +
                "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),'yueslb','�ۼ�'"+","+visit.getRenyjb()+"),0) as bqlj,\n" +
                "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','����'"+","+visit.getRenyjb()+"),0) as tqby,\n" +
                "nvl(getShenhzt("+koujid+strDiancFID+"dqmc,fgsmc,diancmc,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12),'yueslb','�ۼ�'"+","+visit.getRenyjb()+"),0) as tqlj\n" ;

        if(jib==3){
            biaot=
                    "  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC," +
                            "  gethetdxl(a.idd,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') ,a.FENX) BQDXL,\n" +
                            " gethetdxl(a.idd,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) ,a.FENX) TQDXL,\n" +

                            "a.BQYD,a.TQYD,\n" +
                            "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL,"+strzt+" \n"+
                            "  from (\n" +
                            " select decode (grouping(dc.mingc)+grouping(dc.fgsmc),2,100,1,MAX(dc.fuid), MAX(dc.id)) idd,decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

            dianc=" vwdianc dc \n";
            tiaoj="";
            fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                    "--having not grouping(fx.fenx)=1 "+guolzj+
                    " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                    "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
                    " )a   where a.f=0 and a.dcmc=0";
            grouping="   ,grouping(dc.fgsmc)  fgs \n";
        }else{
            if(getBaoblxValue().getValue().equals("������ͳ��")){
				/*biaot="  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
				     "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL,"+strzt+" \n"+
						"  from (\n" +
						" select  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";
				dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
				tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
				fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
					 "--having not grouping(fx.fenx)=1\n" +
					 " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
					 "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
					 " )a   where a.f=0 ";*/

                biaot="  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC," +
                        "  gethetdxl(a.idd,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') ,a.FENX) BQDXL,\n" +
                        " gethetdxl(a.idd,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) ,a.FENX) TQDXL,\n" +
                        "a.BQYD,a.TQYD,\n" +
                        "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL,"+strzt+" \n"+
                        "  from (\n" +
                        " select decode (grouping(dc.mingc)+grouping(dc.fgsmc),2,100,1,MAX(dc.fuid), MAX(dc.id)) idd, decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode("+jib+",2,'',dq.mingc) as dqmc,decode("+jib+",1,'',(select mingc from diancxxb where id="+this.getTreeid()+")) fgsmc,dc.mingc as diancmc";
                dianc=" diancxxb dc,shengfb sf,shengfdqb dq\n";
                tiaoj=" and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                fenz="group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1\n" +
                        " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
                        " )a   where a.f=0 ";

            }else if(getBaoblxValue().getValue().equals("���糧ͳ��")){
                biaot=
                        "  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC," +
                                "  gethetdxl(a.idd,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') ,a.FENX) BQDXL,\n" +
                                " gethetdxl(a.idd,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) ,a.FENX) TQDXL,\n" +
                                "a.BQYD,a.TQYD,\n" +
                                "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL,"+strzt+" \n"+
                                "  from (\n" +
                                " select  decode (grouping(dc.mingc)+grouping(dc.fgsmc),2,100,1,MAX(dc.fuid), MAX(dc.id)) idd, decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                dianc=" vwdianc dc \n";
                tiaoj="";
                fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1 "+guolzj+
                        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
                        " )a   where a.f=0 "+guolzj+guolzj2;
                grouping="   ,grouping(dc.fgsmc)  fgs \n";

            }else if(getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")){
                biaot="  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,\n" +
                        "  gethetdxl(a.idd,to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') ,a.FENX) BQDXL,\n" +
                        " gethetdxl(a.idd,add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12) ,a.FENX) TQDXL,\n" +
                        "a.BQYD,a.TQYD,\n" +
                        "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL, "+strzt+" \n"+
                        "  from ( \n" +
                        " select  decode (grouping(dc.mingc)+grouping(dc.fgsmc),2,100,1,MAX(dc.fuid), MAX(dc.id)) idd, decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                dianc=" vwdianc dc \n";
                tiaoj="";
                fenz="group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1 and   grouping(dc.mingc)=1 "+guolzj+
                        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)"+
                        " )a   where a.f=0  and   dcmc=1  "+guolzj;
                grouping="   ,grouping(dc.fgsmc)  fgs \n";
            }

        }
        String shul="";
        IDropDownBean ddb=getKoujlxValue();
        if(ddb!=null&&ddb.getValue()!=null&&ddb.getValue().equals("�ȱȿھ�")){
            shul="sum(sl.laimsl*dc.konggbl) as laimsl,sum(sl.yingd*dc.konggbl) as yingd,sum(sl.kuid*dc.konggbl) as kuid,sum(sl.yuns*dc.konggbl) as yuns";
        }else{
            shul="sum(sl.laimsl) as laimsl,sum(sl.yingd) as yingd,sum(sl.kuid) as kuid,sum(sl.yuns) as yuns";
        }

        strSQL=
                "select  "+biaot+",\n" +
                        "     decode(1,1,fx.fenx,'') as fenx, sum(nvl(sm.dinghml,0)+nvl(sm.caigml,0)) as bqsm,sum(nvl(smtq.dinghml,0)+nvl(smtq.caigml,0)) as tqsm,\n" +
                        "     sum(sm.dinghml) as bqzd, sum(smtq.dinghml) as tqzd,\n" +
                        "     sum(sm.caigml) as bqsc, sum(smtq.caigml) as tqsc,\n" +
                        "     --�����ص��ͬ������\n"+
                        "	 -- decode(sum(sm.xunjhl),0,decode(sum(bq.ht),0,0,round( sum(sm.dinghml) /(sum(bq.ht))*100,2)),decode(sum(sm.xunjhl),0,0,round(sum(sm.dinghml)/sum(sm.xunjhl)*100,2))) as bqdxl,\n"+
                        "     --ͬ���ص��ͬ������\n"+
                        "    -- decode(sum(smtq.xunjhl),0,decode(sum(tq.ht),0,0,round( sum(smtq.dinghml) /(sum(tq.ht))*100,2)),decode(sum(smtq.xunjhl),0,0,round(sum(smtq.dinghml)/sum(smtq.xunjhl)*100,2))) as tqdxl,\n"+
                        "     sum(sm.yingd) as bqyd,sum(smtq.yingd) as tqyd,\n" +
                        "     sum(sm.kuid) as bqkd,sum(smtq.kuid) as tqkd,\n" +
                        "     sum(sm.yuns) as bqysl,sum(smtq.yuns) as tqysl,\n" +
                        "     grouping(fx.fenx) f ,grouping(dc.mingc) dcmc \n"+grouping+
                        " from\n" +
                        " (select dcid.diancxxb_id,fx.fenx,fx.xuh from\n" +
                        "     (select distinct diancxxb_id from yuetjkjb\n" +
                        "             where  (riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd') or riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12))\n" +
                        "     ) dcid,(select decode(1,1,'����') as fenx,1 as xuh  from dual union select decode(1,1,'�ۼ�')  as fenx,2 as xhu from dual ) fx,diancxxb dc"+dianckjmx_bm+"\n" +
                        "     where dc.id=dcid.diancxxb_id  "+dianckjmx_tj+strGongsID+" ) fx,\n" +
                        " (   select decode(1,1,'����') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id," +
                        "			 nvl(dingh.laimsl,0) as dinghml,--nvl(dingh.xunjhl,0) as xunjhl,\n nvl(caig.laimsl,0) as caigml," +
                        "			 (nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" +
                        "          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,--decode(nvl(sum(sl.hetdxl),0),0,0,round(sum(sl.laimsl)/max(sl.hetdxl)*100,2)) as xunjhl, --����ļƻ���\n" +
                        "						"+shul+"\n" +
                        "                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                          "+strGongsID+" and sl.fenx='����' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" +
                        "                    group by(tj.diancxxb_id)\n" +
                        "               ) dingh\n" +
                        "             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id, "+shul+"\n" +
                        "                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                        "+strGongsID+" and sl.fenx='����' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" +
                        "                  group by(tj.diancxxb_id)\n" +
                        "               ) caig\n" +
                        "           ON dingh.diancxxb_id=caig.diancxxb_id\n" +
                        "    union\n" +
                        "     select decode(1,1,'�ۼ�') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id," +
                        "			 nvl(dingh.laimsl,0) as dinghml,nvl(caig.laimsl,0) as caigml," +
                        "			 (nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" +
                        "          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,--decode(nvl(sum(sl.hetdxl),0),0,0,round(sum(sl.laimsl)/max(sl.hetdxl)*100,2)) as xunjhl, --�ص�ƻ���\n" +
                        "						"+shul+"\n" +
                        "                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                          "+strGongsID+" and sl.fenx='�ۼ�' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" +
                        "                    group by(tj.diancxxb_id)\n" +
                        "               ) dingh\n" +
                        "             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id,"+shul+"\n" +
                        "                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                        "+strGongsID+" and sl.fenx='�ۼ�' and tj.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" +
                        "                  group by(tj.diancxxb_id)\n" +
                        "               ) caig\n" +
                        "           ON dingh.diancxxb_id=caig.diancxxb_id\n" +
                        ") sm,\n" +
                        "( select decode(1,1,'����') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id," +
                        "		  nvl(dingh.laimsl,0) as dinghml, nvl(caig.laimsl,0) as caigml," +
                        "		  (nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns \n"+
//					"(  select decode(1,1,'����') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id ,dingh.laimsl as dinghml,caig.laimsl as caigml,(dingh.yingd+caig.yingd) as yingd,(dingh.kuid+caig.kuid) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" +
                        "          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,--decode(nvl(sum(sl.hetdxl),0),0,0,round(sum(sl.laimsl)/max(sl.hetdxl)*100,2)) as xunjhl, --�ص�ƻ���\n" +
                        "						"+shul+"\n" +
                        "                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                          "+strGongsID+" and sl.fenx='����' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" +
                        "                    group by(tj.diancxxb_id)\n" +
                        "               ) dingh\n" +
                        "             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id, "+shul+"\n" +
                        "                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                        "+strGongsID+" and sl.fenx='����' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" +
                        "                  group by(tj.diancxxb_id)\n" +
                        "               ) caig\n" +
                        "           ON dingh.diancxxb_id=caig.diancxxb_id\n" +
                        "    union\n" +
                        "     select decode(1,1,'�ۼ�') as fenx,decode(dingh.diancxxb_id,null,caig.diancxxb_id,dingh.diancxxb_id) as diancxxb_id," +
                        "			 nvl(dingh.laimsl,0) as dinghml, nvl(caig.laimsl,0) as caigml," +
                        "			 (nvl(dingh.yingd,0)+nvl(caig.yingd,0)) as yingd,(nvl(dingh.kuid,0)+nvl(caig.kuid,0)) as kuid,(nvl(dingh.yuns,0)+nvl(caig.yuns,0)) as yuns\n" +
                        "          from (  select tj.diancxxb_id as diancxxb_id,1 as jihkjb_id,--decode(nvl(sum(sl.hetdxl),0),0,0,round(sum(sl.laimsl)/max(sl.hetdxl)*100,2)) as xunjhl, --�ص�ƻ���\n" +
                        "						"+shul+"\n" +
                        "                    from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                    where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                         "+strGongsID+" and sl.fenx='�ۼ�' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "                          and (tj.jihkjb_id=1 or tj.jihkjb_id=3)"+zhuangt+"\n" +
                        "                    group by(tj.diancxxb_id)\n" +
                        "               ) dingh\n" +
                        "             FULL JOIN (  select tj.diancxxb_id as diancxxb_id,2 as jihkjb_id, "+shul+"\n" +
                        "                 from  "+yb_sl+" sl,yuetjkjb tj,jihkjb j,diancxxb dc\n" +
                        "                 where sl.yuetjkjb_id=tj.id and j.id =tj.jihkjb_id and dc.id= tj.diancxxb_id\n" +
                        "                        "+strGongsID+" and sl.fenx='�ۼ�' and tj.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "                        and (tj.jihkjb_id<>1 and  tj.jihkjb_id<>3)"+zhuangt+"\n" +
                        "                  group by(tj.diancxxb_id)\n" +
                        "               ) caig\n" +
                        "           ON dingh.diancxxb_id=caig.diancxxb_id\n" +
                        ") smtq,\n" +
//---------------------------------------------------------------------------------------------------
                        " (select decode(1,1,'����') as fenx, htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" +
                        "         from  "+yb_cg+" htqk\n" +
                        "         where  htqk.riq=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" +
                        "         group by htqk.diancxxb_id\n" +
                        "union\n" +
                        "  select decode(1,1,'�ۼ�') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" +
                        "         from  "+yb_cg+" htqk\n" +
                        "         where htqk.riq>=to_date('"+intyear+"-01-01','yyyy-mm-dd') and htqk.riq<=to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd')\n" +
                        "               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" +
                        "         group by htqk.diancxxb_id\n" +
                        "          )bq,"+

                        "(\n" +
                        " select decode(1,1,'����') as fenx, htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" +
                        "         from  "+yb_cg+" htqk\n" +
                        "         where  htqk.riq=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" +
                        "         group by htqk.diancxxb_id\n" +
                        "union\n" +
                        "  select decode(1,1,'�ۼ�') as fenx,htqk.diancxxb_id,sum(htqk.yuejhcgl) as ht\n" +
                        "         from  "+yb_cg+" htqk\n" +
                        "         where htqk.riq>=add_months(to_date('"+intyear+"-01-01','yyyy-mm-dd'),-12) and htqk.riq<=add_months(to_date('"+intyear+"-"+intMonth+"-01','yyyy-mm-dd'),-12)\n" +
                        "               and (htqk.jihkjb_id=1 or htqk.jihkjb_id=3)\n" +
                        "         group by htqk.diancxxb_id ) tq,"+


                        dianc +
                        "where dc.id=fx.diancxxb_id "+tiaoj+"\n" +
                        "  and fx.diancxxb_id=sm.diancxxb_id(+)\n" +
                        "  and fx.fenx=sm.fenx(+)\n" +
                        "  and fx.diancxxb_id=bq.diancxxb_id(+)\n" +
                        "  and fx.fenx=bq.fenx(+)\n" +
                        "  and fx.diancxxb_id=tq.diancxxb_id(+)\n" +
                        "  and fx.fenx=tq.fenx(+)\n" +
                        "  and fx.diancxxb_id=smtq.diancxxb_id(+)\n" +
                        "  and fx.fenx=smtq.fenx(+)\n" +  fenz;


//				ֱ���ֳ�����
        ArrHeader=new String[3][20];
        ArrHeader[0]=new String[] {"��λ����","��λ����","ʵ���볧��Ȼú��","ʵ���볧��Ȼú��","���У��ص㶩��ú��","���У��ص㶩��ú��","���У��г��ɹ�ú��","���У��г��ɹ�ú��","�ص㶩����ͬ������","�ص㶩����ͬ������","ӯ������","ӯ������","��������","��������","���������","���������",
                "���״̬","���״̬","���״̬","���״̬"};
        ArrHeader[1]=new String[] {"��λ����","��λ����","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
        ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};

        ArrWidth=new int[] {150,40,60,60,60,60,60,60,60,60,60,60,60,60,60,60,0,0,0,0};

        iFixedRows=1;
        iCol=10;


//			System.out.println(strSQL);
        ResultSet rs = cn.getResultSet(strSQL);

        // ����
//			rt.setBody(new Table(rs,3, 0, iFixedRows));
        Table tb=new Table(rs, 3, 0,1,4);
        rt.setBody(tb);
        rt.body.ShowZero = false;
        rt.setTitle(getBiaotmc()+intyear+"��"+intMonth+"��"+titlename, ArrWidth,4);
        rt.setDefaultTitle(1, 3, "���λ:"+this.getDiancmc(), Table.ALIGN_LEFT);
        rt.setDefaultTitle(7, 3, "�����:"+intyear+"��"+intMonth+"��", Table.ALIGN_LEFT);
        rt.setDefaultTitle(11, 2, "��λ:�֡�%", Table.ALIGN_RIGHT);
        rt.setDefaultTitle(14, 2, "cpiȼ�Ϲ���05��", Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setPageRows(18);
        rt.body.setHeaderData(ArrHeader);// ��ͷ����
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();
        rt.body.ShowZero = true;

//			��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
        if(visit.getRenyjb()!=3){
            int rows=rt.body.getRows();
            int cols=rt.body.getCols();
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

        if(rt.body.getRows()>3){
            rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
        }
        //ҳ��

        rt.createDefautlFooter(ArrWidth);
			/*  rt.setDefautlFooter(2,1,"��׼:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(4,1,"�Ʊ�:",Table.ALIGN_LEFT);
			  rt.setDefautlFooter(6,1,"���:",Table.ALIGN_LEFT);*/


//			rt.createDefautlFooter(ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ����:"+FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))),Table.ALIGN_LEFT);
        rt.setDefautlFooter(6,3,"���:",Table.ALIGN_LEFT);
        rt.setDefautlFooter(11,2,"�Ʊ�:",Table.ALIGN_LEFT);
        tb.setColAlign(2, Table.ALIGN_CENTER);
        rt.setDefautlFooter(rt.footer.getCols()-5,2,Table.PAGENUMBER_NORMAL,Table.ALIGN_RIGHT);
//			����ҳ��
//			 rt.createDefautlFooter(ArrWidth);
//			 rt.setDefautlFooter(1,2,"�����:",Table.ALIGN_LEFT);
//			 rt.setDefautlFooter(8,3,"���:",Table.ALIGN_LEFT);
//			 rt.setDefautlFooter(12,3,"��ϵ�绰:",Table.ALIGN_RIGHT);

        _CurrentPage=1;
        _AllPages=rt.body.getPages();
        if (_AllPages==0){
            _CurrentPage=0;
        }
        cn.Close();
//			System.out.println(rt.getAllPagesHtml());
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
                if (_nianf  == ((IDropDownBean) obj)
                        .getId()) {
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
//		cb.setListeners("select:function(){document.Form0.submit();}");
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