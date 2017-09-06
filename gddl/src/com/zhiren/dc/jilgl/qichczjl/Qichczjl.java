package com.zhiren.dc.jilgl.qichczjl;

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
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.form.IPropertySelectionModel;
import org.apache.tapestry.html.BasePage;

import com.zhiren.common.DateUtil;
import com.zhiren.common.IDropDownBean;
import com.zhiren.common.IDropDownModel;
import com.zhiren.common.JDBCcon;
import com.zhiren.common.Locale;
import com.zhiren.common.MainGlobal;
import com.zhiren.common.ResultSetList;
import com.zhiren.common.SysConstant;
import com.zhiren.common.ext.Toolbar;
import com.zhiren.common.ext.ToolbarButton;
import com.zhiren.common.ext.ToolbarText;
import com.zhiren.common.ext.form.ComboBox;
import com.zhiren.common.ext.form.DateField;
import com.zhiren.common.ext.form.TextField;
import com.zhiren.common.ext.tree.DefaultTree;
import com.zhiren.main.Visit;
import com.zhiren.main.validate.Login;
import com.zhiren.report.RPTInit;
import com.zhiren.report.Report;
import com.zhiren.report.Table;

/**
 * @author rock
 * @discription ���¸İ�ѳ���������̨���ⶼ�ںϵ���һ��ͨ���������п���
 * @since 2009-10-27
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-06
 * �������޸�̨���кϲ�ͳ���к��ʽ����ȷ�������е糧ID����
 */

/**
 * huochaoyuan
 * 2009-11-10 ����̨�ʸ��º�δ��ȡ�����ϵͳԭ�еĹ�Լ����visit.getShuldec()��ͨ���Խϲ��Ӱ����ʵʩ�糧����Ϊ����
 * getGys_Mkxj_Pz(),getDc_Gys_Mkxj_Pz(),getGys_Mk_Pzxj(),getDc_Gys_Mk_Pzxj(),getDc_Gys_Mk_Pzxj(),getDc_Gys_Mk_Pz_Ccxj()
 * ȡ��sql��ӹ�Լ����
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-20
 * �������޸�����̨��û���������䷽ʽ��©��
 */
/*
 * ���ߣ�����
 * ʱ�䣺2009-11-30
 * ����������Ʒ�ֻ��ܱ�getDc_Gys_Mkxj_Pzhz(),getGys_Mkxj_Pzhz()
 */
/*
* ���ߣ�ww
* ʱ�䣺2009-12-26
* ���������ѡ�񱨱�ͳ��ʱ���ǰ��������ڻ��ǰ��������ڣ�Ĭ��Ϊ��������,ֻ��getGys_Mkxj_Pz���������
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
 * ����:tzf
 * ʱ��:2010-01-25
 * �޸�����:����������˾Ҫ��ӯ���������໥���㣬���򱨱����治ƽ���ò������ơ�
 */
/*
 * ���ߣ�������
 * ʱ�䣺2010-01-27 15��24
 * �������޸Ĺ��ڱ����A4ֽ�ĸ�ʽ���⣬
 * 		 �ѱ���ĸ�ʽ�趨ΪA4��ӡ��ʽ��
 */
/*
 * ���ߣ�liht
 * ʱ�䣺2011-12-23
 * ������ԭ��sql������meikxxb��jihkjb_id��jihkjb��id��������xitxxb�����Ӳ������������ƻ��ھ�ͳ�ơ���fahb��jihkjb_id�����������ӷ���getJihkjTJ()��ù�������
 */
/*
 * ���ߣ����
 * ʱ�䣺2013-07-06
 * ����������RPTTYPE_TZ_ALL��Ӧ�ı�����ʾ����
 */
public class Qichczjl extends BasePage {
    private static final int RPTTYPE_TZ_ZERO = 0; //ͳ��ʵ������С��λΪ0
    private static final int RPTTYPE_TZ_ALL = 1;
    private static final int RPTTYPE_TZ_HUOY = 2;
    private static final int RPTTYPE_TZ_QIY = 3;

    private static final String RptKey_gys_mkxj_pz = "JILTZ_gys_mkxj_pz";    //�Ƿֳ���������糧С��
    private static final String RptKey_dc_gys_mkxj_pz = "JILTZ_dc_gys_mkxj_pz";    //�糧��Ӧ��ú��Ʒ��
    private static final String RptKey_gys_mk_pzxj = "JILTZ_gys_mk_pzxj";    //�Ƿֳ���������糧С��
    private static final String RptKey_dc_gys_mk_pzxj = "JILTZ_dc_gys_mk_pzxj";    //�糧��Ӧ��ú��Ʒ��
    private static final String RptKey_gys_mk_pz_ccxj = "JILTZ_gys_mk_pz_ccxj";    //�Ƿֳ���������糧С��
    private static final String RptKey_dc_gys_mk_pz_ccxj = "JILTZ_dc_gys_mk_pz_ccxj";    //�糧��Ӧ��ú��Ʒ�ֳ���
    private static final String RptKey_gys_mk_pzhz = "JILTZ_gys_mk_pzhz";    //�糧��Ӧ��ú��Ʒ�ֻ���
    private static final String RptKey_dc_gys_mk_pzhz = "JILTZ_dc_gys_mk_pzhz";    //�糧��Ӧ��ú��Ʒ�ֻ���

    private static final String RptType_mkxj = "ú��С��";
    private static final String RptType_pzxj = "Ʒ��С��";
    private static final String RptType_ccxj = "����С��";
    private static final String RptType_pzhz = "Ʒ�ֻ���";
    private static final String RptType_zero = "С��Ϊ��";

