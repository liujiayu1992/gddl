package com.zhiren.dc.diaoygl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.ExtTreeUtil;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.dc.diaoygl.AutoCreateDaily_Report_gd;
import com.zhiren.main.Visit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/*
 * ���ߣ���衻�
 * ʱ�䣺2010-09-25
 * ������
 *     ���ݹ�����Ҫ���� �պĴ��ձ�
 */
/*
 * ���ߣ���衻�
 * ʱ�䣺2010-10-8
 * ������
 *     �޸��պĴ��ձ��еڶ�����������ݵ�SQL����Ϊ���½��ı�shouhcfkb��ȡ��
 */
/*
 * �޸��ˣ���衻�
 * �޸�ʱ�䣺2010-10-29
 * ������
 *     ��ȡ�õĿ�ʼʱ��=����ʱ��ʱ���ۼ�ֵ=�³�������ʱ����ۻ��������ۼ�ֵ=��ʼʱ��������ʱ����ۻ�
 */
/*
 * �޸��ˣ���衻�
 * �޸�ʱ�䣺2010-12-25
 * ������
 *    �޸ķֿ���ú����� ú�ۡ��˼ۡ�������ֵΪ�������ݣ��޸���ú������Լ��ʽ��  ��ÿһ��fahb_id ʵ�� sum(round_new(fahb.laimsl,0)) ��
 */
/*
 * �޸��ˣ�songy
 * �޸�ʱ�䣺201-6-14
 * ������xitxxb���Ӳ����жϣ�����ʾ��������ʹ�ͬ��������ݣ���ʾ��������
 *
 *    */
/*
 * ���ڣ�2011-10-08
 * ���ߣ���ʤ��
 * ���÷�Χ����������������е糧
 * ��������������������պĴ�����µ÷ֿ���ú������ӵ������ƻ��ھ���Ʒ���У�
 * 		���ݵ�����ʾú��������ܼƺ͸���ú���С�ơ�
 */
/*
 * ���ڣ�2011-10-26
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL���Ի�����ϢΪ������ʾ�������
 */
/*
 * ���ڣ�2011-11-01
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL���Ի�����ϢΪ����
 * 		��ʾ���зֿ���Ϣ�����û�����䷽ʽ��Ĭ��Ϊ�ա�
 * 		ׯ�ӵ糧���з��������䷽ʽ���̶�Ϊ����
 */
/*
 * ���ڣ�2011-11-03
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL�������������䵥λ������ɵ��ظ����ݡ�
 */
/*
 * ���ڣ�2011-11-04
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL�������������䵥λ������ɵ�����Ϣ��ʾ�쳣������
 */
/*
 * ���ڣ�2011-11-12
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL����ֵ��ú�ۣ��˼���ʾ�ۼ����ݡ�
 * 		 ����ͳ�ƿھ����л��ܲ������Ƿ��з�����Ϣ����
 */
/*
 * ���ڣ�2011-11-16
 * ���ߣ����
 * ���÷�Χ��������������������е糧
 * �����������ֿ���ú��������ѯSQL��������Ӧ���У�������������ʾ���з�ʽ��
 */
/*
 * ���ߣ����
 * �޸�ʱ�䣺2011-11-23
 * ���÷�Χ�������������������λ
 * �������պĴ��ձ�����������ṩϵͳ�Զ��������ܣ�
 * 			��ѯ��ѡ��λ�Ƿ�����ֵ��ú�ۣ�������Ϊ0�����ݣ�������򵯳��Ի����б�
 * 			�б��е�����Ϊ:
 * 				��λ������
 * 				��Ϣ����ȫ����ע��
 */
/*
 * ���ߣ����
 * ʱ�䣺2011-12-13
 * ���÷�Χ���������
 * ���������������Ȩ���㷽��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-01-09
 * ���÷�Χ���������
 * ���������Ӳ������Ƿ���ձ����������Ϣȡ������Ĭ��Ϊ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-05-04
 * ���÷�Χ���������
 * ������ѡ�����ڿ�ʱ���治�Զ�ˢ��,���ڿ��е����ݰ����û�ѡ��䶯
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-07-08
 * ���÷�Χ���������
 * ������������Ϣֻ���������ã������ã������ͷ�����������
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-08-10
 * ���÷�Χ������������������糧��������ׯ�Ӻͺ�����
 * ���������ӳ���ú����ú���У���ͳ�Ƶ��պ��ۼ���Ϣ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2012-09-26
 * ���÷�Χ������������������糧��������ׯ�Ӻͺ�����
 * ����������ú�ۺ��˼۵ļ�Ȩ���㷽ʽ
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-01-06
 * ���÷�Χ�����������˾
 * �������ֿ����������λ�С�
 * 		��ֹ����Ĭ�ϳ�ʼ��Ϊ��ǰ���ڵ�ǰ�졣
 * 		�����¼�û�Ϊ�����������ô�糧��Ĭ��Ϊ�����������ȫ����λ��
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-01-07
 * ���÷�Χ�����������˾
 * ������������������������,
 *      �����������������ݵ���sql.
 */

/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-03-04
 * ���÷�Χ���������
 * ��������澯����ȡ�������ӹ������ͷ�����һ�У����������ʽ
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-03-26
 * ���÷�Χ���������
 * ����������ҳ��bug
 */
/*
 * ����:���
 * ����:2013-09-30
 * �޸�����:���Ӻ�˰��ú���ۺͲ���˰��ú����
 * 			����˰��ú���ۼ��㹫ʽΪ(ú��+�˼�-ú��˰-�˼�˰)*29.271/��ֵ
 */
/*
 * ����:���
 * ����:2013-10-16
 * �޸�����:���䷽ʽֱ��ȡ���ձ��ֿ��
 */
public class Rucrlrzc extends BasePage {

