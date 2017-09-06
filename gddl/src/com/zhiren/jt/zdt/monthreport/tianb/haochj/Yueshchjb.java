package com.zhiren.jt.zdt.monthreport.tianb.haochj;

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
import com.zhiren.common.ext.form.*;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
/*
 * ʱ�䣺2010-10-14
 * ���ߣ�wangxj
 * �޸����ݣ����ȷ��ˮ�ֲ�ʹ�üӷ����㡣
 */
/*
* ʱ�䣺2009-03-26
* ���ߣ� ll
* �޸����ݣ�1��ȥ����˺ͻ��˹��ܡ�
* 		   2������ж�ѭ���ۼƵ�ʱ�䣬�������û�����ݣ�ѭ����������
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
* ʱ�䣺2009-07-28
* ���ߣ� ll
* �޸����ݣ�����ˮ�ֲ���š��޸Ŀ�湫ʽ��kc=kc-shuifctz
*
*/

/*
* ʱ�䣺2009-10-21
* ���ߣ� sy
* //ʵ������Ϊ���ɱ༭������ȡ�������
*
*/
public class Yueshchjb extends BasePage implements PageValidateListener {
    private static final int shouml_shouml = 0;
    //	private static final int sunh_sh = 0;
    private static final int shouml_tiaozl = 1;
    private static final int shouml_yuns = 2;

    private static final int meihl_fady = 0;
    private static final int meihl_gongry = 1;
    private static final int meihl_qith = 2;
    private static final int qickc_kc = 0;
    private static final int huiz_qickc = 0;
    private static final int huiz_shouml = 1;
    private static final int huiz_fady = 2;
    private static final int huiz_gongry = 3;
    private static final int huiz_qith = 4;
    private static final int huiz_sunh = 5;
    private static final int huiz_diaocl = 6;
    private static final int huiz_panyk = 7;
    private static final int huiz_kuc = 8;
    private static final int huiz_shuifctz = 9;
    private static final int huiz_yuns = 10;
    private static final int shuifctz_shuifctz = 0;
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

    private void Save() {
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

        //--------------------------------
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
        JDBCcon con = new JDBCcon();
        Visit visit = (Visit) this.getPage().getVisit();

        ResultSetList rsl = visit.getExtGrid1().getModifyResultSet(getChange());
        StringBuffer sql = new StringBuffer();
        while (rsl.next()) {
            sql.delete(0, sql.length());
            sql.append("begin \n");
            long id = 0;
            if ("0".equals(rsl.getString("ID"))) {
                id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));
                sql.append("insert into yueshchjb("
                        + "id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc,diancxxb_id)values("
                        + id
                        + ","
                        + CurrODate
                        + ",'����',"
                        + rsl.getDouble("qickc") + ","
                        + rsl.getDouble("shouml") + ","
                        + rsl.getDouble("fady") + ","
                        + rsl.getDouble("gongry") + ","
                        + rsl.getDouble("qith") + ","
                        + rsl.getDouble("sunh") + ","
                        + rsl.getDouble("yuns") + ","
                        + rsl.getDouble("diaocl") + ","
                        + rsl.getDouble("panyk") + ","
                        + rsl.getDouble("shuifctz") + ","
                        + rsl.getDouble("kuc") + ","
                        + visit.getDiancxxb_id() + ");\n");
            } else {
                sql.append("update yueshchjb set qickc="
                        + rsl.getDouble("qickc") + ",shouml="
                        + rsl.getDouble("shouml") + ",fady="
                        + rsl.getDouble("fady") + ",gongry="
                        + rsl.getDouble("gongry") + ",qith="
                        + rsl.getDouble("qith") + ",sunh="
                        + rsl.getDouble("sunh") + ",yuns="
                        + rsl.getDouble("yuns") + ",diaocl="
                        + rsl.getDouble("diaocl") + ",panyk="
                        + rsl.getDouble("panyk") + ",shuifctz="
                        + rsl.getDouble("shuifctz") + ",kuc="
                        + rsl.getDouble("kuc")
                        + " where id=" + rsl.getLong("id") + ";\n");
            }
            sql.append("end;");
            con.getUpdate(sql.toString());