    // �����û���ʾ
    private String msg = "";

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = MainGlobal.getExtMessageBox(msg, false);
    }

    protected void initialize() {
        super.initialize();
        setMsg("");
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
    private String briq;

    public String getBRiq() {
        if (briq == null || briq.equals("")) {
            Calendar stra = Calendar.getInstance();
            stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
            stra.add(Calendar.MONTH, -1);
            briq = DateUtil.FormatDate(stra.getTime());
        }
        return briq;
    }

    public void setBRiq(String briq) {
        this.briq = briq;
    }

    // ������
    private String eriq;

    public String getERiq() {
        return eriq;
    }

    public void setERiq(String eriq) {
        this.eriq = eriq;
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

    // �����Ʊ���Ĭ�ϵ�ǰ�û�
    private String getZhibr() {
        Visit visit = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String zhibr = "";
        String zhi = "��";
        String sql = "select zhi from xitxxb where mingc = '�±������Ʊ����Ƿ�Ĭ�ϵ�ǰ�û�' and diancxxb_id = " + visit.getDiancxxb_id();
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

    // Ʒ��������
    public IDropDownBean getPinzValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean3() == null) {
            if (getPinzModel().getOptionCount() > 0) {
                setPinzValue((IDropDownBean) getPinzModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean3();
    }

    public void setPinzValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean3(value);
    }

    public IPropertySelectionModel getPinzModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel3() == null) {
            setPinzModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel3();
    }

    public void setPinzModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel3(value);
    }

    public void setPinzModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(-1, "ȫ��"));
        setPinzModel(new IDropDownModel(list, SysConstant.SQL_Pinz_mei));
    }

    // �ƻ��ھ�������
    public IDropDownBean getJihkjValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean4() == null) {
            if (getJihkjModel().getOptionCount() > 0) {
                setJihkjValue((IDropDownBean) getJihkjModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean4();
    }

    public void setJihkjValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean4(value);
    }

    public IPropertySelectionModel getJihkjModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel4() == null) {
            setJihkjModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel4();
    }

    public void setJihkjModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel4(value);
    }

    public void setJihkjModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(-1, "ȫ��"));
        String sql = "select id, mingc from jihkjb where mingc <> '��'";

        setJihkjModel(new IDropDownModel(list, sql));
    }

    // �������������
    public IDropDownBean getShenhztValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean2() == null) {
            if (getShenhztModel().getOptionCount() > 0) {
                setShenhztValue((IDropDownBean) getShenhztModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean2();
    }

    public void setShenhztValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean2(value);
    }

    public IPropertySelectionModel getShenhztModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel2() == null) {
            setShenhztModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel2();
    }

    public void setShenhztModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel2(value);
    }

    public void setShenhztModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(-1, "ȫ��"));
        list.add(new IDropDownBean(3, "�����"));
        list.add(new IDropDownBean(2, "δ���"));
        setShenhztModel(new IDropDownModel(list));
    }

    // ��������������
    public IDropDownBean getRptTypeValue() {
        if (((Visit) this.getPage().getVisit()).getDropDownBean1() == null) {
            if (getRptTypeModel().getOptionCount() > 0) {
                setRptTypeValue((IDropDownBean) getRptTypeModel().getOption(0));
            }
        }
        return ((Visit) this.getPage().getVisit()).getDropDownBean1();
    }

    public void setRptTypeValue(IDropDownBean value) {
        ((Visit) this.getPage().getVisit()).setDropDownBean1(value);
    }

    public IPropertySelectionModel getRptTypeModel() {
        if (((Visit) this.getPage().getVisit()).getProSelectionModel1() == null) {
            setRptTypeModels();
        }
        return ((Visit) this.getPage().getVisit()).getProSelectionModel1();
    }

    public void setRptTypeModel(IPropertySelectionModel value) {
        ((Visit) this.getPage().getVisit()).setProSelectionModel1(value);
    }

    public void setRptTypeModels() {
        List list = new ArrayList();
        list.add(new IDropDownBean(1, RptType_mkxj));
        list.add(new IDropDownBean(2, RptType_pzxj));
        list.add(new IDropDownBean(3, RptType_ccxj));
        list.add(new IDropDownBean(4, RptType_pzhz));
        list.add(new IDropDownBean(5, RptType_zero));
        setRptTypeModel(new IDropDownModel(list));
    }

    // ���ѡ������ڵ�Ķ�Ӧ�ĵ糧����
    private String getDcMingc(String id) {
        if (id == null || "".equals(id)) {
            return "";
        }
        JDBCcon con = new JDBCcon();
        String mingc = "";
        String sql = "select mingc from diancxxb where id = " + id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            mingc = rsl.getString("mingc");
        }
        rsl.close();
        con.Close();
        return mingc;
    }

    // ���ѡ��Ĺ�Ӧ�����ڵ�Ķ�Ӧ������
    private String[] getGys(String id) {
        String[] gys = {"ȫ��", "-1"};
        if (id == null || "".equals(id)) {
            return gys;
        }
        JDBCcon con = new JDBCcon();
        String sql = "select mingc, jib from vwgongysmkcz where id = " + id;
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            gys[0] = rsl.getString("mingc");
            gys[1] = rsl.getString("jib");
        }
        rsl.close();
        con.Close();
        return gys;
    }

    // �жϵ糧Tree����ѡ�糧ʱ�����ӵ糧
    private boolean isParentDc(String id) {
        boolean isParent = false;
        if (id == null || "".equals(id)) {
            return isParent;
        }
        JDBCcon con = new JDBCcon();
        String sql = "select mingc from diancxxb where fuid = " + id;
        if (con.getHasIt(sql)) {
            isParent = true;
        }
        con.Close();
        return isParent;
    }

    private String getTongjRq(JDBCcon con) {
        if (con == null) {
            con = new JDBCcon();
        }
        String tongjrq = " select * from xitxxb where mingc='��������ͳ������'  and zhuangt=1 and leib='����' and diancxxb_id=" + getTreeid_dc();
        ResultSetList rsl = con.getResultSetList(tongjrq);

        String strTongjrq = "daohrq";

        if (rsl.next()) {
            strTongjrq = rsl.getString("zhi");
        }

        rsl.close();
        con.Close();
        return strTongjrq;
    }

    private String getJihkjTj() {
        JDBCcon con = new JDBCcon();
        String jihkjTj = "and m.JIHKJB_ID = j.id";
        String sql = "select * from xitxxb where mingc = '�������ƻ��ھ�ͳ��' and zhuangt = 1 and leib = '����' and diancxxb_id = " + getTreeid_dc();
        ResultSetList rsl = con.getResultSetList(sql);
        if (rsl.next()) {
            jihkjTj = "and f.JIHKJB_ID = j.id";
        }

        rsl.close();
        con.Close();
        return jihkjTj;
    }

    //	ȡ�����ڲ���SQL
    private String getDateParam() {

//		��������
        String rqsql = "";
        if (getBRiq() == null || "".equals(getBRiq())) {
            rqsql = "and f." + getTongjRq(null) + " >= " + DateUtil.FormatOracleDate(new Date()) + "\n";
        } else {
            rqsql = "and f." + getTongjRq(null) + " >= " + DateUtil.FormatOracleDate(getBRiq()) + "\n";
        }
        if (getERiq() == null || "".equals(getERiq())) {
            rqsql += "and f." + getTongjRq(null) + " < " + DateUtil.FormatOracleDate(new Date()) + "+1\n";
        } else {
            rqsql += "and f." + getTongjRq(null) + " < " + DateUtil.FormatOracleDate(getERiq()) + "+1\n";
        }
        return rqsql;
    }

    // ȡ�ù�Ӧ�̲���SQL
    private String getGysParam() {
        // ��Ӧ��ú������
        String gyssql = "";
//		if ("1".equals(getGys(getTreeid())[1])) {
//			gyssql = "and f.gongysb_id = " + getTreeid() + "\n";
//		} else if ("0".equals(getGys(getTreeid())[1])) {
//			gyssql = "and f.meikxxb_id = " + getTreeid() + "\n";
//		}
        //getTreeid()
        DefaultTree dt = ((Visit) this.getPage().getVisit()).getDefaultTree();
        int jib = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getLevel();
        String gongysb_id = "";
        long meikxxb_id = 0;
        long faz_id = 0;
        if (jib == 2) {
            gyssql += "and f.gongysb_id = " + getTreeid() + "\n";
        } else if (jib == 3) {
            gongysb_id = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getId();
            gyssql += "and f.gongysb_id = " + gongysb_id + "\n";
            meikxxb_id = Long.parseLong(getTreeid()) - Long.parseLong(gongysb_id);
            gyssql += "and f.meikxxb_id = " + meikxxb_id + "\n";
        } else if (jib == 4) {
            gongysb_id = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getParentNode().getId();
            gyssql += "and f.gongysb_id = " + gongysb_id + "\n";
            String meikNodeid = dt.getTree().getTreeRootNode().getNodeById(getTreeid()).getParentNode().getId();
            meikxxb_id = Long.parseLong(meikNodeid) - Long.parseLong(gongysb_id);
            gyssql += "and f.meikxxb_id = " + meikxxb_id + "\n";
            faz_id = Long.parseLong(getTreeid()) - Long.parseLong(meikNodeid);
            gyssql += "and f.faz_id = " + faz_id + "\n";
        }

        return gyssql;
    }

    // ȡ��Ʒ�ֲ���SQL
    private String getPinzParam() {
        // Ʒ��sql
        String pzsql = "";
        if (getPinzValue() != null && getPinzValue().getId() != -1) {
            pzsql = "and f.pinzb_id = " + getPinzValue().getId() + "\n";
        }
        return pzsql;
    }

    // ȡ�����״̬����SQL
    private String getShenhztParam() {
        // Ʒ��sql
        String sql = "";
        if (getShenhztValue().getId() == 3) {
            sql = "and f.hedbz >= " + getShenhztValue().getId() + "\n";
        } else if (getShenhztValue().getId() == 2) {
            sql = "and f.hedbz <= " + getShenhztValue().getId() + "\n";
        }
        return sql;
    }

    // ȡ�����䷽ʽ����SQL
    private String getYsfsParam() {
        Visit v = (Visit) getPage().getVisit();
        // ���䷽ʽsql
        String ysfssql = "";
        if (v.getInt1() == RPTTYPE_TZ_HUOY) {
            ysfssql = "and f.yunsfsb_id = " + SysConstant.YUNSFS_HUOY + "\n";
        } else if (v.getInt1() == RPTTYPE_TZ_QIY) {
            ysfssql = "and f.yunsfsb_id = " + SysConstant.YUNSFS_QIY + "\n";
        }
        return ysfssql;
    }

    // ȡ�õ糧����SQL
    private String getDcParam() {
        // �糧sql
        String dcsql = "";
        if (isParentDc(getTreeid_dc())) {
            dcsql = "and d.fuid = " + getTreeid_dc() + "\n";
        } else {
            dcsql = "and f.diancxxb_id = " + getTreeid_dc() + "\n";
        }
        return dcsql;
    }

    // ��ȡ������
    public String getRptTitle() {
        Visit visit = (Visit) this.getPage().getVisit();
        String sb;
        switch (visit.getInt1()) {
            case RPTTYPE_TZ_ZERO:
            case RPTTYPE_TZ_ALL:
                sb = Locale.RptTitle_Jiltz_All;
                break;
            case RPTTYPE_TZ_HUOY:
                sb = Locale.RptTitle_Jiltz_Huoy;
                break;
            case RPTTYPE_TZ_QIY:
                sb = Locale.RptTitle_Jiltz_Qiy;
                break;
            default:
                sb = Locale.RptTitle_Jiltz_All;
                break;
        }
        return sb;
    }

    // ˢ�ºⵥ�б�
    public void initToolbar() {
        Visit visit = (Visit) this.getPage().getVisit();
        Toolbar tb1 = new Toolbar("tbdiv");
        tb1.addText(new ToolbarText("�������:"));
        DateField dfb = new DateField();
        dfb.setValue(getBRiq());
        dfb.Binding("BRIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
        dfb.setId("guohrqb");
        tb1.addField(dfb);
        tb1.addText(new ToolbarText("-"));
        tb1.addText(new ToolbarText(" �� "));
        DateField dfe = new DateField();
        dfe.setValue(getERiq());
        dfe.Binding("ERIQ", "");// ��htmlҳ�е�id��,���Զ�ˢ��
        dfe.setId("guohrqe");
        tb1.addField(dfe);
        tb1.addText(new ToolbarText("-"));
        // �糧��
        DefaultTree dt1 = new DefaultTree(DefaultTree.tree_dianc_win,
                "diancTree", "" + visit.getDiancxxb_id(), "", null,
                getTreeid_dc());
        setTree_dc(dt1);
        TextField tf1 = new TextField();
        tf1.setId("diancTree_text");
        tf1.setWidth(80);
        tf1.setValue(getDcMingc(getTreeid_dc()));

        ToolbarButton tb3 = new ToolbarButton(null, null,
                "function(){diancTree_window.show();}");
        tb3.setIcon("ext/resources/images/list-items.gif");
        tb3.setCls("x-btn-icon");
        tb3.setMinWidth(20);

        tb1.addText(new ToolbarText("�糧:"));
        tb1.addField(tf1);
        tb1.addItem(tb3);
        tb1.addText(new ToolbarText("-"));
//
//        // ��Ӧ����
        DefaultTree dt = new DefaultTree(DefaultTree.tree_gys_mk_cz, "gongysTree"
                , "" + visit.getDiancxxb_id(), "forms[0]", getTreeid(), getTreeid());
        visit.setDefaultTree(dt);
        TextField tf = new TextField();
        tf.setId("gongysTree_text");
        tf.setWidth(90);
        tf.setValue(getGys(getTreeid())[0]);
        ToolbarButton tb2 = new ToolbarButton(null, null, "function(){gongysTree_window.show();}");
        tb2.setIcon("ext/resources/images/list-items.gif");
        tb2.setCls("x-btn-icon");
        tb2.setMinWidth(20);

        // �ƻ��ھ�
//        tb1.addText(new ToolbarText("�ƻ��ھ�:"));
//        ComboBox jihkj = new ComboBox();
//        jihkj.setTransform("JihkjSelect");
//        jihkj.setWidth(70);
//        jihkj.setListeners("select:function(own,rec,index){Ext.getDom('JihkjSelect').selectedIndex=index}");
//        tb1.addField(jihkj);
//        tb1.addText(new ToolbarText("-"));
//
//        tb1.addText(new ToolbarText("������λ:"));
//        tb1.addField(tf);
//        tb1.addItem(tb2);
//        tb1.addText(new ToolbarText("-"));

//         Ʒ��ѡ��
//        tb1.addText(new ToolbarText("Ʒ��:"));
//        ComboBox pinz = new ComboBox();
//        pinz.setTransform("PinzSelect");
//        pinz.setWidth(50);
//        pinz.setListeners("select:function(own,rec,index){Ext.getDom('PinzSelect').selectedIndex=index}");
//        tb1.addField(pinz);
//        tb1.addText(new ToolbarText("-"));

        // ��������ѡ��
//        tb1.addText(new ToolbarText("����:"));
//        ComboBox leix = new ComboBox();
//        leix.setTransform("LeixSelect");
//        leix.setWidth(80);
//        leix.setListeners("select:function(own,rec,index){Ext.getDom('LeixSelect').selectedIndex=index}");
//        tb1.addField(leix);
//        tb1.addText(new ToolbarText("-"));

        ToolbarButton rbtn = new ToolbarButton(null, "��ѯ", "function() {document.getElementById('RefurbishButton').click();}");
        rbtn.setIcon(SysConstant.Btn_Icon_Search);
        tb1.addItem(rbtn);
        tb1.addText(new ToolbarText("-"));

        // ���״̬ѡ��
//        tb1.addText(new ToolbarText("�Ƿ����:"));
//        ComboBox shenh = new ComboBox();
//        shenh.setTransform("ShenhSelect");
//        shenh.setWidth(80);
//        shenh.setListeners("select:function(own,rec,index){Ext.getDom('ShenhSelect').selectedIndex=index}");
//        tb1.addField(shenh);

        tb1.setWidth("bodyWidth");
        setToolbar(tb1);
    }

    private String getGys_Mkxj_Pz() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        //daohrq  or   fahrq
        String strTongjRq = getTongjRq(con);
        String sq = "";
        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql =
                "select gys, jihkj, mk, daohrq, pz, fz, ches, laimsl,jingz,biaoz, yuns,yingk, zongkd\n" +
                        "  from (" +
                        "select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" +
//			"j.mingc jihkj,\n"+
                        "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                        "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', m.mingc) mk,\n" +
                        "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f." + strTongjRq + "), 2,\n" +
                        "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', to_char(f." + strTongjRq + ",'yyyy-mm-dd'))) daohrq,\n" +
                        "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f." + strTongjRq + "), 2,\n" +
                        "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', p.mingc)) pz,\n" +
                        "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f." + strTongjRq + "), 2,\n" +
                        "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', c.mingc)) fz, sum(f.ches) ches,\n" +
                        "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                        "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz," + yuns_js + " " +
                        "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                        "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c ,jihkjb j\n" +
                        "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                        "and f.pinzb_id = p.id and f.faz_id = c.id\n" +
                        getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                        "group by rollup(g.mingc,m.mingc,f." + strTongjRq + ",p.mingc,c.mingc,j.mingc)\n" +
                        "having ((grouping(f." + strTongjRq + ") = 1 or grouping(c.mingc) = 0))\n" + //and and grouping(j.mingc) = 1
                        "order by grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                        "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                        "grouping(f." + strTongjRq + ")," + strTongjRq + ", max(p.xuh), p.mingc, max(c.xuh), c.mingc"
                        + "         ) dd where nvl(jihkj,0) <> '0'";
        Report rt = new Report();
//		System.out.println(sql);
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_gys_mkxj_pz + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_gys_mkxj_pz + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.gongysb_id_fahb, Locale.jihkjb_id_fahb, Locale.meikxxb_id_fahb, Locale.daohrq_id_fahb,
                    Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 80, 90, 90, 70, 70, 50, 50, 50, 50, 50, 50, 50};

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 4, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(5, 4, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 5, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 6));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 2, i, 6);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 6);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_gys_mkxj_pz + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getGys_Mkxj_zero() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + "0" + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + "0" + ")) + sum(round_new(f.biaoz," + "0" + ")) " +
                    "- sum(round_new(f.jingz," + "0" + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql = "select * \n" +
                "  from (" +
                "select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', m.mingc) mk,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f.daohrq), 2,\n" +
                "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', to_char(f.daohrq,'yyyy-mm-dd'))) daohrq,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f.daohrq), 2,\n" +
                "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', p.mingc)) pz,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc) + grouping(f.daohrq), 2,\n" +
                "g.mingc || '�ϼ�', 1, m.mingc || '�ϼ�', c.mingc)) fz, sum(f.ches) ches,\n" +
                "sum(round_new(f.laimsl,0)) laimsl, sum(round_new(f.jingz,0)) jingz,\n" +
                "sum(round_new(f.biaoz,0)) biaoz, " + yuns_js + " " +
                "sum(round_new(f.yingk,0)) yingk, sum(round_new(f.zongkd,0)) zongkd\n" +
                "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c ,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.faz_id = c.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(g.mingc,m.mingc,f.daohrq,p.mingc,c.mingc,j.mingc)\n" +
                "having (grouping(f.daohrq) = 1 or grouping(c.mingc) = 0) \n" +
                "order by grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "grouping(f.daohrq), daohrq, max(p.xuh), p.mingc, max(c.xuh), c.mingc"
                + ") dd where nvl(jihkj,0) <> '0'";


        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_gys_mkxj_pz + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_gys_mkxj_pz + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.gongysb_id_fahb, Locale.jihkjb_id_fahb, Locale.meikxxb_id_fahb, Locale.daohrq_id_fahb,
                    Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 80, 90, 90, 70, 70, 50, 50, 50, 50, 50, 50, 50};

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 2));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_RIGHT);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 2, i, 5);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 5);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�", Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_gys_mkxj_pz + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getDc_Gys_Mkxj_Pz() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";
        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql = "select * \n" +
                "  from (" +
                "select decode(grouping(d.mingc),1,'�ܼ�',d.mingc) dc,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj,\n" +
                "decode(grouping(m.mingc) + grouping(g.mingc),2,'�ܼ�',1,g.mingc||'�ϼ�',g.mingc) gys,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',m.mingc))) mk,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq),1,m.mingc||'�ϼ�',to_char(f.daohrq,'yyyy-mm-dd'))))) dh,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq),1,m.mingc||'�ϼ�',p.mingc))))pz,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq),1,m.mingc||'�ϼ�', c.mingc))))fz, sum(f.ches) ches,\n" +
                "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz," + yuns_js + " " +
                " sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +

                "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, diancxxb d ,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.faz_id = c.id and f.diancxxb_id = d.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(d.mingc,g.mingc,m.mingc,f.daohrq,p.mingc,c.mingc,j.mingc)\n" +
                "having NOT (GROUPING(j.mingc)=1 AND GROUPING(f.daohrq)=0) \n" +
                "order by grouping(d.mingc), max(d.xuh), d.mingc,\n" +
                "grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "grouping(f.daohrq), daohrq, max(p.xuh), p.mingc, max(c.xuh), c.mingc"
                + ") dd ";

        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_dc_gys_mkxj_pz + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_dc_gys_mkxj_pz + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.diancxxb_id_fahb, Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.daohrq_id_fahb,
                    Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 80, 90, 90, 85, 65, 60, 45, 45, 45, 45, 45, 45, 45};
