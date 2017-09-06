package com.zhiren.jt.zdt.monthreport.tianb.shul;

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
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.WriteLog;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.form.*;
import com.zhiren.jt.zdt.monthreport.tianb.zhil.Yuezlb;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;

/* 
* ʱ�䣺2009-03-26 
* ���ߣ� ll
* �޸����ݣ�1������ѭ���ۼ��㷨��ɾ�����������ۼƣ��������ۼƺͱ������ݷ���������²��뱾���ۼơ�
* 		   2��ȥ����˺ͻ��˹��ܡ�
* 		   3������ж�ѭ���ۼƵ�ʱ�䣬�������û�����ݣ�ѭ����������
* 		   4���޸ı������ݣ���Ӱ���Ժ��·����ݵ����״̬��
* 		   5�����Ӹı乩����λ���ƻ��ھ�������ͬʱ���������������ɱ����ۼ����ݽ����¼��㡣
* 		   6������ɾ���ж���ʾ��ɾ����������ʱ����������ж�Ӧ�ھ����ݣ�����ʾ����ɾ����
*/  
/* 
* ʱ�䣺2009-04-09 
* ���ߣ� ll
* �޸����ݣ�1������xitxxb�еġ��ɱ��������ۼơ��͵糧����Ϊ��������getKoujId()��LeijSelect()�������ж��Ƿ�ִ��ѭ���볧ú�ɱ��ۼ��㷨��
* 				���xitxxb����ֵ����ִ��ѭ���볧ú�ɱ��ۼ��㷨�����xitxxb��û��ֵ����ִ��ѭ���볧ú�ɱ��ۼ��㷨��
* 		   2���޸����¼�������ۼ�ʱ��yuetjkjb_id��ֵȡ���µ�yuetjkjb_id��
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
* �޸����ݣ����ߺ��糧Ҫ����yuezlb������diancrz�ֶ�Ϊ�볧��λ��ֵ��qnet_ar�ֶ�Ϊ��ú���ۼ�Ȩ��λ��ֵ��
* 			�ı����������ʱ������ѭ�����������ۼ�,����diancrz�ֶ����ݡ�
* 		   
*/ 
/* 
* ʱ�䣺2009-04-22
* ���ߣ� ll
* �޸����ݣ�����yuetjkjb������Ψһ�������޸Ĺ�����λ���ƻ��ھ�������ͬʱ��ɾ��yuetjkjb��������ͬ��yuetjkjb_id��
* ����ɾ����yuetjkjb_id���������������ɱ�����������ݣ����¼����ۼ����ݡ�
* 		   
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
* ʱ�䣺2009-06-1
* ���ߣ� ll
* �޸����ݣ���CreateData()�����У����Ӹ��ݹ�Ӧ�̡��ƻ��ھ������䷽ʽ��Ʒ�֡��糧id�����ڵ�������
* ��yuetjkjb�в�ѯyuetjkjb_id�Ƿ��Ѿ����ڵ��жϡ�
* �Ӷ����ⱨ"���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���"����
* 		   
*/

/*
* ʱ�䣺2009-10-21
* ���ߣ� sy
* �޸����ݣ����ӽ���ɾ�����޸ı���ʱ�ж���yueshchjb����ú�Ĵ棩���Ƿ��б������ݣ���������ʾ"����ɾ����ú�Ĵ��е����ݣ�"
*          �޸����ɰ�ťjs���ж������������ݣ��򲻿ɱ༭��
*
*/
public class Yueslb extends BasePage implements PageValidateListener {
    //	�����û���ʾ
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

