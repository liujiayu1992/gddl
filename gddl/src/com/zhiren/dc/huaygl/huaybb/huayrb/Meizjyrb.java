package com.zhiren.dc.huaygl.huaybb.huayrb;

/*
 * ���ߣ�����
 * ʱ�䣺2010-03-29
 * �������޸�SQL��ѯ������������
 */

/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * ���������Ӻ������鵥�ж���ҳ������˵ȵ�ϵͳ����  (�����ö��)
 * mingc = '�����ձ���β'
 * zhi = '��ˣ�XXX'
 * danw = 1  ��ҳ�ŵĶ��뷽ʽ  ������ֵ��Table��
 * zhuangt = 1
 * beiz = '1,3'  1Ϊ��ҳβ������ʼ�� 3Ϊһ��ռ�õ�����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-08-30
 * ����������������ʱ���ѯ���ֳ����ܼ��еı��� getMeizjyrb_ls_hz_cb()
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-07-31
 * �������޸Ļ����ձ��Ĺ�������ȷ��ɲ�ѯ������ʾ�ظ������� getMeizjyrb()
 */
/*
 * ����:tzf
 * ʱ��:2009-4-16
 * �޸�����:�������Ϊ ���νṹ  ����ѯ����� ���������ý��и��ģ����ܳ���ѯ ��������
 */
/*
 * 2009-03-18
 * ��ɭ��
 * �޸Ļ����ձ������ظ����ֵ�bug
 *
 *2009-04-20
 * ����
 * �������ڸ�Ϊ��������
 */
/*
 * 2009-06-18
 * ��ɭ��
 * ��ӱ����������ʾ�еĿ���
 * ֻ�޸���getMeizjyrb()����
 *
 */
/*
 * ���ߣ�ww
 * ʱ�䣺2009-12-26
 * ���������ѡ�񱨱�ͳ��ʱ���ǰ��������ڻ��ǰ��������ڣ�Ĭ��Ϊ��������,ֻ��getMeizjyrb_zhilb() ���������
 * 	   ���ò�����
 * 		insert into xitxxb values(
 getnewid(257),
 1,
 257,                 --diancxxbID
 '��������ͳ������',
 'fahrq',             --fahrq,daohrq
 '',
 '����',
 1,
 'ʹ��'
 )
 * 		
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-28 
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */


/*
 * ���ߣ����
 * ʱ�䣺2010-11-8 
 * ������������һ�ű��ʽ�������������䵥λ���������ڡ��������ڡ�������롢ú����ȫˮ��Mt���ոɻ�ˮ��Mad���յ����ҷ�Aar���ոɻ��ӷ���Vad
 * �յ���ȫ��St,ar����Ͳ������Qb,ad����λ������ Qnet,ar(MJ/kg)����λ������(KCal/Kg)�������޻һ�Vdaf�������������λ������Qgr,ad���������λ������Qgr,d
 */
/*
 * ���ߣ���ʤ��
 * ʱ�䣺2013-01-18
 * ��������¼������Ա���Ϊ¼����Ա.
 *              if(MainGlobal.getXitxx_item("�����ձ�", "�Ƿ���ʾ¼����Ա", "0","��").equals("��")){
					condition="  z.lury,\n";
 * 		 
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-06-01
 * ���������һ����ʽ�ı�����ṹ��getMeizjyrb_zhilb()������ͬ��ֻ�ǲ���ʾ����Ա��¼��Ա
 * 		 
 */

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

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
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

public class Meizjyrb extends BasePage {

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

    // ������
    private String riq;

    public String getRiq() {
        return riq;
    }

    public void setRiq(String riq) {
        this.riq = riq;
    }

    // ҳ��仯��¼
    private String Change;

    public String getChange() {
        return Change;
    }

    public void setChange(String change) {
        Change = change;
    }

    public boolean getRaw() {
        return true;
    }

    private String getTongjRq(JDBCcon con) {
        Visit visit = (Visit) getPage().getVisit();
        if (con == null) {
            con = new JDBCcon();
        }
        String tongjrq = " select * from xitxxb where mingc='��������ͳ������'  and zhuangt=1 and leib='����' and diancxxb_id="
                + visit.getDiancxxb_id();
        ResultSetList rsl = con.getResultSetList(tongjrq);

        String strTongjrq = "daohrq";

        if (rsl.next()) {
            strTongjrq = rsl.getString("zhi");
        }

        rsl.close();
        con.Close();
        return strTongjrq;
    }