    //	�����û���ʾ
    private String msg="";
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg,false);;
    }

    public boolean getRaw() {
        return true;
    }
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


    private boolean blnIsBegin = false;

    public String getPrintTable() {

        if (!blnIsBegin) {
            return "";
        }
        blnIsBegin = false;
//		setMsg(null);
        return getHetltj();
    }
    private String getHetltj() {
        JDBCcon con = new JDBCcon();
        String sDate=this.getBeginriqDate();
        String eDate=this.getEndriqDate();
//        setMsg("��");
        try {
            //----------------------------------------------------------------------------------------------------------
            this.setMsg("");
            boolean isHuay_rc=false;
            String cheksql_rc="select distinct riq from (select  to_char( f.daohrq,'yyyy-mm-dd') riq from fahb f left join zhilb z on f.zhilb_id=z.id "+
                    "where f.daohrq between date'"+sDate+"' and date'"+eDate+"' and z.id is null order by f.daohrq)\n" ;
            ResultSet crs_rc=con.getResultSet(cheksql_rc);
            String lossmsg_rc="��������:<br/>";
            while(crs_rc.next()){
                isHuay_rc=true;
                lossmsg_rc+=crs_rc.getString("riq")+"<br/>";
            }
            crs_rc.close();
            if(isHuay_rc){
                setMsg(lossmsg_rc+"ȱʧ�볧����ֵ,��ע��!<br/>");
            }


            boolean isHuay=false;
            String cheksql="select distinct riq from (select  to_char( m.rulrq,'yyyy-mm-dd') riq from meihyb m left join rulmzlb r on m.rulmzlb_id=r.id "+
                    "where m.rulrq between date'"+sDate+"' and date'"+eDate+"' and r.id is null order by m.rulrq)\n" ;
            ResultSet crs=con.getResultSet(cheksql);
            String lossmsg="��¯����:<br/>";
            while(crs.next()){
                isHuay=true;
                lossmsg+=crs.getString("riq")+"<br/>";
            }
            crs.close();
            if(isHuay){
                setMsg(this.getMsg()+"<br/>"+lossmsg+"ȱʧ��¯����ֵ,��ע��!");
            }


            //----------------------------------------------------------------------------------------------------------
            String sql = "select\n" +
                    "rulrq ,laimsl,rc_qnet_ar ,to_char(rc_stad,'90.99') ,to_char(rc_mt,'90.9') ,to_char(rc_aad,'90.99'),to_char(rc_vdaf,'90.99') ,\n" +
                    "meil ,rl_qnet_ar ,to_char(rl_stad,'90.99') ,to_char(rl_mt,'90.9') ,to_char(rl_aad,'90.99') ,to_char(rl_vdaf,'90.99'),\n" +
                    "rc_qnet_ar-rl_qnet_ar ,round_new(meil*(1-(1-rl_mt/100)/(1-rc_mt/100)),2) \n" +
                    "from (\n" +
                    "(select\n" +
                    "decode(grouping(f.daohrq),1,'�ϼ�',to_char(f.daohrq,'yyyy-mm-dd')) daohrq,\n" +
                    "sum(laimsl) laimsl,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.qnet_ar/0.0041816*laimsl)/sum(laimsl)),0) rc_qnet_ar ,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.stad*laimsl)/sum(laimsl)),2) rc_stad  ,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.mt*laimsl)/sum(laimsl)),1) rc_mt  , \n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.aad*laimsl)/sum(laimsl)),2)  rc_aad,\n" +
                    "round_new(decode(sum(nvl(laimsl,0)),0,0,sum(z.vdaf*laimsl)/sum(laimsl)),2) rc_vdaf\n" +
                    "from fahb f \n" +
                    "inner join zhilb z on z.id=f.zhilb_id\n" +
                    "where f.daohrq between date'"+sDate+"' and date'"+eDate+"'\n" +
                    "group by rollup (f.daohrq)) rc\n" +
                    "inner join \n" +
                    "(select\n" +
                    "decode(grouping(m.rulrq),1,'�ϼ�',to_char(m.rulrq,'yyyy-mm-dd')) rulrq,\n" +
                    " sum(meil) meil,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.qnet_ar/0.0041816*meil)/sum(meil)),0) rl_qnet_ar,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.stad*meil)/sum(meil)),2)  rl_stad,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.mt*meil)/sum(meil)),1) rl_mt , \n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.aad*meil)/sum(meil)),2) rl_aad ,\n" +
                    "round_new(decode(sum(nvl(meil,0)),0,0,sum(r.vdaf*meil)/sum(meil)),2) rl_vdaf\n" +
                    "from meihyb m \n" +
                    "inner join rulmzlb r on m.rulmzlb_id=r.id \n" +
                    "where m.rulrq between date'"+sDate+"' and date'"+eDate+"'\n" +
                    "group by rollup (m.rulrq)\n" +
                    "order by grouping(m.rulrq) desc,m.rulrq \n" +
                    ") rl\n" +
                    "on rc.daohrq=rl.rulrq\n" +
                    ")" ;
            ResultSetList rs = con.getResultSetList(sql);
            Report rt = new Report();
            String[][] ArrHeader;
            int[] ArrWidth;
//		if(zhib){
            int cnt=rs.getColumnCount();
            ArrHeader = new String[1][cnt];
            ArrWidth = new int[]{80, 60, 100, 100, 60, 60, 60, 60, 60, 100, 100, 60, 60, 60, 60, 60, 60, 60, 60};
            ArrHeader[0]=new String[]{ "����", "��ú��<br/>(��)", "������<br/>Qnet,ar<br/>(kcal/kg)", "ȫ������<br/>Stad<br/>(%)","ȫˮ�յ���<br/>Mt<br/>(%)",
                    "�ҷָ����<br/>Aad<br/>(%)", "�ӷ��ݸ����޻һ�<br/>Vdaf<br/>(%)",
                    "��¯ú��<br/>Qnet,ar<br/>(kcal/kg)", "������<br/>MT<br/>(kcal/kg)","ȫ������<br/>Stad<br/>(%)", "ȫˮ�յ���<br/>Mt<br/>(%)",
                    "�ҷָ����<br/>Aad<br/>(%)","�ӷ��ݸ����޻һ�<br/>Vdaf<br/>(%)",
                    "�볧��¯ú��ֵ��(kcal/kg)", "�볧��¯ˮ�ֲ������<br/>(%)"};
//            ArrHeader[0] = new String[]{"����", "�볧", "�볧", "�볧", "�볧", "�볧", "�볧", "�볧", "��¯", "��¯", "��¯", "��¯", "��¯", "��¯", "��¯", "��ֵ��", "��ֵ��", "��ֵ��", "��ֵ��"};
//            ArrHeader[1] = new String[]{"����", "����", "��ֵ", "��ֵ", "ˮ��", "���", "�ҷ�", "�ӷ���", "����", "��ֵ", "��ֵ", "ˮ��", "���", "�ҷ�", "�ӷ���", "ˮ�ֵ���ǰ", "ˮ�ֵ���ǰ", "ˮ�ֵ�����", "ˮ�ֵ�����"};
//            ArrHeader[2] = new String[]{"����", "(��)", "(MJ/kg)", "(kcal/kg)", "Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)", "(��)", "(MJ/kg)", "(kcal/kg)", "Mt(%)", "St,d(%)", "Ad(%)", "Vdaf(%)", "(MJ/kg)", "(kcal/Kg)", "(MJ/kg)", "(kcal/Kg)"};
            rt.setBody(new Table(rs, 1, 0, 1));
            //
//			int aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
//			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.body.setWidth(ArrWidth);
            rt.body.setHeaderData(ArrHeader);
            rt.setTitle("�볧��¯ú����ͳ�Ʊ�", ArrWidth);
            rt.title.setRowHeight(2, 50);
            rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
            rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
            rt.setDefaultTitle(3, 3, "�Ʊ�λ:" + ((Visit) getPage().getVisit()).getDiancqc(), Table.ALIGN_LEFT);
            rt.setDefaultTitle(7, 10, "��λ:(��λ��t��kcal/kg��%)", Table.ALIGN_RIGHT);
//		rt.setDefaultTitle(6, 2, "���:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
            rt.body.setPageRows(rt.PAPER_ROWS);
//		���ӳ��ȵ�����
//			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
            rt.body.mergeFixedCols();
            rt.body.mergeFixedRow();
            rt.body.ShowZero = true;
            rt.createDefautlFooter(ArrWidth);
            rt.setDefautlFooter(1, 17, "��ӡ����:" + DateUtil.FormatDate(new Date()), Table.ALIGN_RIGHT);
            // ����ҳ��
            _CurrentPage = 1;
            _AllPages = rt.body.getPages();
            if (_AllPages == 0) {
                _CurrentPage = 0;
            }
            return rt.getAllPagesHtml();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            con.Close();
        }
        return null;
//		}else{
//			ArrHeader = new String[3][13];
//			ArrWidth = new int[] { 80,60,80,80,60,60,80,80,60,80,80,80,80};
//			ArrHeader[0] = new String[] { "����", "�볧", "�볧","�볧","�볧","��¯","��¯","��¯","��¯","��ֵ��","��ֵ��","��ֵ��","��ֵ��"};
//			ArrHeader[1] = new String[] { "����", "����", "��ֵ","��ֵ","ˮ��","����","��ֵ","��ֵ","ˮ��","ˮ�ֵ���ǰ","ˮ�ֵ���ǰ","ˮ�ֵ�����","ˮ�ֵ�����"};
//			ArrHeader[2] = new String[] { "����", "(��)", "(MJ/kg)","(Kcal/kg)","Mt(%)","(��)","(MJ/kg)","(Kcal/kg)","Mt(%)","(MJ/kg)","(Kcal/kg)","��MJ/kg��","(kcal/kg)"};
//			rt.setBody(new Table(rs, 3, 0, 1));
//			//
//			int aw=rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(),((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
//			rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
//			rt.body.setWidth(ArrWidth);
//			rt.body.setHeaderData(ArrHeader);
//			rt.setTitle("�� �� �� ¯ �� ֵ ��", ArrWidth);
//			rt.title.setRowHeight(2, 50);
//			rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
//			rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//			rt.setDefaultTitle(1, 5, "�Ʊ�λ:" +((Visit) getPage().getVisit()).getDiancqc(),Table.ALIGN_LEFT);
//			rt.setDefaultTitle(11, 3, "��λ:(��)" ,Table.ALIGN_RIGHT);
////			rt.setDefaultTitle(6, 2, "���:"+getNianfValue().getId(),Table.ALIGN_RIGHT);
//			rt.body.setPageRows(rt.PAPER_COLROWS);
////			���ӳ��ȵ�����
//			rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//			rt.body.mergeFixedCols();
//			rt.body.mergeFixedRow();
//			rt.body.ShowZero=true;
//			rt.createDefautlFooter(ArrWidth);
//			rt.setDefautlFooter(1, 13, "��ӡ����:"+DateUtil.FormatDate(new Date()),Table.ALIGN_RIGHT);
//			// ����ҳ��
//			_CurrentPage = 1;
//			_AllPages = rt.body.getPages();
//			if (_AllPages == 0) {
//				_CurrentPage = 0;
//			}
//			con.Close();
//			return rt.getAllPagesHtml();
//		}
    }


    private String zhi ="";
    public String getZhi() {
        if(this.zhi.equals("")){
            setZhi();
        }
        return zhi;
    }
    public void setZhi() {
        Visit v = (Visit)getPage().getVisit();
        JDBCcon con = new JDBCcon();
        ResultSetList rsl = con.getResultSetList("select zhi from xitxxb where mingc = '�ձ����ص糧����' and diancxxb_id ="+v.getDiancxxb_id());
        if(rsl.next()){
            this.zhi="  "+rsl.getString("zhi");
        }else{
            this.zhi=" -1 ";
        }
        rsl.close();
        con.Close();
    }

    private String getShouhc() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String riq=FormatDate(DateUtil.getDate(getBeginriqDate()));
        String riq1=FormatDate(DateUtil.getDate(getEndriqDate()));
        //int jib=this.getDiancTreeJib();
        StringBuffer strSQL = new StringBuffer();
        String beginRiq=this.getBeginriqDate();
        String endRiq=this.getEndriqDate();
        if(beginRiq.equals(endRiq)){
            beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
        }

        strSQL.append("select decode(grouping(dc.mingc), 0, dc.mingc, 1, '�ϼ�') diancmc,\n");

        if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�Ƿ���ձ����������Ϣȡ��", "0", "��").equals("��")){
            strSQL.append("       sum(nvl(dr.gm, 0)) drdh,\n" +
                    "       sum(nvl(lj.gm, 0)) ljdh,\n" +
                    "       sum(nvl(dr.hj, 0)) drhy,\n" +
                    "       sum(nvl(lj.hj, 0)) ljhy,\n" +
                    "       sum(nvl(dr.kuc, 0)) drkc,\n" +
                    "       sum(nvl(dr.kedkc, 0)) drkdkc,\n" +
                    "       sum(nvl(dr.changwml, 0)) drchangwml,\n" +
                    "       sum(nvl(lj.changwml, 0)) ljchangwml,sum(nvl(dr.fadl, 0)) fadl,sum(nvl(dr.gongrl, 0)) gongrl\n" );