    private long getKoujId(String strDate, String strDiancName, String strGongysName, String strJihkj, String strPinzbName, String strYunsfs, long oldtjid) {
        String strSqly = "";
        long lngKoujID = 0;

//		�޸�ǰyuetjkjb������
        String gongysbid_old = "";
        String jihkjbid_old = "";
        String pinzbid_old = "";
        String yunsfsbid_old = "";
//		�ı���yuetjkjb������
        String gongysb_id = "";
        String jihkjb_id = "";
        String pinzb_id = "";
        String yunsfsb_id = "";
        String tjid = "";//������ͬ��yuetjkjid
        JDBCcon con = new JDBCcon();

        if (oldtjid == 0) {
//			�ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
            strSqly = "select nvl(max(id),0) as id from yuetjkjb where riq=to_date('" + strDate + "','yyyy-mm-dd') and diancxxb_id=getDiancId('" + strDiancName + "')\n"
                    + " and gongysb_id=getGongysId('" + strGongysName + "') and pinzb_id=get_Pinzb_Id('" + strPinzbName + "') "
                    + " and jihkjb_id=getJihkjbId('" + strJihkj + "') and yunsfsb_id=get_Yunsfsb_Id('" + strYunsfs + "')";
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
        } else {
            //�жϵ�ǰҳ���õ�gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id��
            //��oldtjid�д�������Ƿ�һ�£���һ����update oldtjid��
            String oldtjidtj = "select id,gongysb_id,jihkjb_id,pinzb_id,yunsfsb_id\n" +
                    "from yuetjkjb where id=" + oldtjid;
            ResultSet tjsql = con.getResultSet(oldtjidtj);
            try {
                while (tjsql.next()) {
                    gongysbid_old = tjsql.getString("gongysb_id");
                    jihkjbid_old = tjsql.getString("jihkjb_id");
                    pinzbid_old = tjsql.getString("pinzb_id");
                    yunsfsbid_old = tjsql.getString("yunsfsb_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            String gongys = "select * from meikdqb where mingc='" + strGongysName + "'";
            ResultSet gyssql = con.getResultSet(gongys);
            try {
                while (gyssql.next()) {
                    gongysb_id = gyssql.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            String jihkj = "select * from jihkjb where mingc='" + strJihkj + "'";
            ResultSet jhsql = con.getResultSet(jihkj);
            try {
                while (jhsql.next()) {
                    jihkjb_id = jhsql.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            String pinz = "select * from pinzb where mingc='" + strPinzbName + "'";
            ResultSet pzsql = con.getResultSet(pinz);
            try {
                while (pzsql.next()) {
                    pinzb_id = pzsql.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            String yunsfs = "select * from yunsfsb where mingc='" + strYunsfs + "'";
            ResultSet yssql = con.getResultSet(yunsfs);
            try {
                while (yssql.next()) {
                    yunsfsb_id = yssql.getString("id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            //�жϹ�����λid�Ƿ�ı䡣
            if (!gongysbid_old.equals(gongysb_id)) {
                gongysbid_old = gongysb_id;
            }
            if (!jihkjbid_old.equals(jihkjb_id)) {
                jihkjbid_old = jihkjb_id;
            }
            if (!pinzbid_old.equals(pinzb_id)) {
                pinzbid_old = pinzb_id;
            }
            if (!yunsfsbid_old.equals(yunsfsb_id)) {
                yunsfsbid_old = yunsfsb_id;
            }

            StringBuffer sql_tjid = new StringBuffer();
            sql_tjid.append("begin \n");
            String tjid_sel = "select id from yuetjkjb where gongysb_id=" + gongysbid_old + " and pinzb_id=" + pinzbid_old + "\n" +
                    " and jihkjb_id=" + jihkjbid_old + " and yunsfsb_id=" + yunsfsbid_old + " and riq=to_date('" + strDate + "','yyyy-mm-dd')\n" +
                    " and diancxxb_id=getDiancId('" + strDiancName + "')";
            ResultSet tjid_sel_sql = con.getResultSet(tjid_sel);
            try {
                if (tjid_sel_sql.next()) {
                    tjid = tjid_sel_sql.getString("id");
                    if (!tjid.equals(oldtjid + "")) {
                        String yueslb = "delete from yueslb where yuetjkjb_id = " + tjid + " ;\n";
                        sql_tjid.append(yueslb);
                        String yuezlb = "delete from yuezlb where yuetjkjb_id = " + tjid + " ;\n";
                        sql_tjid.append(yuezlb);
                        String yuercbmdj = "delete from yuercbmdj where yuetjkjb_id = " + tjid + " ;\n";
                        sql_tjid.append(yuercbmdj);
                        String tjid_del = "delete from yuetjkjb where id in (" + tjid + ");\n";
                        sql_tjid.append(tjid_del);
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                con.Close();
            }
            String tjid_upd = "update  yuetjkjb set gongysb_id=" + gongysbid_old + ",pinzb_id=" + pinzbid_old + ",jihkjb_id=" + jihkjbid_old + ",yunsfsb_id=" + yunsfsbid_old + "\n"
                    + " where id=" + oldtjid + ";\n";

            sql_tjid.append(tjid_upd);
            sql_tjid.append("end;");
            con.getInsert(sql_tjid.toString());
            lngKoujID = oldtjid;
            UpdatezlLjSelect();
            if (gaib == false) {
                UpdatercmLeijSelect();
            }
        }
        con.Close();
        return lngKoujID;
    }

    private boolean gaib = false;

    public boolean Diancmc_zdgj() {

        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();

        String rucmtbsql = "select zhi from xitxxb where mingc='�ɱ��������ۼ�' and diancxxb_id=" + getTreeid();
        ResultSet rucmtb = con.getResultSet(rucmtbsql);

        try {
            //�ж��Ƿ�������sc��true ����ѭ����ִ��ɾ���ͱ���,sc��falseִ��ɾ���ͱ��档
            if (rucmtb.next()) {
                gaib = true;

            } else {
                gaib = false;
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        } finally {
            con.Close();
        }

        return gaib;
    }

    private void Save() {

        List list = new ArrayList();
        Yuezlb yuezlb = new Yuezlb();
//		 ����������ݺ��·�������
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
        //--------------------------------
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();
        ResultSetList drsl = visit.getExtGrid1().getDeleteResultSet(getChange());

        String gongysb_id = "";
        String jihkjb_id = "";
        String pinzb_id = "";
        String yunsfsb_id = "";
        String riq = "";
        String diancxxb_id = "";

        long yuetjkjb_id = 0;//�¿ھ���ID
        long oldtjid = 0;//old-�¿ھ���ID


//



        boolean sc = false;//�ж���ʾ��ʾ��
        StringBuffer sql_delete = new StringBuffer("begin \n");
        while (drsl.next()) {


            gongysb_id = drsl.getString("gongysb_id");
            jihkjb_id = drsl.getString("jihkjb_id");
            pinzb_id = drsl.getString("pinzb_id");
            yunsfsb_id = drsl.getString("yunsfsb_id");
            oldtjid = drsl.getLong("yuetjkjb_id");//old-�¿ھ���ID
            sql_delete.append("delete from ").append("yueslb").append(
                    " where yuetjkjb_id =(select distinct tj.id from yueslb sl,yuetjkjb tj,meikdqb gy,jihkjb jh,yunsfsb ys,pinzb pz where tj.id=sl.yuetjkjb_id and tj.jihkjb_id=jh.id and tj.gongysb_id=gy.id and tj.yunsfsb_id=ys.id and tj.pinzb_id=pz.id and gy.mingc='")
                    .append(gongysb_id).append("' and jh.mingc='").append(jihkjb_id).append("' and pz.mingc='").append(pinzb_id).append("' and ys.mingc='").append(yunsfsb_id).append("'and tj.diancxxb_id=")
                    .append(visit.getDiancxxb_id()).append(" and tj.riq=").append(CurrODate).append(");\n");
        }

        sql_delete.append("end;");
        if (sql_delete.length() > 11) {
            con.getUpdate(sql_delete.toString());
            LeijSelect();
        }

        //***********************����*******************************//
        if (sc == false) {
            ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());

            StringBuffer sql = new StringBuffer();
            while (rsl.next()) {
                sql.delete(0, sql.length());
                //			sc=1;//�ж���ʾ��ʾ��0��ɾ����ʾ��1������ɹ�
                sql.append("begin \n");
                long id = 0;

                riq = rsl.getString("riq");
                diancxxb_id = rsl.getString("diancxxb_id");
                gongysb_id = rsl.getString("gongysb_id");
                jihkjb_id = rsl.getString("jihkjb_id");
                pinzb_id = rsl.getString("pinzb_id");
                yunsfsb_id = rsl.getString("yunsfsb_id");

                if ("0".equals(rsl.getString("ID"))) {//�ж��Ƿ�����������
                    id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                    yuetjkjb_id = getKoujId(riq, diancxxb_id, gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, oldtjid);

                    if (!gongysb_id.equals("�ܼ�")) {
                        sql.append("insert into yueslb("
                                + "id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl)values("
                                + id
                                + ","
                                + yuetjkjb_id
                                + ",'����',"
                                + rsl.getDouble("jingz") + ","
                                + rsl.getDouble("biaoz") + ","
                                + rsl.getDouble("yingd") + ","
                                + rsl.getDouble("kuid") + ","
                                + rsl.getDouble("yuns") + ","
                                + rsl.getDouble("koud") + ","
                                + rsl.getDouble("kous") + ","
                                + rsl.getDouble("kouz") + ","
                                + rsl.getDouble("koum") + ","
                                + rsl.getDouble("zongkd") + ","
                                + rsl.getDouble("sanfsl") + ","
                                + rsl.getDouble("jianjl") + ","
                                + rsl.getDouble("ructzl") + ","
                                + rsl.getDouble("laimsl") + ");\n");
                    }
                } else {//�޸�
                    // old-�¿ھ���ID
                    oldtjid = rsl.getLong("yuetjkjb_id");

                    //����µ��¿ھ���ID
                    yuetjkjb_id = getKoujId(rsl.getString("riq"), rsl.getString("diancxxb_id"), gongysb_id, jihkjb_id, pinzb_id, yunsfsb_id, oldtjid);

                    if (oldtjid != yuetjkjb_id) {//�ж��¿ھ���ID�Ƿ����

                        Slbbean slb = new Slbbean();
                        slb.setOldtjid(oldtjid);
                        slb.setNewtjid(yuetjkjb_id);

                        //���������ID
                        String zl = "select zl.id as id from yuezlb zl,yuetjkjb tj where zl.yuetjkjb_id =tj.id and zl.fenx='����'and tj.id=" + oldtjid;
                        ResultSet rszl = con.getResultSet(zl);
                        try {
                            while (rszl.next()) {
                                String yuezlid = rszl.getString("id");
                                slb.setZlid(yuezlid);
                            }
                        } catch (SQLException e) {
                            // TODO �Զ����� catch ��
                            e.printStackTrace();
                        }
                        con.Close();
                        //					����볧ú�ɱ���ID
                        String cb = "select cb.id as id from yuercbmdj cb,yuetjkjb tj where cb.yuetjkjb_id =tj.id and cb.fenx='����'and tj.id=" + oldtjid;
                        ResultSet rscb = con.getResultSet(cb);
                        try {
                            while (rscb.next()) {
                                String rcmid = rscb.getString("id");
                                slb.setCbmid(rcmid);

                            }
                        } catch (SQLException e) {
                            // TODO �Զ����� catch ��
                            e.printStackTrace();
                        }
                        con.Close();
                        list.add(slb);
                    }
                    if (!gongysb_id.equals("�ܼ�")) {
                        sql.append("update yueslb set yuetjkjb_id=" + yuetjkjb_id + " ,laimsl="
                                + rsl.getDouble("laimsl") + ",jingz="
                                + rsl.getDouble("jingz") + ",biaoz="
                                + rsl.getDouble("biaoz") + ",yingd="
                                + rsl.getDouble("yingd") + ",kuid="
                                + rsl.getDouble("kuid") + ",yuns="
                                + rsl.getDouble("yuns") + ",koud="
                                + rsl.getDouble("koud") + ",kous="
                                + rsl.getDouble("kous") + ",kouz="
                                + rsl.getDouble("kouz") + ",koum="
                                + rsl.getDouble("koum") + ",zongkd="
                                + rsl.getDouble("zongkd") + ",sanfsl="
                                + rsl.getDouble("sanfsl") + ",jianjl="
                                + rsl.getDouble("jianjl") + ",ructzl="
                                + rsl.getDouble("ructzl")
                                + " where id=" + rsl.getLong("id") + ";\n");
                    }
                }
                sql.append("end;\n");
                con.getUpdate(sql.toString());
                UpdatezlLjSelect();
                if (gaib == false) {
                    UpdatercmLeijSelect();
                }
                //*************************�ı������ۼ�************************//
                LeijSelect();

            }
        }
        con.Close();
    }

    public void UpdatezlLjSelect() {
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
//							-------------------�ߺ��糧Ҫ������diancrz�ֶ�Ϊ�볧��λ��ֵ------------------
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
                                "select max(zl.yuetjkjb_id) as yuetjkjbid from yuetjkjb kj,yuezlb zl\n" +
                                        "where zl.yuetjkjb_id=kj.id and kj.diancxxb_id=" + rsllj.getLong("diancxxb_id") + " and kj.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id=" + rsllj.getLong("gongysb_id") + " and kj.jihkjb_id=" + rsllj.getLong("jihkjb_id") +
                                        " and kj.yunsfsb_id=" + rsllj.getLong("yunsfsb_id") + " and kj.pinzb_id=" + rsllj.getLong("pinzb_id") + "\n" +
                                        " and zl.fenx='����'";
                        long kjb_id = 0;
                        ResultSet kjid = con.getResultSet(kjidsql);
                        try {
                            if (kjid.next()) {
                                kjb_id = kjid.getLong("yuetjkjbid");
                            }
                        } catch (SQLException e1) {
                            e1.fillInStackTrace();
                        } finally {
                            con.Close();
                        }

                        if (kjb_id == 0) {

                            kjb_id = getKoujId(leijrq, rsllj.getString("diancmc"), rsllj.getString("gongysbmc"), rsllj.getString("jihkjbmc"), rsllj.getString("pinzbmc"), rsllj.getString("yunsfsbmc"), 0);
                        }
                        yuezlbid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        sqllj.append("insert into yuezlb("
                                + "ID,FENX,YUETJKJB_ID,QNET_AR,AAR,AD,VDAF,MT,STAD,AAD,MAD,QBAD,HAD,VAD,FCAD,STD,QBRAD,HDAF,QGRAD_DAF,SDAF,"
                                + "VAR,QNET_AR_KF,AAR_KF,AD_KF,VDAF_KF,MT_KF,STAD_KF,AAD_KF,MAD_KF,QBAD_KF,HAD_KF,VAD_KF,FCAD_KF,STD_KF, "
                                + "QBRAD_KF,HDAF_KF,QGRAD_DAF_KF,SDAF_KF,VAR_KF,zhuangt,diancrz)values("
                                + yuezlbid + ",'�ۼ�'," + kjb_id + ","
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
                                + rsllj.getDouble("VAR_KF")
                                + "," + zhuangt + "," + rsllj.getDouble("diancrz") + ");\n");
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

    public void UpdatercmLeijSelect() {
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
//		String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"+ getYuef() + "-01");
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        int i;
        for (i = 0; i <= 12 - intMonth; i++) {
            String leijrq = intyear + "-" + (intMonth + i) + "-01";
            boolean jzyf = false;//���ý�ֹ�·ݵ�boolean
            String pdyf = "select y.id as id from yuercbmdj y,yuetjkjb kj where y.yuetjkjb_id = kj.id \n"
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
                //��ѯ�ۼ�ֵ=����+�����ۼ�
                String sqlljcx = "select kj.diancxxb_id,kj.gongysb_id as gongysb_id,kj.jihkjb_id as jihkjb_id,\n"
                        + "       kj.pinzb_id as pinzb_id,kj.yunsfsb_id as yunsfsb_id ,\n"
                        + "       max(dc.mingc) as diancmc,max(meikdqb.mingc) as gongysbmc,max(jihkjb.mingc) as jihkjbmc,max(pinzb.mingc) as pinzbmc,max(yunsfsb.mingc) as yunsfsbmc,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.hetj)/sum(sl.laimsl),2)) as hetj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.meij)/sum(sl.laimsl),2)) as meij,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.meijs)/sum(sl.laimsl),2)) as meijs,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunj)/sum(sl.laimsl),2)) as yunj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunjs)/sum(sl.laimsl),2)) as yunjs,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.daozzf)/sum(sl.laimsl),2)) as daozzf,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.zaf)/sum(sl.laimsl),2)) as zaf,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.qit)/sum(sl.laimsl),2)) as qit,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.jiaohqzf)/sum(sl.laimsl),2)) as jiaohqzf,\n"
                        + "       decode(sum(sl.laimsl),0,0, round((sum(sl.laimsl*y.meij)+sum(sl.laimsl*y.yunj)+sum(sl.laimsl*y.daozzf)+sum(sl.laimsl*y.zaf)+sum(sl.laimsl*y.qit)\n"
                        + "                    +sum(sl.laimsl*y.jiaohqzf))/sum(sl.laimsl)*29.271/(sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))\n"
                        + "                    ,2) ) as  biaomdj,\n"
                        + "       decode(sum(sl.laimsl),0,0, round((sum(sl.laimsl*y.meij)+sum(sl.laimsl*y.yunj)+sum(sl.laimsl*y.daozzf)+sum(sl.laimsl*y.zaf)+sum(sl.laimsl*y.qit)\n"
                        + "                    +sum(sl.laimsl*y.jiaohqzf)-sum(sl.laimsl*y.meijs)-sum(sl.laimsl*y.yunjs))/sum(sl.laimsl)*29.271/(sum(sl.laimsl*zl.qnet_ar)/sum(sl.laimsl))\n"
                        + "                    ,2) ) as  buhsbmdj,\n"
//					+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.biaomdj)/sum(sl.laimsl),2)) as biaomdj,\n"
//					+ "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.buhsbmdj)/sum(sl.laimsl),2)) as buhsbmdj,\n"
                        + "       decode(sum(sl.laimsl),0,0,Round(sum(sl.laimsl*y.yunsjl)/sum(sl.laimsl),0)) as yunsjl\n"
                        + "  from yuercbmdj y, yuetjkjb kj, yueslb sl,yuezlb zl,meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc\n"
                        + " where y.yuetjkjb_id = kj.id\n"
                        + "     and kj.id = sl.yuetjkjb_id(+)\n"
                        + "     and kj.id = zl.yuetjkjb_id(+)\n"
                        + "     and y.fenx='����'\n"
                        + "     and sl.fenx='����'\n"
                        + "     and zl.fenx='����'\n"
                        + "	    and kj.gongysb_id = meikdqb.id\n"
                        + "     and kj.jihkjb_id = jihkjb.id\n"
                        + "     and kj.pinzb_id = pinzb.id\n"
                        + "     and kj.yunsfsb_id = yunsfsb.id\n"
                        + "     and dc.id=kj.diancxxb_id\n"
                        + "     and diancxxb_id =" + visit.getDiancxxb_id() + "\n"
                        + "   and kj.riq >=getYearFirstDate(to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd'))\n"
                        + "   and kj.riq<=to_date('" + intyear + "-" + (intMonth + i) + "-01', 'yyyy-mm-dd')\n"
                        + "   group by (kj.diancxxb_id,kj.gongysb_id,kj.jihkjb_id,kj.pinzb_id,kj.yunsfsb_id)";
                ResultSetList rsllj = con.getResultSetList(sqlljcx);
                if (rsllj.getRows() != 0) {

                    long yuercbmdjid = 0;
                    StringBuffer sqllj = new StringBuffer("begin \n");
//					��ȡ�ۼƵ����ۼ�״̬
                    String shzt =
                            "select max(zhuangt) as zhuangt from yuercbmdj cb,yuetjkjb kj where cb.yuetjkjb_id=kj.id and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                                    "       and kj.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')  and cb.fenx='�ۼ�'";
                    ResultSetList shenhzt = con.getResultSetList(shzt);
                    long zhuangt = 0;

                    while (shenhzt.next()) {
                        zhuangt = shenhzt.getLong("zhuangt");
                    }
                    String deletelj = "delete from yuercbmdj where id in(select cb.id from yuercbmdj cb,yuetjkjb k \n" +
                            "where cb.yuetjkjb_id=k.id and k.riq= to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                            "	   and cb.fenx='�ۼ�' and k.diancxxb_id=" + visit.getDiancxxb_id() + ");\n";
                    sqllj.append(deletelj);

                    while (rsllj.next()) {

                        String kjidsql =
                                "select cb.yuetjkjb_id as yuetjkjbid from yuetjkjb kj,yuercbmdj cb\n" +
                                        "where cb.yuetjkjb_id=kj.id and kj.diancxxb_id=" + rsllj.getLong("diancxxb_id") + " and kj.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id=" + rsllj.getLong("gongysb_id") + " and kj.jihkjb_id=" + rsllj.getLong("jihkjb_id") +
                                        " and kj.yunsfsb_id=" + rsllj.getLong("yunsfsb_id") + " and kj.pinzb_id=" + rsllj.getLong("pinzb_id") + "\n" +
                                        " and cb.fenx='����'";
                        long kjb_id = 0;
                        ResultSet kjid = con.getResultSet(kjidsql);
                        try {
                            if (kjid.next()) {
                                kjb_id = kjid.getLong("yuetjkjbid");
                            }
                        } catch (SQLException e1) {
                            e1.fillInStackTrace();
                        } finally {
                            con.Close();
                        }
                        if (kjb_id == 0) {
                            kjb_id = getKoujId(leijrq, rsllj.getString("diancmc"), rsllj.getString("gongysbmc"), rsllj.getString("jihkjbmc"), rsllj.getString("pinzbmc"), rsllj.getString("yunsfsbmc"), 0);
                        }

                        yuercbmdjid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                        sqllj.append(
                                "insert into yuercbmdj(id,fenx,yuetjkjb_id,hetj,meij,meijs,yunj,yunjs,daozzf,zaf,qit,jiaohqzf,biaomdj,buhsbmdj,yunsjl,zhuangt) values " + "("
                                        + yuercbmdjid
                                        + ",'�ۼ�',"
                                        + kjb_id
                                        + "," + rsllj.getDouble("hetj") + "," + ""
                                        + "" + rsllj.getDouble("meij") + "," + ""
                                        + "" + rsllj.getDouble("meijs") + "" + ""
                                        + "," + rsllj.getDouble("yunj")
                                        + "," + rsllj.getDouble("yunjs")
                                        + "," + rsllj.getDouble("daozzf")
                                        + "," + rsllj.getDouble("zaf")
                                        + "," + rsllj.getDouble("qit")
                                        + "," + rsllj.getDouble("jiaohqzf")
                                        + "," + rsllj.getDouble("biaomdj")
                                        + "," + rsllj.getDouble("buhsbmdj")
                                        + "," + rsllj.getDouble("yunsjl")
                                        + "," + zhuangt
                                        + ");\n");
                    }
                    sqllj.append("end;");
                    con.getInsert(sqllj.toString());
                }
            }
            con.Close();
        }
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
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        String sqllj = "";
        int i;

        ResultSetList rsllj = null;
        for (i = 0; i <= 12 - intMonth; i++) {

            String leijrq = intyear + "-" + (intMonth + i) + "-01";
            boolean jzyf = false;//���ý�ֹ�·ݵ�boolean
            String pdyf = "select sl.id as id from yueslb sl,yuetjkjb kj where sl.yuetjkjb_id = kj.id \n"
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
                    sqllj =
                            "select diancxxb_id,tj.gongysb_id as gongysb_id,tj.jihkjb_id as jihkjb_id,tj.pinzb_id as pinzb_id,tj.yunsfsb_id as yunsfsb_id,\n" +
                                    "				max(dc.mingc) as diancmc,max(meikdqb.mingc) as gongysbmc,max(jihkjb.mingc) as jihkjbmc,max(pinzb.mingc) as pinzbmc,max(yunsfsb.mingc) as yunsfsbmc ,\n" +
                                    "               sum(laimsl) as laimsl,sum(jingz) as jingz,sum(biaoz) as biaoz,sum(yingd) as yingd,sum(kuid) as kuid,sum(yuns) as yuns,sum(koud) as koud,sum(kous) as kous,\n" +
                                    "               sum(kouz) as kouz,sum(koum) as koum,sum(zongkd) as zongkd,sum(sanfsl) as sanfsl,sum(jianjl) as jianjl,sum(ructzl) as ructzl\n" +
                                    "          from yuetjkjb tj, yueslb sl, meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc \n" +
                                    "          where tj.id = sl.yuetjkjb_id\n" +
                                    "             and tj.gongysb_id = meikdqb.id\n" +
                                    "         	  and tj.jihkjb_id = jihkjb.id\n" +
                                    "             and tj.pinzb_id = pinzb.id\n" +
                                    "             and tj.yunsfsb_id = yunsfsb.id\n" +
                                    "			  and dc.id=tj.diancxxb_id \n" +
                                    "			  and tj.diancxxb_id =" + visit.getDiancxxb_id() +
                                    "             and ((tj.riq = add_months(to_date('" + intyear + "-" + (intMonth + i) +
                                    "-01','yyyy-mm-dd'), -1) and fenx = '�ۼ�') or\n" +
                                    "		 	    (tj.riq = to_date('" + intyear + "-" + (intMonth + i) +
                                    "-01','yyyy-mm-dd')" +
                                    " and fenx = '����'))\n" +
                                    "          group by(diancxxb_id,tj.gongysb_id,tj.jihkjb_id,tj.pinzb_id,tj.yunsfsb_id)\n";

                } else {//һ�·ݵ��ۼ�=����
                    sqllj =
                            "select sl.id,sl.yuetjkjb_id as yuetjkjb_id,tj.diancxxb_id,tj.gongysb_id as gongysb_id,tj.jihkjb_id as jihkjb_id,\n" +
                                    "       tj.pinzb_id as pinzb_id,tj.yunsfsb_id as yunsfsb_id ,\n" +
                                    "		dc.mingc as diancmc,meikdqb.mingc as gongysbmc,jihkjb.mingc as jihkjbmc,pinzb.mingc as pinzbmc,yunsfsb.mingc as yunsfsbmc ,\n" +
                                    "                             laimsl,jingz, biaoz,yingd,kuid,yuns,koud,kous,\n" +
                                    "                             kouz,koum,zongkd,sanfsl,jianjl, ructzl\n" +
                                    "                        from yuetjkjb tj, yueslb sl, meikdqb, jihkjb, pinzb, yunsfsb,diancxxb dc\n" +
                                    "                        where tj.id = sl.yuetjkjb_id\n" +
                                    "							and tj.gongysb_id = meikdqb.id\n" +
                                    "                           and tj.jihkjb_id = jihkjb.id\n" +
                                    "                           and tj.pinzb_id = pinzb.id\n" +
                                    "                           and tj.yunsfsb_id = yunsfsb.id\n" +
                                    "                           and dc.id=tj.diancxxb_id\n " +
                                    "                           and diancxxb_id = " + visit.getDiancxxb_id() + "\n" +
                                    "                           and tj.riq = " + CurrODate + " and fenx = '����'\n" +
                                    "                        order by sl.id";


                }
                rsllj = con.getResultSetList(sqllj);

                if (rsllj.getRows() != 0) {
//						-----------------------------------------------------------------------
                    long yueslbid = 0;
                    StringBuffer sqlljbc1 = new StringBuffer("begin \n");
                    //��ȡ�ۼƵ����ۼ�״̬
                    String shzt =
                            "select max(zhuangt) as zhuangt from yueslb sl,yuetjkjb kj where sl.yuetjkjb_id=kj.id and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                                    "       and kj.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')  and sl.fenx='�ۼ�'";
                    ResultSetList shenhzt = con.getResultSetList(shzt);
                    long zhuangt = 0;
                    while (shenhzt.next()) {
                        zhuangt = shenhzt.getLong("zhuangt");
                    }
                    String deletelj = "delete from yueslb where id in(select s.id from yueslb s,yuetjkjb k \n" +
                            "where s.yuetjkjb_id=k.id and k.riq= to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                            "	   and s.fenx='�ۼ�' and k.diancxxb_id=" + visit.getDiancxxb_id() + ");\n";
                    sqlljbc1.append(deletelj);
                    while (rsllj.next()) {
                        String kjidsql =
                                "select sl.yuetjkjb_id as yuetjkjbid from yuetjkjb kj,yueslb sl\n" +
                                        "where sl.yuetjkjb_id=kj.id and kj.diancxxb_id=" + rsllj.getLong("diancxxb_id") + " and kj.riq = to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')\n" +
                                        "      and kj.gongysb_id=" + rsllj.getLong("gongysb_id") + " and kj.jihkjb_id=" + rsllj.getLong("jihkjb_id") +
                                        " and kj.yunsfsb_id=" + rsllj.getLong("yunsfsb_id") + " and kj.pinzb_id=" + rsllj.getLong("pinzb_id") + "\n" +
                                        " and sl.fenx='����'";
                        long kjb_id = 0;
                        ResultSet kjid = con.getResultSet(kjidsql);
                        try {
                            if (kjid.next()) {
                                kjb_id = kjid.getLong("yuetjkjbid");
                            }
                        } catch (SQLException e1) {
                            e1.fillInStackTrace();
                        } finally {
                            con.Close();
                        }
                        if (kjb_id == 0) {

                            kjb_id = getKoujId(leijrq, rsllj.getString("diancmc"), rsllj.getString("gongysbmc"), rsllj.getString("jihkjbmc"), rsllj.getString("pinzbmc"), rsllj.getString("yunsfsbmc"), 0);

                        }
                        yueslbid = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                        sqlljbc1.append("insert into yueslb("
                                + "id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl,zhuangt)values("
                                + yueslbid
                                + ","
                                + kjb_id
                                + ",'�ۼ�',"
                                + rsllj.getDouble("jingz") + ","
                                + rsllj.getDouble("biaoz") + ","
                                + rsllj.getDouble("yingd") + ","
                                + rsllj.getDouble("kuid") + ","
                                + rsllj.getDouble("yuns") + ","
                                + rsllj.getDouble("koud") + ","
                                + rsllj.getDouble("kous") + ","
                                + rsllj.getDouble("kouz") + ","
                                + rsllj.getDouble("koum") + ","
                                + rsllj.getDouble("zongkd") + ","
                                + rsllj.getDouble("sanfsl") + ","
                                + rsllj.getDouble("jianjl") + ","
                                + rsllj.getDouble("ructzl") + ","
                                + rsllj.getDouble("laimsl") + ","
                                + zhuangt + ");\n");
                    }
                    sqlljbc1.append("end;");
                    con.getInsert(sqlljbc1.toString());
//						-----------------------------------------------------------------------

                }
                con.Close();
            }
        }
    }

    private boolean _SaveClick = false;

    public void SaveButton(IRequestCycle cycle) {
        _SaveClick = true;
    }

    private boolean _Refreshclick = false;

    public void RefreshButton(IRequestCycle cycle) {
        _Refreshclick = true;
    }


    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private boolean _DeleteButton = false;

    public void DeleteButton(IRequestCycle cycle) {
        _DeleteButton = true;
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
//		Visit visit = (Visit) this.getPage().getVisit();
        if (_SaveClick) {
            _SaveClick = false;
            Diancmc_zdgj();//�жϵ糧�Ƿ�Ϊ�е���������糧������ѭ������ɱ��ۼ�
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
        //-----------------------------------------------ִ��ɾ���ж�-------------------------------------------------------------
        String sql = "select * from yueshchjb where fenx='����' and diancxxb_id=" + visit.getDiancxxb_id() + " and to_char(riq,'yyyy-MM')='" + riq + "'";
        ResultSet rs = con.getResultSet(sql);
        try {
            //�ж��Ƿ�������
            if (rs.next()) {
                setMsg("����ɾ����ú�Ĵ��е����ݣ�");
                rs.close();
                con.Close();
                return;
            }
            //***********�ж�ɾ��yueslb���Ƿ���yuezlb��Ӧ������***********************************
            sql = "select * from yuezlb z inner join YUETJKJB t on z.YUETJKJB_ID=t.id\n" +
                    "where t.diancxxb_id=" + visit.getDiancxxb_id() + " and z.fenx='����' and to_char(t.riq,'yyyy-MM')='" + riq + "'";
            rs = con.getResultSet(sql);
            //�ж��Ƿ�������sc��true ����ѭ����ִ��ɾ���ͱ���,sc��falseִ��ɾ���ͱ��档
            if (rs.next()) {
                setMsg("ɾ��������ǰ������ɾ��������еĶ�Ӧ���ݣ�");
                rs.close();
                con.Close();
                return;
            }
            String condition=" where to_char(riq,'yyyy-MM')='" + riq + "' and diancxxb_id="+ getTreeid();

        //----------------------------------------------------------------------------------------------------------------------
            String strSql = "begin delete from yueslb where yuetjkjb_id in (select id from yuetjkjb  "+condition+");\n"+
                    "delete from yuetjkjb "+condition+"; end;";
            int flag = con.getDelete(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.DeleteYuezlbFailed + "SQL:"
                        + strSql);
                setMsg("ɾ�������з�������");
            } else {
                setMsg(CurrZnDate + "�����ݱ��ɹ�ɾ����");
            }
            con.Close();
        } catch (Exception e) {
            setMsg("ɾ�������з�������");
            e.printStackTrace();
        } finally {
            con.Close();
        }
    }

    public void CreateData() {

        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String CurrZnDate = getNianf() + "��" + getYuef() + "��";
        String CurrSDate = getNianf() + "-" + getYuef();
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
        int intYuef = Integer.parseInt(getYuef());
        String LastODate = DateUtil.FormatOracleDate(getNianf() + "-" + (intYuef < 11 ? "0" + (intYuef - 1) : (intYuef - 1) + "") + "-01");
        String strSql = "";
        String strSql_lj = "";
        int flag;
        long lngId = 0;
        long falge = 0;//�жϱ����Ƿ�����


        StringBuffer sql1 = new StringBuffer();
        //ȡ����Ӧ�̵���������
        strSql =
                "select --nvl(xuh,0) as xuh,\n" +
                        " gongysb_id, jihkjb_id,yunsfsb_id,sum(laimsl) as laimsl,\n" +
                        " sum(jingz) as jingz,sum(f.biaoz) as biaoz,sum(yingd) as yingd,sum(kuid) as kuid,sum(yuns) as yuns,\n" +
                        " sum(koud) as koud,sum(kous) as kous,sum(kouz) as kouz,sum(koum) as koum,sum(zongkd) as zongkd,\n" +
                        " sum(sanfsl) as sanfsl, sum(jianjsl) as jianjsl from\n" +
                        "             (select --max(gy.dqxh) as xuh,\n" +
                        "					   gy.dqid as gongysb_id , jihkjb_id,yunsfsb_id,\n" +
                        "                      (round_new(sum(biaoz),0)+round_new(sum(yingd),0)-round_new(sum(yingd-yingk),0))  as laimsl,\n" +
                        "                      round_new(sum(maoz-piz),0) as jingz,\n" +
                        "                      round_new(sum(biaoz),0) as biaoz,\n" +
                        "                      round_new(sum(yingd),0) as yingd,\n" +
                        "                      round_new(sum(yingd-yingk),0) as kuid,\n" +
                        "                      round_new(sum(yuns),0) as yuns,\n" +
                        "                      round_new(sum(koud),0) as koud,\n" +
                        "                      round_new(sum(kous),0) as kous,\n" +
                        "                      round_new(sum(kouz),0) as kouz,\n" +
                        "                      round_new(sum(koum),0) as koum,\n" +
                        "                      round_new(sum(zongkd),0) as zongkd, round_new(sum(sanfsl),0) as sanfsl,\n" +
                        "                      round_new(sum(nvl(jianjsl,0)),0) as jianjsl\n" +
                        "              from fahb fh,vwgongysdq gy,\n" +
                        "                   ( select fahb_id,sum(cp.maoz-cp.piz) as jianjsl from chepb cp,fahb fh\n" +
                        "                     where jianjfs='����'\n" +
                        "                           and daohrq>=to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd') and  daohrq<=last_day(to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd'))\n" +
                        "                           and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                        "                           and fh.id=cp.fahb_id\n" +
                        "                     group by fahb_id) c\n" +
                        "              where  daohrq>=to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd') and  daohrq<=last_day(to_date('" + CurrSDate + "-01' ,'yyyy-mm-dd'))\n" +
                        "              and diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                        "              and fh.meikxxb_id=gy.mk_id\n" +
                        "              and fh.id=c.fahb_id(+)\n" +
                        "              group by lieid,gy.dqid ,jihkjb_id,yunsfsb_id)  f\n" +
                        " group by --xuh,\n" +
                        "gongysb_id, jihkjb_id,yunsfsb_id";

        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            con.rollBack();
            con.Close();
            return;
        }
        while (rs.next()) {
            //�ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
            strSql = "select * from yuetjkjb where riq=" + CurrODate + " and diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                    + " and gongysb_id=" + rs.getLong("gongysb_id") + " and pinzb_id=(select id from pinzb where mingc='ԭú')"//+rs.getLong("pinzb_id")
                    + " and jihkjb_id=" + rs.getLong("jihkjb_id") + " and yunsfsb_id=" + rs.getLong("yunsfsb_id");
            ResultSetList rec = con.getResultSetList(strSql);
            if (rec == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            if (rec.next()) {
                lngId = rec.getLong("id");
            } else {
                lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
                strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
                        + lngId + "," + CurrODate + "," + visit.getDiancxxb_id() + "," + rs.getInt("xuh") + "," + rs.getLong("gongysb_id")
                        + ",(select max(id) from pinzb where mingc='ԭú')"//+rs.getLong("pinzb_id")
                        + "," + rs.getLong("jihkjb_id") + "," + rs.getLong("yunsfsb_id") + ")";
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
                    con.rollBack();
                    con.Close();
                    return;
                }
            }
            strSql = "delete from yueslb where yuetjkjb_id=" + lngId;
            flag = con.getDelete(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail + "SQL:" + strSql);
                setMsg("���ɹ��̳��ִ���������ɾ��ʧ�ܣ�");
                con.rollBack();
                con.Close();
                return;
            }
            strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                    + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                    + ","
                    + lngId
                    + ",'����',"
                    + rs.getDouble("jingz") + ","
                    + rs.getDouble("biaoz") + ","
                    + rs.getDouble("yingd") + ","
                    + rs.getDouble("kuid") + ","
                    + rs.getDouble("yuns") + ","
                    + rs.getDouble("koud") + ","
                    + rs.getDouble("kous") + ","
                    + rs.getDouble("kouz") + ","
                    + rs.getDouble("koum") + ","
                    + rs.getDouble("zongkd") + ","
                    + rs.getDouble("sanfsl") + ","
                    + rs.getDouble("jianjsl") + ",0,"
                    + rs.getDouble("laimsl") + ")";

            sql1.append("����" + strSql + "\n");
            flag = con.getInsert(strSql);
            if (flag == -1) {
                WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                setMsg("���ɹ��̳��ִ�������������ʧ�ܣ�");
                con.rollBack();
                con.Close();
                return;
            }
            //*************************�ı������ۼ�************************//

            //�����ۼ�,1�·�ʱ�����µ����ۼơ���֮������ϸ���û�д�����¼��Ϣ�����µ����ۼơ��б����ۼ�=�����ۼ�+����ֵ
            if (intYuef != 1) {
                strSql = "select sl.* from yuetjkjb tj,yueslb sl  where sl.yuetjkjb_id=tj.id and riq=" + LastODate + " and diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                        + " and gongysb_id=" + rs.getLong("gongysb_id") + " and pinzb_id=(select id from pinzb where mingc='ԭú')"//+rs.getLong("pinzb_id")
                        + " and jihkjb_id=" + rs.getLong("jihkjb_id") + " and yunsfsb_id=" + rs.getLong("yunsfsb_id")
                        + " and fenx='�ۼ�'";
                ResultSetList rsSy = con.getResultSetList(strSql);
                if (rsSy == null) {
                    WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
                    setMsg(ErrorMessage.NullResult);
                    con.rollBack();
                    con.Close();
                    return;
                }
                if (rsSy.next()) {
                    strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                            + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                            + ","
                            + lngId
                            + ",'�ۼ�',"
                            + (rs.getDouble("jingz") + rsSy.getDouble("jingz")) + ","
                            + (rs.getDouble("biaoz") + rsSy.getDouble("biaoz")) + ","
                            + (rs.getDouble("yingd") + rsSy.getDouble("yingd")) + ","
                            + (rs.getDouble("kuid") + rsSy.getDouble("kuid")) + ","
                            + (rs.getDouble("yuns") + rsSy.getDouble("yuns")) + ","
                            + (rs.getDouble("koud") + rsSy.getDouble("koud")) + ","
                            + (rs.getDouble("kous") + rsSy.getDouble("kous")) + ","
                            + (rs.getDouble("kouz") + rsSy.getDouble("kouz")) + ","
                            + (rs.getDouble("koum") + rsSy.getDouble("koum")) + ","
                            + (rs.getDouble("zongkd") + rsSy.getDouble("zongkd")) + ","
                            + (rs.getDouble("sanfsl") + rsSy.getDouble("sanfsl")) + ","
                            + (rs.getDouble("jianjsl") + rsSy.getDouble("jianjl")) + ","
                            + rsSy.getDouble("ructzl") + ","
                            + (rsSy.getDouble("laimsl") + rs.getDouble("laimsl")) + ")";
                } else {  //�ϸ���û�д�����¼��Ϣ�����µ����ۼ�
                    strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                            + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                            + ","
                            + lngId
                            + ",'�ۼ�',"
                            + rs.getDouble("jingz") + ","
                            + rs.getDouble("biaoz") + ","
                            + rs.getDouble("yingd") + ","
                            + rs.getDouble("kuid") + ","
                            + rs.getDouble("yuns") + ","
                            + rs.getDouble("koud") + ","
                            + rs.getDouble("kous") + ","
                            + rs.getDouble("kouz") + ","
                            + rs.getDouble("koum") + ","
                            + rs.getDouble("zongkd") + ","
                            + rs.getDouble("sanfsl") + ","
                            + rs.getDouble("jianjsl") + ",0," + rs.getDouble("laimsl") + ")";
                }
                sql1.append("�ۼ�" + strSql + "\n");
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ����������ۼƲ���ʧ�ܣ�");
                    con.rollBack();
                    con.Close();
                    return;
                }
            } else {    //1�·�ʱ�����µ����ۼ�
                strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                        + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                        + ","
                        + lngId
                        + ",'�ۼ�',"
                        + rs.getDouble("jingz") + ","
                        + rs.getDouble("biaoz") + ","
                        + rs.getDouble("yingd") + ","
                        + rs.getDouble("kuid") + ","
                        + rs.getDouble("yuns") + ","
                        + rs.getDouble("koud") + ","
                        + rs.getDouble("kous") + ","
                        + rs.getDouble("kouz") + ","
                        + rs.getDouble("koum") + ","
                        + rs.getDouble("zongkd") + ","
                        + rs.getDouble("sanfsl") + ","
                        + rs.getDouble("jianjsl") + ",0," + rs.getDouble("laimsl") + ")";
                sql1.append("�ۼ�" + strSql + "\n");
                flag = con.getInsert(strSql);
                if (flag == -1) {
                    WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                    setMsg("���ɹ��̳��ִ����������ۼƲ���ʧ�ܣ�");
                    con.rollBack();
                    con.Close();
                    return;
                }
            }
        }


        if (intYuef != 1) {//ȡ�ϸ����У������û�е����ݣ�����ֵΪ0���ۼ�ֵ�����ϸ����ۼ�
            strSql = "select sl.*,tj.gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id,tj.xuh from yueslb sl,yuetjkjb tj \n"
                    + " where sl.yuetjkjb_id=tj.id and riq=" + LastODate + " \n"
                    + " and fenx='�ۼ�' and diancxxb_id=" + visit.getDiancxxb_id();
            ResultSetList rssylj = con.getResultSetList(strSql);
            if (rssylj == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
                setMsg(ErrorMessage.NullResult);
                con.rollBack();
                con.Close();
                return;
            }
            while (rssylj.next()) {
                strSql = "select kj.* from yueslb sl,yuetjkjb kj where sl.yuetjkjb_id=kj.id and kj.riq=" + CurrODate + " and kj.diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                        + " and kj.gongysb_id=" + rssylj.getLong("gongysb_id") + " and kj.pinzb_id=(select id from pinzb where mingc='ԭú') "//+rssylj.getLong("pinzb_id")
                        + " and kj.jihkjb_id=" + rssylj.getLong("jihkjb_id") + " and kj.yunsfsb_id=" + rssylj.getLong("yunsfsb_id");
                ResultSetList recby = con.getResultSetList(strSql);
                if (rssylj == null) {
                    WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
                    setMsg(ErrorMessage.NullResult);
                    con.rollBack();
                    con.Close();
                    return;
                }
                if (!recby.next()) {
//					�ж�yuetjkjb���Ƿ��д���Ϣ��û�в��롣
                    strSql_lj = "select * from yuetjkjb where riq=" + CurrODate + " and diancxxb_id=" + visit.getDiancxxb_id() + "\n"
                            + " and gongysb_id=" + rssylj.getLong("gongysb_id") + " and pinzb_id=" + rssylj.getLong("pinzb_id")
                            + " and jihkjb_id=" + rssylj.getLong("jihkjb_id") + " and yunsfsb_id=" + rssylj.getLong("yunsfsb_id");
                    ResultSetList rec_lj = con.getResultSetList(strSql_lj);
                    if (rec_lj == null) {
                        WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql_lj);
                        setMsg(ErrorMessage.NullResult);
                        con.rollBack();
                        con.Close();
                        return;
                    }
                    if (rec_lj.next()) {
                        lngId = rec_lj.getLong("id");
                    } else {
                        lngId = Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()));
                        strSql = "insert into yuetjkjb(id,riq,diancxxb_id,xuh,gongysb_id,pinzb_id,jihkjb_id,yunsfsb_id) values(\n"
                                + lngId + "," + CurrODate + "," + visit.getDiancxxb_id() + "," + rssylj.getLong("xuh") + "," + rssylj.getLong("gongysb_id")
                                + "," + rssylj.getLong("pinzb_id") + "," + rssylj.getLong("jihkjb_id") + "," + rssylj.getLong("yunsfsb_id") + ")";
                        flag = con.getInsert(strSql);
                        if (flag == -1) {
                            WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                            setMsg("���ɹ��̳��ִ�����ͳ�ƿھ�δ����ɹ���");
                            con.rollBack();
                            con.Close();
                            return;
                        }
                    }
                    strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                            + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                            + ","
                            + lngId
                            + ",'����',0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
                    sql1.append("����" + strSql + "\n");
                    flag = con.getInsert(strSql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                        setMsg("���ɹ��̳��ִ���������δ����ɹ���");
                        con.rollBack();
                        con.Close();
                        return;
                    }
                    strSql = "insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
                            + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
                            + ","
                            + lngId
                            + ",'�ۼ�',"
                            + rssylj.getDouble("jingz") + ","
                            + rssylj.getDouble("biaoz") + ","
                            + rssylj.getDouble("yingd") + ","
                            + rssylj.getDouble("kuid") + ","
                            + rssylj.getDouble("yuns") + ","
                            + rssylj.getDouble("koud") + ","
                            + rssylj.getDouble("kous") + ","
                            + rssylj.getDouble("kouz") + ","
                            + rssylj.getDouble("koum") + ","
                            + rssylj.getDouble("zongkd") + ","
                            + rssylj.getDouble("sanfsl") + ","
                            + rssylj.getDouble("jianjl") + ","
                            + rssylj.getDouble("ructzl") + ","
                            + rssylj.getDouble("laimsl") + ")";
                    sql1.append("�ۼ�" + strSql + "\n");
                    flag = con.getInsert(strSql);
                    if (flag == -1) {
                        WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:" + strSql);
                        setMsg("���ɹ��̳��ִ����������ۼ�δ����ɹ���");
                        con.rollBack();
                        con.Close();
                        return;
                    }
                }
//				else{//��ͳ�ƿھ�������������������û��
//					lngId=recby.getLong("id");
//					strSql="delete from yueslb where yuetjkjb_id="+lngId;
//					flag = con.getDelete(strSql);
//					if(flag == -1) {
//						WriteLog.writeErrorLog(ErrorMessage.DeleteDatabaseFail+"SQL:"+strSql);
//						setMsg("���ɹ��̳��ִ���������ɾ��ʧ�ܣ�");
//						con.rollBack();
//						con.Close();
//						return;
//					}strSql="insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
//						+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
//						+","
//						+lngId
//						+",'����',0,0,0,0,0,0,0,0,0,0,0,0,0,0)";
//					sql1.append("����"+strSql+"\n");
//					flag = con.getInsert(strSql);
//					if(flag == -1) {
//						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
//						setMsg("���ɹ��̳��ִ���������δ����ɹ���");
//						con.rollBack();
//						con.Close();
//						return;
//					}
//					strSql="insert into yueslb(id,yuetjkjb_id,fenx,jingz,biaoz,yingd,kuid,yuns,koud,kous,kouz,koum,zongkd,sanfsl,jianjl,ructzl,laimsl) values("
//						+Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id()))
//						+","
//						+lngId
//						+",'�ۼ�',"
//						+rssylj.getDouble("jingz")+","
//						+rssylj.getDouble("biaoz")+","
//						+rssylj.getDouble("yingd")+","
//						+rssylj.getDouble("kuid")+","
//						+rssylj.getDouble("yuns")+","
//						+rssylj.getDouble("koud")+","
//						+rssylj.getDouble("kous")+","
//						+rssylj.getDouble("kouz")+","
//						+rssylj.getDouble("koum")+","
//						+rssylj.getDouble("zongkd")+","
//						+rssylj.getDouble("sanfsl")+","
//						+rssylj.getDouble("jianjl")+","
//						+rssylj.getDouble("ructzl")+","
//						+rssylj.getDouble("laimsl")+")";
//					sql1.append("�ۼ�"+strSql+"\n");
//					flag = con.getInsert(strSql);
//					if(flag == -1) {
//						WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail+"SQL:"+strSql);
//						setMsg("���ɹ��̳��ִ����������ۼ�δ����ɹ���");
//						con.rollBack();
//						con.Close();
//						return;
//					}
//				}
            }
        }

        con.commit();
        con.Close();
        setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
    }

    public void getSelectData(ResultSetList rsl) {
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) getPage().getVisit();
        String zhuangt = "";
        if (visit.isShifsh() == true) {
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (sl.zhuangt=1 or sl.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and sl.zhuangt=2";
            }
        }
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");

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
//	---------------------------------------------------------------------
        boolean isReture = false;
        if (visit.getRenyjb() == 1) {
            if (treejib == 1) {
                isReture = true;
            }
        } else if (visit.getRenyjb() == 2) {
            if (treejib == 2) {
                isReture = true;
            }
        }


        String sql_sl = "select * from yueslb s, yuetjkjb k,diancxxb dc  where s.yuetjkjb_id = k.id and k.riq = "
                + CurrODate + " and fenx='����' and k.diancxxb_id=dc.id " + str;
        boolean isNotReady = con.getHasIt(sql_sl);  //getHasIt=true ������getHasIt=fulse û��


//		if(isReture) {
//			setMsg("�������Ҫ������Ϣʱ����ѡ����Ҫ���˵Ĺ�˾��糧���ƣ�");
//		}
        if (rsl == null) {
            String strSql =
                    "select max(nvl(id,0)) as id,'����' fenx,riq,diancxxb_id, yuetjkjb_id,\n" +
                            "           decode(grouping(gongysb_id),1,'�ܼ�',gongysb_id) as gongysb_id,\n" +
                            "           decode(grouping(jihkjb_id),1,'-',jihkjb_id) as jihkjb_id,\n" +
                            "           decode(grouping(pinzb_id),1,'-',pinzb_id) as pinzb_id,  decode(grouping(yunsfsb_id),1,'-',yunsfsb_id) as yunsfsb_id,\n" +
                            "           sum(laimsl) as laimsl,sum(jingz) as jingz, sum(biaoz) as biaoz,sum(yingd) as yingd,\n" +
                            "           sum(kuid) as kuid,sum(yuns) as yuns,sum(koud) as koud, sum(kous) as kous,\n" +
                            "           sum(kouz) as kouz,sum(koum) as koum,sum(zongkd) as zongkd,\n" +
                            "           sum(sanfsl) as sanfsl,sum(jianjl) as jianjl,sum(ructzl) as ructzl,\n" +
                            "           sum(zhuangt) as zhuangt\n" +
                            "from(\n" +
                            "       select nvl(sjb.id,0) as id,decode(sjb.fenx,null,'����',sjb.fenx) as fenx,\n" +
                            "                decode(sjb.riq,''," + CurrODate + ",sjb.riq) as riq,\n" +
                            "                dc.mingc as diancxxb_id,\n" +
                            "                nvl(sjb.yuetjkjb_id,0) as yuetjkjb_id,sjb.gongysb_id,sjb.jihkjb_id,sjb.pinzb_id,sjb.yunsfsb_id,\n" +
                            "                nvl(sjb.laimsl,0) as laimsl,nvl(sjb.jingz,0) as jingz,nvl(sjb.biaoz,0) as biaoz,nvl(sjb.yingd,0) as yingd,\n" +
                            "                nvl(sjb.kuid,0) as kuid,nvl(sjb.yuns,0) as yuns,nvl(koud,0) as koud,nvl(sjb.kous,0) as kous,nvl(sjb.kouz,0) as kouz,\n" +
                            "                nvl(sjb.koum,0) as koum,nvl(sjb.zongkd,0) as zongkd,nvl(sjb.sanfsl,0) as sanfsl,nvl(sjb.jianjl,0) as jianjl,\n" +
                            "                nvl(sjb.ructzl,0) as ructzl,nvl(sjb.zhuangt,0) as zhuangt\n" +
                            "       from diancxxb dc,\n" +
                            "               ( select sl.id as id,tj.riq as riq,tj.diancxxb_id as diancxxb_id,sl.yuetjkjb_id as yuetjkjb_id,meikdqb.mingc as gongysb_id\n" +
                            "                       ,jihkjb.mingc as jihkjb_id,pinzb.mingc as pinzb_id,\n" +
                            "                       yunsfsb.mingc as yunsfsb_id,fenx,\n" +
                            "                       laimsl,jingz,biaoz,yingd,kuid,yuns,koud,kous,\n" +
                            "                       kouz,koum,zongkd,sanfsl,jianjl,ructzl,sl.zhuangt\n" +
                            "                 from yuetjkjb tj,yueslb sl,meikdqb,jihkjb,pinzb,yunsfsb\n" +
                            "                 where tj.id=sl.yuetjkjb_id and tj.gongysb_id=meikdqb.id and tj.jihkjb_id=jihkjb.id\n" +
                            "                       and tj.pinzb_id=pinzb.id and tj.yunsfsb_id=yunsfsb.id and fenx='����' and riq=" + CurrODate + zhuangt + "\n" +
                            "                )sjb\n" +
                            "        where sjb.diancxxb_id=dc.id " + str + " order by dc.xuh\n" +
                            ")\n" +
                            "group by rollup(gongysb_id,riq,diancxxb_id, yuetjkjb_id,jihkjb_id,pinzb_id, yunsfsb_id)\n" +
                            "having grouping(gongysb_id) = 1 or grouping(yunsfsb_id)=0\n" +
                            "order by gongysb_id desc,jihkjb_id desc,yunsfsb_id desc,pinzb_id desc,id";


            rsl = con.getResultSetList(strSql);
//			WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
//			setMsg(ErrorMessage.NullResult);
//			return;
        }

        boolean yincan = false;
        while (rsl.next()) {
            if (visit.getRenyjb() == 3) {
                if (rsl.getLong("zhuangt") == 0) {
                    yincan = false;
                } else {
                    yincan = true;
                }
            } else if (visit.getRenyjb() == 1 || visit.getRenyjb() == 2) {
                yincan = true;
            }

        }
        rsl.beforefirst();
        boolean showBtn = false;
        if (rsl.next()) {
            rsl.beforefirst();
            showBtn = false;
        } else {
            showBtn = true;
        }
        ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
        // //���ñ��������ڱ���
        egu.setTableName("yueslb");
        // /������ʾ������
        egu.setWidth("bodyWidth");
//		egu.setHeight("bodyHeight");
        egu.addPaging(0);
        egu.setGridType(ExtGridUtil.Gridstyle_Edit);
        //egu.getColumn("xuh").setHeader("���");
        //egu.getColumn("xuh").setWidth(50);
        egu.getColumn("riq").setHeader("����");
        egu.getColumn("riq").setHidden(true);
        egu.getColumn("riq").setEditor(null);
        egu.getColumn("diancxxb_id").setHeader("�糧����");
        egu.getColumn("diancxxb_id").setHidden(true);
        egu.getColumn("diancxxb_id").setEditor(null);
        egu.getColumn("yuetjkjb_id").setHidden(true);
        egu.getColumn("gongysb_id").setHeader(Locale.gongysb_id_fahb);
        egu.getColumn("gongysb_id").setWidth(90);
        egu.getColumn("jihkjb_id").setHeader(Locale.jihkjb_id_fahb);
        egu.getColumn("jihkjb_id").setWidth(80);
        egu.getColumn("pinzb_id").setHeader(Locale.pinzb_id_fahb);
        egu.getColumn("pinzb_id").setWidth(80);
        egu.getColumn("pinzb_id").setHidden(true);
        egu.getColumn("yunsfsb_id").setHeader(Locale.yunsfsb_id_fahb);
        egu.getColumn("yunsfsb_id").setWidth(70);
        egu.getColumn("fenx").setHeader(Locale.MRtp_fenx);
        egu.getColumn("fenx").setHidden(true);
        egu.getColumn("fenx").setWidth(40);
        egu.getColumn("laimsl").setHeader(Locale.laimsl_fahb + "(��)");
        egu.getColumn("laimsl").setWidth(80);
//		egu.getColumn("laimsl").setEditor(null);
        egu.getColumn("jingz").setHeader(Locale.jingz_fahb + "(��)");
        egu.getColumn("jingz").setWidth(60);
        egu.getColumn("biaoz").setHeader(Locale.biaoz_fahb + "(��)");
        egu.getColumn("biaoz").setWidth(60);
        egu.getColumn("yingd").setHeader(Locale.yingd_fahb + "(��)");
        egu.getColumn("yingd").setWidth(60);
        egu.getColumn("kuid").setHeader(Locale.kuid_fahb + "(��)");
        egu.getColumn("kuid").setWidth(60);
        egu.getColumn("yuns").setHeader(Locale.yuns_fahb + "(��)");
        egu.getColumn("yuns").setWidth(60);
        egu.getColumn("koud").setHeader(Locale.koud_fahb + "(��)");
        egu.getColumn("koud").setWidth(60);
        egu.getColumn("kous").setHeader(Locale.kous_fahb + "(��)");
        egu.getColumn("kous").setHidden(true);
        egu.getColumn("kous").setWidth(60);
        egu.getColumn("kouz").setHeader(Locale.kouz_fahb + "(��)");
        egu.getColumn("kouz").setHidden(true);
        egu.getColumn("kouz").setWidth(60);
        egu.getColumn("koum").setHeader(Locale.koum_fahb + "(��)");
        egu.getColumn("koum").setHidden(true);
        egu.getColumn("koum").setWidth(60);
        egu.getColumn("zongkd").setHeader(Locale.zongkd_fahb + "(��)");
        egu.getColumn("zongkd").setHidden(true);
        egu.getColumn("zongkd").setWidth(80);
        egu.getColumn("sanfsl").setHeader(Locale.sanfsl_fahb + "(��)");
        egu.getColumn("sanfsl").setHidden(true);
        egu.getColumn("sanfsl").setWidth(80);
        egu.getColumn("jianjl").setHeader(Locale.MRtp_jianjl + "(��)");
        egu.getColumn("jianjl").setWidth(80);
        egu.getColumn("ructzl").setHeader(Locale.MRtp_ructzl + "(��)");
        egu.getColumn("ructzl").setHidden(true);
        egu.getColumn("ructzl").setWidth(90);
        egu.getColumn("zhuangt").setHeader("״̬");
        egu.getColumn("zhuangt").setHidden(true);

        //�������ڵ�Ĭ��ֵ,
        egu.getColumn("riq").setDefaultValue(getNianf() + "-" + getYuef() + "-01");
        ResultSetList r = con.getResultSetList("select id,mingc from diancxxb where id=" + visit.getDiancxxb_id() + " order by mingc");
        String mingc = "";
        if (r.next()) {
            mingc = r.getString("mingc");
        }
        con.Close();
        egu.getColumn("diancxxb_id").setDefaultValue(mingc);
        //NumberField nf = new NumberField();

        egu.getColumn("gongysb_id").setEditor(null);
        egu.getColumn("jihkjb_id").setEditor(null);
        egu.getColumn("pinzb_id").setEditor(null);
        egu.getColumn("yunsfsb_id").setEditor(null);
        egu.getColumn("zhuangt").setEditor(null);
        egu.getColumn("fenx").setEditor(null);
        egu.getColumn("fenx").setDefaultValue("����");
        //��Ӧ��
        ComboBox c4 = new ComboBox();
        egu.getColumn("gongysb_id").setEditor(c4);
        c4.setEditable(true);
        egu.getColumn("gongysb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id,mingc from meikdqb   order by mingc"
                ));
        egu.getColumn("gongysb_id").setReturnId(true);
        //�ƻ��ھ�
        egu.getColumn("jihkjb_id").setEditor(new ComboBox());
        egu.getColumn("jihkjb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id, mingc from jihkjb"));
        egu.getColumn("jihkjb_id").setReturnId(true);
        //Ʒ��
        egu.getColumn("pinzb_id").setEditor(new ComboBox());
        egu.getColumn("pinzb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id, mingc from pinzb where leib='ú'"));
        egu.getColumn("pinzb_id").setReturnId(true);
        egu.getColumn("pinzb_id").setDefaultValue("ԭú");
        //���䷽ʽ
        egu.getColumn("yunsfsb_id").setEditor(new ComboBox());
        egu.getColumn("yunsfsb_id").setComboEditor(egu.gridId,
                new IDropDownModel("select id, mingc from yunsfsb"));
        egu.getColumn("yunsfsb_id").setReturnId(true);
        egu.getColumn("yunsfsb_id").setDefaultValue("��·");

        String Sql = "select x.zhi from xitxxb x where x.leib='�±�' and x.danw='����' and x.beiz='ʹ��'";
        ResultSetList rs = con.getResultSetList(Sql);
        if (rs != null) {
            while (rs.next()) {
                String zhi = rs.getString("zhi");
                if (egu.getColByHeader(zhi) != null) {
                    egu.getColByHeader(zhi).hidden = true;
                }
            }
        }

        StringBuffer sb1 = new StringBuffer();

        sb1.append("gridDiv_grid.on('beforeedit',function(e){");
        sb1.append("if(e.record.get('GONGYSB_ID')=='�ܼ�'){e.cancel=true;}");//���糧�е�ֵ��"�ϼ�"ʱ,��һ�в�����༭
        sb1.append("});");

        //�趨�ϼ��в�����
        sb1.append("function gridDiv_save(record){if(record.get('gongysb_id')=='�ܼ�') return 'continue';}");

        egu.addOtherScript(sb1.toString());

        // /���ð�ť
        egu.addTbarText("���");
        ComboBox comb1 = new ComboBox();
        comb1.setWidth(60);
        comb1.setTransform("NianfDropDown");
        comb1.setId("NianfDropDown");//���Զ�ˢ�°�
        comb1.setLazyRender(true);//��̬��
        comb1.setEditable(true);
        egu.addToolbarItem(comb1.getScript());

        egu.addTbarText("�·�");
        ComboBox comb2 = new ComboBox();
        comb2.setWidth(50);
        comb2.setTransform("YuefDropDown");
        comb2.setId("YuefDropDown");//���Զ�ˢ�°�
        comb2.setLazyRender(true);//��̬��
        comb2.setEditable(true);
        egu.addToolbarItem(comb2.getScript());
        egu.addTbarText("-");
//		�糧��
        egu.addTbarText("��λ:");
        ExtTreeUtil etu = new ExtTreeUtil("diancTree", ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage().getVisit()).getDiancxxb_id(), getTreeid());
        setTree(etu);
        egu.addTbarTreeBtn("diancTree");