    // �����Ʊ���Ĭ�ϵ�ǰ�û�
    private String getZhibr() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhibr = "";
        String zhi = "��";
        String sql = "select zhi from xitxxb where mingc='�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' and diancxxb_id="
                + visit.getDiancxxb_id();
        ResultSet rs = con.getResultSet(sql);
        try {
            while (rs.next()) {
                zhi = rs.getString("zhi");
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        if (zhi.equals("��")) {
            zhibr = visit.getRenymc();
        }
        return zhibr;
    }

    // ����������
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
            sb.append("select id,mingc from diancxxb where fuid="
                    + visit.getDiancxxb_id());
        } else {
            sb.append("select id,mingc from diancxxb where id="
                    + visit.getDiancxxb_id());
        }
        setChangbModel(new IDropDownModel(sb.toString()));
    }

    private String REPORT_NAME_MEIZJYRB = "Meizjyrb";// zhillsb�е�����

    private String REPORT_NAME_MEIZJYRB_ZHILB = "Meizjyrb_zhilb";// zhilb�е�����

    private String REPORT_NAME_MEIZJYRB_ZHILB_1 = "Meizjyrb_zhilb_1";

    private String REPORT_NAME_MEIZJYRB_ZHILB_2 = "Meizjyrb_zhilb_2";

    private String REPORT_NAME_MEIZJYRB_ZHILB_hd = "Meizjyrb_zhilb_hd";

    private String REPORT_NAME_MEIZJYRB_ZHILB_fr = "Meizjyrb_zhilb_fr";
    private String  REPORT_NAME_MEIZJYRB_ZHILB_bm ="Meizjyrb_zhilb_bm";//�б�������䵥λ

    private String REPORT_NAME_MEIZJYRB_ZHILB_zr = "Meizjyrb_zhilb_zr";

    private static final String RPT_type_rbls_hz_cb = "Meizjyrb_ls_hz_cb";

    /**
     * 2008-10-18 huochaoyuan ����һ������ú���ɶ��������ŵ����������ԭ�ȱ�����ʾ�����⣬�������һ����ʽ�ı���
     * ������getMeizjyrb_zhilb_1()
     */
    private String mstrReportName = "";

    private boolean blnIsBegin = false;

    // private String leix = "";

    public String getPrintTable() {
        if (!blnIsBegin) {
            return "";
        }
        blnIsBegin = false;
        if (mstrReportName.equals(REPORT_NAME_MEIZJYRB)) {
            return getMeizjyrb();
        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB)) {
            return getMeizjyrb_zhilb();
        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_1)) {
            return getMeizjyrb_zhilb_1();
        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_2)) {
            return getMeizjyrb_zhilb_2();
        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_hd)) {
            return getMeizjyrb_zhilb_hd();
        } else if (mstrReportName.equals(RPT_type_rbls_hz_cb)) {
            return getMeizjyrb_ls_hz_cb();

        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_fr)) {
            return getMeizjyrb_zhilb_fr();
        }else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_bm)) {
            return getMeizjyrb_zhilb_bm();
        } else if (mstrReportName.equals(REPORT_NAME_MEIZJYRB_ZHILB_zr)) {
            return getMeizjyrb_zhilb_zr();
        } else {
            return "�޴˱���";
        }
    }

    // �����ձ�(�볧)zhillsb�е�����
	/*
	 * ���������� maoz-piz�޸�Ϊ laimsl ��������Լ�趨������Լ����ȫˮ����������Լ��Ϊ���� �޸ķ�Χ
	 * getMeizjyrb(),getMeizjyrb_zhilb(),String getMeizjyrb_zhilb_1()
	 * �޸����ڣ�2008-12-04 �޸��ˣ�����
	 */

    // �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧
    private boolean hasDianc(String id) {
        JDBCcon con = new JDBCcon();
        boolean mingc = false;
        String sql = "select mingc from diancxxb where fuid = " + id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            mingc = true;
        }
        rsl.close();
        return mingc;
    }

    private String getMeizjyrb_ls_hz_cb() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();
        String sqldc = "";
		/* �ж��Ƿ���Ҷ�ӽڵ� Ҳ����˵�ǲ������յ糧id */
        if (getTree_dc().getTree().getTreeRootNode()
                .getNodeById(getTreeid_dc()).isLeaf()) {
            sqldc = " and d.id = " + getTreeid_dc();
        } else {
            sqldc = " and d.fuid = " + getTreeid_dc();
        }
        // DateUtil custom = new DateUtil();getRiq()
        String sql = "select d.mingc dc,g.mingc gys, m.mingc mk, p.mingc pz, c.mingc fz,\n"
                + "f.ches, f.biaoz, z.bianm, z.qnet_ar*1000 qnet_ar, "
                + "round_new(z.qnet_ar/0.0041816,0) qnet_ar_k, z.mt, z.mad, z.aad, z.ad,\n"
                + "z.vdaf, z.vad, z.qbad, z.stad, z.std, z.had, z.hdaf\n"
                + "from (select z.zhilb_id,b.bianm,\n"
                + "z.qnet_ar qnet_ar, z.mt mt, z.mad mad, z.aad aad,\n"
                + "z.ad ad, z.vdaf vdaf, z.vad vad, z.qbad qbad,\n"
                + "z.stad stad, z.std std, z.had had, z.hdaf hdaf\n"
                + "from zhillsb z,zhuanmb b, zhuanmlb l\n"
                + "where b.zhuanmlb_id = l.id and l.jib = 3\n"
                + "and z.id = b.zhillsb_id and qnet_ar !=0\n"
                + "and z.shenhzt !=-1) z,\n"
                + "fahb f, meikxxb m, gongysb g, pinzb p, chezxxb c, diancxxb d\n"
                + "where f.gongysb_id = g.id and f.meikxxb_id = m.id\n"
                + "and f.pinzb_id = p.id and f.faz_id = c.id and f.diancxxb_id = d.id\n"
                + "and f.ches != 0 and f.daohrq = "
                + DateUtil.FormatOracleDate(getRiq())
                + sqldc
                + "\n"
                + "and f.zhilb_id = z.zhilb_id\n"
                + "union\n"
                + "select decode(d.mingc,null,'�ܼ�',d.mingc) dc, decode(d.mingc,null,'','С��') gys,'' mk,'' pz,'' fz,\n"
                + "sum(f.ches) cs, sum(f.biaoz) sl, '' bm,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*qnet_ar*1000)/sum(f.biaoz)),0) qnet_ar,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*qnet_ar/0.0041816)/sum(f.biaoz)),0) qnet_ar_k,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*mt)/sum(f.biaoz)),1) mt,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*mad)/sum(f.biaoz)),1) mad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*aad)/sum(f.biaoz)),2) aad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*ad)/sum(f.biaoz)),2) ad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*vdaf)/sum(f.biaoz)),2) vdaf,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*vad)/sum(f.biaoz)),2) vad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*qbad)/sum(f.biaoz)),2) qbad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*stad)/sum(f.biaoz)),2) stad,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*std)/sum(f.biaoz)),2) std,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*had)/sum(f.biaoz)),2) had,\n"
                + "round_new(decode(sum(f.biaoz),0,0,sum(f.biaoz*hdaf)/sum(f.biaoz)),2) hdaf\n"
                + "from (select z.zhilb_id,\n"
                + "round_new(avg(z.qnet_ar),3) qnet_ar, round_new(avg(z.mt),1) mt,\n"
                + "round_new(avg(z.mad),2) mad, round_new(avg(z.aad),2) aad,\n"
                + "round_new(avg(z.ad),2) ad, round_new(avg(z.vdaf),2) vdaf,\n"
                + "round_new(avg(z.vad),2) vad, round_new(avg(z.qbad),2) qbad,\n"
                + "round_new(avg(z.stad),2) stad, round_new(avg(z.std),2) std,\n"
                + "round_new(avg(z.had),2) had, round_new(avg(z.hdaf),2) hdaf\n"
                + "from zhillsb z,zhuanmb b, zhuanmlb l\n"
                + "where b.zhuanmlb_id = l.id and l.jib = 3\n"
                + "and z.id = b.zhillsb_id and qnet_ar !=0\n"
                + "and z.shenhzt !=-1 group by zhilb_id ) z, fahb f, diancxxb d\n"
                + "where f.ches != 0 and f.daohrq = "
                + DateUtil.FormatOracleDate(getRiq())
                + sqldc
                + "\n"
                + "and f.zhilb_id = z.zhilb_id and f.diancxxb_id = d.id\n"
                + "group by rollup(d.mingc)";
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);

        ResultSetList rstmp = con.getResultSetList(buffer.toString());
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb
                .append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='"
                        + RPT_type_rbls_hz_cb + "' order by xuh");
        ResultSetList rsl = con.getResultSetList(sb.toString());
        if (rsl.getRows() > 0) {
            ArrWidth = new int[rsl.getRows()];
            strFormat = new String[rsl.getRows()];
            String biaot = rsl.getString(0, 1);
            String[] Arrbt = biaot.split("!@");
            ArrHeader = new String[Arrbt.length][rsl.getRows()];
            Zidm = new String[rsl.getRows()];
            rs = new ResultSetList();
            while (rsl.next()) {
                Zidm[rsl.getRow()] = rsl.getString("zidm");
                ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
                        : rsl.getString("format");
                String[] title = rsl.getString("biaot").split("!@");
                for (int i = 0; i < title.length; i++) {
                    ArrHeader[i][rsl.getRow()] = title[i];
                }
            }
            rs.setColumnNames(Zidm);
            while (rstmp.next()) {
                rs.getResultSetlist().add(rstmp.getArrString(Zidm));
            }
            rstmp.close();
            rsl.close();
            rsl = con
                    .getResultSetList("select biaot from baobpzb where guanjz='"
                            + RPT_type_rbls_hz_cb + "'");
            String Htitle = "";
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                    .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                    .getString4());// ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            // ----------------------------- �¼ӵķ���
            // -------------------------------------------------------

            ArrHeader = new String[1][21];

            ArrHeader[0] = new String[] { "����", "������λ", "ú��λ����", "Ʒ��", "��վ",
                    "����", "����<br>����<br>(��)", "�������",
                    "�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
                    "�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)", "ȫˮ<br>��(%)<br>Mt",
                    "����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
                    "����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad",
                    "����<br>���ҷ�<br>(%)<br>Ad",
                    "����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
                    "����<br>����<br>����<br>����<br>(%)<br>Vad",
                    "��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
                    "����<br>�������<br>(%)<br>St,ad", "����<br>����<br>(%)<br>St,d",
                    "����<br>����<br>����<br>(%)<br>Had",
                    "����<br>�޻�<br>����<br>(%)<br>Hdaf" };
            ArrWidth = new int[21];

            ArrWidth = new int[] { 50, 80, 80, 60, 60, 40, 40, 70, 35, 35, 35,
                    35, 35, 35, 35, 35, 35, 35, 35, 35, 35 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                    .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                    .getString4());// ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
            rt
                    .setTitle(
                            "ú  ��  ��  ��  ��  ��"
                                    + ((getChangbValue().getId() > 0 && getChangbModel()
                                    .getOptionCount() > 2) ? "("
                                    + getChangbValue().getValue() + ")"
                                    : ""), ArrWidth);
            rt.title.setRowHeight(2, 45);
            rt.title.setRowCells(2, Table.PER_FONTSIZE, 22);
            rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

            rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
            rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

            strFormat = new String[] { "", "", "", "", "", "0", "", "", "0",
                    "0", "0.0", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                    "0.00", "0.00", "0.00", "0.00" };
        }

        rt.setBody(new Table(rs, 1, 0, 5));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        rt.body.mergeFixedCols();
        // for (int i = 1; i <= 28; i++) {
        // rt.body.setColAlign(i, Table.ALIGN_CENTER);
        // }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(16, 4, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
        RPTInit.getInsertSql(v.getDiancxxb_id(), buffer.toString(), rt,
                "ú  ��  ��  ��  ��  ��", "Meizjyrb");

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();

    }

    private String getMeizjyrb() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }
        // DateUtil custom = new DateUtil();
        String sql = "SELECT distinct a.BIANH,\n"
                + "       a.MEIKDQMC,\n"
                + "       a.MEIKDWMC,\n"
                + "       a.PINZ,\n"
                + "       a.FAZMC,\n"
                + "       a.CHES,\n"
                + "       a.JZSL,\n"
                + "       round_new(z.mt, "
                + v.getMtdec()
                + ") as QUANSF,\n"
                + "       round_new(z.mad, 2) as KONGQGZJSF,\n"
                + "       round_new(z.aad, 2) as KONGQGZJHF,\n"
                + "        round_new(z.ad, 2) as GANZJHF,\n"
                + "       round_new(z.aar, 2) as SHOUDJHF,\n"
                + "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
                + "       round_new(z.vdaf, 2) as HUIFF,\n"
                + "       round_new(z.qbad, "
                + v.getFarldec()
                + ") * 1000 as DANTRL,\n"
                + "       round_new(z.qnet_ar, "
                + v.getFarldec()
                + ") * 1000 as FARL,\n"
                + "       round_new(round_new(z.qnet_ar, "
                + v.getFarldec()
                + ") * 7000 / 29.271, 0) as FARL1,\n"
                + "       ROUND_NEW(z.sdaf,2) as SDAF,\n"
                + "       round_new(z.std, 2) as GANZJL,round_new(z.stad, 2) as stad,round_new(z.stad*(100-z.mt)/(100-z.mad), 2) as star,\n"
                + "       round_new(z.hdaf, 2) as HDAF,round_new(z.had, 2) as had,\n"

                + "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"

                + "       TO_CHAR(Y.CAIYSJ, 'YYYY-MM-DD') AS CAIYSJ,\n"
                + "       z.huayy,\n"
                + "       y.lurry as caiyry from\n"
                + "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,zm.bianm as bianh,\n"
                + "                cz.mingc as fazmc,\n"
                + "                p.mingc as pinz,\n"
                + "                z.id as zhillsb_id,\n"
                + "                sum(round_new(f.laimsl, "
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "                sum(f.ches) as ches\n"
                + "  from fahb f, zhillsb z, meikxxb m, chezxxb cz, pinzb p,zhuanmb zm,zhuanmlb zb,gongysb g\n"
                + " where z.zhilb_id = f.zhilb_id\n"
                + s
                + "   and f.gongysb_id=g.id\n"
                + "   and f.pinzb_id = p.id\n"
                + "   and f.faz_id = cz.id\n"
                + "   and f.meikxxb_id = m.id\n"
                + "   and z.id=zm.zhillsb_id\n"
                + "   and zm.zhuanmlb_id=zb.id\n"
                + "   and zb.jib=3\n"
                + "   and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + " group by g.mingc,m.mingc, cz.mingc,zm.bianm,p.mingc,z.id) a,zhillsb z,yangpdhb y\n"// ,fahb
                // f\n"
                + " where z.id = a.zhillsb_id\n"
                // +" and f.zhilb_id=z.zhilb_id\n"
                + "   and y.zhilblsb_id = z.id\n"
                // + " and f.daohrq=" + DateUtil.FormatOracleDate(riq) + "\n"
                + "order by a.MEIKDQMC,a.MEIKDWMC, a.bianh";

        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);

        ResultSetList rstmp = con.getResultSetList(buffer.toString());
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb
                .append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='Meizjyrb' order by xuh");
        ResultSetList rsl = con.getResultSetList(sb.toString());
        if (rsl.getRows() > 0) {
            ArrWidth = new int[rsl.getRows()];
            strFormat = new String[rsl.getRows()];
            String biaot = rsl.getString(0, 1);
            String[] Arrbt = biaot.split("!@");
            ArrHeader = new String[Arrbt.length][rsl.getRows()];
            Zidm = new String[rsl.getRows()];
            rs = new ResultSetList();
            while (rsl.next()) {
                Zidm[rsl.getRow()] = rsl.getString("zidm");
                ArrWidth[rsl.getRow()] = rsl.getInt("kuand");
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? ""
                        : rsl.getString("format");
                String[] title = rsl.getString("biaot").split("!@");
                for (int i = 0; i < title.length; i++) {
                    ArrHeader[i][rsl.getRow()] = title[i];
                }
            }
            rs.setColumnNames(Zidm);
            while (rstmp.next()) {
                rs.getResultSetlist().add(rstmp.getArrString(Zidm));
            }
            rstmp.close();
            rsl.close();
            rsl = con
                    .getResultSetList("select biaot from baobpzb where guanjz='Meizjyrb'");
            String Htitle = "";
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                    .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                    .getString4());// ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            // ----------------------------- �¼ӵķ���
            // -------------------------------------------------------

            ArrHeader = new String[1][23];

            ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��",
                    "��վ", "����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
                    "����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
                    "����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad",
                    "����<br>���ҷ�<br>(%)<br>Ad", "�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
                    "����<br>����<br>����<br>����<br>(%)<br>Vad",
                    "����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
                    "��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
                    "�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
                    "�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
                    "����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d",
                    "����<br>�������<br>(%)<br>St,ad", "�յ�<br>����<br>(%)<br>St,ar",
                    "����<br>�޻�<br>����<br>(%)<br>Hdaf",
                    "����<br>����<br>����<br>(%)<br>Had", "�̶�<br>̼<br>(%)<br>Fcad",
                    "�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd",

                    "ȡ��<br>����", "����Ա", "����Ա" };
            ArrWidth = new int[28];

            ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35,
                    35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 70,
                    45, 45 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                    .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                    .getString4());// ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
            rt
                    .setTitle(
                            "ú  ��  ��  ��  ��  ��"
                                    + ((getChangbValue().getId() > 0 && getChangbModel()
                                    .getOptionCount() > 2) ? "("
                                    + getChangbValue().getValue() + ")"
                                    : ""), ArrWidth);
            rt.title.setRowHeight(2, 45);
            rt.title.setRowCells(2, Table.PER_FONTSIZE, 22);
            rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

            rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
            rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

            strFormat = new String[] { "", "", "", "", "", "", "", "", "0.00",
                    "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "", "0.00",
                    "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                    "0.00", "0" };

        }

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 28; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(16, 4, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);
        RPTInit.getInsertSql(v.getDiancxxb_id(), buffer.toString(), rt,
                "ú  ��  ��  ��  ��  ��", "Meizjyrb");

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();

    }

    private String getMeizjyrb_zhilb() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();
        String strTongjRq = getTongjRq(con);
        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        // DateUtil custom = new DateUtil();
        String sql =

                "SELECT distinct a.BIANH,\n"
                        + "       a.MEIKDQMC,\n"
                        + "       a.MEIKDWMC,\n"
                        + "       a.PINZ,\n"
                        + "       a.FAZMC,\n"
                        + "       a.CHES,\n"
                        + "       a.JZSL,\n"
                        + "       round_new(z.mt, "
                        + v.getMtdec()
                        + ") as QUANSF,\n"
                        + "       round_new(z.mad, 2) as KONGQGZJSF,\n"
                        + "       round_new(z.aad, 2) as KONGQGZJHF,\n"
                        + "        round_new(z.ad, 2) as GANZJHF,\n"
                        + "       round_new(z.aar, 2) as SHOUDJHF,\n"
                        + "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
                        + "       round_new(z.vdaf, 2) as HUIFF,\n"
                        + "       round_new(z.qbad, "
                        + v.getFarldec()
                        + ") * 1000 as DANTRL,\n"
                        + "       round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 1000 as FARL,\n"
                        + "       round_new(round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 7000 / 29.271, 0) as FARL1,\n"
                        + "       ROUND_NEW(z.sdaf , 2) AS SDAF,\n"
                        + "       round_new(z.std, 2) as GANZJL,round_new(z.stad, 2) as stad,round_new(z.stad*(100-z.mt)/(100-z.mad), 2) as star,\n"
                        + "       round_new(z.hdaf, 2) as HDAF,round_new(z.had, 2) as had,\n"
                        + "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
                        + "       TO_CHAR(z.huaysj, 'YYYY-MM-DD') AS CAIYSJ,\n";
        String condition="";
        condition="  z.huayy,\n";
        if(MainGlobal.getXitxx_item("�����ձ�", "�Ƿ���ʾ¼����Ա", "0","��").equals("��")){
            condition="  z.lury,\n";
        }
        sql+=condition;
        sql+="       y.lurry as caiyry \n";
        sql+="from\n"
                + "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
                + "                cz.mingc as fazmc,\n"
                + "                p.mingc as pinz,\n"
                + "                f.zhilb_id as zhilb_id,\n"
                + "                sum(round_new(laimsl, "
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "                sum(f.ches) as ches\n"
                + "  from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                + " where z.id = f.zhilb_id\n"

                + s

                + "   and f.gongysb_id=g.id\n"
                + "   and f.pinzb_id = p.id\n"
                + "   and f.faz_id = cz.id\n"
                + "   and f.meikxxb_id = m.id\n"
                + "   and f."
                + strTongjRq
                + "="
                + DateUtil.FormatOracleDate(riq)
                + "\n"
                + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id) a,zhilb z,yangpdhb y,fahb f\n"
                + " where a.zhilb_id=z.id\n"
                + " and z.caiyb_id=y.caiyb_id and f.zhilb_id=z.id\n"
                + " and f." + strTongjRq + "=" + DateUtil.FormatOracleDate(riq)
                + "\n" + " order by a.bianh";

        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String[][] ArrHeader = new String[1][23];
        // 2013-05-08 ��Ȫ���� ������� ��Ϊ������
        ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��", "��վ",
                "����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
                "����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
                "����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad", "����<br>���ҷ�<br>(%)<br>Ad",
                "�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
                "����<br>����<br>����<br>����<br>(%)<br>Vad",
                "����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
                "��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
                "�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
                "�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
                "����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d",
                "����<br>�������<br>(%)<br>St,ad", "�յ�<br>����<br>(%)<br>St,ar",
                "����<br>�޻�<br>����<br>(%)<br>Hdaf",
                "����<br>����<br>����<br>(%)<br>Had", "�̶�<br>̼<br>(%)<br>Fcad",
                "�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd", "����<br>����", "����Ա", "����Ա" };
        int[] ArrWidth = new int[28];

        ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35, 35,
                35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 70, 45, 45 };

        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��"
                + ((getChangbValue().getId() > 0 && getChangbModel()
                .getOptionCount() > 2) ? "("
                + getChangbValue().getValue() + ")" : ""), ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                "0.00", "0.00", "" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 28; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(16, 4, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }

    //

    private String getMeizjyrb_zhilb_1() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        // DateUtil custom = new DateUtil();
        String sql =

                "SELECT a.BIANH,\n"
                        + "       a.MEIKDQMC,\n"
                        + "       a.MEIKDWMC,\n"
                        + "       a.PINZ,\n"
                        + "       a.FAZMC,\n"
                        + "       a.CHES,\n"
                        + "       a.JZSL,\n"
                        + "       round_new(z.mt, "
                        + v.getMtdec()
                        + ") as QUANSF,\n"
                        + "       round_new(z.mad, 2) as KONGQGZJSF,\n"
                        + "       round_new(z.aad, 2) as KONGQGZJHF,\n"
                        + "        round_new(z.ad, 2) as GANZJHF,\n"
                        + "       round_new(z.aar, 2) as SHOUDJHF,\n"
                        + "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
                        + "       round_new(z.vdaf, 2) as HUIFF,\n"
                        + "       round_new(z.qbad, "
                        + v.getFarldec()
                        + ") * 1000 as DANTRL,\n"
                        + "       round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 1000 as FARL,\n"
                        + "       round_new(round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 7000 / 29.271, 0) as FARL1,\n"
                        + "       ROUND_NEW(z.sdaf , 2) AS SDAF,\n"
                        + "       round_new(z.std, 2) as GANZJL,\n"
                        + "       round_new(z.hdaf, 2) as HDAF,\n"
                        + "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
                        + "       TO_CHAR(z.huaysj, 'YYYY-MM-DD') AS CAIYSJ,\n"
                        + "       z.huayy,\n"
                        + "       z.lury as caiyry from\n"
                        + "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
                        + "                cz.mingc as fazmc,\n"
                        + "                p.mingc as pinz,\n"
                        + "                f.zhilb_id as zhilb_id,\n"
                        + "                sum(round_new(laimsl, "
                        + v.getShuldec()
                        + ")) as jzsl,\n"
                        + "                sum(f.ches) as ches\n"
                        + "  from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                        + " where z.id = f.zhilb_id\n"

                        + s

                        + "   and f.gongysb_id=g.id\n"
                        + "   and f.pinzb_id = p.id\n"
                        + "   and f.faz_id = cz.id\n"
                        + "   and f.meikxxb_id = m.id\n"
                        + "   and f.daohrq="
                        + DateUtil.FormatOracleDate(riq)
                        + "\n"
                        + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id) a,zhilb z,fahb f\n"
                        + " where a.zhilb_id=z.id\n" + " and f.daohrq="
                        + DateUtil.FormatOracleDate(riq) + "\n" + " order by a.bianh";

        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String[][] ArrHeader = new String[1][25];
        ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��", "��վ",
                "����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
                "����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
                "����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad", "����<br>���ҷ�<br>(%)<br>Ad",
                "�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
                "����<br>����<br>����<br>����<br>(%)<br>Vad",
                "����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
                "��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
                "�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
                "�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
                "����<br>�޻�<br>����<br>Sdaf", "����<br>����<br>(%)<br>St,d",
                "����<br>�޻�<br>����<br>(%)<br>Hdaf", "�̶�<br>̼<br>(%)<br>Fcad",
                "�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd", "����<br>����", "����Ա", "¼��Ա" };
        int[] ArrWidth = new int[25];

        ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35, 35,
                35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 70, 45, 45 };

        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��"
                + ((getChangbValue().getId() > 0 && getChangbModel()
                .getOptionCount() > 2) ? "("
                + getChangbValue().getValue() + ")" : ""), ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 25; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(12, 3, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(16, 4, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }

    //	���Ʋ�����������ֵ����ʾ������Ա��¼����Ա����
    private String getMeizjyrb_zhilb_2() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();
        String strTongjRq = getTongjRq(con);
        String s = "";
        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }
        String sql =
                "SELECT distinct a.BIANH,\n"
                        + "       a.MEIKDQMC,\n"
                        + "       a.MEIKDWMC,\n"
                        + "       a.PINZ,\n"
                        + "       a.FAZMC,\n"
                        + "       a.CHES,\n"
                        + "       a.JZSL,\n"
                        + "       round_new(z.mt, "
                        + v.getMtdec()
                        + ") as QUANSF,\n"
                        + "       round_new(z.mad, 2) as KONGQGZJSF,\n"
                        + "       round_new(z.aad, 2) as KONGQGZJHF,\n"
                        + "        round_new(z.ad, 2) as GANZJHF,\n"
                        + "       round_new(z.aar, 2) as SHOUDJHF,\n"
                        + "       round_new(z.vad, 2) as KONGQGZJHFF,\n"
                        + "       round_new(z.vdaf, 2) as HUIFF,\n"
                        + "       round_new(z.qbad, "
                        + v.getFarldec()
                        + ") * 1000 as DANTRL,\n"
                        + "       round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 1000 as FARL,\n"
                        + "       round_new(round_new(z.qnet_ar, "
                        + v.getFarldec()
                        + ") * 7000 / 29.271, 0) as FARL1,\n"
                        + "       ROUND_NEW(z.sdaf , 2) AS SDAF,\n"
                        + "       round_new(z.std, 2) as GANZJL,round_new(z.stad, 2) as stad,round_new(z.stad*(100-z.mt)/(100-z.mad), 2) as star,\n"
                        + "       round_new(z.hdaf, 2) as HDAF,round_new(z.had, 2) as had,\n"
                        + "       round_new(z.fcad, 2) as FCAD,round_new(z.qgrd, 2) as QGRD,\n"
                        + "       TO_CHAR(z.huaysj, 'YYYY-MM-DD') AS CAIYSJ\n"
                        + " from\n"
                        + "(select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
                        + "                cz.mingc as fazmc,\n"
                        + "                p.mingc as pinz,\n"
                        + "                f.zhilb_id as zhilb_id,\n"
                        + "                sum(round_new(laimsl, "
                        + v.getShuldec()
                        + ")) as jzsl,\n"
                        + "                sum(f.ches) as ches\n"
                        + "  from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                        + " where z.id = f.zhilb_id\n"
                        + s
                        + "   and f.gongysb_id=g.id\n"
                        + "   and f.pinzb_id = p.id\n"
                        + "   and f.faz_id = cz.id\n"
                        + "   and f.meikxxb_id = m.id\n"
                        + "   and f."
                        + strTongjRq
                        + "="
                        + DateUtil.FormatOracleDate(riq)
                        + "\n"
                        + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id) a,zhilb z,yangpdhb y,fahb f\n"
                        + " where a.zhilb_id=z.id\n"
                        + " and z.caiyb_id=y.caiyb_id and f.zhilb_id=z.id\n"
                        + " and f." + strTongjRq + "=" + DateUtil.FormatOracleDate(riq)
                        + "\n" + " order by a.bianh";

        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String[][] ArrHeader = new String[1][26];

        ArrHeader[0] = new String[] { "����<br>���", "������λ", "ú��λ����", "Ʒ��", "��վ",
                "����", "����<br>����<br>(��)", "ȫˮ<br>��(%)<br>Mt",
                "����<br>����<br>��<br>ˮ��<br>(%)<br>Mad",
                "����<br>����<br>��<br>�ҷ�<br>(%)<br>Aad",
                "����<br>���ҷ�<br>(%)<br>Ad",
                "�յ�<br>��<br>�ҷ�<br>(%)<br>Aar",
                "����<br>����<br>����<br>����<br>(%)<br>Vad",
                "����<br>�޻�<br>����<br>����<br>(%)<br>Vdaf",
                "��Ͳ<br>����<br>��<br>(J/g)<br>Qb,ad",
                "�յ�<br>����<br>λ��<br>����<br>(J/g)<br>Qnet,ar",
                "�յ�<br>��<br>��λ<br>��ֵ<br>(Kcal<br>/Kg)",
                "����<br>�޻�<br>����<br>Sdaf",
                "����<br>����<br>(%)<br>St,d",
                "����<br>�������<br>(%)<br>St,ad",
                "�յ�<br>����<br>(%)<br>St,ar",
                "����<br>�޻�<br>����<br>(%)<br>Hdaf",
                "����<br>����<br>����<br>(%)<br>Had",
                "�̶�<br>̼<br>(%)<br>Fcad",
                "�ɻ�<br>��λ<br>��<br>(%)<br>Qgrd",
                "����<br>����"};
        int[] ArrWidth = new int[26];

        ArrWidth = new int[] { 50, 80, 80, 35, 45, 35, 35, 35, 35, 35, 35, 35,
                35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 35, 70};

        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��"
                + ((getChangbValue().getId() > 0 && getChangbModel()
                .getOptionCount() > 2) ? "("
                + getChangbValue().getValue() + ")" : ""), ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", "", "",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                "" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 28; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 5, "��ӡ����:" + DateUtil.FormatDate(new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(6, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(14, 3, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(22, 4, "�Ʊ�:" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }
