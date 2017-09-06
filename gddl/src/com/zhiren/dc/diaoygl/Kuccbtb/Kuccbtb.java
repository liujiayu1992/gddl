package com.zhiren.dc.diaoygl.Kuccbtb;

import com.zhiren.common.*;
import com.zhiren.common.ext.ExtGridUtil;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.GridButton;
import com.zhiren.common.ext.GridColumn;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.webservice.shujsc.Shujsc;
import com.zr.utils.math.Math;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;


import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import java.sql.ResultSet;
import java.util.*;
/*
 * ���ߣ���衻�
 * ʱ�䣺2010-8-19
 * ���������ݹ���Ҫ���޸��պĴ��ձ������������ֶΣ����ɵ�ú���������ú���ɵ����
 * ���У����ɵ�ú��ȡ������һ�����ֵ���ɵ����=���-���ɵ�ú����
 * 
 */
/*
 * �޸��ˣ���衻�
 * �޸�ʱ�䣺2010-11-1
 * ������
 * 		�����������ʱ��Լ���������һ��糧��û��¼�����ݣ���ô���������Ҳ����¼��
 * 
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-23
 * ���÷�Χ�������������������λ
 * �������û�������ɰ�ťʱͬʱ�����պĴ��ձ��ͷֿ��չ�����Ϣ
 *		  ���պĴ�¼����棬��ÿ��ˢ�½���ʱ��ѯ��ѡ���ڵ�������1������ѡ����Ϊֹ�Ƿ���ڲ����������ݣ�������Ϊ0�����ݣ���
 *			�����������ʾ����һ�δ��ڲ��������ݵ������д��ڲ��������ݣ�����ʾ�û���Ը��յ���Ϣ���в�¼��
 *		���պĴ�¼����棬���û�������水ťʱ���������ÿ���Ƿ���ȷ���ĶԻ���Ҫ���û�ȷ�ϣ�����û���ȷ���򷵻ص���ǰ�����Ҳ����档
 *		���պĴ�¼�������ÿ�µ�1�տ��Ե���������ĩ��ˮ�ֲ���𡢵���������ӯ����Ϣ������ʱ�����Ϣ�����ã�
 *			������ϵͳ�������պĴ��ձ���������Ϣ�ɱ༭��������н���������
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-11-29
 * ʹ�÷�Χ�������������������λ
 * �����������պĴ��ձ�ʵʱ���¿�湦�ܣ�������ĳһ�տ����������֮��Ŀ����Ϣ��
 * 		������ʱ���µ��յ���ú��-������Ϣ�������������ں�Ŀ����
 */	
/*
 * ���ߣ����
 * ʱ�䣺2012-02-07
 * ʹ�÷�Χ�������������������λ
 * ���������Ϊÿ��1����4�տ�ͨ���޸ĵ���������Ϣ���������Ϣ
 */	
/*
 * ���ߣ����
 * ʱ�䣺2012-02-29
 * ʹ�÷�Χ�������������������λ
 * ����������ѡ��λ�з��ӵ�λ��ϵͳ���á��ֳ����ܳ���ʾ���ɰ�ť����ֵΪ��ʱ��
 * 		 ϵͳ���������ɣ�ɾ�������水ť��
 */	
/*
 * ���ߣ����
 * ʱ�䣺2012-08-10
 * ���÷�Χ������������������糧��������ׯ�Ӻͺ�����
 * ���������ӳ���ú����ú���У�����ú����ú����Ӱ���档
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-10-29
 * ���÷�Χ������������������糧
 * �����������ɵ�������δ�����BUG
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-03-04
 * ���÷�Χ�����������˾
 * ������������������������һ�У��������и�����λ�����޸�getselectDAte()��Ĵ�ֵ��ʽ
 *      
 */	
/*
 * ����:���
 * ����:2013-03-25
 * �޸�����:���������ĵ�λ���Ϊ������
 */

