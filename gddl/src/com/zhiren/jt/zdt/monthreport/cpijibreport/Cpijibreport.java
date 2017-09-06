package com.zhiren.jt.zdt.monthreport.cpijibreport;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.Report;
import com.zhiren.report.Table;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.contrib.palette.SortMode;
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

/* 
 * ʱ�䣺2010-06-18/2010-07-02
 * ���ߣ� liufl
 * �޸����ݣ�ȼ�Ϲ���05-ȼ�Ϲ���12������Ĳ�ѯ
 * 		   
 */
/* 
 * ʱ�䣺2010-09-08
 * ���ߣ� liufl
 * �޸����ݣ��޸�cpi������ѯ��sql,������ѯ1��-���µ�ʱ��ֱ��ȡ�ۼ�ֵ����cpi����һ��������1�¿�ʼ�ľ��ø��±��¼�Ȩ
 * 		   
 */
/* 
 * ʱ�䣺2010-11-30
 * ���ߣ� liufl
 * �޸����ݣ��޸�cpi������ѯҳ�棺����δ��˵ĵ糧������������ݣ����ú�ɫ������ע
 * 		   
 */
/* 
* ʱ�䣺2010-12-15
* ���ߣ� liufl
* �޸����ݣ�1���޸�cpi10��sql�����λ�����������
*/
/* 
* ʱ�䣺2010-12-31
* ���ߣ� liufl
* �޸����ݣ�1���޸�sql,ͳ�ƿھ�ѡ��������ͳ�ơ�ʱ��ɫ��ʾ����ȷ
*/
/* 
* ʱ�䣺2011-01-06
* ���ߣ� liufl
* �޸����ݣ�1���޸�sql,��Ϊ��ѯ��Ϊ���ۼơ������ȥ�������»��ۼơ���
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
* �޸����ݣ���ѯsql��������
*          
*/
/* 
* ʱ�䣺2012-01-12
* ���ߣ�liufl
* �޸����ݣ����ϵͳ���������Ƿ��ѯ��ͼ
*            
*/
public class Cpijibreport extends BasePage implements
        PageValidateListener {

    // �ж��Ƿ��Ǽ����û�
    public boolean isJitUserShow() {
        return ((Visit) getPage().getVisit()).getRenyjb() == 1;// ����
    }

    public boolean isGongsUser() {
        return ((Visit) getPage().getVisit()).getRenyjb() == 2;// ��˾
    }

    public boolean isDiancUser() {
        return ((Visit) getPage().getVisit()).getRenyjb() == 3;// �糧
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

    //ˢ��
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
        // Ϊ "ˢ��" ��ť��Ӵ������
        isBegin = true;
        getSelectData();
    }

    // ******************ҳ���ʼ����********************//
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
            setNianfValue2(null);
            setYuefValue2(null);
            getNianfModels2();
            getYuefModels2();
            this.setTreeid(null);
            this.getTree();
            visit.setDropDownBean5(null);
            visit.setProSelectionModel5(null);
            visit.setProSelectionModel2(null);
            visit.setDropDownBean2(null);
            this.setBaoblxValue(null);
            this.getIBaoblxModels();
            this.getBaoblxValue();
            this.getBaobmcModels();
            this.getBaoblxValue2();

            isBegin = true;
            this.getSelectData();

        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                setNianfValue(null);
                setYuefValue(null);
                getNianfModels();
                getYuefModels();
                setNianfValue2(null);
                setYuefValue2(null);
                getNianfModels2();
                getYuefModels2();
                this.setTreeid(null);
                this.getTree();
                visit.setDropDownBean5(null);
                visit.setProSelectionModel5(null);
                visit.setProSelectionModel2(null);
                visit.setDropDownBean2(null);
                this.setBaoblxValue(null);
                this.getIBaoblxModels();
                this.getBaoblxValue();
                this.getBaobmcModels();
                this.getBaoblxValue2();

                isBegin = true;
                this.getSelectData();

            }
        }

        if (nianfchanged) {
            nianfchanged = false;
            Refurbish();
        }
        if (nianfchanged2) {
            nianfchanged2 = false;
            Refurbish();
        }

        if (yuefchanged) {
            yuefchanged = false;
            Refurbish();
        }
        if (yuefchanged2) {
            yuefchanged2 = false;
            Refurbish();
        }

        if (_fengschange) {
            _fengschange = false;
            Refurbish();
        }

        getToolBars();
        Refurbish();
    }

    // �õ�ϵͳ��Ϣ�������õı������ĵ�λ����
    public String getBiaotmc() {
        String biaotmc = "";
        JDBCcon cn = new JDBCcon();
        String sql_biaotmc = "select zhi from xitxxb where mingc='������ⵥλ����'";
        ResultSet rs = cn.getResultSet(sql_biaotmc);
        try {
            while (rs.next()) {
                biaotmc = rs.getString("zhi");
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return biaotmc;

    }

    //
    private String RT_HET = "yunsjhcx";

    private String mstrReportName = "yunsjhcx";

    private boolean isBegin = false;

    public String getPrintTable() {
        if (!isBegin) {
            return "";
        }
        isBegin = false;
        if (mstrReportName.equals(RT_HET)) {
            return getSelectData();
        } else {
            return "�޴˱���";
        }
    }

    // ״̬  Ŷ
    private int intZhuangt = 0;

    public int getZhuangt() {
        return 1;
    }

    public void setZhuangt(int _value) {
        intZhuangt = 1;
    }


    // ��ѯ����
    private String getSelectData() {
        JDBCcon con = new JDBCcon();
        Cpijibreportbean jibreport = new Cpijibreportbean();
        Visit visit = (Visit) getPage().getVisit();
        String yb_sl = "";
        String yb_zl = "";
        String yb_dj = "";
        String yb_jgmx = "";
        String yb_htqk = "";
        String yb_zb = "";
        String yb_shcm = "";
        String yb_shcy = "";
        String yb_rcy = "";
        if (visit.getRenyjb() == 3) {
            yb_sl = "yueslb";
            yb_zl = "yuezlb";
            yb_dj = "yuercbmdj";
            yb_jgmx = "yuedmjgmxb";
            yb_htqk = "yuecgjhb";
            yb_zb = "yuezbb";
            yb_shcm = "yueshchjb";
            yb_shcy = "yueshcyb";
            yb_rcy = "rucycbb";
        } else if (visit.getRenyjb() == 2) {
            ResultSet rs = con.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
            try {
                if (rs.next()) {
                    String dcids = rs.getString("zhi");
                    if (dcids.indexOf(visit.getDiancxxb_id() + "") >= 0) {
                        yb_sl = "vwyueslb";
                        yb_zl = "vwyuezlb";
                        yb_dj = "vwyuercbmdj";
                        yb_jgmx = "vwyuedmjgmxb";
                        yb_htqk = "vwyuecgjhb";
                        yb_zb = "vwyuezbb";
                        yb_shcm = "vwyueshchjb";
                        yb_shcy = "vwyueshcyb";
                        yb_rcy = "vwrucycbb";
                    } else {
                        yb_sl = "yueslb";
                        yb_zl = "yuezlb";
                        yb_dj = "yuercbmdj";
                        yb_jgmx = "yuedmjgmxb";
                        yb_htqk = "yuecgjhb";
                        yb_zb = "yuezbb";
                        yb_shcm = "yueshchjb";
                        yb_shcy = "yueshcyb";
                        yb_rcy = "rucycbb";
                    }
                } else {
                    yb_sl = "yueslb";
                    yb_zl = "yuezlb";
                    yb_dj = "yuercbmdj";
                    yb_jgmx = "yuedmjgmxb";
                    yb_htqk = "yuecgjhb";
                    yb_zb = "yuezbb";
                    yb_shcm = "yueshchjb";
                    yb_shcy = "yueshcyb";
                    yb_rcy = "rucycbb";
                }
                con.Close();
                rs.close();
            } catch (SQLException e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            }


        } else if (visit.getRenyjb() == 1) {
            ResultSet rs = con.getResultSet("select zhi from xitxxb where mingc='cpi�����Ƿ��ѯ��ͼ' and zhuangt=1");
            try {
                if (rs.next()) {
                    String dcids = rs.getString("zhi");
                    if (dcids.indexOf(visit.getDiancxxb_id() + "") >= 0) {
                        yb_sl = "vwyueslb";
                        yb_zl = "vwyuezlb";
                        yb_dj = "vwyuercbmdj";
                        yb_jgmx = "vwyuedmjgmxb";
                        yb_htqk = "vwyuecgjhb";
                        yb_zb = "vwyuezbb";
                        yb_shcm = "vwyueshchjb";
                        yb_shcy = "vwyueshcyb";
                        yb_rcy = "vwrucycbb";
                    } else {
                        yb_sl = "yueslb";
                        yb_zl = "yuezlb";
                        yb_dj = "yuercbmdj";
                        yb_jgmx = "yuedmjgmxb";
                        yb_htqk = "yuecgjhb";
                        yb_zb = "yuezbb";
                        yb_shcm = "yueshchjb";
                        yb_shcy = "yueshcyb";
                        yb_rcy = "rucycbb";
                    }
                } else {
                    yb_sl = "yueslb";
                    yb_zl = "yuezlb";
                    yb_dj = "yuercbmdj";
                    yb_jgmx = "yuedmjgmxb";
                    yb_htqk = "yuecgjhb";
                    yb_zb = "yuezbb";
                    yb_shcm = "yueshchjb";
                    yb_shcy = "yueshcyb";
                    yb_rcy = "rucycbb";
                }
                con.Close();
                rs.close();
            } catch (SQLException e) {
                // TODO �Զ����� catch ��
                e.printStackTrace();
            }

        }
        String strSQL = "";
        _CurrentPage = 1;
        _AllPages = 1;

        //����1
        long intyear;
        if (getNianfValue() == null) {
            intyear = DateUtil.getYear(new Date());
        } else {
            intyear = getNianfValue().getId();
        }
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // ����2
        long intyear2;
        if (getNianfValue2() == null) {
            intyear2 = DateUtil.getYear(new Date());
        } else {
            intyear2 = getNianfValue2().getId();
        }
        long intMonth2;
        if (getYuefValue2() == null) {
            intMonth2 = DateUtil.getMonth(new Date());
        } else {
            intMonth2 = getYuefValue2().getId();
        }

//		�����ͷ����
        Report rt = new Report();
        int ArrWidth[] = null;
        String ArrHeader[][] = null;
        int iFixedRows = 0;//�̶��к�
        int iCol = 0;//����
        //��������
        String biaot = "";
        String dianc = "";
        String tiaoj = "";
        String fenz = "";
        String grouping = "";
        String dianckjmx_bm = "";
        String dianckjmx_tj = "";
        String strzt = "";
        String koujid = "";

        if (getBaobmc().equals("cpiȼ�Ϲ���05��")) {
            String titlename = "ú̿��Ӧ�����";

            JDBCcon cn = new JDBCcon();

            String guolzj = "";// �����ܼ�
            String zhuangt = "";// ״̬

            String strGongsID = "";
            String danwmc = "";// ��������
            int jib = this.getDiancTreeJib();

            danwmc = getTreeDiancmc(this.getTreeid());
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (sl.zhuangt=1 or sl.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and sl.zhuangt=2";
            }
            String strDiancFID = "";
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                guolzj = "";
                strDiancFID = "'',";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
                strDiancFID = this.getTreeid() + ",";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                guolzj = "   and a.fgs=0 --grouping(dc.mingc)=0\n";
                strDiancFID = "'',";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }
            danwmc = getTreeDiancmc(this.getTreeid());

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }

            strzt = "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueslb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueslb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueslb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueslb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";

            if (jib == 3) {
                biaot =
                        "  a.DANWMC,--a.FENX,\n a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
                                "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL," + strzt + " \n" +
                                "  from (\n" +
                                " select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1 " + guolzj +
                        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                        " )a   where a.f=0 and a.dcmc=0";
                grouping = "   ,grouping(dc.fgsmc)  fgs \n";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    /*biaot="  a.DANWMC,a.FENX,a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
					     "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL,"+strzt+" \n"+
						"  from (select  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";*/
                    biaot = "  a.DANWMC,--a.FENX,\n  a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
                            "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL," + strzt + " \n" +
                            "  from (select  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc";
                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    if (jib == 3) {
                        fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1\n" +
                                " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                                " )a   where a.f=0 and a.dcmc=0";
                    } else {
                        fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1\n" +
                                " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                                " )a   where a.f=0 ";
                    }
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                        guolzj = " and a.fgs=0 and a.dcmc=0--grouping(dc.mingc)=0\n";
                    }
                    biaot =
                            "  a.DANWMC,--a.FENX,\n a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
                                    "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL," + strzt + " \n" +
                                    "  from (\n" +
                                    " select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1 " + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0 " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";

                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot = "  a.DANWMC,--a.FENX,\n  a.BQSM,a.TQSM,a.BQZD,a.TQZD,a.BQSC,a.TQSC,a.BQDXL,a.TQDXL,a.BQYD,a.TQYD,\n" +
                            "a.BQKD,a.TQKD,a.BQYSL,a.TQYSL, " + strzt + " \n" +
                            "  from (\n" +
                            " select decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    if (jib == 3) {
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1 and   grouping(dc.mingc)=1 " + guolzj + " \n" +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                                " )a   where a.f=0  and a.fgs=0 and a.dcmc=0";
                    } else {
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1 and   grouping(dc.mingc)=1 " + guolzj + " \n" +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                                " )a   where a.f=0  and   dcmc=1  " + guolzj;
                    }
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                }
            }

            String shul = "";
            IDropDownBean ddb = getKoujlxValue();
            if (ddb != null && ddb.getValue().equals("�ȱȿھ�")) {
                shul = "sum(sl.laimsl*dc.konggbl) as laimsl,sum(sl.yingd*dc.konggbl) as yingd,sum(sl.kuid*dc.konggbl) as kuid,sum(sl.yuns*dc.konggbl) as yuns";
            } else {
                shul = "sum(sl.laimsl) as laimsl,sum(sl.yingd) as yingd,sum(sl.kuid) as kuid,sum(sl.yuns) as yuns";
            }
            strSQL = jibreport.getSql5(biaot, dianc, tiaoj, grouping, shul, fenz, dianckjmx_tj, dianckjmx_bm, strGongsID, zhuangt, yb_sl, yb_htqk, intyear, intMonth, intyear2, intMonth2);