//    		ArrWidth = new int[] {100, 100, 100, 100, 70, 70, 50, 50, 50, 50, 50, 50, 50 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 4));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);
        rt.body.setColAlign(13, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 3, i, 6);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 6);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.footer.fontSize=10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_dc_gys_mkxj_pz + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getGys_Mk_Pzxj() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql = "select * \n" +
                "  from (" +
                "select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�', m.mingc)) mk,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(p.mingc), 1, m.mingc || '�ϼ�', p.mingc))) pz,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(p.mingc), 1, m.mingc || '�ϼ�',\n" +
                "decode(grouping(f.daohrq), 1, p.mingc || '�ϼ�',to_char(f.daohrq,'yyyy-mm-dd')))))daohrq,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(p.mingc), 1, m.mingc || '�ϼ�',\n" +
                "decode(grouping(f.daohrq), 1, p.mingc || '�ϼ�',c.mingc)))) fz, sum(f.ches) ches,\n" +
                "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz, " + yuns_js + " " +
                "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c ,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.faz_id = c.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(g.mingc,m.mingc,p.mingc,f.daohrq,c.mingc,j.mingc)\n" +
                "having (grouping(f.daohrq) = 1 or grouping(c.mingc) = 0) \n" +
                "order by grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "grouping(p.mingc), max(p.xuh), p.mingc,\n" +
                "grouping(f.daohrq), daohrq,max(c.xuh), c.mingc"
                + ") dd where nvl(jihkj,0) <> '0'";


        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_gys_mk_pzxj + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_gys_mk_pzxj + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.gongysb_id_fahb, Locale.jihkjb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb,
                    Locale.daohrq_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 80, 90, 70, 90, 70, 50, 50, 50, 50, 50, 50, 50};
