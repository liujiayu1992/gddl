package com.zhiren.jt.zdt.monthreport.tianb.zhil;
/* 
* ʱ�䣺2009-03-26 
* ���ߣ� ll
* �޸����ݣ�1������ѭ���ۼ��㷨��ɾ�����������ۼƣ��������ۼƺͱ������ݷ���������²��뱾���ۼơ�
* 		   2��ȥ����˺ͻ��˹��ܡ�
* 		   3������ж�ѭ���ۼƵ�ʱ�䣬�������û�����ݣ�ѭ����������
* 		   4���޸ı������ݣ���Ӱ���Ժ��·����ݵ����״̬��
* 		   5������ɾ���ж���ʾ��ɾ����������ʱ������ɱ��ж�Ӧ�ھ����ݣ�����ʾ����ɾ����
*/  
/* 
* ʱ�䣺2009-04-09 
* ���ߣ� ll
* �޸����ݣ��޸����¼�������ۼ�ʱ��yuetjkjb_id��ֵȡ���µ�yuetjkjb_id��
* 		   
*/
/* 
* ʱ�䣺2009-04-10 
* ���ߣ� ll
* �޸����ݣ�
* 		   �޸����¼�������ۼ�ʱ����������ͬ������yuetjkjb_idֵ��bug���⡣
*/ 
/* 
* ʱ�䣺2009-04-16
* ���ߣ� ll
* �޸����ݣ����ߺ��糧Ҫ������diancrz�ֶ�Ϊ�볧��λ��ֵ��qnet_ar�ֶ�Ϊ��ú���ۼ�Ȩ��λ��ֵ��
* 			ҳ����ʾʱ��ֻ�е糧��idΪ141ʱ����ʾdiancrz���ֶ����ݡ�
* 		   
*/ 
/* 
* ʱ�䣺2009-5-4 
* ���ߣ� sy
* �޸����ݣ�
* 		  �޸Ĳ�ѯ����sql������ֵȡ�������������룬
*         ���µ����ݴ����λʱ��������볧ú�ɱ��е��ܼƲ�ͬ��
*/ 
/* 
* ʱ�䣺2009-05-18
* ���ߣ� ll
* �޸����ݣ�����ƽ�׵糧��һ�����ƣ�ƽ��һ���ƽ�׶���ͬ��һ������ϵͳ����������ݡ�
* 		   �������糧�ͬһ���±�ҳ��ʱҳ�����������beginResponse()���������û�����Ϊ�糧����
  �жϵ�½�糧��糧���Ƿ�һ�£������¼���ˢ��ҳ�档
* 		   
*/ 
/* 
* ʱ�䣺2009-10-10
* ���ߣ� sy
* �޸����ݣ��޸����ɵ�sql����һ��round������λ�������ݿ���λ��һ�¡���roundΪround_new��
*          ����������ֵ�͵糧����һ��
* 		   
*/ 
/*
* ʱ�䣺2011-4-1
* ���ߣ� sy
* �޸����ݣ��޸ĵõ�gongysb_id��yuetjkjb_id ��getInt��ΪgetLong�����������getLong��������int�ᵼ�¾��Ȳ���
*
*
*/

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
import com.zhiren.common.ErrorMessage;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.NumberField;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

public class Yuezlb extends BasePage implements PageValidateListener {
    // �����û���ʾ
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
        ;
    }

    protected void initialize() {
        super.initialize();
        setMsg("");
        setTbmsg(null);
    }

    private String tbmsg;

    public String getTbmsg() {
        return tbmsg;
    }

    public void setTbmsg(String tbmsg) {
        this.tbmsg = tbmsg;
    }

    // ҳ��仯��¼
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    private long getKoujId(String strDate, String strDiancName, String strGongysName, String strJihkj, String strPinzbName, String strYunsfs) {
        String strSqly = "";
        long lngKoujID = 0;

        JDBCcon con = new JDBCcon();

//		�ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
//		strSqly="select nvl(max(id),0) as id from yuetjkjb where riq=to_date('"+strDate+"','yyyy-mm-dd') and diancxxb_id=getDiancId('"+strDiancName+"')\n"
//				+" and gongysb_id=getGongysId('"+strGongysName+"') and pinzb_id=get_Pinzb_Id('"+strPinzbName+"') "
//				+" and jihkjb_id=getJihkjbId('"+strJihkj+"') and yunsfsb_id=get_Yunsfsb_Id('"+strYunsfs+"')";

        strSqly = "select nvl(max(kj.id),0) as id from yuetjkjb kj,yueslb sl where kj.id=sl.yuetjkjb_id and kj.riq=to_date('" + strDate + "','yyyy-mm-dd') and kj.diancxxb_id=getDiancId('" + strDiancName + "')\n"
                + " and kj.gongysb_id=getGongysId('" + strGongysName + "') and kj.pinzb_id=get_Pinzb_Id('" + strPinzbName + "') "
                + " and kj.jihkjb_id=getJihkjbId('" + strJihkj + "') and kj.yunsfsb_id=get_Yunsfsb_Id('" + strYunsfs + "')";
        ResultSetList rec = con.getResultSetList(strSqly);
        if (!rec.next() || rec.getLong("id") == 0) {
            if (!strGongysName.equals("�ܼ�")) {
                lngKoujID = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                con.getInsert("insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
                        + lngKoujID + ",to_date('" + strDate + "','yyyy-mm-dd'),getDiancId('" + strDiancName + "'),0,getGongysId('" + strGongysName
                        + "'),get_Pinzb_Id('" + strPinzbName + "'),getJihkjbId('" + strJihkj + "'),get_Yunsfsb_Id('" + strYunsfs + "'))");
            }
        } else {
            lngKoujID = rec.getLong("id");
        }

        con.Close();
        return lngKoujID;
    }

    private void Save() {
        // ����������ݺ��·�������
        long intMonth;
        if (getYuefValue() == null) {
            intMonth = DateUtil.getMonth(new Date());
        } else {
            intMonth = getYuefValue().getId();
        }
        // ���·���1��ʱ����ʾ01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
        // --------------------------------
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();
        ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());
        boolean sc = false;//�ж���ʾ��ʾ��
        String gongysb_id = "";
        String jihkjb_id = "";
        String pinzb_id = "";
        String yunsfsb_id = "";
        StringBuffer sql_delete = new StringBuffer("begin \n");

        while (drsl.next()) {
            //**********************************************
            gongysb_id = drsl.getString("gongysb_id");
            jihkjb_id = drsl.getString("jihkjb_id");
            pinzb_id = drsl.getString("pinzb_id");
            yunsfsb_id = drsl.getString("yunsfsb_id");
            sql_delete.append("delete from ").append("yuezlb").append(
                    " where yuetjkjb_id =(select distinct tj.id from yuezlb z,yuetjkjb tj,meikdqb gy,jihkjb jh,yunsfsb ys,pinzb pz where z.yuetjkjb_id=tj.id and tj.jihkjb_id=jh.id and tj.gongysb_id=gy.id and tj.yunsfsb_id=ys.id and tj.pinzb_id=pz.id and gy.mingc='")
                    .append(gongysb_id).append("' and jh.mingc='").append(jihkjb_id).append("' and pz.mingc='").append(pinzb_id).append("' and ys.mingc='").append(yunsfsb_id).append("'and tj.diancxxb_id=")
                    .append(visit.getDiancxxb_id()).append(" and tj.riq=").append(CurrODate).append(");\n");
        }
        sql_delete.append("end;");
        if (sql_delete.length() > 11) {

            con.getUpdate(sql_delete.toString());
            LeijSelect();

        }
        if (sc == false) {
            ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
            StringBuffer sql = new StringBuffer();
            while (rsl.next()) {
                sql.delete(0, sql.length());
                sql.append("begin \n");
                long id = 0;
                gongysb_id = rsl.getString("gongysb_id");
                jihkjb_id = rsl.getString("jihkjb_id");
                pinzb_id = rsl.getString("pinzb_id");
                yunsfsb_id = rsl.getString("yunsfsb_id");
                String yuetjkjb_id = "select distinct tj.id as id from yuetjkjb tj,meikdqb gy,jihkjb jh,yunsfsb ys,pinzb pz where tj.jihkjb_id=jh.id and tj.gongysb_id=gy.id and tj.yunsfsb_id=ys.id and tj.pinzb_id=pz.id"
                        + "  and tj.diancxxb_id="
                        + visit.getDiancxxb_id()
                        + " and tj.jihkjb_id=jh.id and gy.mingc='"
                        + gongysb_id
                        + "' and jh.mingc='"
                        + jihkjb_id
                        + "' and ys.mingc='"
                        + yunsfsb_id
                        + "' and pz.mingc ='"
                        + pinzb_id
                        + "' and tj.riq=" + CurrODate;

                long yuetjkjbid = 0;
                ResultSet rs1 = con.getResultSet(yuetjkjb_id);
                try {
                    if (rs1.next()) {
                        yuetjkjbid = rs1.getLong("id");
                    }
                } catch (SQLException e) {
                    // TODO �Զ����� catch ��
                    e.printStackTrace();
                }
                if ("0".equals(rsl.getString("ID"))) {
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage()
                            .getVisit()).getDiancxxb_id()));

                    sql.append("insert into yuezlb("
                            + "ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                            + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                            + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF,diancrz)values("
                            + id + ",'����'," + yuetjkjbid + ","
                            + rsl.getDouble("QNET_AR") + ","
                            + rsl.getDouble("AAR") + ","
                            + rsl.getDouble("AD") + ","
                            + rsl.getDouble("VDAF") + ","
                            + rsl.getDouble("MT") + ","
                            + rsl.getDouble("STAD") + ","
                            + rsl.getDouble("AAD") + ","
                            + rsl.getDouble("MAD") + ","
                            + rsl.getDouble("QBAD") + ","
                            + rsl.getDouble("HAD") + ","
                            + rsl.getDouble("VAD") + ","
                            + rsl.getDouble("FCAD") + ","
                            + rsl.getDouble("STD") + ","
                            + rsl.getDouble("QBRAD") + ","
                            + rsl.getDouble("HDAF") + ","
                            + rsl.getDouble("QGRAD_DAF") + ","
                            + rsl.getDouble("SDAF") + ","
                            + rsl.getDouble("VAR") + ","
                            + rsl.getDouble("QNET_AR_KF") + ","
                            + rsl.getDouble("AAR_KF") + ","
                            + rsl.getDouble("AD_KF") + ","
                            + rsl.getDouble("VDAF_KF") + ","
                            + rsl.getDouble("MT_KF") + ","
                            + rsl.getDouble("STAD_KF") + ","
                            + rsl.getDouble("AAD_KF") + ","
                            + rsl.getDouble("MAD_KF") + ","
                            + rsl.getDouble("QBAD_KF") + ","
                            + rsl.getDouble("HAD_KF") + ","
                            + rsl.getDouble("VAD_KF") + ","
                            + rsl.getDouble("FCAD_KF") + ","
                            + rsl.getDouble("STD_KF") + ","
                            + rsl.getDouble("QBRAD_KF") + ","
                            + rsl.getDouble("HDAF_KF") + ","
                            + rsl.getDouble("QGRAD_DAF_KF") + ","
                            + rsl.getDouble("SDAF_KF") + ","
                            + rsl.getDouble("VAR_KF") + ","
                            + rsl.getDouble("diancrz") + ");\n");
                    // }
                } else {
                    sql.append("update yuezlb set yuetjkjb_id=" + yuetjkjbid
                            + ",QNET_AR=" + rsl.getDouble("QNET_AR") + ",AAR="
                            + rsl.getDouble("AAR") + ",AD=" + rsl.getDouble("AD")
                            + ",VDAF=" + rsl.getDouble("VDAF") + ",MT="
                            + rsl.getDouble("MT") + ",STAD="
                            + rsl.getDouble("STAD") + ",AAD="
                            + rsl.getDouble("AAD") + ",MAD=" + rsl.getDouble("MAD")
                            + ",QBAD=" + rsl.getDouble("QBAD") + ",HAD="
                            + rsl.getDouble("HAD") + ",VAD=" + rsl.getDouble("VAD")
                            + ",FCAD=" + rsl.getDouble("FCAD") + ",STD="
                            + rsl.getDouble("STD") + ",QBRAD="
                            + rsl.getDouble("QBRAD") + ",HDAF="
                            + rsl.getDouble("HDAF") + ",QGRAD_DAF="
                            + rsl.getDouble("QGRAD_DAF") + ",SDAF="
                            + rsl.getDouble("SDAF") + ",VAR="
                            + rsl.getDouble("VAR") + ",QNET_AR_KF="
                            + rsl.getDouble("QNET_AR_KF") + ",AAR_KF="
                            + rsl.getDouble("AAR_KF") + ",AD_KF="
                            + rsl.getDouble("AD_KF") + ",VDAF_KF="
                            + rsl.getDouble("VDAF_KF") + ",MT_KF="
                            + rsl.getDouble("MT_KF") + ",STAD_KF="
                            + rsl.getDouble("STAD_KF") + ",AAD_KF="
                            + rsl.getDouble("AAD_KF") + ",MAD_KF="
                            + rsl.getDouble("MAD_KF") + ",QBAD_KF="
                            + rsl.getDouble("QBAD_KF") + ",HAD_KF="
                            + rsl.getDouble("HAD_KF") + ",VAD_KF="
                            + rsl.getDouble("VAD_KF") + ",FCAD_KF="
                            + rsl.getDouble("FCAD_KF") + ",STD_KF="
                            + rsl.getDouble("STD_KF") + ",QBRAD_KF="
                            + rsl.getDouble("QBRAD_KF") + ",HDAF_KF="
                            + rsl.getDouble("HDAF_KF") + ",QGRAD_DAF_KF="
                            + rsl.getDouble("QGRAD_DAF_KF") + ",SDAF_KF="
                            + rsl.getDouble("SDAF_KF") + ",VAR_KF="
                            + rsl.getDouble("VAR_KF") + ",diancrz="
                            + rsl.getDouble("diancrz") + " where id="
                            + rsl.getLong("id") + ";\n");

                }
                sql.append("end;");
                con.getUpdate(sql.toString());
                //			����ۼ�
                LeijSelect();

            }
        }
        con.Close();
    }

    public void LeijSelect() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        // ����������ݺ��·�������
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
        // ���·���1��ʱ����ʾ01,
        String StrMonth = "";
        if (intMonth < 10) {

            StrMonth = "0" + intMonth;
        } else {
            StrMonth = "" + intMonth;
        }