//				ֱ���ֳ�����
				/*ArrHeader=new String[3][20];
				ArrHeader[0]=new String[] {"��λ����","��λ����","ʵ���볧��Ȼú��","ʵ���볧��Ȼú��","���У��ص㶩��ú��","���У��ص㶩��ú��","���У��г��ɹ�ú��","���У��г��ɹ�ú��","�ص㶩����ͬ������","�ص㶩����ͬ������","ӯ������","ӯ������","��������","��������","���������","���������",
							                     "���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","��λ����","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20"};

				ArrWidth=new int[] {150,40,60,60,60,60,60,60,60,60,60,60,60,60,60,60,0,0,0,0};*/

            ArrHeader = new String[3][19];
            ArrHeader[0] = new String[]{"��λ����", "ʵ���볧��Ȼú��", "ʵ���볧��Ȼú��", "���У��ص㶩��ú��", "���У��ص㶩��ú��", "���У��г��ɹ�ú��", "���У��г��ɹ�ú��", "�ص㶩����ͬ������", "�ص㶩����ͬ������", "ӯ������", "ӯ������", "��������", "��������", "���������", "���������",
                    "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"};

            ArrWidth = new int[]{150, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 0, 0, 0, 0};

            iFixedRows = 1;
            iCol = 10;

            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);
            rt.body.ShowZero = false;
            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��" + titlename, ArrWidth, 4);
            rt.setDefaultTitle(1, 3, "���λ:" + this.getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(7, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(11, 2, "��λ:�֡�%", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(14, 2, "cpiȼ�Ϲ���05��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(18);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = true;

//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        String c1=rs.getString(cols + 1);
                        String c2=rs.getString(cols + 2);
                        String c3=rs.getString(cols + 3);
                        String c4=rs.getString(cols + 4);
                        if (!((c1!=null&&c1.equals("0")) && (c2!=null&&c2.equals("0")) && (c3!=null&&c3.equals("0")) && (c4!=null&&c4.equals("0")))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }
            //ҳ��
            rt.createDefautlFooter(ArrWidth);

            rt.setDefautlFooter(1, 4, "��ӡ����:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), Table.ALIGN_LEFT);
            rt.setDefautlFooter(6, 4, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(10, 3, "�Ʊ�:", Table.ALIGN_LEFT);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);
//				����ҳ��
            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();
            return rt.getAllPagesHtml();

        } else if (getBaobmc().equals("cpiȼ�Ϲ���06��")) {
            String titlename = "ú̿�������������";
            JDBCcon cn = new JDBCcon();

            String zhuangt = "";
            String yueshcmzt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (zb.zhuangt=1 or zb.zhuangt=2)";
                yueshcmzt = " and (shc.zhuangt=1 or shc.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and zb.zhuangt=2";
                yueshcmzt = " and shc.zhuangt=2";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String danwmc = "";//��������

            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                guolzj = "";
                strDiancFID = "'',";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                guolzj = " and a.fgs=0 \n";//�ֹ�˾�鿴����ʱ�����ܼơ�
                strDiancFID = this.getTreeid() + ",";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                guolzj = " and a.fgs=0 \n";
                strDiancFID = "'',";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }
            danwmc = getTreeDiancmc(this.getTreeid());

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }
            strzt = "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueshchjb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueshchjb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueshchjb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueshchjb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";
            if (jib == 3) {
                biaot =
                        "  a.DANWMC,--a.FENX,\n  a.BQFDL,a.TQFDL,a.BQQBML,a.TQQBML,a.BQFHML,a.TQFHML,a.BQGHML,a.TQGHML,a.BQQT,\n" +
                                "a.TQQT,a.BQCC,a.TQCC,a.BQSFC,a.TQSFC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                " from ( select " +
                                " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1 " + guolzj +
                        "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n" +
                        " where a.f=0  and a.dcmc=0";
                grouping = " ,grouping(dc.fgsmc) fgs ";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot =
                            "  a.DANWMC,--a.FENX,\n  a.BQFDL,a.TQFDL,a.BQQBML,a.TQQBML,a.BQFHML,a.TQFHML,a.BQGHML,a.TQGHML,a.BQQT,\n" +
                                    "a.TQQT,a.BQCC,a.TQCC,a.BQSFC,a.TQSFC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from ( select " +
                                    //" decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";
                                    " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc";

                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    if (jib == 3) {
                        fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1\n" +
                                "  \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n" +
                                " where a.f=0 and a.dcmc=0";
                    } else {
                        fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1\n" +
                                "  \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n" +
                                " where a.f=0 ";
                    }
                    grouping = " ";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                        guolzj = " and a.fgs=0 and a.dcmc=0--grouping(dc.mingc)=0\n";
                    }
                    biaot =
                            "  a.DANWMC,--a.FENX,\n  a.BQFDL,a.TQFDL,a.BQQBML,a.TQQBML,a.BQFHML,a.TQFHML,a.BQGHML,a.TQGHML,a.BQQT,\n" +
                                    "a.TQQT,a.BQCC,a.TQCC,a.BQSFC,a.TQSFC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from ( select " +
                                    " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1 " + guolzj +
                            "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n" +
                            " where a.f=0" + guolzj;
                    grouping = " ,grouping(dc.fgsmc) fgs ";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot =
                            "  a.DANWMC,--a.FENX,\n  a.BQFDL,a.TQFDL,a.BQQBML,a.TQQBML,a.BQFHML,a.TQFHML,a.BQGHML,a.TQGHML,a.BQQT,\n" +
                                    "a.TQQT,a.BQCC,a.TQCC,a.BQSFC,a.TQSFC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from ( select " +
                                    " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    if (jib == 3) {
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1 and  grouping(dc.mingc)=1 " + guolzj +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n " +
                                "  where a.f=0 and a.fgs=0 and a.dcmc=0 ";
                    } else {
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1 and  grouping(dc.mingc)=1 " + guolzj +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh))a \n " +
                                "  where a.f=0 and a.dcmc=1 " + guolzj;
                    }

                    grouping = " ,grouping(dc.fgsmc) fgs ";
                }
            }
            String shouhcsl_bq = "";
            String shouhckc_bq = "";
            String shouhcsl_tq = "";
            String shouhckc_tq = "";
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
					/*if(intyear2==intyear&&intMonth==1){
					shouhcsl_bq="zb.fadl*dc.konggbl as fadl,shc.fady*dc.konggbl as fadhml,shc.gongry*dc.konggbl as gongrhml,shc.qith*dc.konggbl as qithyl,\n" +
					        "shc.sunh*dc.konggbl as chucshl,shc.shuifctz*dc.konggbl shuifctz,shc.panyk*dc.konggbl as panyk,shc.kuc*dc.konggbl as shijkc";
					shouhckc_bq="decode(shc.riq,to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),shc.panyk*dc.konggbl,0) as panyk,\n"+
					        "decode(shc.riq,to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),shc.kuc*dc.konggbl,0) as shijkc";
					shouhcsl_tq="zb.fadl*dc.konggbl as fadl,shc.fady*dc.konggbl as fadhml,shc.gongry*dc.konggbl as gongrhml,shc.qith*dc.konggbl as qithyl,\n" +
			        		"shc.sunh*dc.konggbl as chucshl,shc.shuifctz*dc.konggbl shuifctz,shc.panyk*dc.konggbl as panyk,shc.kuc*dc.konggbl as shijkc";
					shouhckc_tq="decode(shc.riq,add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12),shc.panyk*dc.konggbl,0) as panyk,\n"+
			        "decode(shc.riq,add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12),shc.kuc*dc.konggbl,0) as shijkc";
					}else{*/
                shouhcsl_bq = "sum(zb.fadl*dc.konggbl) as fadl,sum(shc.fady*dc.konggbl) as fadhml,sum(shc.gongry*dc.konggbl) as gongrhml,sum(shc.qith*dc.konggbl) as qithyl,\n" +
                        "sum(shc.sunh*dc.konggbl) as chucshl,sum(shc.shuifctz*dc.konggbl) shuifctz,sum(shc.panyk*dc.konggbl) as panyk,sum(shc.kuc*dc.konggbl) as shijkc";
                shouhckc_bq = "sum(decode(shc.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shc.panyk*dc.konggbl,0)) as panyk,\n" +
                        "sum(decode(shc.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shc.kuc*dc.konggbl,0)) as shijkc";
                shouhcsl_tq = "sum(zb.fadl*dc.konggbl) as fadl,sum(shc.fady*dc.konggbl) as fadhml,sum(shc.gongry*dc.konggbl) as gongrhml,sum(shc.qith*dc.konggbl) as qithyl,\n" +
                        "sum(shc.sunh*dc.konggbl) as chucshl,sum(shc.shuifctz*dc.konggbl) shuifctz,sum(shc.panyk*dc.konggbl) as panyk,sum(shc.kuc*dc.konggbl) as shijkc";
                shouhckc_tq = "sum(decode(shc.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shc.panyk*dc.konggbl,0)) as panyk,\n" +
                        "sum(decode(shc.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shc.kuc*dc.konggbl,0)) as shijkc";
                //}
            } else {
					/*if(intyear2==intyear&&intMonth==1){
						shouhcsl_bq="zb.fadl as fadl,shc.fady as fadhml,shc.gongry as gongrhml,shc.qith as qithyl,\n" +
		                "shc.sunh as chucshl,shc.shuifctz shuifctz,shc.panyk as panyk,shc.kuc as shijkc";
						shouhckc_bq="decode(shc.riq,to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),shc.panyk,0) as panyk,\n"+
				       "decode(shc.riq,to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),shc.kuc,0) as shijkc" ;
						shouhcsl_tq="zb.fadl as fadl,shc.fady as fadhml,shc.gongry as gongrhml,shc.qith as qithyl,\n" +
                		"shc.sunh as chucshl,shc.shuifctz shuifctz,shc.panyk as panyk,shc.kuc as shijkc";
						shouhckc_tq="decode(shc.riq,add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12),shc.panyk,0) as panyk,\n"+
						"decode(shc.riq,add_months(to_date('"+intyear2+"-"+intMonth2+"-01','yyyy-mm-dd'),-12),shc.kuc,0) as shijkc" ;
					}else{*/
                shouhcsl_bq = "sum(zb.fadl) as fadl,sum(shc.fady) as fadhml,sum(shc.gongry) as gongrhml,sum(shc.qith) as qithyl,\n" +
                        "sum(shc.sunh) as chucshl,sum(shc.shuifctz) shuifctz,sum(shc.panyk) as panyk,sum(shc.kuc) as shijkc";
                shouhckc_bq = "sum(decode(shc.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shc.panyk,0)) as panyk,\n" +
                        "sum(decode(shc.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shc.kuc,0)) as shijkc";
                shouhcsl_tq = "sum(zb.fadl) as fadl,sum(shc.fady) as fadhml,sum(shc.gongry) as gongrhml,sum(shc.qith) as qithyl,\n" +
                        "sum(shc.sunh) as chucshl,sum(shc.shuifctz) shuifctz,sum(shc.panyk) as panyk,sum(shc.kuc) as shijkc";
                shouhckc_tq = "sum(decode(shc.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shc.panyk,0)) as panyk,\n" +
                        "sum(decode(shc.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shc.kuc,0)) as shijkc";
                //}

            }

            strSQL = jibreport.getSql6(biaot, dianc, yueshcmzt, shouhcsl_bq, shouhckc_bq, shouhcsl_tq, shouhckc_tq, tiaoj, fenz, dianckjmx_bm, grouping, dianckjmx_tj, strGongsID, zhuangt, yb_zb, yb_shcm, intyear, intMonth, intyear2, intMonth2);