//    		ArrWidth = new int[] {100, 100, 70, 100, 70, 50, 50, 50, 50, 50, 50, 50 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 2));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_RIGHT);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 2, i, 5);
        }
        rt.body.merge(rt.body.getRows(), 2, rt.body.getRows(), 5);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_gys_mk_pzxj + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getDc_Gys_Mk_Pzxj() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";
        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql =
                "select *\n" +
                        "  from (" +
                        "select decode(grouping(d.mingc),1,'�ܼ�',d.mingc) dc,\n" +
//			"j.mingc jihkj,\n"+
                        "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                        "decode(grouping(d.mingc) + grouping(g.mingc),2,'�ܼ�',1,d.mingc||'�ϼ�',g.mingc) gys,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',m.mingc))) mk,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(p.mingc),1,m.mingc||'�ϼ�',p.mingc)))) pz,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(p.mingc),1,m.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.daohrq),1,p.mingc||'�ϼ�',\n" +
                        "to_char(f.daohrq,'yyyy-mm-dd')))))) dh,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(p.mingc),1,m.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.daohrq),1,p.mingc||'�ϼ�',\n" +
                        "c.mingc))))) fz, sum(f.ches) ches,\n" +
                        "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                        "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz, " + yuns_js + " " +
                        "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                        "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, diancxxb d ,jihkjb j\n" +
                        "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                        "and f.pinzb_id = p.id and f.faz_id = c.id and f.diancxxb_id = d.id\n" +
                        getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                        "group by rollup(d.mingc,g.mingc,m.mingc,p.mingc,f.daohrq,c.mingc,j.mingc)\n" +
                        "having (grouping(f.daohrq) = 1 or grouping(c.mingc) = 0) \n" +
                        "order by grouping(d.mingc), max(d.xuh), d.mingc,\n" +
                        "grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                        "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                        "grouping(p.mingc), max(p.xuh), p.mingc,\n" +
                        "grouping(f.daohrq), daohrq, max(c.xuh), c.mingc"
                        + ") dd where nvl(jihkj,0) <> '0'";

        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_dc_gys_mk_pzxj + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_dc_gys_mk_pzxj + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.diancxxb_id_fahb, Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb,
                    Locale.pinzb_id_fahb, Locale.daohrq_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{85, 80, 80, 80, 80, 65, 60, 45, 45, 45, 45, 45, 45, 45};