//					"		SUM(nvl(dr.jingjcml,0)) jingjcml \n"
        }else{
            strSQL.append("       round_new(sum(nvl(dr.gm, 0)), 0) drdh,\n" +
                    "       round_new(sum(nvl(lj.gm, 0)), 0) ljdh,\n" +
                    "       round_new(sum(nvl(dr.hj, 0)), 0) drhy,\n" +
                    "       round_new(sum(nvl(lj.hj, 0)), 0) ljhy,\n" +
                    "       round_new(sum(nvl(dr.kuc, 0)), 0) drkc,\n" +
                    "       round_new(sum(nvl(dr.kedkc, 0)), 0) drkdkc,\n" +
                    "       round_new(sum(nvl(dr.changwml, 0)), 0) drchangwml,\n" +
                    "       round_new(sum(nvl(lj.changwml, 0)), 0) ljchangwml,\n" +
                    "       round_new(SUM(nvl(dr.fadl,0)), 0) fadl, \n" +
                    "       round_new(SUM(nvl(dr.gongrl,0)), 0) gongrl \n");
            //	"		round_new(SUM(nvl(dr.jingjcml,0)), 0) jingjcml \n"
        }
        strSQL.append("  from (select distinct dc.id, dc.xuh, dc.mingc\n" +
                "          from vwdianc dc\n" +
                "         where " +
                //"(dc.id="+this.getTreeid()+" or dc.fuid = "+this.getTreeid()+" or dc.fuid ="+fid+" ) and dc.id <> 300" +
                " dc.id in ("+getTreeid()+") and dc.id not in ( "+this.getZhi()+
                "  	)) dc,\n" +
                "       (select dc.id as id,\n" +
                "               sum(dangrgm) gm,\n" +
                "				SUM(dc.jingjcml) AS jingjcml,"+
                "               sum(haoyqkdr) hy,\n" +
                "               sum(fady) as fady,\n" +
                "               sum(gongry) as gongry,\n" +
                "               sum(kuc) as kuc,\n" +
                "               sum(kedkc) as kedkc,\n" +
                "               sum(changwml) as changwml,\n" +
                "               sum(fady + gongry + qity + feiscy) as hj,SUM(gongrl) AS gongrl,SUM(fadl) AS fadl\n" +
                "          from shouhcrbb hc, vwdianc dc\n" +
                "         where hc.diancxxb_id = dc.id\n" +
                "           and riq = to_date('"+endRiq+"', 'yyyy-mm-dd')\n" +
                "         group by dc.id) dr,\n" +
                "       (select dc.id as id,\n" +
                "               sum(dangrgm) gm,\n" +
                "               sum(haoyqkdr) hy,\n" +
                "               sum(changwml) as changwml,\n" +
                "               sum(fady) as fady,\n" +
                "               sum(gongry) as gongry,\n" +
                "               sum(fady + gongry + qity + feiscy) as hj\n" +
                "          from shouhcrbb hc, vwdianc dc\n" +
                "         where hc.diancxxb_id = dc.id\n" +
                "           and riq >= to_date('"+beginRiq+"', 'yyyy-mm-dd')\n" +
                "           and riq <= to_date('"+endRiq+"', 'yyyy-mm-dd')\n" +
                "         group by dc.id) lj\n" +
                " where dc.id = dr.id(+)\n" +
                "   and dc.id = lj.id(+)\n" +
                " group by rollup(dc.mingc)\n" +
                " order by grouping(dc.mingc), max(dc.xuh)");


        //System.out.println(sbsql);
        ResultSetList rs = con.getResultSetList(strSQL.toString());
        Report rt = new Report();

        String ArrHeader[][]=new String[2][10];
        ArrHeader[0]=new String[] {"��λ","ʵ�ʵ���","ʵ�ʵ���","�������","�������","���տ��","���տ��","���У�����ú��","���У�����ú��","������","������"};
        ArrHeader[1]=new String[] {"��λ","����","�ۼ�","����","�ۼ�","������","���ÿ��","���ս�ú","�ۼƽ�ú","������","������"};
        int ArrWidth[]=new int[] {100,90,90,90,90,90,90,90,90,80,80};
        //rs.beforefirst();
        rt.setBody(new Table(rs, 2, 0, 0));
        rt.body.setHeaderData(ArrHeader);
        rt.body.setWidth(ArrWidth);
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.setTitle(getDiancmc(String.valueOf(visit.getDiancxxb_id()))+"ȼ�����Ĵ�ͳ���ձ�",ArrWidth);
        rt.setDefaultTitle(1, 3, "���λ:"+getDiancmc(), Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 4, riq+"��"+riq1, Table.ALIGN_CENTER);
        rt.setDefaultTitle(8, 4, "��λ:�֡�Ԫ/�֡�MJ/Kg����ǧ��ʱ������", Table.ALIGN_RIGHT);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCols();

        //rt.body.setPageRows(21);
        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        return rt.getAllPagesHtml();

    }

    private String getShouhcDetail(){
        JDBCcon con = new JDBCcon();
        StringBuffer SQL = new StringBuffer();
        String beginRiq=this.getBeginriqDate();
        String endRiq=this.getEndriqDate();
        String sr="LJ";
//		�������Ϊ���գ�����ʾ���յ�ú�۵���Ϣ��������ʾ�ۼ���Ϣ
        if(beginRiq.equals(endRiq)){
            beginRiq=DateUtil.Formatdate("yyyy-MM-dd", DateUtil.getFirstDayOfMonth(DateUtil.getDate(endRiq)));
            sr="DR";
        }
        SQL.append("SELECT DANW,DQ,GONGYS,MEIK,JIHKJ,PINZ,YUNSMC,\n");
        SQL.append("MEIJ,YUNJ,REZ,\n");
        SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ)/REZ*29.271,2))HSBMDJ,\n");
        SQL.append("DECODE(REZ,0,0,ROUND((MEIJ+YUNJ-MEIJS-YUNJS)/REZ*29.271,2))BHSBMDJ,DR,LJ\n");
        SQL.append("FROM(SELECT \n");
        if(getBBLXValue().getId()==1){
            SQL.append("	   DECODE(GROUPING(dc.mingc), 1, '�ܼ�', dc.mingc) danw,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '��λС��', SR.DQ) DQ,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(SR.GONGYS), 1, 'С��',SR.GONGYS) GONGYS,\n");
        }else{
            SQL.append("	   DECODE(GROUPING(SR.DQ), 1, '�ܼ�', SR.DQ) dq,\n");
            SQL.append("	   DECODE(GROUPING(SR.DQ) + GROUPING(dc.mingc), 1, '����С��', dc.mingc) danw,\n");
            SQL.append("	   DECODE(GROUPING(dc.mingc) + GROUPING(SR.GONGYS), 1, 'С��',SR.GONGYS) GONGYS,\n");
        }

        SQL.append("       SR.MEIK,\n");
        SQL.append("       SR.JIHKJ,\n");
        SQL.append("       SR.PINZ,\n");
        SQL.append("       SR.YUNSMC YUNSMC,\n");
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ)),0,0,SUM(SR.MEIJ * LAIMSLMJ) /SUM(DECODE(SR.MEIJ,0,0,LAIMSLMJ))),2) MEIJ,\n");
//		SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, 0, SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ, 0, 0, LAIMSLMJ))),2) YUNJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJ) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJ,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)* SR.MEIJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) MEIJS,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ)),0,0,SUM(DECODE(SR.MEIJ, 0, DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ), SR.LAIMSLMJ) * SR.YUNJS) /SUM(DECODE(SR.MEIJ,0,DECODE(SR.YUNJ,0,0,SR.LAIMSLMJ),LAIMSLMJ))),2) YUNJS,\n");
        SQL.append("       ROUND_NEW(DECODE(SUM(DECODE(SR.REZ,0,0,LAIMSLREZ)),0,0,SUM(SR.REZ * SR.LAIMSLREZ) /SUM(DECODE(SR.REZ,0,0,LAIMSLREZ))),2) REZ,\n");
        if(MainGlobal.getXitxx_item("�պĴ��ձ�", "�Ƿ���ձ����������Ϣȡ��", "0", "��").equals("��")){
            SQL.append("       SUM(SR.DR) DR,\n");
            SQL.append("       SUM(SR.LJ) LJ\n");
        }else{
            SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.DR),0),SUM(SR.DR)) DR,\n");
            SQL.append("       DECODE(GROUPING(SR.DQ) + GROUPING(SR.MEIK), 2,round_new(SUM(SR.LJ),0),SUM(SR.LJ)) lj\n");
        }
        SQL.append("  FROM (SELECT LJ.DIANCID,LJ.DQ,\n");
        SQL.append("               LJ.GONGYS,\n");
        SQL.append("               LJ.MEIK,\n");
        SQL.append("               LJ.JIHKJ,\n");
        SQL.append("               LJ.PINZ,\n");
        SQL.append("               DECODE(LJ.DIANCID,202,'����',DECODE(LJ.YUNSMC,'', '����', LJ.YUNSMC)) YUNSMC,\n");
        SQL.append("               "+sr+".MEIJ MEIJ,\n");
        SQL.append("               "+sr+".YUNJ YUNJ,\n");
        SQL.append("               "+sr+".MEIJS MEIJS,\n");
        SQL.append("               "+sr+".YUNJS YUNJS,\n");
        SQL.append("               "+sr+".REZ REZ,\n");
        SQL.append("               "+sr+".LAIMSLMJ LAIMSLMJ,\n");
        SQL.append("               "+sr+".LAIMSLREZ LAIMSLREZ,\n");
        SQL.append("               DR.LAIMSL DR,\n");
        SQL.append("               LJ.LAIMSL LJ\n");
        SQL.append("          FROM (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '����', DQ.MINGC)) DQ,\n");
        SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '����', GYS2.MINGC)) GONGYS,\n");
        SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '����', MK.MINGC)) MEIK,\n");
        SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '����', J.MINGC)) JIHKJ,\n");
        SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '����', P.MINGC)) PINZ,\n");
        SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '����', YSFS.MINGC)) YUNSMC,\n");
        SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
        SQL.append("					   ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
        SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
        SQL.append("                  FROM SHOUHCFKB SHC,\n");
        SQL.append("                       MEIKXXB MK,\n");
        SQL.append("                       GONGYSB GYS,\n");
        SQL.append("                       GONGYSB GYS2,\n");
        SQL.append("                       YUNSFSB YSFS,\n");
        SQL.append("                       JIHKJB J,\n");
        SQL.append("                       PINZB P,\n");
        SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n");
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n");
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n");
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n");
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n");
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n");
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n");
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n");
//		SQL.append("                   		   AND S.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n");
//		SQL.append("                         GROUP BY S.ID) FAH,\n");
        SQL.append("                       (SELECT ID\n");
        SQL.append("                          FROM VWDIANC\n");
        SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
        SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n");
        SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
        SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
        SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
        SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
        SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
        SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
        SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
        SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
        SQL.append("                   AND SHC.RIQ = "+DateUtil.FormatOracleDate(endRiq)+"\n");
        SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
        SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) DR,\n");
        SQL.append("               (SELECT DECODE(GROUPING(DQ.MINGC),1,'',DECODE(DQ.MINGC, NULL, '����', DQ.MINGC)) DQ,\n");
        SQL.append("                       DECODE(GROUPING(GYS2.MINGC),1,'',DECODE(GYS2.MINGC, NULL, '����', GYS2.MINGC)) GONGYS,\n");
        SQL.append("                       DECODE(GROUPING(MK.MINGC),1,'',DECODE(MK.MINGC, NULL, '����', MK.MINGC)) MEIK,\n");
        SQL.append("                       DECODE(GROUPING(J.MINGC),1,'',DECODE(J.MINGC, NULL, '����', J.MINGC)) JIHKJ,\n");
        SQL.append("                       DECODE(GROUPING(P.MINGC),1,'',DECODE(P.MINGC, NULL, '����', P.MINGC)) PINZ,\n");
        SQL.append("                       DECODE(GROUPING(YSFS.MINGC),1,'',DECODE(YSFS.MINGC, NULL, '����', YSFS.MINGC)) YUNSMC,\n");
        SQL.append("                       DIANC.ID DIANCID,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.MEIJ * SHC.LAIMSL) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) MEIJ,\n");