        egu.addTbarText("-");// ���÷ָ���

        // �ж������Ƿ�����
        boolean isLocked = isLocked(con);
//		ˢ�°�ť
        StringBuffer rsb = new StringBuffer();
        rsb.append("function (){")
                .append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('NianfDropDown').value+'��'+Ext.getDom('YuefDropDown').value+'�µ�����,���Ժ�'", true))
                .append("document.getElementById('RefreshButton').click();}");
        GridButton gbr = new GridButton("ˢ��", rsb.toString());
        gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
        egu.addTbarBtn(gbr);
        //�ж������Ƿ��Ѿ��ϴ� ������ϴ� �����޸� ɾ�� �������
        if (visit.getRenyjb() == 3) {
            if (yincan == false) {
                //		��Ӱ�ť
                GridButton gb_insert = new GridButton(GridButton.ButtonType_Insert, egu.gridId, egu.getGridColumns(), null);
                gb_insert.setId("INSERT");
                egu.addTbarBtn(gb_insert);
                //		���ɰ�ť
                GridButton gbc = new GridButton("����", getBtnHandlerScript("CreateButton"));
                gbc.setDisabled(con.getHasIt(sql_sl));
                gbc.setIcon(SysConstant.Btn_Icon_Create);
                egu.addTbarBtn(gbc);
                //		ɾ����ť
                GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
                gbd.setIcon(SysConstant.Btn_Icon_Delete);
                egu.addTbarBtn(gbd);
                //		���水ť
                GridButton ght = new GridButton(GridButton.ButtonType_Save, egu.gridId, egu.getGridColumns(), "SaveButton", MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
//				GridButton ght = new GridButton(GridButton.ButtonType_Save_condition, egu.gridId,egu.getGridColumns(), "SaveButton",MainGlobal.getExtMessageShow("���ڱ�������,���Ժ�...", "������...", 200));
//				GridButton ght = new GridButton(GridButton.ButtonType_SaveAll, egu.gridId,egu.getGridColumns(), "SaveButton");
                ght.setId("SAVE");
                egu.addTbarBtn(ght);
            }
        }


//		 ---------------ҳ��js�ļ��㿪ʼ------------------------


        String sb_str =
                "gridDiv_grid.on('afteredit',function(e){\n" +
                        "rec=gridDiv_ds.getAt(e.row);\n" +
                        "var falge=0;\n" +
                        "var gonghdw=rec.get('GONGYSB_ID');\n" +
                        "var jihkjb=rec.get('JIHKJB_ID');\n" +
                        "var pinzb=rec.get('PINZB_ID');\n" +
                        "var yunsfsb=rec.get('YUNSFSB_ID');\n" +
                        "var rows=gridDiv_ds.getTotalCount();\n" +
                        "for (var i=0;i<rows;i++){\n" +
                        "var rec1=gridDiv_ds.getAt(i);\n" +
                        "var gonghdw1=rec1.get('GONGYSB_ID');\n" +
                        "var jihkjb1=rec1.get('JIHKJB_ID');\n" +
                        "var pinzb1=rec1.get('PINZB_ID');\n" +
                        "var yunsfsb1=rec1.get('YUNSFSB_ID');\n" +
                        "if(i==e.row){\n" +
                        "continue;\n" +
                        "}else if (gonghdw==gonghdw1 && jihkjb==jihkjb1 && pinzb==pinzb1 && yunsfsb==yunsfsb1){\n" +
                        "falge=1;\n Ext.MessageBox.alert('��ʾ��Ϣ',\"��¼����������\"+(i+1)+\"������������ȫ��ͬ�������޸����ݣ�\");\n" +
                        "}else{\n" +
                        " continue;\n" +
                        "}\n" +
                        "}\n" +
                        "if(falge==1){\n" +
                        "Ext.getCmp(\"SAVE\").setDisabled(true) ;\n" +
                        "Ext.getCmp(\"INSERT\").setDisabled(true) ;\n" +
                        "}else{\n" +
                        "Ext.getCmp(\"SAVE\").setDisabled(false) ;\n" +
                        "Ext.getCmp(\"INSERT\").setDisabled(false) ;\n" +
                        "}" +
                        "});";

        StringBuffer sb = new StringBuffer(sb_str);

        egu.addOtherScript(sb.toString());
        setExtGrid(egu);
        con.Close();
    }