//	����

    private String getMeizjyrb_zhilb_hd() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        String sql = "select * from (SELECT decode(grouping(f.daohrq)+grouping(a.meikdwmc)+grouping(a.pinz)+grouping(a.bianh),4,'����',to_char(f.daohrq,'yyyy-mm-dd')) as daohrq,\n"
                + " a.meikdwmc,a.pinz,sum(a.JZSL) as jzsl,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mt,"
                + v.getMtdec()
                + ")*a.jzsl)/sum(a.jzsl)),2) as QUANSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mad, 2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.aad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.vad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHFF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.stad,2)*a.jzsl)/sum(a.jzsl)),2) as stad,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qbad,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as DANTRL,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as FARL,\n"
                + " round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl))*7000/29.271,0) as FARL1\n"
                + " from\n"
                + " (select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,z.huaybh as bianh,\n"
                + "         cz.mingc as fazmc,\n"
                + "         p.mingc as pinz,\n"
                + "         f.zhilb_id as zhilb_id,\n"
                + "         f.id,\n"
                + "         sum(round_new(laimsl,"
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "         sum(f.ches) as ches\n"
                + " from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                + " where z.id = f.zhilb_id\n"
                + s
                + " and f.gongysb_id=g.id\n"
                + " and f.pinzb_id = p.id\n"
                + " and f.faz_id = cz.id\n"
                + " and f.meikxxb_id = m.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + "\n"
                + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.id) a,zhilb z,fahb f\n"
                + " where a.zhilb_id=z.id and f.zhilb_id=z.id and a.id=f.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + " group by rollup(f.daohrq,a.bianh,a.MEIKDWMC,a.pinz)\n"
                + " having not (grouping(f.daohrq)<>1 and grouping(a.pinz)=1)) order by meikdwmc \n";
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String ArrHeader[][] = new String[1][12];
        ArrHeader[0] = new String[] { "��úʱ��", "���", "Ʒ��", "ú��<br>T",
                "ȫˮ��<br>Mt<br>(%)", "�ոɻ�ˮ��Mad<br>(%)", "�ҷ�<br>Aad<br>(%)",
                "�ӷ���<br>Vad<br>(%)", "ȫ��<br>St,ad<br>(%)",
                "��Ͳ������<br>Qb,ad<br>(MJ/kg)", "��λ������<br>Qnet,ar (MJ/kg)",
                "����(cal)" };

        int[] ArrWidth = new int[12];
        ArrWidth = new int[] { 80, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60, 60 };

        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��"
                + ((getChangbValue().getId() > 0 && getChangbModel()
                .getOptionCount() > 2) ? "("
                + getChangbValue().getValue() + ")" : ""), ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);

        rt.setDefaultTitle(1, 5, "��������:" + getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "0", "0.00", "0.00",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 11; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);
        sql = "select * from xitxxb where leib = '����' and zhuangt = 1 and mingc = '�����ձ���β' order by xuh";
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.getRows() > 0) {
            while (rsl.next()) {
                int align = rsl.getInt("danw");
                String[] scol = rsl.getString("beiz").split(",");
                int colstart = Integer.parseInt(scol[0]);
                int colmerge = Integer.parseInt(scol[1]);
                String content = rsl.getString("zhi");
                rt.setDefautlFooter(colstart, colmerge, content, align);
            }
            rsl.close();
        } else {
            rt.setDefautlFooter(1, 2, "���:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(3, 2, "����:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(5, 2, "����:", Table.ALIGN_LEFT);
            rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        }
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }
    private String getMeizjyrb_zhilb_bm() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        String sql = "SELECT decode(a.meikdwmc,null,'�ϼ�',a.meikdwmc) as meikdwmc, yunsdw,a.pinz ,to_char(f.daohrq,'yyyy-mm-dd') as daohrq," +
                " to_char(a.huaysj,'yyyy-mm-dd') as huayrq,a.bianh,sum(a.JZSL) as jzsl,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.mt,"+ v.getMtdec()+ ")*a.jzsl)/sum(a.jzsl)),2),'90.9') as QUANSF,\n"
                + "to_char( round(decode(sum(a.jzsl),0,0,sum(round_new(z.mad, 2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as KONGQGZJSF,\n"
                + "to_char( round(decode(sum(a.jzsl),0,0,sum(round_new(z.aad, 2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as aad,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.ad,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as ad,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.vad,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as KONGQGZJHFF,\n"
                + "to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.vdaf,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as vdaf,\n"
                + " to_char(round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qbad,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3),'90.99') as DANTRL,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3),'90.99') as FARL,\n"
                + "round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"+ v.getFarldec()+ ")*a.jzsl)/sum(a.jzsl))/0.0041816,0) as FARL1, \n"
                + "  to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as qgrad ,\n"
                + "  to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.std,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as std ,\n"
                + "  to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.had,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as had ,\n"
                + "  to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.fcad,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as fcad ,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.aar,2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as SHOUDJHF,\n"
                + " to_char(round(decode(sum(a.jzsl),0,0,sum(round_new(z.stad*(100-z.mt)/(100-z.mad),2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as SHOUDL,\n"
                + " to_char( round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad*100/(100-z.mad),2)*a.jzsl)/sum(a.jzsl)),2),'90.99') as qgrd \n"
                + " from\n"
                + " (select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,wm_concat(distinct y.mingc) as yunsdw,getHuaybh4zl(z.id) as bianh,z.huaysj,\n"
                + "         cz.mingc as fazmc,\n"
                + "         p.mingc as pinz,\n"
                + "         f.zhilb_id as zhilb_id,\n"
                + "         f.id,\n"
                + "         sum(round_new(cp.maoz-cp.piz-cp.koud,"
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "         count(cp.id) as ches\n"
                + " from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g,yunsdwb y ,chepb  cp \n"
                + " where z.id = f.zhilb_id\n"
                + s
                + " and f.gongysb_id=g.id and f.id= cp.fahb_id and cp.yunsdwb_id=y.id(+)               \n"
                + " and f.pinzb_id = p.id\n"
                + " and f.faz_id = cz.id\n"
                + " and f.meikxxb_id = m.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + "\n"
                + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.id,z.id,z.huaysj) a,zhilb z,fahb f\n"
                + " where a.zhilb_id=z.id and f.zhilb_id=z.id and a.id=f.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + " group by rollup((a.MEIKDWMC,yunsdw,f.daohrq,a.huaysj,a.bianh,a.pinz))\n"
                + "  having not (grouping(a.meikdwmc)<>1 and grouping(a.bianh)=1)  \n";
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String ArrHeader[][] = new String[1][23];
        ArrHeader[0] = new String[] { "���","���䵥λ","ú��", "��������", "��������", "�������",
                "ú��<br>(t)", "ȫˮ��<br>Mt<br>(%)", "�ոɻ�ˮ��Mad<br>(%)",
                "����������ҷ�<br>Aad<br>(%)","������ҷ�<br>Ad<br>(%)",
                "�ոɻ��ӷ���<br>Vad<br>(%)","�����޻һ��ӷ���<br>Vdaf<br>(%)",

                "��Ͳ������<br>Qb,ad<br>(MJ/kg)","��λ������<br>Qnet,ar (MJ/kg)", "��λ������(KCal/Kg)","�����������λ������<br>Qgr,ad<br>(MJ/kg)",
                "�����ȫ��<br>St,d<br>(%)","�����������<br>Had<br>(%)","�̶�̼<br>FCad<br>(%)",
                "�յ����ҷ�<br>Aar<br>(%)", "�յ���ȫ��<br>St,ar<br>(%)", "�������λ������<br>Qgr,d<br>(MJ/kg)" };

        int[] ArrWidth = new int[15];
        ArrWidth = new int[] { 100,80, 80, 80, 80,80, 60, 60, 60, 60, 60, 60, 60,
                60, 60, 60, 60, 60, 60,60, 60, 60, 60 };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTmp = new Date();
        String aa = sdf.format(dateTmp);
        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.setDefaultTitle(1, 3, "��������:" + aa, Table.ALIGN_LEFT);
        // rt.setDefaultTitle(3, 1, getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "","", "", "", "", "0.00", "0.0",
                "0.00", "0.00","0.00",  "0.00", "0.00",  "0.00", "0.00",
                "0" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