//    		ArrWidth = new int[] {100, 100, 100, 70, 100, 70, 50, 50, 50, 50, 50, 50, 50 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

//		rt.title.fontSize=10;
//		rt.title.setRowHeight(2, 50);
//		rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
//		rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(5, 3, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 3));

//		rt.body.setColAlign(0, Table.ALIGN_CENTER);
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);
        rt.body.setColAlign(13, Table.ALIGN_RIGHT);
        rt.body.setColAlign(14, Table.ALIGN_RIGHT);


        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 3, i, 6);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 6);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_dc_gys_mk_pzxj + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getGys_Mk_Pz_Ccxj() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql = "select *\n" +
                "  from (" +
                "select decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�', m.mingc)) mk,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.chec), 1, m.mingc||'�ϼ�', f.chec))) chec,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.chec), 1, m.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq), 1, f.chec||'�ϼ�', to_char(daohrq,'yyyy-mm-dd'))))) daohrq,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.chec), 1, m.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq), 1, f.chec||'�ϼ�', p.mingc)))) pz,\n" +
                "decode(grouping(g.mingc), 1, '�ܼ�', decode(grouping(m.mingc), 1, g.mingc||'�ϼ�',\n" +
                "decode(grouping(f.chec), 1, m.mingc||'�ϼ�',\n" +
                "decode(grouping(f.daohrq), 1, f.chec||'�ϼ�', c.mingc)))) fz, sum(f.ches) ches,\n" +
                "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz," + yuns_js + " sum(round_new(f.yingk," + v.getShuldec() + ")) yingk,\n" +
                " sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.faz_id = c.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(g.mingc,m.mingc,f.chec,f.daohrq,p.mingc,c.mingc,j.mingc)\n" +
                "having (grouping(f.daohrq) = 1 or grouping(c.mingc) = 0) \n" +
                "order by grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "grouping(f.chec), f.chec, grouping(f.daohrq), daohrq,\n" +
                "max(p.xuh), p.mingc, max(c.xuh), c.mingc"
                + ") dd where nvl(jihkj,0) <> '0'";


        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_gys_mk_pz_ccxj + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_gys_mk_pz_ccxj + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.gongysb_id_fahb, Locale.jihkjb_id_fahb, Locale.meikxxb_id_fahb, Locale.chec_fahb,
                    Locale.daohrq_id_fahb, Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb,
                    Locale.laimsl_fahb, Locale.jingz_fahb, Locale.biaoz_fahb, Locale.yuns_fahb,
                    Locale.yingk_fahb, Locale.zongkd_fahb}};

//    		ArrWidth = new int[] {90, 90, 65, 65, 65, 65,50, 48, 48, 48, 48, 48, 50 };
            ArrWidth = new int[]{90, 80, 90, 70, 70, 70, 70, 50, 50, 50, 50, 50, 50, 50};

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 2));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);
        rt.body.setColAlign(13, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 2, i, 6);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 6);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_gys_mk_pz_ccxj + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getDc_Gys_Mk_Pz_Ccxj() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql =
                "select *\n" +
                        "  from (" +

                        "select decode(grouping(d.mingc),1,'�ܼ�',d.mingc) dc,\n" +