            //����ۼ�
            String sqllj = "";
            int i = 0;
            for (i = 0; i <= 12 - intMonth; i++) {

                boolean jzyf = false;//���ý�ֹ�·ݵ�boolean
                String pdyf = "select hc.id as id from yueshchjb hc where hc.diancxxb_id=" + visit.getDiancxxb_id() + " and hc.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd')";
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
                    if (intMonth + i != 1) {
                        sqllj =
                                "select diancxxb_id, sum(qickc) as qickc,sum(shouml) as shouml, sum(fady) as fady,sum(gongry) as gongry,sum(qith) as qith, sum(sunh) as sunh,sum(yuns) as yuns,sum(diaocl) as diaocl, sum(panyk) as panyk, sum(shuifctz) as shuifctz,sum(kuc) as kuc\n" +
                                        "  from yueshchjb\n" +
                                        "  where (( riq= to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and fenx = '����') or\n" +
                                        "         (riq=add_months( to_date('" + intyear + "-" + (intMonth + i) +
                                        "-01','yyyy-mm-dd'), -1) and fenx = '�ۼ�')) and diancxxb_id =" + visit.getDiancxxb_id() +
                                        "  group by (diancxxb_id)\n";
                    } else {
                        sqllj =
                                "select id,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc\n" +
                                        " from yueshchjb where riq=to_date('" + intyear + "-" + (intMonth + i) +
                                        "-01','yyyy-mm-dd') and diancxxb_id=" + visit.getDiancxxb_id() + "and fenx='����' order by fenx";
                    }
                    ResultSetList rsllj = con.getResultSetList(sqllj);
                    //			 rsllj = visit.getExtGrid2().getModifyResultSet(getChange());

                    StringBuffer strsqllj = new StringBuffer("begin \n");
                    long yuelj_id = 0;
                    while (rsllj.next()) {
                        //��ȡ�ۼƵ����ۼ�״̬
                        String shzt = "select zhuangt from yueshchjb z where z.diancxxb_id=" + visit.getDiancxxb_id() + " and z.riq=to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd') and z.fenx='�ۼ�'";
                        ResultSetList shenhzt = con.getResultSetList(shzt);
                        long zhuangt = 0;
                        while (shenhzt.next()) {
                            zhuangt = shenhzt.getLong("zhuangt");
                        }

                        strsqllj.append("delete from ").append("yueshchjb").append(
                                " where id =").append("(select distinct sl.id from yueshchjb sl where sl.riq=to_date('" + intyear + "-" + (intMonth + i) +
                                "-01','yyyy-mm-dd') and sl.fenx='�ۼ�' and sl.diancxxb_id=" + visit.getDiancxxb_id() + ")").append(";\n");
                        yuelj_id = Long.parseLong(MainGlobal.getNewID(((Visit) getPage().getVisit()).getDiancxxb_id()));

                        strsqllj.append("insert into yueshchjb("
                                + "id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc,diancxxb_id,zhuangt)values("
                                + yuelj_id
                                + ",to_date('" + intyear + "-" + (intMonth + i) + "-01','yyyy-mm-dd'),'�ۼ�',"
                                + rsllj.getDouble("qickc") + ","
                                + rsllj.getDouble("shouml") + ","
                                + rsllj.getDouble("fady") + ","
                                + rsllj.getDouble("gongry") + ","
                                + rsllj.getDouble("qith") + ","
                                + rsllj.getDouble("sunh") + ","
                                + rsllj.getDouble("yuns") + ","
                                + rsllj.getDouble("diaocl") + ","
                                + rsllj.getDouble("panyk") + ","
                                + rsllj.getDouble("shuifctz") + ","
                                + rsllj.getDouble("kuc") + ","
                                + visit.getDiancxxb_id() + ","
                                + zhuangt + ");\n");
                    }
                    strsqllj.append("end;");
                    con.getInsert(strsqllj.toString());
                }
            }
        }
        con.Close();
        setMsg("����ɹ�!");
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

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }
    public void submit(IRequestCycle cycle) {
        if (_SaveClick) {
            _SaveClick = false;
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
        try {
            String strSql = "delete from yueshchjb where to_char(riq,'yyyy-MM')='" + riq + "' and diancxxb_id = " + getTreeid();
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

    public double[] getShouml(JDBCcon con, Visit visit, String CurrODate) {
        double shouml[] = null;//��ú��
        String strSql = " select zhi from xitxxb where leib='�±�' and mingc='��ú������' and beiz='ʹ��' and diancxxb_id=" + visit.getDiancxxb_id();
        String mingc = "laimsl";//��ú������
        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return shouml;
        }
        if (rs.next()) {
            mingc = rs.getString("zhi");//��ϵͳ��Ϣ����ȡ����ú���Ķ���
        }
        //�ж��Ƿ�����ú��
        strSql = "select sum(" + mingc + ") as shouml,sum(ructzl) ructzl,sum(yuns) yuns from yueslb ys,yuetjkjb yj\n" +
                " where ys.yuetjkjb_id=yj.id and yj.riq=" + CurrODate + " and yj.diancxxb_id=" + visit.getDiancxxb_id() + "\n" +
                " and fenx ='����' group by fenx";
        rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return shouml;
        }
        if (rs.next()) {
            shouml = new double[3];
            shouml[shouml_shouml] = rs.getDouble("shouml");//�õ���ú��
            shouml[shouml_tiaozl] = rs.getDouble("ructzl");
            shouml[shouml_yuns] = rs.getDouble("yuns");//����
        } else {
            setMsg("��ú����ȡʧ�ܣ���ȷ�������±��Ѿ����ɣ�");
            return shouml;
        }
        return shouml;
    }

    public double[] getMeihl(JDBCcon con, Visit visit, String CurrODate) {
        double meihl[] = null;
        String strSql = "select sum(mh.fadhy) as fadhy,sum(mh.gongrhy) as gongrhy,sum(mh.qity) as qity,sum(feiscy) feiscy\n" +
                " from meihyb mh where mh.rulrq >=" + CurrODate + " and mh.rulrq < Add_Months(" + CurrODate + ",1)\n" +
                " and mh.diancxxb_id=" + visit.getDiancxxb_id() + " group by mh.rulrq";
        if (!con.getHasIt(strSql)) {
            setMsg("ȱ��ú�����ݣ�");
            meihl = new double[3];
            meihl[meihl_fady] = 0.0;
            meihl[meihl_gongry] = 0.0;
            meihl[meihl_qith] = 0.0;
            return meihl;
        }
        strSql = "select nvl(sum(mh.fadhy),0) as fadhy,nvl(sum(mh.gongrhy),0) as gongrhy,nvl(sum(mh.qity + mh.feiscy),0) as qity\n" +
                " from meihyb mh where mh.rulrq >=" + CurrODate + " and mh.rulrq < Add_Months(" + CurrODate + ",1)\n" +
                " and mh.diancxxb_id=" + visit.getDiancxxb_id();
        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return meihl;
        }
        if (rs.next()) {
            meihl = new double[3];
            meihl[meihl_fady] = rs.getDouble("fadhy");//������
            meihl[meihl_gongry] = rs.getDouble("gongrhy");//������
            meihl[meihl_qith] = rs.getDouble("qity");//������
        } else {
//			�����ܷ����Ĵ���
            setMsg("δ֪����");
            return meihl;
        }
        return meihl;
    }

    public double[] getShuifctz(JDBCcon con, Visit visit, String CurrODate) {
        double shuifctz[] = null;
        String strSql = " select shuifctz from yueshchjb where riq=Add_Months(" + CurrODate + ",-1) and fenx ='����' and diancxxb_id=" + visit.getDiancxxb_id();//��ѯ�ϸ��µĿ����
        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return shuifctz;
        }
        shuifctz = new double[1];
        if (rs.next()) {//�������µĿ�����ݣ�����/�ۼ�
            shuifctz[shuifctz_shuifctz] = rs.getDouble("shuifctz");
        } else {
            shuifctz[shuifctz_shuifctz] = 0.0;
        }
        return shuifctz;
    }

    public double[] getQickc(JDBCcon con, Visit visit, String CurrODate) {
        double qickc[] = null;
        String strSql = " select kuc from yueshchjb where riq=Add_Months(" + CurrODate + ",-1) and fenx ='����' and diancxxb_id=" + visit.getDiancxxb_id();//��ѯ�ϸ��µĿ����
        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return qickc;
        }
        qickc = new double[1];
        if (rs.next()) {//�������µĿ�����ݣ�����/�ۼ�
            qickc[qickc_kc] = rs.getDouble("kuc");
        } else {
            qickc[qickc_kc] = 0.0;
        }
        return qickc;
    }

    public double[] getLeij(JDBCcon con) {
        Visit visit = (Visit) getPage().getVisit();
        String CurrODate = DateUtil.FormatOracleDate(getNianf() + "-" + getYuef() + "-01");
        double leij[] = null;
        String strSql = " select qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc from yueshchjb where riq=Add_Months(" + CurrODate + ",-1) and fenx ='�ۼ�' and diancxxb_id=" + visit.getDiancxxb_id();//��ѯ�ϸ��µĿ����
        ResultSetList rs = con.getResultSetList(strSql);
        if (rs == null) {//�ж��Ƿ�����ʧ��
            WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + strSql);
            setMsg(ErrorMessage.NullResult);
            return leij;
        }
        leij = new double[11];
        if (rs.next()) {
            leij[huiz_qickc] = rs.getDouble("qickc");
            leij[huiz_shouml] = rs.getDouble("shouml");
            leij[huiz_fady] = rs.getDouble("fady");
            leij[huiz_gongry] = rs.getDouble("gongry");
            leij[huiz_qith] = rs.getDouble("qith");
            leij[huiz_sunh] = rs.getDouble("sunh");
            leij[huiz_yuns] = rs.getDouble("yuns");
            leij[huiz_diaocl] = rs.getDouble("diaocl");
            leij[huiz_panyk] = rs.getDouble("panyk");
            leij[huiz_shuifctz] = rs.getDouble("shuifctz");
            leij[huiz_kuc] = rs.getDouble("kuc");
        } else {
            for (int i = 0; i < leij.length; i++) {
                leij[i] = 0.0;
            }
        }
        return leij;
    }

    public void CreateData() {
        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        con.setAutoCommit(false);
        String CurrZnDate = this.getNianf() + "��" + this.getYuef() + "��";
        String CurrODate = DateUtil.FormatOracleDate(this.getNianf() + "-" + this.getYuef() + "-01");
        int intYuef = Integer.parseInt(this.getYuef());
        String strSql = "";
        boolean flag = false;
        double[] beny = new double[11];
        double panyk = 0.0D;
        double sunh = 0.0D;
        double[] shuifctz = this.getShuifctz(con, visit, CurrODate);
        if (shuifctz != null) {
            double kuc = 0.0D;
            double[] shouml = this.getShouml(con, visit, CurrODate);
            if (shouml != null) {
                double[] meihl = this.getMeihl(con, visit, CurrODate);
                if (meihl != null) {
                    double[] qickc = this.getQickc(con, visit, CurrODate);
                    if (qickc != null) {
                        kuc += qickc[0];
                        kuc += shouml[0] - sunh - shouml[1] - shouml[2];
                        kuc = kuc - meihl[0] - meihl[1] - meihl[2];
                        kuc -= -1.0D * shuifctz[0];
                        kuc += panyk;
                        beny[0] = qickc[0];
                        beny[1] = shouml[0];
                        beny[2] = meihl[0];
                        beny[3] = meihl[1];
                        beny[4] = meihl[2];
                        beny[5] = sunh;
                        beny[10] = shouml[2];
                        beny[6] = shouml[1];
                        beny[7] = panyk;
                        beny[9] = shuifctz[0];
                        beny[8] = kuc;
                        double[] leij = this.getLeij(con);
                        if (leij != null) {
                            double[] benylj = new double[11];

                            for (int i = 1; i < leij.length - 1; ++i) {
                                benylj[i] = leij[i] + beny[i];
                            }

                            benylj[8] = beny[8];
                            strSql = "delete from yueshchjb where riq=" + CurrODate + " and diancxxb_id = " + visit.getDiancxxb_id();
                            int var22 = con.getDelete(strSql);
                            if (var22 == -1) {
                                WriteLog.writeErrorLog("ɾ����ʧ�ܣ�SQL:" + strSql);
                                this.setMsg("���ɹ��̳��ִ������պĴ�ϼ�ɾ��ʧ�ܣ�");
                                con.rollBack();
                                con.Close();
                            } else {
                                strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc) values(" + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id())) + "," + visit.getDiancxxb_id() + "," + CurrODate + ",\'����\'," + beny[0] + "," + beny[1] + "," + beny[2] + "," + beny[3] + "," + beny[4] + "," + beny[5] + "," + beny[10] + "," + beny[6] + "," + beny[7] + "," + beny[9] + "," + beny[8] + ")";
                                var22 = con.getInsert(strSql);
                                if (var22 == -1) {
                                    WriteLog.writeErrorLog("д���ʧ�ܣ�SQL:" + strSql);
                                    this.setMsg("���ɹ��̳��ִ������պĴ�ϼ�δ����ɹ���");
                                    con.rollBack();
                                    con.Close();
                                } else {
                                    if (intYuef == 1) {
                                        benylj[0] = beny[0];
                                        strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc) values(" + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id())) + "," + visit.getDiancxxb_id() + "," + CurrODate + ",\'�ۼ�\'," + beny[0] + "," + beny[1] + "," + beny[2] + "," + beny[3] + "," + beny[4] + "," + beny[5] + "," + beny[10] + "," + beny[6] + "," + beny[7] + "," + beny[9] + "," + beny[8] + ")";
                                    } else {
                                        benylj[0] = leij[0];
                                        strSql = "insert into yueshchjb(id,diancxxb_id,riq,fenx,qickc,shouml,fady,gongry,qith,sunh,yuns,diaocl,panyk,shuifctz,kuc) values(" + Long.parseLong(MainGlobal.getNewID(visit.getDiancxxb_id())) + "," + visit.getDiancxxb_id() + "," + CurrODate + ",\'�ۼ�\'," + benylj[0] + "," + benylj[1] + "," + benylj[2] + "," + benylj[3] + "," + benylj[4] + "," + benylj[5] + "," + beny[10] + "," + benylj[6] + "," + benylj[7] + "," + benylj[9] + "," + benylj[8] + ")";
                                    }

                                    var22 = con.getInsert(strSql);
                                    if (var22 == -1) {
                                        WriteLog.writeErrorLog("д���ʧ�ܣ�SQL:" + strSql);
                                        this.setMsg("���ɹ��̳��ִ������պĴ�ϼ�δ����ɹ���");
                                        con.rollBack();
                                        con.Close();
                                    } else {
                                        con.commit();
                                        con.Close();
                                        this.setMsg(CurrZnDate + "�����ݳɹ����ɣ�");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void getSelectData(ResultSetList rsl) {
        Visit visit = (Visit) this.getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhuangt = "";
        if (visit.isShifsh()) {
            if (visit.getRenyjb() == 3) {
                zhuangt = "";
            } else if (visit.getRenyjb() == 2) {
                zhuangt = " and (yshc.zhuangt=1 or yshc.zhuangt=2)";
            } else if (visit.getRenyjb() == 1) {
                zhuangt = " and yshc.zhuangt=2";
            }
        }

        String str = "";
        int treejib = this.getDiancTreeJib();
        if (treejib == 1) {
            str = "";
        } else if (treejib == 2) {
            str = "and (dc.id = " + this.getTreeid() + " or dc.fuid = " + this.getTreeid() + ")";
        } else if (treejib == 3) {
            str = "and dc.id = " + this.getTreeid();
        }

        String CurrODate = DateUtil.FormatOracleDate(this.getNianf() + "-" + this.getYuef() + "-01");
        String strSql = "select * from yueslb s, yuetjkjb k,diancxxb dc  where s.yuetjkjb_id = k.id and k.riq = " + CurrODate + " and k.diancxxb_id=dc.id " + str;
        boolean isNotReady = !con.getHasIt(strSql);
        if (isNotReady) {
            this.setMsg("����ʹ�ñ�ģ��֮ǰ������������������ݵļ��㣡");
        }

        if (rsl == null) {
            strSql = "select yshc.id,yshc.fenx,yshc.qickc,yshc.shouml,yshc.fady,yshc.gongry,yshc.qith,yshc.sunh,yshc.yuns,yshc.diaocl,yshc.panyk,yshc.shuifctz,yshc.kuc,yshc.zhuangt\n from yueshchjb yshc,diancxxb dc where yshc.riq=" + CurrODate + zhuangt + " and yshc.diancxxb_id=dc.id " + str + "and yshc.fenx=\'����\' order by yshc.fenx";
            rsl = con.getResultSetList(strSql);
        }

        boolean yincan = false;

        while (true) {
            while (rsl.next()) {
                if (visit.getRenyjb() == 3) {
                    if (rsl.getLong("zhuangt") == 0L) {
                        yincan = false;
                    } else {
                        yincan = true;
                    }
                } else if (visit.getRenyjb() == 1 || visit.getRenyjb() == 2) {
                    yincan = true;
                }
            }

            rsl.beforefirst();
            boolean showBtn;
            if (rsl.next()) {
                if(rsl.getLong("zhuangt")==0L){
                    showBtn = true;
                }else {
                    showBtn = false;
                }
                rsl.beforefirst();
            } else {
                showBtn = true;
            }

            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setGridType(1);
            egu.addPaging(0);
            egu.setTableName("yueshchjb");
            egu.setWidth("bodyWidth");
            egu.getColumn("fenx").setHeader("����");
            egu.getColumn("fenx").setWidth(60);
            egu.getColumn("fenx").setHidden(true);
            egu.getColumn("fenx").setEditor((Field) null);
            egu.getColumn("fenx").setDefaultValue("����");
            egu.getColumn("qickc").setHeader("�ڳ����(��)");
            egu.getColumn("qickc").setWidth(80);
            egu.getColumn("shouml").setHeader("ʵ����(��)");
            egu.getColumn("shouml").setWidth(80);
            egu.getColumn("fady").setHeader("������(��)");
            egu.getColumn("fady").setWidth(80);
            egu.getColumn("gongry").setHeader("������(��)");
            egu.getColumn("gongry").setWidth(80);
            egu.getColumn("qith").setHeader("������(��)");
            egu.getColumn("qith").setWidth(80);
            egu.getColumn("sunh").setHeader("�������(��)");
            egu.getColumn("sunh").setWidth(80);
            egu.getColumn("yuns").setHeader("����(��)");
            egu.getColumn("yuns").setWidth(80);
            egu.getColumn("diaocl").setHeader("������(��)");
            egu.getColumn("diaocl").setWidth(80);
            egu.getColumn("diaocl").editor.setMinValue("-1000000");
            egu.getColumn("panyk").setHeader("��ӯ��(��)");
            egu.getColumn("panyk").setWidth(80);
            egu.getColumn("panyk").editor.setMinValue("-1000000");
            egu.getColumn("shuifctz").setHeader("ˮ�ֲ����(��)");
            egu.getColumn("shuifctz").setWidth(100);
            egu.getColumn("shuifctz").editor.setMinValue("-1000000");
            egu.getColumn("kuc").setHeader("���(��)");
            egu.getColumn("kuc").setWidth(80);
            egu.getColumn("zhuangt").setHeader("״̬");
            egu.getColumn("zhuangt").setHidden(true);
            egu.getColumn("zhuangt").setEditor((Field) null);
            egu.getColumn("kuc").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("kuc").setEditor((Field) null);
            egu.getColumn("shouml").setRenderer("function(value,metadata){metadata.css=\'tdTextext\'; return value;}");
            egu.getColumn("shouml").setEditor((Field) null);
            egu.addTbarText("���");
            ComboBox comb1 = new ComboBox();
            comb1.setWidth(50);
            comb1.setTransform("NianfDropDown");
            comb1.setId("NianfDropDown");
            comb1.setLazyRender(true);
            comb1.setEditable(true);
            egu.addToolbarItem(comb1.getScript());
            egu.addTbarText("�·�");
            ComboBox comb2 = new ComboBox();
            comb2.setWidth(50);
            comb2.setTransform("YuefDropDown");
            comb2.setId("YuefDropDown");
            comb2.setLazyRender(true);
            comb2.setEditable(true);
            egu.addToolbarItem(comb2.getScript());
            egu.addTbarText("-");
            egu.addTbarText("��λ:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree", 10, ((Visit) this.getPage().getVisit()).getDiancxxb_id(), this.getTreeid());
            this.setTree(etu);
            egu.addTbarTreeBtn("diancTree");
            egu.addTbarText("-");
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){").append(MainGlobal.getExtMessageBox("\'����ˢ��\'+Ext.getDom(\'NianfDropDown\').value+\'��\'+Ext.getDom(\'YuefDropDown\').value+\'�µ�����,���Ժ�\'", true)).append("document.getElementById(\'RefreshButton\').click();}");
            GridButton gbr = new GridButton("ˢ��", rsb.toString());
            gbr.setIcon("imgs/btnicon/refurbish.gif");
            egu.addTbarBtn(gbr);
            if (visit.getRenyjb() == 3 && !yincan) {
                GridButton sb;
                if (showBtn) {
                    sb = new GridButton("����", this.getBtnHandlerScript("CreateButton"));
                    sb.setDisabled(isNotReady);
                    sb.setIcon("imgs/btnicon/create.gif");
                    egu.addTbarBtn(sb);
                    //ɾ����ť
                    GridButton gbd = new GridButton("ɾ��",getBtnHandlerScript("DelButton"));
                    gbd.setDisabled(isNotReady);
                    gbd.setIcon(SysConstant.Btn_Icon_Delete);
                    egu.addTbarBtn(gbd);
                   /* GridButton savebtn = new GridButton("����",getBtnHandlerScript("SaveButton"));
                    savebtn.setDisabled(isNotReady);
                    savebtn.setIcon(SysConstant.Btn_Icon_Save);
                    egu.addTbarBtn(savebtn);*/
                }

                /*egu.addToolbarButton(1, (String) null);*/
                sb = new GridButton(11, "gridDiv", egu.getGridColumns(), "SaveButton", MainGlobal.getExtMessageShow("���ڱ���,���Ժ�...", "������...", 200));
                sb.setDisabled(isNotReady);
                egu.addTbarBtn(sb);
                //ɾ����ť

            }

            StringBuffer sb1 = new StringBuffer();
            sb1.append("gridDiv_grid.on(\'afteredit\',function(e){");
            sb1.append("if(e.field == \'QICKC\'||e.field == \'SHOUML\'||e.field == \'FADY\'||e.field == \'GONGRY\'||e.field == \'QITH\'||e.field==\'SUNH\'||e.field==\'YUNS\'||e.field==\'DIAOCL\'||e.field==\'PANYK\'||e.field==\'SHUIFCTZ\'||e.field==\'KUC\'){e.record.set(\'KUC\',parseFloat(e.record.get(\'QICKC\')==\'\'?0:e.record.get(\'QICKC\'))+parseFloat(e.record.get(\'SHOUML\')==\'\'?0:e.record.get(\'SHOUML\'))-parseFloat(e.record.get(\'FADY\')==\'\'?0:e.record.get(\'FADY\'))-parseFloat(e.record.get(\'GONGRY\')==\'\'?0:e.record.get(\'GONGRY\')) -parseFloat(e.record.get(\'QITH\')==\'\'?0:e.record.get(\'QITH\'))-parseFloat(e.record.get(\'SUNH\')==\'\'?0:e.record.get(\'SUNH\'))-parseFloat(e.record.get(\'YUNS\')==\'\'?0:e.record.get(\'YUNS\'))-parseFloat(e.record.get(\'DIAOCL\')==\'\'?0:e.record.get(\'DIAOCL\'))+parseFloat(e.record.get(\'PANYK\')==\'\'?0:e.record.get(\'PANYK\'))-(-1)*parseFloat(e.record.get(\'SHUIFCTZ\')==\'\'?0:e.record.get(\'SHUIFCTZ\')) )};");
            sb1.append("});");
            egu.addOtherScript(sb1.toString());
            this.setExtGrid(egu);
            con.Close();
            return;
        }
    }

    public String getBtnHandlerScript(String btnName) {
//		��ť��script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = getNianf() + "��" + getYuef() + "��";
        btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("���������ݽ�����")
                    .append(cnDate).append("���Ѵ����ݣ��Ƿ������");
        } if(btnName.endsWith("DelButton")) {
            btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
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
        return getExtGrid().getGridScript();
    }

    public String getGridHtml() {
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
            visit.setShifsh(true);
            this.setTreeid(null);
            setNianfValue(null);
            setYuefValue(null);
            this.getYuefModels();
            this.getNianfModels();
            setRiq();
        }
        if (visit.getRenyjb() == 3) {
            if (!this.getTreeid().equals(visit.getDiancxxb_id() + "")) {
                visit.setActivePageName(getPageName().toString());
                visit.setList1(null);
                visit.setString1(null);
                visit.setString2(null);
                visit.setString3(null);
                visit.setShifsh(true);
                this.setTreeid(null);
                setNianfValue(null);
                setYuefValue(null);
                this.getYuefModels();
                this.getNianfModels();
                setRiq();
            }
        }
        getSelectData(null);
    }

	/*public long isLocked(JDBCcon con) {//�ж������Ƿ�������
        int intYuef=Integer.parseInt(getYuef());
		String CurrXDate = DateUtil.FormatOracleDate(getNianf()+"-"+(intYuef+1)+"-01");
		String CurrODate=getNianf()+"-"+getYuef();
		Visit visit = (Visit) getPage().getVisit();
		long falge=0;

		if(intYuef!=12){
			if(con.getHasIt("select * from yueshchjb where riq="+CurrXDate)){//�ж��ϸ����Ƿ�������
				if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
						"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
						" and yj.diancxxb_id="+visit.getDiancxxb_id())){//�жϺĴ���Ƿ�������
					falge=1;//��ú����
				}else{
					falge=2;//�¸�������
				}
			}else{
				if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
						"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
						" and yj.diancxxb_id="+visit.getDiancxxb_id())){//�жϺĴ���Ƿ�������
					falge=1;//��ú����
				}else{
					falge=0;//����ɾ��
				}
			}
		}else{
			if(con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
					"to_char(yj.riq,'yyyy-mm')='"+CurrODate+"'\n" +
					" and yj.diancxxb_id="+visit.getDiancxxb_id())){//�жϺĴ���Ƿ�������
				falge=1;//��ú����
			}else{
				falge=0;//����ɾ��
			}
		}
		return falge;
	} */

//	public boolean isLocked(JDBCcon con) {//�ж������Ƿ�������_����
//		String CurrODate = DateUtil.FormatOracleDate(getNianf()+"-"+getYuef()+"-"+"01");
//		Visit visit = (Visit) getPage().getVisit();
//		return con.getHasIt("select * from yuehcb yh,yuetjkjb yj where yh.yuetjkjb_id=yj.id and " +
//				" yj.riq="+CurrODate+"\n" +
//				" and yj.diancxxb_id="+visit.getDiancxxb_id());
//	}

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

//  �糧��

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