//		SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ, 0, 0,SHC.YUNJ ) *SHC.LAIMSL ) /SUM(DECODE(SHC.MEIJ,0,0,SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJ) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJ,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)*SHC.MEIJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) MEIJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)),0,0,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL) * SHC.YUNJS) /SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL))),2) YUNJS,\n");
        SQL.append("                       ROUND_NEW(DECODE(SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL)),0,0,SUM(SHC.REZ * SHC.LAIMSL) /SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))),2) REZ,\n");
        SQL.append("                       SUM(SHC.LAIMSL) LAIMSL,SUM(DECODE(SHC.MEIJ,0,DECODE(SHC.YUNJ,0,0,SHC.LAIMSL),SHC.LAIMSL)) LAIMSLMJ,SUM(DECODE(SHC.REZ,0,0,SHC.LAIMSL))  LAIMSLREZ\n");
        SQL.append("                  FROM SHOUHCFKB SHC,\n");
        SQL.append("                       MEIKXXB MK,\n");
        SQL.append("                       GONGYSB GYS,\n");
        SQL.append("                       GONGYSB GYS2,\n");
        SQL.append("                       YUNSFSB YSFS,\n");
        SQL.append("                       JIHKJB J,\n");
        SQL.append("                       PINZB P,\n");
        SQL.append("                       MEIKDQB DQ,\n");