//		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
//				+ getYuef() + "-01");
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        String sqlljcx = "";
        int i;
        for (i = 0; i <= 12 - intMonth; i++) {
            String leijrq = intyear + "-" + (intMonth + i) + "-01";
            boolean jzyf = false;//���ý�ֹ�·ݵ�boolean
            String pdyf = "select zl.id as id from yuezlb zl,yuetjkjb kj where zl.yuetjkjb_id = kj.id \n"
                    + "and kj.diancxxb_id=" + visit.getDiancxxb_id() + " and kj.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')";
            ResultSet pdyfsql = con.getResultSet(pdyf);
            try {
                //�ж�ѭ���·��Ƿ������ݣ�������jzyf��true,����jzyf��falseֹͣѭ����
                if (pdyfsql.next()) {
                    jzyf = true;
                } else {
                    continue;
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            } finally {
                con.Close();
            }
            if (jzyf == true) {//��ѭ���·������ݣ�ѭ���ۼ�ִ�У���ѭ���·������ݣ���ѭ��������

                if (intMonth + i != 1) {//��ѯ�ۼ�ֵ=����+�����ۼ�
                    sqlljcx = "select max(tj.id) as yuetjkjb_id, tj.diancxxb_id,tj.gongysb_id as gongysb_id,tj.jihkjb_id as jihkjb_id,tj.pinzb_id as pinzb_id,tj.yunsfsb_id as yunsfsb_id,\n"
                            + "       max(dc.mingc) as diancmc,max(meikdqb.mingc) as gongysbmc,max(jihkjb.mingc) as jihkjbmc,max(pinzb.mingc) as pinzbmc,max(yunsfsb.mingc) as yunsfsbmc,\n"
                            + "       max(tj.riq),nvl(sum(ys.laimsl),0),\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.diancrz)/sum(nvl(ys.laimsl,0)),2)) as diancrz,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qnet_ar)/sum(nvl(ys.laimsl,0)),2)) as qnet_ar,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.aar)/sum(nvl(ys.laimsl,0)),2)) as aar,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.ad)/sum(nvl(ys.laimsl,0)),2)) as ad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.vdaf)/sum(nvl(ys.laimsl,0)),2)) as vdaf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.mt)/sum(nvl(ys.laimsl,0)),2)) as mt,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.stad)/sum(nvl(ys.laimsl,0)),2)) as stad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.aad)/sum(nvl(ys.laimsl,0)),2)) as aad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.mad)/sum(nvl(ys.laimsl,0)),2)) as mad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qbad)/sum(nvl(ys.laimsl,0)),2)) as qbad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.had)/sum(nvl(ys.laimsl,0)),2)) as had,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.vad)/sum(nvl(ys.laimsl,0)),2)) as vad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.fcad)/sum(nvl(ys.laimsl,0)),2)) as fcad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.std)/sum(nvl(ys.laimsl,0)),2)) as std,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qbrad)/sum(nvl(ys.laimsl,0)),2)) as qbrad,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.hdaf)/sum(nvl(ys.laimsl,0)),2)) as hdaf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qgrad_daf)/sum(nvl(ys.laimsl,0)),2)) as qgrad_daf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.sdaf)/sum(nvl(ys.laimsl,0)),2)) as sdaf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.var)/sum(nvl(ys.laimsl,0)),2)) as var,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qnet_ar_kf)/sum(nvl(ys.laimsl,0)),2)) as qnet_ar_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.aar_kf)/sum(nvl(ys.laimsl,0)),2)) as aar_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.ad_kf)/sum(nvl(ys.laimsl,0)),2)) as ad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.vdaf_kf)/sum(nvl(ys.laimsl,0)),2)) as vdaf_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.mt_kf)/sum(nvl(ys.laimsl,0)),2)) as mt_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.stad_kf)/sum(nvl(ys.laimsl,0)),2)) as stad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.aad_kf)/sum(nvl(ys.laimsl,0)),2)) as aad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.mad_kf)/sum(nvl(ys.laimsl,0)),2)) as mad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qbad_kf)/sum(nvl(ys.laimsl,0)),2)) as qbad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.had_kf)/sum(nvl(ys.laimsl,0)),2)) as had_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.vad_kf)/sum(nvl(ys.laimsl,0)),2)) as vad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.fcad_kf)/sum(nvl(ys.laimsl,0)),2)) as fcad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.std_kf)/sum(nvl(ys.laimsl,0)),2)) as std_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qbrad_kf)/sum(nvl(ys.laimsl,0)),2)) as qbrad_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.hdaf_kf)/sum(nvl(ys.laimsl,0)),2)) as hdaf_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.qgrad_daf_kf)/sum(nvl(ys.laimsl,0)),2)) as qgrad_daf_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.sdaf_kf)/sum(nvl(ys.laimsl,0)),2)) as sdaf_kf,\n"
                            + "             decode(sum(nvl(ys.laimsl,0)),0,0,round(sum(nvl(ys.laimsl,0)*yz.var_kf)/sum(nvl(ys.laimsl,0)),2)) as var_kf\n"
                            + "          from  yueslb ys,yuezlb yz, meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc ,\n"
                            + "                (select t.*,sl.fenx from yuetjkjb t,yueslb sl\n"
                            + "                 where sl.yuetjkjb_id=t.id\n"
                            + "                         and ((t.riq = add_months(to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')"
                            + ", -1) and sl.fenx = '�ۼ�' )  or\n"
                            + "                             (t.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') "
                            + "and sl.fenx = '����')  )\n"
                            + "                        and t.diancxxb_id ="
                            + visit.getDiancxxb_id()
                            + " ) tj\n"
                            + "         where tj.id = ys.yuetjkjb_id and tj.id=yz.yuetjkjb_id\n"
                            + "			     and dc.id=tj.diancxxb_id \n"
                            + "              and tj.gongysb_id = meikdqb.id\n"
                            + "              and tj.jihkjb_id = jihkjb.id\n"
                            + "              and tj.pinzb_id = pinzb.id\n"
                            + "              and tj.yunsfsb_id = yunsfsb.id\n"
                            + "              and tj.fenx=ys.fenx\n"
                            + "              and ys.fenx=yz.fenx\n"
                            + "         group by(tj.diancxxb_id,tj.gongysb_id,tj.jihkjb_id,tj.pinzb_id,tj.yunsfsb_id)";
                } else {
                    sqlljcx =
                            "select zl.id,zl.yuetjkjb_id as yuetjkjb_id,tj.diancxxb_id,tj.gongysb_id as gongysb_id,tj.jihkjb_id as jihkjb_id," +
                                    "       tj.pinzb_id as pinzb_id,tj.yunsfsb_id as yunsfsb_id ,\n" +
                                    "		dc.mingc as diancmc,meikdqb.mingc as gongysbmc,jihkjb.mingc as jihkjbmc,pinzb.mingc as pinzbmc,yunsfsb.mingc as yunsfsbmc ,\n" +
                                    "              diancrz,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,\n" +
                                    "              VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF,\n" +
                                    "              QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF\n" +
                                    "       from yuetjkjb tj, yuezlb zl, meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc\n" +
                                    "       where tj.id = zl.yuetjkjb_id\n" +
                                    "             and tj.gongysb_id = meikdqb.id\n" +
                                    "             and tj.jihkjb_id = jihkjb.id\n" +
                                    "             and tj.pinzb_id = pinzb.id\n" +
                                    "             and tj.yunsfsb_id = yunsfsb.id\n" +
                                    "             and dc.id=tj.diancxxb_id\n " +
                                    "             and diancxxb_id =" + visit.getDiancxxb_id() + "\n" +
                                    "             and tj.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and fenx = '����'\n" +
                                    "       order by zl.id";
                }
                ResultSetList rsllj = con.getResultSetList(sqlljcx);
                if (rsllj.getRows() != 0) {

                    long yuezlbid = 0;
                    StringBuffer sqllj = new StringBuffer("begin \n");
//					��ȡ�ۼƵ����ۼ�״̬
                    String shzt =
                            "select max(zhuangt) as zhuangt from yuezlb zl,yuetjkjb kj where zl.yuetjkjb_id=kj.id and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                                    "       and kj.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')  and zl.fenx='�ۼ�'";

                    ResultSetList shenhzt = con.getResultSetList(shzt);
                    long zhuangt = 0;

                    while (shenhzt.next()) {
                        zhuangt = shenhzt.getLong("zhuangt");
                    }

                    String deletelj = "delete from yuezlb where id in(select zl.id from yuezlb zl,yuetjkjb k \n" +
                            "where zl.yuetjkjb_id=k.id and k.riq= to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                            "	   and zl.fenx='�ۼ�' and k.diancxxb_id=" + visit.getDiancxxb_id() + ");\n";
                    sqllj.append(deletelj);

                    while (rsllj.next()) {
                        String kjidsql =
                                "select zl.yuetjkjb_id as yuetjkjbid from yuetjkjb kj,yuezlb zl\n" +
                                        "where zl.yuetjkjb_id=kj.id and kj.diancxxb_id=" + rsllj.getLong("diancxxb_id") + " and kj.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id=" + rsllj.getLong("gongysb_id") + " and kj.jihkjb_id=" + rsllj.getLong("jihkjb_id") +
                                        " and kj.yunsfsb_id=" + rsllj.getLong("yunsfsb_id") + " and kj.pinzb_id=" + rsllj.getLong("pinzb_id") + "\n" +
                                        " and zl.fenx='����'";
                        long kjb_id = 0;
                        ResultSet kjid = con.getResultSet(kjidsql);
                        try {
                            while (kjid.next()) {
                                kjb_id = kjid.getLong("yuetjkjbid");
                            }
                        } catch (SQLException e1) {
                            e1.fillInStackTrace();
                        } finally {
                            con.Close();
                        }

                        if (kjb_id == 0) {

                            kjb_id = getKoujId(leijrq, rsllj.getString("diancmc"), rsllj.getString("gongysbmc"), rsllj.getString("jihkjbmc"), rsllj.getString("pinzbmc"), rsllj.getString("yunsfsbmc"));

                        }

                        yuezlbid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        sqllj.append("insert into yuezlb("
                                + "ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                                + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                                + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF,zhuangt,diancrz)values("
                                + yuezlbid + ",'�ۼ�'," + kjb_id
                                + ","
                                + rsllj.getDouble("QNET_AR") + ","
                                + rsllj.getDouble("AAR") + ","
                                + rsllj.getDouble("AD") + ","
                                + rsllj.getDouble("VDAF") + ","
                                + rsllj.getDouble("MT") + ","
                                + rsllj.getDouble("STAD") + ","
                                + rsllj.getDouble("AAD") + ","
                                + rsllj.getDouble("MAD") + ","
                                + rsllj.getDouble("QBAD") + ","
                                + rsllj.getDouble("HAD") + ","
                                + rsllj.getDouble("VAD") + ","
                                + rsllj.getDouble("FCAD") + ","
                                + rsllj.getDouble("STD") + ","
                                + rsllj.getDouble("QBRAD") + ","
                                + rsllj.getDouble("HDAF") + ","
                                + rsllj.getDouble("QGRAD_DAF") + ","
                                + rsllj.getDouble("SDAF") + ","
                                + rsllj.getDouble("VAR") + ","
                                + rsllj.getDouble("QNET_AR_KF") + ","
                                + rsllj.getDouble("AAR_KF") + ","
                                + rsllj.getDouble("AD_KF") + ","
                                + rsllj.getDouble("VDAF_KF") + ","
                                + rsllj.getDouble("MT_KF") + ","
                                + rsllj.getDouble("STAD_KF") + ","
                                + rsllj.getDouble("AAD_KF") + ","
                                + rsllj.getDouble("MAD_KF") + ","
                                + rsllj.getDouble("QBAD_KF") + ","
                                + rsllj.getDouble("HAD_KF") + ","
                                + rsllj.getDouble("VAD_KF") + ","
                                + rsllj.getDouble("FCAD_KF") + ","
                                + rsllj.getDouble("STD_KF") + ","
                                + rsllj.getDouble("QBRAD_KF") + ","
                                + rsllj.getDouble("HDAF_KF") + ","
                                + rsllj.getDouble("QGRAD_DAF_KF") + ","
                                + rsllj.getDouble("SDAF_KF") + ","
                                + rsllj.getDouble("VAR_KF") + ","
                                + zhuangt + "," + rsllj.getDouble("diancrz") + ");\n");
                    }
                    sqllj.append("end;");
                    if (yuezlbid != 0) {
                        con.getInsert(sqllj.toString());
                    }
                }
                con.Close();
            }
        }
    }

    private boolean _SaveChick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveChick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }

    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveChick) {
            _SaveChick = false;
            Save();
            getSelectData(null);
        }
        if (_Refreshclick) {
            _Refreshclick = false;
            setRiq();
            getSelectData(null);
        }
        if (_CreateClick) {
            _CreateClick = false;
            CreateData();
            getSelectData(null);
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
    }

    public void DelData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String CurrZnDate = getNianf() + "��" + getYuef() + "��";
        String riq = getNianf() + "-" + getYuef();
        String sql = "select * from yuercbmdj z inner join YUETJKJB t on z.YUETJKJB_ID=t.id\n" +
                "where t.diancxxb_id=" + visit.getDiancxxb_id() + " and z.fenx='����' and to_char(t.riq,'yyyy-MM')='" + riq + "'";
        ResultSet rs = con.getResultSet(sql);
        try {
            //�ж��Ƿ�������sc��true ����ѭ����ִ��ɾ���ͱ���,sc��falseִ��ɾ���ͱ��档
            if (rs.next()) {
                setMsg("ɾ��������ǰ������ɾ���볧ú�ɱ��еĶ�Ӧ���ݣ�");
                rs.close();
                con.Close();
                return;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            con.Close();
        }

        String strSql =
                "delete from yuezlb where yuetjkjb_id in (select id from yuetjkjb where to_char(riq,'yyyy-MM')='"
                        + riq + "' and diancxxb_id=" + getTreeid() + ")";
        int flag = con.getDelete(strSql);
        if (flag == -1) {
            WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
            setMsg("ɾ�������з�������");
        } else {
            setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
        }
        con.Close();
    }

    public void CreateData() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String CurrZnDate = getNianf() + "��" + getYuef() + "��";
        String CurrSDate = getNianf() + "-" + getYuef();
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        int intYuef = Integer.parseInt(getYuef());
        String LastODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + (intYuef < 11 ? "0" + (intYuef - 1) : (intYuef - 1) + "")
                + "-01");
        String strSql = "";
        int flag;
        long lngId = 0;
        String alert="";
        strSql= " select *  from fahb f,zhilb z\n" +
                "       where  daohrq>=to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd') and  daohrq<=last_day(to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd'))\n" +
                "      and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                "      and f.zhilb_id=z.id(+)\n" ;
        ResultSetList rsl = con.getResultSetList(strSql);
        while (rsl.next()){
            String huaybh=rsl.getString("HUAYBH");
            if("".equals(huaybh)){
                alert="���������ݲ�ȫ�벹ȫ���鱨��!";
                break;
            }
        }
        rsl.close();
        // ȡ����Ӧ�̵���������
        // ----------------------------------Ʒ��Ϊԭú--------------------------------------------
        strSql =
                "select /*nvl(xuh,0) as xuh,*/gongysb_id, jihkjb_id,yunsfsb_id,sum(f.laimsl) as laimsl,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.qnet_ar)/sum(f.laimzl),2)) as qnet_ar,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.aar)/sum(f.laimzl),2)) as aar,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.ad)/sum(f.laimzl),2)) as ad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.vdaf)/sum(f.laimzl),2)) as vdaf,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.mt)/sum(f.laimzl),1)) as mt,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.stad)/sum(f.laimzl),2)) as stad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.aad)/sum(f.laimzl),2)) as aad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.mad)/sum(f.laimzl),2)) as mad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.qbad)/sum(f.laimzl),2)) as qbad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.had)/sum(f.laimzl),2)) as had,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.vad)/sum(f.laimzl),2)) as vad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.fcad)/sum(f.laimzl),2)) as fcad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.std)/sum(f.laimzl),2)) as std,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.qbrad)/sum(f.laimzl),2)) as qbrad,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.hdaf)/sum(f.laimzl),2)) as hdaf,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.qgrad_daf)/sum(f.laimzl),2)) as qgrad_daf,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.sdaf)/sum(f.laimzl),2)) as sdaf,\n" +
                        "      decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*f.var)/sum(f.laimzl),2)) as var\n" +
                        " from\n" +
                        "     (select max(gy.dqxh) as xuh,gy.dqid as gongysb_id , jihkjb_id,yunsfsb_id,\n" +
                        "             (round_new(sum(biaoz),0)+round_new(sum(yingd),0)-round_new(sum(yingd-yingk),0))  as laimsl,\n" +
                        "              sum(f.laimzl) as laimzl,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.qnet_ar)/sum(f.laimzl),3)) as qnet_ar,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.aar)/sum(f.laimzl),2)) as aar,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.ad)/sum(f.laimzl),2)) as ad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.vdaf)/sum(f.laimzl),2)) as vdaf,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.mt)/sum(f.laimzl),1)) as mt,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.stad)/sum(f.laimzl),2)) as stad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.aad)/sum(f.laimzl),2)) as aad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.mad)/sum(f.laimzl),2)) as mad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.qbad)/sum(f.laimzl),3)) as qbad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.had)/sum(f.laimzl),2)) as had,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.vad)/sum(f.laimzl),2)) as vad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.fcad)/sum(f.laimzl),2)) as fcad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.std)/sum(f.laimzl),2)) as std,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.qgrad)/sum(f.laimzl),2)) as qbrad,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.hdaf)/sum(f.laimzl),2)) as hdaf,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.qgrad_daf)/sum(f.laimzl),3)) as qgrad_daf,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.sdaf)/sum(f.laimzl),2)) as sdaf,\n" +
                        "              decode(sum(f.laimzl),0,0,round_new(sum(f.laimzl*z.var)/sum(f.laimzl),2)) as var\n" +
                        "      from fahb f,vwgongysdq gy,zhilb z\n" +
                        "       where  daohrq>=to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd') and  daohrq<=last_day(to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd'))\n" +
                        "      and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                        "      and f.meikxxb_id=gy.mk_id\n" +
                        "      and f.zhilb_id=z.id(+)\n" +
                        "      group by lieid,gy.dqid ,jihkjb_id,yunsfsb_id)  f\n" +
                        " group by /*xuh,*/gongysb_id, jihkjb_id,yunsfsb_id";

        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
                    + strSql);
            setMsg(ErrorMessage.NullResult);
            con.rollBack();
            con.Close();
            return;
        }
        while (rs.next()) {
            // �ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
            strSql = "select *  from yuetjkjb where riq=" + CurrODate + " \n"
                    + " and gongysb_id="
                    + rs.getLong("gongysb_id")
                    + " and  pinzb_id=(select max(id) from pinzb where mingc='ԭú')"//+rs.getInt("pinzb_id")
                    + " and jihkjb_id=" + rs.getInt("jihkjb_id")
                    + " and yunsfsb_id=" + rs.getInt("yunsfsb_id")
                    + "  and diancxxb_id=" + visit.getDiancxxb_id();
            ResultSetList rec = con.getResultSetList(strSql);
            if (rec == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
                        + strSql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            if (rec.next()) {
                lngId = rec.getLong("id");
            } else {
                lngId = Long.parseLong(MainGlobal.getNewID(visit
                        .getDiancxxb_id()));
                strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
                        + lngId
                        + ","
                        + CurrODate
                        + ","
                        + visit.getDiancxxb_id()
                        + ","
                        + rs.getInt("xuh")
                        + ","
                        + rs.getLong("gongysb_id")
                        + ",(select max(id) from pinzb where mingc='ԭú')"// +rs.getInt("pinzb_id")
                        + ","
                        + rs.getInt("jihkjb_id")
                        + ","
                        + rs.getInt("yunsfsb_id") + ")";
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                            + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
                    con.rollBack();
                    con.Close();
                    return;
                }
            }
            strSql = "delete from yuezlb where yuetjkjb_id=" + lngId;
            flag = con.getDelete(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:"
                        + strSql);
                setMsg("���ɹ��̳��ִ���������ɾ��ʧ�ܣ�");
                con.rollBack();
                con.Close();
                return;
            }
            strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                    + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                    + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                    + Long.parseLong(MainGlobal
                    .getNewID(visit.getDiancxxb_id()))
                    + ",'����',"
                    + lngId
                    + ","
                    + rs.getDouble("qnet_ar")
                    + ","
                    + rs.getDouble("AAR")
                    + ","
                    + rs.getDouble("AD")
                    + ","
                    + rs.getDouble("VDAF")
                    + ","
                    + rs.getDouble("MT")
                    + ","
                    + rs.getDouble("STAD")
                    + ","
                    + rs.getDouble("AAD")
                    + ","
                    + rs.getDouble("MAD")
                    + ","
                    + rs.getDouble("QBAD")
                    + ","
                    + rs.getDouble("HAD")
                    + ","
                    + rs.getDouble("VAD")
                    + ","
                    + rs.getDouble("FCAD")
                    + ","
                    + rs.getDouble("STD")
                    + ","
                    + rs.getDouble("QBRAD")
                    + ","
                    + rs.getDouble("HDAF")
                    + ","
                    + rs.getDouble("QGRAD_DAF")
                    + ","
                    + rs.getDouble("SDAF")
                    + ","
                    + rs.getDouble("VAR")
                    + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
            flag = con.getInsert(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
                        + strSql);
                setMsg("���ɹ��̳��ִ�������������ʧ�ܣ�");
                con.rollBack();
                con.Close();
                return;
            }
            // �����ۼ�,1�·�ʱ�����µ����ۼơ���֮������ϸ���û�д�����¼��Ϣ�����µ����ۼơ��б����ۼ�=�����ۼ�+����ֵ
            if (intYuef != 1) {
                strSql = "select zl.*,sl.laimsl from yuetjkjb tj,yuezlb zl,yueslb sl  where sl.yuetjkjb_id=tj.id  "
                        + "and tj.id=zl.yuetjkjb_id and riq="
                        + LastODate
                        + " and diancxxb_id="
                        + visit.getDiancxxb_id()
                        + "\n"
                        + " and gongysb_id="
                        + rs.getLong("gongysb_id") + " and pinzb_id=(select max(id) from pinzb where mingc='ԭú')"//+rs.getInt("pinzb_id")
                        + " and jihkjb_id="
                        + rs.getInt("jihkjb_id")
                        + " and yunsfsb_id="
                        + rs.getInt("yunsfsb_id")
                        + " and sl.fenx=zl.fenx and zl.fenx='�ۼ�'";
                ResultSetList rsSy = con.getResultSetList(strSql);
                if (rsSy == null) {
                    WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
                            + strSql);
                    setMsg(ErrorMessage.NullResult);
                    con.rollBack();
                    con.Close();
                    return;
                }
                if (rsSy.next()) {
                    strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                            + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                            + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                            + Long.parseLong(MainGlobal.getNewID(visit
                            .getDiancxxb_id()))
                            + ",'�ۼ�',"
                            + lngId
                            + ","
                            + ((rs.getDouble("qnet_ar")
                            * rs.getDouble("laimsl") + rsSy
                            .getDouble("qnet_ar")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("AAR") * rs.getDouble("laimsl") + rsSy
                            .getDouble("AAR")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("AD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("AD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("VDAF") * rs.getDouble("laimsl") + rsSy
                            .getDouble("VDAF")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("MT") * rs.getDouble("laimsl") + rsSy
                            .getDouble("MT")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("STAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("STAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("AAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("AAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("MAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("MAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("QBAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("QBAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("HAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("HAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("VAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("VAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("FCAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("FCAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("STD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("STD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("QBRAD") * rs.getDouble("laimsl") + rsSy
                            .getDouble("QBRAD")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("HDAF") * rs.getDouble("laimsl") + rsSy
                            .getDouble("HDAF")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("QGRAD_DAF")
                            * rs.getDouble("laimsl") + rsSy
                            .getDouble("QGRAD_DAF")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("SDAF") * rs.getDouble("laimsl") + rsSy
                            .getDouble("SDAF")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + ((rs.getDouble("VAR") * rs.getDouble("laimsl") + rsSy
                            .getDouble("VAR")
                            * rsSy.getDouble("laimsl")) / (rs
                            .getDouble("laimsl") + rsSy
                            .getDouble("laimsl")))
                            + ","
                            + rsSy.getDouble("QNET_AR_KF")
                            + ","
                            + rsSy.getDouble("AAR_KF")
                            + ","
                            + rsSy.getDouble("AD_KF")
                            + ","
                            + rsSy.getDouble("VDAF_KF")
                            + ","
                            + rsSy.getDouble("MT_KF")
                            + ","
                            + rsSy.getDouble("STAD_KF")
                            + ","
                            + rsSy.getDouble("AAD_KF")
                            + ","
                            + rsSy.getDouble("MAD_KF")
                            + ","
                            + rsSy.getDouble("QBAD_KF")
                            + ","
                            + rsSy.getDouble("HAD_KF")
                            + ","
                            + rsSy.getDouble("VAD_KF")
                            + ","
                            + rsSy.getDouble("FCAD_KF")
                            + ","
                            + rsSy.getDouble("STD_KF")
                            + ","
                            + rsSy.getDouble("QBRAD_KF")
                            + ","
                            + rsSy.getDouble("HDAF_KF")
                            + ","
                            + rsSy.getDouble("QGRAD_DAF_KF")
                            + ","
                            + rsSy.getDouble("SDAF_KF")
                            + ","
                            + rsSy.getDouble("VAR_KF") + ")";
                } else { // �ϸ���û�д�����¼��Ϣ�����µ����ۼ�
                    strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                            + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                            + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                            + Long.parseLong(MainGlobal.getNewID(visit
                            .getDiancxxb_id()))
                            + ",'�ۼ�',"
                            + lngId
                            + ","
                            + rs.getDouble("qnet_ar")
                            + ","
                            + rs.getDouble("AAR")
                            + ","
                            + rs.getDouble("AD")
                            + ","
                            + rs.getDouble("VDAF")
                            + ","
                            + rs.getDouble("MT")
                            + ","
                            + rs.getDouble("STAD")
                            + ","
                            + rs.getDouble("AAD")
                            + ","
                            + rs.getDouble("MAD")
                            + ","
                            + rs.getDouble("QBAD")
                            + ","
                            + rs.getDouble("HAD")
                            + ","
                            + rs.getDouble("VAD")
                            + ","
                            + rs.getDouble("FCAD")
                            + ","
                            + rs.getDouble("STD")
                            + ","
                            + rs.getDouble("QBRAD")
                            + ","
                            + rs.getDouble("HDAF")
                            + ","
                            + rs.getDouble("QGRAD_DAF")
                            + ","
                            + rs.getDouble("SDAF")
                            + ","
                            + rs.getDouble("VAR")
                            + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
                }
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                            + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ����������ۼƲ���ʧ�ܣ�");
                    con.rollBack();
                    con.Close();
                    return;
                }
            } else { // 1�·�ʱ�����µ����ۼ�
                strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                        + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                        + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                        + Long.parseLong(MainGlobal.getNewID(visit
                        .getDiancxxb_id()))
                        + ",'�ۼ�',"
                        + lngId
                        + ","
                        + rs.getDouble("qnet_ar")
                        + ","
                        + rs.getDouble("AAR")
                        + ","
                        + rs.getDouble("AD")
                        + ","
                        + rs.getDouble("VDAF")
                        + ","
                        + rs.getDouble("MT")
                        + ","
                        + rs.getDouble("STAD")
                        + ","
                        + rs.getDouble("AAD")
                        + ","
                        + rs.getDouble("MAD")
                        + ","
                        + rs.getDouble("QBAD")
                        + ","
                        + rs.getDouble("HAD")
                        + ","
                        + rs.getDouble("VAD")
                        + ","
                        + rs.getDouble("FCAD")
                        + ","
                        + rs.getDouble("STD")
                        + ","
                        + rs.getDouble("QBRAD")
                        + ","
                        + rs.getDouble("HDAF")
                        + ","
                        + rs.getDouble("QGRAD_DAF")
                        + ","
                        + rs.getDouble("SDAF")
                        + ","
                        + rs.getDouble("VAR")
                        + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                            + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ����������ۼƲ���ʧ�ܣ�");
                    con.rollBack();
                    con.Close();
                    return;
                }
            }
        }

        if (intYuef != 1) {// ȡ�ϸ����������û�е����ݣ�����ֵΪ0���ۼ�ֵ�����ϸ����ۼ�
            strSql = "select sl.*,tj.* from yuezlb sl,yuetjkjb tj \n"
                    + " where sl.yuetjkjb_id=tj.id and riq=" + LastODate
                    + " \n" + " and fenx='�ۼ�' and diancxxb_id="
                    + visit.getDiancxxb_id();
            ResultSetList rssylj = con.getResultSetList(strSql);
            if (rssylj == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
                        + strSql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            while (rssylj.next()) {
                strSql = "select kj.* from yuezlb zl,yuetjkjb kj where zl.yuetjkjb_id=kj.id and kj.riq=" + CurrODate + " and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                        + " and kj.gongysb_id="
                        + rssylj.getLong("gongysb_id") + " and  kj.pinzb_id=(select max(id) from pinzb where mingc='ԭú')"//+rssylj.getInt("pinzb_id")
                        + " and kj.jihkjb_id=" + rssylj.getInt("jihkjb_id")
                        + " and kj.yunsfsb_id=" + rssylj.getInt("yunsfsb_id");
                ResultSetList recby = con.getResultSetList(strSql);
                if (rssylj == null) {
                    WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:"
                            + strSql);
                    setMsg(ErrorMessage.NullResult);
                    con.rollBack();
                    con.Close();
                    return;
                }
                if (!recby.next()) {
                    strSql = "select kj.* from yueslb sl,yuetjkjb kj where sl.yuetjkjb_id=kj.id and kj.riq=" + CurrODate + " and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                            + " and kj.gongysb_id="
                            + rssylj.getLong("gongysb_id") + " and  kj.pinzb_id=(select max(id) from pinzb where mingc='ԭú')"//+rssylj.getInt("pinzb_id")
                            + " and kj.jihkjb_id=" + rssylj.getInt("jihkjb_id")
                            + " and kj.yunsfsb_id=" + rssylj.getInt("yunsfsb_id");
                    ResultSetList slkj = con.getResultSetList(strSql);
                    if (slkj.next()) {
                        lngId = slkj.getLong("id");
                        flag = 0;
                    } else {
                        flag = -1;
                    }
                    if (flag == -1) {
                        WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                                + "SQL:" + strSql);
                        setMsg("���ɹ��̳��ִ���,������δ����ɹ�������������������ݣ�");
                        con.rollBack();
                        con.Close();
                        return;
                    }
//					lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
//					strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
//							+ lngId
//							+ ","
//							+ CurrODate
//							+ ","
//							+ visit.getDiancxxb_id()
//							+ ","
//							+ rssylj.getInt("xuh")
//							+ ","
//							+ rssylj.getInt("gongysb_id")
//							+ ","
//							+ rssylj.getInt("pinzb_id")
//							+ ","
//							+ rssylj.getInt("jihkjb_id")
//							+ ","
//							+ rssylj.getInt("yunsfsb_id") + ")";
//					flag = con.getInsert(strSql);
//					if (flag == -1) {
//						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
//								+ "SQL:" + strSql);
//						setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
//						con.rollBack();
//						con.Close();
//						return;
//					}
                    strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                            + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                            + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                            + Long.parseLong(MainGlobal.getNewID(visit
                            .getDiancxxb_id()))
                            + ",'����',"
                            + lngId
                            + ",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
                    flag = con.getInsert(strSql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                                + "SQL:" + strSql);
                        setMsg("���ɹ��̳��ִ���������δ����ɹ���");
                        con.rollBack();
                        con.Close();
                        return;
                    }
                    strSql = "insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                            + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                            + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
                            + Long.parseLong(MainGlobal.getNewID(visit
                            .getDiancxxb_id()))
                            + ",'�ۼ�',"
                            + lngId
                            + ","
                            + rssylj.getDouble("qnet_ar")
                            + ","
                            + rssylj.getDouble("AAR")
                            + ","
                            + rssylj.getDouble("AD")
                            + ","
                            + rssylj.getDouble("VDAF")
                            + ","
                            + rssylj.getDouble("MT")
                            + ","
                            + rssylj.getDouble("STAD")
                            + ","
                            + rssylj.getDouble("AAD")
                            + ","
                            + rssylj.getDouble("MAD")
                            + ","
                            + rssylj.getDouble("QBAD")
                            + ","
                            + rssylj.getDouble("HAD")
                            + ","
                            + rssylj.getDouble("VAD")
                            + ","
                            + rssylj.getDouble("FCAD")
                            + ","
                            + rssylj.getDouble("STD")
                            + ","
                            + rssylj.getDouble("QBRAD")
                            + ","
                            + rssylj.getDouble("HDAF")
                            + ","
                            + rssylj.getDouble("QGRAD_DAF")
                            + ","
                            + rssylj.getDouble("SDAF")
                            + ","
                            + rssylj.getDouble("VAR")
                            + ","
                            + rssylj.getDouble("qnet_ar_KF")
                            + ","
                            + rssylj.getDouble("AAR_KF")
                            + ","
                            + rssylj.getDouble("AD_KF")
                            + ","
                            + rssylj.getDouble("VDAF_KF")
                            + ","
                            + rssylj.getDouble("MT_KF")
                            + ","
                            + rssylj.getDouble("STAD_KF")
                            + ","
                            + rssylj.getDouble("AAD_KF")
                            + ","
                            + rssylj.getDouble("MAD_KF")
                            + ","
                            + rssylj.getDouble("QBAD_KF")
                            + ","
                            + rssylj.getDouble("HAD_KF")
                            + ","
                            + rssylj.getDouble("VAD_KF")
                            + ","
                            + rssylj.getDouble("FCAD_KF")
                            + ","
                            + rssylj.getDouble("STD_KF")
                            + ","
                            + rssylj.getDouble("QBRAD_KF")
                            + ","
                            + rssylj.getDouble("HDAF_KF")
                            + ","
                            + rssylj.getDouble("QGRAD_DAF_KF")
                            + ","
                            + rssylj.getDouble("SDAF_KF")
                            + ","
                            + rssylj.getDouble("VAR_KF") + ")";
                    flag = con.getInsert(strSql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail
                                + "SQL:" + strSql);
                        setMsg("���ɹ��̳��ִ����������ۼ�δ����ɹ���");
                        con.rollBack();
                        con.Close();
                        return;
                    }
                }
//				else{
//					lngId=recby.getLong("id");
//					 strSql="delete from yuezlb  where yuetjkjb_id="+lngId;
//					 flag = con.getDelete(strSql);
//					  if(flag == -1) {
//						  WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
//						  setMsg("���ɹ��̳��ִ���������ɾ��ʧ�ܣ�"); con.rollBack(); con.Close();
//						  return;
//					  }
//
//					  strSql="insert into  yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF," +
//							  "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, " +
//							  "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
//							  +Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
//							  +",'����',"
//							  +lngId+",0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
//					  flag = con.getInsert(strSql); if(flag == -1) {
//						  WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
//						  setMsg("���ɹ��̳��ִ���������δ����ɹ���"); con.rollBack(); con.Close();
//						  return;
//					  } strSql="insert into yuezlb(ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF," +
//							   "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, " +
//							   "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF) values("
//							  +Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
//							  +",'�ۼ�'," +lngId
//							  +","
//							  +rssylj.getDouble("qnet_ar")
//							  +","
//							  +rssylj.getDouble("AAR")
//							  +","
//							  +rssylj.getDouble("AD")
//							  +","
//							  +rssylj.getDouble("VDAF")
//							  +","
//							  +rssylj.getDouble("MT")
//							  +","
//							  +rssylj.getDouble("STAD")
//							  +","
//							  +rssylj.getDouble("AAD")
//							  +","
//							  +rssylj.getDouble("MAD")
//							  +","
//							  +rssylj.getDouble("QBAD")
//							  +","
//							  +rssylj.getDouble("HAD")
//							  +","
//							  +rssylj.getDouble("VAD")
//							  +","
//							  +rssylj.getDouble("FCAD")
//							  +","
//							  +rssylj.getDouble("STD")
//							  +","
//							  +rssylj.getDouble("QBRAD")
//							  +","
//							  +rssylj.getDouble("HDAF")
//							  +","
//							  +rssylj.getDouble("QGRAD_DAF")
//							  +","
//							  +rssylj.getDouble("SDAF")
//							  +","
//							  +rssylj.getDouble("VAR")
//							  +","
//							  +rssylj.getDouble("qnet_ar_KF")
//							  +","
//							  +rssylj.getDouble("AAR_KF")
//							  +","
//							  +rssylj.getDouble("AD_KF")
//							  +","
//							  +rssylj.getDouble("VDAF_KF")
//							  +","
//							  +rssylj.getDouble("MT_KF")
//							  +","
//							  +rssylj.getDouble("STAD_KF")
//							  +","
//							  +rssylj.getDouble("AAD_KF")
//							  +","
//							  +rssylj.getDouble("MAD_KF")
//							  +","
//							  +rssylj.getDouble("QBAD_KF")
//							  +","
//							  +rssylj.getDouble("HAD_KF")
//							  +","
//							  +rssylj.getDouble("VAD_KF")
//							  +","
//							  +rssylj.getDouble("FCAD_KF")
//							  +","
//							  +rssylj.getDouble("STD_KF")
//							  +","
//							  +rssylj.getDouble("QBRAD_KF")
//							  +","
//							  +rssylj.getDouble("HDAF_KF")
//							  +","
//							  +rssylj.getDouble("QGRAD_DAF_KF")
//							  +","
//							  +rssylj.getDouble("SDAF_KF")
//							  +","
//							  +rssylj.getDouble("VAR_KF")+")";
//					  flag = con.getInsert(strSql);
//					  if(flag == -1) {
//						  WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
//						  setMsg("���ɹ��̳��ִ����������ۼ�δ����ɹ���"); con.rollBack(); con.Close();
//						  return;
//					  }
//				  }

            }
        }
        con.commit();
        con.Close();
        setMsg(CurrZnDate + "�����ݳɹ����ɣ�"+alert);
    }

    public void getSelectData(ResultSetList rsl) {
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) getPage().getVisit();
        String zhuangt = "";
        String zhuangt1 = "";
        if (visit.isShifsh() == true) {
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
                zhuangt1 = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (zl.zhuangt=1 or zl.zhuangt=2)";
                zhuangt1 = " and (sl.zhuangt=1 or sl.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and zl.zhuangt=2";
                zhuangt1 = " and sl.zhuangt=2";
            }
        }
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"
                + getYuef() + "-01");
        int intYuef = Integer.parseInt(getYuefValue().getValue());
        // ----------�糧��--------------
        String str = "";

        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {// ѡ����ʱˢ�³����еĵ糧
            str = "";
        } else if (treejib == 2) {// ѡ�ֹ�˾��ʱ��ˢ�³��ֹ�˾�����еĵ糧
            str = "and (dc.id = " + getTreeid() + " or dc.fuid = "
                    + getTreeid() + ")";

        } else if (treejib == 3) {// ѡ�糧ֻˢ�³��õ糧
            str = "and dc.id = " + getTreeid() + "";

        }
        // ---------------------------------------------------------------------
        // if(isReture) {
        // setMsg("�������Ҫ������Ϣʱ����ѡ����Ҫ���˵Ĺ�˾��糧���ƣ�");
        // }
        // ----------------------------------------------------------------------
        String strSql = "";
        if (rsl == null) {

            strSql =


                    "select  max(Nvl(Yz.Id, 0)) As Id,  \n" +
                            "        Yz.Yuetjkjb_Id As Yuetjkjb_Id,\n" +
                            "     		  Decode(Grouping(Title.Gongysmc), 1, '�ܼ�', Title.Gongysmc) As Gongysb_Id,\n" +
                            "     	  	 Decode(Grouping( Title.Jihkjbmc), 1, '-',  Title.Jihkjbmc) As Jihkjb_Id,\n" +
                            "    		 Decode(Grouping(Title.Pinzmc), 1, '-', Title.Pinzmc) As Pinzb_Id,\n" +
                            "       	 Decode(Grouping(Title.Yunsfsmc), 1, '-', Title.Yunsfsmc) As Yunsfsb_Id,\n" +
                            "    		  '����' as fenx,\n" +
                            "     		 sum(Nvl(Ys.Laimsl, 0)) As Laimsl,\n" +
                            //-------------------�ߺ��糧Ҫ������diancrz�ֶ�Ϊ�볧��λ��ֵ------------------
                            "          Nvl(Decode(sum(Ys.Laimsl),0,0,Round(sum(Ys.Laimsl * Yz.Diancrz )/Sum( Ys.Laimsl), 2)),0) As Diancrz,\n" +
                            //--------------------qnet_ar�ֶ�Ϊ��ú���ۼ�Ȩ��λ��ֵ---------------------------
                            "          Nvl(Decode(sum(Ys.Laimsl), 0, 0, Round(sum(Ys.Laimsl * Yz.Qnet_Ar) /Sum( Ys.Laimsl), 2)),0) As Qnet_Ar,\n" +
                            "      		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.aar)/sum(Ys.laimsl),2)),0) as aar,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.ad)/sum(Ys.laimsl),2)),0) as ad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.vdaf)/sum(Ys.laimsl),2)),0) as vdaf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.mt)/sum(Ys.laimsl),2)),0) as mt,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.stad)/sum(Ys.laimsl),2)),0) as stad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.aad)/sum(Ys.laimsl),2)),0) as aad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.mad)/sum(Ys.laimsl),2)),0) as mad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qbad)/sum(Ys.laimsl),2)),0) as qbad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.had)/sum(Ys.laimsl),2)),0) as had,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.vad)/sum(Ys.laimsl),2)),0) as vad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.fcad)/sum(Ys.laimsl),2)),0) as fcad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.std)/sum(Ys.laimsl),2)),0) as std,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qbrad)/sum(Ys.laimsl),2)),0) as qbrad,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.hdaf)/sum(Ys.laimsl),2)),0) as hdaf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qgrad_daf)/sum(Ys.laimsl),2)),0) as qgrad_daf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.sdaf)/sum(Ys.laimsl),2)),0) as sdaf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.var)/sum(Ys.laimsl),2)),0) as var,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qnet_ar_kf)/sum(Ys.laimsl),2)),0) as qnet_ar_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.aar_kf)/sum(Ys.laimsl),2)),0) as aar_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.ad_kf)/sum(Ys.laimsl),2)),0) as ad_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.vdaf_kf)/sum(Ys.laimsl),2)),0) as vdaf_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.mt_kf)/sum(Ys.laimsl),2)),0) as mt_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.stad_kf)/sum(Ys.laimsl),2)),0) as stad_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.aad_kf)/sum(Ys.laimsl),2)),0) as aad_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.mad_kf)/sum(Ys.laimsl),2)),0) as mad_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qbad_kf)/sum(Ys.laimsl),2)),0) as qbad_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.had_kf)/sum(Ys.laimsl),2)),0) as had_kf,\n" +
                            "     		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.vad_kf)/sum(Ys.laimsl),2)),0) as vad_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.fcad_kf)/sum(Ys.laimsl),2)),0) as fcad_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.std_kf)/sum(Ys.laimsl),2)),0) as std_kf,\n" +
                            "    	  	 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qbrad_kf)/sum(Ys.laimsl),2)),0) as qbrad_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.hdaf_kf)/sum(Ys.laimsl),2)),0) as hdaf_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.qgrad_daf_kf)/sum(Ys.laimsl),2)),0) as qgrad_daf_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.sdaf_kf)/sum(Ys.laimsl),2)),0) as sdaf_kf,\n" +
                            "    		 nvl(decode(sum(Ys.laimsl),0,0,round(sum(Ys.laimsl*Yz.var_kf)/sum(Ys.laimsl),2)),0) as var_kf,\n" +
                            "            sum(Yz.zhuangt) as zhuangt\n" +
                            " from(\n"
                            + "select tj.id as tjid ,sl.fenx as fenx,gy.mingc as gongysmc,jh.mingc as jihkjbmc,pz.mingc as pinzmc,ysfs.mingc as yunsfsmc\n"
                            + "from yueslb sl,yuetjkjb tj,meikdqb gy,jihkjb jh,yunsfsb ysfs,pinzb pz,diancxxb dc\n"
                            + "where sl.yuetjkjb_id=tj.id and tj.gongysb_id=gy.id and tj.jihkjb_id=jh.id\n"
                            + "     and tj.yunsfsb_id=ysfs.id and tj.pinzb_id=pz.id and sl.fenx='����'\n"
                            + "     and tj.riq="
                            + CurrODate
                            + zhuangt1
                            + " and tj.diancxxb_id=dc.id "
                            + str
                            + "\n"
                            + "union\n"
                            + "select tj.id as tjid,zl.fenx as fenx,gy.mingc as gongysmc,jh.mingc as jihkjbmc,pz.mingc as pinzmc,ysfs.mingc as yunsfsmc\n"
                            + "from yuezlb zl,yuetjkjb tj,meikdqb gy,jihkjb jh,yunsfsb ysfs,pinzb pz,diancxxb dc\n"
                            + "where zl.yuetjkjb_id=tj.id and tj.gongysb_id=gy.id and tj.jihkjb_id=jh.id\n"
                            + "     and tj.yunsfsb_id=ysfs.id and tj.pinzb_id=pz.id and zl.fenx='����'\n"
                            + "     and tj.riq="
                            + CurrODate
                            + zhuangt
                            + " and tj.diancxxb_id=dc.id "
                            + str
                            + "\n"
                            + "     ) title,\n"
                            + "     (select yz.* from yuezlb yz,yuetjkjb tj where yz.yuetjkjb_id=tj.id\n"
                            + "     and tj.riq="
                            + CurrODate
                            + "\n"
                            + "     and yz.fenx='����'\n"
                            + "\n"
                            + "     ) yz,\n"
                            + "     (select ys.yuetjkjb_id,ys.laimsl from yueslb ys,yuetjkjb tj where ys.yuetjkjb_id=tj.id\n"
                            + "     and tj.riq="
                            + CurrODate
                            + "\n"
                            + "     and ys.fenx='����'\n"
                            + "     ) ys\n"
                            + "where Title.tjid=ys.yuetjkjb_id(+) and Title.tjid=yz.yuetjkjb_id(+)" +


                            " group by rollup (Title.Gongysmc, Yz.Yuetjkjb_Id, Title.Jihkjbmc, Title.Yunsfsmc, Title.Pinzmc)\n" +
                            " Having Not(Grouping(Title.Gongysmc) || Grouping(Title.Pinzmc)) = 1\n" +
                            "  Order By Grouping(Title.Gongysmc) Desc";

            rsl = con.getResultSetList(strSql);
            //	System.out.print(strSql);
        }

        ResultSetList rslzl = new ResultSetList();
        if (rslzl == null) {
            String sqlzl = "select * from yuezlb zl,yuetjkjb tj,diancxxb dc where zl.yuetjkjb_id=tj.id and tj.riq="
                    + CurrODate + zhuangt + " and tj.diancxxb_id= dc.id " + str;
            rslzl = con.getResultSetList(sqlzl);
        }
        boolean yincan = false;
        while (rsl.next()) {
            if (visit.getRenyjb() == 3) {
                if (rsl.getLong("zhuangt") == 0) {
                    yincan = false;
                } else {
                    yincan = true;
                }
            } else if (visit.getRenyjb() == 2 || visit.getRenyjb() == 2) {
                yincan = true;
            }

        }
        rsl.beforefirst();
        boolean showBtn = false;
        if (rslzl.next()) {
            rslzl.beforefirst();
            showBtn = false;
        } else {
            showBtn = true;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        egu.setGridType(ExtGridUtil.Gridstyle_Edit);// �趨grid���Ա༭
        // //���ñ��������ڱ���
        egu.setTableName("yuezlb");
        // /������ʾ������
        egu.setWidth("bodyWidth");
        // egu.setHeight("bodyHeight");
        egu.addPaging(0);
        // egu.getColumn("xuh").setHeader("���");
        // egu.getColumn("xuh").setWidth(50);
        egu.getColumn("yuetjkjb_id").setHidden(true);

        // *******************����**********************//
        egu.getColumn("laimsl").setHeader("ʵ����(��)");
        egu.getColumn("laimsl").setWidth(70);
        //-------------------�ߺ��糧Ҫ������diancrz�ֶ�Ϊ�볧��λ��ֵ------------------------------
        egu.getColumn("diancrz").setHeader("�볧��λ��<br>(MJ/kg)");
        egu.getColumn("diancrz").setWidth(80);
        if (!this.getTreeid().equals("141")) {
            egu.getColumn("diancrz").setHidden(true);
        }
        egu.getColumn("qnet_ar").setHeader("��λ��<br>(Qnet,ar)(MJ/kg)");
        egu.getColumn("qnet_ar").setWidth(80);
        egu.getColumn("aar").setHeader("�յ���<br>�ҷ�(Aar)(%)");
        egu.getColumn("aar").setWidth(85);
        egu.getColumn("ad").setHeader("�ɻ���<br>(Ad)(%)");
        egu.getColumn("ad").setWidth(60);
        egu.getColumn("vdaf").setHeader("�ӷ���<br>(Vdaf)(%)");
        egu.getColumn("vdaf").setWidth(65);
        egu.getColumn("mt").setHeader("ȫˮ<br>(Mt)(%)");
        egu.getColumn("mt").setWidth(60);
        egu.getColumn("stad").setHeader("�ոɻ���<br>(St,ad)(%)");
        egu.getColumn("stad").setWidth(60);
        egu.getColumn("aad").setHeader("�ոɻ���<br>(Aad)(%)");
        egu.getColumn("aad").setWidth(60);
        egu.getColumn("mad").setHeader("�ոɻ�ˮ<br>(Mad)(%)");
        egu.getColumn("mad").setWidth(60);
        egu.getColumn("qbad").setHeader("��Ͳ��<br>(Qb,ad)(MJ/Kg)");
        egu.getColumn("qbad").setWidth(60);
        egu.getColumn("had").setHeader("�ոɻ���<br>(Had)(%)");
        egu.getColumn("had").setWidth(60);
        egu.getColumn("vad").setHeader("�ոɻ�<br>�ӷ���(Vad)(%)");
        egu.getColumn("vad").setWidth(60);
        egu.getColumn("fcad").setHeader("�̶�̼<br>(Fcad)(%)");
        egu.getColumn("fcad").setWidth(60);
        egu.getColumn("std").setHeader("�ɻ���<br>(St,d)(%)");
        egu.getColumn("std").setWidth(60);
        egu.getColumn("qbrad").setHeader("�ոɻ�<br>��λ��(Qgr,ad)(MJ/Kg)");
        egu.getColumn("qbrad").setWidth(60);
        egu.getColumn("hdaf").setHeader("�����޻�<br>����(Hdaf)(%)");
        egu.getColumn("hdaf").setWidth(60);
        egu.getColumn("qgrad_daf").setHeader("�����޻һ�<br>��λ��(Qgrad,daf)(MJ/Kg)");
        egu.getColumn("qgrad_daf").setWidth(60);
        egu.getColumn("sdaf").setHeader("�����޻�<br>����(Sdaf)(%)");
        egu.getColumn("sdaf").setWidth(60);
        egu.getColumn("var").setHeader("�յ���<br>�ӷ���(Var)(%)");
        egu.getColumn("var").setWidth(60);

        egu.getColumn("qnet_ar_KF").setHeader("�󷽵�λ��<br>(Qnet,ar)(MJ/Kg)");
        egu.getColumn("qnet_ar_KF").setWidth(85);
        egu.getColumn("aar_KF").setHeader("���յ���<br>�ҷ�(Aar)(%)");
        egu.getColumn("aar_KF").setWidth(100);
        egu.getColumn("ad_KF").setHeader("�󷽸ɻ���<br>(Ad)(%)");
        egu.getColumn("ad_KF").setWidth(80);
        egu.getColumn("vdaf_KF").setHeader("�󷽻ӷ���<br>(Vdaf)(%)");
        egu.getColumn("vdaf_KF").setWidth(80);
        egu.getColumn("mt_KF").setHeader("��ȫˮ<br>(Mt)(%)");
        egu.getColumn("mt_KF").setWidth(70);
        egu.getColumn("stad_KF").setHeader("�󷽿ոɻ���<br>(St,ad)(%)");
        egu.getColumn("stad_KF").setWidth(60);
        egu.getColumn("aad_KF").setHeader("�󷽿ոɻ���<br>(Aad)(%)");
        egu.getColumn("aad_KF").setWidth(60);
        egu.getColumn("mad_KF").setHeader("�󷽿ոɻ�ˮ<br>(Mad)(%)");
        egu.getColumn("mad_KF").setWidth(60);
        egu.getColumn("qbad_KF").setHeader("�󷽵�Ͳ��<br>(Qb,ad)(MJ/Kg)");
        egu.getColumn("qbad_KF").setWidth(60);
        egu.getColumn("had_KF").setHeader("�󷽿ոɻ���<br>(Had)(%)");
        egu.getColumn("had_KF").setWidth(60);
        egu.getColumn("vad_KF").setHeader("�󷽿ոɻ�<br>�ӷ���(Vad)(%)");
        egu.getColumn("vad_KF").setWidth(60);
        egu.getColumn("fcad_KF").setHeader("�󷽹̶�̼<br>(Fcad)(%)");
        egu.getColumn("fcad_KF").setWidth(60);
        egu.getColumn("std_KF").setHeader("�󷽸ɻ���<br>(St,d)(%)");
        egu.getColumn("std_KF").setWidth(80);
        egu.getColumn("qbrad_KF").setHeader("�󷽿ոɻ�<br>��λ��(Qgr,ad)(MJ/Kg)");
        egu.getColumn("qbrad_KF").setWidth(60);
        egu.getColumn("hdaf_KF").setHeader("�󷽸����޻�<br>����(Hdaf)(%)");
        egu.getColumn("hdaf_KF").setWidth(60);
        egu.getColumn("qgrad_daf_KF").setHeader("�󷽸����޻һ�<br>��λ��(Qgrad,daf)(MJ/Kg)");
        egu.getColumn("qgrad_daf_KF").setWidth(60);
        egu.getColumn("sdaf_KF").setHeader("�󷽸����޻�<br>����(Sdaf)(%)");
        egu.getColumn("sdaf_KF").setWidth(60);
        egu.getColumn("var_KF").setHeader("���յ���<br>�ӷ���(Var)(%)");
        egu.getColumn("var_KF").setWidth(60);
        egu.getColumn("gongysb_id").setHeader("������λ");
        egu.getColumn("gongysb_id").setEditor(null);
        egu.getColumn("gongysb_id").setWidth(80);
        egu.getColumn("jihkjb_id").setHeader("�ƻ��ھ�");
        egu.getColumn("jihkjb_id").setEditor(null);
        egu.getColumn("jihkjb_id").setWidth(70);
        egu.getColumn("pinzb_id").setHeader("Ʒ��");
        egu.getColumn("pinzb_id").setHidden(true);
        egu.getColumn("pinzb_id").setEditor(null);
        egu.getColumn("pinzb_id").setWidth(70);
        egu.getColumn("yunsfsb_id").setHeader("���䷽ʽ");
        egu.getColumn("yunsfsb_id").setEditor(null);
        egu.getColumn("yunsfsb_id").setWidth(60);
        egu.getColumn("zhuangt").setHeader("״̬");
        egu.getColumn("zhuangt").setHidden(true);
        egu.getColumn("zhuangt").setEditor(null);
        egu.getColumn("fenx").setHeader("����");
        egu.getColumn("fenx").setHidden(true);
        egu.getColumn("fenx").setWidth(60);
        egu.getColumn("fenx").setDefaultValue("����");

        // egu.getColumn("laimsl").hidden=true;
        egu.getColumn("laimsl").setEditor(null);
        egu.getColumn("gongysb_id").setEditor(null);
        egu.getColumn("jihkjb_id").setEditor(null);
        egu.getColumn("pinzb_id").setEditor(null);
        egu.getColumn("yunsfsb_id").setEditor(null);
        egu.getColumn("fenx").setEditor(null);

        // *****************************************//
//		egu.getColumn("ad").setHidden(true);
        egu.getColumn("aar").setHidden(true);
        egu.getColumn("stad").setHidden(true);
        egu.getColumn("aad").setHidden(true);
        egu.getColumn("mad").setHidden(true);
        egu.getColumn("qbad").setHidden(true);
        egu.getColumn("had").setHidden(true);
        egu.getColumn("vad").setHidden(true);
        egu.getColumn("fcad").setHidden(true);
        egu.getColumn("qbrad").setHidden(true);
        egu.getColumn("hdaf").setHidden(true);
        egu.getColumn("qgrad_daf").setHidden(true);
        egu.getColumn("sdaf").setHidden(true);
        egu.getColumn("var").setHidden(true);
//		egu.getColumn("ad_KF").setHidden(true);
        egu.getColumn("aar_KF").setHidden(true);
        egu.getColumn("stad_KF").setHidden(true);
        egu.getColumn("aad_KF").setHidden(true);
        egu.getColumn("mad_KF").setHidden(true);
        egu.getColumn("qbad_KF").setHidden(true);
        egu.getColumn("had_KF").setHidden(true);
        egu.getColumn("vad_KF").setHidden(true);
        egu.getColumn("fcad_KF").setHidden(true);
        egu.getColumn("qbrad_KF").setHidden(true);
        egu.getColumn("hdaf_KF").setHidden(true);
        egu.getColumn("qgrad_daf_KF").setHidden(true);
        egu.getColumn("sdaf_KF").setHidden(true);
        egu.getColumn("var_KF").setHidden(true);
        // �趨�е�С��λ
        ((NumberField) egu.getColumn("diancrz").editor).setDecimalPrecision(3);
        ((NumberField) egu.getColumn("qnet_ar").editor).setDecimalPrecision(3);
        ((NumberField) egu.getColumn("aar").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("vdaf").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("ad").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("mt").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("std").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("qnet_ar_KF").editor)
                .setDecimalPrecision(2);
        ((NumberField) egu.getColumn("aar_KF").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("vdaf_KF").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("ad_KF").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("mt_KF").editor).setDecimalPrecision(2);
        ((NumberField) egu.getColumn("std_KF").editor).setDecimalPrecision(2);

        StringBuffer sb1 = new StringBuffer();
        sb1.append("gridDiv_grid.on('beforeedit',function(e){");
        sb1.append("if(e.record.get('GONGYSB_ID')=='�ܼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
        sb1.append("});");

        //�趨�ϼ��в�����
        sb1.append("function gridDiv_save(record){if(record.get('gongysb_id')=='�ܼ�') return 'continue';}");
        egu.addOtherScript(sb1.toString());


        // �趨���ɱ༭�е���ɫ
        // egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css='tdTextext';
        // return value;}");
        egu
                .getColumn("gongysb_id")
                .setRenderer(
                        "function(value,metadata){metadata.css='tdTextext'; return value;}");
        egu
                .getColumn("jihkjb_id")
                .setRenderer(
                        "function(value,metadata){metadata.css='tdTextext1'; return value;}");
        egu
                .getColumn("laimsl")
                .setRenderer(
                        "function(value,metadata){metadata.css='tdTextext'; return value;}");

        String Sql = "select zhi from xitxxb x where x.leib='�±�' and x.danw='����' and beiz='ʹ��'";

        ResultSetList rs = con.getResultSetList(Sql);
        if (rs != null) {
            while (rs.next()) {
                String zhi = rs.getString("zhi");
                if (egu.getColByHeader(zhi) != null) {
                    egu.getColByHeader(zhi).hidden = true;
                }
            }
        }
        egu.setDefaultsortable(false);
        // /���ð�ť
        egu.addTbarText("���");
        ComboBox comb1 = new ComboBox();
        comb1.setWidth(50);
        comb1.setTransform("NianfDropDown");
        comb1.setId("NianfDropDown");// ���Զ�ˢ�°�
        comb1.setLazyRender(true);// ��̬��
        comb1.setEditable(true);
        egu.addToolbarItem(comb1.getScript());

        egu.addTbarText("�·�");
        ComboBox comb2 = new ComboBox();
        comb2.setWidth(50);
        comb2.setTransform("YuefDropDown");
        comb2.setId("YuefDropDown");// ���Զ�ˢ�°�
        comb2.setLazyRender(true);// ��̬��
        comb2.setEditable(true);
        egu.addToolbarItem(comb2.getScript());
        // �糧��
        egu.addTbarText("��λ:");
        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid());
        setTree(etu);
        egu.addTbarTreeBtn("diancTree");

        egu.addTbarText("-");// ���÷ָ���
        // �ж������Ƿ�����
        // boolean isLocked = isLocked(con);
        // ˢ�°�ť
        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){").append(
                MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'",
                        true)).append("document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("ˢ��", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        if (visit.getRenyjb() == 3) {
            if (yincan == false) {
                // ���ɰ�ť
                if (showBtn) {
                    GridButton gbc = new GridButton("����",
                            getBtnHandlerScript("CreateButton"));
                    gbc.setIcon(SysConstant.Btn_Icon_Create);
                    egu.addTbarBtn(gbc);
                }
                // ɾ����ť
                GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
                gbd.setIcon(SysConstant.Btn_Icon_Delete);
                egu.addTbarBtn(gbd);
                // ���水ť
                egu.addToolbarButton(GridButton.ButtonType_Save, "SaveButton", MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
            }
        }
        setExtGrid(egu);
        con.Close();
    }

    public String getBtnHandlerScript(String btnName) {
        // ��ť��script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianf() + "��" + getYuef() + "��";
        btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("���������ݽ�����").append(cnDate).append("���Ѵ����ݣ��Ƿ������");
        } else {
            btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){").append(
                "document.getElementById('").append(btnName).append(
                "').click()")