public abstract class Kuccbtb extends BasePage {
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
    }

    //	������
    private String riq;

    public String getRiq() {
        return riq;
    }

    public void setRiq(String riq) {
        this.riq = riq;
    }

    //	ҳ��仯��¼
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    //	����������
    public IDropDownBean getChangbValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
            if (getChangbModel().getOptionCount() > 0) {
                setChangbValue((IDropDownBean) getChangbModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean2();
    }

    public void setChangbValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean2(value);
    }

    public IPropertySelectionModel getChangbModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
            setChangbModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
    }

    public void setChangbModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
    }

    public void setChangbModels() {
        Visit visit = (Visit) this.getPage().getVisit();
        StringBuffer sb = new StringBuffer();
        if (visit.isFencb()) {
            sb.append("select id,mingc from diancxxb where fuid=" + visit.getDiancxxb_id());
        } else {
            sb.append("select id,mingc from diancxxb where id=" + visit.getDiancxxb_id());
        }
        setChangbModel(new IDropDownModel(sb.toString()));
    }

    private void Save() {
        Visit visit = (Visit) getPage().getVisit();
        String CurDate = DateUtil.FormatOracleDate(getRiq());
        System.out.println(CurDate);
        long diancxxb_id = Long.parseLong(getTreeid());
        Map<String,Object> map = new HashMap<>();
        JDBCcon con = new JDBCcon();
        try{
          //  con.setAutoCommit(false);
            int flag =0;
            ExtGridUtil egu=this.getExtGrid();
            String strchange=this.getChange();
            ResultSetList mdrsl = egu.getModifyResultSet(strchange);
            if (strchange == null || "".equals(strchange)) {
                return;
            }
            while (mdrsl.next()){
                // int count=mdrsl.getColumnCount();
                String[] keys=mdrsl.getColumnNames();//Ϊ�˺��� ͨ����������
                for (String key : keys) {
                    map.put(key,mdrsl.getString(key));
                }
                //--------------���ݿ��������------------------
                if(!"0".equals(mdrsl.getString("ID"))) {
                    //ִ�и��²���
                    String    updatesql = "update KUCCBB\n" +
                            "set RUKJE   = '"+map.get("RUKJE")+"',\n" +
                            "    RUKSL   = '"+map.get("RUKSL")+"',\n" +//
                            "    RUKDJ   = '"+map.get("RUKDJ")+"',\n" +//
                            "    RUKTZSL   = '"+map.get("RUKTZSL")+"',\n" +//
                            "    RUKTZJE   = '"+map.get("RUKTZJE")+"',\n" +//
                            "    RUKRZ   = '"+map.get("RUKRZ")+"',\n" +//

                            "    RUKHJSL   = '"+map.get("RUKHJSL")+"',\n" +
                            "    RUKHJJE   = '"+map.get("RUKHJJE")+"',\n" +
                            "    RULSL   = '"+map.get("RULSL")+"',\n" +

                            "    RULJE   = '"+map.get("RULJE")+"',\n" +
                            "    RULTZJE = '"+map.get("RULTZJE")+"',\n" +
                            "    RULTZSL = '"+map.get("RULTZSL")+"',\n" +

                            "    RULHJSL = '"+map.get("RULHJSL")+"',\n" +
                            "    RULHJJE = '"+map.get("RULHJJE")+"',\n" +
                            "    RULRZ   = '"+map.get("RULRZ")+"',\n" +
                            "    KUCSL   = '"+map.get("KUCSL")+"',\n" +
                            "    KUCJE   = '"+map.get("KUCJE")+"',\n" +
                            "    KUCDJ   = '"+map.get("KUCDJ")+"',\n" +
                            "    KUCRZ   = '"+map.get("KUCRZ")+"',\n" +
                            "    ZUORKCSL   = '"+map.get("ZUORKCSL")+"',\n" +
                            "    ZUORKCDJ   = '"+map.get("ZUORKCDJ")+"',\n" +
                            "    ZUORKCJE   = '"+map.get("ZUORKCJE")+"'\n" +
                            "where ID = "+ diancxxb_id +"\n" +
                            "and RIQ = "+CurDate+"\n"+
                            "and MEIZ = '"+map.get("MEIZ")+"'";
                    con.getUpdate(updatesql);
                }else {
                    //ִ�в������
                    String sql = "insert (rukje,)values" + map.get("rukje");
                    String insertsql=
                            "insert into KUCCBB\n" +
                                    "  (RIQ,\n" +
                                    "   MEIZ,\n" +
                                    "   RUKSL,\n" +
                                    "   RUKDJ,\n" +
                                    "   RUKJE,\n" +
                                    "   RUKTZSL,\n" +
                                    "   RUKTZJE,\n" +
                                    "   RUKHJSL,\n" +
                                    "   RUKHJJE,\n" +
                                    "   RUKRZ,\n" +
                                    "   RULSL,\n" +
                                    "   RULJE,\n" +
                                    "   RULTZSL,\n" +
                                    "   RULTZJE,\n" +
                                    "   RULHJSL,\n" +
                                    "   RULHJJE,\n" +
                                    "   RULRZ,\n" +
                                    "   KUCSL,\n" +
                                    "   KUCJE,\n" +
                                    "   KUCDJ,\n" +
                                    "   KUCRZ,\n" +
                                    "   ZUORKCSL,\n" +
                                    "   ZUORKCDJ,\n" +
                                    "   ZUORKCJE)\n" +
                                    "values\n" +
                                    "  (CurDate, map.get(\"MEIZ\"),\n" +
                                    "  map.get(\"RUKSL\"),\n" +
                                    "  map.get(\"RUKDJ\"),\n" +
                                    "  map.get(\"RUKJE\"),\n" +
                                    "  map.get(\"RUKTZSL\"),\n" +
                                    "  map.get(\"RUKTZJE\"),\n" +
                                    "  map.get(\"RUKHJSL\"),\n" +
                                    "  map.get(\"RUKHJJE\"),\n" +
                                    "  map.get(\"RUKRZ\") ,\n" +
                                    "  map.get(\"RULSL\"),\n" +
                                    "  map.get(\"RULJE\"),\n" +
                                    "  map.get(\"RULTZSL\") ,\n" +
                                    "  map.get(\"RULTZJE\") ,\n" +
                                    "  map.get(\"RULHJSL\"),\n" +
                                    "  map.get(\"RULHJJE\"),\n" +
                                    "  map.get(\"RULRZ\"),\n" +
                                    "  map.get(\"KUCSL\"),\n" +
                                    "  map.get(\"KUCJE\"),\n" +
                                    "  map.get(\"KUCDJ\"),\n" +
                                    "  map.get(\"KUCRZ\"),\n" +
                                    "  map.get(\"ZUORKCSL\"),\n" +
                                    "  map.get(\"ZUORKCDJ\"),\n" +
                                    "  map.get(\"ZUORKCJE\"));";
                    con.getInsert(insertsql);

                }





            }
        }catch (Exception e){
            e.printStackTrace();
            con.rollBack();
        }finally {
            con.Close();
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

    private boolean _SubmitButton = false;

    public void SubmitButton(IRequestCycle cycle) {
        _SubmitButton = true;
    }

    private boolean isParentDc(JDBCcon con) {
        String sql = "select * from diancxxb where fuid = " + getTreeid();
        return con.getHasIt(sql);
    }
//���ɰ�ť--zengw
    //˼·:����ҳ������,���'����' ����̨��ȡ���ĵ�����(������),���㲻�ܸı������      ��������ӵ����ݿ�     ˢ��ҳ��
    //��ʼ
    private void CreateData() {
        Visit visit = (Visit) getPage().getVisit();
        String CurDate = DateUtil.FormatOracleDate(getRiq());
        long diancxxb_id = Long.parseLong(getTreeid());
        Map<String,Object> map = new HashMap<>();
        JDBCcon con = new JDBCcon();
        try{
           // con.setAutoCommit(false);
            int flag =0;
            ExtGridUtil egu=this.getExtGrid();
            String strchange=this.getChange();
            ResultSetList mdrsl = egu.getModifyResultSet(strchange);
            if (strchange == null || "".equals(strchange)) {
                return;
            }
            while (mdrsl.next()){
               // int count=mdrsl.getColumnCount();
                String[] keys=mdrsl.getColumnNames();//Ϊ�˺��� ͨ����������
                for (String key : keys) {
                    map.put(key,mdrsl.getString(key));
                }
               // map.put("RULRZ",mdrsl.getString("qnet_ar"));
                map.put("RUKJE",Math.round(Math.getGongsjg(map,"RUKSL*RUKDJ"),2));//�����

                map.put("RUKHJJE",Math.round(Math.getGongsjg(map,"RUKJE+RUKTZJE"),2));//���ϼƽ��
                map.put("RUKHJSL",Math.getGongsjg(map,"RUKSL+RUKTZSL"));//���ϼ�����

                map.put("RULJE",Math.round(Math.getGongsjg(map,"RULSL*((ZUORKCJE+RUKJE)/(ZUORKCSL+RUKSL))"),2));//��¯���rulje=rulsl*buhsdj              buhsdj=(zuorkcje+rukje)/(zuorkcsl+ruksl)
                map.put("RULTZJE",Math.round(Math.getGongsjg(map,"RUKDJ*RULSL"),2));//���������rultzje =rukdj*rulsl
                //ʹ�üӷ�
                map.put("RULHJSL",Math.getGongsjg(map,"RULSL+RULTZSL"));//��¯�ϼ�����rulhjsl ��¯����+��������   rulsl+rultzsl
                map.put("RULHJJE",Math.round(Math.getGongsjg(map,"RULJE+RULTZJE"),2));//rulhjje ��¯���+�������   rulje+rultzje


                /*
SELECT round_new(DECODE(SUM(MEIL), 0, 0, SUM(qnet_ar/0.0041816 * MEIL) / SUM(MEIL)),0) AS qnet_ar
from RULMZLB*/

                String rulrzsql="SELECT ROUND_NEW(DECODE(SUM(MEIL), 0, 0, SUM(qnet_ar/0.0041816 * MEIL) / SUM(MEIL)),0) AS qnet_ar from RULMZLB  where rulrq="+CurDate;
                ResultSetList rsl = con.getResultSetList(rulrzsql);
                System.out.println(rsl);
                if(rsl.next()){
                    map.put("RULRZ",rsl.getString("qnet_ar"));//�����ֵ
                }


                map.put("KUCJE",Math.round(Math.getGongsjg(map,"ZUORKCJE+RUKJE-RULSL*ZUORKCDJ"),2));//�����kucje =      ָ������˰�����(Ԫ)  zuorkcje+rukje-rulsl*zuorkcdj
                map.put("KUCDJ",Math.round(Math.getGongsjg(map,"KUCJE/KUCSL"),2));//��浥��  kucdj =kucje /kucsl

                String zkucrzsql="SELECT ROUND_NEW(DECODE(SUM(MEIL), 0, 0, SUM(qnet_ar/0.0041816 * MEIL) / SUM(MEIL)),0) AS qnet_ar from RULMZLB  where rulrq="+CurDate+"-1";

                ResultSetList rsl1 = null;
                if (zkucrzsql!=null&&!"".equals(zkucrzsql)) {
                    rsl1 = con.getResultSetList(zkucrzsql);
                    if(rsl1.next()){
                        map.put("ZUORKCRZ",rsl.getString("qnet_ar"));//�����ֵ
                    }
                }

                String ruc ="select  CHANGFRZ*CHANGFSL as ruc  from  RUCBMDJRBB";
                ResultSetList rsl2 = null;
                if (ruc != null&&!"".equals(zkucrzsql)) {

                    rsl2 = con.getResultSetList(ruc);
                if(rsl2.next()){
                        map.put("RUC",rsl2.getString("ruc"));
                //�漰����
                map.put("KUCRZ",Math.getGongsjg(map,"(ZUORKCRZ*ZUORKCSL+RUC-RULRZ*RULSL)/KUCSL"));//�����ֵ: �����տ����*���տ����+�����볧��*�����볡��-������¯��*������¯����/���տ��
                        //���տ����:SELECT DECODE(SUM(MEIL), 0, 0, SUM(qnet_ar * MEIL) / SUM(MEIL)) AS qnet_ar from RULMZLB  where rulrq=date'2017-06-21'-1
                        //���տ����:ZUORKCSL
                        //�����볧��*�����볡�� ��ѯ��select*from  RUCBMDJRBB   changfrz ������ֵ   changfsl ��������  CHANGFRZ*CHANGFSL    sql:select  CHANGFRZ*CHANGFSL as dangr  from  RUCBMDJRBB
                        //��¯��ֵ:SELECT DECODE(SUM(MEIL), 0, 0, SUM(qnet_ar * MEIL) / SUM(MEIL)) AS qnet_ar from RULMZLB  where rulrq=date'2017-06-21'

                        // ������¯�� : RULSL
                       // ���տ���� : KUCSL
                    }
                }

                map.put("ZUORKCDJ",Math.round(Math.getGongsjg(map,"ZUORKCJE/ZUORKCSL"),2));//���տ�浥�� ZUORKCDJ =ZUORKCJE/ZUORKCSL


                //--------------���ݿ��������------------------
                       if(!"0".equals(mdrsl.getString("ID"))) {
                           //ִ�и��²���
                           /*String updatesql="update KUCCBB set rukje='map.get(\"RUKJE\")',\n" +
                                   "rulje='map.get(\"RULJE\")',rultzje='map.get(\"RULTZJE\")',rulhjsl='map.get(\"RULHJSL\")',rulhjje='map.get(\"RULHJJE\")',rulrz='map.get(\"RULRZ\")',kucje='map.get(\"KUCJE\")',kucdj='map.get(\"KUCDJ\")',kucrz='map.get(\"KUCRZ\")'\n" +
                                   "\n" +
                                   " where id=diancxxb_id and riq =CurDate;";*/

                       String    updatesql = "update KUCCBB\n" +
                                   "set RUKJE   = '"+map.get("RUKJE")+"',\n" +
                                   "    RUKSL   = '"+map.get("RUKSL")+"',\n" +//
                                   "    RUKDJ   = '"+map.get("RUKDJ")+"',\n" +//
                                   "    RUKTZSL   = '"+map.get("RUKTZSL")+"',\n" +//
                                   "    RUKTZJE   = '"+map.get("RUKTZJE")+"',\n" +//
                                   "    RUKRZ   = '"+map.get("RUKRZ")+"',\n" +//

                                   "    RUKHJSL   = '"+map.get("RUKHJSL")+"',\n" +
                                   "    RUKHJJE   = '"+map.get("RUKHJJE")+"',\n" +
                                   "    RULSL   = '"+map.get("RULSL")+"',\n" +

                                   "    RULJE   = '"+map.get("RULJE")+"',\n" +
                                   "    RULTZJE = '"+map.get("RULTZJE")+"',\n" +
                                   "    RULTZSL = '"+map.get("RULTZSL")+"',\n" +

                                   "    RULHJSL = '"+map.get("RULHJSL")+"',\n" +
                                   "    RULHJJE = '"+map.get("RULHJJE")+"',\n" +
                                   "    RULRZ   = '"+map.get("RULRZ")+"',\n" +
                                   "    KUCSL   = '"+map.get("KUCSL")+"',\n" +
                                   "    KUCJE   = '"+map.get("KUCJE")+"',\n" +
                                   "    KUCDJ   = '"+map.get("KUCDJ")+"',\n" +
                                   "    KUCRZ   = '"+map.get("KUCRZ")+"',\n" +
                                   "    ZUORKCSL   = '"+map.get("ZUORKCSL")+"',\n" +
                                   "    ZUORKCDJ   = '"+map.get("ZUORKCDJ")+"',\n" +
                                   "    ZUORKCJE   = '"+map.get("ZUORKCJE")+"'\n" +
                                   "where ID = "+ diancxxb_id +"\n" +
                                   "and RIQ = "+CurDate+"\n" +
                                   "and MEIZ = '"+map.get("MEIZ")+"'";
                           con.getUpdate(updatesql);
                       }else {
                           //ִ�в������
                           String sql = "insert (rukje,)values" + map.get("rukje");
                           String insertsql=
                                   "insert into KUCCBB\n" +
                                           "  (RIQ,\n" +
                                           "   MEIZ,\n" +
                                           "   RUKSL,\n" +
                                           "   RUKDJ,\n" +
                                           "   RUKJE,\n" +
                                           "   RUKTZSL,\n" +
                                           "   RUKTZJE,\n" +
                                           "   RUKHJSL,\n" +
                                           "   RUKHJJE,\n" +
                                           "   RUKRZ,\n" +
                                           "   RULSL,\n" +
                                           "   RULJE,\n" +
                                           "   RULTZSL,\n" +
                                           "   RULTZJE,\n" +
                                           "   RULHJSL,\n" +
                                           "   RULHJJE,\n" +
                                           "   RULRZ,\n" +
                                           "   KUCSL,\n" +
                                           "   KUCJE,\n" +
                                           "   KUCDJ,\n" +
                                           "   KUCRZ,\n" +
                                           "   ZUORKCSL,\n" +
                                           "   ZUORKCDJ,\n" +
                                           "   ZUORKCJE)\n" +
                                           "values\n" +
                                           "  (CurDate, map.get(\"MEIZ\"),\n" +
                                           "  map.get(\"RUKSL\"),\n" +
                                           "  map.get(\"RUKDJ\"),\n" +
                                           "  map.get(\"RUKJE\"),\n" +
                                           "  map.get(\"RUKTZSL\"),\n" +
                                           "  map.get(\"RUKTZJE\"),\n" +
                                           "  map.get(\"RUKHJSL\"),\n" +
                                           "  map.get(\"RUKHJJE\"),\n" +
                                           "  map.get(\"RUKRZ\") ,\n" +
                                           "  map.get(\"RULSL\"),\n" +
                                           "  map.get(\"RULJE\"),\n" +
                                           "  map.get(\"RULTZSL\") ,\n" +
                                           "  map.get(\"RULTZJE\") ,\n" +
                                           "  map.get(\"RULHJSL\"),\n" +
                                           "  map.get(\"RULHJJE\"),\n" +
                                           "  map.get(\"RULRZ\"),\n" +
                                           "  map.get(\"KUCSL\"),\n" +
                                           "  map.get(\"KUCJE\"),\n" +
                                           "  map.get(\"KUCDJ\"),\n" +
                                           "  map.get(\"KUCRZ\"),\n" +
                                           "  map.get(\"ZUORKCSL\"),\n" +
                                           "  map.get(\"ZUORKCDJ\"),\n" +
                                           "  map.get(\"ZUORKCJE\"));";
                                con.getInsert(insertsql);

                       }





            }
        }catch (Exception e){
            e.printStackTrace();
            con.rollBack();
        }finally {
            con.Close();
        }



     //   con.getUpdate(sql.toString());
     //   con.getUpdate(sql3.toString());
      /*  if (flag==-1){
            con.rollBack();
            con.Close();
            WriteLog.writeErrorLog(ErrorMessage.InsertDatabaseFail + "SQL:"
                    + sql.toString());
            setMsg(ErrorMessage.InsertDatabaseFail);
            return;
        }
        if(flag!=-1){
            setMsg("����ɹ���");
        }
        con.commit();
        con.Close();*/

//		����ʱ�Զ��������պĴ�ͷֿ�����
 /*       AutoCreateDaily_Report_gd RP = new AutoCreateDaily_Report_gd();
        String rbb = RP.CreateRBB(con, diancxxb_id, DateUtil.getDate(getRiq()));
        String fcb = RP.CreateFCB(con, diancxxb_id, DateUtil.getDate(getRiq()));
        String Smsg = "";
        if (rbb.length() > 0) {
            Smsg += rbb + "<br>";
        }
        if (fcb.length() > 0) {
            Smsg += fcb + "<br>";
        }
        if (Smsg.length() > 0) {
            setMsg(Smsg);
        }
        con.Close();*/
    }

    public String getValueSql(GridColumn gc, String value) {
        if ("string".equals(gc.datatype)) {
            if (gc.combo != null) {
                if (gc.returnId) {
                    return "" + gc.combo.getBeanId(value);
                } else {
                    return "'" + value == null ? "" : "'" + value + "'";
                }
            } else {

                return "'" + value == null ? "" : "'" + value + "'";
            }

        } else if ("date".equals(gc.datatype)) {
            return "to_date('" + value + "','yyyy-mm-dd')";
        } else if ("float".equals(gc.datatype)) {
            return value == null || "".equals(value) ? "null" : value;
        } else {
            return value;
        }
    }
    public GridColumn getColumn(int colindex) {
        return (GridColumn) getGridColumns().get(colindex);
    }
    public GridColumn getColumn(String coldataIndex) {
        for (int i = 0; i < getGridColumns().size(); i++) {
            if (coldataIndex.toUpperCase().equals(
                    getColumn(i).dataIndex.toUpperCase())) {
                return getColumn(i);
            }
        }
        return null;
    }
    public List getGridColumns() {
        if (gridColumns == null) {
            gridColumns = new ArrayList();
        }
        return gridColumns;
    }
    public List gridColumns;
    //����
    private boolean _CreateClick = false;

    public void CreateButton(IRequestCycle cycle) {
        _CreateClick = true;
    }

    private void DelData() {
        String CurDate = DateUtil.FormatOracleDate(getRiq());
        String diancxxb_id = getTreeid();
        StringBuffer sb = new StringBuffer();
        JDBCcon con = new JDBCcon();
        sb.append("begin \n");
//		�ж��Ƿ�ͬ�����¿�� Ĭ��ͬ����������Ϊ��ʱ��ͬ������
        if (MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ�ʵʱ���¿��", "0", "��").equals("��")) {
            String kuc_sql = "select jingz+CHANGWML - fady - gongry - qity - cuns - feiscy + tiaozl + shuifctz + panyk kucca from shouhcrbb where diancxxb_id=" + getTreeid() + " and riq = " + DateUtil.FormatOracleDate(this.getRiq());
            ResultSetList kuc_rsl = con.getResultSetList(kuc_sql);
            if (kuc_rsl.next()) {
//					���µ�ǰ�����Ժ�����п��
                sb.append("update shouhcrbb set ")
                        .append("kuc = kuc - ").append(kuc_rsl.getDouble("KUCCA"))
                        .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                        .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
//					���µ�ǰ�����Ժ�����пɵ����
                sb.append("update shouhcrbb set ")
                        .append("kedkc = kuc -bukdml ")
                        .append(" where riq >").append(DateUtil.FormatOracleDate(this.getRiq()))
                        .append(" and diancxxb_id = ").append(getTreeid()).append(";\n");
            }
            kuc_rsl.close();
        }
        sb.append("delete from shouhcrbb where diancxxb_id=").append(diancxxb_id).append(" and riq=").append(CurDate).append(";\n");
        sb.append("end;");

        con.getUpdate(sb.toString());
        con.Close();
    }

    private boolean _DelClick = false;

    public void DelButton(IRequestCycle cycle) {
        _DelClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_SaveClick) {
            _SaveClick = false;
            Save();
        }
        if (_Refreshclick) {
            _Refreshclick = false;
        }

        if (_CreateClick) {
            _CreateClick = false;
            CreateData();
        }
        if (_DelClick) {
            _DelClick = false;
            DelData();
        }
        if (_SubmitButton) {
            _SubmitButton = false;
            submit();
        }
        getSelectData();
    }

    private void submit() {
        JDBCcon con = new JDBCcon();
        try {
//            System.out.println("submit");

            String riq = this.getRiq();
            String sql = "select id from shouhcrbb where riq=date'" + riq + "'";
            ResultSet rs = con.getResultSet(sql);
            String id;
            if (rs.next()) {
                id = rs.getString("id");
            } else {
                this.setMsg("���ݲ�����!");
                return;
            }
            //��ӽӿ�����
            Shujsc sc = new Shujsc();
            sc.addjiekrw(id, "shouhcrbb", "0", "197", riq);
            sc.setEndpointAddress("http://localhost/zdt/InterCom_dt.jws");
            sc.request("shouhcrbb");//ִ�нӿ�����
            sql = "update shouhcrbb set zhaungt=1 where id=" + id;
            int r = con.getUpdate(sql);
            if (r != -1) {
                this.setMsg("�ύ�ɹ�!");
            } else {
                this.setMsg("�ύʧ��!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            con.rollBack();
        } finally {
            con.Close();
        }
    }

    public void getSelectData() {
        JDBCcon con = new JDBCcon();
        try {
            Visit visit = (Visit) getPage().getVisit();
            String CurDate = DateUtil.FormatOracleDate(getRiq());
            String Diancxxb_id = getTreeid();
            StringBuffer sb = new StringBuffer();

            sb.append("select * \n")
                    .append("from KUCCBB  where  riq=").append(CurDate);
                    /*.append( "union all\n")
                    .append("select * \n")
                    .append("from KUCCBB  where id =")
                    .append(Diancxxb_id).append(" and riq=").append(" to_date((select to_char(sysdate, 'yyyy-MM-dd') from dual),'yyyy-mm-dd') ");*/
            ResultSetList rsl = con.getResultSetList(sb.toString());
            if (rsl == null) {
                WriteLog.writeErrorLog(ErrorMessage.NullResult + "��������SQL:" + sb.toString());
                setMsg(ErrorMessage.NullResult);
                return;
            }
            ExtGridUtil egu = new ExtGridUtil("gridDiv", rsl);
            egu.setTableName("Kuccbtb");
            egu.setWidth("bodyWidth");
            egu.addPaging(0);
            egu.setGridType(ExtGridUtil.Gridstyle_Edit);
            //���Ӹ�ѡ��
            egu.setGridSelModel(ExtGridUtil.GridselModel_Check);
            egu.addColumn(1,new GridColumn(GridColumn.ColType_Check));
            egu.getColumn("id").setHidden(true);
            egu.getColumn("id").setEditor(null);
            egu.getColumn("MEIZ").setHeader("ú��");
            egu.getColumn("MEIZ").setEditor(null);
            egu.getColumn("MEIZ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("MEIZ").setWidth(60);
            //egu.getColumn("MEIZ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("ZUORKCSL").setHeader("���տ������<br>(��)");
            egu.getColumn("ZUORKCSL").setWidth(80);
//		egu.getColumn("dangrgm").setHidden(true);
//		egu.getColumn("haoyqkdr").setHidden(true);
            egu.getColumn("ZUORKCDJ").setHeader("���տ�浥��<br>(Ԫ/��)");
            egu.getColumn("ZUORKCDJ").setEditor(null);
            egu.getColumn("ZUORKCDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("ZUORKCDJ").setWidth(80);
            egu.getColumn("ZUORKCJE").setHeader("���տ����<br>(Ԫ)");
            egu.getColumn("ZUORKCJE").setWidth(80);
            egu.getColumn("RUKSL").setHeader("�������<br>(��)");
            egu.getColumn("RUKSL").setWidth(80);
            egu.getColumn("RUKDJ").setHeader("��ⵥ��<br>(Ԫ/��)");
            egu.getColumn("RUKDJ").setWidth(60);
            egu.getColumn("RUKJE").setHeader("�����<br>(Ԫ)");
            egu.getColumn("RUKJE").setEditor(null);
            egu.getColumn("RUKJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RUKJE").setWidth(60);
            egu.getColumn("RUKTZSL").setHeader("����������<br>(��)");
            egu.getColumn("RUKTZSL").setWidth(80);
            egu.getColumn("RUKTZJE").setHeader("���������<br>(Ԫ)");
//            egu.getColumn("RUKTZJE").setEditor(null);
//            egu.getColumn("RUKJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RUKTZJE").setWidth(80);
            egu.getColumn("RUKHJSL").setHeader("���ϼ�����");
            egu.getColumn("RUKHJSL").setEditor(null);//
            egu.getColumn("RUKHJSL").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RUKHJSL").setWidth(80);
            egu.getColumn("RUKHJJE").setHeader("���ϼƽ��");
            egu.getColumn("RUKHJJE").setEditor(null);//
            egu.getColumn("RUKHJJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RUKHJJE").setWidth(80);
            egu.getColumn("RUKRZ").setHeader("���ϼ���ֵ");//db  �������ֵ
            egu.getColumn("RUKRZ").setEditor(null);//
            egu.getColumn("RUKRZ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RUKRZ").setWidth(80);

            egu.getColumn("RULSL").setHeader("��¯����");
            egu.getColumn("RULSL").setWidth(60);
            egu.getColumn("RULJE").setHeader("��¯���");
            egu.getColumn("RULJE").setEditor(null);//
            egu.getColumn("RULJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RULJE").setWidth(80);
            egu.getColumn("RULTZSL").setHeader("��¯��������");
            egu.getColumn("RULTZSL").setWidth(80);
            egu.getColumn("RULTZJE").setHeader("��¯�������");
//            egu.getColumn("RULTZJE").setEditor(null);//
//            egu.getColumn("RULTZJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RULTZJE").setWidth(80);

            egu.getColumn("RULHJSL").setHeader("��¯�ϼ�����");
            egu.getColumn("RULHJSL").setEditor(null);//
            egu.getColumn("RULHJSL").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RULHJSL").setWidth(80);
            egu.getColumn("RULHJJE").setHeader("��¯�ϼƽ��");
            egu.getColumn("RULHJJE").setEditor(null);//
            egu.getColumn("RULHJJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RULHJJE").setWidth(80);
            egu.getColumn("RULRZ").setHeader("��¯�ϼ���ֵ");//db ����¯��ֵ
            egu.getColumn("RULRZ").setEditor(null);//
            egu.getColumn("RULRZ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("RULRZ").setWidth(80);
            egu.getColumn("KUCSL").setHeader("�������");
            egu.getColumn("KUCSL").setEditor(null);//
            egu.getColumn("KUCSL").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("KUCSL").setWidth(60);
            egu.getColumn("KUCJE").setHeader("�����");
            egu.getColumn("KUCJE").setEditor(null);//
            egu.getColumn("KUCJE").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("KUCJE").setWidth(60);
            egu.getColumn("KUCDJ").setHeader("��浥��");
            egu.getColumn("KUCDJ").setEditor(null);//
            egu.getColumn("KUCDJ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("KUCDJ").setWidth(60);
            egu.getColumn("KUCRZ").setHeader("�����ֵ<br>��kcal/kg��");
            egu.getColumn("KUCRZ").setEditor(null);//
            egu.getColumn("KUCRZ").setRenderer("function(value,metadata){metadata.css='tdTextext'; return value;}");
            egu.getColumn("KUCRZ").setWidth(60);




//        egu.getColumn("fadl").setHeader("������<br>(��ǧ��ʱ)");
//		egu.getColumn("fadl").setWidth(80);
//
//		egu.getColumn("gongrl").setHeader("������<br>(����)");
//		egu.getColumn("gongrl").setWidth(70);


//		egu.getColumn("biaoz").setHeader("Ʊ��<br>(��)");
//		egu.getColumn("biaoz").setWidth(60);


//		egu.getColumn("yingd").setHeader("ӯ��<br>(��)");
//		egu.getColumn("yingd").setWidth(60);
//
//		egu.getColumn("kuid").setHeader("����<br>(��)");
//		egu.getColumn("kuid").setWidth(60);
//
//
//
//		egu.getColumn("feiscy").setHeader("��������<br>(��)");
//		egu.getColumn("feiscy").setWidth(60);
//
//
//
//
//		egu.getColumn("bukdml").setHeader("���ɵ�<br>ú��(��)");
//		egu.getColumn("bukdml").setWidth(60);
//		egu.getColumn("changwml").setHeader("����ú��<br>��ú��(��)");
//		egu.getColumn("changwml").setWidth(80);
//		egu.getColumn("kedkc").setHeader("�ɵ����<br>(��)");
//		egu.getColumn("kedkc").setWidth(60);
//		egu.getColumn("kedkc").setHidden(true);
            //egu.getColumn("kedkc").setEditor(null);
//		�жϵ�ǰϵͳ�����Ƿ�Ϊ����1����4�ա����Ϊ����1����4�գ���ô��ѡ�������Ƿ�Ϊ������ĩ�������������������Բ������
            String sql = "SELECT 1 FROM (SELECT FIRST_DAY(SYSDATE) FD,\n" +
                    "               TO_CHAR(LAST_DAY(ADD_MONTHS(SYSDATE, -1)), 'yyyy-mm-dd') LD\n" +
                    "          FROM DUAL) SDAY,\n" +
                    "       (SELECT TO_CHAR(SYSDATE, 'yyyy-mm-dd') FD,\n" +
                    "               TO_CHAR(" + CurDate + ", 'yyyy-mm-dd') LD\n" +
                    "          FROM DUAL) UDAY\n" +
                    " WHERE SDAY.LD = UDAY.LD\n" +
                    "	AND SYSDATE BETWEEN SDAY.FD AND SDAY.FD+3";
//		������ܲ�ѯ��������ô���𣬵�������ˮ�ֲ��������ӯ����Ϣ��������ʾ�Ҳ��ɱ༭
//            if (!con.getHasIt(sql) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���������Ϣ�ɱ༭", "0", "��").equals("��")) {
//                egu.getColumn("cuns").setEditor(null);
//                egu.getColumn("cuns").setHidden(true);
//                egu.getColumn("tiaozl").setEditor(null);
//                egu.getColumn("tiaozl").setHidden(true);
//                egu.getColumn("shuifctz").setEditor(null);
//                egu.getColumn("shuifctz").setHidden(true);
//                egu.getColumn("panyk").setEditor(null);
              //  egu.getColumn("panyk").setHidden(true);//
//            }

//            if (!MainGlobal.getXitxx_item("�պĴ��ձ�", "�պĴ��ձ���ú�ɱ༭", "0", "��").equals("��")) {
//			egu.getColumn("biaoz").setEditor(null);
                //egu.getColumn("jingz").setEditor(null);//
                //egu.getColumn("yuns").setEditor(null);//
//			egu.getColumn("yingd").setEditor(null);
//			egu.getColumn("kuid").setEditor(null);
                //egu.getColumn("kuc").setEditor(null);//
//            }
            egu.addTbarText("����:");
            DateField df = new DateField();
             df.Binding("RIQ", "");
            df.setValue(getRiq());
            egu.addToolbarItem(df.getScript());
            egu.addTbarText("-");// ���÷ָ���
            // ������
            egu.addTbarText("��λ:");
            ExtTreeUtil etu = new ExtTreeUtil("diancTree",                                //��ط� ����
                    ExtTreeUtil.treeWindowType_Dianc, ((Visit) this.getPage()
                    .getVisit()).getDiancxxb_id(), getTreeid());
            setTree(etu);
            egu.addTbarTreeBtn("diancTree");                                              //
//		ˢ�°�ť
            StringBuffer rsb = new StringBuffer();
            rsb.append("function (){")
                    .append(MainGlobal.getExtMessageBox("'����ˢ��'+Ext.getDom('RIQ').value+'������,���Ժ�'", true))
                    .append("document.getElementById('RefreshButton').click();}");
            GridButton gbr = new GridButton("ˢ��", rsb.toString());
            gbr.setIcon(SysConstant.Btn_Icon_Refurbish);
            egu.addTbarBtn(gbr);
//		���ɰ�ť
//            if (visit.isFencb() && isParentDc(con) && MainGlobal.getXitxx_item("�պĴ��ձ�", "�ֳ����ܳ���ʾ���ɰ�ť", diancxxb_id, "��").equals("��")) {
//
//            } else {
            sql = "select zhaungt from shouhcrbb where riq=date'" + this.getRiq() + "'";
            ResultSet rs = con.getResultSet(sql);
            boolean z = false;
            if (rs.next()) {
                z = rs.getInt("zhaungt") != 0;
            }
//            GridButton gbc = new GridButton("����", getBtnHandlerScript("CreateButton"));
//            gbc.setIcon(SysConstant.Btn_Icon_Create);
//            gbc.setDisabled(z);
//            egu.addTbarBtn(gbc);
            egu.addToolbarButton("����", GridButton.ButtonType_Sel, "CreateButton",null, SysConstant.Btn_Icon_Show);

//            GridButton bc = new GridButton("����", "function(){ "
//                    + " var rec = gridDiv_sm.getSelected(); "
//                    + " if(rec != null){var id = rec.get('ID');"
//                    + " var Cobjid = document.getElementById('CHANGEID');"
//                    + " Cobjid.value = id;"
//                    + " var sbtn=document.getElementById('SaveButton');sbtn.click();sbtn.setAttribute('disabled', true);"+MainGlobal.getExtMessageShow("���ڴ�������,���Ժ�...", "������...", 200)+"}}");
//            bc.setIcon(SysConstant.Btn_Icon_Save);
//            egu.addTbarBtn(bc);
////			ɾ����ť
//            GridButton gbd = new GridButton("ɾ��", getBtnHandlerScript("DelButton"));
//            gbd.setIcon(SysConstant.Btn_Icon_Delete);
//            gbd.setDisabled(z);
//            egu.addTbarBtn(gbd);
//			��д���水ť
            String Script = "function (){\n" +
                    "Ext.MessageBox.confirm('��ʾ��Ϣ','���á������Ϣ�Ƿ���ȷ��',function(btn){if(btn == 'yes'){\n" +
                    "var gridDivsave_history = '';var Mrcd = gridDiv_ds.getModifiedRecords();\n" +
                    "for(i = 0; i< Mrcd.length; i++){\n" +
                    "if(typeof(gridDiv_save)=='function'){ var revalue = gridDiv_save(Mrcd[i]); if(revalue=='return'){return;}else if(revalue=='continue'){continue;}}\n" +
                    "if(Mrcd[i].get('DANGRGM') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�DANGRGM ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('DANGRGM') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� DANGRGM �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('DANGRGM')!=0 && Mrcd[i].get('DANGRGM') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� DANGRGM ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('FADY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶη����� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('FADY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('FADY')!=0 && Mrcd[i].get('FADY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('GONGRY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶι����� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('GONGRY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('GONGRY')!=0 && Mrcd[i].get('GONGRY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('QITY') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ������� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('QITY') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('QITY')!=0 && Mrcd[i].get('QITY') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('CUNS') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶδ��� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('CUNS') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ���� �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('CUNS')!=0 && Mrcd[i].get('CUNS') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ���� ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('TIAOZL') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶε����� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('TIAOZL') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ������ �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('TIAOZL')!=0 && Mrcd[i].get('TIAOZL') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ������ ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('SHUIFCTZ') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ�ˮ�ֲ���� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('SHUIFCTZ') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ˮ�ֲ���� �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('SHUIFCTZ')!=0 && Mrcd[i].get('SHUIFCTZ') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ˮ�ֲ���� ����Ϊ��');return;\n" +
                    "}if(Mrcd[i].get('PANYK') < -100000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ���ӯ�� ������Сֵ -100000000');return;\n" +
                    "}if( Mrcd[i].get('PANYK') >100000000000){Ext.MessageBox.alert('��ʾ��Ϣ',' �� '+(i+1)+'��,�ֶ� ��ӯ�� �������ֵ 100000000000');return;\n" +
                    "}if(Mrcd[i].get('PANYK')!=0 && Mrcd[i].get('PANYK') == ''){Ext.MessageBox.alert('��ʾ��Ϣ','�ֶ� ��ӯ�� ����Ϊ��');return;\n" +
                    "}gridDivsave_history += '<result>' + '<sign>U</sign>' + '<ID update=\"true\">' + Mrcd[i].get('ID').replace(/[<]/g,'&lt;').replace(/[>]/g,'&gt;')+ '</ID>'\n" +
//				"+ '<FADL update=\"true\">' + Mrcd[i].get('FADL')+ '</FADL>'\n"+
//				"+ '<GONGRL update=\"true\">' + Mrcd[i].get('GONGRL')+ '</GONGRL>'\n"+
                    "+ '<JINGZ update=\"true\">' + Mrcd[i].get('JINGZ')+ '</JINGZ>'\n" +
                    "+ '<DANGRGM update=\"true\">' + Mrcd[i].get('DANGRGM')+ '</DANGRGM>'\n" +
//				"+ '<BIAOZ update=\"true\">' + Mrcd[i].get('BIAOZ')+ '</BIAOZ>'\n"+
                    "+ '<YUNS update=\"true\">' + Mrcd[i].get('YUNS')+ '</YUNS>'\n" +
//				"+ '<YINGD update=\"true\">' + Mrcd[i].get('YINGD')+ '</YINGD>'\n"+
//				"+ '<KUID update=\"true\">' + Mrcd[i].get('KUID')+ '</KUID>'\n"+
                    "+ '<FADY update=\"true\">' + Mrcd[i].get('FADY')+ '</FADY>'\n" +
                    "+ '<GONGRY update=\"true\">' + Mrcd[i].get('GONGRY')+ '</GONGRY>'\n" +
                    "+ '<QITY update=\"true\">' + Mrcd[i].get('QITY')+ '</QITY>'\n" +
//				"+ '<HAOYQKDR update=\"true\">' + Mrcd[i].get('HAOYQKDR')+ '</HAOYQKDR>'\n"+
//				"+ '<FEISCY update=\"true\">' + Mrcd[i].get('FEISCY')+ '</FEISCY>'\n"+
                    "+ '<CUNS update=\"true\">' + Mrcd[i].get('CUNS')+ '</CUNS>'\n" +
                    "+ '<TIAOZL update=\"true\">' + Mrcd[i].get('TIAOZL')+ '</TIAOZL>'\n" +
                    "+ '<SHUIFCTZ update=\"true\">' + Mrcd[i].get('SHUIFCTZ')+ '</SHUIFCTZ>'\n" +
                    "+ '<PANYK update=\"true\">' + Mrcd[i].get('PANYK')+ '</PANYK>'\n" +
                    "+ '<KUC update=\"true\">' + Mrcd[i].get('KUC')+ '</KUC>'\n" +
//				"+ '<CHANGWML update=\"true\">' + Mrcd[i].get('CHANGWML')+ '</CHANGWML>'\n"+
//				"+ '<BUKDML update=\"true\">' + Mrcd[i].get('BUKDML')+ '</BUKDML>'\n"+
//				"+ '<KEDKC update=\"true\">' + Mrcd[i].get('KEDKC')+ '</KEDKC>'\n"+
                    " + '</result>' ; }\n" +
                    "if(gridDiv_history=='' && gridDivsave_history==''){\n" +
                    "Ext.MessageBox.alert('��ʾ��Ϣ','û�н��иĶ����豣��');\n" +
                    "}else{\n" +
                    "var Cobj = document.getElementById('CHANGE');Cobj.value = '<result>'+gridDiv_history+gridDivsave_history+'</result>';\n" +
                    "document.getElementById('SaveButton').click();\n" +
                    "Ext.MessageBox.show({msg:'���ڴ�������,���Ժ�...',progressText:'������...',width:300,wait:true,waitConfig: {interval:200},icon:Ext.MessageBox.INFO});\n" +
                    "}\n" +
                    "};});\n" +
                    "}";
//			
//			GridButton gbs = new GridButton(GridButton.ButtonType_Save,"gridDiv",egu.getGridColumns(),"SaveButton");
//			egu.addTbarBtn(gbs);
            egu.addToolbarButton("����", GridButton.ButtonType_Sel, "SaveButton",null, SysConstant.Btn_Icon_Save);
          /* GridButton gbs = new GridButton("����", "SaveButton");//
            gbs.setIcon(SysConstant.Btn_Icon_Save);
            gbs.setDisabled(z);
            egu.addTbarBtn(gbs);*/
//            GridButton tj = new GridButton("�ύ", getBtnHandlerScript("SubmitButton"));
//            tj.setIcon(SysConstant.Btn_Icon_SelSubmit);
//            tj.setDisabled(z);
//            egu.addTbarBtn(tj);

//            }

//		grid ���㷽��
            egu.addOtherScript("gridDiv_grid.on('afteredit',countKuc);\n");

            AutoCreateDaily_Report_gd DR = new AutoCreateDaily_Report_gd();
            String msg = DR.ChkRBB(con, Diancxxb_id, DateUtil.getDate(getRiq()));
            if (msg.length() > 0) {
                egu.addOtherScript("Ext.MessageBox.alert('��ʾ��Ϣ','" + msg + "�����ݲ�������');\n");
            }

            setExtGrid(egu);
        } catch (Exception e) {
            e.printStackTrace();
            con.rollBack();
        } finally {
            con.Close();
        }

    }

    public String getBtnHandlerScript(String btnName) {
//		��ť��script
        StringBuffer btnsb = new StringBuffer();
        String cnDate = "'+Ext.getDom('RIQ').value+'";
        btnsb.append("function (){Ext.MessageBox.confirm('��ʾ��Ϣ','");
        if (btnName.endsWith("CreateButton")) {
            btnsb.append("���������ݽ�ͬʱ����:���պĴ���չ���<br>")
                    .append(cnDate).append("���Ѵ����ݣ��Ƿ������");
         } else if (btnName.endsWith("DelButton")) {
            btnsb.append("�Ƿ�ɾ��").append(cnDate).append("�����ݣ�");
        } else {
            btnsb.append("�Ƿ��ύ").append(cnDate).append("�����ݣ�");
        }
        btnsb.append("',function(btn){if(btn == 'yes'){")
                .append("document.getElementById('").append(btnName).append("').click()")
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
            visit.setList1(null);
            setRiq(DateUtil.FormatDate(DateUtil.AddDate(new Date(), -1, DateUtil.AddType_intDay)));
            setChangbModels();
            getSelectData();
        }
   }

    private String treeid;

    public String getTreeid() {
        if (treeid == null || treeid.equals("")) {
            treeid = String.valueOf(((Visit) getPage().getVisit())
                    .getDiancxxb_id());
        }
        return treeid;
    }

    public void setTreeid(String treeid) {
        this.treeid = treeid;
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