//		SQL.append("                       (SELECT S.ID, MAX(F.YUNSFSB_ID) YUNSFSB_ID\n");
//		SQL.append("                          FROM FAHB F, SHOUHCFKB S\n");
//		SQL.append("                         WHERE S.DIANCXXB_ID = F.DIANCXXB_ID\n");
//		SQL.append("                           AND S.RIQ = F.DAOHRQ\n");
//		SQL.append("                           AND S.MEIKXXB_ID = F.MEIKXXB_ID\n");
//		SQL.append("                           AND S.GONGYSB_ID = F.GONGYSB_ID\n");
//		SQL.append("                           AND S.JIHKJB_ID = F.JIHKJB_ID\n");
//		SQL.append("                           AND S.PINZB_ID = F.PINZB_ID\n");
//		SQL.append("    			  		   AND S.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
//		SQL.append("    			   		   AND S.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
//		SQL.append("                         GROUP BY S.ID) FAH,\n");
        SQL.append("                       (SELECT ID\n");
        SQL.append("                          FROM VWDIANC\n");
        SQL.append("                         WHERE ID IN ("+getTreeid()+")\n");
        SQL.append("                           AND VWDIANC.ID NOT IN ("+this.getZhi()+")) DIANC\n");
//		SQL.append("                 WHERE SHC.ID = FAH.ID(+)\n");
        SQL.append("                   WHERE SHC.YUNSFSB_ID = YSFS.ID(+)\n");
        SQL.append("                   AND SHC.PINZB_ID = P.ID(+)\n");
        SQL.append("                   AND GYS2.ID = SHC.GONGYSB_ID\n");
        SQL.append("                   AND GYS.FUID = DQ.ID(+)\n");
        SQL.append("                   AND SHC.MEIKXXB_ID = MK.ID(+)\n");
        SQL.append("                   AND SHC.JIHKJB_ID = J.ID(+)\n");
        SQL.append("                   AND MK.MEIKDQ_ID = GYS.ID\n");
        SQL.append("                   AND SHC.DIANCXXB_ID = DIANC.ID\n");
        SQL.append("    			   AND SHC.RIQ <= "+DateUtil.FormatOracleDate(endRiq)+"\n");
        SQL.append("    			   AND SHC.RIQ >= "+DateUtil.FormatOracleDate(beginRiq)+"\n");
        SQL.append("                 GROUP BY ROLLUP((DQ.MINGC, GYS2.MINGC, MK.MINGC, J.MINGC,P.MINGC, YSFS.MINGC, DIANC.ID))\n");
        SQL.append("                HAVING NOT GROUPING(DQ.MINGC) = 1) LJ\n");
        SQL.append("         WHERE DR.MEIK(+) = LJ.MEIK\n");
        SQL.append("           AND DR.GONGYS(+) = LJ.GONGYS\n");
        SQL.append("           AND DR.YUNSMC(+) = LJ.YUNSMC\n");
        SQL.append("           AND DR.JIHKJ(+) = LJ.JIHKJ\n");
        SQL.append("           AND DR.DQ(+) = LJ.DQ\n");
        SQL.append("           AND DR.PINZ(+) = LJ.PINZ\n");
        SQL.append("           AND DR.DIANCID(+) = LJ.DIANCID) SR, diancxxb dc WHERE sr.diancid=dc.id\n");
        if(getBBLXValue().getId()==1){
            SQL.append(" GROUP BY ROLLUP((dc.mingc,dc.xuh),SR.DQ, (SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
            SQL.append(" ORDER BY GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(SR.GONGYS) DESC, SR.GONGYS");
        }else{
            SQL.append(" GROUP BY ROLLUP(SR.DQ,(dc.mingc,dc.xuh),(SR.GONGYS, SR.MEIK, SR.JIHKJ, SR.PINZ, SR.YUNSMC))\n");
            SQL.append("ORDER BY GROUPING(SR.DQ) DESC, SR.DQ, GROUPING(dc.xuh) DESC,dc.xuh,GROUPING(SR.GONGYS) DESC, SR.GONGYS\n");
        }
        SQL.append(")SR");

        ResultSetList rs = con.getResultSetList(SQL.toString());
        Report rt = new Report();

        String ArrHeader[][]=new String[3][14];
        if(getBBLXValue().getId()==1){
            ArrHeader[0]=new String[] {"�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���"};
            ArrHeader[1]=new String[] {"��λ","����","��Ӧ��","���","�ƻ��ھ�","Ʒ��","����<br>��ʽ","ú��<br>(��˰)","�˼�<br>(��˰)","������ֵ","��˰<br>��ú����","����˰<br>��ú����","��ú��","��ú��"};
            ArrHeader[2]=new String[] {"��λ","����","��Ӧ��","���","�ƻ��ھ�","Ʒ��","����<br>��ʽ","ú��<br>(��˰)","�˼�<br>(��˰)","������ֵ","��˰<br>��ú����","����˰<br>��ú����","����","�ۼ�"};
        }else{
            ArrHeader[0]=new String[] {"�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���","�ֿ���ú���"};
            ArrHeader[1]=new String[] {"����","��λ","��Ӧ��","���","�ƻ��ھ�","Ʒ��","����<br>��ʽ","ú��<br>(��˰)","�˼�<br>(��˰)","������ֵ","��˰<br>��ú����","����˰<br>��ú����","��ú��","��ú��"};
            ArrHeader[2]=new String[] {"����","��λ","��Ӧ��","���","�ƻ��ھ�","Ʒ��","����<br>��ʽ","ú��<br>(��˰)","�˼�<br>(��˰)","������ֵ","��˰<br>��ú����","����˰<br>��ú����","����","�ۼ�"};
        }

        int ArrWidth[]=new int[] {80,80,120,110,60,50,60,60,60,60,60,60,60,60};
        //rs.beforefirst();
        rt.setBody(new Table(rs, 3, 0, 0));
        rt.body.setHeaderData(ArrHeader);
        rt.body.setWidth(ArrWidth);
        rt.body.mergeFixedRow();
        rt.body.mergeFixedCol(1);
        rt.body.mergeFixedCol(2);
        rt.body.ShowZero=true;
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_LEFT);
        rt.body.setColAlign(4, Table.ALIGN_LEFT);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);


        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 2, "���ܣ�", Table.ALIGN_CENTER);
        rt.setDefautlFooter(3, 3, "��ˣ�", Table.ALIGN_CENTER);
        rt.setDefautlFooter(6, 2, "�Ʊ�", Table.ALIGN_CENTER);

        _CurrentPage = 1;
        _AllPages =rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        return rt.getAllPagesHtml();
    }


    private boolean _QueryClick = false;

    public void QueryButton(IRequestCycle cycle) {
        _QueryClick = true;
    }

    public void submit(IRequestCycle cycle) {
        if (_QueryClick) {
            _QueryClick = false;
        }
    }

    public String getPageHome() {
        if (((Visit) getPage().getVisit()).getboolean1()) {
            return "window.location = '" + MainGlobal.getHomeContext(this)
                    + "';";
        } else {
            return "";
        }
    }
    private String FormatDate(Date _date) {
        if (_date == null) {
            return "";
        }
        return DateUtil.Formatdate("yyyy��MM��dd��", _date);
    }
    //	�������ÿ�ʼ
    public String getBeginriqDate(){
        if(((Visit) getPage().getVisit()).getString4()==null||((Visit) getPage().getVisit()).getString4()==""){
            Calendar stra=Calendar.getInstance();
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
            stra.add(Calendar.MONTH,-1);
            ((Visit) getPage().getVisit()).setString4(DateUtil.FormatDate(stra.getTime()));
        }
        return ((Visit) getPage().getVisit()).getString4();
    }
    public void setBeginriqDate(String value){
        ((Visit) getPage().getVisit()).setString4(value);
    }

    public String getEndriqDate(){
        if(((Visit) getPage().getVisit()).getString5()==null||((Visit) getPage().getVisit()).getString5()==""){
            Calendar stra=Calendar.getInstance();
            int T_Date=DateUtil.getDay(new Date());
            if(T_Date<4){
                T_Date=1;
            }else{
                T_Date=T_Date-2;
            }
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), T_Date);
            stra.add(Calendar.MONTH,-1);
            ((Visit) getPage().getVisit()).setString5(DateUtil.FormatDate(stra.getTime()));
        }
        return ((Visit) getPage().getVisit()).getString5();
    }

    public void setEndriqDate(String value){
        ((Visit) getPage().getVisit()).setString5(value);
    }
    //	�������ý���
    private void getToolbars(){
        Toolbar tb1 = new Toolbar("tbdiv");
        tb1.addText(new ToolbarText("��¯����:"));
        DateField df = new DateField();

        df.setValue(this.getBeginriqDate());
        df.Binding("qiandrq1","");// ��htmlҳ�е�id��,���Զ�ˢ��
        df.setWidth(80);
        tb1.addField(df);
        tb1.addText(new ToolbarText("��"));
        DateField df1 = new DateField();
        df1.setValue(this.getEndriqDate());
        df1.Binding("qiandrq2","");// ��htmlҳ�е�id��,���Զ�ˢ��
        df1.setWidth(80);
        tb1.addField(df1);
        tb1.addText(new ToolbarText("-"));

        ExtTreeUtil etu = new ExtTreeUtil("diancTree",
                ExtTreeUtil.treeWindowCheck_Dianc, ((Visit) this.getPage()
                .getVisit()).getDiancxxb_id(), getTreeid(),null,true);

        setTree(etu);
        TextField tf = new TextField();
        tf.setId("diancTree_text");
        tf.setWidth(100);
        String[] str=getTreeid().split(",");
        if(str.length>1){
            tf.setValue("��ϵ糧");
        }else{
            tf.setValue(((IDropDownModel) getDiancmcModel()).getBeanValue(Long.parseLong(str[0])));
        }

        ToolbarButton tb2 = new ToolbarButton(null,null,"function(){diancTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);

        tb1.addText(new ToolbarText("��λ:"));
        tb1.addField(tf);
        tb1.addItem(tb2);

        tb1.addText(new ToolbarText("-"));

//		tb1.addText(new ToolbarText("��������:"));
//		ComboBox cbo_bblx = new ComboBox();
//		cbo_bblx.setTransform("BBLXDropDown");
//		cbo_bblx.setWidth(120);
//		tb1.addField(cbo_bblx);
//		tb1.addText(new ToolbarText("-"));
        ToolbarButton tb = new ToolbarButton(null,"ˢ��","function(){document.Form0.submit();}");
        tb.setIcon(SysConstant.Btn_Icon_Refurbish);
        tb1.addItem(tb);

//		��ˢ��ʱ���û���ʾ�Ƿ���ڲ���������
//		setMsg(null);
        Date beginRiq=DateUtil.getDate(this.getBeginriqDate());
        Date endRiq=DateUtil.getDate(this.getEndriqDate());
//		�������Ϊ���գ�����ʾ���յ�ú�۵���Ϣ��������ʾ�ۼ���Ϣ
        AutoCreateDaily_Report_gd DR=new AutoCreateDaily_Report_gd();
        JDBCcon con = new JDBCcon();
        String errmsg=DR.RPChk(con, getTreeid(),beginRiq,endRiq );
//		if(errmsg.length()>0){
//			setMsg(errmsg+"��Ϣ����ȫ����ע��");
//		}
        con.Close();
        setToolbar(tb1);
    }
    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (visit.getRenyID() == -1) {
            visit.setboolean1(true);
            setZhi();
            return;
        }
        if (!visit.getActivePageName().toString().equals(this.getPageName().toString())) {
            visit.setActivePageName(getPageName().toString());
            visit.setList1(null);
            visit.setDropDownBean1(null);
            visit.setProSelectionModel1(null);
            visit.setDropDownBean2(null);
            visit.setProSelectionModel2(null);
            visit.setDropDownBean4(null);
            visit.setProSelectionModel4(null);
            visit.setExtTree1(null);
            visit.setString4(null);
            visit.setString5(null);
            setTreeid(null);
            setBBLXModel(null);
            setBBLXValue(null);
            setZhi();
//			�����¼�û�Ϊ�����������ô��λ������Ĭ��Ϊȫ����λ��
            if(visit.getDiancxxb_id()==112){
                initDiancTree();
            }
        }
        getToolbars();
        blnIsBegin = true;

    }

    //	������������������
    // �̵���������
    public IDropDownBean getBBLXValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean2() == null) {
            ((Visit) getPage().getVisit()).setDropDownBean2((IDropDownBean) getBBLXModel().getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean2();
    }

    public void setBBLXValue(IDropDownBean Value) {
        ((Visit) getPage().getVisit()).setDropDownBean2(Value);
    }

    public void setBBLXModel(IPropertySelectionModel value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(value);
    }

    public IPropertySelectionModel getBBLXModel() {
        getIBBLXModels();
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void getIBBLXModels() {
        List list= new ArrayList();
        list.add(new IDropDownBean(1, "����λ����"));
        list.add(new IDropDownBean(2, "��úԴ��������"));
        ((Visit) getPage().getVisit()).setProSelectionModel2(new IDropDownModel(list));
    }

    private String treeid;
    public String getTreeid() {
        if(treeid==null||treeid.equals("")){
            treeid=String.valueOf(((Visit) this.getPage().getVisit()).getDiancxxb_id());
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

    public String getTreeScript() {
        return getTree().getWindowTreeScript();
    }
    public String getTreeHtml() {
        return getTree().getWindowTreeHtml(this);
    }
    //	�糧����
//	private IPropertySelectionModel _IDiancModel;
    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit)getPage().getVisit()).getProSelectionModel1() == null) {
            getDiancmcModels();
        }
        return ((Visit)getPage().getVisit()).getProSelectionModel1();
    }

    //	private boolean _DiancmcChange=false;
    public IDropDownBean getDiancmcValue() {
        if(((Visit)getPage().getVisit()).getDropDownBean1()==null){
            ((Visit)getPage().getVisit()).setDropDownBean1((IDropDownBean)getDiancmcModel().getOption(0));
        }
        return ((Visit)getPage().getVisit()).getDropDownBean1();
    }

    public void setDiancmcValue(IDropDownBean Value) {
        ((Visit)getPage().getVisit()).setDropDownBean1(Value);
    }

    public IPropertySelectionModel getDiancmcModels() {
        String sql = "select id,mingc from diancxxb";
        setDiancmcModel(new IDropDownModel(sql)) ;
        return ((Visit) getPage().getVisit()).getProSelectionModel1();
    }

    public void setDiancmcModel(IPropertySelectionModel _value) {
        ((Visit)getPage().getVisit()).setProSelectionModel1(_value);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            cn.Close();
        }
        return IDropDownDiancmc;
    }