//		rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 23; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(7, 2, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(13, 2, "�Ʊ�:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // rt.setDefautlFooter(1, 2, "���:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(3, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(5, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }
    private String getMeizjyrb_zhilb_fr() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        String sql = "SELECT decode(a.meikdwmc,null,'�ϼ�',a.meikdwmc) as meikdwmc,to_char(f.daohrq,'yyyy-mm-dd') as daohrq, to_char(a.huaysj,'yyyy-mm-dd') as huayrq,a.bianh,sum(a.JZSL) as jzsl,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mt,"
                + v.getMtdec()
                + ")*a.jzsl)/sum(a.jzsl)),2) as QUANSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mad, 2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.aad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.aar,2)*a.jzsl)/sum(a.jzsl)),2) as aar,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.vad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHFF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.stad,2)*a.jzsl)/sum(a.jzsl)),2) as stad,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.std,2)*a.jzsl)/sum(a.jzsl)),2) as std,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qbad,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as DANTRL,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as FARL,\n"
                + " round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl))*7000/29.271,0) as FARL1, \n"
                + "round(decode(sum(a.jzsl),0,0,sum(round_new(z.vdaf,2)*a.jzsl)/sum(a.jzsl)),2) as vdaf,\n"
                + "  round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad,2)*a.jzsl)/\n"
                + "sum(a.jzsl)),2) as qgrad ,\n"
                + "  round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad*100/(100-z.mad),2)*a.jzsl)/\n"
                + "sum(a.jzsl)),2) as qgrd \n"
                + " from\n"
                + " (select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,getHuaybh4zl(z.id) as bianh,z.huaysj,\n"
                + "         cz.mingc as fazmc,\n"
                + "         p.mingc as pinz,\n"
                + "         f.zhilb_id as zhilb_id,\n"
                + "         f.id,\n"
                + "         sum(round_new(laimsl,"
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "         sum(f.ches) as ches\n"
                + " from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                + " where z.id = f.zhilb_id\n"
                + s
                + " and f.gongysb_id=g.id\n"
                + " and f.pinzb_id = p.id\n"
                + " and f.faz_id = cz.id\n"
                + " and f.meikxxb_id = m.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + "\n"
                + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.id,z.id,z.huaysj) a,zhilb z,fahb f\n"
                + " where a.zhilb_id=z.id and f.zhilb_id=z.id and a.id=f.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + " group by rollup(a.MEIKDWMC,f.daohrq,a.huaysj,a.bianh)\n"
                + "  having not (grouping(a.meikdwmc)<>1 and grouping(a.bianh)=1)  \n";
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String ArrHeader[][] = new String[1][18];
        ArrHeader[0] = new String[] { "���", "��������", "��������", "�������",
                "ú��<br>(t)", "ȫˮ��<br>Mt<br>(%)", "�ոɻ�ˮ��Mad<br>(%)",
                "�ոɻ��ҷ�<br>Aad<br>(%)", "�յ����ҷ�<br>Aar<br>(%)",
                "�ոɻ��ӷ���<br>Vad<br>(%)", "ȫ��<br>St,ad<br>(%)",
                "�ɻ���<br>St,d<br>(%)", "��Ͳ������<br>Qb,ad<br>(MJ/kg)",
                "��λ������<br>Qnet,ar (MJ/kg)", "��λ������(KCal/Kg)",
                "�����޻һ�<br>Vdaf<br>(%)", "�����������λ������<br>Qgr,ad<br>(MJ/kg)",
                "�������λ������<br>Qgr,d<br>(MJ/kg)" };

        int[] ArrWidth = new int[15];
        ArrWidth = new int[] { 100, 80, 80, 80, 60, 60, 60, 60, 60, 60, 60, 60,
                60, 60, 60, 60, 60, 60 };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTmp = new Date();
        String aa = sdf.format(dateTmp);
        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.setDefaultTitle(1, 3, "��������:" + aa, Table.ALIGN_LEFT);
        // rt.setDefaultTitle(3, 1, getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "0.00", "0.0",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                "0" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 4; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(7, 2, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(13, 2, "�Ʊ�:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // rt.setDefautlFooter(1, 2, "���:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(3, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(5, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }

    private String getMeizjyrb_zhilb_zr() {
        Visit v = (Visit) this.getPage().getVisit();
        Report rt = new Report();
        JDBCcon con = new JDBCcon();

        String s = "";

        if (!this.hasDianc(this.getTreeid_dc())) {
            s = "	  and f.diancxxb_id=" + this.getTreeid_dc() + " \n";// ����
            // ����������;
        }

        String sql = "SELECT decode(a.meikdwmc,null,'�ϼ�',a.meikdwmc) as meikdwmc,to_char(f.daohrq,'yyyy-mm-dd') as daohrq, to_char(a.huaysj,'yyyy-mm-dd') as huayrq,a.bianh,sum(a.JZSL) as jzsl,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mt,"
                + v.getMtdec()
                + ")*a.jzsl)/sum(a.jzsl)),2) as QUANSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.mad, 2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJSF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.aad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHF,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.aar,2)*a.jzsl)/sum(a.jzsl)),2) as aar,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.vad,2)*a.jzsl)/sum(a.jzsl)),2) as KONGQGZJHFF,\n"
                + "round(decode(sum(a.jzsl),0,0,sum(round_new(z.vdaf,2)*a.jzsl)/sum(a.jzsl)),2) as vdaf,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.stad,2)*a.jzsl)/sum(a.jzsl)),2) as stad,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.std,2)*a.jzsl)/sum(a.jzsl)),2) as std,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qbad,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as DANTRL,\n"
                + "  round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad,2)*a.jzsl)/\n"
                + "sum(a.jzsl)),2) as qgrad ,\n"
                + "  round(decode(sum(a.jzsl),0,0,sum(round_new(z.qgrad*100/(100-z.mad),2)*a.jzsl)/\n"
                + "sum(a.jzsl)),2) as qgrd ,\n"
                + " round(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl)),3) as FARL,\n"
                + " round_new(decode(sum(a.jzsl),0,0,sum(round_new(z.qnet_ar,"
                + v.getFarldec()
                + ")*a.jzsl)/sum(a.jzsl))*7000/29.271,0) as FARL1  \n"
                + " from\n"
                + " (select distinct g.mingc as MEIKDQMC, m.mingc as meikdwmc,getHuaybh4zl(z.id) as bianh,z.huaysj,\n"
                + "         cz.mingc as fazmc,\n"
                + "         p.mingc as pinz,\n"
                + "         f.zhilb_id as zhilb_id,\n"
                + "         f.id,\n"
                + "         sum(round_new(laimsl,"
                + v.getShuldec()
                + ")) as jzsl,\n"
                + "         sum(f.ches) as ches\n"
                + " from fahb f, zhilb z, meikxxb m, chezxxb cz, pinzb p,gongysb g\n"
                + " where z.id = f.zhilb_id\n"
                + s
                + " and f.gongysb_id=g.id\n"
                + " and f.pinzb_id = p.id\n"
                + " and f.faz_id = cz.id\n"
                + " and f.meikxxb_id = m.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + "\n"
                + " group by g.mingc,m.mingc, cz.mingc,z.huaybh,p.mingc,f.zhilb_id,f.id,z.id,z.huaysj) a,zhilb z,fahb f\n"
                + " where a.zhilb_id=z.id and f.zhilb_id=z.id and a.id=f.id\n"
                + " and f.daohrq="
                + DateUtil.FormatOracleDate(riq)
                + " group by rollup(a.MEIKDWMC,f.daohrq,a.huaysj,a.bianh)\n"
                + "  having not (grouping(a.meikdwmc)<>1 and grouping(a.bianh)=1)  \n";
        StringBuffer buffer = new StringBuffer();
        buffer.append(sql);
        ResultSet rs = con.getResultSet(buffer,
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        String ArrHeader[][] = new String[1][18];
        ArrHeader[0] = new String[] { "���", "��������", "��������", "�������",
                "ú��<br>(t)", "ȫˮ��<br>Mt<br>(%)", "�ոɻ�ˮ��Mad<br>(%)",
                "�ոɻ��ҷ�<br>Aad<br>(%)", "�յ����ҷ�<br>Aar<br>(%)",
                "�ոɻ��ӷ���<br>Vad<br>(%)", "�����޻һ��ӷ���<br>Vdaf<br>(%)",
                "ȫ��<br>St,ad<br>(%)", "�ɻ���<br>St,d<br>(%)",
                "��Ͳ������<br>Qb,ad<br>(MJ/kg)", "�����������λ������<br>Qgr,ad<br>(MJ/kg)",
                "�������λ������<br>Qgr,d<br>(MJ/kg)", "��λ������<br>Qnet,ar (MJ/kg)",
                "��λ������(KCal/Kg)" };

        int[] ArrWidth = new int[15];
        ArrWidth = new int[] { 100, 80, 80, 80, 60, 60, 60, 60, 60, 60, 60, 60,
                60, 60, 60, 60, 60, 60 };
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dateTmp = new Date();
        String aa = sdf.format(dateTmp);
        int aw = rt.paperStyle(((Visit) this.getPage().getVisit())
                .getDiancxxb_id(), ((Visit) this.getPage().getVisit())
                .getString4());// ȡ�ñ���ֽ������
        rt.getArrWidth(ArrWidth, aw);// ��ӱ����A4����
        rt.setTitle("ú  ��  ��  ��  ��  ��", ArrWidth);
        rt.title.setRowHeight(2, 45);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.setDefaultTitle(1, 3, "��������:" + aa, Table.ALIGN_LEFT);
        // rt.setDefaultTitle(3, 1, getRiq(), Table.ALIGN_LEFT);
        rt.title.setRowCells(3, Table.PER_FONTSIZE, 10);

        String[] strFormat = new String[] { "", "", "", "", "0.00", "0.0",
                "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00",
                "0.00" };

        rt.setBody(new Table(rs, 1, 0, 0));
        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
        rt.body.setPageRows(rt.PAPER_COLROWS);
        // ���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.setColFormat(strFormat);
        for (int i = 1; i <= 4; i++) {
            rt.body.setColAlign(i, Table.ALIGN_CENTER);
        }

        // rt.body.setColCells(2, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(3, Table.PER_FONTSIZE, 7);
        // rt.body.setColCells(5, Table.PER_FONTSIZE, 7);

        rt.createDefautlFooter(ArrWidth);

        rt.setDefautlFooter(1, 2, "����:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(7, 2, "���:", Table.ALIGN_LEFT);
        rt.setDefautlFooter(13, 2, "�Ʊ�:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // rt.setDefautlFooter(1, 2, "���:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(3, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(5, 2, "����:", Table.ALIGN_LEFT);
        // rt.setDefautlFooter(7, 3, "����Ա:", Table.ALIGN_LEFT);
        rt.footer.setRowCells(2, Table.PER_FONTSIZE, 10);

        // ����ҳ��
        _CurrentPage = 1;
        _AllPages = rt.body.getPages();
        if (_AllPages == 0) {
            _CurrentPage = 0;
        }
        con.Close();
        rt.body.setRowHeight(24);

        return rt.getAllPagesHtml();
    }

    // ��ť�ļ����¼�
    private boolean _RefurbishChick = false;

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }

    // ����ť�ύ����
    public void submit(IRequestCycle cycle) {
        if (_RefurbishChick) {
            _RefurbishChick = false;
            getSelectData();
            getMeizjyrb_zhilb_bm();
        }
    }

    // -------------------------�糧Tree-----------------------------------------------------------------
    public IPropertySelectionModel getDiancmcModel() {
        if (((Visit) getPage().getVisit()).getProSelectionModel2() == null) {
            getDiancmcModels();
        }
        return ((Visit) getPage().getVisit()).getProSelectionModel2();
    }

    public void setDiancmcModel(IPropertySelectionModel _value) {
        ((Visit) getPage().getVisit()).setProSelectionModel2(_value);
    }

    public void getDiancmcModels() {
        Visit visit = (Visit) this.getPage().getVisit();
        String sql = " select d.id,d.mingc from diancxxb d where d.id="
                + visit.getDiancxxb_id() + " \n";
        sql += " union \n";
        sql += "  select d.id,d.mingc from diancxxb d where d.fuid="
                + visit.getDiancxxb_id() + " \n";
        setDiancmcModel(new IDropDownModel(sql));
    }

    public String getTreeid_dc() {
        String treeid = ((Visit) getPage().getVisit()).getString3();
        if (treeid == null || treeid.equals("")) {
            ((Visit) getPage().getVisit()).setString3(String
                    .valueOf(((Visit) this.getPage().getVisit())
                            .getDiancxxb_id()));
        }
        return ((Visit) getPage().getVisit()).getString3();
    }

    public void setTreeid_dc(String treeid) {
        ((Visit) getPage().getVisit()).setString3(treeid);
    }

    DefaultTree dc;

    public DefaultTree getTree_dc() {
        return dc;
    }

    public void setTree_dc(DefaultTree etu) {
        dc = etu;
    }

    public String getTreeScript1() {
        return getTree_dc().getScript();
    }

    // -------------------------�糧Tree END----------

    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }

    // ˢ�ºⵥ�б�
    public void getSelectData() {
        Visit visit = (Visit) this.getPage().getVisit();
        Toolbar tb1 = new Toolbar("tbdiv");
        // if (visit.isFencb()) {
        // tb1.addText(new ToolbarText("����:"));
        // ComboBox changbcb = new ComboBox();
        // changbcb.setTransform("ChangbSelect");
        // changbcb.setWidth(130);
        // changbcb
        // .setListeners("select:function(own,rec,index){Ext.getDom('ChangbSelect').selectedIndex=index}");
        // tb1.addField(changbcb);
        // tb1.addText(new ToolbarText("-"));
        // }

        DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
                "diancTree", "" + visit.getDiancxxb_id(), "", null,
                getTreeid_dc());
        setTree_dc(dt1);
        TextField tf1 = new TextField();
        tf1.setId("diancTree_text");
        tf1.setWidth(100);
        tf1.setValue(((IDropDownModel) getDiancmcModel())
                .getBeanValue(Long.parseLong(getTreeid_dc() == null
                        || "".equals(getTreeid_dc()) ? "-1" : getTreeid_dc())));

        ToolbarButton tb3 = new ToolbarButton(null, null,
                "function(){diancTree_window.show();}");
        tb3.setIcon("ext/resources/images/list-items.gif");
        tb3.setCls("x-btn-icon");
        tb3.setMinWidth(20);

        tb1.addText(new ToolbarText("�糧:"));
        tb1.addField(tf1);
        tb1.addItem(tb3);

        tb1.addText(new ToolbarText("-"));

        tb1.addText(new ToolbarText("��������:"));
        DateField df = new DateField();
        df.setValue(getRiq());
        df.Binding("RIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
        df.setId("daohrq");
        tb1.addField(df);
        tb1.addText(new ToolbarText("-"));

        ToolbarButton rbtn = new ToolbarButton(null, "��ѯ",
                "function(){document.getElementById('RefurbishButton').click();}");
        rbtn.setIcon(SysConstant.Btn_Icon_Search);
        tb1.setWidth("bodyWidth");
        tb1.addItem(rbtn);
        tb1.addFill();
        // tb1.addText(new ToolbarText("<marquee width=300
        // scrollamount=2></marquee>"));
        setToolbar(tb1);
    }

    // ������ʹ�õķ���
    public Toolbar getToolbar() {
        return ((Visit) this.getPage().getVisit()).getToolbar();
    }

    public void setToolbar(Toolbar tb1) {
        ((Visit) this.getPage().getVisit()).setToolbar(tb1);
    }

    public String getToolbarScript() {
        // if(getTbmsg()!=null) {
        // getToolbar().deleteItem();
        // getToolbar().addText(new ToolbarText("<marquee width=300
        // scrollamount=2>"+getTbmsg()+"</marquee>"));
        // }
        ((DateField) getToolbar().getItem("daohrq")).setValue(getRiq());
        return getToolbar().getRenderScript();
    }

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
            visit.setActivePageName(getPageName().toString());
            setRiq(DateUtil.FormatDate(new Date()));
            visit.setString1("");
            setChangbValue(null);
            setChangbModel(null);

            // begin��������г�ʼ������
            visit.setString4(null);

            String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
            if (pagewith != null) {

                visit.setString4(pagewith);
            }
            // visit.setString4(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ

            setTreeid_dc(String.valueOf(visit.getDiancxxb_id()));
            getSelectData();
        }
        if (cycle.getRequestContext().getParameters("lx") != null) {
            visit.setString1(cycle.getRequestContext().getParameters("lx")[0]);
            mstrReportName = visit.getString1();
        } else {
            if (visit.getString1().equals("")) {
                mstrReportName = visit.getString1();
            }
        }
        // mstrReportName="diaor04bb";
        // if (mstrReportName.equals("Meizjyrb")) {
        // leix = "1";
        // } else if (mstrReportName.equals("Meizjyrb_zhilb")) {
        // leix = "2";
        // }
        getMeizjyrb_zhilb_bm();
        blnIsBegin = true;

    }

}