//			"j.mingc jihkj,\n"+
                        "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                        "decode(grouping(d.mingc) + grouping(g.mingc),2,'�ܼ�',1,d.mingc||'�ϼ�',g.mingc) gys,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',m.mingc))) mk,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.chec),1,m.mingc||'�ϼ�',f.chec)))) chec,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.chec),1,m.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.daohrq),1,f.chec||'�ϼ�',to_char(f.daohrq,'yyyy-mm-dd')))))) daohrq,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.chec),1,m.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.daohrq),1,f.chec||'�ϼ�',p.mingc))))) pz,\n" +
                        "decode(grouping(d.mingc),1,'�ܼ�',decode(grouping(g.mingc),1, d.mingc||'�ϼ�',\n" +
                        "decode(grouping(m.mingc),1,g.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.chec),1,m.mingc||'�ϼ�',\n" +
                        "decode(grouping(f.daohrq),1,f.chec||'�ϼ�',c.mingc))))) fz, sum(f.ches) ches,\n" +
                        "sum(round_new(f.laimsl," + v.getShuldec() + ")) laimsl, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                        "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz, " + yuns_js + " " +
                        "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                        "from fahb f, gongysb g, meikxxb m, pinzb p, chezxxb c, diancxxb d ,jihkjb j\n" +
                        "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                        "and f.pinzb_id = p.id and f.faz_id = c.id and f.diancxxb_id = d.id\n" +
                        getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                        "group by rollup(d.mingc,g.mingc,m.mingc,f.chec,f.daohrq,p.mingc,c.mingc,j.mingc)\n" +
                        "having (grouping(f.daohrq) = 1 or grouping(c.mingc) = 0)\n" +
                        "order by grouping(d.mingc), max(d.xuh), d.mingc,\n" +
                        "grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                        "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                        "f.chec, grouping(f.daohrq), daohrq,\n" +
                        "max(p.xuh), p.mingc,\n" +
                        "max(c.xuh), c.mingc"
                        + ") dd where nvl(jihkj, 0) <> '0'";


        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_dc_gys_mk_pz_ccxj + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_dc_gys_mk_pz_ccxj + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.diancxxb_id_fahb, Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.chec_fahb,
                    Locale.daohrq_id_fahb, Locale.pinzb_id_fahb, Locale.faz_id_fahb, Locale.ches_fahb, Locale.laimsl_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 80, 90, 90, 70, 70, 70, 70, 50, 50, 50, 50, 50, 50, 50};