//	�ֹ�˾������
//	private boolean _fengschange = false;

    public IDropDownBean getFengsValue() {
        if (((Visit) getPage().getVisit()).getDropDownBean4() == null) {
            ((Visit) getPage().getVisit())
                    .setDropDownBean4((IDropDownBean) getFengsModel()
                            .getOption(0));
        }
        return ((Visit) getPage().getVisit()).getDropDownBean4();
    }

    public void setFengsValue(IDropDownBean Value) {
//		if (getFengsValue().getId() != Value.getId()) {
//			_fengschange = true;
//		}
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
    //	 �õ���½��Ա�����糧��ֹ�˾������
    public String getDiancmc(String diancId) {
        String diancmc = "";
        JDBCcon cn = new JDBCcon();
        //long diancid = ((Visit) getPage().getVisit()).getDiancxxb_id();
        String sql_diancmc = "select d.quanc from diancxxb d where d.id="
                + diancId;
        ResultSet rs = cn.getResultSet(sql_diancmc);
        try {
            while (rs.next()) {
                diancmc = rs.getString("quanc");
            }
        } catch (SQLException e) {
            // TODO �Զ����� catch ��
            e.printStackTrace();
        }
        cn.Close();
        return diancmc;

    }
    public String getDiancmc(){
        String[] str=getTreeid().split(",");
        if(str.length>1){
            return "��ϵ糧";
        }else{
            return getDiancmc(str[0]);
        }
    }
    //
    public Toolbar getToolbar() {
        return ((Visit)this.getPage().getVisit()).getToolbar();
    }
    public void setToolbar(Toolbar tb1) {
        ((Visit)this.getPage().getVisit()).setToolbar(tb1);
    }
    public String getToolbarScript() {
        return getToolbar().getRenderScript()+getOtherScript("diancTree");
    }

    //	��ʼ����ѡ�糧���е�Ĭ��ֵ
    private void initDiancTree(){
        Visit visit = (Visit) getPage().getVisit();
        String sql="SELECT ID\n" +
                "  FROM DIANCXXB\n" +
                " WHERE JIB > 2\n" +
                " START WITH ID = "+visit.getDiancxxb_id()+"\n" +
                "CONNECT BY FUID = PRIOR ID";

        JDBCcon con=new JDBCcon();
        ResultSetList rsl = con.getResultSetList(sql);
        String TreeID="";
        while(rsl.next()){
            TreeID+=rsl.getString("ID")+",";
        }
        if(TreeID.length()>1){
            TreeID=TreeID.substring(0, TreeID.length()-1);
            setTreeid(TreeID);
        }else{
            setTreeid(visit.getDiancxxb_id() + "");
        }
        rsl.close();
        con.Close();
    }

    //	���ӵ糧��ѡ���ļ���
    public String getOtherScript(String treeid){
        String str=" var "+treeid+"_history=\"\";\n"+
                treeid+"_treePanel.on(\"checkchange\",function(node,checked){\n" +
                "    if(checked){\n" +
                "      addNode(node);\n" +
                "    }else{\n" +
                "      subNode(node);\n" +
                "    }\n" +
                "    node.expand();\n" +
                "    node.attributes.checked = checked;\n" +
                "    node.eachChild(function(child) {\n" +
                "      if(child.attributes.checked != checked){\n" +
                "        if(checked){\n" +
                "          addNode(child);\n" +
                "        }else{\n" +
                "          subNode(child);\n" +
                "        }\n" +
                "        child.ui.toggleCheck(checked);\n" +
                "              child.attributes.checked = checked;\n" +
                "              child.fireEvent('checkchange', child, checked);\n" +
                "      }\n" +
                "    });\n" +
                "  },"+treeid+"_treePanel);\n" +
                "  function addNode(node){\n" +
                "    var history = '+,'+node.id+\";\";\n" +
                "    writesrcipt(node,history);\n" +
                "  }\n" +
                "\n" +
                "  function subNode(node){\n" +
                "    var history = '-,'+node.id+\";\";\n" +
                "    writesrcipt(node,history);\n" +
                "  }\n" +
                "function writesrcipt(node,history){\n" +
                "\t\tif("+treeid+"_history==\"\"){\n" +
                "\t\t\t"+treeid+"_history = history;\n" +
                //" 	   document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n"	+
                "\t\t}else{\n" +
                "\t\t\tvar his = "+treeid+"_history.split(\";\");\n" +
                "\t\t\tvar reset = false;\n" +
                "\t\t\tfor(i=0;i<his.length;i++){\n" +
                "\t\t\t\tif(his[i].split(\",\")[1] == node.id){\n" +
                "\t\t\t\t\this[i] = \"\";\n" +
                "\t\t\t\t\treset = true;\n" +
                "\t\t\t\t\tbreak;\n" +
                "\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\tif(reset){\n" +
                "\t\t\t  "+treeid+"_history = his.join(\";\");\n" +
                //"      	 document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                "    }else{\n" +
                "      	 "+treeid+"_history += history;\n" +
                //"        document.getElementById('GONGYSMLTTREE_ID').value=gongysTree_history;\n" +
                "    }\n" +
                "  }\n" +
                "\n" +
                "}";
        return str;
    }
}