    public String getBtnHandlerScript(String btnName) {
//		��ť��script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianf() + "��" + getYuef() + "��";
        btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("���������ݽ�����")
                    .append(cnDate).append("���Ѵ����ݣ��Ƿ������");
        } else {
            btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
//		-------------------------------------------------------------------
                .append(";Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',")
                .append("width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO}); \n")
//		-------------------------------------------------------------------
                .append("}; // end if \n")
                .append("});}");
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
            visit.setList1(null);
            visit.setString1(null);
            visit.setString2(null);
            visit.setString3(null);
            setYuefValue(null);
            setNianfValue(null);
            this.getYuefModels();
            this.getNianfModels();
            visit.setShifsh(true);
            this.setTreeid(null);
            setRiq();

        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                setYuefValue(null);
                setNianfValue(null);
                this.getYuefModels();
                this.getNianfModels();
                visit.setShifsh(true);
                this.setTreeid(null);
                setRiq();
            }
        }
        getSelectData(null);
    }

    public boolean isLocked(JDBCcon con) {
        Visit visit = (Visit) getPage().getVisit();
        String CurrODate ="";
        CurrODate = DateUtil.FormatOracleDate(getNianf() + "-"	+ getYuef() + "-01");
        String chk_sql="SELECT ID\n" +
                "  FROM YUESHCHJB\n" +
                " WHERE RIQ = "+CurrODate+"\n" +
                "   AND DIANCXXB_ID = "+getTreeid()+"\n" +
                "UNION\n" +
                "SELECT Z.ID\n" +
                "  FROM YUEZLB Z, YUETJKJB T\n" +
                " WHERE Z.YUETJKJB_ID = T.ID\n" +
                "   AND RIQ = "+CurrODate+"\n" +
                "   AND DIANCXXB_ID = "+getTreeid()+"";
        return con.getHasIt(chk_sql);
    }
    public String getNianf() {
        return ((Visit) getPage().getVisit()).getString1();
    }

    public void setNianf(String value) {
        ((Visit) getPage().getVisit()).setString1(value);
    }

    public String getYuef() {
        int intYuef = Integer.parseInt(((Visit) getPage().getVisit()).getString3());
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
//    �糧��

    //	 �õ���½�û����ڵ糧���߷ֹ�˾������
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

    //�õ��糧��������ĵ糧���ƻ��߷ֹ�˾,���ŵ�����
    public String getIDropDownDiancmc(String diancmcId) {
        if (diancmcId == null || diancmcId.equals("")) {
            diancmcId = "1";
        }
        String IDropDownDiancmc = "";
        JDBCcon cn = new JDBCcon();

        String sql_diancmc = "select d.mingc from diancxxb d where d.id=" + diancmcId;
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

    //�õ��糧��Ĭ�ϵ�վ
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


    //	�õ��糧����ֵ�ļ���,(jib:1,2,3,) 1�Ǽ���,2�Ƿֹ�˾,3�ǵ糧
    public int getDiancTreeJib() {
        JDBCcon con = new JDBCcon();
        int jib = -1;
        String DiancTreeJib = this.getTreeid();
        //System.out.println("jib:" + DiancTreeJib);
        if (DiancTreeJib == null || DiancTreeJib.equals("")) {
            DiancTreeJib = "0";
        }
        String sqlJib = "select d.jib from diancxxb d where d.id=" + DiancTreeJib;
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