//        	ArrWidth = new int[] {85, 85, 85, 60, 60, 60, 60, 40, 40, 40, 40, 40, 40, 45 };

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 3));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_CENTER);
        rt.body.setColAlign(6, Table.ALIGN_CENTER);
        rt.body.setColAlign(7, Table.ALIGN_CENTER);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);
        rt.body.setColAlign(13, Table.ALIGN_RIGHT);
        rt.body.setColAlign(14, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();
        for (int i = 2; i < rt.body.getRows(); i++) {
            rt.body.merge(i, 3, i, 7);
        }
        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 7);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_dc_gys_mk_pz_ccxj + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getGys_Mk_Pzhz() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }
        String sql = "select *\n" +
                "  from (" +
                "select \n" +
                "decode(grouping(g.mingc),1,'�ܼ�',g.mingc) gys,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                "decode(grouping(g.mingc),1,'�ܼ�',m.mingc) mk,\n" +
                "decode(grouping(g.mingc),1,'�ܼ�',p.mingc) pz,\n" +
                " sum(f.ches) ches,\n" +
                "sum(round_new(f.maoz," + v.getShuldec() + ")) maoz," +
                "sum(round_new(f.piz," + v.getShuldec() + ")) piz, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz, " + yuns_js + " " +
                "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                "from fahb f, gongysb g, meikxxb m, pinzb p,diancxxb d,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.diancxxb_id = d.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(g.mingc,m.mingc,p.mingc,j.mingc)\n" +
                "having (grouping(g.mingc) = 1 or grouping(p.mingc) = 0)\n" +
                "order by \n" +
                "grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "min(f.chec), max(p.xuh), p.mingc"
                + ") dd where nvl(jihkj, 0) <> '0'";


        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;
        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_gys_mk_pzhz + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_gys_mk_pzhz + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.gongysb_id_fahb, Locale.jihkjb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb,
                    Locale.ches_fahb, Locale.maoz_fahb, Locale.piz_fahb, Locale.jingz_fahb, Locale.biaoz_fahb,
                    Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{80, 60, 80, 60, 60, 60, 60, 60, 60, 60, 60, 60};
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_CENTER);
        rt.setDefaultTitle(9, 3, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 2));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_RIGHT);
        rt.body.setColAlign(5, Table.ALIGN_RIGHT);
        rt.body.setColAlign(6, Table.ALIGN_RIGHT);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(Report.PAPER_ROWS);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();

        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 6);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_gys_mk_pzhz + v.getInt1());
        return rt.getAllPagesHtml();
    }

    private String getDc_Gys_Mk_Pzhz() {
        Visit v = (Visit) getPage().getVisit();
        JDBCcon con = new JDBCcon();
        String sq = "";

        String xhjs_str = " select * from xitxxb where mingc='��������ӯ�������໥����' and zhi='��' and leib='����' and zhuangt=1 ";//ӯ�������Ƿ�ͨ���໥����õ�
        ResultSetList rsl_xh = con.getResultSetList(xhjs_str);
        String yuns_js = " sum(round_new(f.yuns," + v.getShuldec() + ")) yuns,\n";// ���� ������ʽ
        if (rsl_xh.next()) {
            yuns_js = " sum(round_new(f.yingk," + v.getShuldec() + ")) + sum(round_new(f.biaoz," + v.getShuldec() + ")) " +
                    "- sum(round_new(f.jingz," + v.getShuldec() + ")) yuns,\n ";// ���� ������ʽ
        }
        rsl_xh.close();
        if (!getJihkjValue().getValue().equals("ȫ��")) {

            sq = " and j.mingc='" + getJihkjValue().getValue() + "'";
        }

        String sql = "select *\n" +
                "  from (" +
                "select decode(grouping(d.mingc),1,'�ܼ�',d.mingc) dc,\n" +
//			"j.mingc jihkj,\n"+
                "decode(grouping(m.mingc) + grouping(g.mingc), 2, '�ܼ�', 1, g.mingc||'�ϼ�', j.mingc) jihkj," +
                "decode(grouping(d.mingc),1,'�ܼ�',g.mingc) gys,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',m.mingc) mk,\n" +
                "decode(grouping(d.mingc),1,'�ܼ�',p.mingc) pz,\n" +
                " sum(f.ches) ches,\n" +
                "sum(round_new(f.maoz," + v.getShuldec() + ")) maoz," +
                "sum(round_new(f.piz," + v.getShuldec() + ")) piz, sum(round_new(f.jingz," + v.getShuldec() + ")) jingz,\n" +
                "sum(round_new(f.biaoz," + v.getShuldec() + ")) biaoz, " + yuns_js + " " +
                "sum(round_new(f.yingk," + v.getShuldec() + ")) yingk, sum(round_new(f.zongkd," + v.getShuldec() + ")) zongkd\n" +
                "from fahb f, gongysb g, meikxxb m, pinzb p,diancxxb d ,jihkjb j\n" +
                "where f.gongysb_id = g.id and f.meikxxb_id = m.id " + getJihkjTj() + sq + "\n" +
                "and f.pinzb_id = p.id and f.diancxxb_id = d.id\n" +
                getDateParam() + getGysParam() + getPinzParam() + getYsfsParam() + getDcParam() + getShenhztParam() +
                "group by rollup(d.mingc,g.mingc,m.mingc,p.mingc,j.mingc)\n" +
                "having (grouping(d.mingc) = 1 or grouping(p.mingc) = 0) \n" +
                "order by grouping(d.mingc), max(d.xuh), d.mingc,\n" +
                "grouping(g.mingc), max(g.xuh), g.mingc,\n" +
                "grouping(m.mingc), max(m.xuh), m.mingc,\n" +
                "min(f.chec), max(p.xuh), p.mingc"
                + ") dd where nvl(jihkj,0) <> '0'";

        Report rt = new Report();
        ResultSetList rstmp = con.getResultSetList(sql);
        ResultSetList rs = null;
        String[][] ArrHeader = null;
        String[] strFormat = null;
        int[] ArrWidth = null;

        int aw = 0;
        String[] Zidm = null;
        StringBuffer sb = new StringBuffer();
        sb.append("select bp.zidm,bp.biaot,bp.xuh,bp.kuand,bp.format from baobpzzb bp,baobpzb b where bp.baobpzb_id=b.id and bp.shifxs=1 and b.guanjz='" + (RptKey_dc_gys_mk_pzhz + v.getInt1()) + "' order by xuh");
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
                strFormat[rsl.getRow()] = rsl.getString("format") == null ? "" : rsl.getString("format");
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
            rsl = con.getResultSetList("select biaot from baobpzb where guanjz='" + (RptKey_dc_gys_mk_pzhz + v.getInt1()) + "'");
            String Htitle = getRptTitle();
            while (rsl.next()) {
                Htitle = rsl.getString("biaot");
            }
            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(Htitle, ArrWidth);
            rsl.close();
        } else {
            rs = rstmp;
            ArrHeader = new String[][]{{Locale.diancxxb_id_fahb, Locale.jihkjb_id_fahb, Locale.gongysb_id_fahb, Locale.meikxxb_id_fahb, Locale.pinzb_id_fahb,
                    Locale.ches_fahb, Locale.maoz_fahb, Locale.piz_fahb, Locale.jingz_fahb,
                    Locale.biaoz_fahb, Locale.yuns_fahb, Locale.yingk_fahb, Locale.zongkd_fahb}};

            ArrWidth = new int[]{90, 60, 80, 90, 90, 60, 60, 60, 60, 60, 60, 60, 60, 60};

            aw = rt.paperStyle(((Visit) this.getPage().getVisit()).getDiancxxb_id(), ((Visit) this.getPage().getVisit()).getString1());//ȡ�ñ���ֽ������
            rt.getArrWidth(ArrWidth, aw);//��ӱ����A4����
            rt.setTitle(getRptTitle(), ArrWidth);
        }

        rt.title.fontSize = 10;
        rt.title.setRowHeight(2, 50);
        rt.title.setRowCells(2, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.title.setRowCells(2, Table.PER_FONTSIZE, 19);
        rt.setDefaultTitle(1, 3, "�Ʊ�λ��" + ((Visit) this.getPage().getVisit()).getDiancqc(),
                Table.ALIGN_LEFT);
        rt.setDefaultTitle(4, 5, getBRiq() + " �� " + getERiq(),
                Table.ALIGN_CENTER);
        rt.setDefaultTitle(9, 4, "��λ���֡���", Table.ALIGN_RIGHT);

        rt.setBody(new Table(rs, 1, 0, 3));
        //rt.body.setColAlign(0, Table.ALIGN_CENTER);r
        rt.body.setColAlign(1, Table.ALIGN_CENTER);
        rt.body.setColAlign(2, Table.ALIGN_CENTER);
        rt.body.setColAlign(3, Table.ALIGN_CENTER);
        rt.body.setColAlign(4, Table.ALIGN_CENTER);
        rt.body.setColAlign(5, Table.ALIGN_RIGHT);
        rt.body.setColAlign(6, Table.ALIGN_RIGHT);
        rt.body.setColAlign(7, Table.ALIGN_RIGHT);
        rt.body.setColAlign(8, Table.ALIGN_RIGHT);
        rt.body.setColAlign(9, Table.ALIGN_RIGHT);
        rt.body.setColAlign(10, Table.ALIGN_RIGHT);
        rt.body.setColAlign(11, Table.ALIGN_RIGHT);
        rt.body.setColAlign(12, Table.ALIGN_RIGHT);

        rt.body.setWidth(ArrWidth);
        rt.body.setHeaderData(ArrHeader);
//		rt.body.setColFormat(strFormat);
        rt.body.setPageRows(28);
//		���ӳ��ȵ�����
        rt.body.setPageRows(rt.getPageRows(rt.body.getPageRows(), aw));
        rt.body.mergeFixedCols();
        rt.body.mergeFixedRow();

        rt.body.merge(rt.body.getRows(), 1, rt.body.getRows(), 7);
//		rt.createDefautlFooter(ArrWidth);
        rt.createFooter(1, ArrWidth);
        rt.setDefautlFooter(1, 3, "��ӡ���ڣ�" + DateUtil.Formatdate("yyyy��MM��dd��", new Date()),
                Table.ALIGN_LEFT);
        rt.setDefautlFooter(5, 2, "��ˣ�", Table.ALIGN_LEFT);
        rt.setDefautlFooter(9, 2, "�Ʊ�" + getZhibr(), Table.ALIGN_LEFT);
        rt.footer.setRowCells(1, Table.PER_VALIGN, Table.VALIGN_TOP);
        rt.footer.fontSize = 10;
//		rt.footer.setRowHeight(1, 1);
        con.Close();
        if (rt.body.getPages() > 0) {
            setCurrentPage(1);
            setAllPages(rt.body.getPages());
        }
        rt.body.setRowHeight(21);
        RPTInit.getInsertSql(v.getDiancxxb_id(), sql, rt, getRptTitle(), "" + RptKey_dc_gys_mk_pzhz + v.getInt1());
        return rt.getAllPagesHtml();
    }

    public String getPrintTable() {
//		����Ǹ��׽ڵ�����Ҫ�ڱ��м���糧���鲢��ʾ����
//        if(isParentDc(getTreeid_dc())){
////			�������Ϊ����Ĭ��
//            if(getRptTypeValue()==null){
//                return getDc_Gys_Mkxj_Pz();
//            }else{
//                if(RptType_mkxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getDc_Gys_Mkxj_Pz();
//                }else if(RptType_pzxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getDc_Gys_Mk_Pzxj();
//                }else if(RptType_ccxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getDc_Gys_Mk_Pz_Ccxj();
//                }else if(RptType_pzhz.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getDc_Gys_Mk_Pzhz();
//                }else{
//                    return "δ֪�ı�������,�п������ó�������ϵϵͳ����Ա��";
//                }
//            }
//        }else{
//            if(getRptTypeValue()==null){
//                return getGys_Mkxj_Pz();
//            }else{
//                if(RptType_mkxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getGys_Mkxj_Pz();
//                }else if(RptType_pzxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getGys_Mk_Pzxj();
//                }else if(RptType_ccxj.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getGys_Mk_Pz_Ccxj();
//                }else if(RptType_pzhz.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getGys_Mk_Pzhz();
//                }else if(RptType_zero.equalsIgnoreCase(getRptTypeValue().getValue())){
//                    return getGys_Mkxj_zero();
//                }else {
//                    return "δ֪�ı�������,�п������ó�������ϵϵͳ����Ա��";
//                }
//            }
//        }
        JDBCcon con = new JDBCcon();
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT rownum as xuh,\n" +
                "       cheph,\n" +
                "       maoz,\n" +
                "       piz,\n" +
                "       jingz,\n" +
                "       kuangfl,\n" +
                "       changkc,\n" +
                "       koud,\n" +
                "       jianmsj,\n" +
                "       jianpsj,\n" +
                "       chengydw,\n" +
                "       meikdwmc,\n" +
                "       meikdqmc,\n" +
                "       pinz\n" +
                "  FROM (SELECT q.cheph cheph,\n" +
                "               sum(q.maoz) maoz,\n" +
                "               SUM((q.piz - q.kouz - q.koum)) piz,\n" +
                "               SUM((q.maoz - q.piz)) jingz,\n" +
                "               sum(q.biaoz) kuangfl,\n" +
                "               SUM((q.maoz - q.piz + q.koud)) - sum(q.biaoz) changkc,\n" +
                "               sum(q.koud) koud,\n" +
                "               to_char(q.zhongcsj, 'yyyy-mm-dd hh24:mi:ss') as jianmsj,\n" +
                "               to_char(q.qingcsj, 'yyyy-mm-dd hh24:mi:ss') as jianpsj,\n" +
                "               y.mingc chengydw,\n" +
                "               mx.mingc meikdwmc,\n" +
                "               m.mingc meikdqmc,\n" +
                "               r.mingc pinz\n" +
                "         from chepb q, meikdqb m, meikxxb mx, pinzb r,fahb f,yunsdwb y,gongysb g\n" +
                "         where g.fuid = m.id(+)\n" +
                "           and f.meikxxb_id = mx.id(+)\n" +
                "           and f.pinzb_id = r.id(+)\n" +
                "           and q.fahb_id=f.id(+)\n" +
                "           and q.yunsdwb_id=y.id\n" +
                "           and f.gongysb_id=g.id\n" +
                "           and trunc(q.zhongcsj) BETWEEN date'"+this.getBRiq()+"' and date'"+this.getERiq()+"'\n" +
                "         group BY q.cheph,\n" +
                "                  q.zhongcsj,\n" +
                "                  q.qingcsj,\n" +
                "                  y.mingc,\n" +
                "                  mx.mingc,\n" +
                "                  m.mingc,\n" +
                "                  r.mingc\n" +
                "         /*order by y.mingc, m.mingc, r.mingc, m.mingc*/) order by chengydw,jianmsj ");
        ResultSet rs = con.getResultSet(new StringBuffer(sb.toString()), 1004, 1007);
        Report rt = new Report();
        String[][] ArrHeader = new String[1][14];
        ArrHeader[0] = new String[]{"���", "����", "ë��", "Ƥ��", "����", "����", "�������", "����", "ë��ʱ��", "Ƥ��ʱ��", "���䵥λ", "���", "��λ", "Ʒ��"};
        int[] ArrWidth = new int[]{30, 50, 50, 50, 50, 50, 50, 50, 130, 130, 150, 60, 170, 30};
        rt.setTitle("��   ��   ��   ��   ��   ��   ¼" , ArrWidth);
        rt.title.setRowHeight(2, 40);
        rt.title.setRowCells(2, 2, 14);
        rt.title.setRowCells(2, 7, 1);
        rt.setDefaultTitleLeft("���ڣ�" + this.getBRiq(), 4);
        rt.setDefaultTitleRight("��λ����", 3);
        rt.setBody(new Table(rs, 1, 0, 4));
        rt.body.setWidth(ArrWidth);
        rt.body.setColAlign(4, 1);
        rt.body.setHeaderData(ArrHeader);
        rt.body.ShowZero = true;
        rt.body.setPageRows(40);
        rt.body.mergeFixedCols();
        rt.body.setCells(2, 1, rt.body.getRows(), 3, 6, -1);
        rt.body.setCells(2, 1, rt.body.getRows(), 3, 7, 1);
        rt.body.setCells(2, 2, rt.body.getRows(), 3, 6, 1);
        rt.body.ShowZero = false;
        rt.body.setPageRows(-1);

        try {
            rs.close();
        } catch (SQLException var9) {
            var9.printStackTrace();
        }

        this._CurrentPage = 1;
        this._AllPages = rt.body.getPages();
        rt.body.setRowHeight(21);
        return rt.getAllPagesHtml();
    }

    // ������ʹ�õķ���
    private String treeid;

    public String getTreeid() {
        if (treeid == null || treeid.equals("")) {
            // treeid=String.valueOf(((Visit)
            // this.getPage().getVisit()).getDiancxxb_id());
            treeid = "0";
        }
        return treeid;
    }

    public void setTreeid(String treeid) {
        if (treeid != null) {
            if (!treeid.equals(this.treeid)) {
                ((TextField) getToolbar().getItem("gongysTree_text"))
                        .setValue(getGys(getTreeid())[0]);
                ((Visit) this.getPage().getVisit()).getDefaultTree().getTree()
                        .setSelectedNodeid(treeid);
            }
        }
        this.treeid = treeid;
    }

    public String getTreeScript() {
        return ((Visit) this.getPage().getVisit()).getDefaultTree().getScript();
    }

    //	-------------------------�糧Tree-----------------------------------------------------------------
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