//		-------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',")
                .append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
                .append("}; // end if \n").append("});}");
        return btnsb.toString();
    }

    public ExtGridUtil getExtGrid() {
        return ((Visit) this.getPage().getVisit()).getExtGrid1();
    }

    public void setExtGrid(ExtGridUtil extgrid) {
        ((Visit) this.getPage().getVisit()).setExtGrid1(extgrid);
    }

    public String getGridScript() {
        if (getExtGrid() == null) {
            return "";
        }
        return getExtGrid().getGridScript();
    }

    public String getGridHtml() {
        if (getExtGrid() == null) {
            return "";
        }
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
            visit.setActivePageName(getPageName().toString());
            visit.setString1(null);
            visit.setString2(null);
            visit.setString3(null);
            visit.setShifsh(true);
            setYuefValue(null);
            setNianfValue(null);
            this.getYuefModels();
            this.getNianfModels();
            setRiq();
            this.setTreeid(null);
        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                visit.setShifsh(true);
                setYuefValue(null);
                setNianfValue(null);
                this.getYuefModels();
                this.getNianfModels();
                setRiq();
                this.setTreeid(null);
            }
        }
        getSelectData(null);
    }

    // public boolean isLocked(JDBCcon con) {
    // // String CurrODate =
    // DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-01");
    // return false;
    // }

    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }

    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit())
                .getString3());
        if (intYuef < 10) {
            return "0" + intYuef;
        } else {
            return ((Visit) getPage().getVisit()).getString3();
        }
    }

    public void setYuef(String value) {
        ((Visit) getPage().getVisit()).setString3(value);
    }

    public void setRiq() {
        setNianf(getNianfValue().getValue());
        setYuef(getYuefValue().getValue());
    }

    // ���������
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
            for (int i = 0; i < getNianfModel().getOptionCount(); i++) {
                Object obj = getNianfModel().getOption(i);
                if (_nianf == ((IDropDownBean) obj).getId()) {
                    _NianfValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _NianfValue;
    }

    public void setNianfValue(IDropDownBean Value) {
        if (_NianfValue != Value) {
            _NianfValue = Value;
        }
    }

    public IPropertySelectionModel getNianfModels() {
        List listNianf = new ArrayList();
        int i;
        for (i = 2004; i <= DateUtil.getYear(new Date()) + 1; i++) {
            listNianf.add(new IDropDownBean(i, String.valueOf(i)));
        }
        _NianfModel = new IDropDownModel(listNianf);
        return _NianfModel;
    }

    public void setNianfModel(IPropertySelectionModel _value) {
        _NianfModel = _value;
    }

    // �·�������
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
            for (int i = 0; i < getYuefModel().getOptionCount(); i++) {
                Object obj = getYuefModel().getOption(i);
                if (_yuef == ((IDropDownBean) obj).getId()) {
                    _YuefValue = (IDropDownBean) obj;
                    break;
                }
            }
        }
        return _YuefValue;
    }

    public void setYuefValue(IDropDownBean Value) {
        if (_YuefValue != Value) {
            _YuefValue = Value;
        }
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

    // �糧��

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
            rs.close();
        } catch (SQLException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return diancmc;

    }

    // �õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
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
            rs.close();
        } catch (SQLException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        } finally {
            cn.Close();
        }

        return IDropDownDiancmc;

    }

    // �õ��糧��Ĭ�ϵ�վ
    public String getDiancDaoz() {
        String daoz = "";
        String treeid = this.getTreeid();
        if (treeid == null || treeid.equals("")) {
            treeid = "1";
        }
        JDBCcon con = new JDBCcon();
        try {
            StringBuffer sql = new StringBuffer();
            sql.append("select dc.mingc, cz.mingc  as daoz\n");
            sql.append("  from diancxxb dc, chezxxb cz,diancdzb dd\n");
            sql.append(" where dd.diancxxb_id=dc.id\n");
            sql.append(" and  dd.chezxxb_id=cz.id\n");
            sql.append("   and dc.id = " + treeid + "");

            ResultSet rs = con.getResultSet(sql.toString());

            while (rs.next()) {
                daoz = rs.getString("daoz");
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            con.Close();
        }

        return daoz;
    }

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

    // �õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        // System.out.println("jib:" + DiancTreeJib);
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
}