//					ֱ���ֳ�����
            ArrHeader = new String[3][24];
            ArrHeader[0] = new String[]{"��λ����", "������", "������", "ȫ������ԭú��", "ȫ������ԭú��", "���У������ú��", "���У������ú��", "���У����Ⱥ�ú��", "���У����Ⱥ�ú��", "���У�������ú��", "���У�������ú��", "�洢�����", "�洢�����",
                    "ˮ�ֲ����", "ˮ�ֲ����", "ʵ�ʿ��", "ʵ�ʿ��", "������", "������", "���״̬", "���״̬", "���״̬", "���״̬",};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23"};

            ArrWidth = new int[]{140, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 50, 0, 0, 0, 0};
            iFixedRows = 1;
            iCol = 10;
            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��" + titlename, ArrWidth, 4);
            rt.setDefaultTitle(1, 4, "���λ:" + this.getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(6, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(13, 4, "��λ:��ǧ��ʱ���֡���/ǧ��ʱ", Table.ALIGN_CENTER);
            rt.setDefaultTitle(18, 2, "cpiȼ�Ϲ���06��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = true;

//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }

            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 3, "��ӡ����:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), Table.ALIGN_LEFT);
            rt.setDefautlFooter(6, 3, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(11, 2, "�Ʊ�:", Table.ALIGN_LEFT);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);
            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();
            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���07��")) {
            String titlename = "ʯ�͹�Ӧ�������������";
            JDBCcon cn = new JDBCcon();

            String zhuangt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (shcy.zhuangt=1 or shcy.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and shcy.zhuangt=2";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String danwmc = "";//��������
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                guolzj = "";
                strDiancFID = "'',";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
                strDiancFID = this.getTreeid() + ",";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                guolzj = "   and a.fgs=0 --grouping(dc.mingc)=0\n";
                strDiancFID = "'',";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }
            danwmc = getTreeDiancmc(this.getTreeid());

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }

            strzt = "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueshcyb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yueshcyb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueshcyb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yueshcyb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";
            if (jib == 3) {
                biaot =
                        "a.DANWMC,--a.FENX,\n  a.BQGYL,a.TQGYL,a.BQQHYL,a.TQQHYL,a.BQFHL,a.TQFHL,a.BQGHL,a.TQGHL,a.BQQT,a.TQQT,a.BQCC,\n" +
                                "a.TQCC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                " from (select" +
                                " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "-- having not grouping(fx.fenx)=1" + guolzj +
                        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n" +
                        " )a   where a.f=0 and a.dcmc=0";
                grouping = "   ,grouping(dc.fgsmc)  fgs \n";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot =
                            "a.DANWMC,--a.FENX,\n  a.BQGYL,a.TQGYL,a.BQQHYL,a.TQQHYL,a.BQFHL,a.TQFHL,a.BQGHL,a.TQGHL,a.BQQT,a.TQQT,a.BQCC,\n" +
                                    "a.TQCC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from (select" +
                                    //" decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc";
                                    " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc, decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc";
                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1\n" +
                            "  \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n" +
                            " )a   where a.f=0 ";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    biaot =
                            "a.DANWMC,--a.FENX,\n  a.BQGYL,a.TQGYL,a.BQQHYL,a.TQQHYL,a.BQFHL,a.TQFHL,a.BQGHL,a.TQGHL,a.BQQT,a.TQQT,a.BQCC,\n" +
                                    "a.TQCC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from (select" +
                                    " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "-- having not grouping(fx.fenx)=1" + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n" +
                            " )a   where a.f=0 " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot =
                            "a.DANWMC,--a.FENX,\n  a.BQGYL,a.TQGYL,a.BQQHYL,a.TQQHYL,a.BQFHL,a.TQFHL,a.BQGHL,a.TQGHL,a.BQQT,a.TQQT,a.BQCC,\n" +
                                    "a.TQCC,a.BQSKC,a.TQSKC,a.BQZKC,a.TQZKC," + strzt + " \n" +
                                    " from (select" +
                                    " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + guolzj +
                            "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh) \n" +
                            " )a   where a.f=0  and   dcmc=1  " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                }
            }
            String shouhcysl_bq = "";
            String shouhcykc_bq = "";
            String shouhcysl_tq = "";
            String shouhcykc_tq = "";
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {

                shouhcysl_tq = "sum(shcy.shouyl*dc.konggbl) as shiygyl,sum(shcy.fadyy*dc.konggbl) as fadhyl,sum(shcy.gongry*dc.konggbl) as gongrhyl,\n" +
                        "sum(shcy.qithy*dc.konggbl) as qithyl,sum(shcy.sunh*dc.konggbl) as chucshl," +
                        "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.kuc*dc.konggbl,0)) as shijkc," +
                        "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.panyk*dc.konggbl,0)) as panyk";
                shouhcykc_tq = "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.kuc*dc.konggbl,0)) as shijkc," +
                        "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.panyk*dc.konggbl,0)) as panyk";
                shouhcysl_bq = "sum(shcy.shouyl*dc.konggbl) as shiygyl,sum(shcy.fadyy*dc.konggbl) as fadhyl,sum(shcy.gongry*dc.konggbl) as gongrhyl,\n" +
                        "sum(shcy.qithy*dc.konggbl) as qithyl,sum(shcy.sunh*dc.konggbl) as chucshl," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.kuc*dc.konggbl,0)) as shijkc," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.panyk*dc.konggbl,0)) as panyk";
                shouhcykc_bq = "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.kuc*dc.konggbl,0)) as shijkc," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.panyk*dc.konggbl,0)) as panyk";

            } else {

                shouhcysl_bq = "sum(shcy.shouyl) as shiygyl,sum(shcy.fadyy) as fadhyl,sum(shcy.gongry) as gongrhyl,\n" +
                        "sum(shcy.qithy) as qithyl,sum(shcy.sunh) as chucshl," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.kuc,0)) as shijkc," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.panyk,0)) as panyk";
                shouhcykc_bq = "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.kuc,0)) as shijkc," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.panyk,0)) as panyk";
                shouhcysl_tq = "sum(shcy.shouyl) as shiygyl,sum(shcy.fadyy) as fadhyl,sum(shcy.gongry) as gongrhyl,\n" +
                        "sum(shcy.qithy) as qithyl,sum(shcy.sunh) as chucshl," +
                        "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.kuc,0)) as shijkc," +
                        "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.panyk,0)) as panyk";
                shouhcykc_tq = "sum(decode(shcy.riq,add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),shcy.kuc,0)) as shijkc," +
                        "sum(decode(shcy.riq,to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),shcy.panyk,0)) as panyk";

            }

            strSQL = jibreport.getSql7(biaot, dianc, tiaoj, grouping, shouhcysl_bq, shouhcykc_bq, fenz, dianckjmx_bm, dianckjmx_tj, shouhcysl_tq, shouhcykc_tq, strGongsID, zhuangt, yb_shcy, intyear, intMonth, intyear2, intMonth2);

//					ֱ���ֳ�����
            ArrHeader = new String[3][21];
            ArrHeader[0] = new String[]{"��λ����", "ʯ�͹�Ӧ��", "ʯ�͹�Ӧ��", "ȫ������ԭ����", "ȫ������ԭ����", "���У����������", "���У����������", "���Ⱥ�����", "���Ⱥ�����", "����������", "����������",
                    "���������", "���������", "ʵ�ʿ��", "ʵ�ʿ��", "������", "������", "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};

            ArrWidth = new int[]{150, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 0, 0, 0, 0};

            iFixedRows = 1;
            iCol = 10;

            ResultSet rs = cn.getResultSet(strSQL);

            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��" + titlename, ArrWidth, 4);
            rt.setDefaultTitle(1, 2, "���λ:" + this.getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(8, 4, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(13, 2, "��λ:��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(16, 2, "cpiȼ�Ϲ���07��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;

//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }
            //ҳ��
            rt.createDefautlFooter(ArrWidth);

            rt.setDefautlFooter(1, 3, "��ӡ����:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), Table.ALIGN_LEFT);
            rt.setDefautlFooter(6, 3, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(11, 4, "�Ʊ�:", Table.ALIGN_LEFT);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            rt.setDefautlFooter(rt.footer.getCols() - 4, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_LEFT);


            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();
            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���08��")) {

            JDBCcon cn = new JDBCcon();
            String zhuangt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String notHuiz = "";
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                strDiancFID = "'',";
                guolzj = "";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                guolzj = " and grouping(dc.fgsmc)=0\n";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���
                strDiancFID = this.getTreeid() + ",";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                strDiancFID = "'',";
                guolzj = " and grouping(dc.mingc)=0\n";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }
            String diancdwmc = "";

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }
            if (jib == 3) {
                diancdwmc = "'',dc.fgsmc,dc.mingc,";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    //diancdwmc="dq.mingc,'',dc.mingc,";
                    diancdwmc = "decode(" + jib + ",2,'',dq.mingc),decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")),dc.mingc,";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    diancdwmc = "'',dc.fgsmc,dc.mingc,";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    diancdwmc = "'',dc.fgsmc,'',";
                }
            }
            strzt = "getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuezbb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";
            if (jib == 3) {
                biaot = "decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
                        "      --fx.fenx as fenx,\n  sum(bq.quanbhybml) as bq_quanbhybml,sum(tq.quanbhybml) as tq_quanbhybml,\n" +
                        "       sum(bq.fadhmzbml) as bq_fadhmzbml,sum(tq.fadhmzbml) as tq_fadhmzbml,\n" +
                        "       sum(bq.gongrhmzbml) as bq_gongrhmzbml,sum(tq.gongrhmzbml) as tq_gongrhmzbml,\n" +
                        "       sum(bq.fadhyzbml) as bq_fadhyzbml,sum(tq.fadhyzbml) as tq_fadhyzbml,\n" +
                        "       sum(bq.gongrhyzbml) as bq_gongrhyzbml,sum(tq.gongrhyzbml) as tq_gongrhyzbml,\n" +
                        "       decode(sum(bq.fadl),0,0, Round((sum(bq.fadhmzbml)+sum(bq.fadhyzbml))*100/sum(bq.fadl),1)) as bq_fadbmhl,\n" +
                        "       decode(sum(tq.fadl),0,0, Round((sum(tq.fadhmzbml)+sum(tq.fadhyzbml))*100/sum(tq.fadl),1)) as tq_fadbmhl,\n" +
                        "       sum(bq.gongrl) as bq_gongrl,sum(tq.gongrl) as tq_gongrl,\n" +
                        "       decode(sum(bq.gongrl),0,0,Round(((sum(bq.gongrhmzbml)+sum(bq.gongrhyzbml))/sum(bq.gongrl)*1000),2)) as bq_gongrbmhl,\n" +
                        "       decode(sum(tq.gongrl),0,0,Round(((sum(tq.gongrhmzbml)+sum(tq.gongrhyzbml))/sum(tq.gongrl)*1000),2)) as tq_gongrbmhl," + strzt + "\n";

                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "having not grouping(fx.fenx)=1" + guolzj +
                        "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot = "decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
                            "      --fx.fenx as fenx,\n  sum(bq.quanbhybml) as bq_quanbhybml,sum(tq.quanbhybml) as tq_quanbhybml,\n" +
                            "       sum(bq.fadhmzbml) as bq_fadhmzbml,sum(tq.fadhmzbml) as tq_fadhmzbml,\n" +
                            "       sum(bq.gongrhmzbml) as bq_gongrhmzbml,sum(tq.gongrhmzbml) as tq_gongrhmzbml,\n" +
                            "       sum(bq.fadhyzbml) as bq_fadhyzbml,sum(tq.fadhyzbml) as tq_fadhyzbml,\n" +
                            "       sum(bq.gongrhyzbml) as bq_gongrhyzbml,sum(tq.gongrhyzbml) as tq_gongrhyzbml,\n" +
                            "       decode(sum(bq.fadl),0,0, Round((sum(bq.fadhmzbml)+sum(bq.fadhyzbml))*100/sum(bq.fadl),1)) as bq_fadbmhl,\n" +
                            "       decode(sum(tq.fadl),0,0, Round((sum(tq.fadhmzbml)+sum(tq.fadhyzbml))*100/sum(tq.fadl),1)) as tq_fadbmhl,\n" +
                            "       sum(bq.gongrl) as bq_gongrl,sum(tq.gongrl) as tq_gongrl,\n" +
                            "       decode(sum(bq.gongrl),0,0,Round(((sum(bq.gongrhmzbml)+sum(bq.gongrhyzbml))/sum(bq.gongrl)*1000),2)) as bq_gongrbmhl,\n" +
                            "       decode(sum(tq.gongrl),0,0,Round(((sum(tq.gongrhmzbml)+sum(tq.gongrhyzbml))/sum(tq.gongrl)*1000),2)) as tq_gongrbmhl," + strzt + "\n";

                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1\n" +
                            "order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    biaot = "decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
                            "      --fx.fenx as fenx,\n sum(bq.quanbhybml) as bq_quanbhybml,sum(tq.quanbhybml) as tq_quanbhybml,\n" +
                            "       sum(bq.fadhmzbml) as bq_fadhmzbml,sum(tq.fadhmzbml) as tq_fadhmzbml,\n" +
                            "       sum(bq.gongrhmzbml) as bq_gongrhmzbml,sum(tq.gongrhmzbml) as tq_gongrhmzbml,\n" +
                            "       sum(bq.fadhyzbml) as bq_fadhyzbml,sum(tq.fadhyzbml) as tq_fadhyzbml,\n" +
                            "       sum(bq.gongrhyzbml) as bq_gongrhyzbml,sum(tq.gongrhyzbml) as tq_gongrhyzbml,\n" +
                            "       decode(sum(bq.fadl),0,0, Round((sum(bq.fadhmzbml)+sum(bq.fadhyzbml))*100/sum(bq.fadl),1)) as bq_fadbmhl,\n" +
                            "       decode(sum(tq.fadl),0,0, Round((sum(tq.fadhmzbml)+sum(tq.fadhyzbml))*100/sum(tq.fadl),1)) as tq_fadbmhl,\n" +
                            "       sum(bq.gongrl) as bq_gongrl,sum(tq.gongrl) as tq_gongrl,\n" +
                            "       decode(sum(bq.gongrl),0,0,Round(((sum(bq.gongrhmzbml)+sum(bq.gongrhyzbml))/sum(bq.gongrl)*1000),2)) as bq_gongrbmhl,\n" +
                            "       decode(sum(tq.gongrl),0,0,Round(((sum(tq.gongrhmzbml)+sum(tq.gongrhyzbml))/sum(tq.gongrl)*1000),2)) as tq_gongrbmhl," + strzt + "\n";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1" + guolzj +
                            "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot = "decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,\n" +
                            "      --fx.fenx as fenx,\n sum(bq.quanbhybml) as bq_quanbhybml,sum(tq.quanbhybml) as tq_quanbhybml,\n" +
                            "       sum(bq.fadhmzbml) as bq_fadhmzbml,sum(tq.fadhmzbml) as tq_fadhmzbml,\n" +
                            "       sum(bq.gongrhmzbml) as bq_gongrhmzbml,sum(tq.gongrhmzbml) as tq_gongrhmzbml,\n" +
                            "       sum(bq.fadhyzbml) as bq_fadhyzbml,sum(tq.fadhyzbml) as tq_fadhyzbml,\n" +
                            "       sum(bq.gongrhyzbml) as bq_gongrhyzbml,sum(tq.gongrhyzbml) as tq_gongrhyzbml,\n" +
                            "       decode(sum(bq.fadl),0,0, Round((sum(bq.fadhmzbml)+sum(bq.fadhyzbml))*100/sum(bq.fadl),1)) as bq_fadbmhl,\n" +
                            "       decode(sum(tq.fadl),0,0, Round((sum(tq.fadhmzbml)+sum(tq.fadhyzbml))*100/sum(tq.fadl),1)) as tq_fadbmhl,\n" +
                            "       sum(bq.gongrl) as bq_gongrl,sum(tq.gongrl) as tq_gongrl,\n" +
                            "       decode(sum(bq.gongrl),0,0,Round(((sum(bq.gongrhmzbml)+sum(bq.gongrhyzbml))/sum(bq.gongrl)*1000),2)) as bq_gongrbmhl,\n" +
                            "       decode(sum(tq.gongrl),0,0,Round(((sum(tq.gongrhmzbml)+sum(tq.gongrhyzbml))/sum(tq.gongrl)*1000),2)) as tq_gongrbmhl," + strzt + "\n";

                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + guolzj +
                            "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                }
            }
            String zhibbsl = "";
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                zhibbsl = "(sum(y.RULMZBZML*dc.konggbl) + sum(y.RULYZBZML*dc.konggbl)) as quanbhybml,sum(y.FADMZBML*dc.konggbl) as fadhmzbml,\n" +
                        "sum(y.GONGRMZBML*dc.konggbl) as gongrhmzbml,sum(y.FADYZBZML*dc.konggbl) as fadhyzbml,\n" +
                        "sum(y.GONGRYZBZML*dc.konggbl) as gongrhyzbml,sum(y.FADL) as fadl,\n" +
                        "decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML*dc.konggbl) + sum(y.FADYZBZML*dc.konggbl)) /sum(y.FADL) * 100),2)) as fadbmhl,sum(y.GONGRL*dc.konggbl) as gongrl,\n" +
                        "decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML*dc.konggbl) + sum(y.GONGRYZBZML*dc.konggbl)) /sum(y.GONGRL*dc.konggbl) * 1000),2)) as gongrbmhl";
            } else {
                zhibbsl = "(sum(y.RULMZBZML) + sum(y.RULYZBZML)) as quanbhybml,sum(y.FADMZBML) as fadhmzbml,\n" +
                        "sum(y.GONGRMZBML) as gongrhmzbml,sum(y.FADYZBZML) as fadhyzbml,\n" +
                        "sum(y.GONGRYZBZML) as gongrhyzbml,sum(y.FADL) as fadl,\n" +
                        "decode(sum(y.FADL),0,0,Round(((sum(y.FADMZBML) + sum(y.FADYZBZML)) /sum(y.FADL) * 100),2)) as fadbmhl,sum(y.GONGRL) as gongrl,\n" +
                        "decode(sum(y.GONGRL),0,0,Round(((sum(y.GONGRMZBML) + sum(y.GONGRYZBZML)) /sum(y.GONGRL) * 1000),2)) as gongrbmhl";
            }

            strSQL = jibreport.getSql8(biaot, dianc, tiaoj, fenz, dianckjmx_bm, dianckjmx_tj, zhibbsl, strGongsID, zhuangt, yb_zb, intyear, intMonth, intyear2, intMonth2);