//	-------------------------�糧Tree END-------------------------------------------------------------

    public Toolbar getToolbar() {
        return ((Visit) this.getPage().getVisit()).getToolbar();
    }

    public void setToolbar(Toolbar tb1) {
        ((Visit) this.getPage().getVisit()).setToolbar(tb1);
    }

    public String getToolbarScript() {
        return getToolbar().getRenderScript();
    }
//	ҳ���ʼ��


    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) {
        Visit visit = (Visit) getPage().getVisit();
        String reportType = cycle.getRequestContext().getParameter("lx");
        Calendar stra = Calendar.getInstance();
        stra.set(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()), 1);
        stra.add(Calendar.MONTH, -1);
        if (reportType != null) {
            setBRiq(DateUtil.FormatDate(stra.getTime()));
            setERiq(DateUtil.FormatDate(new Date()));
            visit.setInt1(Integer.parseInt(reportType));
        }
        if (!visit.getActivePageName().toString().equals(
                this.getPageName().toString())) {
            // �ڴ���ӣ���ҳ���һ�μ���ʱ��Ҫ��Ϊ�յı����򷽷�
            visit.setActivePageName(getPageName().toString());
            setBRiq(DateUtil.FormatDate(stra.getTime()));
            setERiq(DateUtil.FormatDate(new Date()));
            if (reportType == null) {
                visit.setInt1(RPTTYPE_TZ_ALL);
            }

            //begin��������г�ʼ������
            visit.setString1(null);

            String pagewith = cycle.getRequestContext().getParameter("pw");// �ж��Ƿ�����������
            if (pagewith != null) {

                visit.setString1(pagewith);
            }
            //	visit.setString1(null);���洫�ݵķ�Ĭ��ֽ�ŵ���ʽ
            setRptTypeValue(null);
            setRptTypeModel(null);
            setPinzValue(null);
            setPinzModel(null);
            setJihkjValue(null);
            setJihkjModel(null);
            setShenhztValue(null);
            setShenhztModel(null);
            setTreeid_dc(visit.getDiancxxb_id() + "");
            setTreeid(null);
            initToolbar();
        }
    }

    //	��ť�ļ����¼�
    private boolean _RefurbishChick = false;

    public void RefurbishButton(IRequestCycle cycle) {
        _RefurbishChick = true;
    }

    //	����ť�ύ����
    public void submit(IRequestCycle cycle) {
        if (_RefurbishChick) {
            _RefurbishChick = false;

        }
        initToolbar();
    }

    //	ҳ���½��֤
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
}