//	 ֱ���ֳ�����
				/*ArrHeader=new String[3][22];
				ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","ȫ�����ñ�ú��","ȫ�����ñ�ú��","����:�����ú�۱�ú��","����:�����ú�۱�ú��","����:���Ⱥ�ú�۱�ú��","����:���Ⱥ�ú�۱�ú��",
							                    "��������۱�ú��","��������۱�ú��","���Ⱥ����۱�ú��","���Ⱥ����۱�ú��","�����ú����","�����ú����","������","������","���ȱ�ú����","���ȱ�ú����",
							                    "���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};

				ArrWidth=new int[] {150,40,55,55,65,65,65,65,55,55,55,55,55,55,55,55,55,55,0,0,0,0};
				iFixedRows=1;
				String arrFormat[]=new String[]{"","","0","0","0","0","0","0","0","0","0","0","0.0","0.0","0","0","0.00","0.00","","","",""};*/

            ArrHeader = new String[3][21];
            ArrHeader[0] = new String[]{"��λ����", "ȫ�����ñ�ú��", "ȫ�����ñ�ú��", "����:�����ú�۱�ú��", "����:�����ú�۱�ú��", "����:���Ⱥ�ú�۱�ú��", "����:���Ⱥ�ú�۱�ú��",
                    "��������۱�ú��", "��������۱�ú��", "���Ⱥ����۱�ú��", "���Ⱥ����۱�ú��", "�����ú����", "�����ú����", "������", "������", "���ȱ�ú����", "���ȱ�ú����",
                    "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};

            ArrWidth = new int[]{150, 55, 55, 65, 65, 65, 65, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 0, 0, 0, 0};
            iFixedRows = 1;
            String arrFormat[] = new String[]{"", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0.0", "0.0", "0", "0", "0.00", "0.00"};

            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "�±�ú���������", ArrWidth, 4);
            rt.setDefaultTitle(1, 4, "���λ(����):" + ((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(6, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(10, 4, "��λ:�֡���/ǧ��ʱ��ǧ��/����", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(15, 3, "cpiȼ�Ϲ���08��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;
//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rt.body.setColFormat(arrFormat);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }

            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
            rt.setDefautlFooter(6, 3, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(11, 3, "�Ʊ�:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);


            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();

            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���09��")) {
            String titlename = "ú(��)���������";
            JDBCcon cn = new JDBCcon();
            String zhuangt = "";
            String shulzt = "";
            String zhilzt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
                shulzt = "";
                zhilzt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (zb.zhuangt=1 or zb.zhuangt=2) ";
                shulzt = " and (sl.zhuangt=1 or sl.zhuangt=2) ";
                zhilzt = " and (zl.zhuangt=1 or zl.zhuangt=2) ";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and zb.zhuangt=2 ";
                shulzt = " and sl.zhuangt=2 ";
                zhilzt = " and zl.zhuangt=2 ";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String danwmc = "";//��������
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                guolzj = "";
                strDiancFID = "'',";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid=  " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
                strDiancFID = this.getTreeid() + ",";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                guolzj = "   and a.fgs=0 --grouping(dc.mingc)=0\n";
                strDiancFID = "'',";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";

            }
            danwmc = getTreeDiancmc(this.getTreeid());

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }

            strzt = "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'rucycbb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'rucycbb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'rucycbb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc1,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'rucycbb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";
            if (jib == 3) {
                biaot = "a.DANWMC,--a.FENX,\n  a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\n" +
                        "a.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + strzt + "  from (select" +
                        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1" + guolzj +
                        " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                        " )a   where a.f=0 and a.dcmc=0";
                grouping = "   ,grouping(dc.fgsmc)  fgs \n";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot = "a.DANWMC,--a.FENX,\n  a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\n" +
                            "a.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + strzt + "  from (select \n" +
                            //" decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,dq.mingc as dqmc,'' as fgsmc,dc.mingc as diancmc1";
                            "decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc1";
                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1\n" +
                            " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0 ";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    biaot = "a.DANWMC,--a.FENX,\n  a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\n" +
                            "a.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + strzt + "  from (select" +
                            " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1" + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0 " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot = "a.DANWMC,--a.FENX,\n  a.RUC_FRL_BQ,a.RUC_FRL_TQ,a.RUC_FRL_ZD_BQ,a.RUC_FRL_ZD_TQ,a.RUC_FRL_SC_BQ,\n" +
                            "a.RUC_FRL_SC_TQ,a.RUL_FRL_BQ,a.RUL_FRL_TQ,a.REZC_BQ,a.REZC_TQ,a.SY_FARL_BQ,a.SY_FARL_TQ," + strzt + "  from (select" +
                            " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc1";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0  and   dcmc=1  " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                }
            }
            String shul = "";
            String strZhib_bq = "";
            String strZhib_tq = "";
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                shul = "decode(sum(sl.laimsl),0,0,sum((sl.laimsl*dc.konggbl) *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl*dc.konggbl)) as rcfal,\n" +
                        "sum(sl.laimsl*dc.konggbl) as laimsl";
                if (intyear2 == intyear && intMonth == 1) {//ȡ�ۼ�ֵ
                    strZhib_bq = "select zb.diancxxb_id,sum(zb.FADGRYTRML*dc.konggbl) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz\n" +
                            "         from  " + yb_zb + " zb,diancxxb dc,\n" +
                            "     (select rcy.diancxxb_id,sum(rcy.shul*dc.konggbl) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul*dc.konggbl) * rcy.youfrl) /sum(rcy.shul*dc.konggbl)) as rucyrl\n" +
                            "       from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') and rcy.fenx='�ۼ�'\n" +
                            "          and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" +
                            "         where zb.riq=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" +
                            "         group by (zb.diancxxb_id)";
                    strZhib_tq = "select zb.diancxxb_id,sum(zb.FADGRYTRML*dc.konggbl) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz\n" +
                            "         from  " + yb_zb + " zb,diancxxb dc,\n" +
                            "     (select rcy.diancxxb_id,sum(rcy.shul*dc.konggbl) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul*dc.konggbl) * rcy.youfrl) /sum(rcy.shul*dc.konggbl)) as rucyrl\n" +
                            "       from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) and rcy.fenx='�ۼ�'\n" +
                            "          and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" +
                            "         where zb.riq=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" +
                            "         group by (zb.diancxxb_id)";
                } else {//��Ȩ
                    strZhib_bq = "select dc.id as diancxxb_id,zb.rulml as rulml,c.rucysl as rulyl,\n" +
                            "     round_new(c.rucyrl, 2) as rulyrz,round_new(zb.RULTRMPJFRL / 1000, 2) as rulmrz \n" +
                            " from  \n" +
                            "( select zb.diancxxb_id,sum(zb.FADGRYTRML*dc.konggbl) as rulml,\n" +
                            "decode(sum(zb.fadgrytrml),0,0,sum(zb.RULTRMPJFRL * zb.fadgrytrml*dc.konggbl) /sum(zb.fadgrytrml*dc.konggbl)) as RULTRMPJFRL \n" +
                            "from  " + yb_zb + " zb,diancxxb dc \n" +
                            "where zb.diancxxb_id=dc.id and \n" +
                            "zb.riq >= to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.riq <= to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') \n" +
                            "and zb.fenx='����' and zb.zhuangt=2\n" +
                            "group by (zb.diancxxb_id) \n" +
                            ")zb, \n" +
                            "(select rcy.diancxxb_id,sum(rcy.shul*dc.konggbl) as rucysl,\n" +
                            " decode(sum(rcy.shul),0,0,sum(rcy.youfrl * rcy.shul*dc.konggbl) / sum(rcy.shul*dc.konggbl)) as rucyrl\n" +
                            " from  " + yb_rcy + " rcy, diancxxb dc\n" +
                            " where rcy.riq>=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.riq<=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') \n" +
                            "     and rcy.fenx = '����' and rcy.diancxxb_id = dc.id\n" +
                            "     group by (rcy.diancxxb_id)\n" +
                            ")c,\n" +
                            "diancxxb dc\n" +
                            "where \n" +
                            "zb.diancxxb_id(+) = dc.id and c.diancxxb_id(+) = dc.id and nvl(zb.rulml, 0) + nvl(c.rucysl, 0) <> 0\n";
                    strZhib_tq = "select dc.id as diancxxb_id,zb.rulml as rulml,c.rucysl as rulyl,\n" +
                            "     round_new(c.rucyrl, 2) as rulyrz,round_new(zb.RULTRMPJFRL / 1000, 2) as rulmrz \n" +
                            " from  \n" +
                            "( select zb.diancxxb_id,sum(zb.FADGRYTRML*dc.konggbl) as rulml,\n" +
                            "decode(sum(zb.fadgrytrml),0,0,sum(zb.RULTRMPJFRL * zb.fadgrytrml*dc.konggbl) /sum(zb.fadgrytrml*dc.konggbl)) as RULTRMPJFRL \n" +
                            "from  " + yb_zb + " zb,diancxxb dc \n" +
                            "where zb.diancxxb_id=dc.id and \n" +
                            "zb.riq >= add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.riq <= add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) \n" +
                            "and zb.fenx='����' and zb.zhuangt=2\n" +
                            "group by (zb.diancxxb_id) \n" +
                            ")zb, \n" +
                            "(select rcy.diancxxb_id,sum(rcy.shul*dc.konggbl) as rucysl,\n" +
                            " decode(sum(rcy.shul),0,0,sum(rcy.youfrl * rcy.shul*dc.konggbl) / sum(rcy.shul*dc.konggbl)) as rucyrl\n" +
                            " from  " + yb_rcy + " rcy, diancxxb dc\n" +
                            " where rcy.riq>=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.riq<=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) \n" +
                            "     and rcy.fenx = '����' and rcy.diancxxb_id = dc.id\n" +
                            "     group by (rcy.diancxxb_id)\n" +
                            ")c,\n" +
                            "diancxxb dc\n" +
                            "where \n" +
                            "zb.diancxxb_id(+) = dc.id and c.diancxxb_id(+) = dc.id and nvl(zb.rulml, 0) + nvl(c.rucysl, 0) <> 0\n";
                }
            } else {
                shul = "decode(sum(sl.laimsl),0,0,sum(sl.laimsl *decode(dc.id,141,zl.diancrz,zl.qnet_ar)) /sum(sl.laimsl)) as rcfal,\n" +
                        "sum(sl.laimsl) as laimsl";
                if (intyear2 == intyear && intMonth == 1) {//ȡ�ۼ�ֵ
                    strZhib_bq = "select zb.diancxxb_id,sum(zb.FADGRYTRML) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz\n" +
                            "         from  " + yb_zb + " zb,diancxxb dc,\n" +
                            "     (select rcy.diancxxb_id,sum(rcy.shul) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul) * rcy.youfrl) /sum(rcy.shul)) as rucyrl\n" +
                            "       from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') and rcy.fenx='�ۼ�'\n" +
                            "          and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" +
                            "         where zb.riq=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" +
                            "         group by (zb.diancxxb_id)";
                    strZhib_tq = "select zb.diancxxb_id,sum(zb.FADGRYTRML) as rulml,sum(c.rucysl) as rulyl,sum(c.rucyrl) as rulyrz,sum(zb.RULTRMPJFRL / 1000) as rulmrz\n" +
                            "         from  " + yb_zb + " zb,diancxxb dc,\n" +
                            "     (select rcy.diancxxb_id,sum(rcy.shul) as rucysl,decode(sum(rcy.shul),0,0,sum((rcy.shul) * rcy.youfrl) /sum(rcy.shul)) as rucyrl\n" +
                            "       from  " + yb_rcy + " rcy ,diancxxb dc where rcy.riq=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) and rcy.fenx='�ۼ�'\n" +
                            "          and rcy.diancxxb_id=dc.id  " + strGongsID + "   group by (rcy.diancxxb_id) ) c\n" +
                            "         where zb.riq=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) and zb.fenx='�ۼ�'and zb.diancxxb_id=dc.id  " + strGongsID + zhuangt + " and zb.diancxxb_id=c.diancxxb_id(+)\n" +
                            "         group by (zb.diancxxb_id)";
                } else {//��Ȩ
                    strZhib_bq = "select dc.id as diancxxb_id,zb.rulml as rulml,c.rucysl as rulyl,\n" +
                            "     round_new(c.rucyrl, 2) as rulyrz,round_new(zb.RULTRMPJFRL / 1000, 2) as rulmrz \n" +
                            " from  \n" +
                            "( select zb.diancxxb_id,sum(zb.FADGRYTRML) as rulml,\n" +
                            "decode(sum(zb.fadgrytrml),0,0,sum(zb.RULTRMPJFRL * zb.fadgrytrml) /sum(zb.fadgrytrml)) as RULTRMPJFRL \n" +
                            "from  " + yb_zb + " zb ,diancxxb dc\n" +
                            "where zb.diancxxb_id=dc.id and \n" +
                            "zb.riq >= to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and zb.riq <= to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') \n" +
                            "and zb.fenx='����' and zb.zhuangt=2\n" +
                            "group by (zb.diancxxb_id) \n" +
                            ")zb, \n" +
                            "(select rcy.diancxxb_id,sum(rcy.shul) as rucysl,\n" +
                            " decode(sum(rcy.shul),0,0,sum(rcy.shul * rcy.youfrl) / sum(rcy.shul)) as rucyrl\n" +
                            " from  " + yb_rcy + " rcy, diancxxb dc\n" +
                            " where rcy.riq>=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') and rcy.riq<=to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd') \n" +
                            "     and rcy.fenx = '����' and rcy.diancxxb_id = dc.id\n" +
                            "     group by (rcy.diancxxb_id)\n" +
                            ")c,\n" +
                            "diancxxb dc\n" +
                            "where \n" +
                            "zb.diancxxb_id(+) = dc.id and c.diancxxb_id(+) = dc.id and nvl(zb.rulml, 0) + nvl(c.rucysl, 0) <> 0\n";
                    strZhib_tq = "select dc.id as diancxxb_id,zb.rulml as rulml,c.rucysl as rulyl,\n" +
                            "     round_new(c.rucyrl, 2) as rulyrz,round_new(zb.RULTRMPJFRL / 1000, 2) as rulmrz \n" +
                            " from  \n" +
                            "( select zb.diancxxb_id,sum(zb.FADGRYTRML) as rulml,\n" +
                            "decode(sum(zb.fadgrytrml),0,0,sum(zb.RULTRMPJFRL * zb.fadgrytrml) /sum(zb.fadgrytrml)) as RULTRMPJFRL \n" +
                            "from  " + yb_zb + " zb ,diancxxb dc\n" +
                            "where zb.diancxxb_id=dc.id and \n" +
                            "zb.riq >= add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and zb.riq <= add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) \n" +
                            "and zb.fenx='����' and zb.zhuangt=2\n" +
                            "group by (zb.diancxxb_id) \n" +
                            ")zb, \n" +
                            "(select rcy.diancxxb_id,sum(rcy.shul) as rucysl,\n" +
                            " decode(sum(rcy.shul),0,0,sum(rcy.shul * rcy.youfrl) / sum(rcy.shul)) as rucyrl\n" +
                            " from  " + yb_rcy + " rcy, diancxxb dc\n" +
                            " where rcy.riq>=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12) and rcy.riq<=add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12) \n" +
                            "     and rcy.fenx = '����' and rcy.diancxxb_id = dc.id\n" +
                            "     group by (rcy.diancxxb_id)\n" +
                            ")c,\n" +
                            "diancxxb dc\n" +
                            "where \n" +
                            "zb.diancxxb_id(+) = dc.id and c.diancxxb_id(+) = dc.id and nvl(zb.rulml, 0) + nvl(c.rucysl, 0) <> 0\n";
                }
            }

            strSQL = jibreport.getSql9(biaot, dianc, tiaoj, grouping, fenz, dianckjmx_bm, dianckjmx_tj, shul, strZhib_bq, strZhib_tq, strGongsID, shulzt, zhilzt, zhuangt, yb_sl, yb_zl, yb_zb, yb_rcy, intyear, intMonth, intyear2, intMonth2);

            //				ֱ���ֳ�����
				/*ArrHeader =new String[3][18];
				ArrHeader[0]=new String[] {"��λ����","��λ����","�볧úƽ����λ������","�볧úƽ����λ������","���У��ص㶩��","���У��ص㶩��","���У��г��ɹ�","���У��г��ɹ�","��¯úƽ����λ������","��¯úƽ����λ������",
								                     "�볧����¯��ֵ��","�볧����¯��ֵ��","��Ȼ��ƽ��������","��Ȼ��ƽ��������","���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","��λ����","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};

				ArrWidth=new int[] {150,60,70,70,70,70,70,70,70,70,70,70,70,70,0,0,0,0};*/
            ArrHeader = new String[3][17];
            ArrHeader[0] = new String[]{"��λ����", "�볧úƽ����λ������", "�볧úƽ����λ������", "���У��ص㶩��", "���У��ص㶩��", "���У��г��ɹ�", "���У��г��ɹ�", "��¯úƽ����λ������", "��¯úƽ����λ������",
                    "�볧����¯��ֵ��", "�볧����¯��ֵ��", "��Ȼ��ƽ��������", "��Ȼ��ƽ��������", "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

            ArrWidth = new int[]{150, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 0, 0, 0};

            iFixedRows = 1;
            iCol = 10;

            ResultSet rs = cn.getResultSet(strSQL);
            System.out.println(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��" + titlename, ArrWidth, 4);
            rt.setDefaultTitle(1, 2, "���λ:" + this.getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(6, 2, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(9, 2, "��λ:ǧ��/ǧ��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(12, 2, "cpiȼ�Ϲ���09��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;
//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }
            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 2, "��ӡ����:" + FormatDate(DateUtil.getDate(DateUtil.FormatDate(new Date()))), Table.ALIGN_LEFT);
            rt.setDefautlFooter(4, 3, "���:", Table.ALIGN_CENTER);
            rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);

            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();
            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���10��")) {

            JDBCcon cn = new JDBCcon();
            String zhuangt = "";
            String shulzt = "";
            String zhilzt = "";
            String youzt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
                shulzt = "";
                zhilzt = "";
                youzt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
                shulzt = " and (sl.zhuangt=1 or sl.zhuangt=2)";
                zhilzt = " and (zl.zhuangt=1 or zl.zhuangt=2)";
                youzt = " and (r.zhuangt=1 or r.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
                shulzt = " and sl.zhuangt=2";
                zhilzt = " and zl.zhuangt=2";
                youzt = " and r.zhuangt=2";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String notHuiz = "";
            String notHuiz1 = "";
            String fenz_bt = "";
            String where = "";
            String tj = "";
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                strDiancFID = "'',";
                notHuiz = "";
                notHuiz1 = "";
                guolzj = "";
                tj = "  grouping(fx.fenx)f,  grouping(dc.mingc)mc,  grouping(dc.fgsmc)fgs,";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid= " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                strDiancFID = this.getTreeid() + ",";
                notHuiz1 = " having grouping(dc.fgsmc)=0";
                notHuiz = " and a.fgs=0 ";
                guolzj = " and grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
                fenz_bt = " grouping(dc.mingc)mc, grouping(dc.fgsmc)fgs,";
                where = "  where c.fgs+c.mc=1 ";
                tj = "  grouping(fx.fenx)f,  grouping(dc.mingc)mc,  grouping(dc.fgsmc)fgs,";
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                strDiancFID = "'',";
                notHuiz1 = "having not( grouping(dc.fgsmc) =1 )";
                notHuiz = " and a.mc=0";
                guolzj = " and grouping(dc.mingc)=0\n";
                fenz_bt = " grouping(dc.mingc)mc, ";
                where = "  where  c.mc=0 ";
                tj = "  grouping(fx.fenx)f,  grouping(dc.mingc)mc, ";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
                fenz_bt = " grouping(dc.mingc)mc ";
                where = "  where  c.mc=1 ";
                tj = "  grouping(fx.fenx)f,  grouping(dc.mingc)mc ,";
            }

            String mc = "";
            String jihkj = "";
            String rucycbb_bq = "";
            String rucycbb_tq = "";
            String rucycbb_tj = "";
            String rucycbb_bt = "";
            String fx = "";
            String biaot0 = "";
            String diancdwmc = "";
            String shul = "";
            String strYouzb = "";

            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }

            if (getBaoblxValue2().getValue().equals("�糧���ھ�ͳ��")) {
                diancdwmc = "dqmc,fgsmc,diancmc1,";
            } else {
                diancdwmc = "dqmc,fgsmc,diancmc0,";
            }

            strzt = "nvl(getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuercbmdj','����'" + "," + visit.getRenyjb() + "),0)as bqby,\n" +
                    "nvl(getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuercbmdj','�ۼ�'" + "," + visit.getRenyjb() + "),0) as bqlj,\n" +
                    "nvl(getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuercbmdj','����'" + "," + visit.getRenyjb() + "),0) as tqby,\n" +
                    "nvl(getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuercbmdj','�ۼ�'" + "," + visit.getRenyjb() + "),0) as tqlj\n";

            if (getBaoblxValue2().getValue().equals("�糧���ھ�ͳ��")) {
                biaot0 = "select   c.aa,--c.fenx,\n  (bq_chebj+bq_yunj+bq_zaf)bq_daoczhj,(tq_chebj+tq_yunj+tq_zaf)tq_daoczhj,bq_chebj,tq_chebj,\n" +
                        "bq_yunj,tq_yunj,bq_zaf,tq_zaf,bq_rucbmdj,tq_rucbmdj,a.tianrypjdj,b.tianrypjdj, \n";
                if (jib == 3) {
                    biaot = "case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
                            "  case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
                            "  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end  end aa,'' as dqmc,dc.mingc as diancmc1,dc.fgsmc as fgsmc,";
                    mc = "dc.fgsmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n"
                            + "    \n"
                            + "order by\n"
                            + "    decode(grouping(dc.mingc)+grouping(dc.fgsmc)+grouping(fx.kouj),3,3,0) desc,\n"
                            + "    decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,2,0) desc,\n"
                            //+ "    min(dc.fgsmc), dc.fgsxh,dc.fgsmc,\n"
                            + "    max(dc.fgsxh),dc.fgsmc,\n"
                            + "    grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n"
                            + "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";
                } else {
                    if (getBaoblxValue().getValue().equals("������ͳ��")) {
                        biaot = "case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
                                "        case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
                                "        case when grouping(dq.mingc)=0 then dq.mingc||'�ϼ�' else '�ܼ�' end end  end aa,decode(grouping(dc.mingc),1,dq.mingc,decode(" + jib + ",2,'',dq.mingc)) as dqmc,decode(" + jib + ",2,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) as fgsmc,dc.mingc as diancmc1,";
                        mc = "dq.mingc";
                        dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                        tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                        fenz = "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dq.mingc,fx.fenx),(dq.mingc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n" +
                                "order by  decode(grouping(dc.mingc)+grouping(dq.mingc)+grouping(fx.kouj),3,3,0) desc,\n" +
                                "   decode(grouping(dc.mingc)+grouping(dq.mingc),2,2,0) desc,\n" +
                                "   min(dq.xuh), dq.mingc,\n" +
                                "   grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n" +
                                "   grouping(fx.kouj) desc,fx.kouj ,\n" +
                                "   fx.fenx";
                        fenz_bt = "";
                        where = "";

                    } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                        biaot = "case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
                                "  case when grouping(dc.mingc)=0 then '&nbsp;&nbsp;'||dc.mingc else\n" +
                                "  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end  end aa,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc1,";
                        mc = "dc.fgsmc";
                        dianc = " vwdianc dc \n";
                        tiaoj = "";
                        fenz = "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx),(dc.xuh,dc.mingc,fx.fenx),(dc.xuh,dc.mingc,fx.kouj,fx.fenx))\n"
                                + "    \n"
                                + "order by\n"
                                + "    decode(grouping(dc.mingc)+grouping(dc.fgsmc)+grouping(fx.kouj),3,3,0) desc,\n"
                                + "    decode(grouping(dc.mingc)+grouping(dc.fgsmc),2,2,0) desc,\n"
                                //+ "    min(dc.fgsmc), dc.fgsxh,dc.fgsmc,\n"
                                + "    max(dc.fgsxh),dc.fgsmc,\n"
                                + "    grouping(dc.mingc) desc,dc.xuh,dc.mingc,\n"
                                + "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";

                    } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                        biaot = "case when grouping(fx.kouj)=0 then '&nbsp;&nbsp;&nbsp;&nbsp;'||max(j.mingc) else\n" +
                                "  case when grouping(dc.fgsmc)=0 then dc.fgsmc else '�ܼ�' end end aa,'' as dqmc,dc.fgsmc as fgsmc,'' as diancmc1,";
                        mc = "dc.fgsmc";
                        dianc = " vwdianc dc \n";
                        tiaoj = "";
                        fenz = "group by  grouping sets  (fx.fenx,(fx.kouj,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.fenx),(dc.fgsxh,dc.fgsmc,fx.kouj,fx.fenx))\n"
                                + " " + notHuiz1 +
                                "\n"
                                + "order by\n"
                                + "    decode(grouping(dc.fgsmc)+grouping(fx.kouj),2,2,0) desc,\n"
                                + "    decode(grouping(dc.fgsmc),1,1,0) desc,\n"
                                //+ "    min(dc.fgsmc), dc.fgsxh,dc.fgsmc,\n"
                                + "    max(dc.fgsxh),dc.fgsmc,\n"
                                + "    grouping(fx.kouj) desc,fx.kouj ,\n" + "    fx.fenx";
                        fenz_bt = "";
                        where = "";
                    }
                }
                if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                    shul = "  sum(sl.laimsl*dc.konggbl) as laimsl,\n"
                            + "			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl)) as farl,\n"
                            + "			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as buhs_daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as chebj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as yunj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as zaf ";
                    strYouzb = "sum(r.shul*dianc.konggbl) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul*dianc.konggbl)/sum(r.shul*dianc.konggbl),2)) as tianrypjdj";
                } else {
                    shul = "  sum(sl.laimsl) as laimsl,\n"
                            + "			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl,\n"
                            + "			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*sl.laimsl)/sum(sl.laimsl),4)) as buhs_daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*sl.laimsl)/sum(sl.laimsl),2)) as chebj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as zaf  ";
                    strYouzb = "sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj";
                }

                strSQL = jibreport.getSql10_1(biaot, biaot0, fenz_bt, strzt, shul, strYouzb, dianc, tiaoj, shulzt, zhilzt, youzt, mc, fenz, dianckjmx_bm, dianckjmx_tj, where, strGongsID, zhuangt, yb_sl, yb_zl, yb_dj, yb_rcy, intyear, intMonth, intyear2, intMonth2);

            } else {
                biaot0 = "select a.DANWMC,--a.FENX,\n  a.BQ_DAOCZHJ,a.TQ_DAOCZHJ,a.BQ_CHEBJ,a.TQ_CHEBJ,a.BQ_YUNJ,a.TQ_YUNJ,a.BQ_ZAF,a.TQ_ZAF,a.BQ_RUCBMDJ,\n" +
                        "a.TQ_RUCBMDJ,a.BQ_TIANRMDJ,a.TQ_TIANRMDJ,  \n";
                if (jib == 3) {
                    mc = "dc.fgsmc";
                    biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            " --having not grouping(fx.fenx)=1 " + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                    fx = "  where a.f=0 and a.mc=0  ";
                } else {
                    if (getBaoblxValue().getValue().equals("������ͳ��")) {
                        if (jib != 3) {
                            notHuiz = " ";
                        }
                        mc = "dq.mingc";
                        biaot = "  decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(grouping(dc.mingc),1,dq.mingc,decode(" + jib + ",2,'',dq.mingc)) as dqmc,decode(" + jib + ",2,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc0";
                        dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                        tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                        fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                                "--having not grouping(fx.fenx)=1\n" +
                                " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                        fx = " where a.f=0 ";
                        tj = " grouping(fx.fenx)f, ";
                    } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                        mc = "dc.fgsmc";
                        biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
                        dianc = " vwdianc dc \n";
                        tiaoj = "";
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                // " --having not grouping(fx.fenx)=1 "+guolzj+
                                " having not grouping(fx.fenx)=1 " + guolzj +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                        fx = "  where a.f=0 --and a.fgs=0  ";
                    } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                        mc = "dc.fgsmc";
                        biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc0";
                        dianc = " vwdianc dc \n";
                        tiaoj = "";
                        fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                                // " --having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)"+guolzj+
                                "having not grouping(fx.fenx)=1 " + guolzj +
                                " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                                "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                        //fx="  where a.f=0 and a.mc=1  ";
                        if (jib == 1 || jib == 2) {
                            fx = "  where a.f=0 and a.mc=1  ";
                        } else {
                            fx = "  where (a.f=0 or a.mc=1)  ";
                        }

                        if (jib == 3) {
                            notHuiz = " and a.mc=1 ";
                        } else {
                            notHuiz = " ";
                        }
                    }
                }
                if (getBaoblxValue2().getValue().equals("�ص㶩��")) {
                    jihkj = " and (kj.jihkjb_id=1 or kj.jihkjb_id=3)";
                    rucycbb_bq = "";
                    rucycbb_tq = "";
                    rucycbb_tj = "";
                    rucycbb_bt = " '' as bq_tianrmdj,\n" + "        '' as tq_tianrmdj\n";
                } else if (getBaoblxValue2().getValue().equals("�г��ɹ�")) {
                    jihkj = " and kj.jihkjb_id=2";
                    rucycbb_bq = "";
                    rucycbb_tq = "";
                    rucycbb_tj = "";
                    rucycbb_bt = " '' as bq_tianrmdj,\n" + "        '' as tq_tianrmdj\n";
                } else {
                    jihkj = "";
                    rucycbb_bq = "           ,( select r.fenx,r.diancxxb_id,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n" +
                            "             from  " + yb_rcy + " r where r.riq=to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd') " + youzt + " group by (r.diancxxb_id,r.fenx)) a,\n";

                    rucycbb_tq = "           ( select r.fenx,r.diancxxb_id,sum(r.shul) as youl,decode(sum(r.shul),0,0,Round(sum(r.hanszhj*r.shul)/sum(r.shul),2)) as tianrypjdj\n" +
                            "             from  " + yb_rcy + " r where r.riq=add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12)  " + youzt + " group by (r.diancxxb_id,r.fenx)) b\n";

                    rucycbb_tj = " and fx.diancxxb_id=a.diancxxb_id(+)\n" +
                            "        and fx.fenx=a.fenx(+)\n" +
                            "        and fx.diancxxb_id=b.diancxxb_id(+)\n" +
                            "        and fx.fenx=b.fenx(+)\n";
                    rucycbb_bt =
                            "decode(grouping(" + mc + ")+grouping (dc.mingc),2,\n" +
                                    "           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2),1,\n" +
                                    "           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2),\n" +
                                    "           Round(decode(sum(a.youl),0,0,sum(a.tianrypjdj*a.youl)/sum(a.youl)),2)) as bq_tianrmdj,\n" +
                                    "     decode(grouping(" + mc + ")+grouping (dc.mingc),2,\n" +
                                    "\t      \t  Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2),1 ,\n" +
                                    "          Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2),\n" +
                                    "          Round(decode(sum(b.youl),0,0,sum(b.tianrypjdj*b.youl)/sum(b.youl)),2)) as tq_tianrmdj\n";
                }

                if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                    shul = "sum(sl.laimsl*dc.konggbl) as laimsl,\n"
                            + "			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl)) as farl,\n"
                            + "			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as buhs_daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as chebj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as yunj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),2)) as zaf,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.buhsbmdj*(sl.laimsl*dc.konggbl))/sum(sl.laimsl*dc.konggbl),4)) as rucbmdj";
                } else {
                    shul = "  sum(sl.laimsl) as laimsl,\n"
                            + "			decode(sum(sl.laimsl),0,0,sum(nvl(zl.qnet_ar,0)*sl.laimsl)/sum(sl.laimsl)) as farl,\n"
                            + "			decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf-y.meijs-y.yunjs)*sl.laimsl)/sum(sl.laimsl),4)) as buhs_daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.meij+y.yunj+y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as daoczhj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.meij*sl.laimsl)/sum(sl.laimsl),2)) as chebj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.yunj*sl.laimsl)/sum(sl.laimsl),2)) as yunj,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum((y.zaf+y.daozzf+y.qit+y.jiaohqzf)*sl.laimsl)/sum(sl.laimsl),2)) as zaf ,\n"
                            + "         decode(sum(sl.laimsl),0,0,Round(sum(y.buhsbmdj*sl.laimsl)/sum(sl.laimsl),4)) as rucbmdj";
                }
                strSQL = jibreport.getSql10_2(biaot, biaot0, tj, fx, notHuiz, strzt, dianc, tiaoj, shulzt, zhilzt, youzt, mc, shul, fenz, dianckjmx_bm, dianckjmx_tj, rucycbb_bt, jihkj, rucycbb_bq, rucycbb_tq, rucycbb_tj, strGongsID, zhuangt, yb_sl, yb_zl, yb_dj, intyear, intMonth, intyear2, intMonth2);

            }
//	 ֱ���ֳ�����
				/*ArrHeader=new String[3][18];
				ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","�����ۺϼ�","�����ۺϼ�","����(ƽ��)��","����(ƽ��)��","�˷�","�˷�","�ӷ�","�ӷ�","�볧��ú����","�볧��ú����",
							                    "��Ȼ��ƽ������","��Ȼ��ƽ������","���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};

				ArrWidth=new int[] {150,60,70,70,70,70,70,70,70,70,70,70,70,70,0,0,0,0};
				String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","","","",""};*/

            ArrHeader = new String[3][17];
            ArrHeader[0] = new String[]{"��λ����", "�����ۺϼ�", "�����ۺϼ�", "����(ƽ��)��", "����(ƽ��)��", "�˷�", "�˷�", "�ӷ�", "�ӷ�", "�볧��ú����", "�볧��ú����",
                    "��Ȼ��ƽ������", "��Ȼ��ƽ������", "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

            ArrWidth = new int[]{150, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 0, 0, 0};
            String arrFormat[] = new String[]{"", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};

            iFixedRows = 1;

            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "���볧ú(" + getBaoblxValue2().getValue() + ")�۸������", ArrWidth, 4);
            rt.setDefaultTitle(1, 3, "���λ:" + ((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(5, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(8, 2, "��λ:Ԫ/��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(11, 3, "cpiȼ�Ϲ���10��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;
//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rt.body.setColFormat(arrFormat);

            tb.setColAlign(2, Table.ALIGN_CENTER);

            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
            rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);


            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();

            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���11��")) {

            JDBCcon cn = new JDBCcon();
            String zhuangt = "";
            String guolzj = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }

            String strGongsID = "";
            String strDiancFID = "";
            String notHuiz = "";
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                strDiancFID = "'',";
                guolzj = "";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
                strGongsID = "  and (dc.fuid= " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                strDiancFID = this.getTreeid() + ",";
                guolzj = " and  a.fgs=0--grouping(dc.fgsmc)=0\n";//�ֹ�˾�鿴����ʱ�����ܼơ�
            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                strDiancFID = "'',";
                guolzj = "  and a.fgs=0 --grouping(dc.mingc)=0\n";
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }

            String shul = "";
            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }

            strzt = "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuezbb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + "dqmc,fgsmc,diancmc,add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";
            if (jib == 3) {
                biaot = "a.DANWMC,--a.FENX,\n  a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
                        "a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ," + strzt + " \n" +
                        "from ( select" +
                        " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "--having not grouping(fx.fenx)=1" + guolzj +
                        "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                        " )a   where a.f=0 and a.dcmc=0";
                grouping = "   ,grouping(dc.fgsmc)  fgs \n";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot =
                            "a.DANWMC,--a.FENX,\n  a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
                                    "a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ," + strzt + " \n" +
                                    "from ( select" +
                                    " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,decode(" + jib + ",2,'',dq.mingc) as dqmc,decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")) fgsmc,dc.mingc as diancmc";
                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1\n" +
                            " \n order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0 ";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    biaot = "a.DANWMC,--a.FENX,\n  a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
                            "a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ," + strzt + " \n" +
                            "from ( select" +
                            " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1" + guolzj +
                            "  \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0 " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot = "a.DANWMC,--a.FENX,\n  a.BQ_FDTRMDJ,a.TQ_FDTRMDJ,a.BQ_GRTRMDJ,a.TQ_GRTRMDJ,a.BQ_FDZHBMDJ,a.TQ_FDZHBMDJ,a.BQ_FADBMDJ,\n" +
                            "a.TQ_FADBMDJ,a.BQ_FADYBMDJ,a.TQ_FADYBMDJ,a.BQ_GRZHBMDJ,a.TQ_GRZHBMDJ,a.BQ_GRMBMDJ,a.TQ_GRMBMDJ,a.BQ_GRHYBMDJ,a.TQ_GRHYBMDJ," + strzt + " \n" +
                            "from ( select" +
                            " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc,'' as dqmc,dc.fgsmc as fgsmc,dc.mingc as diancmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "--having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + guolzj +
                            " \n order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)" +
                            " )a   where a.f=0  and   dcmc=1  " + guolzj;
                    grouping = "   ,grouping(dc.fgsmc)  fgs \n";
                }
            }
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                shul = "         sum(y.FADYTRML*dc.konggbl) as fadhml,sum(y.GONGRYTRML*dc.konggbl) as gongrhml,\n"
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
            } else {
                shul = "         sum(y.FADYTRML) as fadhml,sum(y.GONGRYTRML) as gongrhml,\n"
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

            strSQL = jibreport.getSql11(biaot, dianc, tiaoj, grouping, shul, fenz, dianckjmx_bm, dianckjmx_tj, strGongsID, zhuangt, yb_zb, intyear, intMonth, intyear2, intMonth2);

//	 ֱ���ֳ�����
				/*ArrHeader=new String[3][22];
				ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","������Ȼú����","������Ȼú����","������Ȼú����","������Ȼú����","�����ۺϱ�ú����","�����ۺϱ�ú����","����:ú�۱�ú����","����:ú�۱�ú����","����:���۱�ú����","����:���۱�ú����","�����ۺϱ�ú����","�����ۺϱ�ú����",
							                    "����:ú�۱�ú����","����:ú�۱�ú����","����:���۱�ú����","����:���۱�ú����","���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22"};

				ArrWidth=new int[] {150,40,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,55,0,0,0,0};
				iFixedRows=1;
				String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0","0","0","0"};*/
            ArrHeader = new String[3][21];
            ArrHeader[0] = new String[]{"��λ����", "������Ȼú����", "������Ȼú����", "������Ȼú����", "������Ȼú����", "�����ۺϱ�ú����", "�����ۺϱ�ú����", "����:ú�۱�ú����", "����:ú�۱�ú����", "����:���۱�ú����", "����:���۱�ú����", "�����ۺϱ�ú����", "�����ۺϱ�ú����",
                    "����:ú�۱�ú����", "����:ú�۱�ú����", "����:���۱�ú����", "����:���۱�ú����", "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21"};

            ArrWidth = new int[]{150, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 55, 0, 0, 0, 0};
            iFixedRows = 1;
            String arrFormat[] = new String[]{"", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};

            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "�·����ú���������", ArrWidth, 4);
            rt.setDefaultTitle(1, 4, "���λ(����):" + ((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(6, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(10, 4, "��λ:Ԫ/��", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(15, 3, "cpiȼ�Ϲ���11��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;
//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rt.body.setColFormat(arrFormat);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }

            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
            rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(11, 3, "�Ʊ�:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);


            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();

            return rt.getAllPagesHtml();
        } else if (getBaobmc().equals("cpiȼ�Ϲ���12��")) {

            JDBCcon cn = new JDBCcon();
            String zhuangt = "";
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (y.zhuangt=1 or y.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and y.zhuangt=2";
            }


            String strGongsID = "";
            String strDiancFID = "";
            String guolzj = "";
            String danwmc = "";//��������
            int jib = this.getDiancTreeJib();
            if (jib == 1) {//ѡ����ʱˢ�³����еĵ糧
                strGongsID = " ";
                strDiancFID = "'',";
                guolzj = "";
            } else if (jib == 2) {//ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
//				strGongsID = "  and dc.fuid=  " +this.getTreeid();
                strGongsID = "  and (dc.fuid= " + this.getTreeid() + " or dc.shangjgsid=" + this.getTreeid() + ")";
                strDiancFID = "" + this.getTreeid() + ",";
                guolzj = " and grouping(dc.fgsmc)=0\n";//���糧���Ƿֹ�˾ʱ,ȥ�����Ż���

            } else if (jib == 3) {//ѡ�糧ֻˢ�³��õ糧
                strGongsID = " and dc.id= " + this.getTreeid();
                strDiancFID = "" + this.getTreeid() + ",";
                guolzj = " and grouping(dc.mingc)=0\n";//���糧���ǵ糧ʱ,ȥ���ֹ�˾�ͼ��Ż���
            } else if (jib == -1) {
                strGongsID = " and dc.id = " + ((Visit) getPage().getVisit()).getDiancxxb_id();
                strDiancFID = "'',";
            }


            if (getYuebValue().getValue().equals("ȫ��")) {
                dianckjmx_bm = "";
                dianckjmx_tj = "";
                koujid = "'',";
            } else {
                dianckjmx_bm = ",dianckjmx kjmx";
                dianckjmx_tj = " and kjmx.diancxxb_id(+)=dc.id and kjmx.dianckjb_id=" + getYuebValue().getId() + " ";
                koujid = getYuebValue().getId() + ",";
            }
            String diancdwmc = "";
            if (jib == 3) {
                diancdwmc = "'',dc.fgsmc,dc.mingc,";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    //diancdwmc="dq.mingc,'',dc.mingc,";
                    diancdwmc = "decode(" + jib + ",2,'',dq.mingc),decode(" + jib + ",1,'',(select mingc from diancxxb where id=" + this.getTreeid() + ")),dc.mingc,";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    diancdwmc = "'',dc.fgsmc,dc.mingc,";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    diancdwmc = "'',dc.fgsmc,'',";
                }
            }
            strzt = "getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuezbb','����'" + "," + visit.getRenyjb() + ")as bqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as bqlj,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','����'" + "," + visit.getRenyjb() + ") as tqby,\n" +
                    "getShenhzt(" + koujid + strDiancFID + diancdwmc + "add_months(to_date('" + intyear + "-" + intMonth + "-01','yyyy-mm-dd'),-12),add_months(to_date('" + intyear2 + "-" + intMonth2 + "-01','yyyy-mm-dd'),-12),'yuezbb','�ۼ�'" + "," + visit.getRenyjb() + ") as tqlj\n";

            if (jib == 3) {
                biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc";
                dianc = " vwdianc dc \n";
                tiaoj = "";
                fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                        "having not grouping(fx.fenx)=1" + guolzj +
                        "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                        "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
            } else {
                if (getBaoblxValue().getValue().equals("������ͳ��")) {
                    biaot = " decode(grouping(dq.mingc)+grouping(dc.mingc),2,'�ܼ�',1,dq.mingc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc";
                    dianc = " diancxxb dc,shengfb sf,shengfdqb dq\n";
                    tiaoj = " and dc.shengfb_id=sf.id and sf.shengfdqb_id=dq.id";
                    fenz = "group by rollup(fx.fenx,dq.mingc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1\n" +
                            "order by grouping(dq.mingc) desc,max(dq.xuh),dq.mingc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                } else if (getBaoblxValue().getValue().equals("���糧ͳ��")) {
                    biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc||'�ϼ�','&nbsp;&nbsp'||dc.mingc) as danwmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1" + guolzj +
                            "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                } else if (getBaoblxValue().getValue().equals("���ֹ�˾ͳ��")) {
                    biaot = " decode(grouping(dc.fgsmc)+grouping(dc.mingc),2,'�ܼ�',1,dc.fgsmc,'&nbsp;&nbsp'||dc.mingc) as danwmc";
                    dianc = " vwdianc dc \n";
                    tiaoj = "";
                    fenz = "group by rollup(fx.fenx,dc.fgsmc,dc.mingc)\n" +
                            "having not grouping(fx.fenx)=1 and (grouping(dc.fgsmc) =1 or grouping(dc.mingc)=1)" + guolzj +
                            "order by grouping(dc.fgsmc) desc,max(dc.fgsxh),dc.fgsmc,\n" +
                            "grouping(dc.mingc) desc,max(dc.xuh) ,dc.mingc,max(fx.xuh)";
                }
            }

            String shul = "";
            if (getKoujlxValue().getValue().equals("�ȱȿھ�")) {
                shul = "sum(y.RANLCB_BHS*dc.konggbl) as ranlzcb,sum(y.FADMCB*dc.konggbl) as fadmtcb,\n"
                        + "         sum(y.GONGRMCB*dc.konggbl) as gongrmtcb,sum(y.FADYCB*dc.konggbl) as fadycb,sum(y.GONGRYCB*dc.konggbl) as gongrycb," +
                        "sum(y.fadl*dc.konggbl-y.gongdl*dc.konggbl-y.GONGRCGDL*dc.konggbl) as gongdl,\n"
                        + "         decode(sum(y.fadl-y.gongdl-y.GONGRCGDL),0,0," +
                        "Round(sum(y.FADMCB*dc.konggbl+y.FADYCB*dc.konggbl)*1000/sum(y.fadl*dc.konggbl-y.gongdl*dc.konggbl-y.GONGRCGDL*dc.konggbl),2)) as danwrlcb";
            } else {
                shul = "sum(y.RANLCB_BHS) as ranlzcb,sum(y.FADMCB) as fadmtcb,\n"
                        + "         sum(y.GONGRMCB) as gongrmtcb,sum(y.FADYCB) as fadycb,sum(y.GONGRYCB) as gongrycb,sum(y.fadl-y.gongdl-y.GONGRCGDL) as gongdl,\n"
                        + "         decode(sum(y.fadl-y.gongdl-y.GONGRCGDL),0,0,Round(sum(y.FADMCB+y.FADYCB)*1000/sum(y.fadl-y.gongdl-y.GONGRCGDL),2)) as danwrlcb";
            }

            strSQL = jibreport.getSql12(biaot, dianc, tiaoj, strzt, shul, fenz, dianckjmx_bm, dianckjmx_tj, strGongsID, zhuangt, yb_zb, intyear, intMonth, intyear2, intMonth2);

//	 ֱ���ֳ�����
				/*ArrHeader=new String[3][18];
				ArrHeader[0]=new String[] {"��λ����","���»��ۼ�","ȼ���ܳɱ�","ȼ���ܳɱ�","����:����ú̿�ɱ�","����:����ú̿�ɱ�","����:����ú̿�ɱ�","����:����ú̿�ɱ�","����:�����ͳɱ�","����:�����ͳɱ�",
							                    "����:�����ͳɱ�","����:�����ͳɱ�","��λȼ�ϳɱ�","��λȼ�ϳɱ�","���״̬","���״̬","���״̬","���״̬"};
				ArrHeader[1]=new String[] {"��λ����","���»��ۼ�","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","ͬ��","����","����","ͬ��","ͬ��"};
				ArrHeader[2]=new String[] {"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18"};

				ArrWidth=new int[] {150,60,70,70,70,70,70,70,70,70,70,70,70,70,0,0,0,0};
				String arrFormat[]=new String[]{"","","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0.00","0","0","0","0"};*/

            ArrHeader = new String[3][17];
            ArrHeader[0] = new String[]{"��λ����", "ȼ���ܳɱ�", "ȼ���ܳɱ�", "����:����ú̿�ɱ�", "����:����ú̿�ɱ�", "����:����ú̿�ɱ�", "����:����ú̿�ɱ�", "����:�����ͳɱ�", "����:�����ͳɱ�",
                    "����:�����ͳɱ�", "����:�����ͳɱ�", "��λȼ�ϳɱ�", "��λȼ�ϳɱ�", "���״̬", "���״̬", "���״̬", "���״̬"};
            ArrHeader[1] = new String[]{"��λ����", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "ͬ��", "����", "����", "ͬ��", "ͬ��"};
            ArrHeader[2] = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"};

            ArrWidth = new int[]{150, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 0, 0, 0, 0};
            String arrFormat[] = new String[]{"", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00"};

            iFixedRows = 1;


            ResultSet rs = cn.getResultSet(strSQL);

            // ����
            Table tb = new Table(rs, 3, 0, 1, 4);
            rt.setBody(tb);

            rt.setTitle(getBiaotmc() + intyear + "��" + intMonth + "��ȼ�ϳɱ����������", ArrWidth, 4);
            rt.setDefaultTitle(1, 3, "���λ:" + ((Visit) getPage().getVisit()).getDiancmc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(5, 3, "�����:" + intyear + "��" + intMonth + "��", Table.ALIGN_LEFT);
            rt.setDefaultTitle(8, 3, "��λ:��Ԫ��Ԫ/ǧǧ��ʱ", Table.ALIGN_RIGHT);
            rt.setDefaultTitle(11, 3, "cpiȼ�Ϲ���12��", Table.ALIGN_RIGHT);

            rt.body.setWidth(ArrWidth);
            rt.body.setPageRows(22);
            rt.body.setHeaderData(ArrHeader);// ��ͷ����
            rt.body.mergeFixedRow();
            rt.body.mergeFixedCols();
            rt.body.ShowZero = false;
//				��δ��˵糧���ñ���ɫ��ֻҪ��δ������ݾͺ�ɫ���
            int rows = rt.body.getRows();
            int cols = rt.body.getCols();
            try {
                rs.beforeFirst();
                for (int i = 4; i < rows + 1; i++) {
                    rs.next();
                    for (int k = 0; k < cols + 1; k++) {
//						     if(!(rt.body.getCellValue(i, cols-3).equals("0")&&rt.body.getCellValue(i, cols-2).equals("0")&&
//						    		 rt.body.getCellValue(i, cols-1).equals("0")&&rt.body.getCellValue(i, cols).equals("0"))){
                        if (!(rs.getString(cols + 1).equals("0") && rs.getString(cols + 2).equals("0") &&
                                rs.getString(cols + 3).equals("0") && rs.getString(cols + 4).equals("0"))) {
                            rt.body.getCell(i, k).backColor = "red";
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rt.body.setColFormat(arrFormat);
            tb.setColAlign(2, Table.ALIGN_CENTER);
            if (rt.body.getRows() > 3) {
                rt.body.setCellAlign(4, 1, Table.ALIGN_CENTER);
            }
            //ҳ��
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 3, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_LEFT);
            rt.setDefautlFooter(5, 2, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(9, 2, "�Ʊ�:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(rt.footer.getCols() - 5, 2, Table.PAGENUMBER_NORMAL, Table.ALIGN_RIGHT);

            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            cn.Close();

            return rt.getAllPagesHtml();
        }

        return rt.getAllPagesHtml();

    }

    // �õ���½��Ա�����糧��ֹ�˾������
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
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    // �糧����
    public boolean _diancmcchange = false;

    private IDropDownBean _DiancmcValue;

    public IDropDownBean getDiancmcValue() {
        if (_DiancmcValue == null) {
            _DiancmcValue = (IDropDownBean) getIDiancmcModel().getOption(0);
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

        String sql = "";
        sql = "select d.id,d.mingc as jianc from diancxxb d order by d.mingc desc";
        _IDiancmcModel = new IDropDownModel(sql);

    }

    // �������
    public boolean _meikdqmcchange = false;

    private IDropDownBean _MeikdqmcValue;

    public IDropDownBean getMeikdqmcValue() {
        if (_MeikdqmcValue == null) {
            _MeikdqmcValue = (IDropDownBean) getIMeikdqmcModels().getOption(0);
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
        try {

            String sql = "";
            sql = "select id,mingc from gongysb order by mingc";
            _IMeikdqmcModel = new IDropDownModel(sql);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return _IMeikdqmcModel;
    }


    // �������� ͳ�ƿھ���ȼ�Ϲ���5-12��
    public boolean _Baoblxchange = false;

    private IDropDownBean _BaoblxValue;

    public IDropDownBean getBaoblxValue() {
        if (_BaoblxValue == null) {
            _BaoblxValue = (IDropDownBean) getIBaoblxModels().getOption(0);
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


    //	�������ͣ�ȼ�Ϲ���10��
    public boolean _Baoblxchange2 = false;

    private IDropDownBean _BaoblxValue2;

    public IDropDownBean getBaoblxValue2() {
        if (_BaoblxValue2 == null) {
            _BaoblxValue2 = (IDropDownBean) getIBaoblxModels2().getOption(0);
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
        try {
            List baoblx2List = new ArrayList();
            baoblx2List.add(new IDropDownBean(0, "�糧���ھ�ͳ��"));
            baoblx2List.add(new IDropDownBean(1, "�糧����"));
            baoblx2List.add(new IDropDownBean(2, "�ص㶩��"));
            baoblx2List.add(new IDropDownBean(3, "�г��ɹ�"));
            _IBaoblxModel2 = new IDropDownModel(baoblx2List);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return _IBaoblxModel2;
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
        try {
            List baoblxList = new ArrayList();
            if (visit.getRenyjb() == 1) {
                baoblxList.add(new IDropDownBean(0, "���ֹ�˾ͳ��"));
                baoblxList.add(new IDropDownBean(1, "���糧ͳ��"));
                baoblxList.add(new IDropDownBean(2, "������ͳ��"));
            } else {
                baoblxList.add(new IDropDownBean(0, "���糧ͳ��"));
                baoblxList.add(new IDropDownBean(1, "������ͳ��"));
            }
            _IBaoblxModel = new IDropDownModel(baoblxList);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }
        return _IBaoblxModel;
    }

    // ��������
    public boolean _Baobmcchange = false;

    private IDropDownBean _BaobmcValue;

    public IDropDownBean getBaobmcValue() {
        if (_BaobmcValue == null) {
            _BaobmcValue = (IDropDownBean) getBaobmcModels().getOption(0);
        }
        return _BaobmcValue;
    }

    public void setBaobmcValue(IDropDownBean Value) {
        long id = -2;
        if (_BaobmcValue != null) {
            id = _BaobmcValue.getId();
        }
        if (Value != null) {
            if (Value.getId() != id) {
                _Baobmcchange = true;
            } else {
                _Baobmcchange = false;
            }
        }
        _BaobmcValue = Value;
    }

    // ȡ�ñ�������
    public String getBaobmc() {
        String baobmc = "";
        String pagemc = "";
        int i = 0;
        switch (Integer.parseInt(getBaobmcValue().getId() + "")) {
            case 0:
                baobmc = "cpiȼ�Ϲ���05��";
                break;
            case 1:
                baobmc = "cpiȼ�Ϲ���06��";
                break;
            case 2:
                baobmc = "cpiȼ�Ϲ���07��";
                break;
            case 3:
                baobmc = "cpiȼ�Ϲ���08��";
                break;
            case 4:
                baobmc = "cpiȼ�Ϲ���09��";
                break;
            case 5:
                baobmc = "cpiȼ�Ϲ���10��";
                break;
            case 6:
                baobmc = "cpiȼ�Ϲ���11��";
                break;
            case 7:
                baobmc = "cpiȼ�Ϲ���12��";
                break;
            default:
                return "�޴˱���";
        }
        return baobmc;

    }

    private IPropertySelectionModel _BaobmcModel;

    public void setBaobmcModel(IPropertySelectionModel value) {
        _BaobmcModel = value;
    }

    public IPropertySelectionModel getBaobmcModel() {
        if (_BaobmcModel == null) {
            getBaobmcModels();
        }
        return _BaobmcModel;
    }

    public IPropertySelectionModel getBaobmcModels() {
//		Visit visit = (Visit) getPage().getVisit();
//		JDBCcon con = new JDBCcon();
        try {
            List fahdwList = new ArrayList();

            fahdwList.add(new IDropDownBean(0, "cpi05"));
            fahdwList.add(new IDropDownBean(1, "cpi06"));
            fahdwList.add(new IDropDownBean(2, "cpi07"));
            fahdwList.add(new IDropDownBean(3, "cpi08"));
            fahdwList.add(new IDropDownBean(4, "cpi09"));
            fahdwList.add(new IDropDownBean(5, "cpi10"));
            fahdwList.add(new IDropDownBean(6, "cpi11"));
            fahdwList.add(new IDropDownBean(7, "cpi12"));

            _BaobmcModel = new IDropDownModel(fahdwList);

        } catch (Exception e) {
            e.printStackTrace();
        } /*finally {
			con.Close();
		}*/
        return _BaobmcModel;

    }

    /**
     * ���1
     */
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
        for (i = 2000; i <= DateUtil.getYear(new Date()) + 1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

    /**
     * ���2
     */
    private static IPropertySelectionModel _NianfModel2;

    public IPropertySelectionModel getNianfModel2() {
        if (_NianfModel2 == null) {
            getNianfModels2();
        }
        return _NianfModel2;
    }

    public void setNianfModel2(IPropertySelectionModel _value) {
        _NianfModel2 = _value;
    }

    private IDropDownBean _NianfValue2;

    public IDropDownBean getNianfValue2() {

        if (_NianfValue2 == null) {
            int _nianf2 = DateUtil.getYear(new Date());
            int _yuef2 = DateUtil.getMonth(new Date());
            if (_yuef2 == 1) {
                _nianf2 = _nianf2 - 1;
            }
            for (int i = 0; i < _NianfModel2.getOptionCount(); i++) {
                Object obj = _NianfModel2.getOption(i);
                if (_nianf2 == ((IDropDownBean) obj).getId()) {
                    _NianfValue2 = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue2;
    }

    public boolean nianfchanged2 = false;

    public void setNianfValue2(IDropDownBean Value) {
        if (_NianfValue2 != Value) {
            nianfchanged2 = true;
        }
        _NianfValue2 = Value;
    }

    // ���������
    public IPropertySelectionModel getNianfModels2() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2000; i <= DateUtil.getYear(new Date()) + 1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel2 = new IDropDownModel(listNianf);
        return _NianfModel2;
    }

    /**
     * �·�1
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

    /**
     * �·�2
     */
    private static IPropertySelectionModel _YuefModel2;

    public IPropertySelectionModel getYuefModel2() {
        if (_YuefModel2 == null) {
            getYuefModels2();
        }
        return _YuefModel2;
    }

    public void setYuefModel2(IPropertySelectionModel _value) {
        _YuefModel2 = _value;
    }

    private IDropDownBean _YuefValue2;

    public IDropDownBean getYuefValue2() {
        if (_YuefValue2 == null) {
            int _yuef2 = DateUtil.getMonth(new Date());
            if (_yuef2 == 1) {
                _yuef2 = 12;
            } else {
                _yuef2 = _yuef2 - 1;
            }
            for (int i = 0; i < _YuefModel2.getOptionCount(); i++) {
                Object obj = _YuefModel2.getOption(i);
                if (_yuef2 == ((IDropDownBean) obj).getId()) {
                    _YuefValue2 = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _YuefValue2;
    }

    public boolean yuefchanged2 = false;

    public void setYuefValue2(IDropDownBean Value) {
        if (_YuefValue2 != Value) {
            yuefchanged2 = true;
        }
        _YuefValue2 = Value;
    }

    // �·�������
    public IPropertySelectionModel getYuefModels2() {
        List listYuef = new ArrayList();
        for (int i = 1; i < 13; i++) {
            listYuef.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _YuefModel2 = new IDropDownModel(listYuef);
        return _YuefModel2;
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


    // ***************************�����ʼ����***************************//
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

    public Date getYesterday(Date dat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public Date getMonthFirstday(Date dat) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dat);
        cal.set(Calendar.DATE, cal.getActualMinimum(Calendar.DATE));
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

    // ҳ���ж�����
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

    // �ֹ�˾������
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
        setDiancxxModel(new IDropDownModel(sql, "�й����Ƽ���"));
    }

    // �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id="
                + DiancTreeJib;
        ResultSet rs = con.getResultSet(sqlJib.toString());

        try {
            while (rs.next()) {
                jib = rs.getInt("jib");
            }
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            con.Close();
        }

        return jib;
    }

    // �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
    public String getTreeDiancmc(String diancmcId) {
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
            rs.close();
        } catch (SQLException e) {

            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    public void getToolBars() {
//		System.out.println("��������ʾ������������");
        Toolbar tb1 = new Toolbar("tbdiv");

        tb1.addText(new ToolbarText("��������:"));
        ComboBox baobmc = new ComboBox();
        baobmc.setTransform("BaobmcDropDown");
        baobmc.setWidth(50);
        baobmc.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(baobmc);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("���:"));
        ComboBox nianf = new ComboBox();
        nianf.setTransform("NIANF");
        nianf.setWidth(50);
        nianf.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(nianf);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("�·�:"));
        ComboBox yuef = new ComboBox();
        yuef.setTransform("YUEF");
        yuef.setWidth(40);
        yuef.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(yuef);
        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("��:"));
        tb1.addText(new ToolbarText("-"));
        ComboBox nianf2 = new ComboBox();
        nianf2.setTransform("NIANF2");
        nianf2.setWidth(50);
        nianf2.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(nianf2);
        tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText(" "));
        ComboBox yuef2 = new ComboBox();
        yuef2.setTransform("YUEF2");
        yuef2.setWidth(40);
        yuef2.setListeners("select:function(){document.Form0.submit();}");
        tb1.addField(yuef2);
        tb1.addText(new ToolbarText("-"));

        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(),
                "-1".equals(getTreeid()) ? null : getTreeid());
        setTree(etu);

        TextField tf = new TextField();
        tf.setId("diancTree_text");
        tf.setWidth(100);
        tf.setValue(((IDropDownModel) getIDiancmcModel()).getBeanValue(Long
                .parseLong(getTreeid())));

        ToolbarButton tb2 = new ToolbarButton(null, null,
                "function(){diancTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);

        tb1.addText(new ToolbarText("��λ:"));
        tb1.addField(tf);
        tb1.addItem(tb2);
        tb1.addText(new ToolbarText("-"));

        if (getDiancTreeJib() != 3) {
            tb1.addText(new ToolbarText("ͳ�ƿھ�:"));
            ComboBox cb = new ComboBox();
            cb.setTransform("BaoblxDropDown");
            cb.setWidth(90);
            cb.setListeners("select:function(){document.Form0.submit();}");
            tb1.addField(cb);
            tb1.addText(new ToolbarText("-"));
        }

        if (getBaobmc().equals("cpiȼ�Ϲ���10��")) {
            tb1.addText(new ToolbarText("��������:"));
            ComboBox baoblx = new ComboBox();
            baoblx.setTransform("BaoblxDropDown2");
            baoblx.setWidth(90);
            baoblx.setListeners("select:function(){document.Form0.submit();}");
            tb1.addField(baoblx);
            tb1.addText(new ToolbarText("-"));
        }

        if (getDiancTreeJib() != 3) {
            tb1.addText(new ToolbarText("�±��ھ�:"));
            ComboBox cb2 = new ComboBox();
            cb2.setTransform("YuebDropDown");
            cb2.setWidth(60);
            cb2.setListeners("select:function(){document.Form0.submit();}");
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


        ToolbarButton tb = new ToolbarButton(null, "ˢ��",
                "function(){document.Form0.submit();}");

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

    // �±��ھ�
    private boolean _yuebchange = false;

    public IDropDownBean getYuebValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
            if (getYuebModel().getOptionCount() > 0) {
                ((Visit) getPage().getVisit())
                        .setDropDownBean2((IDropDownBean) getYuebModel()
                                .getOption(0));
            }
        }
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setYuebValue(IDropDownBean Value) {
        if (getYuebValue().getId() != Value.getId()) {
            _yuebchange = true;
        }
        ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setYuebModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getYuebModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getIYuebModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getIYuebModels() {

        String sql = "select kj.id as id,kj.mingc as mingc from dianckjb kj\n"
                + "\t\twhere kj.fenl_id in (select distinct id from item i where i.bianm='YB' and i.zhuangt=1)\n"
                + "    and kj.diancxxb_id="
                + ((Visit) getPage().getVisit()).getDiancxxb_id();

        ((Visit) getPage().getVisit())
                .setProSelectionModel2(new IDropDownModel(sql, "ȫ��"));
    }

    /////////////////////////////////////////////////////
//	�±��ھ�����
    private boolean _koujlxchange = false;

    public IDropDownBean getKoujlxValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean3() == null) {
            if (getKoujlxModel().getOptionCount() > 0) {
                ((Visit) getPage().getVisit()).setDropDownBean3((IDropDownBean) getKoujlxModel().getOption(0));
            }
        }
        return ((Visit) getPage().getVisit()).getDropDownBean3();
    }

    public void setKoujlxValue(IDropDownBean Value) {
        if (getYuebValue().getId() != Value.getId()) {
            _koujlxchange = true;
        }
        ((Visit) getPage().getVisit()).setDropDownBean3(Value);
    }

    public void setKoujlxModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel3(value);
    }

    public IPropertySelectionModel getKoujlxModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel3() == null) {
            getIKoujlxModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel3();
    }

    public void getIKoujlxModels() {

        String sql = "select id,mingc from item where bianm='YB' order by mingc desc";

        ((Visit) getPage().getVisit()).setProSelectionModel3(new IDropDownModel(sql));
    }
    /////////////////////////////////////////////////////

    private String treeid;

    public String getTreeid() {
        String treeid = ((Visit) getPage().getVisit()).getString2();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString2(String
                    .valueOf(((Visit) this.getPage().getVisit())
                            .getDiancxxb_id()));
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


    public void getColor() {

    }

}